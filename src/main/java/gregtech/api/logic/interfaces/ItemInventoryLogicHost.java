package gregtech.api.logic.interfaces;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.InventoryType;
import gregtech.api.logic.ItemInventoryLogic;

public interface ItemInventoryLogicHost extends ISidedInventory {

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

    /**
     * Only to be used for MultiBlockPart
     * 
     * @return
     */
    @Nullable
    default InventoryType getItemInventoryType() {
        return null;
    }

    @Override
    @Nullable
    default ItemStack decrStackSize(int slot, int count) {
        InventoryType type = getItemInventoryType();
        if (type == InventoryType.Both) return null;
        ItemInventoryLogic logic = getItemLogic(ForgeDirection.UNKNOWN, type == null ? InventoryType.Output : type);
        if (logic == null) return null;
        return logic.extractItem(slot, count);
    }

    @Override
    default int getSizeInventory() {
        InventoryType type = getItemInventoryType();
        if (type == InventoryType.Both) return 0;
        ItemInventoryLogic logic = getItemLogic(ForgeDirection.UNKNOWN, type == null ? InventoryType.Output : type);
        if (logic == null) return 0;
        return logic.getSlots();
    }

    @Override
    @Nullable
    default ItemStack getStackInSlot(int slot) {
        InventoryType type = getItemInventoryType();
        if (type == InventoryType.Both) return null;
        ItemInventoryLogic logic = getItemLogic(ForgeDirection.UNKNOWN, type == null ? InventoryType.Output : type);
        if (logic == null) return null;
        return logic.getInventory()
            .getStackInSlot(slot);
    }

    @Override
    default boolean isItemValidForSlot(int slot, @Nullable ItemStack stack) {
        InventoryType type = getItemInventoryType();
        if (type == InventoryType.Both) return false;
        ItemInventoryLogic logic = getItemLogic(ForgeDirection.UNKNOWN, type == null ? InventoryType.Output : type);
        if (logic == null) return false;
        return logic.getInventory()
            .isItemValid(slot, stack);
    }

    @Override
    default void setInventorySlotContents(int slot, @Nullable ItemStack stack) {
        InventoryType type = getItemInventoryType();
        if (type == InventoryType.Both) return;
        ItemInventoryLogic logic = getItemLogic(ForgeDirection.UNKNOWN, type == null ? InventoryType.Output : type);
        if (logic == null) return;
        logic.getInventory()
            .setStackInSlot(slot, stack);
    }

    @Override
    default boolean canExtractItem(int ignoredSlot, ItemStack ignoredItem, int side) {
        InventoryType type = getItemInventoryType();
        if (type == null) return false;
        return getItemLogic(ForgeDirection.getOrientation(side), type) != null;
    }

    @Override
    default boolean canInsertItem(int ignoredSlot, ItemStack ignoredItem, int side) {
        InventoryType type = getItemInventoryType();
        if (type == null) return false;
        return getItemInventoryType() != InventoryType.Output
            && getItemLogic(ForgeDirection.getOrientation(side), type) != null;
    }

    @Override
    default int[] getAccessibleSlotsFromSide(int side) {
        InventoryType type = getItemInventoryType();
        if (type == null) return new int[0];
        ItemInventoryLogic logic = getItemLogic(ForgeDirection.UNKNOWN, type == null ? InventoryType.Output : type);
        if (logic == null) return new int[0];
        int[] indexes = new int[logic.getSlots()];
        for (int i = 0; i < logic.getSlots(); i++) {
            indexes[i] = i;
        }
        return indexes;
    }

    @Override
    default void closeInventory() {}

    @Override
    default String getInventoryName() {
        return "";
    }

    @Override
    default int getInventoryStackLimit() {
        return 64;
    }

    @Override
    default ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    @Override
    default boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    default boolean isUseableByPlayer(@Nonnull EntityPlayer player) {
        return false;
    }

    @Override
    default void openInventory() {}

}
