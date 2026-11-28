package gregtech.common.ec.multiblock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.common.ec.multiblock.internal.BaseRecipeLogicComponent;

public class CustomRecipeLogicComponent extends BaseRecipeLogicComponent {

    public CustomRecipeLogicComponent(@NotNull ICustomRecipeEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @FunctionalInterface
    public interface ICustomRecipeEvaluator {

        @NotNull
        CheckRecipeResult evaluateRecipe(@NotNull CustomRecipeLogicComponent recipeLogic);
    }

    private final @NotNull ICustomRecipeEvaluator evaluator;
    private @Nullable RecipeLogicComponent.IRecipeMapProvider recipeMapProvider;

    public CustomRecipeLogicComponent setRecipeMapProvider(
        @Nullable RecipeLogicComponent.IRecipeMapProvider recipeMapProvider) {
        this.recipeMapProvider = recipeMapProvider;
        return this;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        currentParallels = 1;
        var result = evaluator.evaluateRecipe(this);
        updateSlots();
        return result;
    }

    @Override
    public @Nullable RecipeMap<?> getRecipeMap() {
        return recipeMapProvider != null ? recipeMapProvider.getRecipeMap() : null;
    }

    int currentParallels = 1;

    @Override
    protected int getCurrentParallels() {
        return currentParallels;
    }

    public void setCurrentParallels(int parallels) {
        currentParallels = parallels;
    }

    @Override
    public long getMaxAllowedRecipeEUt() {
        return -1;
    }
}
