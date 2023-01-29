package gtPlusPlus.core.container.box;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import gtPlusPlus.core.item.tool.misc.box.ContainerBoxBase;
import gtPlusPlus.core.item.tool.misc.box.CustomBoxInventory;
import gtPlusPlus.core.slots.SlotMagicToolBag;

public class MagicBagContainer extends ContainerBoxBase {

    public MagicBagContainer(EntityPlayer par1Player, InventoryPlayer inventoryPlayer,
            CustomBoxInventory CustomBoxInventory) {
        super(
                par1Player,
                inventoryPlayer,
                CustomBoxInventory,
                SlotMagicToolBag.class,
                gtPlusPlus.core.item.tool.misc.box.MagicToolBag.SLOTS);
    }
}
