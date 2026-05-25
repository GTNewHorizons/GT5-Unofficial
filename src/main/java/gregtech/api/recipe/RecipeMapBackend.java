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
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import gregtech.api.objects.GTItemStack;
import gregtech.api.recipe.lookup.GTFluidLookupIngredient;
import gregtech.api.recipe.lookup.GTItemStackLookupIngredient;
import gregtech.api.recipe.lookup.GTOreDictLookupIngredient;
import gregtech.api.recipe.lookup.GTRecipeLookup;
import gregtech.api.recipe.lookup.GTRecipeLookupBuilder;
import gregtech.api.recipe.lookup.GTRecipeLookupIngredient;
import gregtech.api.util.GTLog;
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

    private RecipeMap<?> recipeMap;

    /**
     * Contains-only recipe index based on items. Default recipe lookup uses {@link #recipeLookup}.
     */
    private final SetMultimap<GTItemStack, GTRecipe> itemContainsIndex = LinkedHashMultimap.create();
    /**
     * Contains-only recipe index based on fluids. Default recipe lookup uses {@link #recipeLookup}.
     */
    private final SetMultimap<String, GTRecipe> fluidContainsIndex = LinkedHashMultimap.create();

    /**
     * All the recipes belonging to this backend, indexed by recipe category.
     */
    private final Map<RecipeCategory, Collection<GTRecipe>> recipesByCategory = new HashMap<>();

    /** Size of the cache map below. */
    public static final int CACHE_MAP_SIZE = 256;

    /** Cached recipes, by commutative hash of all inputs. */
    private final GTRecipe[] cacheMap = new GTRecipe[CACHE_MAP_SIZE];

    private GTRecipeLookup recipeLookup = new GTRecipeLookup();

    private boolean recipeLookupDirty;

    private final Map<GTRecipe, Integer> recipeRegistrationOrdinals = new IdentityHashMap<>();

    private int nextRecipeRegistrationOrdinal;

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
        ensureRegistrationOrdinal(recipe);

        GTRecipe result = addToItemMap(recipe);
        addRecipeToLookup(recipe);
        return result;
    }

    /**
     * Adds the supplied recipe to the item cache.
     */
    protected GTRecipe addToItemMap(GTRecipe recipe) {
        if (recipe.mFluidInputs != null) {
            for (FluidStack fluid : recipe.mFluidInputs) {
                addFluidToContainsIndex(fluid, recipe);
            }
        }

        if (recipe.mInputs != null) {
            for (ItemStack item : recipe.mInputs) {
                if (item == null) continue;
                itemContainsIndex.put(new GTItemStack(item), recipe);
            }
        }

        if (recipe instanceof GTRecipe.GTRecipe_WithAlt recipeWithAlt) {

            if (recipeWithAlt.mOreDictAlt != null) {
                for (ItemStack[] itemStacks : recipeWithAlt.mOreDictAlt) {
                    if (itemStacks == null) continue;
                    for (ItemStack item : itemStacks) {
                        if (item == null) continue;
                        itemContainsIndex.put(new GTItemStack(item), recipe);
                    }
                }
            }

            if (recipeWithAlt.mAltFluidInputs != null) {
                for (FluidStack[] fluidStacks : recipeWithAlt.mAltFluidInputs) {
                    if (fluidStacks == null) continue;
                    for (FluidStack fluid : fluidStacks) {
                        addFluidToContainsIndex(fluid, recipe);
                    }
                }
            }
        }
        return recipe;
    }

    private void addFluidToContainsIndex(@Nullable FluidStack fluid, GTRecipe recipe) {
        if (fluid == null || fluid.getFluid() == null) return;
        fluidContainsIndex.put(
            fluid.getFluid()
                .getName(),
            recipe);
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
        recipesByCategory.entrySet()
            .removeIf(
                entry -> entry.getValue()
                    .isEmpty());
        for (GTRecipe recipe : recipesToRemove) {
            recipeRegistrationOrdinals.remove(recipe);
        }
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
        itemContainsIndex.clear();
        fluidContainsIndex.clear();
        Arrays.fill(cacheMap, null);
        recipeLookup = new GTRecipeLookup();
        recipeLookupDirty = false;
        recipeRegistrationOrdinals.clear();
        nextRecipeRegistrationOrdinal = 0;
    }

    private void rebuildLookupStructures(boolean reUnificate) {
        itemContainsIndex.clear();
        fluidContainsIndex.clear();
        Arrays.fill(cacheMap, null);

        GTRecipeLookupBuilder lookupBuilder = new GTRecipeLookupBuilder();
        for (GTRecipe recipe : allRecipes()) {
            if (reUnificate) {
                GTOreDictUnificator.setStackArray(true, true, recipe.mInputs);
                GTOreDictUnificator.setStackArray(true, true, recipe.mOutputs);
            }
            ensureRegistrationOrdinal(recipe);
            addToItemMap(recipe);
            lookupBuilder.add(recipe);
        }
        recipeLookup = lookupBuilder.buildMutable();
        recipeLookupDirty = false;
    }

    private void ensureLookupCurrent() {
        if (!recipeLookupDirty) {
            return;
        }

        GTRecipeLookupBuilder lookupBuilder = new GTRecipeLookupBuilder();
        for (GTRecipe recipe : allRecipes()) {
            ensureRegistrationOrdinal(recipe);
            lookupBuilder.add(recipe);
        }
        recipeLookup = lookupBuilder.buildMutable();
        recipeLookupDirty = false;
    }

    private void addRecipeToLookup(GTRecipe recipe) {
        if (recipeLookupDirty) {
            ensureLookupCurrent();
        }
        if (!GTRecipeLookupBuilder.addToLookup(recipeLookup, recipe)) {
            throw new IllegalStateException("Could not add recipe to lookup");
        }
    }

    /**
     * @return If supplied item is a valid input for any of the recipes
     */
    public boolean containsInput(ItemStack item) {
        return itemContainsIndex.containsKey(new GTItemStack(item))
            || itemContainsIndex.containsKey(new GTItemStack(item, true));
    }

    /**
     * @return If supplied fluid is a valid input for any of the recipes
     */
    public boolean containsInput(Fluid fluid) {
        return fluidContainsIndex.containsKey(fluid.getName());
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
        RecipeLookupProfile profile = RecipeLookupProfile.start(recipeMap, forCollisionCheck);
        long setupStart = profile == null ? 0L : System.nanoTime();
        if (doesOverwriteFindRecipe()) {
            if (profile != null) {
                profile.recordOverwrite();
                profile.addSetupNanos(System.nanoTime() - setupStart);
            }
            return GTStreamUtil.ofNullable(overwriteFindRecipe(rawItems, fluids, specialSlot, cachedRecipe));
        }

        if (recipesByCategory.isEmpty()) {
            if (profile != null) {
                profile.recordEmptyMapReject();
                profile.addSetupNanos(System.nanoTime() - setupStart);
            }
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
                    if (profile != null) {
                        profile.recordMinInputReject();
                        profile.addSetupNanos(System.nanoTime() - setupStart);
                    }
                    return Stream.empty();
                }
            }
            if (properties.minItemInputs > 0) {
                int count = 0;
                for (ItemStack item : rawItems) if (item != null) count++;
                if (count < properties.minItemInputs) {
                    if (profile != null) {
                        profile.recordMinInputReject();
                        profile.addSetupNanos(System.nanoTime() - setupStart);
                    }
                    return Stream.empty();
                }
            }
        }

        ItemStack[] items;
        // Unification happens here in case the item input isn't already unificated.
        if (notUnificated) {
            if (profile == null) {
                items = GTOreDictUnificator.getStackArray(true, (Object[]) rawItems);
            } else {
                long unificationStart = System.nanoTime();
                items = GTOreDictUnificator.getStackArray(true, (Object[]) rawItems);
                profile.addUnificationNanos(System.nanoTime() - unificationStart);
            }
        } else {
            items = rawItems;
        }

        if (profile == null) {
            ensureLookupCurrent();
        } else {
            long ensureStart = System.nanoTime();
            ensureLookupCurrent();
            profile.addEnsureLookupNanos(System.nanoTime() - ensureStart);
        }

        if (forCollisionCheck) {
            Stream<GTRecipe> stream = collisionCandidateStream(
                items,
                fluids,
                specialSlot,
                dontCheckStackSizes,
                profile);
            if (profile != null) {
                profile.addSetupNanos(System.nanoTime() - setupStart);
            }
            return stream;
        }

        // The trie is the only default recipe candidate lookup path. The item/fluid indexes are containsInput-only.
        Stream<GTRecipe> cachedRecipeCandidates = GTStreamUtil.ofNullable(cachedRecipe)
            .filter(recipe -> recipe.mCanBeBuffered)
            .filter(this::isRecipeRegistered);
        if (profile != null) {
            cachedRecipeCandidates = cachedRecipeCandidates.peek(recipe -> profile.recordCachedRecipeCandidate());
        }

        Stream<GTRecipe> cacheMapCandidates = GTStreamUtil.ofSupplier(() -> {
            if (profile != null) {
                profile.recordCacheMapProbe();
            }
            return cacheMap[(hash(items, fluids)) % CACHE_MAP_SIZE];
        })
            .filter(Objects::nonNull)
            .filter(this::isRecipeRegistered);
        if (profile != null) {
            cacheMapCandidates = cacheMapCandidates.peek(recipe -> profile.recordCacheMapCandidate());
        }
        final Stream<GTRecipe> cachedRecipeCandidateStream = cachedRecipeCandidates;
        final Stream<GTRecipe> cacheMapCandidateStream = cacheMapCandidates;

        Stream<GTRecipe> candidates = Stream
            .<Supplier<Stream<GTRecipe>>>of(() -> cachedRecipeCandidateStream, () -> cacheMapCandidateStream, () -> {
                Stream<GTRecipe> trieCandidates = lookupCandidateStream(items, fluids, profile)
                    .filter(this::isRecipeRegistered);
                if (profile != null) {
                    trieCandidates = trieCandidates.peek(recipe -> profile.recordTrieCandidate());
                }
                return trieCandidates.sorted(
                    (first, second) -> Integer
                        .compare(recipeRegistrationOrdinal(first), recipeRegistrationOrdinal(second)));
            })
            .flatMap(Supplier::get)
            .filter(distinctByIdentity())
            .filter(
                recipe -> profiledFilterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes, profile))
            .map(recipe -> profiledModifyFoundRecipe(recipe, items, fluids, specialSlot, profile))
            .filter(Objects::nonNull);
        if (profile != null) {
            candidates = candidates.peek(recipe -> profile.recordMatchedCandidate());
        }

        Stream<GTRecipe> stream = Stream.concat(candidates, GTStreamUtil.ofSupplier(() -> {
            if (profile != null) {
                profile.recordFallbackProbe();
            }
            GTRecipe fallback = findFallback(items, fluids, specialSlot);
            if (profile != null && fallback != null) {
                profile.recordFallbackHit();
            }
            return fallback;
        })
            .filter(Objects::nonNull));
        if (profile != null) {
            profile.addSetupNanos(System.nanoTime() - setupStart);
        }
        return stream;
    }

    private Stream<GTRecipe> collisionCandidateStream(@Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes,
        @Nullable RecipeLookupProfile profile) {
        Stream<GTRecipe> candidates = lookupCandidateStream(items, fluids, profile).filter(this::isRecipeRegistered);
        if (profile != null) {
            candidates = candidates.peek(recipe -> profile.recordTrieCandidate());
        }
        return candidates
            .filter(
                recipe -> profiledFilterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes, profile))
            .map(recipe -> profiledModifyFoundRecipe(recipe, items, fluids, specialSlot, profile))
            .filter(Objects::nonNull);
    }

    private Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids, @Nullable RecipeLookupProfile profile) {
        if (profile == null) {
            return lookupCandidateStream(items, fluids);
        }
        profile.recordTrieBuild();
        long start = System.nanoTime();
        Stream<GTRecipe> stream = lookupCandidateStream(items, fluids);
        profile.addTrieBuildNanos(System.nanoTime() - start);
        return stream;
    }

    protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids) {
        List<List<GTRecipeLookupIngredient>> ingredients = new ArrayList<>();

        for (ItemStack item : items) {
            if (item == null) continue;

            List<GTRecipeLookupIngredient> group = new ArrayList<>();
            addLookupIngredient(group, GTItemStackLookupIngredient.fromRuntime(item));
            addLookupIngredient(group, GTItemStackLookupIngredient.fromRuntimeWildcard(item));
            if (item.hasTagCompound()) {
                addLookupIngredient(group, GTItemStackLookupIngredient.fromSpecialRecipe(item));
            }
            for (GTOreDictLookupIngredient oreIngredient : GTOreDictLookupIngredient.fromRuntime(item)) {
                addLookupIngredient(group, oreIngredient);
            }
            if (!group.isEmpty()) {
                ingredients.add(group);
            }
        }

        for (FluidStack fluid : fluids) {
            if (fluid == null || fluid.getFluid() == null) continue;

            List<GTRecipeLookupIngredient> group = new ArrayList<>(1);
            addLookupIngredient(group, new GTFluidLookupIngredient(fluid));
            ingredients.add(group);
        }

        if (ingredients.isEmpty()) {
            return Stream.empty();
        }

        Iterator<GTRecipe> iterator = recipeLookup.iterator(ingredients);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    private static void addLookupIngredient(List<GTRecipeLookupIngredient> group, GTRecipeLookupIngredient ingredient) {
        if (!group.contains(ingredient)) {
            group.add(ingredient);
        }
    }

    private boolean isRecipeRegistered(GTRecipe recipe) {
        return recipeRegistrationOrdinals.containsKey(recipe);
    }

    private void ensureRegistrationOrdinal(GTRecipe recipe) {
        if (!recipeRegistrationOrdinals.containsKey(recipe)) {
            recipeRegistrationOrdinals.put(recipe, nextRecipeRegistrationOrdinal++);
        }
    }

    private int recipeRegistrationOrdinal(GTRecipe recipe) {
        Integer ordinal = recipeRegistrationOrdinals.get(recipe);
        return ordinal == null ? Integer.MAX_VALUE : ordinal;
    }

    private static Predicate<GTRecipe> distinctByIdentity() {
        Set<GTRecipe> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        return seen::add;
    }

    protected void cache(@Nullable ItemStack @NotNull [] items, @Nullable FluidStack @NotNull [] fluids,
        @NotNull GTRecipe recipe) {
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
    protected boolean filterFindRecipe(@NotNull GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
        if (recipe.mEnabled && !recipe.mFakeRecipe
            && recipe.isRecipeInputEqual(false, dontCheckStackSizes, fluids, items)) {
            return !properties.specialSlotSensitive
                || areStacksEqualOrNull((ItemStack) recipe.mSpecialItems, specialSlot);
        }
        return false;
    }

    private boolean profiledFilterFindRecipe(@NotNull GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes,
        @Nullable RecipeLookupProfile profile) {
        if (profile == null) {
            return filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes);
        }
        long start = System.nanoTime();
        boolean result = filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes);
        profile.addFilterNanos(System.nanoTime() - start, result);
        return result;
    }

    @Nullable
    private GTRecipe profiledModifyFoundRecipe(@NotNull GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot,
        @Nullable RecipeLookupProfile profile) {
        if (profile == null) {
            return modifyFoundRecipe(recipe, items, fluids, specialSlot);
        }
        long start = System.nanoTime();
        GTRecipe result = modifyFoundRecipe(recipe, items, fluids, specialSlot);
        profile.addModifyNanos(System.nanoTime() - start, result != null);
        return result;
    }

    // endregion

    private static final class RecipeLookupProfile {

        private static final boolean ENABLED = Boolean.getBoolean("gt.recipe.lookup.profile");
        private static final long REPORT_INTERVAL_CALLS = Long
            .getLong("gt.recipe.lookup.profile.interval_calls", 5_000L);
        private static final long REPORT_INTERVAL_NANOS = Math
            .max(1L, Long.getLong("gt.recipe.lookup.profile.interval_seconds", 15L)) * 1_000_000_000L;
        private static final ConcurrentMap<String, RecipeLookupProfileStats> STATS = new ConcurrentHashMap<>();

        private final RecipeLookupProfileStats stats;

        private RecipeLookupProfile(RecipeLookupProfileStats stats) {
            this.stats = stats;
        }

        @Nullable
        private static RecipeLookupProfile start(@Nullable RecipeMap<?> recipeMap, boolean collisionCheck) {
            if (!ENABLED) {
                return null;
            }
            String mapName = recipeMap == null ? "<unbound>" : recipeMap.unlocalizedName;
            RecipeLookupProfileStats stats = STATS.computeIfAbsent(mapName, RecipeLookupProfileStats::new);
            stats.recordCall(collisionCheck);
            return new RecipeLookupProfile(stats);
        }

        private void recordOverwrite() {
            stats.recordOverwrite();
        }

        private void recordEmptyMapReject() {
            stats.recordEmptyMapReject();
        }

        private void recordMinInputReject() {
            stats.recordMinInputReject();
        }

        private void addSetupNanos(long nanos) {
            stats.addSetupNanos(nanos);
        }

        private void addUnificationNanos(long nanos) {
            stats.addUnificationNanos(nanos);
        }

        private void addEnsureLookupNanos(long nanos) {
            stats.addEnsureLookupNanos(nanos);
        }

        private void recordCachedRecipeCandidate() {
            stats.recordCachedRecipeCandidate();
        }

        private void recordCacheMapProbe() {
            stats.recordCacheMapProbe();
        }

        private void recordCacheMapCandidate() {
            stats.recordCacheMapCandidate();
        }

        private void recordTrieBuild() {
            stats.recordTrieBuild();
        }

        private void addTrieBuildNanos(long nanos) {
            stats.addTrieBuildNanos(nanos);
        }

        private void recordTrieCandidate() {
            stats.recordTrieCandidate();
        }

        private void recordMatchedCandidate() {
            stats.recordMatchedCandidate();
        }

        private void recordFallbackProbe() {
            stats.recordFallbackProbe();
        }

        private void recordFallbackHit() {
            stats.recordFallbackHit();
        }

        private void addFilterNanos(long nanos, boolean matched) {
            stats.addFilterNanos(nanos, matched);
        }

        private void addModifyNanos(long nanos, boolean returnedRecipe) {
            stats.addModifyNanos(nanos, returnedRecipe);
        }
    }

    private static final class RecipeLookupProfileStats {

        private final String mapName;
        private long lastReportNanos = System.nanoTime();
        private long lastReportCalls;
        private long calls;
        private long collisionCalls;
        private long overwriteCalls;
        private long emptyMapRejects;
        private long minInputRejects;
        private long setupNanos;
        private long unificationNanos;
        private long ensureLookupNanos;
        private long cachedRecipeCandidates;
        private long cacheMapProbes;
        private long cacheMapCandidates;
        private long trieBuilds;
        private long trieBuildNanos;
        private long trieCandidates;
        private long matchedCandidates;
        private long fallbackProbes;
        private long fallbackHits;
        private long filterCalls;
        private long filterMatches;
        private long filterNanos;
        private long modifyCalls;
        private long modifyHits;
        private long modifyNanos;

        private RecipeLookupProfileStats(String mapName) {
            this.mapName = mapName;
        }

        private synchronized void recordCall(boolean collisionCheck) {
            calls++;
            if (collisionCheck) {
                collisionCalls++;
            }
            maybeReport();
        }

        private synchronized void recordOverwrite() {
            overwriteCalls++;
        }

        private synchronized void recordEmptyMapReject() {
            emptyMapRejects++;
        }

        private synchronized void recordMinInputReject() {
            minInputRejects++;
        }

        private synchronized void addSetupNanos(long nanos) {
            setupNanos += nanos;
        }

        private synchronized void addUnificationNanos(long nanos) {
            unificationNanos += nanos;
        }

        private synchronized void addEnsureLookupNanos(long nanos) {
            ensureLookupNanos += nanos;
        }

        private synchronized void recordCachedRecipeCandidate() {
            cachedRecipeCandidates++;
        }

        private synchronized void recordCacheMapProbe() {
            cacheMapProbes++;
        }

        private synchronized void recordCacheMapCandidate() {
            cacheMapCandidates++;
        }

        private synchronized void recordTrieBuild() {
            trieBuilds++;
        }

        private synchronized void addTrieBuildNanos(long nanos) {
            trieBuildNanos += nanos;
        }

        private synchronized void recordTrieCandidate() {
            trieCandidates++;
        }

        private synchronized void recordMatchedCandidate() {
            matchedCandidates++;
        }

        private synchronized void recordFallbackProbe() {
            fallbackProbes++;
        }

        private synchronized void recordFallbackHit() {
            fallbackHits++;
        }

        private synchronized void addFilterNanos(long nanos, boolean matched) {
            filterCalls++;
            if (matched) {
                filterMatches++;
            }
            filterNanos += nanos;
        }

        private synchronized void addModifyNanos(long nanos, boolean returnedRecipe) {
            modifyCalls++;
            if (returnedRecipe) {
                modifyHits++;
            }
            modifyNanos += nanos;
        }

        private void maybeReport() {
            long now = System.nanoTime();
            boolean enoughCalls = RecipeLookupProfile.REPORT_INTERVAL_CALLS > 0
                && calls - lastReportCalls >= RecipeLookupProfile.REPORT_INTERVAL_CALLS;
            boolean enoughTime = now - lastReportNanos >= RecipeLookupProfile.REPORT_INTERVAL_NANOS;
            if (!enoughCalls && !enoughTime) {
                return;
            }

            GTLog.out.println(
                String.format(
                    Locale.ROOT,
                    "[GTRecipeLookupProfile] map=%s calls=%d collision=%d overwrite=%d empty=%d minReject=%d "
                        + "setupAvgUs=%.3f ensureAvgUs=%.3f unifyMs=%.3f cachedCandidates=%d "
                        + "cacheMapProbes=%d cacheMapCandidates=%d trieBuilds=%d trieBuildMs=%.3f "
                        + "trieCandidates=%d trieCandidatesPerBuild=%.3f matched=%d fallbackProbes=%d fallbackHits=%d "
                        + "filterCalls=%d filterMatches=%d filterMs=%.3f modifyCalls=%d modifyHits=%d modifyMs=%.3f",
                    mapName,
                    calls,
                    collisionCalls,
                    overwriteCalls,
                    emptyMapRejects,
                    minInputRejects,
                    averageMicros(setupNanos, calls),
                    averageMicros(ensureLookupNanos, calls),
                    nanosToMillis(unificationNanos),
                    cachedRecipeCandidates,
                    cacheMapProbes,
                    cacheMapCandidates,
                    trieBuilds,
                    nanosToMillis(trieBuildNanos),
                    trieCandidates,
                    average(trieCandidates, trieBuilds),
                    matchedCandidates,
                    fallbackProbes,
                    fallbackHits,
                    filterCalls,
                    filterMatches,
                    nanosToMillis(filterNanos),
                    modifyCalls,
                    modifyHits,
                    nanosToMillis(modifyNanos)));
            lastReportCalls = calls;
            lastReportNanos = now;
        }

        private static double average(long value, long count) {
            return count == 0 ? 0.0D : (double) value / count;
        }

        private static double averageMicros(long nanos, long count) {
            return count == 0 ? 0.0D : (double) nanos / count / 1_000.0D;
        }

        private static double nanosToMillis(long nanos) {
            return (double) nanos / 1_000_000.0D;
        }
    }

    @FunctionalInterface
    public interface BackendCreator<B extends RecipeMapBackend> {

        /**
         * @see RecipeMapBackend#RecipeMapBackend
         */
        B create(RecipeMapBackendPropertiesBuilder propertiesBuilder);
    }
}
