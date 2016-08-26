package miscutil.xmod.ic2.item;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IC2_ItemIC2
extends Item
{
	public IC2_ItemIC2(String internalName)
	{
		setUnlocalizedName(internalName);
		setCreativeTab(AddToCreativeTab.tabMachines);
		setTextureName(CORE.MODID + ":" + internalName);

		GameRegistry.registerItem(this, internalName);
	}

	public String getTextureFolder()
	{
		return null;
	}

	/*  public String getTextureName(int index)
  {
    if ((!this.hasSubtypes) && (index > 0)) {
      return null;
    }
    String name = getUnlocalizedName(new ItemStack(this, 1, index));
    if ((name != null) && (name.length() > 4)) {
      return name.substring(4);
    }
    return name;
  }

  @Override
@SideOnly(Side.CLIENT)
  public void registerIcons(IIconRegister iconRegister)
  {
    int indexCount = 0;
    while (getTextureName(indexCount) != null)
    {
      indexCount++;
      if (indexCount > 32767) {
        throw new RuntimeException("More Item Icons than actually possible @ " + getUnlocalizedName());
      }
    }
    this.textures = new IIcon[indexCount];
    for (int index = 0; index < indexCount; index++) {
      this.textures[index] = iconRegister.registerIcon(CORE.MODID + ":" + getUnlocalizedName());
    }
  }

  @Override
@SideOnly(Side.CLIENT)
  public IIcon getIconFromDamage(int meta)
  {
    if (meta < this.textures.length) {
      return this.textures[meta];
    }
    return this.textures.length < 1 ? null : this.textures[0];
  }*/

	@Override
	public String getUnlocalizedName()
	{
		return super.getUnlocalizedName();
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return getUnlocalizedName();
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack)
	{
		return StatCollector.translateToLocal(getUnlocalizedName(itemStack));
	}

	public IC2_ItemIC2 setRarity(int aRarity)
	{
		this.rarity = aRarity;
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.values()[this.rarity];
	}

	private int rarity = 0;
	protected IIcon[] textures;
}
