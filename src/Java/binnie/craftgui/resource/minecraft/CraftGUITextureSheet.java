package binnie.craftgui.resource.minecraft;

import binnie.core.BinnieCore;
import binnie.core.proxy.BinnieProxy;
import binnie.core.resource.BinnieResource;
import binnie.core.resource.IBinnieTexture;
import binnie.craftgui.core.CraftGUI;

public enum CraftGUITextureSheet
  implements IBinnieTexture
{
  Controls2("controls"),  Panel2("panels"),  Slots("slots");
  
  String name;
  
  private CraftGUITextureSheet(String name)
  {
    this.name = name;
  }
  
  public String toString()
  {
    return this.name;
  }
  
  public BinnieResource getTexture()
  {
    if (BinnieCore.proxy.isServer()) {
      return null;
    }
    return CraftGUI.ResourceManager.getTextureSheet(this.name).getTexture();
  }
}
