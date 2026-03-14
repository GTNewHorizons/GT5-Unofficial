package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.StringUtils;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchInputBattery;

public class MTEHatchInputBatteryGui extends MTEHatchBaseGui<MTEHatchInputBattery> {

    public MTEHatchInputBatteryGui(MTEHatchInputBattery hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentHolderRow(panel, syncManager).child(createSlots(syncManager));
    }

    protected SlotGroupWidget createSlots(PanelSyncManager syncManager) {
        final int dimension = this.getDimension();
        syncManager.registerSlotGroup("battery_inv", dimension);

        String[] matrix = new String[dimension];
        String repeat = StringUtils.getRepetitionOf('x', dimension);
        Arrays.fill(matrix, repeat);
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                'x',
                i -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, i).slotGroup("battery_inv"))
                    .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_CHARGER))
            .build()
            .coverChildren()
            .align(Alignment.Center);
    }

    // just in case any subclasses want to override this
    // value corresponds to the size of any side of the slot group grid
    protected int getDimension() {
        return hatch.mTier;
    }
}
