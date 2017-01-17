package cofh.energy;

import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IEnergyHandler
  extends IEnergyProvider, IEnergyReceiver
{
  public abstract int receiveEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);
  
  public abstract int extractEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);
  
  public abstract int getEnergyStored(ForgeDirection paramForgeDirection);
  
  public abstract int getMaxEnergyStored(ForgeDirection paramForgeDirection);
}
