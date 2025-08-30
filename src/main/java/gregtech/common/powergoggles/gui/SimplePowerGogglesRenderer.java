package gregtech.common.powergoggles.gui;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.math.Color;

import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;

public class SimplePowerGogglesRenderer extends PowerGogglesRenderer {

    @Override
    public void renderMainInfo(RenderGameOverlayEvent.Post event) {
        ScaledResolution resolution = event.resolution;
        int screenHeight = resolution.getScaledHeight();
        int screenWidth = resolution.getScaledWidth();

        FontRenderer fontRenderer = mc.fontRenderer;

        int xOffset = PowerGogglesConfigHandler.mainOffsetX;
        int yOffset = PowerGogglesConfigHandler.mainOffsetY;
        int borderRadius = 3;

        double subScale = PowerGogglesConfigHandler.subTextScaling;
        int gapBetweenLines = 2;
        int scaleOffsetX = xOffset - borderRadius;
        int scaleOffsetY = screenHeight - yOffset
            + gapBetweenLines * 2
            + (int) (fontRenderer.FONT_HEIGHT * 2 * subScale)
            + borderRadius;

        GL11.glPushMatrix();
        GL11.glEnable(GL_CULL_FACE);
        GL11.glTranslated(scaleOffsetX, scaleOffsetY, 0);
        GL11.glScaled(PowerGogglesConfigHandler.hudScale, PowerGogglesConfigHandler.hudScale, 1);
        GL11.glTranslated(-scaleOffsetX, -scaleOffsetY, 0);

        renderBackground(event);
        renderGradientRectangle(event);
        renderStorageText(event);
        renderTimedDifferenceText(event);

        if (PowerGogglesConfigHandler.showPowerChart) {
            renderPowerChart();
        }

        GL11.glPopMatrix();

    }

    private void renderBackground(RenderGameOverlayEvent.Post event) {
        ScaledResolution resolution = event.resolution;
        int screenHeight = resolution.getScaledHeight();

        int xOffset = PowerGogglesConfigHandler.mainOffsetX;
        int yOffset = PowerGogglesConfigHandler.mainOffsetY;
        int w = PowerGogglesConfigHandler.rectangleWidth;
        int h = PowerGogglesConfigHandler.rectangleHeight;
        int borderRadius = 3;

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int gapBetweenLines = 2;
        double mainScale = PowerGogglesConfigHandler.mainTextScaling;
        double subScale = PowerGogglesConfigHandler.subTextScaling;
        int bgColor = Color.argb(47, 20, 76, (int) (255 * 0.85));
        int highestPoint = screenHeight - yOffset
            - h
            - gapBetweenLines
            - (int) (fontRenderer.FONT_HEIGHT * mainScale)
            - borderRadius;
        GuiHelper.drawGradientRect(
            -1,
            xOffset - borderRadius,
            highestPoint,
            xOffset + w + borderRadius,
            screenHeight - yOffset
                + gapBetweenLines * 2
                + (int) (fontRenderer.FONT_HEIGHT * 2 * subScale)
                + borderRadius,
            bgColor,
            bgColor);

    }

    private void renderGradientRectangle(RenderGameOverlayEvent.Post event) {
        ScaledResolution resolution = event.resolution;
        int screenHeight = resolution.getScaledHeight();

        int xOffset = PowerGogglesConfigHandler.mainOffsetX;
        int yOffset = PowerGogglesConfigHandler.mainOffsetY;
        int w = PowerGogglesConfigHandler.rectangleWidth;
        int h = PowerGogglesConfigHandler.rectangleHeight;

        int left = w + xOffset;
        int up = screenHeight - w - yOffset;
        int right = left + h;
        int down = up + w;

        GL11.glPushMatrix();
        GL11.glTranslated(left, down, 0);
        GL11.glRotated(90, 0, 0, -1);
        GL11.glTranslated(-left, -down, 0);

        double scale = 3.3;
        double severity = measurement.compareTo(BigInteger.ZERO) == 0 ? 0
            : new BigDecimal(change5m.multiply(BigInteger.valueOf(100)))
                .divide(new BigDecimal(measurement), RoundingMode.FLOOR)
                .intValue() / 100f;

        int[] gradientSet = new int[] { PowerGogglesConfigHandler.gradientBadColor,
            PowerGogglesConfigHandler.gradientOkColor, PowerGogglesConfigHandler.gradientGoodColor };
        int[] gradients;
        if (severity < 0) {
            gradients = getGradient(-severity, scale, gradientSet[0], gradientSet[1]);
            GuiHelper.drawGradientRect(300, left, up, right, down, gradients[0], gradients[1]);
        } else {
            gradients = getGradient(severity, scale * 1.4f, gradientSet[2], gradientSet[1]);
            GuiHelper.drawGradientRect(300, left, up, right, down, gradients[1], gradients[0]);
        }

        GL11.glDisable(GL_LIGHTING);
        GL11.glPopMatrix();
    }

    private void renderStorageText(RenderGameOverlayEvent.Post event) {}

    private void renderTimedDifferenceText(RenderGameOverlayEvent.Post event) {}

    private void drawScaledString(FontRenderer fontRenderer, String string, int xOffset, int yOffset, int color,
        double scale) {
        GL11.glPushMatrix();
        GL11.glTranslated(xOffset, yOffset, 0);
        GL11.glScaled(scale, scale, 1);
        GL11.glTranslated(-xOffset, -yOffset, 0);
        fontRenderer.drawStringWithShadow(string, xOffset, yOffset, color);
        GL11.glPopMatrix();
    }

    public int[] getGradient(double severity, double scale, int gradientLeft, int gradientRight) {
        int newGradientLeft = gradientLeft;
        int newGradientRight = gradientRight;

        int diffRed = Color.getRed(gradientLeft) - Color.getRed(gradientRight);
        int diffGreen = Color.getGreen(gradientLeft) - Color.getGreen(gradientRight);
        int diffBlue = Color.getBlue(gradientLeft) - Color.getBlue(gradientRight);

        int newLeftRed = Math
            .min(255, Math.max(0, Color.getRed(gradientRight) + (int) (diffRed * Math.min(1, severity * scale))));
        int newLeftGreen = Math
            .min(255, Math.max(0, Color.getGreen(gradientRight) + (int) (diffGreen * Math.min(1, severity * scale))));
        int newLeftBlue = Math
            .min(255, Math.max(0, Color.getBlue(gradientRight) + (int) (diffBlue * Math.min(1, severity * scale))));

        int newRightRed = Math.min(
            255,
            Math.max(0, Color.getRed(gradientRight) + (int) (diffRed * Math.min(1, severity * scale * 0.75))));
        int newRightGreen = Math.min(
            255,
            Math.max(0, Color.getGreen(gradientRight) + (int) (diffGreen * Math.min(1, severity * scale * 0.75))));
        int newRightBlue = Math.min(
            255,
            Math.max(0, Color.getBlue(gradientRight) + (int) (diffBlue * Math.min(1, severity * scale * 0.75))));

        newGradientLeft = Color.rgb(newLeftRed, newLeftGreen, newLeftBlue);
        newGradientRight = Color.rgb(newRightRed, newRightGreen, newRightBlue);

        return new int[] { newGradientLeft, newGradientRight };
    }

    @Override
    public void renderPowerChart() {

    }

    @Override
    public void drawTick() {

    }

    @Override
    public void clear() {
        measurements.clear();
        currentEU = BigInteger.valueOf(0);
        measurement = BigInteger.valueOf(0);
        highest = BigInteger.valueOf(0);
        capacity = 0;
        change5mDiff = 0;
        change1hDiff = 0;
    }

    @Override
    public void setMeasurement(BigInteger newEU, long lscCapacity) {
        capacity = lscCapacity;
        setMeasurement(newEU);
    }

    public void setMeasurement(BigInteger newEU) {
        measurement = newEU;
        if (highest.compareTo(measurement) < 0) highest = measurement;
        currentEU = measurement;
        measurements.addFirst(measurement);
        if (measurements.size() > measurementCount1h) measurements.removeLast();
    }
}
