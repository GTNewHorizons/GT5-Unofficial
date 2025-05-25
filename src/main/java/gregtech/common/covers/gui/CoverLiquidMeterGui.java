package gregtech.common.covers.gui;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.CoverLiquidMeter;

public class CoverLiquidMeterGui extends CoverGui<CoverLiquidMeter> {

    private final int maxCapacity;

    public CoverLiquidMeterGui(CoverLiquidMeter cover, int maxCapacity) {
        super(cover);
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        column.child(
            makeRowLayout().child(positionRow(makeInvertRedstoneRow(cover)))
                .child(positionRow(makeFluidThresholdRow(cover))));
    }

    private Flow makeInvertRedstoneRow(CoverLiquidMeter cover) {
        BooleanSyncValue isInvertedSyncValue = new BooleanSyncValue(cover::isInverted, cover::setInverted);
        return Flow.row()
            .debugName("invert_redstone")
            .child(
                new ToggleButton().value(isInvertedSyncValue)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_ON)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_REDSTONE_OFF)
                    .size(16))
            .child(
                IKey.dynamic(
                    () -> isInvertedSyncValue.getValue() ? GTUtility.trans("INVERTED", "Inverted")
                        : GTUtility.trans("NORMAL", "Normal"))
                    .asWidget());
    }

    private Flow makeFluidThresholdRow(CoverLiquidMeter cover) {
        return Flow.row()
            .debugName("fluid_threshold")
            .child(
                makeNumberField().value(new IntSyncValue(cover::getThreshold, cover::setThreshold))
                    .setNumbers(0, maxCapacity)
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.str(GTUtility.trans("222", "Fluid threshold"))
                    .asWidget());
    }
}
