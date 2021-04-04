package gregtech.common.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.common.blocks.GT_Block_Machines;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_DOWN;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_EAST;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_NORTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_SOUTH;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_UP;
import static gregtech.api.interfaces.metatileentity.IConnectable.CONNECTED_WEST;
import static gregtech.api.interfaces.metatileentity.IConnectable.HAS_FRESHFOAM;
import static gregtech.api.interfaces.metatileentity.IConnectable.HAS_HARDENEDFOAM;
import static gregtech.api.interfaces.metatileentity.IConnectable.NO_CONNECTION;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

public class GT_Renderer_Block implements ISimpleBlockRenderingHandler {
    public static final float blockMin = 0.0F;
    public static final float blockMax = 1.0F;
    private static final float coverThickness = blockMax / 8.0F;
    private static final float coverInnerMin = blockMin + coverThickness;
    private static final float coverInnerMax = blockMax - coverThickness;
    public static GT_Renderer_Block INSTANCE;
    public final int mRenderID;

    public GT_Renderer_Block() {
        this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
        INSTANCE = this;
        RenderingRegistry.registerBlockHandler(this);
    }

    public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof IPipeRenderedTileEntity)) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, new ITexture[][]{
                    ((IPipeRenderedTileEntity) tTileEntity).getTextureCovered((byte) DOWN.ordinal()),
                    ((IPipeRenderedTileEntity) tTileEntity).getTextureCovered((byte) UP.ordinal()),
                    ((IPipeRenderedTileEntity) tTileEntity).getTextureCovered((byte) NORTH.ordinal()),
                    ((IPipeRenderedTileEntity) tTileEntity).getTextureCovered((byte) SOUTH.ordinal()),
                    ((IPipeRenderedTileEntity) tTileEntity).getTextureCovered((byte) WEST.ordinal()),
                    ((IPipeRenderedTileEntity) tTileEntity).getTextureCovered((byte) EAST.ordinal())});
        }
        if ((tTileEntity instanceof ITexturedTileEntity)) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, new ITexture[][]{
                    ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) DOWN.ordinal()),
                    ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) UP.ordinal()),
                    ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) NORTH.ordinal()),
                    ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) SOUTH.ordinal()),
                    ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) WEST.ordinal()),
                    ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) EAST.ordinal())});
        }
        return false;
    }

    public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer, ITexture[][] aTextures) {
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[DOWN.ordinal()], true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[UP.ordinal()], true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[NORTH.ordinal()], true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SOUTH.ordinal()], true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[WEST.ordinal()], true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[EAST.ordinal()], true);
        return true;
    }

    public static boolean renderPipeBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, IPipeRenderedTileEntity aTileEntity, RenderBlocks aRenderer) {
        final byte aConnections = aTileEntity.getConnections();
        if ((aConnections & (HAS_FRESHFOAM | HAS_HARDENEDFOAM)) != 0) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
        }
        final float thickness = aTileEntity.getThickNess();
        if (thickness >= 0.99F) {
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
        }
        // Range of block occupied by pipe
        final float pipeMin = (blockMax - thickness) / 2.0F;
        final float pipeMax = blockMax - pipeMin;
        final boolean[] tIsCovered = new boolean[VALID_DIRECTIONS.length];
        for (int i = 0; i < VALID_DIRECTIONS.length; i++) {
            tIsCovered[i] = (aTileEntity.getCoverIDAtSide((byte) i) != 0);
        }

        final ITexture[][] tIcons = new ITexture[VALID_DIRECTIONS.length][];
        final ITexture[][] tCovers = new ITexture[VALID_DIRECTIONS.length][];
        for (int i = 0; i < VALID_DIRECTIONS.length; i++) {
            tCovers[i] = aTileEntity.getTexture(aBlock, (byte) i);
            tIcons[i] = aTileEntity.getTextureUncovered((byte) i);
        }

        switch (aConnections) {
            case NO_CONNECTION:
                aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                break;
            case CONNECTED_EAST | CONNECTED_WEST:
                // EAST - WEST Pipe Sides
                aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);

                // EAST - WEST Pipe Ends
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                break;
            case CONNECTED_DOWN | CONNECTED_UP:
                // UP - DOWN Pipe Sides
                aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, blockMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);

                // UP - DOWN Pipe Ends
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                break;
            case CONNECTED_NORTH | CONNECTED_SOUTH:
                // NORTH - SOUTH Pipe Sides
                aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, blockMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);

                // NORTH - SOUTH Pipe Ends
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                break;
            default:
                if ((aConnections & CONNECTED_WEST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, pipeMin, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                }
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);

                if ((aConnections & CONNECTED_EAST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMax, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                }
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);

                if ((aConnections & CONNECTED_DOWN) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, pipeMin, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);

                if ((aConnections & CONNECTED_UP) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMax, pipeMin, pipeMax, blockMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);

                if ((aConnections & CONNECTED_NORTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, pipeMin);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[NORTH.ordinal()], false);

                if ((aConnections & CONNECTED_SOUTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMax, pipeMax, pipeMax, blockMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[DOWN.ordinal()], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[UP.ordinal()], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[WEST.ordinal()], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[EAST.ordinal()], false);
                }
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SOUTH.ordinal()], false);
                break;
        }

        // Render covers on pipes
        if (tIsCovered[DOWN.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, coverInnerMin, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            }
            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
            if ((aConnections & CONNECTED_DOWN) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, blockMin, pipeMin);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMax, blockMax, blockMin, blockMax);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMin, pipeMin, blockMin, pipeMax);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMin, pipeMin, blockMax, blockMin, pipeMax);
            }
            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[DOWN.ordinal()], false);
        }

        if (tIsCovered[UP.ordinal()]) {
            aBlock.setBlockBounds(blockMin, coverInnerMax, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            }
            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
            if ((aConnections & CONNECTED_UP) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMax, blockMin, blockMax, blockMax, pipeMin);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMax, blockMax, blockMax, blockMax);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMin, pipeMin, blockMax, pipeMax);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMax, pipeMin, blockMax, blockMax, pipeMax);
            }
            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[UP.ordinal()], false);
        }

        if (tIsCovered[NORTH.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, coverInnerMin);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            }
            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
            if ((aConnections & CONNECTED_NORTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, pipeMin, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMax, blockMax, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, pipeMin, pipeMax, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMin, blockMax, pipeMax, blockMin);
            }
            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[NORTH.ordinal()], false);
        }

        if (tIsCovered[SOUTH.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, coverInnerMax, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            if (!tIsCovered[WEST.ordinal()]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            if (!tIsCovered[EAST.ordinal()]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            }
            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
            if ((aConnections & CONNECTED_SOUTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMax, blockMax, pipeMin, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMax, blockMax, blockMax, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMax, pipeMin, pipeMax, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SOUTH.ordinal()], false);
        }

        if (tIsCovered[WEST.ordinal()]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, coverInnerMin, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            }
            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
            if ((aConnections & CONNECTED_WEST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMin, pipeMin, blockMax);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMin, blockMax, blockMax);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, blockMin, pipeMax, pipeMin);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(blockMin, pipeMin, pipeMax, blockMin, pipeMax, blockMax);
            }
            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[WEST.ordinal()], false);
        }

        if (tIsCovered[EAST.ordinal()]) {
            aBlock.setBlockBounds(coverInnerMax, blockMin, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[DOWN.ordinal()]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            if (!tIsCovered[UP.ordinal()]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            if (!tIsCovered[NORTH.ordinal()]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            if (!tIsCovered[SOUTH.ordinal()]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
            }
            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);

            if ((aConnections & CONNECTED_EAST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMax, blockMin, blockMin, blockMax, pipeMin, blockMax);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMax, pipeMax, blockMin, blockMax, blockMax, blockMax);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMax, pipeMin, blockMin, blockMax, pipeMax, pipeMin);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
                // Middle right panel
                aRenderer.setRenderBounds(blockMax, pipeMin, pipeMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[EAST.ordinal()], false);
        }
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        return true;
    }

    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        aRenderer.enableAO = false;
        if ((aBlock instanceof GT_Block_Machines)) {
            if ((aMeta > 0) && (aMeta < GregTech_API.METATILEENTITIES.length) && (GregTech_API.METATILEENTITIES[aMeta] != null) &&
                    (!GregTech_API.METATILEENTITIES[aMeta].renderInInventory(aBlock, aMeta, aRenderer))) {
                renderNormalInventoryMetaTileEntity(aBlock, aMeta, aRenderer);
            }
        } else if ((aBlock instanceof GT_Block_Ores_Abstract)) {
            GT_TileEntity_Ores tTileEntity = new GT_TileEntity_Ores();
            tTileEntity.mMetaData = ((short) aMeta);

            aBlock.setBlockBoundsForItemRender();
            aRenderer.setRenderBoundsFromBlock(aBlock);

            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 0), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 1), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 2), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 3), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 4), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tTileEntity.getTexture(aBlock, (byte) 5), true);
            Tessellator.instance.draw();
        }
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, 1.0F, 1.0F, 1.0F);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    private static void renderNormalInventoryMetaTileEntity(Block aBlock, int aMeta, RenderBlocks aRenderer) {
        if ((aMeta <= 0) || (aMeta >= GregTech_API.METATILEENTITIES.length)) {
            return;
        }
        IMetaTileEntity tMetaTileEntity = GregTech_API.METATILEENTITIES[aMeta];
        if (tMetaTileEntity == null) {
            return;
        }
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);

        final IGregTechTileEntity iGregTechTileEntity = tMetaTileEntity.getBaseMetaTileEntity();

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        if ((iGregTechTileEntity instanceof IPipeRenderedTileEntity)) {
            final float tThickness = ((IPipeRenderedTileEntity) iGregTechTileEntity).getThickNess();
            final float pipeMin = (blockMax - tThickness) / 2.0F;
            final float pipeMax = blockMax - pipeMin;

            aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) DOWN.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) UP.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) NORTH.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) SOUTH.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, false, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) WEST.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, true, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) EAST.ordinal(), (byte) (CONNECTED_WEST | CONNECTED_EAST), (byte) -1, true, false), true);
        } else {
            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) DOWN.ordinal(), (byte) EAST.ordinal(), (byte) -1, true, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) UP.ordinal(), (byte) EAST.ordinal(), (byte) -1, true, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) NORTH.ordinal(), (byte) EAST.ordinal(), (byte) -1, true, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) SOUTH.ordinal(), (byte) EAST.ordinal(), (byte) -1, true, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) WEST.ordinal(), (byte) EAST.ordinal(), (byte) -1, true, false), true);
            Tessellator.instance.draw();

            Tessellator.instance.startDrawingQuads();
            Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, (byte) EAST.ordinal(), (byte) EAST.ordinal(), (byte) -1, true, false), true);
        }
        Tessellator.instance.draw();
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    public static void renderNegativeYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY - 1, aZ, 0))) return;
            Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY - 1 : aY, aZ));
        }
        if (aIcon == null) return;
        for (ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderYNeg(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1))) return;
            Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
        }
        if (aIcon == null) return;
        for (ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderYPos(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderNegativeZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ - 1, 2))) return;
            Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ - 1 : aZ));
        }
        if (aIcon == null) return;
        for (ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderZNeg(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderPositiveZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ + 1, 3))) return;
            Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ + 1 : aZ));
        }
        if (aIcon == null) return;
        for (ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderZPos(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderNegativeXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX - 1, aY, aZ, 4))) return;
            Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX - 1 : aX, aY, aZ));
        }
        if (aIcon == null) return;
        for (ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderXNeg(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderPositiveXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX + 1, aY, aZ, 5))) return;
            Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX + 1 : aX, aY, aZ));
        }
        if (aIcon == null) return;
        for (ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderXPos(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID, RenderBlocks aRenderer) {
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion;
        TileEntity tileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tileEntity == null) return false;
        if (tileEntity instanceof IGregTechTileEntity) {
            IMetaTileEntity metaTileEntity;
            if ((metaTileEntity = ((IGregTechTileEntity) tileEntity).getMetaTileEntity()) != null &&
                    metaTileEntity.renderInWorld(aWorld, aX, aY, aZ, aBlock, aRenderer)) {
                aRenderer.enableAO = false;
                return true;
            }
        }
        if (tileEntity instanceof IPipeRenderedTileEntity &&
                renderPipeBlock(aWorld, aX, aY, aZ, aBlock, (IPipeRenderedTileEntity) tileEntity, aRenderer)) {
            aRenderer.enableAO = false;
            return true;
        }
        if (renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer)) {
            aRenderer.enableAO = false;
            return true;
        }
        return false;
    }

    public boolean shouldRender3DInInventory(int aModel) {
        return true;
    }

    public int getRenderId() {
        return this.mRenderID;
    }
}
