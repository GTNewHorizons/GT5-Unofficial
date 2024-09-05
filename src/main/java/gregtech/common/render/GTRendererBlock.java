package gregtech.common.render;

import static gregtech.api.enums.GTValues.SIDE_DOWN;
import static gregtech.api.enums.GTValues.SIDE_EAST;
import static gregtech.api.enums.GTValues.SIDE_NORTH;
import static gregtech.api.enums.GTValues.SIDE_SOUTH;
import static gregtech.api.enums.GTValues.SIDE_UP;
import static gregtech.api.enums.GTValues.SIDE_WEST;
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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.angelica.api.ThreadSafeISBRH;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IAllSidedTexturedTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.XSTR;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;

@ThreadSafeISBRH(perThread = true)
public class GTRendererBlock implements ISimpleBlockRenderingHandler {

    public static final float blockMin = 0.0F;
    public static final float blockMax = 1.0F;
    private static final float coverThickness = blockMax / 8.0F;
    private static final float coverInnerMin = blockMin + coverThickness;
    private static final float coverInnerMax = blockMax - coverThickness;

    @Deprecated
    public static GTRendererBlock INSTANCE;
    public static int mRenderID;

    public static void register() {
        mRenderID = RenderingRegistry.getNextAvailableRenderId();
        INSTANCE = new GTRendererBlock();
        RenderingRegistry.registerBlockHandler(INSTANCE);
    }

    private final ITexture[][] textureArray = new ITexture[6][];

    public boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
        RenderBlocks aRenderer) {
        final TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IPipeRenderedTileEntity pipeRenderedTileEntity) {
            textureArray[0] = pipeRenderedTileEntity.getTextureCovered(DOWN);
            textureArray[1] = pipeRenderedTileEntity.getTextureCovered(UP);
            textureArray[2] = pipeRenderedTileEntity.getTextureCovered(NORTH);
            textureArray[3] = pipeRenderedTileEntity.getTextureCovered(SOUTH);
            textureArray[4] = pipeRenderedTileEntity.getTextureCovered(WEST);
            textureArray[5] = pipeRenderedTileEntity.getTextureCovered(EAST);
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, textureArray);
        }
        if (tTileEntity instanceof IAllSidedTexturedTileEntity allSidedTexturedTileEntity) {
            ITexture[] texture = allSidedTexturedTileEntity.getTexture(aBlock);
            textureArray[0] = texture;
            textureArray[1] = texture;
            textureArray[2] = texture;
            textureArray[3] = texture;
            textureArray[4] = texture;
            textureArray[5] = texture;
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, textureArray);
        }
        if (tTileEntity instanceof ITexturedTileEntity texturedTileEntity) {
            textureArray[0] = texturedTileEntity.getTexture(aBlock, DOWN);
            textureArray[1] = texturedTileEntity.getTexture(aBlock, UP);
            textureArray[2] = texturedTileEntity.getTexture(aBlock, NORTH);
            textureArray[3] = texturedTileEntity.getTexture(aBlock, SOUTH);
            textureArray[4] = texturedTileEntity.getTexture(aBlock, WEST);
            textureArray[5] = texturedTileEntity.getTexture(aBlock, EAST);
            return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, textureArray);
        }

        return false;
    }

    public boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
        RenderBlocks aRenderer, ITexture[][] aTextures) {
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SIDE_DOWN], true);
        renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SIDE_UP], true);
        renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SIDE_NORTH], true);
        renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SIDE_SOUTH], true);
        renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SIDE_WEST], true);
        renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[SIDE_EAST], true);
        return true;
    }

    final ITexture[][] tIcons = new ITexture[VALID_DIRECTIONS.length][];
    final ITexture[][] tCovers = new ITexture[VALID_DIRECTIONS.length][];
    final boolean[] tIsCovered = new boolean[VALID_DIRECTIONS.length];

    public boolean renderPipeBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
        IPipeRenderedTileEntity aTileEntity, RenderBlocks aRenderer) {
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

        for (int i = 0; i < VALID_DIRECTIONS.length; i++) {
            final ForgeDirection iSide = VALID_DIRECTIONS[i];
            tIsCovered[i] = (aTileEntity.getCoverIDAtSide(iSide) != 0);
            tCovers[i] = aTileEntity.getTexture(aBlock, iSide);
            tIcons[i] = aTileEntity.getTextureUncovered(iSide);

        }

        switch (aConnections) {
            case NO_CONNECTION -> {
                aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);
            }
            case CONNECTED_EAST | CONNECTED_WEST -> {
                // EAST - WEST Pipe Sides
                aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);

                // EAST - WEST Pipe Ends
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);
            }
            case CONNECTED_DOWN | CONNECTED_UP -> {
                // UP - DOWN Pipe Sides
                aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, blockMax, pipeMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);

                // UP - DOWN Pipe Ends
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
            }
            case CONNECTED_NORTH | CONNECTED_SOUTH -> {
                // NORTH - SOUTH Pipe Sides
                aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, blockMax);
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);

                // NORTH - SOUTH Pipe Ends
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
            }
            default -> {
                if ((aConnections & CONNECTED_WEST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, pipeMin, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
                }
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                if ((aConnections & CONNECTED_EAST) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMax, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
                }
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);
                if ((aConnections & CONNECTED_DOWN) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, pipeMin, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);
                }
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                if ((aConnections & CONNECTED_UP) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMax, pipeMin, pipeMax, blockMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);
                }
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                if ((aConnections & CONNECTED_NORTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, pipeMin);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);
                }
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_NORTH], false);
                if ((aConnections & CONNECTED_SOUTH) == 0) {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                } else {
                    aBlock.setBlockBounds(pipeMin, pipeMin, pipeMax, pipeMax, pipeMax, blockMax);
                    aRenderer.setRenderBoundsFromBlock(aBlock);
                    renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_UP], false);
                    renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_EAST], false);
                }
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tIcons[SIDE_SOUTH], false);
            }
        }

        // Render covers on pipes
        if (tIsCovered[SIDE_DOWN]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, coverInnerMin, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
            }
            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
            if ((aConnections & CONNECTED_DOWN) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, blockMin, pipeMin);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMax, blockMax, blockMin, blockMax);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMin, pipeMin, pipeMin, blockMin, pipeMax);
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMin, pipeMin, blockMax, blockMin, pipeMax);
            }
            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_DOWN], false);
        }

        if (tIsCovered[SIDE_UP]) {
            aBlock.setBlockBounds(blockMin, coverInnerMax, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
            }
            renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
            if ((aConnections & CONNECTED_UP) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMax, blockMin, blockMax, blockMax, pipeMin);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMax, blockMax, blockMax, blockMax);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, blockMax, pipeMin, pipeMin, blockMax, pipeMax);
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, blockMax, pipeMin, blockMax, blockMax, pipeMax);
            }
            renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_UP], false);
        }

        if (tIsCovered[SIDE_NORTH]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, coverInnerMin);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
            }
            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
            if ((aConnections & CONNECTED_NORTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, pipeMin, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMax, blockMax, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, pipeMin, pipeMax, blockMin);
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMin, blockMax, pipeMax, blockMin);
            }
            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_NORTH], false);
        }

        if (tIsCovered[SIDE_SOUTH]) {
            aBlock.setBlockBounds(blockMin, blockMin, coverInnerMax, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
            }
            renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
            if ((aConnections & CONNECTED_SOUTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMax, blockMax, pipeMin, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMax, blockMax, blockMax, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMax, pipeMin, pipeMax, blockMax);
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
                // Middle right panel
                aRenderer.setRenderBounds(pipeMax, pipeMin, blockMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_SOUTH], false);
        }

        if (tIsCovered[SIDE_WEST]) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, coverInnerMin, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
            }
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
            }
            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
            if ((aConnections & CONNECTED_WEST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMin, blockMin, blockMin, blockMin, pipeMin, blockMax);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMin, blockMax, blockMax);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMin, pipeMin, blockMin, blockMin, pipeMax, pipeMin);
                renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
                // Middle right panel
                aRenderer.setRenderBounds(blockMin, pipeMin, pipeMax, blockMin, pipeMax, blockMax);
            }
            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_WEST], false);
        }

        if (tIsCovered[SIDE_EAST]) {
            aBlock.setBlockBounds(coverInnerMax, blockMin, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
            }
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
            }
            renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);

            if ((aConnections & CONNECTED_EAST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                aRenderer.setRenderBounds(blockMax, blockMin, blockMin, blockMax, pipeMin, blockMax);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
                // Upper panel
                aRenderer.setRenderBounds(blockMax, pipeMax, blockMin, blockMax, blockMax, blockMax);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
                // Middle left panel
                aRenderer.setRenderBounds(blockMax, pipeMin, blockMin, blockMax, pipeMax, pipeMin);
                renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
                // Middle right panel
                aRenderer.setRenderBounds(blockMax, pipeMin, pipeMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[SIDE_EAST], false);
        }
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        return true;
    }

    @SideOnly(Side.CLIENT)
    public static void addHitEffects(EffectRenderer effectRenderer, Block block, World world, int x, int y, int z,
        int ordinalSide) {
        double rX = x + XSTR.XSTR_INSTANCE.nextDouble() * 0.8 + 0.1;
        double rY = y + XSTR.XSTR_INSTANCE.nextDouble() * 0.8 + 0.1;
        double rZ = z + XSTR.XSTR_INSTANCE.nextDouble() * 0.8 + 0.1;
        if (ordinalSide == 0) {
            rY = y - 0.1;
        } else if (ordinalSide == 1) {
            rY = y + 1.1;
        } else if (ordinalSide == 2) {
            rZ = z - 0.1;
        } else if (ordinalSide == 3) {
            rZ = z + 1.1;
        } else if (ordinalSide == 4) {
            rX = x - 0.1;
        } else if (ordinalSide == 5) {
            rX = x + 1.1;
        }
        effectRenderer.addEffect(
            (new EntityDiggingFX(
                world,
                rX,
                rY,
                rZ,
                0.0,
                0.0,
                0.0,
                block,
                block.getDamageValue(world, x, y, z),
                ordinalSide)).applyColourMultiplier(x, y, z)
                    .multiplyVelocity(0.2F)
                    .multipleParticleScaleBy(0.6F));
    }

    @SideOnly(Side.CLIENT)
    public static void addDestroyEffects(EffectRenderer effectRenderer, Block block, World world, int x, int y, int z) {
        for (int iX = 0; iX < 4; ++iX) {
            for (int iY = 0; iY < 4; ++iY) {
                for (int iZ = 0; iZ < 4; ++iZ) {
                    final double bX = x + (iX + 0.5) / 4.0;
                    final double bY = y + (iY + 0.5) / 4.0;
                    final double bZ = z + (iZ + 0.5) / 4.0;
                    effectRenderer.addEffect(
                        (new EntityDiggingFX(
                            world,
                            bX,
                            bY,
                            bZ,
                            bX - x - 0.5,
                            bY - y - 0.5,
                            bZ - z - 0.5,
                            block,
                            block.getDamageValue(world, x, y, z))).applyColourMultiplier(x, y, z));
                }
            }
        }
    }

    final TileEntityOres tTileEntity = new TileEntityOres();

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        if (aBlock instanceof BlockOresAbstract) {
            tTileEntity.mMetaData = ((short) aMeta);

            aBlock.setBlockBoundsForItemRender();
            aRenderer.setRenderBoundsFromBlock(aBlock);
            // spotless:off
            ITexture[] texture = tTileEntity.getTexture(aBlock);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
            // spotless:on
        } else if (aMeta > 0 && (aMeta < GregTechAPI.METATILEENTITIES.length)
            && aBlock instanceof BlockMachines
            && (GregTechAPI.METATILEENTITIES[aMeta] != null)
            && (!GregTechAPI.METATILEENTITIES[aMeta].renderInInventory(aBlock, aMeta, aRenderer))) {
                renderNormalInventoryMetaTileEntity(aBlock, aMeta, aRenderer);
            } else if (aBlock instanceof BlockFrameBox) {
                ITexture[] texture = ((BlockFrameBox) aBlock).getTexture(aMeta);
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                // spotless:off
                renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
                renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
                renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
                renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
                renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
                renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, texture, true);
                // spotless:on
            }
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);

        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    private static void renderNormalInventoryMetaTileEntity(Block aBlock, int aMeta, RenderBlocks aRenderer) {
        if ((aMeta <= 0) || (aMeta >= GregTechAPI.METATILEENTITIES.length)) {
            return;
        }
        final IMetaTileEntity tMetaTileEntity = GregTechAPI.METATILEENTITIES[aMeta];
        if (tMetaTileEntity == null) {
            return;
        }
        aBlock.setBlockBoundsForItemRender();
        aRenderer.setRenderBoundsFromBlock(aBlock);

        final IGregTechTileEntity iGregTechTileEntity = tMetaTileEntity.getBaseMetaTileEntity();

        // spotless:off
        if ((iGregTechTileEntity instanceof IPipeRenderedTileEntity renderedPipe)
            && (tMetaTileEntity instanceof MetaPipeEntity pipeEntity)) {
            final float tThickness = renderedPipe.getThickNess();
            final float pipeMin = (blockMax - tThickness) / 2.0F;
            final float pipeMax = blockMax - pipeMin;

            aBlock.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, pipeEntity.getTexture(iGregTechTileEntity, DOWN, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, pipeEntity.getTexture(iGregTechTileEntity, UP, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, pipeEntity.getTexture(iGregTechTileEntity, NORTH, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, pipeEntity.getTexture(iGregTechTileEntity, SOUTH, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, pipeEntity.getTexture(iGregTechTileEntity, WEST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false), true);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, pipeEntity.getTexture(iGregTechTileEntity, EAST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false), true);
        } else {
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, DOWN, WEST, -1, true, false), true);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, UP, WEST, -1, true, false), true);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, NORTH, WEST, -1, true, false), true);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, SOUTH, WEST, -1, true, false), true);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, WEST, WEST, -1, true, false), true);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tMetaTileEntity.getTexture(iGregTechTileEntity, EAST, WEST, -1, true, false), true);
        }
        // spotless:on
    }

    public static void renderNegativeYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
        int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY - 1, aZ, 0))) return;
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY - 1 : aY, aZ));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderYNeg(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
        int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1))) return;
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderYPos(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderNegativeZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
        int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ - 1, 2))) return;
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ - 1 : aZ));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderZNeg(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderPositiveZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
        int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ + 1, 3))) return;
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ + 1 : aZ));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderZPos(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderNegativeXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
        int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX - 1, aY, aZ, 4))) return;
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX - 1 : aX, aY, aZ));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderXNeg(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    public static void renderPositiveXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
        int aZ, ITexture[] aIcon, boolean aFullBlock) {
        if (aWorld != null) {
            if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX + 1, aY, aZ, 5))) return;
            Tessellator.instance
                .setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX + 1 : aX, aY, aZ));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderXPos(aRenderer, aBlock, aX, aY, aZ);
            }
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.gregtechproxy.mRenderTileAmbientOcclusion;
        aRenderer.useInventoryTint = false;

        final TileEntity tileEntity = aWorld.getTileEntity(aX, aY, aZ);

        // If this block does not have a TE, render it as a normal block.
        // Otherwise, render the TE instead.
        if (tileEntity == null && aBlock instanceof BlockFrameBox frameBlock) {
            int meta = aWorld.getBlockMetadata(aX, aY, aZ);
            ITexture[] texture = frameBlock.getTexture(meta);
            if (texture == null) return false;
            textureArray[0] = texture;
            textureArray[1] = texture;
            textureArray[2] = texture;
            textureArray[3] = texture;
            textureArray[4] = texture;
            textureArray[5] = texture;
            renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, textureArray);
            return true;
        }

        if (tileEntity == null) return false;

        if (tileEntity instanceof IGregTechTileEntity) {
            final IMetaTileEntity metaTileEntity;
            if ((metaTileEntity = ((IGregTechTileEntity) tileEntity).getMetaTileEntity()) != null
                && metaTileEntity.renderInWorld(aWorld, aX, aY, aZ, aBlock, aRenderer)) {
                aRenderer.enableAO = false;
                return true;
            }
        }
        if (tileEntity instanceof IPipeRenderedTileEntity
            && renderPipeBlock(aWorld, aX, aY, aZ, aBlock, (IPipeRenderedTileEntity) tileEntity, aRenderer)) {
            aRenderer.enableAO = false;
            return true;
        }
        if (renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer)) {
            aRenderer.enableAO = false;
            return true;
        }
        return false;
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
