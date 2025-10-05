package gregtech.common.covers.gui;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverArm;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverArmGui extends CoverGui<CoverArm> {

    public CoverArmGui(CoverArm cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {

        column.child(
            makeRowLayout().child(positionRow(createTransferModeRow(syncManager)))
                .child(positionRow(createInternalSlotRow()))
                .child(positionRow(createExternalSlotRow())));
    }

    private Flow createTransferModeRow(PanelSyncManager syncManager) {
        BooleanSyncValue isExportSyncer = new BooleanSyncValue(cover::isExport, cover::setExport);
        syncManager.syncValue("isExport", isExportSyncer);
        return Flow.row()
            .child(createExportButton(isExportSyncer))
            .child(createImportButton(isExportSyncer))
            .child(createTransferModeLabel());
    }

    private IWidget createExportButton(BooleanSyncValue isExportSyncer) {
        return new SelectButton().value(LinkedBoolValue.of(isExportSyncer, true))
            .size(16)
            .marginRight(2)
            .overlay(GTGuiTextures.OVERLAY_ROBOT_ARM_EXPORT);
    }

    private IWidget createImportButton(BooleanSyncValue isExportSyncer) {
        return new SelectButton().value(LinkedBoolValue.of(isExportSyncer, false))
            .size(16)
            .marginRight(2)
            .overlay(GTGuiTextures.OVERLAY_ROBOT_ARM_IMPORT);
    }

    private IWidget createTransferModeLabel() {
        return IKey
            .dynamic(
                () -> cover.isExport() ? StatCollector.translateToLocal("gt.interact.desc.export")
                    : StatCollector.translateToLocal("gt.interact.desc.import"))
            .asWidget();
    }

    private Flow createInternalSlotRow() {
        return Flow.row();
    }

    private Flow createExternalSlotRow() {
        return Flow.row();
    }

}
