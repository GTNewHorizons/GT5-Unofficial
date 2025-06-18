package gregtech.api.util;

import static gregtech.api.enums.GTValues.D2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.Contract;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.EmptyRecipeMetadataStorage;
import gregtech.api.recipe.metadata.IRecipeMetadataStorage;
import gregtech.api.util.extensions.ArrayExt;
import it.unimi.dsi.fastutil.ints.Int2LongArrayMap;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongMaps;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongArrayMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongMaps;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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

    /**
     * Caches any input information that can be pre-calculated.
     * This is volatile to keep it threadsafe-ish. For some reason, the prior systems have been threadsafe, so this is
     * too.
     */
    private volatile CachedInputState cachedInputState = null;

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

    /**
     * Checks if this recipe could run a single time or more with the given inputs. In most cases, this is much faster
     * than {@link #maxParallelCalculatedByInputs}.
     * @param items The items in the machine's inputs.
     * @param fluids The fluids in the machine's inputs.
     * @param ignoreStackSizes When true, stack sizes will be ignored and only item/fluid presence will be checked.
     */
    public boolean couldRunOnce(ItemStack[] items, FluidStack[] fluids, boolean ignoreStackSizes) {
        if (items.length <= 64 && fluids.length <= 64 && mInputs.length <= 64 && mFluidInputs.length <= 64) {
            // Check if the available items and fluids could theoretically run this recipe.
            // Fast fail path because most of the time this will be false.
            if (!areAllInputsPresent(items, fluids)) return false;
        }

        CachedInputState cachedInputState = getCachedInputs();

        if (!cachedInputState.useFastItemCheck) {
            return maxParallelCalculatedByInputs(items, fluids, 1) > 0;
        }

        if (ignoreStackSizes) return true;

        if (mInputs.length > 0) {
            Long2LongMap presentItems = getItemHistogram(items);

            for (Long2LongMap.Entry requiredItem : Long2LongMaps.fastIterable(cachedInputState.fastItemInputMap)) {
                if (presentItems.get(requiredItem.getLongKey()) < requiredItem.getLongValue()) return false;
            }
        }

        if (mFluidInputs.length > 0) {
            Int2LongMap presentFluids = getFluidHistogram(fluids);

            for (Int2LongMap.Entry requiredFluid : Int2LongMaps.fastIterable(cachedInputState.fastFluidInputMap)) {
                if (presentFluids.get(requiredFluid.getIntKey()) < requiredFluid.getLongValue()) return false;
            }
        }

        return true;
    }

    /**
     * A fast, fuzzy comparison to make sure all required items are present.
     * If this returns true, this recipe <i>may</i> be able to run.
     * If this returns false, this recipe <i>cannot</i> run.
     * Does not work with more than 64 items or fluids in the params or mInputs/mFluidInputs due to the longs being used
     * as bitmasks.
     */
    private boolean areAllInputsPresent(ItemStack[] items, FluidStack[] fluids) {
        // Bitmask for which input items were already checked.
        // Indices will be skipped if their bit is set.
        long matchedItems = 0;

        for (int i = 0; i < mInputs.length; i++) {
            ItemStack inRecipe = mInputs[i];

            if (inRecipe == null) continue;

            boolean foundMatch = false;

            for (int j = 0; j < items.length; j++) {
                if ((matchedItems & (1L << j)) != 0) continue;

                ItemStack inMachine = items[j];

                if (inMachine == null) {
                    matchedItems |= 1L << j;
                    continue;
                }

                if (GTUtility.areStacksEqual(inRecipe, inMachine, true)) {
                    matchedItems |= 1L << j;
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) return false;
        }

        // Same as matchedItems
        long matchedFluids = 0;

        for (int i = 0; i < mFluidInputs.length; i++) {
            FluidStack inRecipe = mFluidInputs[i];

            if (inRecipe == null) continue;

            boolean foundMatch = false;

            for (int j = 0; j < fluids.length; j++) {
                if ((matchedFluids & (1L << j)) != 0) continue;

                FluidStack inMachine = fluids[j];

                if (inMachine == null) {
                    matchedFluids |= 1L << j;
                    continue;
                }

                if (GTUtility.areFluidsEqual(inRecipe, inMachine, true)) {
                    matchedFluids |= 1L << j;
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) return false;
        }

        return true;
    }

    private static Long2LongMap getItemHistogram(ItemStack[] items) {
        Long2LongMap map = items.length < 16 ? new Long2LongArrayMap(items.length)
            : new Long2LongOpenHashMap(items.length);

        for (int i = 0; i < items.length; i++) {
            ItemStack stack = items[i];

            if (stack == null || stack.getItem() == null) continue;

            long key = GTOreDictUnificator.stackHash(stack);

            map.put(key, map.get(key) + stack.stackSize);
        }

        return map;
    }

    private static Int2LongMap getFluidHistogram(FluidStack[] fluids) {
        Int2LongMap map = fluids.length < 16 ? new Int2LongArrayMap(fluids.length)
            : new Int2LongOpenHashMap(fluids.length);

        for (int i = 0; i < fluids.length; i++) {
            FluidStack stack = fluids[i];

            if (stack == null || stack.getFluid() == null) continue;

            // Same as above, the fluid reference will never change, and we just want a unique key
            int key = GTOreDictUnificator.stackHash(stack);

            map.put(key, map.get(key) + stack.amount);
        }

        return map;
    }

    private CachedInputState getCachedInputs() {
        if (!GregTechAPI.sFullLoadFinished) throw new IllegalStateException("cannot cache recipe inputs before everything has loaded");

        if (cachedInputState == null) {
            cachedInputState = new CachedInputState();
        }

        return cachedInputState;
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


    public boolean isRecipeInputEqual(ItemStack[] items, FluidStack[] fluids, boolean consumeInputs) {
        return isRecipeInputEqual(items, fluids, 1, consumeInputs, false);
    }

    public boolean isRecipeInputEqual(ItemStack[] items, FluidStack[] fluids, boolean consumeInputs, boolean ignoreStackSizes) {
        return isRecipeInputEqual(items, fluids, 1, consumeInputs, ignoreStackSizes);
    }

    /**
     * Checks if this recipe could be ran a specific number of times.
     * @param items The input items present in the machine
     * @param fluids The input fluids present in the machine
     * @param amountMultiplier The multiplier for this recipe. A value of 1 means this recipe will be 'ran' once.
     * @param consumeInputs When true and the recipe matches, the input stacks will be decremented via {@link #consumeInput}
     * @param ignoreStackSizes When true, stack sizes will be ignored when checking this recipe.
     */
    public boolean isRecipeInputEqual(ItemStack[] items, FluidStack[] fluids, long amountMultiplier, boolean consumeInputs, boolean ignoreStackSizes) {
        if (consumeInputs && ignoreStackSizes) throw new IllegalArgumentException("Cannot set consumeInputs and ignoreStackSizes at the same time");

        long maxParallel = maxParallelCalculatedByInputs(items, fluids, amountMultiplier);

        if (ignoreStackSizes) {
            return maxParallel > 0;
        }

        if (maxParallel >= amountMultiplier) {
            if (consumeInputs) {
                consumeInput(amountMultiplier, fluids, items);
            }
            return true;
        }

        return false;
    }

    /**
     * Returns the number of parallel recipes, or 0 if recipe is not satisfied at all.
     */
    public long maxParallelCalculatedByInputs(ItemStack[] items, FluidStack[] fluids, long maxParallel) {
        if (mInputs.length > 0 && items == null) return 0;
        if (mFluidInputs.length > 0 && fluids == null) return 0;
        if (fluids.length < mFluidInputs.length) return 0;
        if (items.length < mInputs.length) return 0;

        CachedInputState cachedInputState = getCachedInputs();

        if (mFluidInputs.length > 0) {
            Int2LongMap presentFluids = getFluidHistogram(fluids);

            for (Int2LongMap.Entry requiredFluid : Int2LongMaps.fastIterable(cachedInputState.fastFluidInputMap)) {
                if (requiredFluid.getLongValue() == 0) continue;

                long possibleParallels = presentFluids.get(requiredFluid.getIntKey()) / requiredFluid.getLongValue();

                if (possibleParallels <= 0) return 0;

                maxParallel = Math.min(maxParallel, possibleParallels);
            }
        }

        if (mInputs.length > 0) {
            if (!cachedInputState.useFastItemCheck) {
                maxParallel = getMaxParallelsItemNBTSensitive(cachedInputState, items, maxParallel);
            } else {
                Long2LongMap presentItems = getItemHistogram(items);

                for (Long2LongMap.Entry requiredItem : Long2LongMaps.fastIterable(cachedInputState.fastItemInputMap)) {
                    long required = requiredItem.getLongValue();

                    if (required == 0) continue;

                    long present = presentItems.get(requiredItem.getLongKey());

                    long possibleParallels = present / required;

                    if (possibleParallels <= 0) return 0;

                    maxParallel = Math.min(maxParallel, possibleParallels);
                }
            }
        }

        return maxParallel;
    }

    private long getMaxParallelsItemNBTSensitive(CachedInputState cachedInputState, ItemStack[] items, long maxParallel) {
        outer: for (RecipeItemInput combinedInput : cachedInputState.mergedInputs) {
            long present = 0;

            // Calculate how many of this input we would need to de able to run {@code maxParallel} parallels.
            long requiredAmount = combinedInput.inputAmount * maxParallel;

            for (ItemStack stack : items) {
                if (combinedInput.matchesType(stack)) {
                    present += stack.stackSize;

                    if (present > requiredAmount) continue outer;
                }
            }

            long possibleParallels = present / combinedInput.inputAmount;

            if (possibleParallels <= 0) return 0;

            maxParallel = Math.min(maxParallel, possibleParallels);
        }

        return maxParallel;
    }

    /**
     * WARNING: Ensure that item inputs and fluid inputs are enough to be consumed with
     * {@link #maxParallelCalculatedByInputs} before calling this method!
     */
    public void consumeInput(long amountMultiplier, FluidStack[] fluids, ItemStack[] items) {
        if (amountMultiplier <= 0) return;

        if (fluids != null) {
            for (FluidStack recipeFluidCost : mFluidInputs) {
                if (recipeFluidCost == null) continue;

                long remainingCost = recipeFluidCost.amount * amountMultiplier;

                for (FluidStack providedFluid : fluids) {
                    if (providedFluid == null) continue;
                    if (!providedFluid.isFluidEqual(recipeFluidCost)) continue;

                    if (providedFluid.amount >= remainingCost) {
                        providedFluid.amount -= (int) remainingCost;
                        remainingCost = 0;
                        break;
                    } else {
                        remainingCost -= providedFluid.amount;
                        providedFluid.amount = 0;
                    }
                }

                if (remainingCost > 0) {
                    GTMod.GT_FML_LOGGER.error("Did not consume enough fluids! Recipe={}", this);
                    GTMod.GT_FML_LOGGER.error("", new Exception());
                }
            }
        }

        if (items == null || items.length == 0) {
            return;
        }

        ItemStack[] unificatedInputs = GTOreDictUnificator.unificate(items, false);

        CachedInputState cachedInputState = getCachedInputs();

        for (final RecipeItemInput recipeItemCost : cachedInputState.mergedInputs) {
            long remainingCost = recipeItemCost.inputAmount * amountMultiplier;

            for (int i = 0; i < items.length && remainingCost > 0; i++) {
                final ItemStack providedItem = items[i];
                if (providedItem == null) continue;
                if (providedItem.stackSize == 0) continue;

                // Compare against the unificated stack, but consume the original stack
                // We can do this since the unificated stacks' indices are identical to the original stacks'
                if (!recipeItemCost.matchesType(unificatedInputs[i])) continue;

                if (providedItem.stackSize >= remainingCost) {
                    providedItem.stackSize -= (int) remainingCost;
                    break;
                } else {
                    remainingCost -= providedItem.stackSize;
                    providedItem.stackSize = 0;
                }
            }

            if (remainingCost > 0) {
                GTMod.GT_FML_LOGGER.error("Did not consume enough items! Recipe={}", this);
                GTMod.GT_FML_LOGGER.error("", new Exception());
            }
        }
    }

    /**
     * Please see JavaDoc on {@link #GTppRecipeHelper} for why this is here.
     */
    public static boolean shouldCheckNBT(ItemStack item) {
        if (GTppRecipeHelper) {
            if (GTUtility.areStacksEqual(item, ic2FluidCell, true)) return true;
            if (GTUtility.areStacksEqual(item, dataStick, true)) return true;
            if (GTUtility.areStacksEqual(item, dataOrb, true)) return true;
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

    private class CachedInputState {
        public final ItemStack[] cachedInputs, unificatedInputs;
        public final FluidStack[] cachedFluidInputs;

        public final Long2LongMap fastItemInputMap;
        public final Int2LongMap fastFluidInputMap;
        public final RecipeItemInput[] mergedInputs;
        /** When true, this recipe doesn't care about NBT or oredict and we can use the sketchy stack hashes for comparing items. */
        public final boolean useFastItemCheck;

        public CachedInputState() {
            cachedInputs = mInputs;
            cachedFluidInputs = mFluidInputs;
            unificatedInputs = GTOreDictUnificator.unificate(mInputs, false);

            fastItemInputMap = getItemHistogram(unificatedInputs);
            fastFluidInputMap = getFluidHistogram(mFluidInputs);

            // If any inputs are NBT sensitive, we can't use the fast item input check
            boolean hasSpecialInputLogic = isNBTSensitive;
            hasSpecialInputLogic |= Stream.of(mInputs)
                .filter(Objects::nonNull)
                .anyMatch(GTRecipe::shouldCheckNBT);

            // If any inputs have wildcard meta, we can't use the fast item input check
            hasSpecialInputLogic |= Stream.of(mInputs)
                .filter(Objects::nonNull)
                .anyMatch(stack -> stack.itemDamage == OreDictionary.WILDCARD_VALUE);

            useFastItemCheck = !hasSpecialInputLogic;

            if (unificatedInputs.length == 0) {
                mergedInputs = EMPTY_INPUT_CACHE;
            } else {
                ObjectArrayList<RecipeItemInput> newCache = new ObjectArrayList<>(unificatedInputs.length);

                for (ItemStack itemStack : unificatedInputs) {
                    if (itemStack == null) continue;

                    RecipeItemInput existingInput = newCache.stream()
                        .filter(existing -> existing.matchesType(itemStack))
                        .findAny()
                        .orElse(null);

                    if (existingInput == null) {
                        newCache.add(new RecipeItemInput(itemStack, isNBTSensitive));
                    } else {
                        existingInput.inputAmount = Math.addExact(existingInput.inputAmount, itemStack.stackSize);
                    }
                }

                mergedInputs = newCache.toArray(new RecipeItemInput[0]);
            }
        }
    }
}
