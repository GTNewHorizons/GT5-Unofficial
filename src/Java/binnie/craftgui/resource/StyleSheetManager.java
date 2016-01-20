package binnie.craftgui.resource;

import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.resource.minecraft.CraftGUIResourceManager;

public class StyleSheetManager
{
  static IStyleSheet defaultSS = new DefaultStyleSheet(null);
  
  public static Texture getTexture(Object key)
  {
    return defaultSS.getTexture(key);
  }
  
  private static class DefaultStyleSheet
    implements IStyleSheet
  {
    public Texture getTexture(Object key)
    {
      return CraftGUI.ResourceManager.getTexture(key.toString());
    }
  }
  
  public static IStyleSheet getDefault()
  {
    return defaultSS;
  }
}
