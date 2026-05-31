package gregtech.api.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.common.ModContainer;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

public final class RecipeLookupValidator {

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");

    public static final String VALIDATE_LOOKUP_PROPERTY = "gt.recipe.lookup.validate";
    public static final String CAPTURE_CALLSITE_PROPERTY = VALIDATE_LOOKUP_PROPERTY + ".capture_callsite";
    private static final String LARGE_BOILER_FAKE_FUELS_MAP_NAME = "gt.recipe.largeboilerfakefuels";
    private static final String QFT_RECIPE_MAP_NAME = "gtpp.recipe.quantumforcesmelter";
    private static final int RECIPE_PROGRESS_INTERVAL = 100;
    private static final long PROGRESS_LOG_INTERVAL_NANOS = 5_000_000_000L;
    private static final int LOOKUP_MATCH_SAMPLE_LIMIT = 16;
    private static final int LOOKUP_CANDIDATE_SCAN_LIMIT = Math
        .max(1, Integer.getInteger("gt.recipe.lookup.validate.max_candidates", 4096));

    private final List<RecipeLookupValidationTarget> targets;
    private final List<String> issues = new ArrayList<>();
    private final List<String> recipeConflictIssues = new ArrayList<>();
    private final List<String> unresolvedOreDictIssues = new ArrayList<>();
    private final List<RuntimeException> errors = new ArrayList<>();
    private final Set<String> reportedRecipeConflictKeys = new HashSet<>();
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

    public static void validateLookup(String mapName, RecipeMapBackend backend) {
        new RecipeLookupValidator(Collections.singletonList(new RecipeLookupValidationTarget(mapName, backend)))
            .validate();
    }

    public static boolean shouldValidateLookup() {
        return Boolean.getBoolean(VALIDATE_LOOKUP_PROPERTY);
    }

    public static boolean shouldCaptureRecipeCallsites() {
        String propertyValue = System.getProperty(CAPTURE_CALLSITE_PROPERTY);
        if (propertyValue != null) {
            return Boolean.parseBoolean(propertyValue);
        }
        return shouldValidateLookup();
    }

    private static String recipeMapName(@Nullable RecipeMap<?> recipeMap) {
        return recipeMap == null ? "<unbound>" : recipeMap.unlocalizedName;
    }

    public static String describeRecipeForValidation(GTRecipe recipe) {
        return "identity=" + System.identityHashCode(recipe)
            + ", category="
            + describeRecipeCategory(recipe)
            + ", owners="
            + describeRecipeOwners(recipe)
            + ", callsite="
            + describeRecipeCallsite(recipe)
            + ", metadata="
            + describeRecipeMetadata(recipe)
            + ", inputs="
            + describeItemStacks(recipe.mInputs)
            + ", fluidInputs="
            + describeFluidStacks(recipe.mFluidInputs)
            + ", outputs="
            + describeItemStacks(recipe.mOutputs)
            + ", fluidOutputs="
            + describeFluidStacks(recipe.mFluidOutputs)
            + ", specialValue="
            + recipe.mSpecialValue
            + ", special="
            + describeObject(recipe.mSpecialItems)
            + ", stackTrace="
            + describeRecipeStackTrace(recipe);
    }

    private static String describeRecipeListForValidation(List<GTRecipe> recipes) {
        return recipes.stream()
            .map(RecipeLookupValidator::describeRecipeForValidation)
            .collect(Collectors.joining("\n    ", "[\n    ", "\n]"));
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
            .map(RecipeLookupValidator::describeModContainer)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    private static String describeRecipeCallsite(GTRecipe recipe) {
        if (recipe.stackTraces == null) {
            return "<disabled>";
        }
        if (recipe.stackTraces.isEmpty()) {
            return "[]";
        }
        List<String> stackTrace = recipe.stackTraces.get(recipe.stackTraces.size() - 1);
        for (String stackTraceElement : stackTrace) {
            if (!isInternalRecipeCallsite(stackTraceElement)) {
                return stackTraceElement;
            }
        }
        return stackTrace.isEmpty() ? "[]" : stackTrace.get(0);
    }

    private static boolean isInternalRecipeCallsite(String stackTraceElement) {
        return stackTraceElement.contains("gregtech.api.util.GTRecipe$");
    }

    private static String describeModContainer(@Nullable ModContainer owner) {
        return owner == null ? "null" : owner.getModId();
    }

    private static String describeRecipeMetadata(GTRecipe recipe) {
        try {
            if (recipe.getMetadataStorage() == null || recipe.getMetadataStorage()
                .getEntries()
                .isEmpty()) {
                return "[]";
            }
            return recipe.getMetadataStorage()
                .getEntries()
                .stream()
                .map(RecipeLookupValidator::describeRecipeMetadataEntry)
                .collect(Collectors.joining(", ", "[", "]"));
        } catch (LinkageError | RuntimeException e) {
            return "<unavailable metadata: " + e.getClass()
                .getSimpleName() + ">";
        }
    }

    private static String describeRecipeMetadataEntry(Map.Entry<RecipeMetadataKey<?>, Object> entry) {
        return describeRecipeMetadataKey(entry.getKey()) + "=" + describeObject(entry.getValue());
    }

    private static String describeRecipeMetadataKey(RecipeMetadataKey<?> key) {
        String text = String.valueOf(key);
        String marker = "identifier=";
        int markerStart = text.indexOf(marker);
        if (markerStart < 0) {
            return text;
        }
        int identifierStart = markerStart + marker.length();
        int identifierEnd = text.indexOf('\'', identifierStart);
        if (identifierEnd < 0) {
            identifierEnd = text.indexOf('}', identifierStart);
        }
        if (identifierEnd <= identifierStart) {
            return text;
        }
        return text.substring(identifierStart, identifierEnd);
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
            .map(RecipeLookupValidator::describeItemStack)
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
            .append(item == null ? "<null item>" : safeUnlocalizedName(stack))
            .append(')');
        if (stack.hasTagCompound()) {
            builder.append(" tag=")
                .append(stack.getTagCompound());
        }
        return builder.toString();
    }

    private static String safeUnlocalizedName(ItemStack stack) {
        try {
            return stack.getUnlocalizedName();
        } catch (LinkageError | RuntimeException e) {
            return "<unavailable name: " + e.getClass()
                .getSimpleName() + ">";
        }
    }

    private static String describeFluidStacks(@Nullable FluidStack[] fluids) {
        if (fluids == null) {
            return "<null>";
        }
        return Arrays.stream(fluids)
            .map(RecipeLookupValidator::describeFluidStack)
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
                        validateRecipe(target, recipes, recipe);
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
        if (!issues.isEmpty() || !recipeConflictIssues.isEmpty() || !unresolvedOreDictIssues.isEmpty()) {
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

    private void validateRecipe(RecipeLookupValidationTarget target, List<GTRecipe> recipes, GTRecipe recipe) {
        RecipeMapBackend backend = target.backend;
        ItemStack[] rawItems = recipe.mInputs == null ? new ItemStack[0] : recipe.mInputs;
        ItemStack[] items = GTOreDictUnificator.getStackArray(true, (Object[]) rawItems);
        FluidStack[] fluids = recipe.mFluidInputs == null ? new FluidStack[0] : recipe.mFluidInputs;
        ItemStack specialSlot = recipe.mSpecialItems instanceof ItemStack ? (ItemStack) recipe.mSpecialItems : null;
        String preLookupRejectReason = preLookupRejectReason(backend, items, fluids);
        if (preLookupRejectReason != null) {
            String unresolvedOreDictReason = unresolvedOreDictInputReason(recipe, rawItems);
            if (unresolvedOreDictReason != null) {
                addUnresolvedOreDictRecipe(target, recipe, unresolvedOreDictReason, preLookupRejectReason);
            } else {
                addPreLookupRejectedRecipe(target, recipe, preLookupRejectReason);
            }
            return;
        }
        List<GTRecipe> expectedMatches = null;
        ValidationLookupResult lookupMatches = null;

        try {
            expectedMatches = fullScanMatches(backend, recipes, items, fluids, specialSlot);
        } catch (RuntimeException e) {
            addError(target, recipe, "running full scan lookup", e);
        }

        try {
            lookupMatches = lookupMatches(backend, items, fluids, specialSlot);
        } catch (RuntimeException e) {
            addError(target, recipe, "running trie lookup", e);
        }

        if (expectedMatches == null || lookupMatches == null) {
            return;
        }

        List<GTRecipe> conflictingMatches = recipeConflictMatches(target, recipe, expectedMatches);
        if (conflictingMatches.size() > 1) {
            addRecipeConflict(target, recipes, recipe, conflictingMatches);
        }

        if (lookupMatches.truncated || !matchesExpectedLookup(expectedMatches, lookupMatches.matches)) {
            addLookupMismatch(target, recipe, expectedMatches, lookupMatches);
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
            unresolvedSlots.add("slot=" + i + ", oreDictId=" + oreDictId + ", oreDictName=" + oreDictName(oreDictId));
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

    private List<GTRecipe> fullScanMatches(RecipeMapBackend backend, List<GTRecipe> recipes, ItemStack[] items,
        FluidStack[] fluids, @Nullable ItemStack specialSlot) {
        List<GTRecipe> matches = new ArrayList<>();
        for (GTRecipe candidate : recipes) {
            if (matchesAfterFinalFilters(backend, candidate, items, fluids, specialSlot)) {
                matches.add(candidate);
            }
        }
        return matches;
    }

    private ValidationLookupResult lookupMatches(RecipeMapBackend backend, ItemStack[] items, FluidStack[] fluids,
        @Nullable ItemStack specialSlot) {
        Iterator<GTRecipe> candidates = backend.lookupCandidateStream(items, fluids)
            .iterator();
        Predicate<GTRecipe> distinct = RecipeMapBackend.distinctByIdentity();
        List<GTRecipe> matches = new ArrayList<>();
        List<GTRecipe> sampleRejectedCandidates = new ArrayList<>(LOOKUP_MATCH_SAMPLE_LIMIT);
        int rawCandidates = 0;
        int filteredMatches = 0;
        while (candidates.hasNext()) {
            GTRecipe candidate = candidates.next();
            rawCandidates++;
            if (!distinct.test(candidate)) {
                continue;
            }
            if (!matchesAfterFinalFilters(backend, candidate, items, fluids, specialSlot)) {
                if (sampleRejectedCandidates.size() < LOOKUP_MATCH_SAMPLE_LIMIT) {
                    sampleRejectedCandidates.add(candidate);
                }
            } else {
                filteredMatches++;
                matches.add(candidate);
            }
            if (rawCandidates >= LOOKUP_CANDIDATE_SCAN_LIMIT) {
                return new ValidationLookupResult(
                    matches,
                    sampleRejectedCandidates,
                    rawCandidates,
                    filteredMatches,
                    true);
            }
        }
        return new ValidationLookupResult(matches, sampleRejectedCandidates, rawCandidates, filteredMatches, false);
    }

    private boolean matchesAfterFinalFilters(RecipeMapBackend backend, GTRecipe candidate, ItemStack[] items,
        FluidStack[] fluids, @Nullable ItemStack specialSlot) {
        if (!backend.filterFindRecipe(candidate, items, fluids, specialSlot, false)) {
            return false;
        }
        return backend.modifyFoundRecipe(candidate, items, fluids, specialSlot) != null;
    }

    private boolean matchesExpectedLookup(List<GTRecipe> expectedMatches, List<GTRecipe> lookupMatches) {
        for (GTRecipe expectedMatch : expectedMatches) {
            if (!containsRecipeIdentity(lookupMatches, expectedMatch)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsRecipeIdentity(List<GTRecipe> recipes, GTRecipe recipe) {
        for (GTRecipe candidate : recipes) {
            if (candidate == recipe) {
                return true;
            }
        }
        return false;
    }

    private List<GTRecipe> recipeConflictMatches(RecipeLookupValidationTarget target, GTRecipe queryRecipe,
        List<GTRecipe> expectedMatches) {
        if (LARGE_BOILER_FAKE_FUELS_MAP_NAME.equals(target.mapName)) {
            return Collections.emptyList();
        }
        if (!QFT_RECIPE_MAP_NAME.equals(target.mapName)) {
            return expectedMatches;
        }

        ItemStack queryCatalyst = queryRecipe.getMetadata(GTRecipeConstants.QFT_CATALYST);
        if (queryCatalyst == null) {
            return expectedMatches;
        }

        return expectedMatches.stream()
            .filter(match -> sameQftCatalyst(queryCatalyst, match))
            .collect(Collectors.toList());
    }

    private boolean sameQftCatalyst(ItemStack queryCatalyst, GTRecipe candidate) {
        ItemStack candidateCatalyst = candidate.getMetadata(GTRecipeConstants.QFT_CATALYST);
        return GTUtility.areStacksEqual(queryCatalyst, candidateCatalyst, false);
    }

    private void addRecipeConflict(RecipeLookupValidationTarget target, List<GTRecipe> recipes, GTRecipe queryRecipe,
        List<GTRecipe> conflictingMatches) {
        String conflictKey = recipeConflictKey(recipes, conflictingMatches);
        if (!reportedRecipeConflictKeys.add(target.mapName + ":" + conflictKey)) {
            return;
        }

        StringBuilder issue = new StringBuilder();
        issue.append("map=")
            .append(target.mapName)
            .append('\n')
            .append("queryRecipe=")
            .append(describeRecipeForValidation(queryRecipe));
        issue.append('\n')
            .append("conflictingMatches=")
            .append(describeRecipeListForValidation(sampleRecipes(conflictingMatches)));
        issue.append('\n')
            .append("conflictMatchCount=")
            .append(conflictingMatches.size());
        recipeConflictIssues.add(issue.toString());
    }

    private String recipeConflictKey(List<GTRecipe> recipes, List<GTRecipe> conflictingMatches) {
        StringBuilder key = new StringBuilder();
        for (GTRecipe conflictingMatch : conflictingMatches) {
            if (key.length() > 0) {
                key.append(',');
            }
            key.append(indexOfRecipeIdentity(recipes, conflictingMatch));
        }
        return key.toString();
    }

    private int indexOfRecipeIdentity(List<GTRecipe> recipes, GTRecipe recipe) {
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i) == recipe) {
                return i;
            }
        }
        return -1;
    }

    private void addPreLookupRejectedRecipe(RecipeLookupValidationTarget target, GTRecipe queryRecipe, String reason) {
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

    private void addLookupMismatch(RecipeLookupValidationTarget target, GTRecipe queryRecipe,
        List<GTRecipe> expectedMatches, ValidationLookupResult lookupMatches) {
        StringBuilder issue = new StringBuilder();
        issue.append("map=")
            .append(target.mapName)
            .append('\n')
            .append("queryRecipe=")
            .append(describeRecipeForValidation(queryRecipe));
        issue.append('\n')
            .append("expectedMatches=")
            .append(expectedMatches.isEmpty() ? "[]" : describeRecipeListForValidation(sampleRecipes(expectedMatches)));
        issue.append('\n')
            .append("lookupMatches=")
            .append(
                lookupMatches.matches.isEmpty() ? "[]"
                    : describeRecipeListForValidation(sampleRecipes(lookupMatches.matches)));
        issue.append('\n')
            .append("lookupRejectedCandidates=")
            .append(
                lookupMatches.sampleRejectedCandidates.isEmpty() ? "[]"
                    : describeRecipeListForValidation(lookupMatches.sampleRejectedCandidates));
        issue.append('\n')
            .append("expectedMatchCount=")
            .append(expectedMatches.size())
            .append(", lookupMatchCount=")
            .append(lookupMatches.matches.size());
        issue.append('\n')
            .append("lookupCandidateScan=")
            .append(lookupMatches.truncated ? "truncated" : "exhausted")
            .append(", rawCandidates=")
            .append(lookupMatches.rawCandidates)
            .append(", filteredMatches=")
            .append(lookupMatches.filteredMatches)
            .append(", max=")
            .append(LOOKUP_CANDIDATE_SCAN_LIMIT);
        issues.add(issue.toString());
    }

    private List<GTRecipe> sampleRecipes(List<GTRecipe> recipes) {
        return recipes.size() <= LOOKUP_MATCH_SAMPLE_LIMIT ? recipes : recipes.subList(0, LOOKUP_MATCH_SAMPLE_LIMIT);
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
        int totalIssues = issues.size() + recipeConflictIssues.size() + unresolvedOreDictIssues.size();
        StringBuilder message = new StringBuilder();
        message.append("GT recipe lookup validation found ")
            .append(totalIssues)
            .append(" issue(s) across ")
            .append(targets.size())
            .append(" map(s).")
            .append("\nlookup mismatch(es)=")
            .append(issues.size())
            .append(", recipe conflict(s)=")
            .append(recipeConflictIssues.size())
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
        for (int i = 0; i < recipeConflictIssues.size(); i++) {
            message.append("\n\n")
                .append(issueIndex++)
                .append(") ")
                .append(recipeConflictIssues.get(i));
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
                "GTRecipeLookupValidator: %s maps=%d/%d recipes=%d/%d %.1f%% elapsed=%s eta=%s issues=%d lookupMismatches=%d recipeConflicts=%d unresolvedOreDict=%d skippedDisabled=%d skippedFake=%d skippedNoLookup=%d",
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
                recipeConflictIssues.size(),
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
                "GTRecipeLookupValidator: %s map=%s mapRecipes=%d maps=%d/%d recipes=%d/%d elapsed=%s issues=%d lookupMismatches=%d recipeConflicts=%d unresolvedOreDict=%d skippedDisabled=%d skippedFake=%d skippedNoLookup=%d",
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
                recipeConflictIssues.size(),
                unresolvedOreDictIssues.size(),
                skippedDisabledRecipes,
                skippedFakeRecipes,
                skippedNoLookupIngredientRecipes));
    }

    private int totalIssueCount() {
        return issues.size() + recipeConflictIssues.size() + unresolvedOreDictIssues.size();
    }

    private static final class ValidationLookupResult {

        private final List<GTRecipe> matches;
        private final List<GTRecipe> sampleRejectedCandidates;
        private final int rawCandidates;
        private final int filteredMatches;
        private final boolean truncated;

        private ValidationLookupResult(List<GTRecipe> matches, List<GTRecipe> sampleRejectedCandidates,
            int rawCandidates, int filteredMatches, boolean truncated) {
            this.matches = matches;
            this.sampleRejectedCandidates = sampleRejectedCandidates;
            this.rawCandidates = rawCandidates;
            this.filteredMatches = filteredMatches;
            this.truncated = truncated;
        }
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

    static final class RecipeLookupValidationTarget {

        private final String mapName;
        private final RecipeMapBackend backend;

        private RecipeLookupValidationTarget(String mapName, RecipeMapBackend backend) {
            this.mapName = mapName;
            this.backend = backend;
        }
    }
}
