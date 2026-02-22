package gregtech.common.gui.modularui.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.CoverPlayerDetector;
import gregtech.common.covers.modes.PlayerDetectionMode;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;

public class CoverPlayerDetectorGui extends CoverBaseGui<CoverPlayerDetector> {

    public CoverPlayerDetectorGui(CoverPlayerDetector cover) {
        super(cover);
    }

    @Override
    protected String getGuiId() {
        return "cover.player_detector";
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
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
                        .addTooltipLine(IKey.lang("gt.interact.desc.Player_Detector.Emit_Player_Close"))
                        .size(16),
                    IKey.lang("gt.interact.desc.Player_Detector.Any_Player")
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, PlayerDetectionMode.OTHER_PLAYERS))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.lang("gt.interact.desc.Player_Detector.Emit_Player_No_Close"))
                        .size(16),
                    IKey.lang("gt.interact.desc.Player_Detector.Other_Players")
                        .asWidget())
                .row(
                    new SelectButton().value(LinkedBoolValue.of(modeSyncValue, PlayerDetectionMode.ONLY_OWNER))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltipLine(IKey.lang("gt.interact.desc.Player_Detector.Emit_You_Close"))
                        .size(16),
                    IKey.lang("gt.interact.desc.Player_Detector.Only_Owner")
                        .asWidget()));
    }
}
