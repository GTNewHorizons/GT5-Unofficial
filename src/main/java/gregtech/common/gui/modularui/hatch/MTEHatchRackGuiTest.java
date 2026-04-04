package gregtech.common.gui.modularui.hatch;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import tectech.thing.metaTileEntity.hatch.MTEHatchRack;

public class MTEHatchRackGuiTest extends MTEHatchBaseGui<MTEHatchRack> {

    public MTEHatchRackGuiTest(MTEHatchRack hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .full();

        // input slots
        // TODO fix sizing
        mainRow
            .child(
                new Grid()
                    .mapTo(
                        2,
                        4,
                        index -> new ItemSlot()
                            .slot(new ModularSlot(hatch.inventoryHandler, index).slotGroup("item_inv")))
                    .minElementMargin(2)
                    .align(Alignment.CENTER)
                    .coverChildren());

        var a = new Grid().mapTo(2, 4, index -> new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, index)))
            .minElementMargin(2)
            .align(Alignment.CENTER)
            .coverChildren();

        return super.createContentSection(panel, syncManager).child(mainRow);
    }
}
