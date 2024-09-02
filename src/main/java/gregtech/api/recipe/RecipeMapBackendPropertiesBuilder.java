package gregtech.api.recipe;

import static gregtech.api.util.GTRecipeMapUtil.buildOrEmpty;

import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Iterables;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Builder class for {@link RecipeMapBackendProperties}.
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class RecipeMapBackendPropertiesBuilder {

    private int minItemInputs;
    private int minFluidInputs;

    private boolean specialSlotSensitive;

    private boolean disableOptimize;

    private Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter = this::defaultBuildRecipe;

    @Nullable
    private Function<? super GTRecipe, ? extends GTRecipe> recipeTransformer;

    RecipeMapBackendPropertiesBuilder() {}

    RecipeMapBackendProperties build() {
        return new RecipeMapBackendProperties(
            minItemInputs,
            minFluidInputs,
            specialSlotSensitive,
            disableOptimize,
            recipeEmitter,
            recipeTransformer);
    }

    public RecipeMapBackendPropertiesBuilder minItemInputs(int minItemInputs) {
        this.minItemInputs = minItemInputs;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder minFluidInputs(int minFluidInputs) {
        this.minFluidInputs = minFluidInputs;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder specialSlotSensitive() {
        this.specialSlotSensitive = true;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder disableOptimize() {
        this.disableOptimize = true;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder recipeEmitter(
        Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter) {
        this.recipeEmitter = recipeEmitter;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder combineRecipeEmitter(
        Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> func) {
        // move recipeEmitter to local variable, so lambda capture the function itself instead of this
        Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> cur = this.recipeEmitter;
        return recipeEmitter(b -> Iterables.concat(cur.apply(b), func.apply(b)));
    }

    public RecipeMapBackendPropertiesBuilder recipeTransformer(
        Function<? super GTRecipe, ? extends GTRecipe> recipeTransformer) {
        this.recipeTransformer = recipeTransformer;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder chainRecipeTransformer(
        Function<? super GTRecipe, ? extends GTRecipe> func) {
        this.recipeTransformer = this.recipeTransformer == null ? func : this.recipeTransformer.andThen(func);
        return this;
    }

    private Iterable<? extends GTRecipe> defaultBuildRecipe(GTRecipeBuilder builder) {
        // TODO sensible validation
        GTRecipeBuilder b = builder;
        if (disableOptimize && builder.isOptimize()) {
            b = copy(builder, b).noOptimize();
        }
        return buildOrEmpty(b);
    }

    private static GTRecipeBuilder copy(GTRecipeBuilder original, GTRecipeBuilder b) {
        return b == original ? b.copy() : b;
    }
}
