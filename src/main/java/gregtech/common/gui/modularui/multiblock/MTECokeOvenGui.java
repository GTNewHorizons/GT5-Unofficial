package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizons.modularui.common.fluid.FluidStackTank;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.widget.GTProgressWidget;
import gregtech.common.tileentities.machines.multi.MTECokeOven;

public class MTECokeOvenGui extends MTEMultiBlockBaseGui<MTECokeOven> {

    public MTECokeOvenGui(MTECokeOven cokeOven) {
        super(cokeOven);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);
        syncManager.registerSlotGroup("item_inv", 0);

        final FluidStackTank fluidTank = new FluidStackTank(
            multiblock::getFluid,
            multiblock::setFluid,
            multiblock::getCapacity);

        final ItemSlot inputSlot = new ItemSlot()
            .slot(new ModularSlot(multiblock.inventoryHandler, 0).slotGroup("item_inv"))
            .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE);

        final ItemSlot outputSlot = new ItemSlot()
            .slot(new ModularSlot(multiblock.inventoryHandler, 1).accessibility(false, true))
            .background(GTGuiTextures.SLOT_ITEM_PRIMITIVE, GTGuiTextures.BACKGROUND_COKE_OVEN_COAL);

        final FluidSlot fluidSlot = new FluidSlot().syncHandler(new FluidSlotSyncHandler(fluidTank).canFillSlot(false))
            .alwaysShowFull(false)
            .size(18, 54)
            .background(GTGuiTextures.BACKGROUND_COKE_OVEN_FLUID_SLOT)
            .overlay(GTGuiTextures.OVERLAY_COKE_OVEN_FLUID_SLOT_GAUGE);

        final ProgressWidget progressArrow = new GTProgressWidget().neiTransferRect(multiblock.getRecipeMap())
            .value(new DoubleSyncValue(() -> (double) multiblock.mProgresstime / multiblock.mMaxProgresstime))
            .texture(GTGuiTextures.PROGRESSBAR_ARROW_BBF, 20)
            .size(20, 18);

        return GTGuis.mteTemplatePanelBuilder(multiblock, guiData, syncManager, uiSettings)
            .moveGregtechLogoPos(8, 63)
            .build()
            .child(createMuffleButton())
            .child(
                new Row().alignX(Alignment.CENTER)
                    .alignY(0.25f)
                    .size(72, 18)
                    .child(inputSlot.marginRight(8))
                    .child(progressArrow.marginRight(8))
                    .child(outputSlot.marginRight(18))
                    .child(fluidSlot));
    }

    @Override
    protected int getMufflerPosFromRightOutwards() {
        return 15;
    }
}
