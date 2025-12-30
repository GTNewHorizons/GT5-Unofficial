package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.ItemList;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchDataAccessGUI extends MTEHatchBaseGui<MTEHatchDataAccess> {

    public MTEHatchDataAccessGUI(MTEHatchDataAccess hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        IWidget slots;

        switch (hatch.mTier) {
            case 4:
                slots = createSlotGroup(syncManager, 2);
                break;
            default:
                slots = createSlotGroup(syncManager, 4);
                break;

        }

        return super.createContentHolderRow(panel, syncManager).child(slots);
    }

    private IWidget createSlotGroup(PanelSyncManager syncManager, int dimension) {
        int inventorySize = hatch.inventoryHandler.getSlots();
        int maxDim = (int) Math.floor(Math.sqrt(inventorySize));
        int gridDim = Math.min(dimension, maxDim);
        int slotCount = gridDim * gridDim;

        syncManager.registerSlotGroup("data", slotCount);

        String[] matrix = new String[gridDim];

        StringBuilder rowBuilder = new StringBuilder(gridDim);
        for (int i = 0; i < gridDim; i++) {
            rowBuilder.append('x');
        }
        String row = rowBuilder.toString();

        for (int i = 0; i < gridDim; i++) {
            matrix[i] = row;
        }

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                'x',
                i -> new ItemSlot().background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_CIRCUIT)
                    .slot(
                        new ModularSlot(hatch.inventoryHandler, i).slotGroup("data")
                            .filter((item) -> ItemList.Tool_DataStick.isStackEqual(item, false, true))

                    ))
            .build()
            .align(Alignment.CENTER);
    }

}
