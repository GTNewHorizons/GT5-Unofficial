package binnie.core.machines.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class SetList<E>
  extends ArrayList<E>
  implements Set<E>
{
  private static final long serialVersionUID = 1277112003159980135L;
  
  public boolean add(E e)
  {
    return contains(e) ? false : super.add(e);
  }
  
  public void add(int index, E e)
  {
    if (!contains(e)) {
      super.add(index, e);
    }
  }
  
  public boolean addAll(Collection<? extends E> c)
  {
    return addAll(size(), c);
  }
  
  public boolean addAll(int index, Collection<? extends E> c)
  {
    Collection<E> copy = new ArrayList(c);
    copy.removeAll(this);
    return super.addAll(index, copy);
  }
}
