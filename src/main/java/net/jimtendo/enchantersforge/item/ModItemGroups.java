package net.jimtendo.enchantersforge.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.jimtendo.enchantersforge.EnchantersForge;
import net.jimtendo.enchantersforge.block.ModBlocks;
import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup ENCHANTERS_FORGE_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(EnchantersForge.MOD_ID, "enchanters_forge"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.enchanters-forge"))
                    .icon(() -> new ItemStack(ModBlocks.CONVERGENCE_TABLE)).entries((displayContext, entries) -> {
                        entries.add(ModBlocks.CONVERGENCE_TABLE);

                        for (Enchantment enchantment : ModEnchantments.MOD_ENCHANTMENTS) {
                            for (int level = 1; level <= enchantment.getMaxLevel(); level++) {
                                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                                EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentLevelEntry(enchantment, level));
                                entries.add(enchantedBook);
                            }
                        }
                    }).build());

    public static void registerItemGroups() {
        EnchantersForge.LOGGER.info("Registering Item Groups for " + EnchantersForge.MOD_ID);
    }
}