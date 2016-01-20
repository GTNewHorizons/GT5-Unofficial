package binnie.core.machines.base;

import binnie.core.machines.inventory.TankSlot;
import binnie.core.machines.power.ITankMachine;
import binnie.core.machines.power.TankInfo;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

class DefaultTankContainer
  implements ITankMachine
{
  public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
  {
    return 0;
  }
  
  public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
  {
    return null;
  }
  
  public TankInfo[] getTankInfos()
  {
    return new TankInfo[0];
  }
  
  public boolean isTankReadOnly(int tank)
  {
    return false;
  }
  
  public boolean isLiquidValidForTank(FluidStack liquid, int tank)
  {
    return false;
  }
  
  public TankSlot addTank(int index, String name, int capacity)
  {
    return null;
  }
  
  public IFluidTank getTank(int index)
  {
    return null;
  }
  
  public TankSlot getTankSlot(int slot)
  {
    return null;
  }
  
  public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
  {
    return null;
  }
  
  public boolean canFill(ForgeDirection from, Fluid fluid)
  {
    return false;
  }
  
  public boolean canDrain(ForgeDirection from, Fluid fluid)
  {
    return false;
  }
  
  public FluidTankInfo[] getTankInfo(ForgeDirection from)
  {
    return new FluidTankInfo[0];
  }
  
  public IFluidTank[] getTanks()
  {
    return new IFluidTank[0];
  }
}
