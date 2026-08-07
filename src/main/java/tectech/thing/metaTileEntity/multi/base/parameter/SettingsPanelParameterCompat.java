package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.List;
import java.util.function.Function;

import com.cleanroommc.modularui.api.drawable.IKey;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;

public final class SettingsPanelParameterCompat {

    public static SettingsPanelBuilder addSettingsForParameters(List<Parameter<?, ?>> parameters,
        Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        return addSettingsForParameters(SettingsPanel.builder(), parameters, configurator, "");
    }

    public static SettingsPanelBuilder addSettingsForParameters(SettingsPanelBuilder builder,
        List<Parameter<?, ?>> parameters, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator,
        String prefix) {
        for (Parameter<?, ?> parameter : parameters) {
            if (!parameter.shouldShowInGui()) continue;

            IKey label = IKey.lang(parameter.getLangKey(), parameter.getLangArgs());
            WidgetConfigurator<?> configure = configurator.apply(parameter);

            parameter.addToSettingsPanel(builder, label, configure, prefix, configurator);
        }
        return builder;
    }
}
