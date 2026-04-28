package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class StructureErrorHighlightRenderer {

    private static final int DURATION_TICKS = 200;
    private static final float LINE_WIDTH = 3.0f;

    private static StructureErrorHighlightRenderer activeHighlight;

    private int ticks;
    private final int targetX;
    private final int targetY;
    private final int targetZ;

    private StructureErrorHighlightRenderer(int x, int y, int z) {
        targetX = x;
        targetY = y;
        targetZ = z;
    }

    public static void highlight(int x, int y, int z) {
        if (activeHighlight != null) {
            activeHighlight.remove();
        }
        StructureErrorHighlightRenderer renderer = new StructureErrorHighlightRenderer(x, y, z);
        activeHighlight = renderer;
        FMLCommonHandler.instance()
            .bus()
            .register(renderer);
        MinecraftForge.EVENT_BUS.register(renderer);
    }

    private void remove() {
        FMLCommonHandler.instance()
            .bus()
            .unregister(this);
        MinecraftForge.EVENT_BUS.unregister(this);
        if (activeHighlight == this) {
            activeHighlight = null;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            ticks++;
            if (ticks > DURATION_TICKS) {
                remove();
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        double pX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
        double pY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
        double pZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glTranslated(-pX, -pY, -pZ);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(LINE_WIDTH);
        GL11.glColor4f(1.0f, 0.2f, 0.2f, 1.0f);

        drawBoxOutline(
            targetX - 0.002,
            targetY - 0.002,
            targetZ - 0.002,
            targetX + 1.002,
            targetY + 1.002,
            targetZ + 1.002);

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    private static void drawBoxOutline(double x1, double y1, double z1, double x2, double y2, double z2) {
        final Tessellator tess = Tessellator.instance;
        tess.startDrawing(GL11.GL_LINE_STRIP);

        tess.addVertex(x1, y1, z1);
        tess.addVertex(x1, y2, z1);
        tess.addVertex(x1, y2, z2);
        tess.addVertex(x1, y1, z2);
        tess.addVertex(x1, y1, z1);

        tess.addVertex(x2, y1, z1);
        tess.addVertex(x2, y2, z1);
        tess.addVertex(x2, y2, z2);
        tess.addVertex(x2, y1, z2);
        tess.addVertex(x2, y1, z1);

        tess.addVertex(x1, y1, z1);
        tess.addVertex(x2, y1, z1);
        tess.addVertex(x2, y1, z2);
        tess.addVertex(x1, y1, z2);
        tess.addVertex(x1, y2, z2);
        tess.addVertex(x2, y2, z2);
        tess.addVertex(x2, y2, z1);
        tess.addVertex(x1, y2, z1);

        tess.draw();
    }
}
