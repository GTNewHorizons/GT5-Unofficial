package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.core.lib.CORE;
import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialDehydrator extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_IndustrialDehydrator> {
	
	private static int CASING_TEXTURE_ID;
	private static String mCasingName = "Vacuum Casing";	
	private HeatingCoilLevel mHeatingCapacity;
	private boolean mDehydratorMode = false;
	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_IndustrialDehydrator> STRUCTURE_DEFINITION = null;

	public GregtechMetaTileEntity_IndustrialDehydrator(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
	}

	public GregtechMetaTileEntity_IndustrialDehydrator(String aName) {
		super(aName);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(3, 10);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialDehydrator(mName);
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Factory Grade Vacuum Furnace")
				.addInfo("Can toggle the operation temperature with a Screwdriver")
				.addInfo("All Dehydrator recipes are Low Temp recipes")
				.addInfo("Speed: 120% | Eu Usage: 50% | Parallel: 4")
				.addInfo("Each 900K over the min. Heat Capacity grants 5% speedup (multiplicatively)")
				.addInfo("Each 1800K over the min. Heat Capacity allows for one upgraded overclock")
				.addInfo("Upgraded overclocks reduce recipe time to 25% and increase EU/t to 400%")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(3, 5, 3, true)
				.addController("Bottom Center")
				.addCasingInfo(mCasingName, 10)
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
	public IStructureDefinition<GregtechMetaTileEntity_IndustrialDehydrator> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_IndustrialDehydrator>builder()
					.addShape(mName, transpose(new String[][]{
							{"CCC", "CCC", "CCC"},
							{"HHH", "H-H", "HHH"},
							{"HHH", "H-H", "HHH"},
							{"HHH", "H-H", "HHH"},
							{"C~C", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMetaTileEntity_IndustrialDehydrator::addIndustrialDehydratorList, CASING_TEXTURE_ID, 1
									),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings4Misc, 10
											)
									)
							)
					)
					.addElement(
							'H',
							ofCoil(
									GregtechMetaTileEntity_IndustrialDehydrator::setCoilLevel, GregtechMetaTileEntity_IndustrialDehydrator::getCoilLevel
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 1, 4, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		setCoilLevel(HeatingCoilLevel.None);
		return checkPiece(mName, 1, 4, 0) && mCasing >= 10 && getCoilLevel() != HeatingCoilLevel.None && checkHatch();
	}

	public final boolean addIndustrialDehydratorList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
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

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active
							: TexturesGtBlock.Overlay_Machine_Controller_Advanced) };
		}
		return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID) };
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return mDehydratorMode ? GTPP_Recipe.GTPP_Recipe_Map.sChemicalDehydratorRecipes : GTPP_Recipe.GTPP_Recipe_Map.sVacuumFurnaceRecipes;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerSecond(ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiIndustrialDehydrator;
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getMachineType() {
		return "Vacuum Furnace / Dehydrator";
	}

	@Override
	public int getMaxParallelRecipes() {
		return 4;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 50;
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "ElectricBlastFurnace";
	}

	public boolean checkRecipe(ItemStack aStack) {
		return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 120);
	}

	@Override
	public boolean checkRecipeGeneric(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, int aMaxParallelRecipes,
			int aEUPercent, int aSpeedBonusPercent, int aOutputChanceRoll) {
		// Based on the Processing Array. A bit overkill, but very flexible.

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[] {};
		this.mOutputFluids = new FluidStack[] {};

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
		long tEnergy = getMaxInputEnergy();
		Logger.WARNING("Running checkRecipeGeneric(0)");

		GT_Recipe tRecipe = this.getRecipeMap().findRecipe(getBaseMetaTileEntity(), mLastRecipe, false,
				gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);

		Logger.WARNING("Running checkRecipeGeneric(1)");
		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;

		if (tRecipe == null || this.mHeatingCapacity.getHeat() < tRecipe.mSpecialValue) {
			Logger.WARNING("BAD RETURN - 1");
			return false;
		}

		aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
			Logger.WARNING("BAD RETURN - 2");
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		int tHeatCapacityDivTiers = (int) (mHeatingCapacity.getHeat() - tRecipe.mSpecialValue) / 900;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;
		// Count recipes to do in parallel, consuming input items and fluids and
		// considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				Logger.WARNING("Broke at " + parallelRecipes + ".");
				break;
			}
			Logger.WARNING("Bumped EU from " + tTotalEUt + " to " + (tTotalEUt + tRecipeEUt) + ".");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			Logger.WARNING("BAD RETURN - 3");
			return false;
		}

		// -- Try not to fail after this point - inputs have already been consumed! --

		// Convert speed bonus to duration multiplier
		// e.g. 100% speed bonus = 200% speed = 100%/200% = 50% recipe duration.
		aSpeedBonusPercent = Math.max(-99, aSpeedBonusPercent);
		float tTimeFactor = 100.0f / (100.0f + aSpeedBonusPercent);
		this.mMaxProgresstime = (int) (tRecipe.mDuration * tTimeFactor);
		int rInt = 2;

		this.mEUt = (int) Math.ceil(tTotalEUt);

		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;

		// Overclock
		if (this.mEUt <= 16) {
			this.mEUt = (this.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
			this.mMaxProgresstime = (this.mMaxProgresstime / (1 << tTier - 1));
		} else {
			while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
				this.mEUt *= 4;
				this.mMaxProgresstime /= (tHeatCapacityDivTiers >= rInt ? 4 : 2);
			}
		}

		if (tHeatCapacityDivTiers > 0) {
			this.mEUt = (int) (this.mEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
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
			if (tS.stackSize > 0)
				tSList.add(tS);
		}
		tOutputItems = tSList.toArray(new ItemStack[tSList.size()]);

		// Commit outputs
		this.mOutputItems = tOutputItems;
		this.mOutputFluids = tOutputFluids;
		updateSlots();

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		Logger.WARNING("GOOD RETURN - 1");
		return true;

	}

	@Override
	public void onModeChangeByScrewdriver(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		mDehydratorMode = Utils.invertBoolean(mDehydratorMode);		
		String aMode = mDehydratorMode ? "Dehydrator" : "Vacuum Furnace";		
		PlayerUtils.messagePlayer(aPlayer, "Mode: "+aMode);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		aNBT.setBoolean("mDehydratorMode", mDehydratorMode);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mDehydratorMode = aNBT.getBoolean("mDehydratorMode");
	}

	public HeatingCoilLevel getCoilLevel() {
		return mHeatingCapacity;
	}

	public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
		mHeatingCapacity = aCoilLevel;
	}
}


