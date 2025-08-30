package gregtech.common.powergoggles.gui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;
import gregtech.common.powergoggles.handlers.PowerGogglesHudHandler;

public class SimplePowerGogglesRenderer extends PowerGogglesRenderer {

    @Override
    public void renderMainInfo(RenderGameOverlayEvent.Post event) {
        PowerGogglesHudHandler hudHandler = PowerGogglesHudHandler.getInstance();
        ScaledResolution resolution = event.resolution;
        int screenHeight = resolution.getScaledHeight();
        int screenWidth = resolution.getScaledWidth();

        FontRenderer fontRenderer = mc.fontRenderer;
        GL11.glPushMatrix();
        GL11.glEnable(GL_CULL_FACE);

        int xOffset = PowerGogglesConfigHandler.mainOffsetX;
        int yOffset = PowerGogglesConfigHandler.mainOffsetY;
        int w = PowerGogglesConfigHandler.rectangleWidth;
        int h = PowerGogglesConfigHandler.rectangleHeight;
        int borderRadius = 3;

        double subScale = PowerGogglesConfigHandler.subTextScaling;
        int gapBetweenLines = 2;
        int scaleOffsetX = xOffset - borderRadius;
        int scaleOffsetY = screenHeight - yOffset
            + gapBetweenLines * 2
            + (int) (fontRenderer.FONT_HEIGHT * 2 * subScale)
            + borderRadius;

        GL11.glTranslated(scaleOffsetX, scaleOffsetY, 0);
        GL11.glScaled(PowerGogglesConfigHandler.hudScale, PowerGogglesConfigHandler.hudScale, 1);
        GL11.glTranslated(-scaleOffsetX, -scaleOffsetY, 0);

        int chartOffsetY = hudHandler.drawPowerRectangle(xOffset, yOffset, h, w, screenHeight, borderRadius);
        if (PowerGogglesConfigHandler.showPowerChart) {
            hudHandler.drawPowerChart(
                xOffset,
                chartOffsetY - borderRadius,
                PowerGogglesConfigHandler.rectangleWidth,
                100,
                screenHeight,
                screenWidth,
                borderRadius);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void renderPowerChart() {

    }

    public SimplePowerGogglesRenderer() {
        super();
    }
}
