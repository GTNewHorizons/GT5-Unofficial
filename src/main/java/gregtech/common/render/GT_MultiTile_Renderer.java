package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.GT_Mod;
import gregtech.api.interfaces.ITexture;
import gregtech.api.logic.ModelRenderLogic;
import gregtech.api.logic.interfaces.ModelRenderLogicHost;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;
import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;

public class GT_MultiTile_Renderer implements ISimpleBlockRenderingHandler {

    private final int renderID;
    public static GT_MultiTile_Renderer INSTANCE;

    public GT_MultiTile_Renderer() {
        this.renderID = RenderingRegistry.getNextAvailableRenderId();
        INSTANCE = this;
        RenderingRegistry.registerBlockHandler(this);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof MultiTileEntityBlock mteBlock)) {
            return;
        }

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        MultiTileEntityRegistry registry = mteBlock.getRegistry();
        if (registry == null) return;
        renderer.setRenderBoundsFromBlock(mteBlock);

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            ITexture texture = registry.getCachedTileEntity(metadata)
                .getTexture(side);
            if (texture == null) continue;
            switch (side) {
                case DOWN -> renderYNegative(null, renderer, 0, 0, 0, block, texture, side);
                case UP -> renderYPositive(null, renderer, 0, 0, 0, block, texture, side);
                case WEST -> renderXNegative(null, renderer, 0, 0, 0, block, texture, side);
                case EAST -> renderXPositive(null, renderer, 0, 0, 0, block, texture, side);
                case NORTH -> renderZNegative(null, renderer, 0, 0, 0, block, texture, side);
                case SOUTH -> renderZPositive(null, renderer, 0, 0, 0, block, texture, side);
                default -> {
                    // Do nothing
                }
            }
        }

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity == null) {
            return false;
        }

        renderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion;
        renderer.useInventoryTint = false;

        if (entity instanceof ModelRenderLogicHost modelEntity && modelEntity.shouldRenderModel()) {
            ModelRenderLogic renderLogic = modelEntity.getRenderLogic();
            return true;
        }

        if (!(entity instanceof MultiTileBasicRender)) {
            return false;
        }

        if (entity instanceof MultiBlockPart) {
            IMultiBlockController controller = ((MultiBlockPart) entity).getTarget(false);
            if (controller instanceof ModelRenderLogicHost && ((ModelRenderLogicHost) controller).shouldRenderModel()) {
                return false;
            }
        }

        MultiTileBasicRender renderedEntity = (MultiTileBasicRender) entity;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            ITexture texture = renderedEntity.getTexture(side);
            if (texture == null) continue;
            switch (side) {
                case DOWN -> renderYNegative(world, renderer, x, y, z, block, texture, side);
                case UP -> renderYPositive(world, renderer, x, y, z, block, texture, side);
                case WEST -> renderXNegative(world, renderer, x, y, z, block, texture, side);
                case EAST -> renderXPositive(world, renderer, x, y, z, block, texture, side);
                case NORTH -> renderZNegative(world, renderer, x, y, z, block, texture, side);
                case SOUTH -> renderZPositive(world, renderer, x, y, z, block, texture, side);
                default -> {
                    // Do nothing
                }
            }
        }
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return renderID;
    }

    private static void renderYNegative(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y - 1, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
        }
        texture.renderYNeg(renderer, block, x, y, z);
    }

    private static void renderZNegative(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y, z - 1, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z - 1));
        }
        texture.renderZNeg(renderer, block, x, y, z);
    }

    private static void renderXNegative(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x - 1, y, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x - 1, y, z));
        }
        texture.renderXNeg(renderer, block, x, y, z);
    }

    private static void renderYPositive(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y + 1, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y + 1, z));
        }
        texture.renderYPos(renderer, block, x, y, z);
    }

    private static void renderXPositive(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x + 1, y, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x + 1, y, z));
        }
        texture.renderXPos(renderer, block, x, y, z);
    }

    private static void renderZPositive(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y, z + 1, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z + 1));
        }
        texture.renderZPos(renderer, block, x, y, z);
    }
}
