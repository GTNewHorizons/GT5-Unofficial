package binnie.core.machines.power;

public abstract interface IProcess
  extends IErrorStateSource
{
  public abstract float getEnergyPerTick();
  
  public abstract String getTooltip();
  
  public abstract boolean isInProgress();
  
  public abstract ProcessInfo getInfo();
}
