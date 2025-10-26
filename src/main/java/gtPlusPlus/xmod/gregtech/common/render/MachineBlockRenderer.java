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
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.render.GTRendererBlock;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;
import gtPlusPlus.xmod.gregtech.common.helpers.GTMethodHelper;

public class MachineBlockRenderer extends GTRendererBlock {

    private static final int mRenderID = RenderingRegistry.getNextAvailableRenderId();

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
    private static void renderNormalInventoryMetaTileEntity(ISBRInventoryContext ctx) {
        if (ctx.getMeta() > 0 && ctx.getMeta() < GregTechAPI.METATILEENTITIES.length) {
            IMetaTileEntity tMetaTileEntity = GregTechAPI.METATILEENTITIES[ctx.getMeta()];
            if (tMetaTileEntity != null) {
                ctx.getBlock().setBlockBoundsForItemRender();
                ctx.setRenderBoundsFromBlock();
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                final Tessellator tess = Tessellator.instance;
                if (tMetaTileEntity.getBaseMetaTileEntity() instanceof IPipeRenderedTileEntity pipeRenderedTile) {
                    float tThickness = pipeRenderedTile.getThickNess();
                    float sp = (1.0F - tThickness) / 2.0F;
                    ctx.getBlock().setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                    ctx.setRenderBoundsFromBlock();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    ctx.renderNegativeYFacing(getTexture(tMetaTileEntity, DOWN, 0b001001, -1, false, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    ctx.renderPositiveYFacing(getTexture(tMetaTileEntity, UP, 0b001001, -1, false, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    ctx.renderNegativeZFacing(getTexture(tMetaTileEntity, ForgeDirection.NORTH, 0b001001, -1, false, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    ctx.renderPositiveZFacing(getTexture(tMetaTileEntity, ForgeDirection.SOUTH, 0b001001, -1, false, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    ctx.renderNegativeXFacing(getTexture(tMetaTileEntity, ForgeDirection.WEST, 0b001001, -1, true, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    ctx.renderPositiveXFacing(getTexture(tMetaTileEntity, ForgeDirection.EAST, 0b001001, -1, true, false));
                    tess.draw();
                } else {
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    ctx.renderNegativeYFacing(getTexture(tMetaTileEntity, DOWN, ForgeDirection.WEST, -1, true, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    ctx.renderPositiveYFacing(getTexture(tMetaTileEntity, UP, ForgeDirection.WEST, -1, true, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    ctx.renderNegativeZFacing(getTexture(tMetaTileEntity, ForgeDirection.NORTH, ForgeDirection.WEST, -1, true, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    ctx.renderPositiveZFacing(getTexture(tMetaTileEntity, ForgeDirection.SOUTH, ForgeDirection.WEST, -1, true, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    ctx.renderNegativeXFacing(getTexture(tMetaTileEntity, ForgeDirection.WEST, ForgeDirection.WEST, -1, true, false));
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    ctx.renderPositiveXFacing(getTexture(tMetaTileEntity, ForgeDirection.EAST, ForgeDirection.WEST, -1, true, false));
                    tess.draw();
                }

                ctx.getBlock().setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                ctx.setRenderBoundsFromBlock();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
    }
    // spotless:on

    public boolean renderStandardBlock(ISBRWorldContext ctx) {
        final TileEntity te = ctx.getBlockAccess()
            .getTileEntity(ctx.getX(), ctx.getY(), ctx.getZ());
        return te instanceof ITexturedTileEntity && renderStandardBlock(
            ctx,
            new ITexture[][] { GTMethodHelper.getTexture(te, ctx.getBlock(), DOWN),
                GTMethodHelper.getTexture(te, ctx.getBlock(), UP),
                GTMethodHelper.getTexture(te, ctx.getBlock(), ForgeDirection.NORTH),
                GTMethodHelper.getTexture(te, ctx.getBlock(), ForgeDirection.SOUTH),
                GTMethodHelper.getTexture(te, ctx.getBlock(), ForgeDirection.WEST),
                GTMethodHelper.getTexture(te, ctx.getBlock(), ForgeDirection.EAST) });
    }

    public boolean renderStandardBlock(ISBRWorldContext ctx, ITexture[][] aTextures) {
        ctx.getBlock()
            .setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        ctx.setRenderBoundsFromBlock();
        ctx.renderNegativeYFacing(aTextures[DOWN.ordinal()]);
        ctx.renderPositiveYFacing(aTextures[UP.ordinal()]);
        ctx.renderNegativeZFacing(aTextures[ForgeDirection.NORTH.ordinal()]);
        ctx.renderPositiveZFacing(aTextures[ForgeDirection.SOUTH.ordinal()]);
        ctx.renderNegativeXFacing(aTextures[ForgeDirection.WEST.ordinal()]);
        ctx.renderPositiveXFacing(aTextures[ForgeDirection.EAST.ordinal()]);
        return true;
    }

    // spotless:off
    public boolean renderPipeBlock(ISBRWorldContext ctx, IPipeRenderedTileEntity aTileEntity) {
        final int aConnections = aTileEntity.getConnections();
        float tThickness = aTileEntity.getThickNess();
        if (tThickness >= 0.99F) {
            return renderStandardBlock(ctx);
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
                return renderStandardBlock(ctx);
            } else {
                final EnumMap<ForgeDirection, ITexture[]> texture = new EnumMap<>(ForgeDirection.class);
                final EnumMap<ForgeDirection, ITexture[]> textureUncovered = new EnumMap<>(ForgeDirection.class);

                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    texture.put(side, GTMethodHelper.getTexture((TileEntity) aTileEntity, ctx.getBlock(), side));
                    textureUncovered.put(side, aTileEntity.getTextureUncovered(side));
                }

                switch (connexionSidesBits) {
                    case NO_CONNECTION -> {
                        ctx.getBlock().setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                        ctx.setRenderBoundsFromBlock();
                        ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                        ctx.renderPositiveYFacing(textureUncovered.get(UP));
                        ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                        ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                        ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                        ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                    }
                    case (CONNECTED_DOWN | CONNECTED_UP) -> {
                        ctx.getBlock().setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                        ctx.setRenderBoundsFromBlock();
                        ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                        ctx.renderPositiveYFacing(textureUncovered.get(UP));
                        ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                        ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                        if (!coveredSides.contains(WEST)) {
                            ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                        }
                        if (!coveredSides.contains(EAST)) {
                            ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                        }
                    }
                    case (CONNECTED_NORTH | CONNECTED_SOUTH) -> {
                        ctx.getBlock().setBlockBounds(sp, 0.0F, sp, sp + tThickness, 1.0F, sp + tThickness);
                        ctx.setRenderBoundsFromBlock();
                        ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                        ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                        ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                        ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                        if (!coveredSides.contains(DOWN)) {
                            ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                        }
                        if (!coveredSides.contains(UP)) {
                            ctx.renderPositiveYFacing(textureUncovered.get(UP));
                        }
                    }
                    case (CONNECTED_WEST | CONNECTED_EAST) -> {
                        ctx.getBlock().setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, 1.0F);
                        ctx.setRenderBoundsFromBlock();
                        ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                        ctx.renderPositiveYFacing(textureUncovered.get(UP));
                        ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                        ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                        if (!coveredSides.contains(NORTH)) {
                            ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                        }
                        if (!coveredSides.contains(SOUTH)) {
                            ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                        }
                    }
                    default -> {
                        if ((connexionSidesBits & CONNECTED_DOWN) == 0) {
                            ctx.getBlock().setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                        } else {
                            ctx.getBlock().setBlockBounds(0.0F, sp, sp, sp, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                            ctx.renderPositiveYFacing(textureUncovered.get(UP));
                            ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                            ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                            if (!coveredSides.contains(WEST)) {
                                ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_UP) == 0) {
                            ctx.getBlock().setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                        } else {
                            ctx.getBlock().setBlockBounds(sp + tThickness, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                            ctx.renderPositiveYFacing(textureUncovered.get(UP));
                            ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                            ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                            if (!coveredSides.contains(EAST)) {
                                ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_NORTH) == 0) {
                            ctx.getBlock().setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                        } else {
                            ctx.getBlock().setBlockBounds(sp, 0.0F, sp, sp + tThickness, sp, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                            ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                            ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                            ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                            if (!coveredSides.contains(DOWN)) {
                                ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_SOUTH) == 0) {
                            ctx.getBlock().setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderPositiveYFacing(textureUncovered.get(UP));
                        } else {
                            ctx.getBlock().setBlockBounds(sp, sp + tThickness, sp, sp + tThickness, 1.0F, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                            ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                            ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                            ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                            if (!coveredSides.contains(UP)) {
                                ctx.renderPositiveYFacing(textureUncovered.get(UP));
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_WEST) == 0) {
                            ctx.getBlock().setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                        } else {
                            ctx.getBlock().setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, sp);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                            ctx.renderPositiveYFacing(textureUncovered.get(UP));
                            ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                            ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                            if (!coveredSides.contains(NORTH)) {
                                ctx.renderNegativeZFacing(textureUncovered.get(NORTH));
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_EAST) == 0) {
                            ctx.getBlock().setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                        } else {
                            ctx.getBlock().setBlockBounds(sp, sp, sp + tThickness, sp + tThickness, sp + tThickness, 1.0F);
                            ctx.setRenderBoundsFromBlock();
                            ctx.renderNegativeYFacing(textureUncovered.get(DOWN));
                            ctx.renderPositiveYFacing(textureUncovered.get(UP));
                            ctx.renderNegativeXFacing(textureUncovered.get(WEST));
                            ctx.renderPositiveXFacing(textureUncovered.get(EAST));
                            if (!coveredSides.contains(SOUTH)) {
                                ctx.renderPositiveZFacing(textureUncovered.get(SOUTH));
                            }
                        }
                    }
                }

                if (coveredSides.contains(DOWN)) {
                    ctx.getBlock().setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeYFacing(texture.get(DOWN));
                    ctx.renderPositiveYFacing(texture.get(DOWN));
                    if (!coveredSides.contains(NORTH)) {
                        ctx.renderNegativeZFacing(texture.get(DOWN));
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        ctx.renderPositiveZFacing(texture.get(DOWN));
                    }

                    if (!coveredSides.contains(WEST)) {
                        ctx.renderNegativeXFacing(texture.get(DOWN));
                    }

                    if (!coveredSides.contains(EAST)) {
                        ctx.renderPositiveXFacing(texture.get(DOWN));
                    }
                }

                if (coveredSides.contains(UP)) {
                    ctx.getBlock().setBlockBounds(0.0F, 0.875F, 0.0F, 1.0F, 1.0F, 1.0F);
                    ctx.setRenderBoundsFromBlock();
                    ctx.renderNegativeYFacing(texture.get(UP));
                    ctx.renderPositiveYFacing(texture.get(UP));
                    if (!coveredSides.contains(NORTH)) {
                        ctx.renderNegativeZFacing(texture.get(UP));
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        ctx.renderPositiveZFacing(texture.get(UP));
                    }

                    if (!coveredSides.contains(WEST)) {
                        ctx.renderNegativeXFacing(texture.get(UP));
                    }

                    if (!coveredSides.contains(EAST)) {
                        ctx.renderPositiveXFacing(texture.get(UP));
                    }
                }

                if (coveredSides.contains(NORTH)) {
                    ctx.getBlock().setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
                    ctx.setRenderBoundsFromBlock();
                    if (!coveredSides.contains(DOWN)) {
                        ctx.renderNegativeYFacing(texture.get(NORTH));
                    }

                    if (!coveredSides.contains(UP)) {
                        ctx.renderPositiveYFacing(texture.get(NORTH));
                    }

                    ctx.renderNegativeZFacing(texture.get(NORTH));
                    ctx.renderPositiveZFacing(texture.get(NORTH));
                    if (!coveredSides.contains(WEST)) {
                        ctx.renderNegativeXFacing(texture.get(NORTH));
                    }

                    if (!coveredSides.contains(EAST)) {
                        ctx.renderPositiveXFacing(texture.get(NORTH));
                    }
                }

                if (coveredSides.contains(SOUTH)) {
                    ctx.getBlock().setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
                    ctx.setRenderBoundsFromBlock();
                    if (!coveredSides.contains(DOWN)) {
                        ctx.renderNegativeYFacing(texture.get(SOUTH));
                    }

                    if (!coveredSides.contains(UP)) {
                        ctx.renderPositiveYFacing(texture.get(SOUTH));
                    }

                    ctx.renderNegativeZFacing(texture.get(SOUTH));
                    ctx.renderPositiveZFacing(texture.get(SOUTH));
                    if (!coveredSides.contains(WEST)) {
                        ctx.renderNegativeXFacing(texture.get(SOUTH));
                    }

                    if (!coveredSides.contains(EAST)) {
                        ctx.renderPositiveXFacing(texture.get(SOUTH));
                    }
                }

                if (coveredSides.contains(WEST)) {
                    ctx.getBlock().setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
                    ctx.setRenderBoundsFromBlock();
                    if (!coveredSides.contains(DOWN)) {
                        ctx.renderNegativeYFacing(texture.get(WEST));
                    }

                    if (!coveredSides.contains(UP)) {
                        ctx.renderPositiveYFacing(texture.get(WEST));
                    }

                    if (!coveredSides.contains(NORTH)) {
                        ctx.renderNegativeZFacing(texture.get(WEST));
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        ctx.renderPositiveZFacing(texture.get(WEST));
                    }

                    ctx.renderNegativeXFacing(texture.get(WEST));
                    ctx.renderPositiveXFacing(texture.get(WEST));
                }

                if (coveredSides.contains(EAST)) {
                    ctx.getBlock().setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    ctx.setRenderBoundsFromBlock();
                    if (!coveredSides.contains(DOWN)) {
                        ctx.renderNegativeYFacing(texture.get(EAST));
                    }

                    if (!coveredSides.contains(UP)) {
                        ctx.renderPositiveYFacing(texture.get(EAST));
                    }

                    if (!coveredSides.contains(NORTH)) {
                        ctx.renderNegativeZFacing(texture.get(EAST));
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        ctx.renderPositiveZFacing(texture.get(EAST));
                    }

                    ctx.renderNegativeXFacing(texture.get(EAST));
                    ctx.renderPositiveXFacing(texture.get(EAST));
                }

                ctx.getBlock().setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                ctx.setRenderBoundsFromBlock();
                return true;
            }
        }
    }
    // spotless:on

    public static void renderNegativeYFacing(ISBRWorldContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.getBlockAccess() != null) {
            if (aFullBlock && !ctx.getRenderBlocks().renderAllFaces
                && !ctx.getBlock()
                    .shouldSideBeRendered(ctx.getBlockAccess(), ctx.getX(), ctx.getY() - 1, ctx.getZ(), 0)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.getBlock()
                    .getMixedBrightnessForBlock(
                        ctx.getBlockAccess(),
                        ctx.getX(),
                        aFullBlock ? ctx.getY() - 1 : ctx.getY(),
                        ctx.getZ()));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYNeg(ctx);
                }
            }
        }

        ctx.getRenderBlocks().flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveYFacing(ISBRWorldContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.getBlockAccess() != null) {
            if (aFullBlock && !ctx.getRenderBlocks().renderAllFaces
                && !ctx.getBlock()
                    .shouldSideBeRendered(ctx.getBlockAccess(), ctx.getX(), ctx.getY() + 1, ctx.getZ(), 1)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.getBlock()
                    .getMixedBrightnessForBlock(
                        ctx.getBlockAccess(),
                        ctx.getX(),
                        aFullBlock ? ctx.getY() + 1 : ctx.getY(),
                        ctx.getZ()));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYPos(ctx);
                }
            }
        }

        ctx.getRenderBlocks().flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderNegativeZFacing(ISBRWorldContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.getBlockAccess() != null) {
            if (aFullBlock && !ctx.getRenderBlocks().renderAllFaces
                && !ctx.getBlock()
                    .shouldSideBeRendered(ctx.getBlockAccess(), ctx.getX(), ctx.getY(), ctx.getZ() - 1, 2)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.getBlock()
                    .getMixedBrightnessForBlock(
                        ctx.getBlockAccess(),
                        ctx.getX(),
                        ctx.getY(),
                        aFullBlock ? ctx.getZ() - 1 : ctx.getZ()));
        }

        ctx.getRenderBlocks().flipTexture = !aFullBlock;
        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZNeg(ctx);
                }
            }
        }

        ctx.getRenderBlocks().flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveZFacing(ISBRWorldContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.getBlockAccess() != null) {
            if (aFullBlock && !ctx.getRenderBlocks().renderAllFaces
                && !ctx.getBlock()
                    .shouldSideBeRendered(ctx.getBlockAccess(), ctx.getX(), ctx.getY(), ctx.getZ() + 1, 3)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.getBlock()
                    .getMixedBrightnessForBlock(
                        ctx.getBlockAccess(),
                        ctx.getX(),
                        ctx.getY(),
                        aFullBlock ? ctx.getZ() + 1 : ctx.getZ()));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZPos(ctx);
                }
            }
        }

        ctx.getRenderBlocks().flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderNegativeXFacing(ISBRWorldContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.getBlockAccess() != null) {
            if (aFullBlock && !ctx.getRenderBlocks().renderAllFaces
                && !ctx.getBlock()
                    .shouldSideBeRendered(ctx.getBlockAccess(), ctx.getX() - 1, ctx.getY(), ctx.getZ(), 4)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.getBlock()
                    .getMixedBrightnessForBlock(
                        ctx.getBlockAccess(),
                        aFullBlock ? ctx.getX() - 1 : ctx.getX(),
                        ctx.getY(),
                        ctx.getZ()));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderXNeg(ctx);
                }
            }
        }

        ctx.getRenderBlocks().flipTexture = false;
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        final ISBRInventoryContext ctx = sbrContextHolder.getSBRInventoryContext(aBlock, aMeta, aModelID, aRenderer);
        aMeta += 30400;
        if (aBlock instanceof BlockMachines) {
            if (aMeta > 0 && aMeta < GregTechAPI.METATILEENTITIES.length
                && GregTechAPI.METATILEENTITIES[aMeta] != null
                && !GregTechAPI.METATILEENTITIES[aMeta].renderInInventory(ctx)) {
                renderNormalInventoryMetaTileEntity(ctx);
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
        final ISBRWorldContext ctx = sbrContextHolder.getSBRWorldContext(aX, aY, aZ, aBlock, aModelID, aRenderer);

        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity != null && (aTileEntity instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity() != null
            && tessAccess.gt5u$hasVertices()
            && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity()
                .renderInWorld(ctx)
            || (aTileEntity instanceof IPipeRenderedTileEntity
                ? renderPipeBlock(ctx, (IPipeRenderedTileEntity) aTileEntity)
                : renderStandardBlock(ctx)));
    }

    @Override
    public int getRenderId() {
        return mRenderID;
    }
}
