package net.jimtendo.enchantersforge;

import net.fabricmc.api.ClientModInitializer;
import net.jimtendo.enchantersforge.screen.ConvergenceTableScreen;
import net.jimtendo.enchantersforge.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class EnchantersForgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        HandledScreens.register(ModScreenHandlers.CONVERGENCE_TABLE_SCREEN_HANDLER, ConvergenceTableScreen::new);

    }
}
