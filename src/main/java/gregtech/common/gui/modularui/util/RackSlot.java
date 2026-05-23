package gregtech.common.gui.modularui.util;

import java.util.function.BiFunction;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.utils.item.IItemHandler;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

public class RackSlot extends ModularSlot {

    private final BooleanSyncValue isActiveSyncer;
    private final IntSyncValue heatSyncer;

    public RackSlot(IItemHandler itemHandler, int index, BooleanSyncValue isActiveSyncer, IntSyncValue heatSyncer) {
        super(itemHandler, index);
        this.isActiveSyncer = isActiveSyncer;
        this.heatSyncer = heatSyncer;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return !isActiveSyncer.getBoolValue() && heatSyncer.getIntValue() <= 0 && super.canTakeStack(playerIn);
    }

    public static BiFunction<IItemHandler, Integer, RackSlot> supplier(BooleanSyncValue isActiveSyncer,
        IntSyncValue heatSyncer) {
        return (itemHandler, index) -> new RackSlot(itemHandler, index, isActiveSyncer, heatSyncer);
    }
}
