package gtPlusPlus.core.container.box;

import gtPlusPlus.core.item.tool.misc.box.ContainerBoxBase;
import gtPlusPlus.core.item.tool.misc.box.CustomBoxInventory;
import gtPlusPlus.core.slots.SlotLunchBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class LunchBoxContainer extends ContainerBoxBase {
	public LunchBoxContainer(EntityPlayer par1Player, InventoryPlayer inventoryPlayer, CustomBoxInventory CustomBoxInventory) {
		super(par1Player, inventoryPlayer, CustomBoxInventory, SlotLunchBox.class, gtPlusPlus.core.item.tool.misc.box.AutoLunchBox.SLOTS);
	}		
}
