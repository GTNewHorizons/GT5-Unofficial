package gregtech.common.handlers;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import com.cleanroommc.modularui.utils.Color;
import com.google.common.math.BigIntegerMath;
import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.misc.WirelessNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PowerGogglesHudHandler {
    static List<Text> hudList = new ArrayList<>();
    static Minecraft mc = Minecraft.getMinecraft();
    static int ticks = 0;
    static final int ticksBetweenMeasurements = 100;
    static BigInteger currentEU = BigInteger.valueOf(0);
    static BigInteger lastChange = BigInteger.valueOf(0);
    static BigInteger measurement = BigInteger.valueOf(0);
    static int measurements = 0;


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void drawHUD(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL || mc.gameSettings.showDebugInfo
            || mc.currentScreen instanceof GuiChat)
            return;

        InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(mc.thePlayer);
        boolean gogglesEquipped = false;
        for(int i = 0; i < baubles.getSizeInventory(); i++){
            ItemStack bauble = baubles.getStackInSlot(i);
            if(bauble == null) continue;
            if(baubles.getStackInSlot(i).getUnlocalizedName().equals("gt.PowerNerd_Goggles")) gogglesEquipped = true;
        }
        if(!gogglesEquipped) return;
        ScaledResolution resolution = event.resolution;
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();
        int xOffset = 0;
        int x = 0;
        int yOffset = 30;
        int y = height - yOffset;

        FontRenderer fontRenderer = mc.fontRenderer;
        GL11.glPushMatrix();
        GuiHelper.drawHoveringText(hudList, new Pos2d(x, y), new Size(width, height), width, 90/100F, true, Alignment.CenterLeft, false);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
    @SideOnly(Side.CLIENT)
    public static void clientTick() {
        if(ticks % ticksBetweenMeasurements == 0){
            measurement = WirelessNetworkManager.getUserEU(mc.thePlayer.getUniqueID());
            lastChange = measurements == 0 ? BigInteger.valueOf(0) : measurement.subtract(currentEU);
            currentEU = measurement;
            EnumChatFormatting changeColor = EnumChatFormatting.WHITE;
            int lastChangeDiff = lastChange.compareTo(BigInteger.valueOf(0));



            hudList = new ArrayList<>();
            if(measurements > 0){
                if(lastChangeDiff < 0) {
                    changeColor = EnumChatFormatting.RED;
                } else if(lastChangeDiff > 0) {
                    changeColor = EnumChatFormatting.GREEN;
                }
            }
            ++measurements;
            hudList.add(new Text(EnumChatFormatting.WHITE+"Wireless EU: " + changeColor+toEngineering(currentEU)));
            hudList.add(new Text(EnumChatFormatting.WHITE+"5s: " + changeColor+toEngineering(lastChange) + (lastChangeDiff == 0 ? String.format(" (%s eu/t) ",toEngineering(lastChange.divide(BigInteger.valueOf(ticksBetweenMeasurements)))) : "")));

        }
        ++ticks;
    }
    public static String toEngineering(BigInteger EU){
        if(EU.abs().compareTo(BigInteger.valueOf(1)) < 0){
            return "0";
        }
        int exponent = BigIntegerMath.log10(EU.abs(), RoundingMode.FLOOR);
        int remainder = exponent % 3;

        String euString = EU.toString();
        if(EU.abs().compareTo(BigInteger.valueOf(1000)) < 0){
            return euString;
        }
        int negative = EU.compareTo(BigInteger.valueOf(1)) < 0 ? 1 : 0;
        String base = euString.substring(0,remainder+1+negative);
        String decimal = euString.substring(remainder+1+negative,Math.min(exponent, remainder+4));
        int E = exponent-remainder; //Round down to nearest 10^3k
        return String.format("%s.%sE%d",base, decimal, E);
    }
}
