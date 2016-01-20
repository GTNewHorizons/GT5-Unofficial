package binnie.craftgui.core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class Tooltip
{
  public void add(String string)
  {
    this.tooltip.add(string);
  }
  
  public String getLine(int index)
  {
    String string = (String)getList().get(index);
    return string;
  }
  
  public void add(List list)
  {
    for (Object obj : list) {
      this.tooltip.add((String)obj);
    }
  }
  
  List<String> tooltip = new ArrayList();
  
  public List<String> getList()
  {
    return this.tooltip;
  }
  
  public boolean exists()
  {
    return this.tooltip.size() > 0;
  }
  
  public static enum Type
    implements Tooltip.ITooltipType
  {
    Standard,  Help,  Information,  User,  Power;
    
    private Type() {}
  }
  
  public void setType(ITooltipType type)
  {
    this.type = type;
  }
  
  ITooltipType type = Type.Standard;
  public int maxWidth = 256;
  
  public void setMaxWidth(int w)
  {
    this.maxWidth = w;
  }
  
  public ITooltipType getType()
  {
    return this.type;
  }
  
  public void add(ItemStack item, String string)
  {
    NBTTagCompound nbt = new NBTTagCompound();
    item.writeToNBT(nbt);
    nbt.setByte("nbt-type", (byte)105);
    add("~~~" + nbt.toString() + "~~~" + string);
  }
  
  public void add(FluidStack item, String string)
  {
    NBTTagCompound nbt = new NBTTagCompound();
    item.writeToNBT(nbt);
    nbt.setByte("nbt-type", (byte)102);
    add("~~~" + nbt.toString() + "~~~" + string);
  }
  
  public static abstract interface ITooltipType {}
}
