package client.renderer;

import common.tileentities.TE_ItemProxyCable;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ConduitRenderer implements ISimpleBlockRenderingHandler {
	
	public static final int RID = RenderingRegistry.getNextAvailableRenderId();
	private static final ConduitRenderer INSTANCE = new ConduitRenderer();

	private ConduitRenderer() {
		
	}
	
	public static ConduitRenderer getInstance() {
		return INSTANCE;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof TE_ItemProxyCable) {
			final TE_ItemProxyCable cable = (TE_ItemProxyCable) te;
			
			final float thickness = TE_ItemProxyCable.getThickness();
			final float space = (1.0f - thickness) / 2.0f;

			float xThickness = thickness;
			float xOffset = space;
			float yThickness = thickness;
			float yOffset = space;
			float zThickness = thickness;
			float zOffset = space;

			for(ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				if(cable.isConnected(side)) {
					switch(side) {
						case DOWN:
							yOffset = 0.0F;
							yThickness += space;
							break;
						case UP:
							yThickness += space;
							break;
						case NORTH:
							zOffset = 0.0F;
							zThickness += space;
							break;
						case SOUTH:
							zThickness += space;
							break;
						case WEST:
							xOffset += 0.0F;
							xThickness += space;
							break;
						case EAST:
							xThickness += space;
							break;
					}
				}
			}

			block.setBlockBounds(xOffset, yOffset, zOffset,
					xOffset + xThickness, yOffset + yThickness, zOffset + zThickness);
			renderer.setRenderBoundsFromBlock(block);
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
