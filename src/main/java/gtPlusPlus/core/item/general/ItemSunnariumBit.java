package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumChatFormatting;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;

public class ItemSunnariumBit extends CoreItem {

    public ItemSunnariumBit() {
        super(
                "SunnariumBit",
                "Sunnarium Bit",
                AddToCreativeTab.tabMisc,
                64,
                0,
                new String[] {},
                EnumRarity.uncommon,
                EnumChatFormatting.GOLD,
                false,
                null);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemSunnariumBit");
    }
}
