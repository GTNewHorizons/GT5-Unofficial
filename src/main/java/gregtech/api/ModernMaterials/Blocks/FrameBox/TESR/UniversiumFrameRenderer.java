package gregtech.api.ModernMaterials.Blocks.FrameBox.TESR;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fox.spiteful.avaritia.render.CosmicRenderShenanigans;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.ModernMaterialsTextureRegister;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import static gregtech.api.ModernMaterials.Blocks.FrameBox.TESR.CustomTextureRegister.universiumFrameTexture;
import static gregtech.api.enums.Mods.GregTech;

public class UniversiumFrameRenderer extends TileEntitySpecialRenderer {

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

        GL11.glColor4f(1,1,1,0.5f);
        renderBlockInWorld(universiumFrameTexture);

        CosmicRenderShenanigans.releaseShader();

        GL11.glPopMatrix();

    }

    private void renderBlockInWorld(IIcon texture) {
        Tessellator tes = Tessellator.instance;

        double x = 0;
        double y = 0;
        double z = 0;

        double[] X = { x - 0.5, x - 0.5, x + 0.5, x + 0.5, x + 0.5, x + 0.5, x - 0.5, x - 0.5 };
        double[] Y = { y + 0.5, y - 0.5, y - 0.5, y + 0.5, y + 0.5, y - 0.5, y - 0.5, y + 0.5 };
        double[] Z = { z + 0.5, z + 0.5, z + 0.5, z + 0.5, z - 0.5, z - 0.5, z - 0.5, z - 0.5 };

        tes.startDrawingQuads();

        double minU;
        double maxU;
        double minV;
        double maxV;

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[1], Y[1], Z[1], maxU, maxV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], maxU, minV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], minU, minV);
            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, maxV);
        }

        {
            // Bottom face.

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, maxV);
            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, minV);
            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, minV);
            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[6], Y[6], Z[6], maxU, maxV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], maxU, minV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], minU, minV);
            tes.addVertexWithUV(X[5], Y[5], Z[5], minU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, maxV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], maxU, minV);
            tes.addVertexWithUV(X[3], Y[3], Z[3], minU, minV);
            tes.addVertexWithUV(X[2], Y[2], Z[2], minU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[3], Y[3], Z[3], maxU, maxV);
            tes.addVertexWithUV(X[4], Y[4], Z[4], maxU, minV);
            tes.addVertexWithUV(X[7], Y[7], Z[7], minU, minV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], minU, maxV);
        }

        {

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, maxV);
            tes.addVertexWithUV(X[3], Y[3], Z[3], maxU, minV);
            tes.addVertexWithUV(X[0], Y[0], Z[0], minU, minV);
            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, maxV);
        }

        tes.draw();

        // ----------------------------------------------
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

}
