package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTEExothermicHearth;

public class MTEExothermicHearthGui extends MTEMultiBlockBaseGui<MTEExothermicHearth> {

    public MTEExothermicHearthGui(MTEExothermicHearth multiblock) {
        super(multiblock);
    }

    // override to put machine mode icon on right panel instead of left
    @Override
    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildrenWidth()
            .fullHeight()
            .childIf(shouldDisplayVoidExcess(), () -> createVoidExcessButton(syncManager))
            .childIf(shouldDisplayInputSeparation(), () -> createInputSeparationButton(syncManager))
            .childIf(shouldDisplayBatchMode(), () -> createBatchModeButton(syncManager))
            .childIf(shouldDisplayRecipeLock(), () -> createLockToSingleRecipeButton(syncManager));
    }

    @Override
    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return super.createRightPanelGapRow(parent, syncManager).child(createModeSwitchButton(syncManager));

    }
}
