package gregtech.api.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Logic of the Item logic for the controller. This is controlling all of the inventories.
 * 
 * @author BlueWeabo
 */
public class ControllerItemLogic {

    private final Map<UUID, ItemInventoryLogic> inventories = new HashMap<>();
    private final Set<Pair<UUID, ItemInventoryLogic>> unallocatedInventories = new HashSet<>();

    public void addInventory(@Nonnull UUID id, @Nonnull ItemInventoryLogic inventory) {
        Pair<UUID, ItemInventoryLogic> found = checkIfInventoryExistsAsUnallocated(inventory);
        if (inventory.isUpgradeInventory() && found != null) {
            unallocatedInventories.remove(found);
            inventories.put(id, found.getRight());
            return;
        }
        inventories.put(id, inventory);
    }

    @Nonnull
    public UUID addInventory(@Nonnull ItemInventoryLogic inventory) {
        Pair<UUID, ItemInventoryLogic> found = checkIfInventoryExistsAsUnallocated(inventory);
        if (inventory.isUpgradeInventory() && found != null) {
            unallocatedInventories.remove(found);
            inventories.put(found.getLeft(), found.getRight());
            return found.getLeft();
        }
        UUID generatedUUID = UUID.randomUUID();
        inventories.put(generatedUUID, inventory);
        return generatedUUID;
    }

    @Nullable
    private Pair<UUID, ItemInventoryLogic> checkIfInventoryExistsAsUnallocated(@Nonnull ItemInventoryLogic inventory) {
        if (unallocatedInventories.size() == 0) {
            return null;
        }
        return unallocatedInventories.stream()
            .filter(
                unallocated -> unallocated.getRight()
                    .getTier() == inventory.getTier()
                    && unallocated.getRight()
                        .getSlots() == inventory.getSlots())
            .findFirst()
            .get();
    }

    /**
     * Removes the inventory with said id and gives it back to be processed if needed.
     */
    @Nonnull
    public ItemInventoryLogic removeInventory(@Nonnull UUID id) {
        return inventories.remove(id);
    }

    @Nonnull
    public ItemInventoryLogic getAllInventoryLogics() {
        return new ItemInventoryLogic(
            inventories.values()
                .stream()
                .map(inv -> inv.getInventory())
                .collect(Collectors.toList()));
    }

    @Nonnull
    public ItemInventoryLogic getInventoryLogic(@Nullable UUID id) {
        if (id == null) return getAllInventoryLogics();
        return inventories.getOrDefault(id, getAllInventoryLogics());
    }

    @Nullable
    public String getInventoryDisplayName(@Nullable UUID id) {
        if (id == null) return "";
        ItemInventoryLogic logic = inventories.get(id);
        if (logic == null) return "";
        if (logic.getDisplayName() == null) return id.toString();
        if (logic.getDisplayName()
            .isEmpty()) return id.toString();
        return logic.getDisplayName();
    }

    public void setInventoryDisplayName(@Nullable UUID id, @Nullable String displayName) {
        if (id == null) return;
        ItemInventoryLogic logic = inventories.get(id);
        if (logic == null) return;
        logic.setDisplayName(displayName);
    }

    @Nonnull
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
                    .getSlots());
            inventoriesNBT.appendTag(inventoryNBT);
        });
        nbt.setTag("inventories", inventoriesNBT);
        return nbt;
    }

    public void loadFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList inventoriesNBT = nbt.getTagList("inventories", Constants.NBT.TAG_COMPOUND);
        if (inventoriesNBT == null) return;
        for (int i = 0; i < inventoriesNBT.tagCount(); i++) {
            NBTTagCompound inventoryNBT = inventoriesNBT.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(inventoryNBT.getString("uuid"));
            ItemInventoryLogic inventory = new ItemInventoryLogic(inventoryNBT.getInteger("invSize"));
            NBTTagCompound internalInventoryNBT = inventoryNBT.getCompoundTag("inventory");
            if (internalInventoryNBT != null) inventory.loadFromNBT(internalInventoryNBT);
            if (inventory.isUpgradeInventory()) {
                unallocatedInventories.add(Pair.of(uuid, inventory));
            } else {
                inventories.put(uuid, inventory);
            }
        }
    }
}
