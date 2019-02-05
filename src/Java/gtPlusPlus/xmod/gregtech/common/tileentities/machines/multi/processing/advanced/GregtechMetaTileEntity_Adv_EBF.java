package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_Adv_EBF extends GregtechMeta_MultiBlockBase {

	public static int CASING_TEXTURE_ID;
	public static String mHotFuelName = "Blazing Pyrotheum";
	public static String mCasingName = "Advanced Blast Furnace Casing";
	public static String mHatchName = "Pyrotheum Hatch";

	private int mHeatingCapacity = 0;
	private int controllerY;

	private static boolean mUsingPollutionOutputs = false;
	private static AutoMap<FluidStack> mPollutionFluidStacks = new AutoMap<FluidStack>();

	private static boolean setPollutionFluids() {
		FluidStack CD, CM, SD;
		CD = FluidUtils.getFluidStack("carbondioxide", 1000);
		CM = FluidUtils.getFluidStack("carbonmonoxide", 1000);
		SD = FluidUtils.getFluidStack("sulfuredioxide", 1000);
		if (mPollutionFluidStacks.size() == 0) {
			if (CD != null)
				mPollutionFluidStacks.put(CD);
			if (CM != null)
				mPollutionFluidStacks.put(CM);
			if (SD != null)
				mPollutionFluidStacks.put(SD);
		}
		if (mPollutionFluidStacks.size() > 0) {
			return true;
		}
		return false;
	}

	public GregtechMetaTileEntity_Adv_EBF(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 11);
		mHotFuelName = FluidUtils.getFluidStack("pyrotheum", 1).getLocalizedName();
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 11);
		mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 968);
		mUsingPollutionOutputs = setPollutionFluids();
	}

	public GregtechMetaTileEntity_Adv_EBF(String aName) {
		super(aName);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 11);
		mHotFuelName = FluidUtils.getFluidStack("pyrotheum", 1).getLocalizedName();
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 11);
		mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 968);
		mUsingPollutionOutputs = setPollutionFluids();
	}

	@Override
	public String getMachineType() {
		return "Blast Furnace";
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Adv_EBF(this.mName);
	}

	public String[] getTooltip() {

		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 11);
		}
		if (mHotFuelName.toLowerCase().contains(".")) {
			mHotFuelName = FluidUtils.getFluidStack("pyrotheum", 1).getLocalizedName();
		}
		if (mHatchName.toLowerCase().contains(".name")) {
			mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 968);
		}

		return new String[] { "Controller Block for the Advanced Electric Blast Furnace",
				"120% faster than using an equal tier EBF", "Only uses 90% of the eu/t normally required",
				"Processes upto 8 recipes at once", 
				"Consumes 10L of " + mHotFuelName + "/s during operation",
				"Each 900K over the min. Heat Capacity grants 5% speedup (multiplicatively)",
				"Each 1800K over the min. Heat Capacity allows for one upgraded overclock",
				"Upgraded overclocks reduce recipe time to 25% and increase EU/t to 400%",
				"Size(WxHxD): 3x4x3 (Hollow), Controller (Front middle bottom)",
				"16x Heating Coils (Two middle Layers, hollow)", 
				"1x " + mHatchName,
				"1x Input Hatch/Bus", 
				"1x Output Hatch/Bus (Bottom Layer)",
				"1x Output Hatch to recover CO2/CO/SO2 (optional, any top layer casing),",
				"    Recovery scales with Muffler Hatch tier", mCasingName + "s for the rest",
				"1x Energy Hatch",
		};
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
			boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced) };
		}
		return new ITexture[] { Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID] };
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "ElectricBlastFurnace";
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sBlastRecipes;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	public boolean checkRecipe(ItemStack aStack) {
		return checkRecipeGeneric(8, 90, 120); // Will have to clone the logic from parent class to handle heating coil
		// tiers.
	}

	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		controllerY = aBaseMetaTileEntity.getYCoord();
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

		this.mHeatingCapacity = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 2, zDir)) {
			return false;
		}
		/*if (!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir),
				CASING_TEXTURE_ID)) {
			return false;
		}*/
		byte tUsedMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, 2, zDir);

		if (!CORE.GTNH) {
			switch (tUsedMeta) {
			case 0:
				this.mHeatingCapacity = 1800;
				break;
			case 1:
				this.mHeatingCapacity = 2700;
				break;
			case 2:
				this.mHeatingCapacity = 3600;
				break;
			case 3:
				this.mHeatingCapacity = 4500;
				break;
			case 4:
				this.mHeatingCapacity = 5400;
				break;
			case 5:
				this.mHeatingCapacity = 7200;
				break;
			case 6:
				this.mHeatingCapacity = 9001;
				break;
			default:Logger.INFO("Heating Coils are bad.");
				return false;
			}
		} else {
			switch (tUsedMeta) {
			case 0:
				this.mHeatingCapacity = 1801;
				break;
			case 1:
				this.mHeatingCapacity = 2701;
				break;
			case 2:
				this.mHeatingCapacity = 3601;
				break;
			case 3:
				this.mHeatingCapacity = 4501;
				break;
			case 4:
				this.mHeatingCapacity = 5401;
				break;
			case 5:
				this.mHeatingCapacity = 7201;
				break;
			case 6:
				this.mHeatingCapacity = 9001;
				break;
			case 7:
				this.mHeatingCapacity = 12001;
				break;
			case 8:
				this.mHeatingCapacity = 15001;
				break;
			default:
				Logger.INFO("Heating Coils are bad.");
				return false;
			}
		}

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((i != 0) || (j != 0)) {					
					//Coils 1
					if (!isValidBlockForStructure(null, CASING_TEXTURE_ID, false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j), StaticFields59.getBlockCasings5(), tUsedMeta)) {
						Logger.INFO("Heating Coils missing.");
						return false;
					}
					
					//Coils 2
					if (!isValidBlockForStructure(null, CASING_TEXTURE_ID, false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j), StaticFields59.getBlockCasings5(), tUsedMeta)) {
						Logger.INFO("Heating Coils missing.");
						return false;
					}					
				}
				
				//Top Layer
				final IGregTechTileEntity tTileEntity2 = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 3, zDir + j);					
				if (!isValidBlockForStructure(tTileEntity2, CASING_TEXTURE_ID, true, aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j), ModBlocks.blockCasings3Misc, 11)) {
					Logger.INFO("Top Layer missing.");
					return false;
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((xDir + i != 0) || (zDir + j != 0)) {
					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0,zDir + j);					
					if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, true, aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j), ModBlocks.blockCasings3Misc, 11)) {
						Logger.INFO("Bottom Layer missing.");
						return false;
					}					
				}
			}
		}
		return true;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(ItemStack aStack) {
		return 25;
	}

	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public boolean addOutput(FluidStack aLiquid) {
		if (aLiquid == null)
			return false;
		int targetHeight;
		FluidStack tLiquid = aLiquid.copy();
		boolean isOutputPollution = false;
		if (mUsingPollutionOutputs) {
			for (FluidStack pollutionFluidStack : mPollutionFluidStacks) {
				if (tLiquid.isFluidEqual(pollutionFluidStack)) {
					isOutputPollution = true;
					break;
				}
			}
		}
		if (isOutputPollution) {
			targetHeight = this.controllerY + 3;
			int pollutionReduction = 0;
			for (GT_MetaTileEntity_Hatch_Muffler tHatch : mMufflerHatches) {
				if (isValidMetaTileEntity(tHatch)) {
					pollutionReduction = 100 - StaticFields59.calculatePollutionReducation(tHatch, 100);
					break;
				}
			}
			tLiquid.amount = tLiquid.amount * (pollutionReduction + 5) / 100;
		} else {
			targetHeight = this.controllerY;
		}
		for (GT_MetaTileEntity_Hatch_Output tHatch : mOutputHatches) {
			if (isValidMetaTileEntity(tHatch) && GT_ModHandler.isSteam(aLiquid) ? tHatch.outputsSteam()
					: tHatch.outputsLiquids()) {
				if (tHatch.getBaseMetaTileEntity().getYCoord() == targetHeight) {
					int tAmount = tHatch.fill(tLiquid, false);
					if (tAmount >= tLiquid.amount) {
						return tHatch.fill(tLiquid, true) >= tLiquid.amount;
					} else if (tAmount > 0) {
						tLiquid.amount = tLiquid.amount - tHatch.fill(tLiquid, true);
					}
				}
			}
		}
		return false;
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
		Logger.WARNING("Running checkRecipeGeneric(0)");

		GT_Recipe tRecipe = this.getRecipeMap().findRecipe(getBaseMetaTileEntity(), mLastRecipe, false,
				gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aItemInputs);

		Logger.WARNING("Running checkRecipeGeneric(1)");
		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;

		if (tRecipe == null || this.mHeatingCapacity < tRecipe.mSpecialValue) {
			Logger.WARNING("BAD RETURN - 1");
			return false;
		}

		if (!this.canBufferOutputs(tRecipe, aMaxParallelRecipes)) {
			Logger.WARNING("BAD RETURN - 2");
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		int tHeatCapacityDivTiers = (mHeatingCapacity - tRecipe.mSpecialValue) / 900;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;
		// Count recipes to do in parallel, consuming input items and fluids and
		// considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) {
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

		if (tHeatCapacityDivTiers > 0)
			this.mEUt = (int) (this.mEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
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

	private volatile int mGraceTimer = 100;

	@SuppressWarnings("unused")
	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (this.mMaxProgresstime > 0 && this.mProgresstime != 0 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()) {			
			if (aTick % 10 == 0 || this.getBaseMetaTileEntity().hasWorkJustBeenEnabled()) {
				if (!this.depleteInput(FluidUtils.getFluidStack("pyrotheum", 5))) {
					this.causeMaintenanceIssue();
					this.stopMachine();
				}
				if (false) { // To be replaced with a config option or something
					this.explodeMultiblock();
				}
			}			
		}
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public int getMaxParallelRecipes() {
		return 8;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 90;
	}

}