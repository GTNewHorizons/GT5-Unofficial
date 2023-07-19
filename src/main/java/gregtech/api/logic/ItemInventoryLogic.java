package gregtech.api.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.forge.ListItemHandler;
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

    protected String displayName;
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

    public ItemInventoryLogic(IItemHandlerModifiable inventory, int tier, boolean isUpgradeInventory) {
        this.inventory = inventory;
        this.tier = tier;
        this.isUpgradeInventory = isUpgradeInventory;
    }

    public ItemInventoryLogic(Collection<IItemHandlerModifiable> inventories) {
        this(new ListItemHandler(inventories), -1, false);
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getTier() {
        return tier;
    }

    public boolean isUpgradeInventory() {
        return isUpgradeInventory;
    }

    public ItemInventoryLogic setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public UUID getConnectedFluidInventoryID() {
        return connectedFluidInventory;
    }

    public void setConnectedFluidInventoryID(UUID connectedFluidTank) {
        this.connectedFluidInventory = connectedFluidTank;
    }

    /**
     * 
     * @return The Item Inventory Logic as an NBTTagCompound to be saved in another nbt as how one wants.
     */
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
        nbt.setTag("Inventory", tList);
        nbt.setInteger("Tier", tier);
        nbt.setString("DisplayName", displayName);
        nbt.setBoolean("IsUpgradeInventory", isUpgradeInventory);
        return nbt;
    }

    /**
     * Loads the Item Inventory Logic from an NBTTagCompound.
     */
    public void loadFromNBT(NBTTagCompound nbt) {
        NBTTagList nbtList = nbt.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtList.tagCount(); i++) {
            final NBTTagCompound tNBT = nbtList.getCompoundTagAt(i);
            final int tSlot = tNBT.getShort("s");
            if (tSlot >= 0 && tSlot < inventory.getSlots()) inventory.setStackInSlot(tSlot, GT_Utility.loadItem(tNBT));
        }
        tier = nbt.getInteger("Tier");
        displayName = nbt.getString("DisplayName");
        isUpgradeInventory = nbt.getBoolean("IsUpgradeInventory");
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    public ItemStack[] getStoredItems() {
        return inventory.getStacks()
            .stream()
            .filter(item -> item != null)
            .collect(Collectors.toList())
            .toArray(new ItemStack[0]);
    }

    public boolean isStackValid(ItemStack item) {
        return true;
    }

    public ItemStack insertItem(ItemStack item) {
        if (!isStackValid(item)) return item;
        for (int i = 0; i < inventory.getSlots() && item != null && item.stackSize > 0; i++) {
            item = inventory.insertItem(i, item, false);
        }
        return item;
    }

    public ItemStack extractItem(int slot, int amount) {
        return inventory.extractItem(slot, amount, false);
    }

    public void sort() {
        Map<ItemHolder, Integer> itemsToSort = new HashMap<>();
        List<ItemHolder> items = new ArrayList<>();

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack item = inventory.getStackInSlot(i);
            if (item == null) continue;
            ItemHolder itemHolder = new ItemHolder(item);
            if (!items.contains(itemHolder)) items.add(itemHolder);
            itemsToSort.put(itemHolder, itemsToSort.getOrDefault(itemHolder, 0) + item.stackSize);
            inventory.setStackInSlot(i, null);
        }
        items.sort(
            Comparator.comparing(
                a -> a.getItem()
                    .getUnlocalizedName() + a.getMeta()));
        int slot = 0;
        for (ItemHolder itemHolder : items) {
            int itemAmount = itemsToSort.get(itemHolder);
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

    public Widget getGuiPart() {
        return getGUIPart(4);
    }

    public Widget getGUIPart(int columnsPerRow) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * columnsPerRow < Math.min(inventory.getSlots(), 128); rows++) {
            final int columnsToMake = Math
                .min(Math.min(inventory.getSlots(), 128) - rows * columnsPerRow, columnsPerRow);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                    new SlotWidget(inventory, rows * columnsPerRow + column).setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        return scrollable;
    }

    private class ItemHolder {

        private Item item;
        private int meta;
        private NBTTagCompound tag;

        public ItemHolder(ItemStack item) {
            this(item.getItem(), Items.feather.getDamage(item), item.getTagCompound());
        }

        public ItemHolder(Item item, int meta, NBTTagCompound tag) {
            this.item = item;
            this.meta = meta;
            this.tag = tag;
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

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ItemHolder)) return false;
            ItemHolder otherIH = (ItemHolder) other;
            return item == otherIH.getItem() && meta == otherIH.getMeta() && tag.equals(otherIH.getNBT());
        }
    }
}
