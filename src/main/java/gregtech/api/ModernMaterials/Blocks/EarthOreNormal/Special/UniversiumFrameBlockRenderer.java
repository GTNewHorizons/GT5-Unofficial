package gregtech.api.ModernMaterials.Blocks.EarthOreNormal.Special;

import static gregtech.api.ModernMaterials.Blocks.FrameBox.Special.CustomTextureRegister.universiumFrameTexture;
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

        CosmicRenderShenanigans.bindItemTexture();

        renderBlock(universiumFrameTexture);

        CosmicRenderShenanigans.releaseShader();

        GL11.glPopMatrix();
    }

}
