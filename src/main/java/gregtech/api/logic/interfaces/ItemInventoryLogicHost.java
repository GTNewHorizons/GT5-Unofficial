package gregtech.api.logic.interfaces;

import java.util.UUID;

import gregtech.api.enums.InventoryType;
import gregtech.api.logic.ItemInventoryLogic;

public interface ItemInventoryLogicHost {

    default ItemInventoryLogic getItemLogic() {
        return getItemLogic(InventoryType.Input);
    }

    ItemInventoryLogic getItemLogic(InventoryType type);

    default ItemInventoryLogic getItemLogic(InventoryType type, UUID id) {
        return getItemLogic(type);
    }
}
