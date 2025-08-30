package gregtech.common.powergoggles.gui;

import static org.lwjgl.opengl.GL11.GL_ALL_ATTRIB_BITS;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LINES;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import com.google.common.math.BigIntegerMath;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.math.Color;

import gregtech.common.powergoggles.PowerGogglesConstants;
import gregtech.common.powergoggles.PowerGogglesMeasurement;
import gregtech.common.powergoggles.PowerGogglesUtil;
import gregtech.common.powergoggles.handlers.PowerGogglesConfigHandler;

public class SimplePowerGogglesRenderer extends PowerGogglesRenderer {

    private FontRenderer fontRenderer;
    private final int borderRadius = 3;
    private final int gapBetweenLines = 2;

    private int gradientRectangleHeight;
    private int gradientRectangleWidth;

    private int chartWidth;
    private int chartHeight;

    private int screenHeight;

    private int xOffset;
    private int yOffset;

    private double mainScale;
    private double subScale;

    private BigInteger euDifference5m = BigInteger.ZERO;
    private BigInteger euDifference1h = BigInteger.ZERO;

    @Override
    public void render(RenderGameOverlayEvent.Post event) {
        updateRenderingProperties(event);

        int scaleOffsetX = xOffset - borderRadius;
        int scaleOffsetY = screenHeight - yOffset;

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
        this.gradientRectangleWidth = PowerGogglesConfigHandler.rectangleWidth;

        this.chartWidth = PowerGogglesConfigHandler.rectangleWidth;
        this.chartHeight = 100;

        ScaledResolution resolution = event.resolution;
        this.screenHeight = resolution.getScaledHeight();

        this.xOffset = PowerGogglesConfigHandler.mainOffsetX;
        this.yOffset = PowerGogglesConfigHandler.mainOffsetY;

        this.mainScale = PowerGogglesConfigHandler.mainTextScaling;
        this.subScale = PowerGogglesConfigHandler.subTextScaling;
    }

    private void renderGradientRectangle() {

        int mainStringHeight = (int) (fontRenderer.FONT_HEIGHT * mainScale);
        int heightAboveRectangle = mainStringHeight + gapBetweenLines + borderRadius;

        int rectangleTop = screenHeight - yOffset + heightAboveRectangle;
        int rectangleBottom = screenHeight - yOffset + heightAboveRectangle + gradientRectangleHeight;
        int rectangleLeft = xOffset;
        int rectangleRight = xOffset + gradientRectangleWidth;

        int[] rectangleColors = getGradientRectangleColors();

        GL11.glPushAttrib(GL_ALL_ATTRIB_BITS);
        GL11.glShadeModel(GL11.GL_SMOOTH); // enable color interpolation (gradients)
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        tessellator.setColorRGBA(
            Color.getRed(rectangleColors[0]),
            Color.getGreen(rectangleColors[0]),
            Color.getBlue(rectangleColors[0]),
            Color.getAlpha(rectangleColors[0]));
        tessellator.addVertex(rectangleLeft, rectangleTop, 300);
        tessellator.addVertex(rectangleLeft, rectangleBottom, 300);

        tessellator.setColorRGBA(
            Color.getRed(rectangleColors[1]),
            Color.getGreen(rectangleColors[1]),
            Color.getBlue(rectangleColors[1]),
            Color.getAlpha(rectangleColors[1]));
        tessellator.addVertex(rectangleRight, rectangleBottom, 300);
        tessellator.addVertex(rectangleRight, rectangleTop, 300);

        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glPopAttrib();
    }

    private int[] getGradientRectangleColors() {
        int[] gradients;
        int colorLeft;
        int colorRight;

        BigInteger lastMeasurement = measurements.isEmpty() ? BigInteger.ZERO
            : measurements.getLast()
                .getMeasurement();

        double differenceRatio = lastMeasurement.compareTo(BigInteger.ZERO) == 0 ? 0
            : new BigDecimal(euDifference5m.multiply(BigInteger.valueOf(100)))
                .divide(new BigDecimal(lastMeasurement), RoundingMode.FLOOR)
                .intValue() / 100f;

        double gradientChangeFactor = 3.3;
        if (differenceRatio < 0) {
            gradients = getGradient(
                -differenceRatio,
                gradientChangeFactor,
                PowerGogglesConfigHandler.gradientBadColor,
                PowerGogglesConfigHandler.gradientOkColor);
            colorLeft = gradients[0];
            colorRight = gradients[1];
        } else {
            gradients = getGradient(
                differenceRatio,
                gradientChangeFactor * 1.6f,
                PowerGogglesConfigHandler.gradientGoodColor,
                PowerGogglesConfigHandler.gradientOkColor);
            colorLeft = gradients[1];
            colorRight = gradients[0];
        }
        return new int[] { colorLeft, colorRight };
    }

    private void renderStorageText() {
        int stringY = screenHeight - yOffset + borderRadius;

        BigInteger measurement = measurements.isEmpty() ? BigInteger.ZERO
            : measurements.getLast()
                .getMeasurement();
        String currentStorage = PowerGogglesUtil.format(measurement);
        int stringColor = getTextColor(euDifference5m);

        drawScaledString(currentStorage, xOffset, stringY, stringColor, mainScale);
    }

    private int getTextColor(BigInteger measurement) {
        return switch (measurement.compareTo(BigInteger.ZERO)) {
            case -1 -> PowerGogglesConfigHandler.textBadColor;
            case 1 -> PowerGogglesConfigHandler.textGoodColor;
            default -> PowerGogglesConfigHandler.textOkColor;
        };
    }

    private void renderTimedDifferenceText() {
        int stringHeight = (int) (fontRenderer.FONT_HEIGHT * subScale);
        int offsetFactor = yOffset - borderRadius - stringHeight - gapBetweenLines * 2 - gradientRectangleHeight;

        String timedDifference5m = PowerGogglesUtil.format(euDifference5m);
        int stringColor5m = getTextColor(euDifference5m);

        String timedDifference1h = PowerGogglesUtil.format(euDifference1h);
        int stringColor1h = getTextColor(euDifference1h);

        int string5mY = screenHeight + gapBetweenLines - offsetFactor;
        int string1hY = string5mY + gapBetweenLines + stringHeight;

        drawScaledString(timedDifference5m, xOffset, string5mY, stringColor5m, subScale);
        drawScaledString(timedDifference1h, xOffset, string1hY, stringColor1h, subScale);

    }

    private void drawScaledString(String string, int xOffset, int yOffset, int color, double scale) {
        GL11.glPushMatrix();
        GL11.glTranslated(xOffset, yOffset, 0);
        GL11.glScaled(scale, scale, 1);
        GL11.glTranslated(-xOffset, -yOffset, 0);
        fontRenderer.drawStringWithShadow(string, xOffset, yOffset, color);
        GL11.glPopMatrix();
    }

    public int[] getGradient(double differenceRatio, double gradientChangeFactor, int gradientLeft, int gradientRight) {

        int diffRed = Color.getRed(gradientLeft) - Color.getRed(gradientRight);
        int diffGreen = Color.getGreen(gradientLeft) - Color.getGreen(gradientRight);
        int diffBlue = Color.getBlue(gradientLeft) - Color.getBlue(gradientRight);

        int newLeftRed = getGradientPart(gradientChangeFactor, Color.getRed(gradientRight), diffRed, differenceRatio);
        int newLeftGreen = getGradientPart(
            gradientChangeFactor,
            Color.getGreen(gradientRight),
            diffGreen,
            differenceRatio);
        int newLeftBlue = getGradientPart(
            gradientChangeFactor,
            Color.getBlue(gradientRight),
            diffBlue,
            differenceRatio);

        int newRightRed = getGradientPart(
            gradientChangeFactor,
            Color.getRed(gradientRight),
            diffRed,
            differenceRatio * 0.75);
        int newRightGreen = getGradientPart(
            gradientChangeFactor,
            Color.getGreen(gradientRight),
            diffGreen,
            differenceRatio * 0.75);
        int newRightBlue = getGradientPart(
            gradientChangeFactor,
            Color.getBlue(gradientRight),
            diffBlue,
            differenceRatio * 0.75);

        int newGradientLeft = Color.rgb(newLeftRed, newLeftGreen, newLeftBlue);
        int newGradientRight = Color.rgb(newRightRed, newRightGreen, newRightBlue);

        return new int[] { newGradientLeft, newGradientRight };
    }

    private int getGradientPart(double gradientChangeFactor, int baseGradientPart, int partDifference,
        double differenceRatio) {
        double appliedPercentageOfDifference = Math.min(1, differenceRatio * gradientChangeFactor);
        int newPart = baseGradientPart + (int) (partDifference * appliedPercentageOfDifference);

        int clampBottom = Math.max(0, newPart);
        int clampTop = Math.min(255, clampBottom);
        return clampTop;
    }

    private void renderBackground() {

        int bgColor = Color.argb(47, 20, 76, (int) (255 * 0.85));

        double mainStringHeight = fontRenderer.FONT_HEIGHT * mainScale;
        double subStringHeight = fontRenderer.FONT_HEIGHT * subScale * 2;
        double gapHeight = gapBetweenLines * 4;

        int bgHeight = (int) (mainStringHeight + gradientRectangleHeight + subStringHeight + gapHeight);

        int bgTop = screenHeight - yOffset - borderRadius;
        int bgBottom = screenHeight - yOffset + borderRadius + bgHeight;
        int bgLeft = xOffset - borderRadius;
        int bgRight = xOffset + gradientRectangleWidth + borderRadius;

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
        tessellator.setColorRGBA(
            Color.getRed(bgColor),
            Color.getGreen(bgColor),
            Color.getBlue(bgColor),
            Color.getAlpha(bgColor));

        tessellator.addVertex(bgLeft, bgTop, -1);
        tessellator.addVertex(bgLeft, bgBottom, -1);
        tessellator.addVertex(bgRight, bgBottom, -1);
        tessellator.addVertex(bgRight, bgTop, -1);

        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glPopAttrib();

    }

    public void renderPowerChart() {

        renderPowerChartBackground();
        if (measurements.isEmpty()) return;

        int measurementCount = Math.min(measurements.size(), PowerGogglesConstants.MEASUREMENT_COUNT_5M);

        BigInteger minReading = measurements.get(0)
            .getMeasurement();
        BigInteger maxReading = measurements.get(0)
            .getMeasurement();
        for (int i = 0; i < measurementCount; i++) {
            BigInteger temp = measurements.get(i)
                .getMeasurement();
            if (temp.compareTo(minReading) < 0) minReading = temp;
            if (maxReading.compareTo(temp) < 0) maxReading = temp;
        }

        int exponent = minReading.compareTo(BigInteger.ZERO) == 0 ? 0
            : BigIntegerMath.log10(minReading, RoundingMode.DOWN);
        minReading = minReading.compareTo(BigInteger.ZERO) == 0 ? BigInteger.ZERO
            : BigInteger.valueOf(10)
                .pow(exponent);

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        double scale = 0.5f;

        drawScaledString(
            PowerGogglesUtil.format(minReading),
            xOffset,
            screenHeight - yOffset - borderRadius * 2 - (int) (fontRenderer.FONT_HEIGHT * scale),
            Color.rgb(237, 2, 158),
            scale);
        drawScaledString(
            minReading.compareTo(maxReading) == 0 ? "" : PowerGogglesUtil.format(maxReading),
            xOffset,
            screenHeight - yOffset - borderRadius * 2 - chartHeight,
            Color.rgb(237, 2, 158),
            scale);
        if (measurementCount < 2) return;

        GL11.glPushAttrib(GL_ALL_ATTRIB_BITS);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL_LINES);

        int negative = PowerGogglesConfigHandler.textBadColor;
        int positive = PowerGogglesConfigHandler.textGoodColor;
        BigInteger lastReading = measurements.getLast()
            .getMeasurement();
        double pointsWidth = chartWidth * 0.8d;
        double lastX = xOffset + chartWidth;
        double lastY = screenHeight + (yOffset + (chartHeight * (-1 + Math.min(
            1,
            1 - lastReading.subtract(minReading)
                .floatValue()
                / maxReading.subtract(minReading)
                    .floatValue()))));
        for (int i = 1; i < measurementCount; i++) {
            BigInteger reading = measurements.get(i)
                .getMeasurement();
            double x = xOffset + chartWidth - (pointsWidth / (measurementCount)) * i;
            double y = screenHeight + (yOffset + (chartHeight * (-1 + Math.min(
                1,
                1 - reading.subtract(minReading)
                    .floatValue()
                    / maxReading.subtract(minReading)
                        .floatValue()))));
            if (reading.compareTo(lastReading) > 0) {
                tessellator
                    .setColorRGBA_F(Color.getRed(negative), Color.getGreen(negative), Color.getBlue(negative), 255);
            } else {
                tessellator
                    .setColorRGBA_F(Color.getRed(positive), Color.getGreen(positive), Color.getBlue(positive), 255);
            }
            lastReading = reading;
            tessellator.addVertex(lastX, lastY, 0);
            tessellator.addVertex(x, y, 0);
            lastX = x;
            lastY = y;
        }
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GL11.glPopAttrib();

    }

    private void renderPowerChartBackground() {

        int left = xOffset;
        int right = xOffset + chartWidth;
        int top = screenHeight - yOffset - chartHeight - borderRadius * 2;
        int bottom = screenHeight - yOffset - borderRadius * 2;
        int bgColor = Color.argb(19, 14, 91, (int) (255 * 0.75f));
        GuiHelper.drawGradientRect(-1, left, top, right, bottom, bgColor, bgColor);
        int borderColor = Color.rgb(81, 79, 104);
        GuiHelper.drawGradientRect(
            -2,
            left - borderRadius,
            top - borderRadius,
            right + borderRadius,
            bottom + borderRadius,
            borderColor,
            borderColor);
    }

    @Override
    public void drawTick() {

    }

    @Override
    public void clear() {
        legacyMeasurements.clear();
        currentEU = BigInteger.valueOf(0);
        measurement = BigInteger.valueOf(0);
        highest = BigInteger.valueOf(0);
        capacity = 0;
        change5mDiff = 0;
        change1hDiff = 0;
    }

    @Override
    public void setLegacyMeasurement(BigInteger newEU, long lscCapacity) {
        capacity = lscCapacity;
        setMeasurement(newEU);
    }

    public void setMeasurement(BigInteger newEU) {
        measurement = newEU;
        if (highest.compareTo(measurement) < 0) highest = measurement;
        currentEU = measurement;
        legacyMeasurements.addFirst(measurement);
        if (legacyMeasurements.size() > measurementCount1h) legacyMeasurements.removeLast();

    }

    @Override
    public void setMeasurements(LinkedList<PowerGogglesMeasurement> measurements) {
        this.measurements = measurements;
        onNewMeasurement();
    }

    @Override
    public void processMeasurement(PowerGogglesMeasurement measurement) {
        measurements.addLast(measurement);
        if (measurements.size() > PowerGogglesConstants.STORED_MEASUREMENTS) {
            measurements.removeFirst();
        }
        onNewMeasurement();
    }

    private void onNewMeasurement() {
        if (measurements.isEmpty()) {
            return;
        }
        update5mDifference();
        update1hDifference();
    }

    private void update5mDifference() {
        LinkedList<PowerGogglesMeasurement> lastMeasurements = getLastMeasurements(
            PowerGogglesConstants.MEASUREMENT_COUNT_5M);
        BigInteger first = lastMeasurements.getFirst()
            .getMeasurement();
        BigInteger last = lastMeasurements.getLast()
            .getMeasurement();

        this.euDifference5m = first.subtract(last);
    }

    private void update1hDifference() {
        LinkedList<PowerGogglesMeasurement> lastMeasurements = getLastMeasurements(
            PowerGogglesConstants.MEASUREMENT_COUNT_1H);
        BigInteger first = lastMeasurements.getFirst()
            .getMeasurement();
        BigInteger last = lastMeasurements.getLast()
            .getMeasurement();

        this.euDifference1h = first.subtract(last);
    }

    private LinkedList<PowerGogglesMeasurement> getLastMeasurements(int count) {
        List<PowerGogglesMeasurement> lastMeasurements = new ArrayList<>(measurements);
        Collections.reverse(lastMeasurements);

        return new LinkedList<>(
            lastMeasurements.stream()
                .limit(count)
                .collect(Collectors.toList()));
    }

}
