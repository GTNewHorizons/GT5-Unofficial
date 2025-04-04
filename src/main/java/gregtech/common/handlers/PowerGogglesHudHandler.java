package gregtech.common.handlers;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINES;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import com.google.common.math.BigIntegerMath;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Color;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PowerGogglesHudHandler {

    public static boolean updateClient = false;
    private final static int TICKS = 1;
    private final static int SECONDS = 20 * TICKS;
    private final static int MINUTES = 60 * SECONDS;
    static List<Text> hudList = new ArrayList<>();
    static LinkedList<BigInteger> measurements = new LinkedList<>();
    static Minecraft mc = Minecraft.getMinecraft();
    public static final int ticksBetweenMeasurements = 100;
    static final int measurementCount5m = 5 * MINUTES / ticksBetweenMeasurements;
    static final int measurementCount1h = 60 * MINUTES / ticksBetweenMeasurements;
    static BigInteger currentEU = BigInteger.valueOf(0);
    static BigInteger measurement = BigInteger.valueOf(0);
    static BigInteger highest = BigInteger.valueOf(0);
    static int measurementCount = 0;
    static int change5mColor;
    static int change1hColor;

    static String storage = "";
    static String change5mString = "";
    static String change1hString = "";
    static BigInteger change5m = BigInteger.valueOf(0);
    static BigInteger change1h = BigInteger.valueOf(0);
    static int change5mDiff;
    static int change1hDiff;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void drawHUD(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL || mc.gameSettings.showDebugInfo
            || mc.currentScreen instanceof GuiChat) return;

        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(mc.thePlayer);
        boolean gogglesEquipped = false;
        for (int i = 0; i < baubles.getSizeInventory(); i++) {
            ItemStack bauble = baubles.getStackInSlot(i);
            if (bauble == null) continue;
            if (baubles.getStackInSlot(i)
                .getUnlocalizedName()
                .equals("gt.PowerNerd_Goggles")) gogglesEquipped = true;
        }
        if (!gogglesEquipped) return;
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
        int chartOffsetY = drawPowerRectangle(xOffset, yOffset, h, w, screenHeight, borderRadius);

        if (PowerGogglesConfigHandler.showPowerChart) {
            drawPowerChart(xOffset, chartOffsetY - borderRadius, 100, 100, screenHeight, screenWidth, borderRadius);
        }
        GL11.glPopMatrix();
    }

    private static int drawPowerRectangle(int xOffset, int yOffset, int w, int h, int screenHeight, int borderRadius) {
        int left = h + xOffset;
        int up = screenHeight - h - yOffset;
        int right = left + w;
        int down = up + h;

        GL11.glPushMatrix();
        GL11.glTranslated(left, down, 0);
        GL11.glRotated(90, 0, 0, -1);
        GL11.glTranslated(-left, -down, 0);

        double scale = 3.3;
        double severity = measurement.compareTo(BigInteger.ZERO) == 0 ? 0
            : new BigDecimal(change5m.multiply(BigInteger.valueOf(100)))
                .divide(new BigDecimal(measurement), RoundingMode.FLOOR)
                .intValue() / 100f;

        java.awt.Color[] gradientSet = getGradientSet(PowerGogglesConfigHandler.gradientIndex);
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

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int gapBetweenLines = 2;
        double mainScale = PowerGogglesConfigHandler.mainTextScaling;
        double subScale = PowerGogglesConfigHandler.subTextScaling;
        int bgColor = Color.argb(47, 20, 76, (int) (255 * 0.85));
        int highestPoint = screenHeight - yOffset
            - w
            - gapBetweenLines
            - (int) (fontRenderer.FONT_HEIGHT * mainScale)
            - borderRadius;
        GuiHelper.drawGradientRect(
            -1,
            xOffset - borderRadius,
            highestPoint,
            xOffset + h + borderRadius,
            screenHeight - yOffset
                + gapBetweenLines * 2
                + (int) (fontRenderer.FONT_HEIGHT * 2 * subScale)
                + borderRadius,
            bgColor,
            bgColor);

        storage = toFormatted(currentEU) + " EU";
        change5mString = "5m: " + toFormatted(change5m)
            + " EU "
            + (change5mDiff != 0
                ? String.format(
                    " (%s eu/t) ",
                    toFormatted(
                        change5m.divide(
                            BigInteger.valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 5 * MINUTES)))))
                : "");
        change1hString = "1h: " + toFormatted(change1h)
            + " EU "
            + (change1hDiff != 0 ? String.format(
                " (%s eu/t)",
                toFormatted(
                    change1h.divide(
                        BigInteger.valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 60 * MINUTES)))))
                : "");
        switch (PowerGogglesConfigHandler.readingIndex) {
            case 0:
                break;
            case 1:
                change5mString = "5m: " + toFormatted(change5m) + " EU";
                change1hString = "1h: " + toFormatted(change1h) + " EU";
                break;
            case 2:
                change5mString = "5m: " + (change5mDiff != 0 ? String.format(
                    " (%s EU/t) ",
                    toFormatted(
                        change5m.divide(
                            BigInteger.valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 5 * MINUTES)))))
                    : "0 EU/t");
                change1hString = "1h: " + (change1hDiff != 0
                    ? String.format(
                        " (%s EU/t)",
                        toFormatted(
                            change1h.divide(
                                BigInteger
                                    .valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 60 * MINUTES)))))
                    : "0 EU/t");
                break;
            default:
                break;
        }
        drawScaledString(
            fontRenderer,
            storage,
            xOffset,
            screenHeight - yOffset - w - gapBetweenLines - (int) (fontRenderer.FONT_HEIGHT * mainScale),
            change5mColor,
            mainScale);
        drawScaledString(
            fontRenderer,
            change5mString,
            xOffset,
            screenHeight - yOffset + gapBetweenLines,
            change5mColor,
            subScale);
        drawScaledString(
            fontRenderer,
            change1hString,
            xOffset,
            screenHeight - yOffset + gapBetweenLines * 2 + (int) (fontRenderer.FONT_HEIGHT * subScale),
            change1hColor,
            subScale);
        return highestPoint;
    }

    private void drawPowerChart(int xOffset, int yOffset, int chartWidth, int chartHeight, int screenHeight,
        int screenWidth, int borderRadius) {
        int readings = Math.min(measurements.size(), measurementCount5m);
        int left = xOffset;
        int right = xOffset + chartWidth;
        int top = yOffset - chartHeight;
        int bottom = yOffset;
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
        if (measurements.isEmpty()) return;
        BigInteger minReading = measurements.get(0);
        BigInteger maxReading = measurements.get(0);
        for (int i = 0; i < readings; i++) {
            BigInteger temp = measurements.get(i);
            if (temp.compareTo(minReading) < 0) minReading = temp;
            if (maxReading.compareTo(temp) < 0) maxReading = temp;
        }
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        double scale = 0.5f;
        drawScaledString(
            fontRenderer,
            toFormatted(minReading),
            xOffset,
            yOffset - (int) (fontRenderer.FONT_HEIGHT * scale),
            Color.rgb(237, 2, 158),
            scale);
        drawScaledString(
            fontRenderer,
            minReading.compareTo(maxReading) == 0 ? "" : toFormatted(maxReading),
            xOffset,
            yOffset - chartHeight,
            Color.rgb(237, 2, 158),
            scale);
        if (readings < 2) return;
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

        BigInteger lastReading = measurements.get(0);
        double pointsWidth = chartWidth * 0.8d;
        double lastX = xOffset + chartWidth;
        double lastY = (yOffset + (chartHeight * (-1 + Math.min(
            1,
            1 - lastReading.subtract(minReading)
                .floatValue()
                / maxReading.subtract(minReading)
                    .floatValue()))));
        for (int i = 1; i < readings; i++) {
            BigInteger reading = measurements.get(i);
            double x = xOffset + chartWidth - (pointsWidth / (readings)) * i;
            double y = (yOffset + (chartHeight * (-1 + Math.min(
                1,
                1 - reading.subtract(minReading)
                    .floatValue()
                    / maxReading.subtract(minReading)
                        .floatValue()))));
            if (reading.compareTo(lastReading) > 0) {
                tessellator.setColorRGBA_F(255, 0, 0, 255);
            } else {
                tessellator.setColorRGBA_F(0, 255, 0, 255);
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

    }

    private static void drawScaledString(FontRenderer fontRenderer, String string, int xOffset, int yOffset, int color,
        double scale) {
        GL11.glPushMatrix();
        GL11.glTranslated(xOffset, yOffset, 0);
        GL11.glScaled(scale, scale, 1);
        GL11.glTranslated(-xOffset, -yOffset, 0);
        fontRenderer.drawStringWithShadow(string, xOffset, yOffset, color);
        GL11.glPopMatrix();
    }

    public static void setMeasurement(BigInteger newEU) {
        measurement = newEU;
        if (highest.compareTo(measurement) < 0) highest = measurement;
        currentEU = measurement;
        measurements.addFirst(measurement);
        if (measurements.size() > measurementCount1h) measurements.removeLast();
        ++measurementCount;
    }

    public static int[] getGradient(double severity, double scale, java.awt.Color gradientLeft,
        java.awt.Color gradientRight) {
        int newGradientLeft = gradientLeft.getRGB();
        int newGradientRight = gradientRight.getRGB();

        int diffRed = gradientLeft.getRed() - gradientRight.getRed();
        int diffGreen = gradientLeft.getGreen() - gradientRight.getGreen();
        int diffBlue = gradientLeft.getBlue() - gradientRight.getBlue();

        int newLeftRed = Math
            .min(255, Math.max(0, gradientRight.getRed() + (int) (diffRed * Math.min(1, severity * scale))));
        int newLeftGreen = Math
            .min(255, Math.max(0, gradientRight.getGreen() + (int) (diffGreen * Math.min(1, severity * scale))));
        int newLeftBlue = Math
            .min(255, Math.max(0, gradientRight.getBlue() + (int) (diffBlue * Math.min(1, severity * scale))));

        int newRightRed = Math
            .min(255, Math.max(0, gradientRight.getRed() + (int) (diffRed * Math.min(1, severity * scale * 0.75))));
        int newRightGreen = Math
            .min(255, Math.max(0, gradientRight.getGreen() + (int) (diffGreen * Math.min(1, severity * scale * 0.75))));
        int newRightBlue = Math
            .min(255, Math.max(0, gradientRight.getBlue() + (int) (diffBlue * Math.min(1, severity * scale * 0.75))));

        newGradientLeft = Color.rgb(newLeftRed, newLeftGreen, newLeftBlue);
        newGradientRight = Color.rgb(newRightRed, newRightGreen, newRightBlue);

        return new int[] { newGradientLeft, newGradientRight };
    }

    private static java.awt.Color[] getGradientSet(int index) {
        switch (index) {
            case 1:
                return new java.awt.Color[] { new java.awt.Color(255, 25, 134), new java.awt.Color(229, 200, 0),
                    new java.awt.Color(11, 165, 255) };
            default:
                return new java.awt.Color[] { new java.awt.Color(255, 50, 50), new java.awt.Color(50, 255, 50),
                    new java.awt.Color(50, 50, 255) };
        }
    }

    @SideOnly(Side.CLIENT)
    public static void drawTick() {
        updateClient = false;
        if (Minecraft.getMinecraft()
            .isGamePaused()) return;

        change5m = getTotalChange(measurements, measurementCount5m);
        change5mDiff = change5m.compareTo(BigInteger.valueOf(0));
        change5mColor = getColor(change5mDiff);

        change1h = getTotalChange(measurements, measurementCount1h);
        change1hDiff = change1h.compareTo(BigInteger.valueOf(0));
        change1hColor = getColor(change5mDiff);

        hudList = new ArrayList<>();
        storage = toFormatted(currentEU) + " EU";
        change5mString = "5m: " + toFormatted(change5m)
            + " EU "
            + (change5mDiff != 0
                ? String.format(
                    " (%s eu/t) ",
                    toFormatted(
                        change5m.divide(
                            BigInteger.valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 5 * MINUTES)))))
                : "");
        change1hString = "1h: " + toFormatted(change1h)
            + " EU "
            + (change1hDiff != 0 ? String.format(
                " (%s eu/t)",
                toFormatted(
                    change1h.divide(
                        BigInteger.valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 60 * MINUTES)))))
                : "");

    }

    private static String toFormatted(BigInteger EU) {
        switch (PowerGogglesConfigHandler.formatIndex) {
            case 1:
                return toCustom(EU);
            case 2:
                return toCustom(EU, true, 3);
            default:
                return toCustom(EU, false, 1);
        }

    }

    private static String toCustom(BigInteger EU) {
        return toCustom(EU, false, 3);
    }

    private static String toCustom(BigInteger EU, boolean overrideEngineering, int baseDigits) {
        String[] suffixes = { "", "K", "M", "G", "T", "P", "E", "Z", "Y", "R", "Q" };
        if (EU.abs()
            .compareTo(BigInteger.valueOf(1)) < 0) {
            return "0";
        }
        int exponent = BigIntegerMath.log10(EU.abs(), RoundingMode.FLOOR);
        int remainder = exponent % baseDigits;

        String euString = EU.toString();
        if (EU.abs()
            .compareTo(BigInteger.valueOf(1000)) < 0) {
            return euString;
        }
        int negative = EU.compareTo(BigInteger.valueOf(1)) < 0 ? 1 : 0;
        String base = euString.substring(0, remainder + 1 + negative);
        String decimal = euString.substring(remainder + 1 + negative, Math.min(exponent, remainder + 4));
        int E = exponent - remainder; // Round down to nearest 10^3k

        if (overrideEngineering) return String.format("%s.%s%s", base, decimal, suffixes[E / 3]);
        return String.format("%s.%sE%d", base, decimal, E);
    }

    private static BigInteger getTotalChange(LinkedList<BigInteger> list, int count) {
        if (list.isEmpty()) return BigInteger.valueOf(0);
        return list.get(0)
            .subtract(list.get(Math.min(count, list.size() - 1)));
    }

    private static int getColor(int compareResult) {
        if (compareResult == 0) return Color.rgb(255, 255, 255);
        if (compareResult < 0) return Color.rgb(255, 0, 0);
        return Color.rgb(0, 255, 0);
    }

    public static void clear() {
        measurements.clear();
        measurementCount = 0;
        highest = BigInteger.valueOf(0);
    }
}
