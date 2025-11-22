package gregtech.common.misc;

import net.minecraft.item.ItemStack;

import gregtech.common.items.armor.MechArmorBase;
import ic2.core.item.ElectricItemManager;

public class NoTooltipElectricItemManager extends ElectricItemManager {

    public static final NoTooltipElectricItemManager INSTANCE = new NoTooltipElectricItemManager();

    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }
}
