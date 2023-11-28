package gregtech.api.modernmaterials.blocks.blocktypes.framebox.special;

import static gregtech.api.modernmaterials.render.Utilities.renderBlock;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import fox.spiteful.avaritia.render.CosmicRenderShenanigans;

public class  UniversiumFrameBlockRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        CosmicRenderShenanigans.inventoryRender = false;
        CosmicRenderShenanigans.cosmicOpacity = 2.5f;

        GL11.glPushMatrix();

        CosmicRenderShenanigans.useShader();

        CosmicRenderShenanigans.bindItemTexture();

        GL11.glTranslated(0.5 + x, 0.5 + y, 0.5 + z);

        // This is a bit of a hack here, we do this solely because rendering a block icon will cause the stars
        // in the shader to not appear as they are part of the item atlas. So we store our block in the item atlas
        // to draw it along with the stars. Otherwise, the shader would need to be edited.
        CosmicRenderShenanigans.bindItemTexture();

        renderBlock(CustomTextureRegister.universiumFrameTexture);

        CosmicRenderShenanigans.releaseShader();

        GL11.glPopMatrix();
    }

}
