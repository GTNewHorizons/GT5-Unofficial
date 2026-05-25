package gregtech.test;

import static net.minecraft.init.Items.iron_ingot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.junit.jupiter.api.Test;

import gregtech.api.recipe.lookup.GTOreDictLookupIngredient;

class GTRecipeLookupIngredientTest {

    static AtomicInteger oreDictIds = new AtomicInteger();

    @Test
    void oreDictFromRuntimeReturnsOneKeyPerRuntimeOreId() {
        String oreName = "lookupIngredientFunctionalOre" + oreDictIds.incrementAndGet();
        ItemStack stack = new ItemStack(iron_ingot, 1, 0);
        OreDictionary.registerOre(oreName, stack);
        GTOreDictLookupIngredient recipeKey = new GTOreDictLookupIngredient(OreDictionary.getOreID(oreName));

        List<GTOreDictLookupIngredient> runtimeKeys = GTOreDictLookupIngredient.fromRuntime(stack);

        assertTrue(runtimeKeys.contains(recipeKey));
        for (GTOreDictLookupIngredient runtimeKey : runtimeKeys) {
            if (runtimeKey.equals(recipeKey)) {
                assertEquals(recipeKey.hashCode(), runtimeKey.hashCode());
            }
        }
    }
}
