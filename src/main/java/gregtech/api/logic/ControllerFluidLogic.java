package gregtech.api.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
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
 * Controller logic for Fluid inventories
 * 
 * @author BlueWeabo
 */
public class ControllerFluidLogic {

    private final Map<UUID, FluidInventoryLogic> inventories = new HashMap<>();
    private final Set<Pair<UUID, FluidInventoryLogic>> unallocatedInventories = new HashSet<>();

    public void addInventory(@Nonnull UUID id, @Nonnull FluidInventoryLogic inventory) {
        Pair<UUID, FluidInventoryLogic> found = checkIfInventoryExistsAsUnallocated(inventory);
        if (inventory.isUpgradeInventory() && found != null) {
            unallocatedInventories.remove(found);
            inventories.put(id, found.getRight());
            return;
        }
        inventories.put(id, inventory);
    }

    @Nonnull
    public UUID addInventory(@Nonnull FluidInventoryLogic inventory) {
        Pair<UUID, FluidInventoryLogic> found = checkIfInventoryExistsAsUnallocated(inventory);
        if (inventory.isUpgradeInventory() && found != null) {
            unallocatedInventories.remove(found);
            inventories.put(found.getLeft(), found.getRight());
            return Objects.requireNonNull(found.getLeft());
        }
        UUID generatedUUID = Objects.requireNonNull(UUID.randomUUID());
        inventories.put(generatedUUID, inventory);
        return generatedUUID;
    }

    @Nullable
    private Pair<UUID, FluidInventoryLogic> checkIfInventoryExistsAsUnallocated(
        @Nonnull FluidInventoryLogic inventory) {
        if (unallocatedInventories.size() == 0) {
            return null;
        }
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
    @Nonnull
    public FluidInventoryLogic removeInventory(@Nonnull UUID id) {
        return Objects.requireNonNull(inventories.remove(id));
    }

    @Nonnull
    public FluidInventoryLogic getAllInventoryLogics() {
        return new FluidInventoryLogic(
            inventories.values()
                .stream()
                .map(inv -> inv.getInventory())
                .collect(Collectors.toList()));
    }

    @Nonnull
    public FluidInventoryLogic getInventoryLogic(@Nullable UUID id) {
        if (id == null) return getAllInventoryLogics();
        return Objects.requireNonNull(inventories.getOrDefault(id, getAllInventoryLogics()));
    }

    @Nonnull
    public Set<Entry<UUID, FluidInventoryLogic>> getAllInventoryLogicsAsEntrySet() {
        return Objects.requireNonNull(inventories.entrySet());
    }

    @Nonnull
    public String getInventoryDisplayName(@Nullable UUID id) {
        if (id == null) return "";
        FluidInventoryLogic logic = inventories.get(id);
        if (logic == null) return "";
        String displayName = logic.getDisplayName();
        if (displayName == null) return Objects.requireNonNull(id.toString());
        return displayName;
    }

    public void setInventoryDisplayName(@Nullable UUID id, @Nullable String displayName) {
        if (id == null) return;
        FluidInventoryLogic logic = inventories.get(id);
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

    public void loadFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList inventoriesNBT = nbt.getTagList("inventories", Constants.NBT.TAG_COMPOUND);
        if (inventoriesNBT == null) return;
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
