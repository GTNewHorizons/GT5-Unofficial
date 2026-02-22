package gregtech.common.covers.gui;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

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
            makeRowLayout().child(positionRow(Rows.makeInvertRedstoneRow(cover)))
                .child(positionRow(makeFluidThresholdRow(cover))));
    }

    private Flow makeFluidThresholdRow(CoverLiquidMeter cover) {
        return Flow.row()
            .debugName("fluid_threshold")
            .child(
                makeNumberField().value(new IntSyncValue(cover::getThreshold, cover::setThreshold))
                    .setNumbers(0, maxCapacity)
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("gt.interact.desc.Fluid_Meter.FluidThreshold")
                    .asWidget());
    }
}
