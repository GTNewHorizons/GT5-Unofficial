package gtPlusPlus.xmod.gregtech.loaders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.jupiter.api.Test;

class RecipeGenMultisUsingFluidInsteadOfCellsTest {

    @Test
    void copiedStacksPreserveInputAmountsWithoutAliasingSourceRecipes() {
        Item sourceItem = new Item().setUnlocalizedName("gtpp.multis.input.amount");
        ItemStack sourceStack = new ItemStack(sourceItem, 16, 0);

        ItemStack[] copied = RecipeGenMultisUsingFluidInsteadOfCells.copyStacks(new ItemStack[] { sourceStack });

        assertNotSame(sourceStack, copied[0]);
        assertEquals(16, sourceStack.stackSize);
        assertEquals(16, copied[0].stackSize);
    }
}
