package gregtech.api.recipe.store.ingredient;

import java.util.Comparator;

public final class MapIngredientComparator implements Comparator<AbstractMapIngredient> {

    public static final Comparator<AbstractMapIngredient> INSTANCE = new MapIngredientComparator();

    private MapIngredientComparator() {}

    @Override
    public int compare(AbstractMapIngredient o1, AbstractMapIngredient o2) {
        if (o1 instanceof MapItemStackIngredient item1) {
            if (o2 instanceof MapItemStackIngredient item2) {
                return MapItemStackIngredient.COMPARATOR.compare(item1, item2);
            } else {
                return -1;
            }
        } else if (o1 instanceof MapFluidStackIngredient fluid1) {
            if (o2 instanceof MapFluidStackIngredient fluid2) {
                return MapFluidStackIngredient.COMPARATOR.compare(fluid1, fluid2);
            } else {
                return 1;
            }
        } else {
            throw new IllegalArgumentException("AbstractMapIngredient is of unknown subclass");
        }
    }
}
