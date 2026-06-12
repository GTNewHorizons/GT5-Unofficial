package gregtech.common.gui.modularui.multiblock.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.api.value.IStringValue;
import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.parameter.BooleanParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.CompositeParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.DoubleParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.IParametrized;
import tectech.thing.metaTileEntity.multi.base.parameter.IntegerParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;
import tectech.thing.metaTileEntity.multi.base.parameter.StringParameter;

public class TTMultiblockBaseGui<T extends TTMultiblockBase> extends MTEMultiBlockBaseGui<T> {

    public TTMultiblockBaseGui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected void initCustomIcons() {
        this.customIcons.put("power_switch_disabled", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
        this.customIcons.put("power_switch_on", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON);
        this.customIcons.put("power_switch_off", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_OFF);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {

        return Flow.column()
            .width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .child(createPowerPassButton())
            .child(createEditParametersButton(panel, syncManager))
            .child(createPowerSwitchButton())
            .childIf(multiblock.doesBindPlayerInventory(), this::createControllerSlot);
    }

    protected ItemSlot createControllerSlot() {
        return new ItemSlot()
            .slot(
                new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex()).singletonSlotGroup())
            .marginTop(4)
            .backgroundOverlay(GTGuiTextures.TT_OVERLAY_SLOT_MESH)
            .overlay(
                GTGuiTextures.TT_CONTROLLER_SLOT_HEAT_SINK.asIcon()
                    .size(18, 6)
                    .marginTop(22));
    }

    protected IWidget createPowerPassButton() {
        return new ToggleButton().value(createPowerPassSyncHandler())
            .tooltip(tooltip -> tooltip.add("Power Pass"))
            .overlay(createPowerPassOverlay());
    }

    private IBoolValue<?> createPowerPassSyncHandler() {
        return new BooleanSyncValue(() -> multiblock.ePowerPass, bool -> {
            multiblock.ePowerPass = bool;
            if (isPowerSwitchDisabled()) { // TRANSFORMER HACK
                if (multiblock.ePowerPass) {
                    baseMetaTileEntity.enableWorking();
                } else {
                    baseMetaTileEntity.disableWorking();
                }
            }
        }).allowC2S();
    }

    private IDrawable createPowerPassOverlay() {
        return new DynamicDrawable(
            () -> isPowerSwitchDisabled() ? GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_DISABLED
                : multiblock.ePowerPass ? GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_ON
                    : GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_OFF);
    }

    protected IWidget createEditParametersButton(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler infoPanel = syncManager.syncedPanel(
            "parametersPanel",
            true,
            (p_syncManager, syncHandler) -> getParameterPanel(panel, p_syncManager));
        return new ButtonWidget<>().overlay(createEditParametersOverlay())
            .tooltipBuilder(t -> t.add("Edit Parameters"))
            .onMousePressed(onEditParametersPressed(infoPanel));
    }

    protected boolean isParametrized() {
        return multiblock instanceof IParametrized;
    }

    private IDrawable createEditParametersOverlay() {
        return new DynamicDrawable(() -> {
            if (isParametrized()) {
                return GTGuiTextures.OVERLAY_BUTTON_EDIT_PARAMETERS_ENABLED.asIcon()
                    .size(16);
            } else {
                return GTGuiTextures.OVERLAY_BUTTON_EDIT_PARAMETERS_DISABLED.asIcon()
                    .size(16);
            }
        });
    }

    private IGuiAction.MousePressed onEditParametersPressed(IPanelHandler infoPanel) {
        return mouseData -> {
            if (!isParametrized()) return false;
            if (!infoPanel.isPanelOpen()) {
                infoPanel.openPanel();
            } else {
                infoPanel.closePanel();
            }
            return true;
        };
    }

    private ModularPanel getParameterPanel(ModularPanel parent, PanelSyncManager syncManager) {
        ModularPanel panel = new ModularPanel("parametersPanel") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.coverChildren()
            .relative(parent)
            .topRel(0)
            .leftRel(1)
            .padding(4);

        List<Parameter<?>> parameters = new ArrayList<>();
        if (multiblock instanceof IParametrized parametrized) parameters = parametrized.getParameters();
        return panel.child(getParameterEditor(panel, syncManager, parameters, true, ""));
    }

    protected Widget<?> getParameterEditor(ModularPanel panel, PanelSyncManager syncManager,
        List<Parameter<?>> parameters, boolean isRoot, String prefix) {
        ListWidget<IWidget, ?> editButtons = new ListWidget<>().width(110)
            .maxSize((getBasePanelHeight() - 8) / (isRoot ? 1 : 2))
            .scrollDirection(new VerticalScrollData());

        parameters.forEach(parameter -> {
            if (!parameter.shouldShowInGui()) {
                return;
            }
            ButtonWidget<?> parameterEditButton = new ButtonWidget<>()
                .overlay(IKey.lang(parameter.getLangKey(), parameter.getLangArgs()))
                .width(100)
                .marginBottom(2)
                .tooltipDynamic(configureParameterEditorButtonTooltip(parameter));

            IPanelHandler editParameterPanel = syncManager.syncedPanel(
                "parameterEditPanel_" + prefix + parameter.getNbtKey(),
                true,
                (p_syncManager, $h) -> openParameterEditPanel(parameterEditButton, parameter, p_syncManager, prefix));

            editButtons.child(parameterEditButton.onMousePressed(d -> {
                if (!editParameterPanel.isPanelOpen()) {
                    editParameterPanel.openPanel();
                } else {
                    editParameterPanel.closePanel();
                }
                return true;
            }));
        });

        return editButtons;
    }

    protected Consumer<RichTooltip> configureParameterEditorButtonTooltip(Parameter<?> parameter) {
        // maybe add this to Parameter as getTooltip?
        if (parameter instanceof IntegerParameter || parameter instanceof DoubleParameter
            || parameter instanceof StringParameter
            || parameter instanceof BooleanParameter) {
            return t -> t.addLine(
                parameter.getValue()
                    .toString());
        }
        if (parameter instanceof CompositeParameter compositeParameter) {
            // values of the parameters are not included to avoid having to deal with composite parameters
            return t -> t.addStringLines(
                compositeParameter.getValue()
                    .stream()
                    .map(param -> GTUtility.translate(param.getLangKey(), param.getLangArgs()))
                    .collect(Collectors.toList()));
        }
        return t -> {};
    }

    protected @NotNull ModularPanel openParameterEditPanel(ButtonWidget<?> parameterEditButton, Parameter<?> parameter,
        PanelSyncManager syncManager, String prefix) {
        ModularPanel panel = new ModularPanel("parameterEditPanel_" + prefix + parameter.getNbtKey()).coverChildren()
            .relative(parameterEditButton)
            .topRel(1)
            .leftRel(0)
            .child(ButtonWidget.panelCloseButton());

        panel.child(
            Flow.column()
                .coverChildren()
                .padding(4)
                .marginRight(20)
                .childPadding(4)
                .child(
                    IKey.lang(parameter.getLangKey(), parameter.getLangArgs())
                        .asWidget())
                .child(createInputWidget(panel, syncManager, parameter)));

        return panel;
    }

    private IWidget createInputWidget(ModularPanel panel, PanelSyncManager syncManager, Parameter<?> parameter) {
        if (parameter instanceof IntegerParameter integerParameter) {
            return new TextFieldWidget().value((IStringValue<?>) integerParameter.createSyncHandler())
                .numbersInt(integerParameter::getMin, integerParameter::getMax);
        }
        if (parameter instanceof DoubleParameter doubleParameter) {
            return new TextFieldWidget().value((IStringValue<?>) doubleParameter.createSyncHandler())
                .numbersDouble(doubleParameter::validateValue);
        }
        if (parameter instanceof BooleanParameter booleanParameter) {
            return new ToggleButton().value((IBoolValue<?>) booleanParameter.createSyncHandler())
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS);
        }
        if (parameter instanceof StringParameter stringParameter) {
            return new TextFieldWidget().value((IStringValue<?>) stringParameter.createSyncHandler());
        }
        if (parameter instanceof CompositeParameter compositeParameter) {
            return getParameterEditor(
                panel,
                syncManager,
                compositeParameter.getValue(),
                false,
                compositeParameter.getNbtKey() + ".");
        }
        throw new IllegalArgumentException(
            "Tried to create an input widget for an unsupported parameter type " + parameter.getClass());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        if (multiblock instanceof IParametrized parametrized) {
            parametrized.getParameters()
                .forEach(parameter -> parameter.registerSyncValue(syncManager, ""));
        }
    }
}
