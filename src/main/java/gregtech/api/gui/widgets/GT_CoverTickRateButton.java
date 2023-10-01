package gregtech.api.gui.widgets;

import static gregtech.api.gui.modularui.GT_UITextures.HOURGLASS;

import java.util.List;

import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.common.covers.CoverInfo;
import org.jetbrains.annotations.NotNull;

public class GT_CoverTickRateButton extends ButtonWidget {

    private static final UITexture BACKGROUND = GT_UITextures.BUTTON_COVER_NORMAL.getSubArea(0, 0, 1, 0.5f);
    private final CoverInfo coverInfo;

    public GT_CoverTickRateButton(@NotNull CoverInfo coverInfo, @NotNull IWidgetBuilder<?> builder) {
        this.coverInfo = coverInfo;
        super.setBackground(BACKGROUND, HOURGLASS);
        super.setOnClick(this::onClick);
        super.dynamicTooltip(this::dynamicTooltip);
        super.attachSyncer(
            new FakeSyncWidget.IntegerSyncer(this.coverInfo::getTickRate, ignored -> {}),
            builder,
            (widget, aInt) -> notifyTooltipChange()
        );
        super.setUpdateTooltipEveryTick(true);
    }

    private void onClick(@NotNull ClickData clickData, @NotNull Widget widget) {
        coverInfo.adjustTickRateMultiplier(clickData.mouseButton == 1);
    }

    private List<String> dynamicTooltip() {
        final int minimumTickRate = coverInfo.getMinimumTickRate();
        final int currentTickRate = coverInfo.getTickRate();

        final ImmutableList.Builder<String> builder = ImmutableList.builder();

        builder.add(
            StatCollector
                .translateToLocalFormatted("gt.cover.info.button.tick_rate.1", coverInfo.getCurrentTickRateFormatted()),
            StatCollector.translateToLocal("gt.cover.info.button.tick_rate.2"));

        if (minimumTickRate != currentTickRate) {
            builder.add(
                StatCollector.translateToLocalFormatted(
                    "gt.cover.info.button.tick_rate.3",
                    coverInfo.getMinimumTickRateFormatted()));
        }

        return builder.build();
    }

}
