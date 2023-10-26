package gregtech.api.ModernMaterials.Blocks.FrameBox.TESR;

import static gregtech.api.ModernMaterials.Blocks.FrameBox.TESR.CustomTextureRegister.universiumFrameTexture;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.renderBlock;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import fox.spiteful.avaritia.render.CosmicRenderShenanigans;

public class UniversiumFrameBlockRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        // I don't really understand fully how this works, so I won't be of much help, sorry.

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

        renderBlock(universiumFrameTexture);

        CosmicRenderShenanigans.releaseShader();

        GL11.glPopMatrix();
    }

}
