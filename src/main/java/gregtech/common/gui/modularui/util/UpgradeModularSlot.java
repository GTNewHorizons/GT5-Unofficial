package gregtech.common.gui.modularui.util;

import java.util.function.BiFunction;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.tileentities.machines.basic.MTEIndustrialApiary;

/**
 * Used by the IApiary upgrade slots
 */
public class UpgradeModularSlot extends ModularSlot {

    public UpgradeModularSlot(IItemHandler itemHandler, int index, MTEIndustrialApiary apiary) {
        super(itemHandler, index);

        changeListener((_, _, client, init) -> {
            if (!client && !init) {
                apiary.getBaseMetaTileEntity()
                    .markInventoryBeenModified();
                apiary.updateModifiers();
            }
        });
    }

    public static BiFunction<IItemHandler, Integer, UpgradeModularSlot> supplier(MTEIndustrialApiary apiary) {
        return (itemHandler, index) -> new UpgradeModularSlot(itemHandler, index, apiary);
    }
}
