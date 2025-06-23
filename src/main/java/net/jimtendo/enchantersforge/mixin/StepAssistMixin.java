package net.jimtendo.enchantersforge.mixin;

import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class StepAssistMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void updateStepHeight(CallbackInfo ci) {
       PlayerEntity player = (PlayerEntity)(Object)this;

       // Get the boots
       ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);

       // Check if boots have the step assist enchantment
       if (!boots.isEmpty() && EnchantmentHelper.getLevel(ModEnchantments.STEP_ASSIST, boots) > 0) {
          // Set step height to 1.0f (full block height)
          player.setStepHeight(1.0f);
       } else {
          // Reset to default player step height when no enchanted boots are equipped
          player.setStepHeight(0.6f);
       }
    }
} 