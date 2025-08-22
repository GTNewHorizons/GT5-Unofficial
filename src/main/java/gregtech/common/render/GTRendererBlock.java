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
import static gregtech.api.interfaces.metatileentity.IConnectable.NO_CONNECTION;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.VALID_DIRECTIONS;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import net.minecraft.block.Block;
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
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IAllSidedTexturedTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.SBRContextHolder;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

@ThreadSafeISBRH(perThread = true)
public class GTRendererBlock implements ISimpleBlockRenderingHandler {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    public static final float BLOCK_MIN = 0.0F;
    public static final float BLOCK_MAX = 1.0F;
    private static final float COVER_THICKNESS = BLOCK_MAX / 8.0F;
    private static final float COVER_INNER_MIN = BLOCK_MIN + COVER_THICKNESS;
    private static final float COVER_INNER_MAX = BLOCK_MAX - COVER_THICKNESS;
    private final ITexture[][] textureArray = new ITexture[6][];
    private final ITexture[] overlayHolder = new ITexture[1];

    protected final SBRContextHolder sbrContextHolder = new SBRContextHolder();

    public boolean renderStandardBlock(ISBRWorldContext ctx) {
        final TileEntity tTileEntity = ctx.getTileEntity();
        if (tTileEntity instanceof IPipeRenderedTileEntity pipeRenderedTileEntity) {
            textureArray[0] = pipeRenderedTileEntity.getTextureCovered(DOWN);
            textureArray[1] = pipeRenderedTileEntity.getTextureCovered(UP);
            textureArray[2] = pipeRenderedTileEntity.getTextureCovered(NORTH);
            textureArray[3] = pipeRenderedTileEntity.getTextureCovered(SOUTH);
            textureArray[4] = pipeRenderedTileEntity.getTextureCovered(WEST);
            textureArray[5] = pipeRenderedTileEntity.getTextureCovered(EAST);
            return renderStandardBlock(ctx, textureArray);
        }
        if (tTileEntity instanceof IAllSidedTexturedTileEntity allSidedTexturedTileEntity) {
            ITexture[] texture = allSidedTexturedTileEntity.getTexture(ctx.getBlock());
            textureArray[0] = texture;
            textureArray[1] = texture;
            textureArray[2] = texture;
            textureArray[3] = texture;
            textureArray[4] = texture;
            textureArray[5] = texture;
            return renderStandardBlock(ctx, textureArray);
        }
        if (tTileEntity instanceof ITexturedTileEntity texturedTileEntity) {
            textureArray[0] = texturedTileEntity.getTexture(ctx.getBlock(), DOWN);
            textureArray[1] = texturedTileEntity.getTexture(ctx.getBlock(), UP);
            textureArray[2] = texturedTileEntity.getTexture(ctx.getBlock(), NORTH);
            textureArray[3] = texturedTileEntity.getTexture(ctx.getBlock(), SOUTH);
            textureArray[4] = texturedTileEntity.getTexture(ctx.getBlock(), WEST);
            textureArray[5] = texturedTileEntity.getTexture(ctx.getBlock(), EAST);
            return renderStandardBlock(ctx, textureArray);
        }

        return false;
    }

    public boolean renderStandardBlock(ISBRWorldContext ctx, ITexture[][] aTextures) {
        ctx.getBlock()
            .setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
        ctx.setRenderBoundsFromBlock();

        final ITexture[] overlays = RenderOverlay.get(ctx.getBlockAccess(), ctx.getX(), ctx.getY(), ctx.getZ());
        final ITexture[] overlayHolder = this.overlayHolder;
        if (overlays != null) {
            ctx.renderNegativeYFacing(aTextures[SIDE_DOWN]);
            if (overlays[SIDE_DOWN] != null) {
                overlayHolder[0] = overlays[SIDE_DOWN];
                ctx.renderNegativeYFacing(overlayHolder);
            }
            ctx.renderPositiveYFacing(aTextures[SIDE_UP]);
            if (overlays[SIDE_UP] != null) {
                overlayHolder[0] = overlays[SIDE_UP];
                ctx.renderPositiveYFacing(overlayHolder);
            }
            ctx.renderNegativeZFacing(aTextures[SIDE_NORTH]);
            if (overlays[SIDE_NORTH] != null) {
                overlayHolder[0] = overlays[SIDE_NORTH];
                ctx.renderNegativeZFacing(overlayHolder);
            }
            ctx.renderPositiveZFacing(aTextures[SIDE_SOUTH]);
            if (overlays[SIDE_SOUTH] != null) {
                overlayHolder[0] = overlays[SIDE_SOUTH];
                ctx.renderPositiveZFacing(overlayHolder);
            }
            ctx.renderNegativeXFacing(aTextures[SIDE_WEST]);
            if (overlays[SIDE_WEST] != null) {
                overlayHolder[0] = overlays[SIDE_WEST];
                ctx.renderNegativeXFacing(overlayHolder);
            }
            ctx.renderPositiveXFacing(aTextures[SIDE_EAST]);
            if (overlays[SIDE_EAST] != null) {
                overlayHolder[0] = overlays[SIDE_EAST];
                ctx.renderPositiveXFacing(overlayHolder);
            }
        } else {
            ctx.renderNegativeYFacing(aTextures[SIDE_DOWN]);
            ctx.renderPositiveYFacing(aTextures[SIDE_UP]);
            ctx.renderNegativeZFacing(aTextures[SIDE_NORTH]);
            ctx.renderPositiveZFacing(aTextures[SIDE_SOUTH]);
            ctx.renderNegativeXFacing(aTextures[SIDE_WEST]);
            ctx.renderPositiveXFacing(aTextures[SIDE_EAST]);
        }
        return true;
    }

    final ITexture[][] tIcons = new ITexture[VALID_DIRECTIONS.length][];
    final ITexture[][] tCovers = new ITexture[VALID_DIRECTIONS.length][];
    final boolean[] tIsCovered = new boolean[VALID_DIRECTIONS.length];

    public boolean renderPipeBlock(ISBRWorldContext ctx, IPipeRenderedTileEntity aTileEntity) {
        final byte aConnections = aTileEntity.getConnections();
        final float thickness = aTileEntity.getThickNess();
        if (thickness >= 0.99F) {
            return renderStandardBlock(ctx);
        }
        // Range of block occupied by pipe
        final float pipeMin = (BLOCK_MAX - thickness) / 2.0F;
        final float pipeMax = BLOCK_MAX - pipeMin;

        final Block block = ctx.getBlock();
        final boolean[] tIsCovered = this.tIsCovered;
        final ITexture[][] tCovers = this.tCovers;
        final ITexture[][] tIcons = this.tIcons;
        for (int i = 0; i < VALID_DIRECTIONS.length; i++) {
            final ForgeDirection iSide = VALID_DIRECTIONS[i];
            tIsCovered[i] = aTileEntity.hasCoverAtSide(iSide);
            tCovers[i] = aTileEntity.getTexture(block, iSide);
            tIcons[i] = aTileEntity.getTextureUncovered(iSide);
        }

        switch (aConnections) {
            case NO_CONNECTION -> {
                block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                ctx.setRenderBoundsFromBlock();
                ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
                ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);
            }
            case CONNECTED_EAST | CONNECTED_WEST -> {
                // EAST - WEST Pipe Sides
                block.setBlockBounds(BLOCK_MIN, pipeMin, pipeMin, BLOCK_MAX, pipeMax, pipeMax);
                ctx.setRenderBoundsFromBlock();
                ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);

                // EAST - WEST Pipe Ends
                ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);
            }
            case CONNECTED_DOWN | CONNECTED_UP -> {
                // UP - DOWN Pipe Sides
                block.setBlockBounds(pipeMin, BLOCK_MIN, pipeMin, pipeMax, BLOCK_MAX, pipeMax);
                ctx.setRenderBoundsFromBlock();
                ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
                ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);

                // UP - DOWN Pipe Ends
                ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
            }
            case CONNECTED_NORTH | CONNECTED_SOUTH -> {
                // NORTH - SOUTH Pipe Sides
                block.setBlockBounds(pipeMin, pipeMin, BLOCK_MIN, pipeMax, pipeMax, BLOCK_MAX);
                ctx.setRenderBoundsFromBlock();
                ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);

                // NORTH - SOUTH Pipe Ends
                ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
            }
            default -> {
                if ((aConnections & CONNECTED_WEST) == 0) {
                    block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                } else {
                    block.setBlockBounds(BLOCK_MIN, pipeMin, pipeMin, pipeMin, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                    ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                    ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                    ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
                }
                ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                if ((aConnections & CONNECTED_EAST) == 0) {
                    block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                } else {
                    block.setBlockBounds(pipeMax, pipeMin, pipeMin, BLOCK_MAX, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                    ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                    ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                    ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
                }
                ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);
                if ((aConnections & CONNECTED_DOWN) == 0) {
                    block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                } else {
                    block.setBlockBounds(pipeMin, BLOCK_MIN, pipeMin, pipeMax, pipeMin, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                    ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
                    ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                    ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);
                }
                ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                if ((aConnections & CONNECTED_UP) == 0) {
                    block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                } else {
                    block.setBlockBounds(pipeMin, pipeMax, pipeMin, pipeMax, BLOCK_MAX, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                    ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
                    ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                    ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);
                }
                ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                if ((aConnections & CONNECTED_NORTH) == 0) {
                    block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                } else {
                    block.setBlockBounds(pipeMin, pipeMin, BLOCK_MIN, pipeMax, pipeMax, pipeMin);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                    ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                    ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                    ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);
                }
                ctx.renderNegativeZFacing(tIcons[SIDE_NORTH]);
                if ((aConnections & CONNECTED_SOUTH) == 0) {
                    block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.setRenderBoundsFromBlock();
                } else {
                    block.setBlockBounds(pipeMin, pipeMin, pipeMax, pipeMax, pipeMax, BLOCK_MAX);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeYFacing(tIcons[SIDE_DOWN]);
                    ctx.renderPositiveYFacing(tIcons[SIDE_UP]);
                    ctx.renderNegativeXFacing(tIcons[SIDE_WEST]);
                    ctx.renderPositiveXFacing(tIcons[SIDE_EAST]);
                }
                ctx.renderPositiveZFacing(tIcons[SIDE_SOUTH]);
            }
        }

        // Render covers on pipes
        final RenderBlocks renderBlocks = ctx.getRenderBlocks();
        if (tIsCovered[SIDE_DOWN]) {
            block.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, COVER_INNER_MIN, BLOCK_MAX);
            ctx.setRenderBoundsFromBlock();
            if (!tIsCovered[SIDE_NORTH]) {
                ctx.renderNegativeZFacing(tCovers[SIDE_DOWN]);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                ctx.renderPositiveZFacing(tCovers[SIDE_DOWN]);
            }
            if (!tIsCovered[SIDE_WEST]) {
                ctx.renderNegativeXFacing(tCovers[SIDE_DOWN]);
            }
            if (!tIsCovered[SIDE_EAST]) {
                ctx.renderPositiveXFacing(tCovers[SIDE_DOWN]);
            }
            ctx.renderPositiveYFacing(tCovers[SIDE_DOWN]);
            if ((aConnections & CONNECTED_DOWN) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MIN, pipeMin);
                ctx.renderNegativeYFacing(tCovers[SIDE_DOWN]);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, pipeMax, BLOCK_MAX, BLOCK_MIN, BLOCK_MAX);
                ctx.renderNegativeYFacing(tCovers[SIDE_DOWN]);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, pipeMin, pipeMin, BLOCK_MIN, pipeMax);
                ctx.renderNegativeYFacing(tCovers[SIDE_DOWN]);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, BLOCK_MIN, pipeMin, BLOCK_MAX, BLOCK_MIN, pipeMax);
            }
            ctx.renderNegativeYFacing(tCovers[SIDE_DOWN]);
        }

        if (tIsCovered[SIDE_UP]) {
            block.setBlockBounds(BLOCK_MIN, COVER_INNER_MAX, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
            ctx.setRenderBoundsFromBlock();
            if (!tIsCovered[SIDE_NORTH]) ctx.renderNegativeZFacing(tCovers[SIDE_UP]);
            if (!tIsCovered[SIDE_SOUTH]) ctx.renderPositiveZFacing(tCovers[SIDE_UP]);
            if (!tIsCovered[SIDE_WEST]) ctx.renderNegativeXFacing(tCovers[SIDE_UP]);
            if (!tIsCovered[SIDE_EAST]) ctx.renderPositiveXFacing(tCovers[SIDE_UP]);
            ctx.renderNegativeYFacing(tCovers[SIDE_UP]);
            if ((aConnections & CONNECTED_UP) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MAX, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, pipeMin);
                ctx.renderPositiveYFacing(tCovers[SIDE_UP]);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MAX, pipeMax, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
                ctx.renderPositiveYFacing(tCovers[SIDE_UP]);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MAX, pipeMin, pipeMin, BLOCK_MAX, pipeMax);
                ctx.renderPositiveYFacing(tCovers[SIDE_UP]);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, BLOCK_MAX, pipeMin, BLOCK_MAX, BLOCK_MAX, pipeMax);
            }
            ctx.renderPositiveYFacing(tCovers[SIDE_UP]);
        }

        if (tIsCovered[SIDE_NORTH]) {
            block.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, COVER_INNER_MIN);
            ctx.setRenderBoundsFromBlock();
            if (!tIsCovered[SIDE_DOWN]) ctx.renderNegativeYFacing(tCovers[SIDE_NORTH]);
            if (!tIsCovered[SIDE_UP]) ctx.renderPositiveYFacing(tCovers[SIDE_NORTH]);
            if (!tIsCovered[SIDE_WEST]) ctx.renderNegativeXFacing(tCovers[SIDE_NORTH]);
            if (!tIsCovered[SIDE_EAST]) ctx.renderPositiveXFacing(tCovers[SIDE_NORTH]);
            ctx.renderPositiveZFacing(tCovers[SIDE_NORTH]);
            if ((aConnections & CONNECTED_NORTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, pipeMin, BLOCK_MIN);
                ctx.renderNegativeZFacing(tCovers[SIDE_NORTH]);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMax, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MIN);
                ctx.renderNegativeZFacing(tCovers[SIDE_NORTH]);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, BLOCK_MIN, pipeMin, pipeMax, BLOCK_MIN);
                ctx.renderNegativeZFacing(tCovers[SIDE_NORTH]);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, pipeMin, BLOCK_MIN, BLOCK_MAX, pipeMax, BLOCK_MIN);
            }
            ctx.renderNegativeZFacing(tCovers[SIDE_NORTH]);
        }

        if (tIsCovered[SIDE_SOUTH]) {
            block.setBlockBounds(BLOCK_MIN, BLOCK_MIN, COVER_INNER_MAX, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
            ctx.setRenderBoundsFromBlock();
            if (!tIsCovered[SIDE_DOWN]) ctx.renderNegativeYFacing(tCovers[SIDE_SOUTH]);
            if (!tIsCovered[SIDE_UP]) ctx.renderPositiveYFacing(tCovers[SIDE_SOUTH]);
            if (!tIsCovered[SIDE_WEST]) ctx.renderNegativeXFacing(tCovers[SIDE_SOUTH]);
            if (!tIsCovered[SIDE_EAST]) ctx.renderPositiveXFacing(tCovers[SIDE_SOUTH]);
            ctx.renderNegativeZFacing(tCovers[SIDE_SOUTH]);
            if ((aConnections & CONNECTED_SOUTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, pipeMin, BLOCK_MAX);
                ctx.renderPositiveZFacing(tCovers[SIDE_SOUTH]);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMax, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
                ctx.renderPositiveZFacing(tCovers[SIDE_SOUTH]);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, BLOCK_MAX, pipeMin, pipeMax, BLOCK_MAX);
                ctx.renderPositiveZFacing(tCovers[SIDE_SOUTH]);
                // Middle right panel
                renderBlocks.setRenderBounds(pipeMax, pipeMin, BLOCK_MAX, BLOCK_MAX, pipeMax, BLOCK_MAX);
            }
            ctx.renderPositiveZFacing(tCovers[SIDE_SOUTH]);
        }

        if (tIsCovered[SIDE_WEST]) {
            block.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, COVER_INNER_MIN, BLOCK_MAX, BLOCK_MAX);
            ctx.setRenderBoundsFromBlock();
            if (!tIsCovered[SIDE_DOWN]) ctx.renderNegativeYFacing(tCovers[SIDE_WEST]);
            if (!tIsCovered[SIDE_UP]) ctx.renderPositiveYFacing(tCovers[SIDE_WEST]);
            if (!tIsCovered[SIDE_NORTH]) ctx.renderNegativeZFacing(tCovers[SIDE_WEST]);
            if (!tIsCovered[SIDE_SOUTH]) ctx.renderPositiveZFacing(tCovers[SIDE_WEST]);
            ctx.renderPositiveXFacing(tCovers[SIDE_WEST]);
            if ((aConnections & CONNECTED_WEST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, pipeMin, BLOCK_MAX);
                ctx.renderNegativeXFacing(tCovers[SIDE_WEST]);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMax, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX);
                ctx.renderNegativeXFacing(tCovers[SIDE_WEST]);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, BLOCK_MIN, BLOCK_MIN, pipeMax, pipeMin);
                ctx.renderNegativeXFacing(tCovers[SIDE_WEST]);
                // Middle right panel
                renderBlocks.setRenderBounds(BLOCK_MIN, pipeMin, pipeMax, BLOCK_MIN, pipeMax, BLOCK_MAX);
            }
            ctx.renderNegativeXFacing(tCovers[SIDE_WEST]);
        }

        if (tIsCovered[SIDE_EAST]) {
            block.setBlockBounds(COVER_INNER_MAX, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
            ctx.setRenderBoundsFromBlock();
            if (!tIsCovered[SIDE_DOWN]) ctx.renderNegativeYFacing(tCovers[SIDE_EAST]);
            if (!tIsCovered[SIDE_UP]) ctx.renderPositiveYFacing(tCovers[SIDE_EAST]);
            if (!tIsCovered[SIDE_NORTH]) ctx.renderNegativeZFacing(tCovers[SIDE_EAST]);
            if (!tIsCovered[SIDE_SOUTH]) ctx.renderPositiveZFacing(tCovers[SIDE_EAST]);
            ctx.renderNegativeXFacing(tCovers[SIDE_EAST]);

            if ((aConnections & CONNECTED_EAST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                renderBlocks.setRenderBounds(BLOCK_MAX, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, pipeMin, BLOCK_MAX);
                ctx.renderPositiveXFacing(tCovers[SIDE_EAST]);
                // Upper panel
                renderBlocks.setRenderBounds(BLOCK_MAX, pipeMax, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
                ctx.renderPositiveXFacing(tCovers[SIDE_EAST]);
                // Middle left panel
                renderBlocks.setRenderBounds(BLOCK_MAX, pipeMin, BLOCK_MIN, BLOCK_MAX, pipeMax, pipeMin);
                ctx.renderPositiveXFacing(tCovers[SIDE_EAST]);
                // Middle right panel
                renderBlocks.setRenderBounds(BLOCK_MAX, pipeMin, pipeMax, BLOCK_MAX, pipeMax, BLOCK_MAX);
            }
            ctx.renderPositiveXFacing(tCovers[SIDE_EAST]);
        }
        block.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);
        ctx.setRenderBoundsFromBlock();

        return true;
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("MethodWithTooManyParameters")
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
    @SuppressWarnings("MethodWithTooManyParameters")
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
        final ISBRInventoryContext ctx = sbrContextHolder.getSBRInventoryContext(aBlock, aMeta, aModelID, aRenderer);
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        if (aBlock instanceof BlockOresAbstract) {
            tTileEntity.mMetaData = ((short) aMeta);

            aBlock.setBlockBoundsForItemRender();
            aRenderer.setRenderBoundsFromBlock(aBlock);
            // spotless:off
            final ITexture[] texture = tTileEntity.getTexture(aBlock);
            ctx.renderNegativeYFacing(texture);
            ctx.renderPositiveYFacing(texture);
            ctx.renderNegativeZFacing(texture);
            ctx.renderPositiveZFacing(texture);
            ctx.renderNegativeXFacing(texture);
            ctx.renderPositiveXFacing(texture);
            // spotless:on
        } else if (aMeta > 0 && (aMeta < GregTechAPI.METATILEENTITIES.length)
            && aBlock instanceof BlockMachines
            && (GregTechAPI.METATILEENTITIES[aMeta] != null)
            && (!GregTechAPI.METATILEENTITIES[aMeta].render(ctx))) {
                renderNormalInventoryMetaTileEntity(ctx);
            } else if (aBlock instanceof BlockFrameBox) {
                ITexture[] texture = ((BlockFrameBox) aBlock).getTexture(aMeta);
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                // spotless:off
            ctx.renderNegativeYFacing(texture);
            ctx.renderPositiveYFacing(texture);
            ctx.renderNegativeZFacing(texture);
            ctx.renderPositiveZFacing(texture);
            ctx.renderNegativeXFacing(texture);
            ctx.renderPositiveXFacing(texture);
            // spotless:on
            } else if (aBlock instanceof IBlockWithTextures texturedBlock) {
                ITexture[][] texture = texturedBlock.getTextures(aMeta);
                if (texture != null) {
                    // spotless:off
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                ctx.renderNegativeYFacing(texture[ForgeDirection.DOWN.ordinal()]);
                ctx.renderPositiveYFacing(texture[ForgeDirection.UP.ordinal()]);
                ctx.renderNegativeZFacing(texture[ForgeDirection.NORTH.ordinal()]);
                ctx.renderPositiveZFacing(texture[ForgeDirection.SOUTH.ordinal()]);
                ctx.renderNegativeXFacing(texture[ForgeDirection.WEST.ordinal()]);
                ctx.renderPositiveXFacing(texture[ForgeDirection.EAST.ordinal()]);
                // spotless:on
                }
            }
        aBlock.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);

        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    private static void renderNormalInventoryMetaTileEntity(ISBRInventoryContext ctx) {
        if ((ctx.getMeta() <= 0) || (ctx.getMeta() >= GregTechAPI.METATILEENTITIES.length)) {
            return;
        }
        final IMetaTileEntity tMetaTileEntity = GregTechAPI.METATILEENTITIES[ctx.getMeta()];
        if (tMetaTileEntity == null) {
            return;
        }
        ctx.getBlock()
            .setBlockBoundsForItemRender();
        ctx.setRenderBoundsFromBlock();

        final IGregTechTileEntity iGregTechTileEntity = tMetaTileEntity.getBaseMetaTileEntity();
        // spotless:off
        if ((iGregTechTileEntity instanceof IPipeRenderedTileEntity renderedPipe)
            && (tMetaTileEntity instanceof MetaPipeEntity pipeEntity)) {
            final float tThickness = renderedPipe.getThickNess();
            final float pipeMin = (BLOCK_MAX - tThickness) / 2.0F;
            final float pipeMax = BLOCK_MAX - pipeMin;

            ctx.getBlock().setBlockBounds(BLOCK_MIN, pipeMin, pipeMin, BLOCK_MAX, pipeMax, pipeMax);
            ctx.setRenderBoundsFromBlock();
            ctx.renderNegativeYFacing(pipeEntity.getTexture(iGregTechTileEntity, DOWN, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false));
            ctx.renderPositiveYFacing(pipeEntity.getTexture(iGregTechTileEntity, UP, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false));
            ctx.renderNegativeZFacing(pipeEntity.getTexture(iGregTechTileEntity, NORTH, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false));
            ctx.renderPositiveZFacing(pipeEntity.getTexture(iGregTechTileEntity, SOUTH, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false));
            ctx.renderNegativeXFacing(pipeEntity.getTexture(iGregTechTileEntity, WEST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false));
            ctx.renderPositiveXFacing(pipeEntity.getTexture(iGregTechTileEntity, EAST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false));
        } else {
            ctx.renderNegativeYFacing(tMetaTileEntity.getTexture(iGregTechTileEntity, DOWN, WEST, -1, true, false));
            ctx.renderPositiveYFacing(tMetaTileEntity.getTexture(iGregTechTileEntity, UP, WEST, -1, true, false));
            ctx.renderNegativeZFacing(tMetaTileEntity.getTexture(iGregTechTileEntity, NORTH, WEST, -1, true, false));
            ctx.renderPositiveZFacing(tMetaTileEntity.getTexture(iGregTechTileEntity, SOUTH, WEST, -1, true, false));
            ctx.renderNegativeXFacing(tMetaTileEntity.getTexture(iGregTechTileEntity, WEST, WEST, -1, true, false));
            ctx.renderPositiveXFacing(tMetaTileEntity.getTexture(iGregTechTileEntity, EAST, WEST, -1, true, false));
        }
        // spotless:on
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        final ISBRWorldContext ctx = sbrContextHolder.getSBRWorldContext(aX, aY, aZ, aBlock, aModelID, aRenderer);

        final TileEntity tileEntity = ctx.getTileEntity();
        final TesselatorAccessor tessAccess = (TesselatorAccessor) Tessellator.instance;

        // If this block does not have a TE, render it as a normal block.
        // Otherwise, render the TE instead.
        if (tileEntity == null && ctx.getBlock() instanceof BlockFrameBox frameBlock) {
            int meta = aWorld.getBlockMetadata(ctx.getX(), ctx.getY(), ctx.getZ());
            ITexture[] texture = frameBlock.getTexture(meta);
            if (texture == null) return false;
            textureArray[0] = texture;
            textureArray[1] = texture;
            textureArray[2] = texture;
            textureArray[3] = texture;
            textureArray[4] = texture;
            textureArray[5] = texture;
            renderStandardBlock(ctx, textureArray);
            return tessAccess.gt5u$hasVertices();
        }

        if (ctx.getBlock() instanceof IBlockWithTextures texturedBlock) {
            final int meta = aWorld.getBlockMetadata(ctx.getX(), ctx.getY(), ctx.getZ());
            ITexture[][] texture = texturedBlock.getTextures(meta);
            if (texture == null) return false;
            renderStandardBlock(ctx, texture);
            return tessAccess.gt5u$hasVertices();
        }

        if (tileEntity == null) return false;

        if (tileEntity instanceof IGregTechTileEntity) {
            final IMetaTileEntity metaTileEntity;
            if ((metaTileEntity = ((IGregTechTileEntity) tileEntity).getMetaTileEntity()) != null
                && metaTileEntity.render(ctx)) {
                return tessAccess.gt5u$hasVertices();
            }
        }
        if (tileEntity instanceof IPipeRenderedTileEntity
            && renderPipeBlock(ctx, (IPipeRenderedTileEntity) tileEntity)) {
            return tessAccess.gt5u$hasVertices();
        }
        if (renderStandardBlock(ctx)) {
            return tessAccess.gt5u$hasVertices();
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int aModel) {
        return true;
    }

    @Override
    public int getRenderId() {
        return RENDER_ID;
    }
}
