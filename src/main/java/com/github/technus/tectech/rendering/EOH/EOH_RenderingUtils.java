package com.github.technus.tectech.rendering.EOH;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.rendering.EOH.EOH_TESR.STAR_LAYER_0;
import static com.github.technus.tectech.rendering.EOH.EOH_TESR.STAR_LAYER_1;
import static com.github.technus.tectech.rendering.EOH.EOH_TESR.STAR_LAYER_2;
import static com.github.technus.tectech.rendering.EOH.EOH_TESR.spaceModel;
import static com.github.technus.tectech.rendering.EOH.EOH_TESR.starModel;
import static java.lang.Math.pow;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public abstract class EOH_RenderingUtils {

    public static void renderStar(IItemRenderer.ItemRenderType type, Color color) {
        GL11.glPushMatrix();

        if (type == IItemRenderer.ItemRenderType.INVENTORY) GL11.glRotated(180, 0, 1, 0);
        else if (type == IItemRenderer.ItemRenderType.EQUIPPED
                || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                    GL11.glTranslated(0.5, 0.5, 0.5);
                    if (type == IItemRenderer.ItemRenderType.EQUIPPED) GL11.glRotated(90, 0, 1, 0);
                }

        // Render star stuff.
        renderStarLayer(0, STAR_LAYER_0, color, 1.0f);
        renderStarLayer(1, STAR_LAYER_1, color, 0.4f);
        renderStarLayer(2, STAR_LAYER_2, color, 0.2f);

        GL11.glPopMatrix();
    }

    public static void renderStar(IItemRenderer.ItemRenderType type) {
        renderStar(type, new Color(1.0f, 0.4f, 0.05f, 1.0f));
    }

    private static void renderStarLayer(int layer, ResourceLocation texture, Color color, float alpha) {

        // Begin animation.
        GL11.glPushMatrix();

        // OpenGL settings, not sure exactly what these do.

        // Disables lighting, so star is always lit (I think).
        GL11.glDisable(GL11.GL_LIGHTING);
        // Culls triangles/quads facing away from the camera
        GL11.glEnable(GL11.GL_CULL_FACE);
        // Allows alpha blending to occur (transparency-based color mixing)
        GL11.glEnable(GL11.GL_BLEND);
        // ???
        if (alpha < 1.0f) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        } else {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        // Bind image to layer of star.
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);

        // 0.01f magic number to shrink sphere obj down.
        // Size obtained from the multis current recipe.
        float scale = 0.01f;

        // Put each subsequent layer further out.
        scale *= pow(1.04f, layer);

        // Scale the star up in the x, y and z directions.
        GL11.glScalef(scale, scale, scale);

        switch (layer) {
            case 0 -> GL11.glRotatef(130 + (System.currentTimeMillis() / 64) % 360, 0F, 1F, 1F);
            case 1 -> GL11.glRotatef(-49 + (System.currentTimeMillis() / 64) % 360, 1F, 1F, 0F);
            case 2 -> GL11.glRotatef(67 + (System.currentTimeMillis() / 64) % 360, 1F, 0F, 1F);
        }

        // Set colour and alpha (transparency) of the star layer.
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;

        GL11.glColor4f(red, green, blue, alpha);

        starModel.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // Finish animation.
        GL11.glPopMatrix();
    }

    public static void beginRenderingBlocksInWorld(final float blockSize) {
        final Tessellator tes = Tessellator.instance;

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_LIGHTING);

        tes.setColorOpaque_F(1f, 1f, 1f);
        tes.startDrawingQuads();

        GL11.glScalef(blockSize, blockSize, blockSize);
    }

    public static void endRenderingBlocksInWorld() {
        Tessellator.instance.draw();

        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    static final double[] BLOCK_X = { -0.5, -0.5, +0.5, +0.5, +0.5, +0.5, -0.5, -0.5 };
    static final double[] BLOCK_Y = { +0.5, -0.5, -0.5, +0.5, +0.5, -0.5, -0.5, +0.5 };
    static final double[] BLOCK_Z = { +0.5, +0.5, +0.5, +0.5, -0.5, -0.5, -0.5, -0.5 };

    public static void addRenderedBlockInWorld(final Block block, final int meta, final double x, final double y,
            final double z) {
        final Tessellator tes = Tessellator.instance;

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

            tes.addVertexWithUV(x + BLOCK_X[1], y + BLOCK_Y[1], z + BLOCK_Z[1], maxU, maxV);
            tes.addVertexWithUV(x + BLOCK_X[0], y + BLOCK_Y[0], z + BLOCK_Z[0], maxU, minV);
            tes.addVertexWithUV(x + BLOCK_X[7], y + BLOCK_Y[7], z + BLOCK_Z[7], minU, minV);
            tes.addVertexWithUV(x + BLOCK_X[6], y + BLOCK_Y[6], z + BLOCK_Z[6], minU, maxV);
        }

        {
            // Bottom face.
            texture = block.getIcon(0, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(x + BLOCK_X[5], y + BLOCK_Y[5], z + BLOCK_Z[5], maxU, minV);
            tes.addVertexWithUV(x + BLOCK_X[2], y + BLOCK_Y[2], z + BLOCK_Z[2], maxU, maxV);
            tes.addVertexWithUV(x + BLOCK_X[1], y + BLOCK_Y[1], z + BLOCK_Z[1], minU, maxV);
            tes.addVertexWithUV(x + BLOCK_X[6], y + BLOCK_Y[6], z + BLOCK_Z[6], minU, minV);
        }

        {
            texture = block.getIcon(2, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(x + BLOCK_X[6], y + BLOCK_Y[6], z + BLOCK_Z[6], maxU, maxV);
            tes.addVertexWithUV(x + BLOCK_X[7], y + BLOCK_Y[7], z + BLOCK_Z[7], maxU, minV);
            tes.addVertexWithUV(x + BLOCK_X[4], y + BLOCK_Y[4], z + BLOCK_Z[4], minU, minV);
            tes.addVertexWithUV(x + BLOCK_X[5], y + BLOCK_Y[5], z + BLOCK_Z[5], minU, maxV);
        }

        {
            texture = block.getIcon(5, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(x + BLOCK_X[5], y + BLOCK_Y[5], z + BLOCK_Z[5], maxU, maxV);
            tes.addVertexWithUV(x + BLOCK_X[4], y + BLOCK_Y[4], z + BLOCK_Z[4], maxU, minV);
            tes.addVertexWithUV(x + BLOCK_X[3], y + BLOCK_Y[3], z + BLOCK_Z[3], minU, minV);
            tes.addVertexWithUV(x + BLOCK_X[2], y + BLOCK_Y[2], z + BLOCK_Z[2], minU, maxV);
        }

        {
            texture = block.getIcon(1, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(x + BLOCK_X[3], y + BLOCK_Y[3], z + BLOCK_Z[3], maxU, maxV);
            tes.addVertexWithUV(x + BLOCK_X[4], y + BLOCK_Y[4], z + BLOCK_Z[4], maxU, minV);
            tes.addVertexWithUV(x + BLOCK_X[7], y + BLOCK_Y[7], z + BLOCK_Z[7], minU, minV);
            tes.addVertexWithUV(x + BLOCK_X[0], y + BLOCK_Y[0], z + BLOCK_Z[0], minU, maxV);
        }

        {
            texture = block.getIcon(3, meta);

            minU = texture.getMinU();
            maxU = texture.getMaxU();
            minV = texture.getMinV();
            maxV = texture.getMaxV();

            tes.addVertexWithUV(x + BLOCK_X[2], y + BLOCK_Y[2], z + BLOCK_Z[2], maxU, maxV);
            tes.addVertexWithUV(x + BLOCK_X[3], y + BLOCK_Y[3], z + BLOCK_Z[3], maxU, minV);
            tes.addVertexWithUV(x + BLOCK_X[0], y + BLOCK_Y[0], z + BLOCK_Z[0], minU, minV);
            tes.addVertexWithUV(x + BLOCK_X[1], y + BLOCK_Y[1], z + BLOCK_Z[1], minU, maxV);
        }
    }

    public static void renderBlockInWorld(final Block block, final int meta, final float blockSize) {
        beginRenderingBlocksInWorld(blockSize);

        addRenderedBlockInWorld(block, meta, 0, 0, 0);

        endRenderingBlocksInWorld();
    }

    public static void renderOuterSpaceShell() {

        // Save current OpenGL state.
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        // Begin animation.
        GL11.glPushMatrix();

        // Disables lighting, so star is always lit.
        GL11.glDisable(GL11.GL_LIGHTING);
        // Merges colors of the various layers of the star.
        // GL11.glEnable(GL11.GL_BLEND);

        // Bind animation to layer of star.
        FMLClientHandler.instance().getClient().getTextureManager()
                .bindTexture(new ResourceLocation(MODID, "models/spaceLayer.png"));

        final float scale = 0.01f * 17.5f;
        // Scale the star up in the x, y and z directions.
        GL11.glScalef(scale, scale, scale);

        GL11.glColor4f(1, 1, 1, 1);

        spaceModel.renderAll();

        // Finish animation.
        GL11.glPopMatrix();

        // Restore previous OpenGL state.
        GL11.glPopAttrib();
    }

}
