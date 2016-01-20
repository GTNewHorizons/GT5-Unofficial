package binnie.core.machines.inventory;

import net.minecraftforge.fluids.FluidStack;

public abstract interface IValidatedTankContainer
{
  public abstract boolean isTankReadOnly(int paramInt);
  
  public abstract boolean isLiquidValidForTank(FluidStack paramFluidStack, int paramInt);
}
