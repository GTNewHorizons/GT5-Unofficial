package gregtech.api.recipe.store.ingredient;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

public final class MapIngredientComparator implements Comparator<AbstractMapIngredient> {

    public static final Comparator<AbstractMapIngredient> INSTANCE = new MapIngredientComparator();

    private MapIngredientComparator() {}

    @Override
    public int compare(@NotNull AbstractMapIngredient o1, @NotNull AbstractMapIngredient o2) {
        int result = Integer.compare(o1.sortingPriority(), o2.sortingPriority());
        if (result != 0) {
            return result;
        }

        return compareSubClass(o1, o2);
    }

    private static int compareSubClass(@NotNull AbstractMapIngredient o1, @NotNull AbstractMapIngredient o2) {
        if (o1 instanceof MapItemStackIngredient ingredient) {
            return MapItemStackIngredient.COMPARATOR.compare(ingredient, (MapItemStackIngredient) o2);
        }
        if (o1 instanceof MapItemStackNBTIngredient ingredient) {
            return MapItemStackNBTIngredient.COMPARATOR.compare(ingredient, (MapItemStackNBTIngredient) o2);
        }
        if (o1 instanceof MapOreDictIngredient ingredient) {
            return MapOreDictIngredient.COMPARATOR.compare(ingredient, (MapOreDictIngredient) o2);
        }
        if (o1 instanceof MapFluidStackIngredient ingredient) {
            return MapFluidStackIngredient.COMPARATOR.compare(ingredient, (MapFluidStackIngredient) o2);
        }

        throw new IllegalArgumentException("AbstractMapIngredient is of unknown class: " + o1.getClass());
    }
}
