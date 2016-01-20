package binnie.craftgui.minecraft.control;

import binnie.core.genetics.IItemStackRepresentitive;
import binnie.craftgui.controls.tab.ControlTab;
import binnie.craftgui.controls.tab.ControlTabBar;
import binnie.craftgui.core.geometry.IPoint;
import binnie.craftgui.core.geometry.Position;
import net.minecraft.item.ItemStack;

public class ControlTabIcon<T>
  extends ControlTab<T>
{
  private ControlItemDisplay item;
  
  public ControlTabIcon(ControlTabBar<T> parent, float x, float y, float w, float h, T value)
  {
    super(parent, x, y, w, h, value);
    this.item = new ControlItemDisplay(this, -8.0F + w / 2.0F, -8.0F + h / 2.0F);
    this.item.hastooltip = false;
  }
  
  public ItemStack getItemStack()
  {
    if ((this.value instanceof IItemStackRepresentitive)) {
      return ((IItemStackRepresentitive)this.value).getItemStackRepresentitive();
    }
    return null;
  }
  
  public void onUpdateClient()
  {
    super.onUpdateClient();
    this.item.setItemStack(getItemStack());
    float x = ((ControlTabBar)getParent()).getDirection().x();
    this.item.setOffset(new IPoint((isCurrentSelection()) || (isMouseOver()) ? 0.0F : -4.0F * x, 0.0F));
  }
  
  public boolean hasOutline()
  {
    return false;
  }
  
  public int getOutlineColour()
  {
    return 16777215;
  }
}
