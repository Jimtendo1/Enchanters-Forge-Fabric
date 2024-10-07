package net.jimtendo.enchantersforge.enchantment.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentStateManager;

import java.util.ArrayList;
import java.util.List;

public class SoulboundHelper {
    private static final String SOUL_BOUND_ITEMS_KEY = "SoulBoundItems";

    public static void storeSoulBoundItems(PlayerEntity player, List<ItemStack> items) {
        if (!(player.getWorld() instanceof ServerWorld)) return;
        ServerWorld world = (ServerWorld) player.getWorld();

        PersistentStateManager persistentStateManager = world.getPersistentStateManager();
        SoulboundPersistentState state = persistentStateManager.getOrCreate(
                SoulboundPersistentState.TYPE,
                "soul_binding_data"
        );

        NbtList itemList = new NbtList();
        for (ItemStack stack : items) {
            NbtCompound itemTag = new NbtCompound();
            stack.writeNbt(itemTag);
            itemList.add(itemTag);
        }

        state.putSoulBoundItems(player.getUuidAsString(), itemList);
        state.markDirty();
    }

    public static List<ItemStack> retrieveSoulBoundItems(PlayerEntity player) {
        if (!(player.getWorld() instanceof ServerWorld)) return new ArrayList<>();
        ServerWorld world = (ServerWorld) player.getWorld();

        PersistentStateManager persistentStateManager = world.getPersistentStateManager();
        SoulboundPersistentState state = persistentStateManager.getOrCreate(
                SoulboundPersistentState.TYPE,
                "soul_binding_data"
        );

        List<ItemStack> items = new ArrayList<>();
        String playerUuid = player.getUuidAsString();

        NbtList itemList = state.getSoulBoundItems(playerUuid);
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                NbtCompound itemTag = itemList.getCompound(i);
                items.add(ItemStack.fromNbt(itemTag));
            }
            state.removeSoulBoundItems(playerUuid);
            state.markDirty();
        }

        return items;
    }
}