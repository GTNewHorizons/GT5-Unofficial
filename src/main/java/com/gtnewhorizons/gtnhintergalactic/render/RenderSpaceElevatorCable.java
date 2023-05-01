package com.gtnewhorizons.gtnhintergalactic.render;

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

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.gtnhintergalactic.GTNHIntergalactic;
import com.gtnewhorizons.gtnhintergalactic.block.BlockSpaceElevatorCable;
import com.gtnewhorizons.gtnhintergalactic.config.Config;
import com.gtnewhorizons.gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

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

        {
            GL11.glPushMatrix();
            renderCable(x, y, z);
            GL11.glPopMatrix();
        }

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
    private void renderCable(double x, double y, double z) {
        // Initiate open GL for proper cable rendering
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        Tessellator tessellator = Tessellator.instance;
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // Draw the cable
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(1F, 1F, 1F);
        renderFullHelix(tessellator, x, y, z);
        tessellator.draw();
        // Reset open GL
        GL11.glDisable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
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
     * Render the motor glow of the Space Elevator
     *
     * @param tes  Tessellator used for rendering
     * @param x    X coordinate of the cable
     * @param y    Y coordinate of the cable
     * @param z    Z coordinate of the cable
     * @param minU Minimum U coordinate of the texture
     * @param maxU Maximum U coordinate of the texture
     * @param minV Minimum V coordinate of the texture
     * @param maxV Maximum V coordinate of the texture
     */
    private void motorGlow(Tessellator tes, double x, double y, double z, double minU, double maxU, double minV,
            double maxV) {
        // spotless:off
        tes.addVertexWithUV(x + 0.5f - 2.51f, y - 22f,          z + 0.5f + 0.5f, minU, maxV);
        tes.addVertexWithUV(x + 0.5f - 2.51f, y + 0.5f + 0.5f,  z + 0.5f + 0.5f, minU, minV);
        tes.addVertexWithUV(x + 0.5f - 2.51f, y + 0.5f + 0.5f,  z + 0.5f - 0.5f, maxU, minV);
        tes.addVertexWithUV(x + 0.5f - 2.51f, y - 22f,          z + 0.5f - 0.5f, maxU, maxV);

        tes.addVertexWithUV(x + 0.5f + 2.51f, y - 22f,          z + 0.5f - 0.5f, minU, maxV);
        tes.addVertexWithUV(x + 0.5f + 2.51f, y + 0.5f + 0.5f,  z + 0.5f - 0.5f, minU, minV);
        tes.addVertexWithUV(x + 0.5f + 2.51f, y + 0.5f + 0.5f,  z + 0.5f + 0.5f, maxU, minV);
        tes.addVertexWithUV(x + 0.5f + 2.51f, y - 22f,          z + 0.5f + 0.5f, maxU, maxV);

        tes.addVertexWithUV(x + 0.5f + 0.5f, y - 22f,          z + 0.5f + 2.51f, minU, maxV);
        tes.addVertexWithUV(x + 0.5f + 0.5f, y + 0.5f + 0.5f,  z + 0.5f + 2.51f, minU, minV);
        tes.addVertexWithUV(x + 0.5f - 0.5f, y + 0.5f + 0.5f,  z + 0.5f + 2.51f, maxU, minV);
        tes.addVertexWithUV(x + 0.5f - 0.5f, y - 22f,          z + 0.5f + 2.51f, maxU, maxV);

        tes.addVertexWithUV(x + 0.5f - 0.5f, y - 22f,          z + 0.5f - 2.51f, minU, maxV);
        tes.addVertexWithUV(x + 0.5f - 0.5f, y + 0.5f + 0.5f,  z + 0.5f - 2.51f, minU, minV);
        tes.addVertexWithUV(x + 0.5f + 0.5f, y + 0.5f + 0.5f,  z + 0.5f - 2.51f, maxU, minV);
        tes.addVertexWithUV(x + 0.5f + 0.5f, y - 22f,          z + 0.5f - 2.51f, maxU, maxV);
        //spotless:on
    }

    /**
     * Draw a clockwise helix part of the cable
     *
     * @param tes    Tessellator used for rendering
     * @param x      X coordinate of the cable
     * @param y      Y coordinate of the cable
     * @param z      Z coordinate of the cable
     * @param offset Vertical offset
     * @param side   Side of the helix
     * @param width  Width of the helix
     * @param minU   Minimum U coordinate of the texture
     * @param maxU   Maximum U coordinate of the texture
     * @param minV   Minimum V coordinate of the texture
     * @param maxV   Maximum V coordinate of the texture
     */
    private void clockwiseHelixPart(Tessellator tes, double x, double y, double z, int offset, double side,
            double width, double minU, double maxU, double minV, double maxV) {

        double sectionHeight = 8 * side;
        int sections = (int) Math.ceil(CABLE_HEIGHT / sectionHeight);

        for (int i = 0; i < 8 * sections; i++) {
            int j = (i + offset) % 8;
            int k = (i + 1 + offset) % 8;
            if (i % 4 == 0) {
                // Light section
                IIcon cableLight = BlockSpaceElevatorCable.textures[2 + ((i / 4) % 80)];
                GTNHIntergalactic.instance.markTextureUsed(cableLight);
                double lightMinU = cableLight.getMinU();
                double lightMaxU = cableLight.getMaxU();
                double lightMinV = cableLight.getMinV();
                double lightMaxV = cableLight.getMaxV();

                tes.addVertexWithUV(
                        x + 0.5f + edgeX[k],
                        y + side * i + side,
                        z + 0.5f + edgeZ[k],
                        lightMinU,
                        lightMaxV);
                tes.addVertexWithUV(
                        x + 0.5f + edgeX[k],
                        y + side * i + (side + width),
                        z + 0.5f + edgeZ[k],
                        lightMinU,
                        lightMinV);
                tes.addVertexWithUV(
                        x + 0.5f + edgeX[j],
                        y + side * i + width,
                        z + 0.5f + edgeZ[j],
                        lightMaxU,
                        lightMinV);
                tes.addVertexWithUV(x + 0.5f + edgeX[j], y + side * i, z + 0.5f + edgeZ[j], lightMaxU, lightMaxV);
            } else {
                tes.addVertexWithUV(x + 0.5f + edgeX[k], y + side * i + side, z + 0.5f + edgeZ[k], minU, maxV);
                tes.addVertexWithUV(
                        x + 0.5f + edgeX[k],
                        y + side * i + (side + width),
                        z + 0.5f + edgeZ[k],
                        minU,
                        minV);
                tes.addVertexWithUV(x + 0.5f + edgeX[j], y + side * i + width, z + 0.5f + edgeZ[j], maxU, minV);
                tes.addVertexWithUV(x + 0.5f + edgeX[j], y + side * i, z + 0.5f + edgeZ[j], maxU, maxV);
            }

            // Inner side
            tes.addVertexWithUV(x + 0.5f + edgeX[j], y + side * i, z + 0.5f + edgeZ[j], maxU, maxV);
            tes.addVertexWithUV(x + 0.5f + edgeX[j], y + side * i + width, z + 0.5f + edgeZ[j], maxU, minV);
            tes.addVertexWithUV(x + 0.5f + edgeX[k], y + side * i + (side + width), z + 0.5f + edgeZ[k], minU, minV);
            tes.addVertexWithUV(x + 0.5f + edgeX[k], y + side * i + side, z + 0.5f + edgeZ[k], minU, maxV);
        }
    }

    /**
     * Render the cable helix of the Space Elevator
     *
     * @param tes Used tessellator for rendering
     * @param x   X coordinate of the cable
     * @param y   Y coordinate of the cable
     * @param z   Z coordinate of the cable
     */
    private void renderFullHelix(Tessellator tes, double x, double y, double z) {
        this.bindTexture(TextureMap.locationBlocksTexture);
        IIcon cablePart = BlockSpaceElevatorCable.textures[0];
        IIcon motorGlow = BlockSpaceElevatorCable.motorGlow;
        double minU = cablePart.getMinU();
        double maxU = cablePart.getMaxU();
        double minV = cablePart.getMinV();
        double maxV = cablePart.getMaxV();

        double motorGlowMinU = motorGlow.getMinU();
        double motorGlowMaxU = motorGlow.getMaxU();
        double motorGlowMinV = motorGlow.getMinV();
        double motorGlowMaxV = motorGlow.getMaxV();

        clockwiseHelixPart(tes, x, y - 23.0, z, 0, 2.0 / 5.4, 0.75, minU, maxU, minV, maxV);
        clockwiseHelixPart(tes, x, y - 23.0, z, 2, 2.0 / 5.4, 0.75, minU, maxU, minV, maxV);
        clockwiseHelixPart(tes, x, y - 23.0, z, 4, 2.0 / 5.4, 0.75, minU, maxU, minV, maxV);
        clockwiseHelixPart(tes, x, y - 23.0, z, 6, 2.0 / 5.4, 0.75, minU, maxU, minV, maxV);

        motorGlow(tes, x, y, z, motorGlowMinU, motorGlowMaxU, motorGlowMinV, motorGlowMaxV);
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
