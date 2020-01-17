package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_IndustrialAlloySmelter extends GregtechMeta_MultiBlockBase {

	public static int CASING_TEXTURE_ID;
	private int mHeatingCapacity = 0;
	private int mLevel = 0;

	public GregtechMetaTileEntity_IndustrialAlloySmelter(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 1);
	}

	public GregtechMetaTileEntity_IndustrialAlloySmelter(String aName) {
		super(aName);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(2, 1);
	}

	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialAlloySmelter(this.mName);
	}

	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_MULTI_SMELTER)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID]};
	}

	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MultiFurnace.png");
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes;
	}

	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(ItemStack aStack) {
		return 15;
	}

	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public String getMachineType() {
		return "Alloy Smelter";
	}

	@Override
	public String[] getTooltip() {
		return new String[]{
				"Controller Block for the Industrial Alloy Smelter",
				"Gains one parallel per voltage tier",
				"Gains one multiplier per coil tier",
				"parallel = tier * coil tier",
				"Gains 5% speed bonus per coil tier",
				"Size(WxHxD): 3x5x3 (Hollow)", 
				"Controller (Front middle at bottom)",
				"Inconel Reinforced Casings (layers 0/4, 10 at least!)",
				"16x Heating Coils (layers 1/3, hollow)",
				"8x Integral Encasement V (middle layer, hollow)",
				"1x Input Bus",
				"1x Output Bus",
				"1x Output Hatch",
				"1x Energy Hatch",
				};
	}

	@Override
	public int getMaxParallelRecipes() {
		return (this.mLevel * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public boolean checkMultiblock(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;

		this.mLevel = 0;
		this.mHeatingCapacity = 0;
		for (int i=1;i<4;i++) {
			if (!aBaseMetaTileEntity.getAirOffset(xDir, i, zDir)) {		
				Logger.INFO("Did not find air inside on layer "+i);
				return false;
			}
		}
		/*if (!addMufflerToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir, 3, zDir),
				CASING_TEXTURE_ID)) {
			return false;
		}*/
		Block tUsedBlock = aBaseMetaTileEntity.getBlockOffset(xDir + 1, 1, zDir);
		byte tUsedMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + 1, 1, zDir);
		this.mLevel = StaticFields59.getTierForCoil(tUsedBlock, tUsedMeta);
		this.mHeatingCapacity = StaticFields59.getHeatingCapacityForCoil(tUsedBlock, tUsedMeta);
		
		int aCasingCount = 0;

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {				
				
				if ((i != 0) || (j != 0)) {					
					//Coils 1
					if (!isValidBlockForStructure(null, CASING_TEXTURE_ID, false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 1, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 1, zDir + j), StaticFields59.getBlockCasings5(), tUsedMeta)) {
						Logger.INFO("Heating Coils missing. First Layer");
						return false;
					}
					
					//Integral Casings
					if (!isValidBlockForStructure(null, CASING_TEXTURE_ID, false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 2, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 2, zDir + j), ModBlocks.blockCasingsTieredGTPP, 4)) {
						Logger.INFO("Integral Framework missing. Second Layer");
						return false;
					}
					
					//Coils 2
					if (!isValidBlockForStructure(null, CASING_TEXTURE_ID, false, aBaseMetaTileEntity.getBlockOffset(xDir + i, 3, zDir + j), (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 3, zDir + j), StaticFields59.getBlockCasings5(), tUsedMeta)) {
						Logger.INFO("Heating Coils missing. Third Layer");
						return false;
					}					
				}
				
				//Top Layer
				
				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, 4, zDir + j);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 4, zDir + j);
				if (aCurrentBlock == ModBlocks.blockCasings3Misc && aCurrentMeta == 1) {
					aCasingCount++;
				}
				
				final IGregTechTileEntity tTileEntity2 = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 4, zDir + j);					
				if (!isValidBlockForStructure(tTileEntity2, CASING_TEXTURE_ID, true, aCurrentBlock, aCurrentMeta, ModBlocks.blockCasings3Misc, 1)) {
					Logger.INFO("Top Layer missing.");
					return false;
				}
			}
		}
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {

				
				Block aCurrentBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j);
				int aCurrentMeta = (int) aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j);
				if (aCurrentBlock == ModBlocks.blockCasings3Misc && aCurrentMeta == 1) {
					aCasingCount++;
				}
				if ((xDir + i != 0) || (zDir + j != 0)) {
					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);					
					if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, true, aCurrentBlock, aCurrentMeta, ModBlocks.blockCasings3Misc, 1)) {
						Logger.INFO("Bottom Layer missing.");
						return false;
					}					
				}
			}
		}
		
		return aCasingCount >= 10;
	}

	public boolean checkRecipe(ItemStack aStack) {
		return checkRecipeGeneric(this.getMaxParallelRecipes(), 0, 5 * this.mLevel); // Will have to clone the logic from parent class to handle heating coil
		// tiers.
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

		if (tRecipe == null) {
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
		int tHeatCapacityDivTiers = mHeatingCapacity / 900;
		if (tHeatCapacityDivTiers > 0) {
			tRecipeEUt = (int) (tRecipeEUt * (Math.pow(0.95, tHeatCapacityDivTiers)));
		}
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

	
}
