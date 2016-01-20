package binnie.core.machines.inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraftforge.common.util.ForgeDirection;

class SidedAccess
{
  private Map<ForgeDirection, AccessDirection> accesses = new HashMap();
  private AccessDirection base = AccessDirection.Both;
  private boolean insertLocked = false;
  private boolean extractLocked = false;
  
  public AccessDirection getAccess(ForgeDirection side)
  {
    return this.accesses.containsKey(side) ? (AccessDirection)this.accesses.get(side) : this.base;
  }
  
  public boolean canInsert(ForgeDirection side)
  {
    return getAccess(side).canInsert();
  }
  
  public boolean canExtract(ForgeDirection side)
  {
    return getAccess(side).canExtract();
  }
  
  public boolean canAccess(ForgeDirection side)
  {
    return getAccess(side).canAccess();
  }
  
  public boolean canChangeInsert()
  {
    return !this.insertLocked;
  }
  
  public boolean canChangeExtract()
  {
    return !this.extractLocked;
  }
  
  public void forbidInsertChange()
  {
    this.insertLocked = true;
  }
  
  public void forbidExtractChange()
  {
    this.extractLocked = true;
  }
  
  public Collection<ForgeDirection> getInsertionSides()
  {
    List<ForgeDirection> dirs = new ArrayList();
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      if (getAccess(dir).canInsert()) {
        dirs.add(dir);
      }
    }
    return dirs;
  }
  
  public Collection<ForgeDirection> getExtractionSides()
  {
    List<ForgeDirection> dirs = new ArrayList();
    for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
      if (getAccess(dir).canExtract()) {
        dirs.add(dir);
      }
    }
    return dirs;
  }
  
  public void setInsert(ForgeDirection side, boolean b)
  {
    if (getAccess(side).canInsert() != b) {
      this.accesses.put(side, getAccess(side).changeInsert(b));
    }
  }
  
  public void setExtract(ForgeDirection side, boolean b)
  {
    if (getAccess(side).canExtract() != b) {
      this.accesses.put(side, getAccess(side).changeExtract(b));
    }
  }
  
  public void setInsert(boolean b)
  {
    if (this.base.canInsert() != b) {
      this.base = this.base.changeInsert(b);
    }
  }
  
  public void setExtract(boolean b)
  {
    if (this.base.canExtract() != b) {
      this.base = this.base.changeExtract(b);
    }
  }
}
