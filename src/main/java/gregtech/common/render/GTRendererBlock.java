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
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IAllSidedTexturedTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.SBRContext;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.blocks.BlockOresAbstract;
import gregtech.common.blocks.TileEntityOres;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

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
    private final ITexture[] overlayHolder = new ITexture[1];

    @SuppressWarnings("MethodWithTooManyParameters")
    public boolean renderStandardBlock(SBRContext ctx) {
        final TileEntity tTileEntity = ctx.world.getTileEntity(ctx.x, ctx.y, ctx.z);
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
            ITexture[] texture = allSidedTexturedTileEntity.getTexture(ctx.block);
            textureArray[0] = texture;
            textureArray[1] = texture;
            textureArray[2] = texture;
            textureArray[3] = texture;
            textureArray[4] = texture;
            textureArray[5] = texture;
            return renderStandardBlock(ctx, textureArray);
        }
        if (tTileEntity instanceof ITexturedTileEntity texturedTileEntity) {
            textureArray[0] = texturedTileEntity.getTexture(ctx.block, DOWN);
            textureArray[1] = texturedTileEntity.getTexture(ctx.block, UP);
            textureArray[2] = texturedTileEntity.getTexture(ctx.block, NORTH);
            textureArray[3] = texturedTileEntity.getTexture(ctx.block, SOUTH);
            textureArray[4] = texturedTileEntity.getTexture(ctx.block, WEST);
            textureArray[5] = texturedTileEntity.getTexture(ctx.block, EAST);
            return renderStandardBlock(ctx, textureArray);
        }

        return false;
    }

    public boolean renderStandardBlock(SBRContext ctx, ITexture[][] aTextures) {
        ctx.block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        ctx.renderer.setRenderBoundsFromBlock(ctx.block);

        ITexture[] overlays = RenderOverlay.get(ctx.world, ctx.x, ctx.y, ctx.z);
        if (overlays != null) {
            renderNegativeYFacing(ctx, aTextures[SIDE_DOWN], true);
            if (overlays[SIDE_DOWN] != null) {
                overlayHolder[0] = overlays[SIDE_DOWN];
                renderNegativeYFacing(ctx, overlayHolder, true);
            }
            renderPositiveYFacing(ctx, aTextures[SIDE_UP], true);
            if (overlays[SIDE_UP] != null) {
                overlayHolder[0] = overlays[SIDE_UP];
                renderPositiveYFacing(ctx, overlayHolder, true);
            }
            renderNegativeZFacing(ctx, aTextures[SIDE_NORTH], true);
            if (overlays[SIDE_NORTH] != null) {
                overlayHolder[0] = overlays[SIDE_NORTH];
                renderNegativeZFacing(ctx, overlayHolder, true);
            }
            renderPositiveZFacing(ctx, aTextures[SIDE_SOUTH], true);
            if (overlays[SIDE_SOUTH] != null) {
                overlayHolder[0] = overlays[SIDE_SOUTH];
                renderPositiveZFacing(ctx, overlayHolder, true);
            }
            renderNegativeXFacing(ctx, aTextures[SIDE_WEST], true);
            if (overlays[SIDE_WEST] != null) {
                overlayHolder[0] = overlays[SIDE_WEST];
                renderNegativeXFacing(ctx, overlayHolder, true);
            }
            renderPositiveXFacing(ctx, aTextures[SIDE_EAST], true);
            if (overlays[SIDE_EAST] != null) {
                overlayHolder[0] = overlays[SIDE_EAST];
                renderPositiveXFacing(ctx, overlayHolder, true);
            }
        } else {
            renderNegativeYFacing(ctx, aTextures[SIDE_DOWN], true);
            renderPositiveYFacing(ctx, aTextures[SIDE_UP], true);
            renderNegativeZFacing(ctx, aTextures[SIDE_NORTH], true);
            renderPositiveZFacing(ctx, aTextures[SIDE_SOUTH], true);
            renderNegativeXFacing(ctx, aTextures[SIDE_WEST], true);
            renderPositiveXFacing(ctx, aTextures[SIDE_EAST], true);
        }
        return true;
    }

    final ITexture[][] tIcons = new ITexture[VALID_DIRECTIONS.length][];
    final ITexture[][] tCovers = new ITexture[VALID_DIRECTIONS.length][];
    final boolean[] tIsCovered = new boolean[VALID_DIRECTIONS.length];

    public boolean renderPipeBlock(SBRContext ctx, IPipeRenderedTileEntity aTileEntity) {
        final byte aConnections = aTileEntity.getConnections();
        final float thickness = aTileEntity.getThickNess();
        if (thickness >= 0.99F) {
            return renderStandardBlock(ctx);
        }
        // Range of block occupied by pipe
        final float pipeMin = (blockMax - thickness) / 2.0F;
        final float pipeMax = blockMax - pipeMin;

        for (int i = 0; i < VALID_DIRECTIONS.length; i++) {
            final ForgeDirection iSide = VALID_DIRECTIONS[i];
            tIsCovered[i] = aTileEntity.hasCoverAtSide(iSide);
            tCovers[i] = aTileEntity.getTexture(ctx.block, iSide);
            tIcons[i] = aTileEntity.getTextureUncovered(iSide);
        }

        switch (aConnections) {
            case NO_CONNECTION -> {
                ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
                renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);
            }
            case CONNECTED_EAST | CONNECTED_WEST -> {
                // EAST - WEST Pipe Sides
                ctx.block.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);

                // EAST - WEST Pipe Ends
                renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);
            }
            case CONNECTED_DOWN | CONNECTED_UP -> {
                // UP - DOWN Pipe Sides
                ctx.block.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, blockMax, pipeMax);
                ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
                renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);

                // UP - DOWN Pipe Ends
                renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
            }
            case CONNECTED_NORTH | CONNECTED_SOUTH -> {
                // NORTH - SOUTH Pipe Sides
                ctx.block.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, blockMax);
                ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);

                // NORTH - SOUTH Pipe Ends
                renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
            }
            default -> {
                if ((aConnections & CONNECTED_WEST) == 0) {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                } else {
                    ctx.block.setBlockBounds(blockMin, pipeMin, pipeMin, pipeMin, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                    renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
                }
                renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                if ((aConnections & CONNECTED_EAST) == 0) {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                } else {
                    ctx.block.setBlockBounds(pipeMax, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                    renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
                }
                renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);
                if ((aConnections & CONNECTED_DOWN) == 0) {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                } else {
                    ctx.block.setBlockBounds(pipeMin, blockMin, pipeMin, pipeMax, pipeMin, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
                    renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);
                }
                renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                if ((aConnections & CONNECTED_UP) == 0) {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                } else {
                    ctx.block.setBlockBounds(pipeMin, pipeMax, pipeMin, pipeMax, blockMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                    renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
                    renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);
                }
                renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                if ((aConnections & CONNECTED_NORTH) == 0) {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                } else {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, blockMin, pipeMax, pipeMax, pipeMin);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                    renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);
                }
                renderNegativeZFacing(ctx, tIcons[SIDE_NORTH], false);
                if ((aConnections & CONNECTED_SOUTH) == 0) {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMin, pipeMax, pipeMax, pipeMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                } else {
                    ctx.block.setBlockBounds(pipeMin, pipeMin, pipeMax, pipeMax, pipeMax, blockMax);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeYFacing(ctx, tIcons[SIDE_DOWN], false);
                    renderPositiveYFacing(ctx, tIcons[SIDE_UP], false);
                    renderNegativeXFacing(ctx, tIcons[SIDE_WEST], false);
                    renderPositiveXFacing(ctx, tIcons[SIDE_EAST], false);
                }
                renderPositiveZFacing(ctx, tIcons[SIDE_SOUTH], false);
            }
        }

        // Render covers on pipes
        if (tIsCovered[SIDE_DOWN]) {
            ctx.block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, coverInnerMin, blockMax);
            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(ctx, tCovers[SIDE_DOWN], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(ctx, tCovers[SIDE_DOWN], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(ctx, tCovers[SIDE_DOWN], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(ctx, tCovers[SIDE_DOWN], false);
            }
            renderPositiveYFacing(ctx, tCovers[SIDE_DOWN], false);
            if ((aConnections & CONNECTED_DOWN) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                ctx.renderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, blockMin, pipeMin);
                renderNegativeYFacing(ctx, tCovers[SIDE_DOWN], false);
                // Upper panel
                ctx.renderer.setRenderBounds(blockMin, blockMin, pipeMax, blockMax, blockMin, blockMax);
                renderNegativeYFacing(ctx, tCovers[SIDE_DOWN], false);
                // Middle left panel
                ctx.renderer.setRenderBounds(blockMin, blockMin, pipeMin, pipeMin, blockMin, pipeMax);
                renderNegativeYFacing(ctx, tCovers[SIDE_DOWN], false);
                // Middle right panel
                ctx.renderer.setRenderBounds(pipeMax, blockMin, pipeMin, blockMax, blockMin, pipeMax);
            }
            renderNegativeYFacing(ctx, tCovers[SIDE_DOWN], false);
        }

        if (tIsCovered[SIDE_UP]) {
            ctx.block.setBlockBounds(blockMin, coverInnerMax, blockMin, blockMax, blockMax, blockMax);
            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(ctx, tCovers[SIDE_UP], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(ctx, tCovers[SIDE_UP], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(ctx, tCovers[SIDE_UP], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(ctx, tCovers[SIDE_UP], false);
            }
            renderNegativeYFacing(ctx, tCovers[SIDE_UP], false);
            if ((aConnections & CONNECTED_UP) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                ctx.renderer.setRenderBounds(blockMin, blockMax, blockMin, blockMax, blockMax, pipeMin);
                renderPositiveYFacing(ctx, tCovers[SIDE_UP], false);
                // Upper panel
                ctx.renderer.setRenderBounds(blockMin, blockMax, pipeMax, blockMax, blockMax, blockMax);
                renderPositiveYFacing(ctx, tCovers[SIDE_UP], false);
                // Middle left panel
                ctx.renderer.setRenderBounds(blockMin, blockMax, pipeMin, pipeMin, blockMax, pipeMax);
                renderPositiveYFacing(ctx, tCovers[SIDE_UP], false);
                // Middle right panel
                ctx.renderer.setRenderBounds(pipeMax, blockMax, pipeMin, blockMax, blockMax, pipeMax);
            }
            renderPositiveYFacing(ctx, tCovers[SIDE_UP], false);
        }

        if (tIsCovered[SIDE_NORTH]) {
            ctx.block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, coverInnerMin);
            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(ctx, tCovers[SIDE_NORTH], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(ctx, tCovers[SIDE_NORTH], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(ctx, tCovers[SIDE_NORTH], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(ctx, tCovers[SIDE_NORTH], false);
            }
            renderPositiveZFacing(ctx, tCovers[SIDE_NORTH], false);
            if ((aConnections & CONNECTED_NORTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                ctx.renderer.setRenderBounds(blockMin, blockMin, blockMin, blockMax, pipeMin, blockMin);
                renderNegativeZFacing(ctx, tCovers[SIDE_NORTH], false);
                // Upper panel
                ctx.renderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMax, blockMax, blockMin);
                renderNegativeZFacing(ctx, tCovers[SIDE_NORTH], false);
                // Middle left panel
                ctx.renderer.setRenderBounds(blockMin, pipeMin, blockMin, pipeMin, pipeMax, blockMin);
                renderNegativeZFacing(ctx, tCovers[SIDE_NORTH], false);
                // Middle right panel
                ctx.renderer.setRenderBounds(pipeMax, pipeMin, blockMin, blockMax, pipeMax, blockMin);
            }
            renderNegativeZFacing(ctx, tCovers[SIDE_NORTH], false);
        }

        if (tIsCovered[SIDE_SOUTH]) {
            ctx.block.setBlockBounds(blockMin, blockMin, coverInnerMax, blockMax, blockMax, blockMax);
            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(ctx, tCovers[SIDE_SOUTH], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(ctx, tCovers[SIDE_SOUTH], false);
            }
            if (!tIsCovered[SIDE_WEST]) {
                renderNegativeXFacing(ctx, tCovers[SIDE_SOUTH], false);
            }
            if (!tIsCovered[SIDE_EAST]) {
                renderPositiveXFacing(ctx, tCovers[SIDE_SOUTH], false);
            }
            renderNegativeZFacing(ctx, tCovers[SIDE_SOUTH], false);
            if ((aConnections & CONNECTED_SOUTH) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                ctx.renderer.setRenderBounds(blockMin, blockMin, blockMax, blockMax, pipeMin, blockMax);
                renderPositiveZFacing(ctx, tCovers[SIDE_SOUTH], false);
                // Upper panel
                ctx.renderer.setRenderBounds(blockMin, pipeMax, blockMax, blockMax, blockMax, blockMax);
                renderPositiveZFacing(ctx, tCovers[SIDE_SOUTH], false);
                // Middle left panel
                ctx.renderer.setRenderBounds(blockMin, pipeMin, blockMax, pipeMin, pipeMax, blockMax);
                renderPositiveZFacing(ctx, tCovers[SIDE_SOUTH], false);
                // Middle right panel
                ctx.renderer.setRenderBounds(pipeMax, pipeMin, blockMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveZFacing(ctx, tCovers[SIDE_SOUTH], false);
        }

        if (tIsCovered[SIDE_WEST]) {
            ctx.block.setBlockBounds(blockMin, blockMin, blockMin, coverInnerMin, blockMax, blockMax);
            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(ctx, tCovers[SIDE_WEST], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(ctx, tCovers[SIDE_WEST], false);
            }
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(ctx, tCovers[SIDE_WEST], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(ctx, tCovers[SIDE_WEST], false);
            }
            renderPositiveXFacing(ctx, tCovers[SIDE_WEST], false);
            if ((aConnections & CONNECTED_WEST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                ctx.renderer.setRenderBounds(blockMin, blockMin, blockMin, blockMin, pipeMin, blockMax);
                renderNegativeXFacing(ctx, tCovers[SIDE_WEST], false);
                // Upper panel
                ctx.renderer.setRenderBounds(blockMin, pipeMax, blockMin, blockMin, blockMax, blockMax);
                renderNegativeXFacing(ctx, tCovers[SIDE_WEST], false);
                // Middle left panel
                ctx.renderer.setRenderBounds(blockMin, pipeMin, blockMin, blockMin, pipeMax, pipeMin);
                renderNegativeXFacing(ctx, tCovers[SIDE_WEST], false);
                // Middle right panel
                ctx.renderer.setRenderBounds(blockMin, pipeMin, pipeMax, blockMin, pipeMax, blockMax);
            }
            renderNegativeXFacing(ctx, tCovers[SIDE_WEST], false);
        }

        if (tIsCovered[SIDE_EAST]) {
            ctx.block.setBlockBounds(coverInnerMax, blockMin, blockMin, blockMax, blockMax, blockMax);
            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
            if (!tIsCovered[SIDE_DOWN]) {
                renderNegativeYFacing(ctx, tCovers[SIDE_EAST], false);
            }
            if (!tIsCovered[SIDE_UP]) {
                renderPositiveYFacing(ctx, tCovers[SIDE_EAST], false);
            }
            if (!tIsCovered[SIDE_NORTH]) {
                renderNegativeZFacing(ctx, tCovers[SIDE_EAST], false);
            }
            if (!tIsCovered[SIDE_SOUTH]) {
                renderPositiveZFacing(ctx, tCovers[SIDE_EAST], false);
            }
            renderNegativeXFacing(ctx, tCovers[SIDE_EAST], false);

            if ((aConnections & CONNECTED_EAST) != 0) {
                // Split outer face to leave hole for pipe
                // Lower panel
                ctx.renderer.setRenderBounds(blockMax, blockMin, blockMin, blockMax, pipeMin, blockMax);
                renderPositiveXFacing(ctx, tCovers[SIDE_EAST], false);
                // Upper panel
                ctx.renderer.setRenderBounds(blockMax, pipeMax, blockMin, blockMax, blockMax, blockMax);
                renderPositiveXFacing(ctx, tCovers[SIDE_EAST], false);
                // Middle left panel
                ctx.renderer.setRenderBounds(blockMax, pipeMin, blockMin, blockMax, pipeMax, pipeMin);
                renderPositiveXFacing(ctx, tCovers[SIDE_EAST], false);
                // Middle right panel
                ctx.renderer.setRenderBounds(blockMax, pipeMin, pipeMax, blockMax, pipeMax, blockMax);
            }
            renderPositiveXFacing(ctx, tCovers[SIDE_EAST], false);
        }
        ctx.block.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        ctx.renderer.setRenderBoundsFromBlock(ctx.block);

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
        final SBRContext ctx = new SBRContext(aBlock, aMeta, aModelID, aRenderer);
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
            renderNegativeYFacing(ctx, texture, true);
            renderPositiveYFacing(ctx, texture, true);
            renderNegativeZFacing(ctx, texture, true);
            renderPositiveZFacing(ctx, texture, true);
            renderNegativeXFacing(ctx, texture, true);
            renderPositiveXFacing(ctx, texture, true);
            // spotless:on
        } else if (aMeta > 0 && (aMeta < GregTechAPI.METATILEENTITIES.length)
            && aBlock instanceof BlockMachines
            && (GregTechAPI.METATILEENTITIES[aMeta] != null)
            && (!GregTechAPI.METATILEENTITIES[aMeta].renderInInventory(aBlock, aMeta, aRenderer))) {
                renderNormalInventoryMetaTileEntity(ctx);
            } else if (aBlock instanceof BlockFrameBox) {
                ITexture[] texture = ((BlockFrameBox) aBlock).getTexture(aMeta);
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                // spotless:off
            renderNegativeYFacing(ctx, texture, true);
            renderPositiveYFacing(ctx, texture, true);
            renderNegativeZFacing(ctx, texture, true);
            renderPositiveZFacing(ctx, texture, true);
            renderNegativeXFacing(ctx, texture, true);
            renderPositiveXFacing(ctx, texture, true);
            // spotless:on
            } else if (aBlock instanceof IBlockWithTextures texturedBlock) {
                ITexture[][] texture = texturedBlock.getTextures(aMeta);
                if (texture != null) {
                    // spotless:off
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                renderNegativeYFacing(ctx, texture[ForgeDirection.DOWN.ordinal()], false);
                renderPositiveYFacing(ctx, texture[ForgeDirection.UP.ordinal()], false);
                renderNegativeZFacing(ctx, texture[ForgeDirection.NORTH.ordinal()], false);
                renderPositiveZFacing(ctx, texture[ForgeDirection.SOUTH.ordinal()], false);
                renderNegativeXFacing(ctx, texture[ForgeDirection.WEST.ordinal()], false);
                renderPositiveXFacing(ctx, texture[ForgeDirection.EAST.ordinal()], false);
                // spotless:on
                }
            }
        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);

        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    private static void renderNormalInventoryMetaTileEntity(SBRContext ctx) {
        if ((ctx.meta <= 0) || (ctx.meta >= GregTechAPI.METATILEENTITIES.length)) {
            return;
        }
        final IMetaTileEntity tMetaTileEntity = GregTechAPI.METATILEENTITIES[ctx.meta];
        if (tMetaTileEntity == null) {
            return;
        }
        ctx.block.setBlockBoundsForItemRender();
        ctx.renderer.setRenderBoundsFromBlock(ctx.block);

        final IGregTechTileEntity iGregTechTileEntity = tMetaTileEntity.getBaseMetaTileEntity();
        // spotless:off
        if ((iGregTechTileEntity instanceof IPipeRenderedTileEntity renderedPipe)
            && (tMetaTileEntity instanceof MetaPipeEntity pipeEntity)) {
            final float tThickness = renderedPipe.getThickNess();
            final float pipeMin = (blockMax - tThickness) / 2.0F;
            final float pipeMax = blockMax - pipeMin;

            ctx.block.setBlockBounds(blockMin, pipeMin, pipeMin, blockMax, pipeMax, pipeMax);
            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
            renderNegativeYFacing(ctx, pipeEntity.getTexture(iGregTechTileEntity, DOWN, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderPositiveYFacing(ctx, pipeEntity.getTexture(iGregTechTileEntity, UP, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderNegativeZFacing(ctx, pipeEntity.getTexture(iGregTechTileEntity, NORTH, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderPositiveZFacing(ctx, pipeEntity.getTexture(iGregTechTileEntity, SOUTH, (CONNECTED_WEST | CONNECTED_EAST), -1, false, false), true);
            renderNegativeXFacing(ctx, pipeEntity.getTexture(iGregTechTileEntity, WEST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false), true);
            renderPositiveXFacing(ctx, pipeEntity.getTexture(iGregTechTileEntity, EAST, (CONNECTED_WEST | CONNECTED_EAST), -1, true, false), true);
        } else {
            renderNegativeYFacing(ctx, tMetaTileEntity.getTexture(iGregTechTileEntity, DOWN, WEST, -1, true, false), true);
            renderPositiveYFacing(ctx, tMetaTileEntity.getTexture(iGregTechTileEntity, UP, WEST, -1, true, false), true);
            renderNegativeZFacing(ctx, tMetaTileEntity.getTexture(iGregTechTileEntity, NORTH, WEST, -1, true, false), true);
            renderPositiveZFacing(ctx, tMetaTileEntity.getTexture(iGregTechTileEntity, SOUTH, WEST, -1, true, false), true);
            renderNegativeXFacing(ctx, tMetaTileEntity.getTexture(iGregTechTileEntity, WEST, WEST, -1, true, false), true);
            renderPositiveXFacing(ctx, tMetaTileEntity.getTexture(iGregTechTileEntity, EAST, WEST, -1, true, false), true);
        }
        // spotless:on
    }

    public static void renderNegativeYFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y - 1, ctx.z, 0)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, aFullBlock ? ctx.y - 1 : ctx.y, ctx.z));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderYNeg(ctx);
            }
        }
    }

    public static void renderPositiveYFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y + 1, ctx.z, 1)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, aFullBlock ? ctx.y + 1 : ctx.y, ctx.z));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderYPos(ctx);
            }
        }
    }

    public static void renderNegativeZFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y, ctx.z - 1, 2)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, ctx.y, aFullBlock ? ctx.z - 1 : ctx.z));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderZNeg(ctx);
            }
        }
    }

    public static void renderPositiveZFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y, ctx.z + 1, 3)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, ctx.y, aFullBlock ? ctx.z + 1 : ctx.z));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderZPos(ctx);
            }
        }
    }

    public static void renderNegativeXFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x - 1, ctx.y, ctx.z, 4)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, aFullBlock ? ctx.x - 1 : ctx.x, ctx.y, ctx.z));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderXNeg(ctx);
            }
        }
    }

    public static void renderPositiveXFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x + 1, ctx.y, ctx.z, 5)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, aFullBlock ? ctx.x + 1 : ctx.x, ctx.y, ctx.z));
        }
        if (aIcon == null) return;
        for (final ITexture iTexture : aIcon) {
            if (iTexture != null) {
                iTexture.renderXPos(ctx);
            }
        }
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        final SBRContext ctx = new SBRContext(aX, aY, aZ, aBlock, aModelID, aRenderer);
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GTMod.proxy.mRenderTileAmbientOcclusion;
        aRenderer.useInventoryTint = false;

        final TileEntity tileEntity = aWorld.getTileEntity(ctx.x, ctx.y, ctx.z);
        final TesselatorAccessor tessAccess = (TesselatorAccessor) Tessellator.instance;

        // If this block does not have a TE, render it as a normal block.
        // Otherwise, render the TE instead.
        if (tileEntity == null && ctx.block instanceof BlockFrameBox frameBlock) {
            int meta = aWorld.getBlockMetadata(ctx.x, ctx.y, ctx.z);
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

        if (ctx.block instanceof IBlockWithTextures texturedBlock) {
            int meta = aWorld.getBlockMetadata(ctx.x, ctx.y, ctx.z);
            ITexture[][] texture = texturedBlock.getTextures(meta);
            if (texture == null) return false;
            renderStandardBlock(ctx);
            return tessAccess.gt5u$hasVertices();
        }

        if (tileEntity == null) return false;

        if (tileEntity instanceof IGregTechTileEntity) {
            final IMetaTileEntity metaTileEntity;
            if ((metaTileEntity = ((IGregTechTileEntity) tileEntity).getMetaTileEntity()) != null
                && metaTileEntity.renderInWorld(aWorld, ctx.x, ctx.y, ctx.z, ctx.block, aRenderer)) {
                aRenderer.enableAO = false;
                return tessAccess.gt5u$hasVertices();
            }
        }
        if (tileEntity instanceof IPipeRenderedTileEntity
            && renderPipeBlock(ctx, (IPipeRenderedTileEntity) tileEntity)) {
            aRenderer.enableAO = false;
            return tessAccess.gt5u$hasVertices();
        }
        if (renderStandardBlock(ctx)) {
            aRenderer.enableAO = false;
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
        return mRenderID;
    }
}
