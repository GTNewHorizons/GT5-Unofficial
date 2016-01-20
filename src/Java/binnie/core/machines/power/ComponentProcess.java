package binnie.core.machines.power;

import binnie.core.machines.IMachine;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ComponentProcess
  extends ComponentProcessIndefinate
  implements IProcessTimed
{
  private float progressAmount = 0.0F;
  
  public ComponentProcess(IMachine machine)
  {
    super(machine, 0.0F);
  }
  
  public float getEnergyPerTick()
  {
    return getProcessEnergy() / getProcessLength();
  }
  
  public float getProgressPerTick()
  {
    return 100.0F / getProcessLength();
  }
  
  protected void onStartTask()
  {
    this.progressAmount += 0.01F;
  }
  
  protected void onCancelTask()
  {
    this.progressAmount = 0.0F;
  }
  
  public void onUpdate()
  {
    super.onUpdate();
    if (this.progressAmount >= 100.0F)
    {
      onFinishTask();
      this.progressAmount = 0.0F;
    }
  }
  
  public void alterProgress(float f)
  {
    this.progressAmount += f;
  }
  
  public void setProgress(float f)
  {
    this.progressAmount = f;
  }
  
  protected void progressTick()
  {
    super.progressTick();
    alterProgress(getProgressPerTick());
  }
  
  public boolean inProgress()
  {
    return this.progressAmount > 0.0F;
  }
  
  public float getProgress()
  {
    return this.progressAmount;
  }
  
  protected void onFinishTask() {}
  
  public void readFromNBT(NBTTagCompound nbt)
  {
    super.readFromNBT(nbt);
    this.progressAmount = nbt.getFloat("progress");
  }
  
  public void writeToNBT(NBTTagCompound nbt)
  {
    super.writeToNBT(nbt);
    nbt.setFloat("progress", this.progressAmount);
  }
  
  public abstract int getProcessLength();
  
  public abstract int getProcessEnergy();
}
