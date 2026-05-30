package gregtech.api.recipe.lookup;

import java.util.Objects;

import gregtech.api.objects.ItemData;

public final class GTItemDataLookupIngredient extends GTRecipeLookupIngredient {

    private final String unificationName;

    private GTItemDataLookupIngredient(String unificationName) {
        super(unificationName.hashCode());
        this.unificationName = unificationName;
    }

    public static GTItemDataLookupIngredient fromItemData(ItemData itemData) {
        Objects.requireNonNull(itemData, "itemData");
        if (!itemData.hasValidPrefixMaterialData()) {
            throw new IllegalArgumentException("itemData must have prefix and material data");
        }
        String unificationName = itemData.toString();
        if (unificationName.isEmpty()) {
            throw new IllegalArgumentException("itemData unification name must not be empty");
        }
        return new GTItemDataLookupIngredient(unificationName);
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        GTItemDataLookupIngredient otherItemData = (GTItemDataLookupIngredient) other;
        return unificationName.equals(otherItemData.unificationName);
    }
}
