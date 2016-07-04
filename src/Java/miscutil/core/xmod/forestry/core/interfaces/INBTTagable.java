package miscutil.core.xmod.forestry.core.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public abstract interface INBTTagable
{
  public abstract void readFromNBT(NBTTagCompound paramNBTTagCompound);
  
  public abstract void writeToNBT(NBTTagCompound paramNBTTagCompound);
}
