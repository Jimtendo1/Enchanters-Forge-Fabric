package net.jimtendo.enchantersforge.enchantment.custom;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.item.ItemStack;

import java.util.List;

public class SoulboundEvents {
    public static void register() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            List<ItemStack> soulBoundItems = SoulboundHelper.retrieveSoulBoundItems(newPlayer);
            for (ItemStack stack : soulBoundItems) {
                if (!newPlayer.getInventory().insertStack(stack)) {
                    newPlayer.dropItem(stack, true, false);
                }
            }
        });
    }
}
