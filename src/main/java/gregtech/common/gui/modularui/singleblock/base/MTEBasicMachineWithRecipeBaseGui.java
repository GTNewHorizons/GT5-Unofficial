package gregtech.common.gui.modularui.singleblock.base;

import static gregtech.api.metatileentity.BaseTileEntity.SPECIAL_SLOT_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.UNUSED_SLOT_TOOLTIP;

import com.cleanroommc.modularui.drawable.UITexture;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.util.GTUtility;

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
        contentSection.child(chargerSlot().align(Alignment.BottomCenter));

        return contentSection;
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        Flow cornerFlow = super.createLeftCornerFlow(panel, syncManager);
        if (!machine.isSteampowered()) {
            cornerFlow.child(
                createAutoOutputButton(
                    "itemAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM,
                    BaseTileEntity.ITEM_TRANSFER_TOOLTIP));
            cornerFlow.child(
                createAutoOutputButton(
                    "fluidAutoOutput",
                    GTGuiTextures.OVERLAY_BUTTON_AUTOOUTPUT_FLUID,
                    BaseTileEntity.FLUID_TRANSFER_TOOLTIP));
        }
        return cornerFlow;
    }

    protected ToggleButton createAutoOutputButton(String syncKey, UITexture overlay, String tooltipKey) {
        return new ToggleButton().size(18)
            .syncHandler(syncKey)
            .overlay(overlay)
            .tooltip(t -> t.addLine(GTUtility.translate(tooltipKey)));
    }

    @Override
    protected Widget<? extends Widget<?>> createSpecialSlot() {
        // todo: fix in mui2 side for tooltip overriding
        String key = properties.useSpecialSlot ? SPECIAL_SLOT_TOOLTIP : UNUSED_SLOT_TOOLTIP;
        return new ItemSlot().marginTop(4)
            .slot(new ModularSlot(machine.inventoryHandler, machine.getSpecialSlotIndex()).slotGroup("item_inv"))
            .tooltip(t -> t.addLine(GTUtility.translate(key)));
    }

    protected ItemSlot chargerSlot() {
        // todo: things from mtebasicmachine that no one cares about
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.rechargerSlotStartIndex()))
            .overlay(GTGuiTextures.OVERLAY_SLOT_CHARGER);
    }
}
