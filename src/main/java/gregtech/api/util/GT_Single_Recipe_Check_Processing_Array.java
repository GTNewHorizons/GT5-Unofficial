package gregtech.api.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableMap;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;

/** Processing Array-specialized version of {@link gregtech.api.util.GT_Single_Recipe_Check}. */
public class GT_Single_Recipe_Check_Processing_Array extends GT_Single_Recipe_Check {

    protected final int recipeAmperage;

    /**
     * The machine type that the locked recipe is for. We will not allow running with other machines.
     */
    protected final ItemStack machineStack;

    protected GT_Single_Recipe_Check_Processing_Array(GT_MetaTileEntity_MultiBlockBase multiBlockBase, GT_Recipe recipe,
            ImmutableMap<GT_Utility.ItemId, Integer> itemCost, ImmutableMap<Fluid, Integer> fluidCost,
            int recipeAmperage, ItemStack machineStack) {
        super(multiBlockBase, recipe, itemCost, fluidCost);

        this.recipeAmperage = recipeAmperage;
        this.machineStack = machineStack;
    }

    public int getRecipeAmperage() {
        return recipeAmperage;
    }

    @Override
    public boolean checkRecipeInputsSingleStack(boolean consumeInputs) {
        throw new UnsupportedOperationException("Use checkRecipeInputs(boolean, int) instead.");
    }

    @Override
    public boolean checkRecipeInputs(boolean consumeInputs) {
        throw new UnsupportedOperationException("Use checkRecipeInputs(boolean, int) instead.");
    }

    /** Returns the number of parallel recipes, or 0 if recipe is not satisfied at all. */
    public int checkRecipeInputs(boolean consumeInputs, int maxParallel) {
        if (!GT_Utility.areStacksEqual(machineStack, multiBlockBase.mInventory[1])) {
            // Machine stack was modified. This is not allowed, so stop processing.
            return 0;
        }
        int parallel = maxParallel;

        List<ItemStack> items = null;
        if (totalItemCost > 0) {
            items = multiBlockBase.getStoredInputs();

            Map<GT_Utility.ItemId, Integer> itemMap = new HashMap<>();
            for (ItemStack itemStack : items) {
                itemMap.merge(GT_Utility.ItemId.createNoCopy(itemStack), itemStack.stackSize, Integer::sum);
            }

            for (Map.Entry<GT_Utility.ItemId, Integer> entry : itemCost.entrySet()) {
                parallel = Math.min(parallel, itemMap.getOrDefault(entry.getKey(), 0) / entry.getValue());
                if (parallel <= 0) {
                    return 0;
                }
            }
        }

        List<FluidStack> fluids = null;
        if (totalFluidCost > 0) {
            fluids = multiBlockBase.getStoredFluids();

            Map<Fluid, Integer> fluidMap = new HashMap<>();
            for (FluidStack fluidStack : fluids) {
                fluidMap.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
            }

            for (Map.Entry<Fluid, Integer> entry : fluidCost.entrySet()) {
                parallel = Math.min(parallel, fluidMap.getOrDefault(entry.getKey(), 0) / entry.getValue());
                if (parallel <= 0) {
                    return 0;
                }
            }
        }

        final int finalParallel = parallel;
        if (consumeInputs) {
            if (totalItemCost > 0) {
                int remainingItemCost = totalItemCost * finalParallel;
                Map<GT_Utility.ItemId, Integer> runningItemCost = itemCost.entrySet()
                                                                          .stream()
                                                                          .collect(
                                                                                  Collectors.toMap(
                                                                                          Map.Entry::getKey,
                                                                                          entry -> entry.getValue()
                                                                                                  * finalParallel));

                for (ItemStack itemStack : items) {
                    GT_Utility.ItemId key = GT_Utility.ItemId.createNoCopy(itemStack);
                    int runningCost = runningItemCost.getOrDefault(key, 0);
                    int paid = Math.min(itemStack.stackSize, runningCost);
                    itemStack.stackSize -= paid;
                    runningItemCost.put(key, runningCost - paid);

                    remainingItemCost -= paid;
                    if (remainingItemCost <= 0) {
                        break;
                    }
                }
            }

            if (totalFluidCost > 0) {
                int remainingFluidCost = totalFluidCost * finalParallel;
                Map<Fluid, Integer> runningFluidCost = fluidCost.entrySet()
                                                                .stream()
                                                                .collect(
                                                                        Collectors.toMap(
                                                                                Map.Entry::getKey,
                                                                                entry -> entry.getValue()
                                                                                        * finalParallel));

                for (FluidStack fluidStack : fluids) {
                    Fluid key = fluidStack.getFluid();
                    int runningCost = runningFluidCost.getOrDefault(key, 0);
                    int paid = Math.min(fluidStack.amount, runningCost);
                    fluidStack.amount -= paid;
                    runningFluidCost.put(key, runningCost - paid);

                    remainingFluidCost -= paid;
                    if (remainingFluidCost <= 0) {
                        break;
                    }
                }
            }
        }

        return finalParallel;
    }

    @Nullable

    public static GT_Single_Recipe_Check tryLoad(GT_MetaTileEntity_MultiBlockBase parent,
            GT_Recipe.GT_Recipe_Map recipeMap, NBTTagCompound tag, ItemStack machineStack) {
        if (recipeMap == null || machineStack == null) return null;
        GT_Recipe found = tryFindRecipe(parent, recipeMap, tag);
        if (found == null) return null;
        return new GT_Single_Recipe_Check_Processing_Array(
                parent,
                found,
                loadItemCost(tag),
                loadFluidCost(tag),
                recipeMap.mAmperage,
                machineStack.copy());
    }

    public static Builder processingArrayBuilder(GT_MetaTileEntity_MultiBlockBase multiBlockBase) {
        return new Builder(multiBlockBase);
    }

    public static final class Builder {

        private final GT_MetaTileEntity_MultiBlockBase multiBlockBase;

        // In order to compute which items and fluids are consumed by the recipe, we compare the
        // multi-block's items and fluids before and after inputs are consumed by the recipe.
        private Map<GT_Utility.ItemId, Integer> beforeItems;
        private Map<Fluid, Integer> beforeFluids;
        private Map<GT_Utility.ItemId, Integer> afterItems;
        private Map<Fluid, Integer> afterFluids;

        private GT_Recipe recipe;
        private int recipeAmperage;

        private Builder(GT_MetaTileEntity_MultiBlockBase multiBlockBase) {
            this.multiBlockBase = multiBlockBase;
        }

        /** Call this before inputs are consumed by the recipe. */
        public Builder setBefore(ItemStack[] inputs, FluidStack[] fluids) {
            beforeItems = buildItemMapDirect(inputs);
            beforeFluids = buildFluidMapDirect(fluids);
            return this;
        }

        static Map<GT_Utility.ItemId, Integer> buildItemMapDirect(ItemStack[] inputs) {
            Map<GT_Utility.ItemId, Integer> itemMap = new HashMap<>();
            for (ItemStack itemStack : inputs) {
                itemMap.merge(GT_Utility.ItemId.create(itemStack), itemStack.stackSize, Integer::sum);
            }
            return itemMap;
        }

        static Map<Fluid, Integer> buildFluidMapDirect(FluidStack[] fluids) {
            Map<Fluid, Integer> fluidMap = new HashMap<>();
            for (FluidStack fluidStack : fluids) {
                fluidMap.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
            }
            return fluidMap;
        }

        /** Call this after inputs are consumed by the recipe. */
        public Builder setAfter(ItemStack[] inputs, FluidStack[] fluids) {
            afterItems = buildItemMapDirect(inputs);
            afterFluids = buildFluidMapDirect(fluids);
            return this;
        }

        public Builder setRecipe(GT_Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public Builder setRecipeAmperage(int recipeAmperage) {
            this.recipeAmperage = recipeAmperage;
            return this;
        }

        public GT_Single_Recipe_Check_Processing_Array build() {
            ImmutableMap.Builder<GT_Utility.ItemId, Integer> itemCostBuilder = ImmutableMap.builder();
            for (Map.Entry<GT_Utility.ItemId, Integer> entry : beforeItems.entrySet()) {
                int cost = entry.getValue() - afterItems.getOrDefault(entry.getKey(), 0);
                if (cost > 0) {
                    itemCostBuilder.put(entry.getKey(), cost);
                }
            }

            ImmutableMap.Builder<Fluid, Integer> fluidCostBuilder = ImmutableMap.builder();
            for (Map.Entry<Fluid, Integer> entry : beforeFluids.entrySet()) {
                int cost = entry.getValue() - afterFluids.getOrDefault(entry.getKey(), 0);
                if (cost > 0) {
                    fluidCostBuilder.put(entry.getKey(), cost);
                }
            }

            return new GT_Single_Recipe_Check_Processing_Array(
                    multiBlockBase,
                    recipe,
                    itemCostBuilder.build(),
                    fluidCostBuilder.build(),
                    recipeAmperage,
                    multiBlockBase.mInventory[1].copy());
        }
    }
}
