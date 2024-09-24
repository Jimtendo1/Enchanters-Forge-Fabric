package net.jimtendo.enchantersforge.enchantment.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class FrostAspectEnchantment extends Enchantment {
    public FrostAspectEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
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
        return 2;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (target instanceof LivingEntity) {
            ((LivingEntity) target).setInPowderSnow(true);
            ((LivingEntity) target).setFrozenTicks(70 * 4 * level);
        }

        super.onTargetDamaged(user, target, level);
    }

    @Override
    public boolean canAccept(Enchantment other) {
        if (other instanceof FireAspectEnchantment || other instanceof PoisonAspectEnchantment || other instanceof WitherAspectEnchantment) {
            return false;
        }
        return super.canAccept(other);
    }
}