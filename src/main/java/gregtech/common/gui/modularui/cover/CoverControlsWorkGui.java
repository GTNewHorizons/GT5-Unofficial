package gregtech.common.gui.modularui.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverControlsWork;
import gregtech.common.covers.conditions.RedstoneCondition;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverControlsWorkGui extends CoverBaseGui<CoverControlsWork> {

    public CoverControlsWorkGui(CoverControlsWork cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.machine_controller";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        EnumSyncValue<RedstoneCondition> conditionModeSyncValue = new EnumSyncValue<>(
            RedstoneCondition.class,
            cover::getRedstoneCondition,
            cover::setRedstoneCondition);
        syncManager.syncValue("condition_mode", conditionModeSyncValue);
        BooleanSyncValue safeModeSyncValue = new BooleanSyncValue(cover::isSafeMode, cover::setSafeMode);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new SelectButton()
                        .value(LinkedBoolValue.of(conditionModeSyncValue, RedstoneCondition.ENABLE_WITH_REDSTONE))
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                        .size(16),
                    IKey.lang("gt.interact.desc.MachContr.EnRedstone")
                        .asWidget())
                .row(
                    new SelectButton()
                        .value(LinkedBoolValue.of(conditionModeSyncValue, RedstoneCondition.DISABLE_WITH_REDSTONE))
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                        .size(16),
                    IKey.lang("gt.interact.desc.MachContr.DisRedstone")
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(conditionModeSyncValue, RedstoneCondition.DISABLE))
                        .overlay(GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.lang("gt.interact.desc.MachContr.DisMachine")
                        .asWidget())
                .row(
                    new ToggleButton().value(safeModeSyncValue)
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.lang("gt.interact.desc.MachContr.SafeMode")
                        .asWidget()));
    }
}
