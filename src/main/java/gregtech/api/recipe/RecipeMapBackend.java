package gregtech.api.recipe;

import static gregtech.api.util.GT_RecipeBuilder.ENABLE_COLLISION_CHECK;
import static gregtech.api.util.GT_RecipeBuilder.handleInvalidRecipe;
import static gregtech.api.util.GT_RecipeBuilder.handleRecipeCollision;
import static gregtech.api.util.GT_Utility.areStacksEqualOrNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_StreamUtil;
import gregtech.api.util.MethodsReturnNonnullByDefault;

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
     * Adds the supplied recipe to the recipe list and index, without any check.
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
        if (recipe instanceof GT_Recipe.GT_Recipe_WithAlt recipeWithAlt) {
            for (ItemStack[] itemStacks : recipeWithAlt.mOreDictAlt) {
                if (itemStacks == null) continue;
                for (ItemStack item : itemStacks) {
                    if (item == null) continue;
                    itemIndex.put(new GT_ItemStack(item), recipe);
                }
            }
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
            if (recipe.mFluidInputs.length < properties.minFluidInputs
                || recipe.mInputs.length < properties.minItemInputs) {
                return Collections.emptyList();
            }
            if (properties.recipeTransformer != null) {
                recipe = properties.recipeTransformer.apply(recipe);
            }
            if (recipe == null) continue;
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
     * Checks if given recipe conflicts with already registered recipes.
     *
     * @return True if collision is found.
     */
    boolean checkCollision(GT_Recipe recipe) {
        return matchRecipeStream(recipe.mInputs, recipe.mFluidInputs, null, null, false, true, true).findAny()
            .isPresent();
    }

    /**
     * Overwrites {@link #matchRecipeStream} method. Also override {@link #doesOverwriteFindRecipe} to make it work.
     */
    @Nullable
    protected GT_Recipe overwriteFindRecipe(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GT_Recipe cachedRecipe) {
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
    protected GT_Recipe modifyFoundRecipe(GT_Recipe recipe, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        return recipe;
    }

    /**
     * Called when {@link #matchRecipeStream} cannot find recipe.
     */
    @Nullable
    protected GT_Recipe findFallback(ItemStack[] items, FluidStack[] fluids, @Nullable ItemStack specialSlot) {
        return null;
    }

    /**
     * Returns all the matched recipes in the form of Stream, without any additional check for matches.
     *
     * @param rawItems            Item inputs.
     * @param fluids              Fluid inputs.
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
    Stream<GT_Recipe> matchRecipeStream(ItemStack[] rawItems, FluidStack[] fluids, @Nullable ItemStack specialSlot,
        @Nullable GT_Recipe cachedRecipe, boolean notUnificated, boolean dontCheckStackSizes,
        boolean forCollisionCheck) {
        if (doesOverwriteFindRecipe()) {
            return GT_StreamUtil.ofNullable(overwriteFindRecipe(rawItems, fluids, specialSlot, cachedRecipe));
        }

        if (recipesByCategory.isEmpty()) {
            return Stream.empty();
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
                    return Stream.empty();
                }
            }
            if (properties.minItemInputs > 0) {
                int count = 0;
                for (ItemStack item : rawItems) if (item != null) count++;
                if (count < properties.minItemInputs) {
                    return Stream.empty();
                }
            }
        }

        ItemStack[] items;
        // Unification happens here in case the item input isn't already unificated.
        if (notUnificated) {
            items = GT_OreDictUnificator.getStackArray(true, (Object[]) rawItems);
        } else {
            items = rawItems;
        }

        return Stream.<Stream<GT_Recipe>>of(
            // Check the recipe which has been used last time in order to not have to search for it again, if possible.
            GT_StreamUtil.ofNullable(cachedRecipe)
                .filter(recipe -> recipe.mCanBeBuffered)
                .filter(recipe -> filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes))
                .map(recipe -> modifyFoundRecipe(recipe, items, fluids, specialSlot))
                .filter(Objects::nonNull),
            // Now look for the recipes inside the item index, but only when the recipes actually can have items inputs.
            GT_StreamUtil.ofConditional(!itemIndex.isEmpty(), items)
                .filter(Objects::nonNull)
                .flatMap(item -> Stream.of(new GT_ItemStack(item), new GT_ItemStack(item, true)))
                .map(itemIndex::get)
                .flatMap(Collection::stream)
                .filter(recipe -> filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes))
                .map(recipe -> modifyFoundRecipe(recipe, items, fluids, specialSlot))
                .filter(Objects::nonNull),
            // If the minimum amount of items required for the recipes is 0, then it could match to fluid-only recipes,
            // so check fluid index too.
            GT_StreamUtil.ofConditional(properties.minItemInputs == 0, fluids)
                .filter(Objects::nonNull)
                .map(
                    fluidStack -> fluidIndex.get(
                        fluidStack.getFluid()
                            .getName()))
                .flatMap(Collection::stream)
                .filter(recipe -> filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes))
                .map(recipe -> modifyFoundRecipe(recipe, items, fluids, specialSlot))
                .filter(Objects::nonNull),
            // Lastly, find fallback.
            forCollisionCheck ? Stream.empty()
                : GT_StreamUtil.ofSupplier(() -> findFallback(items, fluids, specialSlot))
                    .filter(Objects::nonNull))
            .flatMap(Function.identity());
    }

    /**
     * The minimum filter required for recipe match logic. You can override this to have custom validation.
     * <p>
     * Other checks like machine voltage will be done in another places.
     * <p>
     * Note that this won't be called if {@link #doesOverwriteFindRecipe} is true.
     */
    protected boolean filterFindRecipe(GT_Recipe recipe, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
        if (recipe.mEnabled && !recipe.mFakeRecipe
            && recipe.isRecipeInputEqual(false, dontCheckStackSizes, fluids, items)) {
            return !properties.specialSlotSensitive
                || areStacksEqualOrNull((ItemStack) recipe.mSpecialItems, specialSlot);
        }
        return false;
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
