package net.jimtendo.enchantersforge.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.jimtendo.enchantersforge.screen.ConvergenceTableScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class ConvergenceTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private final PropertyDelegate propertyDelegate;
    private int convergenceCost = 0;

    public ConvergenceTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONVERGENCE_TABLE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                if (index == 0) {
                    return convergenceCost;
                }
                return 0;
            }

            @Override
            public void set(int index, int value) {
                if (index == 0) {
                    convergenceCost = value;
                }
            }

            @Override
            public int size() {
                return 1;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.enchanters-forge.convergence_table");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ConvergenceTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate, this);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("ConvergenceCost", convergenceCost);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        convergenceCost = nbt.getInt("ConvergenceCost");
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) return;

        ItemStack input1 = inventory.get(0);
        ItemStack input2 = inventory.get(1);

        if (input1.isEmpty() || input2.isEmpty()) {
            inventory.set(2, ItemStack.EMPTY);
            convergenceCost = 0;
            markDirty();
        } else if (inventory.get(2).isEmpty()) {
            ItemStack result = combineItems(input1, input2);
            if (!result.isEmpty()) {
                inventory.set(2, result);
                markDirty();
            }
        }
    }

    private ItemStack combineItems(ItemStack item1, ItemStack item2) {
        if (!isValidCombination(item1, item2)) {
            return ItemStack.EMPTY;
        }

        ItemStack result = item1.copy();
        Map<Enchantment, Integer> enchantments1 = EnchantmentHelper.get(item1);
        Map<Enchantment, Integer> enchantments2 = EnchantmentHelper.get(item2);

        for (Map.Entry<Enchantment, Integer> entry : enchantments2.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();

            if (enchantments1.containsKey(enchantment)) {
                level = Math.min(enchantment.getMaxLevel(), Math.max(enchantments1.get(enchantment), level) + 1);
            }

            enchantments1.put(enchantment, level);
        }

        EnchantmentHelper.set(enchantments1, result);

        convergenceCost = calculateConvergenceCost(item1, item2, enchantments1);
        return result;
    }

    private int calculateConvergenceCost(ItemStack item1, ItemStack item2, Map<Enchantment, Integer> resultEnchantments) {
        int baseCost = 1;

        for (Map.Entry<Enchantment, Integer> entry : resultEnchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();

            baseCost += level * 2;
        }

        if (hasIncompatibleEnchantments(resultEnchantments)) {
            baseCost *= 2;
        }

        return Math.min(50, Math.max(1, baseCost));
    }

    private boolean hasIncompatibleEnchantments(Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment1 : enchantments.keySet()) {
            for (Enchantment enchantment2 : enchantments.keySet()) {
                if (enchantment1 != enchantment2 && !enchantment1.canCombine(enchantment2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidCombination(ItemStack item1, ItemStack item2) {
        if ((item1.isOf(Items.ENCHANTED_BOOK) && item2.isOf(Items.ENCHANTED_BOOK)) ||
                (!item1.isOf(Items.ENCHANTED_BOOK) && item2.isOf(Items.ENCHANTED_BOOK))) {
            return true;
        }

        return item1.getItem() == item2.getItem();
    }


    public void returnItemsToPlayer(PlayerEntity player) {
        for (int i = 0; i < 2; i++) {
            ItemStack stack = inventory.get(i);
            if (!stack.isEmpty()) {
                player.getInventory().offerOrDrop(stack);
                inventory.set(i, ItemStack.EMPTY);
            }
        }
        markDirty();
    }
}