package gtPlusPlus.xmod.gregtech.api.gui.widget;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.items.MetaGeneratedTool;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ElectricSlotWidget extends SlotWidget {

    public ElectricSlotWidget(IItemHandlerModifiable handler, int index) {
        this(new BaseSlot(handler, index, false) {

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
    }

    private ElectricSlotWidget(BaseSlot slot) {
        super(slot);
        setFilter(
            stack -> (accepts(stack)) || (stack.getItem() instanceof MetaGeneratedTool)
                || (stack.getItem() instanceof IElectricItem));
    }

    private boolean accepts(final ItemStack stack) {
        if (stack == null) {
            return false;
        }
        return (Info.itemEnergy.getEnergyValue(stack) > 0.0D)
            || (ElectricItem.manager.discharge(stack, (1.0D / 0.0D), 4, true, true, true) > 0.0D);
    }
}
