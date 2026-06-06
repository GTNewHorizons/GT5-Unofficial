package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
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
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizons.modularui.api.widget.Interactable;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.widget.GTProgressWidget;
import it.unimi.dsi.fastutil.chars.CharList;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;

public class MTEBasicMachineBaseGui<T extends MTEBasicMachine> extends MTETieredMachineBlockBaseGui<T> {

    BasicUIProperties properties;
    BasicUIProperties.SlotOverlayGetter<IDrawable> slotOverlayFunction;
    protected boolean mAddGregTechLogo = false;

    public MTEBasicMachineBaseGui(T machine, BasicUIProperties properties) {
        super(machine);
        this.properties = properties;
        this.slotOverlayFunction = properties.slotOverlaysMUI2;
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

        DoubleSyncValue percentageSyncer = new DoubleSyncValue(
            () -> eutSyncer.getValue() / (double) GTValues.V[machine.mTier]);

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
        return super.createContentSection(panel, syncManager).child(createItemRecipeArea());
    }

    protected Flow createItemRecipeArea() {
        return Flow.row()
            .coverChildren()
            .horizontalCenter()
            .childPadding((2 * SLOT_SIZE - properties.progressBarWidthMUI2) / 2)
            .mainAxisAlignment(Alignment.MainAxis.CENTER)
            .child(createItemInputSlots())
            .child(createProgressBar())
            .child(createItemOutputSlots());
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = super.createBottomLeftCornerFlow(panel, syncManager);
        if (machine.isSteampowered()) return cornerFlow;

        return cornerFlow
            .child(
                createAutoOutputButton(
                    syncManager,
                    "fluidAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID,
                    BaseTileEntity.FLUID_TRANSFER_TOOLTIP))
            .child(
                createAutoOutputButton(
                    syncManager,
                    "itemAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM,
                    BaseTileEntity.ITEM_TRANSFER_TOOLTIP).marginRight(9))

            .childIf(properties.maxFluidInputs > 0, this::createFluidInputSlot);
    }

    private ButtonWidget<?> createAutoOutputButton(PanelSyncManager syncManager, String syncKey, IDrawable overlay,
        String tooltipKey) {
        BooleanSyncValue syncHandler = syncManager.findSyncHandler(syncKey, BooleanSyncValue.class);

        ButtonWidget<?> button = new ButtonWidget<>();
        IPanelHandler autoOutputPanel = syncManager
            .syncedPanel("sideSelection_" + syncKey, true, (panelSyncManager, b) -> openSideSelector(button, syncKey));
        return button.overlay(overlay)
            .background(
                new DynamicDrawable(
                    () -> syncHandler.getValue() ? GTGuiTextures.BUTTON_STANDARD_PRESSED
                        : GTGuiTextures.BUTTON_STANDARD))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .tooltip(
                t -> t.addLine(GTUtility.translate(tooltipKey))
                    .addLine(GTUtility.translate("GT5U.machines.side_selection.tooltip")))
            .onMousePressed(mouseButton -> {
                if (Interactable.hasShiftDown()) {
                    autoOutputPanel.openPanel();
                } else {
                    boolean newVal = !syncHandler.getValue();
                    syncHandler.setValue(newVal);
                }
                return true;
            });
    }

    private ModularPanel openSideSelector(ButtonWidget<?> button, String syncKey) {

        ModularPanel panel = new ModularPanel("sideSelector_" + syncKey) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.relative(button)
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
        // the fluid output slot is positioned under the first item output slot, which is 0.5 slots over in the gui.
    }

    @Override
    protected ParentWidget<?> createBottomSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createBottomSection(panel, syncManager).child(createChargerSlot().horizontalCenter());
    }

    @Override
    protected Widget<? extends Widget<?>> createSpecialSlot() {
        String[] tooltipKeys = new String[2];
        if (properties.useSpecialSlot) {
            tooltipKeys[0] = "GT5U.machines.special_slot.tooltip";
            tooltipKeys[1] = "GT5U.machines.special_slot.tooltip.1";
        } else {
            tooltipKeys[0] = "GT5U.machines.unused_slot.tooltip";
            tooltipKeys[1] = "GT5U.machines.unused_slot.tooltip.1";
        }
        return new ItemSlot().marginRight(9)
            .slot(
                new ModularSlot(machine.inventoryHandler, machine.getSpecialSlotIndex())
                    .changeListener(
                        (newItem, onlyAmountChanged, client, init) -> {
                            if (!client && !init) baseMetaTileEntity.markInventoryBeenModified();
                        })
                    .singletonSlotGroup(1000))
            .backgroundOverlay(
                properties.useSpecialSlot ? slotOverlayFunction.apply(0, false, false, true) : IDrawable.NONE)
            .tooltip(
                t -> t.addLine(GTUtility.translate(tooltipKeys[0]))
                    .addLine(GTUtility.translate(tooltipKeys[1])))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected ProgressWidget createProgressBar() {
        return new GTProgressWidget()
            .neiTransferRect(properties.neiTransferRectId, GTValues.emptyObjectArray, createTooltipForProgressBar())
            .value(new DoubleSyncValue(() -> (double) machine.mProgresstime / machine.mMaxProgresstime))
            .texture(properties.progressBarMUI2, properties.progressBarWidthMUI2)
            .size(properties.progressBarWidthMUI2, properties.progressBarHeightMUI2 / 2)
            .direction(properties.progressBarDirectionMUI2)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private String createTooltipForProgressBar() {
        final byte machineTier = machine.mTier;
        String tierName = GTUtility.getColoredTierNameFromTier(machineTier);
        return GTUtility.translate("GT5U.machines.nei_transfer.voltage.tooltip", tierName);
    }

    protected ParentWidget<?> createItemInputSlots() {
        return new ParentWidget<>().size(3 * SLOT_SIZE)
            .child(
                new Grid().coverChildren()
                    .gridOfElements(
                        mapInSlotsToMatrix(),
                        ($x, $y, i,
                            key) -> key == 'c'
                                ? new ItemSlot().backgroundOverlay(slotOverlayFunction.apply(i, false, false, false))
                                    .slot(
                                        new ModularSlot(machine.inventoryHandler, machine.getInputSlot() + i)
                                            .changeListener(
                                                (newItem, onlyAmountChanged, client, init) -> {
                                                    if (!client && !init)
                                                        baseMetaTileEntity.markInventoryBeenModified();
                                                })
                                            .slotGroup("item_inv"))
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

    protected ParentWidget<?> createItemOutputSlots() {
        return new ParentWidget<>().size(3 * SLOT_SIZE)
            .child(
                new Grid().coverChildren()
                    .gridOfElements(
                        mapOutSlotsToMatrix(),
                        ($x, $y, i,
                            key) -> key == 'c'
                                ? new ItemSlot().backgroundOverlay(slotOverlayFunction.apply(i, false, true, false))
                                    .slot(
                                        new ModularSlot(machine.inventoryHandler, machine.getOutputSlot() + i)
                                            .changeListener(
                                                (newItem, onlyAmountChanged, client, init) -> {
                                                    if (!client && !init)
                                                        baseMetaTileEntity.markInventoryBeenModified();
                                                })
                                            .accessibility(false, true))
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
