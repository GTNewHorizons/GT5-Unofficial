package com.gtnewhorizons.gtnhintergalactic.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.apache.logging.log4j.Level;
import org.joml.Math;
import org.joml.Matrix4fStack;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizons.gtnhintergalactic.GTNHIntergalactic;
import com.gtnewhorizons.gtnhintergalactic.block.BlockSpaceElevatorCable;
import com.gtnewhorizons.gtnhintergalactic.config.Config;
import com.gtnewhorizons.gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.FMLLog;

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
    private static IModelCustom modelCustom;
    /** Offset of the climber from the Space Elevator Cable block */
    private static final int CLIMBER_OFFSET = 50;
    /** Min Y level that the climber should have */
    private static final int MIN_CLIMBER_HEIGHT = 100;

    /** Used for lazy loading of the shader and buffer objects */
    boolean isInitialized = false;

    /** Distance from center to edge of cable octagon */
    private static final float LONG_DISTANCE = (float) (1.0f + Math.sqrt(2.0f)) / 5.4f;
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

    private static int cableProgram;
    private static int uModelProjectionMatrix;
    private static int uBlockTex;
    private static int uSectionHeight;
    private static int uTime;
    private static int uBaseY;
    private static int uGlowU;
    private static int uGlowV;
    private static int uUV;
    private static int aVertexID = -1;
    private static int vertexIDBuffer = -1;

    private static final FloatBuffer bufModelViewProjection = BufferUtils.createFloatBuffer(16);
    private static final Matrix4fStack modelProjection = new Matrix4fStack(2);

    private static final float SIDE = 2.0f / 5.4f;
    private static final float SECTION_HEIGHT = 8 * SIDE;
    private static final int SECTIONS = (int) Math.ceil(CABLE_HEIGHT / SECTION_HEIGHT);
    private static final int VERTEX_COUNT = 48 * 4 * SECTIONS;

    private static String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();
        InputStream in = RenderSpaceElevatorCable.class.getResourceAsStream(filename);
        Exception exception = null;
        BufferedReader reader;

        if (in == null) return "";

        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Exception innerExc = null;
            try {
                String line;
                while ((line = reader.readLine()) != null) source.append(line).append('\n');
            } catch (Exception exc) {
                exception = exc;
            } finally {
                try {
                    reader.close();
                } catch (Exception exc) {
                    if (innerExc == null) innerExc = exc;
                    else exc.printStackTrace(System.err);
                }
            }

            if (innerExc != null) throw innerExc;
        } catch (Exception exc) {
            exception = exc;
        } finally {
            try {
                in.close();
            } catch (Exception exc) {
                if (exception == null) exception = exc;
                else exc.printStackTrace(System.err);
            }

            if (exception != null) throw exception;
        }

        return source.toString();
    }

    private static String getLogInfo(int obj) {
        return GL20.glGetShaderInfoLog(obj, GL20.glGetShaderi(obj, GL20.GL_INFO_LOG_LENGTH));
    }

    private static int createProgram(String vert, String frag) {
        int vertId = 0, fragId = 0, program;
        if (vert != null) vertId = createShader(vert, GL20.GL_VERTEX_SHADER);
        if (frag != null) fragId = createShader(frag, GL20.GL_FRAGMENT_SHADER);

        program = GL20.glCreateProgram();
        if (program == 0) return 0;

        if (vert != null) GL20.glAttachShader(program, vertId);
        if (frag != null) GL20.glAttachShader(program, fragId);

        GL20.glLinkProgram(program);
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            FMLLog.log(Level.ERROR, getLogInfo(program));
            return 0;
        }

        GL20.glValidateProgram(program);
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            FMLLog.log(Level.ERROR, getLogInfo(program));
            return 0;
        }

        return program;
    }

    private static int createShader(String filename, int shaderType) {
        int shader = 0;
        try {
            shader = GL20.glCreateShader(shaderType);

            if (shader == 0) return 0;

            GL20.glShaderSource(shader, readFileAsString(filename));
            GL20.glCompileShader(shader);

            if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

            return shader;
        } catch (Exception e) {
            GL20.glDeleteProgram(shader);
            e.printStackTrace(System.err);
            return -1;
        }
    }

    /**
     * Create a new render for the space elevator cable
     */
    public RenderSpaceElevatorCable() {
        modelCustom = AdvancedModelLoader
                .loadModel(new ResourceLocation(GTNHIntergalactic.ASSET_PREFIX, "models/climber.obj"));
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
        if (!Config.isCableRenderingEnabled) return;
        if (!(tile instanceof TileEntitySpaceElevatorCable)) return;

        TileEntitySpaceElevatorCable cableTile = (TileEntitySpaceElevatorCable) tile;

        if (!cableTile.shouldRender()) return;

        renderCable(tile, x, y, z, timeSinceLastTick);

        {
            GL11.glPushMatrix();
            // If the Space Elevator is build on a low Y level the climber should reach a minimum height
            GL11.glTranslated(
                    x + 0.5,
                    y + 0.5
                            + cableTile.getClimberHeight()
                            + ((CLIMBER_OFFSET + cableTile.yCoord) < MIN_CLIMBER_HEIGHT ? MIN_CLIMBER_HEIGHT
                                    : CLIMBER_OFFSET),
                    z + 0.5);
            GL11.glRotated(cableTile.getClimberRotation(), 0.0, 1.0, 0.0);
            renderClimber();
            GL11.glPopMatrix();
        }
    }

    /**
     * Render the climber
     */
    private void renderClimber() {
        // Initiate open GL for proper climber rendering
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(climberTexture);
        GL11.glScaled(4, 4, 4);
        // Draw the climber
        modelCustom.renderAll();
        // Reset open GL
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LIGHTING);
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

        if (!isInitialized) {
            // Draw the cable
            final float minU = BlockSpaceElevatorCable.textures[0].getMinU();
            final float maxU = BlockSpaceElevatorCable.textures[0].getMaxU();
            final float minV = BlockSpaceElevatorCable.textures[0].getMinV();
            final float maxV = BlockSpaceElevatorCable.textures[0].getMaxV();

            final float glowMinU = Math.lerp(minU, maxU, 7f / 16f);
            final float glowMaxU = Math.lerp(minU, maxU, 9f / 16f);
            final float glowMinV = Math.lerp(minV, maxV, 7f / 16f);
            final float glowMaxV = Math.lerp(minV, maxV, 9f / 16f);

            cableProgram = createProgram(
                    "/assets/gtnhintergalactic/shaders/spacecable.vert.glsl",
                    "/assets/gtnhintergalactic/shaders/spacecable.frag.glsl");
            GL20.glUseProgram(cableProgram);

            aVertexID = GL20.glGetAttribLocation(cableProgram, "vertexId");

            uModelProjectionMatrix = GL20.glGetUniformLocation(cableProgram, "u_ModelProjection");
            uBlockTex = GL20.glGetUniformLocation(cableProgram, "u_BlockTex");
            uSectionHeight = GL20.glGetUniformLocation(cableProgram, "u_SectionHeight");
            uTime = GL20.glGetUniformLocation(cableProgram, "u_Time");
            uBaseY = GL20.glGetUniformLocation(cableProgram, "u_BaseY");
            uGlowU = GL20.glGetUniformLocation(cableProgram, "u_GlowU");
            uGlowV = GL20.glGetUniformLocation(cableProgram, "u_GlowV");
            uUV = GL20.glGetUniformLocation(cableProgram, "u_UV");

            vertexIDBuffer = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexIDBuffer);
            // Make a 3x-sized buffer to act as a vec3 position input to the shader.
            // Without a position input, the draw causes undefined behaviour in GL 2.x
            final ByteBuffer vertexIDData = BufferUtils.createByteBuffer(VERTEX_COUNT * 4 * 3);
            for (int i = 0; i < VERTEX_COUNT * 3; i++) {
                vertexIDData.putFloat(i * 4, (float) i);
            }
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexIDData, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            final FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(4);
            uvBuffer.put(0, minU);
            uvBuffer.put(1, minV);
            uvBuffer.put(2, maxU);
            uvBuffer.put(3, maxV);

            GL20.glUniform1f(uSectionHeight, SECTION_HEIGHT);
            GL20.glUniform1i(uBlockTex, OpenGlHelper.defaultTexUnit - GL13.GL_TEXTURE0);
            GL20.glUniform2f(uGlowU, glowMinU, glowMaxU);
            GL20.glUniform2f(uGlowV, glowMinV, glowMaxV);
            GL20.glUniform2(uUV, uvBuffer);

            GL20.glUseProgram(0);

            isInitialized = true;

        }

        GL20.glUseProgram(cableProgram);
        GL20.glUniform1f(
                uTime,
                ((tile.getWorldObj().getWorldInfo().getWorldTotalTime() % 60) + timeSinceLastTick) / 60f);
        GL20.glUniform1i(uBaseY, (int) y - 23);

        modelProjection.identity();
        modelProjection.translate((float) x, (float) (y - 23), (float) z);

        GL11.glDisable(GL11.GL_CULL_FACE);
        modelProjection.get(0, bufModelViewProjection);
        GL20.glUniformMatrix4(uModelProjectionMatrix, false, bufModelViewProjection);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexIDBuffer);
        GL20.glVertexAttribPointer(aVertexID, 1, GL11.GL_FLOAT, false, 0, 0);
        GL20.glEnableVertexAttribArray(aVertexID);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, VERTEX_COUNT);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL20.glDisableVertexAttribArray(aVertexID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL20.glUseProgram(0);
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
        return BlockSpaceElevatorCable.getRenderID();
    }
}
