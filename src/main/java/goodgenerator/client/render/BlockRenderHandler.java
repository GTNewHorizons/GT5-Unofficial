package goodgenerator.client.render;

import static gregtech.common.render.GT_Renderer_Block.*;
import static net.minecraftforge.common.util.ForgeDirection.*;
import static net.minecraftforge.common.util.ForgeDirection.EAST;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import goodgenerator.blocks.regularBlock.ITextureBlock;
import gregtech.GT_Mod;

public class BlockRenderHandler implements ISimpleBlockRenderingHandler {

    public static final float blockMin = 0.0F;
    public static final float blockMax = 1.0F;
    public static BlockRenderHandler INSTANCE;
    public final int mRenderID;

    public BlockRenderHandler() {
        this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
        INSTANCE = this;
        RenderingRegistry.registerBlockHandler(this);
    }

    @Override
    public void renderInventoryBlock(Block aBlock, int metadata, int modelId, RenderBlocks aRenderer) {
        aRenderer.enableAO = false;
        aRenderer.useInventoryTint = true;

        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        if (aBlock instanceof ITextureBlock) {
            ITextureBlock tc = (ITextureBlock) aBlock;
            aBlock.setBlockBoundsForItemRender();
            aRenderer.setRenderBoundsFromBlock(aBlock);
            renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, tc.getTexture(aBlock, metadata, DOWN), true);
            renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, tc.getTexture(aBlock, metadata, UP), true);
            renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, tc.getTexture(aBlock, metadata, NORTH), true);
            renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, tc.getTexture(aBlock, metadata, SOUTH), true);
            renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, tc.getTexture(aBlock, metadata, WEST), true);
            renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, tc.getTexture(aBlock, metadata, EAST), true);
        }

        aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
        aRenderer.setRenderBoundsFromBlock(aBlock);

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        aRenderer.useInventoryTint = false;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
            RenderBlocks aRenderer) {
        aRenderer.enableAO = Minecraft.isAmbientOcclusionEnabled() && GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion;
        aRenderer.useInventoryTint = false;
        if (aBlock instanceof ITextureBlock) {
            aBlock.setBlockBounds(blockMin, blockMin, blockMin, blockMax, blockMax, blockMax);
            aRenderer.setRenderBoundsFromBlock(aBlock);
            ITextureBlock tc = (ITextureBlock) aBlock;
            renderNegativeYFacing(
                    aWorld,
                    aRenderer,
                    aBlock,
                    aX,
                    aY,
                    aZ,
                    tc.getTexture(aBlock, DOWN, aWorld, aX, aY, aZ),
                    true);
            renderPositiveYFacing(
                    aWorld,
                    aRenderer,
                    aBlock,
                    aX,
                    aY,
                    aZ,
                    tc.getTexture(aBlock, UP, aWorld, aX, aY, aZ),
                    true);
            renderNegativeZFacing(
                    aWorld,
                    aRenderer,
                    aBlock,
                    aX,
                    aY,
                    aZ,
                    tc.getTexture(aBlock, NORTH, aWorld, aX, aY, aZ),
                    true);
            renderPositiveZFacing(
                    aWorld,
                    aRenderer,
                    aBlock,
                    aX,
                    aY,
                    aZ,
                    tc.getTexture(aBlock, SOUTH, aWorld, aX, aY, aZ),
                    true);
            renderNegativeXFacing(
                    aWorld,
                    aRenderer,
                    aBlock,
                    aX,
                    aY,
                    aZ,
                    tc.getTexture(aBlock, WEST, aWorld, aX, aY, aZ),
                    true);
            renderPositiveXFacing(
                    aWorld,
                    aRenderer,
                    aBlock,
                    aX,
                    aY,
                    aZ,
                    tc.getTexture(aBlock, EAST, aWorld, aX, aY, aZ),
                    true);
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }
}
