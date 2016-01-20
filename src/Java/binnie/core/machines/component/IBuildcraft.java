package binnie.core.machines.component;

import binnie.core.triggers.TriggerData;
import buildcraft.api.statements.IActionExternal;
import buildcraft.api.statements.IActionReceptor;
import java.util.List;

public abstract interface IBuildcraft
{
  public static abstract interface TriggerProvider
  {
    public abstract void getTriggers(List<TriggerData> paramList);
  }
  
  public static abstract interface ActionProvider
    extends IActionReceptor
  {
    public abstract void getActions(List<IActionExternal> paramList);
  }
}
