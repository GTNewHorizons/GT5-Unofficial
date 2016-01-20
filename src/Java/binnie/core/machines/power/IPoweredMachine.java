package binnie.core.machines.power;

import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Optional.Interface;
import ic2.api.energy.tile.IEnergySink;

@Optional.Interface(iface="ic2.api.energy.tile.IEnergySink", modid="IC2")
public abstract interface IPoweredMachine
  extends IEnergySink, IEnergyHandler
{
  public abstract PowerInfo getPowerInfo();
  
  public abstract PowerInterface getInterface();
}
