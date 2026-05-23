package gregtech.common.gui.modularui.hatch;

import static tectech.thing.metaTileEntity.hatch.MTEHatchCapacitor.componentBinds;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.gui.modularui.util.CapacitorSlot;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import tectech.thing.metaTileEntity.hatch.MTEHatchCapacitor;
import tectech.util.TTUtility;

public class MTEHatchCapacitorGui extends MTEHatchBaseGui<MTEHatchCapacitor> {

    public MTEHatchCapacitorGui(MTEHatchCapacitor hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue isActive = syncManager.findSyncHandler("isActive", BooleanSyncValue.class);

        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(4)
                .slotGroupKey("capacitor_inventory")
                .filter(
                    itemStack -> !isActive.getBoolValue()
                        && componentBinds.containsKey(TTUtility.getUniqueIdentifier(itemStack)))
                .itemSlotSupplier(() -> new ItemSlot().backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CHARGER))
                .modularSlotSupplier(CapacitorSlot.supplier(isActive))
                .build()
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("isActive", new BooleanSyncValue(baseMetaTileEntity::isActive));
    }
}
