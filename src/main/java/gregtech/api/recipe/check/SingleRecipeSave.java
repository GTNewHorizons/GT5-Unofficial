package gregtech.api.recipe.check;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.IRecipeLookUp;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Saves the last recipe on a machine with recipe lock enabled.
 */
public class SingleRecipeSave implements IRecipeLookUp {

    @Nonnull
    private final GT_Recipe recipe;
    @Nonnull
    private final RecipeMap<?> recipeMap;

    private SingleRecipeSave(@Nonnull GT_Recipe recipe, @Nonnull RecipeMap<?> recipeMap) {
        this.recipe = recipe;
        this.recipeMap = recipeMap;
    }

    @Nonnull
    public GT_Recipe getRecipe() {
        return recipe;
    }

    @Nonnull
    public RecipeMap<?> getRecipeMap() {
        return recipeMap;
    }

    public NBTTagCompound writeToNBT() {
        // Here we encode recipe input, output and all other important values.
        // At load time we do a recipe check again, so in case the recipe is gone, we can stop tracking.
        // Of course the next step would be auto migrating to new recipe (if any), but given
        // we don't yet have a mean to uniquely name a recipe, this will have to make do.
        // Consider move serialization code to GT_Recipe once this has been proven to work
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("recipemap", recipeMap.unlocalizedName);
        if (recipe.mInputs != null) {
            tag.setTag("inputs", writeList(recipe.mInputs, GT_Utility::saveItem));
        }
        if (recipe.mOutputs != null) {
            tag.setTag("outputs", writeList(recipe.mOutputs, GT_Utility::saveItem));
        }
        if (recipe.mChances != null) {
            tag.setIntArray("chances", recipe.mChances);
        }
        if (recipe.mFluidInputs != null) {
            tag.setTag(
                "fInputs",
                writeList(
                    recipe.mFluidInputs,
                    s -> s == null ? new NBTTagCompound() : s.writeToNBT(new NBTTagCompound())));
        }
        if (recipe.mFluidOutputs != null) {
            tag.setTag(
                "fOutputs",
                writeList(
                    recipe.mFluidOutputs,
                    s -> s == null ? new NBTTagCompound() : s.writeToNBT(new NBTTagCompound())));
        }
        tag.setInteger("eut", recipe.mEUt);
        tag.setInteger("duration", recipe.mDuration);
        tag.setInteger("specialValue", recipe.mSpecialValue);
        return tag;
    }

    private static <T, NBT extends NBTBase> NBTTagList writeList(T[] arr, Function<T, NBT> ser) {
        return writeList(Arrays.asList(arr), ser);
    }

    private static <T, NBT extends NBTBase> NBTTagList writeList(Collection<T> arr, Function<T, NBT> ser) {
        NBTTagList l = new NBTTagList();
        for (T t : arr) {
            l.appendTag(ser.apply(t));
        }
        return l;
    }

    @Nullable
    public static SingleRecipeSave tryLoad(RecipeMap<?> recipeMap, NBTTagCompound tag) {
        if (tag == null || tag.hasNoTags()) return null;

        RecipeMap<?> mapToUse;
        if (tag.hasKey("recipemap")) {
            String mapName = tag.getString("recipemap");
            RecipeMap<?> foundMap = RecipeMap.ALL_RECIPE_MAPS.get(mapName);
            if (foundMap != null) {
                mapToUse = foundMap;
            } else {
                mapToUse = recipeMap;
            }
        } else {
            mapToUse = recipeMap;
        }
        if (mapToUse == null) {
            return null;
        }

        GT_Recipe foundRecipe = tryFindRecipe(mapToUse, tag);
        if (foundRecipe == null) return null;
        return SingleRecipeSave.builder(mapToUse)
            .setRecipe(foundRecipe)
            .build();
    }

    private static GT_Recipe tryFindRecipe(@Nonnull RecipeMap<?> recipeMap, NBTTagCompound tag) {
        ItemStack[] inputs = GT_Utility.streamCompounds(tag.getTagList("inputs", Constants.NBT.TAG_COMPOUND))
            .map(GT_Utility::loadItem)
            .toArray(ItemStack[]::new);
        ItemStack[] outputs = GT_Utility.streamCompounds(tag.getTagList("outputs", Constants.NBT.TAG_COMPOUND))
            .map(GT_Utility::loadItem)
            .toArray(ItemStack[]::new);
        FluidStack[] fInputs = GT_Utility.streamCompounds(tag.getTagList("fInputs", Constants.NBT.TAG_COMPOUND))
            .map(FluidStack::loadFluidStackFromNBT)
            .toArray(FluidStack[]::new);
        FluidStack[] fOutputs = GT_Utility.streamCompounds(tag.getTagList("fOutputs", Constants.NBT.TAG_COMPOUND))
            .map(FluidStack::loadFluidStackFromNBT)
            .toArray(FluidStack[]::new);
        int eut = tag.getInteger("eut");
        GT_Recipe found = recipeMap.findRecipe(null, false, GT_Values.V[GT_Utility.getTier(eut)] + 2, fInputs, inputs);
        int[] chances = tag.getIntArray("chances");
        if (chances.length == 0) chances = null;
        if (found == null || !GT_Utility.equals(inputs, found.mInputs)
            || !Arrays.equals(fInputs, found.mFluidInputs)
            || !GT_Utility.equals(outputs, found.mOutputs)
            || !Arrays.equals(fOutputs, found.mFluidOutputs)
            || !Arrays.equals(chances, found.mChances)
            || found.mDuration != tag.getInteger("duration")
            || found.mEUt != eut
            || found.mSpecialValue != tag.getInteger("specialValue")) return null;
        return found;
    }

    public static Builder builder(@Nonnull RecipeMap<?> recipeMap) {
        return new Builder(Objects.requireNonNull(recipeMap));
    }

    @Override
    public int getAmperage() {
        return recipeMap.getFrontend()
            .getUIProperties().amperage;
    }

    @Override
    public Stream<GT_Recipe> matchRecipeStream(ItemStack[] rawItems, FluidStack[] fluids,
        @Nullable ItemStack specialSlot, @Nullable GT_Recipe cachedRecipe, boolean notUnificated,
        boolean dontCheckStackSizes, boolean forCollisionCheck) {
        ItemStack[] items;
        // Unification happens here in case the item input isn't already unificated.
        if (notUnificated) {
            items = GT_OreDictUnificator.getStackArray(true, (Object[]) rawItems);
        } else {
            items = rawItems;
        }
        return Stream.of(recipe)
            .filter(
                recipe -> recipeMap.getBackend()
                    .filterFindRecipe(recipe, items, fluids, specialSlot, dontCheckStackSizes))
            .map(
                recipe -> recipeMap.getBackend()
                    .modifyFoundRecipe(recipe, items, fluids, specialSlot))
            .filter(Objects::nonNull);
    }

    public static class Builder {

        private final RecipeMap<?> recipeMap;
        private GT_Recipe recipe;

        private Builder(@Nonnull RecipeMap<?> recipeMap) {
            this.recipeMap = recipeMap;
        }

        public Builder setRecipe(@Nonnull GT_Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public SingleRecipeSave build() {
            if (recipe == null) {
                throw new IllegalStateException("recipe is not set");
            }
            return new SingleRecipeSave(recipe, recipeMap);
        }
    }
}
