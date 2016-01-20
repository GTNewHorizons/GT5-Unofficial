package binnie.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.item.ItemStack;

public class ItemStackSet
  implements Set<ItemStack>
{
  public String toString()
  {
    return this.itemStacks.toString();
  }
  
  List<ItemStack> itemStacks = new ArrayList();
  
  protected ItemStack getExisting(ItemStack stack)
  {
    for (ItemStack stack2 : this.itemStacks) {
      if (stack2.isItemEqual(stack)) {
        return stack2;
      }
    }
    return null;
  }
  
  public boolean add(ItemStack e)
  {
    if (e != null)
    {
      if (getExisting(e) == null) {
        return this.itemStacks.add(e.copy());
      }
      getExisting(e).stackSize += e.stackSize;
    }
    return false;
  }
  
  public boolean addAll(Collection<? extends ItemStack> c)
  {
    boolean addedAll = true;
    for (ItemStack stack : c) {
      addedAll = (add(stack)) && (addedAll);
    }
    return addedAll;
  }
  
  public void clear()
  {
    this.itemStacks.clear();
  }
  
  public boolean contains(Object o)
  {
    if (!(o instanceof ItemStack)) {
      return false;
    }
    return getExisting((ItemStack)o) != null;
  }
  
  public boolean containsAll(Collection<?> c)
  {
    boolean addedAll = true;
    for (Object o : c) {
      addedAll = (addedAll) && (contains(o));
    }
    return false;
  }
  
  public boolean isEmpty()
  {
    return this.itemStacks.isEmpty();
  }
  
  public Iterator<ItemStack> iterator()
  {
    return this.itemStacks.iterator();
  }
  
  public boolean remove(Object o)
  {
    if (contains(o))
    {
      ItemStack r = (ItemStack)o;
      ItemStack existing = getExisting(r);
      if (existing.stackSize > r.stackSize) {
        existing.stackSize -= r.stackSize;
      } else {
        this.itemStacks.remove(existing);
      }
    }
    return false;
  }
  
  public boolean removeAll(Collection<?> c)
  {
    boolean addedAll = true;
    for (Object o : c)
    {
      boolean removed = remove(o);
      addedAll = (removed) && (addedAll);
    }
    return false;
  }
  
  public boolean retainAll(Collection<?> c)
  {
    return this.itemStacks.retainAll(c);
  }
  
  public int size()
  {
    return this.itemStacks.size();
  }
  
  public Object[] toArray()
  {
    return this.itemStacks.toArray();
  }
  
  public <T> T[] toArray(T[] a)
  {
    return this.itemStacks.toArray(a);
  }
}
