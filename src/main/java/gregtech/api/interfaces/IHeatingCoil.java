package gregtech.api.interfaces;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.HeatingCoilLevel;

public interface IHeatingCoil {

    HeatingCoilLevel getCoilHeat(int meta);

    default HeatingCoilLevel getCoilHeat(ItemStack stack) {
        return getCoilHeat(stack.getItemDamage());
    }

    void setOnCoilCheck(Consumer<IHeatingCoil> callback);

    Consumer<IHeatingCoil> getOnCoilCheck();
}
