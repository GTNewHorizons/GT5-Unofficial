package gregtech.api.logic.interfaces;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.InventoryType;
import gregtech.api.logic.ItemInventoryLogic;

public interface ItemInventoryLogicHost {

    /**
     * To be used for single blocks or when directly interacting with the controller
     * 
     * @param side The side from where items are being inputted or extracted from
     * @param type The type of inventory being accessed. For inputting its Input, For outputting its Output.
     * @return The Item Logic responsible for said type.
     */
    @Nullable
    ItemInventoryLogic getItemLogic(@Nonnull ForgeDirection side, @Nonnull InventoryType type);

    /**
     * Only to be used by MultiBlockPart for accessing the Controller Inventory
     * 
     * @param type Type of inventory, is it Input or Output
     * @param id   ID of the locked inventory. A null id is all inventories of said controller of said type
     * @return The Item Logic responsible for everything that should be done with said inventory
     */
    @Nullable
    default ItemInventoryLogic getItemLogic(@Nonnull InventoryType type, @Nullable UUID id) {
        return getItemLogic(ForgeDirection.UNKNOWN, type);
    }
}
