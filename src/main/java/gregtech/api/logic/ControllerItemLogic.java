package gregtech.api.logic;

import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class ControllerItemLogic {

    private HashMap<UUID, ItemInventoryLogic> inventories;

    public void addInventory(UUID id, ItemInventoryLogic inventory) {
        inventories.put(id, inventory);
    }

    /**
     * Removes the inventory with said id and gives it back to be processed if needed.
     */
    public ItemInventoryLogic removeInventory(UUID id) {
        return inventories.remove(id);
    }

    public ItemInventoryLogic getAllInventoryLogics() {
        return new ItemInventoryLogic(
            inventories.values()
                .stream()
                .map(inv -> inv.getInventory())
                .collect(Collectors.toList()));
    }

    public ItemInventoryLogic getInventoryLogic(UUID id) {
        return inventories.get(id);
    }
}
