package binnie.extrabees.apiary;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemIndustrialFrame
  extends Item
{
  @SideOnly(Side.CLIENT)
  public boolean requiresMultipleRenderPasses()
  {
    return true;
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
  {
    for (IndustrialFrame frame : )
    {
      ItemStack stack = new ItemStack(this);
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("frame", frame.ordinal());
      stack.setTagCompound(nbt);
      par3List.add(stack);
    }
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
    super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    IndustrialFrame frame = getFrame(par1ItemStack);
    if (frame == null) {
      par3List.add("Invalid Contents");
    } else {
      par3List.add(frame.getName());
    }
  }
  
  public String getItemStackDisplayName(ItemStack par1ItemStack)
  {
    return "Industrial Frame";
  }
  
  public ItemIndustrialFrame()
  {
    setCreativeTab(CreativeTabs.tabMisc);
    setMaxDamage(400);
    setMaxStackSize(1);
    setUnlocalizedName("industrialFrame");
  }
  
  public static IndustrialFrame getFrame(ItemStack stack)
  {
    if ((stack == null) || (!stack.hasTagCompound()) || (!stack.getTagCompound().hasKey("frame"))) {
      return null;
    }
    return IndustrialFrame.values()[stack.getTagCompound().getInteger("frame")];
  }
}
