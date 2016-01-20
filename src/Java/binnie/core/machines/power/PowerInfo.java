package binnie.core.machines.power;

import forestry.api.core.INBTTagable;
import net.minecraft.nbt.NBTTagCompound;

public class PowerInfo
  implements INBTTagable
{
  private float currentEnergy = 0.0F;
  private float maxEnergy = 0.0F;
  
  public PowerInfo(IPoweredMachine machine, float currentInput)
  {
    this.currentEnergy = ((float)machine.getInterface().getEnergy(PowerSystem.RF));
    this.maxEnergy = ((float)machine.getInterface().getCapacity(PowerSystem.RF));
  }
  
  public PowerInfo() {}
  
  public int getStoredEnergy()
  {
    return (int)this.currentEnergy;
  }
  
  public int getMaxEnergy()
  {
    return (int)this.maxEnergy;
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    this.currentEnergy = nbttagcompound.getInteger("current");
    this.maxEnergy = nbttagcompound.getInteger("max");
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setInteger("current", getStoredEnergy());
    nbttagcompound.setInteger("max", getMaxEnergy());
  }
}
