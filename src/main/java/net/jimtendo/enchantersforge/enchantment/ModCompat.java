package net.jimtendo.enchantersforge.enchantment;

import net.fabricmc.loader.api.FabricLoader;
import net.jimtendo.enchantersforge.EnchantersForge;
import net.jimtendo.enchantersforge.enchantment.custom.aether.AetherInebriationEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModCompat {
    public static final String AETHER_MOD_ID = "aether";
    public static Enchantment AETHER_INEBRIATION_ENCHANTMENT;

    public static void registerCompatEnchantments() {
        if (isAetherLoaded()) {
            EnchantersForge.LOGGER.info("Aether mod detected! Registering Aether compatibility enchantments.");
            registerAetherEnchantments();
        } else {
            EnchantersForge.LOGGER.info("Aether mod not detected. Skipping Aether compatibility enchantments.");
        }
    }

    private static boolean isAetherLoaded() {
        return FabricLoader.getInstance().isModLoaded(AETHER_MOD_ID);
    }

    private static void registerAetherEnchantments() {
        // Register the Aether-exclusive Inebriation enchantment
        AETHER_INEBRIATION_ENCHANTMENT = Registry.register(
                Registries.ENCHANTMENT,
                new Identifier(EnchantersForge.MOD_ID, "aether_inebriation"),
                new AetherInebriationEnchantment()
        );
        EnchantersForge.LOGGER.info("Registered Aether Inebriation Enchantment.");
    }
} 