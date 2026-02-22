package gregtech.common.gui.modularui.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.CoverGuiData;
import gregtech.common.covers.CoverLiquidMeter;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;

public class CoverLiquidMeterGui extends CoverBaseGui<CoverLiquidMeter> {

    private final int maxCapacity;

    public CoverLiquidMeterGui(CoverLiquidMeter cover, int maxCapacity) {
        super(cover);
        this.maxCapacity = maxCapacity;
    }

    @Override
    public void addUIWidgets(PanelSyncManager syncManager, Flow column, CoverGuiData data) {
        column.child(
            makeRowLayout().child(positionRow(RowHelper.makeInvertRedstoneRow(cover)))
                .child(positionRow(makeFluidThresholdRow(cover))));
    }

    private Flow makeFluidThresholdRow(CoverLiquidMeter cover) {
        return Flow.row()
            .name("fluid_threshold")
            .child(
                makeNumberField().value(new IntSyncValue(cover::getThreshold, cover::setThreshold))
                    .setNumbers(0, maxCapacity)
                    .setFocusOnGuiOpen(true))
            .child(
                IKey.lang("gt.interact.desc.Fluid_Meter.FluidThreshold")
                    .asWidget());
    }
}
