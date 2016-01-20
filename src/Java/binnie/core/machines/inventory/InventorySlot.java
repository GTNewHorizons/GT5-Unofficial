package binnie.core.machines.inventory;

import binnie.Binnie;
import binnie.core.BinnieCore;
import binnie.core.language.ManagerLanguage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class InventorySlot
  extends BaseSlot<ItemStack>
{
  public static enum Type
  {
    Standard,  Recipe;
    
    private Type() {}
  }
  
  private ItemStack itemStack = null;
  private Type type = Type.Standard;
  
  public InventorySlot(int index, String unlocName)
  {
    super(index, unlocName);
  }
  
  public ItemStack getContent()
  {
    return this.itemStack;
  }
  
  public ItemStack getItemStack()
  {
    return getContent();
  }
  
  public void setContent(ItemStack itemStack)
  {
    this.itemStack = itemStack;
  }
  
  public ItemStack decrStackSize(int amount)
  {
    if (this.itemStack == null) {
      return null;
    }
    if (this.itemStack.stackSize <= amount)
    {
      ItemStack returnStack = this.itemStack.copy();
      this.itemStack = null;
      return returnStack;
    }
    ItemStack returnStack = this.itemStack.copy();
    this.itemStack.stackSize -= amount;
    returnStack.stackSize = amount;
    return returnStack;
  }
  
  public void readFromNBT(NBTTagCompound slotNBT)
  {
    if (slotNBT.hasKey("item"))
    {
      NBTTagCompound itemNBT = slotNBT.getCompoundTag("item");
      this.itemStack = ItemStack.loadItemStackFromNBT(itemNBT);
    }
    else
    {
      this.itemStack = null;
    }
  }
  
  public void writeToNBT(NBTTagCompound slotNBT)
  {
    NBTTagCompound itemNBT = new NBTTagCompound();
    if (this.itemStack != null) {
      this.itemStack.writeToNBT(itemNBT);
    }
    slotNBT.setTag("item", itemNBT);
  }
  
  public void setItemStack(ItemStack duplicate)
  {
    setContent(duplicate);
  }
  
  public SlotValidator getValidator()
  {
    return (SlotValidator)this.validator;
  }
  
  public void setType(Type type)
  {
    this.type = type;
    if (type == Type.Recipe)
    {
      setReadOnly();
      forbidInteraction();
    }
  }
  
  public Type getType()
  {
    return this.type;
  }
  
  public boolean isRecipe()
  {
    return this.type == Type.Recipe;
  }
  
  public String getName()
  {
    return Binnie.Language.localise(BinnieCore.instance, "gui.slot." + this.unlocName);
  }
}
