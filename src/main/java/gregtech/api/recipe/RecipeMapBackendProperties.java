package gregtech.api.recipe;

import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Data object to store properties used for {@link RecipeMapBackend}. Use {@link #builder()} for creation.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class RecipeMapBackendProperties {

    static RecipeMapBackendPropertiesBuilder builder() {
        return new RecipeMapBackendPropertiesBuilder();
    }

    /**
     * Minimum amount of item inputs required for the recipes.
     */
    public final int minItemInputs;
    /**
     * Minimum amount of fluid inputs required for the recipes.
     */
    public final int minFluidInputs;
    /**
     * Whether to allow recipes which only use a programmed circuit, and no item/fluid inputs. If this is false, such
     * recipes are optimized away.
     */
    public boolean allowCircuitOnly;

    /**
     * Whether this backend should check for equality of special slot when searching recipe.
     */
    public final boolean specialSlotSensitive;

    /**
     * If recipe builder should stop optimizing inputs.
     */
    public final boolean disableOptimize;

    /**
     * Changes how recipes are emitted by a particular recipe builder.
     */
    public final Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter;

    /**
     * Runs a custom hook on all recipes added <b>via builder</b>.
     */
    @Nullable
    public final Function<? super GTRecipe, ? extends GTRecipe> recipeTransformer;

    RecipeMapBackendProperties(int minItemInputs, int minFluidInputs, boolean allowCircuitOnly,
        boolean specialSlotSensitive, boolean disableOptimize,
        Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter,
        @Nullable Function<? super GTRecipe, ? extends GTRecipe> recipeTransformer) {
        if (minItemInputs < 0 || minFluidInputs < 0) {
            throw new IllegalArgumentException("minItemInputs and minFluidInputs cannot be negative");
        }
        this.minItemInputs = minItemInputs;
        this.minFluidInputs = minFluidInputs;
        this.allowCircuitOnly = allowCircuitOnly;
        this.specialSlotSensitive = specialSlotSensitive;
        this.disableOptimize = disableOptimize;
        this.recipeEmitter = recipeEmitter;
        this.recipeTransformer = recipeTransformer;
    }
}
