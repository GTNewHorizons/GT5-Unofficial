package gregtech.common.gui.modularui.widget.settings;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import it.unimi.dsi.fastutil.Pair;

record ButtonSettingRow(IKey label, WidgetConfigurator<ButtonWidget<?>> configure)
    implements ISettingRow<ButtonWidget<?>> {

    @Override
    public @NotNull Pair<IKey, ButtonWidget<?>> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        ButtonWidget<?> button = new ButtonWidget<>();

        if (configure != null) configure.configure(panel, syncManager, button);

        return Pair.of(label, button);
    }
}
