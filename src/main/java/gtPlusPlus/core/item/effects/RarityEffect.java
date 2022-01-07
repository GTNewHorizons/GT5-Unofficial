package gtPlusPlus.core.item.effects;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 *
This determines the name colour. EnumRarity can be:
EnumRarity.common - the standard white colour.
EnumRarity.uncommon - a yellow colour.
EnumRarity.rare - a light blue colour. This is used for enchanted items.
EnumRarity.epic - the purple colour used on the Golden Apple.
@SideOnly is an FML annotation. It marks the method below it for existing only on one side. Possible values are:
Side.CLIENT is probably the most common one. This marks the method as existing only on the client side.
Side.SERVER marks the method as existing only on the server side.
 *
 */

public class RarityEffect extends Item {

	public RarityEffect(final int par1){
		super();
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return EnumRarity.common;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		return true;
	}

}
