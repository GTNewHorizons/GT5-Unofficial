package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.*;

import gregtech.api.interfaces.IIconContainer;
import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.*;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.*;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.*;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_MatterFab;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MatterFab;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_MassFabricator extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_MassFabricator> {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3200;
	
	public int mMatterProduced = 0;
	public int mScrapProduced = 0;
	public int mAmplifierProduced = 0;
	public int mScrapUsed = 0;
	public int mAmplifierUsed = 0;

	public static String mCasingName1 = "Matter Fabricator Casing";
	public static String mCasingName2 = "Containment Casing";
	public static String mCasingName3 = "Matter Generation Coil";
	
	private int mMode = 0;

	private final static int MODE_SCRAP = 1;
	private final static int MODE_UU = 0;

	public static boolean sRequiresUUA = false;
	private static FluidStack[] mUU = new FluidStack[2];
	private static ItemStack mScrap[] = new ItemStack[2];

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_MassFabricator> STRUCTURE_DEFINITION = null;

	public int getAmplifierUsed(){
		return this.mAmplifierUsed;
	}

	public int getMatterProduced(){
		return this.mMatterProduced;
	}

	public int getScrapProduced(){
		return this.mScrapProduced;
	}

	public GregtechMetaTileEntity_MassFabricator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_MassFabricator(final String aName) {
		super(aName);
	}

	@Override
	public String getMachineType() {
		return "Mass Fabricator / Recycler";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Matter Fabricator")
				.addInfo("Speed: 100% | Eu Usage: 80%")
				.addInfo("Parallel: Scrap = 64 | UU = 8 * Tier")
				.addInfo("Produces UU-A, UU-M & Scrap")
				.addInfo("Change mode with screwdriver")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(5, 4, 5, true)
				.addController("Front Center")
				.addCasingInfo(mCasingName3, 9)
				.addCasingInfo(mCasingName2, 24)
				.addCasingInfo(mCasingName1, 40)
				.addInputBus("Any Casing", 1)
				.addOutputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.addOutputHatch("Any Casing", 1)
				.addEnergyHatch("Any Casing", 1)
				.addMaintenanceHatch("Any Casing", 1)
				.addMufflerHatch("Any Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	@Override
	protected IIconContainer getActiveOverlay() {
		return TexturesGtBlock.Overlay_MatterFab_Active_Animated;
	}

	@Override
	protected IIconContainer getInactiveOverlay() {
		return TexturesGtBlock.Overlay_MatterFab_Animated;
	}

	@Override
	protected int getCasingTextureId() {
		return TAE.GTPP_INDEX(9);
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "MatterFabricator";
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MatterFab(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_MatterFab(aPlayerInventory, aBaseMetaTileEntity);
	}

	@Override
	public void onConfigLoad(final GT_Config aConfig) {
		super.onConfigLoad(aConfig);
		sDurationMultiplier = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUM_Duration_Multiplier", sDurationMultiplier);
		sUUAperUUM = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_per_UUM", sUUAperUUM);
		sUUASpeedBonus = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Speed_Bonus", sUUASpeedBonus);
		sRequiresUUA = aConfig.get(ConfigCategories.machineconfig, "Massfabricator.UUA_Requirement", sRequiresUUA);
		//Materials.UUAmplifier.mChemicalFormula = ("Mass Fabricator Eff/Speed Bonus: x" + sUUASpeedBonus);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		ArrayList<ItemStack> tItems = getStoredInputs();
		ArrayList<FluidStack> tFluids = getStoredFluids();
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);
		init();
		return checkRecipeGeneric(tItemInputs, tFluidInputs, 4, 80, 00, 100);
	}

	public static boolean sInit = false;
	
	public static void init() {
		if (!sInit) {
			if (mScrap[0] == null) {
				mScrap[0] = ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrap"));
			}	
			if (mScrap[1] == null) {
				mScrap[1] = ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrapbox"));
			}		
			if (mUU[0] == null) {
				mUU[0] = Materials.UUAmplifier.getFluid(100);
			}
			if (mUU[1] == null) {
				mUU[1] = Materials.UUMatter.getFluid(100);
			}
			sInit = true;
		}		
	}
	
	@Override
	public IStructureDefinition<GregtechMetaTileEntity_MassFabricator> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_MassFabricator>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC"},
							{"CGGGC", "G---G", "G---G", "G---G", "CGGGC"},
							{"CGGGC", "G---G", "G---G", "G---G", "CGGGC"},
							{"CC~CC", "CHHHC", "CHHHC", "CHHHC", "CCCCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_MassFabricator::addMassFabricatorList, TAE.GTPP_INDEX(9), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasingsMisc, 9
											)
									)
							)
					)
					.addElement(
							'H',
							ofBlock(
									ModBlocks.blockCasingsMisc, 8
							)
					)
					.addElement(
							'G',
							ofBlock(
									ModBlocks.blockCasings3Misc, 15
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 2, 3, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 2, 3, 0) && mCasing >= 40 && checkHatch();
	}

	public final boolean addMassFabricatorList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiMassFabricator;
	}

	@Override
	public int getAmountOfOutputs() {
		return 10;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_MassFabricator(this.mName);
	}

	/**
	 * Special Recipe Handling
	 */


	@Override
	public GT_Recipe_Map getRecipeMap() {
		return this.mMode == MODE_SCRAP ? GT_Recipe_Map.sRecyclerRecipes : GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes;
		//return Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes;
	}

	@Override
	public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes, int aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {
		if (this.mMode == MODE_SCRAP) {
			return checkRecipeScrap(aItemInputs, aFluidInputs, getMaxParallelRecipes(), getEuDiscountForParallelism(), aSpeedBonusPercent, aOutputChanceRoll);
		}
		else {
			return checkRecipeUU(aItemInputs, aFluidInputs, getMaxParallelRecipes(), getEuDiscountForParallelism(), aSpeedBonusPercent, aOutputChanceRoll);
		}
	}	
	
	public boolean checkRecipeScrap(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		long tEnergy = getMaxInputEnergy();
		ItemStack aPotentialOutput = GT_ModHandler.getRecyclerOutput(GT_Utility.copyAmount(1, aItemInputs[0]), 0);
		GT_Recipe tRecipe = new GTPP_Recipe(false, new ItemStack[]{GT_Utility.copyAmount(1, aItemInputs[0])}, aPotentialOutput == null ? null : new ItemStack[]{aPotentialOutput}, null, new int[]{2000}, null, null, 40, MaterialUtils.getVoltageForTier(1), 0);

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
			log("BAD RETURN - 2");
			return false;
		}

		int parallelRecipes = 0;
		// Count recipes to do in parallel, consuming input items and fluids and
		// considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				break;
			}
			log("Bumped EU from " + tTotalEUt + " to " + (tTotalEUt + tRecipeEUt) + ". ");
			tTotalEUt += tRecipeEUt;
		}
		log("Broke at " + parallelRecipes + ".");
		if (parallelRecipes > 0) {
			// -- Try not to fail after this point - inputs have already been
			// consumed! --

			// Convert speed bonus to duration multiplier
			// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe
			// duration.
			aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
			float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
			this.mMaxProgresstime = (int) (tRecipe.mDuration * tTimeFactor);
			this.mEUt = (int) Math.ceil(tTotalEUt);
			this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
			this.mEfficiencyIncrease = 10000;
			// Overclock
			if (this.mEUt <= 16) {
				this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
				this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
			}
			else {
				while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
					this.mEUt *= 4;
					this.mMaxProgresstime /= 4;
				}
			}
			if (this.mEUt > 0) {
				this.mEUt = (-this.mEUt);
			}
			this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
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
			for (ItemStack aOutputStack : tOutputItems) {
				if (aOutputStack != null) {
					mScrapProduced += aOutputStack.stackSize;
				}
			}
			// Sanitize item stack size, splitting any stacks greater than max
			// stack size
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
				if (tS.stackSize > 0)
					tSList.add(tS);
			}
			tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);
			// Commit outputs
			this.mOutputItems = tOutputItems;
			updateSlots();
			// Play sounds (GT++ addition - GT multiblocks play no sounds)
			startProcess();
			return true;
		}
		return false;
	}
	
	public boolean checkRecipeUU(
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
		long tEnergy = getMaxInputEnergy();
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
		
		aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
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
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, true, aFluidInputs, aItemInputs)) {
				break;
			}
			log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
			tTotalEUt += tRecipeEUt;
		}

		log("Broke at "+parallelRecipes+".");
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
				this.mMaxProgresstime /= 4;
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
		
		
		int aMatterProduced = 0;
		int aAmplifierProduced = 0;
		int aScrapUsed = 0;
		int aAmplifierUsed = 0;
		
		for (int i=0; i<parallelRecipes; i++) {
			//Logger.INFO("Trying to bump stats "+i);
			for (ItemStack aInput : tRecipe.mInputs) {
				if (aInput != null && GT_Utility.areStacksEqual(aInput, mScrap[0], true)) {
					aScrapUsed += aInput.stackSize;
					//Logger.INFO("Found Scrap to use.");
				}
			}
			for (FluidStack aInput : tRecipe.mFluidInputs) {
				if (aInput != null && GT_Utility.areFluidsEqual(aInput, mUU[0], true)) {
					aAmplifierUsed += aInput.amount;
					//Logger.INFO("Found UU-A to use.");
				}
			}
			for (FluidStack aOutput : tRecipe.mFluidOutputs) {
				if (aOutput != null && GT_Utility.areFluidsEqual(aOutput, mUU[0], true)) {
					aAmplifierProduced += aOutput.amount;
					//Logger.INFO("Found UU-A as Output.");
				}
				if (aOutput != null && GT_Utility.areFluidsEqual(aOutput, mUU[1], true)) {
					aMatterProduced += aOutput.amount;
					//Logger.INFO("Found UU-M as Output.");
				}
			}
		}

		this.mMatterProduced += aMatterProduced;
		this.mAmplifierProduced += aAmplifierProduced;
		this.mScrapUsed += aScrapUsed;
		this.mAmplifierUsed += aAmplifierUsed;

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
	
	@Override
	public int getMaxParallelRecipes() {
		return this.mMode == MODE_SCRAP ? 64 : 8 * (Math.max(1, GT_Utility.getTier(getMaxInputVoltage())));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 80;
	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		int aMode = this.mMode + 1;
		if (aMode > 1) {
			this.mMode = MODE_UU;
			PlayerUtils.messagePlayer(aPlayer, "Mode ["+this.mMode+"]: Matter/AmpliFabricator");
		}
		else if (aMode == 1) {
			this.mMode = MODE_SCRAP;
			PlayerUtils.messagePlayer(aPlayer, "Mode ["+this.mMode+"]: Recycler");
		}
		else {
			this.mMode = MODE_SCRAP;
			PlayerUtils.messagePlayer(aPlayer, "Mode ["+this.mMode+"]: Recycler");
		}
		GT_Recipe_Map r = this.getRecipeMap();
		final Collection<GT_Recipe> x = r.mRecipeList;
		Logger.INFO("Dumping " + r.mUnlocalizedName + " Recipes for Debug. size: "+x.size());
		for (final GT_Recipe newBo : x) {
			Logger.INFO("========================");
			Logger.INFO("Dumping Input: " + ItemUtils.getArrayStackNames(newBo.mInputs));
			Logger.INFO("Dumping Inputs " + ItemUtils.getFluidArrayStackNames(newBo.mFluidInputs));
			Logger.INFO("Dumping Duration: " + newBo.mDuration);
			Logger.INFO("Dumping EU/t: " + newBo.mEUt);
			Logger.INFO("Dumping Output: " + ItemUtils.getArrayStackNames(newBo.mOutputs));
			Logger.INFO("Dumping Output: " + ItemUtils.getFluidArrayStackNames(newBo.mFluidOutputs));
			Logger.INFO("========================");
		}
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mScrapProduced", mScrapProduced);
		aNBT.setInteger("mAmplifierProduced", mAmplifierProduced);
		aNBT.setInteger("mMatterProduced", mMatterProduced);
		aNBT.setInteger("mScrapUsed", mScrapUsed);
		aNBT.setInteger("mAmplifierUsed", mAmplifierUsed);
		aNBT.setInteger("mMode", mMode);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		mScrapProduced = aNBT.getInteger("mScrapProduced");
		mAmplifierProduced = aNBT.getInteger("mAmplifierProduced");
		mMatterProduced = aNBT.getInteger("mMatterProduced");
		mScrapUsed = aNBT.getInteger("mScrapUsed");
		mAmplifierUsed = aNBT.getInteger("mAmplifierUsed");
		mMode = aNBT.getInteger("mMode");
		super.loadNBTData(aNBT);
	}

}