package binnie.core.machines.power;

abstract interface IProcessTimed
  extends IProcess, IErrorStateSource
{
  public abstract int getProcessLength();
  
  public abstract int getProcessEnergy();
  
  public abstract float getProgress();
  
  public abstract float getProgressPerTick();
}
