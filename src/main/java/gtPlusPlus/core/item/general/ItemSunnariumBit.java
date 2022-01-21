package gtPlusPlus.core.item.general;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumChatFormatting;

public class ItemSunnariumBit extends CoreItem {

	public ItemSunnariumBit() {
		super("SunnariumBit", "Sunnarium Bit", AddToCreativeTab.tabMisc, 64, 0, new String[] {}, EnumRarity.uncommon, EnumChatFormatting.GOLD, false, null);
		this.setTextureName(CORE.MODID+":"+"itemSunnariumBit");
	}

}
