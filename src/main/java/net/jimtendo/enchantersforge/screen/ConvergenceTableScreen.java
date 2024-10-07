package net.jimtendo.enchantersforge.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.jimtendo.enchantersforge.EnchantersForge;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConvergenceTableScreen extends HandledScreen<ConvergenceTableScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(EnchantersForge.MOD_ID, "textures/gui/convergence_table_gui.png");
    private static final Identifier ERROR_TEXTURE = new Identifier(EnchantersForge.MOD_ID, "textures/gui/error.png");

    public ConvergenceTableScreen(ConvergenceTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleY = 12;
        playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        if (handler.getConvergenceCost() > 0) {
            Text costText = Text.translatable("container.repair.cost", handler.getConvergenceCost());
            int costWidth = textRenderer.getWidth(costText);
            int costX = x + 104 - costWidth / 2;
            int costY = y + 24;

            context.fill(costX - 2, costY - 2, costX + costWidth + 2, costY + 10, 0x4F000000);

            int textColor;
            if (handler.getConvergenceCost() > client.player.experienceLevel && !client.player.getAbilities().creativeMode) {
                textColor = 0xFF6060;
            } else {
                textColor = 0x80FF20;
            }
            context.drawText(textRenderer, costText, costX + 1, costY + 1, 0xFF000000, false);
            context.drawText(textRenderer, costText, costX, costY, textColor, false);
        }

        if (handler.hasInputs() && !handler.isValidRecipe()) {
            RenderSystem.setShaderTexture(0, ERROR_TEXTURE);
            int arrowX = x + 99;
            int arrowY = y + 45;
            context.drawTexture(ERROR_TEXTURE, arrowX, arrowY, 0, 0, 28, 21, 28, 21);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}