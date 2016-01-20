package binnie.core.machines.power;

import forestry.api.core.INBTTagable;
import net.minecraft.nbt.NBTTagCompound;

public class PowerInterface
  implements INBTTagable
{
  private int capacity;
  private int energy;
  
  public PowerInterface(int capacity)
  {
    this.capacity = (capacity * 100);
    this.energy = 0;
  }
  
  public int getCapacity()
  {
    return this.capacity;
  }
  
  public int getEnergy()
  {
    return this.energy;
  }
  
  public int addEnergy(int amount, boolean shouldDo)
  {
    int added = Math.min(getEnergySpace(), amount);
    if (shouldDo) {
      this.energy += added;
    }
    return added;
  }
  
  public int useEnergy(int amount, boolean simulate)
  {
    int added = Math.min(getEnergy(), amount);
    if (simulate) {
      this.energy -= added;
    }
    return added;
  }
  
  public int getEnergySpace()
  {
    return getCapacity() - getEnergy();
  }
  
  public double addEnergy(PowerSystem unit, double amount, boolean simulate)
  {
    return unit.convertTo(addEnergy(unit.convertFrom(amount), simulate));
  }
  
  public double useEnergy(PowerSystem unit, double amount, boolean simulate)
  {
    return unit.convertTo(useEnergy(unit.convertFrom(amount), simulate));
  }
  
  public double getEnergy(PowerSystem unit)
  {
    return unit.convertTo(getEnergy());
  }
  
  public double getCapacity(PowerSystem unit)
  {
    return unit.convertTo(getCapacity());
  }
  
  public double getEnergySpace(PowerSystem unit)
  {
    return unit.convertTo(getEnergySpace());
  }
  
  public void readFromNBT(NBTTagCompound nbt)
  {
    this.energy = nbt.getInteger("Energy");
    if (this.energy > this.capacity) {
      this.energy = this.capacity;
    } else if (this.energy < 0) {
      this.energy = 0;
    }
  }
  
  public void writeToNBT(NBTTagCompound nbt)
  {
    nbt.setInteger("Energy", getEnergy());
  }
}
