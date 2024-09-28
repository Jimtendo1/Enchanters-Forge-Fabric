package net.jimtendo.enchantersforge.enchantment;

import net.jimtendo.enchantersforge.EnchantersForge;
import net.jimtendo.enchantersforge.enchantment.custom.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModEnchantments {
    public static final List<Enchantment> MOD_ENCHANTMENTS = new ArrayList<>();

    public static final Enchantment POISON_ASPECT = registerEnchantment("poison_aspect",
            new PoisonAspectEnchantment());
    public static final Enchantment WITHER_ASPECT = registerEnchantment("wither_aspect",
            new WitherAspectEnchantment());
    public static final Enchantment FROST_ASPECT = registerEnchantment("frost_aspect",
            new FrostAspectEnchantment());
    public static final Enchantment INCINERATE = registerEnchantment("incinerate",
            new IncinerateEnchantment());
    public static final Enchantment INTOXICATE = registerEnchantment("intoxicate",
            new IntoxicateEnchantment());
    public static final Enchantment FROSTBITE = registerEnchantment("frostbite",
            new FrostbiteEnchantment());
    public static final Enchantment RICOCHET = registerEnchantment("ricochet",
            new RicochetEnchantment());
    public static final Enchantment DISARMING = registerEnchantment("disarming",
            new DisarmingEnchantment());
    public static final Enchantment POSEIDONS_CALL = registerEnchantment("poseidons_call",
            new PoseidonsCallEnchantment());
    public static final Enchantment WATERS_EMBRACE = registerEnchantment("waters_embrace",
            new WatersEmbraceEnchantment());
    public static final Enchantment VITALITY_BOOST = registerEnchantment("vitality_boost",
            new VitalityBoostEnchantment());
    public static final Enchantment VAMPIRISM = registerEnchantment("vampirism",
            new VampirismEnchantment());
    public static final Enchantment HOMING = registerEnchantment("homing",
            new HomingEnchantment());
    public static final Enchantment SOULBOUND = registerEnchantment("soulbound",
            new SoulboundEnchantment());

    private static Enchantment registerEnchantment(String name, Enchantment enchantment) {
        Enchantment registeredEnchantment = Registry.register(Registries.ENCHANTMENT, new Identifier(EnchantersForge.MOD_ID, name), enchantment);
        MOD_ENCHANTMENTS.add(registeredEnchantment);
        return registeredEnchantment;
    }

    public static void registerModEnchantments() {
        EnchantersForge.LOGGER.info("Registering ModEnchantments for " + EnchantersForge.MOD_ID);
    }
}