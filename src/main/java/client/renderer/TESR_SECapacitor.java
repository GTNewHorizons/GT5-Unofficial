package client.renderer;

import common.tileentities.TE_SpaceElevatorCapacitor;
import kekztech.KekzCore;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TESR_SECapacitor extends TileEntitySpecialRenderer {

    private static final ResourceLocation capSide = new ResourceLocation(KekzCore.MODID, "textures/blocks/SpaceElevatorCapacitor_side_renderbase.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTick) {
        final Tessellator tessellator = Tessellator.instance;
        final TE_SpaceElevatorCapacitor teCap = (TE_SpaceElevatorCapacitor) te;
        // Setup vertices
        final double fbr_x = x + 1;
        final double fbr_z = z + 1;

        final double ftr_y = y + 1;

        final double uv_a_u = 1.0D;
        final double uv_a_v = 1.0D;

        final double uv_b_u = 1.0D;
        final double uv_b_v = 0.0D;

        final double uv_c_u = 0.0D;
        final double uv_c_v = 0.0D;

        final double uv_d_u = 0.0D;
        final double uv_d_v = 1.0D;
        // Render sides
        super.bindTexture(capSide);

        // Prepare Tessellator
        tessellator.startDrawingQuads();
        // Render the caps as red if there are maintenance issues
        if(teCap.isDamaged()) {
            final float wave = (float) Math.abs(Math.sin((te.getWorldObj().getTotalWorldTime() + partialTick) / 20.0D));
            final int redSat = 64 + (int) Math.ceil(191 * wave);
            tessellator.setColorRGBA(redSat, 0, 0, 255);
        } else {
            final int sat = (int) Math.ceil(teCap.getChargeLevel() * 255);
            tessellator.setColorRGBA(0, 0, sat, 255);
        }
        tessellator.setBrightness(255);
        // (DOWN and UP faces are not rendered as they will not ever be visible in the Space Elevator structure)
        // NORTH
        tessellator.addVertexWithUV(x, y, z, uv_a_u, uv_a_v);
        tessellator.addVertexWithUV(x, ftr_y, z, uv_b_u, uv_b_v);
        tessellator.addVertexWithUV(fbr_x, ftr_y, z, uv_c_u, uv_c_v);
        tessellator.addVertexWithUV(fbr_x, y, z, uv_d_u, uv_d_v);
        // SOUTH
        tessellator.addVertexWithUV(fbr_x, y, fbr_z, uv_a_u, uv_a_v);
        tessellator.addVertexWithUV(fbr_x, ftr_y, fbr_z, uv_b_u, uv_b_v);
        tessellator.addVertexWithUV(x, ftr_y, fbr_z, uv_c_u, uv_c_v);
        tessellator.addVertexWithUV(x, y, fbr_z, uv_d_u, uv_d_v);
        // WEST
        tessellator.addVertexWithUV(x, y, fbr_z, uv_a_u, uv_a_v);
        tessellator.addVertexWithUV(x, ftr_y, fbr_z, uv_b_u, uv_b_v);
        tessellator.addVertexWithUV(x, ftr_y, z, uv_c_u, uv_c_v);
        tessellator.addVertexWithUV(x, y, z, uv_d_u, uv_d_v);
        // EAST
        tessellator.addVertexWithUV(fbr_x, y, z, uv_a_u, uv_a_v);
        tessellator.addVertexWithUV(fbr_x, ftr_y, z, uv_b_u, uv_b_v);
        tessellator.addVertexWithUV(fbr_x, ftr_y, fbr_z, uv_c_u, uv_c_v);
        tessellator.addVertexWithUV(fbr_x, y, fbr_z, uv_d_u, uv_d_v);
        // Draw!
        tessellator.draw();
    }
}
