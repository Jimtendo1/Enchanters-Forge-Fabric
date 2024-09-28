package net.jimtendo.enchantersforge.mixin;

import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.jimtendo.enchantersforge.enchantment.custom.PoseidonsCallEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {

	@Shadow private ItemStack tridentStack;
	@Shadow private boolean dealtDamage;

	protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "onEntityHit", at = @At("TAIL"))
	private void onEntityHit(EntityHitResult entityHitResult, CallbackInfo ci) {
		if (!this.getWorld().isClient()) {
			Entity owner = this.getOwner();
			if (owner instanceof LivingEntity) {
				int reaperLevel = EnchantmentHelper.getLevel(ModEnchantments.POSEIDONS_CALL, this.tridentStack);
				if (reaperLevel > 0) {
					PoseidonsCallEnchantment reaperEnchant = (PoseidonsCallEnchantment) ModEnchantments.POSEIDONS_CALL;
					reaperEnchant.onTridentImpact((TridentEntity)(Object)this, (LivingEntity)owner, this.tridentStack);
				}
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		if (!this.getWorld().isClient() && this.inGround) {
			Entity owner = this.getOwner();
			if (owner instanceof LivingEntity) {
				int reaperLevel = EnchantmentHelper.getLevel(ModEnchantments.POSEIDONS_CALL, this.tridentStack);
				if (reaperLevel > 0) {
					PoseidonsCallEnchantment reaperEnchant = (PoseidonsCallEnchantment) ModEnchantments.POSEIDONS_CALL;
					reaperEnchant.onTridentImpact((TridentEntity)(Object)this, (LivingEntity)owner, this.tridentStack);
				}
			}
		}
	}
}