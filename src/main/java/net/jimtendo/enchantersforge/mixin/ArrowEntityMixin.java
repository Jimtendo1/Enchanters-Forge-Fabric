package net.jimtendo.enchantersforge.mixin;

import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PersistentProjectileEntity.class)
public class ArrowEntityMixin {
	private int ricochetCount = 0;
	private int maxRicochets = 0;
	private int homingLevel = 0;
	private double initialSpeed = 0;

	@Inject(method = "setOwner", at = @At("RETURN"))
	private void onSetOwner(Entity entity, CallbackInfo ci) {
		if (entity instanceof LivingEntity) {
			PersistentProjectileEntity arrow = (PersistentProjectileEntity) (Object) this;
			LivingEntity livingEntity = (LivingEntity) entity;
			ItemStack bowStack = livingEntity.getMainHandStack();

			if (bowStack.isEmpty()) {
				bowStack = livingEntity.getOffHandStack();
			}

			if (!bowStack.isEmpty()) {
				int ricochetLevel = EnchantmentHelper.getLevel(ModEnchantments.RICOCHET, bowStack);
				maxRicochets = ricochetLevel;

				homingLevel = EnchantmentHelper.getLevel(ModEnchantments.HOMING, bowStack);
				initialSpeed = arrow.getVelocity().length();
			}
		}
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		PersistentProjectileEntity arrow = (PersistentProjectileEntity) (Object) this;
		if (!arrow.getWorld().isClient && !arrow.isOnGround() && homingLevel > 0) {
			applyHoming(arrow);
		}
	}

	private void applyHoming(PersistentProjectileEntity arrow) {
		World world = arrow.getWorld();
		Vec3d arrowPos = arrow.getPos();
		Box searchBox = new Box(arrowPos.add(-3, -3, -3), arrowPos.add(3, 3, 3));

		List<Entity> nearbyEntities = world.getOtherEntities(arrow, searchBox,
				entity -> entity instanceof LivingEntity && !(entity == arrow.getOwner()));

		if (!nearbyEntities.isEmpty()) {
			Entity target = nearbyEntities.get(0);
			for (Entity entity : nearbyEntities) {
				if (entity.squaredDistanceTo(arrow) < target.squaredDistanceTo(arrow)) {
					target = entity;
				}
			}

			Vec3d targetPos = target.getPos().add(0, target.getHeight() / 2, 0);
			Vec3d currentVelocity = arrow.getVelocity();
			double currentSpeed = currentVelocity.length();

			Vec3d directionToTarget = targetPos.subtract(arrowPos).normalize();

			double homingStrength = 0.1 * homingLevel;
			Vec3d newDirection = currentVelocity.normalize().add(directionToTarget.multiply(homingStrength)).normalize();

			double speedDecay = 0.99;
			double newSpeed = Math.max(currentSpeed * speedDecay, currentSpeed * 0.5);

			Vec3d newVelocity = newDirection.multiply(newSpeed);
			arrow.setVelocity(newVelocity);
		}
	}

	@Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
	private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
		return;
	}

	@Inject(method = "onBlockHit", at = @At("HEAD"), cancellable = true)
	private void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {
		PersistentProjectileEntity arrow = (PersistentProjectileEntity) (Object) this;
		World world = arrow.getWorld();

		if (!world.isClient && !arrow.isOnGround() && maxRicochets > 0) {
			if (handleRicochet(arrow, blockHitResult)) {
				ci.cancel();
			}
		}
	}

	private boolean handleRicochet(PersistentProjectileEntity arrow, BlockHitResult hitResult) {
		if (ricochetCount < maxRicochets) {
			Vec3d normal = Vec3d.of(hitResult.getSide().getVector());
			Vec3d velocity = arrow.getVelocity();

			Vec3d reflectedVelocity = velocity.subtract(normal.multiply(2 * velocity.dotProduct(normal)));

			double speedMultiplier = 0.8;
			reflectedVelocity = reflectedVelocity.multiply(speedMultiplier);

			double minVelocity = 0.1;
			if (reflectedVelocity.lengthSquared() < minVelocity * minVelocity) {
				reflectedVelocity = reflectedVelocity.normalize().multiply(minVelocity);
			}

			Vec3d newPos = hitResult.getPos().add(normal.multiply(0.05f));

			arrow.setVelocity(reflectedVelocity);
			arrow.setPos(newPos.x, newPos.y, newPos.z);

			ricochetCount++;
			arrow.velocityDirty = true;

			arrow.setOnGround(false);

			return true;
		}
		return false;
	}
}