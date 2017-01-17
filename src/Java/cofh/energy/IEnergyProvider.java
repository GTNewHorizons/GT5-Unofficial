package cofh.energy;

import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IEnergyProvider
  extends IEnergyConnection
{
  public abstract int extractEnergy(ForgeDirection paramForgeDirection, int paramInt, boolean paramBoolean);
  
  public abstract int getEnergyStored(ForgeDirection paramForgeDirection);
  
  public abstract int getMaxEnergyStored(ForgeDirection paramForgeDirection);
}
