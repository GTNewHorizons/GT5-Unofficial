package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.metatileentity.implementations.MTEBasicBatteryBuffer;
import gregtech.common.gui.modularui.singleblock.base.MTETieredMachineBlockBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;

public class MTEBasicBatteryBufferGui extends MTETieredMachineBlockBaseGui<MTEBasicBatteryBuffer> {

    public MTEBasicBatteryBufferGui(MTEBasicBatteryBuffer machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(
            new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(getDimension())
                .build()
                .center());
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }

    private int getDimension() {
        return (int) Math.floor(Math.sqrt(machine.mInventory.length));
    }
}
