package binnie.core.multiblock;

import binnie.core.machines.Machine;
import binnie.core.machines.TileEntityMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

class TileEntityMultiblockMachine
  extends TileEntity
{
  private boolean inStructure;
  private int tileX;
  private int tileY;
  private int tileZ;
  
  boolean inStructure()
  {
    return this.inStructure;
  }
  
  public Machine getMachine()
  {
    return getMasterMachine();
  }
  
  private Machine getMasterMachine()
  {
    if (!this.inStructure) {
      return null;
    }
    TileEntity tile = this.worldObj.getTileEntity(this.xCoord + this.tileX, this.yCoord + this.tileY, this.zCoord + this.tileZ);
    if ((tile instanceof TileEntityMachine)) {
      return ((TileEntityMachine)tile).getMachine();
    }
    return null;
  }
}
