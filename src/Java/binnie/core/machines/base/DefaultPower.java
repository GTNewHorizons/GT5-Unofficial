package binnie.core.machines.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import binnie.core.machines.power.IPoweredMachine;
import binnie.core.machines.power.PowerInfo;
import binnie.core.machines.power.PowerInterface;

class DefaultPower
  implements IPoweredMachine
{
  public PowerInfo getPowerInfo()
  {
    return new PowerInfo(this, 0.0F);
  }
  
 
  public double getDemandedEnergy()
  {
    return 0.0D;
  }
  

  public int getSinkTier()
  {
    return 0;
  }
  

  public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
  {
    return 0.0D;
  }
  

  public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
  {
    return false;
  }
  
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    return 0;
  }
  
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
  {
    return 0;
  }
  
  public int getEnergyStored(ForgeDirection from)
  {
    return 0;
  }
  
  public int getMaxEnergyStored(ForgeDirection from)
  {
    return 0;
  }
  
  public boolean canConnectEnergy(ForgeDirection from)
  {
    return false;
  }
  
  public PowerInterface getInterface()
  {
    return null;
  }
}
