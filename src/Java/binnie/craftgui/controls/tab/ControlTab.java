package binnie.craftgui.controls.tab;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.ITooltip;
import binnie.craftgui.core.Tooltip;
import binnie.craftgui.core.geometry.IArea;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.events.EventValueChanged;
import binnie.craftgui.minecraft.control.ControlItemDisplay;
import binnie.craftgui.minecraft.control.ControlTabIcon;
import binnie.craftgui.resource.Texture;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import java.util.List;

public class ControlTab<T>
  extends Control
  implements ITooltip, IControlValue<T>
{
  private ControlTabBar<T> tabBar;
  protected T value;
  
  public ControlTab(ControlTabBar<T> parent, float x, float y, float w, float h, T value)
  {
    super(parent, x, y, w, h);
    setValue(value);
    this.tabBar = parent;
    addAttribute(Attribute.MouseOver);
    
    addSelfEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        ControlTab.this.callEvent(new EventValueChanged(ControlTab.this.getWidget(), ControlTab.this.getValue()));
      }
    });
  }
  
  public void getTooltip(Tooltip tooltip)
  {
    String name = getName();
    if ((name != null) && (!name.isEmpty())) {
      tooltip.add(getName());
    }
    if ((this.value instanceof ITooltip)) {
      ((ITooltip)this.value).getTooltip(tooltip);
    }
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
    return (getValue() != null) && (getValue().equals(this.tabBar.getValue()));
  }
  
  public Position getTabPosition()
  {
    return this.tabBar.position;
  }
  
  public String getName()
  {
    return this.value.toString();
  }
  
  public void onRenderBackground()
  {
    Object texture = CraftGUITexture.TabDisabled;
    if (isMouseOver()) {
      texture = CraftGUITexture.TabHighlighted;
    } else if (isCurrentSelection()) {
      texture = CraftGUITexture.Tab;
    }
    Texture lTexture = CraftGUI.Render.getTexture(texture);
    Position position = getTabPosition();
    
    Texture iTexture = lTexture.crop(position, 8.0F);
    

    IArea area = getArea();
    if (texture == CraftGUITexture.TabDisabled) {
      if ((position == Position.Top) || (position == Position.Left))
      {
        area.setPosition(area.getPosition().sub(new IPoint(4 * position.x(), 4 * position.y())));
        area.setSize(area.getSize().add(new IPoint(4 * position.x(), 4 * position.y())));
      }
      else
      {
        area.setSize(area.getSize().sub(new IPoint(4 * position.x(), 4 * position.y())));
      }
    }
    CraftGUI.Render.texture(iTexture, area);
    if ((this instanceof ControlTabIcon))
    {
      ControlTabIcon icon = (ControlTabIcon)this;
      ControlItemDisplay item = (ControlItemDisplay)getWidgets().get(0);
      if (texture == CraftGUITexture.TabDisabled) {
        item.setColour(-1431655766);
      } else {
        item.setColour(-1);
      }
      if (icon.hasOutline())
      {
        iTexture = CraftGUI.Render.getTexture(CraftGUITexture.TabOutline);
        iTexture = iTexture.crop(position, 8.0F);
        
        CraftGUI.Render.colour(icon.getOutlineColour());
        
        CraftGUI.Render.texture(iTexture, area.inset(2));
      }
    }
  }
}
