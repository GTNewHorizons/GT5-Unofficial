package gregtech.api.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GT_StructureUtilityMuTE.UpgradeCasings;

public class ControllerItemLogic {

    private final Map<UUID, ItemInventoryLogic> inventories = new HashMap<>();
    private final Set<Pair<UUID, ItemInventoryLogic>> unallocatedInventories = new HashSet<>();

    public UUID addInventory(@NotNull ItemInventoryLogic inventory) {
        Pair<UUID, ItemInventoryLogic>  found = checkIfInventoryExistsAsUnallocated(inventory);
        if(inventory.isUpgradeInventory() && found != null) {
            unallocatedInventories.remove(found);
            inventories.put(found.getLeft(), found.getRight());
            return found.getLeft();
        }
        UUID generatedUUID = UUID.randomUUID();
        inventories.put(generatedUUID, inventory);
        return generatedUUID;
    }

    private Pair<UUID, ItemInventoryLogic>  checkIfInventoryExistsAsUnallocated(@NotNull ItemInventoryLogic inventory) {
        return unallocatedInventories.stream().filter(unallocated -> unallocated.getRight().getTier() == inventory.getTier()).findFirst().get();
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
        if (id == null) return getAllInventoryLogics();
        return inventories.get(id);
    }
}
