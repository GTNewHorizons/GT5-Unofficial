package gregtech.common.gui.modularui.multiblock.base;

import java.util.function.Function;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.parameter.IParametrized;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;
import tectech.thing.metaTileEntity.multi.base.parameter.SettingsPanelParameterCompat;

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
            .addTooltipLine(GTUtility.translate("tt.gui.tooltip.power_pass"))
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
            .addTooltipLine(GTUtility.translate("tt.gui.tooltip.edit_parameters"))
            .onMousePressed(onEditParametersPressed(infoPanel));
    }

    protected final boolean isParametrized() {
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
        return _ -> {
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
        if (!isParametrized())
            throw new UnsupportedOperationException("Cannot open parameter panel on an unparametrized multiblock!");

        ModularPanel panel = new ModularPanel("parametersPanel") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.coverChildrenWidth(80)
            .coverChildrenHeight()
            .relative(parent)
            .topRel(0)
            .leftRel(1)
            .padding(4)
            .child(ButtonWidget.panelCloseButton());

        Flow mainColumn = Flow.column()
            .coverChildren()
            .childPadding(10)
            .child(
                IKey.lang("tt.gui.parameter.header")
                    .asWidget())
            .child(getSettingsPanelBuilder().build(panel, syncManager, getBasePanelHeight() - 8));

        return panel.child(mainColumn);
    }

    protected SettingsPanelBuilder getSettingsPanelBuilder() {
        return SettingsPanelParameterCompat
            .addSettingsForParameters(((IParametrized) multiblock).getParameters(), getParameterWidgetConfigurator());
    }

    protected Function<Parameter<?, ?>, WidgetConfigurator<?>> getParameterWidgetConfigurator() {
        return _ -> null;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        if (isParametrized()) {
            ((IParametrized) multiblock).getParameters()
                .forEach(parameter -> parameter.registerSyncValue(syncManager));
        }
    }
}
