package binnie.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraftforge.fluids.FluidStack;

class FluidStackSet
  implements Set<FluidStack>
{
  public String toString()
  {
    return this.itemStacks.toString();
  }
  
  List<FluidStack> itemStacks = new ArrayList();
  
  protected FluidStack getExisting(FluidStack stack)
  {
    for (FluidStack stack2 : this.itemStacks) {
      if (stack2.isFluidEqual(stack)) {
        return stack2;
      }
    }
    return null;
  }
  
  public boolean add(FluidStack e)
  {
    if (e != null)
    {
      if (getExisting(e) == null) {
        return this.itemStacks.add(e.copy());
      }
      getExisting(e).amount += e.amount;
    }
    return false;
  }
  
  public boolean addAll(Collection<? extends FluidStack> c)
  {
    boolean addedAll = true;
    for (FluidStack stack : c) {
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
    if (!(o instanceof FluidStack)) {
      return false;
    }
    return getExisting((FluidStack)o) != null;
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
  
  public Iterator<FluidStack> iterator()
  {
    return this.itemStacks.iterator();
  }
  
  public boolean remove(Object o)
  {
    if (contains(o))
    {
      FluidStack r = (FluidStack)o;
      FluidStack existing = getExisting(r);
      if (existing.amount > r.amount) {
        existing.amount -= r.amount;
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
