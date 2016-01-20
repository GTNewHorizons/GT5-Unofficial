package binnie.core;

public abstract interface IInitializable
{
  public abstract void preInit();
  
  public abstract void init();
  
  public abstract void postInit();
}
