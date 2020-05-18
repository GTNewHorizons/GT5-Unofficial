package client.renderer;

import kekztech.KekzCore;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TESR_SETether extends TileEntitySpecialRenderer {

    private static final ResourceLocation tetherBeamTexture = new ResourceLocation(KekzCore.MODID, "textures/effects/Tether_beam.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
        float beamLengthScale = 1.0F; // [0.0F, 1.0F] -> linear scale from 0 to 256
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        // Get Tessellator instance
        final Tessellator tessellator = Tessellator.instance;
        // Bind beam texture and set texture params
        super.bindTexture(tetherBeamTexture);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        // Set render flags for inner beam
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        OpenGlHelper.glBlendFunc(770, 1, 1, 0);
        // Prepare Tessellator
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(255, 255, 255, 32);
        // Variables stuff II
        final double halfBeamWidth = 0.38D;
        final double d_rot1 = 0.5D + Math.cos(2.356194490192345D) * halfBeamWidth; // rotates the beam...
        final double d_rot2 = 0.5D + Math.sin(2.356194490192345D) * halfBeamWidth;
        final double d_rot3 = 0.5D + Math.cos((Math.PI / 4D)) * halfBeamWidth;
        final double d_rot4 = 0.5D + Math.sin((Math.PI / 4D)) * halfBeamWidth;
        final double d_rot5 = 0.5D + Math.cos(3.9269908169872414D) * halfBeamWidth;
        final double d_rot6 = 0.5D + Math.sin(3.9269908169872414D) * halfBeamWidth;
        final double d_rot7 = 0.5D + Math.cos(5.497787143782138D) * halfBeamWidth;
        final double d_rot8 = 0.5D + Math.sin(5.497787143782138D) * halfBeamWidth; // ...until here
        final double height = 256.0F * beamLengthScale;
        final double uv_x1 = 0.0D;
        final double uv_x2 = 1.0D;
        final double uv_y1 = -1.0D; // This makes the beam stream upwards if you add a time sensitive number to it
        final double uv_y2 = (double)(256.0F * beamLengthScale) * (0.5D / halfBeamWidth) + uv_y1;
        // Construct mesh with texture
        tessellator.addVertexWithUV(x + d_rot1, y + height, z + d_rot2, uv_x2, uv_y2);
        tessellator.addVertexWithUV(x + d_rot1, y, z + d_rot2, uv_x2, uv_y1);
        tessellator.addVertexWithUV(x + d_rot3, y, z + d_rot4, uv_x1, uv_y1);
        tessellator.addVertexWithUV(x + d_rot3, y + height, z + d_rot4, uv_x1, uv_y2);
        tessellator.addVertexWithUV(x + d_rot7, y + height, z + d_rot8, uv_x2, uv_y2);
        tessellator.addVertexWithUV(x + d_rot7, y, z + d_rot8, uv_x2, uv_y1);
        tessellator.addVertexWithUV(x + d_rot5, y, z + d_rot6, uv_x1, uv_y1);
        tessellator.addVertexWithUV(x + d_rot5, y + height, z + d_rot6, uv_x1, uv_y2);
        tessellator.addVertexWithUV(x + d_rot3, y + height, z + d_rot4, uv_x2, uv_y2);
        tessellator.addVertexWithUV(x + d_rot3, y, z + d_rot4, uv_x2, uv_y1);
        tessellator.addVertexWithUV(x + d_rot7, y, z + d_rot8, uv_x1, uv_y1);
        tessellator.addVertexWithUV(x + d_rot7, y + height, z + d_rot8, uv_x1, uv_y2);
        tessellator.addVertexWithUV(x + d_rot5, y + height, z + d_rot6, uv_x2, uv_y2);
        tessellator.addVertexWithUV(x + d_rot5, y, z + d_rot6, uv_x2, uv_y1);
        tessellator.addVertexWithUV(x + d_rot1, y, z + d_rot2, uv_x1, uv_y1);
        tessellator.addVertexWithUV(x + d_rot1, y + height, z + d_rot2, uv_x1, uv_y2);
        // Draw!
        tessellator.draw();

        // Reset render flags
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }

}
