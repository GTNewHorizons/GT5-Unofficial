package gregtech.api.ModernMaterials.Blocks.FrameBox.TESR;

import fox.spiteful.avaritia.render.CosmicRenderShenanigans;
import gregtech.api.ModernMaterials.Blocks.FrameBox.FrameBoxTileEntity;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class UniversiumFrameRenderer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        FrameBoxTileEntity frameBox = (FrameBoxTileEntity) tile;
        int ID = frameBox.getMaterialID();

        ModernMaterial material = ModernMaterialUtilities.getMaterialFromID(ID);

        GL11.glPushMatrix();
        CosmicRenderShenanigans.inventoryRender = true;
        CosmicRenderShenanigans.useShader();

        RenderBlocks renderBlocks = new RenderBlocks(tile.getWorldObj());

        Tessellator.instance.startDrawingQuads();
        renderBlocks.renderBlockAsItem(tile.getBlockType(), ID, 1f);
        Tessellator.instance.draw();

        CosmicRenderShenanigans.releaseShader();
        GL11.glPopMatrix();
    }
}
