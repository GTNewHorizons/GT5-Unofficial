package gregtech.common.gui.modularui.cover.redstone;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverWirelessMaintenanceDetector;
import gregtech.common.covers.redstone.CoverWirelessMaintenanceDetector.MaintenanceMode;
import gregtech.common.gui.modularui.cover.base.CoverAdvancedRedstoneTransmitterBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverWirelessMaintenenceDetectorGui
    extends CoverAdvancedRedstoneTransmitterBaseGui<CoverWirelessMaintenanceDetector> {

    public CoverWirelessMaintenenceDetectorGui(CoverWirelessMaintenanceDetector cover) {
        super(cover);
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<MaintenanceMode, ?> maintenanceModeSync = new EnumSyncValue<>(
            MaintenanceMode.class,
            cover::getMode,
            cover::setMode).allowC2S();
        syncManager.syncValue("maintenanceMode", maintenanceModeSync);
        super.addUIWidgets(syncManager, column, data);
    }

    @Override
    protected Flow makeThirdFlow(PanelSyncManager syncManager, CoverGuiData data) {
        // column contains 4 other rows, each has 2 enum values
        BooleanSyncValue physicalSyncer = new BooleanSyncValue(cover::isPhysical, cover::setPhysical);
        @SuppressWarnings("unchecked")
        EnumSyncValue<MaintenanceMode, ?> maintenanceSync = syncManager
            .findSyncHandler("maintenanceMode", EnumSyncValue.class);
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
            .child(makeSyncedBoolRow(maintenanceSync, MaintenanceMode.NO_ISSUE, MaintenanceMode.ONE_ISSUE))
            .child(makeSyncedBoolRow(maintenanceSync, MaintenanceMode.TWO_ISSUES, MaintenanceMode.THREE_ISSUES))
            .child(makeSyncedBoolRow(maintenanceSync, MaintenanceMode.FOUR_ISSUES, MaintenanceMode.FIVE_ISSUES))
            .childIf(
                usesTurbines,
                () -> makeSyncedBoolRow(maintenanceSync, MaintenanceMode.ROTOR_80, MaintenanceMode.ROTOR_100))
            .child(physicalRow(physicalSyncer));
    }

    protected Flow makeSyncedBoolRow(EnumSyncValue<MaintenanceMode, ?> syncValue, MaintenanceMode value1,
        MaintenanceMode value2) {
        return Flow.row()
            .coverChildren()
            .child(makeMaintanenceIssueRow(syncValue, value1).marginRight(8))
            .child(makeMaintanenceIssueRow(syncValue, value2))
            .marginBottom(4);
    }

    private Flow makeMaintanenceIssueRow(EnumSyncValue<MaintenanceMode, ?> syncValue, MaintenanceMode value) {
        return Flow.row()
            .size(90, 18)
            .child(
                new SelectButton().value(LinkedBoolValue.of(syncValue, value))
                    .size(16)
                    .marginRight(2)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK))
            .child(new TextWidget(StatCollector.translateToLocal(value.getDescriptorKey())));
    }
}
