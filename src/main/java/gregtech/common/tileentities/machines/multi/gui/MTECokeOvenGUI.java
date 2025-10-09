package gregtech.common.tileentities.machines.multi.gui;

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

import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.modularui2.widget.GTProgressWidget;
import gregtech.common.tileentities.machines.multi.MTECokeOven;

public class MTECokeOvenGUI extends MTEMultiBlockBaseGui {

    public MTECokeOvenGUI(MTECokeOven cokeOven) {
        super(cokeOven);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);
        syncManager.registerSlotGroup("item_inv", 0);

        final FluidStackTank fluidTank = new FluidStackTank(base::getFluid, base::setFluid, base::getCapacity);

        final ItemSlot inputSlot = new ItemSlot().slot(new ModularSlot(base.inventoryHandler, 0).slotGroup("item_inv"))
            .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE);

        final ItemSlot outputSlot = new ItemSlot()
            .slot(new ModularSlot(base.inventoryHandler, 1).accessibility(false, true))
            .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL);

        final FluidSlot fluidSlot = new FluidSlot().syncHandler(new FluidSlotSyncHandler(fluidTank).canFillSlot(false))
            .alwaysShowFull(false);

        final ProgressWidget progressArrow = new GTProgressWidget().neiTransferRect(base.getRecipeMap())
            .value(new DoubleSyncValue(() -> (double) base.mProgresstime / base.mMaxProgresstime))
            .texture(GTGuiTextures.PROGRESSBAR_ARROW_BBF, 20)
            .size(20, 18);

        return GTGuis.mteTemplatePanelBuilder(base, guiData, syncManager, uiSettings)
            .build()
            .child(createMufflerButton(0, -15))
            .child(
                new Row().alignX(Alignment.CENTER)
                    .alignY(0.25f)
                    .size(72, 18)
                    .child(inputSlot.marginRight(8))
                    .child(progressArrow.marginRight(8))
                    .child(outputSlot.marginRight(8))
                    .child(fluidSlot));
    }
}
