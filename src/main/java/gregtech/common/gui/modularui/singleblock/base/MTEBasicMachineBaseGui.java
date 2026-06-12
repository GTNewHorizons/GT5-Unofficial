package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.InteractionSyncHandler;
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

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.util.MachineModularSlot;
import gregtech.common.modularui2.widget.GTProgressWidget;
import it.unimi.dsi.fastutil.chars.CharList;
import tectech.thing.metaTileEntity.pipe.MTEPipeData;
import tectech.thing.metaTileEntity.pipe.MTEPipeLaser;

public class MTEBasicMachineBaseGui<T extends MTEBasicMachine> extends MTETieredMachineBlockBaseGui<T> {

    protected BasicUIProperties properties;
    protected BasicUIProperties.SlotOverlayGetter<IDrawable> slotOverlayFunction;
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
        ToggleButton button = null;
        Reference<ToggleButton> buttonRef = new WeakReference<>(button);

        IPanelHandler autoOutputPanel = syncManager
            .syncedPanel("sideSelection_" + syncKey, true, (_, _) -> openSideSelector(buttonRef, syncKey));

        button = new ToggleButton() {

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

        if (isEnabled) button.addTooltipLine(GTUtility.translate(tooltipKey));
        if (!isEnabled) button.tooltip(
            t -> t.addLine(GTUtility.translate(BUTTON_FORBIDDEN_TOOLTIP))
                .addLine(
                    GTUtility.getColoredSecondaryTooltip(
                        GTUtility
                            .translate("GT5U.gui.button.forbidden.reason", GTUtility.translate(disabledTooltipKey)))))
            .widgetTheme(GTWidgetThemes.TOGGLE_BUTTON_DISABLED);

        return button;
    }

    private ModularPanel openSideSelector(Reference<? extends IWidget> parentRef, String syncKey) {

        ModularPanel panel = new ModularPanel("sideSelector_" + syncKey) {

            @Override
            public boolean isDraggable() {
                return false;
            }
        }.relative(parentRef.get())
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
        return super.createBottomSection(panel, syncManager).child(
            Flow.column()
                .coverChildren()
                .decoration()
                .bottomRel(0)
                .horizontalCenter()
                .child(createErrorIcon(panel, syncManager))
                .child(createChargerSlot()));
    }

    // typically, this is used for the 'special slot' on singleblocks
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

    protected Widget<?> createErrorIcon(ModularPanel panel, PanelSyncManager syncManager) {
        BooleanSyncValue stutteringSyncer = new BooleanSyncValue(machine::isStuttering);
        syncManager.syncValue("stuttering", stutteringSyncer);

        return new DynamicDrawable(
            () -> stutteringSyncer.getBoolValue() ? GTGuiTextures.OVERLAY_POWER_LOSS : IDrawable.EMPTY).asWidget()
                .size(18)
                .tooltipAutoUpdate(true)
                .tooltipShowUpTimer(TOOLTIP_DELAY)
                .tooltipBuilder(t -> {
                    if (stutteringSyncer.getBoolValue()) addToRichTooltip(
                        () -> machine.mTooltipCache.getData(
                            "GT5U.machines.stalled_stuttering.tooltip",
                            GTUtility.translate("GT5U.machines.powersource.power"))).accept(t);
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
