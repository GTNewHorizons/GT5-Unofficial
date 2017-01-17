package cofh.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnergyHandler
  extends TileEntity
  implements IEnergyHandler
{
  protected EnergyStorage storage = new EnergyStorage(32000);
  
  public void readFromNBT(NBTTagCompound nbt)
  {
    super.readFromNBT(nbt);
    this.storage.readFromNBT(nbt);
  }
  
  public void writeToNBT(NBTTagCompound nbt)
  {
    super.writeToNBT(nbt);
    this.storage.writeToNBT(nbt);
  }
  
  public boolean canConnectEnergy(ForgeDirection from)
  {
    return true;
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    return this.storage.receiveEnergy(maxReceive, simulate);
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
  {
    return this.storage.extractEnergy(maxExtract, simulate);
  }
  
  public int getEnergyStored(ForgeDirection from)
  {
    return this.storage.getEnergyStored();
  }
  
  public int getMaxEnergyStored(ForgeDirection from)
  {
    return this.storage.getMaxEnergyStored();
  }
}
