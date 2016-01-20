package binnie.core.machines.power;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum PowerSystem
{
  MJ(100.0D),  RF(10.0D),  EU(40.0D);
  
  double conversion;
  
  private PowerSystem(double conversion)
  {
    this.conversion = conversion;
  }
  
  public double convertTo(int value)
  {
    return value / this.conversion;
  }
  
  public int convertFrom(double value)
  {
    return (int)(value * this.conversion);
  }
  
  public static PowerSystem get(int i)
  {
    return values()[(i % values().length)];
  }
  
  public String getUnitName()
  {
    return name();
  }
  
  public ItemStack saveTo(ItemStack stack)
  {
    NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    tag.setByte("power-system", (byte)ordinal());
    stack.setTagCompound(tag);
    return stack;
  }
}
