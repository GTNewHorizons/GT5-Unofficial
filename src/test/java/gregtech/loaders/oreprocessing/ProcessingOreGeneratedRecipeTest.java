package gregtech.loaders.oreprocessing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.junit.jupiter.api.Test;

import gregtech.api.recipe.metadata.EmptyRecipeMetadataStorage;
import gregtech.api.util.GTRecipe;
import sun.reflect.ReflectionFactory;

class ProcessingOreGeneratedRecipeTest {

    private static final Constructor<GTRecipe> GT_RECIPE_CONSTRUCTOR = constructorFor(GTRecipe.class);

    @Test
    void generatedOreRecipeKeyKeepsDistinctInputsWithSameOutputsSeparate() {
        Item firstOre = new Item().setUnlocalizedName("test.zinc.ore");
        Item secondOre = new Item().setUnlocalizedName("test.zinc.ore");
        ItemStack output = new ItemStack(new Item().setUnlocalizedName("test.crushed.zinc"), 2);

        assertNotEquals(
            ProcessingOre.generatedRecipeBodyKey(generatedRecipe(new ItemStack(firstOre), output)),
            ProcessingOre.generatedRecipeBodyKey(generatedRecipe(new ItemStack(secondOre), output)));
    }

    @Test
    void generatedOreRecipeKeyMatchesExactDuplicateInputAndOutput() {
        ItemStack input = new ItemStack(new Item().setUnlocalizedName("test.zinc.ore"));
        ItemStack output = new ItemStack(new Item().setUnlocalizedName("test.crushed.zinc"), 2);

        assertEquals(
            ProcessingOre.generatedRecipeBodyKey(generatedRecipe(input, output)),
            ProcessingOre.generatedRecipeBodyKey(generatedRecipe(input, output)));
    }

    private static GTRecipe generatedRecipe(ItemStack input, ItemStack output) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = new ItemStack[] { input };
        recipe.mOutputs = new ItemStack[] { output };
        recipe.mFluidInputs = new FluidStack[0];
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mInputChances = new int[0];
        recipe.mOutputChances = new int[0];
        recipe.mFluidInputChances = new int[0];
        recipe.mFluidOutputChances = new int[0];
        recipe.mDuration = 20;
        recipe.mEUt = 2;
        recipe.mSpecialValue = 0;
        recipe.isNBTSensitive = false;
        setMetadataStorage(recipe);
        return recipe;
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

    private static <T> T allocate(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    private static void setMetadataStorage(GTRecipe recipe) {
        try {
            Field field = GTRecipe.class.getDeclaredField("metadataStorage");
            field.setAccessible(true);
            field.set(recipe, EmptyRecipeMetadataStorage.INSTANCE);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }
}
