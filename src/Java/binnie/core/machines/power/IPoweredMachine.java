package binnie.core.machines.power;

import ic2.api.energy.tile.IEnergySink;
import cofh.api.energy.IEnergyHandler;

public abstract interface IPoweredMachine
  extends IEnergySink, IEnergyHandler
{
  public abstract PowerInfo getPowerInfo();
  
  public abstract PowerInterface getInterface();
}
