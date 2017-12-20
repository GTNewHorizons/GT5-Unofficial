package cofh.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEnergyHandler
  extends TileEntity
  implements IEnergyHandler
{
  protected EnergyStorage storage = new EnergyStorage(32000);
  
  @Override
public void readFromNBT(NBTTagCompound nbt)
  {
    super.readFromNBT(nbt);
    this.storage.readFromNBT(nbt);
  }
  
  @Override
public void writeToNBT(NBTTagCompound nbt)
  {
    super.writeToNBT(nbt);
    this.storage.writeToNBT(nbt);
  }
  
  @Override
public boolean canConnectEnergy(ForgeDirection from)
  {
    return true;
  }
  
  @Override
public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    return this.storage.receiveEnergy(maxReceive, simulate);
  }
  
  @Override
public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
  {
    return this.storage.extractEnergy(maxExtract, simulate);
  }
  
  @Override
public int getEnergyStored(ForgeDirection from)
  {
    return this.storage.getEnergyStored();
  }
  
  @Override
public int getMaxEnergyStored(ForgeDirection from)
  {
    return this.storage.getMaxEnergyStored();
  }
}
