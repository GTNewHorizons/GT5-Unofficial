package gtPlusPlus.core.item.effects;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RarityRare extends Item {

	public RarityRare(){
		super();
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		return EnumRarity.rare;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		return true;
	}

}
