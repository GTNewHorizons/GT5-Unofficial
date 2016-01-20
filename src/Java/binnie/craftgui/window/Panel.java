package binnie.craftgui.window;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.minecraft.MinecraftGUI.PanelType;
import binnie.craftgui.resource.minecraft.CraftGUITexture;

public class Panel
  extends Control
{
  IPanelType type;
  
  public Panel(IWidget parent, float x, float y, float width, float height, IPanelType type)
  {
    super(parent, x, y, width, height);
    this.type = type;
  }
  
  public Panel(IWidget parent, IArea area, IPanelType type)
  {
    this(parent, area.x(), area.y(), area.w(), area.h(), type);
  }
  
  public IPanelType getType()
  {
    return this.type;
  }
  
  public void onRenderBackground()
  {
    IPanelType panelType = getType();
    if ((panelType instanceof MinecraftGUI.PanelType)) {
      switch (1.$SwitchMap$binnie$craftgui$minecraft$MinecraftGUI$PanelType[((MinecraftGUI.PanelType)panelType).ordinal()])
      {
      case 1: 
        CraftGUI.Render.texture(CraftGUITexture.PanelBlack, getArea());
        break;
      case 2: 
        CraftGUI.Render.texture(CraftGUITexture.PanelGray, getArea());
        break;
      case 3: 
        CraftGUI.Render.texture(CraftGUITexture.PanelTinted, getArea());
        break;
      case 4: 
        CraftGUI.Render.texture(CraftGUITexture.Outline, getArea());
        break;
      case 5: 
        CraftGUI.Render.texture(CraftGUITexture.TabOutline, getArea());
        break;
      }
    }
  }
  
  public static abstract interface IPanelType {}
}
