package gregtech.common.modularui2.widget;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;

public class LimitingItemStackHandler extends ItemStackHandler implements IItemHandlerModifiable, IItemHandler {

    private final int slotLimit;

    private LimitingItemStackHandler(int slots, int slotLimit) {
        super(slots);
        this.slotLimit = slotLimit;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit;
    }
}
