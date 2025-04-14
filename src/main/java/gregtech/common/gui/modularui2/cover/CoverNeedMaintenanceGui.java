package gregtech.common.gui.modularui2.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverNeedMaintainance;
import gregtech.common.covers.conditions.MaintenanceAlertCondition;
import gregtech.common.covers.modes.RedstoneMode;
import gregtech.common.gui.modularui2.FlowActions;
import gregtech.common.gui.modularui2.LinkedBoolValue;
import gregtech.common.gui.modularui2.widgets.SelectButton;

public class CoverNeedMaintenanceGui extends CoverGui<CoverNeedMaintainance> {

    @Override
    protected String getGuiId() {
        return "cover.maintenance";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        CoverNeedMaintainance cover = getCover(guiData);
        EnumSyncValue<MaintenanceAlertCondition> thresholdSyncValue = new EnumSyncValue<>(
            MaintenanceAlertCondition.class,
            cover::getMaintenanceAlertCondition,
            cover::setMaintenanceAlertCondition);
        syncManager.syncValue("threshold", thresholdSyncValue);
        EnumSyncValue<RedstoneMode> redstoneModeSyncValue = new EnumSyncValue<>(
            RedstoneMode.class,
            cover::getRedstoneMode,
            mode -> {
                cover.setRedstoneMode(mode);
                FlowActions.resize(column);
            });

        column.child(
            Flow.row()
                .marginLeft(WIDGET_MARGIN)
                .coverChildrenHeight()
                .width(getGUIWidth() - WIDGET_MARGIN * 4)
                .mainAxisAlignment(Alignment.MainAxis.SPACE_BETWEEN)
                .child(
                    new Grid().coverChildren()
                        .minElementMargin(1, 1)
                        .alignment(Alignment.CenterLeft)
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUE_1))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("056", "Emit if 1 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("247", "1 Issue"))
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_2))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("058", "Emit if 2 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("248", "2 Issues"))
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_3))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("060", "Emit if 3 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("249", "3 Issues"))
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_4))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("062", "Emit if 4 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("250", "4 Issues"))
                                .asWidget()))
                .child(
                    new Grid().coverChildren()
                        .minElementMargin(1, 1)
                        .alignment(Alignment.CenterLeft)
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_5))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.str(GTUtility.trans("064", "Emit if 5 Maintenance Needed")))
                                .size(16),
                            IKey.str(GTUtility.trans("251", "5 Issues"))
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ROTOR_20))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(
                                    IKey.str(
                                        GTUtility.trans("066", "Emit if rotor needs maintenance low accuracy mod")))
                                .size(16),
                            IKey.str(GTUtility.trans("252", "Rotor < 20%"))
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ROTOR_0))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(
                                    IKey.str(
                                        GTUtility.trans("068", "Emit if rotor needs maintenance high accuracy mod")))
                                .size(16),
                            IKey.str(GTUtility.trans("253", "Rotor ≈ 0%"))
                                .asWidget())
                        .row(
                            new CycleButtonWidget().value(redstoneModeSyncValue)
                                .stateOverlay(RedstoneMode.NORMAL, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                .stateOverlay(RedstoneMode.INVERTED, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                .size(16),
                            IKey.dynamic(
                                () -> redstoneModeSyncValue.getValue() == RedstoneMode.NORMAL
                                    ? GTUtility.trans("NORMAL", "Normal")
                                    : GTUtility.trans("INVERTED", "Inverted"))
                                .asWidget())));
    }
}
