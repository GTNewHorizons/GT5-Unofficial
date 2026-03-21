package gregtech.common.gui.modularui.singleblock.base;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;

// For singleblock special (non-MTEBasicMachine) machine guis
public abstract class MTESpecialMachineBlockBaseGui<T extends MTETieredMachineBlock>
    extends MTETieredMachineBlockBaseGui<T> {

    public MTESpecialMachineBlockBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected boolean supportsMuffler() {
        return false;
    }

    @Override
    protected Flow createContentHolderRow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentHolderRow(panel, syncManager).paddingBottom(0);
    }

    @Override
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightCornerFlow(panel, syncManager).paddingBottom(2)
            .child(createLogo());
    }
}
