package gregtech.api.recipe.maps;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Special Class for Recycler Recipe handling.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecyclerBackend extends NonGTBackend {

    public RecyclerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    protected GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe) {
        if (items.length == 0 || items[0] == null) {
            return null;
        }
        if (cachedRecipe != null && cachedRecipe.isRecipeInputEqual(false, true, fluids, items)) {
            return cachedRecipe;
        }
        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, items[0]));
        ItemStack output = GTModHandler.getRecyclerOutput(items[0], 0);
        if (output != null) {
            builder.itemOutputs(output)
                .outputChances(1250);
        }
        return builder.duration(45)
            .eut(1)
            .noOptimize()
            .build()
            .orElse(null);
    }

    @Override
    public boolean containsInput(ItemStack item) {
        return GTModHandler.getRecyclerOutput(item, 0) != null;
    }
}
