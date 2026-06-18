package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.pcb.MTEPCBUpgradeBase;

public class MTEPCBUpgradeBaseGui extends MTEMultiBlockBaseGui<MTEPCBUpgradeBase<?>> {

    public MTEPCBUpgradeBaseGui(MTEPCBUpgradeBase<?> multiblock) {
        super(multiblock);
    }

    @Override
    protected IWidget createStructureErrorWidget(PanelSyncManager syncManager) {
        GenericListSyncHandler<StructureError> errors = syncManager
            .findSyncHandler("structureErrors", GenericListSyncHandler.class);

        DynamicSyncHandler errorSyncer = new DynamicSyncHandler().widgetProvider((syncManager1, packet) -> {
            Flow columns = Flow.column()
                .coverChildrenHeight(0)
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .childPadding(1);

            for (StructureError error : errors.getValue()) {
                if (error == StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR) continue;
                columns.child(error.createWidget(this));
            }
            return columns.setEnabledIf(widget -> !baseMetaTileEntity.isActive());
        })
            .allowC2S();

        errors.setChangeListener(() -> errorSyncer.notifyUpdate(packet -> {}));

        return new DynamicSyncedWidget<>().widthRel(0.85f)
            .coverChildrenHeight(0)
            .syncHandler(errorSyncer);
    }
}
