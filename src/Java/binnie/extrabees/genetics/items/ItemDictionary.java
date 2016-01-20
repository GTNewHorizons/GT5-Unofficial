package binnie.extrabees.genetics.items;

import binnie.extrabees.ExtraBees;
import binnie.extrabees.core.ExtraBeeGUID;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemDictionary
  extends Item
{
  IIcon iconMaster;
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    this.itemIcon = ExtraBees.proxy.getIcon(register, "apiaristDatabase");
    this.iconMaster = ExtraBees.proxy.getIcon(register, "masterApiaristDatabase");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int par1)
  {
    return par1 == 0 ? this.itemIcon : this.iconMaster;
  }
  
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
    super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    if (par1ItemStack.getItemDamage() > 0) {
      par3List.add("Flora-in-a-box");
    }
  }
  
  @SideOnly(Side.CLIENT)
  public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
  {
    super.getSubItems(par1, par2CreativeTabs, par3List);
    par3List.add(new ItemStack(par1, 1, 1));
  }
  
  public ItemDictionary()
  {
    setCreativeTab(Tabs.tabApiculture);
    setUnlocalizedName("dictionary");
    setMaxStackSize(1);
  }
  
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
  {
    if (itemstack.getItemDamage() == 0) {
      ExtraBees.proxy.openGui(ExtraBeeGUID.Database, player, (int)player.posX, (int)player.posY, (int)player.posZ);
    } else {
      ExtraBees.proxy.openGui(ExtraBeeGUID.DatabaseNEI, player, (int)player.posX, (int)player.posY, (int)player.posZ);
    }
    return itemstack;
  }
  
  public String getItemStackDisplayName(ItemStack i)
  {
    return i.getItemDamage() == 0 ? "Apiarist Database" : "Master Apiarist Database";
  }
}
