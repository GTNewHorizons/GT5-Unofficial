package binnie.extrabees.core;

import binnie.Binnie;
import binnie.core.IInitializable;
import binnie.core.item.ManagerItem;
import forestry.api.core.Tabs;

public class ModuleCore
  implements IInitializable
{
  public void preInit()
  {
    binnie.extrabees.ExtraBees.itemMisc = Binnie.Item.registerMiscItems(ExtraBeeItems.values(), Tabs.tabApiculture);
  }
  
  public void init() {}
  
  public void postInit() {}
}
