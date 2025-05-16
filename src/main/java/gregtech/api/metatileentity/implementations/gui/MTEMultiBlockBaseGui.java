package gregtech.api.metatileentity.implementations.gui;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableStack;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.enums.StructureError;
import gregtech.api.enums.VoidingMode;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public class MTEMultiBlockBaseGui {

    protected final MTEMultiBlockBase base;
    protected List<UITexture> machineModeIcons = new ArrayList<>();
    protected Map<String, UITexture> customIcons = new HashMap<>();
    protected final int textBoxToInventoryGap = 26;

    public MTEMultiBlockBaseGui(MTEMultiBlockBase base) {
        this.base = base;
        initCustomIcons();
    }

    protected void initCustomIcons() {
        this.customIcons.put("power_switch_disabled", GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
        this.customIcons.put("power_switch_on", GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
        this.customIcons.put("power_switch_off", GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
    }

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {

        ModularPanel panel = new ModularPanel("MTEMultiblockBase").size(198, 181 + textBoxToInventoryGap)
            .padding(4);

        registerSyncValues(syncManager);
        setMachineModeIcons();

        Flow panelColumn = new Column().sizeRel(1)
            .child(createTopRow(syncManager, panel));

        Flow inventoryRow = new Row().widthRel(1)
            .height(76)
            .alignX(0);

        if (base.doesBindPlayerInventory()) {
            inventoryRow.child(
                SlotGroupWidget.playerInventory(false)
                    .marginLeft(4));
        }

        panelColumn.child(createPanelGap(syncManager, panel))
            .child(inventoryRow);

        addTitleTextStyle(panel, base.getLocalName());
        inventoryRow.child(createButtonColumn(panel, syncManager));

        return panel.child(panelColumn);
    }

    protected IWidget createTopRow(PanelSyncManager syncManager, ModularPanel panel) {
        return new Row().size(machineInfoSize()[0] + 4, machineInfoSize()[1] + 3)
            .child(
                new ParentWidget<>().sizeRel(1)
                    .padding(3)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel)
                            .size(machineInfoSize()[0] - 4, machineInfoSize()[1] - 3))
                    .child(
                        new SingleChildWidget<>().bottomRel(0, 10, 0)
                            .rightRel(0, 10, 0)
                            .size(18, 18)
                            .widgetTheme(GTWidgetThemes.PICTURE_LOGO)));
    }

    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        Flow buttonColumn = new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(MainAxis.END)
            .child(createStructureUpdateButton(syncManager))
            .child(createPowerSwitchButton());

        if (base.doesBindPlayerInventory()) {
            buttonColumn.child(
                new ItemSlot()
                    .slot(new ModularSlot(base.inventoryHandler, base.getControllerSlotIndex()).slotGroup("item_inv")));
        }
        return buttonColumn;
    }

    protected IWidget createPowerSwitchButton() {
        return new ToggleButton().value(new BooleanSyncValue(base::isAllowedToWork, bool -> {
            if (!isAllowedToWorkButtonEnabled()) return;
            if (bool) base.enableWorking();
            else {
                if (base.maxProgresstime() > 0) base.disableWorking();
                else base.stopMachine(ShutDownReasonRegistry.NONE);
            }
        }))
            .tooltip(tooltip -> tooltip.add("Power Switch"))
            .size(18, 18)
            .marginBottom(4)
            .overlay(
                new DynamicDrawable(
                    () -> !isAllowedToWorkButtonEnabled() ? this.customIcons.get("power_switch_disabled")
                        : base.getBaseMetaTileEntity()
                            .isAllowedToWork() ? this.customIcons.get("power_switch_on")
                                : this.customIcons.get("power_switch_off")));
    }

    protected boolean isAllowedToWorkButtonEnabled() {
        return true;
    }

    private void addTitleTextStyle(ModularPanel panel, String title) {
        panel.child(
            new ParentWidget<>().coverChildren()
                .topRel(0, -4, 1)
                .leftRel(0, -4, 0)
                .widgetTheme(GTWidgetThemes.BACKGROUND_TITLE)
                .child(
                    IKey.str(title)
                        .asWidget()
                        .alignment(Alignment.Center)
                        .widgetTheme(GTWidgetThemes.TEXT_TITLE)
                        .marginLeft(5)
                        .marginRight(5)
                        .marginTop(5)
                        .marginBottom(1)));
    }

    protected int[] machineInfoSize() {
        return base.doesBindPlayerInventory() ? new int[] { 184, 91 } : new int[] { 184, 171 };
    }

    protected int[] mainTerminalSize() {
        return base.doesBindPlayerInventory() ? new int[] { 190, 91 } : new int[] { 190, 171 };
    }

    protected IWidget createPanelGap(PanelSyncManager syncManager, ModularPanel parent) {
        Flow panelGap = new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createVoidExcessButton(syncManager))
            .child(createInputSeparationButton(syncManager));

        if (!machineModeIcons.isEmpty()) panelGap.child(createModeSwitchButton(syncManager));
        panelGap.child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager));
        if (base.supportsPowerPanel()) panelGap.child(createPowerPanelButton(syncManager, parent));

        return panelGap;
    }

    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return createTerminalTextWidget(syncManager);
    }

    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager) {
        ListWidget<IWidget, ?> resultWidget = new ListWidget<>()
            .child(
                new TextWidget(GTUtility.trans("132", "Pipe is loose. (Wrench)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mWrench)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(GTUtility.trans("133", "Screws are loose. (Screwdriver)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mScrewdriver)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(GTUtility.trans("134", "Something is stuck. (Soft Mallet)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mSoftHammer)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(GTUtility.trans("135", "Platings are dented. (Hammer)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mHardHammer)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(GTUtility.trans("136", "Circuitry burned out. (Soldering)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mSolderingTool)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(GTUtility.trans("137", "That doesn't belong there. (Crowbar)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mCrowbar)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(GTUtility.trans("138", "Incomplete Structure.")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mMachine)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.too_uncertain")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> (base.getErrorDisplayID() & 128) != 0)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.invalid_parameters"))
                    .color(Color.WHITE.main)
                    .setEnabledIf(widget -> (base.getErrorDisplayID() & 256) != 0)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget(
                    GTUtility.trans("139", "Hit with Soft Mallet") + "\n"
                        + GTUtility.trans("140", "to (re-)start the Machine")
                        + "\n"
                        + GTUtility.trans("141", "if it doesn't start.")).color(Color.WHITE.main)
                            .setEnabledIf(
                                widget -> base.getErrorDisplayID() == 0 && !base.getBaseMetaTileEntity()
                                    .isActive()
                                    && !base.getBaseMetaTileEntity()
                                        .isAllowedToWork())
                            .marginBottom(2)
                            .widthRel(1))
            .child(
                new TextWidget(GTUtility.trans("142", "Running perfectly.")).color(Color.WHITE.main)
                    .setEnabledIf(
                        widget -> base.getErrorDisplayID() == 0 && base.getBaseMetaTileEntity()
                            .isActive())
                    .marginBottom(2)
                    .widthRel(1))
            .child(createShutdownDurationWidget())
            .child(createShutdownReasonWidget())
            .child(createRecipeResultWidget());

        if (base.showRecipeTextInGUI()) {
            // Display current recipe
            resultWidget.child(createRecipeInfoWidget(syncManager));
        }
        resultWidget.onUpdateListener((unused) -> {
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(resultWidget);
            }
        });

        return resultWidget;
    }

    private IWidget createRecipeInfoWidget(PanelSyncManager syncManager) {
        return IKey.dynamic(() -> ((StringSyncValue) syncManager.getSyncHandler("recipeInfo:0")).getValue())
            .asWidget()
            .marginBottom(2)
            .widthRel(1)
            .setEnabledIf(
                widget -> (((GenericListSyncHandler<?>) syncManager.getSyncHandler("itemOutput:0")).getValue() != null
                    && !((GenericListSyncHandler<?>) syncManager.getSyncHandler("itemOutput:0")).getValue()
                        .isEmpty())
                    || ((GenericListSyncHandler<?>) syncManager.getSyncHandler("fluidOutput:0")).getValue() != null
                        && !((GenericListSyncHandler<?>) syncManager.getSyncHandler("fluidOutput:0")).getValue()
                            .isEmpty());
    }

    private IWidget createShutdownDurationWidget() {
        return IKey.dynamic(() -> {
            Duration time = Duration.ofSeconds((base.getTotalRunTime() - base.getLastWorkingTick()) / 20);
            return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.shutdown_duration",
                time.toHours(),
                time.toMinutes() % 60,
                time.getSeconds() % 60);
        })
            .asWidget()
            .marginBottom(2)
            .widthRel(1)
            .setEnabledIf(
                widget -> base.shouldDisplayShutDownReason() && !base.getBaseMetaTileEntity()
                    .isActive()
                    && !base.getBaseMetaTileEntity()
                        .isAllowedToWork());
    }

    private IWidget createShutdownReasonWidget() {
        return IKey.dynamic(
            () -> base.getBaseMetaTileEntity()
                .getLastShutDownReason()
                .getDisplayString())
            .asWidget()
            .marginBottom(2)
            .widthRel(1)
            .setEnabledIf(
                widget -> base.shouldDisplayShutDownReason() && !base.getBaseMetaTileEntity()
                    .isActive()
                    && !base.getBaseMetaTileEntity()
                        .isAllowedToWork()
                    && GTUtility.isStringValid(
                        base.getBaseMetaTileEntity()
                            .getLastShutDownReason()
                            .getDisplayString()));
    }

    private IWidget createRecipeResultWidget() {
        return IKey.dynamic(
            () -> base.getCheckRecipeResult()
                .getDisplayString())
            .asWidget()
            .marginBottom(2)
            .widthRel(1)
            .setEnabledIf(
                widget -> base.shouldDisplayCheckRecipeResult() && GTUtility.isStringValid(
                    base.getCheckRecipeResult()
                        .getDisplayString())
                    && (base.isAllowedToWork() || base.getBaseMetaTileEntity()
                        .isActive()
                        || base.getCheckRecipeResult()
                            .persistsOnShutdown()));
    }

    protected IWidget createStructureUpdateButton(PanelSyncManager syncManager) {
        IntSyncValue structureUpdateSyncer = new IntSyncValue(
            base::getStructureUpdateTime,
            base::setStructureUpdateTime);
        syncManager.syncValue("structureUpdate", structureUpdateSyncer);

        return new ToggleButton().size(18, 18)
            .value(
                new BooleanSyncValue(
                    () -> structureUpdateSyncer.getValue() > -20,
                    val -> { if (val) structureUpdateSyncer.setValue(1); }))
            .overlay(GTGuiTextures.OVERLAY_BUTTON_STRUCTURE_UPDATE)
            .tooltipBuilder(t -> { t.addLine(IKey.lang("GT5U.gui.button.structure_update")); });
    }

    protected IWidget createPowerPanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler powerPanel = syncManager
            .panel("powerPanel", (p_syncManager, syncHandler) -> openPowerControlPanel(p_syncManager, parent), true);

        return new ButtonWidget<>().size(18, 18)
            .rightRel(0, 6, 0)
            .marginTop(4)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/power_panel"))
            .onMousePressed(d -> {
                if (!powerPanel.isPanelOpen()) {
                    powerPanel.openPanel();
                } else {
                    powerPanel.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.power_panel")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openPowerControlPanel(PanelSyncManager syncManager, ModularPanel parent) {
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;

        return new ModularPanel("powerPanel").pos(x, y)
            .size(120, 130)
            .child(
                new Column().sizeRel(1)
                    .padding(3)
                    .child(makeTitleTextWidget())
                    .child(
                        IKey.lang("GTPP.CC.parallel")
                            .asWidget()
                            .marginBottom(4))
                    .child(makeParallelConfigurator(syncManager))

            );
    }

    private IWidget makeTitleTextWidget() {
        return new TextWidget(
            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("GT5U.gui.text.power_panel"))
                .alignment(Alignment.Center)
                .size(120, 18)
                .marginBottom(4);
    }

    private IWidget makeParallelConfigurator(PanelSyncManager syncManager) {
        IntSyncValue maxParallelSyncer = new IntSyncValue(base::getMaxParallelRecipes, base::setMaxParallelForPanel);
        syncManager.syncValue("maxParallel", maxParallelSyncer);
        BooleanSyncValue alwaysMaxParallelSyncer = new BooleanSyncValue(
            base::isAlwaysMaxParallel,
            base::setAlwaysMaxParallel);
        syncManager.syncValue("alwaysMaxParallel", alwaysMaxParallelSyncer);

        IntSyncValue powerPanelMaxParallelSyncer = new IntSyncValue(
            base::getPowerPanelMaxParallel,
            base::setPowerPanelMaxParallel);

        return new Row().widthRel(1)
            .height(18)
            .paddingLeft(3)
            .paddingRight(3)
            .mainAxisAlignment(MainAxis.CENTER)
            .child(
                new TextFieldWidget().value(powerPanelMaxParallelSyncer)
                    .setTextAlignment(Alignment.Center)
                    .setNumbers(
                        () -> alwaysMaxParallelSyncer.getValue() ? maxParallelSyncer.getValue() : 1,
                        maxParallelSyncer::getValue)
                    .tooltipBuilder(
                        t -> t.addLine(
                            IKey.dynamic(
                                () -> alwaysMaxParallelSyncer.getValue()
                                    ? StatCollector.translateToLocalFormatted(
                                        "GT5U.gui.text.lockedvalue",
                                        maxParallelSyncer.getValue())
                                    : StatCollector.translateToLocalFormatted(
                                        "GT5U.gui.text.rangedvalue",
                                        1,
                                        maxParallelSyncer.getValue()))))
                    .tooltipShowUpTimer(TOOLTIP_DELAY)
                    .size(70, 14)
                    .marginBottom(4))
            .child(
                new ButtonWidget<>().size(18, 18)
                    .overlay(new DynamicDrawable(() -> {
                        if (alwaysMaxParallelSyncer.getValue())
                            return UITexture.fullImage(GTGuiTextures.OVERLAY_BUTTON_CHECKMARK.location);
                        return UITexture.fullImage(GTGuiTextures.OVERLAY_BUTTON_CROSS.location);
                    }))
                    .onMousePressed(d -> {
                        alwaysMaxParallelSyncer.setValue(!alwaysMaxParallelSyncer.getValue());
                        powerPanelMaxParallelSyncer.setValue(maxParallelSyncer.getValue());
                        return true;
                    })
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.max_parallel")))
                    .tooltipShowUpTimer(TOOLTIP_DELAY));
    }

    protected IWidget createLockToSingleRecipeButton(PanelSyncManager syncManager) {
        BooleanSyncValue recipeLockSyncer = new BooleanSyncValue(base::isRecipeLockingEnabled, base::setRecipeLocking);
        syncManager.syncValue("recipeLock", recipeLockSyncer);

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!base.supportsSingleRecipeLocking()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(
                new BooleanSyncValue(() -> recipeLockSyncer.getValue() || !base.supportsSingleRecipeLocking(), bool -> {
                    if (base.supportsSingleRecipeLocking()) {
                        recipeLockSyncer.setValue(bool);
                    }
                }))
            .overlay(new DynamicDrawable(() -> {
                UITexture forbidden = GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN;
                if (recipeLockSyncer.getValue()) {
                    if (base.supportsSingleRecipeLocking()) {
                        return GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED;
                    } else {
                        return new DrawableStack(GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED);
                    }
                } else {
                    if (base.supportsSingleRecipeLocking()) {
                        return GTGuiTextures.OVERLAY_BUTTON_RECIPE_UNLOCKED;
                    } else {
                        return new DrawableStack(GTGuiTextures.OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED, forbidden);
                    }
                }
            }))
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang("GT5U.gui.button.lock_recipe"));
                if (!base.supportsSingleRecipeLocking()) t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
            });
    }

    protected IWidget createBatchModeButton(PanelSyncManager syncManager) {
        BooleanSyncValue batchModeSyncer = new BooleanSyncValue(base::isBatchModeEnabled, base::setBatchMode);
        syncManager.syncValue("batchMode", batchModeSyncer);

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!base.supportsBatchMode()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(new BooleanSyncValue(() -> batchModeSyncer.getValue() || !base.supportsBatchMode(), bool -> {
                if (base.supportsBatchMode()) {
                    batchModeSyncer.setValue(bool);
                }
            }))
            .overlay(new DynamicDrawable(() -> {
                UITexture forbidden = GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN;
                if (batchModeSyncer.getValue()) {
                    if (base.supportsBatchMode()) {
                        return GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON;
                    } else {
                        return new DrawableStack(GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
                    }
                } else {

                    if (base.supportsBatchMode()) {
                        return GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF;
                    } else {
                        return new DrawableStack(GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED, forbidden);
                    }
                }
            }))
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang("GT5U.gui.button.batch_mode"));
                if (!base.supportsBatchMode()) t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
            });
    }

    protected IWidget createModeSwitchButton(PanelSyncManager syncManager) {
        IntSyncValue machineModeSyncer = new IntSyncValue(base::getMachineMode, base::setMachineMode);
        syncManager.syncValue("machineMode", machineModeSyncer);

        return new CycleButtonWidget().size(18, 18)
            .value(new IntSyncValue(machineModeSyncer::getValue, machineModeSyncer::setValue))
            .length(machineModeIcons.size())
            .overlay(new DynamicDrawable(() -> getMachineModeIcon(machineModeSyncer.getValue())))
            .tooltipBuilder(t -> {
                t.addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.mode_switch")))
                    .addLine(IKey.dynamic(base::getMachineModeName));
            });
    }

    protected UITexture getMachineModeIcon(int index) {
        if (index > machineModeIcons.size() - 1) return null;
        return machineModeIcons.get(index);
    }

    protected IWidget createInputSeparationButton(PanelSyncManager syncManager) {

        BooleanSyncValue inputSeparationSyncer = new BooleanSyncValue(
            base::isInputSeparationEnabled,
            base::setInputSeparation);
        syncManager.syncValue("inputSeparation", inputSeparationSyncer);

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!base.supportsInputSeparation()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(
                new BooleanSyncValue(
                    () -> inputSeparationSyncer.getValue() || !base.supportsInputSeparation(),
                    bool -> {
                        if (base.supportsInputSeparation()) {
                            inputSeparationSyncer.setValue(bool);
                        }
                    }))
            .overlay(new DynamicDrawable(() -> {
                UITexture forbidden = GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN;
                if (inputSeparationSyncer.getValue()) {
                    if (base.supportsInputSeparation()) {
                        return GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON;
                    } else {
                        return new DrawableStack(GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED, forbidden);
                    }
                } else {

                    if (base.supportsInputSeparation()) {
                        return GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF;
                    } else {
                        return new DrawableStack(GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED, forbidden);
                    }
                }
            }))
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang("GT5U.gui.button.input_separation"));
                if (!base.supportsInputSeparation()) t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
            });
    }

    protected IWidget createVoidExcessButton(PanelSyncManager syncManager) {

        IntSyncValue voidExcessSyncer = new IntSyncValue(
            () -> base.getVoidingMode()
                .ordinal(),
            val -> base.setVoidingMode(VoidingMode.fromOrdinal(val)));
        syncManager.syncValue("voidExcess", voidExcessSyncer);

        return new CycleButtonWidget() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!base.supportsVoidProtection()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(
                new IntSyncValue(
                    voidExcessSyncer::getValue,
                    val -> { if (base.supportsVoidProtection()) voidExcessSyncer.setValue(val); }))
            .length(
                base.getAllowedVoidingModes()
                    .size())
            .overlay(
                new DynamicDrawable(
                    () -> base.supportsVoidProtection() ? base.getVoidingMode().buttonOverlayNew
                        : new DrawableStack(
                            base.getVoidingMode().buttonOverlayNew,
                            GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN)))
            .tooltipBuilder(t -> {
                t.addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.voiding_mode")))
                    .addLine(
                        IKey.dynamic(
                            () -> StatCollector.translateToLocal(
                                base.getVoidingMode()
                                    .getTransKey())));
                if (!base.supportsVoidProtection()) {
                    t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
                }
            });
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {
        syncManager.syncValue(
            "errors",
            new GenericSyncValue<EnumSet<StructureError>>(
                base::getStructureErrors,
                base::setStructureErrors,
                new StructureErrorAdapter()));
        syncManager.syncValue("errorID", new IntSyncValue(base::getErrorDisplayID, base::setErrorDisplayID));
        syncManager.syncValue(
            "machineActive",
            new BooleanSyncValue(
                () -> base.getBaseMetaTileEntity()
                    .isActive(),
                val -> base.getBaseMetaTileEntity()
                    .setActive(val)));

        syncManager.syncValue("wrench", new BooleanSyncValue(() -> base.mWrench, val -> base.mWrench = val));
        syncManager
            .syncValue("screwdriver", new BooleanSyncValue(() -> base.mScrewdriver, val -> base.mScrewdriver = val));
        syncManager
            .syncValue("softHammer", new BooleanSyncValue(() -> base.mSoftHammer, val -> base.mSoftHammer = val));
        syncManager
            .syncValue("hardHammer", new BooleanSyncValue(() -> base.mHardHammer, val -> base.mHardHammer = val));
        syncManager.syncValue(
            "solderingTool",
            new BooleanSyncValue(() -> base.mSolderingTool, val -> base.mSolderingTool = val));
        syncManager.syncValue("crowbar", new BooleanSyncValue(() -> base.mCrowbar, val -> base.mCrowbar = val));
        syncManager.syncValue("machine", new BooleanSyncValue(() -> base.mMachine, val -> base.mMachine = val));

        syncManager.syncValue("totalRunTime", new LongSyncValue(base::getTotalRunTime, base::setTotalRunTime));
        syncManager.syncValue("lastWorkingTick", new LongSyncValue(base::getLastWorkingTick, base::setLastWorkingTick));
        BooleanSyncValue wasShutDown = new BooleanSyncValue(
            () -> base.getBaseMetaTileEntity()
                .wasShutdown(),
            val -> base.getBaseMetaTileEntity()
                .setShutdownStatus(val));
        syncManager.syncValue("wasShutdown", wasShutDown);
        syncManager.syncValue(
            "shutdownReason",
            new GenericSyncValue<ShutDownReason>(
                () -> base.getBaseMetaTileEntity()
                    .getLastShutDownReason(),
                reason -> {
                    base.getBaseMetaTileEntity()
                        .setShutDownReason(reason);
                },
                new ShutdownReasonAdapter()));
        syncManager.syncValue(
            "checkRecipeResult",
            new GenericSyncValue<CheckRecipeResult>(
                base::getCheckRecipeResult,
                base::setCheckRecipeResult,
                new CheckRecipeResultAdapter()));
        syncManager.syncValue(
            "fluidOutput",
            new GenericListSyncHandler<FluidStack>(
                () -> base.mOutputFluids != null ? Arrays.stream(base.mOutputFluids)
                    .map(fluidStack -> {
                        if (fluidStack == null) return null;
                        return new FluidStack(fluidStack, fluidStack.amount) {

                            @Override
                            public boolean isFluidEqual(FluidStack other) {
                                return super.isFluidEqual(other) && amount == other.amount;
                            }
                        };
                    })
                    .collect(Collectors.toList()) : Collections.emptyList(),
                val -> base.mOutputFluids = val.toArray(new FluidStack[0]),
                NetworkUtils::readFluidStack,
                NetworkUtils::writeFluidStack));
        syncManager.syncValue(
            "itemOutput",
            new GenericListSyncHandler<ItemStack>(
                () -> base.mOutputItems != null ? Arrays.asList(base.mOutputItems) : Collections.emptyList(),
                val -> base.mOutputItems = val.toArray(new ItemStack[0]),
                NetworkUtils::readItemStack,
                NetworkUtils::writeItemStack));
        syncManager
            .syncValue("progressTime", new IntSyncValue(() -> base.mProgresstime, val -> base.mProgresstime = val));
        syncManager.syncValue(
            "maxProgressTime",
            new IntSyncValue(() -> base.mMaxProgresstime, val -> base.mMaxProgresstime = val));

        StringSyncValue recipeInfoSyncer = new StringSyncValue(base::generateCurrentRecipeInfoString);
        syncManager.syncValue("recipeInfo", recipeInfoSyncer);
    }

    protected void setMachineModeIcons() {}
}
