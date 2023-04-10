package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.common.entities.GT_EntityFXPollution;
import gregtech.common.misc.GT_ClientPollutionMap;

@SideOnly(Side.CLIENT)
public class GT_PollutionRenderer {

    private static GT_ClientPollutionMap pollutionMap;
    private static int playerPollution = 0;

    private static boolean DEBUG = false;

    // PARTICLES_POLLUTION_START + PARTICLES_POLLUTION_END -> Max Particles
    private static final int PARTICLES_MAX_NUM = 100;
    private static final int PARTICLES_POLLUTION_START = 400000;
    private static final int PARTICLES_POLLUTION_END = 3500000;

    private static final int FOG_START_AT_POLLUTION = 400000;
    private static final int FOG_MAX_AT_POLLUTION = 7000000;
    // jump from linear to exponential fog. x*FOG_MAX_AT_POLLUTION+FOG_START_AT_POLLUTION
    private static final double FOG_START_EXP_RATIO = 0.02D;

    private static final float[] fogColor = { 0.3f, 0.25f, 0.1f };
    private static final short[] grassColor = { 230, 180, 40 };
    private static final short[] leavesColor = { 160, 80, 15 };
    private static final short[] liquidColor = { 160, 200, 10 };
    private static final short[] foliageColor = { 160, 80, 15 };

    // TODO need to soft update some blocks, grass and leaves does more often than liquid it looks like.

    public GT_PollutionRenderer() {
        pollutionMap = new GT_ClientPollutionMap();
    }

    public void preLoad() {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    public void processPacket(ChunkCoordIntPair chunk, int pollution) {
        pollutionMap.addChunkPollution(chunk.chunkXPos, chunk.chunkZPos, pollution);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void enteredWorld(WorldEvent.Load event) {
        EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;
        if (!event.world.isRemote || p == null) return;
        pollutionMap.reset();
    }

    private static int color(int color, int pollution, int low, float high, short[] colors) {
        if (pollution < low) return color;

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        float p = (pollution - low) / high;
        if (p > 1) p = 1;
        float pi = 1 - p;

        r = ((int) (r * pi + p * colors[0])) & 0xFF;
        g = ((int) (g * pi + p * colors[1])) & 0xFF;
        b = ((int) (b * pi + p * colors[2])) & 0xFF;

        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    // Methods for hodgepodge to color grass / foliage blocks etc.
    public static int colorGrass(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z) / 1000, 350, 600, grassColor);
    }

    public static int colorLeaves(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z) / 1000, 300, 500, leavesColor);
    }

    public static int colorLiquid(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z) / 1000, 300, 500, liquidColor);
    }

    public static int colorFoliage(int oColor, int x, int z) {
        return color(oColor, pollutionMap.getPollution(x, z) / 1000, 300, 500, foliageColor);
    }

    public static int getKnownPollution(int x, int z) {
        return pollutionMap.getPollution(x, z);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void manipulateColor(EntityViewRenderEvent.FogColors event) {
        if (!DEBUG && Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) return;

        if (event.block.getMaterial() == Material.water || event.block.getMaterial() == Material.lava) return;

        float x = fogIntensityLastTick > 1 ? 1F : (float) fogIntensityLastTick;
        float xi = 1 - x;

        event.red = xi * event.red + x * fogColor[0];
        event.green = xi * event.green + x * fogColor[1];
        event.blue = xi * event.blue + x * fogColor[2];
    }

    private static final int END_MAX_DISTANCE = 192 - 1;
    private static double fogIntensityLastTick = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderGTPollutionFog(EntityViewRenderEvent.RenderFogEvent event) {
        if (!GT_Mod.gregtechproxy.mRenderPollutionFog) return;

        if ((!DEBUG && Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode)
            || (fogIntensityLastTick <= 0 && fogIntensityLastTick >= FOG_START_EXP_RATIO)) return;

        if (event.fogMode == 0) {
            double v = 1 - fogIntensityLastTick / FOG_START_EXP_RATIO;
            // trying to smooth out jump from linear to exponential
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, (float) ((END_MAX_DISTANCE - 20) * 0.75F * v + 20));
            GL11.glFogf(GL11.GL_FOG_END, (float) (END_MAX_DISTANCE * (0.75F + v * 0.25F)));
        }
        // else if ( event.fogMode < 0) { }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderGTPollutionFog(EntityViewRenderEvent.FogDensity event) {
        if (!GT_Mod.gregtechproxy.mRenderPollutionFog) return;

        if (!DEBUG && Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode) return;

        if (event.entity.isPotionActive(Potion.blindness) || (fogIntensityLastTick < FOG_START_EXP_RATIO)
            || event.block.getMaterial() == Material.water
            || event.block.getMaterial() == Material.lava) return;

        GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP2);
        event.density = (float) Math.pow(fogIntensityLastTick - FOG_START_EXP_RATIO, .75F) / 5 + 0.01F;
        event.setCanceled(true);
    }

    private double lastUpdate = 0;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) return;
        EntityClientPlayerMP player = mc.thePlayer;
        if (player == null) return;

        if (event.phase == TickEvent.Phase.START) {
            if (event.renderTickTime < lastUpdate) lastUpdate = lastUpdate - 1;
            float step = (float) ((event.renderTickTime - lastUpdate) / 50);
            lastUpdate = event.renderTickTime;

            float fogIntensity = (playerPollution - FOG_START_AT_POLLUTION) / (float) FOG_MAX_AT_POLLUTION;
            if (fogIntensity > 1) fogIntensity = 1;
            else if (fogIntensity < 0) fogIntensity = 0;

            double e = fogIntensity - fogIntensityLastTick;
            if (e != 0) {
                if (e > 0.2) e = 0.2D;
                else if (e < -0.5) e = -0.5D;

                if (e > 0.001D || e < -0.001D) fogIntensityLastTick += step * e;
                else fogIntensityLastTick = fogIntensity;
            }
        } else if (DEBUG) {
            drawPollution("Intensity: " + (fogIntensityLastTick * 10000), 0);
            drawPollution(
                "Pollution: " + pollutionMap.getPollution(
                    Minecraft.getMinecraft().thePlayer.lastTickPosX,
                    Minecraft.getMinecraft().thePlayer.lastTickPosZ),
                20);
            drawPollution(
                "Density:   "
                    + ((float) (Math.pow(fogIntensityLastTick - FOG_START_EXP_RATIO, .75F) / 5 + 0.01F) * 10000),
                40);
        }
    }

    // Adding dirt particles in the air
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!GT_Mod.gregtechproxy.mRenderDirtParticles) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) return;
        EntityClientPlayerMP player = mc.thePlayer;
        if (player == null || (player.capabilities.isCreativeMode && !DEBUG)) return;

        World w = player.worldObj;
        playerPollution = pollutionMap.getPollution(player.lastTickPosX, player.lastTickPosZ);

        float intensity = ((float) playerPollution - PARTICLES_POLLUTION_START) / PARTICLES_POLLUTION_END;
        if (intensity < 0) return;
        else if (intensity > 1) intensity = 1;
        else intensity *= intensity;

        int x = MathHelper.floor_double(player.posX);
        int y = MathHelper.floor_double(player.posY);
        int z = MathHelper.floor_double(player.posZ);

        int numParticles = Math.round(intensity * PARTICLES_MAX_NUM);

        for (int l = 0; l < numParticles; ++l) {
            int i1 = x + w.rand.nextInt(16) - w.rand.nextInt(16);
            int j1 = y + w.rand.nextInt(16) - w.rand.nextInt(16);
            int k1 = z + w.rand.nextInt(16) - w.rand.nextInt(16);
            Block block = w.getBlock(i1, j1, k1);

            if (block.getMaterial() == Material.air) {
                EntityFX fx = new GT_EntityFXPollution(
                    w,
                    (float) i1 + w.rand.nextFloat(),
                    (float) j1 + w.rand.nextFloat(),
                    (float) k1 + w.rand.nextFloat());
                mc.effectRenderer.addEffect(fx);
            }
        }
    }

    private void drawPollution(String text, int off) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, 0, off, 0xFFFFFFFF);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
}
