package gtPlusPlus.xmod.gregtech.common.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IPipeRenderedTileEntity;
import gregtech.api.interfaces.tileentity.ITexturedTileEntity;
import gregtech.common.blocks.GT_Block_Machines;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.common.render.GT_Renderer_Block;
import gtPlusPlus.xmod.gregtech.common.blocks.GTPP_Block_Machines;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class GTPP_Render_MachineBlock extends GT_Renderer_Block {
	
	public static GTPP_Render_MachineBlock INSTANCE;
	public final int mRenderID = RenderingRegistry.getNextAvailableRenderId();

	public GTPP_Render_MachineBlock() {
		INSTANCE = this;
		RenderingRegistry.registerBlockHandler(this);
	}
	
	private static ITexture[] getTexture(IMetaTileEntity arg0, int arg1, int arg2, int arg3, boolean arg4, boolean arg5) {
		IGregTechTileEntity arg0b = arg0.getBaseMetaTileEntity();
		return arg0.getTexture(arg0b, (byte) arg1, (byte) arg2, (byte) arg3, arg4, arg5);
	}

	private static void renderNormalInventoryMetaTileEntity(Block aBlock, int aMeta, RenderBlocks aRenderer) {
		if (aMeta > 0 && aMeta < GregTech_API.METATILEENTITIES.length) {
			IMetaTileEntity tMetaTileEntity = GregTech_API.METATILEENTITIES[aMeta];
			if (tMetaTileEntity != null) {
				aBlock.setBlockBoundsForItemRender();
				aRenderer.setRenderBoundsFromBlock(aBlock);
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				if (tMetaTileEntity.getBaseMetaTileEntity() instanceof IPipeRenderedTileEntity) {
					float tThickness = ((IPipeRenderedTileEntity) tMetaTileEntity.getBaseMetaTileEntity())
							.getThickNess();
					float sp = (1.0F - tThickness) / 2.0F;
					aBlock.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
					aRenderer.setRenderBoundsFromBlock(aBlock);
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
					renderNegativeYFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 0, 9, -1, false, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
					renderPositiveYFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 1, 9, -1, false, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
					renderNegativeZFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 2, 9, -1, false, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
					renderPositiveZFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 3, 9, -1, false, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
					renderNegativeXFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 4, 9, -1, true, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
					renderPositiveXFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 5, 9, -1, true, false),
							true);
					Tessellator.instance.draw();
				} else {
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
					renderNegativeYFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 0, 4, -1, true, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
					renderPositiveYFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 1, 4, -1, true, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
					renderNegativeZFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 2, 4, -1, true, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
					renderPositiveZFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 3, 4, -1, true, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
					renderNegativeXFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 4, 4, -1, true, false),
							true);
					Tessellator.instance.draw();
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
					renderPositiveXFacing((IBlockAccess) null, aRenderer, aBlock, 0, 0, 0,
							getTexture(tMetaTileEntity, 5, 4, -1, true, false),
							true);
					Tessellator.instance.draw();
				}

				aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				aRenderer.setRenderBoundsFromBlock(aBlock);
				GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			}
		}
	}

	public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
			RenderBlocks aRenderer) {
		TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return tTileEntity instanceof ITexturedTileEntity
				? renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer,
						new ITexture[][]{((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 0),
								((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 1),
								((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 2),
								((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 3),
								((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 4),
								((ITexturedTileEntity) tTileEntity).getTexture(aBlock, (byte) 5)})
				: false;
	}

	public static boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
			RenderBlocks aRenderer, ITexture[][] aTextures) {
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

	public static boolean renderPipeBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock,
			IPipeRenderedTileEntity aTileEntity, RenderBlocks aRenderer) {
		byte aConnections = aTileEntity.getConnections();
		if ((aConnections & 192) != 0) {
			return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
		} else {
			float tThickness = aTileEntity.getThickNess();
			if (tThickness >= 0.99F) {
				return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
			} else {
				float sp = (1.0F - tThickness) / 2.0F;
				byte tConnections = 0;

				for (byte tIsCovered = 0; tIsCovered < 6; ++tIsCovered) {
					if ((aConnections & 1 << tIsCovered) != 0) {
						tConnections = (byte) (tConnections | 1 << (tIsCovered + 2) % 6);
					}
				}

				boolean[] arg14 = new boolean[6];

				for (byte tIcons = 0; tIcons < 6; ++tIcons) {
					arg14[tIcons] = aTileEntity.getCoverIDAtSide(tIcons) != 0;
				}

				if (arg14[0] && arg14[1] && arg14[2] && arg14[3] && arg14[4] && arg14[5]) {
					return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
				} else {
					ITexture[][] arg15 = new ITexture[6][];
					ITexture[][] tCovers = new ITexture[6][];

					for (byte i = 0; i < 6; ++i) {
						tCovers[i] = aTileEntity.getTexture(aBlock, i);
						arg15[i] = aTileEntity.getTextureUncovered(i);
					}

					if (tConnections == 0) {
						aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
						renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
						renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
						renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
						renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
						renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
					} else if (tConnections == 3) {
						aBlock.setBlockBounds(0.0F, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
						renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
						renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
						renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
						if (!arg14[4]) {
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
						}

						if (!arg14[5]) {
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
						}
					} else if (tConnections == 12) {
						aBlock.setBlockBounds(sp, 0.0F, sp, sp + tThickness, 1.0F, sp + tThickness);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
						renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
						renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
						renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
						if (!arg14[0]) {
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
						}

						if (!arg14[1]) {
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
						}
					} else if (tConnections == 48) {
						aBlock.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, 1.0F);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
						renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
						renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
						renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
						if (!arg14[2]) {
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
						}

						if (!arg14[3]) {
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
						}
					} else {
						if ((tConnections & 1) == 0) {
							aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
						} else {
							aBlock.setBlockBounds(0.0F, sp, sp, sp, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
							if (!arg14[4]) {
								renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
							}
						}

						if ((tConnections & 2) == 0) {
							aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
						} else {
							aBlock.setBlockBounds(sp + tThickness, sp, sp, 1.0F, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
							if (!arg14[5]) {
								renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
							}
						}

						if ((tConnections & 4) == 0) {
							aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
						} else {
							aBlock.setBlockBounds(sp, 0.0F, sp, sp + tThickness, sp, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
							if (!arg14[0]) {
								renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
							}
						}

						if ((tConnections & 8) == 0) {
							aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
						} else {
							aBlock.setBlockBounds(sp, sp + tThickness, sp, sp + tThickness, 1.0F, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
							if (!arg14[1]) {
								renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
							}
						}

						if ((tConnections & 16) == 0) {
							aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
						} else {
							aBlock.setBlockBounds(sp, sp, 0.0F, sp + tThickness, sp + tThickness, sp);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
							if (!arg14[2]) {
								renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[2], false);
							}
						}

						if ((tConnections & 32) == 0) {
							aBlock.setBlockBounds(sp, sp, sp, sp + tThickness, sp + tThickness, sp + tThickness);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
						} else {
							aBlock.setBlockBounds(sp, sp, sp + tThickness, sp + tThickness, sp + tThickness, 1.0F);
							aRenderer.setRenderBoundsFromBlock(aBlock);
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[0], false);
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[1], false);
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[4], false);
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[5], false);
							if (!arg14[3]) {
								renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, arg15[3], false);
							}
						}
					}

					if (arg14[0]) {
						aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[0], false);
						renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[0], false);
						if (!arg14[2]) {
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[0], false);
						}

						if (!arg14[3]) {
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[0], false);
						}

						if (!arg14[4]) {
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[0], false);
						}

						if (!arg14[5]) {
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[0], false);
						}
					}

					if (arg14[1]) {
						aBlock.setBlockBounds(0.0F, 0.875F, 0.0F, 1.0F, 1.0F, 1.0F);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[1], false);
						renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[1], false);
						if (!arg14[2]) {
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[1], false);
						}

						if (!arg14[3]) {
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[1], false);
						}

						if (!arg14[4]) {
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[1], false);
						}

						if (!arg14[5]) {
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[1], false);
						}
					}

					if (arg14[2]) {
						aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						if (!arg14[0]) {
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[2], false);
						}

						if (!arg14[1]) {
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[2], false);
						}

						renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[2], false);
						renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[2], false);
						if (!arg14[4]) {
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[2], false);
						}

						if (!arg14[5]) {
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[2], false);
						}
					}

					if (arg14[3]) {
						aBlock.setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						if (!arg14[0]) {
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[3], false);
						}

						if (!arg14[1]) {
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[3], false);
						}

						renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[3], false);
						renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[3], false);
						if (!arg14[4]) {
							renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[3], false);
						}

						if (!arg14[5]) {
							renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[3], false);
						}
					}

					if (arg14[4]) {
						aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						if (!arg14[0]) {
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[4], false);
						}

						if (!arg14[1]) {
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[4], false);
						}

						if (!arg14[2]) {
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[4], false);
						}

						if (!arg14[3]) {
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[4], false);
						}

						renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[4], false);
						renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[4], false);
					}

					if (arg14[5]) {
						aBlock.setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
						aRenderer.setRenderBoundsFromBlock(aBlock);
						if (!arg14[0]) {
							renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[5], false);
						}

						if (!arg14[1]) {
							renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[5], false);
						}

						if (!arg14[2]) {
							renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[5], false);
						}

						if (!arg14[3]) {
							renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[5], false);
						}

						renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[5], false);
						renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, tCovers[5], false);
					}

					aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
					aRenderer.setRenderBoundsFromBlock(aBlock);
					return true;
				}
			}
		}
	}

	public static void renderNegativeYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
			int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY - 1, aZ, 0)) {
				return;
			}

			Tessellator.instance
					.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY - 1 : aY, aZ));
		}

		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; ++i) {
				if (aIcon[i] != null) {
					aIcon[i].renderYNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}

		aRenderer.flipTexture = false;
	}

	public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
			int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1)) {
				return;
			}

			Tessellator.instance
					.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
		}

		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; ++i) {
				if (aIcon[i] != null) {
					aIcon[i].renderYPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}

		aRenderer.flipTexture = false;
	}

	public static void renderNegativeZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
			int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ - 1, 2)) {
				return;
			}

			Tessellator.instance
					.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ - 1 : aZ));
		}

		aRenderer.flipTexture = !aFullBlock;
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; ++i) {
				if (aIcon[i] != null) {
					aIcon[i].renderZNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}

		aRenderer.flipTexture = false;
	}

	public static void renderPositiveZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
			int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ + 1, 3)) {
				return;
			}

			Tessellator.instance
					.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ + 1 : aZ));
		}

		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; ++i) {
				if (aIcon[i] != null) {
					aIcon[i].renderZPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}

		aRenderer.flipTexture = false;
	}

	public static void renderNegativeXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
			int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX - 1, aY, aZ, 4)) {
				return;
			}

			Tessellator.instance
					.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX - 1 : aX, aY, aZ));
		}

		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; ++i) {
				if (aIcon[i] != null) {
					aIcon[i].renderXNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}

		aRenderer.flipTexture = false;
	}

	public static void renderPositiveXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY,
			int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if (aFullBlock && !aBlock.shouldSideBeRendered(aWorld, aX + 1, aY, aZ, 5)) {
				return;
			}

			Tessellator.instance
					.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX + 1 : aX, aY, aZ));
		}

		aRenderer.flipTexture = !aFullBlock;
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; ++i) {
				if (aIcon[i] != null) {
					aIcon[i].renderXPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}

		aRenderer.flipTexture = false;
	}

	public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
		aMeta += 30400;
		if (aBlock instanceof GT_Block_Machines || aBlock instanceof GTPP_Block_Machines) {
			if (aMeta > 0 && aMeta < GregTech_API.METATILEENTITIES.length
					&& GregTech_API.METATILEENTITIES[aMeta] != null
					&& !GregTech_API.METATILEENTITIES[aMeta].renderInInventory(aBlock, aMeta, aRenderer)) {
				renderNormalInventoryMetaTileEntity(aBlock, aMeta, aRenderer);
			}
		}
		aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		aRenderer.setRenderBoundsFromBlock(aBlock);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID,
			RenderBlocks aRenderer) {
		TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
		return aTileEntity == null
				? false
				: (aTileEntity instanceof IGregTechTileEntity
						&& ((IGregTechTileEntity) aTileEntity).getMetaTileEntity() != null
						&& ((IGregTechTileEntity) aTileEntity).getMetaTileEntity().renderInWorld(aWorld, aX, aY, aZ,
								aBlock, aRenderer)
										? true
										: (aTileEntity instanceof IPipeRenderedTileEntity
												? renderPipeBlock(aWorld, aX, aY, aZ, aBlock,
														(IPipeRenderedTileEntity) aTileEntity, aRenderer)
												: renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer)));
	}

	public boolean shouldRender3DInInventory(int aModel) {
		return true;
	}

	public int getRenderId() {
		return this.mRenderID;
	}
}