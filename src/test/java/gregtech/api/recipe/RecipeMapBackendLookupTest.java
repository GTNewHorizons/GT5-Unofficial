package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import gregtech.api.recipe.lookup.GTItemStackLookupIngredient;
import gregtech.api.recipe.lookup.GTRecipeLookup;
import gregtech.api.recipe.lookup.GTRecipeLookupIngredient;
import gregtech.api.util.GTRecipe;
import sun.reflect.ReflectionFactory;

class RecipeMapBackendLookupTest {

    private static final Constructor<GTRecipe> GT_RECIPE_CONSTRUCTOR = constructorFor(GTRecipe.class);
    private static final Constructor<RecipeCategory> RECIPE_CATEGORY_CONSTRUCTOR = constructorFor(RecipeCategory.class);

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

    private static class NoContainsIndexBackend extends RecipeMapBackend {

        private NoContainsIndexBackend() {
            super(new RecipeMapBackendPropertiesBuilder());
        }

        @Override
        protected GTRecipe addToItemMap(GTRecipe recipe) {
            return recipe;
        }
    }
}
