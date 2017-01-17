package cofh.energy;

public abstract interface IEnergyStorage
{
  public abstract int receiveEnergy(int paramInt, boolean paramBoolean);
  
  public abstract int extractEnergy(int paramInt, boolean paramBoolean);
  
  public abstract int getEnergyStored();
  
  public abstract int getMaxEnergyStored();
}
