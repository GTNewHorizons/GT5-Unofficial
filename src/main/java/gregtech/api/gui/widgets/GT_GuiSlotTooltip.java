package gregtech.api.gui.widgets;

import java.awt.Rectangle;

import gregtech.api.util.GT_TooltipDataCache.TooltipData;
import net.minecraft.inventory.Slot;

public class GT_GuiSlotTooltip extends GT_GuiTooltip {
    private final Slot slot;

    public GT_GuiSlotTooltip(Slot slot, TooltipData data) {
        super(new Rectangle(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1, 18, 18), data);
        this.slot = slot;
    }

    @Override
    protected void onTick() {
        super.onTick();
        // If disabled by super, stay disabled.
        this.enabled = this.enabled && this.slot.getStack() == null;
    }
}
