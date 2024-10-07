package net.jimtendo.enchantersforge.screen;

import net.jimtendo.enchantersforge.block.entity.ConvergenceTableBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class ConvergenceTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final ConvergenceTableBlockEntity blockEntity;

    public ConvergenceTableScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(1), null);
    }

    public ConvergenceTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate, ConvergenceTableBlockEntity blockEntity) {
        super(ModScreenHandlers.CONVERGENCE_TABLE_SCREEN_HANDLER, syncId);
        checkSize(inventory, 3);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.blockEntity = blockEntity;
        inventory.onOpen(playerInventory.player);

        this.addSlot(new ConvergenceTableSlot(inventory, 0, 27, 47));
        this.addSlot(new ConvergenceTableSlot(inventory, 1, 76, 47));
        this.addSlot(new Slot(inventory, 2, 134, 47) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return playerEntity.experienceLevel >= getConvergenceCost() || playerEntity.getAbilities().creativeMode;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.getAbilities().creativeMode) {
                    player.addExperienceLevels(-getConvergenceCost());
                }
                inventory.setStack(0, ItemStack.EMPTY);
                inventory.setStack(1, ItemStack.EMPTY);
                super.onTakeItem(player, stack);
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);
    }

    private class ConvergenceTableSlot extends Slot {
        public ConvergenceTableSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public void setStack(ItemStack stack) {
            super.setStack(stack);
            updateResult();
        }
    }

    private void updateResult() {
        if (blockEntity != null) {
            blockEntity.tick(blockEntity.getWorld(), blockEntity.getPos(), blockEntity.getCachedState());
        }
    }
    public boolean hasInputs() {
        return !inventory.getStack(0).isEmpty() || !inventory.getStack(1).isEmpty();
    }

    public boolean isValidRecipe() {
        if (!hasInputs()) {
            return true;
        }

        ItemStack output = inventory.getStack(2);

        return !output.isEmpty() && getConvergenceCost() > 0;
    }

    public int getConvergenceCost() {
        return this.propertyDelegate.get(0);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (blockEntity != null) {
            blockEntity.returnItemsToPlayer(player);
        }
    }
}