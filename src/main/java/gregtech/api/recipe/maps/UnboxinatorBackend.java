package gregtech.api.recipe.maps;

import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;

import java.util.function.Predicate;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class UnboxinatorBackend extends RecipeMapBackend {

    public UnboxinatorBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected FindRecipeResult findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        Predicate<GT_Recipe> recipeValidator) {
        if (items.length == 0 || !ItemList.IC2_Scrapbox.isStackEqual(items[0], false, true)) {
            return NOT_FOUND;
        }

        ItemStack output = GT_ModHandler.getRandomScrapboxDrop();
        if (output == null) {
            return NOT_FOUND;
        }
        return GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Scrapbox.get(1))
            .itemOutputs(output)
            .duration(16)
            .eut(1)
            // It is not allowed to be buffered due to the random Output
            .noBuffer()
            // Due to its randomness it is not good if there are Items in the Output Slot, because those Items could
            // manipulate the outcome.
            .needsEmptyOutput()
            .buildAndGetResult(recipeValidator);
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return ItemList.IC2_Scrapbox.isStackEqual(item, false, true) || super.containsInput(item);
    }
}
