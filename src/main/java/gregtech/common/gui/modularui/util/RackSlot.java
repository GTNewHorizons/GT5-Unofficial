package gregtech.common.gui.modularui.util;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

public class RackSlot extends ItemSlot {

    private final BooleanSyncValue isActiveSyncer;
    private final IntSyncValue heatSyncer;

    public RackSlot(BooleanSyncValue isActiveSyncer, IntSyncValue heatSyncer) {
        this.isActiveSyncer = isActiveSyncer;
        this.heatSyncer = heatSyncer;
    }

    @Override
    public ItemSlot slot(ModularSlot slot) {
        // create new ModularSlot with restriction on extracting, copy over properties
        ModularSlot newSlot = new ModularSlot(slot.getItemHandler(), slot.getSlotIndex()) {

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return !isActiveSyncer.getBoolValue() && heatSyncer.getIntValue() <= 0 && super.canTakeStack(playerIn);
            }
        }.slotGroup(slot.getSlotGroupName())
            .filter(slot.getFilter());

        return super.slot(newSlot);
    }
}
