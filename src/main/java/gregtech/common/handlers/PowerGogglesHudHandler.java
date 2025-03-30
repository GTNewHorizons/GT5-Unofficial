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
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PowerGogglesHudHandler {
    static List<Text> hudList = new ArrayList<>();
    static Minecraft mc = Minecraft.getMinecraft();

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


        GL11.glPopMatrix();
    }
    @SideOnly(Side.CLIENT)
    public static void clientTick() {
        hudList = new ArrayList<>();
        hudList.add(new Text(toEngineering(WirelessNetworkManager.getUserEU(mc.thePlayer.getUniqueID()))).color(Color.rgb(255,255,255)));
    }
    public static String toEngineering(BigInteger EU){
        String euString = EU.toString();
        return String.format("%sE%d",euString.substring(0,2), euString.length()/3);
    }
}
