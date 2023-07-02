package gregtech.api.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.forge.ListItemHandler;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.util.GT_Utility;

public class ItemInventoryLogic {

    protected String displayName;
    protected final IItemHandlerModifiable inventory;
    protected UUID connectedFluidInventory;

    public ItemInventoryLogic(int numberOfSlots) {
        this(new ItemStackHandler(numberOfSlots));
    }

    public ItemInventoryLogic(IItemHandlerModifiable inventory) {
        this.inventory = inventory;
    }

    public ItemInventoryLogic(Collection<IItemHandlerModifiable> inventories) {
        this(new ListItemHandler(inventories));
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UUID getConnectedFluidInventoryID() {
        return connectedFluidInventory;
    }

    public void setConnectedFluidInventoryID(UUID connectedFluidTank) {
        this.connectedFluidInventory = connectedFluidTank;
    }

    /**
     * 
     * @return The Item Inventory Logic as an NBTTagList to be saved in another nbt as how one wants.
     */
    public NBTTagList saveToNBT() {
        final NBTTagList tList = new NBTTagList();
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            final ItemStack tStack = inventory.getStackInSlot(slot);
            if (tStack == null) continue;

            final NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("s", (byte) slot);
            tStack.writeToNBT(tag);
            tList.appendTag(tag);
        }
        return tList;
    }

    /**
     * Loads the Item Inventory Logic from an NBTTagList.
     */
    public void loadFromNBT(NBTTagList nbtList) {
        for (int i = 0; i < nbtList.tagCount(); i++) {
            final NBTTagCompound tNBT = nbtList.getCompoundTagAt(i);
            final int tSlot = tNBT.getShort("s");
            if (tSlot >= 0 && tSlot < inventory.getSlots()) inventory.setStackInSlot(tSlot, GT_Utility.loadItem(tNBT));
        }
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    public ItemStack[] getStoredItems() {
        return inventory.getStacks()
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
        System.out.println("sorting");
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
