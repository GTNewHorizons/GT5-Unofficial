package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.Arrays;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.enums.GTValues;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.widget.GTProgressWidget;

public class MTEBasicMachineWithRecipeBaseGui extends MTEBasicMachineBaseGui<MTEBasicMachineWithRecipe> {

    BasicUIProperties properties;
    BasicUIProperties.SlotOverlayGetter<IDrawable> slotOverlayFunction;

    public MTEBasicMachineWithRecipeBaseGui(MTEBasicMachineWithRecipe machine, BasicUIProperties properties) {
        super(machine);
        this.properties = properties;
        this.slotOverlayFunction = properties.slotOverlaysMUI2;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        BooleanSyncValue itemSync = new BooleanSyncValue(
            () -> machine.mItemTransfer,
            value -> machine.mItemTransfer = value);
        BooleanSyncValue fluidSync = new BooleanSyncValue(
            () -> machine.mFluidTransfer,
            value -> machine.mFluidTransfer = value);
        syncManager.syncValue("itemAutoOutput", itemSync);
        syncManager.syncValue("fluidAutoOutput", fluidSync);

    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createContentSection(panel, syncManager).child(createChargerSlot().align(Alignment.BottomCenter))
            .child(
                createItemRecipeArea().alignX(Alignment.CENTER)
                    .alignY(0.2f));
    }

    protected Flow createItemRecipeArea() {

        return Flow.row()
            .width(18 * 8 + properties.progressBarWidthMUI2)
            .paddingLeft(10)
            .coverChildrenHeight()
            .child(
                new ParentWidget<>().size(18 * 3)
                    .marginRight(9)
                    .child(createItemInputSlots().align(Alignment.CenterRight)))
            .child(
                createProgressBar().tooltipShowUpTimer(TOOLTIP_DELAY)
                    .marginRight(7))
            .child(
                new ParentWidget<>().size(18 * 3)
                    .child(createItemOutputSlots().align(Alignment.CenterLeft)));
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = super.createLeftCornerFlow(panel, syncManager);
        if (machine.isSteampowered()) return cornerFlow;

        return cornerFlow
            .child(
                createAutoOutputButton(
                    "fluidAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID,
                    BaseTileEntity.FLUID_TRANSFER_TOOLTIP))
            .child(
                createAutoOutputButton(
                    "itemAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM,
                    BaseTileEntity.ITEM_TRANSFER_TOOLTIP).marginRight(9))

            .childIf(properties.maxFluidInputs > 0, this::createFluidInputSlot);
    }

    @Override
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightCornerFlow(panel, syncManager)
            .childIf(this.doesAddSpecialSlot(), this::createSpecialSlot)
            .childIf(properties.maxFluidOutputs > 0, this::createFluidOutputSlot);
        // the fluid output slot is positioned under the first item output slot, which is 1.5 slots over in the gui.
    }

    protected ToggleButton createAutoOutputButton(String syncKey, UITexture overlay, String tooltipKey) {
        return new ToggleButton().size(18)
            .syncHandler(syncKey)
            .overlay(overlay)
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .tooltip(t -> t.addLine(GTUtility.translate(tooltipKey)));
    }

    private boolean shouldIncreaseGuiHeight() {
        return properties.maxItemInputs > 6 || properties.maxItemOutputs > 6;
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + (9 * (this.shouldIncreaseGuiHeight() ? 1 : 0));
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
            .slot(new ModularSlot(machine.inventoryHandler, machine.getSpecialSlotIndex()).slotGroup("item_inv"))
            .background(
                GTGuiTextures.SLOT_ITEM_STANDARD,
                properties.useSpecialSlot ? slotOverlayFunction.apply(0, false, false, true) : IDrawable.NONE)
            .tooltip(
                t -> t.addLine(GTUtility.translate(tooltipKeys[0]))
                    .addLine(GTUtility.translate(tooltipKeys[1])))
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    protected ItemSlot createChargerSlot() {

        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.rechargerSlotStartIndex()))
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_CHARGER)
            .tooltip(this::createTooltipForChargerSlot)
            .tooltipShowUpTimer(TOOLTIP_DELAY);
    }

    private void createTooltipForChargerSlot(RichTooltip tooltip) {
        final byte machineTier = machine.mTier;
        String tierName = GTUtility.getColoredTierNameFromTier(machineTier);
        tooltip.addLine(GTUtility.translate("GT5U.machines.battery_slot.tooltip"))
            .addLine(GTUtility.translate("GT5U.machines.battery_slot.tooltip.1", tierName))
            .addLine(GTUtility.translate("GT5U.machines.battery_slot.tooltip.2", tierName));
    }

    protected ProgressWidget createProgressBar() {
        return new GTProgressWidget()
            .neiTransferRect(properties.neiTransferRectId, GTValues.emptyObjectArray, createTooltipForProgressBar())
            .value(new DoubleSyncValue(() -> (double) machine.mProgresstime / machine.mMaxProgresstime))
            .texture(properties.progressBarMUI2, properties.progressBarWidthMUI2)
            .size(properties.progressBarWidthMUI2, properties.progressBarHeightMUI2 / 2)
            .direction(properties.progressBarDirectionMUI2);
    }

    private String createTooltipForProgressBar() {
        final byte machineTier = machine.mTier;
        String tierName = GTUtility.getColoredTierNameFromTier(machineTier);
        return GTUtility.translate("GT5U.machines.nei_transfer.voltage.tooltip", tierName);
    }

    protected SlotGroupWidget createItemInputSlots() {
        String[] matrix = mapInSlotsToMatrix();

        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                'a',
                i -> IDrawable.NONE.asWidget()
                    .size(18))
            .key(
                'c',
                i -> new ItemSlot()
                    .background(GTGuiTextures.SLOT_ITEM_STANDARD, slotOverlayFunction.apply(i, false, false, false))
                    .slot(
                        new ModularSlot(machine.inventoryHandler, machine.getInputSlot() + i)
                            .singletonSlotGroup(50 + i)))
            .build();
    }

    protected FluidSlot createFluidInputSlot() {
        return new FluidSlot().overlay(slotOverlayFunction.apply(0, true, false, false))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .syncHandler(machine.getFluidTank());
    }

    protected SlotGroupWidget createItemOutputSlots() {
        String[] matrix = mapOutSlotsToMatrix();
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key(
                'a',
                i -> IDrawable.NONE.asWidget()
                    .size(18))
            .key(
                'c',
                i -> new ItemSlot()
                    .background(GTGuiTextures.SLOT_ITEM_STANDARD, slotOverlayFunction.apply(i, false, true, false))
                    .slot(
                        new ModularSlot(machine.inventoryHandler, machine.getOutputSlot() + i)
                            .accessibility(false, true)))
            .build();
    }

    protected FluidSlot createFluidOutputSlot() {
        return new FluidSlot().overlay(slotOverlayFunction.apply(0, true, true, false))
            .tooltipShowUpTimer(TOOLTIP_DELAY)
            .syncHandler(new FluidSlotSyncHandler(machine.getFluidOutputTank()).canFillSlot(false));
    }

    protected String[] mapInSlotsToMatrix() {
        int slots = properties.maxItemInputs;
        String[] matrix = new String[1 + ((slots - 1) / 3)];

        Arrays.fill(matrix, "aaa");
        switch (slots) {
            case 1 -> {
                matrix[0] = "aac";
            }
            case 2 -> {
                matrix[0] = "acc";
            }
            case 3 -> {
                matrix[0] = "ccc";
            }
            case 4 -> {
                matrix[0] = "acc";
                matrix[1] = "acc";
            }
            case 5 -> {
                matrix[0] = "acc";
                matrix[1] = "ccc";
            }
            case 6 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
            }
            case 7 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "aac";
            }
            case 8 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "acc";
            }
            case 9 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "ccc";
            }
        }
        return matrix;

    }

    protected String[] mapOutSlotsToMatrix() {
        int slots = properties.maxItemOutputs;

        String[] matrix = new String[1 + ((slots - 1) / 3)];

        Arrays.fill(matrix, "aaa");
        switch (slots) {
            case 1 -> {
                matrix[0] = "caa";
            }
            case 2 -> {
                matrix[0] = "cca";
            }
            case 3 -> {
                matrix[0] = "ccc";
            }
            case 4 -> {
                matrix[0] = "cca";
                matrix[1] = "cca";
            }
            case 5 -> {
                matrix[0] = "ccc";
                matrix[1] = "cca";
            }
            case 6 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
            }
            case 7 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "caa";
            }
            case 8 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "cca";
            }
            case 9 -> {
                matrix[0] = "ccc";
                matrix[1] = "ccc";
                matrix[2] = "ccc";
            }
        }
        return matrix;

    }

}
