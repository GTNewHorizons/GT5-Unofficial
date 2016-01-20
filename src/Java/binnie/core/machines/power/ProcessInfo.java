package binnie.core.machines.power;

import forestry.api.core.INBTTagable;
import net.minecraft.nbt.NBTTagCompound;

public class ProcessInfo
  implements INBTTagable
{
  private float currentProgress = 0.0F;
  private int processEnergy = 0;
  private int processTime = 0;
  private float energyPerTick = 0.0F;
  
  public ProcessInfo(IProcess process)
  {
    this.energyPerTick = process.getEnergyPerTick();
    if ((process instanceof IProcessTimed))
    {
      IProcessTimed time = (IProcessTimed)process;
      this.currentProgress = time.getProgress();
      this.processEnergy = time.getProcessEnergy();
      this.processTime = time.getProcessLength();
    }
    else
    {
      this.currentProgress = (process.isInProgress() ? 100.0F : 0.0F);
    }
  }
  
  public ProcessInfo() {}
  
  public float getCurrentProgress()
  {
    return this.currentProgress;
  }
  
  public int getProcessEnergy()
  {
    return this.processEnergy;
  }
  
  public int getProcessTime()
  {
    return this.processTime;
  }
  
  public float getEnergyPerTick()
  {
    return this.energyPerTick;
  }
  
  public void readFromNBT(NBTTagCompound nbttagcompound)
  {
    this.energyPerTick = nbttagcompound.getFloat("ept");
    this.processEnergy = nbttagcompound.getInteger("e");
    this.processTime = nbttagcompound.getInteger("t");
    this.currentProgress = nbttagcompound.getFloat("p");
  }
  
  public void writeToNBT(NBTTagCompound nbttagcompound)
  {
    nbttagcompound.setFloat("ept", this.energyPerTick);
    nbttagcompound.setFloat("p", this.currentProgress);
    nbttagcompound.setInteger("e", this.processEnergy);
    nbttagcompound.setInteger("t", this.processTime);
  }
}
