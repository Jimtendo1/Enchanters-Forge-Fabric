package net.jimtendo.enchantersforge.mixin;

import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.jimtendo.enchantersforge.enchantment.custom.PoseidonsCallEnchantment;
import net.jimtendo.enchantersforge.enchantment.custom.VitalityBoostEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Unique
	private static final UUID VITALITY_BOOST_UUID = UUID.fromString("5D6F0BA2-1186-46AC-B896-C61C5CEE99CC");

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		World world = entity.getWorld();

		if (entity instanceof PlayerEntity && !world.isClient()) {
			updateVitalityBoost(entity);
		}
	}

	@Unique
	private void updateVitalityBoost(LivingEntity entity) {
		float totalBoost = 0f;
		VitalityBoostEnchantment vitalityEnchantment = (VitalityBoostEnchantment) ModEnchantments.VITALITY_BOOST;

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.ARMOR) {
				ItemStack stack = entity.getEquippedStack(slot);
				int level = EnchantmentHelper.getLevel(vitalityEnchantment, stack);
				if (level > 0) {
					totalBoost += vitalityEnchantment.getHealthBoost(slot, level);
				}
			}
		}

		EntityAttributeInstance healthAttribute = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
		if (healthAttribute != null) {
			// Remove the existing modifier if it exists
			healthAttribute.removeModifier(VITALITY_BOOST_UUID);

			if (totalBoost > 0) {
				// Add the new modifier
				EntityAttributeModifier modifier = new EntityAttributeModifier(
						VITALITY_BOOST_UUID,
						"Vitality boost",
						totalBoost,
						EntityAttributeModifier.Operation.ADDITION
				);
				healthAttribute.addPersistentModifier(modifier);
			}

			// Heal the entity if their health is above their new max health
			if (entity.getHealth() > entity.getMaxHealth()) {
				entity.setHealth(entity.getMaxHealth());
			}
		}
	}
}