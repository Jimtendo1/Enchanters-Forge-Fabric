package net.jimtendo.enchantersforge.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.jimtendo.enchantersforge.EnchantersForge;
import net.jimtendo.enchantersforge.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<ConvergenceTableBlockEntity> CONVERGENCE_TABLE_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(EnchantersForge.MOD_ID,"convergence_table_be"),
                    FabricBlockEntityTypeBuilder.create(ConvergenceTableBlockEntity::new,
                            ModBlocks.CONVERGENCE_TABLE).build());

    public static void registerBlockEntities() {
        EnchantersForge.LOGGER.info("Registering Block Entities for " + EnchantersForge.MOD_ID);
    }
}
