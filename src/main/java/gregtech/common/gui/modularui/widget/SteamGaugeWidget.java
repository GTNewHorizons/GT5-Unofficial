package gregtech.common.gui.modularui.widget;

import com.cleanroommc.modularui.api.value.sync.IIntSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;

public class SteamGaugeWidget extends ParentWidget<SteamGaugeWidget> {

    public static final int GAUGE_SIZE = 42;
    public static final int GAUGE_CONNECTOR_SIZE = 6;
    public static final int GAUGE_NEEDLE_WIDTH = 18;
    public static final int GAUGE_NEEDLE_HEIGHT = 4;

    public SteamGaugeWidget(IIntSyncValue<?> steamStoredSyncer, IIntSyncValue<?> maxSteamSyncer) {
        coverChildren();
        child(
            new Widget<>().widgetTheme(GTWidgetThemes.STEAM_GAUGE)
                .tooltipDynamic(
                    t -> t.addLine(
                        GTUtility.translate(
                            "GT5U.machines.steam.amount",
                            steamStoredSyncer.getValue(),
                            maxSteamSyncer.getValue())))
                .tooltipAutoUpdate(true));
        child(
            new CircularGaugeDrawable(() -> (double) steamStoredSyncer.getIntValue() / maxSteamSyncer.getIntValue())
                .asWidget()
                .widgetTheme(GTWidgetThemes.STEAM_GAUGE_NEEDLE)
                .top(GAUGE_SIZE / 2)
                .left(GAUGE_SIZE / 2));
    }
}
