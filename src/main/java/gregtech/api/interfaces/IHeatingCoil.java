package gregtech.api.interfaces;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.HeatingCoilLevel;

public interface IHeatingCoil {

    HeatingCoilLevel getCoilHeat(int meta);

    default HeatingCoilLevel getCoilHeat(ItemStack stack) {
        return getCoilHeat(stack.getItemDamage());
    }
}
