package net.jimtendo.enchantersforge.enchantment.custom;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.PersistentState;

public class SoulboundPersistentState extends PersistentState {
    private final NbtCompound data;

    public SoulboundPersistentState() {
        this.data = new NbtCompound();
    }

    public SoulboundPersistentState(NbtCompound nbt) {
        this.data = nbt;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.copyFrom(this.data);
        return nbt;
    }

    public void putSoulBoundItems(String playerUuid, NbtList items) {
        this.data.put(playerUuid, items);
    }

    public NbtList getSoulBoundItems(String playerUuid) {
        return this.data.getList(playerUuid, 10); // 10 is the NBT type for Compound
    }

    public void removeSoulBoundItems(String playerUuid) {
        this.data.remove(playerUuid);
    }

    public static SoulboundPersistentState createFromNbt(NbtCompound tag) {
        return new SoulboundPersistentState(tag);
    }
}
