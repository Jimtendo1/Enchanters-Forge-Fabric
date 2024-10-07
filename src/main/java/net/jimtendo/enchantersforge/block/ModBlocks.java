package net.jimtendo.enchantersforge.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.jimtendo.enchantersforge.EnchantersForge;
import net.jimtendo.enchantersforge.block.custom.ConvergenceTableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block CONVERGENCE_TABLE = registerBlock("convergence_table",
            new ConvergenceTableBlock(FabricBlockSettings.copyOf(Blocks.ENCHANTING_TABLE).nonOpaque().sounds(BlockSoundGroup.AMETHYST_BLOCK)));


    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(EnchantersForge.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(EnchantersForge.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        EnchantersForge.LOGGER.info("Registering ModBlocks for " + EnchantersForge.MOD_ID);
    }
}