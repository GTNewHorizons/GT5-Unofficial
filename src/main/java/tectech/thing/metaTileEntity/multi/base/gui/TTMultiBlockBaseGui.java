package tectech.thing.metaTileEntity.multi.base.gui;

import static gregtech.api.enums.Mods.GTNHIntergalactic;
import static gregtech.api.enums.Mods.GregTech;
import static tectech.Reference.MODID;

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
import com.cleanroommc.modularui.widget.SingleChildWidget;
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

    private TTMultiblockBase ttBase;

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
    public IWidget createPanelGap(PanelSyncManager syncManager, ModularPanel parent) {
        Flow panelGap = new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createVoidExcessButton(syncManager))
            .child(createInputSeparationButton(syncManager));

        if (ttBase.supportsMachineModeSwitch()) panelGap.child(createModeSwitchButton(syncManager));
        panelGap.child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager))
            .child(createStructureUpdateButton(syncManager));
        if (supportsPowerPanel()) panelGap.child(createPowerPanelButton(syncManager, parent));
        panelGap.child(createMaintIssueHoverable(syncManager))
            .child(createShutdownReasonHoverable(syncManager));
        return panelGap;
    }

    private IWidget createMaintIssueHoverable(PanelSyncManager syncManager) {
        UITexture noMaint = UITexture.builder()
            .location(GregTech.ID, "gui/icons/noMaint")
            .imageSize(16, 16)
            .build();
        IntSyncValue maintSyncer = (IntSyncValue) syncManager.getSyncHandler("maintCount:0");

        return new DynamicDrawable(
            () -> maintSyncer.getValue() == 0 ? noMaint
                : IKey.str(EnumChatFormatting.DARK_RED + String.valueOf(maintSyncer.getValue()))).asWidget()
                    .tooltipBuilder(t -> makeMaintenanceHoverableTooltip(t, maintSyncer))
                    .tooltipAutoUpdate(true)
                    .background(GuiTextures.SLOT_ITEM);
    }

    private IWidget createShutdownReasonHoverable(PanelSyncManager syncManager) {
        UITexture checkmark = UITexture.builder()
            .location(GregTech.ID, "gui/overlay_button/checkmark")
            .imageSize(16, 16)
            .build();
        BooleanSyncValue wasShutdownSyncer = (BooleanSyncValue) syncManager.getSyncHandler("wasShutdown:0");
        LongSyncValue euVarSyncer = (LongSyncValue) syncManager.getSyncHandler("storedEU:0");

        return new HoverableIcon(new DynamicDrawable(() -> {
            if (wasShutdownSyncer.getValue() || euVarSyncer.getValue() == 0)
                return getTextureForShutdownReason(ttBase.getBaseMetaTileEntity(), euVarSyncer.getValue());
            return checkmark;
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

        Flow buttonColumn = new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .child(createPowerPassButton())
            .child(createEditParametersButton(panel, syncManager))
            .child(createPowerSwitchButton());
        if (base.doesBindPlayerInventory()) {
            buttonColumn.child(
                new ItemSlot()
                    .slot(new ModularSlot(base.inventoryHandler, base.getControllerSlotIndex()).slotGroup("item_inv"))
                    .background(new DrawableStack(GuiTextures.SLOT_ITEM, mesh))
                    .overlay(
                        heatSinkSmall.asIcon()
                            .size(18, 6)
                            .marginTop(22)));
        }
        return buttonColumn;
    }

    private void makeMaintenanceHoverableTooltip(RichTooltip t, IntSyncValue maintSyncer) {
        UITexture crowbarFalse = UITexture.builder()
            .location(GregTech.ID, "gui/icons/crowbarFalse")
            .imageSize(16, 16)
            .build();
        UITexture hardhammerFalse = UITexture.builder()
            .location(GregTech.ID, "gui/icons/hardhammerFalse")
            .imageSize(16, 16)
            .build();
        UITexture screwdriverFalse = UITexture.builder()
            .location(GregTech.ID, "gui/icons/screwdriverFalse")
            .imageSize(16, 16)
            .build();
        UITexture softhammerFalse = UITexture.builder()
            .location(GregTech.ID, "gui/icons/softhammerFalse")
            .imageSize(16, 16)
            .build();
        UITexture solderingFalse = UITexture.builder()
            .location(GregTech.ID, "gui/icons/solderingFalse")
            .imageSize(16, 16)
            .build();
        UITexture wrenchFalse = UITexture.builder()
            .location(GregTech.ID, "gui/icons/wrenchFalse")
            .imageSize(16, 16)
            .build();

        if (maintSyncer.getValue() == 0) {
            t.addLine(IKey.str(EnumChatFormatting.GREEN + "No maintenance issues!"));
            return;
        }
        if (!ttBase.mCrowbar) t.add(
            crowbarFalse.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mHardHammer) t.add(
            hardhammerFalse.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mScrewdriver) t.add(
            screwdriverFalse.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mSoftHammer) t.add(
            softhammerFalse.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mSolderingTool) t.add(
            solderingFalse.asIcon()
                .size(16, 16))
            .add(" ");
        if (!ttBase.mWrench) t.add(
            wrenchFalse.asIcon()
                .size(16, 16))
            .add(" ");
    }

    private String getTooltipForShutdownReason(ShutDownReason lastShutDownReason, long eu) {
        if (lastShutDownReason.getKey()
            .equals(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE.getKey())) {
            return "Structure incomplete.";
        } else if (eu == 0) {
            return EnumChatFormatting.DARK_RED + "I don't have power!";
        } else if (lastShutDownReason.getKey()
            .equals(ShutDownReasonRegistry.POWER_LOSS.getKey())) {
                return "Lost Power!";
            } else if (lastShutDownReason.getKey()
                .equals(ShutDownReasonRegistry.NO_REPAIR.getKey())) {
                    return "Machine too damaged!";
                } else if (lastShutDownReason.getKey()
                    .equals(ShutDownReasonRegistry.NONE.getKey())) {
                        return "Manual shutdown (get it?)";
                    }
        return "WTF?";
    }

    public boolean supportsPowerPanel() {
        return false;
    }

    private IDrawable getTextureForShutdownReason(IGregTechTileEntity tileEntity, long eu) {
        UITexture noRepairTexture = UITexture.builder()
            .location(GregTech.ID, "gui/icons/wrenchFalse")
            .imageSize(16, 16)
            .build();

        UITexture powerLossTexture = UITexture.builder()
            .location(GregTech.ID, "gui/picture/stalled_electricity")
            .imageSize(16, 16)
            .build();

        UITexture structureIncompleteTexture = UITexture.builder()
            .location(GregTech.ID, "gui/icons/structureIncomplete")
            .imageSize(16, 16)
            .build();

        UITexture manualShutdown = UITexture.builder()
            .location(GregTech.ID, "gui/icons/manualShutdown")
            .imageSize(16, 16)
            .build();
        UITexture unpowered = UITexture.builder()
            .location(GregTech.ID, "gui/icons/unpowered")
            .imageSize(16, 16)
            .build();

        ShutDownReason lastShutDownReason = tileEntity.getLastShutDownReason();
        if (lastShutDownReason.getKey()
            .equals(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE.getKey())) {
            return structureIncompleteTexture;
        } else if (eu == 0) {
            return unpowered;
        } else if (lastShutDownReason.getKey()
            .equals(ShutDownReasonRegistry.POWER_LOSS.getKey())) {
                return powerLossTexture;
            } else if (lastShutDownReason.getKey()
                .equals(ShutDownReasonRegistry.NO_REPAIR.getKey())) {
                    return noRepairTexture;
                } else if (lastShutDownReason.getKey()
                    .equals(ShutDownReasonRegistry.NONE.getKey())) {
                        return manualShutdown;
                    }
        return null;
    }

    public IWidget createPowerPassButton() {
        UITexture powerPassOn = UITexture.fullImage(MODID, "gui/overlay_button/power_pass_on");
        UITexture powerPassOff = UITexture.fullImage(MODID, "gui/overlay_button/power_pass_off");
        UITexture powerPassDisabled = UITexture.fullImage(MODID, "gui/overlay_button/power_pass_disabled");

        return new ToggleButton().value(new BooleanSyncValue(() -> ttBase.ePowerPass, bool -> {
            if (!isAllowedToWorkButtonEnabled()) return;
            ttBase.ePowerPass = bool;
            if (!isAllowedToWorkButtonEnabled()) { // TRANSFORMER HACK
                if (ttBase.ePowerPass) {
                    ttBase.getBaseMetaTileEntity()
                        .enableWorking();
                } else {
                    ttBase.getBaseMetaTileEntity()
                        .disableWorking();
                }
            }
        }))
            .tooltip(tooltip -> tooltip.add("Power Switch"))
            .size(18, 18)
            .overlay(
                new DynamicDrawable(
                    () -> !isAllowedToWorkButtonEnabled() ? powerPassDisabled
                        : ttBase.ePowerPass ? powerPassOn : powerPassOff));

    }

    public IWidget createEditParametersButton(ModularPanel panel, PanelSyncManager syncManager) {
        IPanelHandler infoPanel = syncManager
            .panel("info_panel", (p_syncManager, syncHandler) -> getParameterPanel(panel, p_syncManager), true);
        UITexture editParametersEnabled = UITexture.fullImage(GTNHIntergalactic.ID, "gui/overlay_button/options");
        UITexture editParametersDisabled = UITexture
            .fullImage(GTNHIntergalactic.ID, "gui/overlay_button/options_disabled");
        ButtonWidget editParametersButton = new ButtonWidget();
        editParametersButton.overlay(new DynamicDrawable(() -> {
            if (ttBase.parameterList.isEmpty()) {
                return editParametersDisabled.asIcon()
                    .size(16, 16);
            } else {
                return editParametersEnabled.asIcon()
                    .size(16, 16);
            }
        }));
        editParametersButton.tooltip(new RichTooltip(editParametersButton).add("Edit Parameters"));
        editParametersButton.size(18, 18);
        editParametersButton.onMousePressed(mouseData -> {
            if (ttBase.parameterList.isEmpty()) return false;
            if (!infoPanel.isPanelOpen()) {
                infoPanel.openPanel();
            } else {
                infoPanel.closePanel();
            }
            return true;
        });
        return editParametersButton;
    }

    private ModularPanel getParameterPanel(ModularPanel parent, PanelSyncManager syncManager) {
        Area parentArea = parent.getArea();
        ModularPanel panel = new ModularPanel("parameters") {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.size(125, 191)
            .pos(parentArea.x + parentArea.width, parentArea.y);
        ListWidget<IWidget, ?> parameterListWidget = new ListWidget<>();
        parameterListWidget.sizeRel(1)
            .margin(2);
        for (Parameter<?> parameter : ttBase.parameterList) {
            if (!parameter.show) continue;
            TextFieldWidget parameterField = new TextFieldWidget();
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
            parameterField.sizeRel(0.9f, 0.5f)
                .align(com.cleanroommc.modularui.utils.Alignment.Center);

            ToggleButton parameterButton = new ToggleButton();
            if (parameter instanceof Parameter.BooleanParameter booleanParameter) {
                parameterButton
                    .value(new BooleanSyncValue(booleanParameter::getValue, bool -> booleanParameter.invert()))
                    .overlay(
                        false,
                        UITexture.builder()
                            .location(GregTech.ID, "gui/overlay_button/cross.png")
                            .imageSize(18, 18)
                            .build())
                    .overlay(
                        true,
                        UITexture.builder()
                            .location(GregTech.ID, "gui/overlay_button/checkmark.png")
                            .imageSize(18, 18)
                            .build())
                    .align(com.cleanroommc.modularui.utils.Alignment.Center)
                    .size(18, 18);
            }

            parameterListWidget.child(
                new Column().heightRel(0.2f)
                    .child(
                        IKey.str(parameter.getLocalizedName())
                            .asWidget()
                            .alignment(com.cleanroommc.modularui.utils.Alignment.Center)
                            .sizeRel(1, 0.5f))
                    .child(
                        new SingleChildWidget<>().sizeRel(1, 0.5f)
                            .child(parameter instanceof Parameter.BooleanParameter ? parameterButton : parameterField))
                    .marginBottom(2));
        }
        panel.child(parameterListWidget);

        return panel;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        IntSyncValue maintSyncer = new IntSyncValue(() -> {
            int maintIsuses = 0;
            maintIsuses += ttBase.mCrowbar ? 0 : 1;
            maintIsuses += ttBase.mHardHammer ? 0 : 1;
            maintIsuses += ttBase.mScrewdriver ? 0 : 1;
            maintIsuses += ttBase.mSoftHammer ? 0 : 1;
            maintIsuses += ttBase.mSolderingTool ? 0 : 1;
            maintIsuses += ttBase.mWrench ? 0 : 1;
            return maintIsuses;
        });
        syncManager.syncValue("maintCount", maintSyncer);

        LongSyncValue euVarSyncer = new LongSyncValue(() -> ttBase.getEUVar());
        syncManager.syncValue("storedEU", euVarSyncer);
    }
}
