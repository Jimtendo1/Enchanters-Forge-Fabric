package net.jimtendo.enchantersforge.enchantment.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

public class IncinerateEnchantment extends Enchantment {
    public IncinerateEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.ARMOR, new EquipmentSlot[] {
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

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (attacker != null) {
            attacker.setOnFireFor(level * 4); // Set attacker on fire for 4 seconds per level
        }
    }
    @Override
    public boolean canAccept(Enchantment other) {
        if (other instanceof ThornsEnchantment || other instanceof FrostbiteEnchantment || other instanceof IntoxicateEnchantment) {
            return false;
        }
        return super.canAccept(other);
    }
}