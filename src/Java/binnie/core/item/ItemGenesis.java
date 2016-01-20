package binnie.core.item;

import binnie.core.BinnieCore;
import binnie.core.gui.BinnieCoreGUI;
import binnie.core.proxy.BinnieProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.core.Tabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGenesis
  extends Item
{
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    this.itemIcon = BinnieCore.proxy.getIcon(register, "genesis");
  }
  
  public ItemGenesis()
  {
    setCreativeTab(Tabs.tabApiculture);
    setUnlocalizedName("genesis");
    setMaxStackSize(1);
  }
  
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
  {
    BinnieCore.proxy.openGui(BinnieCoreGUI.Genesis, player, (int)player.posX, (int)player.posY, (int)player.posZ);
    
    return itemstack;
  }
  
  public String getItemStackDisplayName(ItemStack i)
  {
    return "Genesis";
  }
}
