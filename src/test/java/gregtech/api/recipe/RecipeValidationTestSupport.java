package gregtech.api.recipe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.recipe.lookup.GTFluidLookupIngredient;
import gregtech.api.recipe.lookup.GTItemDataLookupIngredient;
import gregtech.api.recipe.lookup.GTItemStackLookupIngredient;
import gregtech.api.recipe.lookup.GTRecipeLookup;
import gregtech.api.recipe.lookup.GTRecipeLookupIngredient;
import gregtech.api.util.GTRecipe;
import sun.misc.Unsafe;
import sun.reflect.ReflectionFactory;

final class RecipeValidationTestSupport {

    static final Constructor<GTRecipe> GT_RECIPE_CONSTRUCTOR = constructorFor(GTRecipe.class);
    static final Constructor<GTRecipe.GTRecipe_WithAlt> GT_RECIPE_WITH_ALT_CONSTRUCTOR = constructorFor(
        GTRecipe.GTRecipe_WithAlt.class);
    static final Constructor<RecipeCategory> RECIPE_CATEGORY_CONSTRUCTOR = constructorFor(RecipeCategory.class);
    static final Unsafe UNSAFE = unsafe();

    private RecipeValidationTestSupport() {}

    static GTRecipe recipe(Item input, Item output, RecipeCategory category) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = new ItemStack[] { new ItemStack(input, 1, 0) };
        recipe.mOutputs = new ItemStack[] { new ItemStack(output, 1, 0) };
        recipe.mFluidInputs = new FluidStack[0];
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mEnabled = true;
        recipe.mCanBeBuffered = true;
        recipe.setRecipeCategory(category);
        finalizeAllocatedRecipe(recipe);
        return recipe;
    }

    static GTRecipe recipe(ItemStack[] inputs, FluidStack[] fluidInputs, Item output, RecipeCategory category) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = inputs;
        recipe.mOutputs = new ItemStack[] { new ItemStack(output, 1, 0) };
        recipe.mFluidInputs = fluidInputs;
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mEnabled = true;
        recipe.mCanBeBuffered = true;
        recipe.setRecipeCategory(category);
        finalizeAllocatedRecipe(recipe);
        return recipe;
    }

    static GTRecipe recipeWithoutInputs(Item output, RecipeCategory category) {
        GTRecipe recipe = allocate(GT_RECIPE_CONSTRUCTOR);
        recipe.mInputs = new ItemStack[0];
        recipe.mOutputs = new ItemStack[] { new ItemStack(output, 1, 0) };
        recipe.mFluidInputs = new FluidStack[0];
        recipe.mFluidOutputs = new FluidStack[0];
        recipe.mEnabled = true;
        recipe.mCanBeBuffered = true;
        recipe.setRecipeCategory(category);
        finalizeAllocatedRecipe(recipe);
        return recipe;
    }

    static RecipeMapBackend backendForValidationTests() {
        return new RecipeMapBackend(new RecipeMapBackendPropertiesBuilder()) {

            @Override
            protected Stream<GTRecipe> lookupCandidateStream(@Nullable ItemStack @NotNull [] items,
                @Nullable FluidStack @NotNull [] fluids) {
                List<GTRecipeLookupIngredient> ingredients = new ArrayList<>();
                Consumer<GTRecipeLookupIngredient> adder = ingredients::add;

                for (ItemStack item : items) {
                    if (item == null) continue;

                    GTItemStackLookupIngredient.fromRuntime(adder, item);
                    GTItemDataLookupIngredient.fromRuntime(adder, item);
                }

                for (FluidStack fluid : fluids) {
                    if (fluid == null || fluid.getFluid() == null) continue;

                    GTFluidLookupIngredient.fromRuntime(adder, fluid);
                }

                if (ingredients.isEmpty()) {
                    return Stream.empty();
                }

                Iterator<GTRecipe> iterator = recipeLookupForTests(this).iterator(ingredients);
                return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
            }
        };
    }

    private static GTRecipeLookup recipeLookupForTests(RecipeMapBackend backend) {
        try {
            Field field = RecipeMapBackend.class.getDeclaredField("recipeLookup");
            field.setAccessible(true);
            return (GTRecipeLookup) field.get(backend);
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    static RecipeCategory category() {
        return allocate(RECIPE_CATEGORY_CONSTRUCTOR);
    }

    static Item item(String name) {
        return new Item().setUnlocalizedName(name);
    }

    private static void finalizeAllocatedRecipe(GTRecipe recipe) {
        if (RecipeLookupValidator.shouldCaptureRecipeCallsites()) {
            if (recipe.stackTraces == null) {
                recipe.stackTraces = new ArrayList<>();
            }
            if (recipe.owners == null) {
                recipe.owners = new ArrayList<>();
            }
        }
    }

    static void ensureMinecraftStackComparisonItem() {
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

    static <T> T allocate(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Constructor<T> constructorFor(Class<T> clazz) {
        try {
            ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
            Constructor<Object> objectConstructor = Object.class.getDeclaredConstructor();
            Constructor<T> constructor = (Constructor<T>) reflectionFactory
                .newConstructorForSerialization(clazz, objectConstructor);
            constructor.setAccessible(true);
            return constructor;
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
}
