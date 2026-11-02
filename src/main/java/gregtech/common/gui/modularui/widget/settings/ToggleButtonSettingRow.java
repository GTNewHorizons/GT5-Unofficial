package gregtech.common.gui.modularui.widget.settings;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import it.unimi.dsi.fastutil.Pair;

record ToggleButtonSettingRow(IKey label, IBoolValue<?> value, WidgetConfigurator<ToggleButton> configure)
    implements ISettingRow<ToggleButton> {

    @Override
    public @NotNull Pair<IKey, ToggleButton> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        ToggleButton toggleButton = new ToggleButton().value(value);

        if (configure != null) configure.configure(panel, syncManager, toggleButton);

        return Pair.of(label, toggleButton);
    }
}
