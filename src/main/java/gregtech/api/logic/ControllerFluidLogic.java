package gregtech.api.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

/**
 * Controller logic for Fluid inventories
 * 
 * @author BlueWeabo
 */
public class ControllerFluidLogic {

    private final Map<UUID, FluidInventoryLogic> inventories = new HashMap<>();
    private final Set<Pair<UUID, FluidInventoryLogic>> unallocatedInventories = new HashSet<>();

    public void addInventory(UUID id, @NotNull FluidInventoryLogic inventory) {
        Pair<UUID, FluidInventoryLogic> found = checkIfInventoryExistsAsUnallocated(inventory);
        if (inventory.isUpgradeInventory() && found != null) {
            unallocatedInventories.remove(found);
            inventories.put(id, found.getRight());
            return;
        }
        inventories.put(id, inventory);
    }

    public UUID addInventory(@NotNull FluidInventoryLogic inventory) {
        Pair<UUID, FluidInventoryLogic> found = checkIfInventoryExistsAsUnallocated(inventory);
        if (inventory.isUpgradeInventory() && found != null) {
            unallocatedInventories.remove(found);
            inventories.put(found.getLeft(), found.getRight());
            return found.getLeft();
        }
        UUID generatedUUID = UUID.randomUUID();
        inventories.put(generatedUUID, inventory);
        return generatedUUID;
    }

    private Pair<UUID, FluidInventoryLogic> checkIfInventoryExistsAsUnallocated(
        @NotNull FluidInventoryLogic inventory) {
        return unallocatedInventories.stream()
            .filter(
                unallocated -> unallocated.getRight()
                    .getTier() == inventory.getTier())
            .findFirst()
            .get();
    }

    /**
     * Removes the inventory with said id and gives it back to be processed if needed.
     */
    public FluidInventoryLogic removeInventory(@NotNull UUID id) {
        return inventories.remove(id);
    }

    public FluidInventoryLogic getAllInventoryLogics() {
        return new FluidInventoryLogic(
            inventories.values()
                .stream()
                .map(inv -> inv.getInventory())
                .collect(Collectors.toList()));
    }

    public FluidInventoryLogic getInventoryLogic(UUID id) {
        if (id == null) return getAllInventoryLogics();
        return inventories.get(id);
    }

    public String getInventoryDisplayName(UUID id) {
        if (id == null) return "";
        FluidInventoryLogic logic = inventories.get(id);
        if (logic == null) return "";
        return logic.getDisplayName() == null || logic.getDisplayName()
            .isEmpty() ? id.toString() : logic.getDisplayName();
    }

    public void setInventoryDisplayName(UUID id, String displayName) {
        if (id == null) return;
        FluidInventoryLogic logic = inventories.get(id);
        if (logic == null) return;
        logic.setDisplayName(displayName);
    }

    public NBTTagCompound saveToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList inventoriesNBT = new NBTTagList();
        inventories.forEach((uuid, inventory) -> {
            NBTTagCompound inventoryNBT = new NBTTagCompound();
            inventoryNBT.setTag("inventory", inventory.saveToNBT());
            inventoryNBT.setString("uuid", uuid.toString());
            inventoryNBT.setInteger(
                "invSize",
                inventory.getInventory()
                    .getTanks());
            inventoryNBT.setLong(
                "tankCapacity",
                inventory.getInventory()
                    .getTankCapacity(0));
            inventoriesNBT.appendTag(inventoryNBT);
        });
        nbt.setTag("inventories", inventoriesNBT);
        return nbt;
    }

    public void loadFromNBT(NBTTagCompound nbt) {
        NBTTagList inventoriesNBT = nbt.getTagList("inventories", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < inventoriesNBT.tagCount(); i++) {
            NBTTagCompound inventoryNBT = inventoriesNBT.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(inventoryNBT.getString("uuid"));
            FluidInventoryLogic inventory = new FluidInventoryLogic(
                inventoryNBT.getInteger("invSize"),
                inventoryNBT.getLong("tankCapacity"));
            inventory.loadFromNBT(inventoryNBT.getCompoundTag("inventory"));
            if (inventory.isUpgradeInventory()) {
                unallocatedInventories.add(Pair.of(uuid, inventory));
            } else {
                inventories.put(uuid, inventory);
            }
        }
    }
}
