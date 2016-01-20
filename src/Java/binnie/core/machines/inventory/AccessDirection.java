package binnie.core.machines.inventory;

 enum AccessDirection
{
  Both,  In,  Out,  Neither;
  
  private AccessDirection() {}
  
  boolean canInsert()
  {
    return (this == Both) || (this == In);
  }
  
  boolean canExtract()
  {
    return (this == Both) || (this == Out);
  }
  
  boolean canAccess()
  {
    return this != Neither;
  }
  
  AccessDirection changeInsert(boolean b)
  {
    if (b)
    {
      if (this == Out) {
        return Both;
      }
      if (this == Neither) {
        return In;
      }
    }
    else
    {
      if (this == Both) {
        return Out;
      }
      if (this == In) {
        return Neither;
      }
    }
    return this;
  }
  
  AccessDirection changeExtract(boolean b)
  {
    if (b)
    {
      if (this == In) {
        return Both;
      }
      if (this == Neither) {
        return Out;
      }
    }
    else
    {
      if (this == Both) {
        return In;
      }
      if (this == Out) {
        return Neither;
      }
    }
    return this;
  }
  
  public String getTextColour()
  {
    switch (1.$SwitchMap$binnie$core$machines$inventory$AccessDirection[ordinal()])
    {
    case 1: 
      return "§a";
    case 2: 
      return "§e";
    case 3: 
      return "§c";
    }
    return "§b";
  }
  
  public int getShadeColour()
  {
    switch (1.$SwitchMap$binnie$core$machines$inventory$AccessDirection[ordinal()])
    {
    case 1: 
      return 1431699285;
    case 2: 
      return 1442840405;
    case 3: 
      return 1442796885;
    }
    return 1431699455;
  }
}
