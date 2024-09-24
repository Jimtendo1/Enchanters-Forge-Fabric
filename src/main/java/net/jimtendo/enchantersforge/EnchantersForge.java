package net.jimtendo.enchantersforge;

import net.fabricmc.api.ModInitializer;

import net.jimtendo.enchantersforge.block.ModBlocks;
import net.jimtendo.enchantersforge.block.entity.ModBlockEntities;
import net.jimtendo.enchantersforge.enchantment.ModEnchantments;
import net.jimtendo.enchantersforge.enchantment.custom.SoulboundEvents;
import net.jimtendo.enchantersforge.item.ModItemGroups;
import net.jimtendo.enchantersforge.screen.ModScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnchantersForge implements ModInitializer {
	public static final String MOD_ID = "enchanters_forge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		ModEnchantments.registerModEnchantments();

		ModItemGroups.registerItemGroups();

		ModBlocks.registerModBlocks();

		ModScreenHandlers.registerScreenHandlers();

		ModBlockEntities.registerBlockEntities();

		SoulboundEvents.register();

	}
}