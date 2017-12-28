package gtPlusPlus.core.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gtPlusPlus.api.objects.Logger;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class CustomOreBlockRenderer implements ISimpleBlockRenderingHandler{

	public static CustomOreBlockRenderer INSTANCE;
	public final int mRenderID;

	public CustomOreBlockRenderer() {
		this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
		INSTANCE = this;
		RenderingRegistry.registerBlockHandler(this);
		Logger.INFO("Registered Custom Ore Block Renderer.");
	}

	public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
		Block tTileEntity = aBlock;
		if ((tTileEntity instanceof ITexturedTileEntity)) {
			return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, new ITexture[][]{((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 0), ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 1), ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 2), ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 3), ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 4), ((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 5)});
		}
		return false;
	}

	public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer, ITexture[][] aTextures) {
		aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		aRenderer.setRenderBoundsFromBlock(aBlock);

		renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[0], true);
		renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[1], true);
		renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[2], true);
		renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[3], true);
		renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[4], true);
		renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[5], true);
		return true;
	}

	public static void renderNegativeYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY - 1, aZ, 0))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY - 1 : aY, aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderYNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderYPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderNegativeZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ - 1, 2))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ - 1 : aZ));
		}
		aRenderer.flipTexture = (!aFullBlock);
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderZNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderPositiveZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ + 1, 3))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ + 1 : aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderZPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderNegativeXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX - 1, aY, aZ, 4))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX - 1 : aX, aY, aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderXNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderPositiveXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX + 1, aY, aZ, 5))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX + 1 : aX, aY, aZ));
		}
		aRenderer.flipTexture = (!aFullBlock);
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderXPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
		aBlock.setBlockBoundsForItemRender();
		aRenderer.setRenderBoundsFromBlock(aBlock);
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
		renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedTileEntity) aBlock).getTexture(aBlock, (byte) 0), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
		renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedTileEntity) aBlock).getTexture(aBlock, (byte) 1), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
		renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedTileEntity) aBlock).getTexture(aBlock, (byte) 2), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
		renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedTileEntity) aBlock).getTexture(aBlock, (byte) 3), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
		renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedTileEntity) aBlock).getTexture(aBlock, (byte) 4), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
		renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedTileEntity) aBlock).getTexture(aBlock, (byte) 5), true);
		Tessellator.instance.draw();
		aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		aRenderer.setRenderBoundsFromBlock(aBlock);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID, RenderBlocks aRenderer) {
		return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
	}

	public boolean shouldRender3DInInventory(int aModel) {
		return true;
	}

	public int getRenderId() {
		return this.mRenderID;
	}

}
