package gregtech.api.recipe.store.ingredient;

import java.util.Comparator;
import java.util.Objects;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GT_Utility;
import gregtech.api.util.item.ItemHolder;

/**
 * Basic ingredient for Item and Metadata
 */
public final class MapItemStackIngredient extends AbstractMapIngredient {

    private static final Comparator<MapItemStackIngredient> CIRCUIT_COMPARATOR = (a, b) -> {
        // circuits get priority as a trie search depth optimization
        if (GT_Utility.isAnyIntegratedCircuit(a.item, a.meta)) {
            if (!GT_Utility.isAnyIntegratedCircuit(b.item, b.meta)) {
                return -1;
            }
        } else if (GT_Utility.isAnyIntegratedCircuit(b.item, b.meta)) {
            return 1;
        }

        return 0;
    };

    public static final Comparator<MapItemStackIngredient> COMPARATOR = CIRCUIT_COMPARATOR
        .thenComparingInt((MapItemStackIngredient i) -> Item.getIdFromItem(i.item))
        .thenComparingInt((MapItemStackIngredient i) -> i.meta);

    private final Item item;
    private final int meta;

    public MapItemStackIngredient(@NotNull ItemHolder holder) {
        this(holder.getItem(), holder.getMeta());
    }

    public MapItemStackIngredient(@NotNull ItemStack stack) {
        this(Objects.requireNonNull(stack.getItem()), Items.feather.getDamage(stack));
    }

    public MapItemStackIngredient(@NotNull Item item, int meta) {
        this.item = item;
        this.meta = meta;
    }

    @Override
    public int sortingPriority() {
        return 10;
    }

    @Override
    protected int hash() {
        return GT_Utility.stackHashCode(item, meta);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapItemStackIngredient that)) return false;

        return meta == that.meta && item.equals(that.item);
    }

    @Override
    public String toString() {
        return "MapItemStackIngredient{" + new ItemStack(item, 1, meta) + '}';
    }
}
