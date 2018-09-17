package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_Multi_Basic_Slotted;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

public abstract class GregtechMeta_MultiBlockBase
extends
GT_MetaTileEntity_MultiBlockBase {

	public GT_Recipe mLastRecipe;
	private boolean mInternalCircuit = false;
	protected long mTotalRunTime = 0;

	public ArrayList<GT_MetaTileEntity_Hatch_InputBattery> mChargeHatches = new ArrayList<GT_MetaTileEntity_Hatch_InputBattery>();
	public ArrayList<GT_MetaTileEntity_Hatch_OutputBattery> mDischargeHatches = new ArrayList<GT_MetaTileEntity_Hatch_OutputBattery>();

	public GregtechMeta_MultiBlockBase(final int aID, final String aName,
			final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMeta_MultiBlockBase(final String aName) {
		super(aName);
	}

	public static boolean isValidMetaTileEntity(
			final MetaTileEntity aMetaTileEntity) {
		return (aMetaTileEntity.getBaseMetaTileEntity() != null)
				&& (aMetaTileEntity.getBaseMetaTileEntity()
						.getMetaTileEntity() == aMetaTileEntity)
				&& !aMetaTileEntity.getBaseMetaTileEntity().isDead();
	}

	public abstract boolean hasSlotInGUI();

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		if (hasSlotInGUI()) {
			return new GT_Container_MultiMachine(aPlayerInventory,	aBaseMetaTileEntity);			
		}
		else {
			return new CONTAINER_MultiMachine(aPlayerInventory,	aBaseMetaTileEntity);			
		}
	}
	
	public abstract String getCustomGUIResourceName();
	
	public boolean requiresVanillaGtGUI() {
		return false;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {		
		String aCustomGUI = getCustomGUIResourceName();
		aCustomGUI = aCustomGUI != null ? aCustomGUI : "MultiblockDisplay";
		aCustomGUI = aCustomGUI + ".png";
		if (hasSlotInGUI()) {
			if (!requiresVanillaGtGUI()) {
				return new GUI_Multi_Basic_Slotted(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), aCustomGUI);			
			}
			else {
				return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), aCustomGUI);			
			}			
		}
		else {
			return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), aCustomGUI);			
		}		
	}

	public abstract String getMachineType();
	
	public String getMachineTooltip() {
		return "Machine Type: " + EnumChatFormatting.YELLOW + getMachineType() + EnumChatFormatting.RESET;
	}
	
	public String[] getExtraInfoData() {
		return new String[0];
	};

	@Override
	public final String[] getInfoData() {
		ArrayList<String> mInfo = new ArrayList<String>();
		if (!this.getMetaName().equals("")) {
			mInfo.add(this.getMetaName());
		}

		String[] extra = getExtraInfoData();

		if (extra == null) {
			extra = new String[0];
		}
		if (extra.length > 0) {
			for (String s : extra) {
				mInfo.add(s);
			}
		}

		long seconds = (this.mTotalRunTime/20);
		int weeks = (int) (TimeUnit.SECONDS.toDays(seconds) / 7);
		int days = (int) (TimeUnit.SECONDS.toDays(seconds) - 7 * weeks);
		long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(days) - TimeUnit.DAYS.toHours(7*weeks);
		long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		mInfo.add(getMachineTooltip());
		mInfo.add("Progress: " + Integer.toString((this.mProgresstime / 20)) +" / "+ Integer.toString((this.mMaxProgresstime / 20)) + " secs");
		mInfo.add("Efficiency: " + Float.toString((this.mEfficiency / 100.0F)) + "%");
		mInfo.add("Problems: " + Integer.toString((this.getIdealStatus() - this.getRepairStatus())));
		mInfo.add("Pollution: "+this.getPollutionPerTick(null)*20+"/second");
		mInfo.add("Total Time Since Built: " + Integer.toString(weeks)+" Weeks, " + Integer.toString(days) + " Days, ");
		mInfo.add(Long.toString(hours) + " Hours, " + Long.toString(minutes) + " Minutes, " + Long.toString(second) + " Seconds.");
		mInfo.add("Total Time in ticks: " + Long.toString(this.mTotalRunTime));

		String[] mInfo2 = new String[mInfo.size()];
		mInfo.toArray(mInfo2);
		return mInfo2;



	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean isCorrectMachinePart(final ItemStack paramItemStack) {
		return true;
	}

	@Override
	public int getDamageToComponent(final ItemStack paramItemStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack p0) {
		return false;
	}

	@Override
	public void startSoundLoop(final byte aIndex, final double aX, final double aY, final double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 1) {
			GT_Utility.doSoundAtClient(getSound(), 10, 1.0F, aX, aY, aZ);
		}
	}

	public void startProcess() {
		if(GT_Utility.isStringValid(getSound())) this.sendLoopStart((byte) 1);
	}

	public String getSound() { return ""; }

	public boolean canBufferOutputs(final GT_Recipe aRecipe, int aParallelRecipes) {
		if (aRecipe.mOutputs.length > 16) {
			// Gendustry custom comb with a billion centrifuge outputs? Do it anyway.
			return true;
		}

		// Count slots available in output buses
		ArrayList<ItemStack> tBusStacks = new ArrayList<>();

		int tEmptySlots = 0;
		for (final GT_MetaTileEntity_Hatch_OutputBus tBus : this.mOutputBusses) {
			if (!isValidMetaTileEntity(tBus)) {
				continue;
			}
			final IInventory tBusInv = tBus.getBaseMetaTileEntity();
			for (int i = 0; i < tBusInv.getSizeInventory(); i++) {
				if (tBus.getStackInSlot(i) == null) {
					tEmptySlots++;
				}
				else {
					tBusStacks.add(tBus.getStackInSlot(i));
				}
			}
		}

		int slotsNeeded = aRecipe.mOutputs.length;
		for (final ItemStack tRecipeOutput: aRecipe.mOutputs) {
			if (tRecipeOutput == null) continue;
			int amount = tRecipeOutput.stackSize * aParallelRecipes;
			for (final ItemStack tBusStack : tBusStacks) {
				if (GT_Utility.areStacksEqual(tBusStack, tRecipeOutput)) {
					if (tBusStack.stackSize + amount <= tBusStack.getMaxStackSize()) {
						slotsNeeded--;
						break;
					}
				}
			}
		}
		// Enough open slots?
		if (tEmptySlots < slotsNeeded) return false;

		// For each output fluid, make sure an output hatch can accept it.
		for (FluidStack tRecipeFluid: aRecipe.mFluidOutputs) {
			if (tRecipeFluid == null) continue;
			boolean tCanBufferFluid = false;
			int tRecipeAmount = tRecipeFluid.amount;
			for (final GT_MetaTileEntity_Hatch_Output tHatch : this.mOutputHatches) {
				FluidStack tHatchFluid = tHatch.getFluid();
				if (tHatchFluid == null) {
					if(tHatch.getCapacity() > tRecipeAmount) {
						tCanBufferFluid = true;
						break;
					}
				}
				else if (tHatchFluid.isFluidEqual(tRecipeFluid) && tHatch.getCapacity() - tHatchFluid.amount > tRecipeAmount) {
					tCanBufferFluid = true;
					break;
				}
			}
			if (!tCanBufferFluid) return false;
		}
		return true;
	}

	/**
	 * A Static {@link Method} object which holds the current status of logging.
	 */
	public static Method aLogger = null;

	public void log(String s) {
		boolean isDebugLogging = CORE.DEBUG;	
		boolean reset = false;
		if (aLogger == null || reset) {
			if (isDebugLogging) {
				try {
					aLogger = Logger.class.getMethod("INFO", String.class);
				} catch (NoSuchMethodException | SecurityException e) {}
			}
			else {
				try {
					aLogger = Logger.class.getMethod("MACHINE_INFO", String.class);
				} catch (NoSuchMethodException | SecurityException e) {}			
			}
		}
		try {
			aLogger.invoke(null, s);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}

	}

	public boolean checkRecipeGeneric() {
		return checkRecipeGeneric(1, 100, 0);
	}

	public boolean checkRecipeGeneric(int aMaxParallelRecipes, int aEUPercent, int aSpeedBonusPercent) {
		return checkRecipeGeneric(aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, 10000);
	}

	public boolean checkRecipeGeneric(int aMaxParallelRecipes, int aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {
		ArrayList<ItemStack> tItems = getStoredInputs();
		ArrayList<FluidStack> tFluids = getStoredFluids();
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
		return checkRecipeGeneric(tItemInputs, tFluidInputs, aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, aOutputChanceRoll);
	}

	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {
		// Based on the Processing Array. A bit overkill, but very flexible.




		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		log("Running checkRecipeGeneric(0)");


		GT_Recipe tRecipe = findRecipe(
				getBaseMetaTileEntity(), mLastRecipe, false,
				gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);

		log("Running checkRecipeGeneric(1)");
		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;

		if (tRecipe == null) {
			log("BAD RETURN - 1");
			return false;
		}

		if (!this.canBufferOutputs(tRecipe, aMaxParallelRecipes)) {
			log("BAD RETURN - 2");
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;

		log("parallelRecipes: "+parallelRecipes);
		log("aMaxParallelRecipes: "+aMaxParallelRecipes);
		log("tTotalEUt: "+tTotalEUt);
		log("tVoltage: "+tVoltage);
		log("tRecipeEUt: "+tRecipeEUt);
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				log("Broke at "+parallelRecipes+".");
				break;
			}
			log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
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
		this.mMaxProgresstime = (int)(tRecipe.mDuration * tTimeFactor);

		this.mEUt = (int)Math.ceil(tTotalEUt);

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;

		// Overclock
		if (this.mEUt <= 16) {
			this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
			this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
		} else {
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= 2;
			}
		}

		if (this.mEUt > 0) {
			this.mEUt = (-this.mEUt);
		}

		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

		// Collect fluid outputs
		FluidStack[] tOutputFluids = new FluidStack[tRecipe.mFluidOutputs.length];
		for (int h = 0; h < tRecipe.mFluidOutputs.length; h++) {
			if (tRecipe.getFluidOutput(h) != null) {
				tOutputFluids[h] = tRecipe.getFluidOutput(h).copy();
				tOutputFluids[h].amount *= parallelRecipes;
			}
		}

		// Collect output item types
		ItemStack[] tOutputItems = new ItemStack[tRecipe.mOutputs.length];
		for (int h = 0; h < tRecipe.mOutputs.length; h++) {
			if (tRecipe.getOutput(h) != null) {
				tOutputItems[h] = tRecipe.getOutput(h).copy();
				tOutputItems[h].stackSize = 0;
			}
		}

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
		tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

		// Commit outputs
		this.mOutputItems = tOutputItems;
		this.mOutputFluids = tOutputFluids;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		log("GOOD RETURN - 1");
		return true;
	}

	public GT_Recipe reduceRecipeTimeByPercentage(final GT_Recipe tRecipe,
			final float percentage) {
		int cloneTime = 0;
		GT_Recipe baseRecipe;
		GT_Recipe cloneRecipe = null;

		baseRecipe = tRecipe.copy();
		if ((baseRecipe != null) && ((cloneRecipe != baseRecipe) || (cloneRecipe == null))) {
			cloneRecipe = baseRecipe.copy();
			log("Setting Recipe");
		}
		if ((baseRecipe != null) && ((cloneTime != baseRecipe.mDuration) || (cloneTime == 0))) {
			cloneTime = baseRecipe.mDuration;
			log("Setting Time");
		}

		if ((cloneRecipe != null) && cloneRecipe.mDuration > 0) {
			final int originalTime = cloneRecipe.mDuration;
			final int tempTime = MathUtils.findPercentageOfInt(cloneRecipe.mDuration,
					(100 - percentage));
			cloneRecipe.mDuration = tempTime;
			if (cloneRecipe.mDuration < originalTime) {
				log("Generated recipe with a smaller time. | "
						+ originalTime + " | " + cloneRecipe.mDuration + " |");
				return cloneRecipe;
			} else {
				log("Did not generate recipe with a smaller time. | "
						+ originalTime + " | " + cloneRecipe.mDuration + " |");
				return tRecipe;
			}
		}
		log("Error generating recipe, returning null.");
		return null;

	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity,
			final long aTick) {

		//Time Counter
		if (aBaseMetaTileEntity.isServerSide()){
			this.mTotalRunTime++;
		}

		if (aBaseMetaTileEntity.isServerSide()) {
			if (mUpdate == 0 || this.mStartUpCheck == 0) {
				this.mChargeHatches.clear();
				this.mDischargeHatches.clear();
			}
		}

		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public void explodeMultiblock() {
		MetaTileEntity tTileEntity;
		for (final Iterator<GT_MetaTileEntity_Hatch_InputBattery> localIterator = this.mChargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}
		tTileEntity = null;
		for (final Iterator<GT_MetaTileEntity_Hatch_OutputBattery> localIterator = this.mDischargeHatches
				.iterator(); localIterator.hasNext(); tTileEntity
				.getBaseMetaTileEntity()
				.doExplosion(gregtech.api.enums.GT_Values.V[8])) {
			tTileEntity = localIterator.next();
		}
		super.explodeMultiblock();
	}

	protected int getGUICircuit(ItemStack[] t) {
		Item g = CI.getNumberedCircuit(0).getItem();
		ItemStack guiSlot = this.mInventory[1];
		int mMode = -1;		
		if (guiSlot != null && guiSlot.getItem() == g) {
			this.mInternalCircuit = true;	
			return guiSlot.getItemDamage();
		}
		else {
			this.mInternalCircuit = false;
		}

		if (!this.mInternalCircuit) {
			for (ItemStack j : t) {
				if (j.getItem() == g) {
					mMode = j.getItemDamage();
					break;
				}
			}
		}
		return mMode;
	}

	protected ItemStack getGUIItemStack() {
		ItemStack guiSlot = this.mInventory[1];		
		return guiSlot;
	}

	@Override
	public void updateSlots() {
		for (final GT_MetaTileEntity_Hatch_InputBattery tHatch : this.mChargeHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
		for (final GT_MetaTileEntity_Hatch_OutputBattery tHatch : this.mDischargeHatches) {
			if (isValidMetaTileEntity(tHatch)) {
				tHatch.updateSlots();
			}
		}
		super.updateSlots();
	}

	public boolean isToolCreative(ItemStack mStack){
		Materials t1 = GT_MetaGenerated_Tool.getPrimaryMaterial(mStack);
		Materials t2 = GT_MetaGenerated_Tool.getSecondaryMaterial(mStack);
		if (t1 == Materials._NULL && t2 == Materials._NULL){
			return true;
		}
		return false;
	}

	/**
	 * Causes a Random Maint. Issue.
	 * @return {@link boolean} - Returns whether or not an issue was caused, should always be true.
	 */
	public boolean causeMaintenanceIssue() {
		boolean b = false;
		switch (this.getBaseMetaTileEntity().getRandomNumber(6)) {
		case 0 : {
			this.mWrench = false;
			b = true;
			break;
		}
		case 1 : {
			this.mScrewdriver = false;
			b = true;
			break;
		}
		case 2 : {
			this.mSoftHammer = false;
			b = true;
			break;
		}
		case 3 : {
			this.mHardHammer = false;
			b = true;
			break;
		}
		case 4 : {
			this.mSolderingTool = false;
			b = true;
			break;
		}
		case 5 : {
			this.mCrowbar = false;
			b = true;
			break;
		}
		}
		return b;
	}

	@Override
	public boolean addToMachineList(final IGregTechTileEntity aTileEntity,
			final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}

		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
			log("Found GT_MetaTileEntity_Hatch_InputBattery");
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mChargeHatches.add(
					(GT_MetaTileEntity_Hatch_InputBattery) aMetaTileEntity);
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
			log("Found GT_MetaTileEntity_Hatch_OutputBattery");
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mDischargeHatches.add(
					(GT_MetaTileEntity_Hatch_OutputBattery) aMetaTileEntity);
		}
		if (LoadedMods.TecTech){
			if (isThisHatchMultiDynamo(aMetaTileEntity)) {
				log("Found isThisHatchMultiDynamo");
				updateTexture(aTileEntity, aBaseCasingIndex);
				return this.mMultiDynamoHatches.add(
						(GT_MetaTileEntity_Hatch) aMetaTileEntity);
			}

		}
		return super.addToMachineList(aTileEntity, aBaseCasingIndex);
	}

	public boolean addChargeableToMachineList(final IGregTechTileEntity aTileEntity,
			final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBattery) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mChargeHatches.add(
					(GT_MetaTileEntity_Hatch_InputBattery) aMetaTileEntity);
		}
		return false;
	}

	public boolean addDischargeableInputToMachineList(
			final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBattery) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mDischargeHatches.add(
					(GT_MetaTileEntity_Hatch_OutputBattery) aMetaTileEntity);
		}
		return false;
	}


	public boolean addFluidInputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
			return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
		}
		return false;
	}

	public boolean addFluidOutputToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
		}
		return false;
	}
	
	public boolean resetRecipeMapForAllInputHatches() {
		return resetRecipeMapForAllInputHatches(this.getRecipeMap());
	}
	
	public boolean resetRecipeMapForAllInputHatches(GT_Recipe_Map aMap) {
		int cleared = 0;
		for (GT_MetaTileEntity_Hatch_Input g : this.mInputHatches) {
			if (resetRecipeMapForHatch(g, aMap)) {
				cleared++;
			}
		}
		for (GT_MetaTileEntity_Hatch_InputBus g : this.mInputBusses) {
			if (resetRecipeMapForHatch(g, aMap)) {
				cleared++;
			}
		}
		return cleared > 0;
	}
	public boolean resetRecipeMapForHatch(IGregTechTileEntity aTileEntity, GT_Recipe_Map aMap) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();;
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
			return resetRecipeMapForHatch((GT_MetaTileEntity_Hatch)aMetaTileEntity, aMap);
		}
		else {
			return false;
		}
	}
	
	public boolean resetRecipeMapForHatch(GT_MetaTileEntity_Hatch aTileEntity, GT_Recipe_Map aMap) {
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity;
		if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input || aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input){				
				((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;	
				((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = aMap;					
			}
			else {	
				((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = null;	
				((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity).mRecipeMap = aMap;				
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		resetRecipeMapForAllInputHatches();
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}
	

	/**
	 * Enable Texture Casing Support if found in GT 5.09
	 */

	@SuppressWarnings("deprecation")
	public boolean updateTexture(final IGregTechTileEntity aTileEntity, int aCasingID){
		try { //gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch.updateTexture(int)

			final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			}			
			Method mProper = Class.forName("gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch").getDeclaredMethod("updateTexture", int.class);
			if (mProper != null){
				if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)){
					mProper.setAccessible(true);
					mProper.invoke(aMetaTileEntity, aCasingID);
					log("Good Method Call for updateTexture.");
					return true;
				}

			}
			else {
				log("Bad Method Call for updateTexture.");
				if (GT_MetaTileEntity_Hatch.class.isInstance(aMetaTileEntity)){
					if (aCasingID <= Byte.MAX_VALUE) {
						((GT_MetaTileEntity_Hatch) aTileEntity.getMetaTileEntity()).mMachineBlock = (byte) aCasingID;
						log("Good Method Call for updateTexture. Used fallback method of setting mMachineBlock as casing id was <= 128.");
						return true;
					}
					else {
						log("updateTexture returning false. 1.2");
					}
				}
				else {
					log("updateTexture returning false. 1.3");
				}
			}
			log("updateTexture returning false. 1");
			return false;
		}
		catch (NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log("updateTexture returning false.");
			log("updateTexture returning false. 2");
			e.printStackTrace();
			return false;	
		}	

	}









	/**
	 * TecTech Support
	 */


	/**
	 * This is the array Used to Store the Tectech Multi-Amp hatches.
	 */

	public ArrayList<GT_MetaTileEntity_Hatch> mMultiDynamoHatches = new ArrayList<GT_MetaTileEntity_Hatch>();	

	/**
	 * TecTech Multi-Amp Dynamo Support
	 * @param aTileEntity - The Dynamo Hatch
	 * @param aBaseCasingIndex - Casing Texture
	 * @return
	 */

	public boolean addMultiAmpDynamoToMachineList(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex){
		//GT_MetaTileEntity_Hatch_DynamoMulti
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (isThisHatchMultiDynamo(aTileEntity)) {
			updateTexture(aTileEntity, aBaseCasingIndex);
			return this.mMultiDynamoHatches.add((GT_MetaTileEntity_Hatch) aMetaTileEntity);
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public boolean isThisHatchMultiDynamo(Object aMetaTileEntity){
		Class mDynamoClass;
		try {
			mDynamoClass = Class.forName("com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_DynamoMulti");
			if (mDynamoClass != null){
				if (mDynamoClass.isInstance(aMetaTileEntity)){
					return true;
				}
			}
		}
		catch (ClassNotFoundException e) {}
		return false;
	}

	@Override
	public boolean addDynamoToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (LoadedMods.TecTech){
			if (isThisHatchMultiDynamo(aTileEntity)) {
				addMultiAmpDynamoToMachineList(aTileEntity, aBaseCasingIndex);
			}

		}
		return super.addDynamoToMachineList(aTileEntity, aBaseCasingIndex);
	}


	/**
	 * Pollution Management
	 */

	public int getPollutionPerTick(ItemStack arg0) {
		return 0;
	}

	public String getPollutionTooltip() {
		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			return "Causes " + 20 * this.getPollutionPerTick(null) + " Pollution per second";
		}
		else {
			return "";
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setLong("mTotalRunTime", this.mTotalRunTime);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mTotalRunTime = aNBT.getLong("mTotalRunTime");
		super.loadNBTData(aNBT);
	}













	/**
	 * Custom Find Recipe with Debugging
	 */


	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated,
			final boolean aDontCheckStackSizes, final long aVoltage, final FluidStack[] aFluids,
			final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, null, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids,
				(ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated,
			final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, (ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final boolean aDontCheckStackSizes, final long aVoltage,
			final FluidStack[] aFluids, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids,
				(ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids,
			final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, (ItemStack) null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids,
			final ItemStack aSpecialSlot, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, true, aVoltage, aFluids, aSpecialSlot,
				aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe,
			final boolean aNotUnificated, final boolean aDontCheckStackSizes, final long aVoltage,
			final FluidStack[] aFluids, final ItemStack aSpecialSlot, ItemStack... aInputs) {
		if (this.getRecipeMap().mRecipeList.isEmpty()) {
			log("No Recipes in Map to search through.");
			return null;
		}
		GT_Recipe mRecipeResult = null;
		try {
			if (GregTech_API.sPostloadFinished) {
				if (this.getRecipeMap().mMinimalInputFluids > 0) {
					if (aFluids == null) {
						log("aFluids == null && minFluids > 0");
						return null;
					}
					int tAmount = 0;
					for (final FluidStack aFluid : aFluids) {
						if (aFluid != null) {
							++tAmount;
						}
					}
					if (tAmount < this.getRecipeMap().mMinimalInputFluids) {
						log("Not enough fluids?");
						return null;
					}
				}
				if (this.getRecipeMap().mMinimalInputItems > 0) {
					if (aInputs == null) {
						log("No inputs and minItems > 0");
						return null;
					}
					int tAmount = 0;
					for (final ItemStack aInput : aInputs) {
						if (aInput != null) {
							++tAmount;
						}
					}
					if (tAmount < this.getRecipeMap().mMinimalInputItems) {
						log("Not enough items?");
						return null;
					}
				}
			}
			else {
				log("Game Not Loaded properly for recipe lookup.");
			}
			if (aNotUnificated) {
				aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
			}
			if (aRecipe != null && !aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered
					&& aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
				mRecipeResult = (aRecipe.mEnabled/* && aVoltage * this.getRecipeMap().mAmperage >= aRecipe.mEUt*/) ? aRecipe : null;
				log("x) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
				if (mRecipeResult != null) {
					return mRecipeResult;
				}
			}
			if (mRecipeResult == null && this.getRecipeMap().mUsualInputCount >= 0 && aInputs != null && aInputs.length > 0) {
				for (final ItemStack tStack : aInputs) {
					if (tStack != null) {
						Collection<GT_Recipe> tRecipes = this.getRecipeMap().mRecipeItemMap.get(new GT_ItemStack(tStack));
						if (tRecipes != null) {
							for (final GT_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
									mRecipeResult = (tRecipe.mEnabled/* && aVoltage * this.getRecipeMap().mAmperage >= tRecipe.mEUt*/)
											? tRecipe
													: null;
									log("1) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
									//return mRecipeResult;
								}
							}
						}

						//TODO - Investigate if this requires to be in it's own block
						tRecipes = this.getRecipeMap().mRecipeItemMap
								.get(new GT_ItemStack(GT_Utility.copyMetaData(32767L, new Object[]{tStack})));
						if (tRecipes != null) {
							for (final GT_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe
										&& tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
									mRecipeResult = (tRecipe.mEnabled /*&& aVoltage * this.getRecipeMap().mAmperage >= tRecipe.mEUt*/)
											? tRecipe
													: null;
									log("2) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
									//return mRecipeResult;
								}
							}
						}
					}
				}
			}
			if (mRecipeResult == null && this.getRecipeMap().mMinimalInputItems == 0 && aFluids != null && aFluids.length > 0) {
				for (final FluidStack aFluid2 : aFluids) {
					if (aFluid2 != null) {
						final Collection<GT_Recipe> tRecipes = this.getRecipeMap().mRecipeFluidMap.get(aFluid2.getFluid());
						if (tRecipes != null) {
							for (final GT_Recipe tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe
										&& tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
									mRecipeResult = (tRecipe.mEnabled/* && aVoltage * this.getRecipeMap().mAmperage >= tRecipe.mEUt*/)
											? tRecipe
													: null;
									log("3) Found Recipe? "+(mRecipeResult != null ? "true" : "false"));
									//return mRecipeResult;
								}
							}
						}
					}
				}
			}
		}
		catch (Throwable t) {
			log("Invalid recipe lookup.");			
		}		
		if (mRecipeResult == null) {
			return this.getRecipeMap().findRecipe(aTileEntity, aRecipe,	aNotUnificated, aDontCheckStackSizes, aVoltage,	aFluids, aSpecialSlot, aInputs);
		}
		else {
			return mRecipeResult;
		}
	}



}
