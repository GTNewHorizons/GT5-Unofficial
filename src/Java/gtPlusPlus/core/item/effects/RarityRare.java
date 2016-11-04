package gtPlusPlus.core.item.effects;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class RarityRare extends Item {

	public RarityRare(final int par1) {
		super();
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack) {
		return EnumRarity.rare;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack) {
		return true;
	}

}
