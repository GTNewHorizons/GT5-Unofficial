package gregtech.common.handlers;

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
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import com.google.common.math.BigIntegerMath;
import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PowerGogglesHudHandler {
    public static boolean updateClient = false;
    private final static int TICKS = 1;
    private final static int SECONDS = 20*TICKS;
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
    static int measurementCount = 0;

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
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        int factor = resolution.getScaleFactor();
        int xOffset = 0;
        int x = -5;
        int yOffset = 20;
        int y = (height - yOffset);

        FontRenderer fontRenderer = mc.fontRenderer;
        GL11.glPushMatrix();
        GuiHelper.drawHoveringText(
            hudList,
            new Pos2d(x, y),
            new Size(150, 60),
            150,
            0.75f,
            true,
            Alignment.CenterLeft,
            false);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public static void setMeasurement(BigInteger newEU) {
        measurement = newEU;
        lastChange = measurementCount <= 1 ? BigInteger.valueOf(0) : measurement.subtract(currentEU);
        currentEU = measurement;
    }

    @SideOnly(Side.CLIENT)
    public static void drawTick() {
        updateClient = false;
        if (Minecraft.getMinecraft()
            .isGamePaused()) return;
        int lastChangeDiff = lastChange.compareTo(BigInteger.valueOf(0));
        EnumChatFormatting changeColor = getColor(lastChangeDiff);

        measurements.addFirst(lastChange);
        if (measurements.size() > measurementCount1h) measurements.removeLast();

        BigInteger change5m = getReadingSum(measurements, measurementCount5m);
        int change5mDiff = change5m.compareTo(BigInteger.valueOf(0));
        EnumChatFormatting change5mColor = getColor(change5mDiff);

        BigInteger change1h = getReadingSum(measurements, measurementCount1h);
        int change1hDiff = change1h.compareTo(BigInteger.valueOf(0));
        EnumChatFormatting change1hColor = getColor(change5mDiff);

        hudList = new ArrayList<>();
        ++measurementCount;
        hudList
            .add(new Text(EnumChatFormatting.WHITE + "Storage: " + change5mColor + toEngineering(currentEU) + " EU"));
        hudList.add(
            new Text(
                EnumChatFormatting.WHITE + "5s: "
                    + changeColor
                    + toEngineering(lastChange)
                    + " EU"
                    + (lastChangeDiff != 0 ? String.format(
                        " (%s eu/t) ",
                        toEngineering(lastChange.divide(BigInteger.valueOf(ticksBetweenMeasurements)))) : "")));
        hudList.add(
            new Text(
                EnumChatFormatting.WHITE + "5m: "
                    + change5mColor
                    + toEngineering(change5m)
                    + " EU"
                    + (change5mDiff != 0
                        ? String.format(" (%s eu/t) ", toEngineering(change5m.divide(BigInteger.valueOf(5 * MINUTES))))
                        : "")));
        hudList.add(
            new Text(
                EnumChatFormatting.WHITE + "1h: "
                    + change1hColor
                    + toEngineering(change1h)
                    + " EU"
                    + (change1hDiff != 0
                        ? String.format(" (%s eu/t) ", toEngineering(change1h.divide(BigInteger.valueOf(60 * MINUTES))))
                        : "")));

    }

    private static String toEngineering(BigInteger EU) {
        if (EU.abs()
            .compareTo(BigInteger.valueOf(1)) < 0) {
            return "0";
        }
        int exponent = BigIntegerMath.log10(EU.abs(), RoundingMode.FLOOR);
        int remainder = exponent % 3;

        String euString = EU.toString();
        if (EU.abs()
            .compareTo(BigInteger.valueOf(1000)) < 0) {
            return euString;
        }
        int negative = EU.compareTo(BigInteger.valueOf(1)) < 0 ? 1 : 0;
        String base = euString.substring(0, remainder + 1 + negative);
        String decimal = euString.substring(remainder + 1 + negative, Math.min(exponent, remainder + 4));
        int E = exponent - remainder; // Round down to nearest 10^3k
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

    private static EnumChatFormatting getColor(int compareResult) {
        if (compareResult == 0) return EnumChatFormatting.WHITE;
        if (compareResult < 0) return EnumChatFormatting.RED;
        return EnumChatFormatting.GREEN;
    }
    public static void clear(){
        measurements.clear();
        lastChange = BigInteger.valueOf(0);
        measurementCount = 0;
    }
}
