package binnie.extrabees.liquids;

import binnie.Binnie;
import binnie.core.IInitializable;
import binnie.core.liquid.ItemFluidContainer;
import binnie.core.liquid.ManagerLiquid;

public class ModuleLiquids
  implements IInitializable
{
  public void preInit()
  {
    Binnie.Liquid.createLiquids(ExtraBeeLiquid.values(), ItemFluidContainer.LiquidExtraBee);
  }
  
  public void init() {}
  
  public void postInit() {}
}
