package gregtech.api.recipe;

import java.util.function.Consumer;
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
     * Whether this backend should check for equality of special slot when searching recipe.
     */
    public final boolean specialSlotSensitive;

    /**
     * Changes how recipes are emitted by a particular recipe builder.
     */
    public final Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter;

    @Nullable
    private Consumer<? super GTRecipeBuilder> builderTransformer;
    @Nullable
    private Consumer<? super GTRecipe> recipeTransformer;

    RecipeMapBackendProperties(int minItemInputs, int minFluidInputs, boolean specialSlotSensitive,
        Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter,
        @Nullable Consumer<? super GTRecipeBuilder> builderTransformer,
        @Nullable Consumer<? super GTRecipe> recipeTransformer) {
        if (minItemInputs < 0 || minFluidInputs < 0) {
            throw new IllegalArgumentException("minItemInputs and minFluidInputs cannot be negative");
        }
        this.minItemInputs = minItemInputs;
        this.minFluidInputs = minFluidInputs;
        this.specialSlotSensitive = specialSlotSensitive;
        this.recipeEmitter = recipeEmitter;
        this.builderTransformer = builderTransformer;
        this.recipeTransformer = recipeTransformer;
    }

    public void appendBuilderTransformer(Consumer<? super GTRecipeBuilder> builderTransformer) {
        if (this.builderTransformer == null) {
            this.builderTransformer = builderTransformer;
        } else {
            Consumer<? super GTRecipeBuilder> t = this.builderTransformer;
            this.builderTransformer = b -> {
                t.accept(b);
                builderTransformer.accept(b);
            };
        }
    }

    public void appendRecipeTransformer(Consumer<? super GTRecipe> recipeTransformer) {
        if (this.recipeTransformer == null) {
            this.recipeTransformer = recipeTransformer;
        } else {
            Consumer<? super GTRecipe> t = this.recipeTransformer;
            this.recipeTransformer = r -> {
                t.accept(r);
                recipeTransformer.accept(r);
            };
        }
    }

    public void transformBuilder(GTRecipeBuilder builder) {
        if (builderTransformer != null) {
            builderTransformer.accept(builder);
        }
    }

    public void transformRecipe(GTRecipe recipe) {
        if (recipeTransformer != null) {
            recipeTransformer.accept(recipe);
        }
    }
}
