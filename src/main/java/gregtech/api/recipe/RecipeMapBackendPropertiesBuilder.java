package gregtech.api.recipe;

import static gregtech.api.util.GT_RecipeMapUtil.buildOrEmpty;

import java.util.function.Function;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Iterables;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
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

    private Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> recipeEmitter = this::defaultBuildRecipe;

    @Nullable
    private Function<? super GT_Recipe, ? extends GT_Recipe> recipeTransformer;

    @Nullable
    private String recipeConfigCategory;
    @Nullable
    private Function<? super GT_Recipe, String> recipeConfigKeyConvertor;

    RecipeMapBackendPropertiesBuilder() {}

    RecipeMapBackendProperties build() {
        return new RecipeMapBackendProperties(
            minItemInputs,
            minFluidInputs,
            specialSlotSensitive,
            disableOptimize,
            recipeEmitter,
            recipeTransformer,
            recipeConfigCategory,
            recipeConfigKeyConvertor);
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
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> recipeEmitter) {
        this.recipeEmitter = recipeEmitter;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder combineRecipeEmitter(
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> func) {
        // move recipeEmitter to local variable, so lambda capture the function itself instead of this
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> cur = this.recipeEmitter;
        return recipeEmitter(b -> Iterables.concat(cur.apply(b), func.apply(b)));
    }

    public RecipeMapBackendPropertiesBuilder recipeTransformer(
        Function<? super GT_Recipe, ? extends GT_Recipe> recipeTransformer) {
        this.recipeTransformer = recipeTransformer;
        return this;
    }

    public RecipeMapBackendPropertiesBuilder chainRecipeTransformer(
        Function<? super GT_Recipe, ? extends GT_Recipe> func) {
        this.recipeTransformer = this.recipeTransformer == null ? func : this.recipeTransformer.andThen(func);
        return this;
    }

    public RecipeMapBackendPropertiesBuilder recipeConfigFile(String category,
        Function<? super GT_Recipe, String> keyConvertor) {
        this.recipeConfigCategory = category;
        this.recipeConfigKeyConvertor = keyConvertor;
        return this;
    }

    private Iterable<? extends GT_Recipe> defaultBuildRecipe(GT_RecipeBuilder builder) {
        // TODO sensible validation
        GT_RecipeBuilder b = builder;
        if (disableOptimize && builder.isOptimize()) {
            b = copy(builder, b).noOptimize();
        }
        return buildOrEmpty(b);
    }

    private static GT_RecipeBuilder copy(GT_RecipeBuilder original, GT_RecipeBuilder b) {
        return b == original ? b.copy() : b;
    }
}
