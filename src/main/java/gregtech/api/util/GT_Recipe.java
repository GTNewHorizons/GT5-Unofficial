package gregtech.api.util;

import static gregtech.api.enums.GT_Values.D2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.RecipeMetadataStorage;
import gregtech.api.util.extensions.ArrayExt;
import ic2.core.Ic2Items;

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
    private RecipeMetadataStorage metadataStorage = new RecipeMetadataStorage();
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
        owners = new ArrayList<>(aRecipe.owners);
        reloadOwner();
    }

    /**
     * Only for {@link GT_RecipeBuilder}.
     */
    GT_Recipe(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs, FluidStack[] mFluidOutputs,
        int[] mChances, Object mSpecialItems, int mDuration, int mEUt, int mSpecialValue, boolean mEnabled,
        boolean mHidden, boolean mFakeRecipe, boolean mCanBeBuffered, boolean mNeedsEmptyOutput, boolean nbtSensitive,
        String[] neiDesc, RecipeMetadataStorage metadataStorage, RecipeCategory recipeCategory) {
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
        this.metadataStorage = metadataStorage;
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
     * Creates empty recipe where everything is set to default.
     */
    public static GT_Recipe empty() {
        return new GT_Recipe(false, null, null, null, null, null, null, 0, 0, 0);
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

        int remainingCost;

        if (aFluidInputs != null) {
            for (FluidStack recipeFluidCost : mFluidInputs) {
                if (recipeFluidCost != null) {
                    remainingCost = recipeFluidCost.amount * amountMultiplier;

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
                    remainingCost = recipeItemCost.stackSize * amountMultiplier;

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
     * are found but not enough. Refer to SingleRecipeCheck#checkRecipeInputs.
     */
    public double maxParallelCalculatedByInputs(int maxParallel, FluidStack[] aFluidInputs, ItemStack... aInputs) {
        if (mInputs.length > 0 && aInputs == null) return 0;
        if (mFluidInputs.length > 0 && aFluidInputs == null) return 0;

        double currentParallel = maxParallel;

        if (aFluidInputs != null) {
            // Create map for fluid -> stored amount
            Map<Fluid, Integer> fluidMap = new HashMap<>();
            Map<Fluid, Integer> fluidCost = new HashMap<>();
            for (FluidStack fluidStack : aFluidInputs) {
                if (fluidStack == null) continue;
                fluidMap.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
            }
            for (FluidStack fluidStack : mFluidInputs) {
                if (fluidStack == null) continue;
                fluidCost.merge(fluidStack.getFluid(), fluidStack.amount, Integer::sum);
            }

            // Check how many parallels can it perform for each fluid
            for (Map.Entry<Fluid, Integer> costEntry : fluidCost.entrySet()) {
                if (costEntry.getValue() > 0) {
                    currentParallel = Math.min(
                        currentParallel,
                        (double) fluidMap.getOrDefault(costEntry.getKey(), 0) / costEntry.getValue());
                }
                if (currentParallel <= 0) {
                    return 0;
                }
            }
        }

        double remainingCost;
        int providedAmount;
        if (aInputs != null) {
            nextRecipeItemCost: for (ItemStack recipeItemCost : mInputs) {

                ItemStack unifiedItemCost = GT_OreDictUnificator.get_nocopy(true, recipeItemCost);
                if (unifiedItemCost != null) {
                    remainingCost = recipeItemCost.stackSize * currentParallel;
                    providedAmount = 0;

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
                        // for non-consumed input
                        if (recipeItemCost.stackSize == 0) continue nextRecipeItemCost;

                        providedAmount += providedItem.stackSize;

                        if (providedAmount >= remainingCost) continue nextRecipeItemCost;
                    }
                    if (providedAmount == 0) return 0;
                    currentParallel = Math.min(currentParallel, (double) providedAmount / recipeItemCost.stackSize);
                }
            }
        }
        return currentParallel;
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

    public <T> GT_Recipe setMetadata(RecipeMetadataKey<T> key, T value) {
        metadataStorage.store(key, value);
        return this;
    }

    /**
     * Gets metadata associated with this recipe. Can return null. Use {@link #getMetadata(RecipeMetadataKey, Object)}
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
    public <T> T getMetadata(RecipeMetadataKey<T> key, @Nullable T defaultValue) {
        return key.cast(metadataStorage.getMetadata(key, defaultValue));
    }

    public RecipeMetadataStorage getMetadataStorage() {
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
    }

    public static class GT_Recipe_WithAlt extends GT_Recipe {

        ItemStack[][] mOreDictAlt;

        /**
         * Only for {@link GT_RecipeBuilder}.
         */
        GT_Recipe_WithAlt(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs,
            FluidStack[] mFluidOutputs, int[] mChances, Object mSpecialItems, int mDuration, int mEUt,
            int mSpecialValue, boolean mEnabled, boolean mHidden, boolean mFakeRecipe, boolean mCanBeBuffered,
            boolean mNeedsEmptyOutput, boolean nbtSensitive, String[] neiDesc, RecipeMetadataStorage metadataStorage,
            RecipeCategory recipeCategory, ItemStack[][] mOreDictAlt) {
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
