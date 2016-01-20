package binnie.core.machines.inventory;

public abstract interface IChargedSlots
{
  public abstract float getCharge(int paramInt);
  
  public abstract void setCharge(int paramInt, float paramFloat);
  
  public abstract void alterCharge(int paramInt, float paramFloat);
}
