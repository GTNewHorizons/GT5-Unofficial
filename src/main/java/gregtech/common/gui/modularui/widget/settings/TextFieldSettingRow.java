package gregtech.common.gui.modularui.widget.settings;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IStringValue;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.github.bsideup.jabel.Desugar;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import it.unimi.dsi.fastutil.Pair;

@Desugar
record TextFieldSettingRow(IKey label, IStringValue<?> value, WidgetConfigurator<TextFieldWidget> configure)
    implements ISettingRow<TextFieldWidget> {

    @Override
    public @NotNull Pair<IKey, TextFieldWidget> build(ModularPanel panel, PanelSyncManager syncManager,
        SettingsPanel settings) {
        TextFieldWidget textFieldWidget = new TextFieldWidget().value(value)
            .width(80);

        if (configure != null) configure.configure(panel, syncManager, textFieldWidget);

        return Pair.of(label, textFieldWidget);
    }
}
