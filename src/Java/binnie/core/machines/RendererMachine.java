package binnie.core.machines;

import binnie.Binnie;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class RendererMachine
  extends TileEntitySpecialRenderer
  implements ISimpleBlockRenderingHandler
{
  RenderBlocks blockRenderer;
  
  public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float var8)
  {
    renderTileEntity((TileEntityMachine)entity, x, y, z, var8, this.blockRenderer);
  }
  
  public void renderTileEntity(TileEntityMachine entity, double x, double y, double z, float var8, RenderBlocks renderer)
  {
    if ((entity != null) && (entity.getMachine() != null))
    {
      MachinePackage machinePackage = entity.getMachine().getPackage();
      machinePackage.renderMachine(entity.getMachine(), x, y, z, var8, renderer);
    }
  }
  
  public void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int j)
  {
    TileEntity entity = block.createTileEntity(null, i);
    renderTileEntity((TileEntityMachine)entity, 0.0D, -0.1D, 0.0D, 0.0625F, renderblocks);
  }
  
  public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
  {
    if (modelID == Binnie.Machine.getMachineRenderID()) {
      renderInvBlock(renderer, block, metadata, modelID);
    }
  }
  
  public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
  {
    TileEntityMachine tile = (TileEntityMachine)world.getTileEntity(x, y, z);
    if ((tile != null) && (tile.getMachine() != null) && (tile.getMachine().getPackage() != null) && (tile.getMachine().getPackage().getGroup() != null) && (!tile.getMachine().getPackage().getGroup().customRenderer)) {
      renderTileEntity(tile, x, y, z, 1.0F, renderer);
    }
    return true;
  }
  
  public boolean shouldRender3DInInventory(int i)
  {
    return true;
  }
  
  public int getRenderId()
  {
    return Binnie.Machine.getMachineRenderID();
  }
  
  public void func_147496_a(World par1World)
  {
    this.blockRenderer = new RenderBlocks(par1World);
  }
}
