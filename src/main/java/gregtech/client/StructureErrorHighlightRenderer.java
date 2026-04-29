package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class StructureErrorHighlightRenderer {

    private static final int DURATION_TICKS = 200;
    private static final float EXPAND = 0.002f;

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

        AxisAlignedBB box = AxisAlignedBB
            .getBoundingBox(targetX, targetY, targetZ, targetX + 1.0, targetY + 1.0, targetZ + 1.0)
            .expand(EXPAND, EXPAND, EXPAND)
            .getOffsetBoundingBox(-pX, -pY, -pZ);

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        RenderGlobal.drawOutlinedBoundingBox(box, 0xFF5555);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
