package gregtech.api.recipe.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import gregtech.api.util.GTRecipe;
import sun.reflect.ReflectionFactory;

class GTRecipeLookupTest {

    private static final Constructor<GTRecipe> GT_RECIPE_CONSTRUCTOR = gtRecipeConstructor();

    @Test
    void storesMultipleRecipesOnSamePathInInsertionOrder() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe first = recipe();
        GTRecipe second = recipe();

        lookup.add(first, groups(group(ore(1)), group(ore(2))));
        lookup.add(second, groups(group(ore(1)), group(ore(2))));

        Iterator<GTRecipe> iterator = lookup.iterator(group(ore(1), ore(2)));

        assertTrue(iterator.hasNext());
        assertSame(first, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(second, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void insertsEveryAlternativeIngredientPath() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe recipe = recipe();

        lookup.add(recipe, groups(group(ore(10), ore(11)), group(ore(12))));

        assertSame(recipe, onlyResult(lookup.iterator(group(ore(10), ore(12)))));
        assertSame(recipe, onlyResult(lookup.iterator(group(ore(11), ore(12)))));
        assertFalse(
            lookup.iterator(group(ore(13), ore(12)))
                .hasNext());
    }

    @Test
    void itemLookupKeysIgnoreNbtSensitivity() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe normalRecipe = recipe();
        GTRecipe nbtSensitiveRecipe = recipe();
        ItemStack stack = new ItemStack(new Item().setUnlocalizedName("lookup.item.nbt.branch"), 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("integer", 123456);
        stack.setTagCompound(tag);

        GTRecipeLookupIngredient normal = GTItemStackLookupIngredient.fromRecipe(stack);
        GTRecipeLookupIngredient nbtSensitive = GTItemStackLookupIngredient.fromNbtSensitiveRecipe(stack);

        assertEquals(normal, nbtSensitive);
        lookup.add(normalRecipe, groups(group(normal)));
        lookup.add(nbtSensitiveRecipe, groups(group(nbtSensitive)));

        List<GTRecipe> results = results(lookup.iterator(group(normal)));
        assertEquals(2, results.size());
        assertTrue(results.contains(normalRecipe));
        assertTrue(results.contains(nbtSensitiveRecipe));
    }

    @Test
    void keepsLongerPathReachableWhenItSharesExistingLeaf() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe existing = recipe();
        GTRecipe overlapping = recipe();

        lookup.add(existing, groups(group(ore(30))));
        lookup.add(overlapping, groups(group(ore(30)), group(ore(32))));

        List<GTRecipe> results = results(lookup.iterator(group(ore(30), ore(32))));
        assertEquals(2, results.size());
        assertTrue(results.contains(existing));
        assertTrue(results.contains(overlapping));
    }

    @Test
    void returnsCandidatesInInsertionOrderAcrossDifferentPaths() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe first = recipe();
        GTRecipe second = recipe();

        lookup.add(first, groups(group(ore(60))));
        lookup.add(second, groups(group(ore(61)), group(ore(60))));

        List<GTRecipe> results = results(lookup.iterator(group(ore(61), ore(60))));

        assertEquals(Arrays.asList(first, second), results);
    }

    @Test
    void keepsShorterPathReachableWhenItSharesExistingBranch() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe existing = recipe();
        GTRecipe overlapping = recipe();

        lookup.add(existing, groups(group(ore(40)), group(ore(41))));
        lookup.add(overlapping, groups(group(ore(40))));

        assertSame(overlapping, onlyResult(lookup.iterator(group(ore(40)))));
        List<GTRecipe> results = results(lookup.iterator(group(ore(40), ore(41))));
        assertEquals(2, results.size());
        assertTrue(results.contains(existing));
        assertTrue(results.contains(overlapping));
    }

    @Test
    void returnsLongerPathBeforeSharedPrefixWhenRegisteredFirst() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe longer = recipe();
        GTRecipe prefix = recipe();

        lookup.add(longer, groups(group(ore(70)), group(ore(71))));
        lookup.add(prefix, groups(group(ore(70))));

        List<GTRecipe> results = results(lookup.iterator(group(ore(70), ore(71))));

        assertEquals(Arrays.asList(longer, prefix), results);
    }

    @Test
    void returnsLongerRepeatedPathBeforeSharedPrefixWhenRegisteredFirst() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe longer = recipe();
        GTRecipe prefix = recipe();

        lookup.add(longer, groups(group(ore(80)), group(ore(80)), group(ore(80)), group(ore(81))));
        lookup.add(prefix, groups(group(ore(80)), group(ore(80)), group(ore(81))));

        List<GTRecipe> results = results(lookup.iterator(group(ore(80), ore(81))));

        assertEquals(Arrays.asList(longer, prefix), results);
    }

    @Test
    void returnsSharedPrefixBeforeLongerRepeatedPathWhenRegisteredFirst() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe prefix = recipe();
        GTRecipe longer = recipe();

        lookup.add(prefix, groups(group(ore(90)), group(ore(90)), group(ore(91))));
        lookup.add(longer, groups(group(ore(90)), group(ore(90)), group(ore(90)), group(ore(91))));

        List<GTRecipe> results = results(lookup.iterator(group(ore(90), ore(91))));

        assertEquals(Arrays.asList(prefix, longer), results);
    }

    @Test
    void iteratorHasNextIsIdempotentAndNextThrowsWhenExhausted() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        GTRecipe first = recipe();
        GTRecipe second = recipe();

        lookup.add(first, groups(group(ore(50))));
        lookup.add(second, groups(group(ore(50))));

        Iterator<GTRecipe> iterator = lookup.iterator(group(ore(50)));

        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertSame(first, iterator.next());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertSame(second, iterator.next());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void iteratorTraversesAllCandidatesWithoutFixedLimit() {
        GTRecipeLookup lookup = new GTRecipeLookup();
        List<GTRecipe> recipes = new ArrayList<>();
        List<GTRecipeLookupIngredient> queryKeys = new ArrayList<>();

        for (int i = 0; i < 96; i++) {
            GTRecipe recipe = recipe();
            recipes.add(recipe);
            queryKeys.add(ore(100 + i));
            lookup.add(recipe, groups(group(ore(100 + i))));
        }

        Iterator<GTRecipe> iterator = lookup.iterator(queryKeys);
        for (GTRecipe recipe : recipes) {
            assertTrue(iterator.hasNext());
            assertSame(recipe, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    private static GTRecipe onlyResult(Iterator<GTRecipe> iterator) {
        assertTrue(iterator.hasNext());
        GTRecipe recipe = iterator.next();
        assertFalse(iterator.hasNext());
        return recipe;
    }

    private static List<GTRecipe> results(Iterator<GTRecipe> iterator) {
        List<GTRecipe> results = new ArrayList<>();
        iterator.forEachRemaining(results::add);
        return results;
    }

    private static GTRecipe recipe() {
        try {
            return GT_RECIPE_CONSTRUCTOR.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Constructor<GTRecipe> gtRecipeConstructor() {
        try {
            Constructor<Object> objectConstructor = Object.class.getDeclaredConstructor();
            Constructor<?> constructor = ReflectionFactory.getReflectionFactory()
                .newConstructorForSerialization(GTRecipe.class, objectConstructor);
            constructor.setAccessible(true);
            return (Constructor<GTRecipe>) constructor;
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static GTOreDictLookupIngredient ore(int oreId) {
        return new GTOreDictLookupIngredient(oreId);
    }

    @SafeVarargs
    private static List<List<GTRecipeLookupIngredient>> groups(List<GTRecipeLookupIngredient>... groups) {
        return Arrays.asList(groups);
    }

    private static List<GTRecipeLookupIngredient> group(GTRecipeLookupIngredient... ingredients) {
        return Arrays.asList(ingredients);
    }

}
