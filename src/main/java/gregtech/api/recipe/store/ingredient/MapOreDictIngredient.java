package gregtech.api.recipe.store.ingredient;

import java.util.Comparator;

import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

/**
 * Basic ingredient for matching oredict
 */
public final class MapOreDictIngredient extends AbstractMapIngredient {

    public static final Comparator<MapOreDictIngredient> COMPARATOR = Comparator.comparingInt(i -> i.oreDict);

    private final int oreDict;

    public MapOreDictIngredient(int oreDict) {
        this.oreDict = oreDict;
    }

    public MapOreDictIngredient(@NotNull String oreDict) {
        this(OreDictionary.getOreID(oreDict));
    }

    @Override
    public int sortingPriority() {
        return 50;
    }

    @Override
    protected int hash() {
        return oreDict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapOreDictIngredient that)) return false;

        return oreDict == that.oreDict;
    }

    @Override
    public String toString() {
        return "MapOreDictIngredient{" + "oreDict=" + OreDictionary.getOreName(oreDict) + ", id=" + oreDict + '}';
    }
}
