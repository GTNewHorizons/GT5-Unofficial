package binnie.core.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public abstract interface IItemMisc
  extends IItemEnum
{
  public abstract IIcon getIcon(ItemStack paramItemStack);
  
  @SideOnly(Side.CLIENT)
  public abstract void registerIcons(IIconRegister paramIIconRegister);
  
  public abstract void addInformation(List paramList);
}
