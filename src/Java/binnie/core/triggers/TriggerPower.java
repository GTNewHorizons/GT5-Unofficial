package binnie.core.triggers;

import binnie.core.machines.Machine;
import binnie.core.machines.power.IPoweredMachine;
import binnie.core.machines.power.PowerInterface;

public class TriggerPower
{
  public static TriggerData powerNone(Object tile)
  {
    return new TriggerData(BinnieTrigger.triggerPowerNone, Boolean.valueOf(getPercentage(tile) < 0.0500000007450581D));
  }
  
  public static TriggerData powerLow(Object tile)
  {
    return new TriggerData(BinnieTrigger.triggerPowerLow, Boolean.valueOf(getPercentage(tile) < 0.3499999940395355D));
  }
  
  public static TriggerData powerMedium(Object tile)
  {
    double p = getPercentage(tile);
    return new TriggerData(BinnieTrigger.triggerPowerMedium, Boolean.valueOf((p >= 0.3499999940395355D) && (p <= 0.6499999761581421D)));
  }
  
  public static TriggerData powerHigh(Object tile)
  {
    double p = getPercentage(tile);
    return new TriggerData(BinnieTrigger.triggerPowerHigh, Boolean.valueOf(getPercentage(tile) > 0.6499999761581421D));
  }
  
  public static TriggerData powerFull(Object tile)
  {
    double p = getPercentage(tile);
    return new TriggerData(BinnieTrigger.triggerPowerFull, Boolean.valueOf(getPercentage(tile) > 0.949999988079071D));
  }
  
  private static double getPercentage(Object tile)
  {
    IPoweredMachine process = (IPoweredMachine)Machine.getInterface(IPoweredMachine.class, tile);
    if (process != null)
    {
      double percentage = process.getInterface().getEnergy() / process.getInterface().getCapacity();
      
      return percentage;
    }
    return 0.0D;
  }
}
