package binnie.extrabees.gui.database;

import binnie.craftgui.controls.ControlText;
import binnie.craftgui.controls.ControlTextCentered;
import binnie.craftgui.controls.listbox.ControlList;
import binnie.craftgui.controls.listbox.ControlOption;
import binnie.craftgui.core.geometry.CraftGUIUtil;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.minecraft.control.ControlItemDisplay;
import java.text.DecimalFormat;

public class ControlProductsItem
  extends ControlOption<ControlProductsBox.Product>
{
  ControlItemDisplay item;
  
  public ControlProductsItem(ControlList<ControlProductsBox.Product> controlList, ControlProductsBox.Product value, int y)
  {
    super(controlList, value, y);
    this.item = new ControlItemDisplay(this, 4.0F, 4.0F);
    this.item.setTooltip();
    
    ControlText textWidget = new ControlTextCentered(this, 2.0F, "");
    CraftGUIUtil.moveWidget(textWidget, new IPoint(12.0F, 0.0F));
    if (value != null)
    {
      this.item.setItemStack(value.item);
      float time = (int)(55000.0D / value.chance);
      
      float seconds = time / 20.0F;
      float minutes = seconds / 60.0F;
      float hours = minutes / 60.0F;
      
      DecimalFormat df = new DecimalFormat("#.0");
      if (hours > 1.0F) {
        textWidget.setValue("Every " + df.format(hours) + " hours");
      } else if (minutes > 1.0F) {
        textWidget.setValue("Every " + df.format(minutes) + " min.");
      } else {
        textWidget.setValue("Every " + df.format(seconds) + " sec.");
      }
    }
  }
}
