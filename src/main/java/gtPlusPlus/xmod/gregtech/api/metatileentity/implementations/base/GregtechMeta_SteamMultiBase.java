package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.ConcurrentHashSet;
import gtPlusPlus.api.objects.data.ConcurrentSet;
import gtPlusPlus.api.objects.data.FlexiblePair;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusOutput;

public abstract class GregtechMeta_SteamMultiBase<T extends GregtechMeta_SteamMultiBase<T>>
        extends GregtechMeta_MultiBlockBase<T> {

    public ArrayList<GT_MetaTileEntity_Hatch_Steam_BusInput> mSteamInputs = new ArrayList<GT_MetaTileEntity_Hatch_Steam_BusInput>();
    public ArrayList<GT_MetaTileEntity_Hatch_Steam_BusOutput> mSteamOutputs = new ArrayList<GT_MetaTileEntity_Hatch_Steam_BusOutput>();
    public ArrayList<GT_MetaTileEntity_Hatch_CustomFluidBase> mSteamInputFluids = new ArrayList<GT_MetaTileEntity_Hatch_CustomFluidBase>();

    protected static final String TT_steaminputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamInputBus");
    protected static final String TT_steamoutputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamOutputBus");
    protected static final String TT_steamhatch = StatCollector.translateToLocal("GTPP.MBTT.SteamHatch");

    public GregtechMeta_SteamMultiBase(String aName) {
        super(aName);
    }

    public GregtechMeta_SteamMultiBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
            final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()),
                    aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()) };
    }

    protected abstract GT_RenderedTexture getFrontOverlay();

    protected abstract GT_RenderedTexture getFrontOverlayActive();

    private int getCasingTextureIndex() {
        return 10;
    }

    @Override
    public int getEuDiscountForParallelism() {
        return 0;
    }

    @Override
    public boolean checkRecipe(ItemStack arg0) {

        log("Running checkRecipeGeneric(0)");
        ArrayList<ItemStack> tItems = getStoredInputs();
        ArrayList<FluidStack> tFluids = getStoredFluids();
        GT_Recipe_Map tMap = this.getRecipeMap();
        if (tMap == null) {
            return false;
        }
        ItemStack[] aItemInputs = tItems.toArray(new ItemStack[0]);
        FluidStack[] aFluidInputs = tFluids.toArray(new FluidStack[0]);
        GT_Recipe tRecipe = tMap.findRecipe(getBaseMetaTileEntity(), mLastRecipe, false, V[1], null, null, aItemInputs);
        if (tRecipe == null) {
            log("BAD RETURN - 1");
            return false;
        }

        int aEUPercent = 100;
        int aSpeedBonusPercent = 0;
        int aOutputChanceRoll = 10000;

        // Reset outputs and progress stats
        this.lEUt = 0;
        this.mMaxProgresstime = 0;
        this.mOutputItems = new ItemStack[] {};
        this.mOutputFluids = new FluidStack[] {};

        log("Running checkRecipeGeneric(1)");
        // Remember last recipe - an optimization for findRecipe()
        this.mLastRecipe = tRecipe;

        int aMaxParallelRecipes = canBufferOutputs(
                tRecipe.mOutputs,
                tRecipe.mFluidOutputs,
                this.getMaxParallelRecipes());
        if (aMaxParallelRecipes == 0) {
            log("BAD RETURN - 2");
            return false;
        }

        // EU discount
        float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
        float tTotalEUt = 0.0f;

        int parallelRecipes = 0;

        log("parallelRecipes: " + parallelRecipes);
        log("aMaxParallelRecipes: " + aMaxParallelRecipes);
        log("tTotalEUt: " + tTotalEUt);
        log("tRecipeEUt: " + tRecipeEUt);

        // Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
        for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (32 - tRecipeEUt); parallelRecipes++) {
            if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
                log("Broke at " + parallelRecipes + ".");
                break;
            }
            log("Bumped EU from " + tTotalEUt + " to " + (tTotalEUt + tRecipeEUt) + ".");
            tTotalEUt += tRecipeEUt;
        }

        if (parallelRecipes == 0) {
            log("BAD RETURN - 3");
            return false;
        }

        // -- Try not to fail after this point - inputs have already been consumed! --

        // Convert speed bonus to duration multiplier
        // e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
        aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
        float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
        this.mMaxProgresstime = (int) (tRecipe.mDuration * tTimeFactor * 1.5f);

        this.lEUt = (long) Math.ceil(tTotalEUt * 1.33f);

        // this.mEUt = (3 * tRecipe.mEUt);

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        if (this.lEUt > 0) {
            this.lEUt = (-this.lEUt);
        }

        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

        // Collect fluid outputs
        FluidStack[] tOutputFluids = getOutputFluids(tRecipe, parallelRecipes);

        // Collect output item types
        ItemStack[] tOutputItems = getOutputItems(tRecipe);

        // Set output item stack sizes (taking output chance into account)
        for (int f = 0; f < tOutputItems.length; f++) {
            if (tRecipe.mOutputs[f] != null && tOutputItems[f] != null) {
                for (int g = 0; g < parallelRecipes; g++) {
                    if (getBaseMetaTileEntity().getRandomNumber(aOutputChanceRoll) < tRecipe.getOutputChance(f))
                        tOutputItems[f].stackSize += tRecipe.mOutputs[f].stackSize;
                }
            }
        }

        tOutputItems = removeNulls(tOutputItems);

        // Sanitize item stack size, splitting any stacks greater than max stack size
        List<ItemStack> splitStacks = new ArrayList<ItemStack>();
        for (ItemStack tItem : tOutputItems) {
            while (tItem.getMaxStackSize() < tItem.stackSize) {
                ItemStack tmp = tItem.copy();
                tmp.stackSize = tmp.getMaxStackSize();
                tItem.stackSize = tItem.stackSize - tItem.getMaxStackSize();
                splitStacks.add(tmp);
            }
        }

        if (splitStacks.size() > 0) {
            ItemStack[] tmp = new ItemStack[splitStacks.size()];
            tmp = splitStacks.toArray(tmp);
            tOutputItems = ArrayUtils.addAll(tOutputItems, tmp);
        }

        // Strip empty stacks
        List<ItemStack> tSList = new ArrayList<ItemStack>();
        for (ItemStack tS : tOutputItems) {
            if (tS.stackSize > 0) tSList.add(tS);
        }
        tOutputItems = tSList.toArray(new ItemStack[0]);

        // Commit outputs
        this.mOutputItems = tOutputItems;
        this.mOutputFluids = tOutputFluids;
        updateSlots();

        // Play sounds (GT++ addition - GT multiblocks play no sounds)
        startProcess();

        log("GOOD RETURN - 1");
        return true;
    }

    public ArrayList<FluidStack> getAllSteamStacks() {
        ArrayList<FluidStack> aFluids = new ArrayList<FluidStack>();
        FluidStack aSteam = FluidUtils.getSteam(1);
        for (FluidStack aFluid : this.getStoredFluids()) {
            if (aFluid.isFluidEqual(aSteam)) {
                aFluids.add(aFluid);
            }
        }
        return aFluids;
    }

    public int getTotalSteamStored() {
        int aSteam = 0;
        for (FluidStack aFluid : getAllSteamStacks()) {
            aSteam += aFluid.amount;
        }
        return aSteam;
    }

    public boolean tryConsumeSteam(int aAmount) {
        if (getTotalSteamStored() <= 0) {
            return false;
        } else {
            return this.depleteInput(FluidUtils.getSteam(aAmount));
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack arg0) {
        return 0;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
                this.mSteamInputs.clear();
                this.mSteamOutputs.clear();
                this.mSteamInputFluids.clear();
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    /**
     * Called every tick the Machine runs
     */
    public boolean onRunningTick(ItemStack aStack) {
        fixAllMaintenanceIssue();
        if (lEUt < 0) {
            long aSteamVal = ((-lEUt * 10000) / Math.max(1000, mEfficiency));
            // Logger.INFO("Trying to drain "+aSteamVal+" steam per tick.");
            if (!tryConsumeSteam((int) aSteamVal)) {
                stopMachine();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            log("Invalid IGregTechTileEntity");
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            log("Invalid IMetaTileEntity");
            return false;
        }

        // Use this to determine the correct value, then update the hatch texture after.
        boolean aDidAdd = false;

        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_CustomFluidBase) {
            log("Adding Steam Input Hatch");
            aDidAdd = addToMachineListInternal(mSteamInputFluids, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusInput) {
            log("Trying to set recipe map. Type: " + (getRecipeMap() != null ? getRecipeMap().mNEIName : "Null"));
            this.resetRecipeMapForHatch(aTileEntity, getRecipeMap());
            log("Adding Steam Input Bus");
            aDidAdd = addToMachineListInternal(mSteamInputs, aMetaTileEntity, aBaseCasingIndex);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Steam_BusOutput) {
            log("Adding Steam Output Bus");
            aDidAdd = addToMachineListInternal(mSteamOutputs, aMetaTileEntity, aBaseCasingIndex);
        }

        return aDidAdd;
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
    }

    public FluidStack[] getOutputFluids(GT_Recipe aRecipe, int parallelRecipes) {
        // Collect fluid outputs
        FluidStack[] tOutputFluids = new FluidStack[aRecipe.mFluidOutputs.length];
        for (int h = 0; h < aRecipe.mFluidOutputs.length; h++) {
            if (aRecipe.getFluidOutput(h) != null) {
                tOutputFluids[h] = aRecipe.getFluidOutput(h).copy();
                tOutputFluids[h].amount *= parallelRecipes;
            }
        }
        return tOutputFluids;
    }

    public ItemStack[] getOutputItems(GT_Recipe aRecipe) {
        // Collect output item types
        ItemStack[] tOutputItems = new ItemStack[aRecipe.mOutputs.length];
        for (int h = 0; h < aRecipe.mOutputs.length; h++) {
            if (aRecipe.getOutput(h) != null) {
                tOutputItems[h] = aRecipe.getOutput(h).copy();
                tOutputItems[h].stackSize = 0;
            }
        }
        return tOutputItems;
    }

    public int getOutputCount(ItemStack[] aOutputs) {
        return aOutputs.length;
    }

    public int getOutputFluidCount(FluidStack[] aOutputs) {
        return aOutputs.length;
    }

    public int canBufferOutputs(final ItemStack[] aOutputs, FluidStack[] aFluidOutputs, int aParallelRecipes) {

        log("Determining if we have space to buffer outputs. Parallel: " + aParallelRecipes);

        // Null recipe or a recipe with lots of outputs?
        // E.G. Gendustry custom comb with a billion centrifuge outputs?
        // Do it anyway, provided the multi allows it. Default behaviour is aAllow16SlotWithoutCheck = true.
        if (aOutputs == null && aFluidOutputs == null) {
            return 0;
        }

        // Do we even need to check for item outputs?
        boolean aDoesOutputItems = aOutputs != null ? aOutputs.length > 0 : false;
        // Do we even need to check for fluid outputs?
        boolean aDoesOutputFluids = aFluidOutputs != null ? aFluidOutputs.length > 0 : false;

        if (!aDoesOutputItems && !aDoesOutputFluids) {
            return 0;
        }

        /*
         * ======================================== Item Management ========================================
         */

        if (aDoesOutputItems) {
            log("We have items to output.");

            // How many slots are free across all the output buses?
            int aInputBusSlotsFree = 0;

            /*
             * Create Variables for Item Output
             */

            AutoMap<FlexiblePair<ItemStack, Integer>> aItemMap = new AutoMap<FlexiblePair<ItemStack, Integer>>();
            AutoMap<ItemStack> aItemOutputs = new AutoMap<ItemStack>(aOutputs);

            for (final GT_MetaTileEntity_Hatch_Steam_BusOutput tBus : this.mSteamOutputs) {
                if (!isValidMetaTileEntity(tBus)) {
                    continue;
                }
                final IInventory tBusInv = tBus.getBaseMetaTileEntity();
                for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
                    if (tBus.getStackInSlot(i) == null) {
                        aInputBusSlotsFree++;
                    } else {
                        ItemStack aT = tBus.getStackInSlot(i);
                        int aSize = aT.stackSize;
                        aT = aT.copy();
                        aT.stackSize = 0;
                        aItemMap.put(new FlexiblePair<ItemStack, Integer>(aT, aSize));
                    }
                }
            }

            // Count the slots we need, later we can check if any are able to merge with existing stacks
            int aRecipeSlotsRequired = 0;

            // A map to hold the items we will be 'inputting' into the output buses. These itemstacks are actually the
            // recipe outputs.
            ConcurrentSet<FlexiblePair<ItemStack, Integer>> aInputMap = new ConcurrentHashSet<FlexiblePair<ItemStack, Integer>>();

            // Iterate over the outputs, calculating require stack spacing they will require.
            for (int i = 0; i < getOutputCount(aOutputs); i++) {
                ItemStack aY = aItemOutputs.get(i);
                if (aY == null) {
                    continue;
                } else {
                    int aStackSize = aY.stackSize * aParallelRecipes;
                    if (aStackSize > 64) {
                        int aSlotsNeedsForThisStack = (int) Math.ceil((double) ((float) aStackSize / 64f));
                        // Should round up and add as many stacks as required nicely.
                        aRecipeSlotsRequired += aSlotsNeedsForThisStack;
                        for (int o = 0; o < aRecipeSlotsRequired; o++) {
                            int aStackToRemove = (aStackSize -= 64) > 64 ? 64 : aStackSize;
                            aY = aY.copy();
                            aY.stackSize = 0;
                            aInputMap.add(new FlexiblePair<ItemStack, Integer>(aY, aStackToRemove));
                        }
                    } else {
                        // Only requires one slot
                        aRecipeSlotsRequired++;
                        aY = aY.copy();
                        aY.stackSize = 0;
                        aInputMap.add(new FlexiblePair<ItemStack, Integer>(aY, aStackSize));
                    }
                }
            }

            // We have items to add to the output buses. See if any are not full stacks and see if we can make them
            // full.
            if (aInputMap.size() > 0) {
                // Iterate over the current stored items in the Output busses, if any match and are not full, we can try
                // account for merging.
                busItems: for (FlexiblePair<ItemStack, Integer> y : aItemMap) {
                    // Iterate over the 'inputs', we can safely remove these as we go.
                    outputItems: for (FlexiblePair<ItemStack, Integer> u : aInputMap) {
                        // Create local vars for readability.
                        ItemStack aOutputBusStack = y.getKey();
                        ItemStack aOutputStack = u.getKey();
                        // Stacks match, including NBT.
                        if (GT_Utility.areStacksEqual(aOutputBusStack, aOutputStack, false)) {
                            // Stack Matches, but it's full, continue.
                            if (aOutputBusStack.stackSize >= 64) {
                                // This stack is full, no point checking it.
                                continue busItems;
                            } else {
                                // We can merge these two stacks without any hassle.
                                if ((aOutputBusStack.stackSize + aOutputStack.stackSize) <= 64) {
                                    // Update the stack size in the bus storage map.
                                    y.setValue(aOutputBusStack.stackSize + aOutputStack.stackSize);
                                    // Remove the 'input' stack from the recipe outputs, so we don't try count it again.
                                    aInputMap.remove(u);
                                    continue outputItems;
                                }
                                // Stack merging is too much, so we fill this stack, leave the remainder.
                                else {
                                    int aRemainder = (aOutputBusStack.stackSize + aOutputStack.stackSize) - 64;
                                    // Update the stack size in the bus storage map.
                                    y.setValue(64);
                                    // Create a new object to iterate over later, with the remainder data;
                                    FlexiblePair<ItemStack, Integer> t = new FlexiblePair<ItemStack, Integer>(
                                            u.getKey(),
                                            aRemainder);
                                    // Remove the 'input' stack from the recipe outputs, so we don't try count it again.
                                    aInputMap.remove(u);
                                    // Add the remainder stack.
                                    aInputMap.add(t);
                                    continue outputItems;
                                }
                            }
                        } else {
                            continue outputItems;
                        }
                    }
                }
            }

            // We have stacks that did not merge, do we have space for them?
            if (aInputMap.size() > 0) {
                if (aInputMap.size() > aInputBusSlotsFree) {
                    aParallelRecipes = (int) Math
                            .floor((double) aInputBusSlotsFree / aInputMap.size() * aParallelRecipes);
                    // We do not have enough free slots in total to accommodate the remaining managed stacks.
                    log(" Free: " + aInputBusSlotsFree + ", Required: " + aInputMap.size());
                    if (aParallelRecipes == 0) {
                        log("Failed to find enough space for all item outputs.");
                        return 0;
                    }
                }
            }

            /*
             * End Item Management
             */

        }

        /*
         * ======================================== Fluid Management ========================================
         */

        if (aDoesOutputFluids) {
            log("We have Fluids to output.");
            // How many slots are free across all the output buses?
            int aFluidHatches = 0;
            int aEmptyFluidHatches = 0;
            int aFullFluidHatches = 0;
            // Create Map for Fluid Output
            ArrayList<Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>> aOutputHatches = new ArrayList<Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>>();
            for (final GT_MetaTileEntity_Hatch_Output tBus : this.mOutputHatches) {
                if (!isValidMetaTileEntity(tBus)) {
                    continue;
                }
                aFluidHatches++;
                // Map the Hatch with the space left for easy checking later.
                if (tBus.getFluid() == null) {
                    aOutputHatches.add(
                            new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(
                                    tBus,
                                    null,
                                    tBus.getCapacity()));
                } else {
                    int aSpaceLeft = tBus.getCapacity() - tBus.getFluidAmount();
                    aOutputHatches.add(
                            new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(
                                    tBus,
                                    tBus.getFluid(),
                                    aSpaceLeft));
                }
            }
            // Create a map of all the fluids we would like to output, we can iterate over this and see how many we can
            // merge into existing hatch stacks.
            ArrayList<FluidStack> aOutputFluids = new ArrayList<FluidStack>();
            // Ugly ass boxing
            aOutputFluids.addAll(new AutoMap<FluidStack>(aFluidOutputs));
            // Iterate the Hatches, updating their 'stored' data.
            // for (Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aHatchData : aOutputHatches) {
            for (int i = 0; i < aOutputHatches.size(); i++) {
                // The Hatch Itself
                GT_MetaTileEntity_Hatch_Output aHatch = aOutputHatches.get(i).getValue_1();
                // Fluid in the Hatch
                FluidStack aHatchStack = aOutputHatches.get(i).getValue_2();
                // Space left in Hatch
                int aSpaceLeftInHatch = aHatch.getCapacity() - aHatch.getFluidAmount();
                // Hatch is full,
                if (aSpaceLeftInHatch <= 0) {
                    aFullFluidHatches++;
                    aOutputHatches.remove(aOutputHatches.get(i));
                    i--;
                    continue;
                }
                // Hatch has space
                else {
                    // Check if any fluids match
                    // aFluidMatch: for (FluidStack aOutputStack : aOutputFluids) {
                    for (int j = 0; j < getOutputFluidCount(aFluidOutputs); j++) {
                        // log(" aHatchStack "+aHatchStack.getLocalizedName()+" aOutput stack
                        // "+aOutputStack.getLocalizedName());
                        if (GT_Utility.areFluidsEqual(aHatchStack, aOutputFluids.get(j))) {
                            int aFluidToPutIntoHatch = aOutputFluids.get(j).amount * aParallelRecipes;
                            // Not Enough space to insert all of the fluid.
                            // We fill this hatch and add a smaller Fluidstack back to the iterator.
                            if (aSpaceLeftInHatch < aFluidToPutIntoHatch) {
                                // Copy existing Hatch Stack
                                FluidStack aNewHatchStack = aHatchStack.copy();
                                aNewHatchStack.amount = 0;
                                // Copy existing Hatch Stack again
                                FluidStack aNewOutputStack = aHatchStack.copy();
                                aNewOutputStack.amount = 0;
                                // How much fluid do we have left after we fill the hatch?
                                int aFluidLeftAfterInsert = aFluidToPutIntoHatch - aSpaceLeftInHatch;
                                // Set new stacks to appropriate values
                                aNewHatchStack.amount = aHatch.getCapacity();
                                aNewOutputStack.amount = aFluidLeftAfterInsert;
                                // Remove fluid from output list, merge success
                                aOutputFluids.remove(aOutputFluids.get(j));
                                j--;
                                // Remove hatch from hatch list, data is now invalid.
                                aOutputHatches.remove(aOutputHatches.get(i));
                                i--;
                                // Add remaining Fluid to Output list
                                aOutputFluids.add(aNewOutputStack);
                                // Re-add hatch to hatch list, with new data.
                                // Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aNewHatchData = new
                                // Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(aHatch, aNewHatchStack,
                                // aNewHatchStack.amount);
                                // aOutputHatches.add(aNewHatchData);
                                break;
                            }
                            // We can fill this hatch perfectly (rare case), may as well add it directly to the full
                            // list.
                            else if (aSpaceLeftInHatch == aFluidToPutIntoHatch) {
                                // Copy Old Stack
                                FluidStack aNewHatchStack = aHatchStack.copy();
                                // Add in amount from output stack
                                aNewHatchStack.amount += aFluidToPutIntoHatch;
                                // Remove fluid from output list, merge success
                                aOutputFluids.remove(aOutputFluids.get(j));
                                j--;
                                // Remove hatch from hatch list, data is now invalid.
                                aOutputHatches.remove(aOutputHatches.get(i));
                                i--;
                                // Re-add hatch to hatch list, with new data.
                                Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aNewHatchData = new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(
                                        aHatch,
                                        aNewHatchStack,
                                        aNewHatchStack.amount);
                                aOutputHatches.add(aNewHatchData);
                                break;
                            }
                            // We have more space than we need to merge, so we remove the stack from the output list and
                            // update the hatch list.
                            else {
                                // Copy Old Stack
                                FluidStack aNewHatchStack = aHatchStack.copy();
                                // Add in amount from output stack
                                aNewHatchStack.amount += aFluidToPutIntoHatch;
                                // Remove fluid from output list, merge success
                                aOutputFluids.remove(aOutputFluids.get(j));
                                j--;
                                // Remove hatch from hatch list, data is now invalid.
                                aOutputHatches.remove(aOutputHatches.get(i));
                                i--;
                                // Re-add hatch to hatch list, with new data.
                                Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aNewHatchData = new Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer>(
                                        aHatch,
                                        aNewHatchStack,
                                        aNewHatchStack.amount);
                                aOutputHatches.add(aNewHatchData);
                                // Check next fluid
                                continue;
                            }

                        } else {
                            continue;
                        }
                    }
                }
            }

            for (Triplet<GT_MetaTileEntity_Hatch_Output, FluidStack, Integer> aFreeHatchCheck : aOutputHatches) {
                // Free Hatch
                if (aFreeHatchCheck.getValue_2() == null || aFreeHatchCheck.getValue_3() == 0
                        || aFreeHatchCheck.getValue_1().getFluid() == null) {
                    aEmptyFluidHatches++;
                }
            }

            // We have Fluid Stacks we did not merge. Do we have space?
            log("fluids to output " + aOutputFluids.size() + " empty hatches " + aEmptyFluidHatches);
            if (aOutputFluids.size() > 0) {
                // Not enough space to add fluids.
                if (aOutputFluids.size() > aEmptyFluidHatches) {
                    aParallelRecipes = (int) Math
                            .floor((double) aEmptyFluidHatches / aOutputFluids.size() * aParallelRecipes);
                    log(
                            "Failed to find enough space for all fluid outputs. Free: " + aEmptyFluidHatches
                                    + ", Required: "
                                    + aOutputFluids.size());
                    return 0;
                }
            }

            /*
             * End Fluid Management
             */
        }

        return aParallelRecipes;
    }

    /*
     * Handle I/O with custom hatches
     */

    @Override
    public boolean depleteInput(FluidStack aLiquid) {
        if (aLiquid == null) return false;
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : mSteamInputFluids) {
            if (isValidMetaTileEntity(tHatch)) {
                FluidStack tLiquid = tHatch.getFluid();
                if (tLiquid != null && tLiquid.isFluidEqual(aLiquid)) {
                    tLiquid = tHatch.drain(aLiquid.amount, false);
                    if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
                        tLiquid = tHatch.drain(aLiquid.amount, true);
                        return tLiquid != null && tLiquid.amount >= aLiquid.amount;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean depleteInput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        FluidStack aLiquid = GT_Utility.getFluidForFilledItem(aStack, true);
        if (aLiquid != null) return depleteInput(aLiquid);
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : mSteamInputFluids) {
            if (isValidMetaTileEntity(tHatch)) {
                if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(0))) {
                    if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
                        tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
                        return true;
                    }
                }
            }
        }
        for (GT_MetaTileEntity_Hatch_Steam_BusInput tHatch : mSteamInputs) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    if (GT_Utility.areStacksEqual(aStack, tHatch.getBaseMetaTileEntity().getStackInSlot(i))) {
                        if (tHatch.getBaseMetaTileEntity().getStackInSlot(0).stackSize >= aStack.stackSize) {
                            tHatch.getBaseMetaTileEntity().decrStackSize(0, aStack.stackSize);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ArrayList<FluidStack> getStoredFluids() {
        ArrayList<FluidStack> rList = new ArrayList<FluidStack>();
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : mSteamInputFluids) {
            if (isValidMetaTileEntity(tHatch) && tHatch.getFillableStack() != null) {
                rList.add(tHatch.getFillableStack());
            }
        }
        return rList;
    }

    @Override
    public ArrayList<ItemStack> getStoredInputs() {
        ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
        for (GT_MetaTileEntity_Hatch_Steam_BusInput tHatch : mSteamInputs) {
            tHatch.mRecipeMap = getRecipeMap();
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    if (tHatch.getBaseMetaTileEntity().getStackInSlot(i) != null) {
                        rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
                    }
                }
            }
        }
        return rList;
    }

    @Override
    public boolean addOutput(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;
        aStack = GT_Utility.copy(aStack);
        boolean outputSuccess = true;
        while (outputSuccess && aStack.stackSize > 0) {
            outputSuccess = false;
            ItemStack single = aStack.splitStack(1);
            for (GT_MetaTileEntity_Hatch_Steam_BusOutput tHatch : mSteamOutputs) {
                if (!outputSuccess && isValidMetaTileEntity(tHatch)) {
                    for (int i = tHatch.getSizeInventory() - 1; i >= 0 && !outputSuccess; i--) {
                        if (tHatch.getBaseMetaTileEntity().addStackToSlot(i, single)) outputSuccess = true;
                    }
                }
            }
            for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
                if (!outputSuccess && isValidMetaTileEntity(tHatch) && tHatch.outputsItems()) {
                    if (tHatch.getBaseMetaTileEntity().addStackToSlot(1, single)) outputSuccess = true;
                }
            }
        }
        return outputSuccess;
    }

    @Override
    public ArrayList<ItemStack> getStoredOutputs() {
        ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
        for (GT_MetaTileEntity_Hatch_Steam_BusOutput tHatch : mSteamOutputs) {
            if (isValidMetaTileEntity(tHatch)) {
                for (int i = tHatch.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
                    rList.add(tHatch.getBaseMetaTileEntity().getStackInSlot(i));
                }
            }
        }
        return rList;
    }

    @Override
    public void updateSlots() {
        for (GT_MetaTileEntity_Hatch_CustomFluidBase tHatch : mSteamInputFluids)
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
        for (GT_MetaTileEntity_Hatch_Steam_BusInput tHatch : mSteamInputs)
            if (isValidMetaTileEntity(tHatch)) tHatch.updateSlots();
    }

    protected static <T extends GregtechMeta_SteamMultiBase<T>> GT_HatchElementBuilder<T> buildSteamInput(
            Class<T> typeToken) {
        return buildHatchAdder(typeToken).adder(GregtechMeta_SteamMultiBase::addToMachineList).hatchIds(31040)
                .shouldReject(t -> !t.mSteamInputFluids.isEmpty());
    }

    protected enum SteamHatchElement implements IHatchElement<GregtechMeta_SteamMultiBase<?>> {

        InputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(GT_MetaTileEntity_Hatch_Steam_BusInput.class);
            }

            @Override
            public long count(GregtechMeta_SteamMultiBase<?> t) {
                return t.mSteamInputs.size();
            }
        },
        OutputBus_Steam {

            @Override
            public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
                return Collections.singletonList(GT_MetaTileEntity_Hatch_Steam_BusOutput.class);
            }

            @Override
            public long count(GregtechMeta_SteamMultiBase<?> t) {
                return t.mSteamOutputs.size();
            }
        },;

        @Override
        public IGT_HatchAdder<? super GregtechMeta_SteamMultiBase<?>> adder() {
            return GregtechMeta_SteamMultiBase::addToMachineList;
        }
    }
}
