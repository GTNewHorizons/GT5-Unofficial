package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchEnergyGui extends MTEHatchBaseGui<MTEHatchEnergy> {

    public MTEHatchEnergyGui(MTEHatchEnergy hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> parent = super.createContentSection(panel, syncManager);

        parent.child(
            GTGuiTextures.PICTURE_ENERGY_HATCH_FUSE_BOX.asWidget()
                .size(160, 76)
                .center());
        parent.child(
            new ItemSlot()
                .slot(new ModularSlot(machine.inventoryHandler, MTEHatchEnergy.FUSE_SLOT).singletonSlotGroup())
                .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_FUSE)
                .center());

        return parent;
    }
}
