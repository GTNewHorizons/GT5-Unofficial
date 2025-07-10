package kubatech.api.eig;

import static kubatech.api.utils.ItemUtils.readItemStackFromNBT;
import static kubatech.api.utils.ItemUtils.writeItemStackToNBT;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.map.ItemStackMap;

public class EIGDropTable {

    private static final String NBT_DROP_TABLE_ITEM_KEY = "item";
    private static final String NBT_DROP_TABLE_COUNT_KEY = "count";

    private final @NotNull ItemStackMap<Double> dropTable;

    /**
     * Initialises a new empty drop table.
     */
    public EIGDropTable() {
        this.dropTable = new ItemStackMap<>(true);
    }

    /**
     * Loads a serialised drop table from nbt.
     *
     * @param nbt The nbt tag that contains the key for a drop table
     * @param key The name of the key name for the drop table.
     */
    public EIGDropTable(@NotNull NBTTagCompound nbt, String key) {
        // should create an empty table if no drops are found.
        this(nbt.getTagList(key, 10));
    }

    /**
     * Loads a serialised drop table from nbt.
     *
     * @param nbt The nbt tag that contains the key for a drop table
     */
    public EIGDropTable(@NotNull NBTTagList nbt) {
        this();
        for (int i = 0; i < nbt.tagCount(); i++) {
            NBTTagCompound drop = nbt.getCompoundTagAt(i);
            dropTable.merge(
                readItemStackFromNBT(drop.getCompoundTag(NBT_DROP_TABLE_ITEM_KEY)),
                drop.getDouble(NBT_DROP_TABLE_COUNT_KEY),
                Double::sum);
        }
    }

    /**
     * Serialises the drop table to nbt
     *
     * @return The serialised drop table.
     */
    public @NotNull NBTTagList save() {
        NBTTagList nbt = new NBTTagList();
        for (Map.Entry<ItemStack, Double> entry : this.dropTable.entrySet()) {
            NBTTagCompound entryNBT = new NBTTagCompound();
            entryNBT.setTag(NBT_DROP_TABLE_ITEM_KEY, writeItemStackToNBT(entry.getKey()));
            entryNBT.setDouble(NBT_DROP_TABLE_COUNT_KEY, entry.getValue());
            nbt.appendTag(entryNBT);
        }
        return nbt;
    }

    /**
     * Adds a drop to the drop table
     *
     * @param itemStack The item to add to the table.
     * @param amount    The amount to add to the table.
     */
    public void addDrop(@NotNull ItemStack itemStack, double amount) {
        ItemStack key = itemStack.copy();
        key.stackSize = 1;
        this.dropTable.merge(key, amount, Double::sum);
    }

    /**
     * Adds the values from this drop table to another, but multiplies the amount by a random amount bound by variance.
     *
     * @param target   The drop table that you want to add the value to.
     * @param variance How much to vary the amounts of this drop table to, 0 < x < 1 plz
     * @param rand     The random source for the variance.
     */
    public void addTo(@NotNull EIGDropTable target, double variance, @NotNull Random rand) {
        this.addTo(target, 1.0, variance, rand);
    }

    /**
     * Adds the values from this drop table to another, but multiplies the amount by a multiplier and a random amount
     * bound by variance.
     *
     * @param target     The drop table that you want to add the value to.
     * @param multiplier A multiplier to apply to all amounts from this drop table.
     * @param variance   How much to vary the amounts of this drop table to, 0 < x < 1 plz.
     * @param rand       The random source for the variance.
     */
    public void addTo(@NotNull EIGDropTable target, double multiplier, double variance, @NotNull Random rand) {
        this.addTo(target, variance * (rand.nextDouble() - 0.5) * multiplier);
    }

    /**
     * Adds the values from this drop table to another.
     *
     * @param target The drop table that you want to add the value to.
     */
    public void addTo(@NotNull EIGDropTable target) {
        this.addTo(target, 1.0);
    }

    /**
     * Adds the values from this drop table to another but multiplies the values by a multiplier.
     *
     * @param target     The drop table that you want to add the value to.
     * @param multiplier A multiplier to apply to all amounts from this drop table.
     */
    public void addTo(@NotNull EIGDropTable target, double multiplier) {
        for (Map.Entry<ItemStack, Double> entry : this.dropTable.entrySet()) {
            target.dropTable.merge(entry.getKey(), entry.getValue() * multiplier, Double::sum);
        }
    }

    /**
     * Checks if the drop table is empty;
     *
     * @return true if empty.
     */
    public boolean isEmpty() {
        return this.dropTable.isEmpty();
    }

    /**
     * Returns the entry set for this drop table.
     *
     * @return ItemStack -> amount
     */
    public @NotNull Set<Map.Entry<ItemStack, Double>> entrySet() {
        return this.dropTable.entrySet();
    }

    /**
     * Gets the amount for a specific item.
     *
     * @param item The item to look for.
     * @return 0 if nothing is found else a positive value.
     */
    public double getItemAmount(ItemStack item) {
        if (this.dropTable.containsKey(item)) {
            return this.dropTable.get(item);
        }
        return 0;
    }

    /**
     * Sets the amount for a specific item.
     *
     * @param item The item to look for.
     */
    public void setItemAmount(ItemStack item, double value) {
        this.dropTable.put(item, value);
    }

    /**
     * Removes an item from the drop table
     *
     * @param item The item to remove from the drop table.
     */
    public void removeItem(ItemStack item) {
        this.dropTable.remove(item);
    }

    /**
     * Creates a new drop table that is the intersection of this drop table and another.
     *
     *
     * @param with The drop table to intersect with.
     * @return The result of the intersection.
     */
    public @NotNull EIGDropTable intersect(@NotNull EIGDropTable with) {
        EIGDropTable ret = new EIGDropTable();
        for (ItemStack key : with.dropTable.keySet()) {
            if (this.dropTable.containsKey(key)) {
                ret.addDrop(key, this.dropTable.get(key));
            }
        }
        return ret;
    }

    /**
     * Consumes drops with drop counts above 1 and returns a list of the consumed item stacks.
     *
     * @return The list of consumed items;
     */
    public ItemStack @NotNull [] getDrops() {
        // doesn't need to filter for less than 0 so that the EIG displays the progress of incomplete items.
        return this.dropTable.entrySet()
            .parallelStream()
            .map(EIGDropTable::computeDrops)
            .toArray(ItemStack[]::new);
    }

    /**
     * Consumes the items in the entry and returns the consumed item without removing partial items.
     *
     * @param entry The entry to consume from
     * @return The item tha twas removed.
     */
    private static @NotNull ItemStack computeDrops(Map.@NotNull Entry<ItemStack, Double> entry) {
        ItemStack copied = entry.getKey()
            .copy();
        copied.stackSize = (int) Math.floor(entry.getValue());
        if (entry.getValue() >= 1.0d) {
            entry.setValue(entry.getValue() % 1);
        }
        return copied;
    }
}
