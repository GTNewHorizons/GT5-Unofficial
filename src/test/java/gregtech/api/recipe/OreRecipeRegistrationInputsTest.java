package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

class OreRecipeRegistrationInputsTest {

    @AfterEach
    void tearDown() {
        System.clearProperty(OreRecipeDedupeFlags.CANONICAL_INPUTS_PROPERTY);
    }

    @Test
    void usesConcreteSourceStackByDefault() {
        Item sourceItem = new Item().setUnlocalizedName("test.concrete.silicon");
        ItemStack sourceStack = new ItemStack(sourceItem, 3, 7);

        ItemStack recipeInput = OreRecipeRegistrationInputs
            .recipeInputStack(OrePrefixes.ingot, Materials.Silicon, sourceStack, 2);

        assertNotSame(sourceStack, recipeInput);
        assertSame(sourceItem, recipeInput.getItem());
        assertEquals(7, recipeInput.getItemDamage());
        assertEquals(2, recipeInput.stackSize);
    }
}
