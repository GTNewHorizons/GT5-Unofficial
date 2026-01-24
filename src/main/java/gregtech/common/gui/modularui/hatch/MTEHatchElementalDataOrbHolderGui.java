package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchElementalDataOrbHolder;

public class MTEHatchElementalDataOrbHolderGui extends MTEHatchBaseGui<MTEHatchElementalDataOrbHolder> {

    public MTEHatchElementalDataOrbHolderGui(MTEHatchElementalDataOrbHolder hatch) {
        super(hatch);
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {

        syncManager.registerSlotGroup("item_inv", 4);
        return super.createContentHolderRow(panel, syncManager).child(
            SlotGroupWidget.builder()
                .matrix("xxxx", "xxxx", "xxxx", "xxxx")
                .key(
                    'x',
                    i -> new ItemSlot()
                        .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_DATA_ORB)
                        .slot(
                            new ModularSlot(hatch.inventoryHandler, i).slotGroup("item_inv")
                                .filter(stack -> ItemList.Tool_DataOrb.isStackEqual(stack, false, true)))

                )
                .build()
                .align(Alignment.CENTER));
    }

    @Override
    protected boolean doesAddCircuitSlot() {
        return true;
    }
}
