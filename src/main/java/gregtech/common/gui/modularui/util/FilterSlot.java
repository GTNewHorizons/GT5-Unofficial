package gregtech.common.gui.modularui.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.item.PhantomSingleSlotItemStackHandler;

public class FilterSlot extends PhantomItemSlot {

    public FilterSlot() {
        itemTooltip().tooltipBuilder(tooltip -> tooltip.add(IKey.lang("GT5U.bus.filterTooltip.full")));
        tooltip(tooltip -> tooltip.add(IKey.lang("GT5U.bus.filterTooltip.empty")));
        background(GuiTextures.SLOT_ITEM, GTGuiTextures.OVERLAY_SLOT_FILTER);
    }

    public FilterSlot(Supplier<ItemStack> getter, Consumer<ItemStack> setter) {
        slot(new PhantomSingleSlotItemStackHandler(getter, setter), 0);
    }
}
