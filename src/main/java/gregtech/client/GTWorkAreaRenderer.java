package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.MTEOreDrillingPlantBase;

@SideOnly(Side.CLIENT)
public class GTWorkAreaRenderer {

    private static final int WORK_AREA_MINED_CHUNK_COLOR = 0x808080;
    private static final int WORK_AREA_CURRENT_CHUNK_COLOR = 0x00FF00;
    private static final int WORK_AREA_PENDING_CHUNK_COLOR = 0xFFD700;
    private static final int WORK_AREA_COLOR = 0x9003fc;
    private static final int WORK_AREA_FACE_ALPHA = 51; // 20% of 255
    private static final double NUMBER_DRAW_IN_Y_AXIS = 200D;
    private static final float NUMBER_SCALE = 0.6F;

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.theWorld == null || mc.renderViewEntity == null) {
            return;
        }

        Entity camera = mc.renderViewEntity;

        double cameraX = camera.lastTickPosX + (camera.posX - camera.lastTickPosX) * event.partialTicks;
        double cameraY = camera.lastTickPosY + (camera.posY - camera.lastTickPosY) * event.partialTicks;
        double cameraZ = camera.lastTickPosZ + (camera.posZ - camera.lastTickPosZ) * event.partialTicks;

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(-cameraX, -cameraY, -cameraZ);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);

        GL11.glDepthMask(false);

        GL11.glLineWidth(2.0F);

        for (Object object : mc.theWorld.loadedTileEntityList) {
            if (!(object instanceof IGregTechTileEntity gregTechTile)) {
                continue;
            }

            IMetaTileEntity metaTileEntity = gregTechTile.getMetaTileEntity();

            if (!(metaTileEntity instanceof MTEOreDrillingPlantBase oreDrill)) {
                continue;
            }

            if (!oreDrill.isWorkAreaShown()) {
                continue;
            }

            AxisAlignedBB workArea = oreDrill.getWorkAreaAABB();
            if (workArea == null) {
                continue;
            }

            drawWorkAreaFaces(workArea);
            // AxisAlignedBB must not be offset with -cameraX/Y/Z
            RenderGlobal.drawOutlinedBoundingBox(workArea, WORK_AREA_COLOR);

            for (MTEOreDrillingPlantBase.WorkAreaChunk chunk : oreDrill.getWorkAreaChunksInWorkOrder()) {
                renderChunkNumber(
                    mc.fontRenderer,
                    String.valueOf(chunk.order()),
                    (chunk.chunkX() << 4) + 8.0D,
                    (chunk.chunkZ() << 4) + 8.0D,
                    getChunkNumberColor(chunk.state()));
            }
        }

        GL11.glDepthMask(true);

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void drawWorkAreaFaces(@NotNull AxisAlignedBB box) {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(WORK_AREA_COLOR, WORK_AREA_FACE_ALPHA);

        double minX = box.minX;
        double minY = box.minY;
        double minZ = box.minZ;
        double maxX = box.maxX;
        double maxY = box.maxY;
        double maxZ = box.maxZ;

        // North face - minZ
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, maxY, minZ);
        tessellator.addVertex(minX, maxY, minZ);

        // South face - maxZ
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(minX, maxY, maxZ);
        tessellator.addVertex(maxX, maxY, maxZ);

        // West face - minX
        tessellator.addVertex(minX, minY, maxZ);
        tessellator.addVertex(minX, minY, minZ);
        tessellator.addVertex(minX, maxY, minZ);
        tessellator.addVertex(minX, maxY, maxZ);

        // East face - maxX
        tessellator.addVertex(maxX, minY, minZ);
        tessellator.addVertex(maxX, minY, maxZ);
        tessellator.addVertex(maxX, maxY, maxZ);
        tessellator.addVertex(maxX, maxY, minZ);

        tessellator.draw();
    }

    private void renderChunkNumber(@NotNull FontRenderer fontRenderer, String text, double x, double z, int color) {
        GL11.glPushMatrix();

        GL11.glTranslated(x, NUMBER_DRAW_IN_Y_AXIS, z);

        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

        GL11.glScalef(-NUMBER_SCALE, -NUMBER_SCALE, NUMBER_SCALE);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glDepthMask(false);

        int width = fontRenderer.getStringWidth(text);
        fontRenderer.drawString(text, -width / 2, 0, color);

        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }

    private int getChunkNumberColor(@NotNull MTEOreDrillingPlantBase.WorkAreaChunkState state) {
        return switch (state) {
            case MINED -> WORK_AREA_MINED_CHUNK_COLOR;
            case CURRENT -> WORK_AREA_CURRENT_CHUNK_COLOR;
            case PENDING -> WORK_AREA_PENDING_CHUNK_COLOR;
        };
    }
}
