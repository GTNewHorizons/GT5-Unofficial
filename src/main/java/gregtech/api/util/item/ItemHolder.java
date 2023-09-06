package gregtech.api.util.item;

import static net.minecraftforge.oredict.OreDictionary.getOreIDs;

import java.util.Arrays;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.util.GT_Utility;

public class ItemHolder {

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

    @Override
    public int hashCode() {
        return GT_Utility.stackToInt(toStack());
    }

    @Nonnull
    private ItemStack toStack() {
        ItemStack item = new ItemStack(this.item, 1, meta);
        item.stackTagCompound = tag;
        return item;
    }
}
