package binnie.core.triggers;

import binnie.core.machines.component.IBuildcraft.TriggerProvider;
import buildcraft.api.statements.IStatementContainer;
import buildcraft.api.statements.ITriggerExternal;
import buildcraft.api.statements.ITriggerInternal;
import buildcraft.api.statements.ITriggerProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

class TriggerProvider
  implements ITriggerProvider
{
  static TriggerProvider instance = new TriggerProvider();
  public static List<BinnieTrigger> triggers = new ArrayList();
  
  public Collection<ITriggerExternal> getExternalTriggers(ForgeDirection side, TileEntity tile)
  {
    LinkedList<TriggerData> list = new LinkedList();
    
    LinkedList<ITriggerExternal> triggerData = new LinkedList();
    if ((tile instanceof IBuildcraft.TriggerProvider)) {
      ((IBuildcraft.TriggerProvider)tile).getTriggers(list);
    }
    for (TriggerData data : list) {
      if ((data.getKey() != null) && (data.getKey().getUniqueTag() != null)) {
        triggerData.add(data.getKey());
      }
    }
    return triggerData;
  }
  
  public static boolean isTriggerActive(ITriggerExternal trigger, TileEntity tile)
  {
    LinkedList<TriggerData> list = new LinkedList();
    
    LinkedList<ITriggerExternal> triggerData = new LinkedList();
    if ((tile instanceof IBuildcraft.TriggerProvider)) {
      ((IBuildcraft.TriggerProvider)tile).getTriggers(list);
    }
    for (TriggerData data : list) {
      if (data.getKey() == trigger) {
        return data.getValue().booleanValue();
      }
    }
    return false;
  }
  
  public Collection<ITriggerInternal> getInternalTriggers(IStatementContainer container)
  {
    return new ArrayList();
  }
}
