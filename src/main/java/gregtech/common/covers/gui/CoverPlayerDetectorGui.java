package gregtech.common.covers.gui;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverPlayerDetector;
import gregtech.common.covers.modes.PlayerDetectionMode;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverPlayerDetectorGui extends CoverGui<CoverPlayerDetector> {

    public CoverPlayerDetectorGui(CoverPlayerDetector cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.player_detector";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        EnumSyncValue<PlayerDetectionMode> modeSyncValue = new EnumSyncValue<>(
            PlayerDetectionMode.class,
            cover::getPlayerDetectionMode,
            cover::setPlayerDetectionMode);
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
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, PlayerDetectionMode.ANY_PLAYER))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("068.1", "Emit if any Player is close")))
                        .size(16),
                    IKey.str(GTUtility.trans("319", "Any player"))
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, PlayerDetectionMode.OTHER_PLAYERS))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("069.1", "Emit if other Player is close")))
                        .size(16),
                    IKey.str(GTUtility.trans("320", "Other players"))
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, PlayerDetectionMode.ONLY_OWNER))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.str(GTUtility.trans("070", "Emit if you are close")))
                        .size(16),
                    IKey.str(GTUtility.trans("321", "Only owner"))
                        .asWidget()));
    }
}
