package gregtech.api.recipe;

import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;
import static gregtech.api.recipe.check.FindRecipeResult.ofSuccess;
import static gregtech.api.util.GT_RecipeBuilder.handleInvalidRecipe;
import static gregtech.api.util.GT_RecipeBuilder.handleRecipeCollision;
import static gregtech.api.util.GT_Utility.areStacksEqualOrNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Unmodifiable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Responsible for recipe addition / search for recipemap.
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeMapBackend {

    private static final Predicate<GT_Recipe> ALWAYS = r -> true;

    private RecipeMap<?> recipeMap;

    /**
     * Recipe index based on items.
     */
    private final SetMultimap<GT_ItemStack, GT_Recipe> itemIndex = HashMultimap.create();
    /**
     * Recipe index based on fluids.
     */
    private final SetMultimap<String, GT_Recipe> fluidIndex = HashMultimap.create();

    /**
     * All the recipes belonging to this backend, indexed by recipe category.
     */
    private final Map<RecipeCategory, Collection<GT_Recipe>> recipesByCategory = new HashMap<>();

    /**
     * List of recipemaps that also receive recipe addition from this backend.
     */
    private final List<IRecipeMap> downstreams = new ArrayList<>(0);

    /**
     * All the properties specific to this backend.
     */
    protected final RecipeMapBackendProperties properties;

    public RecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        this.properties = propertiesBuilder.build();
        GregTech_API.itemStackMultiMaps.add(itemIndex);
    }

    void setRecipeMap(RecipeMap<?> recipeMap) {
        this.recipeMap = recipeMap;
    }

    /**
     * @return Properties specific to this backend.
     */
    public RecipeMapBackendProperties getProperties() {
        return properties;
    }

    /**
     * @return All the recipes belonging to this backend. Returned collection is immutable,
     *         use {@link #compileRecipe} to add / {@link #removeRecipes} to remove.
     */
    @Unmodifiable
    public Collection<GT_Recipe> getAllRecipes() {
        return Collections.unmodifiableCollection(allRecipes());
    }

    /**
     * @return Raw recipe list
     */
    private Collection<GT_Recipe> allRecipes() {
        return recipesByCategory.values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @return All the recipes belonging to this backend, indexed by recipe category.
     */
    @Unmodifiable
    public Collection<GT_Recipe> getRecipesByCategory(RecipeCategory recipeCategory) {
        return Collections
            .unmodifiableCollection(recipesByCategory.getOrDefault(recipeCategory, Collections.emptyList()));
    }

    @Unmodifiable
    public Map<RecipeCategory, Collection<GT_Recipe>> getRecipeCategoryMap() {
        return Collections.unmodifiableMap(recipesByCategory);
    }

    // region add recipe

    /**
     * Adds the supplied to recipe without any check.
     *
     * @return Supplied recipe.
     */
    public GT_Recipe compileRecipe(GT_Recipe recipe) {
        if (recipe.getRecipeCategory() == null) {
            recipe.setRecipeCategory(recipeMap.getDefaultRecipeCategory());
        }
        recipesByCategory.computeIfAbsent(recipe.getRecipeCategory(), v -> new ArrayList<>())
            .add(recipe);
        for (FluidStack fluid : recipe.mFluidInputs) {
            if (fluid == null) continue;
            fluidIndex.put(
                fluid.getFluid()
                    .getName(),
                recipe);
        }
        return addToItemMap(recipe);
    }

    /**
     * Adds the supplied recipe to the item cache.
     */
    protected GT_Recipe addToItemMap(GT_Recipe recipe) {
        for (ItemStack item : recipe.mInputs) {
            if (item == null) continue;
            itemIndex.put(new GT_ItemStack(item), recipe);
        }
        return recipe;
    }

    /**
     * Builds recipe from supplied recipe builder and adds it.
     */
    protected Collection<GT_Recipe> doAdd(GT_RecipeBuilder builder) {
        Iterable<? extends GT_Recipe> recipes = properties.recipeEmitter.apply(builder);
        Collection<GT_Recipe> ret = new ArrayList<>();
        for (GT_Recipe recipe : recipes) {
            if (properties.recipeConfigCategory != null) {
                assert properties.recipeConfigKeyConvertor != null;
                String configKey = properties.recipeConfigKeyConvertor.apply(recipe);
                if (configKey != null && (recipe.mDuration = GregTech_API.sRecipeFile
                    .get(properties.recipeConfigCategory, configKey, recipe.mDuration)) <= 0) {
                    continue;
                }
            }
            if (recipe.mFluidInputs.length < properties.minFluidInputs
                || recipe.mInputs.length < properties.minItemInputs) {
                return Collections.emptyList();
            }
            if (properties.recipeTransformer != null) {
                recipe = properties.recipeTransformer.apply(recipe);
            }
            if (recipe == null) continue;
            if (builder.isCheckForCollision() && checkCollision(recipe)) {
                handleCollision(recipe);
                continue;
            }
            if (recipe.getRecipeCategory() != null && recipe.getRecipeCategory().recipeMap != this.recipeMap) {
                handleInvalidRecipe();
                continue;
            }
            ret.add(compileRecipe(recipe));
        }
        if (!ret.isEmpty()) {
            builder.clearInvalid();
            for (IRecipeMap downstream : downstreams) {
                downstream.doAdd(builder);
            }
        }
        return ret;
    }

    private void handleCollision(GT_Recipe recipe) {
        StringBuilder errorInfo = new StringBuilder();
        boolean hasAnEntry = false;
        for (FluidStack fluid : recipe.mFluidInputs) {
            if (fluid == null) {
                continue;
            }
            String s = fluid.getLocalizedName();
            if (s == null) {
                continue;
            }
            if (hasAnEntry) {
                errorInfo.append("+")
                    .append(s);
            } else {
                errorInfo.append(s);
            }
            hasAnEntry = true;
        }
        for (ItemStack item : recipe.mInputs) {
            if (item == null) {
                continue;
            }
            String itemName = item.getDisplayName();
            if (hasAnEntry) {
                errorInfo.append("+")
                    .append(itemName);
            } else {
                errorInfo.append(itemName);
            }
            hasAnEntry = true;
        }
        handleRecipeCollision(errorInfo.toString());
    }

    void addDownstream(IRecipeMap downstream) {
        downstreams.add(downstream);
    }

    /**
     * Removes supplied recipes from recipe list. Do not use unless absolute necessity!
     */
    public void removeRecipes(Collection<? extends GT_Recipe> recipesToRemove) {
        for (Collection<GT_Recipe> recipes : recipesByCategory.values()) {
            recipes.removeAll(recipesToRemove);
        }
        for (GT_ItemStack key : new HashMap<>(itemIndex.asMap()).keySet()) {
            itemIndex.get(key)
                .removeAll(recipesToRemove);
        }
        for (String key : new HashMap<>(fluidIndex.asMap()).keySet()) {
            fluidIndex.get(key)
                .removeAll(recipesToRemove);
        }
    }

    /**
     * Removes supplied recipe from recipe list. Do not use unless absolute necessity!
     */
    public void removeRecipe(GT_Recipe recipe) {
        removeRecipes(Collections.singleton(recipe));
    }

    /**
     * If you want to shoot your foot...
     */
    public void clearRecipes() {
        recipesByCategory.clear();
    }

    // endregion

    /**
     * Re-unificates all the items present in recipes. Also reflects recipe removals.
     */
    public void reInit() {
        itemIndex.clear();
        for (GT_Recipe recipe : allRecipes()) {
            GT_OreDictUnificator.setStackArray(true, recipe.mInputs);
            GT_OreDictUnificator.setStackArray(true, recipe.mOutputs);
            addToItemMap(recipe);
        }
    }

    /**
     * @return If supplied item is a valid input for any of the recipes
     */
    public boolean containsInput(ItemStack item) {
        return itemIndex.containsKey(new GT_ItemStack(item)) || itemIndex.containsKey(new GT_ItemStack(item, true));
    }

    /**
     * @return If supplied fluid is a valid input for any of the recipes
     */
    public boolean containsInput(Fluid fluid) {
        return fluidIndex.containsKey(fluid.getName());
    }

    // region find recipe

    /**
     * Finds a matching recipe.
     * <p>
     * This method is marked as final and package-private, so that any change to it won't break subclasses.
     * Use {@link #overwriteFindRecipe}, {@link #modifyFoundRecipe} or {@link #findFallback} to tweak behavior.
     */
    final FindRecipeResult findRecipeWithResult(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        Predicate<GT_Recipe> recipeValidator, @Nullable GT_Recipe cachedRecipe, boolean notUnificated,
        boolean dontCheckStackSizes) {
        if (doesOverwriteFindRecipe()) {
            return overwriteFindRecipe(items, fluids, specialSlot, recipeValidator, cachedRecipe);
        }

        FindRecipeResult result = doFind(
            items,
            fluids,
            specialSlot,
            recipeValidator,
            cachedRecipe,
            notUnificated,
            dontCheckStackSizes,
            false);
        if (result.isSuccessful()) {
            return modifyFoundRecipe(result, items, fluids, specialSlot);
        }
        return findFallback(items, fluids, specialSlot, recipeValidator);
    }

    /**
     * Checks if given recipe conflicts with already registered recipes.
     *
     * @return True if collision is found.
     */
    public boolean checkCollision(GT_Recipe recipe) {
        return doFind(recipe.mInputs, recipe.mFluidInputs, null, ALWAYS, null, false, true, true).isSuccessful();
    }

    /**
     * Overwrites {@link #doFind} method. Also override {@link #doesOverwriteFindRecipe} to make it work.
     */
    protected FindRecipeResult overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, Predicate<GT_Recipe> recipeValidator, @Nullable GT_Recipe cachedRecipe) {
        return NOT_FOUND;
    }

    /**
     * @return Whether to use {@link #overwriteFindRecipe} for finding recipe.
     */
    protected boolean doesOverwriteFindRecipe() {
        return false;
    }

    /**
     * Modifies successfully found recipe.
     */
    protected FindRecipeResult modifyFoundRecipe(FindRecipeResult result, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        return result;
    }

    /**
     * Called when {@link #doFind} cannot find recipe.
     */
    protected FindRecipeResult findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        Predicate<GT_Recipe> recipeValidator) {
        return NOT_FOUND;
    }

    /**
     * Actual logic to find recipe.
     *
     * @param forCollisionCheck If this method is called to check collision with already registered recipes.
     */
    private FindRecipeResult doFind(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        Predicate<GT_Recipe> recipeValidator, @Nullable GT_Recipe cachedRecipe, boolean notUnificated,
        boolean dontCheckStackSizes, boolean forCollisionCheck) {
        if (recipesByCategory.isEmpty()) {
            return NOT_FOUND;
        }

        // Some recipe classes require a certain amount of inputs of certain kinds. Like "at least 1 fluid + 1 item"
        // or "at least 2 items" before they start searching for recipes.
        // This improves performance massively, especially when people leave things like programmed circuits,
        // molds or shapes in their machines.
        // For checking collision, we assume min inputs check already has been passed as of building the recipe.
        if (!forCollisionCheck) {
            if (properties.minFluidInputs > 0) {
                int count = 0;
                for (FluidStack fluid : fluids) if (fluid != null) count++;
                if (count < properties.minFluidInputs) {
                    return NOT_FOUND;
                }
            }
            if (properties.minItemInputs > 0) {
                int count = 0;
                for (ItemStack item : items) if (item != null) count++;
                if (count < properties.minItemInputs) {
                    return NOT_FOUND;
                }
            }
        }

        // Unification happens here in case the item input isn't already unificated.
        if (notUnificated) {
            items = GT_OreDictUnificator.getStackArray(true, (Object[]) items);
        }

        // Check the recipe which has been used last time in order to not have to search for it again, if possible.
        if (cachedRecipe != null) {
            if (!cachedRecipe.mFakeRecipe && cachedRecipe.mCanBeBuffered
                && cachedRecipe.isRecipeInputEqual(false, dontCheckStackSizes, fluids, items)) {
                if (!properties.specialSlotSensitive
                    || areStacksEqualOrNull((ItemStack) cachedRecipe.mSpecialItems, specialSlot)) {
                    if (cachedRecipe.mEnabled && recipeValidator.test(cachedRecipe)) {
                        return ofSuccess(cachedRecipe);
                    }
                }
            }
        }

        // Now look for the recipes inside the item index, but only when the recipes actually can have items inputs.
        if (!itemIndex.isEmpty()) {
            for (ItemStack item : items) {
                if (item == null) continue;
                Collection<GT_Recipe> nonWildcardRecipes = itemIndex.get(new GT_ItemStack(item));
                if (nonWildcardRecipes != null) {
                    Optional<GT_Recipe> recipeCandidate = loopRecipes(
                        nonWildcardRecipes,
                        items,
                        fluids,
                        specialSlot,
                        recipeValidator,
                        dontCheckStackSizes);
                    if (recipeCandidate.isPresent()) {
                        return ofSuccess(recipeCandidate.get());
                    }
                }
                Collection<GT_Recipe> wildcardRecipes = itemIndex.get(new GT_ItemStack(item, true));
                if (wildcardRecipes != null) {
                    Optional<GT_Recipe> recipeCandidate = loopRecipes(
                        wildcardRecipes,
                        items,
                        fluids,
                        specialSlot,
                        recipeValidator,
                        dontCheckStackSizes);
                    if (recipeCandidate.isPresent()) {
                        return ofSuccess(recipeCandidate.get());
                    }
                }
            }
        }

        // If the minimum amount of items required for the recipes is 0, then it could match to fluid-only recipes,
        // so check fluid index too.
        if (properties.minItemInputs == 0) {
            for (FluidStack fluid : fluids) {
                if (fluid == null) continue;
                Collection<GT_Recipe> recipes = fluidIndex.get(
                    fluid.getFluid()
                        .getName());
                if (recipes != null) {
                    Optional<GT_Recipe> recipeCandidate = loopRecipes(
                        recipes,
                        items,
                        fluids,
                        specialSlot,
                        recipeValidator,
                        dontCheckStackSizes);
                    if (recipeCandidate.isPresent()) {
                        return ofSuccess(recipeCandidate.get());
                    }
                }
            }
        }

        // And nothing has been found.
        return NOT_FOUND;
    }

    private Optional<GT_Recipe> loopRecipes(Collection<GT_Recipe> recipes, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, Predicate<GT_Recipe> recipeValidator, boolean dontCheckStackSizes) {
        for (GT_Recipe recipe : recipes) {
            if (!recipe.mFakeRecipe && recipe.isRecipeInputEqual(false, dontCheckStackSizes, fluids, items)) {
                if (!properties.specialSlotSensitive
                    || areStacksEqualOrNull((ItemStack) recipe.mSpecialItems, specialSlot)) {
                    if (recipe.mEnabled && recipeValidator.test(recipe)) {
                        return Optional.of(recipe);
                    }
                }
            }
        }
        return Optional.empty();
    }

    // endregion

    @FunctionalInterface
    public interface BackendCreator<B extends RecipeMapBackend> {

        /**
         * @see RecipeMapBackend#RecipeMapBackend
         */
        B create(RecipeMapBackendPropertiesBuilder propertiesBuilder);
    }
}
