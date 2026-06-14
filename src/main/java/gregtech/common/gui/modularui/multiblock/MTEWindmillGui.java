package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import bartworks.common.tileentities.multis.MTEWindmill;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.structure.error.StructureError;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.modularui2.factory.GTBaseGuiBuilder;
import gregtech.common.modularui2.widget.GTProgressWidget;

public class MTEWindmillGui extends MTEMultiBlockBaseGui<MTEWindmill> {

    private static final int MACHINE_ROW_HEIGHT = 76;

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

        final ItemSlot inputSlotIncomplete = new ItemSlot()
            .slot(
                new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex()).singletonSlotGroup())
            .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_CRUSHED_ORE);

        GenericListSyncHandler<StructureError> error = syncManager
            .findSyncHandler("structureErrors", GenericListSyncHandler.class);

        return new GTBaseGuiBuilder(multiblock, guiData, syncManager, uiSettings).moveGregtechLogoPos(8, 63)
            .build()
            .child(
                createMachineRowIncomplete(syncManager, inputSlotIncomplete).setEnabledIf(
                    _ -> !error.getValue()
                        .isEmpty())
                    .horizontalCenter())
            .child(
                createMachineRow(progressGrinder, inputSlot).setEnabledIf(
                    _ -> error.getValue()
                        .isEmpty())
                    .horizontalCenter())
            .child(
                createStructureUpdateButton(syncManager).resizer()
                    .pos(151, 63)
                    .getWidget())
            .child(
                createPowerSwitchButton().resizer()
                    .pos(131, 63)
                    .getWidget());
    }

    protected Flow createMachineRow(ProgressWidget progressGrinder, ItemSlot inputSlot) {
        return Flow.row()
            .horizontalCenter()
            .size(72, MACHINE_ROW_HEIGHT)
            .child(progressGrinder.center())
            .child(inputSlot.center());
    }

    protected Flow createMachineRowIncomplete(PanelSyncManager syncManager, ItemSlot inputSlot) {
        return Flow.col()
            .fullWidth()
            .height(MACHINE_ROW_HEIGHT)
            .padding(6, 6, 10, 0)
            .child((createStructureErrorWidget(syncManager)))
            .child(inputSlot.pos(30, 63));
    }
}
