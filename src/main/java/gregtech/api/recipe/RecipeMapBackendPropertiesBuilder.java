package gregtech.api.recipe;

import static gregtech.api.util.GTRecipeMapUtil.buildOrEmpty;

import java.util.function.Consumer;
import java.util.function.Function;

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

    private Consumer<? super GTRecipeBuilder> builderTransformer;

    private Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter = this::defaultBuildRecipe;

    private Consumer<? super GTRecipe> recipeTransformer;

    RecipeMapBackendPropertiesBuilder() {}

    RecipeMapBackendProperties build() {
        return new RecipeMapBackendProperties(
            minItemInputs,
            minFluidInputs,
            specialSlotSensitive,
            recipeEmitter,
            builderTransformer,
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

    public RecipeMapBackendPropertiesBuilder builderTransformer(Consumer<? super GTRecipeBuilder> builderTransformer) {
        if (this.builderTransformer == null) {
            this.builderTransformer = builderTransformer;
        } else {
            Consumer<? super GTRecipeBuilder> t = this.builderTransformer;
            this.builderTransformer = b -> {
                t.accept(b);
                builderTransformer.accept(b);
            };
        }
        return this;
    }

    private Iterable<? extends GTRecipe> defaultBuildRecipe(GTRecipeBuilder builder) {
        // TODO sensible validation
        GTRecipeBuilder b = builder;
        b = copy(builder, b);
        return buildOrEmpty(b);
    }

    private static GTRecipeBuilder copy(GTRecipeBuilder original, GTRecipeBuilder b) {
        return b == original ? b.copy() : b;
    }

    public RecipeMapBackendPropertiesBuilder recipeTransformer(Consumer<? super GTRecipe> recipeTransformer) {
        if (this.recipeTransformer == null) {
            this.recipeTransformer = recipeTransformer;
        } else {
            Consumer<? super GTRecipe> t = this.recipeTransformer;
            this.recipeTransformer = r -> {
                t.accept(r);
                recipeTransformer.accept(r);
            };
        }
        return this;
    }
}
