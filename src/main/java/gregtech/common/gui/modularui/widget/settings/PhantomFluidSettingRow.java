package gregtech.common.gui.modularui.widget.settings;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.github.bsideup.jabel.Desugar;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import it.unimi.dsi.fastutil.Pair;

@Desugar
record PhantomFluidSettingRow(IKey label, FluidSlotSyncHandler value, WidgetConfigurator<FluidSlot> configure)
    implements ISettingRow<FluidSlot> {

    @Override
    public @NotNull Pair<IKey, FluidSlot> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        FluidSlot fluidSlot = new FluidSlot().syncHandler(value);

        if (configure != null) configure.configure(panel, syncManager, fluidSlot);

        return Pair.of(label, fluidSlot);
    }
}
