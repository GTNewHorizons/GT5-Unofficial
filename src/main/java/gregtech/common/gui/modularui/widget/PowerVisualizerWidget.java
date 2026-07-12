package gregtech.common.gui.modularui.widget;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;

import gregtech.api.modularui2.GTGuiTextures;

public class PowerVisualizerWidget extends ParentWidget<PowerVisualizerWidget> {

    public PowerVisualizerWidget(LongSyncValue storedEu, LongSyncValue maxStoredEu, IntSyncValue recipeEu,
        IntSyncValue consumedEu, LongSyncValue averageDrawnEu, LongSyncValue maxEuDraw, LongSyncValue maxEuOverdraw) {
        size(31, 75);
        child(createEnergyStorageBar(storedEu, maxStoredEu));
        child(createEnergyConsumptionBar(consumedEu, maxEuDraw));
        child(createEnergyRecipeUsageBar(recipeEu, maxEuDraw));
        child(createEnergyUsageBar(averageDrawnEu, maxEuDraw));
        child(createEnergyOverdrawBar(averageDrawnEu, maxEuDraw, maxEuOverdraw));
        child(createEnergyPanel(storedEu, maxStoredEu, consumedEu, averageDrawnEu, maxEuDraw, maxEuOverdraw));
    }

    private ProgressWidget createEnergyStorageBar(LongSyncValue storedEu, LongSyncValue maxStoredEu) {
        DoubleSyncValue percentageFilled = new DoubleSyncValue(
            () -> storedEu.getValue() / (double) Math.max(1, maxStoredEu.getValue()));

        return makeEnergyBar(
            percentageFilled,
            GTGuiTextures.PROGRESSBAR_ENERGY_EMPTY,
            GTGuiTextures.PROGRESSBAR_ENERGY_FULL,
            14,
            6);
    }

    private ProgressWidget createEnergyConsumptionBar(IntSyncValue consumedEu, LongSyncValue maxEuDraw) {
        DoubleSyncValue percentageFilled = new DoubleSyncValue(
            () -> consumedEu.getValue() / (double) maxEuDraw.getValue());

        return makeEnergyBar(
            percentageFilled,
            GTGuiTextures.TRANSPARENT,
            GTGuiTextures.PROGRESSBAR_ENERGY_CONSUMPTION,
            5,
            11);
    }

    private IWidget createEnergyRecipeUsageBar(IntSyncValue recipeEu, LongSyncValue maxEuDraw) {
        DoubleSyncValue percentageFilled = new DoubleSyncValue(
            () -> recipeEu.getValue() / (double) maxEuDraw.getValue());

        return makeEnergyBar(
            percentageFilled,
            GTGuiTextures.TRANSPARENT,
            GTGuiTextures.PROGRESSBAR_ENERGY_RECIPEY_USAGE,
            5,
            11);
    }

    private ProgressWidget createEnergyUsageBar(LongSyncValue averageDrawnEu, LongSyncValue maxEuDraw) {
        DoubleSyncValue percentageFilled = new DoubleSyncValue(
            () -> Math.min(1d, averageDrawnEu.getValue() / (double) maxEuDraw.getValue()));

        return makeEnergyBar(
            percentageFilled,
            GTGuiTextures.TRANSPARENT,
            GTGuiTextures.PROGRESSBAR_ENERGY_USAGE,
            5,
            16);
    }

    private IWidget createEnergyOverdrawBar(LongSyncValue averageDrawnEu, LongSyncValue maxEuDraw,
        LongSyncValue maxEuOverdraw) {
        DoubleSyncValue percentageFilled = new DoubleSyncValue(
            () -> Math.max(0d, (averageDrawnEu.getValue() - maxEuDraw.getValue()) / (double) maxEuOverdraw.getValue()));

        return makeEnergyBar(
            percentageFilled,
            GTGuiTextures.TRANSPARENT,
            GTGuiTextures.PROGRESSBAR_ENERGY_OVERDRAW,
            5,
            16);
    }

    private static ProgressWidget makeEnergyBar(DoubleSyncValue percentageFilled, UITexture background,
        UITexture texture, int width, int left) {
        return new ProgressWidget().texture(background, texture, 45)
            .size(width, 45)
            .top(15)
            .left(left)
            .direction(ProgressWidget.Direction.UP)
            .value(percentageFilled);
    }

    private IWidget createEnergyPanel(LongSyncValue storedEu, LongSyncValue maxStoredEu, IntSyncValue consumedEu,
        LongSyncValue averageDrawnEu, LongSyncValue maxEuDraw, LongSyncValue maxEuOverdraw) {
        return GTGuiTextures.ENERGY_GAUGE_PANEL.asWidget()
            .sizeRel(1)
            .tooltipDynamic(
                t -> t.addLine(String.format("Stored EU: %d/%d", storedEu.getValue(), maxStoredEu.getValue()))
                    .addLine(
                        String.format("Recipe EU consumption: %d/%d eu/t", consumedEu.getValue(), maxEuDraw.getValue()))
                    .addLine(
                        String.format(
                            "Average EU usage: %d/%d eu/t",
                            averageDrawnEu.getValue(),
                            maxEuOverdraw.getValue())))
            .tooltipAutoUpdate(true);
    }
}
