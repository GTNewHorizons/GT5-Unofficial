package binnie.craftgui.minecraft;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;

class ListMap<T>
  implements List<T>
{
  private LinkedHashMap<Integer, T> map = new LinkedHashMap();
  
  public int size()
  {
    int i = -1;
    for (Iterator i$ = this.map.keySet().iterator(); i$.hasNext();)
    {
      int k = ((Integer)i$.next()).intValue();
      if (k > i) {
        i = k;
      }
    }
    return i + 1;
  }
  
  public boolean isEmpty()
  {
    return this.map.isEmpty();
  }
  
  public boolean contains(Object o)
  {
    return this.map.containsValue(o);
  }
  
  public Iterator<T> iterator()
  {
    return this.map.values().iterator();
  }
  
  public Object[] toArray()
  {
    return this.map.values().toArray();
  }
  
  public <P> P[] toArray(P[] a)
  {
    return this.map.values().toArray(a);
  }
  
  public boolean add(T e)
  {
    if (get(size()) == null)
    {
      add(size(), e);
      return true;
    }
    return false;
  }
  
  public boolean remove(Object o)
  {
    return false;
  }
  
  public boolean containsAll(Collection<?> c)
  {
    return this.map.values().containsAll(c);
  }
  
  public boolean addAll(Collection<? extends T> c)
  {
    return false;
  }
  
  public boolean addAll(int index, Collection<? extends T> c)
  {
    return false;
  }
  
  public boolean removeAll(Collection<?> c)
  {
    return false;
  }
  
  public boolean retainAll(Collection<?> c)
  {
    return false;
  }
  
  public void clear()
  {
    this.map.clear();
  }
  
  public T get(int index)
  {
    return this.map.get(Integer.valueOf(index));
  }
  
  public T set(int index, T element)
  {
    this.map.put(Integer.valueOf(index), element);
    return element;
  }
  
  public void add(int index, T element)
  {
    this.map.put(Integer.valueOf(index), element);
  }
  
  public T remove(int index)
  {
    return null;
  }
  
  public int indexOf(Object o)
  {
    for (Map.Entry<Integer, T> entry : this.map.entrySet()) {
      if (entry.getValue() == o) {
        return ((Integer)entry.getKey()).intValue();
      }
    }
    return 0;
  }
  
  public int lastIndexOf(Object o)
  {
    return indexOf(o);
  }
  
  public ListIterator<T> listIterator()
  {
    return null;
  }
  
  public ListIterator<T> listIterator(int index)
  {
    return null;
  }
  
  public List<T> subList(int fromIndex, int toIndex)
  {
    return null;
  }
}
