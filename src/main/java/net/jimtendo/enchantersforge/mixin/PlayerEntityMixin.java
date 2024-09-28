package net.jimtendo.enchantersforge.mixin;

import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.jimtendo.enchantersforge.enchantment.custom.SoulboundHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
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
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        World world = player.getWorld();

        if (!world.isClient && player.isInsideWaterOrBubbleColumn() && player.age % 20 == 0) { // Check every second
            ItemStack mainHandStack = player.getMainHandStack();
            ItemStack offHandStack = player.getOffHandStack();

            int enchantmentLevel = Math.max(
                    getWatersEmbraceLevel(mainHandStack),
                    getWatersEmbraceLevel(offHandStack)
            );

            if (enchantmentLevel > 0) {
                float healAmount = enchantmentLevel * 0.5f; // 0.5 hearts per level
                player.heal(healAmount);
            }
        }
    }

    private int getWatersEmbraceLevel(ItemStack stack) {
        if (stack.getItem() instanceof TridentItem) {
            return EnchantmentHelper.getLevel(ModEnchantments.WATERS_EMBRACE, stack);
        }
        return 0;
    }
}

