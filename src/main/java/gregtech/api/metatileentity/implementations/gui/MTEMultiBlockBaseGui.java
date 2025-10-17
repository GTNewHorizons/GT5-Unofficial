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
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.item.Item;
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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.widgets.ResizableItemDisplayWidget;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.modularui2.sync.Predicates;

public class MTEMultiBlockBaseGui {

    protected final MTEMultiBlockBase base;
    private final IGregTechTileEntity baseMetaTileEntity;
    protected List<UITexture> machineModeIcons = new ArrayList<>();
    protected Map<String, UITexture> customIcons = new HashMap<>();
    private final int borderRadius = 4;
    protected final int textBoxToInventoryGap = 26;
    protected final Map<String, IPanelHandler> panelMap = new HashMap<>();
    protected Map<String, UITexture> shutdownReasonTextureMap = new HashMap<>();
    protected Map<String, String> shutdownReasonTooltipMap = new HashMap<>();

    public MTEMultiBlockBaseGui(MTEMultiBlockBase base) {
        this.base = base;
        this.baseMetaTileEntity = base.getBaseMetaTileEntity();
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
        return panel.child(
            Flow.column()
                .padding(borderRadius)
                .child(createTerminalRow(panel, syncManager))
                .child(createPanelGap(panel, syncManager))
                .child(createInventoryRow(panel, syncManager)));
    }

    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(base, guiData, syncManager, uiSettings).setWidth(getBasePanelWidth())
            .setHeight(getBasePanelHeight())
            .doesAddGregTechLogo(false)
            // Has to be replaced with inventory row to fit buttons
            .doesBindPlayerInventory(false)
            .doesAddMufflerButton(base.canBeMuffled())
            .build();
    }

    protected int getBasePanelWidth() {
        return 198;
    }

    protected int getBasePanelHeight() {
        return 181 + textBoxToInventoryGap;
    }

    protected Flow createTerminalRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().size(getTerminalRowWidth(), getTerminalRowHeight())
            .child(
                new ParentWidget<>().size(getTerminalWidgetWidth(), getTerminalWidgetHeight())
                    .paddingTop(4)
                    .paddingBottom(4)
                    .paddingLeft(4)
                    .paddingRight(0)
                    .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
                    .child(
                        createTerminalTextWidget(syncManager, panel)
                            .size(getTerminalWidgetWidth() - 4, getTerminalWidgetHeight() - 8)
                            .collapseDisabledChild())
                    .childIf(base.supportsTerminalCornerColumn(), createTerminalCornerColumn(panel, syncManager)));
    }

    protected Flow createTerminalCornerColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().coverChildren()
            .rightRel(0, 6, 0)
            .bottomRel(0, 6, 0)
            .childIf(base.supportsShutdownReasonHoverable(), createShutdownReasonHoverableTerminal(syncManager))
            .childIf(base.supportsMaintenanceIssueHoverable(), createMaintIssueHoverableTerminal(syncManager))
            .childIf(
                base.supportsLogo(),
                new Widget<>().size(18, 18)
                    .marginTop(4)
                    .widgetTheme(GTWidgetThemes.PICTURE_LOGO));
    }

    protected int getTerminalRowWidth() {
        return 190;
    }

    protected int getTerminalRowHeight() {
        return base.doesBindPlayerInventory() ? 94 : 174;
    }

    protected int getTerminalWidgetWidth() {
        return getTerminalRowWidth();
    }

    protected int getTerminalWidgetHeight() {
        return getTerminalRowHeight();
    }

    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        IntSyncValue startupCheckSyncer = new IntSyncValue(base::getmStartUpCheck);
        syncManager.syncValue("startupCheck", startupCheckSyncer);

        return new ListWidget<>().widthRel(1)
            .child(
                IKey.lang("GT5U.multiblock.startup")
                    .color(Color.WHITE.main)
                    .asWidget()
                    .alignment(Alignment.CenterLeft)
                    .setEnabledIf(w -> startupCheckSyncer.getValue() > 0)
                    .marginBottom(2)
                    .widthRel(1))
            .child(
                new TextWidget<>(GTUtility.trans("142", "Running perfectly.")).color(Color.WHITE.main)
                    .setEnabledIf(widget -> base.getErrorDisplayID() == 0 && baseMetaTileEntity.isActive())
                    .marginBottom(2)
                    .widthRel(1))
            .child(createShutdownDurationWidget(syncManager))
            .child(createShutdownReasonWidget(syncManager))
            .child(createRecipeResultWidget())
            .childIf(base.showRecipeTextInGUI(), createRecipeInfoTextWidget(syncManager))

            .childIf(base.showRecipeTextInGUI(), createRecipeInfoWidget(syncManager));
    }

    protected IWidget createShutdownDurationWidget(PanelSyncManager syncManager) {
        LongSyncValue shutdownDurationSyncer = (LongSyncValue) syncManager.getSyncHandler("shutdownDuration:0");
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
                widget -> base.shouldDisplayShutDownReason() && !baseMetaTileEntity.isActive()
                    && !baseMetaTileEntity.isAllowedToWork());

    }

    protected IWidget createShutdownReasonWidget(PanelSyncManager syncManager) {
        StringSyncValue shutdownReasonSync = (StringSyncValue) syncManager.getSyncHandler("shutdownDisplayString:0");
        return IKey.dynamic(shutdownReasonSync::getValue)
            .asWidget()
            .widthRel(1)
            .marginBottom(2)
            .setEnabledIf(widget -> shouldShutdownReasonBeDisplayed(shutdownReasonSync.getValue()));
    }

    protected boolean shouldShutdownReasonBeDisplayed(String shutdownString) {
        return base.shouldDisplayShutDownReason() && !baseMetaTileEntity.isActive()
            && !baseMetaTileEntity.isAllowedToWork()
            && GTUtility.isStringValid(shutdownString);
    }

    private IWidget createRecipeResultWidget() {
        return IKey.dynamic(
            () -> base.getCheckRecipeResult()
                .getDisplayString())
            .asWidget()
            .widthRel(1)
            .marginBottom(2)
            .setEnabledIf(widget -> shouldRecipeResultBeDisplayed());

    }

    private boolean shouldRecipeResultBeDisplayed() {
        CheckRecipeResult recipeResult = base.getCheckRecipeResult();
        return base.shouldDisplayCheckRecipeResult() && GTUtility.isStringValid(recipeResult.getDisplayString())
            && (base.isAllowedToWork() || baseMetaTileEntity.isActive() || recipeResult.persistsOnShutdown());
    }

    private IWidget createRecipeInfoWidget(PanelSyncManager syncManager) {
        IntSyncValue maxProgressTimeSyncer = (IntSyncValue) syncManager.getSyncHandler("maxProgressTime:0");
        GenericListSyncHandler<ItemStack> itemOutputSyncer = (GenericListSyncHandler<ItemStack>) syncManager
            .getSyncHandler("itemOutput:0");
        GenericListSyncHandler<FluidStack> fluidOutputSyncer = (GenericListSyncHandler<FluidStack>) syncManager
            .getSyncHandler("fluidOutput:0");

        DynamicSyncHandler recipeHandler = new DynamicSyncHandler().widgetProvider((syncManager1, packet) -> {
            if (packet == null) {
                return new EmptyWidget();
            }
            return Flow.column()
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

    private final int DISPLAY_ROW_HEIGHT = 13;

    private IWidget createItemRecipeInfo(PacketBuffer packet, PanelSyncManager syncManager) {
        int size = packet.readInt();
        Flow column = Flow.column()
            .coverChildren();

        Map<ItemDisplayKey, Long> itemDisplayMap = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ItemStack item = NetworkUtils.readItemStack(packet);
            itemDisplayMap
                .merge(new ItemDisplayKey(item.getItem(), item.getItemDamage()), (long) item.stackSize, Long::sum);
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
            Item item = entry.getKey()
                .item();
            int damage = entry.getKey()
                .damage();
            long amount = entry.getValue();
            column.child(
                Flow.row()
                    .widthRel(1)
                    .height(DISPLAY_ROW_HEIGHT)
                    .child(createItemDrawable(item, damage))
                    .child(createHoverableTextForItem(item, damage, amount, syncManager)));
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
        return IKey.dynamic(() -> ((StringSyncValue) syncManager.getSyncHandler("recipeInfo:0")).getValue())
            .asWidget()
            .marginBottom(2)
            .widthRel(1)
            .setEnabledIf(
                widget -> Predicates.isNonEmptyList(syncManager.getSyncHandler("itemOutput:0"))
                    || Predicates.isNonEmptyList(syncManager.getSyncHandler("fluidOutput:0")));
    }

    private ResizableItemDisplayWidget createItemDrawable(Item item, int damage) {
        return new ResizableItemDisplayWidget().background(IDrawable.EMPTY)
            .displayAmount(false)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            // Second argument is stacksize, don't care about it
            .item(new ItemStack(item, 1, damage))
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(1);
    }

    private TextWidget<?> createHoverableTextForItem(Item item, int damage, long amount, PanelSyncManager syncManager) {
        // Second argument is stacksize, don't care about it
        ItemStack itemStack = new ItemStack(item, 1, damage);
        IntSyncValue maxProgressTimeSyncer = (IntSyncValue) syncManager.getSyncHandler("maxProgressTime:0");
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
        String itemTextLine = EnumChatFormatting.AQUA + GTUtility.truncateText(itemName, 48 - amountString.length())
            + amountString;
        return itemTextLine;
    }

    private ResizableItemDisplayWidget createFluidDrawable(FluidStack fluidStack) {
        // uses an itemstack representation of the fluid as there is no FluidDisplayWidget (yet)
        ItemStack fluidDisplayStack = GTUtility.getFluidDisplayStack(fluidStack, false, false);
        return new ResizableItemDisplayWidget().background(IDrawable.EMPTY)
            .displayAmount(false)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .item(fluidDisplayStack)
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(1);
    }

    private TextWidget<?> createHoverableTextForFluid(FluidStack fluidStack, long amount,
        PanelSyncManager syncManager) {
        IntSyncValue maxProgressSyncer = (IntSyncValue) syncManager.getSyncHandler("maxProgressTime:0");
        String fluidName = EnumChatFormatting.AQUA + fluidStack.getLocalizedName() + EnumChatFormatting.RESET;

        return new TextWidget<>(IKey.dynamic(() -> getFluidTextLine(fluidName, amount, maxProgressSyncer)))
            .height(DISPLAY_ROW_HEIGHT)
            .scale(0.75f)
            .tooltip(
                t -> t.addLine(
                    EnumChatFormatting.AQUA + fluidName
                        + "\n"
                        + GTUtility.appendRate(true, amount, false, maxProgressSyncer.getValue())));
    }

    private @NotNull String getFluidTextLine(String fluidName, long amount, IntSyncValue maxProgressTimeSyncer) {
        String amountString = EnumChatFormatting.WHITE + " x "
            + EnumChatFormatting.GOLD
            + GTUtility.formatShortenedLong(amount)
            + "L"
            + EnumChatFormatting.WHITE
            + GTUtility.appendRate(false, amount, true, maxProgressTimeSyncer.getValue());
        String fluidTextLine = EnumChatFormatting.AQUA + GTUtility.truncateText(fluidName, 48 - amountString.length())
            + amountString;
        return fluidTextLine;
    }

    // TODO: separate panel gap into 'left row' and 'right row', for easier usage
    protected Flow createPanelGap(ModularPanel parent, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .paddingRight(6)
            .paddingLeft(4)
            .height(textBoxToInventoryGap)
            .child(createVoidExcessButton(syncManager))
            .child(createInputSeparationButton(syncManager))
            .child(createBatchModeButton(syncManager))
            .child(createLockToSingleRecipeButton(syncManager))
            .childIf(!machineModeIcons.isEmpty(), createModeSwitchButton(syncManager))
            .childIf(base.supportsPowerPanel(), createPowerPanelButton(syncManager, parent));
    }

    protected IWidget createVoidExcessButton(PanelSyncManager syncManager) {
        IntSyncValue voidExcessSyncer = (IntSyncValue) syncManager.getSyncHandler("voidExcess:0");
        return new ButtonWidget<>().size(18, 18)
            .onMousePressed(mouseButton -> this.voidExcessOnMousePressed(mouseButton, voidExcessSyncer))
            .overlay(base.supportsVoidProtection() ? getVoidExcessOverlay() : getForcedVoidExcessOverlay())
            .tooltipBuilder(this::createVoidExcessTooltip);
    }

    private boolean voidExcessOnMousePressed(int mouseButton, IntSyncValue voidExcessSyncer) {
        if (!base.supportsVoidProtection()) return false;
        Set<VoidingMode> allowed = base.getAllowedVoidingModes();
        int voidingMode = 0;
        switch (mouseButton) {
            case 0 -> voidingMode = base.getVoidingMode()
                .nextInCollection(allowed)
                .ordinal();
            case 1 -> voidingMode = base.getVoidingMode()
                .previousInCollection(allowed)
                .ordinal();
        }
        voidExcessSyncer.setValue(voidingMode);
        return true;
    }

    private IDrawable getForcedVoidExcessOverlay() {
        return new DrawableStack(base.getVoidingMode().buttonOverlay, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
    }

    private IDrawable getVoidExcessOverlay() {
        return new DynamicDrawable(() -> base.getVoidingMode().buttonOverlay);
    }

    private void createVoidExcessTooltip(RichTooltip t) {
        t.addLine(IKey.dynamic(() -> StatCollector.translateToLocal("GT5U.gui.button.voiding_mode")))
            .addLine(
                IKey.dynamic(
                    () -> StatCollector.translateToLocal(
                        base.getVoidingMode()
                            .getTransKey())));
        if (!base.supportsVoidProtection()) {
            t.addLine(IKey.lang(BUTTON_FORBIDDEN_TOOLTIP));
        }
    }

    protected IWidget createInputSeparationButton(PanelSyncManager syncManager) {
        BooleanSyncValue inputSeparationSyncer = (BooleanSyncValue) syncManager.getSyncHandler("inputSeparation:0");
        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!base.supportsInputSeparation()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(getInputSeparationSyncValue(inputSeparationSyncer))
            .overlay(
                base.supportsInputSeparation() ? getInputSeparationOverlay(inputSeparationSyncer)
                    : getForcedInputSeparationOverlay())
            .tooltipBuilder(this::createInputSeparationTooltip);
    }

    private BooleanSyncValue getInputSeparationSyncValue(BooleanSyncValue inputSeparationSyncer) {
        return new BooleanSyncValue(() -> inputSeparationSyncer.getValue() || !base.supportsInputSeparation(), bool -> {
            if (base.supportsInputSeparation()) {
                inputSeparationSyncer.setValue(bool);
            }
        });
    }

    private IDrawable getForcedInputSeparationOverlay() {
        UITexture texture = base.isBatchModeEnabled() ? GTGuiTextures.OVERLAY_BUTTON_INPUT_SEPARATION_ON_DISABLED
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
            base::supportsInputSeparation,
            base::isInputSeparationEnabled,
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
     * @see gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures#addDynamicTooltipOfFeatureToButton(Widget,
     *      Supplier, Supplier, String, String) For equivalent method but made for ModularUI
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
        IntSyncValue machineModeSyncer = (IntSyncValue) syncManager.getSyncHandler("machineMode:0");
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
            .addLine(IKey.dynamic(base::getMachineModeName));
    }

    protected IWidget createBatchModeButton(PanelSyncManager syncManager) {
        BooleanSyncValue batchModeSyncer = (BooleanSyncValue) syncManager.getSyncHandler("batchMode:0");

        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!base.supportsBatchMode()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(getBatchModeSyncValue(batchModeSyncer))
            .overlay(base.supportsBatchMode() ? getBatchModeOverlay(batchModeSyncer) : getForcedBatchModeOverlay())
            .tooltipBuilder(this::createBatchModeTooltip);
    }

    private BooleanSyncValue getBatchModeSyncValue(BooleanSyncValue batchModeSyncer) {
        return new BooleanSyncValue(() -> batchModeSyncer.getValue() || !base.supportsBatchMode(), bool -> {
            if (base.supportsBatchMode()) {
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
        UITexture texture = base.isBatchModeEnabled() ? GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED
            : GTGuiTextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED;
        return new DrawableStack(texture, GTGuiTextures.OVERLAY_BUTTON_FORBIDDEN);
    }

    private void createBatchModeTooltip(RichTooltip t) {
        addDynamicTooltipOfFeatureToButton(
            t,
            base::supportsBatchMode,
            base::isBatchModeEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_on"),
            StatCollector.translateToLocal("GT5U.gui.button.batch_mode_off"));
    }

    protected IWidget createLockToSingleRecipeButton(PanelSyncManager syncManager) {
        BooleanSyncValue recipeLockSyncer = (BooleanSyncValue) syncManager.getSyncHandler("recipeLock:0");
        return new ToggleButton() {

            @NotNull
            @Override
            public Result onMousePressed(int mouseButton) {
                if (!base.supportsSingleRecipeLocking()) return Result.IGNORE;
                return super.onMousePressed(mouseButton);
            }
        }.size(18, 18)
            .value(getRecipeLockSyncValue(recipeLockSyncer))
            .overlay(
                base.supportsSingleRecipeLocking() ? getRecipeLockOverlay(recipeLockSyncer)
                    : getForcedRecipeLockOverlay())
            .tooltipBuilder(this::createRecipeLockTooltip);
    }

    private BooleanSyncValue getRecipeLockSyncValue(BooleanSyncValue recipeLockSyncer) {
        return new BooleanSyncValue(() -> recipeLockSyncer.getValue() || !base.supportsSingleRecipeLocking(), bool -> {
            if (base.supportsSingleRecipeLocking()) {
                recipeLockSyncer.setValue(bool);
            }
        });
    }

    private IDrawable getForcedRecipeLockOverlay() {
        UITexture texture = base.mLockedToSingleRecipe ? GTGuiTextures.OVERLAY_BUTTON_RECIPE_LOCKED_DISABLED
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
            base::supportsSingleRecipeLocking,
            base::isRecipeLockingEnabled,
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_on"),
            StatCollector.translateToLocal("GT5U.gui.button.lock_recipe_off"));
    }

    protected IWidget createPowerPanelButton(PanelSyncManager syncManager, ModularPanel parent) {
        IPanelHandler powerPanel = syncManager
            .panel("powerPanel", (p_syncManager, syncHandler) -> openPowerControlPanel(p_syncManager, parent), true);
        return new ButtonWidget<>().size(18, 18)
            .right(2)
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
            base::makesPowerfailEvents,
            base::setPowerfailEventCreationStatus);
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
        IntSyncValue maxParallelSyncer = new IntSyncValue(base::getMaxParallelRecipes, base::setMaxParallelForPanel);
        BooleanSyncValue alwaysMaxParallelSyncer = new BooleanSyncValue(
            base::isAlwaysMaxParallel,
            base::setAlwaysMaxParallel);
        syncManager.syncValue("maxParallel", maxParallelSyncer);
        syncManager.syncValue("alwaysMaxParallel", alwaysMaxParallelSyncer);

        // The PanelSyncManager seems to belong to absolutely nothing?
        // Not sure how that works but trying to use .syncHandler instead of .value causes a crash because
        // This PanelSyncManager has no panel and the widget tries to get a syncHandler from "powerPanel"
        IntSyncValue powerPanelMaxParallelSyncer = new IntSyncValue(
            base::getPowerPanelMaxParallel,
            base::setPowerPanelMaxParallel);
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
        IntSyncValue maintSyncer = (IntSyncValue) syncManager.getSyncHandler("maintCount:0");
        return new DynamicDrawable(
            () -> maintSyncer.getValue() == 0 ? GTGuiTextures.OVERLAY_NO_MAINTENANCE_ISSUES
                : IKey.str(EnumChatFormatting.DARK_RED + String.valueOf(maintSyncer.getValue()))).asWidget()
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
        if (!base.mCrowbar) t.add(
            GTGuiTextures.OVERLAY_NEEDS_CROWBAR.asIcon()
                .size(16, 16))
            .add(" ");
        if (!base.mHardHammer) t.add(
            GTGuiTextures.OVERLAY_NEEDS_HARDHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!base.mScrewdriver) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SCREWDRIVER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!base.mSoftMallet) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOFTHAMMER.asIcon()
                .size(16, 16))
            .add(" ");
        if (!base.mSolderingTool) t.add(
            GTGuiTextures.OVERLAY_NEEDS_SOLDERING.asIcon()
                .size(16, 16))
            .add(" ");
        if (!base.mWrench) t.add(
            GTGuiTextures.OVERLAY_NEEDS_WRENCH.asIcon()
                .size(16, 16))
            .add(" ");
    }

    protected IWidget createShutdownReasonHoverableTerminal(PanelSyncManager syncManager) {
        BooleanSyncValue wasShutdownSyncer = (BooleanSyncValue) syncManager.getSyncHandler("wasShutdown:0");
        StringSyncValue shutDownReasonSyncer = (StringSyncValue) syncManager.getSyncHandler("shutdownReasonKey:0");
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
            .tooltipAutoUpdate(true);
    }

    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return new Row().widthRel(1)
            .height(76)
            .alignX(0)
            .childIf(
                base.doesBindPlayerInventory(),
                SlotGroupWidget.playerInventory(false)
                    .marginLeft(4))
            .child(createButtonColumn(panel, syncManager));
    }

    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(MainAxis.END)
            .child(createStructureUpdateButton(syncManager))
            .child(createPowerSwitchButton())
            .childIf(
                base.doesBindPlayerInventory(),
                new ItemSlot()
                    .slot(new ModularSlot(base.inventoryHandler, base.getControllerSlotIndex()).slotGroup("item_inv")));
    }

    protected IWidget createStructureUpdateButton(PanelSyncManager syncManager) {
        return new ToggleButton().size(18, 18)
            .syncHandler("structureUpdateButton")
            .overlay(GTGuiTextures.OVERLAY_BUTTON_STRUCTURE_UPDATE)
            .tooltipBuilder(t -> { t.addLine(IKey.lang("GT5U.gui.button.structure_update")); });
    }

    protected IWidget createPowerSwitchButton() {
        return new ToggleButton().syncHandler("powerSwitch")
            .tooltip(tooltip -> tooltip.add("Power Switch"))
            .size(18, 18)
            .marginBottom(4)
            .overlay(
                new DynamicDrawable(
                    () -> isPowerSwitchDisabled() ? this.customIcons.get("power_switch_disabled")
                        : baseMetaTileEntity.isAllowedToWork() ? this.customIcons.get("power_switch_on")
                            : this.customIcons.get("power_switch_off")));
    }

    protected boolean isPowerSwitchDisabled() {
        return false;
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
            new BooleanSyncValue(baseMetaTileEntity::isActive, baseMetaTileEntity::setActive));

        syncManager.syncValue("wrench", new BooleanSyncValue(() -> base.mWrench, val -> base.mWrench = val));
        syncManager
            .syncValue("screwdriver", new BooleanSyncValue(() -> base.mScrewdriver, val -> base.mScrewdriver = val));
        syncManager
            .syncValue("softMallet", new BooleanSyncValue(() -> base.mSoftMallet, val -> base.mSoftMallet = val));
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
            baseMetaTileEntity::wasShutdown,
            baseMetaTileEntity::setShutdownStatus);
        syncManager.syncValue("wasShutdown", wasShutDown);

        LongSyncValue shutdownDurationSyncer = new LongSyncValue(
            () -> (base.getTotalRunTime() - base.getLastWorkingTick()) / 20);
        syncManager.syncValue("shutdownDuration", shutdownDurationSyncer);

        syncManager.syncValue(
            "shutdownReason",
            new GenericSyncValue<ShutDownReason>(
                baseMetaTileEntity::getLastShutDownReason,
                baseMetaTileEntity::setShutDownReason,
                new ShutdownReasonAdapter()));
        syncManager.syncValue(
            "shutdownDisplayString",
            new StringSyncValue(
                () -> base.getBaseMetaTileEntity()
                    .getLastShutDownReason()
                    .getDisplayString()));
        syncManager.syncValue(
            "shutdownReasonKey",
            new StringSyncValue(
                () -> base.getBaseMetaTileEntity()
                    .getLastShutDownReason()
                    .getKey()));
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
                NetworkUtils::writeFluidStack,
                (a, b) -> a.isFluidEqual(b) && a.amount == b.amount,
                null));

        syncManager
            .syncValue("progressTime", new IntSyncValue(() -> base.mProgresstime, val -> base.mProgresstime = val));

        IntSyncValue maxProgressTimeSyncer = new IntSyncValue(
            () -> base.mMaxProgresstime,
            val -> base.mMaxProgresstime = val);
        syncManager.syncValue("maxProgressTime", maxProgressTimeSyncer);

        GenericListSyncHandler<ItemStack> itemOutputSyncer = new GenericListSyncHandler<>(
            () -> base.mOutputItems != null ? Arrays.asList(base.mOutputItems) : Collections.emptyList(),
            val -> base.mOutputItems = val.toArray(new ItemStack[0]),
            NetworkUtils::readItemStack,
            NetworkUtils::writeItemStack,
            (a, b) -> a.isItemEqual(b) && a.stackSize == b.stackSize,
            null);
        syncManager.syncValue("itemOutput", itemOutputSyncer);

        StringSyncValue recipeInfoSyncer = new StringSyncValue(base::generateCurrentRecipeInfoString);
        syncManager.syncValue("recipeInfo", recipeInfoSyncer);

        // Widget Specific
        BooleanSyncValue powerSwitchSyncer = new BooleanSyncValue(base::isAllowedToWork, bool -> {
            if (isPowerSwitchDisabled()) return;
            if (bool) base.enableWorking();
            else {
                if (base.maxProgresstime() > 0) base.disableWorking();
                else base.stopMachine(ShutDownReasonRegistry.NONE);
            }
        });
        syncManager.syncValue("powerSwitch", powerSwitchSyncer);

        IntSyncValue structureUpdateSyncer = new IntSyncValue(
            base::getStructureUpdateTime,
            base::setStructureUpdateTime);
        BooleanSyncValue structureUpdateButtonSyncer = new BooleanSyncValue(
            () -> structureUpdateSyncer.getValue() > -20,
            val -> { if (val) structureUpdateSyncer.setValue(1); });
        syncManager.syncValue("structureUpdate", structureUpdateSyncer);
        syncManager.syncValue("structureUpdateButton", structureUpdateButtonSyncer);

        BooleanSyncValue recipeLockSyncer = new BooleanSyncValue(base::isRecipeLockingEnabled, base::setRecipeLocking);
        syncManager.syncValue("recipeLock", recipeLockSyncer);

        BooleanSyncValue batchModeSyncer = new BooleanSyncValue(base::isBatchModeEnabled, base::setBatchMode);
        syncManager.syncValue("batchMode", batchModeSyncer);

        IntSyncValue machineModeSyncer = new IntSyncValue(base::getMachineMode, base::setMachineMode);
        syncManager.syncValue("machineMode", machineModeSyncer);

        BooleanSyncValue inputSeparationSyncer = new BooleanSyncValue(
            base::isInputSeparationEnabled,
            base::setInputSeparation);
        syncManager.syncValue("inputSeparation", inputSeparationSyncer);

        IntSyncValue voidExcessSyncer = new IntSyncValue(
            () -> base.getVoidingMode()
                .ordinal(),
            val -> { if (base.supportsVoidProtection()) base.setVoidingMode(VoidingMode.fromOrdinal(val)); });
        syncManager.syncValue("voidExcess", voidExcessSyncer);

        syncManager.registerSlotGroup("item_inv", 1);

        IntSyncValue maintSyncer = new IntSyncValue(() -> {
            int maintIsuses = 0;
            maintIsuses += base.mCrowbar ? 0 : 1;
            maintIsuses += base.mHardHammer ? 0 : 1;
            maintIsuses += base.mScrewdriver ? 0 : 1;
            maintIsuses += base.mSoftMallet ? 0 : 1;
            maintIsuses += base.mSolderingTool ? 0 : 1;
            maintIsuses += base.mWrench ? 0 : 1;
            return maintIsuses;
        });

        syncManager.syncValue("maintCount", maintSyncer);

        BooleanSyncValue mufflerSyncer = new BooleanSyncValue(base::isMuffled, base::setMuffled);
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
