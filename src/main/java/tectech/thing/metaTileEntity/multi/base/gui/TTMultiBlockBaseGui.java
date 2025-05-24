package tectech.thing.metaTileEntity.multi.base.gui;

import static tectech.Reference.MODID;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.HoverableIcon;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import tectech.thing.metaTileEntity.multi.base.Parameter;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class TTMultiBlockBaseGui extends MTEMultiBlockBaseGui {

    private final TTMultiblockBase ttBase;

    public TTMultiBlockBaseGui(MTEMultiBlockBase base) {
        super(base);
        ttBase = (TTMultiblockBase) base;
    }

    @Override
    protected void initCustomIcons() {
        this.customIcons.put("power_switch_disabled", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
        this.customIcons.put("power_switch_on", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_ON);
        this.customIcons.put("power_switch_off", GTGuiTextures.TT_OVERLAY_BUTTON_POWER_SWITCH_OFF);
    }

    @Override
    public IWidget createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createVoidExcessButton(syncManager))
            .child(createInputSeparationButton(syncManager))
            .childIf(ttBase.supportsMachineModeSwitch(), createModeSwitchButton(syncManager))
            .child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager))
            .child(createStructureUpdateButton(syncManager))
            .childIf(supportsPowerPanel(), createPowerPanelButton(syncManager, parent))
            .child(createMaintIssueHoverable(syncManager))
            .child(createShutdownReasonHoverable(syncManager));
    }

    private IWidget createMaintIssueHoverable(PanelSyncManager syncManager) {
        IntSyncValue maintSyncer = (IntSyncValue) syncManager.getSyncHandler("maintCount:0");
        return new DynamicDrawable(
            () -> maintSyncer.getValue() == 0 ? GTGuiTextures.OVERLAY_NO_MAINTENANCE_ISSUES
                : IKey.str(EnumChatFormatting.DARK_RED + String.valueOf(maintSyncer.getValue()))).asWidget()
                    .tooltipBuilder(t -> makeMaintenanceHoverableTooltip(t, maintSyncer))
                    .tooltipAutoUpdate(true)
                    .background(GuiTextures.SLOT_ITEM);
    }

    private IWidget createShutdownReasonHoverable(PanelSyncManager syncManager) {
        BooleanSyncValue wasShutdownSyncer = (BooleanSyncValue) syncManager.getSyncHandler("wasShutdown:0");
        LongSyncValue euVarSyncer = (LongSyncValue) syncManager.getSyncHandler("storedEU:0");

        return new HoverableIcon(new DynamicDrawable(() -> {
            if (wasShutdownSyncer.getValue() || euVarSyncer.getValue() == 0) {
                return getTextureForShutdownReason(ttBase.getBaseMetaTileEntity(), euVarSyncer.getValue());
            } else {
                return GTGuiTextures.OVERLAY_BUTTON_CHECKMARK;
            }
        }).asIcon()).asWidget()
            .tooltipBuilder(t -> makeShutdownReasonHoverableTooltip(t, wasShutdownSyncer, euVarSyncer))
            .tooltipAutoUpdate(true);
    }

    private void makeShutdownReasonHoverableTooltip(RichTooltip t, BooleanSyncValue wasShutdownSyncer,
        LongSyncValue euVarSyncer) {
        if (wasShutdownSyncer.getValue() || euVarSyncer.getValue() == 0) {
            t.add(
                getTooltipForShutdownReason(
                    ttBase.getBaseMetaTileEntity()
                        .getLastShutDownReason(),
                    euVarSyncer.getValue()));
        } else {
            t.add(EnumChatFormatting.GREEN + "Running fine.");
        }
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        UITexture mesh = UITexture.builder()
            .location(MODID, "gui/overlay_slot/mesh")
            .canApplyTheme(true)
            .build();
        UITexture heatSinkSmall = UITexture.builder()
            .location(MODID, "gui/picture/heat_sink_small")
            .canApplyTheme(true)
            .build();

        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .child(createPowerPassButton())
            .child(createEditParametersButton(panel, syncManager))
            .child(createPowerSwitchButton())
            .childIf(
                base.doesBindPlayerInventory(),
                new ItemSlot()
                    .slot(new ModularSlot(base.inventoryHandler, base.getControllerSlotIndex()).slotGroup("item_inv"))
                    .background(new DrawableStack(GuiTextures.SLOT_ITEM, mesh))
                    .overlay(
                        heatSinkSmall.asIcon()
                            .size(18, 6)
                            .marginTop(22)));
    }

    private void makeMaintenanceHoverableTooltip(RichTooltip t, IntSyncValue maintSyncer) {
        if (maintSyncer.getValue() == 0) {
            t.addLine(IKey.str(EnumChatFormatting.GREEN + "No maintenance issues!"));
            return;
        }
        if (!ttBase.mCrowbar) t.add(
            GTGuiTextures.OVERLAY_NEEDS_CROWBAR.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mHardHammer) t.add(
            GTGuiTextures.OVERLAY_NEEDS_HARDHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mScrewdriver) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SCREWDRIVER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mSoftMallet) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOFTHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mSolderingTool) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOLDERING.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mWrench) t.add(
            GTGuiTextures.OVERLAY_NEEDS_WRENCH.asIcon()
                .size(16, 16))
            .add(" ");
    }

    private String getTooltipForShutdownReason(ShutDownReason lastShutDownReason, long eu) {
        String key = lastShutDownReason.getKey();
        if (key.equals(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE.getKey())) return "Structure incomplete.";
        if (eu == 0) return EnumChatFormatting.DARK_RED + "I don't have power!";
        if (key.equals(ShutDownReasonRegistry.POWER_LOSS.getKey())) return "Lost Power!";
        if (key.equals(ShutDownReasonRegistry.NO_REPAIR.getKey())) return "Machine too damaged!";
        if (key.equals(ShutDownReasonRegistry.NONE.getKey())) return "Manual shutdown (get it?)";
        return "REPORT ME! I'M NOT SUPPOSED TO BE HERE!";
    }

    public boolean supportsPowerPanel() {
        return false;
    }

    private IDrawable getTextureForShutdownReason(IGregTechTileEntity tileEntity, long eu) {
        String key = tileEntity.getLastShutDownReason()
            .getKey();
        if (key.equals(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE.getKey()))
            return GTGuiTextures.OVERLAY_STRUCTURE_INCOMPLETE;
        if (eu == 0) return GTGuiTextures.OVERLAY_UNPOWERED;
        if (key.equals(ShutDownReasonRegistry.POWER_LOSS.getKey())) return GTGuiTextures.OVERLAY_POWER_LOSS;
        if (key.equals(ShutDownReasonRegistry.NO_REPAIR.getKey())) return GTGuiTextures.OVERLAY_TOO_DAMAGED;
        if (key.equals(ShutDownReasonRegistry.NONE.getKey())) return GTGuiTextures.OVERLAY_MANUAL_SHUTDOWN;
        return null;
    }

    public IWidget createPowerPassButton() {
        return new ToggleButton().value(new BooleanSyncValue(() -> ttBase.ePowerPass, bool -> {
            if (isPowerSwitchDisabled()) return;
            ttBase.ePowerPass = bool;
            if (isPowerSwitchDisabled()) { // TRANSFORMER HACK
                if (ttBase.ePowerPass) {
                    ttBase.getBaseMetaTileEntity()
                        .enableWorking();
                } else {
                    ttBase.getBaseMetaTileEntity()
                        .disableWorking();
                }
            }
        }))
            .tooltip(tooltip -> tooltip.add("Power Pass"))
            .size(18, 18)
            .overlay(
                new DynamicDrawable(
                    () -> isPowerSwitchDisabled() ? GTGuiTextures.powerPassDisabled
                        : ttBase.ePowerPass ? GTGuiTextures.powerPassOn : GTGuiTextures.powerPassOff));

    }

    public IWidget createEditParametersButton(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler infoPanel = syncManager
            .panel("info_panel", (p_syncManager, syncHandler) -> getParameterPanel(panel, p_syncManager), true);
        return new ButtonWidget<>().overlay(new DynamicDrawable(() -> {
            if (ttBase.parameterList.isEmpty()) {
                return GTGuiTextures.editParametersDisabled.asIcon()
                    .size(16, 16);
            } else {
                return GTGuiTextures.editParametersEnabled.asIcon()
                    .size(16, 16);
            }
        }))
            .tooltipBuilder(t -> t.add("Edit Parameters"))
            .size(18, 18)
            .onMousePressed(mouseData -> {
                if (ttBase.parameterList.isEmpty()) return false;
                if (!infoPanel.isPanelOpen()) {
                    infoPanel.openPanel();
                } else {
                    infoPanel.closePanel();
                }
                return true;
            });
    }

    private ModularPanel getParameterPanel(ModularPanel parent, PanelSyncManager syncManager) {
        Area parentArea = parent.getArea();
        return new ModularPanel("parameters") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(125, 191)
            .pos(parentArea.x + parentArea.width, parentArea.y)
            .child(
                new ListWidget<>().sizeRel(1)
                    .margin(4)
                    .children(createParameterListWidgetChildren()));
    }

    private Iterable<IWidget> createParameterListWidgetChildren() {
        List<IWidget> result = new ArrayList<>();
        for (Parameter<?> parameter : ttBase.parameterList) {
            if (!parameter.show) continue;
            result.add(
                new Column().widthRel(1)
                    .height(35)
                    .marginTop(4)
                    .child(
                        IKey.str(parameter.getLocalizedName())
                            .asWidget()
                            .marginBottom(4))
                    .child(createParameterWidget(parameter)));
        }
        return result;
    }

    private IWidget createParameterWidget(Parameter<?> parameter) {
        if (parameter instanceof Parameter.BooleanParameter booleanParameter)
            return createButtonParameterWidget(booleanParameter);
        return createTextFieldParameterWidget(parameter);
    }

    private IWidget createTextFieldParameterWidget(Parameter<?> parameter) {
        TextFieldWidget parameterField = new TextFieldWidget().size(90, 12);
        configureParameterField(parameterField, parameter);
        return parameterField;
    }

    private void configureParameterField(TextFieldWidget parameterField, Parameter<?> parameter) {
        if (parameter instanceof Parameter.IntegerParameter intParameter) {
            parameterField.value(new IntSyncValue(intParameter::getValue, intParameter::setValue))
                .setNumbers(intParameter::getMinValue, intParameter::getMaxValue);
        } else if (parameter instanceof Parameter.DoubleParameter doubleParameter) {
            parameterField.value(new DoubleSyncValue(doubleParameter::getValue, doubleParameter::setValue))
                .setNumbersDouble(
                    val -> Math.max(doubleParameter.getMinValue(), Math.min(doubleParameter.getMaxValue(), val)));
        } else if (parameter instanceof Parameter.StringParameter stringParameter) {
            parameterField.value(new StringSyncValue(stringParameter::getValue, stringParameter::setValue));
        }
        parameterField.setText(parameter.getValueString());
    }

    private IWidget createButtonParameterWidget(Parameter.BooleanParameter booleanParameter) {
        return new ToggleButton().value(new BooleanSyncValue(booleanParameter::getValue, booleanParameter::setValue))
            .overlay(
                false,
                GTGuiTextures.OVERLAY_BUTTON_CROSS.asIcon()
                    .size(18))
            .overlay(
                true,
                GTGuiTextures.OVERLAY_BUTTON_CHECKMARK.asIcon()
                    .size(18))
            .align(Alignment.Center)
            .size(18, 18)
            .marginTop(4);
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        IntSyncValue maintSyncer = new IntSyncValue(() -> {
            int maintIsuses = 0;
            maintIsuses += ttBase.mCrowbar ? 0 : 1;
            maintIsuses += ttBase.mHardHammer ? 0 : 1;
            maintIsuses += ttBase.mScrewdriver ? 0 : 1;
            maintIsuses += ttBase.mSoftMallet ? 0 : 1;
            maintIsuses += ttBase.mSolderingTool ? 0 : 1;
            maintIsuses += ttBase.mWrench ? 0 : 1;
            return maintIsuses;
        });
        syncManager.syncValue("maintCount", maintSyncer);

        LongSyncValue euVarSyncer = new LongSyncValue(() -> ttBase.getEUVar());
        syncManager.syncValue("storedEU", euVarSyncer);
    }
}
