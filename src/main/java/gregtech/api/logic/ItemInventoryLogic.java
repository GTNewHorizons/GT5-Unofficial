package gregtech.api.logic;

import static net.minecraftforge.oredict.OreDictionary.getOreIDs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.forge.ListItemHandler;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.util.GT_Utility;

/**
 * Generic Item logic for MuTEs.
 * 
 * @author BlueWeabo
 */
public class ItemInventoryLogic {

    private static final int DEFAULT_COLUMNS_PER_ROW = 4;
    private static final int POSITION_INTERVAL = 18;
    private static final Size SIZE = new Size(18, 18);

    protected String displayName;
    @Nonnull
    protected final IItemHandlerModifiable inventory;
    protected UUID connectedFluidInventory;
    protected int tier;
    protected boolean isUpgradeInventory;

    public ItemInventoryLogic(int numberOfSlots) {
        this(numberOfSlots, 0);
    }

    public ItemInventoryLogic(int numberOfSlots, int tier) {
        this(new ItemStackHandler(numberOfSlots), tier, false);
    }

    public ItemInventoryLogic(int numberOfSlots, int tier, boolean isUpgradeInventory) {
        this(new ItemStackHandler(numberOfSlots), tier, isUpgradeInventory);
    }

    public ItemInventoryLogic(@Nonnull IItemHandlerModifiable inventory, int tier, boolean isUpgradeInventory) {
        this.inventory = inventory;
        this.tier = tier;
        this.isUpgradeInventory = isUpgradeInventory;
    }

    public ItemInventoryLogic(Collection<IItemHandlerModifiable> inventories) {
        this(new ListItemHandler(inventories), -1, false);
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public int getTier() {
        return tier;
    }

    public boolean isUpgradeInventory() {
        return isUpgradeInventory;
    }

    public int getSlots() {
        return getInventory().getSlots();
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    @Nullable
    public UUID getConnectedFluidInventoryID() {
        return connectedFluidInventory;
    }

    public void setConnectedFluidInventoryID(@Nullable UUID connectedFluidTank) {
        this.connectedFluidInventory = connectedFluidTank;
    }

    /**
     * 
     * @return The Item Inventory Logic as an NBTTagCompound to be saved in another nbt as how one wants.
     */
    @Nonnull
    public NBTTagCompound saveToNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        final NBTTagList tList = new NBTTagList();
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            final ItemStack tStack = inventory.getStackInSlot(slot);
            if (tStack == null) continue;

            final NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("s", (byte) slot);
            tStack.writeToNBT(tag);
            tList.appendTag(tag);
        }
        nbt.setTag("inventory", tList);
        nbt.setInteger("tier", tier);
        if (displayName != null) {
            nbt.setString("displayName", displayName);
        }
        nbt.setBoolean("isUpgradeInventory", isUpgradeInventory);
        if (connectedFluidInventory != null) {
            nbt.setString("connectedFluidInventory", connectedFluidInventory.toString());
        }
        return nbt;
    }

    /**
     * Loads the Item Inventory Logic from an NBTTagCompound.
     */
    public void loadFromNBT(@Nonnull NBTTagCompound nbt) {
        tier = nbt.getInteger("tier");
        if (nbt.hasKey("displayName")) {
            displayName = nbt.getString("displayName");
        }

        isUpgradeInventory = nbt.getBoolean("isUpgradeInventory");
        if (nbt.hasKey("connectedFluidInventory")) {
            connectedFluidInventory = UUID.fromString(nbt.getString("connectedFluidInventory"));
        }

        NBTTagList nbtList = nbt.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
        if (nbtList == null) return;

        for (int i = 0; i < nbtList.tagCount(); i++) {
            final NBTTagCompound tNBT = nbtList.getCompoundTagAt(i);
            final int tSlot = tNBT.getShort("s");
            if (tSlot >= 0 && tSlot < inventory.getSlots()) {
                inventory.setStackInSlot(tSlot, GT_Utility.loadItem(tNBT));
            }
        }
    }

    @Nonnull
    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    @Nonnull
    public ItemStack[] getStoredItems() {
        final ItemStack[] items = inventory.getStacks()
            .stream()
            .filter(item -> item != null)
            .collect(Collectors.toList())
            .toArray(new ItemStack[0]);
        if (items == null) {
            return new ItemStack[0];
        }
        return items;
    }

    public boolean isStackValid(ItemStack item) {
        return true;
    }

    @Nullable
    public ItemStack insertItem(ItemStack item) {
        if (!isStackValid(item)) return item;
        for (int i = 0; i < inventory.getSlots() && item != null && item.stackSize > 0; i++) {
            item = inventory.insertItem(i, item, false);
        }
        return item;
    }

    @Nullable
    public ItemStack extractItem(int slot, int amount) {
        return inventory.extractItem(slot, amount, false);
    }

    public boolean subtractItemAmount(@Nonnull ItemStack amountToSubtract, boolean simulate) {
        ItemHolder itemToSubtractFrom = new ItemHolder(amountToSubtract);
        Map<ItemHolder, Integer> itemMap = getMapOfStoredItems();
        if (!itemMap.containsKey(itemToSubtractFrom)) {
            return false;
        }

        if (itemMap.get(itemToSubtractFrom) < amountToSubtract.stackSize) {
            return false;
        }

        if (simulate) {
            return true;
        }

        itemMap.put(itemToSubtractFrom, itemMap.get(itemToSubtractFrom) - amountToSubtract.stackSize);
        putInItemsFromMap(itemMap, null);
        return true;
    }

    @Nullable
    public ItemStack getItemInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    public void sort() {
        Map<ItemHolder, Integer> itemMap = new HashMap<>();
        List<ItemHolder> sortedItems = new ArrayList<>();

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack item = getItemInSlot(i);
            if (item == null) continue;
            ItemHolder itemHolder = new ItemHolder(item);
            if (!sortedItems.contains(itemHolder)) sortedItems.add(itemHolder);
            itemMap.put(itemHolder, itemMap.getOrDefault(itemHolder, 0) + item.stackSize);
            inventory.setStackInSlot(i, null);
        }
        sortedItems.sort(
            Comparator.comparing(
                a -> a.getItem()
                    .getUnlocalizedName() + a.getMeta()));
        putInItemsFromMap(itemMap, sortedItems);
    }

    public void update(boolean shouldSort) {
        if (shouldSort) {
            sort();
        }

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack item = inventory.getStackInSlot(i);
            if (item == null) continue;
            if (item.stackSize > 0) continue;
            inventory.setStackInSlot(i, null);
        }
    }

    @Nonnull
    public Widget getGuiPart() {
        return getGUIPart(DEFAULT_COLUMNS_PER_ROW);
    }

    @Nonnull
    public Widget getGUIPart(int columnsPerRow) {
        final Scrollable scrollable = new Scrollable();
        scrollable.setVerticalScroll();
        for (int rows = 0; rows * columnsPerRow < Math.min(inventory.getSlots(), 128); rows++) {
            final int columnsToMake = Math
                .min(Math.min(inventory.getSlots(), 128) - rows * columnsPerRow, columnsPerRow);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                    new SlotWidget(inventory, rows * columnsPerRow + column)
                        .setPos(column * POSITION_INTERVAL, rows * POSITION_INTERVAL)
                        .setSize(SIZE));
            }
        }
        return scrollable;
    }

    @Nonnull
    private Map<ItemHolder, Integer> getMapOfStoredItems() {
        Map<ItemHolder, Integer> items = new HashMap<>();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack item = getItemInSlot(i);
            if (item == null) continue;
            ItemHolder itemHolder = new ItemHolder(item);
            items.put(itemHolder, items.getOrDefault(itemHolder, 0) + item.stackSize);
            inventory.setStackInSlot(i, null);
        }
        return items;
    }

    private void putInItemsFromMap(@Nonnull Map<ItemHolder, Integer> itemMap, @Nullable List<ItemHolder> sortedList) {
        int slot = 0;
        for (ItemHolder itemHolder : (sortedList == null ? itemMap.keySet() : sortedList)) {
            int itemAmount = itemMap.get(itemHolder);
            ItemStack item = new ItemStack(itemHolder.getItem(), 0, itemHolder.getMeta());
            item.setTagCompound(itemHolder.getNBT());
            while (itemAmount > 0) {
                item.stackSize = Math.min(item.getMaxStackSize(), itemAmount);
                itemAmount -= item.stackSize;
                inventory.setStackInSlot(slot, item);
                slot++;
            }
        }
    }

    private class ItemHolder {

        private final Item item;
        private final int meta;
        private final NBTTagCompound tag;
        private final int[] oreIDs;

        public ItemHolder(ItemStack item) {
            this.item = item.getItem();
            this.meta = Items.feather.getDamage(item);
            this.tag = item.getTagCompound();
            this.oreIDs = getOreIDs(item);
        }

        public Item getItem() {
            return item;
        }

        public int getMeta() {
            return meta;
        }

        public NBTTagCompound getNBT() {
            return tag;
        }

        public int[] getOreDictTagIDs() {
            return oreIDs;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ItemHolder otherIH)) return false;
            if (Arrays.stream(oreIDs)
                .anyMatch(id -> {
                    for (int i = 0; i < otherIH.getOreDictTagIDs().length; i++) {
                        if (id == otherIH.getOreDictTagIDs()[i]) return true;
                    }
                    return false;
                })) {
                return true;
            }
            return item == otherIH.getItem() && meta == otherIH.getMeta() && tag.equals(otherIH.getNBT());
        }
    }
}
