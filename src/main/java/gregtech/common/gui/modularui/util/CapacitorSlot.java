package gregtech.common.gui.modularui.util;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

public class CapacitorSlot extends ItemSlot {

    private final BooleanSyncValue isActiveSyncer;

    public CapacitorSlot(BooleanSyncValue isActiveSyncer) {
        this.isActiveSyncer = isActiveSyncer;
    }

    @Override
    public ItemSlot slot(ModularSlot slot) {
        // create new ModularSlot with restriction on extracting, copy over properties
        ModularSlot newSlot = new ModularSlot(slot.getItemHandler(), slot.getSlotIndex()) {

            @Override
            public boolean canTakeStack(EntityPlayer playerIn) {
                return !isActiveSyncer.getBoolValue();
            }
        }.slotGroup(slot.getSlotGroupName())
            .filter(slot.getFilter());

        return super.slot(newSlot);
    }
}
