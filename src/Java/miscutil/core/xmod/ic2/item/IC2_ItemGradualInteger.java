package miscutil.core.xmod.ic2.item;

import ic2.api.item.ICustomDamageItem;
import ic2.core.util.StackUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class IC2_ItemGradualInteger
  extends IC2_ItemGradual
  implements ICustomDamageItem
{
  private final int maxDmg;
  
  public IC2_ItemGradualInteger(String internalName, int maxdmg)
  {
    super(internalName);
    
    this.maxDmg = maxdmg;
  }
  
  @Override
public int getCustomDamage(ItemStack stack)
  {
    NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
    return nbt.getInteger("advDmg");
  }
  
  @Override
public int getMaxCustomDamage(ItemStack stack)
  {
    return this.maxDmg;
  }
  
  @Override
public void setCustomDamage(ItemStack stack, int damage)
  {
    NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
    nbt.setInteger("advDmg", 0);
    
    int maxStackDamage = stack.getMaxDamage();
    if (maxStackDamage > 2) {
      //stack.setItemDamage(1 + (int)Util.map(damage, this.maxDmg, maxStackDamage - 2));
    }
  }
  
  @Override
public boolean applyCustomDamage(ItemStack stack, int damage, EntityLivingBase src)
  {
    setCustomDamage(stack, getCustomDamage(stack) + damage);
    return true;
  }
}
