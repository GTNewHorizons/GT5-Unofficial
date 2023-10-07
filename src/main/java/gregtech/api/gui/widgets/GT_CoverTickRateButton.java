package gregtech.api.gui.widgets;

import static gregtech.api.gui.modularui.GT_UITextures.OVERLAY_BUTTON_HOURGLASS;
import static gregtech.common.covers.CoverInfo.MAX_TICK_RATE_ADDITION;

import java.util.List;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.common.covers.CoverInfo;

public class GT_CoverTickRateButton extends ButtonWidget {

    private static final UITexture BACKGROUND = GT_UITextures.BUTTON_COVER_NORMAL.getSubArea(0, 0, 1, 0.5f);

    private final CoverInfo coverInfo;
    private int clientTickRate;
    private int tickRateAddition;

    public GT_CoverTickRateButton(@NotNull CoverInfo coverInfo, @NotNull IWidgetBuilder<?> builder) {
        this.coverInfo = coverInfo;
        this.clientTickRate = coverInfo.getTickRate();
        this.tickRateAddition = coverInfo.getTickRateAddition();

        super.setBackground(BACKGROUND, OVERLAY_BUTTON_HOURGLASS);
        super.setOnClick(this::onClick);
        super.dynamicTooltip(this::dynamicTooltip);
        super.attachSyncer(
            new FakeSyncWidget.IntegerSyncer(this.coverInfo::getTickRate, integer -> clientTickRate = integer),
            builder,
            (widget, aInt) -> notifyTooltipChange())
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(
                        this.coverInfo::getTickRateAddition,
                        integer -> tickRateAddition = integer),
                    builder);

    }

    private void onClick(@NotNull ClickData clickData, @NotNull Widget widget) {
        final boolean isDecreasing = clickData.mouseButton == 1;

        if (clickData.ctrl) {
            // Do five operations at once if Ctrl is held down. Since the actual increase granted by each invocation
            // can be different on each call, just call the method several times rather than trying to do a bunch of
            // weird math or adding a pointless for-loop.
            coverInfo.adjustTickRateMultiplier(isDecreasing);
            coverInfo.adjustTickRateMultiplier(isDecreasing);
            coverInfo.adjustTickRateMultiplier(isDecreasing);
            coverInfo.adjustTickRateMultiplier(isDecreasing);
        }
        coverInfo.adjustTickRateMultiplier(isDecreasing);
    }

    private List<String> dynamicTooltip() {
        final String boundsNotification;

        if (tickRateAddition == 0) {
            boundsNotification = StatCollector.translateToLocal("gt.cover.info.button.bounds_notification.minimum");
        } else if (tickRateAddition >= MAX_TICK_RATE_ADDITION - 1) {
            // Clamping can make tickRateAddition approach but never actually equal MAX_ADDITION, so we need this
            // adjustment.
            boundsNotification = StatCollector.translateToLocal("gt.cover.info.button.bounds_notification.maximum");
        } else {
            boundsNotification = "";
        }

        return ImmutableList.of(
            StatCollector.translateToLocalFormatted(
                "gt.cover.info.button.tick_rate.1",
                new CoverInfo.ClientTickRateFormatter(clientTickRate),
                boundsNotification),
            StatCollector.translateToLocal("gt.cover.info.button.tick_rate.2"),
            StatCollector.translateToLocal("gt.cover.info.button.tick_rate.3"));
    }
}
