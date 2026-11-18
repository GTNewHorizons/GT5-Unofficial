package gtnhintergalactic.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.model.wavefront.WavefrontVBOBuilder;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.ShaderRecipe;
import gregtech.common.render.shader.SharedShaders;
import gregtech.common.render.shader.Uniform;
import gtnhintergalactic.GTNHIntergalactic;
import gtnhintergalactic.block.BlockSpaceElevatorCable;
import gtnhintergalactic.config.IGConfig;
import gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

/**
 * Renderer for the elevator cable
 *
 * Credit for the model goes to Adam Credit for the model's texture and UV go to Jimbno
 *
 * @author minecraft7771
 */
public class RenderSpaceElevatorCable extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    /** Location of the climber texture */
    private static final ResourceLocation climberTexture = new ResourceLocation(
        GTNHIntergalactic.ASSET_PREFIX,
        "textures/models/climber.png");

    /** Model of the climber */
    private static IVertexArrayObject modelCustom;
    /** Offset of the climber from the Space Elevator Cable block */
    private static final int CLIMBER_OFFSET = 50;
    /** Min Y level that the climber should have */
    private static final int MIN_CLIMBER_HEIGHT = 100;

    private static boolean isInitialized = false;
    private static boolean hasFailed = false;

    /** Distance from center to edge of cable octagon */
    private static final float LONG_DISTANCE = (1.0f + Math.sqrt(2.0f)) / 5.4f;
    /** Distance from center to end of parallel side */
    private static final float SHORT_DISTANCE = 1.0f / 5.4f;
    /** Height of the full rendered cable */
    private static final double CABLE_HEIGHT = 512.0;
    /** X edges of the helix */
    private static final float[] edgeX = { LONG_DISTANCE, LONG_DISTANCE, SHORT_DISTANCE, -SHORT_DISTANCE,
        -LONG_DISTANCE, -LONG_DISTANCE, -SHORT_DISTANCE, SHORT_DISTANCE };
    /** Z edges of the helix */
    private static final float[] edgeZ = { SHORT_DISTANCE, -SHORT_DISTANCE, -LONG_DISTANCE, -LONG_DISTANCE,
        -SHORT_DISTANCE, SHORT_DISTANCE, LONG_DISTANCE, LONG_DISTANCE };

    private static final Matrix4fStack modelProjection = new Matrix4fStack(2);
    private static final Matrix4f climberMatrix = new Matrix4f();

    private static final float SIDE = 2.0f / 5.4f;
    private static final float SECTION_HEIGHT = 8 * SIDE;
    private static final int SECTIONS = (int) Math.ceil(CABLE_HEIGHT / SECTION_HEIGHT);
    private static final int VERTEX_COUNT = 48 * 4 * SECTIONS;

    private static ShaderHandle cableShader;
    private static Uniform cableBaseY;
    private static Uniform cableTime;

    public static void reload() {
        isInitialized = false;
        hasFailed = false;
        release();

        if (!SharedShaders.ready()) {
            hasFailed = true;
            return;
        }

        try {
            modelCustom = WavefrontVBOBuilder.compileToVBO(
                new ResourceLocation(GTNHIntergalactic.ASSET_PREFIX, "models/climber.obj"),
                SharedShaders.textured()
                    .vertexFormat());
        } catch (RuntimeException e) {
            GTNHIntergalactic.LOG.error("Failed to load space elevator climber model", e);
            release();
            hasFailed = true;
            return;
        }

        final float minU = BlockSpaceElevatorCable.textures[0].getMinU();
        final float maxU = BlockSpaceElevatorCable.textures[0].getMaxU();
        final float minV = BlockSpaceElevatorCable.textures[0].getMinV();
        final float maxV = BlockSpaceElevatorCable.textures[0].getMaxV();

        final float glowMinU = Math.lerp(minU, maxU, 7f / 16f);
        final float glowMaxU = Math.lerp(minU, maxU, 9f / 16f);
        final float glowMinV = Math.lerp(minV, maxV, 7f / 16f);
        final float glowMaxV = Math.lerp(minV, maxV, 9f / 16f);

        // Atlas coordinates only exist after texture stitch
        final ShaderRecipe cable = ShaderRecipe.of(GTNHIntergalactic.ASSET_PREFIX, "spacecable")
            .required("u_BaseY", "u_Time")
            .constant("u_SectionHeight", SECTION_HEIGHT)
            .constant("u_GlowU", glowMinU, glowMaxU)
            .constant("u_GlowV", glowMinV, glowMaxV)
            .constantArray("u_UV", 2, minU, minV, maxU, maxV)
            .sampler("u_BlockTex", OpenGlHelper.defaultTexUnit - GL13.GL_TEXTURE0)
            .modelUniform("u_ModelProjection")
            .attributeless("vertexId", VERTEX_COUNT);

        cableBaseY = cable.uniform("u_BaseY");
        cableTime = cable.uniform("u_Time");
        cableShader = cable.bake();

        if (!cableShader.isValid()) {
            GTNHIntergalactic.LOG.error("Failed to initialize space elevator cable shader");
            release();
            hasFailed = true;
            return;
        }

        isInitialized = true;
    }

    private static void release() {
        if (cableShader != null) {
            cableShader.release();
            cableShader = null;
        }
        if (modelCustom != null) {
            modelCustom.delete();
            modelCustom = null;
        }
    }

    /**
     * Render the TE to which this render belongs
     *
     * @param tile              TE to be rendered
     * @param x                 X coordinate of the TE
     * @param y                 Y coordinate of the TE
     * @param z                 Z coordinate of the TE
     * @param timeSinceLastTick Time that has passed since the last tick
     */
    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!IGConfig.spaceElevator.isCableRenderingEnabled) return;
        if (!(tile instanceof TileEntitySpaceElevatorCable)) return;
        if (hasFailed || !isInitialized) return;

        final TileEntitySpaceElevatorCable cableTile = (TileEntitySpaceElevatorCable) tile;

        if (!cableTile.shouldRender()) return;

        renderCable(tile, x, y, z, timeSinceLastTick);

        climberMatrix.identity()
            .translate(
                (float) x + 0.5f,
                (float) (y + 0.5
                    + cableTile.getClimberHeight()
                    + ((CLIMBER_OFFSET + cableTile.yCoord) < MIN_CLIMBER_HEIGHT ? MIN_CLIMBER_HEIGHT : CLIMBER_OFFSET)),
                (float) z + 0.5f)
            .rotateY((float) Math.toRadians(cableTile.getClimberRotation()))
            .scale(4);
        renderClimber();
    }

    /**
     * Render the climber
     */
    private void renderClimber() {
        final boolean blendWas = GL11.glGetBoolean(GL11.GL_BLEND);
        final boolean cullWas = GL11.glGetBoolean(GL11.GL_CULL_FACE);
        final long blendFuncWas = RenderState.savedBlendFunc();

        // Initiate open GL for proper climber rendering
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(climberTexture);

        final ShaderHandle shader = SharedShaders.textured();
        shader.use();
        GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 1f);
        shader.uploadModel(climberMatrix);
        modelCustom.render();
        ShaderProgram.clear();

        // Reset open GL
        RenderState.restore(GL11.GL_BLEND, blendWas);
        RenderState.restore(GL11.GL_CULL_FACE, cullWas);
        RenderState.restoreBlendFunc(blendFuncWas);
    }

    /**
     * Render the cable of the Space Elevator
     *
     * @param x X coordinate of the block
     * @param y Y coordinate of the block
     * @param z Z coordinate of the block
     */
    private void renderCable(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        this.bindTexture(TextureMap.locationBlocksTexture);

        cableShader.use();
        GL20.glUniform1f(
            cableShader.loc(cableTime),
            ((tile.getWorldObj()
                .getWorldInfo()
                .getWorldTotalTime() % 60) + timeSinceLastTick) / 60f);
        GL20.glUniform1i(cableShader.loc(cableBaseY), (int) y - 23);

        modelProjection.identity();
        modelProjection.translate((float) x, (float) (y - 23), (float) z);

        final boolean cullWas = GL11.glGetBoolean(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_CULL_FACE);
        cableShader.uploadModel(modelProjection);

        cableShader.draw();

        RenderState.restore(GL11.GL_CULL_FACE, cullWas);
        ShaderProgram.clear();
    }

    /**
     * Render a block in an inventory slot
     *
     * @param block    Block that should be rendered
     * @param metadata Meta data of the block
     * @param modelId  ID of the blocks model
     * @param renderer Used renderer
     */
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glPushMatrix();
        // Get icons from custom register (useful for renderers and fluids)
        IIcon side = BlockSpaceElevatorCable.textures[0];
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, side);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, side);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, side);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, side);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, side);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, side);
        tes.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glPopMatrix();
    }

    /**
     * Render a block in the world
     *
     * @param world    World in which the block should be rendered
     * @param x        X coordinate of the block
     * @param y        Y coordinate of the block
     * @param z        Z coordinate of the block
     * @param block    Block that should be rendered
     * @param modelId  ID of the blocks model
     * @param renderer Used renderer
     * @return True if the block was rendered, else false
     */
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntitySpaceElevatorCable) {
            if (!((TileEntitySpaceElevatorCable) te).shouldRender()) {
                GL11.glPushMatrix();
                // Get icons from custom register (useful for renderers and fluids)
                IIcon side = BlockSpaceElevatorCable.textures[0];
                float minU = side.getMinU();
                float maxU = side.getMaxU();
                float minV = side.getMinV();
                float maxV = side.getMaxV();
                Tessellator tes = Tessellator.instance;
                // spotless:off

                tes.addVertexWithUV(x    , y    , z    , maxU, maxV);
                tes.addVertexWithUV(x    , y + 1, z    , maxU, minV);
                tes.addVertexWithUV(x + 1, y + 1, z    , minU, minV);
                tes.addVertexWithUV(x + 1, y    , z    , minU, maxV);

                tes.addVertexWithUV(x + 1, y    , z + 1, maxU, maxV);
                tes.addVertexWithUV(x + 1, y + 1, z + 1, maxU, minV);
                tes.addVertexWithUV(x    , y + 1, z + 1, minU, minV);
                tes.addVertexWithUV(x    , y    , z + 1, minU, maxV);

                tes.addVertexWithUV(x    , y    , z + 1, maxU, maxV);
                tes.addVertexWithUV(x    , y + 1, z + 1, maxU, minV);
                tes.addVertexWithUV(x    , y + 1, z    , minU, minV);
                tes.addVertexWithUV(x    , y    , z    , minU, maxV);

                tes.addVertexWithUV(x + 1, y    , z    , maxU, maxV);
                tes.addVertexWithUV(x + 1, y + 1, z    , maxU, minV);
                tes.addVertexWithUV(x + 1, y + 1, z + 1, minU, minV);
                tes.addVertexWithUV(x + 1, y    , z + 1, minU, maxV);

                tes.addVertexWithUV(x + 1, y    , z    , maxU, maxV);
                tes.addVertexWithUV(x + 1, y    , z + 1, maxU, minV);
                tes.addVertexWithUV(x    , y    , z + 1, minU, minV);
                tes.addVertexWithUV(x    , y    , z    , minU, maxV);

                tes.addVertexWithUV(x    , y + 1, z    , maxU, maxV);
                tes.addVertexWithUV(x    , y + 1, z + 1, maxU, minV);
                tes.addVertexWithUV(x + 1, y + 1, z + 1, minU, minV);
                tes.addVertexWithUV(x + 1, y + 1, z    , minU, maxV);
                // spotless:on
                GL11.glPopMatrix();
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the block with this renderer should be rendered 3D in an inventory
     *
     * @param modelId ID of the model that will be checked
     * @return True if it should be rendered in 3D, else false
     */
    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    /**
     * Get the assigned render ID of this renderer
     *
     * @return Render ID
     */
    @Override
    public int getRenderId() {
        return BlockSpaceElevatorCable.renderID;
    }
}
