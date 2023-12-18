package gregtech.api.recipe;

import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;

// spotless:off spotless likes formatting @code to &#64;code
/**
 * Helper class for searching recipe. Retrieve instance with {@link RecipeMap#findRecipeQuery}.
 * <p>
 * It features fluent API, so you can find recipes like this:
 *
 * <pre>
 * {@code
 *     GT_Recipe recipe = recipeMap.findRecipeQuery()
 *         .items(inputItems)
 *         .fluids(inputFluids)
 *         .find();
 * }
 * </pre>
 */
// spotless:on
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class FindRecipeQuery {

    private static final Predicate<GT_Recipe> ALWAYS = r -> true;

    private final RecipeMap<?> recipeMap;

    @Nullable
    private ItemStack[] items;
    @Nullable
    private FluidStack[] fluids;
    @Nullable
    private ItemStack specialSlot;
    private Predicate<GT_Recipe> filter = ALWAYS;
    private long voltage = Integer.MAX_VALUE;
    @Nullable
    private GT_Recipe cachedRecipe;
    private boolean notUnificated;
    private boolean dontCheckStackSizes;
    private boolean forCollisionCheck;

    FindRecipeQuery(RecipeMap<?> recipeMap) {
        this.recipeMap = recipeMap;
    }

    // region executors

    /**
     * @return The first matched recipe, or null if not found.
     */
    @Nullable
    public GT_Recipe find() {
        return findAll().findFirst()
            .orElse(null);
    }

    /**
     * @return All the matched recipes in the form of Stream.
     */
    public Stream<GT_Recipe> findAll() {
        if (items == null) {
            items = new ItemStack[0];
        }
        if (fluids == null) {
            fluids = new FluidStack[0];
        }

        return recipeMap.getBackend()
            .matchRecipeStream(
                items,
                fluids,
                specialSlot,
                cachedRecipe,
                notUnificated,
                dontCheckStackSizes,
                forCollisionCheck)
            .filter(recipe -> voltage * recipeMap.getAmperage() >= recipe.mEUt && filter.test(recipe));
    }

    /**
     * Checks if given inputs conflict with already registered recipes.
     *
     * @return True if collision is found.
     */
    public boolean checkCollision() {
        dontCheckStackSizes = true;
        forCollisionCheck = true;
        return findAll().findAny()
            .isPresent();
    }

    // endregion

    // region setters

    /**
     * @param items Item inputs.
     */
    public FindRecipeQuery items(@Nullable ItemStack... items) {
        this.items = items;
        return this;
    }

    /**
     * @param fluids Fluid inputs.
     */
    public FindRecipeQuery fluids(@Nullable FluidStack... fluids) {
        this.fluids = fluids;
        return this;
    }

    /**
     * @param specialSlot Content of the special slot. Normal recipemaps don't need this, but some do.
     *                    Set {@link RecipeMapBuilder#specialSlotSensitive} to make it actually functional.
     *                    Alternatively overriding {@link RecipeMapBackend#filterFindRecipe} will also work.
     */
    public FindRecipeQuery specialSlot(@Nullable ItemStack specialSlot) {
        this.specialSlot = specialSlot;
        return this;
    }

    /**
     * @param filter Matched recipe will be tested by this function. If it returns false, the query will attempt to
     *               find next recipe.
     */
    public FindRecipeQuery filter(Predicate<GT_Recipe> filter) {
        this.filter = filter;
        return this;
    }

    /**
     * @param voltage Recipes that exceed this voltage won't match. It will be automatically multiplied by amperage
     *                of the recipemap.
     */
    public FindRecipeQuery voltage(long voltage) {
        this.voltage = voltage;
        return this;
    }

    /**
     * @param cachedRecipe If this is not null, the query tests it before all other recipes.
     */
    public FindRecipeQuery cachedRecipe(@Nullable GT_Recipe cachedRecipe) {
        this.cachedRecipe = cachedRecipe;
        return this;
    }

    /**
     * @param notUnificated If this is set to true, item inputs will be unificated.
     */
    public FindRecipeQuery notUnificated(boolean notUnificated) {
        this.notUnificated = notUnificated;
        return this;
    }

    /**
     * @param dontCheckStackSizes If this is set to true, the query won't check item count and fluid amount
     *                            for the matched recipe.
     */
    public FindRecipeQuery dontCheckStackSizes(boolean dontCheckStackSizes) {
        this.dontCheckStackSizes = dontCheckStackSizes;
        return this;
    }

    // endregion
}
