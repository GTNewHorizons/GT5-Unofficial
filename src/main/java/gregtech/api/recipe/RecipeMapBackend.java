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
import java.util.concurrent.atomic.AtomicBoolean;
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
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.SetMultimap;

import cpw.mods.fml.common.ModContainer;
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

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");

    public static final String VALIDATE_LOOKUP_PROPERTY = "gt.recipe.lookup.validate";

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
                if (fluid == null) continue;
                fluidContainsIndex.put(
                    fluid.getFluid()
                        .getName(),
                    recipe);
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
                        if (fluid == null) continue;
                        fluidContainsIndex.put(
                            fluid.getFluid()
                                .getName(),
                            recipe);
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
            lookupBuilder.add(recipe);
        }
        recipeLookup = lookupBuilder.buildMutable();
        recipeLookupDirty = false;
    }

    private void addRecipeToLookup(GTRecipe recipe) {
        if (recipeLookupDirty) {
            ensureLookupCurrent();
        }
        GTRecipeLookupBuilder.addToLookup(recipeLookup, recipe);
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
            .filter(recipe -> recipe.mCanBeBuffered);
        if (profile != null) {
            cachedRecipeCandidates = cachedRecipeCandidates.peek(recipe -> profile.recordCachedRecipeCandidate());
        }

        Stream<GTRecipe> cacheMapCandidates = GTStreamUtil.ofSupplier(() -> {
            if (profile != null) {
                profile.recordCacheMapProbe();
            }
            return cacheMap[(hash(items, fluids)) % CACHE_MAP_SIZE];
        })
            .filter(Objects::nonNull);
        if (profile != null) {
            cacheMapCandidates = cacheMapCandidates.peek(recipe -> profile.recordCacheMapCandidate());
        }
        final Stream<GTRecipe> cachedRecipeCandidateStream = cachedRecipeCandidates;
        final Stream<GTRecipe> cacheMapCandidateStream = cacheMapCandidates;

        AtomicBoolean matchedBeforeFallback = new AtomicBoolean(false);
        Stream<GTRecipe> candidates = Stream
            .<Supplier<Stream<GTRecipe>>>of(() -> cachedRecipeCandidateStream, () -> cacheMapCandidateStream, () -> {
                Stream<GTRecipe> trieCandidates = lookupCandidateStream(items, fluids, profile);
                if (profile != null) {
                    trieCandidates = trieCandidates.peek(recipe -> profile.recordTrieCandidate());
                }
                return trieCandidates;
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
        candidates = candidates.peek(recipe -> matchedBeforeFallback.set(true));

        Stream<GTRecipe> dynamicFallback = GTStreamUtil.ofSupplier(() -> {
            if (matchedBeforeFallback.get()) {
                return null;
            }
            if (profile != null) {
                profile.recordFallbackProbe();
            }
            GTRecipe fallback = findFallback(items, fluids, specialSlot);
            if (profile != null && fallback != null) {
                profile.recordFallbackHit();
            }
            return fallback;
        })
            .filter(Objects::nonNull);
        dynamicFallback = dynamicFallback.peek(recipe -> matchedBeforeFallback.set(true));

        Stream<GTRecipe> stream = Stream.concat(candidates, dynamicFallback);
        if (profile != null) {
            profile.addSetupNanos(System.nanoTime() - setupStart);
        }
        return stream;
    }

    public static boolean shouldValidateLookup() {
        return Boolean.getBoolean(VALIDATE_LOOKUP_PROPERTY);
    }

    public static void validateLookup() {
        validateLookup(RecipeMap.ALL_RECIPE_MAPS.values());
    }

    static void validateLookup(Collection<? extends RecipeMap<?>> recipeMaps) {
        List<RecipeLookupValidationTarget> targets = new ArrayList<>();
        for (RecipeMap<?> recipeMap : recipeMaps) {
            RecipeMapBackend backend = recipeMap.getBackend();
            if (backend.doesOverwriteFindRecipe()) {
                continue;
            }
            targets.add(new RecipeLookupValidationTarget(recipeMapName(recipeMap), backend));
        }
        new RecipeLookupValidator(targets).validate();
    }

    static void validateLookup(String mapName, RecipeMapBackend backend) {
        new RecipeLookupValidator(Collections.singletonList(new RecipeLookupValidationTarget(mapName, backend)))
            .validate();
    }

    private static String recipeMapName(@Nullable RecipeMap<?> recipeMap) {
        return recipeMap == null ? "<unbound>" : recipeMap.unlocalizedName;
    }

    private static String describeRecipeForValidation(GTRecipe recipe) {
        return "identity=" + System.identityHashCode(recipe)
            + ", category="
            + describeRecipeCategory(recipe)
            + ", owners="
            + describeRecipeOwners(recipe)
            + ", inputs="
            + describeItemStacks(recipe.mInputs)
            + ", fluidInputs="
            + describeFluidStacks(recipe.mFluidInputs)
            + ", outputs="
            + describeItemStacks(recipe.mOutputs)
            + ", fluidOutputs="
            + describeFluidStacks(recipe.mFluidOutputs)
            + ", special="
            + describeObject(recipe.mSpecialItems)
            + ", stackTrace="
            + describeRecipeStackTrace(recipe);
    }

    private static String describeRecipeListForValidation(List<GTRecipe> recipes) {
        return recipes.stream()
            .map(RecipeMapBackend::describeRecipeForValidation)
            .collect(Collectors.joining("\n    ", "[\n    ", "\n]"));
    }

    private static boolean containsIdentity(List<GTRecipe> recipes, GTRecipe target) {
        for (GTRecipe recipe : recipes) {
            if (recipe == target) {
                return true;
            }
        }
        return false;
    }

    private static final class RecipeLookupValidationTarget {

        private final String mapName;
        private final RecipeMapBackend backend;

        private RecipeLookupValidationTarget(String mapName, RecipeMapBackend backend) {
            this.mapName = mapName;
            this.backend = backend;
        }
    }

    private static final class RecipeLookupValidator {

        private static final int RECIPE_PROGRESS_INTERVAL = 100;
        private static final long PROGRESS_LOG_INTERVAL_NANOS = 5_000_000_000L;

        private final List<RecipeLookupValidationTarget> targets;
        private final List<String> issues = new ArrayList<>();
        private final List<String> unresolvedOreDictIssues = new ArrayList<>();
        private final List<RuntimeException> errors = new ArrayList<>();
        private final int totalRecipes;

        private int skippedDisabledRecipes;
        private int skippedFakeRecipes;
        private int skippedNoLookupIngredientRecipes;
        private long startNanos;
        private long lastProgressNanos;
        private int processedMaps;
        private int processedRecipes;

        private RecipeLookupValidator(Collection<RecipeLookupValidationTarget> targets) {
            this.targets = new ArrayList<>(targets);
            int recipeCount = 0;
            for (RecipeLookupValidationTarget target : this.targets) {
                recipeCount += target.backend.allRecipes()
                    .size();
            }
            this.totalRecipes = recipeCount;
        }

        private void validate() {
            startNanos = System.nanoTime();
            lastProgressNanos = startNanos;
            logProgress("starting");

            for (RecipeLookupValidationTarget target : targets) {
                RecipeMapBackend backend = target.backend;
                List<GTRecipe> recipes = new ArrayList<>(backend.allRecipes());
                logMapProgress("map-start", target, recipes.size(), processedMaps + 1);
                try {
                    backend.ensureLookupCurrent();
                    for (GTRecipe recipe : recipes) {
                        if (shouldValidateRecipe(backend, recipe)) {
                            validateRecipe(target, recipe);
                        }
                        processedRecipes++;
                        maybeLogProgress();
                    }
                } catch (RuntimeException e) {
                    addError(target, null, "preparing trie lookup", e);
                    processedRecipes += recipes.size();
                }
                processedMaps++;
                logMapProgress("map-finished", target, recipes.size(), processedMaps);
                maybeLogProgress();
            }

            logProgress("finished");
            if (!issues.isEmpty() || !unresolvedOreDictIssues.isEmpty()) {
                throw buildException();
            }
        }

        private boolean shouldValidateRecipe(RecipeMapBackend backend, GTRecipe recipe) {
            if (!recipe.mEnabled) {
                skippedDisabledRecipes++;
                return false;
            }
            if (recipe.mFakeRecipe) {
                skippedFakeRecipes++;
                return false;
            }

            ItemStack[] items = recipe.mInputs == null ? new ItemStack[0] : recipe.mInputs;
            FluidStack[] fluids = recipe.mFluidInputs == null ? new FluidStack[0] : recipe.mFluidInputs;
            if (hasLookupIngredients(recipe) || preLookupRejectReason(backend, items, fluids) != null) {
                return true;
            }
            skippedNoLookupIngredientRecipes++;
            return false;
        }

        private void validateRecipe(RecipeLookupValidationTarget target, GTRecipe recipe) {
            RecipeMapBackend backend = target.backend;
            ItemStack[] items = recipe.mInputs == null ? new ItemStack[0] : recipe.mInputs;
            FluidStack[] fluids = recipe.mFluidInputs == null ? new FluidStack[0] : recipe.mFluidInputs;
            ItemStack specialSlot = recipe.mSpecialItems instanceof ItemStack ? (ItemStack) recipe.mSpecialItems : null;
            String preLookupRejectReason = preLookupRejectReason(backend, items, fluids);
            if (preLookupRejectReason != null) {
                String unresolvedOreDictReason = unresolvedOreDictInputReason(recipe, items);
                if (unresolvedOreDictReason != null) {
                    addUnresolvedOreDictRecipe(target, recipe, unresolvedOreDictReason, preLookupRejectReason);
                } else {
                    addPreLookupRejectedRecipe(target, recipe, preLookupRejectReason);
                }
                return;
            }
            List<GTRecipe> trieMatches = null;

            try {
                trieMatches = trieMatches(backend, items, fluids, specialSlot);
            } catch (RuntimeException e) {
                addError(target, recipe, "running trie lookup", e);
            }

            if (trieMatches == null) {
                return;
            }

            if (!containsIdentity(trieMatches, recipe)) {
                addMissingQueryRecipe(target, recipe, trieMatches);
            }
        }

        @Nullable
        private String preLookupRejectReason(RecipeMapBackend backend, ItemStack[] items, FluidStack[] fluids) {
            if (backend.properties.minFluidInputs > 0) {
                int fluidInputs = 0;
                for (FluidStack fluid : fluids) {
                    if (fluid != null) {
                        fluidInputs++;
                    }
                }
                if (fluidInputs < backend.properties.minFluidInputs) {
                    return "minFluidInputs=" + backend.properties.minFluidInputs + ", actualFluidInputs=" + fluidInputs;
                }
            }

            if (backend.properties.minItemInputs > 0) {
                int itemInputs = 0;
                for (ItemStack item : items) {
                    if (item != null) {
                        itemInputs++;
                    }
                }
                if (itemInputs < backend.properties.minItemInputs) {
                    return "minItemInputs=" + backend.properties.minItemInputs + ", actualItemInputs=" + itemInputs;
                }
            }

            return null;
        }

        @Nullable
        private String unresolvedOreDictInputReason(GTRecipe recipe, ItemStack[] items) {
            if (!(recipe instanceof GTRecipe.GTRecipe_WithAlt)) {
                return null;
            }

            GTRecipe.GTRecipe_WithAlt recipeWithAlt = (GTRecipe.GTRecipe_WithAlt) recipe;
            if (recipeWithAlt.mOreDictIds == null) {
                return null;
            }

            List<String> unresolvedSlots = new ArrayList<>();
            int slotCount = Math.min(items.length, recipeWithAlt.mOreDictIds.length);
            for (int i = 0; i < slotCount; i++) {
                int oreDictId = recipeWithAlt.mOreDictIds[i];
                if (items[i] != null || oreDictId < 0 || hasOreDictAlternatives(recipeWithAlt, i)) {
                    continue;
                }
                unresolvedSlots
                    .add("slot=" + i + ", oreDictId=" + oreDictId + ", oreDictName=" + oreDictName(oreDictId));
            }

            return unresolvedSlots.isEmpty() ? null : String.join("; ", unresolvedSlots);
        }

        private boolean hasOreDictAlternatives(GTRecipe.GTRecipe_WithAlt recipe, int slot) {
            if (recipe.mOreDictAlt == null || slot >= recipe.mOreDictAlt.length) {
                return false;
            }
            ItemStack[] alternatives = recipe.mOreDictAlt[slot];
            if (alternatives == null) {
                return false;
            }
            for (ItemStack alternative : alternatives) {
                if (alternative != null) {
                    return true;
                }
            }
            return false;
        }

        private String oreDictName(int oreDictId) {
            String name;
            try {
                name = OreDictionary.getOreName(oreDictId);
            } catch (LinkageError | RuntimeException e) {
                return "<unavailable>";
            }
            return name == null ? "<unknown>" : name;
        }

        private boolean hasLookupIngredients(GTRecipe recipe) {
            if (recipe.mInputs != null) {
                for (ItemStack item : recipe.mInputs) {
                    if (item != null) {
                        return true;
                    }
                }
            }
            if (recipe.mFluidInputs != null) {
                for (FluidStack fluid : recipe.mFluidInputs) {
                    if (fluid != null && fluid.getFluid() != null) {
                        return true;
                    }
                }
            }
            return false;
        }

        private List<GTRecipe> trieMatches(RecipeMapBackend backend, ItemStack[] items, FluidStack[] fluids,
            @Nullable ItemStack specialSlot) {
            return backend.lookupCandidateStream(items, fluids)
                .filter(distinctByIdentity())
                .filter(recipe -> backend.filterFindRecipe(recipe, items, fluids, specialSlot, false))
                .collect(Collectors.toList());
        }

        private void addPreLookupRejectedRecipe(RecipeLookupValidationTarget target, GTRecipe queryRecipe,
            String reason) {
            StringBuilder issue = new StringBuilder();
            issue.append("map=")
                .append(target.mapName)
                .append('\n')
                .append("queryRecipe=")
                .append(describeRecipeForValidation(queryRecipe));
            issue.append('\n')
                .append("query rejected before trie lookup: ")
                .append(reason);
            issues.add(issue.toString());
        }

        private void addUnresolvedOreDictRecipe(RecipeLookupValidationTarget target, GTRecipe queryRecipe,
            String unresolvedOreDictReason, String preLookupRejectReason) {
            StringBuilder issue = new StringBuilder();
            issue.append("map=")
                .append(target.mapName)
                .append('\n')
                .append("queryRecipe=")
                .append(describeRecipeForValidation(queryRecipe));
            issue.append('\n')
                .append("unresolved oredict input: ")
                .append(unresolvedOreDictReason);
            issue.append('\n')
                .append("query rejected before trie lookup: ")
                .append(preLookupRejectReason);
            unresolvedOreDictIssues.add(issue.toString());
        }

        private void addMissingQueryRecipe(RecipeLookupValidationTarget target, GTRecipe queryRecipe,
            List<GTRecipe> trieMatches) {
            StringBuilder issue = new StringBuilder();
            issue.append("map=")
                .append(target.mapName)
                .append('\n')
                .append("queryRecipe=")
                .append(describeRecipeForValidation(queryRecipe));
            issue.append('\n')
                .append("lookupMatches=")
                .append(trieMatches.isEmpty() ? "[]" : describeRecipeListForValidation(trieMatches));
            issues.add(issue.toString());
        }

        private void addError(RecipeLookupValidationTarget target, @Nullable GTRecipe queryRecipe, String action,
            RuntimeException error) {
            StringBuilder issue = new StringBuilder();
            issue.append("map=")
                .append(target.mapName)
                .append('\n')
                .append("error while ")
                .append(action)
                .append(": ")
                .append(error);
            if (queryRecipe != null) {
                issue.append('\n')
                    .append("queryRecipe=")
                    .append(describeRecipeForValidation(queryRecipe));
            }
            issues.add(issue.toString());
            errors.add(error);
        }

        private IllegalStateException buildException() {
            int totalIssues = issues.size() + unresolvedOreDictIssues.size();
            StringBuilder message = new StringBuilder();
            message.append("GT recipe lookup validation found ")
                .append(totalIssues)
                .append(" issue(s) across ")
                .append(targets.size())
                .append(" map(s).")
                .append("\nlookup mismatch(es)=")
                .append(issues.size())
                .append(", unresolved oredict input(s)=")
                .append(unresolvedOreDictIssues.size())
                .append("\nskipped recipe(s): disabled=")
                .append(skippedDisabledRecipes)
                .append(", fake=")
                .append(skippedFakeRecipes)
                .append(", noLookupIngredients=")
                .append(skippedNoLookupIngredientRecipes);
            int issueIndex = 1;
            for (int i = 0; i < issues.size(); i++) {
                message.append("\n\n")
                    .append(issueIndex++)
                    .append(") ")
                    .append(issues.get(i));
            }
            for (int i = 0; i < unresolvedOreDictIssues.size(); i++) {
                message.append("\n\n")
                    .append(issueIndex++)
                    .append(") ")
                    .append(unresolvedOreDictIssues.get(i));
            }

            IllegalStateException exception = new IllegalStateException(message.toString());
            for (RuntimeException error : errors) {
                exception.addSuppressed(error);
            }
            return exception;
        }

        private void maybeLogProgress() {
            long now = System.nanoTime();
            if (processedRecipes == totalRecipes || processedRecipes % RECIPE_PROGRESS_INTERVAL == 0
                || now - lastProgressNanos >= PROGRESS_LOG_INTERVAL_NANOS) {
                logProgress("running");
                lastProgressNanos = now;
            }
        }

        private void logProgress(String phase) {
            long elapsedNanos = System.nanoTime() - startNanos;
            double percent = totalRecipes == 0 ? 100.0 : processedRecipes * 100.0 / totalRecipes;
            LOGGER.info(
                String.format(
                    Locale.ROOT,
                    "GTRecipeLookupValidator: %s maps=%d/%d recipes=%d/%d %.1f%% elapsed=%s eta=%s issues=%d lookupMismatches=%d unresolvedOreDict=%d skippedDisabled=%d skippedFake=%d skippedNoLookup=%d",
                    phase,
                    processedMaps,
                    targets.size(),
                    processedRecipes,
                    totalRecipes,
                    percent,
                    formatDuration(elapsedNanos),
                    estimateEta(elapsedNanos),
                    totalIssueCount(),
                    issues.size(),
                    unresolvedOreDictIssues.size(),
                    skippedDisabledRecipes,
                    skippedFakeRecipes,
                    skippedNoLookupIngredientRecipes));
        }

        private void logMapProgress(String phase, RecipeLookupValidationTarget target, int mapRecipes, int mapIndex) {
            long elapsedNanos = System.nanoTime() - startNanos;
            LOGGER.info(
                String.format(
                    Locale.ROOT,
                    "GTRecipeLookupValidator: %s map=%s mapRecipes=%d maps=%d/%d recipes=%d/%d elapsed=%s issues=%d lookupMismatches=%d unresolvedOreDict=%d skippedDisabled=%d skippedFake=%d skippedNoLookup=%d",
                    phase,
                    target.mapName,
                    mapRecipes,
                    mapIndex,
                    targets.size(),
                    processedRecipes,
                    totalRecipes,
                    formatDuration(elapsedNanos),
                    totalIssueCount(),
                    issues.size(),
                    unresolvedOreDictIssues.size(),
                    skippedDisabledRecipes,
                    skippedFakeRecipes,
                    skippedNoLookupIngredientRecipes));
        }

        private int totalIssueCount() {
            return issues.size() + unresolvedOreDictIssues.size();
        }

        private String estimateEta(long elapsedNanos) {
            if (processedRecipes <= 0 || totalRecipes <= 0) {
                return "?";
            }
            long remainingRecipes = totalRecipes - processedRecipes;
            long etaNanos = (long) (elapsedNanos * (remainingRecipes / (double) processedRecipes));
            return formatDuration(etaNanos);
        }

        private String formatDuration(long nanos) {
            long seconds = Math.max(0L, nanos / 1_000_000_000L);
            return String.format(Locale.ROOT, "%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60);
        }
    }

    private static String describeRecipeCategory(GTRecipe recipe) {
        RecipeCategory category = recipe.getRecipeCategory();
        return category == null ? "<none>" : String.valueOf(category.unlocalizedName);
    }

    private static String describeRecipeOwners(GTRecipe recipe) {
        if (recipe.owners == null) {
            return "<disabled>";
        }
        if (recipe.owners.isEmpty()) {
            return "[]";
        }
        return recipe.owners.stream()
            .map(RecipeMapBackend::describeModContainer)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String describeModContainer(@Nullable ModContainer owner) {
        return owner == null ? "null" : owner.getModId();
    }

    private static String describeRecipeStackTrace(GTRecipe recipe) {
        if (recipe.stackTraces == null) {
            return "<disabled>";
        }
        if (recipe.stackTraces.isEmpty()) {
            return "[]";
        }
        List<String> stackTrace = recipe.stackTraces.get(recipe.stackTraces.size() - 1);
        return stackTrace.stream()
            .limit(12)
            .collect(Collectors.joining(" <- ", "[", stackTrace.size() > 12 ? " <- ...]" : "]"));
    }

    private static String describeItemStacks(@Nullable ItemStack[] stacks) {
        if (stacks == null) {
            return "<null>";
        }
        return Arrays.stream(stacks)
            .map(RecipeMapBackend::describeItemStack)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String describeItemStack(@Nullable ItemStack stack) {
        if (stack == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        Item item = stack.getItem();
        Object registryName = item == null ? null : Item.itemRegistry.getNameForObject(item);
        builder.append(registryName == null ? "<unregistered>" : registryName)
            .append(':')
            .append(stack.getItemDamage())
            .append(" x")
            .append(stack.stackSize)
            .append(" (")
            .append(item == null ? "<null item>" : stack.getUnlocalizedName())
            .append(')');
        if (stack.hasTagCompound()) {
            builder.append(" tag=")
                .append(stack.getTagCompound());
        }
        return builder.toString();
    }

    private static String describeFluidStacks(@Nullable FluidStack[] fluids) {
        if (fluids == null) {
            return "<null>";
        }
        return Arrays.stream(fluids)
            .map(RecipeMapBackend::describeFluidStack)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String describeFluidStack(@Nullable FluidStack fluid) {
        if (fluid == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(
            fluid.getFluid() == null ? "<null fluid>"
                : fluid.getFluid()
                    .getName())
            .append(" x")
            .append(fluid.amount);
        if (fluid.tag != null) {
            builder.append(" tag=")
                .append(fluid.tag);
        }
        return builder.toString();
    }

    private static String describeObject(@Nullable Object object) {
        if (object instanceof ItemStack) {
            return describeItemStack((ItemStack) object);
        }
        return String.valueOf(object);
    }

    private Stream<GTRecipe> collisionCandidateStream(@Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes,
        @Nullable RecipeLookupProfile profile) {
        Stream<GTRecipe> candidates = lookupCandidateStream(items, fluids, profile);
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
        profile.recordTrieLookupSetup();
        long start = System.nanoTime();
        Stream<GTRecipe> stream = lookupCandidateStream(items, fluids);
        profile.addTrieLookupSetupNanos(System.nanoTime() - start);
        return stream;
    }

    protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
        @Nullable FluidStack @NotNull [] fluids) {
        List<List<GTRecipeLookupIngredient>> ingredients = new ArrayList<>();

        for (ItemStack item : items) {
            if (item == null) continue;

            List<GTRecipeLookupIngredient> group = new ArrayList<>();
            addRuntimeItemStackLookupIngredients(group, item);
            addRuntimeOreDictLookupIngredients(group, item);
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

    static void addRuntimeItemStackLookupIngredients(List<GTRecipeLookupIngredient> group, ItemStack item) {
        addLookupIngredient(group, GTItemStackLookupIngredient.fromRuntime(item));
        addLookupIngredient(group, GTItemStackLookupIngredient.fromRuntimeWildcard(item));
        if (item.hasTagCompound()) {
            addLookupIngredient(group, GTItemStackLookupIngredient.fromNbtSensitiveRecipe(item));
        }
    }

    private static void addRuntimeOreDictLookupIngredients(List<GTRecipeLookupIngredient> group, ItemStack item) {
        for (int oreId : OreDictionary.getOreIDs(item)) {
            addLookupIngredient(group, new GTOreDictLookupIngredient(oreId));
        }
    }

    private static void addLookupIngredient(List<GTRecipeLookupIngredient> group, GTRecipeLookupIngredient ingredient) {
        if (!group.contains(ingredient)) {
            group.add(ingredient);
        }
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

        private void recordTrieLookupSetup() {
            stats.recordTrieLookupSetup();
        }

        private void addTrieLookupSetupNanos(long nanos) {
            stats.addTrieLookupSetupNanos(nanos);
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
        private long trieLookupSetups;
        private long trieLookupSetupNanos;
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

        private synchronized void recordTrieLookupSetup() {
            trieLookupSetups++;
        }

        private synchronized void addTrieLookupSetupNanos(long nanos) {
            trieLookupSetupNanos += nanos;
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
                        + "cacheMapProbes=%d cacheMapCandidates=%d trieLookupSetups=%d trieLookupSetupMs=%.3f "
                        + "trieCandidates=%d trieCandidatesPerLookupSetup=%.3f matched=%d fallbackProbes=%d fallbackHits=%d "
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
                    trieLookupSetups,
                    nanosToMillis(trieLookupSetupNanos),
                    trieCandidates,
                    average(trieCandidates, trieLookupSetups),
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
