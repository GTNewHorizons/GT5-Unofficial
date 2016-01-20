package binnie.core.util;

import java.util.List;
import net.minecraftforge.fluids.FluidStack;

public class UniqueFluidStackSet
  extends FluidStackSet
{
  public boolean add(FluidStack e)
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
      FluidStack r = (FluidStack)o;
      FluidStack existing = getExisting(r);
      this.itemStacks.remove(existing);
    }
    return false;
  }
}
