package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
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
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        syncManager.registerSlotGroup("item_inv", 4);

        return super.createContentSection(panel, syncManager).child(
            SlotGroupWidget.builder()
                .matrix("xxxx", "xxxx", "xxxx", "xxxx")
                .key(
                    'x',
                    i -> new ItemSlot().slot(
                        new ModularSlot(hatch.inventoryHandler, i).slotGroup("item_inv")
                            .filter(stack -> ItemList.Tool_DataOrb.isStackEqual(stack, false, true)))
                        .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_DATA_ORB)

                )
                .build()
                .center());
    }

    @Override
    protected boolean doesAddCircuitSlot() {
        return true;
    }
}
