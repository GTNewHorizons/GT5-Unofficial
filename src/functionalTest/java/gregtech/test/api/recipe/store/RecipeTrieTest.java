package gregtech.test.api.recipe.store;

import gregtech.api.recipe.store.RecipeTrie;
import gregtech.api.util.GT_RecipeBuilder;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RecipeTrieTest {

    @Test
    public void testAdd() {
        RecipeTrie trie = new RecipeTrie();

        boolean result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertTrue(result);
        Assertions.assertEquals(1, trie.size());

        // adding the same recipe twice should fail

        result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertFalse(result);
        Assertions.assertEquals(1, trie.size());

        // add another valid recipe

        result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertTrue(result);
        Assertions.assertEquals(2, trie.size());

        // adding a conflicting recipe should fail

        result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.gold_ingot))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertFalse(result);
        Assertions.assertEquals(2, trie.size());

        // add another valid recipe

        result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.gold_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertTrue(result);
        Assertions.assertEquals(3, trie.size());

        // adding the same recipe with a different input ordering should fail

        result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.gold_ingot), new ItemStack(Items.iron_ingot))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertFalse(result);
        Assertions.assertEquals(3, trie.size());

        // adding a new unique recipe should succeed

        result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.iron_ingot), new ItemStack(Items.flint))
            .fluidInputs(new FluidStack(FluidRegistry.WATER, 1))
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertTrue(result);
        Assertions.assertEquals(4, trie.size());

        // empty recipe inputs should fail

        result = trie.add(GT_RecipeBuilder.builder()
            .itemOutputs(new ItemStack(Items.diamond))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertFalse(result);
        Assertions.assertEquals(4, trie.size());

        // empty outputs do not matter to the trie and should succeed

        result = trie.add(GT_RecipeBuilder.builder()
            .itemInputs(new ItemStack(Items.melon_seeds))
            .duration(1)
            .eut(1)
            .build().get()
        );
        Assertions.assertTrue(result);
        Assertions.assertEquals(5, trie.size());
    }

    @Test
    public void testFind() {

    }

    @Test
    public void testStream() {

    }

    @Test
    public void testRemove() {

    }
}
