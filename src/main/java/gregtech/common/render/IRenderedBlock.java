package gregtech.common.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ILayeredTexture;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public interface IRenderedBlock {

    /** @return the Textures to be rendered */
    @SideOnly(Side.CLIENT)
    ITexture[] getTexture(Block aBlock, byte aSide, int aRenderPass, boolean[] aShouldSideBeRendered);

    @SideOnly(Side.CLIENT)
    ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass);

    @SideOnly(Side.CLIENT)
    default ILayeredTexture getTexture(Block block, ForgeDirection sideDirection, boolean isActive, int renderPass) {
        return (ILayeredTexture) TextureFactory
            .of(getTexture(block, (byte) sideDirection.ordinal(), isActive, renderPass));
    }

    /** gets the Amount of Render Passes for this TileEntity or similar Handler. Only gets called once per Rendering. */
    @SideOnly(Side.CLIENT)
    int getRenderPasses(Block aBlock);

    /** if this uses said Render Pass or if it can be skipped entirely. */
    @SideOnly(Side.CLIENT)
    boolean usesRenderPass(int aRenderPass);

    /** sets the Block Size rendered; return false for letting it select the normal Block Bounds. */
    @SideOnly(Side.CLIENT)
    boolean setBlockBounds(Block aBlock, int aRenderPass);

    /** returning true stops all the other Rendering from happening. */
    @SideOnly(Side.CLIENT)
    default boolean renderItem(Block aBlock, RenderBlocks aRenderer) {
        return false;
    }

    /** returning true stops all the other Rendering from happening. */
    @SideOnly(Side.CLIENT)
    default boolean renderBlock(Block aBlock, RenderBlocks aRenderer, IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    /** if this Block lets the TileEntity or a similar Handler do all the Inventory Render work. */
    @SideOnly(Side.CLIENT)
    IRenderedBlock passRenderingToObject(ItemStack aStack);

    /** if this Block lets the TileEntity or a similar Handler do all the World Render work. */
    @SideOnly(Side.CLIENT)
    IRenderedBlock passRenderingToObject(IBlockAccess aWorld, int aX, int aY, int aZ);

    class ErrorRenderer implements IRenderedBlockSideCheck, IRenderedBlock {

        public static final ErrorRenderer INSTANCE = new ErrorRenderer();
        public ITexture[] mErrorTexture = Textures.BlockIcons.ERROR_RENDERING;

        @Override
        public ITexture[] getTexture(Block aBlock, byte aSide, int aRenderPass, boolean[] aShouldSideBeRendered) {
            return mErrorTexture;
        }

        @Override
        public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
            return mErrorTexture;
        }

        @Override
        public int getRenderPasses(Block aBlock) {
            return 1;
        }

        @Override
        public boolean usesRenderPass(int aRenderPass) {
            return true;
        }

        @Override
        public boolean setBlockBounds(Block aBlock, int aRenderPass) {
            aBlock.setBlockBounds(-0.25F, -0.25F, -0.25F, 1.25F, 1.25F, 1.25F);
            return true;
        }

        @Override
        public boolean renderFullBlockSide(Block aBlock, RenderBlocks aRenderer, byte aSide) {
            return true;
        }

        @Override
        public IRenderedBlock passRenderingToObject(ItemStack aStack) {
            return this;
        }

        @Override
        public IRenderedBlock passRenderingToObject(IBlockAccess aWorld, int aX, int aY, int aZ) {
            return this;
        }

        @Override
        public boolean renderBlock(Block aBlock, RenderBlocks aRenderer, IBlockAccess aWorld, int aX, int aY, int aZ) {
            aBlock.setBlockBounds(-0.25F, -0.25F, -0.25F, 1.25F, 1.25F, 1.25F);
            GT_Renderer_Block.renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, mErrorTexture, false);
            GT_Renderer_Block.renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, mErrorTexture, false);
            GT_Renderer_Block.renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, mErrorTexture, false);
            GT_Renderer_Block.renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, mErrorTexture, false);
            GT_Renderer_Block.renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, mErrorTexture, false);
            GT_Renderer_Block.renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, mErrorTexture, false);
            return true;
        }
    }
}
