package gregtech.api.metatileentity.implementations.gui;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static tectech.Reference.MODID;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DrawableArray;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.GTMod;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.VoidingMode;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public class MTEMultiBlockBaseGUI {

    private final MTEMultiBlockBase base;

    public MTEMultiBlockBaseGUI(MTEMultiBlockBase base) {
        this.base = base;
    }

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager) {

        int textBoxToInventoryGap = 26;

        ModularPanel panel = new ModularPanel("MTEMultiblockBase").size(198, 181 + textBoxToInventoryGap)
            .padding(4);

        registerSyncValues(panel, syncManager);
        ListWidget<IWidget, ?> machineInfo = new ListWidget<>()
            .size(machineInfoSize()[0] - 4, machineInfoSize()[1] - 3);

        Flow panelColumn = new Column().sizeRel(1);
        if (base.doesBindPlayerInventory()) {
            panelColumn.child(
                new SingleChildWidget<>().size(machineInfoSize()[0] + 4, machineInfoSize()[1] + 3)
                    .padding(3)
                    .background(
                        UITexture.builder()
                            .location(GregTech.ID, "gui/background/text_field")
                            .adaptable(1)
                            .imageSize(142, 28)
                            .build()
                            .asIcon())
                    .child(machineInfo));
        } else {
            panelColumn.child(
                new SingleChildWidget<>().size(190, 171)
                    .child(machineInfo));
        }
        Flow inventoryRow = new Row().widthRel(1)
            .height(76)
            .alignX(0);
        Flow buttonColumn = new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(com.cleanroommc.modularui.utils.Alignment.MainAxis.END);
        if (base.doesBindPlayerInventory()) {
            inventoryRow.child(
                SlotGroupWidget.playerInventory(0)
                    .leftRel(0)
                    .marginLeft(4));
        }

        Flow panelGap = new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap);
        insertThingsInGap(panelGap, syncManager, panel);
        panelColumn.child(panelGap);

        insertTexts(machineInfo, base.getInvSlot(), syncManager, panel);
        addTitleTextStyle(panel, base.getLocalName());

        buttonColumn.child(createStructureUpdateButton(syncManager));
        if (shouldMakePowerSwitchButtonEnabled()) addPowerSwitchButtton(buttonColumn);
        addGregtechLogo(panel);

        if (base.doesBindPlayerInventory()) {
            buttonColumn.child(
                new ItemSlot().slot(
                    SyncHandlers.itemSlot(base.getInvSlot(), 0)
                        .singletonSlotGroup())
                    .marginTop(4));
        }
        inventoryRow.child(buttonColumn);
        panelColumn.child(inventoryRow);

        return panel.child(panelColumn);
    }

    public boolean shouldMakePowerSwitchButtonEnabled() {
        return true;
    }

    public void addPowerSwitchButtton(Flow buttonColumn) {
        UITexture powerSwitchOn = UITexture.fullImage(GregTech.ID, "gui/overlay_button/power_switch_on");
        UITexture powerSwitchOff = UITexture.fullImage(GregTech.ID, "gui/overlay_button/power_switch_off");
        UITexture powerSwitchDisabled = UITexture.fullImage(MODID, "gui/overlay_button/power_switch_disabled");

        buttonColumn.child(new ToggleButton().value(new BooleanSyncValue(base::isAllowedToWork, bool -> {
            if (!isAllowedToWorkButtonEnabled()) return;
            if (bool) base.enableWorking();
            else base.disableWorking();
        }))
            .tooltip(tooltip -> tooltip.add("Power Switch"))
            .size(18, 18)
            .overlay(
                new DynamicDrawable(
                    () -> !isAllowedToWorkButtonEnabled() ? powerSwitchDisabled
                        : base.getBaseMetaTileEntity()
                            .isAllowedToWork() ? powerSwitchOn : powerSwitchOff)));
    }

    public boolean isAllowedToWorkButtonEnabled() {
        return true;
    }

    protected void addTitleTextStyle(ModularPanel panel, String title) {
        final int TAB_PADDING = 3;
        final int TITLE_PADDING = 2;
        int titleWidth = 0, titleHeight = 0;
        if (NetworkUtils.isClient()) {
            final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            final List<String> titleLines = fontRenderer
                .listFormattedStringToWidth(title, base.getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
            titleWidth = titleLines.size() > 1 ? base.getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
                : fontRenderer.getStringWidth(title);
            // noinspection PointlessArithmeticExpression
            titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1) * 1;
        }

        final SingleChildWidget<?> tab = new SingleChildWidget<>();
        final com.cleanroommc.modularui.widgets.TextWidget text = new com.cleanroommc.modularui.widgets.TextWidget(
            title).color(0x404040)
                .alignment(com.cleanroommc.modularui.utils.Alignment.CenterLeft)
                .width(titleWidth);

        UITexture angular = UITexture.builder()
            .location(GregTech.ID, "gui/tab/title_angular_%s")
            .adaptable(4)
            .imageSize(18, 18)
            .canApplyTheme(true)
            .build();
        UITexture dark = UITexture.builder()
            .location(GregTech.ID, "gui/tab/title_dark")
            .adaptable(4)
            .imageSize(28, 28)
            .canApplyTheme(true)
            .build();
        if (GTMod.gregtechproxy.mTitleTabStyle == 1) {
            panel.child(
                angular.asWidget()
                    .pos(0, -(titleHeight + TAB_PADDING) + 1)
                    .size(base.getGUIWidth(), titleHeight + TAB_PADDING * 2));
            text.pos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            panel.child(
                dark.asWidget()
                    .pos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                    .size(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1));
            text.pos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        panel.child(text);
    }

    protected int[] machineInfoSize() {
        return new int[] { 184, 91 };
    }

    protected int[] mainTerminalSize() {
        return new int[] { 190, 91 };
    }

    public void insertThingsInGap(Flow panelGap, PanelSyncManager syncManager, ModularPanel parent) {
        UITexture noMaint = UITexture.builder()
            .location(GregTech.ID, "gui/icons/noMaint")
            .imageSize(16, 16)
            .build();
        UITexture checkmark = UITexture.builder()
            .location(GregTech.ID, "gui/overlay_button/checkmark")
            .imageSize(16, 16)
            .build();

        panelGap.child(createVoidExcessButton(syncManager))
            .child(createInputSeparationButton(syncManager));
        if (base.supportsMachineModeSwitch()) panelGap.child(createModeSwitchButton(syncManager));
        panelGap.child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager));
        if (base.supportsPowerPanel()) panelGap.child(createPowerPanelButton(syncManager, parent));
    }

    public void insertTexts(ListWidget<IWidget, ?> machineInfo, ItemStackHandler invSlot, PanelSyncManager syncManager,
        ModularPanel parentPanel) {
        machineInfo.child(
            new com.cleanroommc.modularui.widgets.TextWidget(GTUtility.trans("132", "Pipe is loose. (Wrench)"))
                .color(Color.WHITE.main)
                .setEnabledIf(widget -> !base.mWrench)
                .marginBottom(2)
                .widthRel(1)

        );

        machineInfo.child(
            new com.cleanroommc.modularui.widgets.TextWidget(GTUtility.trans("133", "Screws are loose. (Screwdriver)"))
                .color(Color.WHITE.main)
                .setEnabledIf(widget -> !base.mScrewdriver)
                .marginBottom(2)
                .widthRel(1)

        );

        machineInfo.child(

            new com.cleanroommc.modularui.widgets.TextWidget(
                GTUtility.trans("134", "Something is stuck. (Soft Mallet)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mSoftHammer)
                    .marginBottom(2)
                    .widthRel(1)

        );
        machineInfo.child(

            new com.cleanroommc.modularui.widgets.TextWidget(GTUtility.trans("135", "Platings are dented. (Hammer)"))
                .color(Color.WHITE.main)
                .setEnabledIf(widget -> !base.mHardHammer)
                .marginBottom(2)
                .widthRel(1)

        );

        machineInfo.child(

            new com.cleanroommc.modularui.widgets.TextWidget(
                GTUtility.trans("136", "Circuitry burned out. (Soldering)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mSolderingTool)
                    .marginBottom(2)
                    .widthRel(1)

        );

        machineInfo.child(

            new com.cleanroommc.modularui.widgets.TextWidget(
                GTUtility.trans("137", "That doesn't belong there. (Crowbar)")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> !base.mCrowbar)
                    .marginBottom(2)
                    .widthRel(1)

        );

        machineInfo.child(
            new com.cleanroommc.modularui.widgets.TextWidget(GTUtility.trans("138", "Incomplete Structure."))
                .color(Color.WHITE.main)
                .setEnabledIf(widget -> !base.mMachine)
                .marginBottom(2)
                .widthRel(1)

        );

        machineInfo.child(
            new com.cleanroommc.modularui.widgets.TextWidget(
                StatCollector.translateToLocal("GT5U.gui.text.too_uncertain")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> (base.getErrorDisplayID() & 128) != 0)
                    .marginBottom(2)
                    .widthRel(1)

        );

        machineInfo.child(
            new com.cleanroommc.modularui.widgets.TextWidget(
                StatCollector.translateToLocal("GT5U.gui.text.invalid_parameters")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> (base.getErrorDisplayID() & 256) != 0)
                    .marginBottom(2)
                    .widthRel(1)

        );

        machineInfo.child(
            new com.cleanroommc.modularui.widgets.TextWidget(
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
                        .widthRel(1)

        );

        machineInfo.child(
            new com.cleanroommc.modularui.widgets.TextWidget(GTUtility.trans("142", "Running perfectly."))
                .color(Color.WHITE.main)
                .setEnabledIf(
                    widget -> base.getErrorDisplayID() == 0 && base.getBaseMetaTileEntity()
                        .isActive())
                .marginBottom(2)
                .widthRel(1)

        );

        com.cleanroommc.modularui.widgets.TextWidget shutdownDuration = IKey.dynamic(() -> {
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

        machineInfo.child(shutdownDuration);

        com.cleanroommc.modularui.widgets.TextWidget shutdownReason = IKey.dynamic(
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

        machineInfo.child(shutdownReason);

        com.cleanroommc.modularui.widgets.TextWidget checkRecipeResultWidget = IKey
            .dynamic(
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
        machineInfo.child(checkRecipeResultWidget);

        if (base.showRecipeTextInGUI()) {
            // Display current recipe
            com.cleanroommc.modularui.widgets.TextWidget recipeInfoWidget = IKey
                .dynamic(() -> ((StringSyncValue) syncManager.getSyncHandler("recipeInfo:0")).getValue())
                .asWidget()
                .marginBottom(2)
                .widthRel(1)
                .setEnabledIf(
                    widget -> (((GenericListSyncHandler<?>) syncManager.getSyncHandler("itemOutput:0")).getValue()
                        != null
                        && !((GenericListSyncHandler<?>) syncManager.getSyncHandler("itemOutput:0")).getValue()
                            .isEmpty())
                        || ((GenericListSyncHandler<?>) syncManager.getSyncHandler("fluidOutput:0")).getValue() != null
                            && !((GenericListSyncHandler<?>) syncManager.getSyncHandler("fluidOutput:0")).getValue()
                                .isEmpty());
            machineInfo.child(recipeInfoWidget);
        }
        machineInfo.onUpdateListener((bla) -> {
            if (NetworkUtils.isClient()) {
                WidgetTree.resize(machineInfo);
            }
        });
    }

    public void addGregtechLogo(ModularPanel panel) {
        panel.child(
            new SingleChildWidget<>().overlay(UITexture.fullImage(MODID, "gui/picture/tectech_logo_dark"))
                .size(18, 18)
                .pos(190 - 18 - 2, base.doesBindPlayerInventory() ? 91 - 18 - 2 : 171 - 18 - 2));
    }

    public IWidget createStructureUpdateButton(PanelSyncManager syncManager) {
        IntSyncValue structureUpdateSyncer = new IntSyncValue(
            base::getStructureUpdateTime,
            base::setStructureUpdateTime);
        syncManager.syncValue("structureUpdate", structureUpdateSyncer);

        ToggleButton structureUpdateButton = new ToggleButton().size(18, 18)
            .value(
                new BooleanSyncValue(
                    () -> structureUpdateSyncer.getValue() > -20,
                    val -> { if (val) structureUpdateSyncer.setValue(1); }))
            .overlay(GTGuiTextures.OVERLAY_BUTTON_STRUCTURE_UPDATE)
            .tooltipBuilder(t -> { t.addLine(IKey.lang("GT5U.gui.button.structure_update")); });

        return structureUpdateButton;
    }

    public IWidget createPowerPanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler bla = syncManager.panel(
            "powerPanel",
            (p_syncManager, syncHandler) -> openPowerControlPanel(p_syncManager, syncHandler, parent),
            true);

        return new com.cleanroommc.modularui.widgets.ButtonWidget<>().size(18, 18)
            .rightRel(0, 6, 0)
            .marginTop(4)
            .overlay(UITexture.fullImage(GregTech.ID, "gui/overlay_button/power_panel"))
            .onMousePressed(d -> {
                if (!bla.isPanelOpen()) {
                    bla.openPanel();
                } else {
                    bla.closePanel();
                }
                return true;
            })
            .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.power_panel")))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private ModularPanel openPowerControlPanel(PanelSyncManager syncManager, IPanelHandler thisPanel,
        ModularPanel parent) {
        Area area = parent.getArea();
        int x = area.x + area.width;
        int y = area.y;
        ModularPanel panel = new ModularPanel("powerPanel").pos(x, y)
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

        return panel;
    }

    private IWidget makeTitleTextWidget() {
        return new com.cleanroommc.modularui.widgets.TextWidget(
            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("GT5U.gui.text.power_panel"))
                .alignment(com.cleanroommc.modularui.utils.Alignment.Center)
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
            .mainAxisAlignment(com.cleanroommc.modularui.utils.Alignment.MainAxis.CENTER)
            .child(
                new TextFieldWidget().value(powerPanelMaxParallelSyncer)
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
                new com.cleanroommc.modularui.widgets.ButtonWidget<>().size(18, 18)
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

    public IWidget createLockToSingleRecipeButton(PanelSyncManager syncManager) {
        BooleanSyncValue recipeLockSyncer = new BooleanSyncValue(base::isRecipeLockingEnabled, base::setRecipeLocking);
        syncManager.syncValue("recipeLock", recipeLockSyncer);

        ToggleButton lockToSingleRecipeButton = new ToggleButton() {

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
                        recipeLockSyncer.setValue(!recipeLockSyncer.getValue());
                    }
                }))
            .overlay(new DynamicDrawable(() -> {
                UITexture forbidden = GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN;
                if (recipeLockSyncer.getValue()) {
                    if (base.supportsSingleRecipeLocking()) {
                        return GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED;
                    } else {
                        return new DrawableArray(GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED);
                    }
                } else {

                    if (base.supportsSingleRecipeLocking()) {
                        return GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED;
                    } else {
                        return new DrawableArray(GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED, forbidden);
                    }
                }
            }))
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang("GT5U.gui.button.lock_recipe"));
                if (!base.supportsSingleRecipeLocking()) t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
            });

        return lockToSingleRecipeButton;
    }

    public IWidget createBatchModeButton(PanelSyncManager syncManager) {
        BooleanSyncValue batchModeSyncer = new BooleanSyncValue(base::isBatchModeEnabled, base::setBatchMode);
        syncManager.syncValue("batchMode", batchModeSyncer);

        ToggleButton batchModeButton = new ToggleButton() {

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
                        return new DrawableArray(GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
                    }
                } else {

                    if (base.supportsBatchMode()) {
                        return GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF;
                    } else {
                        return new DrawableArray(GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED, forbidden);
                    }
                }
            }))
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang("GT5U.gui.button.batch_mode"));
                if (!base.supportsBatchMode()) t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
            });
        return batchModeButton;
    }

    public IWidget createModeSwitchButton(PanelSyncManager syncManager) {
        IntSyncValue machineModeSyncer = new IntSyncValue(base::getMachineMode, base::setMachineMode);
        syncManager.syncValue("machineMode", machineModeSyncer);

        CycleButtonWidget machineModeButton = new CycleButtonWidget().size(18, 18)
            .value(
                new IntSyncValue(
                    machineModeSyncer::getValue,
                    val -> { if (base.supportsMachineModeSwitch()) machineModeSyncer.setValue(val); }))
            .length(machineModes())
            .overlay(new DynamicDrawable(() -> {
                com.gtnewhorizons.modularui.api.drawable.UITexture bla = base
                    .getMachineModeIcon(machineModeSyncer.getValue());
                return UITexture.builder()
                    .location(bla.location)
                    .imageSize(18, 18)
                    .build();
            }))
            .tooltipBuilder(t -> {
                t.addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.mode_switch")))
                    .addLine(
                        IKey.dynamic(
                            () -> StatCollector.translateToLocal(
                                base.getVoidingMode()
                                    .getTransKey())));
                if (!base.supportsVoidProtection()) {
                    t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
                }
            });
        return machineModeButton;
    }

    public int machineModes() {
        return 2;
    }

    public IWidget createInputSeparationButton(PanelSyncManager syncManager) {

        BooleanSyncValue inputSeparationSyncer = new BooleanSyncValue(
            base::isInputSeparationEnabled,
            base::setInputSeparation);
        syncManager.syncValue("inputSeparation", inputSeparationSyncer);

        ToggleButton inputSeparationButton = new ToggleButton() {

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
                        return new DrawableArray(GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED, forbidden);
                    }
                } else {

                    if (base.supportsInputSeparation()) {
                        return GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF;
                    } else {
                        return new DrawableArray(GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED, forbidden);
                    }
                }
            }))
            .tooltipBuilder(t -> {
                t.addLine(IKey.lang("GT5U.gui.button.input_separation"));
                if (!base.supportsInputSeparation()) t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
            });

        return inputSeparationButton;
    }

    public IWidget createVoidExcessButton(PanelSyncManager syncManager) {

        IntSyncValue voidExcessSyncer = new IntSyncValue(
            () -> base.getVoidingMode()
                .ordinal(),
            val -> base.setVoidingMode(VoidingMode.fromOrdinal(val)));
        syncManager.syncValue("voidExcess", voidExcessSyncer);

        CycleButtonWidget voidExcessButton = new CycleButtonWidget() {

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
            .background(
                new DynamicDrawable(
                    () -> UITexture.builder()
                        .location(base.getVoidingMode().buttonTexture.location)
                        .canApplyTheme(true)
                        .build()))
            .stateHoverOverlay(base.supportsVoidProtection(), GuiTextures.MC_BUTTON_PRESSED)
            .overlay(
                new DynamicDrawable(
                    () -> base.supportsVoidProtection()
                        ? UITexture.fullImage(base.getVoidingMode().buttonOverlay.location)
                        : new DrawableArray(
                            UITexture.fullImage(base.getVoidingMode().buttonOverlay.location),
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
        return voidExcessButton;
    }

    public void registerSyncValues(ModularPanel panel, PanelSyncManager syncManager) {
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

    private class StructureErrorAdapter implements IByteBufAdapter<EnumSet<StructureError>> {

        @Override
        public EnumSet<StructureError> deserialize(PacketBuffer buffer) {
            byte[] data = new byte[buffer.readVarIntFromBuffer()];
            buffer.readBytes(data);

            BitSet bits = BitSet.valueOf(data);

            EnumSet<StructureError> out = EnumSet.noneOf(StructureError.class);

            for (StructureError error : StructureError.values()) {
                if (bits.get(error.ordinal())) {
                    out.add(error);
                }
            }

            return out;
        }

        @Override
        public void serialize(PacketBuffer buffer, EnumSet<StructureError> errors) {
            BitSet bits = new BitSet();

            for (StructureError error : errors) {
                bits.set(error.ordinal());
            }

            byte[] data = bits.toByteArray();

            buffer.writeVarIntToBuffer(data.length);
            buffer.writeBytes(data);
        }

        @Override
        public boolean areEqual(@NotNull EnumSet<StructureError> t1, @NotNull EnumSet<StructureError> t2) {
            return false;
        }
    }

    private class ShutdownReasonAdapter implements IByteBufAdapter<ShutDownReason> {

        @Override
        public ShutDownReason deserialize(PacketBuffer buffer) throws IOException {
            String id = NetworkUtils.readStringSafe(buffer);
            ShutDownReason result = ShutDownReasonRegistry.getSampleFromRegistry(id)
                .newInstance();
            result.decode(buffer);
            return result;
        }

        @Override
        public void serialize(PacketBuffer buffer, ShutDownReason result) throws IOException {
            NetworkUtils.writeStringSafe(buffer, result.getID());
            result.encode(buffer);
        }

        @Override
        public boolean areEqual(@NotNull ShutDownReason t1, @NotNull ShutDownReason t2) {
            return false;
        }
    }

    private class CheckRecipeResultAdapter implements IByteBufAdapter<CheckRecipeResult> {

        @Override
        public CheckRecipeResult deserialize(PacketBuffer buffer) throws IOException {
            String id = NetworkUtils.readStringSafe(buffer);
            CheckRecipeResult result = CheckRecipeResultRegistry.getSampleFromRegistry(id)
                .newInstance();
            result.decode(buffer);
            return result;
        }

        @Override
        public void serialize(PacketBuffer buffer, CheckRecipeResult result) throws IOException {
            NetworkUtils.writeStringSafe(buffer, result.getID());
            result.encode(buffer);
        }

        @Override
        public boolean areEqual(@NotNull CheckRecipeResult t1, @NotNull CheckRecipeResult t2) {
            return false;
        }
    }
}
