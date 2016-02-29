package miscutil.core.waila;

import net.minecraft.nbt.NBTTagCompound;

public abstract interface IWailaNBTProvider
{
  public abstract void getData(NBTTagCompound paramNBTTagCompound);
}
