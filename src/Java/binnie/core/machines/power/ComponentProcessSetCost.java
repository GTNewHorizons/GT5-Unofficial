package binnie.core.machines.power;

import binnie.core.machines.IMachine;

public class ComponentProcessSetCost
  extends ComponentProcess
{
  private int processLength;
  private int processEnergy;
  
  public ComponentProcessSetCost(IMachine machine, int rfCost, int timePeriod)
  {
    super(machine);
    this.processLength = timePeriod;
    this.processEnergy = rfCost;
  }
  
  public int getProcessLength()
  {
    return this.processLength;
  }
  
  public int getProcessEnergy()
  {
    return this.processEnergy;
  }
}
