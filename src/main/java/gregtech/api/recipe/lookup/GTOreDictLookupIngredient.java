package gregtech.api.recipe.lookup;

import java.util.function.Consumer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GTOreDictLookupIngredient extends GTRecipeLookupIngredient {

    private final int oreId;

    public GTOreDictLookupIngredient(int oreId) {
        super(oreId);
        this.oreId = oreId;
    }

    public static void fromRuntime(Consumer<? super GTOreDictLookupIngredient> ingredients, ItemStack stack) {
        for (int oreId : OreDictionary.getOreIDs(stack)) {
            ingredients.accept(new GTOreDictLookupIngredient(oreId));
        }
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        return oreId == ((GTOreDictLookupIngredient) other).oreId;
    }
}
