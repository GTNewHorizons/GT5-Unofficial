package gregtech.common.gui.modularui.widget;

import java.util.function.Predicate;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;

import gregtech.api.modularui2.GTGuiTextures;

public class PowerVisualizerWidget extends ParentWidget<PowerVisualizerWidget> {

    public PowerVisualizerWidget(Predicate<PowerVisualizerWidget> visibilityPredicate, LongSyncValue storedEu,
                                 LongSyncValue maxStoredEu, IntSyncValue recipeEu, LongSyncValue averageDrawnEu, LongSyncValue maxEuDraw,
                                 LongSyncValue maxEuOverdraw) {
        size(31, 75);
        child(createEnergyStorageBar(storedEu, maxStoredEu));
        child(createEnergyConsumptionBar(recipeEu, maxEuDraw));
        child(createEnergyUsageBar(averageDrawnEu, maxEuDraw));
        child(createEnergyOverdrawBar(averageDrawnEu, maxEuDraw, maxEuOverdraw));
        child(createEnergyPanel(storedEu, maxStoredEu, recipeEu, averageDrawnEu, maxEuDraw, maxEuOverdraw));
        setEnabledIf(visibilityPredicate);
    }

    private ProgressWidget createEnergyStorageBar(LongSyncValue euSyncer, LongSyncValue maxEuSyncer) {
        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> euSyncer.getValue() / (double) Math.max(1, maxEuSyncer.getValue()));

        return makeEnergyBar(
            percentageSyncer,
            GTGuiTextures.PROGRESSBAR_ENERGY_EMPTY,
            GTGuiTextures.PROGRESSBAR_ENERGY_FULL,
            14,
            6);
    }

    private ProgressWidget createEnergyConsumptionBar(IntSyncValue eutSyncer, LongSyncValue maxStandardInputSyncer) {
        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> eutSyncer.getValue() / (double) maxStandardInputSyncer.getValue());

        return makeEnergyBar(
            percentageSyncer,
            GTGuiTextures.TRANSPARENT,
            GTGuiTextures.PROGRESSBAR_ENERGY_CONSUMPTION,
            5,
            11);
    }

    private ProgressWidget createEnergyUsageBar(LongSyncValue averageInputSyncer,
        LongSyncValue maxStandardInputSyncer) {
        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> Math.min(1d, averageInputSyncer.getValue() / (double) maxStandardInputSyncer.getValue()));

        return makeEnergyBar(
            percentageSyncer,
            GTGuiTextures.TRANSPARENT,
            GTGuiTextures.PROGRESSBAR_ENERGY_USAGE,
            5,
            16);
    }

    private IWidget createEnergyOverdrawBar(LongSyncValue averageInputSyncer, LongSyncValue maxStandardInputSyncer,
        LongSyncValue maxOverdrawInputSyncer) {
        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> Math.max(
                0d,
                (averageInputSyncer.getValue() - maxStandardInputSyncer.getValue())
                    / (double) maxOverdrawInputSyncer.getValue()));

        return makeEnergyBar(
            percentageSyncer,
            GTGuiTextures.TRANSPARENT,
            GTGuiTextures.PROGRESSBAR_ENERGY_OVERDRAW,
            5,
            16);
    }

    private static ProgressWidget makeEnergyBar(DoubleSyncValue percentageSyncer, UITexture background,
        UITexture texture, int width, int left) {
        return new ProgressWidget().texture(background, texture, 45)
            .size(width, 45)
            .top(15)
            .left(left)
            .direction(ProgressWidget.Direction.UP)
            .value(percentageSyncer);
    }

    private IWidget createEnergyPanel(LongSyncValue euSyncer, LongSyncValue maxEuSyncer, IntSyncValue eutSyncer,
        LongSyncValue averageInputSyncer, LongSyncValue maxStandardInputSyncer, LongSyncValue maxOverdrawInputSyncer) {
        return GTGuiTextures.ENERGY_GAUGE_PANEL.asWidget()
            .sizeRel(1)
            .tooltipDynamic(
                t -> t.addLine(String.format("Stored EU: %d/%d", euSyncer.getValue(), maxEuSyncer.getValue()))
                    .addLine(
                        String.format(
                            "Recipe EU consumption: %d/%d eu/t",
                            eutSyncer.getValue(),
                            maxStandardInputSyncer.getValue()))
                    .addLine(
                        String.format(
                            "Average EU usage: %d/%d eu/t",
                            averageInputSyncer.getValue(),
                            maxOverdrawInputSyncer.getValue())))
            .tooltipAutoUpdate(true);
    }
}
