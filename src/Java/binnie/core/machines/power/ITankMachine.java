package binnie.core.machines.power;

import binnie.core.machines.inventory.IValidatedTankContainer;
import binnie.core.machines.inventory.TankSlot;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public abstract interface ITankMachine
  extends IFluidHandler, IValidatedTankContainer
{
  public abstract TankInfo[] getTankInfos();
  
  public abstract IFluidTank[] getTanks();
  
  public abstract TankSlot addTank(int paramInt1, String paramString, int paramInt2);
  
  public abstract IFluidTank getTank(int paramInt);
  
  public abstract TankSlot getTankSlot(int paramInt);
}
