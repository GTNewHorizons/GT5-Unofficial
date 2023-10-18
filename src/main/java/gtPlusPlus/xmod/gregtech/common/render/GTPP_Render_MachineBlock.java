package gtPlusPlus.xmod.gregtech.common.render;

import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_DOWN;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_EAST;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_NORTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_SOUTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_UP;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_WEST;
import static gregtech.api.interfaces.metatileentity.IConnectable.HAS_FOAM;
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
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.common.blocks.GT_Block_Machines;
import gregtech.common.render.GT_Renderer_Block;
import gtPlusPlus.xmod.gregtech.common.helpers.GT_MethodHelper;

public class GTPP_Render_MachineBlock extends GT_Renderer_Block {

    public static GTPP_Render_MachineBlock INSTANCE;
    public final int mRenderID = RenderingRegistry.getNextAvailableRenderId();

    public GTPP_Render_MachineBlock() {
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

    private static void renderNormalInventoryMetaTileEntity(Block aBlock, int aMeta, RenderBlocks aRenderer) {
        if (aMeta > 0 && aMeta < GregTech_API.METATILEENTITIES.length) {
            IMetaTileEntity tMetaTileEntity = GregTech_API.METATILEENTITIES[aMeta];
            if (tMetaTileEntity != null) {
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                if (tMetaTileEntity.getBaseMetaTileEntity() instanceof IPipeRenderedTileEntity pipeRenderedTile) {
                    float tThickness = pipeRenderedTile.getThickNess();
                    float sp = (1.0F - tThickness) / 2.0F;
                    aBlock.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
                    renderNegativeYFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, DOWN, 0b001001, -1, false, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
                    renderPositiveYFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, UP, 0b001001, -1, false, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
                    renderNegativeZFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.NORTH, 0b001001, -1, false, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
                    renderPositiveZFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.SOUTH, 0b001001, -1, false, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
                    renderNegativeXFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.WEST, 0b001001, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
                    renderPositiveXFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.EAST, 0b001001, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                } else {
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
                    renderNegativeYFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, DOWN, ForgeDirection.WEST, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
                    renderPositiveYFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, UP, ForgeDirection.WEST, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
                    renderNegativeZFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.NORTH, ForgeDirection.WEST, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
                    renderPositiveZFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.SOUTH, ForgeDirection.WEST, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
                    renderNegativeXFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.WEST, ForgeDirection.WEST, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                    Tessellator.instance.startDrawingQuads();
                    Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
                    renderPositiveXFacing(
                            null,
                            aRenderer,
                            aBlock,
                            0,
                            0,
                            0,
                            getTexture(tMetaTileEntity, ForgeDirection.EAST, ForgeDirection.WEST, -1, true, false),
                            true);
                    Tessellator.instance.draw();
                }

                aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
    }

    public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
            RenderBlocks aRenderer) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);

        return tTileEntity instanceof ITexturedTileEntity
                ? renderStandardBlock(
                        aWorld,
                        aX,
                        aY,
                        aZ,
                        aBlock,
                        aRenderer,
                        new ITexture[][] { GT_MethodHelper.getTexture(tTileEntity, aBlock, DOWN),
                                GT_MethodHelper.getTexture(tTileEntity, aBlock, UP),
                                GT_MethodHelper.getTexture(tTileEntity, aBlock, ForgeDirection.NORTH),
                                GT_MethodHelper.getTexture(tTileEntity, aBlock, ForgeDirection.SOUTH),
                                GT_MethodHelper.getTexture(tTileEntity, aBlock, ForgeDirection.WEST),
                                GT_MethodHelper.getTexture(tTileEntity, aBlock, ForgeDirection.EAST) })
                : false;
    }

    public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
            RenderBlocks aRenderer, ITexture[][] aTextures) {
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[DOWN.ordinal()], true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[UP.ordinal()], true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[ForgeDirection.NORTH.ordinal()], true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[ForgeDirection.SOUTH.ordinal()], true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[ForgeDirection.WEST.ordinal()], true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[ForgeDirection.EAST.ordinal()], true);
        return true;
    }

    public static boolean renderPipeBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
            IPipeRenderedTileEntity aTileEntity, RenderBlocks aRenderer) {
        final int aConnections = aTileEntity.getConnections();
        if ((aConnections & HAS_FOAM) != 0) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
        } else {
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
                    if (aTileEntity.getCoverIDAtSide(side) != 0) coveredSides.add(side);
                }

                if (coveredSides.containsAll(EnumSet.of(DOWN, UP, NORTH, SOUTH, WEST, EAST))) {
                    return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
                } else {
                    final EnumMap<ForgeDirection, ITexture[]> texture = new EnumMap<>(ForgeDirection.class);
                    final EnumMap<ForgeDirection, ITexture[]> textureUncovered = new EnumMap<>(ForgeDirection.class);

                    for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                        texture.put(side, GT_MethodHelper.getTexture((TileEntity) aTileEntity, aBlock, side));
                        textureUncovered.put(side, aTileEntity.getTextureUncovered(side));
                    }

                    switch (connexionSidesBits) {
                        case NO_CONNECTION -> {
                            aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(DOWN),
                                    false);
                            renderPositiveYFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(UP),
                                    false);
                            renderNegativeZFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(NORTH),
                                    false);
                            renderPositiveZFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(SOUTH),
                                    false);
                            renderNegativeXFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(WEST),
                                    false);
                            renderPositiveXFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(EAST),
                                    false);
                        }
                        case (CONNECTED_DOWN | CONNECTED_UP) -> {
                            aBlock.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(DOWN),
                                    false);
                            renderPositiveYFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(UP),
                                    false);
                            renderNegativeZFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(NORTH),
                                    false);
                            renderPositiveZFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(SOUTH),
                                    false);
                            if (!coveredSides.contains(WEST)) {
                                renderNegativeXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(WEST),
                                        false);
                            }
                            if (!coveredSides.contains(EAST)) {
                                renderPositiveXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(EAST),
                                        false);
                            }
                        }
                        case (CONNECTED_NORTH | CONNECTED_SOUTH) -> {
                            aBlock.setBlockBounds(sp, 0.0F, sp, sp + tThickness, 1.0F, sp + tThickness);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeZFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(NORTH),
                                    false);
                            renderPositiveZFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(SOUTH),
                                    false);
                            renderNegativeXFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(WEST),
                                    false);
                            renderPositiveXFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(EAST),
                                    false);
                            if (!coveredSides.contains(DOWN)) {
                                renderNegativeYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(DOWN),
                                        false);
                            }
                            if (!coveredSides.contains(UP)) {
                                renderPositiveYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(UP),
                                        false);
                            }
                        }
                        case (CONNECTED_WEST | CONNECTED_EAST) -> {
                            aBlock.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, 1.0F);
                            aRenderer.setRenderBoundsFromBlock(aBlock);
                            renderNegativeYFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(DOWN),
                                    false);
                            renderPositiveYFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(UP),
                                    false);
                            renderNegativeXFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(WEST),
                                    false);
                            renderPositiveXFacing(
                                    aWorld,
                                    aRenderer,
                                    aBlock,
                                    aX,
                                    aY,
                                    aZ,
                                    textureUncovered.get(EAST),
                                    false);
                            if (!coveredSides.contains(NORTH)) {
                                renderNegativeZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(NORTH),
                                        false);
                            }
                            if (!coveredSides.contains(SOUTH)) {
                                renderPositiveZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(SOUTH),
                                        false);
                            }
                        }
                        default -> {
                            if ((connexionSidesBits & CONNECTED_DOWN) == 0) {
                                aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(WEST),
                                        false);
                            } else {
                                aBlock.setBlockBounds(0.0F, sp, sp, sp, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(DOWN),
                                        false);
                                renderPositiveYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(UP),
                                        false);
                                renderNegativeZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(NORTH),
                                        false);
                                renderPositiveZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(SOUTH),
                                        false);
                                if (!coveredSides.contains(WEST)) {
                                    renderNegativeXFacing(
                                            aWorld,
                                            aRenderer,
                                            aBlock,
                                            aX,
                                            aY,
                                            aZ,
                                            textureUncovered.get(WEST),
                                            false);
                                }
                            }
                            if ((connexionSidesBits & CONNECTED_UP) == 0) {
                                aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderPositiveXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(EAST),
                                        false);
                            } else {
                                aBlock.setBlockBounds(sp + tThickness, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(DOWN),
                                        false);
                                renderPositiveYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(UP),
                                        false);
                                renderNegativeZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(NORTH),
                                        false);
                                renderPositiveZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(SOUTH),
                                        false);
                                if (!coveredSides.contains(EAST)) {
                                    renderPositiveXFacing(
                                            aWorld,
                                            aRenderer,
                                            aBlock,
                                            aX,
                                            aY,
                                            aZ,
                                            textureUncovered.get(EAST),
                                            false);
                                }
                            }
                            if ((connexionSidesBits & CONNECTED_NORTH) == 0) {
                                aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(DOWN),
                                        false);
                            } else {
                                aBlock.setBlockBounds(sp, 0.0F, sp, sp + tThickness, sp, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(NORTH),
                                        false);
                                renderPositiveZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(SOUTH),
                                        false);
                                renderNegativeXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(WEST),
                                        false);
                                renderPositiveXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(EAST),
                                        false);
                                if (!coveredSides.contains(DOWN)) {
                                    renderNegativeYFacing(
                                            aWorld,
                                            aRenderer,
                                            aBlock,
                                            aX,
                                            aY,
                                            aZ,
                                            textureUncovered.get(DOWN),
                                            false);
                                }
                            }
                            if ((connexionSidesBits & CONNECTED_SOUTH) == 0) {
                                aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderPositiveYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(UP),
                                        false);
                            } else {
                                aBlock.setBlockBounds(sp, sp + tThickness, sp, sp + tThickness, 1.0F, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(NORTH),
                                        false);
                                renderPositiveZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(SOUTH),
                                        false);
                                renderNegativeXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(WEST),
                                        false);
                                renderPositiveXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(EAST),
                                        false);
                                if (!coveredSides.contains(UP)) {
                                    renderPositiveYFacing(
                                            aWorld,
                                            aRenderer,
                                            aBlock,
                                            aX,
                                            aY,
                                            aZ,
                                            textureUncovered.get(UP),
                                            false);
                                }
                            }
                            if ((connexionSidesBits & CONNECTED_WEST) == 0) {
                                aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(NORTH),
                                        false);
                            } else {
                                aBlock.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, sp);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(DOWN),
                                        false);
                                renderPositiveYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(UP),
                                        false);
                                renderNegativeXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(WEST),
                                        false);
                                renderPositiveXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(EAST),
                                        false);
                                if (!coveredSides.contains(NORTH)) {
                                    renderNegativeZFacing(
                                            aWorld,
                                            aRenderer,
                                            aBlock,
                                            aX,
                                            aY,
                                            aZ,
                                            textureUncovered.get(NORTH),
                                            false);
                                }
                            }
                            if ((connexionSidesBits & CONNECTED_EAST) == 0) {
                                aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderPositiveZFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(SOUTH),
                                        false);
                            } else {
                                aBlock.setBlockBounds(sp, sp, sp + tThickness, sp + tThickness, sp + tThickness, 1.0F);
                                aRenderer.setRenderBoundsFromBlock(aBlock);
                                renderNegativeYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(DOWN),
                                        false);
                                renderPositiveYFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(UP),
                                        false);
                                renderNegativeXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(WEST),
                                        false);
                                renderPositiveXFacing(
                                        aWorld,
                                        aRenderer,
                                        aBlock,
                                        aX,
                                        aY,
                                        aZ,
                                        textureUncovered.get(EAST),
                                        false);
                                if (!coveredSides.contains(SOUTH)) {
                                    renderPositiveZFacing(
                                            aWorld,
                                            aRenderer,
                                            aBlock,
                                            aX,
                                            aY,
                                            aZ,
                                            textureUncovered.get(SOUTH),
                                            false);
                                }
                            }
                        }
                    }

                    if (coveredSides.contains(DOWN)) {
                        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(DOWN), false);
                        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(DOWN), false);
                        if (!coveredSides.contains(NORTH)) {
                            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(DOWN), false);
                        }

                        if (!coveredSides.contains(SOUTH)) {
                            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(DOWN), false);
                        }

                        if (!coveredSides.contains(WEST)) {
                            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(DOWN), false);
                        }

                        if (!coveredSides.contains(EAST)) {
                            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(DOWN), false);
                        }
                    }

                    if (coveredSides.contains(UP)) {
                        aBlock.setBlockBounds(0.0F, 0.875F, 0.0F, 1.0F, 1.0F, 1.0F);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(UP), false);
                        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(UP), false);
                        if (!coveredSides.contains(NORTH)) {
                            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(UP), false);
                        }

                        if (!coveredSides.contains(SOUTH)) {
                            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(UP), false);
                        }

                        if (!coveredSides.contains(WEST)) {
                            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(UP), false);
                        }

                        if (!coveredSides.contains(EAST)) {
                            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(UP), false);
                        }
                    }

                    if (coveredSides.contains(NORTH)) {
                        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        if (!coveredSides.contains(DOWN)) {
                            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(NORTH), false);
                        }

                        if (!coveredSides.contains(UP)) {
                            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(NORTH), false);
                        }

                        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(NORTH), false);
                        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(NORTH), false);
                        if (!coveredSides.contains(WEST)) {
                            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(NORTH), false);
                        }

                        if (!coveredSides.contains(EAST)) {
                            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(NORTH), false);
                        }
                    }

                    if (coveredSides.contains(SOUTH)) {
                        aBlock.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        if (!coveredSides.contains(DOWN)) {
                            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(SOUTH), false);
                        }

                        if (!coveredSides.contains(UP)) {
                            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(SOUTH), false);
                        }

                        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(SOUTH), false);
                        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(SOUTH), false);
                        if (!coveredSides.contains(WEST)) {
                            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(SOUTH), false);
                        }

                        if (!coveredSides.contains(EAST)) {
                            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(SOUTH), false);
                        }
                    }

                    if (coveredSides.contains(WEST)) {
                        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        if (!coveredSides.contains(DOWN)) {
                            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(WEST), false);
                        }

                        if (!coveredSides.contains(UP)) {
                            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(WEST), false);
                        }

                        if (!coveredSides.contains(NORTH)) {
                            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(WEST), false);
                        }

                        if (!coveredSides.contains(SOUTH)) {
                            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(WEST), false);
                        }

                        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(WEST), false);
                        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(WEST), false);
                    }

                    if (coveredSides.contains(EAST)) {
                        aBlock.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        aRenderer.setRenderBoundsFromBlock(aBlock);
                        if (!coveredSides.contains(DOWN)) {
                            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(EAST), false);
                        }

                        if (!coveredSides.contains(UP)) {
                            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(EAST), false);
                        }

                        if (!coveredSides.contains(NORTH)) {
                            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(EAST), false);
                        }

                        if (!coveredSides.contains(SOUTH)) {
                            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(EAST), false);
                        }

                        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(EAST), false);
                        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, texture.get(EAST), false);
                    }

                    aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    return true;
                }
            }
        }
    }

    public static void renderNegativeYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
            int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY - 1, aZ, 0)) {
                return;
            }

            Tessellator.instance
                    .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY - 1 : aY, aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYNeg(aRenderer, aBlock, aX, aY, aZ);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
            int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1)) {
                return;
            }

            Tessellator.instance
                    .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYPos(aRenderer, aBlock, aX, aY, aZ);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    public static void renderNegativeZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
            int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ - 1, 2)) {
                return;
            }

            Tessellator.instance
                    .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ - 1 : aZ));
        }

        aRenderer.flipTexture = !aFullBlock;
        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZNeg(aRenderer, aBlock, aX, aY, aZ);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    public static void renderPositiveZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
            int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ + 1, 3)) {
                return;
            }

            Tessellator.instance
                    .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ + 1 : aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZPos(aRenderer, aBlock, aX, aY, aZ);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    public static void renderNegativeXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
            int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX - 1, aY, aZ, 4)) {
                return;
            }

            Tessellator.instance
                    .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX - 1 : aX, aY, aZ));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderXNeg(aRenderer, aBlock, aX, aY, aZ);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    public static void renderPositiveXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
            int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX + 1, aY, aZ, 5)) {
                return;
            }

            Tessellator.instance
                    .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX + 1 : aX, aY, aZ));
        }

        aRenderer.flipTexture = !aFullBlock;
        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderXPos(aRenderer, aBlock, aX, aY, aZ);
                }
            }
        }

        aRenderer.flipTexture = false;
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        aMeta += 30400;
        if (aBlock instanceof GT_Block_Machines) {
            if (aMeta > 0 && aMeta < GregTech_API.METATILEENTITIES.length
                    && GregTech_API.METATILEENTITIES[aMeta] != null
                    && !GregTech_API.METATILEENTITIES[aMeta].renderInInventory(aBlock, aMeta, aRenderer)) {
                renderNormalInventoryMetaTileEntity(aBlock, aMeta, aRenderer);
            }
        }
        aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
            RenderBlocks aRenderer) {
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity == null ? false
                : (aTileEntity instanceof IGregTechTileEntity
                        && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity() != null
                        && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity()
                                .renderInWorld(aWorld, aX, aY, aZ, aBlock, aRenderer)
                                        ? true
                                        : (aTileEntity instanceof IPipeRenderedTileEntity
                                                ? renderPipeBlock(
                                                        aWorld,
                                                        aX,
                                                        aY,
                                                        aZ,
                                                        aBlock,
                                                        (IPipeRenderedTileEntity) aTileEntity,
                                                        aRenderer)
                                                : renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer)));
    }

    @Override
    public boolean shouldRender3DInInventory(int aModel) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }
}
