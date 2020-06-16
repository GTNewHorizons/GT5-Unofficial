package gregtech.common.render;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.misc.GT_ClientPollutionMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GT_PollutionRenderer {
    private static GT_ClientPollutionMap pollutionMap;
    private int minVal, maxVal;

    public GT_PollutionRenderer() {
        pollutionMap = new GT_ClientPollutionMap();
    }

    public void preLoad() {
        minVal = 350000;
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public void processPacket(ChunkCoordIntPair chunk, int pollution) {
        //int prev = pollutionMap.getChunkPollution(chunk.chunkXPos, chunk.chunkZPos);
        pollutionMap.addChunkPollution(chunk.chunkXPos, chunk.chunkZPos, pollution);
        //if (Math.abs(prev-pollution) > 1000) {
        //    Minecraft.getMinecraft().thePlayer.worldObj.markBlockRangeForRenderUpdate();
        //} // TODO this in a smart way somehow.
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void enteredWorld(WorldEvent.Load asd) {
        EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;
        if (!asd.world.isRemote || p == null)
            return;
        pollutionMap.reset();
    }

    private static int color(int color, int pollution, int low, float hi, short[] colors) {
        if ( pollution < low)
            return color;

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        float p = (pollution - low) / hi;
        if (p > 1) p = 1;
        float pi = 1 - p;

        r = ((int) (r * pi + p * colors[0])) & 0xFF;
        g = ((int) (g * pi + p * colors[1])) & 0xFF;
        b = ((int) (b * pi + p * colors[2])) & 0xFF;

        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    private static final short[] grassColors = {250, 200, 40};
    public static int colorGrass(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z)/1000, 250, 600, grassColors);
    }

    private static final short[] leavesColor = {160, 80, 15};
    public static int colorLeaves(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z)/1000, 200, 500, leavesColor);
    }

    private static final short[] liquidColor = {160, 200, 10};
    public static int colorLiquid(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z)/1000, 200, 500, liquidColor);
    }

    private static final short[] foliageColor = {160, 80, 15};
    public static int colorFoliage(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z)/1000, 200, 500, foliageColor);
    }

    private final float[] fogColor = {0.375f, 0.3125f, 0.1484f};
    @SubscribeEvent(priority = EventPriority.LOW)
    public void manipulateColor(EntityViewRenderEvent.FogColors event) {
        if (false && Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            return;
        float x = (float) lastEnd;
        if (x>1) x=1;
        float xi = 1 - x;
        event.red = x*event.red + xi*fogColor[0];
        event.green = x*event.green + xi*fogColor[1];
        event.blue = x*event.blue + xi*fogColor[2];
    }

    private static final int END_MIN_DISTANCE = 1;
    private static final int END_MAX_DISTANCE = 192 - END_MIN_DISTANCE;

    private static double lastStart = 1;
    private static double lastEnd = 1;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderGTPollutionFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (false && Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            return;
        int pollution = pollutionMap.getPollution(Minecraft.getMinecraft().thePlayer.lastTickPosX, Minecraft.getMinecraft().thePlayer.lastTickPosZ);

        if ( event.fogMode == 0 && (pollution > minVal || (lastEnd + lastStart) < 2)) {
            float st = (1-(pollution-minVal)/150000F);
            if (st > 1) st = 1;
            else if (st < 0) st = 0;

            double e = st-lastStart;
            if (e != 0) {
                if (e > 0.001 || e < -0.001)
                    lastStart += step*e;
                else
                    lastStart = st;
            }

            float end = (1-(pollution-minVal)/1500000F);
            if (end > 1) end = 1;
            else if (end < 0) end = 0;
            end *= end * end;

            e = end - lastEnd;
            if (e != 0) {
                if (e > 0.001 || e < -0.001)
                    lastEnd += step*e;
                else
                    lastEnd = end;
            }

            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, (float) (END_MAX_DISTANCE * lastStart * 0.75F));
            GL11.glFogf(GL11.GL_FOG_END, (float) (1F +END_MAX_DISTANCE * lastEnd));
        }
    }

    private double lastUpdate = 0;
    private float step = 0;
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null)
            return;
        EntityClientPlayerMP player = mc.thePlayer;
        if (player == null)
            return;

        if (event.phase == TickEvent.Phase.START) {
            if (event.renderTickTime < lastUpdate)
                lastUpdate = lastUpdate - 1;
            // Higher value>change fog quicker
            // /1000 ->
            step = (float) ((event.renderTickTime - lastUpdate) / 300F);
            lastUpdate = event.renderTickTime;
        }
        else {
            pollutionMap.draw3x3Chinks(player.posX, player.posZ);
            drawPollution((int) (lastStart * 192), 0);
            drawPollution((int) (lastEnd * 192), 30);

        }
    }

    private void drawPollution(int amount, int off){
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(""+amount, 0, off, 0xFFFFFFFF);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
