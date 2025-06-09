package gregtech.api.recipe;

import static gregtech.api.util.GTRecipeBuilder.ENABLE_COLLISION_CHECK;
import static gregtech.api.util.GTRecipeBuilder.handleInvalidRecipe;
import static gregtech.api.util.GTRecipeBuilder.handleInvalidRecipeLowFluids;
import static gregtech.api.util.GTRecipeBuilder.handleInvalidRecipeLowItems;
import static gregtech.api.util.GTRecipeBuilder.handleRecipeCollision;
import static gregtech.api.util.GTUtility.areStacksEqualOrNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Unmodifiable;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.objects.FastGTItemStack;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.iterators.Generator;
import gregtech.api.objects.iterators.MergedIterator;
import gregtech.api.objects.iterators.NonNullGenerator;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import it.unimi.dsi.fastutil.objects.ObjectIterators;

/**
 * Responsible for recipe addition / search for recipemap.
 * <p>
 * In order to bind custom backend to recipemap, use {@link RecipeMapBuilder#of(String, BackendCreator)}.
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RecipeMapBackend {

    private RecipeMap<?> recipeMap;

    /**
     * Recipe index based on items.
     */
    private final SetMultimap<GTItemStack, GTRecipe> itemIndex = LinkedHashMultimap.create();
    /**
     * Recipe index based on fluids.
     */
    private final SetMultimap<String, GTRecipe> fluidIndex = LinkedHashMultimap.create();

    /**
     * All the recipes belonging to this backend, indexed by recipe category.
     */
    private final Map<RecipeCategory, Collection<GTRecipe>> recipesByCategory = new HashMap<>();

    /**
     * All the properties specific to this backend.
     */
    protected final RecipeMapBackendProperties properties;

    public RecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        this.properties = propertiesBuilder.build();
        GregTechAPI.itemStackMultiMaps.add(itemIndex);
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
    public Collection<GTRecipe> getAllRecipes() {
        return Collections.unmodifiableCollection(allRecipes());
    }

    /**
     * @return Raw recipe list
     */
    private Collection<GTRecipe> allRecipes() {
        return recipesByCategory.values()
            .stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @return All the recipes belonging to this backend, indexed by recipe category.
     */
    @Unmodifiable
    public Collection<GTRecipe> getRecipesByCategory(RecipeCategory recipeCategory) {
        return Collections
            .unmodifiableCollection(recipesByCategory.getOrDefault(recipeCategory, Collections.emptyList()));
    }

    @Unmodifiable
    public Map<RecipeCategory, Collection<GTRecipe>> getRecipeCategoryMap() {
        return Collections.unmodifiableMap(recipesByCategory);
    }

    // region add recipe

    /**
     * Adds the supplied recipe to the recipe list and index, without any check.
     *
     * @return Supplied recipe.
     */
    public GTRecipe compileRecipe(GTRecipe recipe) {
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
    protected GTRecipe addToItemMap(GTRecipe recipe) {
        for (ItemStack item : recipe.mInputs) {
            if (item == null) continue;
            itemIndex.put(new GTItemStack(item), recipe);
        }
        if (recipe instanceof GTRecipe.GTRecipe_WithAlt recipeWithAlt) {
            for (ItemStack[] itemStacks : recipeWithAlt.mOreDictAlt) {
                if (itemStacks == null) continue;
                for (ItemStack item : itemStacks) {
                    if (item == null) continue;
                    itemIndex.put(new GTItemStack(item), recipe);
                }
            }
        }
        return recipe;
    }

    /**
     * Builds recipe from supplied recipe builder and adds it.
     */
    protected Collection<GTRecipe> doAdd(GTRecipeBuilder builder) {
        properties.transformBuilder(builder);
        Iterable<? extends GTRecipe> recipes = properties.recipeEmitter.apply(builder);
        Collection<GTRecipe> ret = new ArrayList<>();
        for (GTRecipe recipe : recipes) {
            if (recipe.mInputs.length < properties.minItemInputs) {
                handleInvalidRecipeLowItems();
                return Collections.emptyList();
            }
            if (recipe.mFluidInputs.length < properties.minFluidInputs) {
                handleInvalidRecipeLowFluids();
                return Collections.emptyList();
            }
            properties.transformRecipe(recipe);
            if (builder.isCheckForCollision() && ENABLE_COLLISION_CHECK && checkCollision(recipe)) {
                handleCollision(recipe);
                continue;
            }
            if (recipe.getRecipeCategory() != null && recipe.getRecipeCategory().recipeMap != this.recipeMap) {
                handleInvalidRecipe();
                continue;
            }
            ret.add(compileRecipe(recipe));
        }
        return ret;
    }

    private void handleCollision(GTRecipe recipe) {
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

    /**
     * Removes supplied recipes from recipe list. Do not use unless absolute necessity!
     */
    public void removeRecipes(Collection<? extends GTRecipe> recipesToRemove) {
        for (Collection<GTRecipe> recipes : recipesByCategory.values()) {
            recipes.removeAll(recipesToRemove);
        }
        for (GTItemStack key : new HashMap<>(itemIndex.asMap()).keySet()) {
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
    public void removeRecipe(GTRecipe recipe) {
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
        for (GTRecipe recipe : allRecipes()) {
            GTOreDictUnificator.setStackArray(true, true, recipe.mInputs);
            GTOreDictUnificator.setStackArray(true, true, recipe.mOutputs);
            addToItemMap(recipe);
        }
    }

    /**
     * @return If supplied item is a valid input for any of the recipes
     */
    public boolean containsInput(ItemStack item) {
        return itemIndex.containsKey(new GTItemStack(item)) || itemIndex.containsKey(new GTItemStack(item, true));
    }

    /**
     * @return If supplied fluid is a valid input for any of the recipes
     */
    public boolean containsInput(Fluid fluid) {
        return fluidIndex.containsKey(fluid.getName());
    }

    // region find recipe

    /**
     * Checks if given recipe conflicts with already registered recipes.
     *
     * @return True if collision is found.
     */
    boolean checkCollision(GTRecipe recipe) {
        return matchRecipeStream(recipe.mInputs, recipe.mFluidInputs, null, null, false, true, true, -1, null)
            .hasNext();
    }

    /**
     * Overwrites {@link #matchRecipeStream} method. Also override {@link #doesOverwriteFindRecipe} to make it work.
     */
    @Nullable
    protected GTRecipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe) {
        return null;
    }

    /**
     * @return Whether to use {@link #overwriteFindRecipe} for finding recipe.
     */
    protected boolean doesOverwriteFindRecipe() {
        return false;
    }

    /**
     * Modifies successfully found recipe. Make sure not to mutate the found recipe but use copy!
     */
    @Nullable
    protected GTRecipe modifyFoundRecipe(GTRecipe recipe, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        return recipe;
    }

    /**
     * Called when {@link #matchRecipeStream} cannot find recipe.
     */
    @Nullable
    protected GTRecipe findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot) {
        return null;
    }

    /**
     * Returns all the matched recipes in the form of Stream, without any additional check for matches.
     *
     * @param rawItems            Item inputs.
     * @param rawFluids           Fluid inputs.
     * @param specialSlot         Content of the special slot. Normal recipemaps don't need this, but some do.
     *                            Set {@link RecipeMapBuilder#specialSlotSensitive} to make it actually functional.
     *                            Alternatively overriding {@link #filterFindRecipe} will also work.
     * @param cachedRecipe        If this is not null, this method tests it before all other recipes.
     * @param notUnificated       If this is set to true, item inputs will be unificated.
     * @param dontCheckStackSizes If this is set to true, this method won't check item count and fluid amount
     *                            for the matched recipe.
     * @param forCollisionCheck   If this method is called to check collision with already registered recipes.
     * @return Stream of matches recipes.
     */
    Iterator<GTRecipe> matchRecipeStream(ItemStack[] rawItems, FluidStack[] rawFluids, @Nullable ItemStack specialSlot,
        @Nullable GTRecipe cachedRecipe, boolean notUnificated, boolean dontCheckStackSizes, boolean forCollisionCheck,
        long maxEUt, @Nullable Predicate<GTRecipe> filter) {

        if (doesOverwriteFindRecipe()) {
            return ObjectIterators.singleton(overwriteFindRecipe(rawItems, rawFluids, specialSlot, cachedRecipe));
        }

        if (recipesByCategory.isEmpty()) {
            return ObjectIterators.emptyIterator();
        }

        // Some recipe classes require a certain amount of inputs of certain kinds. Like "at least 1 fluid + 1 item"
        // or "at least 2 items" before they start searching for recipes.
        // This improves performance massively, especially when people leave things like programmed circuits,
        // molds or shapes in their machines.
        // For checking collision, we assume min inputs check already has been passed as of building the recipe.
        if (!forCollisionCheck) {
            if (properties.minFluidInputs > 0) {
                if (GTDataUtils.countNonNulls(rawFluids) < properties.minFluidInputs) {
                    return ObjectIterators.emptyIterator();
                }
            }
            if (properties.minItemInputs > 0) {
                if (GTDataUtils.countNonNulls(rawItems) < properties.minItemInputs) {
                    return ObjectIterators.emptyIterator();
                }
            }
        }

        rawItems = GTDataUtils.withoutNulls(rawItems);

        ItemStack[] items;
        FluidStack[] fluids = GTDataUtils.withoutNulls(rawFluids);

        // Unification happens here in case the item input isn't already unificated.
        if (notUnificated) {
            items = GTOreDictUnificator.getStackArray(true, (Object[]) rawItems);
        } else {
            items = rawItems;
        }

        // Check the recipe which has been used last time in order to not have to search for it again, if possible.
        if (cachedRecipe != null && cachedRecipe.mCanBeBuffered) {
            if (filterFindRecipe(cachedRecipe, items, fluids, specialSlot, dontCheckStackSizes)) {
                GTRecipe modified = modifyFoundRecipe(cachedRecipe, items, fluids, specialSlot);

                if (modified != null) return ObjectIterators.singleton(modified);
            }
        }

        List<Iterator<GTRecipe>> iters = new ArrayList<>();

        // Now look for the recipes inside the item index, but only when the recipes actually can have items inputs.
        if (!itemIndex.isEmpty()) {

            /*
             * What this does:
             * 1. Get the next item from the list of input items
             * 2. First, find the recipes in itemIndex with the exact item (no wildcard meta)
             * 3. Check each recipe in the returned set to see if it's valid
             * 4. If it is, then try to modify the recipe
             * 5. If the recipe was rejected by the modifier method, skip it
             * 6. Test the recipe with the given dynamic predicate
             * 7. If the recipe is valid, yield it
             * 8. Repeat 2-7 with a wildcard item (meta = GTValues.V)
             * 9. Repeat 1-8 for the next item in the list of input items
             */

            iters.add(new NonNullGenerator<>(new Generator<>() {

                private final Iterator<ItemStack> itemIter = ObjectIterators.wrap(items);

                private boolean wasExact = false;
                private Iterator<GTRecipe> recipeIter = null;

                private final FastGTItemStack key = new FastGTItemStack(Items.feather, 0);

                @Override
                public @Nullable GTRecipe next() {
                    while (true) {
                        if (this.recipeIter == null || !this.recipeIter.hasNext()) {
                            boolean doExact = !wasExact;
                            wasExact ^= true;

                            if (!itemIter.hasNext()) return null;

                            ItemStack item = itemIter.next();

                            if (item == null) continue;

                            key.item = item.getItem();
                            key.metadata = doExact ? item.itemDamage : GTValues.W;

                            Set<GTRecipe> recipeSet = itemIndex.get(key);

                            if (recipeSet == null || recipeSet.isEmpty()) continue;

                            this.recipeIter = recipeSet.iterator();
                        }

                        GTRecipe recipe = this.recipeIter.next();

                        if (maxEUt > 0 && recipe.mEUt > maxEUt) continue;

                        if (!filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes)) continue;

                        recipe = modifyFoundRecipe(recipe, items, fluids, specialSlot);

                        if (recipe == null) continue;

                        if (filter != null && !filter.test(recipe)) continue;

                        return recipe;
                    }
                }
            }));
        }

        // If the minimum amount of items required for the recipes is 0, then it could match to fluid-only recipes,
        // so check fluid index too.
        if (properties.minItemInputs == 0) {

            /*
             * What this does:
             * 1. Get the next fluid from the list of input fluids
             * 2. Find the recipes in fluidIndex with that fluid
             * 3. Check each recipe in the returned set to see if it's valid
             * 4. If it is, then try to modify the recipe
             * 5. If the recipe was rejected by the modifier method, skip it
             * 6. Test the recipe with the given dynamic predicate
             * 7. If the recipe is valid, yield it
             * 9. Repeat 1-8 for the next fluid in the list of input fluids
             */

            iters.add(new NonNullGenerator<>(new Generator<>() {

                private final Iterator<FluidStack> fluidIter = ObjectIterators.wrap(fluids);

                private Iterator<GTRecipe> recipes = null;

                @Override
                public @Nullable GTRecipe next() {
                    while (true) {
                        if (recipes == null || !recipes.hasNext()) {
                            if (!fluidIter.hasNext()) return null;

                            FluidStack fluid = fluidIter.next();

                            if (fluid == null) continue;

                            Set<GTRecipe> recipes = fluidIndex.get(
                                fluid.getFluid()
                                    .getName());

                            if (recipes == null || recipes.isEmpty()) continue;

                            this.recipes = recipes.iterator();
                        }

                        GTRecipe recipe = this.recipes.next();

                        if (maxEUt > 0 && recipe.mEUt > maxEUt) continue;

                        if (!filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes)) continue;

                        recipe = modifyFoundRecipe(recipe, items, fluids, specialSlot);

                        if (recipe == null) continue;

                        if (filter != null && !filter.test(recipe)) continue;

                        return recipe;
                    }
                }
            }));
        }

        // Lastly, find fallback.
        if (!forCollisionCheck) {
            iters.add(new NonNullGenerator<>(new Generator<>() {

                private boolean called = false;

                @Override
                public @Nullable GTRecipe next() {
                    if (called) return null;

                    called = true;

                    return findFallback(items, fluids, specialSlot);
                }
            }));
        }

        // noinspection unchecked
        return new MergedIterator<>(iters.toArray(new Iterator[0]));
    }

    /**
     * The minimum filter required for recipe match logic. You can override this to have custom validation.
     * <p>
     * Other checks like machine voltage will be done in another places.
     * <p>
     * Note that this won't be called if {@link #doesOverwriteFindRecipe} is true.
     */
    protected boolean filterFindRecipe(GTRecipe recipe, ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
        if (!recipe.mEnabled) return false;
        if (recipe.mFakeRecipe) return false;
        if (properties.specialSlotSensitive && !areStacksEqualOrNull((ItemStack) recipe.mSpecialItems, specialSlot)) return false;
        if (!recipe.couldRunOnce(items, fluids, dontCheckStackSizes)) return false;

        return true;
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
