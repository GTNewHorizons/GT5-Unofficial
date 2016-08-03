package cofh.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IEnergyConnection
{
  public abstract boolean canConnectEnergy(ForgeDirection paramForgeDirection);
}
