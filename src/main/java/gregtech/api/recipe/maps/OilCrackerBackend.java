package gregtech.api.recipe.maps;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OilCrackerBackend extends RecipeMapBackend {

    private final Set<String> validCatalystFluidNames = new HashSet<>();

    public OilCrackerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    @Override
    public GT_Recipe compileRecipe(GT_Recipe recipe) {
        super.compileRecipe(recipe);
        if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 1 && recipe.mFluidInputs[1] != null) {
            validCatalystFluidNames.add(
                recipe.mFluidInputs[1].getFluid()
                    .getName());
        }
        return recipe;
    }

    public boolean isValidCatalystFluid(FluidStack fluid) {
        return validCatalystFluidNames.contains(
            fluid.getFluid()
                .getName());
    }
}
