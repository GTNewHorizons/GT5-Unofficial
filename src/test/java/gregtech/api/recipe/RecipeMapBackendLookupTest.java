package gregtech.api.recipe;

import static gregtech.api.enums.OrePrefixes.circuit;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import cpw.mods.fml.common.registry.RegistryDelegate;
import gregtech.api.enums.Materials;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.lookup.GTFluidLookupIngredient;
import gregtech.api.recipe.lookup.GTItemDataLookupIngredient;
import gregtech.api.recipe.lookup.GTItemStackLookupIngredient;
import gregtech.api.recipe.lookup.GTRecipeLookup;
import gregtech.api.recipe.lookup.GTRecipeLookupIngredient;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.recipe.metadata.RecipeMetadataStorage;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import sun.reflect.ReflectionFactory;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
class RecipeMapBackendLookupTest {

    private static final Constructor<GTRecipe> GT_RECIPE_CONSTRUCTOR = constructorFor(GTRecipe.class);
    private static final Constructor<GTRecipe.GTRecipe_WithAlt> GT_RECIPE_WITH_ALT_CONSTRUCTOR = constructorFor(
        GTRecipe.GTRecipe_WithAlt.class);
    private static final Constructor<RecipeCategory> RECIPE_CATEGORY_CONSTRUCTOR = constructorFor(RecipeCategory.class);
    private static final Constructor<FluidStack> FLUID_STACK_CONSTRUCTOR = constructorFor(FluidStack.class);

    @Test
    void trieCandidatesKeepLookupOrderOnRuntimePath() {
        Item input = item("lookup.order.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe firstRegistered = recipe(input, item("lookup.order.first.output"), category);
        GTRecipe secondRegistered = recipe(input, item("lookup.order.second.output"), category);
        RecipeMapBackend backend = new ReversedCandidateBackend(secondRegistered, firstRegistered);
        backend.compileRecipe(firstRegistered);
        backend.compileRecipe(secondRegistered);

        assertSame(
            secondRegistered,
            backend
                .matchRecipeStream(
                    new ItemStack[] { new ItemStack(input, 1, 0) },
                    new FluidStack[0],
                    null,
                    null,
                    false,
                    false,
                    false)
                .findFirst()
                .orElse(null));

        List<GTRecipe> allMatches = backend
            .matchRecipeStream(
                new ItemStack[] { new ItemStack(input, 1, 0) },
                new FluidStack[0],
                null,
                null,
                false,
                false,
                false)
            .collect(Collectors.toList());
        assertEquals(Arrays.asList(secondRegistered, firstRegistered), allMatches);
    }

    @Test
    void compileRecipeMakesRecipeImmediatelyFindable() {
        ensureMinecraftStackComparisonItem();
        Item input = item("lookup.incremental.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(input, item("lookup.incremental.output"), category);
        RecipeMapBackend backend = new NoContainsIndexBackend();

        backend.compileRecipe(recipe);

        assertSame(
            recipe,
            backend
                .matchRecipeStream(
                    new ItemStack[] { new ItemStack(input, 1, 0) },
                    new FluidStack[0],
                    null,
                    null,
                    false,
                    false,
                    false)
                .findFirst()
                .orElse(null));
    }

    @Test
    void incrementalRecipeInsertionUsesFrequencyOrderingForNewRecipe() {
        ensureMinecraftStackComparisonItem();
        Item common = item("lookup.incremental.frequency.common");
        Item rare = item("lookup.incremental.frequency.rare");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe commonOnlyRecipe = recipe(common, item("lookup.incremental.frequency.common.output"), category);
        GTRecipe twoInputRecipe = recipe(
            new ItemStack[] { new ItemStack(common, 1, 0), new ItemStack(rare, 1, 0) },
            new FluidStack[0],
            item("lookup.incremental.frequency.two_input.output"),
            category);
        RecipeMapBackend backend = new TrueFilterLookupBackend();
        backend.compileRecipe(commonOnlyRecipe);

        backend.compileRecipe(twoInputRecipe);

        List<GTRecipe> matches = backend
            .matchRecipeStream(
                new ItemStack[] { new ItemStack(common, 1, 0), new ItemStack(rare, 1, 0) },
                new FluidStack[0],
                null,
                null,
                false,
                false,
                false)
            .collect(Collectors.toList());
        assertEquals(Arrays.asList(commonOnlyRecipe, twoInputRecipe), matches);
    }

    @Test
    void collisionCandidateStreamDoesNotSortCandidates() {
        Item input = item("lookup.collision.unsorted.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe firstRegistered = recipe(input, item("lookup.collision.unsorted.first.output"), category);
        GTRecipe secondRegistered = recipe(input, item("lookup.collision.unsorted.second.output"), category);
        RecipeMapBackend backend = new ReversedCandidateBackend(secondRegistered, firstRegistered);
        backend.compileRecipe(firstRegistered);
        backend.compileRecipe(secondRegistered);

        List<GTRecipe> collisionMatches = backend
            .matchRecipeStream(
                new ItemStack[] { new ItemStack(input, 1, 0) },
                new FluidStack[0],
                null,
                null,
                false,
                true,
                true)
            .collect(Collectors.toList());

        assertEquals(Arrays.asList(secondRegistered, firstRegistered), collisionMatches);
    }

    @Test
    void cachedRecipeHitDoesNotBuildTrieCandidates() {
        Item input = item("lookup.cached.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(input, item("lookup.cached.output"), category);
        GTRecipe fallbackRecipe = recipe(input, item("lookup.cached.fallback.output"), category);
        CountingLookupBackend backend = new CountingLookupBackend(fallbackRecipe);
        backend.compileRecipe(recipe);

        assertSame(
            recipe,
            backend
                .matchRecipeStream(
                    new ItemStack[] { new ItemStack(input, 1, 0) },
                    new FluidStack[0],
                    null,
                    recipe,
                    false,
                    false,
                    false)
                .findFirst()
                .orElse(null));
        assertEquals(0, backend.lookupCandidateStreamCalls);
    }

    @Test
    void cacheMapHitDoesNotBuildTrieCandidates() {
        Item input = item("lookup.cachemap.input");
        ItemStack[] items = { new ItemStack(input, 1, 0) };
        FluidStack[] fluids = new FluidStack[0];
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(input, item("lookup.cachemap.output"), category);
        GTRecipe fallbackRecipe = recipe(input, item("lookup.cachemap.fallback.output"), category);
        CountingLookupBackend backend = new CountingLookupBackend(fallbackRecipe);
        backend.compileRecipe(recipe);
        backend.cache(items, fluids, recipe);

        assertSame(
            recipe,
            backend.matchRecipeStream(items, fluids, null, null, false, false, false)
                .findFirst()
                .orElse(null));
        assertEquals(0, backend.lookupCandidateStreamCalls);
    }

    @Test
    void runtimeTrieMissDoesNotUseDiagnosticFallbackOrWriteDiagnosticLog(@TempDir Path tempDir) throws Exception {
        File previousLogFile = GTLog.mLogFile;
        GTLog.mLogFile = tempDir.resolve("logs")
            .resolve("GregTech.log")
            .toFile();

        try {
            Item input = item("lookup.diagnostic.input");
            RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
            GTRecipe recipe = recipe(input, item("lookup.diagnostic.output"), category);
            RecipeMapBackend backend = new EmptyLookupBackend();
            backend.compileRecipe(recipe);

            assertFalse(
                backend
                    .matchRecipeStream(
                        new ItemStack[] { new ItemStack(input, 1, 0) },
                        new FluidStack[0],
                        null,
                        null,
                        false,
                        false,
                        false)
                    .findAny()
                    .isPresent());

            Path missLog = tempDir.resolve("logs")
                .resolve("RecipeLookupMisses.log");
            assertFalse(Files.exists(missLog));
        } finally {
            GTLog.mLogFile = previousLogFile;
        }
    }

    @Test
    void collisionTrieMissDoesNotUseDiagnosticFallback() {
        Item input = item("lookup.diagnostic.collision.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(input, item("lookup.diagnostic.collision.output"), category);
        RecipeMapBackend backend = new EmptyLookupBackend();
        backend.compileRecipe(recipe);

        assertFalse(
            backend
                .matchRecipeStream(
                    new ItemStack[] { new ItemStack(input, 1, 0) },
                    new FluidStack[0],
                    null,
                    null,
                    false,
                    true,
                    true)
                .findAny()
                .isPresent());
    }

    @Test
    void lookupVerifierAggregatesEveryMissingRecipeBeforeThrowing() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.validation.missing";
        MissingLookupBackend backend = new MissingLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe firstRecipe = recipe(
            item("lookup.validation.first.input"),
            item("lookup.validation.first.output"),
            category);
        GTRecipe secondRecipe = recipe(
            item("lookup.validation.second.input"),
            item("lookup.validation.second.output"),
            category);
        GTRecipe fakeRecipe = recipe(
            item("lookup.validation.skipped.fake.input"),
            item("lookup.validation.skipped.fake.output"),
            category);
        fakeRecipe.mFakeRecipe = true;
        GTRecipe disabledRecipe = recipe(
            item("lookup.validation.skipped.disabled.input"),
            item("lookup.validation.skipped.disabled.output"),
            category);
        disabledRecipe.mEnabled = false;
        backend.compileRecipe(firstRecipe);
        backend.compileRecipe(secondRecipe);
        backend.compileRecipe(fakeRecipe);
        backend.compileRecipe(disabledRecipe);
        backend.compileRecipe(recipeWithoutInputs(item("lookup.validation.skipped.no_lookup.output"), category));

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertTrue(
            error.getMessage()
                .contains(mapName));
        assertTrue(
            error.getMessage()
                .contains("lookup.validation.first.input"));
        assertTrue(
            error.getMessage()
                .contains("lookup.validation.second.input"));
        assertTrue(
            error.getMessage()
                .contains("skipped recipe(s): disabled=1, fake=1, noLookupIngredients=1"));
    }

    @Test
    void lookupVerifierReportsRecipeConflictWhenMultipleRecipesMatchSameQuery() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.validation.order";
        Item input = item("lookup.validation.order.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe firstRegistered = recipe(input, item("lookup.validation.order.first.output"), category);
        GTRecipe secondRegistered = recipe(input, item("lookup.validation.order.second.output"), category);
        RecipeMapBackend backend = new ReversedCandidateBackend(secondRegistered, firstRegistered);
        backend.compileRecipe(firstRegistered);
        backend.compileRecipe(secondRegistered);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertRecipeConflict(error, mapName, 1);
    }

    @Test
    void lookupVerifierReportsConflictWhenEquivalentTailMatchesExist() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.validation.tail_order";
        Item input = item("lookup.validation.tail_order.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe firstRegistered = recipe(input, item("lookup.validation.tail_order.first.output"), category);
        GTRecipe secondRegistered = recipe(input, item("lookup.validation.tail_order.second.output"), category);
        GTRecipe thirdRegistered = recipe(input, item("lookup.validation.tail_order.third.output"), category);
        RecipeMapBackend backend = new ReversedCandidateBackend(firstRegistered, thirdRegistered, secondRegistered);
        backend.compileRecipe(firstRegistered);
        backend.compileRecipe(secondRegistered);
        backend.compileRecipe(thirdRegistered);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertRecipeConflict(error, mapName, 1);
    }

    @Test
    void lookupVerifierTreatsQftCatalystAsConflictSelector() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gtpp.recipe.quantumforcesmelter";
        Item input = item("lookup.validation.qft_selector.input");
        Item catalyst = item("lookup.validation.qft_selector.catalyst");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe plasticRecipe = recipe(input, item("lookup.validation.qft_selector.plastic.output"), category);
        GTRecipe rubberRecipe = recipe(input, item("lookup.validation.qft_selector.rubber.output"), category);
        setMetadataStorage(plasticRecipe, qftCatalystMetadata(new ItemStack(catalyst, 1, 17)));
        setMetadataStorage(rubberRecipe, qftCatalystMetadata(new ItemStack(catalyst, 1, 18)));
        RecipeMapBackend backend = new ReversedCandidateBackend(rubberRecipe, plasticRecipe);
        backend.compileRecipe(plasticRecipe);
        backend.compileRecipe(rubberRecipe);

        assertDoesNotThrow(() -> RecipeLookupValidator.validateLookup(mapName, backend));
    }

    @Test
    void lookupVerifierReportsQftConflictWhenCatalystMatches() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gtpp.recipe.quantumforcesmelter";
        Item input = item("lookup.validation.qft_conflict.input");
        Item catalyst = item("lookup.validation.qft_conflict.catalyst");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe firstRecipe = recipe(input, item("lookup.validation.qft_conflict.first.output"), category);
        GTRecipe secondRecipe = recipe(input, item("lookup.validation.qft_conflict.second.output"), category);
        setMetadataStorage(firstRecipe, qftCatalystMetadata(new ItemStack(catalyst, 1, 17)));
        setMetadataStorage(secondRecipe, qftCatalystMetadata(new ItemStack(catalyst, 1, 17)));
        RecipeMapBackend backend = new ReversedCandidateBackend(secondRecipe, firstRecipe);
        backend.compileRecipe(firstRecipe);
        backend.compileRecipe(secondRecipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertRecipeConflict(error, mapName, 1);
    }

    @Test
    void lookupVerifierIgnoresLargeBoilerFakeFuelConflicts() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.largeboilerfakefuels";
        Item input = item("lookup.validation.large_boiler_fake_fuel.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe noContainerRecipe = recipe(
            input,
            item("lookup.validation.large_boiler_fake_fuel.no_output"),
            category);
        noContainerRecipe.mOutputs = new ItemStack[0];
        GTRecipe containerReturnRecipe = recipe(
            input,
            item("lookup.validation.large_boiler_fake_fuel.container_return"),
            category);
        RecipeMapBackend backend = new ReversedCandidateBackend(containerReturnRecipe, noContainerRecipe);
        backend.compileRecipe(noContainerRecipe);
        backend.compileRecipe(containerReturnRecipe);

        assertDoesNotThrow(() -> RecipeLookupValidator.validateLookup(mapName, backend));
    }

    @Test
    void lookupVerifierReportsConflictForWildcardRecipeInput() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.validation.wildcard_query";
        Item input = item("lookup.validation.wildcard_query.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe exactRecipe = recipe(
            new ItemStack[] { new ItemStack(input, 1, 0) },
            new FluidStack[0],
            item("lookup.validation.wildcard_query.exact.output"),
            category);
        GTRecipe wildcardRecipe = recipe(
            new ItemStack[] { new ItemStack(input, 1, OreDictionary.WILDCARD_VALUE) },
            new FluidStack[0],
            item("lookup.validation.wildcard_query.wildcard.output"),
            category);
        RecipeMapBackend backend = new WildcardQueryBackend(exactRecipe, wildcardRecipe);
        backend.compileRecipe(exactRecipe);
        backend.compileRecipe(wildcardRecipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertRecipeConflict(error, mapName, 1);
    }

    @Test
    void lookupVerifierAcceptsWildcardInputWhenNoCandidatePassesFinalFilters() {
        ensureMinecraftStackComparisonItem();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            new ItemStack[] {
                new ItemStack(item("lookup.validation.wildcard_rejected.input"), 1, OreDictionary.WILDCARD_VALUE) },
            new FluidStack[0],
            item("lookup.validation.wildcard_rejected.output"),
            category);
        FilterRejectingLookupBackend backend = new FilterRejectingLookupBackend(recipe);
        backend.compileRecipe(recipe);

        assertDoesNotThrow(
            () -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.wildcard_rejected", backend));
    }

    @Test
    void lookupVerifierReportsConflictForAmbiguousNbtSensitiveMatches() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.nbt_seed";
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        Item seedItem = item("lookup.validation.nbt_seed.input");
        GTRecipe firstRecipe = recipe(
            new ItemStack[] { tagged(seedItem, "a") },
            new FluidStack[0],
            item("lookup.validation.nbt_seed.first.output"),
            category);
        firstRecipe.isNBTSensitive = true;
        GTRecipe queryRecipe = recipe(
            new ItemStack[] { tagged(seedItem, "a") },
            new FluidStack[0],
            item("lookup.validation.nbt_seed.query.output"),
            category);
        queryRecipe.isNBTSensitive = true;
        RecipeMapBackend backend = new ReversedCandidateBackend(queryRecipe, firstRecipe);
        backend.compileRecipe(firstRecipe);
        backend.compileRecipe(queryRecipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertRecipeConflict(error, mapName, 1);
    }

    @Test
    void lookupVerifierSurvivesItemsThatThrowWhileDescribingName() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.validation.bad_name";
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            new ThrowingUnlocalizedNameItem(),
            item("lookup.validation.bad_name.output"),
            category);
        MissingLookupBackend backend = new MissingLookupBackend();
        backend.compileRecipe(recipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertTrue(
            error.getMessage()
                .contains("<unavailable name"));
        assertFalse(
            error.getMessage()
                .contains("error while preparing trie lookup"));
    }

    @Test
    void lookupVerifierDoesNotStopAfterFindingQueryRecipe() {
        ensureMinecraftStackComparisonItem();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            item("lookup.validation.short_circuit.input"),
            item("lookup.validation.short_circuit.output"),
            category);
        ThrowingAfterFirstCandidateBackend backend = new ThrowingAfterFirstCandidateBackend(recipe);
        backend.compileRecipe(recipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.short_circuit", backend));

        assertTrue(
            error.getMessage()
                .contains("error while running trie lookup"));
        assertEquals(1, error.getSuppressed().length);
        assertTrue(
            error.getSuppressed()[0].getMessage()
                .contains("validator exhausted candidate stream"));
    }

    @Test
    void lookupVerifierAcceptsCandidatesRejectedByFilterWhenFullScanAlsoRejects() {
        ensureMinecraftStackComparisonItem();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            item("lookup.validation.filter_reject.input"),
            item("lookup.validation.filter_reject.output"),
            category);
        FilterRejectingLookupBackend backend = new FilterRejectingLookupBackend(recipe);
        backend.compileRecipe(recipe);

        assertDoesNotThrow(() -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.filter_reject", backend));
    }

    @Test
    void lookupVerifierDoesNotNeedContainsIndexCandidates() {
        NoContainsIndexBackend backend = new NoContainsIndexBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        backend.compileRecipe(
            recipe(item("lookup.validation.unindexed.input"), item("lookup.validation.unindexed.output"), category));

        assertDoesNotThrow(() -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.unindexed", backend));
    }

    @Test
    void lookupVerifierReportsConflictForCrossUnificationMatches() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.cross_unification";
        NoOreDictLookupBackend backend = new NoOreDictLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        Item representativeItem = item("lookup.validation.cross_unification.representative");
        Item equivalentItem = item("lookup.validation.cross_unification.equivalent");
        Item circuitItem = item("lookup.validation.cross_unification.circuit");
        ItemStack representative = new ItemStack(representativeItem, 1, 0);
        ItemStack equivalent = new ItemStack(equivalentItem, 1, 0);
        String unificationName = circuit.get(Materials.HV)
            .toString();
        boolean hadPreviousTarget = GTOreDictUnificator.getName2StackMap()
            .containsKey(unificationName);
        ItemStack previousTarget = GTOreDictUnificator.getName2StackMap()
            .put(unificationName, representative);

        try {
            GTOreDictUnificator.setItemData(representative, new ItemData(circuit, Materials.HV));
            GTOreDictUnificator.setItemData(equivalent, new ItemData(circuit, Materials.HV));
            GTOreDictUnificator.resetUnificationEntries();

            backend.compileRecipe(
                recipe(
                    new ItemStack[] { representative, new ItemStack(circuitItem, 0, 11) },
                    new FluidStack[0],
                    item("lookup.validation.cross_unification.representative.output"),
                    category));
            backend.compileRecipe(
                recipe(
                    new ItemStack[] { equivalent, new ItemStack(circuitItem, 0, 11) },
                    new FluidStack[0],
                    item("lookup.validation.cross_unification.equivalent.output"),
                    category));

            IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> RecipeLookupValidator.validateLookup(mapName, backend));

            assertRecipeConflict(error, mapName, 1);
        } finally {
            if (hadPreviousTarget) {
                GTOreDictUnificator.getName2StackMap()
                    .put(unificationName, previousTarget);
            } else {
                GTOreDictUnificator.getName2StackMap()
                    .remove(unificationName);
            }
            GTOreDictUnificator.removeItemData(representative);
            GTOreDictUnificator.removeItemData(equivalent);
            GTOreDictUnificator.resetUnificationEntries();
        }
    }

    @Test
    void lookupVerifierReportsConflictWhenItemDataMakesRawInputEquivalent() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.item_data";
        NoOreDictLookupBackend backend = new NoOreDictLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        Item representativeItem = item("lookup.validation.item_data.representative");
        Item equivalentItem = item("lookup.validation.item_data.equivalent");
        ItemStack representative = new ItemStack(representativeItem, 1, 0);
        ItemStack equivalent = new ItemStack(equivalentItem, 1, 0);
        String unificationName = circuit.get(Materials.MV)
            .toString();
        boolean hadPreviousTarget = GTOreDictUnificator.getName2StackMap()
            .containsKey(unificationName);
        ItemStack previousTarget = GTOreDictUnificator.getName2StackMap()
            .put(unificationName, representative);

        try {
            GTOreDictUnificator.setItemData(representative, new ItemData(circuit, Materials.MV));
            ItemData equivalentData = new ItemData(circuit, Materials.MV);
            equivalentData.mBlackListed = true;
            GTOreDictUnificator.setItemData(equivalent, equivalentData);
            GTOreDictUnificator.resetUnificationEntries();

            backend.compileRecipe(
                recipe(
                    new ItemStack[] { representative },
                    new FluidStack[0],
                    item("lookup.validation.item_data.representative.output"),
                    category));
            backend.compileRecipe(
                recipe(
                    new ItemStack[] { equivalent },
                    new FluidStack[0],
                    item("lookup.validation.item_data.equivalent.output"),
                    category));

            IllegalStateException error = assertThrows(
                IllegalStateException.class,
                () -> RecipeLookupValidator.validateLookup(mapName, backend));

            assertRecipeConflict(error, mapName, 1);
        } finally {
            if (hadPreviousTarget) {
                GTOreDictUnificator.getName2StackMap()
                    .put(unificationName, previousTarget);
            } else {
                GTOreDictUnificator.getName2StackMap()
                    .remove(unificationName);
            }
            GTOreDictUnificator.removeItemData(representative);
            GTOreDictUnificator.removeItemData(equivalent);
            GTOreDictUnificator.resetUnificationEntries();
        }
    }

    @Test
    void lookupVerifierIgnoresRecipesWithNoLookupIngredients() {
        EmptyLookupBackend backend = new EmptyLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        backend.compileRecipe(recipeWithoutInputs(item("lookup.validation.empty.output"), category));

        assertDoesNotThrow(() -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.empty", backend));
    }

    @Test
    void lookupVerifierIgnoresFakeRecipes() {
        EmptyLookupBackend backend = new EmptyLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(item("lookup.validation.fake.input"), item("lookup.validation.fake.output"), category);
        recipe.mFakeRecipe = true;
        backend.compileRecipe(recipe);

        assertDoesNotThrow(() -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.fake", backend));
    }

    @Test
    void lookupVerifierIgnoresDisabledRecipes() {
        EmptyLookupBackend backend = new EmptyLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            item("lookup.validation.disabled.input"),
            item("lookup.validation.disabled.output"),
            category);
        recipe.mEnabled = false;
        backend.compileRecipe(recipe);

        assertDoesNotThrow(() -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.disabled", backend));
    }

    @Test
    void lookupVerifierReportsRecipesRejectedByMinInputGate() {
        Fluid water = fluid("lookup.validation.min_input_gate.water");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            new ItemStack[] { null },
            new FluidStack[] { fluidStack(water, 750) },
            item("lookup.validation.min_input_gate.output"),
            category);
        MinItemInputBackend backend = new MinItemInputBackend(recipe);
        backend.compileRecipe(recipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.min_input_gate", backend));
        assertTrue(
            error.getMessage()
                .contains("query rejected before trie lookup"));
        assertTrue(
            error.getMessage()
                .contains("minItemInputs=1"));
        assertTrue(
            error.getMessage()
                .contains("unresolved oredict input(s)=0"));
    }

    @Test
    void lookupVerifierCategorizesMissingOreDictInputs() {
        Fluid water = fluid("lookup.validation.missing_oredict.water");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe.GTRecipe_WithAlt recipe = recipeWithAlt(
            new ItemStack[] { null },
            new FluidStack[] { fluidStack(water, 750) },
            item("lookup.validation.missing_oredict.output"),
            category);
        recipe.mOreDictIds = new int[] { 12345 };
        recipe.mOreDictAlt = new ItemStack[][] { new ItemStack[0] };
        MinItemInputBackend backend = new MinItemInputBackend(recipe);
        backend.compileRecipe(recipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.missing_oredict", backend));
        assertTrue(
            error.getMessage()
                .contains("unresolved oredict input(s)=1"));
        assertTrue(
            error.getMessage()
                .contains("lookup mismatch(es)=0"));
        assertTrue(
            error.getMessage()
                .contains("unresolved oredict input"));
        assertTrue(
            error.getMessage()
                .contains("oreDictId=12345"));
    }

    @Test
    void runtimeItemStackKeysDoNotIncludeGtUnificationTarget() {
        ensureMinecraftStackComparisonItem();
        Item representativeItem = item("lookup.unified.representative");
        Item equivalentItem = item("lookup.unified.equivalent");
        ItemStack representative = new ItemStack(representativeItem, 1, 0);
        ItemStack equivalent = new ItemStack(equivalentItem, 1, 0);
        String unificationName = circuit.get(Materials.LV)
            .toString();
        boolean hadPreviousTarget = GTOreDictUnificator.getName2StackMap()
            .containsKey(unificationName);
        ItemStack previousTarget = GTOreDictUnificator.getName2StackMap()
            .put(unificationName, representative);

        try {
            GTOreDictUnificator.setItemData(representative, new ItemData(circuit, Materials.LV));
            GTOreDictUnificator.setItemData(equivalent, new ItemData(circuit, Materials.LV));
            GTOreDictUnificator.resetUnificationEntries();

            List<GTRecipeLookupIngredient> group = new ArrayList<>();
            GTItemStackLookupIngredient.fromRuntime(group::add, equivalent);

            assertTrue(group.contains(GTItemStackLookupIngredient.fromRuntime(equivalent)));
            assertTrue(group.contains(GTItemStackLookupIngredient.fromRuntimeWildcard(equivalent)));
            assertFalse(group.contains(GTItemStackLookupIngredient.fromRuntime(representative)));
            assertFalse(group.contains(GTItemStackLookupIngredient.fromRuntimeWildcard(representative)));
        } finally {
            if (hadPreviousTarget) {
                GTOreDictUnificator.getName2StackMap()
                    .put(unificationName, previousTarget);
            } else {
                GTOreDictUnificator.getName2StackMap()
                    .remove(unificationName);
            }
            GTOreDictUnificator.removeItemData(representative);
            GTOreDictUnificator.removeItemData(equivalent);
            GTOreDictUnificator.resetUnificationEntries();
        }
    }

    @Test
    void lookupVerifierReportsConflictForNullableItemSlotMatches() {
        ensureMinecraftStackComparisonItem();
        String mapName = "gt.recipe.lookup.test.nullable";
        RecipeMapBackend backend = new NoOreDictLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        Item material = item("lookup.validation.nullable.material");
        Item saw = item("lookup.validation.nullable.saw");
        Fluid water = fluid("lookup.validation.nullable.water");
        GTRecipe nullableSlotRecipe = recipe(
            new ItemStack[] { null, new ItemStack(saw, 0, 0) },
            new FluidStack[] { fluidStack(water, 4) },
            item("lookup.validation.nullable.output"),
            category);
        GTRecipe concreteRecipe = recipe(
            new ItemStack[] { new ItemStack(material, 1, 0), new ItemStack(saw, 0, 0) },
            new FluidStack[] { fluidStack(water, 4) },
            item("lookup.validation.nullable.concrete.output"),
            category);
        backend.compileRecipe(nullableSlotRecipe);
        backend.compileRecipe(concreteRecipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeLookupValidator.validateLookup(mapName, backend));

        assertRecipeConflict(error, mapName, 1);
    }

    @Test
    void lookupVerifierMatchesAltRecipeRepresentativeInput() {
        ensureMinecraftStackComparisonItem();
        RecipeMapBackend backend = new NoOreDictLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        Item representativeItem = item("lookup.validation.alt_representative.representative");
        Item alternativeItem = item("lookup.validation.alt_representative.alternative");
        Item circuitItem = item("lookup.validation.alt_representative.circuit");
        GTRecipe.GTRecipe_WithAlt recipe = recipeWithAlt(
            new ItemStack[] { new ItemStack(representativeItem, 1, 0), new ItemStack(circuitItem, 0, 21) },
            new FluidStack[0],
            item("lookup.validation.alt_representative.output"),
            category);
        recipe.mOreDictAlt = new ItemStack[][] { { new ItemStack(alternativeItem, 1, 0) },
            { new ItemStack(circuitItem, 0, 21) } };
        recipe.mOreDictIds = new int[] { -1, -1 };

        backend.compileRecipe(recipe);

        assertDoesNotThrow(
            () -> RecipeLookupValidator.validateLookup("gt.recipe.lookup.test.alt_representative", backend));
    }

    @Test
    void lookupVerifierDescriptionIncludesCapturedRecipeCallsite() {
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            item("lookup.validation.callsite.input"),
            item("lookup.validation.callsite.output"),
            category);
        recipe.stackTraces = new ArrayList<>();
        recipe.stackTraces.add(
            Arrays.asList(
                "gregtech.api.util.GTRecipe$GTRecipe_WithAlt.<init>(GTRecipe.java:1429)",
                "com.example.ModRecipes.register(ModRecipes.java:42)",
                "com.example.ModLoader.load(ModLoader.java:7)"));

        String description = RecipeLookupValidator.describeRecipeForValidation(recipe);

        assertTrue(description.contains("callsite=com.example.ModRecipes.register(ModRecipes.java:42)"));
    }

    @Test
    void lookupVerifierDescriptionIncludesRecipeMetadata() {
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(
            item("lookup.validation.metadata.input"),
            item("lookup.validation.metadata.output"),
            category);
        RecipeMetadataKey<ItemStack> catalystKey = SimpleRecipeMetadataKey
            .create(ItemStack.class, "lookup_validation_metadata_catalyst");
        RecipeMetadataStorage metadataStorage = new RecipeMetadataStorage();
        metadataStorage.store(catalystKey, new ItemStack(item("lookup.validation.metadata.catalyst"), 1, 4));
        setMetadataStorage(recipe, metadataStorage);

        String description = RecipeLookupValidator.describeRecipeForValidation(recipe);

        assertTrue(description.contains("metadata=[lookup_validation_metadata_catalyst="), description);
        assertTrue(description.contains(":4 x1 (item.lookup.validation.metadata.catalyst)"), description);
    }

    private static GTRecipe recipe(Item input, Item output, RecipeCategory category) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = new ItemStack[] { new ItemStack(input, 1, 0) };
        recipe.mOutputs = new ItemStack[] { new ItemStack(output, 1, 0) };
        recipe.mFluidInputs = new FluidStack[0];
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mEnabled = true;
        recipe.mCanBeBuffered = true;
        recipe.setRecipeCategory(category);
        return recipe;
    }

    private static GTRecipe recipe(ItemStack[] inputs, FluidStack[] fluidInputs, Item output, RecipeCategory category) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = inputs;
        recipe.mOutputs = new ItemStack[] { new ItemStack(output, 1, 0) };
        recipe.mFluidInputs = fluidInputs;
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mEnabled = true;
        recipe.mCanBeBuffered = true;
        recipe.setRecipeCategory(category);
        return recipe;
    }

    private static GTRecipe recipeWithoutInputs(Item output, RecipeCategory category) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = new ItemStack[0];
        recipe.mOutputs = new ItemStack[] { new ItemStack(output, 1, 0) };
        recipe.mFluidInputs = new FluidStack[0];
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mEnabled = true;
        recipe.mCanBeBuffered = true;
        recipe.setRecipeCategory(category);
        return recipe;
    }

    private static GTRecipe.GTRecipe_WithAlt recipeWithAlt(ItemStack[] inputs, FluidStack[] fluidInputs, Item output,
        RecipeCategory category) {
        GTRecipe.GTRecipe_WithAlt recipe = allocate(GT_RECIPE_WITH_ALT_CONSTRUCTOR);
        recipe.mInputs = inputs;
        recipe.mOutputs = new ItemStack[] { new ItemStack(output, 1, 0) };
        recipe.mFluidInputs = fluidInputs;
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mEnabled = true;
        recipe.mCanBeBuffered = true;
        recipe.setRecipeCategory(category);
        return recipe;
    }

    private static <T> T allocate(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void assertRecipeConflict(IllegalStateException error, String mapName, int expectedCount) {
        assertRecipeConflict(error, mapName, expectedCount, 0);
    }

    private static void assertRecipeConflict(IllegalStateException error, String mapName, int expectedCount,
        int expectedLookupMismatches) {
        assertTrue(
            error.getMessage()
                .contains(mapName));
        assertTrue(
            error.getMessage()
                .contains("recipe conflict(s)=" + expectedCount));
        assertTrue(
            error.getMessage()
                .contains("lookup mismatch(es)=" + expectedLookupMismatches));
        assertTrue(
            error.getMessage()
                .contains("conflictingMatches="));
    }

    private static GTRecipeLookup recipeLookup(RecipeMapBackend backend) {
        try {
            Field field = RecipeMapBackend.class.getDeclaredField("recipeLookup");
            field.setAccessible(true);
            return (GTRecipeLookup) field.get(backend);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> constructorFor(Class<T> type) {
        try {
            Constructor<Object> objectConstructor = Object.class.getDeclaredConstructor();
            Constructor<?> constructor = ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(type, objectConstructor);
            constructor.setAccessible(true);
            return (Constructor<T>) constructor;
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void setMetadataStorage(GTRecipe recipe, IRecipeMetadataStorage metadataStorage) {
        try {
            Field field = GTRecipe.class.getDeclaredField("metadataStorage");
            field.setAccessible(true);
            field.set(recipe, metadataStorage);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static IRecipeMetadataStorage qftCatalystMetadata(ItemStack catalyst) {
        RecipeMetadataStorage metadataStorage = new RecipeMetadataStorage();
        metadataStorage.store(GTRecipeConstants.QFT_CATALYST, catalyst);
        return metadataStorage;
    }

    private static Item item(String name) {
        return new Item().setUnlocalizedName(name);
    }

    private static Fluid fluid(String name) {
        return new Fluid(name);
    }

    private static final class ThrowingUnlocalizedNameItem extends Item {

        @Override
        public String getUnlocalizedName(ItemStack stack) {
            throw new ArrayIndexOutOfBoundsException("bad metadata");
        }
    }

    private static void ensureMinecraftStackComparisonItem() {
        putRegistryObject("feather", new Item().setUnlocalizedName("feather"));
        putRegistryObject("minecraft:feather", new Item().setUnlocalizedName("feather"));
        if (Items.feather == null) {
            throw new AssertionError("Items.feather was not initialized from the test item registry");
        }
    }

    @SuppressWarnings("unchecked")
    private static void putRegistryObject(String key, Item item) {
        try {
            Field registryObjects = net.minecraft.util.RegistrySimple.class.getDeclaredField("registryObjects");
            registryObjects.setAccessible(true);
            ((Map<Object, Object>) registryObjects.get(Item.itemRegistry)).put(key, item);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static ItemStack tagged(Item item, String value) {
        ItemStack stack = new ItemStack(item, 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("crop", value);
        stack.setTagCompound(tag);
        return stack;
    }

    private static FluidStack fluidStack(Fluid fluid, int amount) {
        try {
            FluidStack stack = allocate(FLUID_STACK_CONSTRUCTOR);
            Field fluidField = FluidStack.class.getDeclaredField("fluid");
            Field fluidDelegateField = FluidStack.class.getDeclaredField("fluidDelegate");
            fluidField.setAccessible(true);
            fluidDelegateField.setAccessible(true);
            fluidField.set(stack, fluid);
            fluidDelegateField.set(stack, new RegistryDelegate<Fluid>() {

                @Override
                public Fluid get() {
                    return fluid;
                }

                @Override
                public String name() {
                    return fluid.getName();
                }

                @Override
                public Class<Fluid> type() {
                    return Fluid.class;
                }
            });
            stack.amount = amount;
            return stack;
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static class NoOreDictLookupBackend extends RecipeMapBackend {

        private NoOreDictLookupBackend() {
            super(new RecipeMapBackendPropertiesBuilder());
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            List<GTRecipeLookupIngredient> ingredients = new ArrayList<>();
            Consumer<GTRecipeLookupIngredient> adder = ingredients::add;

            for (ItemStack item : items) {
                if (item == null) continue;

                GTItemStackLookupIngredient.fromRuntime(adder, item);
                GTItemDataLookupIngredient.fromRuntime(adder, item);
            }

            for (FluidStack fluid : fluids) {
                if (fluid == null || fluid.getFluid() == null) continue;

                GTFluidLookupIngredient.fromRuntime(adder, fluid);
            }

            if (ingredients.isEmpty()) {
                return Stream.empty();
            }

            Iterator<GTRecipe> iterator = recipeLookup(this).iterator(ingredients);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
        }
    }

    private static final class TrueFilterLookupBackend extends NoOreDictLookupBackend {

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }

        @Override
        protected boolean filterFindRecipe(GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
            return true;
        }
    }

    private static final class ReversedCandidateBackend extends RecipeMapBackend {

        private final GTRecipe[] candidates;

        private ReversedCandidateBackend(GTRecipe... candidates) {
            super(new RecipeMapBackendPropertiesBuilder());
            this.candidates = candidates;
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }

        @Override
        protected boolean filterFindRecipe(GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
            return true;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            return Stream.of(candidates);
        }
    }

    private static final class WildcardQueryBackend extends RecipeMapBackend {

        private final GTRecipe exactRecipe;
        private final GTRecipe wildcardRecipe;

        private WildcardQueryBackend(GTRecipe exactRecipe, GTRecipe wildcardRecipe) {
            super(new RecipeMapBackendPropertiesBuilder());
            this.exactRecipe = exactRecipe;
            this.wildcardRecipe = wildcardRecipe;
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }

        @Override
        protected boolean filterFindRecipe(GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
            return true;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            if (items.length > 0 && items[0] != null && items[0].getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                return Stream.of(wildcardRecipe);
            }
            return Stream.of(exactRecipe, wildcardRecipe);
        }
    }

    private static final class CountingLookupBackend extends RecipeMapBackend {

        private final GTRecipe[] candidates;
        private int lookupCandidateStreamCalls;

        private CountingLookupBackend(GTRecipe... candidates) {
            super(new RecipeMapBackendPropertiesBuilder());
            this.candidates = candidates;
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }

        @Override
        protected boolean filterFindRecipe(GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
            return true;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            lookupCandidateStreamCalls++;
            return Stream.of(candidates);
        }
    }

    private static final class NoContainsIndexBackend extends NoOreDictLookupBackend {

        private NoContainsIndexBackend() {
            super();
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }
    }

    private static final class EmptyLookupBackend extends RecipeMapBackend {

        private EmptyLookupBackend() {
            super(new RecipeMapBackendPropertiesBuilder());
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            return Stream.empty();
        }
    }

    private static final class MinItemInputBackend extends RecipeMapBackend {

        private final GTRecipe recipe;

        private MinItemInputBackend(GTRecipe recipe) {
            super(new RecipeMapBackendPropertiesBuilder().minItemInputs(1));
            this.recipe = recipe;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            return Stream.of(recipe);
        }
    }

    private static final class MissingLookupBackend extends RecipeMapBackend {

        private MissingLookupBackend() {
            super(new RecipeMapBackendPropertiesBuilder());
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            return Stream.empty();
        }
    }

    private static final class ThrowingAfterFirstCandidateBackend extends RecipeMapBackend {

        private final GTRecipe recipe;

        private ThrowingAfterFirstCandidateBackend(GTRecipe recipe) {
            super(new RecipeMapBackendPropertiesBuilder());
            this.recipe = recipe;
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }

        @Override
        protected boolean filterFindRecipe(GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
            return true;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            return Stream.concat(
                Stream.of(recipe),
                Stream.generate(() -> { throw new IllegalStateException("validator exhausted candidate stream"); }));
        }
    }

    private static final class FilterRejectingLookupBackend extends RecipeMapBackend {

        private final GTRecipe recipe;

        private FilterRejectingLookupBackend(GTRecipe recipe) {
            super(new RecipeMapBackendPropertiesBuilder());
            this.recipe = recipe;
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }

        @Override
        protected boolean filterFindRecipe(GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
            return false;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            return Stream.of(recipe);
        }
    }
}
