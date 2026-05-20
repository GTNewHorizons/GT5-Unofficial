package gregtech.common.gui.modularui.hatch.base;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;

import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicTankBaseGui;

/**
 * A base class for common hatch implementations. Has configurable corner panels and makes building ui's easier.
 * The main overriding is done in
 * {@link #createContentSection(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager)}
 *
 * For heavily custom UI's, override
 * {@link #build(com.cleanroommc.modularui.factory.PosGuiData, com.cleanroommc.modularui.value.sync.PanelSyncManager, com.cleanroommc.modularui.screen.UISettings)}
 * instead.
 */
public class MTEHatchBaseGui<T extends MTEHatch> extends MTEBasicTankBaseGui<T> {

    public MTEHatchBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        // reset content section to an empty slate
        return supportsBottomRowOverlap() ? getOverlappingEmptyContent() : getEmptyContent();
    }

    @Override
    protected boolean supportsTopRightCornerFlow() {
        return false;
    }
}
