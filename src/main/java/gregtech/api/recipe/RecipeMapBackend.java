package gregtech.api.recipe;

import static gregtech.api.util.GTRecipeBuilder.ENABLE_COLLISION_CHECK;
import static gregtech.api.util.GTRecipeBuilder.handleInvalidRecipe;
import static gregtech.api.util.GTRecipeBuilder.handleInvalidRecipeLowFluids;
import static gregtech.api.util.GTRecipeBuilder.handleInvalidRecipeLowItems;
import static gregtech.api.util.GTRecipeBuilder.handleRecipeCollision;
import static gregtech.api.util.GTUtility.areStacksEqualOrNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import gregtech.api.objects.GTItemStack;
import gregtech.api.recipe.lookup.GTFluidLookupIngredient;
import gregtech.api.recipe.lookup.GTItemDataLookupIngredient;
import gregtech.api.recipe.lookup.GTItemStackLookupIngredient;
import gregtech.api.recipe.lookup.GTOreDictLookupIngredient;
import gregtech.api.recipe.lookup.GTRecipeLookup;
import gregtech.api.recipe.lookup.GTRecipeLookupBuilder;
import gregtech.api.recipe.lookup.GTRecipeLookupIngredient;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTStreamUtil;
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

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");

    private RecipeMap<?> recipeMap;

    /**
     * Contains all items that are present in some recipe in the corresponding recipeMap.
     * This is used for hatch filtering.
     */
    private final Set<GTItemStack> meaningfulItems = new HashSet<>();
    /**
     * Contains all items that are present in some recipe in the corresponding recipeMap.
     * This is used for hatch filtering.
     */
    private final Set<String> meaningfulFluids = new HashSet<>();

    /**
     * All the recipes belonging to this backend, indexed by recipe category.
     */
    private final Map<RecipeCategory, Collection<GTRecipe>> recipesByCategory = new HashMap<>();

    /** Size of the cache map below. */
    public static final int CACHE_MAP_SIZE = 256;

    /** Cached recipes, by commutative hash of all inputs. */
    private final @Nullable GTRecipe[] cacheMap = new GTRecipe[CACHE_MAP_SIZE];

    private GTRecipeLookup recipeLookup = new GTRecipeLookup();
    private GTRecipeLookupBuilder.LookupBuildState recipeLookupState = GTRecipeLookupBuilder.newLookupState();

    /**
     * All the properties specific to this backend.
     */
    protected final RecipeMapBackendProperties properties;

    public RecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        this.properties = propertiesBuilder.build();
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
     * @return All the recipes belonging to this backend. Returned collection is immutable, use {@link #compileRecipe}
     *         to add / {@link #removeRecipes} to remove.
     */
    @Unmodifiable
    public Collection<GTRecipe> getAllRecipes() {
        return Collections.unmodifiableCollection(allRecipes());
    }

    /**
     * @return Raw recipe list
     */
    Collection<GTRecipe> allRecipes() {
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

        GTRecipe result = addToItemMap(recipe);
        addRecipeToLookup(recipe);
        return result;
    }

    /**
     * Adds the supplied recipe to the meaningful item/fluid lists.
     */
    protected GTRecipe addToItemMap(GTRecipe recipe) {
        for (FluidStack fluid : recipe.mFluidInputs) {
            meaningfulFluids.add(
                fluid.getFluid()
                    .getName());
        }

        for (ItemStack item : recipe.mInputs) {
            meaningfulItems.add(new GTItemStack(item));
        }

        if (recipe instanceof GTRecipe.GTRecipe_WithAlt recipeWithAlt) {

            if (recipeWithAlt.mOreDictAlt != null) {
                for (ItemStack[] itemStacks : recipeWithAlt.mOreDictAlt) {
                    if (itemStacks == null) continue;
                    for (ItemStack item : itemStacks) {
                        meaningfulItems.add(new GTItemStack(item));
                    }
                }
            }

            if (recipeWithAlt.mAltFluidInputs != null) {
                for (FluidStack[] fluidStacks : recipeWithAlt.mAltFluidInputs) {
                    if (fluidStacks == null) continue;
                    for (FluidStack fluid : fluidStacks) {
                        meaningfulFluids.add(
                            fluid.getFluid()
                                .getName());
                    }
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
        recipesByCategory.entrySet()
            .removeIf(
                entry -> entry.getValue()
                    .isEmpty());
        rebuildLookupStructures(false);
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
        clearLookupStructures();
    }

    // endregion

    /**
     * Re-unificates all the items present in recipes. Also reflects recipe removals.
     */
    public void reInit() {
        rebuildLookupStructures(true);
    }

    private void clearLookupStructures() {
        meaningfulItems.clear();
        meaningfulFluids.clear();
        Arrays.fill(cacheMap, null);
        recipeLookup = new GTRecipeLookup();
        recipeLookupState = GTRecipeLookupBuilder.newLookupState();
    }

    private void rebuildLookupStructures(boolean reUnificate) {
        meaningfulItems.clear();
        meaningfulFluids.clear();
        Arrays.fill(cacheMap, null);

        GTRecipeLookupBuilder lookupBuilder = new GTRecipeLookupBuilder();
        for (GTRecipe recipe : allRecipes()) {
            if (reUnificate) {
                GTOreDictUnificator.setStackArray(true, true, recipe.mInputs);
                GTOreDictUnificator.setStackArray(true, true, recipe.mOutputs);
            }
            addToItemMap(recipe);
            lookupBuilder.add(recipe);
        }
        GTRecipeLookupBuilder.BuildResult result = lookupBuilder.buildMutableWithState();
        recipeLookup = result.lookup;
        recipeLookupState = result.state;
    }

    private void addRecipeToLookup(GTRecipe recipe) {
        GTRecipeLookupBuilder.addToLookup(recipeLookup, recipeLookupState, recipe);
    }

    /**
     * @return If supplied item is a valid input for any of the recipes
     */
    public boolean containsInput(ItemStack item) {
        return meaningfulItems.contains(new GTItemStack(item)) || meaningfulItems.contains(new GTItemStack(item, true));
    }

    /**
     * @return If supplied fluid is a valid input for any of the recipes
     */
    public boolean containsInput(Fluid fluid) {
        return meaningfulFluids.contains(fluid.getName());
    }

    // region find recipe

    /**
     * Checks if given recipe conflicts with already registered recipes.
     *
     * @return True if collision is found.
     */
    boolean checkCollision(GTRecipe recipe) {
        return matchRecipeStream(recipe.mInputs, recipe.mFluidInputs, null, null, false, true, true).findAny()
            .isPresent();
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
    public boolean doesOverwriteFindRecipe() {
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
     * @param fluids              Fluid inputs.
     * @param specialSlot         Content of the special slot. Normal recipemaps don't need this, but some do. Set
     *                            {@link RecipeMapBuilder#specialSlotSensitive} to make it actually functional.
     *                            Alternatively overriding {@link #filterFindRecipe} will also work.
     * @param cachedRecipe        If this is not null, this method tests it before all other recipes.
     * @param notUnificated       If this is set to true, item inputs will be unificated.
     * @param dontCheckStackSizes If this is set to true, this method won't check item count and fluid amount for the
     *                            matched recipe.
     * @param forCollisionCheck   If this method is called to check collision with already registered recipes.
     * @return Stream of matches recipes.
     */
    Stream<GTRecipe> matchRecipeStream(@Nullable ItemStack @NotNull [] rawItems,
        @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, @Nullable GTRecipe cachedRecipe,
        boolean notUnificated, boolean dontCheckStackSizes, boolean forCollisionCheck) {
        if (doesOverwriteFindRecipe()) {
            return GTStreamUtil.ofNullable(overwriteFindRecipe(rawItems, fluids, specialSlot, cachedRecipe));
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
            items = GTOreDictUnificator.getStackArray(true, (Object[]) rawItems);
        } else {
            items = rawItems;
        }

        // Recipe lookup uses the trie as fast filtering, the trie does not contain amount checks and nbt checking to
        // allow fast trie traversal. The core logic of the recipe check happens inside filterFindRecipe,
        // specially, maxParallelCalculatedByInputs in GTRecipe.

        return Stream.concat(
            Stream.<Stream<GTRecipe>>of(
                GTStreamUtil.ofNullable(cachedRecipe)
                    .filter(recipe -> recipe.mCanBeBuffered),
                GTStreamUtil.ofSupplier(() -> cacheMap[(hash(items, fluids)) % CACHE_MAP_SIZE])
                    .filter(Objects::nonNull),
                Stream.<Supplier<Stream<GTRecipe>>>of(() -> lookupCandidateStream(items, fluids))
                    .flatMap(Supplier::get))
                .flatMap(Function.identity())
                .distinct()
                .filter(recipe -> filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes))
                .map(recipe -> modifyFoundRecipe(recipe, items, fluids, specialSlot))
                .filter(Objects::nonNull),
            forCollisionCheck ? Stream.empty()
                : GTStreamUtil.ofSupplier(() -> findFallback(items, fluids, specialSlot))
                    .filter(Objects::nonNull));
    }

    public static boolean shouldValidateLookup() {
        return RecipeLookupValidator.shouldValidateLookup();
    }

    protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids) {
        List<GTRecipeLookupIngredient> ingredients = new ArrayList<>();
        Consumer<GTRecipeLookupIngredient> adder = ingredient -> addLookupIngredient(ingredients, ingredient);

        for (ItemStack item : items) {
            if (item == null) continue;

            GTItemStackLookupIngredient.fromRuntime(adder, item);
            GTItemDataLookupIngredient.fromRuntime(adder, item);
            GTOreDictLookupIngredient.fromRuntime(adder, item);
        }

        for (FluidStack fluid : fluids) {
            if (fluid == null || fluid.getFluid() == null) continue;

            GTFluidLookupIngredient.fromRuntime(adder, fluid);
        }

        if (ingredients.isEmpty()) {
            return Stream.empty();
        }

        Iterator<GTRecipe> iterator = recipeLookup.iterator(ingredients);
        return StreamSupport
            .stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.NONNULL | Spliterator.IMMUTABLE), false);
    }

    private static void addLookupIngredient(List<GTRecipeLookupIngredient> group, GTRecipeLookupIngredient ingredient) {
        if (!group.contains(ingredient)) {
            group.add(ingredient);
        }
    }

    static Predicate<GTRecipe> distinctByIdentity() {
        Set<GTRecipe> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        return seen::add;
    }

    protected void cache(@Nullable ItemStack @NotNull [] items, @Nullable FluidStack @NotNull [] fluids,
        GTRecipe recipe) {
        cacheMap[hash(items, fluids) % CACHE_MAP_SIZE] = recipe;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    protected int hash(@Nullable ItemStack @NotNull [] items, @Nullable FluidStack @NotNull [] fluids) {
        int hash = 0;
        for (int i = 0; i < items.length; i++) {
            ItemStack stack = items[i];
            if (stack == null) continue;
            Item item = stack.getItem();
            assert item != null;
            hash ^= item.hashCode();
            if (item.getHasSubtypes()) hash += Items.feather.getDamage(stack);
        }
        for (int i = 0; i < fluids.length; i++) {
            FluidStack stack = fluids[i];
            if (stack == null) continue;
            Fluid fluid = stack.getFluid();
            hash ^= fluid.hashCode();
        }
        return hash;
    }

    /**
     * The minimum filter required for recipe match logic. You can override this to have custom validation.
     * <p>
     * Other checks like machine voltage will be done in another places.
     * <p>
     * Note that this won't be called if {@link #doesOverwriteFindRecipe} is true.
     */
    protected boolean filterFindRecipe(GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
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
