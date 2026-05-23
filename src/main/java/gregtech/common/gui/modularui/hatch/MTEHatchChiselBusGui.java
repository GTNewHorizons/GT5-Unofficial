package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.builder.ItemSlotGridBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchChiselBus;

public class MTEHatchChiselBusGui extends MTEHatchBaseGui<MTEHatchChiselBus> {

    public MTEHatchChiselBusGui(MTEHatchChiselBus hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        int totalSlots = machine.inventoryHandler.getSlots();
        int normalSlots = totalSlots - 1;

        // Register slot groups
        syncManager.registerSlotGroup("inputs", normalSlots);

        Flow content = Flow.row()
            .marginLeft(SLOT_SIZE)
            .childPadding(SLOT_SIZE / 2)
            .coverChildren();

        // target slot
        content.child(new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, normalSlots).singletonSlotGroup()));

        // has to be list widget so it can scroll
        ListWidget<IWidget, ?> inputList = new ListWidget<>().size(4 * SLOT_SIZE)
            .child(
                new ItemSlotGridBuilder(machine.inventoryHandler, syncManager).size(4, normalSlots / 4)
                    .slotGroupKey("inputs")
                    .build());

        content.child(inputList);

        return super.createContentSection(panel, syncManager).child(content);
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
