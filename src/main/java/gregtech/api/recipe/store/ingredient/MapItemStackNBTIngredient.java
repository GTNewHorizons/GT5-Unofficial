package gregtech.api.recipe.store.ingredient;

import java.util.Comparator;
import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GT_Utility;
import gregtech.api.util.item.ItemHolder;

/**
 * Basic ingredient for matching Item, Meta, and NBT tags
 */
public final class MapItemStackNBTIngredient extends AbstractMapIngredient {

    public static final Comparator<MapItemStackNBTIngredient> COMPARATOR = Comparator
        .comparingInt((MapItemStackNBTIngredient i) -> Item.getIdFromItem(i.item))
        .thenComparingInt((MapItemStackNBTIngredient i) -> i.meta)
        .thenComparing((MapItemStackNBTIngredient i) -> i.nbt.toString());

    private final Item item;
    private final int meta;
    private final NBTTagCompound nbt;

    public MapItemStackNBTIngredient(@NotNull ItemHolder holder) {
        this(holder.getItem(), holder.getMeta(), Objects.requireNonNull(holder.getNBT(), "ItemHolder had no NBT"));
    }

    public MapItemStackNBTIngredient(@NotNull ItemStack stack) {
        this(
            Objects.requireNonNull(stack.getItem()),
            stack.getItemDamage(),
            Objects.requireNonNull(stack.getTagCompound(), "ItemHolder had no NBT"));
    }

    public MapItemStackNBTIngredient(@NotNull Item item, int meta, @NotNull NBTTagCompound nbt) {
        this.item = item;
        this.meta = meta;
        this.nbt = nbt;
    }

    @Override
    public int sortingPriority() {
        return 20;
    }

    @Override
    protected int hash() {
        return GT_Utility.stackHashCode(item, meta) * 31 + nbt.hashCode(); // TODO something better?
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapItemStackNBTIngredient that)) return false;

        return meta == that.meta && item.equals(that.item) && nbt.equals(that.nbt);
    }
}
