package tectech.rendering.EOH;

import static tectech.Reference.MODID;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_0;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_1;
import static tectech.rendering.EOH.EOHTileEntitySR.STAR_LAYER_2;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.client.FMLClientHandler;

public abstract class EOHRenderingUtils {

    private static final Color EOHStarColour = new Color(1.0f, 0.4f, 0.05f, 1.0f);

    public static void renderEOHStar(IItemRenderer.ItemRenderType type, float partialTicks, double starRadius) {
        renderStar(type, EOHStarColour, partialTicks, starRadius);
    }

    // Used for GORGE item renderer only.
    private static final Color GORGEStarColour = new Color(1.0f, 1.0f, 1.0f, 1.0f);

    public static void renderGORGEStar(IItemRenderer.ItemRenderType type, float partialTicks, double starRadius) {
        renderStar(type, GORGEStarColour, partialTicks, starRadius);
    }

    private static void renderStar(IItemRenderer.ItemRenderType type, Color color, float partialTicks,
        double starRadius) {
        GL11.glPushMatrix();

        if (type == IItemRenderer.ItemRenderType.INVENTORY) GL11.glRotated(180, 0, 1, 0);
        else if (type == IItemRenderer.ItemRenderType.EQUIPPED
            || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glTranslated(0.5, 0.5, 0.5);
                if (type == IItemRenderer.ItemRenderType.EQUIPPED) GL11.glRotated(90, 0, 1, 0);
            }

        // Render star stuff.
        renderStarLayer(0, STAR_LAYER_0, color, 1.0f, partialTicks, starRadius);
        renderStarLayer(1, STAR_LAYER_1, color, 0.4f, partialTicks, starRadius);
        renderStarLayer(2, STAR_LAYER_2, color, 0.2f, partialTicks, starRadius);

        GL11.glPopMatrix();
    }

    private static void renderStarLayer(int layer, ResourceLocation texture, Color color, float alpha,
        float partialTicks, double starRadius) {

        if (layer >= 3) throw new IllegalArgumentException("Star rendering only supports three layers.");

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        if (alpha < 1.0f) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        } else {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(texture);

        float[] rotationSpeeds = { 1.5f, 1.2f, 1.6f };
        float[] baseRotations = { 130f, -49f, 67f };

        float rotation = (baseRotations[layer] + rotationSpeeds[layer] * partialTicks) % 360f;

        switch (layer) {
            case 0 -> GL11.glRotatef(rotation, 0F, 1F, 1F);
            case 1 -> GL11.glRotatef(rotation, 1F, 1F, 0F);
            case 2 -> GL11.glRotatef(rotation, 1F, 0F, 1F);
        }

        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);

        int maxLayer = 2;
        double scale = starRadius * Math.pow(0.95f, maxLayer - layer);
        renderTessellatedSphere(64, 64, scale);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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

    public static void renderTessellatedSphere(int slices, int stacks, double radiusInBlocks) {
        renderCachedSphereVBO(slices, stacks, radiusInBlocks, false);
    }

    private static void renderTessellatedSphereWithInvertedNormals(int slices, int stacks, double radiusInBlocks) {
        renderCachedSphereVBO(slices, stacks, radiusInBlocks, true);
    }

    public static void renderOuterSpaceShell(double playerDistance) {

        // Save current OpenGL state.
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        // Begin animation.
        GL11.glPushMatrix();

        // Disables lighting, so star is always lit.
        GL11.glDisable(GL11.GL_LIGHTING);
        // Merges colors of the various layers of the star.
        // GL11.glEnable(GL11.GL_BLEND);

        // Bind animation to layer of star.
        FMLClientHandler.instance()
            .getClient()
            .getTextureManager()
            .bindTexture(new ResourceLocation(MODID, "models/spaceLayer.png"));

        final float scale = 0.01f * 17.5f;
        // Scale the star up in the x, y and z directions.
        GL11.glScalef(scale, scale, scale);

        GL11.glColor4f(1, 1, 1, 1);

        // Render the sphere object itself dynamically.
        renderTessellatedSphereWithInvertedNormals(64, 64, 74);

        // Finish animation.
        GL11.glPopMatrix();

        // Restore previous OpenGL state.
        GL11.glPopAttrib();
    }

    private static void renderCachedSphereVBO(int slices, int stacks, double radiusInBlocks, boolean invertFrontFace) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        boolean wasCull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        int oldCullMode = GL11.glGetInteger(GL11.GL_CULL_FACE_MODE);
        int oldFrontFace = GL11.glGetInteger(GL11.GL_FRONT_FACE);

        GL11.glEnable(GL11.GL_CULL_FACE);

        if (invertFrontFace) {
            GL11.glFrontFace(GL11.GL_CW);
            GL11.glCullFace(GL11.GL_BACK);
        } else {
            GL11.glFrontFace(oldFrontFace);
            GL11.glCullFace(GL11.GL_FRONT);
        }

        IVertexArrayObject vbo = SphereVBOCache.getOrCreate(slices, stacks);

        GL11.glPushMatrix();
        int inversionMultiplier = invertFrontFace ? -1 : 1;
        GL11.glScaled(inversionMultiplier * radiusInBlocks, radiusInBlocks, radiusInBlocks);
        vbo.render();
        GL11.glPopMatrix();

        GL11.glFrontFace(oldFrontFace);
        GL11.glCullFace(oldCullMode);
        if (!wasCull) GL11.glDisable(GL11.GL_CULL_FACE);
    }

}
