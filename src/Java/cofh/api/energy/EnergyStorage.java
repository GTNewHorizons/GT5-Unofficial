package cofh.api.energy;

import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage
  implements IEnergyStorage
{
  protected int energy;
  protected int capacity;
  protected int maxReceive;
  protected int maxExtract;
  
  public EnergyStorage(int capacity)
  {
    this(capacity, capacity, capacity);
  }
  
  public EnergyStorage(int capacity, int maxTransfer)
  {
    this(capacity, maxTransfer, maxTransfer);
  }
  
  public EnergyStorage(int capacity, int maxReceive, int maxExtract)
  {
    this.capacity = capacity;
    this.maxReceive = maxReceive;
    this.maxExtract = maxExtract;
  }
  
  public EnergyStorage readFromNBT(NBTTagCompound nbt)
  {
    this.energy = nbt.getInteger("Energy");
    if (this.energy > this.capacity) {
      this.energy = this.capacity;
    }
    return this;
  }
  
  public NBTTagCompound writeToNBT(NBTTagCompound nbt)
  {
    if (this.energy < 0) {
      this.energy = 0;
    }
    nbt.setInteger("Energy", this.energy);
    return nbt;
  }
  
  public void setCapacity(int capacity)
  {
    this.capacity = capacity;
    if (this.energy > capacity) {
      this.energy = capacity;
    }
  }
  
  public void setMaxTransfer(int maxTransfer)
  {
    setMaxReceive(maxTransfer);
    setMaxExtract(maxTransfer);
  }
  
  public void setMaxReceive(int maxReceive)
  {
    this.maxReceive = maxReceive;
  }
  
  public void setMaxExtract(int maxExtract)
  {
    this.maxExtract = maxExtract;
  }
  
  public int getMaxReceive()
  {
    return this.maxReceive;
  }
  
  public int getMaxExtract()
  {
    return this.maxExtract;
  }
  
  public void setEnergyStored(int energy)
  {
    this.energy = energy;
    if (this.energy > this.capacity) {
      this.energy = this.capacity;
    } else if (this.energy < 0) {
      this.energy = 0;
    }
  }
  
  public void modifyEnergyStored(int energy)
  {
    this.energy += energy;
    if (this.energy > this.capacity) {
      this.energy = this.capacity;
    } else if (this.energy < 0) {
      this.energy = 0;
    }
  }
  
  public int receiveEnergy(int maxReceive, boolean simulate)
  {
    int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
    if (!simulate) {
      this.energy += energyReceived;
    }
    return energyReceived;
  }
  
  public int extractEnergy(int maxExtract, boolean simulate)
  {
    int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));
    if (!simulate) {
      this.energy -= energyExtracted;
    }
    return energyExtracted;
  }
  
  public int getEnergyStored()
  {
    return this.energy;
  }
  
  public int getMaxEnergyStored()
  {
    return this.capacity;
  }
}
