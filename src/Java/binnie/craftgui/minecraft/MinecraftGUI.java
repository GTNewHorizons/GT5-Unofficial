package binnie.craftgui.minecraft;

import binnie.craftgui.window.Panel.IPanelType;

public class MinecraftGUI
{
  public static enum PanelType
    implements Panel.IPanelType
  {
    Black,  Gray,  Tinted,  Coloured,  Outline,  TabOutline;
    
    private PanelType() {}
  }
}
