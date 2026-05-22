package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.item.Item;

public class BaseItemWithDamageValue extends Item {

    public BaseItemWithDamageValue(final String unlocalizedName) {
        this.setUnlocalizedName(unlocalizedName);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setMaxStackSize(1);
        this.setMaxDamage(100);
    }

}
