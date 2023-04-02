package com.github.technus.tectech.thing.block;

import static com.github.technus.tectech.Reference.MODID;
import static java.lang.Math.*;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderEyeOfHarmony extends TileEntitySpecialRenderer {

    public static final ResourceLocation STAR_LAYER_0 = new ResourceLocation(MODID, "models/StarLayer0.png");
    public static final ResourceLocation STAR_LAYER_1 = new ResourceLocation(MODID, "models/StarLayer1.png");
    public static final ResourceLocation STAR_LAYER_2 = new ResourceLocation(MODID, "models/StarLayer2.png");
    public static IModelCustom starModel;
    private static IModelCustom spaceModel;

    public RenderEyeOfHarmony() {
        starModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Star.obj"));
        spaceModel = AdvancedModelLoader.loadModel(new ResourceLocation(MODID, "models/Space.obj"));
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEyeOfHarmony)) return;

        TileEyeOfHarmony EOHRenderTile = (TileEyeOfHarmony) tile;

        // Render outer space layer.

        {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
            renderOuterSpaceShell();

            renderOrbitObjects(EOHRenderTile);

            // Render star stuff.
            renderStarLayer(EOHRenderTile, 0, STAR_LAYER_0, 1.0f);
            renderStarLayer(EOHRenderTile, 1, STAR_LAYER_1, 0.4f);
            renderStarLayer(EOHRenderTile, 2, STAR_LAYER_2, 0.2f);

            GL11.glPopMatrix();
        }
    }

    private void renderOrbitObjects(final TileEyeOfHarmony EOHRenderTile) {

        if (EOHRenderTile.getOrbitingObjects() != null) {

            if (EOHRenderTile.getOrbitingObjects().size() == 0) {
                EOHRenderTile.generateImportantInfo();
            }

            for (TileEyeOfHarmony.OrbitingObject t : EOHRenderTile.getOrbitingObjects()) {
                renderOrbit(EOHRenderTile, t);
            }
        }
    }

    void renderOrbit(final TileEyeOfHarmony EOHRenderTile, final TileEyeOfHarmony.OrbitingObject orbitingObject) {
        // Render orbiting body.
        GL11.glPushMatrix();
        GL11.glRotatef(orbitingObject.zAngle, 0, 0, 1);
        GL11.glRotatef(orbitingObject.xAngle, 1, 0, 0);
        GL11.glRotatef((orbitingObject.rotationSpeed * 0.1f * EOHRenderTile.angle) % 360.0f, 0F, 1F, 0F);
        GL11.glTranslated(-0.2 - orbitingObject.distance - STAR_RESCALE * EOHRenderTile.getSize(), 0, 0);
        GL11.glRotatef((orbitingObject.orbitSpeed * 0.1f * EOHRenderTile.angle) % 360.0f, 0F, 1F, 0F);
        renderBlockInWorld(orbitingObject.block, 0, orbitingObject.scale);
        GL11.glPopMatrix();
    }

    public static void renderOuterSpaceShell() {

        // Begin animation.
        GL11.glPushMatrix();

        // OpenGL settings, not sure exactly what these do.

        // Disables lighting, so star is always lit (I think).
        GL11.glDisable(GL11.GL_LIGHTING);
        // Merges colours of the various layers of the star?
        GL11.glEnable(GL11.GL_BLEND);

        // Bind animation to layer of star.
        FMLClientHandler.instance().getClient().getTextureManager()
                .bindTexture(new ResourceLocation(MODID, "models/spaceLayer.png"));

        final float scale = 0.01f * 17.5f;
        // Scale the star up in the x, y and z directions.
        GL11.glScalef(scale, scale, scale);

        GL11.glColor4f(1, 1, 1, 1);

        spaceModel.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        // Finish animation.
        GL11.glPopMatrix();
    }

    private static final float STAR_RESCALE = 0.2f;

    private void renderStarLayer(TileEyeOfHarmony EOHRenderTile, int layer, ResourceLocation texture, float alpha) {

        // Begin animation.
        GL11.glPushMatrix();

        // OpenGL settings, not sure exactly what these do.

        // Disables lighting, so star is always lit (I think).
        GL11.glDisable(GL11.GL_LIGHTING);
        // Culls things out of line of sight?
        GL11.glEnable(GL11.GL_CULL_FACE);
        // Merges colours of the various layers of the star?
        GL11.glEnable(GL11.GL_BLEND);
        // ???
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Bind animation to layer of star.
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);

        // 0.01f magic number to shrink sphere obj down.
        // Size obtained from the multis current recipe.
        float scale = 0.01f * STAR_RESCALE * EOHRenderTile.getSize();

        // Put each subsequent layer further out.
        scale *= pow(1.04f, layer);

        // Scale the star up in the x, y and z directions.
        GL11.glScalef(scale, scale, scale);

        switch (layer) {
            case 0:
                GL11.glRotatef(130, 0F, 1F, 1F);
                break;
            case 1:
                GL11.glRotatef(-49, 1F, 1F, 0F);
                break;
            case 2:
                GL11.glRotatef(67, 1F, 0F, 1F);
                break;
        }

        // Set colour and alpha (transparency) of the star layer.
        GL11.glColor4f(1, 1, 1, alpha);

        // Spin the star around according to the multi time dilation tier.
        GL11.glRotatef((0.03f * EOHRenderTile.angle * EOHRenderTile.getRotationSpeed()) % 360.0f, 0F, 0F, 1F);

        starModel.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        // Finish animation.
        GL11.glPopMatrix();
    }

    private void renderBlockInWorld(Block block, int meta, float blockSize) {
        Tessellator tes = Tessellator.instance;

        this.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        tes.setColorOpaque_F(1f, 1f, 1f);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        tes.setColorOpaque_F(1f, 1f, 1f);

        // Add the rendering calls here (Can and should use helper functions that do the vertex calls)

        double x = 0;
        double y = 0;
        double z = 0;

        double[] X = { x - 0.5, x - 0.5, x + 0.5, x + 0.5, x + 0.5, x + 0.5, x - 0.5, x - 0.5 };
        double[] Y = { y + 0.5, y - 0.5, y - 0.5, y + 0.5, y + 0.5, y - 0.5, y - 0.5, y + 0.5 };
        double[] Z = { z + 0.5, z + 0.5, z + 0.5, z + 0.5, z - 0.5, z - 0.5, z - 0.5, z - 0.5 };

        tes.startDrawingQuads();

        GL11.glScalef(blockSize, blockSize, blockSize);

        IIcon texture;

        double minU;
        double maxU;
        double minV;
        double maxV;

        {
            texture = block.getIcon(4, meta);

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
            texture = block.getIcon(0, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(X[2], Y[2], Z[2], maxU, maxV);
            tes.addVertexWithUV(X[5], Y[5], Z[5], maxU, minV);
            tes.addVertexWithUV(X[6], Y[6], Z[6], minU, minV);
            tes.addVertexWithUV(X[1], Y[1], Z[1], minU, maxV);
        }

        {
            texture = block.getIcon(2, meta);

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
            texture = block.getIcon(5, meta);

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
            texture = block.getIcon(1, meta);

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
            texture = block.getIcon(3, meta);

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
