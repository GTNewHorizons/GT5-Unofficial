package binnie.extrabees.genetics.items;

import binnie.extrabees.ExtraBees;
import binnie.extrabees.core.ExtraBeeGUID;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemPunnettSquare
  extends Item
{
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    this.itemIcon = ExtraBees.proxy.getIcon(register, "");
  }
  
  public ItemPunnettSquare()
  {
    setCreativeTab(CreativeTabs.tabTools);
    setMaxStackSize(1);
  }
  
  public String getItemStackDisplayName(ItemStack itemstack)
  {
    return "Punnett Square";
  }
  
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
  {
    ExtraBees.proxy.openGui(ExtraBeeGUID.PunnettSquare, player, (int)player.posX, (int)player.posY, (int)player.posZ);
    
    return itemstack;
  }
}
