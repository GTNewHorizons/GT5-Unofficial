package gregtech.api.logic.interfaces;

import java.util.UUID;

import gregtech.api.logic.ItemInventoryLogic;

public interface ItemInventoryLogicHost {

    ItemInventoryLogic getItemLogic(InventoryType type);

    default ItemInventoryLogic getItemLogic(InventoryType type, UUID id) {
        return getItemLogic(type);
    }

    public enum InventoryType {
        Input,
        Output;
    }
}
