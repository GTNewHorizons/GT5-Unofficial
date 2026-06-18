package gregtech.common.modularui2.widget;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.covers.Cover;
import gregtech.common.text.ClientTickRateFormatter;

public class CoverTickRateButton extends ButtonWidget<CoverTickRateButton> {

    private final Cover cover;

    public CoverTickRateButton(Cover cover, PanelSyncManager syncManager) {
        this.cover = cover;

        overlay(GTGuiTextures.OVERLAY_BUTTON_HOURGLASS);
        syncHandler(new InteractionSyncHandler().setOnMousePressed(this::onClick));
        tooltipBuilder(this::buildTooltip);

        IntSyncValue tickRateAdditionSyncHandler = SyncHandlers
            .intNumber(cover::getTickRateAddition, cover::setTickRateAddition);
        tickRateAdditionSyncHandler.setChangeListener(this::markTooltipDirty);
        syncManager.syncValue("tickRateAddition", tickRateAdditionSyncHandler);
    }

    private void onClick(MouseData mouseData) {
        final int iterations = mouseData.ctrl ? 5 : 1;
        final boolean isDecreasing = mouseData.mouseButton == 1;

        // Do five operations at once if Ctrl is held down. Since the actual increase granted by each invocation can be
        // different on each call, just call the method several times rather than trying to do a bunch of weird math.
        for (int i = 0; i < iterations; i++) {
            cover.adjustTickRateMultiplier(isDecreasing);
        }
    }

    private void buildTooltip(RichTooltip tooltip) {
        final String boundsNotification;
        final int tickRateAddition = cover.getTickRateAddition();

        if (tickRateAddition == 0) {
            boundsNotification = StatCollector.translateToLocal("gt.cover.info.button.bounds_notification.minimum");
        } else if (cover.isTickRateAdditionMax()) {
            boundsNotification = StatCollector.translateToLocal("gt.cover.info.button.bounds_notification.maximum");
        } else {
            boundsNotification = "";
        }

        tooltip
            .addLine(
                IKey.lang(
                    "gt.cover.info.button.tick_rate.1",
                    new ClientTickRateFormatter(cover.getTickRate()),
                    boundsNotification))
            .addLine(IKey.lang("gt.cover.info.button.tick_rate.2"))
            .addLine(IKey.lang("gt.cover.info.button.tick_rate.3"));
    }
}
