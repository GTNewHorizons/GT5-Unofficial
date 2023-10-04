package gregtech.api.recipe.check;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableMap;

import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Utility.ItemId;

/**
 * Used by machines that are locked to a single recipe, for faster recipe check.
 * <p>
 * Computation time will be like these:
 * <ul>
 * Normal recipe check:
 * <ul>
 * {@link RecipeMap#findRecipeWithResult Find recipe from recipemap}: O(NCR)
 * where N = number of machine inputs, C = average amount of recipe candidates found for specific input,
 * R = computation time to {@link GT_Recipe#isRecipeInputEqual check if inputs match to recipe}
 * </ul>
 * <ul>
 * {@link GT_Recipe#isRecipeInputEqual Check if inputs match to recipe}: O(NM)
 * where N = number of machine inputs, M = number of recipe inputs
 * </ul>
 * </ul>
 * <ul>
 * {@link #checkRecipeInputs Single recipe check}: O(N + M)
 * where N = number of machine inputs, M = number of recipe inputs
 * </ul>
 */
public class SingleRecipeCheck {

    @Nonnull
    private final GT_Recipe recipe;
    @Nonnull
    private final RecipeMap<?> recipeMap;
    @Nonnull
    private final ImmutableMap<ItemId, Integer> itemCost;
    @Nonnull
    private final ImmutableMap<Fluid, Integer> fluidCost;

    private final int totalItemCost;
    private final int totalFluidCost;

    private SingleRecipeCheck(@Nonnull GT_Recipe recipe, @Nonnull RecipeMap<?> recipeMap,
        @Nonnull ImmutableMap<ItemId, Integer> itemCost, @Nonnull ImmutableMap<Fluid, Integer> fluidCost) {
        this.recipe = recipe;
        this.recipeMap = recipeMap;
        this.itemCost = itemCost;
        this.fluidCost = fluidCost;

        this.totalItemCost = itemCost.values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
        this.totalFluidCost = fluidCost.values()
            .stream()
            .mapToInt(Integer::intValue)
            .sum();
    }

    @Nonnull
    public GT_Recipe getRecipe() {
        return recipe;
    }

    @Nonnull
    public RecipeMap<?> getRecipeMap() {
        return recipeMap;
    }

    /**
     * Returns the number of parallel recipes, or 0 if recipe is not satisfied at all.
     */
    public int checkRecipeInputs(boolean consumeInputs, int maxParallel, ItemStack[] itemInputs,
        FluidStack[] fluidInputs) {
        int currentParallel = maxParallel;

        if (totalItemCost > 0) {
            // Create map for item -> stored amount
            Map<ItemId, Integer> itemMap = new HashMap<>();
            for (ItemStack itemStack : itemInputs) {
                if (itemStack == null) continue;
                itemMap.merge(ItemId.createNoCopy(itemStack), itemStack.stackSize, Integer::sum);
            }

            // Check how many parallels can it perform for each item
            for (Map.Entry<ItemId, Integer> costEntry : itemCost.entrySet()) {
                currentParallel = Math
                    .min(currentParallel, itemMap.getOrDefault(costEntry.getKey(), 0) / costEntry.getValue());
                if (currentParallel <= 0) {
                    return 0;
                }
            }
        }

        if (totalFluidCost > 0) {
            // Create map for fluid -> stored amount
            Map<Fluid, Integer> fluidMap = new HashMap<>();
            for (FluidStack fluidStack : fluidInputs) {
                if (fluidStack == null) continue;
                fluidMap.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
            }

            // Check how many parallels can it perform for each fluid
            for (Map.Entry<Fluid, Integer> costEntry : fluidCost.entrySet()) {
                currentParallel = Math
                    .min(currentParallel, fluidMap.getOrDefault(costEntry.getKey(), 0) / costEntry.getValue());
                if (currentParallel <= 0) {
                    return 0;
                }
            }
        }

        final int finalParallel = currentParallel;
        if (consumeInputs) {
            if (totalItemCost > 0) {
                int remainingItemCost = totalItemCost * finalParallel;
                Map<ItemId, Integer> runningItemCost = itemCost.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() * finalParallel));

                for (ItemStack itemStack : itemInputs) {
                    if (itemStack == null) continue;
                    ItemId key = ItemId.createNoCopy(itemStack);
                    int runningCost = runningItemCost.getOrDefault(key, 0);
                    int paid = Math.min(itemStack.stackSize, runningCost);
                    itemStack.stackSize -= paid;
                    runningItemCost.put(key, runningCost - paid);

                    remainingItemCost -= paid;
                    // If all item costs are paid, we don't need to iterate inputs furthermore
                    if (remainingItemCost <= 0) {
                        break;
                    }
                }
            }

            if (totalFluidCost > 0) {
                int remainingFluidCost = totalFluidCost * finalParallel;
                Map<Fluid, Integer> runningFluidCost = fluidCost.entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue() * finalParallel));

                for (FluidStack fluidStack : fluidInputs) {
                    if (fluidStack == null) continue;
                    Fluid key = fluidStack.getFluid();
                    int runningCost = runningFluidCost.getOrDefault(key, 0);
                    int paid = Math.min(fluidStack.amount, runningCost);
                    fluidStack.amount -= paid;
                    runningFluidCost.put(key, runningCost - paid);

                    remainingFluidCost -= paid;
                    // If all fluid costs are paid, we don't need to iterate inputs furthermore
                    if (remainingFluidCost <= 0) {
                        break;
                    }
                }
            }
        }

        return finalParallel;
    }

    public NBTTagCompound writeToNBT() {
        // Here we encode recipe input, output and all other important values.
        // At load time we do a recipe check again, so in case the recipe is gone, we can stop tracking.
        // Of course the next step would be auto migrating to new recipe (if any), but given
        // we don't yet have a mean to uniquely name a recipe, this will have to make do.
        // Consider move serialization code to GT_Recipe once this has been proven to work
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("recipemap", recipeMap.mUnlocalizedName);
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
        tag.setTag("itemCost", writeList(itemCost.entrySet(), e -> {
            NBTTagCompound ret = new NBTTagCompound();
            ret.setTag(
                "id",
                e.getKey()
                    .writeToNBT());
            ret.setInteger("count", e.getValue());
            return ret;
        }));
        tag.setTag("fluidCost", writeList(fluidCost.entrySet(), e -> {
            NBTTagCompound ret = new NBTTagCompound();
            ret.setString(
                "id",
                e.getKey()
                    .getName());
            ret.setInteger("count", e.getValue());
            return ret;
        }));
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
    public static SingleRecipeCheck tryLoad(RecipeMap<?> recipeMap, NBTTagCompound tag) {
        if (tag == null || tag.hasNoTags()) return null;

        RecipeMap<?> mapToUse;
        if (tag.hasKey("recipemap")) {
            String mapName = tag.getString("recipemap");
            RecipeMap<?> foundMap = RecipeMap.findRecipeMap(mapName);
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
        return new SingleRecipeCheck(foundRecipe, mapToUse, loadItemCost(tag), loadFluidCost(tag));
    }

    private static ImmutableMap<Fluid, Integer> loadFluidCost(NBTTagCompound tag) {
        return GT_Utility.streamCompounds(tag.getTagList("fluidCost", Constants.NBT.TAG_COMPOUND))
            .collect(
                GT_Utility
                    .toImmutableMapSerial(t -> FluidRegistry.getFluid(t.getString("id")), t -> t.getInteger("count")));
    }

    private static ImmutableMap<ItemId, Integer> loadItemCost(NBTTagCompound tag) {
        return GT_Utility.streamCompounds(tag.getTagList("itemCost", Constants.NBT.TAG_COMPOUND))
            .collect(
                GT_Utility
                    .toImmutableMapSerial(t -> ItemId.create(t.getCompoundTag("id")), t -> t.getInteger("count")));
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
        GT_Recipe found = recipeMap.findRecipe(null, false, GT_Values.V[GT_Utility.getTier(eut)], fInputs, inputs);
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

    private static ImmutableMap<ItemId, Integer> buildItemMap(ItemStack[] inputs) {
        Map<ItemId, Integer> itemMap = new HashMap<>();
        for (ItemStack itemStack : inputs) {
            if (itemStack == null) continue;
            itemMap.merge(ItemId.create(itemStack), itemStack.stackSize, Integer::sum);
        }
        return ImmutableMap.copyOf(itemMap);
    }

    private static ImmutableMap<Fluid, Integer> buildFluidMap(FluidStack[] fluids) {
        Map<Fluid, Integer> fluidMap = new HashMap<>();
        for (FluidStack fluidStack : fluids) {
            if (fluidStack == null) continue;
            fluidMap.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
        }
        return ImmutableMap.copyOf(fluidMap);
    }

    public static Builder builder(@Nonnull RecipeMap<?> recipeMap) {
        return new Builder(Objects.requireNonNull(recipeMap));
    }

    public static class Builder {

        private final RecipeMap<?> recipeMap;

        // In order to compute which items and fluids are consumed by the recipe, we compare the
        // multi-block's items and fluids before and after inputs are consumed by the recipe.
        private Map<ItemId, Integer> beforeItems;
        private Map<Fluid, Integer> beforeFluids;
        private Map<ItemId, Integer> afterItems;
        private Map<Fluid, Integer> afterFluids;

        private GT_Recipe recipe;

        private Builder(@Nonnull RecipeMap<?> recipeMap) {
            this.recipeMap = recipeMap;
        }

        public Builder setBefore(ItemStack[] inputs, FluidStack[] fluids) {
            beforeItems = buildItemMap(inputs);
            beforeFluids = buildFluidMap(fluids);
            return this;
        }

        public Builder setAfter(ItemStack[] inputs, FluidStack[] fluids) {
            afterItems = buildItemMap(inputs);
            afterFluids = buildFluidMap(fluids);
            return this;
        }

        public Builder setRecipe(@Nonnull GT_Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        private ImmutableMap<ItemId, Integer> buildItemCost() {
            ImmutableMap.Builder<ItemId, Integer> itemCostBuilder = ImmutableMap.builder();
            for (Map.Entry<ItemId, Integer> entry : beforeItems.entrySet()) {
                int cost = entry.getValue() - afterItems.getOrDefault(entry.getKey(), 0);
                if (cost > 0) {
                    itemCostBuilder.put(entry.getKey(), cost);
                }
            }
            return itemCostBuilder.build();
        }

        private ImmutableMap<Fluid, Integer> buildFluidCost() {
            ImmutableMap.Builder<Fluid, Integer> fluidCostBuilder = ImmutableMap.builder();
            for (Map.Entry<Fluid, Integer> entry : beforeFluids.entrySet()) {
                int cost = entry.getValue() - afterFluids.getOrDefault(entry.getKey(), 0);
                if (cost > 0) {
                    fluidCostBuilder.put(entry.getKey(), cost);
                }
            }
            return fluidCostBuilder.build();
        }

        public SingleRecipeCheck build() {
            if (recipe == null) {
                throw new IllegalStateException("recipe is not set");
            }
            return new SingleRecipeCheck(recipe, recipeMap, buildItemCost(), buildFluidCost());
        }
    }
}
