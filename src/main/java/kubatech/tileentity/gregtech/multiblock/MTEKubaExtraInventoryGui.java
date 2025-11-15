package kubatech.tileentity.gregtech.multiblock;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;

// for Mapiary and EIG
public class MTEKubaExtraInventoryGui<T extends KubaTechGTMultiBlockBase<T>> extends MTEKubaGui<T> {
    public MTEKubaExtraInventoryGui(T multiblock) {
        super(multiblock);
    }

    @Override
    protected Flow createButtonColumn(ModularPanel panel, PanelSyncManager syncManager) {
        return new Column().width(18)
            .leftRel(1, -2, 1)
            .mainAxisAlignment(Alignment.MainAxis.END)
            .reverseLayout(true)
            .child(createExtraInventoryButton())
            .child(createPowerSwitchButton())
            .child(createStructureUpdateButton(syncManager));

    }

    // Todo. Rewrite this to actually Inventory
    protected IWidget createExtraInventoryButton(){
        return new ItemSlot()
            .slot(
                new ModularSlot(multiblock.inventoryHandler, multiblock.getControllerSlotIndex())
                    .slotGroup("item_inv"))
            .marginTop(4);
    }
}
