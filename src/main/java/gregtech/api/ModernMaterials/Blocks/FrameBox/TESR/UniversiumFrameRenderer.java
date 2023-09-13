package gregtech.api.ModernMaterials.Blocks.FrameBox.TESR;

import fox.spiteful.avaritia.render.CosmicRenderShenanigans;
import gregtech.api.ModernMaterials.ModernMaterialsTextureRegister;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class UniversiumFrameRenderer extends TileEntitySpecialRenderer {
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        // I don't really understand fully how this works, so I won't be of much help, sorry.

        CosmicRenderShenanigans.inventoryRender = false;
        CosmicRenderShenanigans.cosmicOpacity = 2.5f;

        GL11.glPushMatrix();

        CosmicRenderShenanigans.useShader();

        RenderBlocks blockRenderer = new RenderBlocks(tile.getWorldObj());

        CosmicRenderShenanigans.bindItemTexture();

        GL11.glTranslated(0.5 + x, 0.5 + y, 0.5 + z);
        blockRenderer.renderBlockAsItem(tile.getBlockType(), 0, 1.0f);
        //drawBlock(tile.blockType, 0, blockRenderer);
        GL11.glTranslated(0, 5, 0);
        blockRenderer.renderMaxY =  1;
        blockRenderer.renderMinY = -1;
        blockRenderer.renderFaceYPos(Blocks.coal_block, x, y, z, ModernMaterialsTextureRegister.frameGT);
        //this.blockIcon = ModernMaterialsTextureRegister.frameGT;

        //blockRenderer.renderBlockAsItem(, 0, 1.0f);
        //drawBlock(Blocks.coal_block, 0, blockRenderer);
        CosmicRenderShenanigans.releaseShader();

        //Minecraft mc = Minecraft.getMinecraft();
        //mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPopMatrix();

    }
}
