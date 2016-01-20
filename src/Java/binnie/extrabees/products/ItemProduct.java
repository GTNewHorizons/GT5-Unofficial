package binnie.extrabees.products;

import binnie.core.item.IItemEnum;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemProduct
  extends Item
{
  IItemEnum[] types;
  
  public ItemProduct(IItemEnum[] types)
  {
    setMaxStackSize(64);
    setMaxDamage(0);
    setHasSubtypes(true);
    this.types = types;
  }
  
  public IItemEnum get(ItemStack stack)
  {
    int i = stack.getItemDamage();
    if ((i >= 0) && (i < this.types.length)) {
      return this.types[i];
    }
    return this.types[0];
  }
  
  public String getItemStackDisplayName(ItemStack itemstack)
  {
    return get(itemstack).getName(itemstack);
  }
  
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList)
  {
    for (IItemEnum type : this.types) {
      if (type.isActive()) {
        itemList.add(new ItemStack(this, 1, type.ordinal()));
      }
    }
  }
}
