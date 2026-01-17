package gregtech.common.gui.modularui.multiblock.base;

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
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
import com.cleanroommc.modularui.drawable.DrawableStack;
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
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.EmptyWidget;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
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
import gregtech.api.gui.widgets.CommonWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.ItemDisplayKey;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.adapter.CheckRecipeResultAdapter;
import gregtech.common.gui.modularui.adapter.ShutdownReasonAdapter;
import gregtech.common.gui.modularui.adapter.StructureErrorAdapter;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.modularui2.sync.Predicates;

public class MTEMultiBlockBaseGui<T extends MTEMultiBlockBase> {

    protected final T multiblock;
    protected final IGregTechTileEntity baseMetaTileEntity;
    protected List<UITexture> machineModeIcons = new ArrayList<>();
    protected Map<String, UITexture> customIcons = new HashMap<>();
    private static final int borderRadius = 4;
    protected final Map<String, IPanelHandler> panelMap = new HashMap<>();
    protected Map<String, UITexture> shutdownReasonTextureMap = new HashMap<>();
    protected Map<String, String> shutdownReasonTooltipMap = new HashMap<>();

    public MTEMultiBlockBaseGui(T multiblock) {
        this.multiblock = multiblock;
        this.baseMetaTileEntity = multiblock.getBaseMetaTileEntity();
        initCustomIcons();
        initShutdownMaps();
    }

    public MTEMultiBlockBaseGui withMachineModeIcons(UITexture... icons) {
        Collections.addAll(this.machineModeIcons, icons);
        return this;
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

    }

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        setMachineModeIcons();
        registerSyncValues(syncManager);

        ModularPanel panel = getBasePanel(guiData, syncManager, uiSettings);
        return panel.child(createMainColumn(panel, syncManager));
    }

    public Flow createMainColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.column()
            .padding(borderRadius)
            .child(createTerminalRow(panel, syncManager))
            .childIf(multiblock.canBeMuffled(), this.createMuffleButton())
            .child(createPanelGap(panel, syncManager))
            .childIf(multiblock.supportsInventoryRow(), createInventoryRow(panel, syncManager));
    }

    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(multiblock, guiData, syncManager, uiSettings).setWidth(getBasePanelWidth())
            .setHeight(getBasePanelHeight())
            .doesAddGregTechLogo(false)
            // Has to be replaced with inventory row to fit buttons
            .doesBindPlayerInventory(false)
            .build();
    }

    protected ToggleButton createMuffleButton() {
        return CommonWidgets.createMuffleButton("mufflerSyncer")
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

    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(createTerminalParentWidget(panel, syncManager));
    }

    protected ParentWidget<?> createTerminalParentWidget(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
            .paddingTop(4)
            .paddingBottom(4)
            .paddingLeft(4)
            .paddingRight(0)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .child(
                createTerminalTextWidget(syncManager, panel)
                    .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                    .collapseDisabledChild())
            .childIf(
                multiblock.supportsTerminalRightCornerColumn(),
                createTerminalRightCornerColumn(panel, syncManager))
            .childIf(multiblock.supportsTerminalLeftCornerColumn(), createTerminalLeftCornerColumn(panel, syncManager));
    }

    protected Flow createTerminalRightCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .rightRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .childIf(multiblock.supportsShutdownReasonHoverable(), createShutdownReasonHoverableTerminal(syncManager))
            .childIf(multiblock.supportsMaintenanceIssueHoverable(), createMaintIssueHoverableTerminal(syncManager))
            .childIf(multiblock.supportsLogo(), makeLogoWidget(syncManager, panel));
    }

    protected Widget<? extends Widget<?>> makeLogoWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return new IDrawable.DrawableWidget(IDrawable.EMPTY).size(18)
            .marginTop(4)
            .widgetTheme(GTWidgetThemes.PICTURE_LOGO);
    }

    protected Flow createTerminalLeftCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .leftRel(0, 6, 0)
            .bottomRel(0, 6, 0);
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

    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue startupCheckSyncer = new IntSyncValue(multiblock::getmStartUpCheck);
        syncManager.syncValue("startupCheck", startupCheckSyncer);

        return new ListWidget<>().widthRel(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .child(
                IKey.lang("GT5U.multiblock.startup")
                    .color(Color.WHITE.main)
                    .asWidget()
                    .alignment(Alignment.CenterLeft)
                    .setEnabledIf(w -> startupCheckSyncer.getValue() > 0)
                    .marginBottom(2)
                    .widthRel(1))
            .childIf(
                multiblock.hasRunningText(),
                new TextWidget<>(GTUtility.trans("142", "Running perfectly.")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> multiblock.getErrorDisplayID() == 0 && baseMetaTileEntity.isActive())
                    .marginBottom(2)
                    .widthRel(1))
            .child(createShutdownDurationWidget(syncManager))
            .child(createShutdownReasonWidget(syncManager))
            .child(createRecipeResultWidget())
            .childIf(multiblock.showRecipeTextInGUI(), createRecipeInfoTextWidget(syncManager))

            .childIf(multiblock.showRecipeTextInGUI(), createRecipeInfoWidget(syncManager));
    }

    protected IWidget createShutdownDurationWidget(PanelSyncManager syncManager) {
        LongSyncValue shutdownDurationSyncer = (LongSyncValue) syncManager
            .getSyncHandlerFromMapKey("shutdownDuration:0");
        return IKey.dynamic(() -> {
            Duration time = Duration.ofSeconds(shutdownDurationSyncer.getValue());
            return StatCollector.translateToLocalFormatted(
                "GT5U.gui.text.shutdown_duration",
                time.toHours(),
                time.toMinutes() % 60,
                time.getSeconds() % 60);
        })
            .asWidget()
            .widthRel(1)
            .setEnabledIf(
                widget -> multiblock.shouldDisplayShutDownReason() && !baseMetaTileEntity.isActive()
                    && !baseMetaTileEntity.isAllowedToWork());

    }

    protected IWidget createShutdownReasonWidget(PanelSyncManager syncManager) {
        StringSyncValue shutdownReasonSync = (StringSyncValue) syncManager
            .getSyncHandlerFromMapKey("shutdownDisplayString:0");
        return IKey.dynamic(shutdownReasonSync::getValue)
            .asWidget()
            .widthRel(1)
            .marginBottom(2)
            .setEnabledIf(widget -> shouldShutdownReasonBeDisplayed(shutdownReasonSync.getValue()));
    }

    protected boolean shouldShutdownReasonBeDisplayed(String shutdownString) {
        return multiblock.shouldDisplayShutDownReason() && !baseMetaTileEntity.isActive()
            && !baseMetaTileEntity.isAllowedToWork()
            && GTUtility.isStringValid(shutdownString);
    }

    private IWidget createRecipeResultWidget() {
        return IKey.dynamic(
            () -> multiblock.getCheckRecipeResult()
                .getDisplayString())
            .asWidget()
            .widthRel(1)
            .marginBottom(2)
            .setEnabledIf(widget -> shouldRecipeResultBeDisplayed());

    }

    private boolean shouldRecipeResultBeDisplayed() {
        CheckRecipeResult recipeResult = multiblock.getCheckRecipeResult();
        return multiblock.shouldDisplayCheckRecipeResult() && GTUtility.isStringValid(recipeResult.getDisplayString())
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
                .coverChildren()
                .child(createItemRecipeInfo(packet, syncManager))
                .child(createFluidRecipeInfo(packet, syncManager));
        });

        itemOutputSyncer
            .setChangeListener(() -> notifyRecipeHandler(recipeHandler, itemOutputSyncer, fluidOutputSyncer));
        fluidOutputSyncer
            .setChangeListener(() -> notifyRecipeHandler(recipeHandler, itemOutputSyncer, fluidOutputSyncer));
        return new DynamicSyncedWidget<>().widthRel(0.85f)
            .coverChildrenHeight()
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

    private final int DISPLAY_ROW_HEIGHT = 15;

    private IWidget createItemRecipeInfo(PacketBuffer packet, PanelSyncManager syncManager) {
        int size = packet.readInt();
        Flow column = Flow.column()
            .coverChildren();

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
            .collect(Collectors.toList());

        // create row for each entry
        for (Map.Entry<ItemDisplayKey, Long> entry : sortedEntries) {
            ItemDisplayKey key = entry.getKey();
            long amount = entry.getValue();
            column.child(
                Flow.row()
                    .widthRel(1)
                    .height(DISPLAY_ROW_HEIGHT)
                    .child(createItemDrawable(key))
                    .child(createHoverableTextForItem(key, amount, syncManager)));
        }

        return column;
    }

    private IWidget createFluidRecipeInfo(PacketBuffer packet, PanelSyncManager syncManager) {
        int size = packet.readInt();
        Flow column = Flow.column()
            .coverChildren();

        // create merged map of fluidstack to total amount in recipe
        final Map<FluidStack, Long> fluidDisplayMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            FluidStack fluidStack = NetworkUtils.readFluidStack(packet);
            // Some multiblocks set outputs to null
            if (fluidStack == null) {
                continue;
            }
            long amount = (long) fluidStack.amount;
            // map.merge requires the objects to be the same. fluidstacks with different stacksizes will be different.
            // set the amount to 1 to ensure fluid stacks of the same fluid get merged together
            fluidStack.amount = 1;
            fluidDisplayMap.merge(fluidStack, amount, Long::sum);
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
            .collect(Collectors.toList());

        // create row for each entry
        for (Map.Entry<FluidStack, Long> entry : sortedEntryList) {
            FluidStack fluidStack = entry.getKey();
            long amount = entry.getValue();
            column.child(
                Flow.row()
                    .widthRel(1)
                    .height(DISPLAY_ROW_HEIGHT)
                    .child(createFluidDrawable(fluidStack))
                    .child(createHoverableTextForFluid(fluidStack, amount, syncManager)));
        }
        return column;
    }

    private IWidget createRecipeInfoTextWidget(PanelSyncManager syncManager) {
        return IKey.dynamic(() -> ((StringSyncValue) syncManager.getSyncHandlerFromMapKey("recipeInfo:0")).getValue())
            .asWidget()
            .marginBottom(2)
            .widthRel(1)
            .setEnabledIf(
                widget -> Predicates.isNonEmptyList(syncManager.getSyncHandlerFromMapKey("itemOutput:0"))
                    || Predicates.isNonEmptyList(syncManager.getSyncHandlerFromMapKey("fluidOutput:0")));
    }

    private ItemDisplayWidget createItemDrawable(ItemDisplayKey key) {
        // Second argument is stacksize, don't care about it
        ItemStack itemStack = new ItemStack(key.item(), 1, key.damage());
        itemStack.setTagCompound(key.nbt());

        return new ItemDisplayWidget().background(IDrawable.EMPTY)
            .displayAmount(false)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .item(itemStack)
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(2);
    }

    private TextWidget<?> createHoverableTextForItem(ItemDisplayKey key, long amount, PanelSyncManager syncManager) {
        // Second argument is stacksize, don't care about it
        ItemStack itemStack = new ItemStack(key.item(), 1, key.damage());
        itemStack.setTagCompound(key.nbt());
        IntSyncValue maxProgressTimeSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("maxProgressTime:0");
        String itemName = EnumChatFormatting.AQUA + itemStack.getDisplayName() + EnumChatFormatting.RESET;

        return new TextWidget<>(IKey.dynamic(() -> getItemTextLine(itemName, amount, maxProgressTimeSyncer)))
            .height(DISPLAY_ROW_HEIGHT)
            .scale(0.75f)
            .tooltip(
                t -> t.addLine(
                    EnumChatFormatting.AQUA + itemName
                        + "\n"
                        + GTUtility.appendRate(false, amount, false, maxProgressTimeSyncer.getValue())));
    }

    private @NotNull String getItemTextLine(String itemName, long amount, IntSyncValue maxProgressTimeSyncer) {

        String amountString = EnumChatFormatting.WHITE + " x "
            + EnumChatFormatting.GOLD
            + GTUtility.formatShortenedLong(amount)
            + EnumChatFormatting.WHITE
            + GTUtility.appendRate(false, amount, true, maxProgressTimeSyncer.getValue());
        String itemTextLine = EnumChatFormatting.AQUA + GTUtility.truncateText(itemName, 46 - amountString.length())
            + amountString;
        return itemTextLine;
    }

    private FluidDisplayWidget createFluidDrawable(FluidStack fluidStack) {
        return new FluidDisplayWidget().background(IDrawable.EMPTY)
            .displayAmount(false)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .fluid(fluidStack)
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(2);
    }

    private TextWidget<?> createHoverableTextForFluid(FluidStack fluidStack, long amount,
        PanelSyncManager syncManager) {
        IntSyncValue maxProgressSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("maxProgressTime:0");
        String fluidName = EnumChatFormatting.AQUA + fluidStack.getLocalizedName() + EnumChatFormatting.RESET;

        return new TextWidget<>(IKey.dynamic(() -> getFluidTextLine(fluidName, amount, maxProgressSyncer)))
            .height(DISPLAY_ROW_HEIGHT)
            .scale(0.75f)
            .tooltip(
                t -> t.addLine(
                    EnumChatFormatting.AQUA + fluidName
                        + "\n"
                        + GTUtility.appendRate(true, amount, false, maxProgressSyncer.getIntValue())));
    }

    private @NotNull String getFluidTextLine(String fluidName, long amount, IntSyncValue maxProgressTimeSyncer) {
        String amountString = EnumChatFormatting.WHITE + " x "
            + EnumChatFormatting.GOLD
            + GTUtility.formatShortenedLong(amount)
            + "L"
            + EnumChatFormatting.WHITE
            + GTUtility.appendRate(false, amount, true, maxProgressTimeSyncer.getValue());
        String fluidTextLine = EnumChatFormatting.AQUA + GTUtility.truncateText(fluidName, 46 - amountString.length())
            + amountString;
        return fluidTextLine;
    }

    /**
     * Split into two methods, one for left/right side of the panel gap
     * {@link #createLeftPanelGapRow(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager)}
     * {@link #createRightPanelGapRow(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager)}
     *
     */
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(2)
            .paddingLeft(4)
            .height(getTextBoxToInventoryGap())
            .child(createLeftPanelGapRow(parent, syncManager))
            .child(createRightPanelGapRow(parent, syncManager));
    }

    protected Flow createLeftPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().coverChildrenWidth()
            .heightRel(1)
            .childIf(shouldDisplayVoidExcess(), createVoidExcessButton(syncManager))
            .childIf(shouldDisplayInputSeparation(), createInputSeparationButton(syncManager))
            .childIf(shouldDisplayBatchMode(), createBatchModeButton(syncManager))
            .childIf(shouldDisplayRecipeLock(), createLockToSingleRecipeButton(syncManager))
            .childIf(!machineModeIcons.isEmpty(), createModeSwitchButton(syncManager));
    }

    protected Flow createRightPanelGapRow(ModularPanel parent, PanelSyncManager syncManager) {
        return Flow.row()
            .mainAxisAlignment(MainAxis.END)
            .reverseLayout(true)
            .align(Alignment.CenterRight)
            .coverChildrenWidth()
            .heightRel(1)
            .childIf(multiblock.supportsPowerPanel(), createPowerPanelButton(syncManager, parent));
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

    protected IWidget createVoidExcessButton(PanelSyncManager syncManager) {
        IntSyncValue voidExcessSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("voidExcess:0");
        return new ButtonWidget<>().size(18, 18)
            .onMousePressed(mouseButton -> this.voidExcessOnMousePressed(mouseButton, voidExcessSyncer))
            .overlay(multiblock.supportsVoidProtection() ? getVoidExcessOverlay() : getForcedVoidExcessOverlay())
            .tooltipBuilder(this::createVoidExcessTooltip);
    }

    private boolean voidExcessOnMousePressed(int mouseButton, IntSyncValue voidExcessSyncer) {
        if (!multiblock.supportsVoidProtection()) return false;
        Set<VoidingMode> allowed = multiblock.getAllowedVoidingModes();
        int voidingMode = 0;
        switch (mouseButton) {
            case 0 -> voidingMode = multiblock.getVoidingMode()
                .nextInCollection(allowed)
                .ordinal();
            case 1 -> voidingMode = multiblock.getVoidingMode()
                .previousInCollection(allowed)
                .ordinal();
        }
        voidExcessSyncer.setValue(voidingMode);
        return true;
    }

    private IDrawable getForcedVoidExcessOverlay() {
        return new DrawableStack(multiblock.getVoidingMode().buttonOverlay, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
    }

    private IDrawable getVoidExcessOverlay() {
        return new DynamicDrawable(() -> multiblock.getVoidingMode().buttonOverlay);
    }

    private void createVoidExcessTooltip(RichTooltip t) {
        t.addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.voiding_mode")))
            .addLine(
                IKey.dynamic(
                    () -> StatCollector.translateToLocal(
                        multiblock.getVoidingMode()
                            .getTransKey())));
        if (!multiblock.supportsVoidProtection()) {
            t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
        }
    }

    protected IWidget createInputSeparationButton(PanelSyncManager syncManager) {
        BooleanSyncValue inputSeparationSyncer = (BooleanSyncValue) syncManager
            .getSyncHandlerFromMapKey("inputSeparation:0");
        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!multiblock.supportsInputSeparation()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(getInputSeparationSyncValue(inputSeparationSyncer))
            .overlay(
                multiblock.supportsInputSeparation() ? getInputSeparationOverlay(inputSeparationSyncer)
                    : getForcedInputSeparationOverlay())
            .tooltipBuilder(this::createInputSeparationTooltip);
    }

    private BooleanSyncValue getInputSeparationSyncValue(BooleanSyncValue inputSeparationSyncer) {
        return new BooleanSyncValue(
            () -> inputSeparationSyncer.getValue() || !multiblock.supportsInputSeparation(),
            bool -> {
                if (multiblock.supportsInputSeparation()) {
                    inputSeparationSyncer.setValue(bool);
                }
            });
    }

    private IDrawable getForcedInputSeparationOverlay() {
        UITexture texture = multiblock.isBatchModeEnabled() ? GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED
            : GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF_DISABLED;
        return new DrawableStack(texture, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
    }

    private IDrawable getInputSeparationOverlay(BooleanSyncValue inputSeparationSyncer) {
        return new DynamicDrawable(
            () -> inputSeparationSyncer.getValue() ? GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON
                : GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_OFF);
    }

    private void createInputSeparationTooltip(RichTooltip t) {
        addDynamicTooltipOfFeatureToButton(
            t,
            multiblock::supportsInputSeparation,
            multiblock::isInputSeparationEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.input_separation_on"),
            StatCollector.translateToLocal("GT5U.gui.button.input_separation_off"));
    }

    /**
     * Adds a dynamic line to a RichTooltip that displays the status of a multi-block feature.
     * <p>
     * The tooltip behavior depends on feature support:
     * <ul>
     * <li>If the feature is supported: Shows a dynamic tooltip that updates based on the feature's enabled state</li>
     * <li>If the feature is not supported: Shows a static tooltip with the current state plus a "forbidden"
     * message</li>
     * </ul>
     *
     * @param tooltip                the RichTooltip to add the line to
     * @param supportsFeature        supplier that returns {@code true} if the multi-block feature is supported
     * @param isFeatureEnabled       supplier that returns {@code true} if the feature is currently enabled
     * @param tooltipFeatureEnabled  tooltip text to display when the feature is enabled
     * @param tooltipFeatureDisabled tooltip text to display when the feature is disabled
     *
     * @see gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures#addDynamicTooltipOfFeatureToButton(com.gtnewhorizons.modularui.api.widget.Widget,
     *      java.util.function.Supplier, java.util.function.Supplier, String, String)
     *      For equivalent method but made for ModularUI
     */
    private void addDynamicTooltipOfFeatureToButton(RichTooltip tooltip, Supplier<Boolean> supportsFeature,
        Supplier<Boolean> isFeatureEnabled, String tooltipFeatureEnabled, String tooltipFeatureDisabled) {

        if (supportsFeature.get()) {
            tooltip.addLine(IKey.dynamic(() -> {
                if (isFeatureEnabled.get()) {
                    return tooltipFeatureEnabled;
                } else {
                    return tooltipFeatureDisabled;
                }
            }));
        } else {
            if (isFeatureEnabled.get()) {
                tooltip.addLine(tooltipFeatureEnabled);
            } else {
                tooltip.addLine(tooltipFeatureDisabled);
            }

            tooltip.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
        }
    }

    protected IWidget createModeSwitchButton(PanelSyncManager syncManager) {
        IntSyncValue machineModeSyncer = (IntSyncValue) syncManager.getSyncHandlerFromMapKey("machineMode:0");
        return new CycleButtonWidget().size(18, 18)
            .syncHandler("machineMode")
            .length(machineModeIcons.size())
            .overlay(new DynamicDrawable(() -> getMachineModeIcon(machineModeSyncer.getValue())))
            .tooltipBuilder(this::createModeSwitchTooltip);
    }

    protected UITexture getMachineModeIcon(int index) {
        if (index > machineModeIcons.size() - 1) return null;
        return machineModeIcons.get(index);
    }

    private void createModeSwitchTooltip(RichTooltip t) {
        t.addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.mode_switch")))
            .addLine(IKey.dynamic(multiblock::getMachineModeName));
    }

    protected IWidget createBatchModeButton(PanelSyncManager syncManager) {
        BooleanSyncValue batchModeSyncer = (BooleanSyncValue) syncManager.getSyncHandlerFromMapKey("batchMode:0");

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!multiblock.supportsBatchMode()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(getBatchModeSyncValue(batchModeSyncer))
            .overlay(
                multiblock.supportsBatchMode() ? getBatchModeOverlay(batchModeSyncer) : getForcedBatchModeOverlay())
            .tooltipBuilder(this::createBatchModeTooltip);
    }

    private BooleanSyncValue getBatchModeSyncValue(BooleanSyncValue batchModeSyncer) {
        return new BooleanSyncValue(() -> batchModeSyncer.getValue() || !multiblock.supportsBatchMode(), bool -> {
            if (multiblock.supportsBatchMode()) {
                batchModeSyncer.setValue(bool);
            }
        });
    }

    private IDrawable getBatchModeOverlay(BooleanSyncValue batchModeSyncer) {
        return new DynamicDrawable(
            () -> batchModeSyncer.getValue() ? GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON
                : GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
    }

    private IDrawable getForcedBatchModeOverlay() {
        UITexture texture = multiblock.isBatchModeEnabled() ? GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED
            : GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED;
        return new DrawableStack(texture, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
    }

    private void createBatchModeTooltip(RichTooltip t) {
        addDynamicTooltipOfFeatureToButton(
            t,
            multiblock::supportsBatchMode,
            multiblock::isBatchModeEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_on"),
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_off"));
    }

    protected IWidget createLockToSingleRecipeButton(PanelSyncManager syncManager) {
        BooleanSyncValue recipeLockSyncer = (BooleanSyncValue) syncManager.getSyncHandlerFromMapKey("recipeLock:0");
        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!multiblock.supportsSingleRecipeLocking()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(getRecipeLockSyncValue(recipeLockSyncer))
            .overlay(
                multiblock.supportsSingleRecipeLocking() ? getRecipeLockOverlay(recipeLockSyncer)
                    : getForcedRecipeLockOverlay())
            .tooltipBuilder(this::createRecipeLockTooltip);
    }

    private BooleanSyncValue getRecipeLockSyncValue(BooleanSyncValue recipeLockSyncer) {
        return new BooleanSyncValue(
            () -> recipeLockSyncer.getValue() || !multiblock.supportsSingleRecipeLocking(),
            bool -> {
                if (multiblock.supportsSingleRecipeLocking()) {
                    recipeLockSyncer.setValue(bool);
                }
            });
    }

    private IDrawable getForcedRecipeLockOverlay() {
        UITexture texture = multiblock.mLockedToSingleRecipe ? GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED
            : GTGuiTextures.OVERLAY_BUTTON_RECIPE_UNLOCKED_DISABLED;
        return new DrawableStack(texture, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
    }

    private IDrawable getRecipeLockOverlay(BooleanSyncValue recipeLockSyncer) {
        return new DynamicDrawable(
            () -> recipeLockSyncer.getValue() ? GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED
                : GTGuiTextures.OVERLAY_BUTTON_RECIPE_UNLOCKED);
    }

    private void createRecipeLockTooltip(RichTooltip t) {
        addDynamicTooltipOfFeatureToButton(
            t,
            multiblock::supportsSingleRecipeLocking,
            multiblock::isRecipeLockingEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_on"),
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_off"));
    }

    protected IWidget createPowerPanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler powerPanel = syncManager
            .panel("powerPanel", (p_syncManager, syncHandler) -> openPowerControlPanel(p_syncManager, parent), true);
        return new ButtonWidget<>().size(18, 18)
            .marginTop(4)
            .marginLeft(4)
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

        return new ModularPanel("powerPanel").relative(parent)
            .leftRel(1)
            .topRel(0)
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
                    .child(makePowerfailEventsToggleRow()));
    }

    protected IWidget makeTitleTextWidget() {
        return new TextWidget<>(
            EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("GT5U.gui.text.power_panel"))
                .alignment(Alignment.Center)
                .size(120, 18)
                .marginBottom(4);
    }

    private IWidget makePowerfailEventsToggleRow() {
        BooleanSyncValue powerfailSyncer = new BooleanSyncValue(
            multiblock::makesPowerfailEvents,
            multiblock::setPowerfailEventCreationStatus);
        return new Row().widthRel(1)
            .height(18)
            .paddingLeft(3)
            .paddingRight(3)
            .mainAxisAlignment(MainAxis.CENTER)
            .child(
                new TextWidget<>(IKey.lang("GT5U.gui.text.powerfail_events")).height(18)
                    .marginRight(2))
            .child(
                new ToggleButton().size(18, 18)
                    .value(powerfailSyncer)
                    .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                    .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS));
    }

    private IWidget makeParallelConfigurator(PanelSyncManager syncManager) {
        IntSyncValue maxParallelSyncer = new IntSyncValue(
            multiblock::getMaxParallelRecipes,
            multiblock::setMaxParallelForPanel);
        BooleanSyncValue alwaysMaxParallelSyncer = new BooleanSyncValue(
            multiblock::isAlwaysMaxParallel,
            multiblock::setAlwaysMaxParallel);
        syncManager.syncValue("maxParallel", maxParallelSyncer);
        syncManager.syncValue("alwaysMaxParallel", alwaysMaxParallelSyncer);

        // The PanelSyncManager seems to belong to absolutely nothing?
        // Not sure how that works but trying to use .syncHandler instead of .value causes a crash because
        // This PanelSyncManager has no panel and the widget tries to get a syncHandler from "powerPanel"
        IntSyncValue powerPanelMaxParallelSyncer = new IntSyncValue(
            multiblock::getPowerPanelMaxParallel,
            multiblock::setPowerPanelMaxParallel);
        return new Row().widthRel(1)
            .marginBottom(4)
            .height(18)
            .paddingLeft(3)
            .paddingRight(3)
            .mainAxisAlignment(MainAxis.CENTER)
            .child(
                makeParallelConfiguratorTextFieldWidget(
                    maxParallelSyncer,
                    alwaysMaxParallelSyncer,
                    powerPanelMaxParallelSyncer))
            .child(
                new ButtonWidget<>().size(18, 18)
                    .overlay(new DynamicDrawable(() -> {
                        if (alwaysMaxParallelSyncer.getValue()) {
                            return GTGuiTextures.OVERLAY_BUTTON_CHECKMARK;
                        } else {
                            return GTGuiTextures.OVERLAY_BUTTON_CROSS;
                        }
                    }))
                    .onMousePressed(d -> {
                        alwaysMaxParallelSyncer.setValue(!alwaysMaxParallelSyncer.getValue());
                        powerPanelMaxParallelSyncer.setValue(maxParallelSyncer.getValue());
                        return true;
                    })
                    .tooltipBuilder(t -> t.addLine(IKey.lang("GT5U.gui.button.max_parallel")))
                    .tooltipShowUpTimer(TOOLTIP_DELAY));
    }

    protected IWidget makeParallelConfiguratorTextFieldWidget(IntSyncValue maxParallelSyncer,
        BooleanSyncValue alwaysMaxParallelSyncer, IntSyncValue powerPanelMaxParallelSyncer) {
        return new TextFieldWidget().value(powerPanelMaxParallelSyncer)
            .setTextAlignment(Alignment.Center)
            .setNumbers(
                () -> alwaysMaxParallelSyncer.getValue() ? maxParallelSyncer.getValue() : 1,
                maxParallelSyncer::getValue)
            .tooltipBuilder(
                t -> t.addLine(
                    IKey.dynamic(
                        () -> alwaysMaxParallelSyncer.getValue()
                            ? StatCollector
                                .translateToLocalFormatted("GT5U.gui.text.lockedvalue", maxParallelSyncer.getValue())
                            : StatCollector.translateToLocalFormatted(
                                "GT5U.gui.text.rangedvalue",
                                1,
                                maxParallelSyncer.getValue()))))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .size(70, 14)
            .marginBottom(4)
            .marginRight(16);
    }

    protected IWidget createMaintIssueHoverableTerminal(PanelSyncManager syncManager) {
        IntSyncValue maintSyncer = syncManager.findSyncHandler("maintCount", IntSyncValue.class);
        return new DynamicDrawable(() -> {
            switch (maintSyncer.getIntValue()) {
                case 0 -> {
                    return GTGuiTextures.OVERLAY_NO_MAINTENANCE_ISSUES;
                }
                case 6 -> {
                    return GTGuiTextures.OVERLAY_ALL_MAINTENANCE_ISSUES;
                }
                default -> {
                    return GTGuiTextures.OVERLAY_SOME_MAINTENANCE_ISSUES;
                }
            }
        }

        ).asWidget()
            .size(18, 18)
            .marginTop(4)
            .tooltipBuilder(t -> makeMaintenanceHoverableTooltip(t, maintSyncer))
            .tooltipAutoUpdate(true);
    }

    protected void makeMaintenanceHoverableTooltip(RichTooltip t, IntSyncValue maintSyncer) {
        if (maintSyncer.getValue() == 0) {
            t.addLine(IKey.str(EnumChatFormatting.GREEN + "No maintenance issues!"));
            return;
        }
        if (!multiblock.mCrowbar) t.add(
            GTGuiTextures.OVERLAY_NEEDS_CROWBAR.asIcon()
                .size(16, 16))
            .add(" ");
        if (!multiblock.mHardHammer) t.add(
            GTGuiTextures.OVERLAY_NEEDS_HARDHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!multiblock.mScrewdriver) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SCREWDRIVER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!multiblock.mSoftMallet) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOFTHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!multiblock.mSolderingTool) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOLDERING.asIcon()
                .size(16, 16))
            .add(" ");
        if (!multiblock.mWrench) t.add(
            GTGuiTextures.OVERLAY_NEEDS_WRENCH.asIcon()
                .size(16, 16))
            .add(" ");
    }

    protected IWidget createShutdownReasonHoverableTerminal(PanelSyncManager syncManager) {
        BooleanSyncValue wasShutdownSyncer = (BooleanSyncValue) syncManager.getSyncHandlerFromMapKey("wasShutdown:0");
        StringSyncValue shutDownReasonSyncer = (StringSyncValue) syncManager
            .getSyncHandlerFromMapKey("shutdownReasonKey:0");
        return new HoverableIcon(new DynamicDrawable(() -> {
            if (wasShutdownSyncer.getBoolValue()) {
                return getTextureForReason(shutDownReasonSyncer.getValue());
            }
            return null;
        }).asIcon()).asWidget()
            .size(18, 18)
            .tooltipBuilder(t -> {
                if (wasShutdownSyncer.getBoolValue()) {
                    t.add(getToolTipForReason(shutDownReasonSyncer.getValue()));
                }
            })
            .tooltipAutoUpdate(true)
            .setEnabledIf(widget -> shouldShutdownReasonBeDisplayed(shutDownReasonSyncer.getValue()));
    }

    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .height(76)
            .alignX(0)
            .childIf(
                multiblock.doesBindPlayerInventory(),
                SlotGroupWidget.playerInventory(false)
                    .marginLeft(4))
            .child(createButtonColumn(panel, syncManager));
    }

    // children are added bottom to top
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(MainAxis.END)
            .reverseLayout(true)
            .childIf(
                multiblock.doesBindPlayerInventory(),
                new ItemSlot()
                    .slot(
                        new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex())
                            .slotGroup("item_inv"))
                    .marginTop(4))
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));

    }

    protected IWidget createStructureUpdateButton(PanelSyncManager syncManager) {
        return new ToggleButton().size(18, 18)
            .syncHandler("structureUpdateButton")
            .overlay(GTGuiTextures.OVERLAY_BUTTON_STRUCTURE_UPDATE)
            .tooltipBuilder(t -> { t.addLine(IKey.lang("GT5U.gui.button.structure_update")); });
    }

    protected ToggleButton createPowerSwitchButton() {
        return CommonWidgets.createPowerSwitchButton("powerSwitch", isPowerSwitchDisabled(), baseMetaTileEntity);
    }

    protected boolean isPowerSwitchDisabled() {
        return false;
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {
        syncManager.syncValue(
            "errors",
            new GenericSyncValue<EnumSet<StructureError>>(
                multiblock::getStructureErrors,
                multiblock::setStructureErrors,
                new StructureErrorAdapter()));
        syncManager
            .syncValue("errorID", new IntSyncValue(multiblock::getErrorDisplayID, multiblock::setErrorDisplayID));
        syncManager.syncValue(
            "machineActive",
            new BooleanSyncValue(baseMetaTileEntity::isActive, baseMetaTileEntity::setActive));

        syncManager
            .syncValue("wrench", new BooleanSyncValue(() -> multiblock.mWrench, val -> multiblock.mWrench = val));
        syncManager.syncValue(
            "screwdriver",
            new BooleanSyncValue(() -> multiblock.mScrewdriver, val -> multiblock.mScrewdriver = val));
        syncManager.syncValue(
            "softMallet",
            new BooleanSyncValue(() -> multiblock.mSoftMallet, val -> multiblock.mSoftMallet = val));
        syncManager.syncValue(
            "hardHammer",
            new BooleanSyncValue(() -> multiblock.mHardHammer, val -> multiblock.mHardHammer = val));
        syncManager.syncValue(
            "solderingTool",
            new BooleanSyncValue(() -> multiblock.mSolderingTool, val -> multiblock.mSolderingTool = val));
        syncManager
            .syncValue("crowbar", new BooleanSyncValue(() -> multiblock.mCrowbar, val -> multiblock.mCrowbar = val));
        syncManager
            .syncValue("machine", new BooleanSyncValue(() -> multiblock.mMachine, val -> multiblock.mMachine = val));

        syncManager
            .syncValue("totalRunTime", new LongSyncValue(multiblock::getTotalRunTime, multiblock::setTotalRunTime));
        syncManager.syncValue(
            "lastWorkingTick",
            new LongSyncValue(multiblock::getLastWorkingTick, multiblock::setLastWorkingTick));
        BooleanSyncValue wasShutDown = new BooleanSyncValue(
            baseMetaTileEntity::wasShutdown,
            baseMetaTileEntity::setShutdownStatus);
        syncManager.syncValue("wasShutdown", wasShutDown);

        LongSyncValue shutdownDurationSyncer = new LongSyncValue(
            () -> (multiblock.getTotalRunTime() - multiblock.getLastWorkingTick()) / 20);
        syncManager.syncValue("shutdownDuration", shutdownDurationSyncer);

        GenericSyncValue<ShutDownReason> shutdownReasonSyncer = new GenericSyncValue<ShutDownReason>(
            baseMetaTileEntity::getLastShutDownReason,
            baseMetaTileEntity::setShutDownReason,
            new ShutdownReasonAdapter());
        syncManager.syncValue("shutdownReason", shutdownReasonSyncer);

        syncManager.syncValue(
            "shutdownDisplayString",
            new StringSyncValue(
                () -> multiblock.getBaseMetaTileEntity()
                    .getLastShutDownReason()
                    .getDisplayString()));
        syncManager.syncValue(
            "shutdownReasonKey",
            new StringSyncValue(
                () -> multiblock.getBaseMetaTileEntity()
                    .getLastShutDownReason()
                    .getKey()));
        syncManager.syncValue(
            "checkRecipeResult",
            new GenericSyncValue<CheckRecipeResult>(
                multiblock::getCheckRecipeResult,
                multiblock::setCheckRecipeResult,
                new CheckRecipeResultAdapter()));
        syncManager.syncValue(
            "fluidOutput",
            new GenericListSyncHandler<FluidStack>(
                () -> multiblock.mOutputFluids != null ? Arrays.stream(multiblock.mOutputFluids)
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
                val -> multiblock.mOutputFluids = val.toArray(new FluidStack[0]),
                NetworkUtils::readFluidStack,
                NetworkUtils::writeFluidStack,
                (a, b) -> a.isFluidEqual(b) && a.amount == b.amount,
                null));

        syncManager.syncValue(
            "progressTime",
            new IntSyncValue(() -> multiblock.mProgresstime, val -> multiblock.mProgresstime = val));

        IntSyncValue maxProgressTimeSyncer = new IntSyncValue(
            () -> multiblock.mMaxProgresstime,
            val -> multiblock.mMaxProgresstime = val);
        syncManager.syncValue("maxProgressTime", maxProgressTimeSyncer);

        GenericListSyncHandler<ItemStack> itemOutputSyncer = new GenericListSyncHandler<>(
            () -> multiblock.mOutputItems != null ? Arrays.asList(multiblock.mOutputItems) : Collections.emptyList(),
            val -> multiblock.mOutputItems = val.toArray(new ItemStack[0]),
            NetworkUtils::readItemStack,
            NetworkUtils::writeItemStack,
            (a, b) -> a.isItemEqual(b) && a.stackSize == b.stackSize,
            null);
        syncManager.syncValue("itemOutput", itemOutputSyncer);

        StringSyncValue recipeInfoSyncer = new StringSyncValue(multiblock::generateCurrentRecipeInfoString);
        syncManager.syncValue("recipeInfo", recipeInfoSyncer);

        // Widget Specific
        BooleanSyncValue powerSwitchSyncer = new BooleanSyncValue(multiblock::isAllowedToWork, bool -> {
            if (isPowerSwitchDisabled()) return;
            if (bool) multiblock.enableWorking();
            else {
                if (multiblock.maxProgresstime() > 0) {
                    multiblock.disableWorking();
                    wasShutDown.setValue(true);
                    shutdownReasonSyncer.setValue(ShutDownReasonRegistry.NONE);
                } else multiblock.stopMachine(ShutDownReasonRegistry.NONE);
            }
        });
        syncManager.syncValue("powerSwitch", powerSwitchSyncer);

        IntSyncValue structureUpdateSyncer = new IntSyncValue(
            multiblock::getStructureUpdateTime,
            multiblock::setStructureUpdateTime);
        BooleanSyncValue structureUpdateButtonSyncer = new BooleanSyncValue(
            () -> structureUpdateSyncer.getValue() > -20,
            val -> { if (val) structureUpdateSyncer.setValue(1); });
        syncManager.syncValue("structureUpdate", structureUpdateSyncer);
        syncManager.syncValue("structureUpdateButton", structureUpdateButtonSyncer);

        BooleanSyncValue recipeLockSyncer = new BooleanSyncValue(
            multiblock::isRecipeLockingEnabled,
            multiblock::setRecipeLocking);
        syncManager.syncValue("recipeLock", recipeLockSyncer);

        BooleanSyncValue batchModeSyncer = new BooleanSyncValue(
            multiblock::isBatchModeEnabled,
            multiblock::setBatchMode);
        syncManager.syncValue("batchMode", batchModeSyncer);

        IntSyncValue machineModeSyncer = new IntSyncValue(multiblock::getMachineMode, multiblock::setMachineMode);
        syncManager.syncValue("machineMode", machineModeSyncer);

        BooleanSyncValue inputSeparationSyncer = new BooleanSyncValue(
            multiblock::isInputSeparationEnabled,
            multiblock::setInputSeparation);
        syncManager.syncValue("inputSeparation", inputSeparationSyncer);

        IntSyncValue voidExcessSyncer = new IntSyncValue(
            () -> multiblock.getVoidingMode()
                .ordinal(),
            val -> {
                if (multiblock.supportsVoidProtection()) multiblock.setVoidingMode(VoidingMode.fromOrdinal(val));
            });
        syncManager.syncValue("voidExcess", voidExcessSyncer);

        syncManager.registerSlotGroup("item_inv", 1);

        IntSyncValue maintSyncer = new IntSyncValue(() -> {
            int maintIsuses = 0;
            maintIsuses += multiblock.mCrowbar ? 0 : 1;
            maintIsuses += multiblock.mHardHammer ? 0 : 1;
            maintIsuses += multiblock.mScrewdriver ? 0 : 1;
            maintIsuses += multiblock.mSoftMallet ? 0 : 1;
            maintIsuses += multiblock.mSolderingTool ? 0 : 1;
            maintIsuses += multiblock.mWrench ? 0 : 1;
            return maintIsuses;
        });

        syncManager.syncValue("maintCount", maintSyncer);

        BooleanSyncValue mufflerSyncer = new BooleanSyncValue(multiblock::isMuffled, multiblock::setMuffled);
        syncManager.syncValue("mufflerSyncer", mufflerSyncer);
    }

    protected void setMachineModeIcons() {}

    // Method for registering Icons/Tooltip Text to specific ShutDownReasons. Override for custom icons/conditions.

    protected UITexture getTextureForReason(String key) {
        return this.shutdownReasonTextureMap.getOrDefault(key, null);
    }

    protected String getToolTipForReason(String key) {
        return this.shutdownReasonTooltipMap
            .getOrDefault(key, EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.gui.hoverable.error"));
    }
}
