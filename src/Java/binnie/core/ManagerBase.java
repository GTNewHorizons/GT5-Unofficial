package binnie.core;

import binnie.Binnie;
import java.util.List;

public abstract class ManagerBase
  implements IInitializable
{
  public ManagerBase()
  {
    Binnie.Managers.add(this);
  }
  
  public void preInit() {}
  
  public void init() {}
  
  public void postInit() {}
}
