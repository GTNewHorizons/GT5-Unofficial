package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.gtnewhorizons.modularui.api.widget.Interactable;

import gregtech.api.enums.TieredVariant;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.util.MachineModularSlot;
import gregtech.common.modularui2.widget.GTProgressWidget;
import it.unimi.dsi.fastutil.chars.CharList;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;

public class MTEBasicMachineBaseGui<T extends MTEBasicMachine> extends MTETieredMachineBlockBaseGui<T> {

    protected final BasicUIProperties properties;
    protected final BasicUIProperties.SlotOverlayGetter<IDrawable> slotOverlayFunction;
    protected final UITexture progressBarTexture;
    protected boolean mAddGregTechLogo = false;

    protected final Map<BooleanSyncValue, GTTooltipDataCache.TooltipData> errorMap = new HashMap<>();

    public MTEBasicMachineBaseGui(T machine, BasicUIProperties properties) {
        super(machine);
        this.properties = properties;

        TieredVariant tieredVariant = machine.getTieredVariant();
        this.slotOverlayFunction = tieredVariant == TieredVariant.STANDARD ? properties.slotOverlaysMUI2
            : (index, isFluid, isOutput, isSpecial) -> properties.slotOverlaysSteamMUI2
                .apply(index, isFluid, isOutput, isSpecial)
                .get(tieredVariant);
        this.progressBarTexture = tieredVariant == TieredVariant.STANDARD ? properties.progressBarMUI2
            : properties.progressBarTextureSteamMUI2.get(tieredVariant);
    }

    public MTEBasicMachineBaseGui<T> useGregTechLogo(boolean addLogo) {
        this.mAddGregTechLogo = addLogo;
        return this;
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return super.build(guiData, syncManager, uiSettings).child(createBatteryWidget(syncManager).leftRel(0, 0, 1));
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue itemSync = new BooleanSyncValue(
            () -> machine.mItemTransfer,
            value -> machine.mItemTransfer = value).allowC2S();
        BooleanSyncValue fluidSync = new BooleanSyncValue(
            () -> machine.mFluidTransfer,
            value -> machine.mFluidTransfer = value).allowC2S();
        syncManager.syncValue("itemAutoOutput", itemSync);
        syncManager.syncValue("fluidAutoOutput", fluidSync);

        syncManager.registerSlotGroup("item_inv", 1 + ((properties.maxItemInputs - 1) / 3));

        syncManager.syncValue("storedEu", new LongSyncValue(machine::getEUVar));
        syncManager.syncValue("maxEu", new LongSyncValue(machine::maxEUStore));
        syncManager.syncValue("eut", new IntSyncValue(() -> machine.mEUt));

        LongSyncValue averageInputSyncer = new LongSyncValue(() -> {
            BaseMetaTileEntity metaTileEntity = ((BaseMetaTileEntity) machine.getBaseMetaTileEntity());
            return metaTileEntity.getAverageElectricInput();
        });
        syncManager.syncValue("averageInput", averageInputSyncer);
        syncManager.syncValue("maxEuInput", new LongSyncValue(machine::maxEUInput));
        syncManager
            .syncValue("maxAllowedInput", new LongSyncValue(() -> machine.maxAmperesIn() * machine.maxEUInput()));

        initErrors(syncManager);
    }

    /**
     * Subclasses can add their own errors to the error map in this method.
     */
    protected void initErrors(PanelSyncManager syncManager) {
        BooleanSyncValue powerfailSyncer = new BooleanSyncValue(machine::isStuttering);
        syncManager.syncValue("powerfail", powerfailSyncer);
        errorMap.put(
            powerfailSyncer,
            machine.mTooltipCache.getData(
                "GT5U.machines.stalled_stuttering.tooltip",
                GTUtility.translate("GT5U.machines.powersource.power")));
    }

    private ParentWidget<?> createBatteryWidget(PanelSyncManager syncManager) {

        return new ParentWidget<>().size(31, 75)
            .child(createEnergyStorageBar(syncManager))
            .child(createEnergyConsumptionBar(syncManager))
            .child(createEnergyUsageBar(syncManager))
            .child(createEnergyPanel(syncManager));
    }

    private ProgressWidget createEnergyStorageBar(PanelSyncManager syncManager) {
        LongSyncValue euSyncer = syncManager.findSyncHandler("storedEu", LongSyncValue.class);
        LongSyncValue maxEuSyncer = syncManager.findSyncHandler("maxEu", LongSyncValue.class);

        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> euSyncer.getValue() / (double) Math.max(1, maxEuSyncer.getValue()));

        return new ProgressWidget()
            .texture(GTGuiTextures.PROGRESSBAR_ENERGY_EMPTY, GTGuiTextures.PROGRESSBAR_ENERGY_FULL, 45)
            .size(14, 45)
            .top(15)
            .left(6)
            .direction(ProgressWidget.Direction.UP)
            .value(percentageSyncer)
            .tooltipDynamic(
                t -> t.addLine(String.format("Stored EU: %d/%d", euSyncer.getValue(), maxEuSyncer.getValue())));
    }

    private ProgressWidget createEnergyConsumptionBar(PanelSyncManager syncManager) {
        IntSyncValue eutSyncer = syncManager.findSyncHandler("eut", IntSyncValue.class);
        LongSyncValue maxAllowedInputSyncer = syncManager.findSyncHandler("maxAllowedInput", LongSyncValue.class);

        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> eutSyncer.getValue() / (double) maxAllowedInputSyncer.getValue());

        return new ProgressWidget().texture(GTGuiTextures.TRANSPARENT, GTGuiTextures.PROGRESSBAR_ENERGY_CONSUMPTION, 45)
            .size(5, 45)
            .top(15)
            .left(11)
            .direction(ProgressWidget.Direction.UP)
            .value(percentageSyncer);
    }

    private ProgressWidget createEnergyUsageBar(PanelSyncManager syncManager) {
        LongSyncValue averageInputSyncer = syncManager.findSyncHandler("averageInput", LongSyncValue.class);
        LongSyncValue maxAllowedInputSyncer = syncManager.findSyncHandler("maxAllowedInput", LongSyncValue.class);

        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> averageInputSyncer.getValue() / (double) maxAllowedInputSyncer.getValue());

        return new ProgressWidget().texture(GTGuiTextures.TRANSPARENT, GTGuiTextures.PROGRESSBAR_ENERGY_USAGE, 45)
            .size(5, 45)
            .top(15)
            .left(16)
            .direction(ProgressWidget.Direction.UP)
            .value(percentageSyncer);
    }

    private IWidget createEnergyPanel(PanelSyncManager syncManager) {
        LongSyncValue euSyncer = syncManager.findSyncHandler("storedEu", LongSyncValue.class);
        LongSyncValue maxEuSyncer = syncManager.findSyncHandler("maxEu", LongSyncValue.class);
        IntSyncValue eutSyncer = syncManager.findSyncHandler("eut", IntSyncValue.class);
        LongSyncValue averageInputSyncer = syncManager.findSyncHandler("averageInput", LongSyncValue.class);
        LongSyncValue maxEuInputSyncer = syncManager.findSyncHandler("maxEuInput", LongSyncValue.class);
        LongSyncValue maxAllowedInputSyncer = syncManager.findSyncHandler("maxAllowedInput", LongSyncValue.class);

        return GTGuiTextures.ENERGY_GAUGE_PANEL.asWidget()
            .sizeRel(1)
            .tooltipDynamic(
                t -> t.addLine(String.format("Stored EU: %d/%d", euSyncer.getValue(), maxEuSyncer.getValue()))
                    .addLine(
                        String.format(
                            "Recipe EU consumption: %d/%d eu/t",
                            eutSyncer.getValue(),
                            maxEuInputSyncer.getValue()))
                    .addLine(
                        String.format(
                            "Average EU usage: %d/%d eu/t",
                            averageInputSyncer.getValue(),
                            maxAllowedInputSyncer.getValue())))
            .tooltipAutoUpdate(true);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createItemRecipeArea(panel, syncManager));
    }

    protected Flow createItemRecipeArea(ModularPanel panel, PanelSyncManager syncManager) {
        return Flow.row()
            .coverChildren()
            .horizontalCenter()
            .childPadding((2 * SLOT_SIZE - properties.progressBarWidthMUI2) / 2)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .child(createItemInputSlots(panel, syncManager))
            .child(createProgressBar(panel, syncManager))
            .child(createItemOutputSlots(panel, syncManager));
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = super.createBottomLeftCornerFlow(panel, syncManager);
        if (machine.isSteampowered()) return cornerFlow;

        return cornerFlow
            .child(
                createAutoOutputButton(
                    properties.maxFluidOutputs > 0,
                    syncManager,
                    "fluidAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID,
                    BaseTileEntity.FLUID_TRANSFER_TOOLTIP,
                    "GT5U.gui.button.forbidden.reason.fluid"))
            .child(
                createAutoOutputButton(
                    properties.maxItemOutputs > 0,
                    syncManager,
                    "itemAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM,
                    BaseTileEntity.ITEM_TRANSFER_TOOLTIP,
                    "GT5U.gui.button.forbidden.reason.item"))

            .childIf(properties.maxFluidInputs > 0, () -> createFluidInputSlot().marginLeft(SLOT_SIZE / 2));
    }

    private Widget<?> createAutoOutputButton(boolean isEnabled, PanelSyncManager syncManager, String syncKey,
        UITexture overlay, String tooltipKey, String disabledTooltipKey) {
        // needed to make the IPanelBuilder lambda and ToggleButton override work
        ToggleButton[] button = new ToggleButton[1];

        IPanelHandler autoOutputPanel = syncManager
            .syncedPanel("sideSelection_" + syncKey, true, (_, _) -> openSideSelector(button, syncKey));

        button[0] = new ToggleButton() {

            @Override
            public @NotNull Result onMousePressed(int mouseButton) {
                if (!isEnabled) return Result.IGNORE;
                if (Interactable.hasShiftDown()) {
                    autoOutputPanel.openPanel();
                    return Result.SUCCESS;
                }
                return super.onMousePressed(mouseButton);
            }
        }.value(syncManager.findSyncHandler(syncKey, BooleanSyncValue.class))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .overlay(overlay);

        if (isEnabled) button[0].addTooltipLine(GTUtility.translate(tooltipKey));
        if (!isEnabled) button[0].tooltip(
            t -> t.addLine(GTUtility.translate(BUTTON_FORBIDDEN_TOOLTIP))
                .addLine(
                    GTUtility.getColoredSecondaryTooltip(
                        GTUtility
                            .translate("GT5U.gui.button.forbidden.reason", GTUtility.translate(disabledTooltipKey)))))
            .widgetTheme(GTWidgetThemes.TOGGLE_BUTTON_DISABLED);

        return button[0];
    }

    private ModularPanel openSideSelector(ToggleButton[] parent, String syncKey) {

        ModularPanel panel = new ModularPanel("sideSelector_" + syncKey) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.relative(parent[0])
            .background(IDrawable.EMPTY)
            .coverChildren();
        List<IWidget> buttons = new ArrayList<>();

        // Top
        buttons.add(null);
        buttons
            .add(createSideSelectionButton(panel, ForgeDirection.UP, GTGuiTextures.OVERLAY_BUTTON_SIDE_SELECTION_UP));
        buttons.add(null);

        // Middle
        buttons.add(
            createSideSelectionButton(
                panel,
                this.machine.mMainFacing.getRotation(ForgeDirection.UP),
                GTGuiTextures.OVERLAY_BUTTON_SIDE_SELECTION_LEFT));
        buttons.add(null);
        buttons.add(
            createSideSelectionButton(
                panel,
                this.machine.mMainFacing.getRotation(ForgeDirection.DOWN),
                GTGuiTextures.OVERLAY_BUTTON_SIDE_SELECTION_RIGHT));

        // Bottom
        buttons.add(null);
        buttons.add(
            createSideSelectionButton(panel, ForgeDirection.DOWN, GTGuiTextures.OVERLAY_BUTTON_SIDE_SELECTION_DOWN));
        buttons.add(
            createSideSelectionButton(
                panel,
                this.machine.mMainFacing.getOpposite(),
                GTGuiTextures.OVERLAY_BUTTON_SIDE_SELECTION_BACK));

        return panel.child(
            new Grid().coverChildren()
                .gridOf(3, buttons));
    }

    private IWidget createSideSelectionButton(ModularPanel panel, ForgeDirection direction, IDrawable texture) {
        InteractionSyncHandler sideSelectionHandler = new InteractionSyncHandler().setOnMousePressed(mouseButton -> {
            // This is copied 1:1 from MetaTileEntity code because i didn't want to hook into onWrenchRightClick
            final IGregTechTileEntity meta = this.machine.getBaseMetaTileEntity();
            if (!meta.isValidFacing(direction)) {
                return;
            }
            meta.setFrontFacing(direction);

            for (final ForgeDirection s : ForgeDirection.VALID_DIRECTIONS) {
                final IGregTechTileEntity iGregTechTileEntity = meta.getIGregTechTileEntityAtSide(s);
                if (iGregTechTileEntity != null) {
                    if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeLaser pipe) pipe.updateNetwork(true);
                    if (iGregTechTileEntity.getMetaTileEntity() instanceof MTEPipeData pipe) pipe.updateNetwork(true);
                }
            }
            panel.closeIfOpen();
        });
        return new ButtonWidget<>().syncHandler(sideSelectionHandler)
            .overlay(texture);
    }

    @Override
    protected Flow createBottomRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomRightCornerFlow(panel, syncManager)
            .childIf(this.doesAddSpecialSlot(), this::createSpecialSlot)
            .childIf(properties.maxFluidOutputs > 0, this::createFluidOutputSlot);
    }

    protected boolean supportsChargerSlot() {
        return true;
    }

    @Override
    protected ParentWidget<?> createBottomSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomSection(panel, syncManager).child(
            Flow.column()
                .coverChildren()
                .decoration()
                .bottomRel(0)
                .horizontalCenter()
                .child(createErrorWidget(panel, syncManager))
                .childIf(supportsChargerSlot(), this::createChargerSlot));
    }

    /**
     * Typically, this is used for the 'special slot' on singleblocks
     */
    protected ItemSlot createSpecialSlot() {
        String[] tooltipKeys = new String[2];
        if (properties.useSpecialSlot) {
            tooltipKeys[0] = "GT5U.machines.special_slot.tooltip";
            tooltipKeys[1] = "GT5U.machines.special_slot.tooltip.1";
        } else {
            tooltipKeys[0] = "GT5U.machines.unused_slot.tooltip";
            tooltipKeys[1] = "GT5U.machines.unused_slot.tooltip.1";
        }
        return new ItemSlot().marginRight(SLOT_SIZE / 2)
            .slot(new MachineModularSlot(machine.inventoryHandler, machine.getSpecialSlotIndex(), baseMetaTileEntity))
            .backgroundOverlay(
                properties.useSpecialSlot ? slotOverlayFunction.apply(0, false, false, true) : IDrawable.NONE)
            .tooltip(
                t -> t.addLine(GTUtility.translate(tooltipKeys[0]))
                    .addLine(GTUtility.translate(tooltipKeys[1])))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected ProgressWidget createProgressBar(ModularPanel panel, PanelSyncManager syncManager) {
        return new GTProgressWidget()
            .neiTransferRect(properties.neiTransferRectId, new Object[] { machine }, createTooltipForProgressBar())
            .value(new DoubleSyncValue(() -> (double) machine.mProgresstime / machine.mMaxProgresstime))
            .texture(progressBarTexture, properties.progressBarWidthMUI2)
            .size(properties.progressBarWidthMUI2, properties.progressBarHeightMUI2 / 2)
            .direction(properties.progressBarDirectionMUI2)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private String createTooltipForProgressBar() {
        final byte machineTier = machine.mTier;
        String tierName = GTUtility.getColoredTierNameFromTier(machineTier);
        return GTUtility.translate("GT5U.machines.nei_transfer.voltage.tooltip", tierName);
    }

    protected Widget<?> createErrorWidget(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue hasErrorSyncer = new BooleanSyncValue(
            () -> errorMap.keySet()
                .stream()
                .anyMatch(BooleanSyncValue::getBoolValue));
        syncManager.syncValue("hasError", hasErrorSyncer);

        IDrawable.DrawableWidget widget = new IDrawable.DrawableWidget(IDrawable.EMPTY);
        hasErrorSyncer.changeListener(widget::markTooltipDirty);

        return widget.size(SLOT_SIZE)
            .widgetTheme(GTWidgetThemes.PICTURE_ERROR)
            .setEnabledIf(_ -> hasErrorSyncer.getBoolValue())
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .tooltipBuilder(t -> {
                if (hasErrorSyncer.getBoolValue()) addTooltipDataToRichTooltip(
                    () -> errorMap.get(
                        errorMap.keySet()
                            .stream()
                            .filter(BooleanSyncValue::getBoolValue)
                            .findFirst()
                            .get())).accept(t);
            });
    }

    protected ParentWidget<?> createItemInputSlots(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().size(3 * SLOT_SIZE)
            .child(
                new Grid().coverChildren()
                    .gridOfElements(
                        mapInSlotsToMatrix(),
                        (_, _, i,
                            key) -> key == 'c'
                                ? new ItemSlot().backgroundOverlay(slotOverlayFunction.apply(i, false, false, false))
                                    .slot(
                                        new MachineModularSlot(
                                            machine.inventoryHandler,
                                            machine.getInputSlot() + i,
                                            baseMetaTileEntity).slotGroup("item_inv"))
                                : null)
                    .verticalCenter()
                    .rightRel(0));
    }

    protected FluidSlot createFluidInputSlot() {
        return new FluidSlot().backgroundOverlay(slotOverlayFunction.apply(0, true, false, false))
            .syncHandler(new FluidSlotSyncHandler(machine.getFluidTank()) {

                @Override
                protected void onValueChanged() {
                    super.onValueChanged();
                    if (this.getSyncManager()
                        .isClient()) return;
                    baseMetaTileEntity.markInventoryBeenModified();
                }
            });
    }

    protected ParentWidget<?> createItemOutputSlots(ModularPanel panel, PanelSyncManager syncManager) {
        return new ParentWidget<>().size(3 * SLOT_SIZE)
            .child(
                new Grid().coverChildren()
                    .gridOfElements(
                        mapOutSlotsToMatrix(),
                        (_, _, i,
                            key) -> key == 'c'
                                ? new ItemSlot().backgroundOverlay(slotOverlayFunction.apply(i, false, true, false))
                                    .slot(
                                        new MachineModularSlot(
                                            machine.inventoryHandler,
                                            machine.getOutputSlot() + i,
                                            baseMetaTileEntity).accessibility(false, true))
                                : null)
                    .verticalCenter()
                    .leftRel(0));
    }

    protected FluidSlot createFluidOutputSlot() {
        return new FluidSlot().backgroundOverlay(slotOverlayFunction.apply(0, true, true, false))
            .syncHandler(new FluidSlotSyncHandler(machine.getFluidOutputTank()) {

                @Override
                protected void onValueChanged() {
                    super.onValueChanged();
                    if (this.getSyncManager()
                        .isClient()) return;
                    baseMetaTileEntity.markInventoryBeenModified();
                }

            }.canFillSlot(false));
    }

    protected List<CharList> mapInSlotsToMatrix() {
        int slots = properties.maxItemInputs;
        String[] matrix = new String[1 + ((slots - 1) / 3)];

        Arrays.fill(matrix, "");
        switch (slots) {
            case 1 -> matrix[0] = "c";
            case 2 -> matrix[0] = "cc";
            case 3 -> matrix[0] = "ccc";
            case 4 -> {
                matrix[0] = "cc";
                matrix[1] = "cc";
            }
            case 5 -> {
                matrix[0] = " cc";
                matrix[1] = "ccc";
            }
            case 6 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
            }
            case 7 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "  c";
            }
            case 8 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = " cc";
            }
            case 9 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "ccc";
            }
        }

        return Arrays.stream(matrix)
            .map(String::toCharArray)
            .map(CharList::of)
            .collect(Collectors.toList());
    }

    protected List<CharList> mapOutSlotsToMatrix() {
        int slots = properties.maxItemOutputs;

        String[] matrix = new String[1 + ((slots - 1) / 3)];

        Arrays.fill(matrix, "");
        switch (slots) {
            case 1 -> matrix[0] = "c";
            case 2 -> matrix[0] = "cc";
            case 3 -> matrix[0] = "ccc";
            case 4 -> {
                matrix[0] = "cc";
                matrix[1] = "cc";
            }
            case 5 -> {
                matrix[0] = "ccc";
                matrix[1] = "cc ";
            }
            case 6 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
            }
            case 7 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "c  ";
            }
            case 8 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "cc ";
            }
            case 9 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "ccc";
            }
        }
        return Arrays.stream(matrix)
            .map(String::toCharArray)
            .map(CharList::of)
            .collect(Collectors.toList());
    }

    @Override
    protected boolean doesAddGregTechLogo() {
        return this.mAddGregTechLogo;
    }
}
