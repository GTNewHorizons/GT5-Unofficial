package gregtech.common.gui.modularui.hatch;

import java.util.Arrays;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchChiselBus;

public class MTEHatchChiselBusGui extends MTEHatchBaseGui<MTEHatchChiselBus> {

    public MTEHatchChiselBusGui(MTEHatchChiselBus hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        int totalSlots = hatch.inventoryHandler.getSlots();
        int normalSlots = totalSlots - 1;

        // Register slot groups
        syncManager.registerSlotGroup("inputs", normalSlots);
        syncManager.registerSlotGroup("target", 1);

        Flow content = Flow.row()
            .coverChildrenHeight()
            .paddingLeft(3)
            .width(168 - 20)
            .align(Alignment.CenterLeft)
            .mainAxisAlignment(Alignment.MainAxis.CENTER);

        // has to be list widget so it can scroll

        ListWidget<IWidget, ?> inputList = new ListWidget<>().size(18 * 4, 18 * 4);
        int rows = (int) Math.ceil(normalSlots / 4.0);
        String[] matrix = new String[rows];
        Arrays.fill(matrix, "xxxx");

        // Target slot
        content.child(new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, normalSlots).slotGroup("target")));
        inputList.child(
            SlotGroupWidget.builder()
                .matrix(matrix)
                .key(
                    'x',
                    index -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index).slotGroup("inputs")))
                .build())
            .marginLeft(3);

        content.child(inputList);

        return super.createContentSection(panel, syncManager).child(content);
    }
}
