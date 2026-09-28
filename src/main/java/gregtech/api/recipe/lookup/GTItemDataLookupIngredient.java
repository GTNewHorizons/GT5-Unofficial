package gregtech.api.recipe.lookup;

import java.util.Objects;
import java.util.function.Consumer;

import net.minecraft.item.ItemStack;

import gregtech.api.material.MaterialData;
import gregtech.api.objects.ItemData;

public final class GTItemDataLookupIngredient extends GTRecipeLookupIngredient {

    private final String unificationName;

    private GTItemDataLookupIngredient(String unificationName) {
        super(unificationName.hashCode());
        this.unificationName = unificationName;
    }

    public static GTItemDataLookupIngredient fromAssociationName(String associationName) {
        Objects.requireNonNull(associationName, "associationName");
        if (associationName.isEmpty()) {
            throw new IllegalArgumentException("associationName must not be empty");
        }
        return new GTItemDataLookupIngredient(associationName);
    }

    public static void fromRuntime(Consumer<? super GTItemDataLookupIngredient> ingredients, ItemStack stack) {
        ItemData itemData = MaterialData.get(stack);
        if (itemData == null) return;
        String unificationName = itemData.toString();
        if (unificationName.isEmpty()) return;
        ingredients.accept(new GTItemDataLookupIngredient(unificationName));
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        GTItemDataLookupIngredient otherItemData = (GTItemDataLookupIngredient) other;
        return unificationName.equals(otherItemData.unificationName);
    }
}
