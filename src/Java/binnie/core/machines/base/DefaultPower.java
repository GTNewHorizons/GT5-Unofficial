package binnie.core.machines.base;

import binnie.core.machines.power.IPoweredMachine;
import binnie.core.machines.power.PowerInfo;
import binnie.core.machines.power.PowerInterface;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

class DefaultPower
  implements IPoweredMachine
{
  public PowerInfo getPowerInfo()
  {
    return new PowerInfo(this, 0.0F);
  }
  
  @Optional.Method(modid="IC2")
  public double getDemandedEnergy()
  {
    return 0.0D;
  }
  
  @Optional.Method(modid="IC2")
  public int getSinkTier()
  {
    return 0;
  }
  
  @Optional.Method(modid="IC2")
  public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
  {
    return 0.0D;
  }
  
  @Optional.Method(modid="IC2")
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
