package gregtech.api.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2LongOpenHashMap;

public class RecipeAssemblyLine {

    public static final ArrayList<RecipeAssemblyLine> sAssemblylineRecipes = new ArrayList<>();

    static {
        if (!Boolean.getBoolean("com.gtnh.gt5u.ignore-invalid-assline-recipe"))
            GregTechAPI.sFirstWorldTick.add(RecipeAssemblyLine::checkInvalidRecipes);
        else GTLog.out.println("NOT CHECKING INVALID ASSLINE RECIPE.");
    }

    private static void checkInvalidRecipes() {
        int invalidCount = 0;
        GTLog.out.println("Started assline validation");
        for (RecipeAssemblyLine recipe : sAssemblylineRecipes) {
            if (recipe.getPersistentHash() == 0) {
                invalidCount++;
                GTLog.err.printf("Invalid recipe: %s%n", recipe);
            }
        }
        if (invalidCount > 0) throw new RuntimeException("There are "
            + invalidCount
            + " invalid assembly line recipe(s)! Check GregTech.log for details!");
    }

    public ItemStack mResearchItem;
    public int mResearchTime;
    public int mResearchVoltage;
    public ItemStack[] mInputs;
    public FluidStack[] mFluidInputs;
    public ItemStack mOutput;
    public int mDuration;
    public int mEUt;
    public ItemStack[][] mOreDictAlt;
    private int mPersistentHash;

    private final List<ItemStack> dataSticksForNEI = new ArrayList<>();

    /**
     * THIS CONSTRUCTOR DOES SET THE PERSISTENT HASH.
     * <p>
     * if you set one yourself, it will give you one of the RunetimeExceptions!
     */
    public RecipeAssemblyLine(ItemStack aResearchItem, int aResearchTime, int aResearchVoltage, ItemStack[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt) {
        this(
            aResearchItem,
            aResearchTime,
            aResearchVoltage,
            aInputs,
            aFluidInputs,
            aOutput,
            aDuration,
            aEUt,
            new ItemStack[aInputs.length][]);
        int tPersistentHash = 1;
        for (ItemStack tInput : aInputs)
            tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(tInput, true, false);
        tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(aOutput, true, false);
        for (FluidStack tFluidInput : aFluidInputs)
            tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(tFluidInput, true, false);
        tPersistentHash = tPersistentHash * 31 + aResearchTime;
        tPersistentHash = tPersistentHash * 31 + aResearchVoltage;
        tPersistentHash = tPersistentHash * 31 + aDuration;
        tPersistentHash = tPersistentHash * 31 + aEUt;
        setPersistentHash(tPersistentHash);
    }

    /**
     * THIS CONSTRUCTOR DOES <b>NOT</b> SET THE PERSISTENT HASH.
     * <p>
     * if you don't set one yourself, it will break a lot of stuff!
     */
    public RecipeAssemblyLine(ItemStack aResearchItem, int aResearchTime, int aResearchVoltage, ItemStack[] aInputs,
        FluidStack[] aFluidInputs, ItemStack aOutput, int aDuration, int aEUt, ItemStack[][] aAlt) {
        mResearchItem = aResearchItem;
        mResearchTime = aResearchTime;
        mResearchVoltage = aResearchVoltage;
        mInputs = aInputs;
        mFluidInputs = aFluidInputs;
        mOutput = aOutput;
        mDuration = aDuration;
        mEUt = aEUt;
        mOreDictAlt = aAlt;
    }

    public int getPersistentHash() {
        if (mPersistentHash == 0)
            GTLog.err.println("Assline recipe persistent hash has not been set! Recipe: " + mOutput);
        return mPersistentHash;
    }

    @Override
    public String toString() {
        return "GTRecipe_AssemblyLine{"
            + "mResearchItem="
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
     * @param aPersistentHash the persistent hash. it should reflect the exact input used to generate this recipe If 0
     *                        is passed in, the actual persistent hash will be automatically remapped to 1 instead.
     * @throws IllegalStateException if the persistent hash has been set already
     */
    public void setPersistentHash(int aPersistentHash) {
        if (this.mPersistentHash != 0) throw new IllegalStateException("Cannot set persistent hash twice!");
        if (aPersistentHash == 0) this.mPersistentHash = 1;
        else this.mPersistentHash = aPersistentHash;
    }

    /**
     * WARNING: this class will maintain a strong reference over ALL data sticks created this way. DO NOT call this
     * methods recklessly as it will cause memory leak!
     */
    public ItemStack newDataStickForNEI(String aDisplayName) {
        ItemStack dataStick = ItemList.Tool_DataStick.getWithName(1L, aDisplayName);
        // we don't actually needs to set the recipe data here. no one will read the recipe data before a world load
        // and before a world load id remap will happen and the recipe data will be finally set in the below
        // reInit() method
        // AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(dataStick, this, false);
        dataSticksForNEI.add(dataStick);
        return dataStick;
    }

    public static void reInit() {
        for (RecipeAssemblyLine recipe : sAssemblylineRecipes) {
            for (ItemStack stack : recipe.dataSticksForNEI) {
                AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(stack, recipe, false);
            }
        }
    }

    /**
     * @param inputBusses List of input busses to check.
     * @return An array containing the amount of item to consume from the first slot of every input bus. {@code null} if
     * at least one item fails to match the recipe ingredient.
     */
    public static int[] getItemConsumptionAmountArray(ArrayList<MTEHatchInputBus> inputBusses,
        RecipeAssemblyLine recipe) {
        int itemCount = recipe.mInputs.length;
        if (itemCount == 0) return null;
        int[] tStacks = new int[itemCount];
        for (int i = 0; i < itemCount; i++) {
            MTEHatchInputBus inputBus = inputBusses.get(i);
            if (!inputBus.isValid()) return null;
            ItemStack slotStack;
            if (inputBus instanceof MTEHatchInputBusME meBus) {
                slotStack = meBus.getFirstShadowItemStack(true);
            } else {
                slotStack = inputBus.getFirstStack();
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
            if (GTUtility.areStacksEqual(aSlotStack, aIngredient, true)) {
                return aIngredient.stackSize;
            }
            return -1;
        }
        for (ItemStack tAltStack : alts) {
            if (GTUtility.areStacksEqual(aSlotStack, tAltStack, true)) {
                return tAltStack.stackSize;
            }
        }
        return -1;
    }

    /**
     * @param inputBusses      Input bus list to check. Usually the input bus list of multi.
     * @param itemConsumptions Should be generated by {@link RecipeAssemblyLine#getItemConsumptionAmountArray}.
     * @Return The number of parallel recipes, or 0 if recipe is not satisfied at all. 0 < number < 1 means that inputs
     * are found but not enough.
     */
    public static double maxParallelCalculatedByInputItems(ArrayList<MTEHatchInputBus> inputBusses, int maxParallel,
        int[] itemConsumptions, Map<GTUtility.ItemId, ItemStack> inputsFromME) {
        // Recipe item matching is done in the generation of itemConsumptions.

        Map<GTUtility.ItemId, Long> itemConsumptionsFromME = new Object2LongOpenHashMap<>();
        double currentParallel = maxParallel;

        // Calculate the amount of each item to consume from ME
        for (int i = 0; i < itemConsumptions.length; i++) {
            MTEHatchInputBus inputBus = inputBusses.get(i);
            if (!inputBus.isValid()) return 0;
            if (inputBus instanceof MTEHatchInputBusME meBus) {
                ItemStack item = meBus.getFirstShadowItemStack(true);
                if (item == null) return 0;
                GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(item);
                itemConsumptionsFromME.merge(id, (long) itemConsumptions[i], Long::sum);
            }
        }
        // Calculate parallel from ME input busses
        for (Map.Entry<GTUtility.ItemId, Long> entry : itemConsumptionsFromME.entrySet()) {
            if (!inputsFromME.containsKey(entry.getKey())) return 0;
            long consume = entry.getValue();
            // For non-consumed inputs
            if (consume == 0) continue;
            currentParallel = Math.min(currentParallel, (double) inputsFromME.get(entry.getKey()).stackSize / consume);
            if (currentParallel <= 0) return 0;
        }

        // Calculate parallel from regular input busses
        for (int i = 0; i < itemConsumptions.length; i++) {
            MTEHatchInputBus inputBus = inputBusses.get(i);
            if (!inputBus.isValid()) return 0;
            if (inputBus instanceof MTEHatchInputBusME) continue;

            ItemStack item = inputBus.getFirstStack();
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
     * @return The number of parallel recipes, or 0 if recipe is not satisfied at all. 0 < number < 1 means that fluids
     * are found but not enough.
     */
    public static double maxParallelCalculatedByInputFluids(ArrayList<MTEHatchInput> inputHatches, int maxParallel,
        FluidStack[] fluidConsumptions, Map<Fluid, FluidStack> fluidsFromME) {
        Map<Fluid, Long> fluidConsumptionsFromME = new Reference2LongOpenHashMap<>();
        double currentParallel = maxParallel;

        // Calculate the amount of each fluid to consume from ME
        for (int i = 0; i < fluidConsumptions.length; i++) {
            MTEHatchInput inputHatch = inputHatches.get(i);
            if (!inputHatch.isValid()) return 0;
            if (inputHatch instanceof MTEHatchInputME meHatch) {
                FluidStack fluid = meHatch.getFirstShadowFluidStack(true);
                if (fluid == null) return 0;
                if (!GTUtility.areFluidsEqual(fluid, fluidConsumptions[i])) return 0;
                fluidConsumptionsFromME.merge(fluid.getFluid(), (long) fluidConsumptions[i].amount, Long::sum);
            }
        }
        // Calculate parallel from ME input hatches
        for (Map.Entry<Fluid, Long> entry : fluidConsumptionsFromME.entrySet()) {
            Fluid fluid = entry.getKey();
            if (!fluidsFromME.containsKey(fluid)) return 0;
            long consume = entry.getValue();
            currentParallel = Math.min(currentParallel, (double) fluidsFromME.get(fluid).amount / consume);
            if (currentParallel <= 0) return 0;
        }

        // Calculate parallel from regular input hatches
        for (int i = 0; i < fluidConsumptions.length; i++) {
            MTEHatchInput inputHatch = inputHatches.get(i);
            if (!inputHatch.isValid()) return 0;
            if (inputHatch instanceof MTEHatchInputME) continue;

            FluidStack fluid;
            if (inputHatch instanceof MTEHatchMultiInput multiInput) {
                fluid = multiInput.getFluid();
            } else {
                fluid = inputHatch.getFillableStack();
            }
            if (fluid == null) return 0;
            if (!GTUtility.areFluidsEqual(fluid, fluidConsumptions[i])) return 0;
            currentParallel = Math.min(currentParallel, (double) fluid.amount / fluidConsumptions[i].amount);
            if (currentParallel <= 0) return 0;
        }
        return currentParallel;
    }

    /**
     * WARNING: Ensure that item inputs are enough to be consumed with
     * {@link RecipeAssemblyLine#maxParallelCalculatedByInputItems} before calling this method!
     *
     * @param inputBusses      Input bus list to check. Usually the input bus list of multi.
     * @param itemConsumptions Should be generated by {@link RecipeAssemblyLine#getItemConsumptionAmountArray}.
     */
    public static void consumeInputItems(ArrayList<MTEHatchInputBus> inputBusses, int amountMultiplier,
        int[] itemConsumptions, Map<GTUtility.ItemId, ItemStack> inputsFromME) {
        for (int i = 0; i < itemConsumptions.length; i++) {
            MTEHatchInputBus inputBus = inputBusses.get(i);
            if (!inputBus.isValid()) continue;
            ItemStack item;
            if (inputBus instanceof MTEHatchInputBusME meBus) {
                ItemStack itemStack = meBus.getFirstShadowItemStack(true);
                item = inputsFromME.get(GTUtility.ItemId.createNoCopy(itemStack));
            } else {
                item = inputBus.getFirstStack();
            }
            item.stackSize -= itemConsumptions[i] * amountMultiplier;
        }
    }

    /**
     * WARNING: Ensure that fluid inputs are enough to be consumed with
     * {@link RecipeAssemblyLine#maxParallelCalculatedByInputFluids} before calling this method!
     *
     * @param inputHatches      Input hatch list to check. Usually the input hatch list of multi.
     * @param fluidConsumptions Fluid inputs of the recipe.
     */
    public static void consumeInputFluids(ArrayList<MTEHatchInput> inputHatches, int amountMultiplier,
        FluidStack[] fluidConsumptions, Map<Fluid, FluidStack> fluidsFromME) {
        for (int i = 0; i < fluidConsumptions.length; i++) {
            MTEHatchInput inputHatch = inputHatches.get(i);
            if (!inputHatch.isValid()) continue;
            FluidStack fluid;
            if (inputHatch instanceof MTEHatchInputME meHatch) {
                FluidStack fluidStack = meHatch.getFirstShadowFluidStack(true);
                fluid = fluidsFromME.get(fluidStack.getFluid());
            } else if (inputHatch instanceof MTEHatchMultiInput multiInput) {
                fluid = multiInput.getFluid();
            } else {
                fluid = inputHatch.getFillableStack();
            }
            fluid.amount -= fluidConsumptions[i].amount * amountMultiplier;
        }
    }
}
