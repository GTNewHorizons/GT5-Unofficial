package gregtech.api.util;

import static gregtech.api.enums.GT_Values.D2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Contract;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_MultiInput;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.EmptyRecipeMetadataStorage;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.api.util.item.ItemHolder;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_Input_ME;
import ic2.core.Ic2Items;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;

public class GT_Recipe implements Comparable<GT_Recipe> {

    /**
     * If you want to change the Output, feel free to modify or even replace the whole ItemStack Array, for Inputs,
     * please add a new Recipe, because of the HashMaps.
     */
    public ItemStack[] mInputs, mOutputs;
    /**
     * If you want to change the Output, feel free to modify or even replace the whole ItemStack Array, for Inputs,
     * please add a new Recipe, because of the HashMaps.
     */
    public FluidStack[] mFluidInputs, mFluidOutputs;
    /**
     * If you changed the amount of Array-Items inside the Output Array then the length of this Array must be larger or
     * equal to the Output Array. A chance of 10000 equals 100%
     */
    public int[] mChances;
    /**
     * An Item that needs to be inside the Special Slot, like for example the Copy Slot inside the Printer. This is only
     * useful for Fake Recipes in NEI, since findRecipe() and containsInput() don't give a shit about this Field. Lists
     * are also possible.
     */
    public Object mSpecialItems;

    public int mDuration, mEUt, mSpecialValue;
    /**
     * Use this to just disable a specific Recipe, but the Configuration enables that already for every single Recipe.
     */
    public boolean mEnabled = true;
    /**
     * If this Recipe is hidden from NEI
     */
    public boolean mHidden = false;
    /**
     * If this Recipe is Fake and therefore doesn't get found by the findRecipe Function (It is still in the HashMaps,
     * so that containsInput does return T on those fake Inputs)
     */
    public boolean mFakeRecipe = false;
    /**
     * If this Recipe can be stored inside a Machine in order to make Recipe searching more Efficient by trying the
     * previously used Recipe first. In case you have a Recipe Map overriding things and returning one time use Recipes,
     * you have to set this to F.
     */
    public boolean mCanBeBuffered = true;
    /**
     * If this Recipe needs the Output Slots to be completely empty. Needed in case you have randomised Outputs
     */
    public boolean mNeedsEmptyOutput = false;
    /**
     * If this is set to true, NBT equality is required for recipe check.
     */
    public boolean isNBTSensitive = false;
    /**
     * Used for describing recipes that do not fit the default recipe pattern (for example Large Boiler Fuels)
     */
    private String[] neiDesc = null;
    /**
     * Holds a set of metadata for this recipe.
     */
    @Nonnull
    private final IRecipeMetadataStorage metadataStorage;
    /**
     * Category this recipe belongs to. Recipes belonging to recipemap are forced to have non-null category when added,
     * otherwise it can be null.
     */
    private RecipeCategory recipeCategory;
    /**
     * Stores which mod added this recipe
     */
    public List<ModContainer> owners = new ArrayList<>();
    /**
     * Stores stack traces where this recipe was added
     */
    // BW wants to overwrite it, so no final
    public List<List<String>> stackTraces = new ArrayList<>();

    private GT_Recipe(GT_Recipe aRecipe, boolean shallow) {
        mInputs = shallow ? aRecipe.mInputs : GT_Utility.copyItemArray(aRecipe.mInputs);
        mOutputs = shallow ? aRecipe.mOutputs : GT_Utility.copyItemArray(aRecipe.mOutputs);
        mSpecialItems = aRecipe.mSpecialItems;
        mChances = aRecipe.mChances;
        mFluidInputs = shallow ? aRecipe.mFluidInputs : GT_Utility.copyFluidArray(aRecipe.mFluidInputs);
        mFluidOutputs = shallow ? aRecipe.mFluidOutputs : GT_Utility.copyFluidArray(aRecipe.mFluidOutputs);
        mDuration = aRecipe.mDuration;
        mSpecialValue = aRecipe.mSpecialValue;
        mEUt = aRecipe.mEUt;
        mNeedsEmptyOutput = aRecipe.mNeedsEmptyOutput;
        isNBTSensitive = aRecipe.isNBTSensitive;
        mCanBeBuffered = aRecipe.mCanBeBuffered;
        mFakeRecipe = aRecipe.mFakeRecipe;
        mEnabled = aRecipe.mEnabled;
        mHidden = aRecipe.mHidden;
        metadataStorage = EmptyRecipeMetadataStorage.INSTANCE;
        owners = new ArrayList<>(aRecipe.owners);
        reloadOwner();
    }

    /**
     * Only for {@link GT_RecipeBuilder}.
     */
    GT_Recipe(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs, FluidStack[] mFluidOutputs,
        int[] mChances, Object mSpecialItems, int mDuration, int mEUt, int mSpecialValue, boolean mEnabled,
        boolean mHidden, boolean mFakeRecipe, boolean mCanBeBuffered, boolean mNeedsEmptyOutput, boolean nbtSensitive,
        String[] neiDesc, @Nullable IRecipeMetadataStorage metadataStorage, RecipeCategory recipeCategory) {
        this.mInputs = mInputs;
        this.mOutputs = mOutputs;
        this.mFluidInputs = mFluidInputs;
        this.mFluidOutputs = mFluidOutputs;
        this.mChances = mChances;
        this.mSpecialItems = mSpecialItems;
        this.mDuration = mDuration;
        this.mEUt = mEUt;
        this.mSpecialValue = mSpecialValue;
        this.mEnabled = mEnabled;
        this.mHidden = mHidden;
        this.mFakeRecipe = mFakeRecipe;
        this.mCanBeBuffered = mCanBeBuffered;
        this.mNeedsEmptyOutput = mNeedsEmptyOutput;
        this.isNBTSensitive = nbtSensitive;
        this.neiDesc = neiDesc;
        this.metadataStorage = metadataStorage == null ? EmptyRecipeMetadataStorage.INSTANCE : metadataStorage.copy();
        this.recipeCategory = recipeCategory;

        reloadOwner();
    }

    public GT_Recipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        if (aInputs == null) aInputs = new ItemStack[0];
        if (aOutputs == null) aOutputs = new ItemStack[0];
        if (aFluidInputs == null) aFluidInputs = new FluidStack[0];
        if (aFluidOutputs == null) aFluidOutputs = new FluidStack[0];
        if (aChances == null) aChances = new int[aOutputs.length];
        if (aChances.length < aOutputs.length) aChances = Arrays.copyOf(aChances, aOutputs.length);

        aInputs = ArrayExt.withoutTrailingNulls(aInputs, ItemStack[]::new);
        aOutputs = ArrayExt.withoutTrailingNulls(aOutputs, ItemStack[]::new);
        aFluidInputs = ArrayExt.withoutNulls(aFluidInputs, FluidStack[]::new);
        aFluidOutputs = ArrayExt.withoutNulls(aFluidOutputs, FluidStack[]::new);

        GT_OreDictUnificator.setStackArray(true, aInputs);
        GT_OreDictUnificator.setStackArray(true, aOutputs);

        for (ItemStack tStack : aOutputs) GT_Utility.updateItemStack(tStack);

        for (int i = 0; i < aChances.length; i++) if (aChances[i] <= 0) aChances[i] = 10000;
        for (int i = 0; i < aFluidInputs.length; i++) aFluidInputs[i] = aFluidInputs[i].copy();
        for (int i = 0; i < aFluidOutputs.length; i++) aFluidOutputs[i] = aFluidOutputs[i].copy();

        if (aOptimize && aDuration >= 32) {
            ArrayList<ItemStack> tList = new ArrayList<>();
            tList.addAll(Arrays.asList(aInputs));
            tList.addAll(Arrays.asList(aOutputs));
            for (int i = 0; i < tList.size(); i++) if (tList.get(i) == null) tList.remove(i--);

            for (byte i = (byte) Math.min(64, aDuration / 16); i > 1; i--) if (aDuration / i >= 16) {
                boolean temp = true;
                for (ItemStack stack : tList) if (stack.stackSize % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) for (FluidStack aFluidInput : aFluidInputs) if (aFluidInput.amount % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) for (FluidStack aFluidOutput : aFluidOutputs) if (aFluidOutput.amount % i != 0) {
                    temp = false;
                    break;
                }
                if (temp) {
                    for (ItemStack itemStack : tList) itemStack.stackSize /= i;
                    for (FluidStack aFluidInput : aFluidInputs) aFluidInput.amount /= i;
                    for (FluidStack aFluidOutput : aFluidOutputs) aFluidOutput.amount /= i;
                    aDuration /= i;
                }
            }
        }

        mInputs = aInputs;
        mOutputs = aOutputs;
        mSpecialItems = aSpecialItems;
        mChances = aChances;
        mFluidInputs = aFluidInputs;
        mFluidOutputs = aFluidOutputs;
        mDuration = aDuration;
        mSpecialValue = aSpecialValue;
        mEUt = aEUt;
        metadataStorage = EmptyRecipeMetadataStorage.INSTANCE;
        // checkCellBalance();
        reloadOwner();
    }

    // aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
    public GT_Recipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4,
        int aSpecialValue, int aType) {
        this(
            true,
            new ItemStack[] { aInput1 },
            new ItemStack[] { aOutput1, aOutput2, aOutput3, aOutput4 },
            null,
            null,
            null,
            null,
            0,
            0,
            Math.max(1, aSpecialValue));

        if (mInputs.length > 0 && aSpecialValue > 0) {
            switch (aType) {
                // Diesel Generator
                case 0 -> {
                    RecipeMaps.dieselFuels.addRecipe(this);
                    RecipeMaps.largeBoilerFakeFuels.getBackend()
                        .addDieselRecipe(this);
                }
                // Gas Turbine
                case 1 -> RecipeMaps.gasTurbineFuels.addRecipe(this);

                // Thermal Generator
                case 2 -> RecipeMaps.hotFuels.addRecipe(this);

                // Plasma Generator
                case 4 -> RecipeMaps.plasmaFuels.addRecipe(this);

                // Magic Generator
                case 5 -> RecipeMaps.magicFuels.addRecipe(this);

                // Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
                default -> {
                    RecipeMaps.denseLiquidFuels.addRecipe(this);
                    RecipeMaps.largeBoilerFakeFuels.getBackend()
                        .addDenseLiquidRecipe(this);
                }
            }
        }
    }

    // Dummy GT_Recipe maker...
    public GT_Recipe(ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        this(
            true,
            aInputs,
            aOutputs,
            aSpecialItems,
            aChances,
            aFluidInputs,
            aFluidOutputs,
            aDuration,
            aEUt,
            aSpecialValue);
    }

    /**
     * Re-unificates all the items present in recipes.
     */
    public static void reInit() {
        GT_Log.out.println("GT_Mod: Re-Unificating Recipes.");
        for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            map.getBackend()
                .reInit();
        }
    }

    public ItemStack getRepresentativeInput(int aIndex) {
        if (aIndex < 0 || aIndex >= mInputs.length) return null;
        return GT_Utility.copyOrNull(mInputs[aIndex]);
    }

    public ItemStack getOutput(int aIndex) {
        if (aIndex < 0 || aIndex >= mOutputs.length) return null;
        return GT_Utility.copyOrNull(mOutputs[aIndex]);
    }

    /**
     * Dictates the ItemStacks displayed in the output slots of any NEI page handled by the default GT NEI handler.
     * Override to make shown items differ from a GT_Recipe's item output array
     *
     * @see gregtech.nei.GT_NEI_DefaultHandler
     * @param i Slot index
     * @return ItemStack to be displayed in the slot
     */
    public ItemStack getRepresentativeOutput(int i) {
        return getOutput(i);
    }

    public int getOutputChance(int aIndex) {
        if (mChances == null) return 10000;
        if (aIndex < 0 || aIndex >= mChances.length) return 10000;
        return mChances[aIndex];
    }

    public FluidStack getRepresentativeFluidInput(int aIndex) {
        if (aIndex < 0 || aIndex >= mFluidInputs.length || mFluidInputs[aIndex] == null) return null;
        return mFluidInputs[aIndex].copy();
    }

    public FluidStack getFluidOutput(int aIndex) {
        if (aIndex < 0 || aIndex >= mFluidOutputs.length || mFluidOutputs[aIndex] == null) return null;
        return mFluidOutputs[aIndex].copy();
    }

    public void checkCellBalance() {
        if (!D2 || mInputs.length < 1) return;

        int tInputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInputs);
        int tOutputAmount = GT_ModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutputs);

        if (tInputAmount < tOutputAmount) {
            if (!Materials.Tin.contains(mInputs)) {
                GT_Log.err.println("You get more Cells, than you put in? There must be something wrong.");
                new Exception().printStackTrace(GT_Log.err);
            }
        } else if (tInputAmount > tOutputAmount) {
            if (!Materials.Tin.contains(mOutputs)) {
                GT_Log.err.println("You get less Cells, than you put in? GT Machines usually don't destroy Cells.");
                new Exception().printStackTrace(GT_Log.err);
            }
        }
    }

    public GT_Recipe copy() {
        return new GT_Recipe(this, false);
    }

    public GT_Recipe copyShallow() {
        return new GT_Recipe(this, true);
    }

    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, FluidStack[] aFluidInputs,
        ItemStack... aInputs) {
        return isRecipeInputEqual(aDecreaseStacksizeBySuccess, false, 1, aFluidInputs, aInputs);
    }

    // For non-multiplied recipe amount values
    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes,
        FluidStack[] aFluidInputs, ItemStack... aInputs) {
        return isRecipeInputEqual(aDecreaseStacksizeBySuccess, aDontCheckStackSizes, 1, aFluidInputs, aInputs);
    }

    /**
     * Okay, did some code archeology to figure out what's going on here.
     *
     * <p>
     * This variable was added in <a
     * href=https://github.com/GTNewHorizons/GT5-Unofficial/commit/9959ab7443982a19ad329bca424ab515493432e9>this
     * commit,</a> in order to fix the issues mentioned in <a
     * href=https://github.com/GTNewHorizons/GT5-Unofficial/pull/183>the PR</a>.
     *
     * <p>
     * It looks like it controls checking NBT. At this point, since we are still using universal fluid cells which store
     * their fluids in NBT, it probably will not be safe to disable the NBT checks in the near future. Data sticks may
     * be another case. Anyway, we probably can't get rid of this without some significant changes to clean up recipe
     * inputs.
     */
    public static boolean GTppRecipeHelper;

    /**
     * WARNING: Do not call this method with both {@code aDecreaseStacksizeBySuccess} and {@code aDontCheckStackSizes}
     * set to {@code true}! You'll get weird behavior.
     */
    public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes,
        int amountMultiplier, FluidStack[] aFluidInputs, ItemStack... aInputs) {
        double maxParallel = maxParallelCalculatedByInputs(amountMultiplier, aFluidInputs, aInputs);
        if (aDontCheckStackSizes) {
            return maxParallel > 0;
        } else if (maxParallel >= amountMultiplier) {
            if (aDecreaseStacksizeBySuccess) {
                consumeInput(amountMultiplier, aFluidInputs, aInputs);
            }
            return true;
        }
        return false;
    }

    /**
     * WARNING: Ensure that item inputs and fluid inputs are enough to be consumed with
     * {@link #maxParallelCalculatedByInputs} before calling this method!
     */
    public void consumeInput(int amountMultiplier, FluidStack[] aFluidInputs, ItemStack... aInputs) {
        if (amountMultiplier <= 0) return;

        long remainingCost;

        if (aFluidInputs != null) {
            for (FluidStack recipeFluidCost : mFluidInputs) {
                if (recipeFluidCost != null) {
                    remainingCost = (long) recipeFluidCost.amount * amountMultiplier;

                    for (FluidStack providedFluid : aFluidInputs) {
                        if (providedFluid != null && providedFluid.isFluidEqual(recipeFluidCost)) {
                            if (providedFluid.amount >= remainingCost) {
                                providedFluid.amount -= remainingCost;
                                break;
                            } else {
                                remainingCost -= providedFluid.amount;
                                providedFluid.amount = 0;
                            }
                        }
                    }
                }
            }
        }

        if (aInputs != null) {
            for (ItemStack recipeItemCost : mInputs) {
                ItemStack unifiedItemCost = GT_OreDictUnificator.get_nocopy(true, recipeItemCost);
                if (unifiedItemCost != null) {
                    remainingCost = (long) recipeItemCost.stackSize * amountMultiplier;

                    for (ItemStack providedItem : aInputs) {
                        if (isNBTSensitive && !GT_Utility.areStacksEqual(providedItem, unifiedItemCost, false)) {
                            continue;
                        } else if (!isNBTSensitive
                            && !GT_OreDictUnificator.isInputStackEqual(providedItem, unifiedItemCost)) {
                                continue;
                            }

                        if (GTppRecipeHelper) { // Please see JavaDoc on GTppRecipeHelper for why this is here.
                            if (GT_Utility.areStacksEqual(providedItem, Ic2Items.FluidCell.copy(), true)
                                || GT_Utility.areStacksEqual(providedItem, ItemList.Tool_DataStick.get(1L), true)
                                || GT_Utility.areStacksEqual(providedItem, ItemList.Tool_DataOrb.get(1L), true)) {
                                if (!GT_Utility.areStacksEqual(providedItem, recipeItemCost, false)) continue;
                            }
                        }

                        if (providedItem.stackSize >= remainingCost) {
                            providedItem.stackSize -= remainingCost;
                            break;
                        } else {
                            remainingCost -= providedItem.stackSize;
                            providedItem.stackSize = 0;
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the number of parallel recipes, or 0 if recipe is not satisfied at all. 0 < number < 1 means that inputs
     * are found but not enough.
     */
    public double maxParallelCalculatedByInputs(int maxParallel, FluidStack[] aFluidInputs, ItemStack... aInputs) {
        if (mInputs.length > 0 && aInputs == null) return 0;
        if (mFluidInputs.length > 0 && aFluidInputs == null) return 0;

        double currentParallel = maxParallel;

        // We need to have any fluids inputs, otherwise the code below does nothing. The second check is always true
        // because of early exit condition above.
        if (mFluidInputs.length > 0 /* && aFluidInputs != null */) {
            // Create map for fluid -> stored amount
            Reference2LongMap<Fluid> fluidMap = new Reference2LongArrayMap<>(2);
            Reference2LongMap<Fluid> fluidCost = new Reference2LongArrayMap<>(2);
            for (FluidStack fluidStack : aFluidInputs) {
                if (fluidStack == null) continue;
                fluidMap.mergeLong(fluidStack.getFluid(), fluidStack.amount, Long::sum);
            }
            for (FluidStack fluidStack : mFluidInputs) {
                if (fluidStack == null) continue;
                fluidCost.mergeLong(fluidStack.getFluid(), fluidStack.amount, Long::sum);
            }

            // Check how many parallels can it perform for each fluid
            for (Reference2LongMap.Entry<Fluid> costEntry : fluidCost.reference2LongEntrySet()) {
                if (costEntry.getLongValue() > 0) {
                    currentParallel = Math.min(
                        currentParallel,
                        (double) fluidMap.getOrDefault(costEntry.getKey(), 0L) / costEntry.getLongValue());
                }
                if (currentParallel <= 0) {
                    return 0;
                }
            }
        }

        if (mInputs.length > 0) {
            double remainingCost;
            long providedAmount;
            Object2LongMap<GT_Utility.ItemId> itemCostMap = new Object2LongArrayMap<>(2);

            for (ItemStack itemStack : mInputs) {
                if (itemStack == null) continue;
                if (shouldCheckNBT(itemStack)) {
                    GT_Utility.ItemId itemId = GT_Utility.ItemId.createNoCopy(itemStack);
                    itemCostMap.mergeLong(itemId, itemStack.stackSize, Long::sum);
                    continue;
                }
                ItemStack unifiedItem = GT_OreDictUnificator.get_nocopy(true, itemStack);
                if (unifiedItem != null) {
                    GT_Utility.ItemId unifiedId;
                    if (isNBTSensitive) unifiedId = GT_Utility.ItemId.createNoCopy(unifiedItem);
                    else unifiedId = GT_Utility.ItemId.createWithoutNBT(unifiedItem);
                    itemCostMap.mergeLong(unifiedId, itemStack.stackSize, Long::sum);
                }
            }

            ItemStack unifiedItemCost;
            nextRecipeItemCost: for (Map.Entry<GT_Utility.ItemId, Long> costEntry : itemCostMap.entrySet()) {
                unifiedItemCost = costEntry.getKey()
                    .getItemStack();
                if (unifiedItemCost != null) {
                    remainingCost = costEntry.getValue() * currentParallel;
                    providedAmount = 0;

                    for (ItemStack providedItem : aInputs) {
                        if (!areInputStackAndRecipeCostMatched(providedItem, unifiedItemCost)) continue;
                        // for non-consumed input
                        if (costEntry.getValue() == 0) continue nextRecipeItemCost;

                        providedAmount += providedItem.stackSize;

                        if (providedAmount >= remainingCost) continue nextRecipeItemCost;
                    }
                    if (providedAmount == 0) return 0;
                    currentParallel = Math.min(currentParallel, (double) providedAmount / costEntry.getValue());
                }
            }
        }
        return currentParallel;
    }

    private boolean areInputStackAndRecipeCostMatched(ItemStack providedItem, ItemStack unifiedItemCost) {
        if (isNBTSensitive || shouldCheckNBT(providedItem)) {
            return GT_Utility.areStacksEqual(providedItem, unifiedItemCost, false);
        } else {
            return GT_OreDictUnificator.isInputStackEqual(providedItem, unifiedItemCost);
        }
    }

    /**
     * Please see JavaDoc on {@link #GTppRecipeHelper} for why this is here.
     */
    private boolean shouldCheckNBT(ItemStack item) {
        if (GTppRecipeHelper) {
            return GT_Utility.areStacksEqual(item, Ic2Items.FluidCell.copy(), true)
                || GT_Utility.areStacksEqual(item, ItemList.Tool_DataStick.get(1L), true)
                || GT_Utility.areStacksEqual(item, ItemList.Tool_DataOrb.get(1L), true);
        }
        return false;
    }

    public boolean isRecipePossible(@Nullable ItemInventoryLogic itemInput, @Nullable FluidInventoryLogic fluidInput) {
        return getAmountOfRecipesDone(itemInput, fluidInput, 1, true) > 0;
    }

    public long getAmountOfRecipesDone(@Nullable ItemInventoryLogic itemInput, @Nullable FluidInventoryLogic fluidInput,
        long maxParallel, boolean simulate) {
        if (itemInput == null) {
            itemInput = new ItemInventoryLogic(0);
        }

        if (fluidInput == null) {
            fluidInput = new FluidInventoryLogic(0, 0);
        }

        itemInput.startRecipeCheck();
        Map<ItemHolder, Long> recipeItems = getItemInputsAsItemMap();
        for (Entry<ItemHolder, Long> entry : recipeItems.entrySet()) {
            maxParallel = Math
                .min(maxParallel, itemInput.calculateAmountOfTimesItemCanBeTaken(entry.getKey(), entry.getValue()));
        }

        for (FluidStack fluid : mFluidInputs) {
            if (fluid == null) continue;
            maxParallel = Math
                .min(maxParallel, fluidInput.calculateAmountOfTimesFluidCanBeTaken(fluid.getFluid(), fluid.amount));
        }

        if (simulate) {
            itemInput.stopRecipeCheck();
            return maxParallel;
        }

        for (Entry<ItemHolder, Long> entry : recipeItems.entrySet()) {
            itemInput.subtractItemAmount(entry.getKey(), entry.getValue() * maxParallel, false);
        }

        for (FluidStack fluid : mFluidInputs) {
            if (fluid == null) continue;
            fluidInput.drain(fluid.getFluid(), fluid.amount * maxParallel, false);
        }
        itemInput.stopRecipeCheck();
        return maxParallel;
    }

    private Map<ItemHolder, Long> getItemInputsAsItemMap() {
        Map<ItemHolder, Long> items = new HashMap<>();
        for (ItemStack item : mInputs) {
            if (item == null) continue;
            ItemHolder itemHolder = new ItemHolder(item);
            items.put(itemHolder, items.getOrDefault(itemHolder, 0L) + item.stackSize);
        }
        return items;
    }

    @Override
    public int compareTo(GT_Recipe recipe) {
        // first lowest tier recipes
        // then fastest
        // then with lowest special value
        // then dry recipes
        // then with fewer inputs
        if (this.mEUt != recipe.mEUt) {
            return this.mEUt - recipe.mEUt;
        } else if (this.mDuration != recipe.mDuration) {
            return this.mDuration - recipe.mDuration;
        } else if (this.mSpecialValue != recipe.mSpecialValue) {
            return this.mSpecialValue - recipe.mSpecialValue;
        } else if (this.mFluidInputs.length != recipe.mFluidInputs.length) {
            return this.mFluidInputs.length - recipe.mFluidInputs.length;
        } else if (this.mInputs.length != recipe.mInputs.length) {
            return this.mInputs.length - recipe.mInputs.length;
        }
        return 0;
    }

    public String[] getNeiDesc() {
        return neiDesc;
    }

    /**
     * Sets description shown on NEI. <br>
     * If you have a large number of recipes for the recipemap, this is not efficient memory wise, so use
     * {@link gregtech.api.recipe.RecipeMapBuilder#neiSpecialInfoFormatter} instead.
     */
    public void setNeiDesc(String... neiDesc) {
        this.neiDesc = neiDesc;
    }

    // region metadata

    // Don't try implementing setMetadata, as metadataStorage can be EmptyRecipeMetadataStorage

    /**
     * Gets metadata associated with this recipe. Can return null. Use
     * {@link #getMetadataOrDefault(RecipeMetadataKey, Object)}
     * if you want to specify default value.
     */
    @Nullable
    public <T> T getMetadata(RecipeMetadataKey<T> key) {
        return key.cast(metadataStorage.getMetadata(key));
    }

    /**
     * Gets metadata associated with this recipe with default value. Does not return null unless default value is null.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public <T> T getMetadataOrDefault(RecipeMetadataKey<T> key, @Nullable T defaultValue) {
        return key.cast(metadataStorage.getMetadataOrDefault(key, defaultValue));
    }

    @Nonnull
    public IRecipeMetadataStorage getMetadataStorage() {
        return metadataStorage;
    }

    // endregion

    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    /**
     * Exists only for recipe copying from external. For ordinal use case, use {@link GT_RecipeBuilder#recipeCategory}.
     */
    public void setRecipeCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    private static final List<String> excludedStacktraces = Arrays.asList(
        "java.lang.Thread",
        "gregtech.api.interfaces.IRecipeMap",
        "gregtech.api.interfaces.IRecipeMap$1",
        "gregtech.api.recipe.RecipeMap",
        "gregtech.api.recipe.RecipeMapBackend",
        "gregtech.api.recipe.RecipeMapBackendPropertiesBuilder",
        "gregtech.api.util.GT_Recipe",
        "gregtech.api.util.GT_RecipeBuilder",
        "gregtech.api.util.GT_RecipeConstants",
        "gregtech.api.util.GT_RecipeMapUtil",
        "gregtech.common.GT_RecipeAdder");

    public void reloadOwner() {
        setOwner(
            Loader.instance()
                .activeModContainer());

        if (GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace) {
            List<String> toAdd = new ArrayList<>();
            for (StackTraceElement stackTrace : Thread.currentThread()
                .getStackTrace()) {
                if (excludedStacktraces.stream()
                    .noneMatch(
                        c -> stackTrace.getClassName()
                            .equals(c))) {
                    toAdd.add(formatStackTrace(stackTrace));
                }
            }
            stackTraces.add(toAdd);
        }
    }

    private static String formatStackTrace(StackTraceElement stackTraceElement) {
        String raw = stackTraceElement.toString();
        int startParen = raw.lastIndexOf('(');
        int colon = raw.lastIndexOf(':');
        if (colon == -1) {
            // native or unknown source
            return raw;
        }
        // strip class name and leave line number, as class name is already shown
        return raw.substring(0, startParen + 1) + raw.substring(colon);
    }

    public void setOwner(ModContainer newOwner) {
        ModContainer oldOwner = !owners.isEmpty() ? this.owners.get(owners.size() - 1) : null;
        if (newOwner != null && newOwner != oldOwner) {
            owners.add(newOwner);
        }
    }

    /**
     * Use in case {@link Loader#activeModContainer()} isn't helpful
     */
    public void setOwner(String modId) {
        for (ModContainer mod : Loader.instance()
            .getModList()) {
            if (mod.getModId()
                .equals(modId)) {
                setOwner(mod);
                return;
            }
        }
    }

    public GT_Recipe setInputs(ItemStack... aInputs) {
        // TODO determine if we need this without trailing nulls call
        this.mInputs = ArrayExt.withoutTrailingNulls(aInputs, ItemStack[]::new);
        return this;
    }

    public GT_Recipe setOutputs(ItemStack... aOutputs) {
        this.mOutputs = ArrayExt.withoutTrailingNulls(aOutputs, ItemStack[]::new);
        return this;
    }

    public GT_Recipe setFluidInputs(FluidStack... aInputs) {
        this.mFluidInputs = ArrayExt.withoutTrailingNulls(aInputs, FluidStack[]::new);
        return this;
    }

    public GT_Recipe setFluidOutputs(FluidStack... aOutputs) {
        this.mFluidOutputs = ArrayExt.withoutTrailingNulls(aOutputs, FluidStack[]::new);
        return this;
    }

    public GT_Recipe setDuration(int aDuration) {
        this.mDuration = aDuration;
        return this;
    }

    public GT_Recipe setEUt(int aEUt) {
        this.mEUt = aEUt;
        return this;
    }

    public static class GT_Recipe_AssemblyLine {

        public static final ArrayList<GT_Recipe_AssemblyLine> sAssemblylineRecipes = new ArrayList<>();

        static {
            if (!Boolean.getBoolean("com.gtnh.gt5u.ignore-invalid-assline-recipe"))
                GregTech_API.sFirstWorldTick.add(GT_Recipe_AssemblyLine::checkInvalidRecipes);
            else GT_Log.out.println("NOT CHECKING INVALID ASSLINE RECIPE.");
        }

        private static void checkInvalidRecipes() {
            int invalidCount = 0;
            GT_Log.out.println("Started assline validation");
            for (GT_Recipe_AssemblyLine recipe : sAssemblylineRecipes) {
                if (recipe.getPersistentHash() == 0) {
                    invalidCount++;
                    GT_Log.err.printf("Invalid recipe: %s%n", recipe);
                }
            }
            if (invalidCount > 0) throw new RuntimeException(
                "There are " + invalidCount + " invalid assembly line recipe(s)! Check GregTech.log for details!");
        }

        public ItemStack mResearchItem;
        public int mResearchTime;
        public ItemStack[] mInputs;
        public FluidStack[] mFluidInputs;
        public ItemStack mOutput;
        public int mDuration;
        public int mEUt;
        public ItemStack[][] mOreDictAlt;
        private int mPersistentHash;

        /**
         * THIS CONSTRUCTOR DOES SET THE PERSISTENT HASH.
         * <p>
         * if you set one yourself, it will give you one of the RunetimeExceptions!
         */
        public GT_Recipe_AssemblyLine(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
            FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
            this(
                aResearchItem,
                aResearchTime,
                aInputs,
                aFluidInputs,
                aOutput,
                aDuration,
                aEUt,
                new ItemStack[aInputs.length][]);
            int tPersistentHash = 1;
            for (ItemStack tInput : aInputs)
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tInput, true, false);
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aResearchItem, true, false);
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aOutput, true, false);
            for (FluidStack tFluidInput : aFluidInputs)
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(tFluidInput, true, false);
            tPersistentHash = tPersistentHash * 31 + aResearchTime;
            tPersistentHash = tPersistentHash * 31 + aDuration;
            tPersistentHash = tPersistentHash * 31 + aEUt;
            setPersistentHash(tPersistentHash);
        }

        /**
         * THIS CONSTRUCTOR DOES <b>NOT</b> SET THE PERSISTENT HASH.
         * <p>
         * if you don't set one yourself, it will break a lot of stuff!
         */
        public GT_Recipe_AssemblyLine(ItemStack aResearchItem, int aResearchTime, ItemStack[] aInputs,
            FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt, ItemStack[][] aAlt) {
            mResearchItem = aResearchItem;
            mResearchTime = aResearchTime;
            mInputs = aInputs;
            mFluidInputs = aFluidInputs;
            mOutput = aOutput;
            mDuration = aDuration;
            mEUt = aEUt;
            mOreDictAlt = aAlt;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            GT_ItemStack[] thisInputs = new GT_ItemStack[this.mInputs.length];
            int totalInputStackSize = 0;
            for (int i = 0; i < this.mInputs.length; i++) {
                thisInputs[i] = new GT_ItemStack(this.mInputs[i]);
                totalInputStackSize += thisInputs[i].mStackSize;
            }
            int inputHash = Arrays.deepHashCode(thisInputs);
            int inputFluidHash = Arrays.deepHashCode(this.mFluidInputs);
            GT_ItemStack thisOutput = new GT_ItemStack(mOutput);
            GT_ItemStack thisResearch = new GT_ItemStack(mResearchItem);
            int miscRecipeDataHash = Arrays.deepHashCode(
                new Object[] { totalInputStackSize, mDuration, mEUt, thisOutput, thisResearch, mResearchTime });
            result = prime * result + inputFluidHash;
            result = prime * result + inputHash;
            result = prime * result + miscRecipeDataHash;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof GT_Recipe_AssemblyLine other)) {
                return false;
            }
            if (this.mInputs.length != other.mInputs.length) {
                return false;
            }
            if (this.mFluidInputs.length != other.mFluidInputs.length) {
                return false;
            }
            // Check Outputs Match
            GT_ItemStack output1 = new GT_ItemStack(this.mOutput);
            GT_ItemStack output2 = new GT_ItemStack(other.mOutput);
            if (!output1.equals(output2)) {
                return false;
            }
            // Check Scanned Item Match
            GT_ItemStack scan1 = new GT_ItemStack(this.mResearchItem);
            GT_ItemStack scan2 = new GT_ItemStack(other.mResearchItem);
            if (!scan1.equals(scan2)) {
                return false;
            }
            // Check Items Match
            GT_ItemStack[] thisInputs = new GT_ItemStack[this.mInputs.length];
            GT_ItemStack[] otherInputs = new GT_ItemStack[other.mInputs.length];
            for (int i = 0; i < thisInputs.length; i++) {
                thisInputs[i] = new GT_ItemStack(this.mInputs[i]);
                otherInputs[i] = new GT_ItemStack(other.mInputs[i]);
            }
            for (int i = 0; i < thisInputs.length; i++) {
                if (!thisInputs[i].equals(otherInputs[i]) || thisInputs[i].mStackSize != otherInputs[i].mStackSize) {
                    return false;
                }
            }
            // Check Fluids Match
            for (int i = 0; i < this.mFluidInputs.length; i++) {
                if (!this.mFluidInputs[i].isFluidStackIdentical(other.mFluidInputs[i])) {
                    return false;
                }
            }

            return this.mDuration == other.mDuration && this.mEUt == other.mEUt
                && this.mResearchTime == other.mResearchTime;
        }

        public int getPersistentHash() {
            if (mPersistentHash == 0)
                GT_Log.err.println("Assline recipe persistent hash has not been set! Recipe: " + mOutput);
            return mPersistentHash;
        }

        @Override
        public String toString() {
            return "GT_Recipe_AssemblyLine{" + "mResearchItem="
                + mResearchItem
                + ", mResearchTime="
                + mResearchTime
                + ", mInputs="
                + Arrays.toString(mInputs)
                + ", mFluidInputs="
                + Arrays.toString(mFluidInputs)
                + ", mOutput="
                + mOutput
                + ", mDuration="
                + mDuration
                + ", mEUt="
                + mEUt
                + ", mOreDictAlt="
                + Arrays.toString(mOreDictAlt)
                + '}';
        }

        /**
         * @param aPersistentHash the persistent hash. it should reflect the exact input used to generate this recipe If
         *                        0 is passed in, the actual persistent hash will be automatically remapped to 1
         *                        instead.
         * @throws IllegalStateException if the persistent hash has been set already
         */
        public void setPersistentHash(int aPersistentHash) {
            if (this.mPersistentHash != 0) throw new IllegalStateException("Cannot set persistent hash twice!");
            if (aPersistentHash == 0) this.mPersistentHash = 1;
            else this.mPersistentHash = aPersistentHash;
        }

        /**
         * @param inputBusses List of input busses to check.
         * @return An array containing the amount of item to consume from the first slot of every input bus.
         *         {@code null} if at least one item fails to match the recipe ingredient.
         */
        public static int[] getItemConsumptionAmountArray(ArrayList<GT_MetaTileEntity_Hatch_InputBus> inputBusses,
            GT_Recipe_AssemblyLine recipe) {
            int itemCount = recipe.mInputs.length;
            if (itemCount == 0) return null;
            int[] tStacks = new int[itemCount];
            for (int i = 0; i < itemCount; i++) {
                GT_MetaTileEntity_Hatch_InputBus inputBus = inputBusses.get(i);
                if (!inputBus.isValid()) return null;
                ItemStack slotStack;
                if (inputBus instanceof GT_MetaTileEntity_Hatch_InputBus_ME meBus) {
                    slotStack = meBus.getShadowItemStack(0);
                } else {
                    slotStack = inputBus.getStackInSlot(0);
                }
                if (slotStack == null) return null;

                int amount = getMatchedIngredientAmount(slotStack, recipe.mInputs[i], recipe.mOreDictAlt[i]);
                if (amount < 0) return null;

                tStacks[i] = amount;
            }
            return tStacks;
        }

        public static int getMatchedIngredientAmount(ItemStack aSlotStack, ItemStack aIngredient, ItemStack[] alts) {
            if (alts == null || alts.length == 0) {
                if (GT_Utility.areStacksEqual(aSlotStack, aIngredient, true)) {
                    return aIngredient.stackSize;
                }
                return -1;
            }
            for (ItemStack tAltStack : alts) {
                if (GT_Utility.areStacksEqual(aSlotStack, tAltStack, true)) {
                    return tAltStack.stackSize;
                }
            }
            return -1;
        }

        /**
         * @param inputBusses      Input bus list to check. Usually the input bus list of multi.
         * @param itemConsumptions Should be generated by {@link GT_Recipe_AssemblyLine#getItemConsumptionAmountArray}.
         * @Return The number of parallel recipes, or 0 if recipe is not satisfied at all. 0 < number < 1 means that
         *         inputs are found but not enough.
         */
        public static double maxParallelCalculatedByInputItems(ArrayList<GT_MetaTileEntity_Hatch_InputBus> inputBusses,
            int maxParallel, int[] itemConsumptions, Map<GT_Utility.ItemId, ItemStack> inputsFromME) {
            // Recipe item matching is done in the generation of itemConsumptions.

            Map<GT_Utility.ItemId, Long> itemConsumptionsFromME = new Object2LongOpenHashMap<>();
            double currentParallel = maxParallel;

            // Calculate the amount of each item to consume from ME
            for (int i = 0; i < itemConsumptions.length; i++) {
                GT_MetaTileEntity_Hatch_InputBus inputBus = inputBusses.get(i);
                if (!inputBus.isValid()) return 0;
                if (inputBus instanceof GT_MetaTileEntity_Hatch_InputBus_ME meBus) {
                    ItemStack item = meBus.getShadowItemStack(0);
                    if (item == null) return 0;
                    GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(item);
                    itemConsumptionsFromME.merge(id, (long) itemConsumptions[i], Long::sum);
                }
            }
            // Calculate parallel from ME input busses
            for (Entry<GT_Utility.ItemId, Long> entry : itemConsumptionsFromME.entrySet()) {
                if (!inputsFromME.containsKey(entry.getKey())) return 0;
                long consume = entry.getValue();
                // For non-consumed inputs
                if (consume == 0) continue;
                currentParallel = Math
                    .min(currentParallel, (double) inputsFromME.get(entry.getKey()).stackSize / consume);
                if (currentParallel <= 0) return 0;
            }

            // Calculate parallel from regular input busses
            for (int i = 0; i < itemConsumptions.length; i++) {
                GT_MetaTileEntity_Hatch_InputBus inputBus = inputBusses.get(i);
                if (!inputBus.isValid()) return 0;
                if (inputBus instanceof GT_MetaTileEntity_Hatch_InputBus_ME) continue;

                ItemStack item = inputBus.getStackInSlot(0);
                if (item == null) return 0;
                // For non-consumed inputs
                if (itemConsumptions[i] == 0) continue;
                currentParallel = Math.min(currentParallel, (double) item.stackSize / itemConsumptions[i]);
                if (currentParallel <= 0) return 0;
            }
            return currentParallel;
        }

        /**
         * @param inputHatches      Input hatch list to check. Usually the input hatch list of multi.
         * @param fluidConsumptions Fluid inputs of the recipe.
         * @return The number of parallel recipes, or 0 if recipe is not satisfied at all. 0 < number < 1 means that
         *         fluids are found but not enough.
         */
        public static double maxParallelCalculatedByInputFluids(ArrayList<GT_MetaTileEntity_Hatch_Input> inputHatches,
            int maxParallel, FluidStack[] fluidConsumptions, Map<Fluid, FluidStack> fluidsFromME) {
            Map<Fluid, Long> fluidConsumptionsFromME = new Reference2LongOpenHashMap<>();
            double currentParallel = maxParallel;

            // Calculate the amount of each fluid to consume from ME
            for (int i = 0; i < fluidConsumptions.length; i++) {
                GT_MetaTileEntity_Hatch_Input inputHatch = inputHatches.get(i);
                if (!inputHatch.isValid()) return 0;
                if (inputHatch instanceof GT_MetaTileEntity_Hatch_Input_ME meHatch) {
                    FluidStack fluid = meHatch.getShadowFluidStack(0);
                    if (fluid == null) return 0;
                    if (!GT_Utility.areFluidsEqual(fluid, fluidConsumptions[i])) return 0;
                    fluidConsumptionsFromME.merge(fluid.getFluid(), (long) fluidConsumptions[i].amount, Long::sum);
                }
            }
            // Calculate parallel from ME input hatches
            for (Entry<Fluid, Long> entry : fluidConsumptionsFromME.entrySet()) {
                Fluid fluid = entry.getKey();
                if (!fluidsFromME.containsKey(fluid)) return 0;
                long consume = entry.getValue();
                currentParallel = Math.min(currentParallel, (double) fluidsFromME.get(fluid).amount / consume);
                if (currentParallel <= 0) return 0;
            }

            // Calculate parallel from regular input hatches
            for (int i = 0; i < fluidConsumptions.length; i++) {
                GT_MetaTileEntity_Hatch_Input inputHatch = inputHatches.get(i);
                if (!inputHatch.isValid()) return 0;
                if (inputHatch instanceof GT_MetaTileEntity_Hatch_Input_ME) continue;

                FluidStack fluid;
                if (inputHatch instanceof GT_MetaTileEntity_Hatch_MultiInput multiInput) {
                    fluid = multiInput.getFluid(0);
                } else {
                    fluid = inputHatch.getFillableStack();
                }
                if (fluid == null) return 0;
                if (!GT_Utility.areFluidsEqual(fluid, fluidConsumptions[i])) return 0;
                currentParallel = Math.min(currentParallel, (double) fluid.amount / fluidConsumptions[i].amount);
                if (currentParallel <= 0) return 0;
            }
            return currentParallel;
        }

        /**
         * WARNING: Ensure that item inputs are enough to be consumed with
         * {@link GT_Recipe_AssemblyLine#maxParallelCalculatedByInputItems} before calling this method!
         *
         * @param inputBusses      Input bus list to check. Usually the input bus list of multi.
         * @param itemConsumptions Should be generated by {@link GT_Recipe_AssemblyLine#getItemConsumptionAmountArray}.
         */
        public static void consumeInputItems(ArrayList<GT_MetaTileEntity_Hatch_InputBus> inputBusses,
            int amountMultiplier, int[] itemConsumptions, Map<GT_Utility.ItemId, ItemStack> inputsFromME) {
            for (int i = 0; i < itemConsumptions.length; i++) {
                GT_MetaTileEntity_Hatch_InputBus inputBus = inputBusses.get(i);
                if (!inputBus.isValid()) continue;
                ItemStack item;
                if (inputBus instanceof GT_MetaTileEntity_Hatch_InputBus_ME meBus) {
                    item = inputsFromME.get(GT_Utility.ItemId.createNoCopy(meBus.getShadowItemStack(0)));
                } else {
                    item = inputBus.getStackInSlot(0);
                }
                item.stackSize -= itemConsumptions[i] * amountMultiplier;
            }
        }

        /**
         * WARNING: Ensure that fluid inputs are enough to be consumed with
         * {@link GT_Recipe_AssemblyLine#maxParallelCalculatedByInputFluids} before calling this method!
         *
         * @param inputHatches      Input hatch list to check. Usually the input hatch list of multi.
         * @param fluidConsumptions Fluid inputs of the recipe.
         */
        public static void consumeInputFluids(ArrayList<GT_MetaTileEntity_Hatch_Input> inputHatches,
            int amountMultiplier, FluidStack[] fluidConsumptions, Map<Fluid, FluidStack> fluidsFromME) {
            for (int i = 0; i < fluidConsumptions.length; i++) {
                GT_MetaTileEntity_Hatch_Input inputHatch = inputHatches.get(i);
                if (!inputHatch.isValid()) continue;
                FluidStack fluid;
                if (inputHatch instanceof GT_MetaTileEntity_Hatch_Input_ME meHatch) {
                    fluid = fluidsFromME.get(
                        meHatch.getShadowFluidStack(0)
                            .getFluid());
                } else if (inputHatch instanceof GT_MetaTileEntity_Hatch_MultiInput multiInput) {
                    fluid = multiInput.getFluid(0);
                } else {
                    fluid = inputHatch.getFillableStack();
                }
                fluid.amount -= fluidConsumptions[i].amount * amountMultiplier;
            }
        }
    }

    public static class GT_Recipe_WithAlt extends GT_Recipe {

        public ItemStack[][] mOreDictAlt;

        /**
         * Only for {@link GT_RecipeBuilder}.
         */
        GT_Recipe_WithAlt(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs,
            FluidStack[] mFluidOutputs, int[] mChances, Object mSpecialItems, int mDuration, int mEUt,
            int mSpecialValue, boolean mEnabled, boolean mHidden, boolean mFakeRecipe, boolean mCanBeBuffered,
            boolean mNeedsEmptyOutput, boolean nbtSensitive, String[] neiDesc,
            @Nullable IRecipeMetadataStorage metadataStorage, RecipeCategory recipeCategory,
            ItemStack[][] mOreDictAlt) {
            super(
                mInputs,
                mOutputs,
                mFluidInputs,
                mFluidOutputs,
                mChances,
                mSpecialItems,
                mDuration,
                mEUt,
                mSpecialValue,
                mEnabled,
                mHidden,
                mFakeRecipe,
                mCanBeBuffered,
                mNeedsEmptyOutput,
                nbtSensitive,
                neiDesc,
                metadataStorage,
                recipeCategory);
            this.mOreDictAlt = mOreDictAlt;
        }

        public GT_Recipe_WithAlt(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems,
            int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
            int aSpecialValue, ItemStack[][] aAlt) {
            super(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecialItems,
                aChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue);
            mOreDictAlt = aAlt;
        }

        public Object getAltRepresentativeInput(int aIndex) {
            if (aIndex < 0) return null;
            if (aIndex < mOreDictAlt.length) {
                if (mOreDictAlt[aIndex] != null && mOreDictAlt[aIndex].length > 0) {
                    ItemStack[] rStacks = new ItemStack[mOreDictAlt[aIndex].length];
                    for (int i = 0; i < mOreDictAlt[aIndex].length; i++) {
                        rStacks[i] = GT_Utility.copyOrNull(mOreDictAlt[aIndex][i]);
                    }
                    return rStacks;
                }
            }
            if (aIndex >= mInputs.length) return null;
            return GT_Utility.copyOrNull(mInputs[aIndex]);
        }
    }
}
