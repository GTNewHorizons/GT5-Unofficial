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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import cpw.mods.fml.common.registry.RegistryDelegate;
import gregtech.api.enums.Materials;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.lookup.GTFluidLookupIngredient;
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
        backend.compileRecipe(firstRecipe);
        backend.compileRecipe(secondRecipe);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeMapBackend.validateLookup(mapName, backend));

        assertTrue(
            error.getMessage()
                .contains(mapName));
        assertTrue(
            error.getMessage()
                .contains("lookup.validation.first.input"));
        assertTrue(
            error.getMessage()
                .contains("lookup.validation.second.input"));
    }

    @Test
    void lookupVerifierDoesNotNeedContainsIndexCandidates() {
        NoContainsIndexBackend backend = new NoContainsIndexBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        backend.compileRecipe(
            recipe(item("lookup.validation.unindexed.input"), item("lookup.validation.unindexed.output"), category));

        assertDoesNotThrow(() -> RecipeMapBackend.validateLookup("gt.recipe.lookup.test.unindexed", backend));
    }

    @Test
    void lookupVerifierDoesNotRequireCrossUnificationMatches() {
        ensureMinecraftStackComparisonItem();
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

            assertDoesNotThrow(
                () -> RecipeMapBackend.validateLookup("gt.recipe.lookup.test.cross_unification", backend));
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

        assertDoesNotThrow(() -> RecipeMapBackend.validateLookup("gt.recipe.lookup.test.empty", backend));
    }

    @Test
    void lookupVerifierIgnoresFakeRecipes() {
        EmptyLookupBackend backend = new EmptyLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        GTRecipe recipe = recipe(item("lookup.validation.fake.input"), item("lookup.validation.fake.output"), category);
        recipe.mFakeRecipe = true;
        backend.compileRecipe(recipe);

        assertDoesNotThrow(() -> RecipeMapBackend.validateLookup("gt.recipe.lookup.test.fake", backend));
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

        assertDoesNotThrow(() -> RecipeMapBackend.validateLookup("gt.recipe.lookup.test.disabled", backend));
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
            RecipeMapBackend.addRuntimeItemStackLookupIngredients(group, equivalent);

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
    void lookupVerifierAcceptsNullableItemSlotMatches() {
        ensureMinecraftStackComparisonItem();
        RecipeMapBackend backend = new NoOreDictLookupBackend();
        RecipeCategory category = allocate(RECIPE_CATEGORY_CONSTRUCTOR);
        Item material = item("lookup.validation.nullable.material");
        Item saw = item("lookup.validation.nullable.saw");
        Fluid water = new Fluid("lookup.validation.nullable.water");
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

        assertDoesNotThrow(() -> RecipeMapBackend.validateLookup("gt.recipe.lookup.test.nullable", backend));
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

    private static FluidStack fluidStack(Fluid fluid, int amount) {
        try {
            FluidStack stack = (FluidStack) UNSAFE.allocateInstance(FluidStack.class);
            Field fluidField = FluidStack.class.getDeclaredField("fluid");
            Field fluidDelegateField = FluidStack.class.getDeclaredField("fluidDelegate");
            Field amountField = FluidStack.class.getDeclaredField("amount");
            UNSAFE.putObject(stack, UNSAFE.objectFieldOffset(fluidField), fluid);
            UNSAFE.putObject(stack, UNSAFE.objectFieldOffset(fluidDelegateField), new RegistryDelegate<Fluid>() {

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
            UNSAFE.putInt(stack, UNSAFE.objectFieldOffset(amountField), amount);
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
            List<List<GTRecipeLookupIngredient>> ingredients = new ArrayList<>();

            for (ItemStack item : items) {
                if (item == null) continue;

                List<GTRecipeLookupIngredient> group = new ArrayList<>();
                RecipeMapBackend.addRuntimeItemStackLookupIngredients(group, item);
                if (!group.isEmpty()) {
                    ingredients.add(group);
                }
            }

            for (FluidStack fluid : fluids) {
                if (fluid == null || fluid.getFluid() == null) continue;

                List<GTRecipeLookupIngredient> group = new ArrayList<>(1);
                group.add(new GTFluidLookupIngredient(fluid));
                ingredients.add(group);
            }

            if (ingredients.isEmpty()) {
                return Stream.empty();
            }

            Iterator<GTRecipe> iterator = recipeLookup(this).iterator(ingredients);
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
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
}
