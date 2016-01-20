package binnie.craftgui.minecraft.render;

import binnie.craftgui.core.IWidget;
import binnie.craftgui.minecraft.GuiCraftGUI;

public abstract class RenderCommand
{
  IWidget widget;
  
  public RenderCommand(IWidget widget)
  {
    this.widget = widget;
  }
  
  public abstract void render(GuiCraftGUI paramGuiCraftGUI);
}
