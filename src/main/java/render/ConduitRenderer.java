package render;

import common.tileentities.TE_ItemProxyCable;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class ConduitRenderer implements ISimpleBlockRenderingHandler {
	
	public static final int RID = RenderingRegistry.getNextAvailableRenderId();
	private static final ConduitRenderer instance = new ConduitRenderer();

	private ConduitRenderer() {
		
	}
	
	public static ConduitRenderer getInstance() {
		return instance;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator tessellator = Tessellator.instance;
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TE_ItemProxyCable) {
			final TE_ItemProxyCable cable = (TE_ItemProxyCable) te;
			
			final float thickness = TE_ItemProxyCable.getThickness();
			final float space = (1.0f - thickness) / 2.0f;
			
			if(cable.getConnections() == 63) { // No need to render a center cube if it's hidden anyways
				block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
				renderer.setRenderBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			} else if (cable.getConnections() == 0) { // Only center cube required
				block.setBlockBounds(space, space, space, space + thickness, space + thickness, space + thickness);
				renderer.setRenderBounds(space, space, space, space + thickness, space + thickness, space + thickness);
			}
			
			final Tessellator f = Tessellator.instance;
			f.startDrawingQuads();
			IIcon icon = block.getIcon(0, 0);
			// South face
			if(cable.isConnected(ForgeDirection.SOUTH)) {
				f.addVertexWithUV(x + space + thickness, y + space, z + 1, icon.getMaxU(), icon.getMaxV());
				f.addVertexWithUV(x + space + thickness, y + space + thickness, z + 1, icon.getMaxU(), icon.getMinV());
				f.addVertexWithUV(x + space, y + space + thickness, z + 1, icon.getMinU(), icon.getMinV());
				f.addVertexWithUV(x + space, y + space, z + 1, icon.getMinU(), icon.getMaxV());
			}
			
			f.draw();
		}
		
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ConduitRenderer.RID;
	}

}
