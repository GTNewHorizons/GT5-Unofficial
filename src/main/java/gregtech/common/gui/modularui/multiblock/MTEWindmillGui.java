package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import bartworks.common.tileentities.multis.MTEWindmill;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.modularui2.widget.GTProgressWidget;

public class MTEWindmillGui extends MTEMultiBlockBaseGui<MTEWindmill> {

    public MTEWindmillGui(MTEWindmill windmill) {
        super(windmill);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);

        final ProgressWidget progressGrinder = new GTProgressWidget()
            .value(new DoubleSyncValue(() -> (double) multiblock.mProgresstime / multiblock.mMaxProgresstime))
            .texture(GTGuiTextures.PROGRESSBAR_WINDMILL_GRINDSTONE, 64)
            .size(64, 64);

        final ItemSlot inputSlot = new ItemSlot()
            .slot(
                new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex()).singletonSlotGroup())
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CRUSHED_ORE);

        return new GTBaseGuiBuilder(multiblock, guiData, syncManager, uiSettings).moveGregtechLogoPos(8, 63)
            .build()
            .child(
                Flow.column()
                    .child(createMachineRow(progressGrinder, inputSlot)));
    }

    protected Flow createMachineRow(ProgressWidget progressGrinder, ItemSlot inputSlot) {
        return Flow.row()
            .horizontalCenter()
            .size(72, 76)
            .child(progressGrinder.center())
            .child(inputSlot.center());
    }
}
