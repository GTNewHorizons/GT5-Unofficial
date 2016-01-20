package binnie.craftgui.minecraft;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.resource.minecraft.CraftGUITexture;

public abstract class Dialog
  extends Control
{
  public Dialog(IWidget parent, float w, float h)
  {
    super(parent, (parent.w() - w) / 2.0F, (parent.h() - h) / 2.0F, w, h);
    addAttribute(Attribute.MouseOver);
    addAttribute(Attribute.AlwaysOnTop);
    addAttribute(Attribute.BlockTooltip);
    initialise();
    addEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        if (!Dialog.this.getArea().contains(Dialog.this.getRelativeMousePosition()))
        {
          Dialog.this.onClose();
          Dialog.this.getParent().deleteChild(Dialog.this);
        }
      }
    }.setOrigin(EventHandler.Origin.Any, this));
  }
  
  public abstract void initialise();
  
  public abstract void onClose();
  
  public void onRenderBackground()
  {
    CraftGUI.Render.gradientRect(getArea().outset(400), -1442840576, -1442840576);
    CraftGUI.Render.texture(CraftGUITexture.Window, getArea());
    CraftGUI.Render.texture(CraftGUITexture.TabOutline, getArea().inset(4));
  }
  
  public boolean isMouseOverWidget(IPoint relativeMouse)
  {
    return true;
  }
}
