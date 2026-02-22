package gregtech.common.gui.modularui.cover;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.common.covers.CoverFluidLimiter;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;

public class CoverFluidLimiterGui extends CoverBaseGui<CoverFluidLimiter> {

    public CoverFluidLimiterGui(CoverFluidLimiter cover) {
        super(cover);
    }

    public void addUIWidgets(PanelSyncManager syncManager, Flow column) {
        column.child(
            IKey.lang("GT5U.gui.text.fluid_limiter.threshold")
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
                            t -> cover.setThreshold((float) t / 100)))
                    .marginLeft(4));

    }
}
