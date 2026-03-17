package gregtech.common.gui.modularui.widget.settings;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.github.bsideup.jabel.Desugar;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.PhantomFluidSettingRow.PhantomFluidWidgets;
import it.unimi.dsi.fastutil.Pair;

@Desugar
record PhantomFluidSettingRow(IKey label, WidgetConfigurator<FluidSlot> configure, FluidSlotSyncHandler value)
    implements ISettingRow<PhantomFluidWidgets> {

    @Desugar
    public record PhantomFluidWidgets(TextWidget<?> labelWidget, FluidSlot slot) {

    }

    @Override
    public Pair<PhantomFluidWidgets, Widget<?>> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        PhantomFluidWidgets w = new PhantomFluidWidgets(
            label.asWidget()
                .textAlign(Alignment.CenterRight),
            new FluidSlot().syncHandler(value)
                .marginLeft(6)
                .marginRight(2));

        if (configure != null) configure.configure(panel, syncManager, w.slot);

        return Pair.of(
            w,
            Flow.row()
                .mainAxisAlignment(MainAxis.START)
                .widthRel(1f)
                .height(20)
                .child(w.labelWidget)
                .child(w.slot));
    }

    @Override
    public void resize(SettingsPanel settings, PhantomFluidWidgets w, Widget<?> widget, int dividerPosition) {
        w.labelWidget.width(dividerPosition);
    }
}
