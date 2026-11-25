package gregtech.common.gui.modularui.multiblock.base;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_DISABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FEATURE_ENABLED_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.HoverableIcon;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Alignment.MainAxis;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.ec.OrderedSectionBuilder;
import gregtech.api.ec.component.IGuiSyncableComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.ec.component.gui.IGuiProviderComponent;
import gregtech.api.ec.component.gui.MultiblockGuiBuilder;
import gregtech.api.ec.component.multiblock.IMaintenanceLogicComponent;
import gregtech.api.ec.component.multiblock.IStructureLogicComponent;
import gregtech.api.ec.implementations.MTEMultiBlockECBase;
import gregtech.api.enums.VoidingMode;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.gui.ItemDisplayKey;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.api.structure.error.IStructureErrorGui;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.adapter.ShutdownReasonAdapter;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.modularui2.sync.Predicates;

public class MTEMultiBlockECBaseGui<T extends MTEMultiBlockECBase<T>> implements IStructureErrorGui {

    protected final T multiblock;
    protected final IGregTechTileEntity baseMetaTileEntity;
    protected Map<String, UITexture> customIcons = new HashMap<>();
    private static final int borderRadius = 4;
    protected final Map<String, IPanelHandler> panelMap = new HashMap<>();
    protected Map<String, UITexture> shutdownReasonTextureMap = new HashMap<>();
    protected Map<String, String> shutdownReasonTooltipMap = new HashMap<>();

    public MTEMultiBlockECBaseGui(T multiblock) {
        this.multiblock = multiblock;
        this.baseMetaTileEntity = multiblock.getBaseMetaTileEntity();
        initCustomIcons();
        initShutdownMaps();
    }

    protected void initCustomIcons() {
        this.customIcons.put("power_switch_disabled", GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_DISABLED);
        this.customIcons.put("power_switch_on", GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_ON);
        this.customIcons.put("power_switch_off", GTGuiTextures.OVERLAY_BUTTON_POWER_SWITCH_OFF);
    }

    protected void initShutdownMaps() {
        this.shutdownReasonTextureMap
            .put(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE.getKey(), GTGuiTextures.OVERLAY_STRUCTURE_INCOMPLETE);
        this.shutdownReasonTextureMap.put(ShutDownReasonRegistry.POWER_LOSS.getKey(), GTGuiTextures.OVERLAY_POWER_LOSS);
        this.shutdownReasonTextureMap.put(ShutDownReasonRegistry.NO_REPAIR.getKey(), GTGuiTextures.OVERLAY_TOO_DAMAGED);
        this.shutdownReasonTextureMap.put(ShutDownReasonRegistry.NONE.getKey(), GTGuiTextures.OVERLAY_MANUAL_SHUTDOWN);
        this.shutdownReasonTextureMap.put("computation_loss", GTGuiTextures.OVERLAY_COMPUTATION_LOSS);
        this.shutdownReasonTextureMap.put(ShutDownReasonRegistry.NO_ROTOR.getKey(), GTGuiTextures.OVERLAY_ROTOR);
        this.shutdownReasonTextureMap.put(ShutDownReasonRegistry.WIND_LOW.getKey(), GTGuiTextures.OVERLAY_WIND);
        this.shutdownReasonTextureMap.put(ShutDownReasonRegistry.WIND_HIGH.getKey(), GTGuiTextures.OVERLAY_WIND);
        this.shutdownReasonTooltipMap.put(
            ShutDownReasonRegistry.STRUCTURE_INCOMPLETE.getKey(),
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.incomplete"));
        this.shutdownReasonTooltipMap.put(
            ShutDownReasonRegistry.POWER_LOSS.getKey(),
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.powerloss"));
        this.shutdownReasonTooltipMap.put(
            ShutDownReasonRegistry.NO_REPAIR.getKey(),
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.norepair"));
        this.shutdownReasonTooltipMap.put(
            ShutDownReasonRegistry.NONE.getKey(),
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.manualshutdown"));
        this.shutdownReasonTooltipMap.put(
            "computation_loss",
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.computation_loss"));
        this.shutdownReasonTooltipMap.put(
            ShutDownReasonRegistry.NO_ROTOR.getKey(),
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.norotor"));
        this.shutdownReasonTooltipMap.put(
            ShutDownReasonRegistry.WIND_LOW.getKey(),
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.windlow"));
        this.shutdownReasonTooltipMap.put(
            ShutDownReasonRegistry.WIND_HIGH.getKey(),
            EnumChatFormatting.DARK_RED + StatCollector.translateToLocal("GT5U.gui.hoverable.windhigh"));
    }

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);
        MultiblockGuiBuilder builder = new MultiblockGuiBuilder();
        registerDefaultGuiSections(builder);
        for (var comp : multiblock.getComponents(IGuiProviderComponent.KEY)) {
            comp.configureGui(builder);
        }
        multiblock.configureGui(builder);
        ModularPanel panel = getBasePanel(guiData, syncManager, uiSettings);
        initPanelMap(panel, syncManager);
        return panel.child(createMainColumn(panel, syncManager, builder));
    }

    protected void registerDefaultGuiSections(@NotNull MultiblockGuiBuilder builder) {
        var recipeLogic = multiblock.tryGetComponent(IRecipeLogicComponent.KEY);
        var structureLogic = multiblock.getComponent(IStructureLogicComponent.KEY);
        var maintenanceLogic = multiblock.tryGetComponent(IMaintenanceLogicComponent.KEY);
        builder.terminalTexts()
            .add(
                IGuiProviderComponent.TerminalSection.START_UP,
                (panel, syncManager) -> IKey.lang("GT5U.multiblock.startup")
                    .color(Color.WHITE.main)
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .setEnabledIf(w -> structureLogic.getStartUpCheckTimer() > 0)
                    .marginBottom(2)
                    .fullWidth())
            .addIf(
                showRunningTextInGui(),
                IGuiProviderComponent.TerminalSection.RUNNING,
                (panel,
                    syncManager) -> new TextWidget<>(
                        StatCollector.translateToLocalFormatted("gt.interact.desc.mb.running")).color(Color.WHITE.main)
                            .setEnabledIf(
                                widget -> multiblock.isFormed()
                                    && (maintenanceLogic == null || maintenanceLogic.getMaintenanceErrorCount() == 0)
                                    && baseMetaTileEntity.isActive())
                            .marginBottom(2)
                            .fullWidth())
            .add(
                IGuiProviderComponent.TerminalSection.SHUTDOWN_REASON,
                (panel, syncManager) -> createShutdownReasonWidget(syncManager))
            .add(
                IGuiProviderComponent.TerminalSection.STRUCTURE_ERRORS,
                (panel, syncManager) -> createStructureErrorWidget(syncManager));
        if (recipeLogic != null) {
            builder.terminalTexts()
                .add(
                    IGuiProviderComponent.TerminalSection.SHUTDOWN_DURATION,
                    (panel, syncManager) -> createShutdownDurationWidget(syncManager, recipeLogic))
                .add(
                    IGuiProviderComponent.TerminalSection.RECIPE_RESULT,
                    (panel, syncManager) -> createRecipeResultWidget(recipeLogic))
                .addIf(
                    showRecipeTextInGui(),
                    IGuiProviderComponent.TerminalSection.RECIPE_PROGRESS,
                    (panel, syncManager) -> createRecipeInfoTextWidget(syncManager, recipeLogic))
                .addIf(
                    showRecipeTextInGui(),
                    IGuiProviderComponent.TerminalSection.RECIPE_OUTPUTS,
                    (panel, syncManager) -> createRecipeInfoWidget(syncManager))
                .addIf(
                    showPendingOutputsInGui(),
                    IGuiProviderComponent.TerminalSection.PENDING_TITLE,
                    (panel,
                        syncManager) -> new TextWidget<>(
                            StatCollector.translateToLocalFormatted("gt.interact.desc.mb.pending"))
                                .color(Color.WHITE.main)
                                .setEnabledIf(widget -> recipeLogic.hasPendingOutputs())
                                .marginBottom(2)
                                .fullWidth())
                .addIf(
                    showPendingOutputsInGui(),
                    IGuiProviderComponent.TerminalSection.PENDING_OUTPUTS,
                    (panel, syncManager) -> createPendingInfoWidget(syncManager));
        }

        builder.leftGap()
            .addIf(
                shouldDisplayVoidExcess(),
                IGuiProviderComponent.LeftGapSection.VOIDING,
                (panel, syncManager) -> createDisabledButton(
                    VoidingMode.VOID_ALL.buttonOverlay,
                    (t) -> t.addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.voiding_mode")))
                        .addLine(IKey.dynamic(() -> StatCollector.translateToLocal(VoidingMode.VOID_ALL.getTransKey())))
                        .addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP))))
            .addIf(
                shouldDisplayInputSeparation(),
                IGuiProviderComponent.LeftGapSection.INPUT_SEPARATION,
                (panel, syncManager) -> createDisabledButton(
                    GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF,
                    (t) -> buildDisabledFeatureTooltip(
                        t,
                        StatCollector.translateToLocal("GT5U.gui.button.input_separation"))))
            .addIf(
                shouldDisplayBatchMode(),
                IGuiProviderComponent.LeftGapSection.BATCH_MODE,
                (panel, syncManager) -> createDisabledButton(
                    GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF,
                    (t) -> buildDisabledFeatureTooltip(
                        t,
                        StatCollector.translateToLocal("GT5U.gui.button.batch_mode"))))
            .addIf(
                shouldDisplayRecipeLock(),
                IGuiProviderComponent.LeftGapSection.RECIPE_LOCK,
                (panel, syncManager) -> createDisabledButton(
                    GTGuiTextures.OVERLAY_BUTTON_RECIPE_UNLOCKED,
                    (t) -> buildDisabledFeatureTooltip(
                        t,
                        StatCollector.translateToLocal("GT5U.gui.button.lock_recipe"))));
        builder.rightGap()
            .addIf(
                multiblock.supportsPowerPanel(),
                IGuiProviderComponent.RightGapSection.POWER_PANEL,
                (panel, syncManager) -> createPowerPanelButton(syncManager, panel, builder));
        builder.leftCorner();
        builder.rightCorner()
            .addIf(
                supportsShutdownReasonHoverable(),
                IGuiProviderComponent.RightCornerSection.SHUTDOWN_REASON,
                (panel, syncManager) -> createShutdownReasonHoverableTerminal(syncManager))
            .addIf(
                supportsLogo(),
                IGuiProviderComponent.RightCornerSection.LOGO,
                (panel, syncManager) -> makeLogoWidget(syncManager, panel));
        builder.powerPanel()
            .add(IGuiProviderComponent.PowerPanelSection.TITLE, ((panel, syncManager) -> makeTitleTextWidget()))
            .add(
                IGuiProviderComponent.PowerPanelSection.POWERFAIL_EVENTS,
                (panel, syncManager) -> makePowerfailEventsToggleRow());
    }

    public Flow createMainColumn(ModularPanel panel, PanelSyncManager syncManager, MultiblockGuiBuilder builder) {
        return Flow.column()
            .padding(borderRadius)
            .child(createTerminalRow(panel, syncManager, builder))
            .childIf(multiblock.canBeMuffled(), this::createMuffleButton)
            .child(createPanelGap(panel, syncManager, builder))
            .childIf(multiblock.supportsInventoryRow(), () -> createInventoryRow(panel, syncManager));
    }

    protected void initPanelMap(ModularPanel parent, PanelSyncManager syncManager) {}

    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(multiblock, guiData, syncManager, uiSettings).setWidth(getBasePanelWidth())
            .setHeight(getBasePanelHeight())
            .doesAddGregTechLogo(false)
            // Has to be replaced with inventory row to fit buttons
            .doesBindPlayerInventory(false)
            .build();
    }

    protected ToggleButton createMuffleButton() {
        return CommonButtons.createMuffleButton("mufflerSyncer")
            .top(getMufflerPosFromTop())
            .right(-getMufflerPosFromRightOutwards());
    }

    protected int getMufflerPosFromTop() {
        return 0;
    }

    protected int getMufflerPosFromRightOutwards() {
        return 13;
    }

    protected int getBasePanelWidth() {
        return 198;
    }

    protected int getBasePanelHeight() {
        return 181 + getTextBoxToInventoryGap();
    }

    protected int getTextBoxToInventoryGap() {
        return 22;
    }

    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager, MultiblockGuiBuilder builder) {
        return Flow.row()
            .size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(createTerminalParentWidget(panel, syncManager, builder));
    }

    protected ParentWidget<?> createTerminalParentWidget(ModularPanel panel, PanelSyncManager syncManager,
        MultiblockGuiBuilder builder) {
        return new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
            .paddingTop(4)
            .paddingBottom(4)
            .paddingLeft(4)
            .paddingRight(0)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .child(
                createTerminalTextWidget(syncManager, panel, builder)
                    .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                    .collapseDisabledChild())
            .childIf(
                supportsTerminalRightCornerColumn(),
                () -> createTerminalRightCornerColumn(panel, syncManager, builder))
            .childIf(
                supportsTerminalLeftCornerColumn(),
                () -> createTerminalLeftCornerColumn(panel, syncManager, builder));
    }

    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager,
        MultiblockGuiBuilder builder) {
        var column = Flow.column()
            .coverChildren()
            .rightRel(0, 6, 0)
            .bottomRel(0, 6, 0);
        appendWidgets(builder.rightCorner(), column::child, panel, syncManager);
        return column;
    }

    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return new IDrawable.DrawableWidget(IDrawable.EMPTY).size(18)
            .marginTop(4)
            .widgetTheme(GTWidgetThemes.PICTURE_LOGO);
    }

    protected Flow createTerminalLeftCornerColumn(ModularPanel panel, PanelSyncManager syncManager,
        MultiblockGuiBuilder builder) {
        var column = Flow.column()
            .coverChildren()
            .leftRel(0, 6, 0)
            .bottomRel(0, 6, 0);
        appendWidgets(builder.leftCorner(), column::child, panel, syncManager);
        return column;
    }

    protected int getTerminalRowWidth() {
        return 190;
    }

    protected int getTerminalRowHeight() {
        return multiblock.doesBindPlayerInventory() ? 94 : 174;
    }

    protected int getTerminalWidgetWidth() {
        return getTerminalRowWidth();
    }

    protected int getTerminalWidgetHeight() {
        return getTerminalRowHeight();
    }

    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent,
        MultiblockGuiBuilder builder) {
        var widgetList = new ListWidget<>().fullWidth()
            .crossAxisAlignment(Alignment.CrossAxis.START);
        appendWidgets(builder.terminalTexts(), widgetList::child, parent, syncManager);
        return widgetList;
    }

    protected IWidget createShutdownDurationWidget(PanelSyncManager syncManager,
        @NotNull IRecipeLogicComponent recipeLogic) {
        return IKey.dynamic(() -> {
            long seconds = (multiblock.getTotalRunTime() - recipeLogic.getLastWorkingTick()) / 20;
            Duration time = Duration.ofSeconds(seconds);
            return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.shutdown_duration",
                time.toHours(),
                time.toMinutes() % 60,
                time.getSeconds() % 60);
        })
            .asWidget()
            .fullWidth()
            .marginBottom(2)
            .setEnabledIf(
                widget -> multiblock.shouldDisplayShutDownReason() && !baseMetaTileEntity.isActive()
                    && !baseMetaTileEntity.isAllowedToWork());

    }

    protected IWidget createShutdownReasonWidget(PanelSyncManager syncManager) {
        return IKey.dynamic(
            () -> baseMetaTileEntity.getLastShutDownReason()
                .getDisplayString())
            .asWidget()
            .fullWidth()
            .marginBottom(2)
            .setEnabledIf(
                widget -> shouldShutdownReasonBeDisplayed(
                    baseMetaTileEntity.getLastShutDownReason()
                        .getDisplayString()));
    }

    protected boolean shouldShutdownReasonBeDisplayed(String shutdownString) {
        return multiblock.shouldDisplayShutDownReason() && !baseMetaTileEntity.isActive()
            && !baseMetaTileEntity.isAllowedToWork()
            && GTUtility.isStringValid(shutdownString);
    }

    protected IWidget createStructureErrorWidget(PanelSyncManager syncManager) {
        GenericListSyncHandler<StructureError> errors = syncManager
            .findSyncHandler("structureErrors", GenericListSyncHandler.class);

        DynamicSyncHandler errorSyncer = new DynamicSyncHandler().widgetProvider((syncManager1, packet) -> {
            Flow columns = Flow.column()
                .coverChildrenHeight(0)
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .childPadding(2);

            for (StructureError error : errors.getValue()) {
                // For now just skip these errors, they will be present in most multiblock and will cause confusion.
                if (error == StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR) continue;
                columns.child(error.createWidget(this));
            }
            return columns.setEnabledIf(
                widget -> multiblock.shouldDisplayShutDownReason() && !baseMetaTileEntity.isActive()
                    && !baseMetaTileEntity.isAllowedToWork());
        })
            .allowC2S();

        errors.setChangeListener(() -> errorSyncer.notifyUpdate(packet -> {}));

        return new DynamicSyncedWidget<>().widthRel(0.85f)
            .coverChildrenHeight(0)
            .syncHandler(errorSyncer);
    }

    private IWidget createRecipeResultWidget(@NotNull IRecipeLogicComponent component) {
        return IKey.dynamic(
            () -> component.getCheckRecipeResult()
                .getDisplayString())
            .asWidget()
            .fullWidth()
            .marginBottom(2)
            .setEnabledIf(widget -> shouldRecipeResultBeDisplayed(component));
    }

    private boolean shouldRecipeResultBeDisplayed(@NotNull IRecipeLogicComponent component) {
        var recipeResult = component.getCheckRecipeResult();
        return showRecipeResultInGui() && GTUtility.isStringValid(recipeResult.getDisplayString())
            && (multiblock.isAllowedToWork() || baseMetaTileEntity.isActive() || recipeResult.persistsOnShutdown());
    }

    private IWidget createRecipeInfoWidget(PanelSyncManager syncManager) {
        IntSyncValue maxProgressTimeSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("maxProgressTime:0");
        GenericListSyncHandler<ItemStack> itemOutputSyncer = (GenericListSyncHandler<ItemStack>) syncManager
            .getSyncHandlerFromMapKey("itemOutput:0");
        GenericListSyncHandler<FluidStack> fluidOutputSyncer = (GenericListSyncHandler<FluidStack>) syncManager
            .getSyncHandlerFromMapKey("fluidOutput:0");

        DynamicSyncHandler recipeHandler = new DynamicSyncHandler().widgetProvider((syncManager1, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return Flow.column()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .coverChildren(0)
                .child(createItemRecipeInfo(packet, syncManager, showOutputRates()))
                .child(createFluidRecipeInfo(packet, syncManager, showOutputRates()));
        })
            .allowC2S();

        Runnable listener = () -> notifyRecipeHandler(recipeHandler, itemOutputSyncer, fluidOutputSyncer);
        maxProgressTimeSyncer.setChangeListener(listener);
        itemOutputSyncer.setChangeListener(listener);
        fluidOutputSyncer.setChangeListener(listener);

        return new DynamicSyncedWidget<>().widthRel(0.85f)
            .coverChildrenHeight(0)
            .syncHandler(recipeHandler);
    }

    private IWidget createPendingInfoWidget(PanelSyncManager syncManager) {
        GenericListSyncHandler<ItemStack> itemPendingSyncer = (GenericListSyncHandler<ItemStack>) syncManager
            .getSyncHandlerFromMapKey("itemPending:0");
        GenericListSyncHandler<FluidStack> fluidPendingSyncer = (GenericListSyncHandler<FluidStack>) syncManager
            .getSyncHandlerFromMapKey("fluidPending:0");

        DynamicSyncHandler recipeHandler = new DynamicSyncHandler().widgetProvider((syncManager1, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return Flow.column()
                .crossAxisAlignment(Alignment.CrossAxis.START)
                .coverChildren(0)
                .child(createItemRecipeInfo(packet, syncManager, false))
                .child(createFluidRecipeInfo(packet, syncManager, false));
        })
            .allowC2S();

        itemPendingSyncer
            .setChangeListener(() -> notifyRecipeHandler(recipeHandler, itemPendingSyncer, fluidPendingSyncer));
        fluidPendingSyncer
            .setChangeListener(() -> notifyRecipeHandler(recipeHandler, itemPendingSyncer, fluidPendingSyncer));
        return new DynamicSyncedWidget<>().widthRel(0.85f)
            .coverChildrenHeight(0)
            .syncHandler(recipeHandler);

    }

    private void notifyRecipeHandler(DynamicSyncHandler recipeHandler,
        GenericListSyncHandler<ItemStack> itemOutputSyncer, GenericListSyncHandler<FluidStack> fluidOutputSyncer) {
        recipeHandler.notifyUpdate(packet -> {
            List<ItemStack> items = itemOutputSyncer.getValue();
            packet.writeInt(items.size());
            for (ItemStack item : items) {
                NetworkUtils.writeItemStack(packet, item);
            }

            List<FluidStack> fluids = fluidOutputSyncer.getValue();
            packet.writeInt(fluids.size());
            for (FluidStack fluid : fluids) {
                NetworkUtils.writeFluidStack(packet, fluid);
            }
        });
    }

    private static final int DISPLAY_ROW_PRODUCT_HEIGHT = 8;
    private static final int DISPLAY_ROW_RATE_HEIGHT = 6;
    private static final int DISPLAY_ROW_HEIGHT = DISPLAY_ROW_PRODUCT_HEIGHT + DISPLAY_ROW_RATE_HEIGHT + 1;

    private IWidget createItemRecipeInfo(PacketBuffer packet, PanelSyncManager syncManager, boolean showRate) {
        int size = packet.readInt();
        Flow column = Flow.column()
            .coverChildren(0);

        Map<ItemDisplayKey, Long> itemDisplayMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ItemStack item = NetworkUtils.readItemStack(packet);
            // Some multiblocks set outputs to null
            if (item == null) {
                continue;
            }
            itemDisplayMap.merge(
                new ItemDisplayKey(item.getItem(), item.getItemDamage(), item.getTagCompound()),
                (long) item.stackSize,
                Long::sum);
        }
        // a and b comparison swapped for stacksize on purpose to get descending order
        List<Map.Entry<ItemDisplayKey, Long>> sortedEntries = itemDisplayMap.entrySet()
            .stream()
            .sorted((a, b) -> {
                ItemDisplayKey itemDisplayA = a.getKey();
                Long stackSizeA = a.getValue();
                ItemDisplayKey itemDisplayB = b.getKey();
                Long stackSizeB = b.getValue();

                if (stackSizeA.equals(stackSizeB)) {
                    ItemStack itemA = new ItemStack(itemDisplayA.item(), 1, itemDisplayA.damage());
                    ItemStack itemB = new ItemStack(itemDisplayB.item(), 1, itemDisplayB.damage());
                    return itemA.getDisplayName()
                        .compareTo(itemB.getDisplayName());
                } else {
                    return stackSizeB.compareTo(stackSizeA);
                }
            })
            .toList();

        // create row for each entry
        for (Map.Entry<ItemDisplayKey, Long> entry : sortedEntries) {
            ItemDisplayKey key = entry.getKey();
            long amount = entry.getValue();
            column.child(
                Flow.row()
                    .fullWidth()
                    .height(DISPLAY_ROW_HEIGHT)
                    .child(createItemDrawable(key))
                    .child(createHoverableTextForItem(key, amount, syncManager, showRate)));
        }

        return column;
    }

    protected boolean showOutputRates() {
        return true;
    }

    private IWidget createFluidRecipeInfo(PacketBuffer packet, PanelSyncManager syncManager, boolean showRate) {
        int size = packet.readInt();
        Flow column = Flow.column()
            .coverChildren(0);

        // create merged map of fluidstack to total amount in recipe
        final Map<FluidStack, Long> fluidDisplayMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            FluidStack fluidStack = NetworkUtils.readFluidStack(packet);
            // Some multiblocks set outputs to null
            if (fluidStack == null) {
                continue;
            }
            long amount = GTUtility.getFluidAmount(fluidStack);
            // map.merge requires the objects to be the same. fluidstacks with different stacksizes will be different.
            // set the amount to 1 to ensure fluid stacks of the same fluid get merged together
            GTUtility.setFluidAmount(fluidStack, 1);
            fluidDisplayMap.merge(fluidStack, amount, GTUtility::addSafe);
        }

        // sort map and return as List of Entries
        final List<Map.Entry<FluidStack, Long>> sortedEntryList = fluidDisplayMap.entrySet()
            .stream()
            .sorted(
                Map.Entry.<FluidStack, Long>comparingByValue()
                    .thenComparing(
                        entry -> entry.getKey()
                            .getLocalizedName())
                    .reversed())
            .toList();

        // create row for each entry
        for (Map.Entry<FluidStack, Long> entry : sortedEntryList) {
            FluidStack fluidStack = entry.getKey();
            long amount = entry.getValue();
            column.child(
                Flow.row()
                    .fullWidth()
                    .height(DISPLAY_ROW_HEIGHT)
                    .child(createFluidDrawable(fluidStack))
                    .child(createHoverableTextForFluid(fluidStack, amount, syncManager, showRate)));
        }
        return column;
    }

    private IWidget createRecipeInfoTextWidget(PanelSyncManager syncManager,
        @NotNull IRecipeLogicComponent recipeLogic) {
        return IKey.dynamic(() -> generateCurrentRecipeInfoString(recipeLogic))
            .asWidget()
            .marginBottom(2)
            .fullWidth()
            .setEnabledIf(_ -> Predicates.isPositive(syncManager.getSyncHandlerFromMapKey("maxProgressTime:0")));
    }

    private ItemDisplayWidget createItemDrawable(ItemDisplayKey key) {
        // Second argument is stacksize, don't care about it
        ItemStack itemStack = new ItemStack(key.item(), 1, key.damage());
        itemStack.setTagCompound(key.nbt());

        return new ItemDisplayWidget().disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .displayAmount(false)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .item(itemStack)
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(2);
    }

    private IWidget createHoverableTextForItem(ItemDisplayKey key, long amount, PanelSyncManager syncManager,
        boolean showRate) {
        // Second argument is stacksize, don't care about it
        ItemStack itemStack = new ItemStack(key.item(), 1, key.damage());
        itemStack.setTagCompound(key.nbt());
        IntSyncValue maxProgressTimeSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("maxProgressTime:0");
        String itemName = itemStack.getDisplayName();

        return Flow.column()
            .coverChildren(0)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(
                new TextWidget<>(IKey.dynamic(() -> EnumChatFormatting.AQUA + itemName))
                    .height(DISPLAY_ROW_PRODUCT_HEIGHT)
                    .scale(0.75f))
            .child(
                new TextWidget<>(IKey.dynamic(() -> getItemAmountTextLine(amount, maxProgressTimeSyncer, showRate)))
                    .height(DISPLAY_ROW_RATE_HEIGHT)
                    .scale(0.6f))
            .tooltip(t -> {
                if (showRate) {
                    t.addLine(
                        EnumChatFormatting.AQUA + itemName
                            + "\n"
                            + GTUtility.appendRate(false, amount, false, maxProgressTimeSyncer.getValue()));
                }
            });
    }

    private @NotNull String getItemAmountTextLine(long amount, IntSyncValue maxProgressTimeSyncer, boolean showRate) {
        String shortenedCount = GTUtility.formatShortenedLong(amount);
        String rateShort = showRate ? GTUtility.appendRate(false, amount, true, maxProgressTimeSyncer.getValue()) : "";
        return StatCollector.translateToLocalFormatted("GT5U.gui.text.item_amount_display", "", shortenedCount)
            + rateShort;
    }

    private FluidDisplayWidget createFluidDrawable(FluidStack fluidStack) {
        return new FluidDisplayWidget().disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .displayAmount(false)
            .value(fluidStack)
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(2);
    }

    private IWidget createHoverableTextForFluid(FluidStack fluidStack, long amount, PanelSyncManager syncManager,
        boolean showRate) {
        IntSyncValue maxProgressSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("maxProgressTime:0");
        String fluidName = fluidStack.getLocalizedName();

        return Flow.column()
            .coverChildren(0)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(
                new TextWidget<>(IKey.dynamic(() -> EnumChatFormatting.AQUA + fluidName))
                    .height(DISPLAY_ROW_PRODUCT_HEIGHT)
                    .scale(0.75f)
                    .textAlign(Alignment.CenterLeft))
            .child(
                new TextWidget<>(IKey.dynamic(() -> getFluidAmountTextLine(amount, maxProgressSyncer, showRate)))
                    .height(DISPLAY_ROW_RATE_HEIGHT)
                    .scale(0.6f)
                    .textAlign(Alignment.CenterLeft))
            .tooltip(t -> {
                if (showRate) {
                    t.addLine(
                        EnumChatFormatting.AQUA + fluidName
                            + "\n"
                            + GTUtility.appendRate(true, amount, false, maxProgressSyncer.getIntValue()));
                }
            });
    }

    private @NotNull String getFluidAmountTextLine(long amount, IntSyncValue maxProgressTimeSyncer, boolean showRate) {
        String shortenedCount = GTUtility.formatShortenedLong(amount);
        String rateShort = showRate ? GTUtility.appendRate(true, amount, true, maxProgressTimeSyncer.getValue()) : "";
        return StatCollector.translateToLocalFormatted("GT5U.gui.text.fluid_amount_display", "", shortenedCount)
            + rateShort;
    }

    /**
     * Split into two methods, one for left/right side of the panel gap
     * {@link #createLeftPanelGapRow(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager, MultiblockGuiBuilder)}
     * {@link #createRightPanelGapRow(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager, MultiblockGuiBuilder)}
     *
     */
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager, MultiblockGuiBuilder builder) {
        return Flow.row()
            .fullWidth()
            .paddingRight(2)
            .paddingLeft(4)
            .height(getTextBoxToInventoryGap())
            .mainAxisAlignment(MainAxis.SPACE_BETWEEN)
            .child(createLeftPanelGapRow(parent, syncManager, builder))
            .child(createRightPanelGapRow(parent, syncManager, builder));
    }

    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager,
        MultiblockGuiBuilder builder) {
        var row = Flow.row()
            .coverChildrenWidth()
            .fullHeight();
        appendWidgets(builder.leftGap(), row::child, parent, syncManager);
        return row;
    }

    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager,
        MultiblockGuiBuilder builder) {
        var row = Flow.row()
            .mainAxisAlignment(MainAxis.END)
            .reverseLayout(true)
            .coverChildrenWidth()
            .fullHeight();
        appendWidgets(builder.rightGap(), row::child, parent, syncManager);
        return row;
    }

    protected boolean shouldDisplayVoidExcess() {
        return true;
    }

    protected boolean shouldDisplayInputSeparation() {
        return true;
    }

    protected boolean shouldDisplayBatchMode() {
        return true;
    }

    protected boolean shouldDisplayRecipeLock() {
        return true;
    }

    protected ButtonWidget<?> createDisabledButton(@NotNull UITexture overlay,
        @NotNull Consumer<RichTooltip> tooltipBuilder) {
        return new ButtonWidget<>().widgetTheme(GTWidgetThemes.BUTTON_DISABLED)
            .overlay(new DynamicDrawable(() -> overlay))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .tooltipBuilder(tooltipBuilder);
    }

    protected void buildDisabledFeatureTooltip(@NotNull RichTooltip tooltip, @NotNull String featureTitle) {
        tooltip.addLine(featureTitle)
            .addLine(
                GTUtility.getColoredSecondaryTooltip(StatCollector.translateToLocal(BUTTON_FEATURE_DISABLED_TOOLTIP)))
            .addLine(StatCollector.translateToLocal(BUTTON_FORBIDDEN_TOOLTIP));
    }

    protected ButtonWidget<?> createPowerPanelButton(PanelSyncManager syncManager, ModularPanel parent,
        MultiblockGuiBuilder builder) {
        IPanelHandler powerPanel = syncManager.syncedPanel(
            "powerPanel",
            true,
            (panelSyncManager, _) -> openPowerControlPanel(panelSyncManager, parent, builder));
        return new ButtonWidget<>().marginLeft(4)
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

    private ModularPanel openPowerControlPanel(PanelSyncManager syncManager, ModularPanel parent,
        MultiblockGuiBuilder builder) {

        var powerPanel = new ModularPanel("powerPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
            .size(120, 130);
        Flow column = Flow.column()
            .full()
            .padding(3);
        appendWidgets(builder.powerPanel(), column::child, powerPanel, syncManager);
        return powerPanel.child(column);
    }

    protected IWidget makeTitleTextWidget() {
        return new TextWidget<>(
            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("GT5U.gui.text.power_panel"))
                .textAlign(Alignment.Center)
                .size(120, 18)
                .marginBottom(4);
    }

    private IWidget makePowerfailEventsToggleRow() {
        BooleanSyncValue powerfailSyncer = new BooleanSyncValue(
            multiblock::makesPowerfailEvents,
            multiblock::setPowerfailEventCreationStatus).allowC2S();
        return Flow.row()
            .fullWidth()
            .height(18)
            .paddingLeft(3)
            .paddingRight(3)
            .mainAxisAlignment(MainAxis.CENTER)
            .child(
                new TextWidget<>(IKey.lang("GT5U.gui.text.powerfail_events")).height(18)
                    .marginRight(2))
            .child(
                new ToggleButton().value(powerfailSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS));
    }

    protected IWidget createShutdownReasonHoverableTerminal(PanelSyncManager syncManager) {
        BooleanSyncValue wasShutdownSyncer = (BooleanSyncValue) syncManager.getSyncHandlerFromMapKey("wasShutdown:0");
        return new HoverableIcon(new DynamicDrawable(() -> {
            if (wasShutdownSyncer.getBoolValue()) {
                return getTextureForReason(getShutDownReasonKey());
            }
            return null;
        }).asIcon()).asWidget()
            .size(18, 18)
            .tooltipBuilder(t -> {
                if (wasShutdownSyncer.getBoolValue()) {
                    t.add(getToolTipForReason(getShutDownReasonKey()));
                }
            })
            .tooltipAutoUpdate(true)
            .setEnabledIf(widget -> shouldShutdownReasonBeDisplayed(getShutDownReasonKey()));
    }

    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .height(76)
            .childIf(
                multiblock.doesBindPlayerInventory(),
                () -> SlotGroupWidget.playerInventory(false)
                    .marginLeft(4))
            .child(createButtonColumn(panel, syncManager));
    }

    // children are added bottom to top
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(MainAxis.END)
            .reverseLayout(true)
            .childIf(
                multiblock.doesBindPlayerInventory(),
                () -> new ItemSlot()
                    .slot(new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex()) {

                        @Override
                        public int getSlotStackLimit() {
                            return multiblock.getInventoryStackLimit();
                        }
                    }.singletonSlotGroup())
                    .marginTop(4))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));

    }

    protected IWidget createStructureUpdateButton(PanelSyncManager syncManager) {
        return new ToggleButton().syncHandler("structureUpdateButton")
            .overlay(GTGuiTextures.OVERLAY_BUTTON_STRUCTURE_UPDATE)
            .tooltipBuilder(t -> { t.addLine(IKey.lang("GT5U.gui.button.structure_update")); });
    }

    protected ToggleButton createPowerSwitchButton() {
        return CommonButtons.createPowerSwitchButton("powerSwitch", isPowerSwitchDisabled(), baseMetaTileEntity);
    }

    protected boolean isPowerSwitchDisabled() {
        return false;
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {
        syncManager.syncValue(
            "machineActive",
            new BooleanSyncValue(baseMetaTileEntity::isActive, baseMetaTileEntity::setActive));

        syncManager
            .syncValue("totalRunTime", new LongSyncValue(multiblock::getTotalRunTime, multiblock::setTotalRunTime));
        BooleanSyncValue wasShutDown = new BooleanSyncValue(
            baseMetaTileEntity::wasShutdown,
            baseMetaTileEntity::setShutdownStatus).allowC2S();
        syncManager.syncValue("wasShutdown", wasShutDown);

        GenericSyncValue<ShutDownReason, ?> shutdownReasonSyncer = GenericSyncValue.builder(ShutDownReason.class)
            .getter(baseMetaTileEntity::getLastShutDownReason)
            .setter(baseMetaTileEntity::setShutDownReason)
            .adapter(new ShutdownReasonAdapter())
            .build()
            .allowC2S();
        syncManager.syncValue("shutdownReason", shutdownReasonSyncer);

        // Widget Specific
        BooleanSyncValue powerSwitchSyncer = new BooleanSyncValue(multiblock::isAllowedToWork, bool -> {
            // This setter also runs on the client when the value is synced from the server. Toggling the machine there
            // would overwrite state the server just sent, such as the shutdown reason.
            if (!baseMetaTileEntity.isServerSide()) return;
            if (isPowerSwitchDisabled()) return;
            if (bool) multiblock.enableWorking();
            else {
                if (multiblock.maxProgresstime() > 0) {
                    multiblock.disableWorking();
                    wasShutDown.setValue(true);
                    shutdownReasonSyncer.setValue(ShutDownReasonRegistry.NONE);
                } else multiblock.stopMachine(ShutDownReasonRegistry.NONE);
            }
        }).allowC2S();
        syncManager.syncValue("powerSwitch", powerSwitchSyncer);

        IntSyncValue structureUpdateSyncer = new IntSyncValue(
            multiblock::getStructureUpdateTime,
            multiblock::setStructureUpdateTime).allowC2S();
        BooleanSyncValue structureUpdateButtonSyncer = new BooleanSyncValue(
            () -> structureUpdateSyncer.getValue() > -20,
            val -> { if (val) structureUpdateSyncer.setValue(1); }).allowC2S();
        syncManager.syncValue("structureUpdate", structureUpdateSyncer);
        syncManager.syncValue("structureUpdateButton", structureUpdateButtonSyncer);

        BooleanSyncValue mufflerSyncer = new BooleanSyncValue(multiblock::isMuffled, multiblock::setMuffled).allowC2S();
        syncManager.syncValue("mufflerSyncer", mufflerSyncer);

        for (var comp : multiblock.getComponents(IGuiSyncableComponent.KEY)) {
            comp.registerGuiSyncValues(syncManager);
        }
    }

    // Method for registering Icons/Tooltip Text to specific ShutDownReasons. Override for custom icons/conditions.

    protected String getShutDownReasonKey() {
        return baseMetaTileEntity.getLastShutDownReason()
            .getKey();
    }

    protected UITexture getTextureForReason(String key) {
        return this.shutdownReasonTextureMap.getOrDefault(key, null);
    }

    protected String getToolTipForReason(String key) {
        return this.shutdownReasonTooltipMap
            .getOrDefault(key, EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.gui.hoverable.error"));
    }

    protected final NumberFormatMUI numberFormat = new NumberFormatMUI();

    protected String generateCurrentRecipeInfoString(@NotNull IRecipeLogicComponent recipeLogic) {
        var mProgresstime = recipeLogic.getProgressTime();
        var mMaxProgresstime = recipeLogic.getMaxProgressTime();
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);
        String current = numberFormat.format((double) mProgresstime / 20);
        String max = numberFormat.format((double) mMaxProgresstime / 20);
        numberFormat.setMinimumFractionDigits(1);
        numberFormat.setMaximumFractionDigits(1);
        String percent = numberFormat.format((double) mProgresstime / mMaxProgresstime * 100);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);
        return translateToLocalFormatted("GT5U.gui.text.progress_recipe", current, max, percent);
    }

    // For MUI2 corner column toggling
    protected boolean supportsTerminalRightCornerColumn() {
        return true;
    }

    protected boolean supportsTerminalLeftCornerColumn() {
        return false;
    }

    protected boolean supportsLogo() {
        return true;
    }

    protected boolean supportsShutdownReasonHoverable() {
        return true;
    }

    protected boolean showRecipeResultInGui() {
        return true;
    }

    protected boolean showRecipeTextInGui() {
        return multiblock.hasComponent(IRecipeLogicComponent.KEY);
    }

    protected boolean showRunningTextInGui() {
        return true;
    }

    protected boolean showPendingOutputsInGui() {
        return true;
    }

    public static void appendWidgets(
        @NotNull OrderedSectionBuilder<IGuiProviderComponent.GuiWidgetFactory> sectionBuilder,
        @NotNull Consumer<IWidget> addr, @NotNull ModularPanel panel, @NotNull PanelSyncManager syncManager) {
        sectionBuilder.forEach(factory -> {
            var widget = factory.create(panel, syncManager);
            if (widget != null) {
                addr.accept(widget);
            }
        });
    }

    public static ToggleButton createActiveFeatureToggleButton(@NotNull BooleanSyncValue syncValue,
        @NotNull UITexture offOverlay, @NotNull UITexture onOverlay, @NotNull String titleLangKey,
        @NotNull Supplier<Boolean> isFeatureEnabled) {
        return new ToggleButton().value(syncValue)
            .overlay(false, offOverlay)
            .overlay(true, onOverlay)
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .tooltipBuilder(t -> {
                t.addLine(StatCollector.translateToLocal(titleLangKey))
                    .addLine(IKey.dynamic(() -> {
                        if (isFeatureEnabled.get()) {
                            return GTUtility.getColoredSecondaryTooltip(
                                StatCollector.translateToLocal(BUTTON_FEATURE_ENABLED_TOOLTIP));
                        } else {
                            return GTUtility.getColoredSecondaryTooltip(
                                StatCollector.translateToLocal(BUTTON_FEATURE_DISABLED_TOOLTIP));
                        }
                    }));
            });
    }
}
