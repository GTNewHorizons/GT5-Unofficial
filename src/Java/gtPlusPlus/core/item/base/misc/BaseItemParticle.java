package gtPlusPlus.core.item.base.misc;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class BaseItemParticle extends CoreItem {

	private final Material mParticleMaterial;
	
	public BaseItemParticle(Material aMat, String aType) {
		super("particle"+aMat.getLocalizedName()+aType, aMat.getLocalizedName()+" "+aType, AddToCreativeTab.tabOther, 64, 0, new String[] {}, EnumRarity.rare, EnumChatFormatting.DARK_AQUA, false, null);
		this.setTextureName(CORE.MODID + ":" + "science/Atom");
		mParticleMaterial = aMat;
	}

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
		return this.mParticleMaterial.getRgbAsHex();
	}
	
}
