package gregtech.common.gui.modularui.cover.redstone;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.covers.redstone.CoverWirelessMaintenanceDetector;
import gregtech.common.gui.modularui.cover.base.CoverAdvancedRedstoneTransmitterBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverWirelessMaintenenceDetectorGui
    extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessMaintenanceDetector> {

    public CoverWirelessMaintenenceDetectorGui(CoverAdvancedRedstoneTransmitterBase cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<CoverWirelessMaintenanceDetector.MaintenanceMode> maintenanceModeSync = new EnumSyncValue<>(
            CoverWirelessMaintenanceDetector.MaintenanceMode.class,
            cover::getMode,
            cover::setMode);
        syncManager.syncValue("maintenanceMode", maintenanceModeSync);
        super.addUIWidgets(syncManager, column, data);
    }

    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        // column contains 4 other rows, each has 2 enum values
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        EnumSyncValue<CoverWirelessMaintenanceDetector.MaintenanceMode> maintenanceSync = (EnumSyncValue<CoverWirelessMaintenanceDetector.MaintenanceMode>) syncManager
            .getSyncHandlerFromMapKey("maintenanceMode:0");
        final ICoverable tile = data.getCoverable();
        boolean usesTurbines = false;

        if (!tile.isDead() && tile instanceof IGregTechTileEntity gTE && gTE.getMetaTileEntity() != null) {
            IMetaTileEntity metaTE = (gTE.getMetaTileEntity());
            if (metaTE instanceof MTEMultiBlockBase multiTE) {
                usesTurbines = multiTE.usesTurbines();
            }
        }
        return Flow.column()
            .coverChildren()
            .child(
                makeSyncedBoolRow(
                    maintenanceSync,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.NO_ISSUE,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.ONE_ISSUE))
            .child(
                makeSyncedBoolRow(
                    maintenanceSync,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.TWO_ISSUES,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.THREE_ISSUES))
            .child(
                makeSyncedBoolRow(
                    maintenanceSync,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.FOUR_ISSUES,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.FIVE_ISSUES))
            .childIf(
                usesTurbines, ()->
                makeSyncedBoolRow(
                    maintenanceSync,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.ROTOR_80,
                    CoverWirelessMaintenanceDetector.MaintenanceMode.ROTOR_100))
            .child(physicalRow(physicalSyncer));
    }

    protected Flow makeSyncedBoolRow(EnumSyncValue syncValue, CoverWirelessMaintenanceDetector.MaintenanceMode value1,
        CoverWirelessMaintenanceDetector.MaintenanceMode value2) {
        return Flow.row()
            .coverChildren()
            .child(makeMaintanenceIssueRow(syncValue, value1).marginRight(8))
            .child(makeMaintanenceIssueRow(syncValue, value2))
            .marginBottom(4);
    }

    private Flow makeMaintanenceIssueRow(EnumSyncValue syncValue,
        CoverWirelessMaintenanceDetector.MaintenanceMode value) {
        return new Row().size(90, 18)
            .child(
                new SelectButton().value(LinkedBoolValue.of(syncValue, value))
                    .size(16)
                    .marginRight(2)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK))
            .child(new TextWidget(StatCollector.translateToLocal(value.getDescriptorKey())));
    }

    @Override
    protected int getGUIHeight() {
        return 180;
    }
}
