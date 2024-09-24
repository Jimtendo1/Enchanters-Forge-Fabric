package net.jimtendo.enchantersforge.mixin;

import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.jimtendo.enchantersforge.enchantment.custom.SoulboundHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "dropInventory", at = @At("HEAD"))
    private void onDropInventory(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        List<ItemStack> soulBoundItems = new ArrayList<>();

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (EnchantmentHelper.getLevel(ModEnchantments.SOULBOUND, stack) > 0) {
                soulBoundItems.add(stack.copy());
                player.getInventory().setStack(i, ItemStack.EMPTY);
            }
        }

        if (!soulBoundItems.isEmpty()) {
            SoulboundHelper.storeSoulBoundItems(player, soulBoundItems);
        }
    }
}
