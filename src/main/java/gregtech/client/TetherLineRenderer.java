package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.common.config.Client;

public class TetherLineRenderer {

    private static final float MAX_LINE_WIDTH = 13f;
    private static final int MAX_LINE_TICKS = 14;

    private int ticks;
    private final int tetherX;
    private final int tetherY;
    private final int tetherZ;

    private TetherLineRenderer(int x, int y, int z) {
        tetherX = x;
        tetherY = y;
        tetherZ = z;
    }

    public static void addTetherLineRenderer(int x, int y, int z) {
        if (!Client.render.renderMagLevTethers) return;
        TetherLineRenderer handler = new TetherLineRenderer(x, y, z);
        FMLCommonHandler.instance()
            .bus()
            .register(handler);
        MinecraftForge.EVENT_BUS.register(handler);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            this.ticks++;
            if (this.ticks > MAX_LINE_TICKS) {
                FMLCommonHandler.instance()
                    .bus()
                    .unregister(this);
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        Entity entity = Minecraft.getMinecraft().renderViewEntity;
        double pX = entity.prevPosX + (entity.posX - entity.prevPosX) * e.partialTicks;
        double pY = entity.prevPosY + (entity.posY - entity.prevPosY) * e.partialTicks;
        double pZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * e.partialTicks;
        GL11.glPushMatrix();
        GL11.glTranslated(-pX, -pY, -pZ);
        renderLineToPlayer(pX, pY, pZ, this.ticks);
        GL11.glPopMatrix();
    }

    private void renderLineToPlayer(double pX, double pY, double pZ, int renderTick) {
        Entity player = Minecraft.getMinecraft().thePlayer;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        double progress = (double) renderTick / MAX_LINE_TICKS;
        double timeFactor = (Math.sin(Math.PI * progress) + 1.0) / 2.0;

        GL11.glLineWidth((float) (MAX_LINE_WIDTH * timeFactor));

        final Tessellator tess = Tessellator.instance;
        // todo: quads? not all gpus support lineWidth > 1
        tess.startDrawing(GL11.GL_LINES);
        tess.setColorRGBA(195, 92, 193, (int) (255 * timeFactor));
        tess.addVertex(pX, pY - (player.height / 2), pZ);
        tess.addVertex(this.tetherX + 0.5, this.tetherY + 0.5, this.tetherZ + 0.5);
        tess.draw();

        GL11.glPopAttrib();
    }
}
