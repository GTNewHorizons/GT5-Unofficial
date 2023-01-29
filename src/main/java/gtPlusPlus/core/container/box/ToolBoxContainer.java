package gtPlusPlus.core.container.box;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import gtPlusPlus.core.item.tool.misc.box.ContainerBoxBase;
import gtPlusPlus.core.item.tool.misc.box.CustomBoxInventory;
import gtPlusPlus.core.item.tool.misc.box.UniversalToolBox;
import gtPlusPlus.core.slots.SlotToolBox;

public class ToolBoxContainer extends ContainerBoxBase {

    public ToolBoxContainer(EntityPlayer par1Player, InventoryPlayer inventoryPlayer,
            CustomBoxInventory CustomBoxInventory) {
        super(par1Player, inventoryPlayer, CustomBoxInventory, SlotToolBox.class, UniversalToolBox.SLOTS);
    }
}
