package gtPlusPlus.core.item.effects;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RarityRare extends Item {

	public RarityRare(int par1){
		super();
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.rare;
	}
	
	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return true;
	}
	
}
