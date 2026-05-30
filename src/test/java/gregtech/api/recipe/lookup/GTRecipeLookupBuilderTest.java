package gregtech.api.recipe.lookup;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.Test;

import cpw.mods.fml.common.registry.RegistryDelegate;
import gregtech.api.util.GTRecipe;
import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

class GTRecipeLookupBuilderTest {

    private static final Constructor<GTRecipe> GT_RECIPE_CONSTRUCTOR = constructorFor(GTRecipe.class);
    private static final Constructor<GTRecipe.GTRecipe_WithAlt> GT_RECIPE_WITH_ALT_CONSTRUCTOR = constructorFor(
        GTRecipe.GTRecipe_WithAlt.class);
    private static final Constructor<FluidStack> FLUID_STACK_CONSTRUCTOR = constructorFor(FluidStack.class);
    private static final Field FLUID_STACK_DELEGATE = field(FluidStack.class, "fluidDelegate");
    private static final Unsafe UNSAFE = unsafe();

    @Test
    void buildsItemAndFluidLookupIgnoringStackSizesAndFluidAmounts() {
        Item item = item("builder.item.normal");
        Fluid fluid = new Fluid("builder_fluid_normal");
        GTRecipe recipe = recipe(
            new ItemStack[] { new ItemStack(item, 64, 2) },
            new FluidStack[] { fluidStack(fluid, 1000) });

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(recipe)
            .build();

        Iterator<GTRecipe> iterator = lookup.iterator(
            group(
                GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 1, 2)),
                new GTFluidLookupIngredient(fluid)));

        assertSame(recipe, onlyResult(iterator));
    }

    @Test
    void indexesOreDictIdsAndAltStacksForAltRecipes() {
        Item representative = item("builder.item.ore.representative");
        Item alternative = item("builder.item.ore.alternative");
        GTRecipe.GTRecipe_WithAlt recipe = recipeWithAlt(new ItemStack[] { new ItemStack(representative, 1, 0) }, null);
        recipe.mOreDictIds = new int[] { 7001 };
        recipe.mOreDictAlt = new ItemStack[][] { { new ItemStack(alternative, 1, 0) } };

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(recipe)
            .build();

        assertSame(recipe, onlyResult(lookup.iterator(group(new GTOreDictLookupIngredient(7001)))));
        assertSame(
            recipe,
            onlyResult(
                lookup.iterator(group(GTItemStackLookupIngredient.fromRuntime(new ItemStack(alternative, 1, 0))))));
    }

    @Test
    void ignoresOreDictIdWhenRecipeInputSlotIsNull() {
        Fluid fluid = new Fluid("builder_fluid_null_ore_slot");
        GTRecipe.GTRecipe_WithAlt recipe = recipeWithAlt(
            new ItemStack[] { null },
            new FluidStack[] { fluidStack(fluid, 750) });
        recipe.mOreDictIds = new int[] { 7003 };
        recipe.mOreDictAlt = new ItemStack[][] { new ItemStack[0] };

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(recipe)
            .build();

        assertSame(recipe, onlyResult(lookup.iterator(group(new GTFluidLookupIngredient(fluid)))));
        assertFalse(
            lookup.iterator(group(new GTOreDictLookupIngredient(7003)))
                .hasNext());
    }

    @Test
    void oreDictAltRecipeInputMatchesAlternativeStackWhenOreIdDoesNot() {
        ensureMinecraftStackComparisonItem();
        Item representative = item("builder.item.ore.match.representative");
        Item alternative = item("builder.item.ore.match.alternative");
        GTRecipe.GTRecipe_WithAlt recipe = recipeWithAlt(new ItemStack[] { new ItemStack(representative, 1, 0) }, null);
        recipe.mOreDictIds = new int[] { 7002 };
        recipe.mOreDictAlt = new ItemStack[][] { { new ItemStack(alternative, 1, 0) } };

        assertTrue(recipe.isRecipeInputEqual(false, true, null, new ItemStack(alternative, 1, 0)));
    }

    @Test
    void usesLegacyOreDictAltStacksWhenOreIdsAreMissing() {
        Item representative = item("builder.item.legacy.representative");
        Item firstAlt = item("builder.item.legacy.first");
        Item secondAlt = item("builder.item.legacy.second");
        GTRecipe.GTRecipe_WithAlt recipe = recipeWithAlt(new ItemStack[] { new ItemStack(representative, 1, 0) }, null);
        recipe.mOreDictIds = null;
        recipe.mOreDictAlt = new ItemStack[][] { { new ItemStack(firstAlt, 8, 3), new ItemStack(secondAlt, 16, 4) } };

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(recipe)
            .build();

        assertSame(
            recipe,
            onlyResult(lookup.iterator(group(GTItemStackLookupIngredient.fromRuntime(new ItemStack(firstAlt, 1, 3))))));
        assertSame(
            recipe,
            onlyResult(
                lookup.iterator(group(GTItemStackLookupIngredient.fromRuntime(new ItemStack(secondAlt, 64, 4))))));
    }

    @Test
    void addsMainAndAlternativeFluidKeysForOneFluidSlot() {
        Fluid main = new Fluid("builder_fluid_main");
        Fluid alternative = new Fluid("builder_fluid_alt");
        GTRecipe.GTRecipe_WithAlt recipe = recipeWithAlt(null, new FluidStack[] { fluidStack(main, 1000) });
        recipe.mAltFluidInputs = new FluidStack[][] { { fluidStack(alternative, 1) } };

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(recipe)
            .build();

        assertSame(recipe, onlyResult(lookup.iterator(group(new GTFluidLookupIngredient(main)))));
        assertSame(recipe, onlyResult(lookup.iterator(group(new GTFluidLookupIngredient(alternative)))));
    }

    @Test
    void skipsFakeRecipesForSearchableLookup() {
        Item item = item("builder.item.fake");
        GTRecipe recipe = recipe(new ItemStack[] { new ItemStack(item, 1, 0) }, null);
        recipe.mFakeRecipe = true;

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(recipe)
            .build();

        assertFalse(
            lookup.iterator(group(GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 1, 0))))
                .hasNext());
    }

    @Test
    void sortingByIncreasingFrequencyKeepsOverlappingRecipesReachable() {
        Item common = item("builder.item.common");
        Item rare = item("builder.item.rare");
        GTRecipe prefixRecipe = recipe(new ItemStack[] { new ItemStack(common, 1, 0) }, null);
        GTRecipe overlappingRecipe = recipe(
            new ItemStack[] { new ItemStack(common, 1, 0), new ItemStack(rare, 1, 0) },
            null);

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(prefixRecipe)
            .add(overlappingRecipe)
            .build();

        Iterator<GTRecipe> iterator = lookup.iterator(
            group(
                GTItemStackLookupIngredient.fromRuntime(new ItemStack(common, 1, 0)),
                GTItemStackLookupIngredient.fromRuntime(new ItemStack(rare, 1, 0))));

        assertSame(prefixRecipe, iterator.next());
        assertSame(overlappingRecipe, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void preservesDuplicateOrderedLeavesFromBuilderInsertion() {
        Item item = item("builder.item.duplicates");
        GTRecipe first = recipe(new ItemStack[] { new ItemStack(item, 1, 0) }, null);
        GTRecipe second = recipe(new ItemStack[] { new ItemStack(item, 64, 0) }, null);

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(first)
            .add(second)
            .build();

        Iterator<GTRecipe> iterator = lookup
            .iterator(group(GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 1, 0))));

        assertSame(first, iterator.next());
        assertSame(second, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void builtLookupIsImmutable() {
        Item item = item("builder.item.immutable");
        GTRecipe recipe = recipe(new ItemStack[] { new ItemStack(item, 1, 0) }, null);

        GTRecipeLookup lookup = new GTRecipeLookupBuilder().add(recipe)
            .build();

        assertThrows(
            UnsupportedOperationException.class,
            () -> lookup
                .add(recipe, groups(group(GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 1, 0))))));
    }

    private static GTRecipe onlyResult(Iterator<GTRecipe> iterator) {
        assertTrue(iterator.hasNext());
        GTRecipe recipe = iterator.next();
        assertFalse(iterator.hasNext());
        return recipe;
    }

    private static GTRecipe recipe(ItemStack[] itemInputs, FluidStack[] fluidInputs) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = itemInputs == null ? new ItemStack[0] : itemInputs;
        recipe.mOutputs = new ItemStack[0];
        recipe.mFluidInputs = fluidInputs == null ? new FluidStack[0] : fluidInputs;
        recipe.mFluidOutputs = new FluidStack[0];
        return recipe;
    }

    private static GTRecipe.GTRecipe_WithAlt recipeWithAlt(ItemStack[] itemInputs, FluidStack[] fluidInputs) {
        GTRecipe.GTRecipe_WithAlt recipe = allocate(GT_RECIPE_WITH_ALT_CONSTRUCTOR);
        recipe.mInputs = itemInputs == null ? new ItemStack[0] : itemInputs;
        recipe.mOutputs = new ItemStack[0];
        recipe.mFluidInputs = fluidInputs == null ? new FluidStack[0] : fluidInputs;
        recipe.mFluidOutputs = new FluidStack[0];
        return recipe;
    }

    private static <T> T allocate(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
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

    private static Field field(Class<?> type, String name) {
        try {
            Field field = type.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
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

    @SafeVarargs
    private static List<List<GTRecipeLookupIngredient>> groups(List<GTRecipeLookupIngredient>... groups) {
        return Arrays.asList(groups);
    }

    private static List<GTRecipeLookupIngredient> group(GTRecipeLookupIngredient... ingredients) {
        return Arrays.asList(ingredients);
    }

    private static Item item(String name) {
        return new Item().setUnlocalizedName(name)
            .setHasSubtypes(true);
    }

    private static FluidStack fluidStack(Fluid fluid, int amount) {
        FluidStack stack = allocate(FLUID_STACK_CONSTRUCTOR);
        stack.amount = amount;
        try {
            FLUID_STACK_DELEGATE.set(stack, new TestFluidDelegate(fluid));
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
        return stack;
    }

    private static final class TestFluidDelegate implements RegistryDelegate<Fluid> {

        private final Fluid fluid;

        private TestFluidDelegate(Fluid fluid) {
            this.fluid = fluid;
        }

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
    }
}
