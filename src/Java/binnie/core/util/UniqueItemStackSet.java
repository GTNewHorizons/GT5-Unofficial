package binnie.core.util;

import java.util.List;
import net.minecraft.item.ItemStack;

public class UniqueItemStackSet
  extends ItemStackSet
{
  public boolean add(ItemStack e)
  {
    if ((e != null) && 
      (getExisting(e) == null)) {
      return this.itemStacks.add(e.copy());
    }
    return false;
  }
  
  public boolean remove(Object o)
  {
    if (contains(o))
    {
      ItemStack r = (ItemStack)o;
      ItemStack existing = getExisting(r);
      this.itemStacks.remove(existing);
    }
    return false;
  }
}
