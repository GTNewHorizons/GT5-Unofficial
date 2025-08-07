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
import gregtech.api.render.SBRContext;
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
    private static void renderNormalInventoryMetaTileEntity(SBRContext ctx) {
        if (ctx.meta > 0 && ctx.meta < GregTechAPI.METATILEENTITIES.length) {
            IMetaTileEntity tMetaTileEntity = GregTechAPI.METATILEENTITIES[ctx.meta];
            if (tMetaTileEntity != null) {
                ctx.block.setBlockBoundsForItemRender();
                ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                final Tessellator tess = Tessellator.instance;
                if (tMetaTileEntity.getBaseMetaTileEntity() instanceof IPipeRenderedTileEntity pipeRenderedTile) {
                    float tThickness = pipeRenderedTile.getThickNess();
                    float sp = (1.0F - tThickness) / 2.0F;
                    ctx.block.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    renderNegativeYFacing(ctx, getTexture(tMetaTileEntity, DOWN, 0b001001, -1, false, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    renderPositiveYFacing(ctx, getTexture(tMetaTileEntity, UP, 0b001001, -1, false, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    renderNegativeZFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.NORTH, 0b001001, -1, false, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    renderPositiveZFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.SOUTH, 0b001001, -1, false, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    renderNegativeXFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.WEST, 0b001001, -1, true, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    renderPositiveXFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.EAST, 0b001001, -1, true, false), true);
                    tess.draw();
                } else {
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, -1.0F, 0.0F);
                    renderNegativeYFacing(ctx, getTexture(tMetaTileEntity, DOWN, ForgeDirection.WEST, -1, true, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 1.0F, 0.0F);
                    renderPositiveYFacing(ctx, getTexture(tMetaTileEntity, UP, ForgeDirection.WEST, -1, true, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, -1.0F);
                    renderNegativeZFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.NORTH, ForgeDirection.WEST, -1, true, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(0.0F, 0.0F, 1.0F);
                    renderPositiveZFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.SOUTH, ForgeDirection.WEST, -1, true, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(-1.0F, 0.0F, 0.0F);
                    renderNegativeXFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.WEST, ForgeDirection.WEST, -1, true, false), true);
                    tess.draw();
                    tess.startDrawingQuads();
                    tess.setNormal(1.0F, 0.0F, 0.0F);
                    renderPositiveXFacing(ctx, getTexture(tMetaTileEntity, ForgeDirection.EAST, ForgeDirection.WEST, -1, true, false), true);
                    tess.draw();
                }

                ctx.block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
    }
    // spotless:on

    public boolean renderStandardBlock(SBRContext ctx) {
        final TileEntity te = ctx.world.getTileEntity(ctx.x, ctx.y, ctx.z);
        return te instanceof ITexturedTileEntity && renderStandardBlock(
            ctx,
            new ITexture[][] { GTMethodHelper.getTexture(te, ctx.block, DOWN),
                GTMethodHelper.getTexture(te, ctx.block, UP),
                GTMethodHelper.getTexture(te, ctx.block, ForgeDirection.NORTH),
                GTMethodHelper.getTexture(te, ctx.block, ForgeDirection.SOUTH),
                GTMethodHelper.getTexture(te, ctx.block, ForgeDirection.WEST),
                GTMethodHelper.getTexture(te, ctx.block, ForgeDirection.EAST) });
    }

    public boolean renderStandardBlock(SBRContext ctx, ITexture[][] aTextures) {
        ctx.block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        ctx.renderer.setRenderBoundsFromBlock(ctx.block);
        renderNegativeYFacing(ctx, aTextures[DOWN.ordinal()], true);
        renderPositiveYFacing(ctx, aTextures[UP.ordinal()], true);
        renderNegativeZFacing(ctx, aTextures[ForgeDirection.NORTH.ordinal()], true);
        renderPositiveZFacing(ctx, aTextures[ForgeDirection.SOUTH.ordinal()], true);
        renderNegativeXFacing(ctx, aTextures[ForgeDirection.WEST.ordinal()], true);
        renderPositiveXFacing(ctx, aTextures[ForgeDirection.EAST.ordinal()], true);
        return true;
    }

    // spotless:off
    public boolean renderPipeBlock(SBRContext ctx, IPipeRenderedTileEntity aTileEntity) {
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
                    texture.put(side, GTMethodHelper.getTexture((TileEntity) aTileEntity, ctx.block, side));
                    textureUncovered.put(side, aTileEntity.getTextureUncovered(side));
                }

                switch (connexionSidesBits) {
                    case NO_CONNECTION -> {
                        ctx.block.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                        ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                        renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                        renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                        renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                        renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                        renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                        renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                    }
                    case (CONNECTED_DOWN | CONNECTED_UP) -> {
                        ctx.block.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                        ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                        renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                        renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                        renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                        renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                        if (!coveredSides.contains(WEST)) {
                            renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                        }
                        if (!coveredSides.contains(EAST)) {
                            renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                        }
                    }
                    case (CONNECTED_NORTH | CONNECTED_SOUTH) -> {
                        ctx.block.setBlockBounds(sp, 0.0F, sp, sp + tThickness, 1.0F, sp + tThickness);
                        ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                        renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                        renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                        renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                        renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                        if (!coveredSides.contains(DOWN)) {
                            renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                        }
                        if (!coveredSides.contains(UP)) {
                            renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                        }
                    }
                    case (CONNECTED_WEST | CONNECTED_EAST) -> {
                        ctx.block.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, 1.0F);
                        ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                        renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                        renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                        renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                        renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                        if (!coveredSides.contains(NORTH)) {
                            renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                        }
                        if (!coveredSides.contains(SOUTH)) {
                            renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                        }
                    }
                    default -> {
                        if ((connexionSidesBits & CONNECTED_DOWN) == 0) {
                            ctx.block.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                        } else {
                            ctx.block.setBlockBounds(0.0F, sp, sp, sp, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                            renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                            renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                            renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                            if (!coveredSides.contains(WEST)) {
                                renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_UP) == 0) {
                            ctx.block.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                        } else {
                            ctx.block.setBlockBounds(sp + tThickness, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                            renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                            renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                            renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                            if (!coveredSides.contains(EAST)) {
                                renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_NORTH) == 0) {
                            ctx.block.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                        } else {
                            ctx.block.setBlockBounds(sp, 0.0F, sp, sp + tThickness, sp, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                            renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                            renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                            renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                            if (!coveredSides.contains(DOWN)) {
                                renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_SOUTH) == 0) {
                            ctx.block.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                        } else {
                            ctx.block.setBlockBounds(sp, sp + tThickness, sp, sp + tThickness, 1.0F, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                            renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                            renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                            renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                            if (!coveredSides.contains(UP)) {
                                renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_WEST) == 0) {
                            ctx.block.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                        } else {
                            ctx.block.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, sp);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                            renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                            renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                            renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                            if (!coveredSides.contains(NORTH)) {
                                renderNegativeZFacing(ctx, textureUncovered.get(NORTH), false);
                            }
                        }
                        if ((connexionSidesBits & CONNECTED_EAST) == 0) {
                            ctx.block.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                        } else {
                            ctx.block.setBlockBounds(sp, sp, sp + tThickness, sp + tThickness, sp + tThickness, 1.0F);
                            ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                            renderNegativeYFacing(ctx, textureUncovered.get(DOWN), false);
                            renderPositiveYFacing(ctx, textureUncovered.get(UP), false);
                            renderNegativeXFacing(ctx, textureUncovered.get(WEST), false);
                            renderPositiveXFacing(ctx, textureUncovered.get(EAST), false);
                            if (!coveredSides.contains(SOUTH)) {
                                renderPositiveZFacing(ctx, textureUncovered.get(SOUTH), false);
                            }
                        }
                    }
                }

                if (coveredSides.contains(DOWN)) {
                    ctx.block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeYFacing(ctx, texture.get(DOWN), false);
                    renderPositiveYFacing(ctx, texture.get(DOWN), false);
                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(ctx, texture.get(DOWN), false);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(ctx, texture.get(DOWN), false);
                    }

                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(ctx, texture.get(DOWN), false);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(ctx, texture.get(DOWN), false);
                    }
                }

                if (coveredSides.contains(UP)) {
                    ctx.block.setBlockBounds(0.0F, 0.875F, 0.0F, 1.0F, 1.0F, 1.0F);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    renderNegativeYFacing(ctx, texture.get(UP), false);
                    renderPositiveYFacing(ctx, texture.get(UP), false);
                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(ctx, texture.get(UP), false);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(ctx, texture.get(UP), false);
                    }

                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(ctx, texture.get(UP), false);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(ctx, texture.get(UP), false);
                    }
                }

                if (coveredSides.contains(NORTH)) {
                    ctx.block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(ctx, texture.get(NORTH), false);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(ctx, texture.get(NORTH), false);
                    }

                    renderNegativeZFacing(ctx, texture.get(NORTH), false);
                    renderPositiveZFacing(ctx, texture.get(NORTH), false);
                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(ctx, texture.get(NORTH), false);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(ctx, texture.get(NORTH), false);
                    }
                }

                if (coveredSides.contains(SOUTH)) {
                    ctx.block.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(ctx, texture.get(SOUTH), false);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(ctx, texture.get(SOUTH), false);
                    }

                    renderNegativeZFacing(ctx, texture.get(SOUTH), false);
                    renderPositiveZFacing(ctx, texture.get(SOUTH), false);
                    if (!coveredSides.contains(WEST)) {
                        renderNegativeXFacing(ctx, texture.get(SOUTH), false);
                    }

                    if (!coveredSides.contains(EAST)) {
                        renderPositiveXFacing(ctx, texture.get(SOUTH), false);
                    }
                }

                if (coveredSides.contains(WEST)) {
                    ctx.block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(ctx, texture.get(WEST), false);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(ctx, texture.get(WEST), false);
                    }

                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(ctx, texture.get(WEST), false);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(ctx, texture.get(WEST), false);
                    }

                    renderNegativeXFacing(ctx, texture.get(WEST), false);
                    renderPositiveXFacing(ctx, texture.get(WEST), false);
                }

                if (coveredSides.contains(EAST)) {
                    ctx.block.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                    if (!coveredSides.contains(DOWN)) {
                        renderNegativeYFacing(ctx, texture.get(EAST), false);
                    }

                    if (!coveredSides.contains(UP)) {
                        renderPositiveYFacing(ctx, texture.get(EAST), false);
                    }

                    if (!coveredSides.contains(NORTH)) {
                        renderNegativeZFacing(ctx, texture.get(EAST), false);
                    }

                    if (!coveredSides.contains(SOUTH)) {
                        renderPositiveZFacing(ctx, texture.get(EAST), false);
                    }

                    renderNegativeXFacing(ctx, texture.get(EAST), false);
                    renderPositiveXFacing(ctx, texture.get(EAST), false);
                }

                ctx.block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                ctx.renderer.setRenderBoundsFromBlock(ctx.block);
                return true;
            }
        }
    }
    // spotless:on

    public static void renderNegativeYFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y - 1, ctx.z, 0)) {
                return;
            }
            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, aFullBlock ? ctx.y - 1 : ctx.y, ctx.z));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYNeg(ctx);
                }
            }
        }

        ctx.renderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveYFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y + 1, ctx.z, 1)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, aFullBlock ? ctx.y + 1 : ctx.y, ctx.z));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderYPos(ctx);
                }
            }
        }

        ctx.renderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderNegativeZFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y, ctx.z - 1, 2)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, ctx.y, aFullBlock ? ctx.z - 1 : ctx.z));
        }

        ctx.renderer.flipTexture = !aFullBlock;
        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZNeg(ctx);
                }
            }
        }

        ctx.renderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveZFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x, ctx.y, ctx.z + 1, 3)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, ctx.x, ctx.y, aFullBlock ? ctx.z + 1 : ctx.z));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderZPos(ctx);
                }
            }
        }

        ctx.renderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderNegativeXFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x - 1, ctx.y, ctx.z, 4)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, aFullBlock ? ctx.x - 1 : ctx.x, ctx.y, ctx.z));
        }

        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderXNeg(ctx);
                }
            }
        }

        ctx.renderer.flipTexture = false;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    public static void renderPositiveXFacing(SBRContext ctx, ITexture[] aIcon, boolean aFullBlock) {
        if (ctx.world != null) {
            if (aFullBlock && !ctx.renderer.renderAllFaces
                && !ctx.block.shouldSideBeRendered(ctx.world, ctx.x + 1, ctx.y, ctx.z, 5)) {
                return;
            }

            Tessellator.instance.setBrightness(
                ctx.block.getMixedBrightnessForBlock(ctx.world, aFullBlock ? ctx.x + 1 : ctx.x, ctx.y, ctx.z));
        }

        ctx.renderer.flipTexture = !aFullBlock;
        if (aIcon != null) {
            for (ITexture iTexture : aIcon) {
                if (iTexture != null) {
                    iTexture.renderXPos(ctx);
                }
            }
        }

        ctx.renderer.flipTexture = false;
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        final SBRContext ctx = new SBRContext(aBlock, aMeta, aModelID, aRenderer);
        aMeta += 30400;
        if (aBlock instanceof BlockMachines) {
            if (aMeta > 0 && aMeta < GregTechAPI.METATILEENTITIES.length
                && GregTechAPI.METATILEENTITIES[aMeta] != null
                && !GregTechAPI.METATILEENTITIES[aMeta].renderInInventory(aBlock, aMeta, aRenderer)) {
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
        final SBRContext ctx = new SBRContext(aX, aY, aZ, aBlock, aModelID, aRenderer);

        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        return aTileEntity != null && (aTileEntity instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity() != null
            && tessAccess.gt5u$hasVertices()
            && ((IGregTechTileEntity) aTileEntity).getMetaTileEntity()
                .renderInWorld(aWorld, aX, aY, aZ, aBlock, aRenderer)
            || (aTileEntity instanceof IPipeRenderedTileEntity
                ? renderPipeBlock(ctx, (IPipeRenderedTileEntity) aTileEntity)
                : renderStandardBlock(ctx)));
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }
}
