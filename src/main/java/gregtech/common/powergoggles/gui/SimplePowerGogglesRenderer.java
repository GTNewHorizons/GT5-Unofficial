package gregtech.common.powergoggles.gui;

import static org.lwjgl.opengl.GL11.GL_ALL_ATTRIB_BITS;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.math.Color;

import gregtech.common.powergoggles.PowerGogglesUtil;
import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;

public class SimplePowerGogglesRenderer extends PowerGogglesRenderer {

    private FontRenderer fontRenderer;
    private final int borderRadius = 3;
    private final int gapBetweenLines = 2;

    private int gradientRectangleHeight;
    private int screenHeight;
    private int xOffset;
    private int yOffset;
    private double mainScale;
    private double subScale;

    @Override
    public void renderMainInfo(RenderGameOverlayEvent.Post event) {
        updateRenderingProperties(event);

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

        renderGradientRectangle();
        renderStorageText();
        renderTimedDifferenceText();

        if (PowerGogglesConfigHandler.showPowerChart) {
            renderPowerChart();
        }
        renderBackground();

        GL11.glPopMatrix();

    }

    private void updateRenderingProperties(RenderGameOverlayEvent.Post event) {
        if (fontRenderer == null) {
            fontRenderer = mc.fontRenderer;
        }
        this.gradientRectangleHeight = PowerGogglesConfigHandler.rectangleHeight;

        ScaledResolution resolution = event.resolution;
        this.screenHeight = resolution.getScaledHeight();

        this.xOffset = PowerGogglesConfigHandler.mainOffsetX;
        this.yOffset = PowerGogglesConfigHandler.mainOffsetY;

        this.mainScale = PowerGogglesConfigHandler.mainTextScaling;
        this.subScale = PowerGogglesConfigHandler.subTextScaling;
    }

    private void renderGradientRectangle() {
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

    private void renderStorageText() {
        int offsetFactor = yOffset + gradientRectangleHeight + gapBetweenLines;
        int stringHeight = (int) (fontRenderer.FONT_HEIGHT * mainScale);
        int stringY = screenHeight - offsetFactor - stringHeight;

        String currentStorage = measurements.isEmpty() ? "0" : PowerGogglesUtil.format(measurements.getFirst());;

        drawScaledString(currentStorage, xOffset, stringY, Color.rgb(255, 255, 255), mainScale);
    }

    private void renderTimedDifferenceText() {
        int offsetFactor = yOffset - gapBetweenLines;
        int stringHeight = (int) (fontRenderer.FONT_HEIGHT * subScale);

        String timedDifference5m = PowerGogglesUtil.format(BigInteger.valueOf(12389143));
        String timedDifference1h = PowerGogglesUtil.format(BigInteger.valueOf(123891430));

        int string5mY = screenHeight - offsetFactor;
        int string1hY = string5mY + gapBetweenLines + stringHeight;

        drawScaledString(timedDifference5m, xOffset, string5mY, Color.rgb(255, 255, 255), subScale);
        drawScaledString(timedDifference1h, xOffset, string1hY, Color.rgb(255, 255, 255), subScale);

    }

    private void drawScaledString(String string, int xOffset, int yOffset, int color, double scale) {
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

    private void renderBackground() {

        int w = PowerGogglesConfigHandler.rectangleWidth;
        int h = PowerGogglesConfigHandler.rectangleHeight;

        org.lwjgl.util.Color bgColor = new org.lwjgl.util.Color(47, 20, 76, (int) (255 * 0.85));
        int bgHeightAboveGradient = gapBetweenLines * 2 + (int) (fontRenderer.FONT_HEIGHT * mainScale)
            + gradientRectangleHeight
            + borderRadius;
        int bgHeightBelowGradient = gapBetweenLines + (int) (fontRenderer.FONT_HEIGHT * 2 * subScale) + borderRadius;

        int bgTop = screenHeight - yOffset - bgHeightAboveGradient;
        int bgBottom = screenHeight - yOffset + borderRadius + bgHeightBelowGradient;
        int bgLeft = xOffset - borderRadius;
        int bgRight = xOffset + w + borderRadius;

        GL11.glPushAttrib(GL_ALL_ATTRIB_BITS);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), bgColor.getAlpha());

        tessellator.addVertex(bgLeft, bgTop, -1);
        tessellator.addVertex(bgLeft, bgBottom, -1);
        tessellator.addVertex(bgRight, bgBottom, -1);
        tessellator.addVertex(bgRight, bgTop, -1);

        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glPopAttrib();

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
