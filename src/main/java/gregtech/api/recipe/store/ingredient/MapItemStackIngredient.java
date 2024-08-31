package gregtech.api.recipe.store.ingredient;

import java.util.Comparator;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.util.GT_Utility;
import gregtech.api.util.item.ItemHolder;

public final class MapItemStackIngredient extends AbstractMapIngredient {

    public static final Comparator<MapItemStackIngredient> COMPARATOR = (a, b) -> {
        // circuits get priority as a trie search depth optimization
        if (GT_Utility.isAnyIntegratedCircuit(a.stack)) {
            if (!GT_Utility.isAnyIntegratedCircuit(b.stack)) {
                return -1;
            }
        } else if (GT_Utility.isAnyIntegratedCircuit(b.stack)) {
            return 1;
        }

        return ItemHolder.COMPARATOR.compare(a.stack, b.stack);
    };

    private final ItemHolder stack;

    public MapItemStackIngredient(@NotNull ItemHolder stack) {
        this.stack = stack;
    }

    public MapItemStackIngredient(@NotNull ItemStack stack) {
        this(new ItemHolder(stack));
    }

    @Override
    protected int hash() {
        return stack.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapItemStackIngredient that)) return false;

        return stack.equals(that.stack);
    }

    @Override
    public String toString() {
        return "MapItemStackIngredient{" + stack + '}';
    }
}
