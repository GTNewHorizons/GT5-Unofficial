package gregtech.api.recipe;

import static gregtech.api.enums.OrePrefixes.circuit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gregtech.api.enums.Materials;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.lookup.GTItemStackLookupIngredient;
import gregtech.api.recipe.lookup.GTRecipeLookup;
import gregtech.api.recipe.lookup.GTRecipeLookupIngredient;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

class RecipeMapBackendLookupTest {

    private static final Constructor<GTRecipe> GT_RECIPE_CONSTRUCTOR = constructorFor(GTRecipe.class);
    private static final Constructor<RecipeCategory> RECIPE_CATEGORY_CONSTRUCTOR = constructorFor(RecipeCategory.class);
    private static final Unsafe UNSAFE = unsafe();

    @Test
    void trieCandidatesAreSortedBackToRegistrationOrder() {
        Item input = item("lookup.order.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe firstRegistered = recipe(input, item("lookup.order.first.output"), category);
        GTRecipe secondRegistered = recipe(input, item("lookup.order.second.output"), category);
        RecipeMapBackend backend = new ReversedCandidateBackend(secondRegistered, firstRegistered);
        backend.compileRecipe(firstRegistered);
        backend.compileRecipe(secondRegistered);

        assertSame(
            firstRegistered,
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
        assertEquals(Arrays.asList(firstRegistered, secondRegistered), allMatches);
    }

    @Test
    void compileRecipeAddsToLookupIncrementallyWithoutDirtyingLookup() {
        Item input = item("lookup.incremental.input");
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(input, item("lookup.incremental.output"), category);
        RecipeMapBackend backend = new NoContainsIndexBackend();

        backend.compileRecipe(recipe);

        assertFalse(recipeLookupDirty(backend));
        List<GTRecipeLookupIngredient> runtimeInput = Collections
            .singletonList(GTItemStackLookupIngredient.fromRuntime(new ItemStack(input, 1, 0)));
        Iterator<GTRecipe> matches = recipeLookup(backend).iterator(Collections.singletonList(runtimeInput));
        assertSame(recipe, matches.next());
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
        CountingLookupBackend backend = new CountingLookupBackend(recipe);
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
        CountingLookupBackend backend = new CountingLookupBackend(recipe);
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
    void diagnosticFallbackFindsAndLogsRecipeAfterTrieMiss(@TempDir Path tempDir) throws Exception {
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

            Path missLog = tempDir.resolve("logs")
                .resolve("RecipeLookupMisses.log");
            assertTrue(Files.isRegularFile(missLog));
            String log = new String(Files.readAllBytes(missLog), StandardCharsets.UTF_8);
            assertTrue(log.contains("[GTRecipeLookupDiagnosticFallback]"));
            assertTrue(log.contains("lookup.diagnostic.input"));
            assertTrue(log.contains("lookup.diagnostic.output"));
        } finally {
            GTLog.mLogFile = previousLogFile;
        }
    }

    @Test
    void diagnosticFallbackDoesNotRunForCollisionCheck() {
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
    void runtimeItemStackKeysIncludeGtUnificationTarget() {
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
            RecipeMapBackend.addRuntimeItemStackLookupIngredients(group, equivalent);

            assertTrue(group.contains(GTItemStackLookupIngredient.fromRuntime(representative)));
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

    private static <T> T allocate(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static boolean recipeLookupDirty(RecipeMapBackend backend) {
        try {
            Field field = RecipeMapBackend.class.getDeclaredField("recipeLookupDirty");
            field.setAccessible(true);
            return field.getBoolean(backend);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
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

    private static Item item(String name) {
        return new Item().setUnlocalizedName(name);
    }

    private static Unsafe unsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void ensureMinecraftStackComparisonItem() {
        if (Items.feather != null) {
            return;
        }
        try {
            Field field = Items.class.getDeclaredField("feather");
            Object base = UNSAFE.staticFieldBase(field);
            long offset = UNSAFE.staticFieldOffset(field);
            UNSAFE.putObject(base, offset, new Item());
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
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
        protected boolean filterFindRecipe(@NotNull GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot, boolean dontCheckStackSizes) {
            return true;
        }

        @Override
        protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
            @Nullable FluidStack @NotNull [] fluids) {
            return Stream.of(candidates);
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
        protected boolean filterFindRecipe(@NotNull GTRecipe recipe, @Nullable ItemStack @NotNull [] items,
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

    private static class NoContainsIndexBackend extends RecipeMapBackend {

        private NoContainsIndexBackend() {
            super(new RecipeMapBackendPropertiesBuilder());
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
}
