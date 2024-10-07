package net.jimtendo.enchantersforge.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.jimtendo.enchantersforge.EnchantersForge;
import net.minecraft.inventory.Inventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<ConvergenceTableScreenHandler> CONVERGENCE_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(EnchantersForge.MOD_ID, "convergence_table"),
                    new ExtendedScreenHandlerType<>(ConvergenceTableScreenHandler::new));

    public static void registerScreenHandlers() {
        EnchantersForge.LOGGER.info("Registering Screen Handlers for " + EnchantersForge.MOD_ID);
    }
}