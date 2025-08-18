package gregtech.common.covers.gui.redstone;

import gregtech.api.modularui2.CoverGuiData;
import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneTransmitterBase;
import gregtech.common.covers.redstone.CoverWirelessMaintenanceDetector;
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
            .getSyncHandler("maintenanceMode:0");
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
            .child(
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
            .child(
                new Row().size(90, 18)
                    .child(
                        new SelectButton().value(LinkedBoolValue.of(syncValue, value1))
                            .size(16)
                            .marginRight(2)
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK))
                    .child(new TextWidget(StatCollector.translateToLocal(value1.getDescriptorKey())))
                    .marginRight(8))
            .child(
                new Row().size(90, 18)
                    .child(
                        new SelectButton().value(LinkedBoolValue.of(syncValue, value2))
                            .size(16)
                            .marginRight(2)
                            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK))
                    .child(new TextWidget(StatCollector.translateToLocal(value2.getDescriptorKey()))))
            .marginBottom(4);
    }

    @Override
    protected int getGUIHeight() {
        return 180;
    }
}
