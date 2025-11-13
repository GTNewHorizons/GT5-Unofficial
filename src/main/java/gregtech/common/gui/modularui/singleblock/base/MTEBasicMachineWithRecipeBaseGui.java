package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.SPECIAL_SLOT_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.UNUSED_SLOT_TOOLTIP;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
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

import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;
import gregtech.common.modularui2.widget.GTProgressWidget;

public class MTEBasicMachineWithRecipeBaseGui extends MTEBasicMachineBaseGui<MTEBasicMachineWithRecipe> {

    BasicUIProperties properties;

    public MTEBasicMachineWithRecipeBaseGui(MTEBasicMachineWithRecipe machine, BasicUIProperties properties) {
        super(machine);
        this.properties = properties;
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
        ParentWidget<?> contentSection = super.createContentSection(panel, syncManager);
        contentSection.child(createChargerSlot().align(Alignment.BottomCenter));
        contentSection.child(
            createItemRecipeArea().alignX(Alignment.CENTER)
                .alignY(0.3f));
        return contentSection;
    }

    protected Flow createItemRecipeArea() {

        Flow itemRow = Flow.row()
            .coverChildren();
        itemRow.child(createItemInputSlots().marginRight(6))
            .child(createProgressBar().marginRight(6))
            .child(createItemOutputSlots());

        return itemRow;
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = super.createLeftCornerFlow(panel, syncManager);
        if (machine.isSteampowered()) return cornerFlow;

        cornerFlow
            .child(
                createAutoOutputButton(
                    "itemAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM,
                    BaseTileEntity.ITEM_TRANSFER_TOOLTIP))
            .child(
                createAutoOutputButton(
                    "fluidAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID,
                    BaseTileEntity.FLUID_TRANSFER_TOOLTIP).marginRight(18));

        cornerFlow.childIf(properties.maxFluidInputs > 0, createFluidInputSlot());

        return cornerFlow;
    }

    @Override
    protected Flow createRightCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        return super.createRightCornerFlow(panel, syncManager)
            .childIf(properties.maxFluidOutputs > 0, createFluidOutputSlot());
    }

    @Override
    protected IDrawable.DrawableWidget createLogo() {
        return super.createLogo().marginLeft(20);
    }

    protected ToggleButton createAutoOutputButton(String syncKey, UITexture overlay, String tooltipKey) {
        return new ToggleButton().size(18)
            .syncHandler(syncKey)
            .overlay(overlay)
            .tooltip(t -> t.addLine(GTUtility.translate(tooltipKey)));
    }

    @Override
    protected int getBasePanelHeight() {
        return super.getBasePanelHeight() + (18 * (properties.maxItemInputs >= 6 ? 1 : 0));
    }

    @Override
    protected Widget<? extends Widget<?>> createSpecialSlot() {
        // todo: fix in mui2 side for tooltip overriding
        String key = properties.useSpecialSlot ? SPECIAL_SLOT_TOOLTIP : UNUSED_SLOT_TOOLTIP;
        return new ItemSlot().marginTop(4)
            .slot(new ModularSlot(machine.inventoryHandler, machine.getSpecialSlotIndex()).slotGroup("item_inv"))
            .tooltip(t -> t.addLine(GTUtility.translate(key)));
    }

    protected ItemSlot createChargerSlot() {
        // todo: things from mtebasicmachine that no one cares about
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.rechargerSlotStartIndex()))
            .overlay(GTGuiTextures.OVERLAY_SLOT_CHARGER);
    }

    protected SlotGroupWidget createItemInputSlots() {
        int maxInputSlots = this.properties.maxItemInputs;
        String[] matrix = mapSlotsToMatrix(maxInputSlots);// new String[1 + ((maxInputSlots-1)/3)];
        // for(int i = 0; i < maxInputSlots; i++)
        // {
        // int row = i/3;
        // matrix[row] = matrix[row] == null ? "c" : matrix[row]+"c";
        // }
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key('a', i -> new IDrawable.DrawableWidget(IDrawable.EMPTY).size(18))
            .key('c', i -> new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.getInputSlot() + i)))
            .build();
    }

    protected static String[] mapSlotsToMatrix(int slots) {
        String[] matrix = new String[] { "aaa", "aaa", "aaa" };
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

    protected FluidSlot createFluidInputSlot() {
        return new FluidSlot().syncHandler(machine.getFluidTank());
    }

    protected ProgressWidget createProgressBar() {
        return new GTProgressWidget().neiTransferRect(machine.getRecipeMap())
            .value(new DoubleSyncValue(() -> (double) machine.mProgresstime / machine.mMaxProgresstime))
            .texture(GTGuiTextures.PROGRESSBAR_ARROW_STANDARD, 20);
    }

    protected SlotGroupWidget createItemOutputSlots() {
        int maxOutputSlots = this.properties.maxItemOutputs;
        String[] matrix = mapSlotsToMatrix(maxOutputSlots);// new String[1 + ((maxInputSlots-1)/3)];
        // for(int i = 0; i < maxInputSlots; i++)
        // {
        // int row = i/3;
        // matrix[row] = matrix[row] == null ? "c" : matrix[row]+"c";
        // }
        return SlotGroupWidget.builder()
            .matrix(matrix)
            .key('a', i -> new IDrawable.DrawableWidget(IDrawable.EMPTY).size(18))
            .key('c', i -> new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.getOutputSlot() + i)))
            .build();
    }

    protected FluidSlot createFluidOutputSlot() {
        return new FluidSlot().syncHandler(new FluidSlotSyncHandler(machine.getFluidOutputTank()).canFillSlot(false));
    }
}
