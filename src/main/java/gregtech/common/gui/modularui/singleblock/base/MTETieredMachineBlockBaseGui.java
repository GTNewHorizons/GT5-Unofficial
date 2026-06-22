package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.common.CommonButtons;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;

/**
 * A base class for singleblock MUI2 guis. Has configurable corner panels and makes building ui's easier.
 * The main overriding is done in
 * {@link #createContentSection(com.cleanroommc.modularui.screen.ModularPanel, com.cleanroommc.modularui.value.sync.PanelSyncManager)}
 *
 * For heavily custom UI's, override
 * {@link #build(com.cleanroommc.modularui.factory.PosGuiData, com.cleanroommc.modularui.value.sync.PanelSyncManager, com.cleanroommc.modularui.screen.UISettings)}
 * instead.
 */
public class MTETieredMachineBlockBaseGui<T extends MTETieredMachineBlock> {

    public static final int SLOT_SIZE = 18;

    protected final T machine;
    protected final IGregTechTileEntity baseMetaTileEntity;

    public MTETieredMachineBlockBaseGui(T machine) {
        this.machine = machine;
        this.baseMetaTileEntity = machine.getBaseMetaTileEntity();
    }

    private final int borderRadius = 4;

    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);

        ModularPanel panel = this.getBasePanel(guiData, syncManager, uiSettings);

        return panel.child(
            Flow.column()
                .full()
                .padding(borderRadius)
                .child(createContentHolder(panel, syncManager))
                .childIf(this.supportsTopRightCornerFlow(), this::createTopRightCornerFlow)
                .childIf(machine.supportsInventoryRow(), () -> createInventoryRow(panel, syncManager)));
    }

    protected void registerSyncValues(PanelSyncManager syncManager) {
        BooleanSyncValue powerSwitchSyncer = new BooleanSyncValue(baseMetaTileEntity::isAllowedToWork, bool -> {
            if (bool) baseMetaTileEntity.enableWorking();
            else baseMetaTileEntity.disableWorking();
        }).allowC2S();
        syncManager.syncValue("powerSwitch", powerSwitchSyncer);

        BooleanSyncValue mufflerSyncer = new BooleanSyncValue(
            baseMetaTileEntity::isMuffled,
            baseMetaTileEntity::setMuffler).allowC2S();
        syncManager.syncValue("mufflerSyncer", mufflerSyncer);
    }

    protected ModularPanel getBasePanel(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new GTBaseGuiBuilder(machine, guiData, syncManager, uiSettings).setWidth(getBasePanelWidth())
            .setHeight(getBasePanelHeight())
            .doesAddGregTechLogo(false)
            .doesAddGhostCircuitSlot(false)
            // Has to be replaced with inventory row for alignment reasons
            .doesBindPlayerInventory(false)
            .build();
    }

    protected int getBasePanelWidth() {
        return 176;
    }

    protected int getBasePanelHeight() {
        return 166;
    }

    protected boolean supportsBottomRowOverlap() {
        return false;
    }

    private ParentWidget<?> createContentHolder(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> holder = supportsBottomRowOverlap() ? new ParentWidget<>()
            : Flow.column()
                .childPadding(2);

        return holder.size(getContentHolderWidth(), getContentHolderHeight())
            .padding(getContentPaddingHorizontal(), getContentPaddingVertical())
            .child(createContentSection(panel, syncManager))
            .child(createBottomSection(panel, syncManager));
    }

    protected static ParentWidget<?> getEmptyContent() {
        return new ParentWidget<>().expanded()
            .fullWidth();
    }

    protected static ParentWidget<?> getOverlappingEmptyContent() {
        return new ParentWidget<>().full();
    }

    /**
     * Override this method to make a custom GUI.
     */
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return supportsBottomRowOverlap() ? getOverlappingEmptyContent() : getEmptyContent();
    }

    protected int getContentPaddingHorizontal() {
        return 3;
    }

    protected int getContentPaddingVertical() {
        return 2;
    }

    protected int getContentHolderWidth() {
        return getBasePanelWidth() - 2 * borderRadius;
    }

    protected int getContentHolderHeight() {
        return (getBasePanelHeight() - borderRadius * 2) - (machine.doesBindPlayerInventory() ? 80 : 0);
    }

    /**
     * Contains the bottom left and bottom right corner flows. Usually the place for the logo, buttons,
     * or extra slots.
     * <p>
     * Will not overlap with the main content section by default, override {@link #supportsBottomRowOverlap()}
     * to change this behaviour.
     */
    protected ParentWidget<?> createBottomSection(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> bottomSection = new ParentWidget<>().fullWidth()
            .coverChildrenHeight()
            .childIf(this.supportsBottomLeftCornerFlow(), () -> createBottomLeftCornerFlow(panel, syncManager))
            .childIf(this.supportsBottomRightCornerFlow(), () -> createBottomRightCornerFlow(panel, syncManager));

        if (supportsBottomRowOverlap()) bottomSection.bottomRel(0);

        return bottomSection;
    }

    protected boolean supportsBottomLeftCornerFlow() {
        return true;
    }

    // Row by default, going left to right
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .verticalCenter()
            .leftRel(0);
    }

    protected boolean supportsBottomRightCornerFlow() {
        return true;
    }

    // Row by default, going right to left
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .coverChildren()
            .verticalCenter()
            .rightRel(0)
            .childIf(this.doesAddGregTechLogo(), this::makeLogoWidget)
            .childIf(this.doesAddCircuitSlot(), () -> this.createCircuitSlot(syncManager));
    }

    protected boolean doesAddGregTechLogo() {
        return true;
    }

    protected IWidget createInventoryRow(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .fullWidth()
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .height(76)
            .childIf(machine.doesBindPlayerInventory(), () -> SlotGroupWidget.playerInventory(false));
    }

    protected boolean doesAddSpecialSlot() {
        return true;
    }

    protected boolean supportsPowerSwitch() {
        return true;
    }

    protected boolean supportsMuffler() {
        return true;
    }

    protected Widget<?> makeLogoWidget() {
        return new IDrawable.DrawableWidget(IDrawable.EMPTY).size(SLOT_SIZE)
            .widgetTheme(GTWidgetThemes.PICTURE_LOGO);
    }

    // will add if the machine is an instance of IConfigurationCircuitSupport
    protected boolean doesAddCircuitSlot() {
        return machine instanceof IConfigurationCircuitSupport cc && cc.allowSelectCircuit();
    }

    protected Widget<?> createCircuitSlot(PanelSyncManager syncManager) {
        return CommonWidgets.createCircuitSlot(syncManager, machine)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected boolean supportsTopRightCornerFlow() {
        return true;
    }

    protected Flow createTopRightCornerFlow() {
        return Flow.column()
            .coverChildren()
            .topRel(0)
            .rightRel(0)
            .childIf(supportsMuffler(), this::createMufflerButton)
            .childIf(supportsPowerSwitch(), this::createPowerSwitchButton);
    }

    protected ToggleButton createMufflerButton() {
        return CommonButtons.createMuffleButton("mufflerSyncer")
            .disableThemeBackground(true)
            .disableHoverThemeBackground(true);
    }

    protected ToggleButton createPowerSwitchButton() {
        return CommonButtons.createSmallPowerSwitchButton("powerSwitch")
            .disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected ItemSlot createChargerSlot() {
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.rechargerSlotStartIndex()))
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CHARGER)
            .tooltip(this::createTooltipForChargerSlot)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected void createTooltipForChargerSlot(RichTooltip tooltip) {
        final byte machineTier = machine.mTier;
        String tierName = GTUtility.getColoredTierNameFromTier(machineTier);
        tooltip.addLine(GTUtility.translate("GT5U.machines.battery_slot.tooltip"))
            .addLine(GTUtility.translate("GT5U.machines.battery_slot.tooltip.1", tierName))
            .addLine(GTUtility.translate("GT5U.machines.battery_slot.tooltip.2", tierName));
    }

    /// Sets a static tooltip using the machine's tooltip cache. This means the tooltip **can not** change while the Gui
    /// is open.
    ///
    /// Accounts for extended tooltips shown when shift is held down.
    protected Consumer<RichTooltip> configureTooltip(String key, Object... args) {
        GTTooltipDataCache.TooltipData data = machine.mTooltipCache.getData(key, args);
        return addToRichTooltip(() -> data);
    }

    /// Sets a dynamic tooltip without saving it to the machine's tooltip cache. This means the tooltip **can** change
    /// while the Gui is open.
    ///
    /// Accounts for extended tooltips shown when shift is held down.
    @SafeVarargs
    protected final Consumer<RichTooltip> configureDynamicTooltip(String key, Supplier<Object>... args) {
        return addToRichTooltip(
            () -> machine.mTooltipCache.getUncachedTooltipData(
                key,
                Arrays.stream(args)
                    .map(Supplier::get)
                    .toArray()));
    }

    protected static @NotNull Consumer<RichTooltip> addToRichTooltip(Supplier<GTTooltipDataCache.TooltipData> data) {
        return t -> t.addStringLines(Interactable.hasShiftDown() ? data.get().shiftText : data.get().text)
            .titleMargin(2);
    }
}
