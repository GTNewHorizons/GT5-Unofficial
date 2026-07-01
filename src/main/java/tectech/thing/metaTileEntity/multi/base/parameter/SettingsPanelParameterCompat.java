package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IIntValue;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.EnumCycleButtonWidget;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanel;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;

public final class SettingsPanelParameterCompat {

    public static SettingsPanelBuilder addSettingsForParameters(List<Parameter<?, ?>> parameters,
        Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        return addSettingsForParameters(SettingsPanel.builder(), parameters, configurator, "");
    }

    @SuppressWarnings("unchecked")
    private static SettingsPanelBuilder addSettingsForParameters(SettingsPanelBuilder builder,
        List<Parameter<?, ?>> parameters, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator,
        String prefix) {
        for (Parameter<?, ?> parameter : parameters) {
            if (!parameter.shouldShowInGui()) continue;

            IKey label = IKey.lang(parameter.getLangKey(), parameter.getLangArgs());
            WidgetConfigurator<?> configure = configurator.apply(parameter);

            if (parameter instanceof IntegerParameter integerParameter) {
                builder.addIntEditor(
                    label,
                    integerParameter.getSyncHandler(),
                    integerParameter::validate,
                    (WidgetConfigurator<TextFieldWidget>) configure);
            } else if (parameter instanceof LongParameter longParameter) {
                builder.addLongEditor(
                    label,
                    longParameter.getSyncHandler(),
                    longParameter::validate,
                    (WidgetConfigurator<TextFieldWidget>) configure);
            } else if (parameter instanceof DoubleParameter doubleParameter) {
                builder.addDoubleEditor(
                    label,
                    doubleParameter.getSyncHandler(),
                    doubleParameter::validate,
                    (WidgetConfigurator<TextFieldWidget>) configure);
            } else if (parameter instanceof StringParameter stringParameter) {
                builder.addStringEditor(
                    label,
                    stringParameter.getSyncHandler(),
                    (WidgetConfigurator<TextFieldWidget>) configure);
            } else if (parameter instanceof EnumParameter<?>enumParameter) {
                addEnumCycleButtonWithCast(
                    builder,
                    label,
                    enumParameter.getEnumClass(),
                    enumParameter.getSyncHandler(),
                    configure);
            } else if (parameter instanceof FluidParameter fluidParameter) {
                builder.addPhantomFluidSlot(
                    label,
                    fluidParameter.getSyncHandler(),
                    (WidgetConfigurator<FluidSlot>) configure);
            } else if (parameter instanceof BooleanParameter booleanParameter) {
                builder.addToggleButton(label, booleanParameter.getSyncHandler(), (panel, syncManager, widget) -> {
                    widget.overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS);
                    if (configure != null)
                        ((WidgetConfigurator<ToggleButton>) configure).configure(panel, syncManager, widget);
                });
            } else if (parameter instanceof CompositeParameter compositeParameter) {
                builder.addButton(label, compositeParameterConfigurator(compositeParameter, prefix, configurator));
            } else {
                throw new UnsupportedOperationException(
                    "This parameter type is not yet supported by the SettingsPanel: " + parameter.getClass());
            }
        }
        return builder;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> void addEnumCycleButtonWithCast(SettingsPanelBuilder builder, IKey label,
        Class<E> clazz, IIntValue<?> value, WidgetConfigurator<?> configure) {
        builder.addEnumCycleButton(label, clazz, value, (WidgetConfigurator<EnumCycleButtonWidget<E>>) configure);
    }

    private static WidgetConfigurator<ButtonWidget<?>> compositeParameterConfigurator(CompositeParameter parameter,
        String prefix, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        return (_, syncManager, widget) -> {
            IPanelHandler parameterEditPanel = syncManager.syncedPanel(
                "parameterEditPanel_" + prefix + parameter.getNbtKey(),
                true,
                (p_syncManager, _) -> openParameterEditPanel(widget, parameter, p_syncManager, prefix, configurator));

            widget.width(80)
                .overlay(IKey.lang("tt.gui.tooltip.open_editor"))
                .onMousePressed(_ -> {
                    if (!parameterEditPanel.isPanelOpen()) {
                        parameterEditPanel.openPanel();
                    } else {
                        parameterEditPanel.closePanel();
                    }
                    return true;
                })
                .tooltip(
                    t -> t.addStringLines(
                        parameter.getValue()
                            .stream()
                            .map(p -> GTUtility.translate(p.getLangKey(), p.getLangArgs()))
                            .collect(Collectors.toList())));

        };
    }

    private static @NotNull ModularPanel openParameterEditPanel(ButtonWidget<?> parameterEditButton,
        CompositeParameter parameter, PanelSyncManager syncManager, String prefix,
        Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        ModularPanel panel = new ModularPanel("parameterEditPanel_" + prefix + parameter.getNbtKey()).coverChildren()
            .relative(parameterEditButton)
            .topRel(1)
            .leftRel(0)
            .child(ButtonWidget.panelCloseButton());

        panel.child(
            Flow.column()
                .coverChildren()
                .padding(4)
                .childPadding(10)
                .child(
                    IKey.lang(parameter.getLangKey(), parameter.getLangArgs())
                        .asWidget())
                .child(
                    addSettingsForParameters(
                        SettingsPanel.builder(),
                        parameter.getValue(),
                        configurator,
                        prefix + parameter.getNbtKey() + ".").build(panel, syncManager, 100)));

        return panel;
    }
}
