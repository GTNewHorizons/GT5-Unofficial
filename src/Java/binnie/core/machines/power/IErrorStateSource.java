package binnie.core.machines.power;

public abstract interface IErrorStateSource
{
  public abstract ErrorState canWork();
  
  public abstract ErrorState canProgress();
}
