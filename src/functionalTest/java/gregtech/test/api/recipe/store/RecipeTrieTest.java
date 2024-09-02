package gregtech.test.api.recipe.store;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableSet;

import gregtech.api.recipe.store.RecipeTrie;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;

public class RecipeTrieTest {

    @Test
    public void testAdd() {
        RecipeTrie trie = new RecipeTrie();

        boolean result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, trie.size());

        // adding the same recipe twice should fail

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertFalse(result);
        Assertions.assertEquals(1, trie.size());

        // add another valid recipe

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.iron_ingot))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertTrue(result);
        Assertions.assertEquals(2, trie.size());

        // adding a conflicting recipe should fail

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertFalse(result);
        Assertions.assertEquals(2, trie.size());

        // add another valid recipe

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.gold_ingot))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertTrue(result);
        Assertions.assertEquals(3, trie.size());

        // adding the same recipe with a different input ordering should fail

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.gold_ingot), new ItemStack(Items.iron_ingot))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertFalse(result);
        Assertions.assertEquals(3, trie.size());

        // adding a new unique recipe should succeed

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.flint))
                .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertTrue(result);
        Assertions.assertEquals(4, trie.size());

        // empty recipe inputs should fail

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemOutputs(new ItemStack(Items.diamond))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertFalse(result);
        Assertions.assertEquals(4, trie.size());

        // empty outputs do not matter to the trie and should succeed

        result = trie.add(
            GT_RecipeBuilder.builder()
                .itemInputs(new ItemStack(Items.melon_seeds))
                .duration(1)
                .eut(1)
                .build()
                .get());
        Assertions.assertTrue(result);
        Assertions.assertEquals(5, trie.size());
    }

    @Test
    public void testFind() {
        RecipeTrie trie = new RecipeTrie();
        GT_Recipe recipe = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe);
        GT_Recipe recipe2 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.LAVA, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe2);
        GT_Recipe recipe3 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.diamond))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe3);
        GT_Recipe recipe4 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe4);
        GT_Recipe recipe5 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.flint))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe5);
        GT_Recipe recipe6 = GT_RecipeBuilder.builder()
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1), new FluidStack(FluidRegistry.LAVA, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe6);

        GT_Recipe result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) },
            i -> true);
        Assertions.assertEquals(recipe, result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot) },
            new FluidStack[] { new FluidStack(FluidRegistry.LAVA, 1) },
            i -> true);
        Assertions.assertEquals(recipe2, result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.diamond) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) },
            i -> true);
        Assertions.assertEquals(recipe3, result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.diamond), new ItemStack(Items.gold_ingot) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) },
            i -> true);
        Assertions.assertEquals(recipe4, result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.flint), },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) },
            i -> true);
        Assertions.assertEquals(recipe5, result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.flint), new ItemStack(Items.gold_ingot) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) },
            i -> true);
        Assertions.assertEquals(recipe5, result);

        result = trie.findMatching(
            new ItemStack[] {},
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1), new FluidStack(FluidRegistry.LAVA, 1) },
            i -> true);
        Assertions.assertEquals(recipe6, result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.gold_ingot) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1), new FluidStack(FluidRegistry.LAVA, 1) },
            i -> true);
        Assertions.assertEquals(recipe6, result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.iron_ingot), },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) },
            i -> true);
        Assertions.assertNull(result);

        result = trie
            .findMatching(new ItemStack[] { new ItemStack(Items.iron_ingot), }, new FluidStack[] {}, i -> true);
        Assertions.assertNull(result);

        result = trie.findMatching(
            new ItemStack[] { new ItemStack(Items.gold_ingot) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) },
            i -> true);
        Assertions.assertNull(result);

        result = trie.findMatching(new ItemStack[] { new ItemStack(Items.gold_ingot) }, new FluidStack[] {}, i -> true);
        Assertions.assertNull(result);

        result = trie
            .findMatching(new ItemStack[] {}, new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1) }, i -> true);
        Assertions.assertNull(result);
    }

    @Test
    public void testFindAll() {
        RecipeTrie trie = new RecipeTrie();
        GT_Recipe recipe = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe);
        GT_Recipe recipe2 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.LAVA, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe2);
        GT_Recipe recipe3 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.diamond))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe3);
        GT_Recipe recipe4 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.gold_ingot), new ItemStack(Items.diamond))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe4);
        GT_Recipe recipe5 = GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.flint))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe5);
        GT_Recipe recipe6 = GT_RecipeBuilder.builder()
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1), new FluidStack(FluidRegistry.LAVA, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build()
            .get();
        trie.add(recipe6);
        var all = ImmutableSet.of(recipe, recipe2, recipe3, recipe4, recipe5, recipe6);
        var subset4 = ImmutableSet.of(recipe, recipe2, recipe5, recipe6);

        var results = trie.findAll(
            new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot),
                new ItemStack(Items.diamond), new ItemStack(Items.flint) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1), new FluidStack(FluidRegistry.LAVA, 1) });
        Assertions.assertEquals(6, results.size());
        Assertions.assertTrue(results.containsAll(all));
        Assertions.assertTrue(all.containsAll(results));

        results = trie.findAll(
            new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot),
                new ItemStack(Items.flint) },
            new FluidStack[] { new FluidStack(FluidRegistry.WATER, 1), new FluidStack(FluidRegistry.LAVA, 1) });
        Assertions.assertEquals(4, results.size());
        Assertions.assertTrue(results.containsAll(subset4));
        Assertions.assertTrue(subset4.containsAll(results));

        results = trie.findAll(
            new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot),
                new ItemStack(Items.diamond), new ItemStack(Items.flint) },
            new FluidStack[] {});
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(recipe5, results.toArray()[0]);
        Assertions.assertTrue(all.containsAll(results));
    }

    @Test
    public void testRemove() {

    }
}
