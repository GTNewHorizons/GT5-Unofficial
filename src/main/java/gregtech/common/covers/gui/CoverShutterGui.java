package gregtech.common.covers.gui;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverShutter;
import gregtech.common.covers.modes.ShutterMode;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverShutterGui extends CoverGui<CoverShutter> {

    public CoverShutterGui(CoverShutter cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.shutter";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<ShutterMode> modeSyncValue = new EnumSyncValue<>(
            ShutterMode.class,
            cover::getShutterMode,
            cover::setShutterMode);
        syncManager.syncValue("mode", modeSyncValue);

        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, ShutterMode.OPEN_IF_ENABLED))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("082", "Open if work enabled"))
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, ShutterMode.OPEN_IF_DISABLED))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("083", "Open if work disabled"))
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, ShutterMode.ONLY_OUTPUT))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("084", "Only Output allowed"))
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, ShutterMode.ONLY_INPUT))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .size(16),
                    IKey.str(GTUtility.trans("085", "Only Input allowed"))
                        .asWidget()));
    }

}
