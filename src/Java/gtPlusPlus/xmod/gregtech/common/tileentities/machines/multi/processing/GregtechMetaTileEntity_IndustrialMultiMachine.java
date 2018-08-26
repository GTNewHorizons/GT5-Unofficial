package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialMultiMachine
extends GregtechMeta_MultiBlockBase {

	protected int mInternalMode = 0;
	protected GT_Recipe[] mLastRecipeExtended = new GT_Recipe[9];
	private static final int MODE_COMPRESSOR = 0;
	private static final int MODE_LATHE = 1;
	private static final int MODE_MAGNETIC = 2;
	private static final int MODE_FERMENTER = 3;
	private static final int MODE_FLUIDEXTRACT = 4;
	private static final int MODE_EXTRACTOR = 5;
	private static final int MODE_LASER = 6;
	private static final int MODE_AUTOCLAVE = 7;
	private static final int MODE_REPLICATOR = 8;
	private static final int[][] MODE_MAP = new int[][] {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}};


	public GregtechMetaTileEntity_IndustrialMultiMachine(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialMultiMachine(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialMultiMachine(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Nine in One";
	}

	@Override
	public String[] getDescription() {
		return new String[]{"Controller Block for the Industrial Multi-Machine",
				"250% faster than using single block machines of the same voltage",
				"Only uses 80% of the eu/t normally required",
				"Processes two items per voltage tier",
				"Size: 3x3x3 (Hollow)",
				"Controller (front centered)",
				"6 Multi-Use casings required (Minimum)",
				"Read Multi-Machine Manual for extra information",
				getPollutionTooltip(),
				getMachineTooltip(),
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getTextureIndex()], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[getTextureIndex()]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "VacuumFreezer";
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		ArrayList<FluidStack> tFluids = getStoredFluids();	
		//Logger.MACHINE_INFO("1");
		for (GT_MetaTileEntity_Hatch_InputBus tBus : mInputBusses) {
			ArrayList<ItemStack> tBusItems = new ArrayList<ItemStack>();
			tBus.mRecipeMap = getRecipeMap();
			//Logger.MACHINE_INFO("2");
			if (isValidMetaTileEntity(tBus)) {
				//Logger.MACHINE_INFO("3");
				for (int i = tBus.getBaseMetaTileEntity().getSizeInventory() - 1; i >= 0; i--) {
					if (tBus.getBaseMetaTileEntity().getStackInSlot(i) != null)
						tBusItems.add(tBus.getBaseMetaTileEntity().getStackInSlot(i));
				}
			}

			Object[] tempArray = tFluids.toArray(new FluidStack[] {});
			FluidStack[] properArray;
			properArray = ((tempArray != null && tempArray.length > 0) ? (FluidStack[]) tempArray : new FluidStack[] {});

			//Logger.MACHINE_INFO("4");
			if (checkRecipeGeneric(tBusItems.toArray(new ItemStack[]{}), properArray,
					(2*GT_Utility.getTier(this.getMaxInputVoltage())), 80, 250, 10000)) return true;
		}
		return false;
		
		//return checkRecipeGeneric(2*GT_Utility.getTier(this.getMaxInputVoltage()), 90, 180);
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		}
		int tAmount = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = -1; h < 2; h++) {
					if ((h != 0) || ((((xDir + i) != 0) || ((zDir + j) != 0)) && ((i != 0) || (j != 0)))) {
						final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if (!this.addToMachineList(tTileEntity, getTextureIndex())) {
							final Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							final byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
							if (((tBlock != ModBlocks.blockCasings3Misc) || (tMeta != 2))) {
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		return tAmount >= 6 && this.mMufflerHatches.size() > 0;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {		
		if (mInternalMode == 0) {
			return 20;
		}
		else if (mInternalMode == 1) {
			return 20;
		}
		else if (mInternalMode == 2) {
			return 30;
		}
		else {
			return 50;
		}	
	}

	public int getTextureIndex() {
		return TAE.getIndexFromPage(2, 2);
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	private ItemStack getCircuit(ItemStack[] t) {
		for (ItemStack j : t) {
			if (j.getItem() == CI.getNumberedCircuit(0).getItem()) {
				if (j.getItemDamage() >= 20 && j.getItemDamage() <= 22) {
					return j;
				}
			}
		}
		return null;
	}

	private final int getCircuitID(ItemStack circuit) {
		int H = circuit.getItemDamage();
		int T = (H == 20 ? 0 : (H == 21 ? 1 : (H == 22 ? 2 : -1)));
		return MODE_MAP[this.mInternalMode][T];
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	private final GT_Recipe.GT_Recipe_Map getRecipeMap(ItemStack circuit) {
		return getRecipeMap(getCircuitID(circuit));
	}

	private static final GT_Recipe.GT_Recipe_Map getRecipeMap(int aMode) {
		if (aMode == MODE_COMPRESSOR) {
			return GT_Recipe.GT_Recipe_Map.sCompressorRecipes;
		}
		else if (aMode == MODE_LATHE) {
			return GT_Recipe.GT_Recipe_Map.sLatheRecipes;			
		}
		else if (aMode == MODE_MAGNETIC) {
			return GT_Recipe.GT_Recipe_Map.sPolarizerRecipes;			
		}
		else if (aMode == MODE_FERMENTER) {
			return GT_Recipe.GT_Recipe_Map.sFermentingRecipes;			
		}
		else if (aMode == MODE_FLUIDEXTRACT) {
			return GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes;			
		}
		else if (aMode == MODE_EXTRACTOR) {
			return GT_Recipe.GT_Recipe_Map.sExtractorRecipes;			
		}
		else if (aMode == MODE_LASER) {
			return GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes;			
		}
		else if (aMode == MODE_AUTOCLAVE) {
			return GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes;			
		}
		else if (aMode == MODE_REPLICATOR) {
			return null;
		}
		else {
			return null;
		}
	}

	private final AutoMap<ItemStack> mReplicatorOutputMap = new AutoMap<ItemStack>();

	@Override
	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {
		
		// Based on the Processing Array. A bit overkill, but very flexible.

		// Get Circuit info for this recipe.
		ItemStack tCircuit = getCircuit(aItemInputs);
		int tCircuitID = getCircuitID(tCircuit);
		
		Logger.MACHINE_INFO("Mode: "+tCircuitID);

		// Time to Defer to Special Handling if it's in replicator mode.
		if (tCircuitID == MODE_REPLICATOR) {
		    return false;
		//	return checkReplicatorRecipe(aItemInputs, aFluidInputs, aMaxParallelRecipes, aEUPercent, aSpeedBonusPercent, aOutputChanceRoll);
		}

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};



		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));



		GT_Recipe tRecipe = this.getRecipeMap(tCircuit).findRecipe(
				getBaseMetaTileEntity(), this.mLastRecipeExtended[tCircuitID], false,
				gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);

		// Remember last recipe - an optimization for findRecipe()
		//this.mLastRecipe = tRecipe; //Let's not do this, it's bad.
		//Instead, how about I use a array for types?
		this.mLastRecipeExtended[tCircuitID] = tRecipe;

		if (tRecipe == null) {
			Logger.MACHINE_INFO("BAD RETURN - 1|"+tCircuitID);
			
			if (aItemInputs.length > 0) {
				Logger.MACHINE_INFO("Input Items: "+ItemUtils.getArrayStackNames(aItemInputs));
			}
			if (aFluidInputs.length > 0) {
				Logger.MACHINE_INFO("Input Fluids: "+ItemUtils.getFluidArrayStackNames(aFluidInputs));
			}
			return false;
		}

		if (!this.canBufferOutputs(tRecipe, aMaxParallelRecipes)) {
			Logger.MACHINE_INFO("BAD RETURN - 2|"+tCircuitID);
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;

		Logger.WARNING("parallelRecipes: "+parallelRecipes);
		Logger.WARNING("aMaxParallelRecipes: "+aMaxParallelRecipes);
		Logger.WARNING("tTotalEUt: "+tTotalEUt);
		Logger.WARNING("tVoltage: "+tVoltage);
		Logger.WARNING("tRecipeEUt: "+tRecipeEUt);
		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				Logger.WARNING("Broke at "+parallelRecipes+".");
				break;
			}
			Logger.WARNING("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			Logger.MACHINE_INFO("BAD RETURN - 3|"+tCircuitID);
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

		Logger.MACHINE_INFO("GOOD RETURN - 1|"+tCircuitID);
		return true;
	}

	private FluidStack mReplicatorFluidOutput;
	//Replicator handling
	public boolean checkReplicatorRecipe(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {

		// Based on the Processing Array. A bit overkill, but very flexible.
		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};
		this.mReplicatorOutputMap.clear();

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		if (canBufferOutputs(aItemInputs, aMaxParallelRecipes)) {
			Logger.WARNING("BAD RETURN - 2.0");
			return false;
		}

		ItemStack tDataOrb = null;
		ItemStack tCellStack = null;
		ItemStack tReplicatedItem;
		FluidStack tOutputFluid = null;
		FluidStack tInputFluid = null;
		final Materials tMaterial = Element.get(Behaviour_DataOrb.getDataName(tDataOrb)).mLinkedMaterials.get(0);
		final long tMass = tMaterial.getMass();	

		// Find First Data Orb with Scan Data
		for (ItemStack I : aItemInputs) {
			if (ItemList.Tool_DataOrb.isStackEqual((Object) I, false, true) && Behaviour_DataOrb.getDataTitle(I).equals("Elemental-Scan")) {
				tDataOrb = I.copy();
				break;
			}
		}

		// Find First empty cell stack
		for (ItemStack I : aItemInputs) {
			if (ItemList.Cell_Empty.isStackEqual((Object) I)) {
				tCellStack = I.copy();
				break;
			}
		}

		// Find UUM
		for (FluidStack F : aFluidInputs) {
			if (F != null && F.isFluidEqual(Materials.UUMatter.getFluid(1L))) {
				final FluidStack tFluid = F;
				if (tFluid.amount >= tMass && tMass > 0L) {
					tInputFluid = tFluid;
				}
			}
		}

		// No Data Orb or UUM found?
		if (tDataOrb == null || tInputFluid == null) {
			return false;
		}	
		// Temp Values
		int tEUt = (int) GT_Values.V[(int) this.getInputTier()];
		int tMaxProgresstime = (int) (tMass * 512L / (1 << tTier - 1));	
		float tRecipeEUt = (tEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;
		int parallelRecipes = 0;
		ItemStack[] expectedInputs = {tDataOrb};
		FluidStack[] expectedFluidInputs = {Materials.UUMatter.getFluid(tMass)};	

		/**
		 * Magic
		 */

		int COST_UUM = 0;
		int COST_CELLS = 0;

		// Determine Output Item & Cost.
		if ((tReplicatedItem = GT_OreDictUnificator.get(OrePrefixes.dust, (Object) tMaterial, 1L)) == null) {
			if ((tReplicatedItem = GT_OreDictUnificator.get(OrePrefixes.cell, (Object) tMaterial, 1L)) != null) {
				if ((tOutputFluid = GT_Utility.getFluidForFilledItem(tReplicatedItem, true)) == null) {
					if (ItemList.Cell_Empty.isStackEqual((Object) tCellStack) && this.canBufferOutputs(new ItemStack[]{tReplicatedItem}, 1)) {
						COST_CELLS = 1;
						COST_UUM = (int) tMass;
					}					
				} else {
					tReplicatedItem = null;
					if (this.getDrainableStack() == null || (this.getDrainableStack().isFluidEqual(tOutputFluid) && this.getDrainableStack().amount < 16000)) {
						COST_UUM = (int) tMass;
					}
				}
			}
		} else if (this.canBufferOutputs(new ItemStack[]{tReplicatedItem}, 1)) {
			COST_UUM = (int) tMass;
		}

		// Costs no UUM and no valid outputs? Let's bail gracefully before we consume inputs.
		if (COST_UUM <= 0 || (tReplicatedItem == null && tOutputFluid == null)) {
			return false;
		}

		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) {
			if (!isRecipeInputEqual(true, aFluidInputs, aItemInputs, expectedFluidInputs, expectedInputs)) {
				Logger.WARNING("Broke at "+parallelRecipes+"..0");
				break;
			}
			Logger.WARNING("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+"..0");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			Logger.WARNING("BAD RETURN - 3.0");
			return false;
		}

		// Set Vars to Parralel amount
		COST_CELLS *= parallelRecipes;

		// Requires a cell? Ok, let's use some.
		if (COST_CELLS > 0) {
			this.depleteInput(ItemUtils.getEmptyCell(COST_CELLS));
		}

		// Build an output map, for simplicity.
		for (int r=0;r<parallelRecipes;r++) {
			this.mReplicatorOutputMap.put(ItemUtils.getSimpleStack(tReplicatedItem, 1));
		}

		// -- Try not to fail after this point - inputs have already been consumed! --
		ItemStack[] mBuiltOutput = this.mReplicatorOutputMap.toArray();

		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
		float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
		this.mMaxProgresstime = (int)(tMaxProgresstime * tTimeFactor);

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

		// Collect output item types
		ItemStack[] tOutputItems = new ItemStack[1];
		for (int h = 0; h < 1; h++) {
			if (mBuiltOutput[h] != null) {
				tOutputItems[h] = mBuiltOutput[h].copy();
				tOutputItems[h].stackSize = 0;
			}
		}

		// Set output item stack sizes (taking output chance into account)
		for (int f = 0; f < tOutputItems.length; f++) {
			if (mBuiltOutput[f] != null && tOutputItems[f] != null) {
				for (int g = 0; g < parallelRecipes; g++) {
					tOutputItems[f].stackSize += mBuiltOutput[f].stackSize;
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
		//this.mOutputFluids = tOutputFluids;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		Logger.WARNING("GOOD RETURN - 1.0");
		return true;
	}


	//Special Space Checking
	private boolean canBufferOutputs(ItemStack[] aInputs, int aParallelRecipes) {
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

		int slotsNeeded = aInputs.length;
		for (final ItemStack tRecipeOutput: aInputs) {
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
		return true;

	}

	public FluidStack getDrainableStack() {
		return this.mReplicatorFluidOutput;
	}

	public FluidStack setDrainableStack(final FluidStack aFluid) {
		return this.mReplicatorFluidOutput = aFluid;
	}

	public boolean isRecipeInputEqual(final boolean aDecreaseStacksizeBySuccess, 
			final FluidStack[] aFluidInputs,
			final ItemStack[] aInputs,
			final FluidStack[] mFluidInputs,
			final ItemStack[] mInputs) {
		return this.isRecipeInputEqual(aDecreaseStacksizeBySuccess, false, aFluidInputs, aInputs, mFluidInputs, mInputs);
	}

	public boolean isRecipeInputEqual(
			final boolean aDecreaseStacksizeBySuccess,
			final boolean aDontCheckStackSizes,
			final FluidStack[] aFluidInputs,
			final ItemStack[] aInputs,
			final FluidStack[] aExpectedFluidInputs,
			final ItemStack[] mExpectedInputs) {
		if (aExpectedFluidInputs.length > 0 && aFluidInputs == null) {
			return false;
		}
		for (final FluidStack tFluid : aExpectedFluidInputs) {
			if (tFluid != null) {
				boolean temp = true;
				int amt = tFluid.amount;
				for (final FluidStack aFluid : aFluidInputs) {
					if (aFluid != null && aFluid.isFluidEqual(tFluid)) {
						if (aDontCheckStackSizes) {
							temp = false;
							break;
						}
						amt -= aFluid.amount;
						if (amt < 1) {
							temp = false;
							break;
						}
					}
				}
				if (temp) {
					return false;
				}
			}
		}
		if (mExpectedInputs.length > 0 && aInputs == null) {
			return false;
		}
		for (final ItemStack tStack : mExpectedInputs) {
			if (tStack != null) {
				int amt = tStack.stackSize;
				boolean temp = true;
				for (final ItemStack aStack : aInputs) {
					if (GT_Utility.areUnificationsEqual(aStack, tStack, true)
							|| GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) {
						if (aDontCheckStackSizes) {
							temp = false;
							break;
						}
						amt -= aStack.stackSize;
						if (amt < 1) {
							temp = false;
							break;
						}
					}
				}
				if (temp) {
					return false;
				}
			}
		}
		if (aDecreaseStacksizeBySuccess) {
			if (aFluidInputs != null) {
				for (final FluidStack tFluid : aExpectedFluidInputs) {
					if (tFluid != null) {
						int amt = tFluid.amount;
						for (final FluidStack aFluid2 : aFluidInputs) {
							if (aFluid2 != null && aFluid2.isFluidEqual(tFluid)) {
								if (aDontCheckStackSizes) {
									final FluidStack fluidStack = aFluid2;
									fluidStack.amount -= amt;
									break;
								}
								if (aFluid2.amount >= amt) {
									final FluidStack fluidStack2 = aFluid2;
									fluidStack2.amount -= amt;
									amt = 0;
									break;
								}
								amt -= aFluid2.amount;
								aFluid2.amount = 0;
							}
						}
					}
				}
			}
			if (aInputs != null) {
				for (final ItemStack tStack : mExpectedInputs) {
					if (tStack != null) {
						int amt = tStack.stackSize;
						for (final ItemStack aStack2 : aInputs) {
							if (GT_Utility.areUnificationsEqual(aStack2, tStack, true) || GT_Utility
									.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack2), tStack, true)) {
								if (aDontCheckStackSizes) {
									final ItemStack itemStack = aStack2;
									itemStack.stackSize -= amt;
									break;
								}
								if (aStack2.stackSize >= amt) {
									final ItemStack itemStack2 = aStack2;
									itemStack2.stackSize -= amt;
									amt = 0;
									break;
								}
								amt -= aStack2.stackSize;
								aStack2.stackSize = 0;
							}
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (mInternalMode < 2) {
			mInternalMode++;
		}
		else {
			mInternalMode = 0;
		}
		String mModeString = (mInternalMode == 0 ? "Metal" : mInternalMode == 1 ? "Fluid" : mInternalMode == 2 ? "Misc." : "null");
		PlayerUtils.messagePlayer(aPlayer, "Multi-Machine is now in "+mModeString+" mode.");
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mInternalMode", mInternalMode);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mInternalMode = aNBT.getInteger("mInternalMode");
		super.loadNBTData(aNBT);
	}


}