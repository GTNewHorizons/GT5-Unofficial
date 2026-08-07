package gregtech.common.powergoggles.gui;

import static org.lwjgl.opengl.GL11.GL_ALL_ATTRIB_BITS;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LINES;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

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

    private static final DecimalFormat PERCENTAGE_FORMATTER = new DecimalFormat("0.00%");

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

    private final PowerGogglesMeasurement[] lastMeasurementsCache = new PowerGogglesMeasurement[PowerGogglesConstants.MEASUREMENT_COUNT_1H];
    private int lastMeasurementsCount;

    private final CachedText cachedStorage = new CachedText();
    private final CachedText cached5mText = new CachedText();
    private final CachedText cached1hText = new CachedText();
    private final CachedText cachedChartMinText = new CachedText();
    private final CachedText cachedChartMaxText = new CachedText();

    private final int[] cachedGradientRectangleColors = new int[2];
    private BigInteger cachedGradientMeasurement;
    private BigInteger cachedGradientDifference;

    private static final class CachedText {

        private BigInteger value;
        private int configIndex;
        private String text;

        private String get(BigInteger value, int configIndex, Function<BigInteger, String> formatter) {
            if (!value.equals(this.value) || configIndex != this.configIndex) {
                this.value = value;
                this.configIndex = configIndex;
                this.text = formatter.apply(value);
            }
            return this.text;
        }
    }

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

        if (PowerGogglesConfigHandler.showMeasurements) {
            renderStorageText();
            if (PowerGogglesConfigHandler.showPowerBar) {
                renderGradientRectangle();
            }
            renderTimedDifferenceText();
            renderBackground();
        }

        if (PowerGogglesConfigHandler.showPowerChart) {
            renderPowerChart();
        }

        GL11.glPopMatrix();

    }

    private void updateRenderingProperties(RenderGameOverlayEvent.Post event) {
        if (fontRenderer == null) {
            fontRenderer = mc.fontRenderer;
        }
        this.gradientRectangleHeight = PowerGogglesConfigHandler.showPowerBar
            ? PowerGogglesConfigHandler.rectangleHeight
            : 0;
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

    private void renderStorageText() {
        int stringY = screenHeight - yOffset + borderRadius;

        int stringColor = getTextColor(euDifference5m);

        renderEuStorage(stringY, stringColor);
        renderFillPercentage(stringY, stringColor);

    }

    private void renderEuStorage(int stringY, int stringColor) {
        BigInteger measurement = measurements.isEmpty() ? BigInteger.ZERO
            : measurements.getLast()
                .getMeasurement();

        String currentStorage = cachedStorage
            .get(measurement, PowerGogglesConfigHandler.formatIndex, PowerGogglesUtil::format);
        drawScaledString(currentStorage, xOffset, stringY, stringColor, mainScale);
    }

    private void renderFillPercentage(int stringY, int stringColor) {
        double percentage = getFillPercentage();
        String percentageText = PERCENTAGE_FORMATTER.format(percentage);

        int stringX = xOffset + gradientRectangleWidth - fontRenderer.getStringWidth(percentageText);
        drawScaledString(percentageText, stringX, stringY, stringColor, mainScale);
    }

    private double getFillPercentage() {
        if (measurements.isEmpty()) {
            return 0;
        }

        PowerGogglesMeasurement measurementData = measurements.getLast();
        double measurement = measurementData.getMeasurement()
            .doubleValue();

        if (measurementData.isWireless()) {
            double maximumMeasurement = getMaximumMeasurement(measurements).doubleValue();
            if (measurement == 0 || maximumMeasurement == 0) {
                return 0;
            }
            return clampPercentage(measurement / maximumMeasurement);
        } else {
            long capacity = measurementData.getCapacity();
            if (capacity == 0) {
                return 0;
            }
            return clampPercentage(measurement / capacity);
        }
    }

    private double clampPercentage(double percentage) {
        return Math.max(0, Math.min(1, percentage));
    }

    private int getTextColor(BigInteger measurement) {
        return switch (measurement.compareTo(BigInteger.ZERO)) {
            case -1 -> PowerGogglesConfigHandler.textBadColor;
            case 1 -> PowerGogglesConfigHandler.textGoodColor;
            default -> PowerGogglesConfigHandler.textOkColor;
        };
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
        BigInteger lastMeasurement = measurements.isEmpty() ? BigInteger.ZERO
            : measurements.getLast()
                .getMeasurement();

        if (lastMeasurement == cachedGradientMeasurement && euDifference5m == cachedGradientDifference) {
            return cachedGradientRectangleColors;
        }
        cachedGradientMeasurement = lastMeasurement;
        cachedGradientDifference = euDifference5m;

        double differenceRatio;
        if (lastMeasurement.equals(BigInteger.ZERO)) {
            PowerGogglesMeasurement[] recentMeasurements = getLastMeasurements(
                PowerGogglesConstants.MEASUREMENT_COUNT_5M);
            if (getMaximumMeasurement(recentMeasurements, lastMeasurementsCount).compareTo(BigInteger.ZERO) > 0) {
                differenceRatio = -1;
            } else {
                differenceRatio = 0;
            }
        } else {
            differenceRatio = new BigDecimal(euDifference5m.multiply(BigInteger.valueOf(100)))
                .divide(new BigDecimal(lastMeasurement), RoundingMode.FLOOR)
                .doubleValue() / 100f;
        }

        double gradientChangeFactor = 3.3;
        if (differenceRatio < 0) {
            int[] gradients = getGradient(
                -differenceRatio,
                gradientChangeFactor,
                PowerGogglesConfigHandler.gradientBadColor,
                PowerGogglesConfigHandler.gradientOkColor);
            cachedGradientRectangleColors[0] = gradients[0];
            cachedGradientRectangleColors[1] = gradients[1];
        } else {
            int[] gradients = getGradient(
                differenceRatio,
                gradientChangeFactor * 1.6f,
                PowerGogglesConfigHandler.gradientGoodColor,
                PowerGogglesConfigHandler.gradientOkColor);
            cachedGradientRectangleColors[0] = gradients[1];
            cachedGradientRectangleColors[1] = gradients[0];
        }
        return cachedGradientRectangleColors;
    }

    private void renderTimedDifferenceText() {
        int stringHeight = (int) (fontRenderer.FONT_HEIGHT * subScale);
        int offsetFactor = yOffset - borderRadius - stringHeight - gapBetweenLines * 2 - gradientRectangleHeight;

        int string5mY = screenHeight + gapBetweenLines - offsetFactor;
        int string1hY = string5mY + gapBetweenLines + stringHeight;

        render5mDifference(string5mY);
        render1hDifference(string1hY);

    }

    private void render5mDifference(int stringY) {
        String timedDifference5m = cached5mText.get(euDifference5m, getTextConfigKey(), this::format5mDifference);
        int stringColor5m = getTextColor(euDifference5m);

        drawScaledString(timedDifference5m, xOffset, stringY, stringColor5m, subScale);
    }

    private String format5mDifference(BigInteger difference) {
        String formattedDifference5m = PowerGogglesUtil.format(difference);

        int tickCount5m = 5 * PowerGogglesConstants.MINUTES;
        BigInteger tickDifference5m = difference.divide(BigInteger.valueOf(tickCount5m));
        String formattedTickDifference5m = PowerGogglesUtil.format(tickDifference5m);
        return getTimedDifferenceText("5m: ", formattedDifference5m, formattedTickDifference5m);
    }

    private void render1hDifference(int y) {
        String timedDifference1h = cached1hText.get(euDifference1h, getTextConfigKey(), this::format1hDifference);
        int stringColor1h = getTextColor(euDifference1h);
        drawScaledString(timedDifference1h, xOffset, y, stringColor1h, subScale);
    }

    private String format1hDifference(BigInteger difference) {
        String formattedDifference1h = PowerGogglesUtil.format(difference);

        int tickCount1h = PowerGogglesConstants.HOURS;
        BigInteger tickDifference1h = difference.divide(BigInteger.valueOf(tickCount1h));
        String formattedTickDifference1h = PowerGogglesUtil.format(tickDifference1h);

        return getTimedDifferenceText("1h: ", formattedDifference1h, formattedTickDifference1h);
    }

    private int getTextConfigKey() {
        return PowerGogglesConfigHandler.readingIndex * 100 + PowerGogglesConfigHandler.formatIndex;
    }

    private String getTimedDifferenceText(String prefix, String formattedDifference, String formattedTickDifference) {
        return switch (PowerGogglesConfigHandler.readingIndex) {
            case 0 -> String.format("%s%s EU (%s EU/t)", prefix, formattedDifference, formattedTickDifference);
            case 1 -> String.format("%s%s EU", prefix, formattedDifference);
            case 2 -> String.format("%s%s EU/t", prefix, formattedTickDifference);
            default -> "How did you even get this reading type?";
        };
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
        return Math.min(255, clampBottom);
    }

    private void renderBackground() {

        int bgColor = PowerGogglesConfigHandler.measurementsBackgroundColor;

        double mainStringHeight = fontRenderer.FONT_HEIGHT * mainScale;
        double subStringHeight = PowerGogglesConfigHandler.showMeasurements ? fontRenderer.FONT_HEIGHT * subScale * 2
            : 0;
        double gapHeight = gapBetweenLines * (PowerGogglesConfigHandler.showMeasurements ? 4 : 2);

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
        renderGraphScaleIndicator();
        if (measurements.isEmpty()) return;

        PowerGogglesMeasurement[] lastMeasurements = getLastMeasurements(PowerGogglesConstants.MEASUREMENT_COUNT_5M);
        int measurementCount = lastMeasurementsCount;

        BigInteger minReading = getMinimumMeasurement(lastMeasurements, measurementCount);
        BigInteger maxReading = getMaximumMeasurement(lastMeasurements, measurementCount);

        if (minReading.compareTo(BigInteger.ZERO) > 0) {
            int exponent = BigIntegerMath.log10(minReading, RoundingMode.DOWN);
            minReading = BigInteger.valueOf(10)
                .pow(exponent);
        }

        if (PowerGogglesConfigHandler.manualGraphScale) {
            BigInteger manualMin = parseConfigBigInteger(PowerGogglesConfigHandler.manualGraphMin);
            BigInteger manualMax = parseConfigBigInteger(PowerGogglesConfigHandler.manualGraphMax);
            if (manualMin != null && manualMax != null && manualMin.compareTo(manualMax) < 0) {
                minReading = manualMin;
                maxReading = manualMax;
            }
        }

        renderPowerChartBounds(minReading, maxReading);
        if (measurementCount < 2) return;

        renderPowerChartLines(minReading, maxReading, lastMeasurements, measurementCount);
    }

    private void renderGraphScaleIndicator() {
        if (!PowerGogglesConfigHandler.manualGraphScale) return;

        String indicator = "M";
        double scale = 0.5f;
        int right = xOffset + chartWidth;
        int top = screenHeight - yOffset - chartHeight - borderRadius * 2;
        int padding = 0;
        int x = right - padding - (int) (fontRenderer.getStringWidth(indicator) * scale);
        int y = top + padding;

        drawScaledString(indicator, x, y, PowerGogglesConfigHandler.chartManualScaleIndicatorColor, scale);
    }

    private BigInteger parseConfigBigInteger(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            BigInteger parsed = new BigInteger(value);
            return parsed.compareTo(BigInteger.ZERO) < 0 ? null : parsed;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private BigInteger getMinimumMeasurement(List<PowerGogglesMeasurement> lastMeasurements) {
        BigInteger minimum = null;
        for (PowerGogglesMeasurement measurement : lastMeasurements) {
            BigInteger value = measurement.getMeasurement();
            if (minimum == null || value.compareTo(minimum) < 0) {
                minimum = value;
            }
        }
        return minimum == null ? BigInteger.ZERO : minimum;
    }

    private BigInteger getMaximumMeasurement(List<PowerGogglesMeasurement> lastMeasurements) {
        BigInteger maximum = null;
        for (PowerGogglesMeasurement measurement : lastMeasurements) {
            BigInteger value = measurement.getMeasurement();
            if (maximum == null || value.compareTo(maximum) > 0) {
                maximum = value;
            }
        }
        return maximum == null ? BigInteger.ZERO : maximum;
    }

    private BigInteger getMinimumMeasurement(PowerGogglesMeasurement[] lastMeasurements, int count) {
        BigInteger minimum = null;
        for (int i = 0; i < count; i++) {
            BigInteger value = lastMeasurements[i].getMeasurement();
            if (minimum == null || value.compareTo(minimum) < 0) {
                minimum = value;
            }
        }
        return minimum == null ? BigInteger.ZERO : minimum;
    }

    private BigInteger getMaximumMeasurement(PowerGogglesMeasurement[] lastMeasurements, int count) {
        BigInteger maximum = null;
        for (int i = 0; i < count; i++) {
            BigInteger value = lastMeasurements[i].getMeasurement();
            if (maximum == null || value.compareTo(maximum) > 0) {
                maximum = value;
            }
        }
        return maximum == null ? BigInteger.ZERO : maximum;
    }

    private void renderPowerChartBackground() {

        int left = xOffset;
        int right = xOffset + chartWidth;
        int top = screenHeight - yOffset - chartHeight - borderRadius * 2;
        int bottom = screenHeight - yOffset - borderRadius * 2;
        int bgColor = PowerGogglesConfigHandler.chartBackgroundColor;
        GuiHelper.drawGradientRect(-1, left, top, right, bottom, bgColor, bgColor);
        int borderColor = PowerGogglesConfigHandler.chartBorderColor;
        GuiHelper.drawGradientRect(
            -2,
            left - borderRadius,
            top - borderRadius,
            right + borderRadius,
            bottom + borderRadius,
            borderColor,
            borderColor);
    }

    private void renderPowerChartBounds(BigInteger minReading, BigInteger maxReading) {
        double scale = 0.5f;
        String minText = cachedChartMinText
            .get(minReading, PowerGogglesConfigHandler.formatIndex, PowerGogglesUtil::format);
        drawScaledString(
            minText,
            xOffset,
            screenHeight - yOffset - borderRadius * 2 - (int) (fontRenderer.FONT_HEIGHT * scale),
            PowerGogglesConfigHandler.chartMinTextColor,
            scale);
        String maxText = minReading.equals(maxReading) ? ""
            : cachedChartMaxText.get(maxReading, PowerGogglesConfigHandler.formatIndex, PowerGogglesUtil::format);
        drawScaledString(
            maxText,
            xOffset,
            screenHeight - yOffset - borderRadius * 2 - chartHeight,
            PowerGogglesConfigHandler.chartMaxTextColor,
            scale);
    }

    private void renderPowerChartLines(BigInteger minReading, BigInteger maxReading,
        PowerGogglesMeasurement[] lastMeasurements, int measurementCount) {

        double completeChartLineWidth = chartWidth * 0.8d;
        int chartY = yOffset + borderRadius * 2;

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

        BigInteger lastMeasurement = lastMeasurements[0].getMeasurement();
        double lastX = xOffset + borderRadius + (chartWidth * 0.2d);
        double minReadingDouble = minReading.doubleValue();
        double maxReadingDouble = maxReading.doubleValue();
        double lastY = getPointY(chartY, chartHeight, minReadingDouble, maxReadingDouble, lastMeasurement);
        double lineWidth = completeChartLineWidth / PowerGogglesConstants.MEASUREMENT_COUNT_5M;

        for (int i = 1; i < measurementCount; i++) {

            BigInteger measurement = lastMeasurements[i].getMeasurement();
            setLineColor(tessellator, lastMeasurement, measurement);

            double currentX = lastX + lineWidth;
            double currentY = getPointY(chartY, chartHeight, minReadingDouble, maxReadingDouble, measurement);

            tessellator.addVertex(lastX, lastY, 0);
            tessellator.addVertex(currentX, currentY, 0);

            lastMeasurement = measurement;
            lastX = currentX;
            lastY = currentY;
        }

        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GL11.glPopAttrib();
    }

    private double getPointY(int chartY, int chartHeight, double minReading, double maxReading,
        BigInteger measurement) {
        if (maxReading <= minReading) {
            return screenHeight - chartY;
        }

        double value = measurement.doubleValue();
        if (value < minReading) {
            value = minReading;
        } else if (value > maxReading) {
            value = maxReading;
        }
        double heightPercentage = (value - minReading) / (maxReading - minReading);
        return screenHeight - (chartY + (chartHeight * heightPercentage));
    }

    private void setLineColor(Tessellator tessellator, BigInteger lastMeasurement, BigInteger measurement) {
        int negative = PowerGogglesConfigHandler.textBadColor;
        int positive = PowerGogglesConfigHandler.textGoodColor;

        if (measurement.compareTo(lastMeasurement) < 0) {
            tessellator.setColorRGBA(
                Color.getRed(negative),
                Color.getGreen(negative),
                Color.getBlue(negative),
                Color.getAlpha(negative));
        } else {
            tessellator.setColorRGBA(
                Color.getRed(positive),
                Color.getGreen(positive),
                Color.getBlue(positive),
                Color.getAlpha(positive));
        }
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
        update5mDifference();
        update1hDifference();
    }

    private void update5mDifference() {
        if (measurements.size() <= 1) {
            this.euDifference5m = BigInteger.ZERO;
            return;
        }

        getLastMeasurements(PowerGogglesConstants.MEASUREMENT_COUNT_5M);
        if (lastMeasurementsCount <= 1) {
            this.euDifference5m = BigInteger.ZERO;
            return;
        }
        BigInteger oldest = lastMeasurementsCache[0].getMeasurement();
        BigInteger newest = lastMeasurementsCache[lastMeasurementsCount - 1].getMeasurement();

        this.euDifference5m = newest.subtract(oldest);
    }

    private void update1hDifference() {
        if (measurements.size() <= 1) {
            this.euDifference1h = BigInteger.ZERO;
            return;
        }

        getLastMeasurements(PowerGogglesConstants.MEASUREMENT_COUNT_1H);
        if (lastMeasurementsCount <= 1) {
            this.euDifference1h = BigInteger.ZERO;
            return;
        }
        BigInteger oldest = lastMeasurementsCache[0].getMeasurement();
        BigInteger newest = lastMeasurementsCache[lastMeasurementsCount - 1].getMeasurement();

        this.euDifference1h = newest.subtract(oldest);
    }

    private PowerGogglesMeasurement[] getLastMeasurements(int count) {
        int stored = Math.min(count, Math.min(measurements.size(), lastMeasurementsCache.length));
        int skip = measurements.size() - stored;
        int index = stored;
        Iterator<PowerGogglesMeasurement> iterator = measurements.descendingIterator();
        while (index > 0) {
            PowerGogglesMeasurement measurement = iterator.next();
            if (skip > 0) {
                skip--;
                continue;
            }
            lastMeasurementsCache[--index] = measurement;
        }
        lastMeasurementsCount = stored;
        return lastMeasurementsCache;
    }

}
