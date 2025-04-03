package gregtech.common.handlers;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;

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
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import com.google.common.math.BigIntegerMath;
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
    static BigInteger lastChange = BigInteger.valueOf(0);
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
        // if (!gogglesEquipped) return;
        ScaledResolution resolution = event.resolution;
        int screenHeight = resolution.getScaledHeight();

        FontRenderer fontRenderer = mc.fontRenderer;
        GL11.glPushMatrix();
        GL11.glEnable(GL_CULL_FACE);
        // GuiHelper.drawHoveringText(
        // hudList,
        // new Pos2d(x, y),
        // new Size(150, 45),
        // 150,
        // 0.75f,
        // false,
        // Alignment.CenterLeft,
        // false);

        int xOffset = PowerGogglesConfigHandler.mainOffsetX;
        int yOffset = PowerGogglesConfigHandler.mainOffsetY;
        int w = PowerGogglesConfigHandler.rectangleWidth;
        int h = PowerGogglesConfigHandler.rectangleHeight;
        drawPowerRectangle(xOffset, yOffset, h, w, screenHeight);
        GL11.glPopMatrix();
    }

    private static void drawPowerRectangle(int xOffset, int yOffset, int w, int h, int screenHeight) {
        int left = h + xOffset;
        int up = screenHeight - h - yOffset;
        int right = left + w;
        int down = up + h;

        int gradientLeft;
        int gradientRight = Color.rgb(0, 255, 0);
        if (change5m.compareTo(BigInteger.ZERO) >= 0) {
            gradientLeft = Color.rgb(0, 255, 0);
        } else {
            double scale = 100d / 33d;
            double severity = measurement.compareTo(BigInteger.ZERO) == 0 ? 1
                : Math.min(
                    1,
                    Math.abs(
                        new BigDecimal(change5m.multiply(BigInteger.valueOf(100)))
                            .divide(new BigDecimal(measurement), RoundingMode.FLOOR)
                            .intValue() / 100f));
            int gradientFactor = (int) (255 * (severity));
            gradientLeft = Color.rgb(255, Math.min(255, Math.max(0, 255 - (int) (gradientFactor * scale))), 0);
            gradientRight = Color.rgb(
                Math.min(255, (int) (gradientFactor * 1.5f * scale)),
                Math.min(255, Math.max(0, 255 - (int) (gradientFactor * Math.sqrt(severity) * scale))),
                0); // Calculation done by trial and error until it looked decent in-game
        }

        GL11.glPushMatrix();
        GL11.glTranslated(left, down, 0);
        GL11.glRotated(90, 0, 0, -1);
        GL11.glTranslated(-left, -down, 0);
        GuiHelper.drawGradientRect(300, left, up, right, down, gradientLeft, gradientRight);
        GL11.glDisable(GL_LIGHTING);
        GL11.glPopMatrix();

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int gapBetweenLines = 2;
        double mainScale = PowerGogglesConfigHandler.mainTextScaling;
        double subScale = PowerGogglesConfigHandler.subTextScaling;
        int borderRadius = 3;
        int bgColor = Color.argb(47, 20, 76, (int) (255 * 0.85));
        GuiHelper.drawGradientRect(
            -1,
            xOffset - borderRadius,
            screenHeight - yOffset - w - gapBetweenLines - fontRenderer.FONT_HEIGHT - borderRadius,
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
        int mode = 0;
        switch (mode) {
            case 0:
                break;
            case 1:
                change5mString = "5m: " + toFormatted(change5m);
                change1hString = "1h: " + toFormatted(change1h);
                break;
            case 2:
                change5mString = "5m: " + (change5mDiff != 0 ? String.format(
                    " (%s eu/t) ",
                    toFormatted(
                        change5m.divide(
                            BigInteger.valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 5 * MINUTES)))))
                    : "0");
                change1hString = "1h: " + (change1hDiff != 0
                    ? String.format(
                        " (%s eu/t)",
                        toFormatted(
                            change1h.divide(
                                BigInteger
                                    .valueOf(Math.min(measurements.size() * ticksBetweenMeasurements, 60 * MINUTES)))))
                    : "0");
                break;
            default:
                break;
        }
        drawScaledString(
            fontRenderer,
            storage,
            xOffset,
            screenHeight - yOffset - w - gapBetweenLines - fontRenderer.FONT_HEIGHT,
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
        lastChange = measurementCount == 0 ? BigInteger.valueOf(0) : measurement.subtract(currentEU);
        currentEU = measurement;
        if (measurementCount > 0) measurements.addFirst(lastChange);
        if (measurements.size() > measurementCount1h) measurements.removeLast();
        ++measurementCount;
    }

    @SideOnly(Side.CLIENT)
    public static void drawTick() {
        updateClient = false;
        if (Minecraft.getMinecraft()
            .isGamePaused()) return;

        change5m = getReadingSum(measurements, measurementCount5m);
        change5mDiff = change5m.compareTo(BigInteger.valueOf(0));
        change5mColor = getColor(change5mDiff);

        change1h = getReadingSum(measurements, measurementCount1h);
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
        switch (PowerGogglesConfigHandler.numberFormatting) {
            case "ENGINEERING":
                return toCustom(EU);
            case "SI":
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

    private static BigInteger getReadingSum(LinkedList<BigInteger> list, int count) {
        BigInteger result = BigInteger.ZERO;
        int actualCount = Math.min(list.size(), count);
        for (int i = 0; i < actualCount; i++) {
            result = result.add(list.get(i));
        }
        return result;
    }

    private static int getColor(int compareResult) {
        if (compareResult == 0) return Color.rgb(255, 255, 255);
        if (compareResult < 0) return Color.rgb(255, 0, 0);
        return Color.rgb(0, 255, 0);
    }

    public static void clear() {
        measurements.clear();
        lastChange = BigInteger.valueOf(0);
        measurementCount = 0;
        highest = BigInteger.valueOf(0);
    }
}
