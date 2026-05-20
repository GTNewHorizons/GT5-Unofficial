package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.ItemSlotGridBuilder;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchElementalDataOrbHolder;

public class MTEHatchElementalDataOrbHolderGui extends MTEHatchBaseGui<MTEHatchElementalDataOrbHolder> {

    public MTEHatchElementalDataOrbHolderGui(MTEHatchElementalDataOrbHolder hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(4)
                .filter(itemStack -> ItemList.Tool_DataOrb.isStackEqual(itemStack, false, true))
                .slotModifier(itemSlot -> itemSlot.backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_DATA_ORB))
                .build()
                .center());
    }

    @Override
    protected boolean doesAddCircuitSlot() {
        return true;
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
