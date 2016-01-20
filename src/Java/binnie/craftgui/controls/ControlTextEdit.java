package binnie.craftgui.controls;

import binnie.craftgui.controls.core.Control;
import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.Attribute;
import binnie.craftgui.core.CraftGUI;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.renderer.Renderer;
import binnie.craftgui.events.EventHandler.Origin;
import binnie.craftgui.events.EventKey.Down;
import binnie.craftgui.events.EventKey.Down.Handler;
import binnie.craftgui.events.EventMouse.Down;
import binnie.craftgui.events.EventMouse.Down.Handler;
import binnie.craftgui.events.EventTextEdit;
import binnie.craftgui.events.EventWidget.GainFocus;
import binnie.craftgui.events.EventWidget.GainFocus.Handler;
import binnie.craftgui.events.EventWidget.LoseFocus;
import binnie.craftgui.events.EventWidget.LoseFocus.Handler;
import binnie.craftgui.minecraft.GuiCraftGUI;
import binnie.craftgui.minecraft.Window;
import binnie.craftgui.resource.minecraft.CraftGUITexture;
import net.minecraft.client.gui.GuiTextField;

public class ControlTextEdit
  extends Control
  implements IControlValue<String>
{
  GuiTextField field;
  
  public ControlTextEdit(IWidget parent, float x, float y, float width, float height)
  {
    super(parent, x, y, width, height);
    
    this.field = new GuiTextField(getWindow().getGui().getFontRenderer(), 0, 0, 10, 10);
    addAttribute(Attribute.CanFocus);
    addAttribute(Attribute.MouseOver);
    this.field.setEnableBackgroundDrawing(false);
    

































    addEventHandler(new EventKey.Down.Handler()
    {
      public void onEvent(EventKey.Down event)
      {
        ControlTextEdit.this.field.textboxKeyTyped(event.getCharacter(), event.getKey());
        String text = ControlTextEdit.this.getValue();
        if (!text.equals(ControlTextEdit.this.cachedValue))
        {
          ControlTextEdit.this.cachedValue = text;
          ControlTextEdit.this.callEvent(new EventTextEdit(ControlTextEdit.this, ControlTextEdit.this.cachedValue));
          ControlTextEdit.this.onTextEdit(ControlTextEdit.this.cachedValue);
        }
      }
    }.setOrigin(EventHandler.Origin.Self, this));
    



    addEventHandler(new EventMouse.Down.Handler()
    {
      public void onEvent(EventMouse.Down event)
      {
        ControlTextEdit.this.field.mouseClicked((int)ControlTextEdit.this.getRelativeMousePosition().x(), (int)ControlTextEdit.this.getRelativeMousePosition().y(), event.getButton());
      }
    }.setOrigin(EventHandler.Origin.Self, this));
    


    addEventHandler(new EventWidget.GainFocus.Handler()
    {
      public void onEvent(EventWidget.GainFocus event)
      {
        ControlTextEdit.this.field.setFocused(true);
      }
    }.setOrigin(EventHandler.Origin.Self, this));
    


    addEventHandler(new EventWidget.LoseFocus.Handler()
    {
      public void onEvent(EventWidget.LoseFocus event)
      {
        ControlTextEdit.this.field.setFocused(false);
      }
    }.setOrigin(EventHandler.Origin.Self, this));
  }
  
  public String getValue()
  {
    return this.field.getText() == null ? "" : this.field.getText();
  }
  
  public void setValue(String value)
  {
    if (!getValue().equals(value))
    {
      this.field.setText(value);
      this.field.setCursorPosition(0);
    }
  }
  
  private String cachedValue = "";
  
  public void onUpdateClient() {}
  
  protected void onTextEdit(String value) {}
  
  public void onRenderBackground()
  {
    CraftGUI.Render.texture(CraftGUITexture.Slot, getArea());
    renderTextField();
  }
  
  protected void renderTextField()
  {
    this.field.width = ((int)w());
    this.field.height = ((int)h());
    this.field.xPosition = ((int)((h() - 8.0F) / 2.0F));
    this.field.yPosition = ((int)((h() - 8.0F) / 2.0F));
    this.field.drawTextBox();
  }
}
