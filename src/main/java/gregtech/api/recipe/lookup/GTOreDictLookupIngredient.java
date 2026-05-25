package gregtech.api.recipe.lookup;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class GTOreDictLookupIngredient extends GTRecipeLookupIngredient {

    private final int oreId;

    public GTOreDictLookupIngredient(int oreId) {
        super(oreId);
        this.oreId = oreId;
    }

    public static List<GTOreDictLookupIngredient> fromRuntime(ItemStack stack) {
        int[] oreIds = OreDictionary.getOreIDs(stack);
        List<GTOreDictLookupIngredient> result = new ArrayList<>(oreIds.length);
        for (int oreId : oreIds) {
            result.add(new GTOreDictLookupIngredient(oreId));
        }
        return result;
    }

    public int getOreId() {
        return oreId;
    }

    @Override
    protected boolean equalsSameClass(GTRecipeLookupIngredient other) {
        return oreId == ((GTOreDictLookupIngredient) other).oreId;
    }
}
