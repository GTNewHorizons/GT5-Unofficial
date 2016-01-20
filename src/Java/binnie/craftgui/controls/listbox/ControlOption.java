package binnie.craftgui.controls.listbox;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.resource.minecraft.CraftGUITexture;

public class ControlOption<T>
  extends Control
  implements IControlValue<T>
{
  T value;
  
  public void onUpdateClient()
  {
    if (getValue() == null) {
      return;
    }
    int colour = 10526880;
    if (isCurrentSelection()) {
      colour = 16777215;
    }
    setColour(colour);
  }
  
  public ControlOption(ControlList<T> controlList, T option)
  {
    this(controlList, option, 16);
  }
  
  public ControlOption(ControlList<T> controlList, T option, int height)
  {
    super(controlList, 0.0F, height, controlList.getSize().x(), 20.0F);
    this.value = option;
    if (this.value != null) {
      addAttribute(Attribute.MouseOver);
    }
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        ((IControlValue)ControlOption.this.getParent()).setValue(ControlOption.this.getValue());
      }
    });
  }
  
  public T getValue()
  {
    return this.value;
  }
  
  public void setValue(T value)
  {
    this.value = value;
  }
  
  public boolean isCurrentSelection()
  {
    return (getValue() != null) && (getValue().equals(((IControlValue)getParent()).getValue()));
  }
  
  public void onRenderForeground()
  {
    if (isCurrentSelection()) {
      CraftGUI.Render.texture(CraftGUITexture.Outline, getArea());
    }
  }
}
