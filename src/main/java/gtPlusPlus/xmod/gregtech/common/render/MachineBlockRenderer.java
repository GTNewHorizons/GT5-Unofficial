package gtPlusPlus.xmod.gregtech.common.render;

import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_DOWN;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_EAST;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_NORTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_SOUTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_UP;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_WEST;
import static gregtech.api.interfaces.metatileentity.IConnectable.NO_CONNECTION;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.EnumMap;
import java.util.EnumSet;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.util.LightingHelper;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.render.GTRendererBlock;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;
import gtPlusPlus.xmod.gregtech.common.helpers.GTMethodHelper;

public class MachineBlockRenderer extends GTRendererBlock {

    public static MachineBlockRenderer INSTANCE;
    public final int mRenderID = RenderingRegistry.getNextAvailableRenderId();

    public MachineBlockRenderer() {
        INSTANCE = this;
        RenderingRegistry.registerBlockHandler(this);
    }

    private static ITexture[] getTexture(IMetaTileEntity tile, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean arg5) {
        final IGregTechTileEntity gtTile = tile.getBaseMetaTileEntity();
        return tile.getTexture(gtTile, side, facing, (byte) colorIndex, active, arg5);
    }

    private static ITexture[] getTexture(IMetaTileEntity tile, ForgeDirection side, int facingMask, int colorIndex,
        boolean active, boolean arg5) {
        final MetaPipeEntity gtTile = (MetaPipeEntity) tile.getBaseMetaTileEntity();
        return gtTile.getTexture((IGregTechTileEntity) tile, side, facingMask, colorIndex, active, arg5);
    }

    // spotless:off
    private static void renderNormalInventoryMetaTileEntity(Block aBlock, int aMeta, RenderBlocks aRenderer, LightingHelper lightingHelper) {
        if (aMeta > 0 && aMeta < GregTechAPI.METATILEENTITIES.length) {
            IMetaTileEntity tMetaTileEntity = GregTechAPI.METATILEENTITIES[aMeta];
            if (tMetaTileEntity != null) {
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                final Tessellator tess = Tessellator.instance;
                if (tMetaTileEntity.getBaseMetaTileEntity() instanceof IPipeRenderedTileEntity pipeRenderedTile) {
                    float tThickness = pipeRenderedTile.getThickNess();
                    float sp = (1.0F - tThickness) / 2.0F;
                    aBlock.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    renderNegativeYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, DOWN, 0b001001, -1, false, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    renderPositiveYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, UP, 0b001001, -1, false, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    renderNegativeZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.NORTH, 0b001001, -1, false, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    renderPositiveZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.SOUTH, 0b001001, -1, false, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    renderNegativeXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.WEST, 0b001001, -1, true, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    renderPositiveXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.EAST, 0b001001, -1, true, false), true, -1);
                    tess.draw();
                } else {
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    renderNegativeYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, DOWN, ForgeDirection.WEST, -1, true, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    renderPositiveYFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, UP, ForgeDirection.WEST, -1, true, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    renderNegativeZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.NORTH, ForgeDirection.WEST, -1, true, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    renderPositiveZFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.SOUTH, ForgeDirection.WEST, -1, true, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    renderNegativeXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.WEST, ForgeDirection.WEST, -1, true, false), true, -1);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    renderPositiveXFacing(null, aRenderer, lightingHelper, aBlock, 0, 0, 0, getTexture(tMetaTileEntity, ForgeDirection.EAST, ForgeDirection.WEST, -1, true, false), true, -1);
                    tess.draw();
                }

                aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
    }
    // spotless:on

    public boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
        RenderBlocks aRenderer) {
        final TileEntity te = aWorld.getTileEntity(aX, aY, aZ);
        return te instanceof ITexturedTileEntity && renderStandardBlock(
            aWorld,
            aX,
            aY,
            aZ,
            aBlock,
            aRenderer,
            new ITexture[][] { GTMethodHelper.getTexture(te, aBlock, DOWN), GTMethodHelper.getTexture(te, aBlock, UP),
                GTMethodHelper.getTexture(te, aBlock, ForgeDirection.NORTH),
                GTMethodHelper.getTexture(te, aBlock, ForgeDirection.SOUTH),
                GTMethodHelper.getTexture(te, aBlock, ForgeDirection.WEST),
                GTMethodHelper.getTexture(te, aBlock, ForgeDirection.EAST) });
    }

    public boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
        RenderBlocks aRenderer, ITexture[][] aTextures) {
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        final LightingHelper lightingHelper = new LightingHelper(aRenderer);
        final int worldRenderPass = ForgeHooksClient.getWorldRenderPass();
        renderNegativeYFacing(
            aWorld,
            aRenderer,
            lightingHelper,
            aBlock,
            aX,
            aY,
            aZ,
            aTextures[DOWN.ordinal()],
            true,
            worldRenderPass);
        renderPositiveYFacing(
            aWorld,
            aRenderer,
            lightingHelper,
            aBlock,
            aX,
            aY,
            aZ,
            aTextures[UP.ordinal()],
            true,
            worldRenderPass);
        renderNegativeZFacing(
            aWorld,
            aRenderer,
            lightingHelper,
            aBlock,
            aX,
            aY,
            aZ,
            aTextures[ForgeDirection.NORTH.ordinal()],
            true,
            worldRenderPass);
        renderPositiveZFacing(
            aWorld,
            aRenderer,
            lightingHelper,
            aBlock,
            aX,
            aY,
            aZ,
            aTextures[ForgeDirection.SOUTH.ordinal()],
            true,
            worldRenderPass);
        renderNegativeXFacing(
            aWorld,
            aRenderer,
            lightingHelper,
            aBlock,
            aX,
            aY,
            aZ,
            aTextures[ForgeDirection.WEST.ordinal()],
            true,
            worldRenderPass);
        renderPositiveXFacing(
            aWorld,
            aRenderer,
            lightingHelper,
            aBlock,
            aX,
            aY,
            aZ,
            aTextures[ForgeDirection.EAST.ordinal()],
            true,
            worldRenderPass);
        return true;
    }

    // spotless:off
    public boolean renderPipeBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, IPipeRenderedTileEntity aTileEntity, RenderBlocks aRenderer, LightingHelper lightingHelper, int worldRenderPass) {
        final int aConnections = aTileEntity.getConnections();
        float tThickness = aTileEntity.getThickNess();
        if (tThickness >= 0.99F) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
        } else {
            float sp = (1.0F - tThickness) / 2.0F;
            int connexionSidesBits = 0;

            for (int ordinalSide = 0; ordinalSide < 6; ++ordinalSide) {
                if ((aConnections & 1 << ordinalSide) != 0) {
                    connexionSidesBits = connexionSidesBits | 1 << (ordinalSide + 2) % 6;
                }
            }

            final EnumSet<ForgeDirection> coveredSides = EnumSet.noneOf(ForgeDirection.class);

            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (aTileEntity.hasCoverAtSide(side)) coveredSides.add(side);
            }

            if (coveredSides.containsAll(EnumSet.of(DOWN, UP, NORTH, SOUTH, WEST, EAST))) {
                return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
            } else {
                final EnumMap<ForgeDirection, ITexture[]> texture = new EnumMap<>(ForgeDirection.class);
                final EnumMap<ForgeDirection, ITexture[]> textureUncovered = new EnumMap<>(ForgeDirection.class);

                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    texture.put(side, GTMethodHelper.getTexture((TileEntity) aTileEntity, aBlock, side));
                    textureUncovered.put(side, aTileEntity.getTextureUncovered(side));
                }

                switch (connexionSidesBits) {
                    case NO_CONNECTION -> {
                        aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                    }
                    case (CONNECTED_DOWN | CONNECTED_UP) -> {
                        aBlock.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                        if (!coveredSides.contains(WEST)) {
                            renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                        }
                        if (!coveredSides.contains(EAST)) {
                            renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                        }
                    }
                    case (CONNECTED_NORTH | CONNECTED_SOUTH) -> {
                        aBlock.setBlockBounds(sp, 0.0F, sp, sp + tThickness, 1.0F, sp + tThickness);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                        if (!coveredSides.contains(DOWN)) {
                            renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                        }
                        if (!coveredSides.contains(UP)) {
                            renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                        }
                    }
                    case (CONNECTED_WEST | CONNECTED_EAST) -> {
                        aBlock.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, 1.0F);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                        if (!coveredSides.contains(NORTH)) {
                            renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                        }
                        if (!coveredSides.contains(SOUTH)) {
                            renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                        }
                    }
                    default -> {
                        if ((connexionSidesBits & CONNECTED_DOWN) == 0) {
                            aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                        } else {
                            aBlock.setBlockBounds(0.0F, sp, sp, sp, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                            renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                            renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                            renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                            if (!coveredSides.contains(WEST)) {
                                renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_UP) == 0) {
                            aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                        } else {
                            aBlock.setBlockBounds(sp + tThickness, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                            renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                            renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                            renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                            if (!coveredSides.contains(EAST)) {
                                renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_NORTH) == 0) {
                            aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                        } else {
                            aBlock.setBlockBounds(sp, 0.0F, sp, sp + tThickness, sp, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                            renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                            renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                            renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                            if (!coveredSides.contains(DOWN)) {
                                renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_SOUTH) == 0) {
                            aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                        } else {
                            aBlock.setBlockBounds(sp, sp + tThickness, sp, sp + tThickness, 1.0F, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                            renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                            renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                            renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                            if (!coveredSides.contains(UP)) {
                                renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_WEST) == 0) {
                            aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                        } else {
                            aBlock.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, sp);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                            renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                            renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                            renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                            if (!coveredSides.contains(NORTH)) {
                                renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(NORTH), false, worldRenderPass);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_EAST) == 0) {
                            aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                        } else {
                            aBlock.setBlockBounds(sp, sp, sp + tThickness, sp + tThickness, sp + tThickness, 1.0F);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(DOWN), false, worldRenderPass);
                            renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(UP), false, worldRenderPass);
                            renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(WEST), false, worldRenderPass);
                            renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(EAST), false, worldRenderPass);
                            if (!coveredSides.contains(SOUTH)) {
                                renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, textureUncovered.get(SOUTH), false, worldRenderPass);
                            }
                        }
                    }
                }

                if (coveredSides.contains(DOWN)) {
                    aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(DOWN), false, worldRenderPass);
                    renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(DOWN), false, worldRenderPass);
                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(DOWN), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(DOWN), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(DOWN), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(DOWN), false, worldRenderPass);
                    }
                }

                if (coveredSides.contains(UP)) {
                    aBlock.setBlockBounds(0.0F, 0.875F, 0.0F, 1.0F, 1.0F, 1.0F);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(UP), false, worldRenderPass);
                    renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(UP), false, worldRenderPass);
                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(UP), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(UP), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(UP), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(UP), false, worldRenderPass);
                    }
                }

                if (coveredSides.contains(NORTH)) {
                    aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(NORTH), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(NORTH), false, worldRenderPass);
                    }

                    renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(NORTH), false, worldRenderPass);
                    renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(NORTH), false, worldRenderPass);
                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(NORTH), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(NORTH), false, worldRenderPass);
                    }
                }

                if (coveredSides.contains(SOUTH)) {
                    aBlock.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(SOUTH), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(SOUTH), false, worldRenderPass);
                    }

                    renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(SOUTH), false, worldRenderPass);
                    renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(SOUTH), false, worldRenderPass);
                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(SOUTH), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(SOUTH), false, worldRenderPass);
                    }
                }

                if (coveredSides.contains(WEST)) {
                    aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(WEST), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(WEST), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(WEST), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(WEST), false, worldRenderPass);
                    }

                    renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(WEST), false, worldRenderPass);
                    renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(WEST), false, worldRenderPass);
                }

                if (coveredSides.contains(EAST)) {
                    aBlock.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(EAST), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(EAST), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(EAST), false, worldRenderPass);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(EAST), false, worldRenderPass);
                    }

                    renderNegativeXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(EAST), false, worldRenderPass);
                    renderPositiveXFacing(aWorld, aRenderer, lightingHelper, aBlock, aX, aY, aZ, texture.get(EAST), false, worldRenderPass);
                }

                aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                return true;
            }
        }
    }
    // spotless:on

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderNegativeYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, LightingHelper lightingHelper,
        Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock, int worldRenderPass) {
        if (aWorld != null) {
            if (aFullBlock && !aRenderer.renderAllFaces && !aBlock.shouldSideBeRendered(aWorld, aX, aY - 1, aZ, 0)) {
                return;
            }
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY - 1 : aY, aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, LightingHelper lightingHelper,
        Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock, int worldRenderPass) {
        if (aWorld != null) {
            if (aFullBlock && !aRenderer.renderAllFaces && !aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1)) {
                return;
            }

            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderNegativeZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, LightingHelper lightingHelper,
        Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock, int worldRenderPass) {
        if (aWorld != null) {
            if (aFullBlock && !aRenderer.renderAllFaces && !aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ - 1, 2)) {
                return;
            }

            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ - 1 : aZ));
        }

        aRenderer.flipTexture = !aFullBlock;
        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, LightingHelper lightingHelper,
        Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock, int worldRenderPass) {
        if (aWorld != null) {
            if (aFullBlock && !aRenderer.renderAllFaces && !aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ + 1, 3)) {
                return;
            }

            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ + 1 : aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderNegativeXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, LightingHelper lightingHelper,
        Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock, int worldRenderPass) {
        if (aWorld != null) {
            if (aFullBlock && !aRenderer.renderAllFaces && !aBlock.shouldSideBeRendered(aWorld, aX - 1, aY, aZ, 4)) {
                return;
            }

            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX - 1 : aX, aY, aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderXNeg(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, LightingHelper lightingHelper,
        Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock, int worldRenderPass) {
        if (aWorld != null) {
            if (aFullBlock && !aRenderer.renderAllFaces && !aBlock.shouldSideBeRendered(aWorld, aX + 1, aY, aZ, 5)) {
                return;
            }

            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX + 1 : aX, aY, aZ));
        }

        aRenderer.flipTexture = !aFullBlock;
        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderXPos(aRenderer, lightingHelper, aBlock, aX, aY, aZ, worldRenderPass);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        LightingHelper lightingHelper = new LightingHelper(aRenderer);
        aMeta += 30400;
        if (aBlock instanceof BlockMachines) {
            if (aMeta > 0 && aMeta < GregTechAPI.METATILEENTITIES.length
                && GregTechAPI.METATILEENTITIES[aMeta] != null
                && !GregTechAPI.METATILEENTITIES[aMeta].renderInInventory(aBlock, aMeta, aRenderer)) {
                renderNormalInventoryMetaTileEntity(aBlock, aMeta, aRenderer, lightingHelper);
            }
        }
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        final TesselatorAccessor tessAccess = (TesselatorAccessor) Tessellator.instance;
        final LightingHelper lightingHelper = new LightingHelper(aRenderer);
        final int renderWorldPass = ForgeHooksClient.getWorldRenderPass();

        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity != null && (aTileEntity instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity() != null
            && tessAccess.gt5u$hasVertices()
            && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity()
                .renderInWorld(aWorld, aX, aY, aZ, aBlock, aRenderer)
            || (aTileEntity instanceof IPipeRenderedTileEntity
                ? renderPipeBlock(
                    aWorld,
                    aX,
                    aY,
                    aZ,
                    aBlock,
                    (IPipeRenderedTileEntity) aTileEntity,
                    aRenderer,
                    lightingHelper,
                    renderWorldPass)
                : renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, lightingHelper, renderWorldPass)));
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }
}
