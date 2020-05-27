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
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GT_PollutionRenderer {

    public GT_PollutionRenderer() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderGTPollutionFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            return;

        float sourRain = getCurrentSourRainRenderRatio();
        if (sourRain > 0D) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, sourRain);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateColor(EntityViewRenderEvent.FogColors event) {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            return;
        float[] nucolor = curveColorArray(
                new float[]{
                        event.red,
                        event.green,
                        event.blue
                },
                new float[]{
                        0.375f,  //0.546875f,
                        0.3125f, //0.3125f,
                        0.1484f  //0.15625f
                }
        );
        event.red = nucolor[0];
        event.green = nucolor[1];
        event.blue = nucolor[2];
    }

    private static float getCurrentSourRainRenderRatio() {
        float player = GT_Pollution.mPlayerPollution;
        float limit = GT_Mod.gregtechproxy.mPollutionSourRainLimit;
        return Math.min(1f, Math.max(player/limit, 0f));
    }

    private static double getCurrentPollutionRenderRatio() {
        double player = GT_Pollution.mPlayerPollution;
        double limit = GT_Mod.gregtechproxy.mPollutionSmogLimit;
        return Math.min(1D, Math.max(player/limit, 0D));
    }

    private static short[] curveColorArray(short[] originalColorsSplit, short[] newColorsSplit){
        double y = getCurrentPollutionRenderRatio();
        return new short[] {
                (short) ((1D-y) * (double) originalColorsSplit[0] + y * (double) newColorsSplit[0]),
                (short) ((1D-y) * (double) originalColorsSplit[1] + y * (double) newColorsSplit[1]),
                (short) ((1D-y) * (double) originalColorsSplit[2] + y * (double) newColorsSplit[2])
        };
    }

    private static float[] curveColorArray(float[] originalColorsSplit, float[] newColorsSplit){
        double y = getCurrentPollutionRenderRatio();
        return new float[] {
                (float) ((1D-y) * (double) originalColorsSplit[0] + y * (double) newColorsSplit[0]),
                (float) ((1D-y) * (double) originalColorsSplit[1] + y * (double) newColorsSplit[1]),
                (float) ((1D-y) * (double) originalColorsSplit[2] + y * (double) newColorsSplit[2])
        };
    }

    private static int curveIntegerColor(int originalColor, int newColor) {
        return makeIntColorFromRBGArray(
                curveColorArray(
                        splitColorToRBGArray(originalColor),
                        splitColorToRBGArray(newColor)
                )
        );
    }

    public static int makeIntColorFromRBGArray(short[] color) {
        return ((color[0] & 0xff) << 16) | ((color[1] & 0xff) << 8) | (color[2] & 0xff);
    }

    public static short[] splitColorToRBGArray(int rgb) {
        return new short[]{(short) ((rgb >> 16) & 0xFF), (short) ((rgb >> 8) & 0xFF), (short) (rgb & 0xFF)};
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateGrassColor(BiomeEvent.GetGrassColor event) {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            return;
        event.newColor = curveIntegerColor(event.originalColor,0xD2691E);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateWaterColor(BiomeEvent.GetWaterColor event) {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            return;
        event.newColor = curveIntegerColor(event.originalColor,0x556B2F);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void manipulateFoliageColor(BiomeEvent.GetFoliageColor event) {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            return;
        event.newColor = curveIntegerColor(event.originalColor,0xCD853F);
    }

    private static int pollutionLastTick;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event){
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null)
            return;
        EntityClientPlayerMP player = mc.thePlayer;
        if (player == null || player.capabilities == null || player.capabilities.isCreativeMode)
            return;
        if(pollutionLastTick != GT_Pollution.mPlayerPollution && event.side == Side.CLIENT) {
            pollutionLastTick = GT_Pollution.mPlayerPollution;
            double x = player.posX,
                   z = player.posZ;

            int renderDistanceBlocks = pollutionLastTick > GT_Mod.gregtechproxy.mPollutionSmogLimit ?
                    25 :
                    (mc.gameSettings.renderDistanceChunks + 2) * 16;

            //May be a bit crude, but should do the job on *every* height.
            player.worldObj.markBlockRangeForRenderUpdate((int) (x - renderDistanceBlocks), 0, (int) (z - renderDistanceBlocks),
                    (int) (x + renderDistanceBlocks), 256, (int) (z + renderDistanceBlocks));
        }
    }
}
