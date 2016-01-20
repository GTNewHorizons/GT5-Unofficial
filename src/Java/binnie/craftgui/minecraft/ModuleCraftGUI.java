package binnie.craftgui.minecraft;

import binnie.core.IInitializable;

public class ModuleCraftGUI
  implements IInitializable
{
  public void preInit() {}
  
  public void init() {}
  
  public void postInit()
  {
    for (GUIIcon icon : ) {
      icon.register();
    }
  }
}
