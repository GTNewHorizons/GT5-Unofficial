package gregtech.common.gui.modularui.multiblock.base;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.api.value.IStringValue;
import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.parameter.BooleanParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.DoubleParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.IParametrized;
import tectech.thing.metaTileEntity.multi.base.parameter.IntegerParameter;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;

public class TTMultiblockBaseGui<T extends TTMultiblockBase> extends MTEMultiBlockBaseGui<T> {

    protected Map<String, SyncHandler> parameterSyncers = new HashMap<>();

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

        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .child(createPowerPassButton())
            .child(createEditParametersButton(panel, syncManager))
            .child(createPowerSwitchButton())
            .childIf(multiblock.doesBindPlayerInventory(), this::createControllerSlot);
    }

    private IWidget createControllerSlot() {
        return new ItemSlot()
            .slot(
                new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex()).slotGroup("item_inv"))
            .marginTop(4)
            .background(new DrawableStack(GuiTextures.SLOT_ITEM, GTGuiTextures.TT_OVERLAY_SLOT_MESH))
            .overlay(
                GTGuiTextures.TT_CONTROLLER_SLOT_HEAT_SINK.asIcon()
                    .size(18, 6)
                    .marginTop(22));
    }

    protected IWidget createPowerPassButton() {
        return new ToggleButton().value(createPowerPassSyncHandler())
            .tooltip(tooltip -> tooltip.add("Power Pass"))
            .size(18, 18)
            .overlay(createPowerPassOverlay());
    }

    private IBoolValue<?> createPowerPassSyncHandler() {
        return new BooleanSyncValue(() -> multiblock.ePowerPass, bool -> {
            multiblock.ePowerPass = bool;
            if (isPowerSwitchDisabled()) { // TRANSFORMER HACK
                if (multiblock.ePowerPass) {
                    multiblock.getBaseMetaTileEntity()
                        .enableWorking();
                } else {
                    multiblock.getBaseMetaTileEntity()
                        .disableWorking();
                }
            }
        });
    }

    private IDrawable createPowerPassOverlay() {
        return new DynamicDrawable(
            () -> isPowerSwitchDisabled() ? GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_DISABLED
                : multiblock.ePowerPass ? GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_ON
                    : GTGuiTextures.OVERLAY_BUTTON_POWER_PASS_OFF);
    }

    protected IWidget createEditParametersButton(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler infoPanel = syncManager
            .syncedPanel("info_panel", true, (p_syncManager, syncHandler) -> getParameterPanel(panel, p_syncManager));
        return new ButtonWidget<>().overlay(createEditParametersOverlay())
            .tooltipBuilder(t -> t.add("Edit Parameters"))
            .size(18, 18)
            .onMousePressed(onEditParametersPressed(infoPanel));
    }

    private IDrawable createEditParametersOverlay() {
        return new DynamicDrawable(() -> {
            if (multiblock instanceof IParametrized) {
                return GTGuiTextures.OVERLAY_BUTTON_EDIT_PARAMETERS_ENABLED.asIcon()
                    .size(16, 16);
            } else {
                return GTGuiTextures.OVERLAY_BUTTON_EDIT_PARAMETERS_DISABLED.asIcon()
                    .size(16, 16);
            }
        });
    }

    private IGuiAction.MousePressed onEditParametersPressed(IPanelHandler infoPanel) {
        return mouseData -> {
            if (!(multiblock instanceof IParametrized)) return false;
            if (!infoPanel.isPanelOpen()) {
                infoPanel.openPanel();
            } else {
                infoPanel.closePanel();
            }
            return true;
        };
    }

    // Panel implementation will come with first parametrized multiblock port
    private ModularPanel getParameterPanel(ModularPanel parent, PanelSyncManager syncManager) {
        ModularPanel panel = new ModularPanel("parameters") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.coverChildren()
            .relative(parent)
            .rightRel(0, 0, 1)
            .topRel(0)
            .padding(4);

        Flow column = Flow.column()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.START);

        multiblock.parameterMap.forEach((mapKey, parameter) -> {
            String key = parameter.getLangKey();
            ButtonWidget<?> parameterEditButton = new ButtonWidget<>().overlay(IKey.lang(key))
                .width(100)
                .marginBottom(2);

            IPanelHandler editParameterPanel = syncManager.syncedPanel(
                mapKey,
                true,
                (s, h) -> openParameterEditPanel(parameterEditButton, parameter, syncManager, mapKey));

            column.child(parameterEditButton.onMousePressed(d -> {
                if (!editParameterPanel.isPanelOpen()) {
                    editParameterPanel.openPanel();
                } else {
                    editParameterPanel.closePanel();
                }
                return true;
            }));
        });

        return panel.child(column);
    }

    private @NotNull ModularPanel openParameterEditPanel(ButtonWidget<?> parameterEditButton, Parameter<?> parameter,
        PanelSyncManager syncManager, String mapKey) {
        return new ModularPanel(mapKey) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.coverChildren()
            .relative(parameterEditButton)
            .topRel(1)
            .leftRel(0)
            .child(
                Flow.column()
                    .coverChildren()
                    .padding(4)
                    .child(createParameterEditLabelRow(parameter))
                    .child(createInputWidget(parameter, mapKey, syncManager)));
    }

    private IWidget createParameterEditLabelRow(Parameter<?> parameter) {
        return Flow.row()
            .coverChildren()
            .child(
                IKey.lang(parameter.getLangKey())
                    .asWidget()
                    .alignment(Alignment.CenterLeft)
                    .margin(0, 14, 2, 2))
            .child(
                ButtonWidget.panelCloseButton()
                    .top(0)
                    .right(0));
    }

    private IWidget createInputWidget(Parameter<?> parameter, String mapKey, PanelSyncManager syncManager) {

        if (parameter instanceof IntegerParameter integerParameter) {
            return new TextFieldWidget().value((IStringValue<?>) integerParameter.createSyncHandler())
                .setNumbers(integerParameter::getMin, integerParameter::getMax);
        }
        if (parameter instanceof DoubleParameter doubleParameter) {
            return new TextFieldWidget().value((IStringValue<?>) doubleParameter.createSyncHandler())
                .setNumbersDouble(doubleParameter::validateValue);
        }
        if (parameter instanceof BooleanParameter booleanParameter) {
            return new ToggleButton().value((IBoolValue<?>) booleanParameter.createSyncHandler())
                .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS);
        }
        throw new IllegalArgumentException(
            "Tried to create an input widget for an unsupported parameter type " + parameter.getClass());
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        multiblock.parameterMap
            .forEach((key, parameter) -> { syncManager.syncValue(key, parameter.createSyncHandler()); });
    }
}
