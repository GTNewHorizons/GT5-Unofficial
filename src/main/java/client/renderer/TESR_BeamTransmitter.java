package client.renderer;

import common.tileentities.TE_BeamTransmitter;
import kekztech.KekzCore;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TESR_BeamTransmitter extends TileEntitySpecialRenderer {

    private static final ResourceLocation beamTexture = new ResourceLocation(KekzCore.MODID, "textures/effects/Tether_beam.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
        final TE_BeamTransmitter beamTransmitter = (TE_BeamTransmitter) te;

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        // Get Tessellator instance
        final Tessellator tessellator = Tessellator.instance;
        // Bind beam texture and set texture params
        super.bindTexture(beamTexture);
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
        final float exactTime = (float)beamTransmitter.getWorldObj().getTotalWorldTime() + partialTick;
        final float streamTextureOffset = -exactTime * 0.2F - (float) MathHelper.floor_float(-exactTime * 0.1F);

        final double halfBeamWidth = 0.1D;
        final double height = beamTransmitter.getDistanceFromTarget();
        final double uv_x1 = 0.0D;
        final double uv_x2 = 1.0D;
        final double uv_y1 = -1.0D - streamTextureOffset; // This makes the beam stream upwards if you subtract a time sensitive number from it
        final double uv_y2 = height * (0.5D / (halfBeamWidth * 2)) + uv_y1;
        // Construct mesh with texture
        tessellator.addVertexWithUV(x + 0.5 + halfBeamWidth, y + 0.5, z + 0.5, uv_x2, uv_y2);
        tessellator.addVertexWithUV(x + 0.5 + halfBeamWidth, y + height, z + 0.5, uv_x2, uv_y1);
        tessellator.addVertexWithUV(x + 0.5 - halfBeamWidth, y + height, z + 0.5, uv_x1, uv_y1);
        tessellator.addVertexWithUV(x + 0.5 - halfBeamWidth, y + 0.5, z + 0.5, uv_x1, uv_y2);

        tessellator.addVertexWithUV(x + 0.5, y + 0.5, z + 0.5 + halfBeamWidth, uv_x2, uv_y2);
        tessellator.addVertexWithUV(x + 0.5, y + height, z + 0.5 + halfBeamWidth, uv_x2, uv_y1);
        tessellator.addVertexWithUV(x + 0.5, y + height, z + 0.5 - halfBeamWidth, uv_x1, uv_y1);
        tessellator.addVertexWithUV(x + 0.5, y + 0.5, z + 0.5 - halfBeamWidth, uv_x1, uv_y2);
        // Draw!
        tessellator.draw();

        // Reset render flags
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }
}
