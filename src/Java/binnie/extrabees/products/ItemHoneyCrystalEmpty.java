package binnie.extrabees.products;

import binnie.extrabees.ExtraBees;
import binnie.extrabees.proxy.ExtraBeesProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class ItemHoneyCrystalEmpty
  extends ItemHoneyCrystal
{
  public ItemHoneyCrystalEmpty()
  {
    setMaxDamage(0);
    setMaxStackSize(64);
    setUnlocalizedName("honeyCrystalEmpty");
  }
  
  @SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister register)
  {
    this.itemIcon = ExtraBees.proxy.getIcon(register, "honeyCrystalEmpty");
  }
  
  public String getItemStackDisplayName(ItemStack i)
  {
    return ExtraBees.proxy.localise("item.honeycrystal.empty");
  }
}
