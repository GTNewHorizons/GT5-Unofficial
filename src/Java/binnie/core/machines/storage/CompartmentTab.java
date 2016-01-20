package binnie.core.machines.storage;

import binnie.craftgui.minecraft.EnumColor;
import forestry.api.core.INBTTagable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

class CompartmentTab
  implements INBTTagable
{
  private String name;
  private ItemStack icon;
  private EnumColor color;
  private int id;
  
  public CompartmentTab(int id)
  {
    this.name = "";
    this.icon = new ItemStack(Items.paper);
    this.color = EnumColor.White;
    this.id = id;
  }
  
  public void readFromNBT(NBTTagCompound nbt)
  {
    this.name = nbt.getString("name");
    this.icon = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("icon"));
    this.color = EnumColor.values()[nbt.getByte("color")];
    this.id = nbt.getByte("id");
  }
  
  public void writeToNBT(NBTTagCompound nbt)
  {
    nbt.setString("name", this.name);
    NBTTagCompound n = new NBTTagCompound();
    this.icon.writeToNBT(n);
    nbt.setTag("icon", n);
    nbt.setByte("color", (byte)this.color.ordinal());
    nbt.setByte("id", (byte)this.id);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public ItemStack getIcon()
  {
    return this.icon;
  }
  
  public EnumColor getColor()
  {
    return this.color;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setName(String name)
  {
    this.name = (name == null ? "" : name);
  }
  
  public void setIcon(ItemStack icon)
  {
    this.icon = icon;
  }
  
  public void setColor(EnumColor color)
  {
    this.color = color;
  }
}
