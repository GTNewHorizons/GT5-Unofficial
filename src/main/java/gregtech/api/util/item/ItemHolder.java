package gregtech.api.util.item;

import static net.minecraftforge.oredict.OreDictionary.getOreIDs;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Comparator;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.util.GT_Utility;

public class ItemHolder {

    public static final Comparator<ItemHolder> COMPARATOR = Comparator
        .comparingInt((ItemHolder i) -> Item.getIdFromItem(i.getItem()))
        .thenComparing(ItemHolder::getMeta)
        .thenComparing((ItemHolder i) -> IntBuffer.wrap(i.getOreDictTagIDs()))
        .thenComparing(
            (ItemHolder i) -> i.getNBT() == null ? ""
                : i.getNBT()
                    .toString());

    private final Item item;
    private final int meta;
    private final NBTTagCompound tag;
    private final int[] oreIDs;

    public ItemHolder(@Nonnull ItemStack item) {
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
        if (other == this) return true;
        if (!(other instanceof ItemHolder otherIH)) return false;
        for (int id : oreIDs) {
            for (int i = 0; i < otherIH.getOreDictTagIDs().length; i++) {
                if (id == otherIH.getOreDictTagIDs()[i]) {
                    return true;
                }
            }
        }

        if (item != otherIH.getItem() || meta != otherIH.getMeta()) {
            return false;
        }
        if (this.tag == null && otherIH.getNBT() == null) return true;
        if (this.tag == null || otherIH.getNBT() == null) return false;
        return this.tag.equals(otherIH.tag);
    }

    @Override
    public int hashCode() {
        return GT_Utility.itemToInt(item, meta);
    }

    @Nonnull
    private ItemStack toStack() {
        ItemStack item = new ItemStack(this.item, 1, meta);
        item.stackTagCompound = tag;
        return item;
    }

    @Override
    public String toString() {
        return "ItemHolder{" + "item="
            + item
            + ", meta="
            + meta
            + ", tag="
            + tag
            + ", oreIDs="
            + Arrays.toString(oreIDs)
            + '}';
    }
}
