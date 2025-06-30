package net.jimtendo.enchantersforge.enchantment.custom.aether;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AetherInebriationEnchantment extends Enchantment {
    public AetherInebriationEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        super.onTargetDamaged(user, target, level);
        if (target instanceof LivingEntity livingTarget) {
            StatusEffect inebriation = Registries.STATUS_EFFECT.get(new Identifier("aether", "inebriation"));
            if (inebriation != null) {
                livingTarget.addStatusEffect(new StatusEffectInstance(inebriation, 100, 0)); // 5 seconds, amplifier 0
            }
        }
    }
} 