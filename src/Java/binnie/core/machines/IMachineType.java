package binnie.core.machines;

import binnie.core.network.IOrdinaled;

public abstract interface IMachineType
  extends IOrdinaled
{
  public abstract Class<? extends MachinePackage> getPackageClass();
  
  public abstract boolean isActive();
}
