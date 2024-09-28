package net.jimtendo.enchantersforge.enchantment.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class VitalityBoostEnchantment extends Enchantment {
    public VitalityBoostEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[]{
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
        });
    }

    @Override
    public int getMinPower(int level) {
        return 10 + 20 * (level - 1);
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public float getHealthBoost(EquipmentSlot slot, int level) {
        float baseBoost = level * 2.0f; // 2 health points (1 heart) per level
        float multiplier = switch (slot) {
            case HEAD -> 0.75f;
            case CHEST -> 1.25f;
            case LEGS -> 1.0f;
            case FEET -> 0.75f;
            default -> 0f;
        };
        return baseBoost * multiplier;
    }
}