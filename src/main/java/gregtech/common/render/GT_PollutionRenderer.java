package gregtech.common.render;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.common.GT_Pollution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;

@SideOnly(Side.CLIENT)
public class GT_PollutionRenderer {

    public GT_PollutionRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateDensity(EntityViewRenderEvent.FogDensity event) {
        double curr = getCurrentPollutionRenderRatio();
        if (event.density != 0.1f || curr > 0D) {
            float newDensity =  (float) (curr * (0.15D * getCurrentSourRainRenderRatio() + 0.1D));
            if (event.density != 0.1f)
                event.density = Math.max(event.density, newDensity);
            else
                event.density = newDensity;
            event.setCanceled(true);
        }
    }

    private static float curveActivateColor(float max) {
        double current = getCurrentPollutionRenderRatio() * max;
        return (float) Math.min(current, max) / 255f;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateColor(EntityViewRenderEvent.FogColors event) {
        event.red = curveActivateColor(140f);
        event.green = curveActivateColor(80f);
        event.blue = curveActivateColor(40f);
    }

    private static double getCurrentSourRainRenderRatio() {
        double player = GT_Pollution.mPlayerPollution;
        double limit = GT_Mod.gregtechproxy.mPollutionSourRainLimit;
        return Math.min(1D, Math.max(player/limit, 0D));
    }

    private static double getCurrentPollutionRenderRatio() {
        double player = GT_Pollution.mPlayerPollution;
        double limit = GT_Mod.gregtechproxy.mPollutionSmogLimit;
        return Math.min(1D, Math.max(player/limit, 0D));
    }

    private static int curveIntegerColor(int originalColor, int newColor) {
        short[] originalColorsSplit = splitColorToRBGArray(originalColor);
        short[] newColorsSplit = splitColorToRBGArray(newColor);
        double y = getCurrentPollutionRenderRatio();
        short[] color = {
                (short) ((1D-y) * originalColorsSplit[0] + y * newColorsSplit[0]),
                (short) ((1D-y) * originalColorsSplit[1] + y * newColorsSplit[1]),
                (short) ((1D-y) * originalColorsSplit[2] + y * newColorsSplit[2])
        };
        return ((color[0] & 0xff) << 16) | ((color[1] & 0xff) << 8) | (color[2] & 0xff);
    }

    public static short[] splitColorToRBGArray(int rgb) {
        return new short[]{(short) ((rgb >> 16) & 0xFF), (short) ((rgb >> 8) & 0xFF), (short) (rgb & 0xFF)};
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateGrassColor(BiomeEvent.GetGrassColor event) {
        event.newColor = curveIntegerColor(event.originalColor,0xD2691E);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateWaterColor(BiomeEvent.GetWaterColor event) {
        event.newColor = curveIntegerColor(event.originalColor,0x556B2F);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateFoliageColor(BiomeEvent.GetFoliageColor event) {
        event.newColor = curveIntegerColor(event.originalColor,0xCD853F);
    }

    private static int pollutionLastTick;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event){
        if(pollutionLastTick != GT_Pollution.mPlayerPollution && event.side == Side.CLIENT) {
            pollutionLastTick = GT_Pollution.mPlayerPollution;
            Minecraft mc = Minecraft.getMinecraft();
            EntityClientPlayerMP player = mc.thePlayer;
            double x = player.posX, z = player.posZ;

            //We want to overshoot here to really trigger new render, even on the farest away chunks!
            double renderDistanceBlocks = (mc.gameSettings.renderDistanceChunks + 2) * 16D;

            //May be a bit crude, but should do the job on *every* height.
            player.worldObj.markBlockRangeForRenderUpdate((int) (x - renderDistanceBlocks), 0, (int) (z - renderDistanceBlocks),
                    (int) (x + renderDistanceBlocks), 256, (int) (z + renderDistanceBlocks));
        }
    }
}
