package binnie.craftgui.extratrees.kitchen;

import binnie.craftgui.controls.core.IControlValue;
import binnie.craftgui.core.IWidget;
import binnie.craftgui.minecraft.control.ControlSlotBase;
import binnie.extratrees.alcohol.Glassware;
import net.minecraft.item.ItemStack;

public class ControlSlotGlassware
  extends ControlSlotBase
  implements IControlValue<Glassware>
{
  Glassware glassware;
  
  public ControlSlotGlassware(IWidget parent, int x, int y, Glassware glassware)
  {
    super(parent, x, y);
    this.glassware = glassware;
  }
  
  public Glassware getValue()
  {
    return this.glassware;
  }
  
  public void setValue(Glassware value)
  {
    this.glassware = value;
  }
  
  public ItemStack getItemStack()
  {
    return this.glassware.get(1);
  }
}
