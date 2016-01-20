package binnie.core.triggers;

import binnie.core.IInitializable;
import binnie.extrabees.ExtraBees;
import buildcraft.api.statements.StatementManager;

public class ModuleTrigger
  implements IInitializable
{
  public void preInit() {}
  
  public void init()
  {
    BinnieAction.actionPauseProcess = new BinnieAction("Pause Process", "binnie.action.pauseProcess", "actions/PauseProcess");
    BinnieAction.actionCancelTask = new BinnieAction("Cancel Task", "binnie.action.cancelTask", "actions/CancelTask");
    
    StatementManager.registerActionProvider(new ActionProvider());
    
    BinnieTrigger.triggerNoBlankTemplate = new BinnieTrigger("No Blank Template", "binnie.trigger.noBlankTemplate", ExtraBees.instance, "triggers/NoBlankTemplate");
    

    BinnieTrigger.triggerNoTemplate = new BinnieTrigger("No Template", "binnie.trigger.noTemplate", ExtraBees.instance, "triggers/NoTemplate");
    

    BinnieTrigger.triggerIsWorking = new BinnieTrigger("Is Working", "binnie.trigger.isWorking", "triggers/IsWorking");
    BinnieTrigger.triggerIsNotWorking = new BinnieTrigger("Is Not Working", "binnie.trigger.isNotWorking", "triggers/IsNotWorking");
    BinnieTrigger.triggerCanWork = new BinnieTrigger("Can Work", "binnie.trigger.canWork", "triggers/CanWork");
    BinnieTrigger.triggerCannotWork = new BinnieTrigger("Cannot Work", "binnie.trigger.cannotWork", "triggers/CannotWork");
    
    BinnieTrigger.triggerPowerNone = new BinnieTrigger("Power None", "binnie.trigger.powerNone", "triggers/PowerNone");
    BinnieTrigger.triggerPowerLow = new BinnieTrigger("Power Low", "binnie.trigger.powerLow", "triggers/PowerLow");
    BinnieTrigger.triggerPowerMedium = new BinnieTrigger("Power Medium", "binnie.trigger.powerMedium", "triggers/PowerMedium");
    BinnieTrigger.triggerPowerHigh = new BinnieTrigger("Power High", "binnie.trigger.powerHigh", "triggers/PowerHigh");
    BinnieTrigger.triggerPowerFull = new BinnieTrigger("Power Full", "binnie.trigger.powerFull", "triggers/PowerFull");
    

    BinnieTrigger.triggerSerumFull = new BinnieTrigger("Serum Full", "binnie.trigger.serumFull", ExtraBees.instance, "triggers/SerumFull");
    BinnieTrigger.triggerSerumPure = new BinnieTrigger("Serum Pure", "binnie.trigger.serumPure", ExtraBees.instance, "triggers/SerumPure");
    BinnieTrigger.triggerSerumEmpty = new BinnieTrigger("Serum Pure", "binnie.trigger.serumEmpty", ExtraBees.instance, "triggers/SerumEmpty");
    
    StatementManager.registerTriggerProvider(new TriggerProvider());
  }
  
  public void postInit() {}
}
