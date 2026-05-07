package gregtech.common.gui.modularui.util;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.UpOrDown;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.PhantomItemSlot;

import gregtech.api.modularui2.GTGuiTextures;

public class StockingSlot extends PhantomItemSlot {

    private final BooleanSyncValue isLocked;

    public StockingSlot(BooleanSyncValue isLocked) {
        this.isLocked = isLocked;

        itemTooltip().tooltipBuilder(
            tooltip -> tooltip.addLine(
                IKey.dynamic(
                    () -> translateToLocal(
                        isLocked.getBoolValue() ? "GT5U.machines.stocking_bus.cannot_set_slot"
                            : "modularui.phantom.single.clear"))));
        tooltipDynamic(tooltip -> {
            if (isLocked.getBoolValue())
                tooltip.addLine(IKey.dynamic(() -> translateToLocal("GT5U.machines.stocking_bus.cannot_set_slot")));
        });
        background(
            new DynamicDrawable(() -> isLocked.getBoolValue() ? GTGuiTextures.SLOT_ITEM_DARK : GuiTextures.SLOT_ITEM),
            GTGuiTextures.OVERLAY_SLOT_ARROW_ME);
    }

    @Override
    public boolean onMouseScroll(UpOrDown scrollDirection, int amount) {
        return false;
    }

    @Override
    public PhantomItemSlot slot(ModularSlot slot) {
        return syncHandler(new PhantomItemSlotSH(slot) {

            @Override
            protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
                if (isLocked.getBoolValue()) return;
                super.phantomClick(mouseData, cursorStack);
            }
        });
    }
}
