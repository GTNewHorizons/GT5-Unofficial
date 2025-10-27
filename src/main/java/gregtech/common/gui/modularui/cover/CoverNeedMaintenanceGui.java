package gregtech.common.gui.modularui.cover;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverNeedMaintainance;
import gregtech.common.covers.conditions.MaintenanceAlertCondition;
import gregtech.common.covers.modes.RedstoneMode;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverNeedMaintenanceGui extends CoverBaseGui<CoverNeedMaintainance> {

    public CoverNeedMaintenanceGui(CoverNeedMaintainance cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.maintenance";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<MaintenanceAlertCondition> thresholdSyncValue = new EnumSyncValue<>(
            MaintenanceAlertCondition.class,
            cover::getMaintenanceAlertCondition,
            cover::setMaintenanceAlertCondition);
        syncManager.syncValue("threshold", thresholdSyncValue);
        EnumSyncValue<RedstoneMode> redstoneModeSyncValue = new EnumSyncValue<>(
            RedstoneMode.class,
            cover::getRedstoneMode,
            cover::setRedstoneMode);

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
                                .addTooltipLine(IKey.lang("gt.interact.desc.need_maint_count", "1", ""))
                                .size(16),
                            IKey.lang("gt.interact.desc.issue")
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_2))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.lang("gt.interact.desc.need_maint_count", "2", ""))
                                .size(16),
                            IKey.lang("gt.interact.desc.issues", "2")
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_3))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.lang("gt.interact.desc.need_maint_count", "3", ""))
                                .size(16),
                            IKey.lang("gt.interact.desc.issues", "3")
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_4))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.lang("gt.interact.desc.need_maint_count", "4", ""))
                                .size(16),
                            IKey.lang("gt.interact.desc.issues", "4")
                                .asWidget()))
                .child(
                    new Grid().coverChildren()
                        .minElementMargin(1, 1)
                        .alignment(Alignment.CenterLeft)
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ISSUES_5))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.lang("gt.interact.desc.need_maint_count", "5", ""))
                                .size(16),
                            IKey.lang("gt.interact.desc.issues", "5")
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ROTOR_20))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.lang("gt.interact.desc.need_maint_rotor_lo"))
                                .size(16),
                            IKey.lang("gt.interact.desc.issue_rotor_low")
                                .asWidget())
                        .row(
                            new SelectButton()
                                .value(LinkedBoolValue.of(thresholdSyncValue, MaintenanceAlertCondition.ROTOR_0))
                                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                                .addTooltipLine(IKey.lang("gt.interact.desc.need_maint_rotor_hi"))
                                .size(16),
                            IKey.lang("gt.interact.desc.issue_rotor_dead")
                                .asWidget())
                        .row(
                            new CycleButtonWidget().value(redstoneModeSyncValue)
                                .stateOverlay(RedstoneMode.NORMAL, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                .stateOverlay(RedstoneMode.INVERTED, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                                .size(16),
                            IKey.dynamic(
                                () -> redstoneModeSyncValue.getValue() == RedstoneMode.NORMAL
                                    ? translateToLocal("gt.interact.desc.normal")
                                    : translateToLocal("gt.interact.desc.inverted"))
                                .asWidget())));
    }
}
