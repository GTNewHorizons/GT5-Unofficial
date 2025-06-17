package gregtech.api.util;

import static gregtech.api.enums.GTValues.D2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.EmptyRecipeMetadataStorage;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import it.unimi.dsi.fastutil.ints.Int2LongArrayMap;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongArrayMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.Reference2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;

public class GTRecipe implements Comparable<GTRecipe> {

    private static ItemStack dataStick;
    private static ItemStack dataOrb;
    private static ItemStack ic2FluidCell;

    public static void setItemStacks() {
        ic2FluidCell = ItemList.Cell_Universal_Fluid.get(1);
        dataStick = ItemList.Tool_DataStick.get(1L);
        dataOrb = ItemList.Tool_DataOrb.get(1L);
    }

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

    /** Used for simple cache validation */
    private ItemStack[] inputsAtCacheTime = null;
    /** Unified and type-merged stacks of mInputs, each item is guaranteed to be unique */
    private RecipeItemInput[] mergedInputCache = null;
    private static final RecipeItemInput[] EMPTY_INPUT_CACHE = new RecipeItemInput[0];


    private GTRecipe(GTRecipe aRecipe, boolean shallow) {
        mInputs = shallow ? aRecipe.mInputs : GTUtility.copyItemArray(aRecipe.mInputs);
        mOutputs = shallow ? aRecipe.mOutputs : GTUtility.copyItemArray(aRecipe.mOutputs);
        mSpecialItems = aRecipe.mSpecialItems;
        mChances = aRecipe.mChances;
        mFluidInputs = shallow ? aRecipe.mFluidInputs : GTUtility.copyFluidArray(aRecipe.mFluidInputs);
        mFluidOutputs = shallow ? aRecipe.mFluidOutputs : GTUtility.copyFluidArray(aRecipe.mFluidOutputs);
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
     * Only for {@link GTRecipeBuilder}.
     */
    GTRecipe(ItemStack[] mInputs, ItemStack[] mOutputs, FluidStack[] mFluidInputs, FluidStack[] mFluidOutputs,
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

    public GTRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
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

        GTOreDictUnificator.setStackArray(true, true, aInputs);
        GTOreDictUnificator.setStackArray(true, true, aOutputs);

        for (ItemStack tStack : aOutputs) GTUtility.updateItemStack(tStack);

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
    public GTRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4,
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

    // Dummy GTRecipe maker...
    public GTRecipe(ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
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
        GTLog.out.println("GTMod: Re-Unificating Recipes.");
        for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            map.getBackend()
                .reInit();
        }
        RecipeAssemblyLine.reInit();
    }

    public ItemStack getRepresentativeInput(int aIndex) {
        if (aIndex < 0 || aIndex >= mInputs.length) return null;
        return GTUtility.copyOrNull(mInputs[aIndex]);
    }

    public ItemStack getOutput(int aIndex) {
        if (aIndex < 0 || aIndex >= mOutputs.length) return null;
        return GTUtility.copyOrNull(mOutputs[aIndex]);
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

        int tInputAmount = GTModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mInputs);
        int tOutputAmount = GTModHandler.getCapsuleCellContainerCountMultipliedWithStackSize(mOutputs);

        if (tInputAmount < tOutputAmount) {
            if (!Materials.Tin.contains(mInputs)) {
                GTLog.err.println("You get more Cells, than you put in? There must be something wrong.");
                new Exception().printStackTrace(GTLog.err);
            }
        } else if (tInputAmount > tOutputAmount) {
            if (!Materials.Tin.contains(mOutputs)) {
                GTLog.err.println("You get less Cells, than you put in? GT Machines usually don't destroy Cells.");
                new Exception().printStackTrace(GTLog.err);
            }
        }
    }

    public GTRecipe copy() {
        return new GTRecipe(this, false);
    }

    public GTRecipe copyShallow() {
        return new GTRecipe(this, true);
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

    public boolean couldRunOnce(ItemStack[] items, FluidStack[] fluids, boolean dontCheckStackSizes) {
        if (items.length > 64 || fluids.length > 64 || mInputs.length > 64 || mFluidInputs.length > 64) {
            return isRecipeInputEqual(false, false, fluids, items);
        }

        // Check if the given inputs could theoretically run this recipe.
        // Fast fail path because most of the time this will be false.
        if (!areAllInputsPresent(items, fluids)) return false;

        if (isNBTSensitive) {
            return isRecipeInputEqual(false, false, fluids, items);
        }

        if (dontCheckStackSizes) return true;

        if (mInputs.length > 0) {
            var requiredItems = getItemHistogram(mInputs);
            var presentItems = getItemHistogram(items);

            for (var requiredItem : requiredItems.long2LongEntrySet()) {
                if (presentItems.get(requiredItem.getLongKey()) < requiredItem.getLongValue()) return false;
            }
        }

        if (mFluidInputs.length > 0) {
            var requiredFluids = getFluidHistogram(mFluidInputs);
            var presentFluids = getFluidHistogram(fluids);

            for (var requiredFluid : requiredFluids.int2LongEntrySet()) {
                if (presentFluids.get(requiredFluid.getIntKey()) < requiredFluid.getLongValue()) return false;
            }
        }

        return true;
    }

    private boolean areAllInputsPresent(ItemStack[] items, FluidStack[] fluids) {
        long matchedItems = 0;

        for (int i = 0; i < mInputs.length; i++) {
            ItemStack inRecipe = mInputs[i];

            if (inRecipe == null) continue;

            boolean foundMatch = false;

            for (int j = 0; j < items.length; j++) {
                if ((matchedItems & (1L << j)) != 0) continue;

                ItemStack inMachine = items[j];

                if (inMachine == null) continue;

                if (GTUtility.areStacksEqual(inRecipe, inMachine, !isNBTSensitive)) {
                    matchedItems |= 1L << j;
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) return false;
        }

        long matchedFluids = 0;

        for (int i = 0; i < mFluidInputs.length; i++) {
            FluidStack inRecipe = mFluidInputs[i];

            if (inRecipe == null) continue;

            boolean foundMatch = false;

            for (int j = 0; j < fluids.length; j++) {
                if ((matchedFluids & (1L << j)) != 0) continue;

                FluidStack inMachine = fluids[j];

                if (inMachine == null) continue;

                if (GTUtility.areFluidsEqual(inRecipe, inMachine, !isNBTSensitive)) {
                    matchedFluids |= 1L << j;
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) return false;
        }

        return true;
    }

    private Long2LongMap getItemHistogram(ItemStack[] items) {
        Long2LongMap map = items.length < 16 ? new Long2LongArrayMap(items.length)
            : new Long2LongOpenHashMap(items.length);

        for (int i = 0; i < items.length; i++) {
            ItemStack stack = items[i];

            if (stack == null || stack.getItem() == null) continue;

            // Use the object identity hashcode here because Item references cannot change.
            // An id lookup would be too slow, even if it makes more sense. We just care about a unique int value.
            long key = ((long) Objects.hashCode(stack.getItem())) << 32 | (long) stack.itemDamage;

            map.put(key, map.get(key) + stack.stackSize);
        }

        return map;
    }

    private Int2LongMap getFluidHistogram(FluidStack[] fluids) {
        Int2LongMap map = fluids.length < 16 ? new Int2LongArrayMap(fluids.length)
            : new Int2LongOpenHashMap(fluids.length);

        for (int i = 0; i < fluids.length; i++) {
            FluidStack stack = fluids[i];

            if (stack == null || stack.getFluid() == null) continue;

            // Same as above, the fluid reference will never change and we just want a unique key
            int key = Objects.hashCode(stack.getFluid());

            map.put(key, map.get(key) + stack.amount);
        }

        return map;
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
     * @return Computes a (cached) array of all input items, combined by type into stacks. Do not mutate.
     */
    private @NotNull RecipeItemInput @NotNull [] getCachedCombinedItemInputs() {
        if (mergedInputCache != null) {
            if (mInputs != inputsAtCacheTime) {
                throw new IllegalStateException(
                    "Inputs to this recipe have been modified since first recipe match: " + this);
            }
            return mergedInputCache;
        }

        synchronized (this) {
            // In case another thread initialized it while this synchronized block was locked:
            if (mergedInputCache != null) {
                if (mInputs != inputsAtCacheTime) {
                    throw new IllegalStateException(
                        "Inputs to this recipe have been modified since first recipe match: " + this);
                }
                return mergedInputCache;
            }

            final ItemStack[] inputs = mInputs;
            inputsAtCacheTime = inputs;
            if (inputs == null || inputs.length == 0) {
                mergedInputCache = EMPTY_INPUT_CACHE;
                return mergedInputCache;
            }
            final ObjectArrayList<@NotNull RecipeItemInput> newCache = ObjectArrayList
                .wrap(new RecipeItemInput[inputs.length], 0);
            for (final ItemStack itemStack : inputs) {
                if (itemStack == null) continue;
                final RecipeItemInput existingInput = newCache.stream()
                    .filter(existing -> existing.matchesType(itemStack))
                    .findAny()
                    .orElse(null);
                if (existingInput == null) {
                    newCache.add(new RecipeItemInput(itemStack, isNBTSensitive));
                } else {
                    existingInput.inputAmount = Math.addExact(existingInput.inputAmount, itemStack.stackSize);
                }
            }
            final RecipeItemInput[] frozenCache = newCache.toArray(new RecipeItemInput[0]);
            if (GregTechAPI.sFullLoadFinished) {
                mergedInputCache = frozenCache;
            }
            return frozenCache;
        }
    }

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

        if (aFluidInputs != null) {
            for (FluidStack recipeFluidCost : mFluidInputs) {
                if (recipeFluidCost != null) {
                    long remainingCost = (long) recipeFluidCost.amount * amountMultiplier;

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

        if (aInputs == null || aInputs.length == 0) {
            return;
        }

        final ItemData[] unifiedProvidedInputs = new ItemData[aInputs.length];
        for (int i = 0; i < aInputs.length; i++) {
            unifiedProvidedInputs[i] = GTOreDictUnificator.getAssociation(aInputs[i]);
        }
        final @NotNull RecipeItemInput @NotNull [] combinedInputs = getCachedCombinedItemInputs();

        for (final RecipeItemInput recipeItemCost : combinedInputs) {
            long remainingCost = recipeItemCost.inputAmount * amountMultiplier;

            for (int iProvided = 0; iProvided < aInputs.length && remainingCost > 0; iProvided++) {
                final ItemStack providedItem = aInputs[iProvided];
                if (providedItem == null || providedItem.stackSize == 0) {
                    continue;
                }

                final ItemData providedUnifiedItem = unifiedProvidedInputs[iProvided];
                if (!recipeItemCost.matchesRecipe(providedUnifiedItem, providedItem)) {
                    continue;
                }

                if (providedItem.stackSize >= remainingCost) {
                    providedItem.stackSize -= (int) remainingCost;
                    break;
                } else {
                    remainingCost -= providedItem.stackSize;
                    providedItem.stackSize = 0;
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
            final @NotNull RecipeItemInput @NotNull [] combinedInputs = getCachedCombinedItemInputs();

            if (aInputs.length < combinedInputs.length) {
                // Fewer item types provided than required by the recipe, making it impossible to satisfy.
                return 0;
            }
            final ItemData[] unifiedProvidedInputs = new ItemData[aInputs.length];
            for (int i = 0; i < aInputs.length; i++) {
                unifiedProvidedInputs[i] = GTOreDictUnificator.getAssociation(aInputs[i]);
            }

            recipeItemLoop: for (final RecipeItemInput combinedInput : combinedInputs) {
                double remainingCost = combinedInput.inputAmount * currentParallel;
                long providedAmount = 0;

                for (int i = 0; i < unifiedProvidedInputs.length; i++) {
                    final ItemData providedUnifiedItem = unifiedProvidedInputs[i];
                    final ItemStack providedItem = aInputs[i];
                    if (!combinedInput.matchesRecipe(providedUnifiedItem, providedItem)) {
                        continue;
                    }

                    providedAmount += providedItem.stackSize;

                    if (providedAmount >= remainingCost) {
                        continue recipeItemLoop;
                    }
                }
                if (providedAmount == 0) {
                    return 0;
                }
                currentParallel = Math.min(currentParallel, (double) providedAmount / combinedInput.inputAmount);
            }
        }
        return currentParallel;
    }

    /**
     * Please see JavaDoc on {@link #GTppRecipeHelper} for why this is here.
     */
    private static boolean shouldCheckNBT(ItemStack item) {
        if (GTppRecipeHelper) {
            return GTUtility.areStacksEqual(item, ic2FluidCell, true) || GTUtility.areStacksEqual(item, dataStick, true)
                || GTUtility.areStacksEqual(item, dataOrb, true);
        }
        return false;
    }

    @Override
    public int compareTo(GTRecipe recipe) {
        // first lowest tier recipes
        // then fastest
        // then with lowest special value
        // then dry recipes
        // then with fewer inputs
        if (this.mEUt != recipe.mEUt) {
            return Integer.compare(this.mEUt, recipe.mEUt);
        } else if (this.mDuration != recipe.mDuration) {
            return Integer.compare(this.mDuration, recipe.mDuration);
        } else if (this.mSpecialValue != recipe.mSpecialValue) {
            return Integer.compare(this.mSpecialValue, recipe.mSpecialValue);
        } else if (this.mFluidInputs.length != recipe.mFluidInputs.length) {
            return Integer.compare(this.mFluidInputs.length, recipe.mFluidInputs.length);
        } else if (this.mInputs.length != recipe.mInputs.length) {
            return Integer.compare(this.mInputs.length, recipe.mInputs.length);
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
     * Exists only for recipe copying from external. For ordinal use case, use {@link GTRecipeBuilder#recipeCategory}.
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
        "gregtech.api.util.GTRecipe",
        "gregtech.api.util.GTRecipeBuilder",
        "gregtech.api.util.GTRecipeConstants",
        "gregtech.api.util.GTRecipeMapUtil",
        "gregtech.common.GTRecipeAdder");

    public void reloadOwner() {
        setOwner(
            Loader.instance()
                .activeModContainer());

        if (GTMod.gregtechproxy.mNEIRecipeOwnerStackTrace) {
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

    public GTRecipe setInputs(ItemStack... aInputs) {
        // TODO determine if we need this without trailing nulls call
        this.mInputs = ArrayExt.withoutTrailingNulls(aInputs, ItemStack[]::new);
        return this;
    }

    public GTRecipe setOutputs(ItemStack... aOutputs) {
        this.mOutputs = ArrayExt.withoutTrailingNulls(aOutputs, ItemStack[]::new);
        return this;
    }

    public GTRecipe setFluidInputs(FluidStack... aInputs) {
        this.mFluidInputs = ArrayExt.withoutTrailingNulls(aInputs, FluidStack[]::new);
        return this;
    }

    public GTRecipe setFluidOutputs(FluidStack... aOutputs) {
        this.mFluidOutputs = ArrayExt.withoutTrailingNulls(aOutputs, FluidStack[]::new);
        return this;
    }

    public GTRecipe setChances(int... aChances) {
        this.mChances = aChances;
        return this;
    }

    public GTRecipe setDuration(int aDuration) {
        this.mDuration = aDuration;
        return this;
    }

    public GTRecipe setEUt(int aEUt) {
        this.mEUt = aEUt;
        return this;
    }

}
