package binnie.core.machines;

import java.util.Collection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract interface IMachine
  extends IOwnable
{
  public abstract void addComponent(MachineComponent paramMachineComponent);
  
  public abstract MachineUtil getMachineUtil();
  
  public abstract <T> T getInterface(Class<T> paramClass);
  
  public abstract void markDirty();
  
  public abstract World getWorld();
  
  public abstract TileEntity getTileEntity();
  
  public abstract <T> Collection<T> getInterfaces(Class<T> paramClass);
  
  public abstract MachinePackage getPackage();
}
