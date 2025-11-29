package gregtech.common.render;

import static gregtech.api.enums.GTValues.SIDE_DOWN;
import static gregtech.api.enums.GTValues.SIDE_EAST;
import static gregtech.api.enums.GTValues.SIDE_NORTH;
import static gregtech.api.enums.GTValues.SIDE_SOUTH;
import static gregtech.api.enums.GTValues.SIDE_UP;
import static gregtech.api.enums.GTValues.SIDE_WEST;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
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
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.RenderOverlay;
import gregtech.api.render.SBRContextHolder;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockMachines;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

@ThreadSafeISBRH(perThread = true)
public class GTRendererBlock implements ISimpleBlockRenderingHandler {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    public static final float BLOCK_MIN = 0.0F;
    public static final float BLOCK_MAX = 1.0F;
    private final ITexture[][] textureArray = new ITexture[6][];
    private final ITexture[] overlayHolder = new ITexture[1];

    protected final SBRContextHolder sbrContextHolder = new SBRContextHolder();

    public boolean renderStandardBlock(ISBRWorldContext ctx) {
        final TileEntity tTileEntity = ctx.getTileEntity();
        final ITexture[][] textureArray = this.textureArray;
        final Block block = ctx.getBlock();
        if (tTileEntity instanceof IAllSidedTexturedTileEntity allSidedTexturedTileEntity) {
            ITexture[] texture = allSidedTexturedTileEntity.getTexture(block);
            textureArray[0] = texture;
            textureArray[1] = texture;
            textureArray[2] = texture;
            textureArray[3] = texture;
            textureArray[4] = texture;
            textureArray[5] = texture;
            return renderStandardBlock(ctx, textureArray);
        }
        if (tTileEntity instanceof ITexturedTileEntity texturedTileEntity) {
            textureArray[0] = texturedTileEntity.getTexture(block, DOWN);
            textureArray[1] = texturedTileEntity.getTexture(block, UP);
            textureArray[2] = texturedTileEntity.getTexture(block, NORTH);
            textureArray[3] = texturedTileEntity.getTexture(block, SOUTH);
            textureArray[4] = texturedTileEntity.getTexture(block, WEST);
            textureArray[5] = texturedTileEntity.getTexture(block, EAST);
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

    private IMetaTileEntity getMTE(Block block, int meta) {
        if (!(block instanceof BlockMachines)) return null;

        return GTUtility.getIndexSafe(GregTechAPI.METATILEENTITIES, meta);
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
        final ISBRInventoryContext ctx = sbrContextHolder.getSBRInventoryContext(aBlock, aMeta, aModelID, aRenderer);
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        IMetaTileEntity imte = getMTE(aBlock, aMeta);

        if (imte != null && !imte.renderInInventory(ctx)) {
            renderNormalInventoryMetaTileEntity(ctx, imte);
        } else if (aBlock instanceof IBlockWithTextures texturedBlock) {
            ITexture[][] texture = texturedBlock.getInventoryTextures(aMeta);
            if (texture != null) {
                aBlock.setBlockBoundsForItemRender();
                aRenderer.setRenderBoundsFromBlock(aBlock);
                ctx.renderNegativeYFacing(texture[ForgeDirection.DOWN.ordinal()]);
                ctx.renderPositiveYFacing(texture[ForgeDirection.UP.ordinal()]);
                ctx.renderNegativeZFacing(texture[ForgeDirection.NORTH.ordinal()]);
                ctx.renderPositiveZFacing(texture[ForgeDirection.SOUTH.ordinal()]);
                ctx.renderNegativeXFacing(texture[ForgeDirection.WEST.ordinal()]);
                ctx.renderPositiveXFacing(texture[ForgeDirection.EAST.ordinal()]);
            }
        }
        aBlock.setBlockBounds(BLOCK_MIN, BLOCK_MIN, BLOCK_MIN, BLOCK_MAX, BLOCK_MAX, BLOCK_MAX);

        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    private static void renderNormalInventoryMetaTileEntity(ISBRInventoryContext ctx, IMetaTileEntity imte) {
        final Block block = ctx.getBlock();
        block.setBlockBoundsForItemRender();
        ctx.setRenderBoundsFromBlock();

        final IGregTechTileEntity igte = imte.getBaseMetaTileEntity();
        ctx.renderNegativeYFacing(imte.getTexture(igte, DOWN, WEST, -1, true, false));
        ctx.renderPositiveYFacing(imte.getTexture(igte, UP, WEST, -1, true, false));
        ctx.renderNegativeZFacing(imte.getTexture(igte, NORTH, WEST, -1, true, false));
        ctx.renderPositiveZFacing(imte.getTexture(igte, SOUTH, WEST, -1, true, false));
        ctx.renderNegativeXFacing(imte.getTexture(igte, WEST, WEST, -1, true, false));
        ctx.renderPositiveXFacing(imte.getTexture(igte, EAST, WEST, -1, true, false));
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
        RenderBlocks aRenderer) {
        final ISBRWorldContext ctx = sbrContextHolder.getSBRWorldContext(aX, aY, aZ, aBlock, aModelID, aRenderer);

        final TileEntity tileEntity = ctx.getTileEntity();
        final TesselatorAccessor tessAccess = (TesselatorAccessor) Tessellator.instance;

        if (aBlock instanceof IBlockWithTextures texturedBlock) {
            final ITexture[][] texture = texturedBlock.getTextures(aWorld, aX, aY, aZ);
            if (texture == null) return false;
            renderStandardBlock(ctx, texture);
            return tessAccess.gt5u$hasVertices();
        }

        if (tileEntity == null) return false;

        if (tileEntity instanceof IGregTechTileEntity) {
            final IMetaTileEntity metaTileEntity;
            if ((metaTileEntity = ((IGregTechTileEntity) tileEntity).getMetaTileEntity()) != null
                && metaTileEntity.renderInWorld(ctx)) {
                return tessAccess.gt5u$hasVertices();
            }
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
