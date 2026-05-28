package gregtech.api.recipe.lookup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

import org.junit.jupiter.api.Test;

class GTRecipeLookupIngredientTest {

    @Test
    void normalItemKeysIgnoreStackSizeButKeepDamage() {
        Item item = itemWithSubtypes("lookup.item.damage");
        GTItemStackLookupIngredient recipeKey = GTItemStackLookupIngredient.fromRecipe(new ItemStack(item, 64, 1));
        GTItemStackLookupIngredient runtimeKey = GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 1, 1));
        GTItemStackLookupIngredient differentDamage = GTItemStackLookupIngredient
            .fromRuntime(new ItemStack(item, 1, 2));

        assertEquals(recipeKey, runtimeKey);
        assertEquals(runtimeKey, recipeKey);
        assertEquals(recipeKey.hashCode(), runtimeKey.hashCode());
        assertNotEquals(recipeKey, differentDamage);
    }

    @Test
    void itemKeysKeepDamageForNonSubtypeDamageableItems() {
        Item item = new Item().setUnlocalizedName("lookup.item.damageable")
            .setMaxDamage(100);
        GTItemStackLookupIngredient damageOne = GTItemStackLookupIngredient.fromRecipe(new ItemStack(item, 1, 1));
        GTItemStackLookupIngredient alsoDamageOne = GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 64, 1));
        GTItemStackLookupIngredient damageTwo = GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 1, 2));

        assertFalse(item.getHasSubtypes());
        assertEquals(damageOne, alsoDamageOne);
        assertEquals(damageOne.hashCode(), alsoDamageOne.hashCode());
        assertNotEquals(damageOne, damageTwo);
    }

    @Test
    void wildcardItemKeysUseWildcardDamageSymmetrically() {
        Item item = itemWithSubtypes("lookup.item.wildcard");
        GTItemStackLookupIngredient recipeWildcard = GTItemStackLookupIngredient
            .fromRecipe(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE));
        GTItemStackLookupIngredient runtimeWildcard = GTItemStackLookupIngredient
            .fromRuntimeWildcard(new ItemStack(item, 64, 2));
        GTItemStackLookupIngredient runtimeExact = GTItemStackLookupIngredient.fromRuntime(new ItemStack(item, 64, 2));

        assertEquals(recipeWildcard, runtimeWildcard);
        assertEquals(runtimeWildcard, recipeWildcard);
        assertEquals(recipeWildcard.hashCode(), runtimeWildcard.hashCode());
        assertNotEquals(recipeWildcard, runtimeExact);
    }

    @Test
    void nbtSensitiveItemKeysAreMarkedAndDistinctFromNormalItemKeys() {
        ItemStack stack = new ItemStack(itemWithSubtypes("lookup.item.nbt"), 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("integer", 123456);
        stack.setTagCompound(tag);
        ItemStack stackWithDifferentTag = new ItemStack(stack.getItem(), 1, 0);
        NBTTagCompound differentTag = new NBTTagCompound();
        differentTag.setInteger("integer", 654321);
        stackWithDifferentTag.setTagCompound(differentTag);

        GTItemStackLookupIngredient normal = GTItemStackLookupIngredient.fromRecipe(stack);
        GTItemStackLookupIngredient nbtSensitive = GTItemStackLookupIngredient.fromNbtSensitiveRecipe(stack);
        GTItemStackLookupIngredient nbtSensitiveWithDifferentTag = GTItemStackLookupIngredient
            .fromNbtSensitiveRecipe(stackWithDifferentTag);

        assertFalse(normal.isNbtSensitive());
        assertTrue(nbtSensitive.isNbtSensitive());
        assertNotEquals(normal, nbtSensitive);
        // The NBT-sensitive key only marks the candidate path. Final NBT equality is checked by GTRecipe validation.
        assertEquals(nbtSensitive, nbtSensitiveWithDifferentTag);
    }

    @Test
    void oreDictKeysUseOreId() {
        GTOreDictLookupIngredient recipeKey = new GTOreDictLookupIngredient(1234);
        GTOreDictLookupIngredient runtimeKey = new GTOreDictLookupIngredient(1234);
        GTOreDictLookupIngredient differentOre = new GTOreDictLookupIngredient(1235);

        assertEquals(recipeKey, runtimeKey);
        assertEquals(runtimeKey, recipeKey);
        assertEquals(recipeKey.hashCode(), runtimeKey.hashCode());
        assertNotEquals(recipeKey, differentOre);
    }

    @Test
    void fluidKeysUseFluidIdentity() {
        Fluid water = new Fluid("lookup_test_water");
        Fluid lava = new Fluid("lookup_test_lava");
        GTFluidLookupIngredient recipeKey = new GTFluidLookupIngredient(water);
        GTFluidLookupIngredient runtimeKey = new GTFluidLookupIngredient(water);
        GTFluidLookupIngredient differentFluid = new GTFluidLookupIngredient(lava);

        assertEquals(recipeKey, runtimeKey);
        assertEquals(runtimeKey, recipeKey);
        assertEquals(recipeKey.hashCode(), runtimeKey.hashCode());
        assertNotEquals(recipeKey, differentFluid);
    }

    private static Item itemWithSubtypes(String name) {
        return new Item().setUnlocalizedName(name)
            .setHasSubtypes(true);
    }
}
