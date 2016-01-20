package binnie.core.triggers;

import binnie.core.machines.Machine;
import binnie.core.machines.power.IProcess;

public class TriggerWorking
{
  public static TriggerData isNotWorking(Object inventory)
  {
    IProcess process = (IProcess)Machine.getInterface(IProcess.class, inventory);
    boolean b = false;
    if (process != null) {
      b = (process.canWork() != null) && (process.canProgress() != null);
    }
    return new TriggerData(BinnieTrigger.triggerIsNotWorking, Boolean.valueOf(b));
  }
  
  public static TriggerData isWorking(Object inventory)
  {
    IProcess process = (IProcess)Machine.getInterface(IProcess.class, inventory);
    boolean b = false;
    if (process != null) {
      b = (process.canWork() == null) && (process.canProgress() == null);
    }
    return new TriggerData(BinnieTrigger.triggerIsWorking, Boolean.valueOf(b));
  }
  
  public static TriggerData canWork(Object inventory)
  {
    IProcess process = (IProcess)Machine.getInterface(IProcess.class, inventory);
    boolean b = false;
    if (process != null) {
      b = process.canWork() == null;
    }
    return new TriggerData(BinnieTrigger.triggerCanWork, Boolean.valueOf(b));
  }
  
  public static TriggerData cannotWork(Object inventory)
  {
    IProcess process = (IProcess)Machine.getInterface(IProcess.class, inventory);
    boolean b = false;
    if (process != null) {
      b = process.canWork() != null;
    }
    return new TriggerData(BinnieTrigger.triggerCannotWork, Boolean.valueOf(b));
  }
}
