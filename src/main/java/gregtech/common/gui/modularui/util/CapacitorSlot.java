package gregtech.common.gui.modularui.util;

import java.util.function.BiFunction;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

public class CapacitorSlot extends ModularSlot {

    private final BooleanSyncValue isActiveSyncer;

    public CapacitorSlot(IItemHandler itemHandler, int index, BooleanSyncValue isActiveSyncer) {
        super(itemHandler, index);
        this.isActiveSyncer = isActiveSyncer;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return !isActiveSyncer.getBoolValue();
    }

    public static BiFunction<IItemHandler, Integer, CapacitorSlot> supplier(BooleanSyncValue isActiveSyncer) {
        return (itemHandler, index) -> new CapacitorSlot(itemHandler, index, isActiveSyncer);
    }
}
