package gregtech.common.covers.gui;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.covers.CoverFluidLimiter;

public class CoverFluidLimiterGui extends CoverGui<CoverFluidLimiter> {

    public CoverFluidLimiterGui(CoverFluidLimiter cover) {
        super(cover);
    }

    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        DoubleSyncValue thresholdSyncer = new DoubleSyncValue(
            () -> (double) Math.round(cover.getThreshold() * 100),
            t -> cover.setThreshold(
                Double.valueOf(t)
                    .floatValue() / 100));
        syncManager.syncValue("threshold", thresholdSyncer);
        column.child(
            IKey.str(StatCollector.translateToLocal("GT5U.gui.text.fluid_limiter.threshold"))
                .color(Color.GREY.darker(1))
                .asWidget()
                .marginBottom(4)
                .marginLeft(4))
            .child(
                makeNumberField().setNumbers(0, 100)
                    .setFocusOnGuiOpen(true)
                    .value(
                        new IntSyncValue(
                            () -> Math.round(cover.getThreshold() * 100),
                            t -> cover.setThreshold(
                                Double.valueOf(t)
                                    .floatValue() / 100)))
                    .marginLeft(4));

    }
}
