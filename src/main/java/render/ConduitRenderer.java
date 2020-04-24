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

public class ConduitRenderer implements ISimpleBlockRenderingHandler {
	
	private static final ConduitRenderer instance = new ConduitRenderer();
	private final int renderID = RenderingRegistry.getNextAvailableRenderId();
	
	private ConduitRenderer() {
		
	}
	
	public static ConduitRenderer getInstance() {
		return instance;
	}
	
	public void registerRenderer() {
		RenderingRegistry.registerBlockHandler(this);
	}
	
	@Override
	public int getRenderId() {
		return renderID;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		System.out.println("custom renderer");
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
				System.out.println("tesselating");
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
		return false;
	}
	
}
