package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

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
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.api.helpers.GregtechPlusPlus_API.Multiblock_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.multi.SpecialMultiBehaviour;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_AlgaeFarm;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_FrothFlotationCell extends GregtechMeta_MultiBlockBase {

	public GregtechMTE_FrothFlotationCell(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_FrothFlotationCell(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_FrothFlotationCell(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Flotation Cell";
	}

	@Override
	public String[] getTooltip() {
		return new String[] {
				"Process that milled ore!",
		};
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}


	@Override
	public boolean isFacingValid(final byte aFacing) {
		if (aFacing == 0 || aFacing > 1) {
			return false;
		}
		return true;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		int aID = TAE.getIndexFromPage(2, 1);
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[aID], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[aID]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GTPP_Recipe.GTPP_Recipe_Map.sFlotationCellRecipes;
	}

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		Block aCasing1 = ModBlocks.blockCasings3Misc;
		int aCasingMeta1 = 1;
		Block aCasing2 = ModBlocks.blockSpecialMultiCasings;
		int aCasingMeta2 = 9;
		int aCasingCount1 = 0;
		int aCasingCount2 = 0;
		int aControllerY = aBaseMetaTileEntity.getYCoord();
		// Check adjacent blocks
		for (byte side = 2; side < 6; side++) {
			Block aBlock = aBaseMetaTileEntity.getBlockAtSide(side);
			int aMeta = aBaseMetaTileEntity.getMetaIDAtSide(side);
			if (this.isValidBlockForStructure(null, 0, false, aBlock, aMeta, aCasing1, aCasingMeta1)) {
				aCasingCount1++;
			}
			else {
				log("Bad block at Y:"+(aControllerY));
				return false;
			}
		}
		// Check first layer
		aControllerY--;
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {

				int aWorldOffsetX = aBaseMetaTileEntity.getXCoord() + x;
				int aWorldOffsetZ = aBaseMetaTileEntity.getZCoord() + z;

				// Don't check air
				if ((x == -2 && z != 0) || (x == 2 && z != 0) || (z == -2 && x != 0) || (z == 2 && x != 0) || (x == 0 && z == 0)) {
					continue;
				}
				else {
					Block aBlock = aBaseMetaTileEntity.getBlock(aWorldOffsetX, aControllerY, aWorldOffsetZ);
					int aMeta = aBaseMetaTileEntity.getMetaID(aWorldOffsetX, aControllerY, aWorldOffsetZ);
					if (this.isValidBlockForStructure(null, 0, false, aBlock, aMeta, aCasing2, aCasingMeta2)) {
						aCasingCount2++;
					}
					else {
						log("Bad block at Y:"+(aControllerY)+", X:"+aWorldOffsetX+", Z:"+aWorldOffsetZ);
						aBaseMetaTileEntity.getWorld().setBlock(aWorldOffsetX, aControllerY, aWorldOffsetZ, Blocks.bookshelf);
						return false;
					}
				}				
			}	
		}
		// Check circular tower
		aControllerY--;
		for (int y = aControllerY; y > (aControllerY-5); y--) {
			for (int x = -2; x < 3; x++) {
				for (int z = -2; z < 3; z++) {

					int aWorldOffsetX = aBaseMetaTileEntity.getXCoord() + x;
					int aWorldOffsetZ = aBaseMetaTileEntity.getZCoord() + z;

					// Don't check air
					if ((x == -2 && z != 0) || (x == 2 && z != 0) || (z == -2 && x != 0) || (z == 2 && x != 0) || (x == 0 && z == 0)) {
						continue;
					}
					// Don't check air
					else if ((x == -1 && z == 0) || (x == 1 && z == 0) || (z == -1 && x == 0) || (z == 1 && x == 0) || (x == 0 && z == 0)) {
						continue;
					}
					else {
						Block aBlock = aBaseMetaTileEntity.getBlock(aWorldOffsetX, y, aWorldOffsetZ);
						int aMeta = aBaseMetaTileEntity.getMetaID(aWorldOffsetX, y, aWorldOffsetZ);
						if (this.isValidBlockForStructure(null, 0, false, aBlock, aMeta, aCasing2, aCasingMeta2)) {
							aCasingCount2++;
						}
						else {
							log("Bad block at Y:"+(y)+", X:"+aWorldOffsetX+", Z:"+aWorldOffsetZ);
							return false;
						}
					}				
				}	
			}
		}
		// Check Base
		aControllerY -= 5;
		for (int y = aControllerY; y > (aControllerY-2); y--) {		
			for (int x = -3; x < 4; x++) {
				for (int z = -3; z < 4; z++) {
					int aWorldOffsetX = aBaseMetaTileEntity.getXCoord() + x;
					int aWorldOffsetZ = aBaseMetaTileEntity.getZCoord() + z;
					if ((x == -3 && z == -3) || (x == 3 && z == 3) || (x == -3 && z == 3) || (x == 3 && z == -3)) {
						continue;
					}				
					else if ((x == -3 && z == -2) || (x == -2 && z == -3) || (x == 3 && z == 2) || (x == 2 && z == 3)) {
						continue;
					}				
					else if ((x == -2 && z == 3) || (x == -3 && z == 2) || (x == 3 && z == -2) || (x == 2 && z == -3)) {
						continue;
					}
					else {					
						Block aBlock = aBaseMetaTileEntity.getBlock(aWorldOffsetX, y, aWorldOffsetZ);
						int aMeta = aBaseMetaTileEntity.getMetaID(aWorldOffsetX, y, aWorldOffsetZ);
						IGregTechTileEntity aTile = aBaseMetaTileEntity.getIGregTechTileEntity(aWorldOffsetX, y, aWorldOffsetZ);
						if (this.isValidBlockForStructure(aTile, TAE.getIndexFromPage(2, 1), true, aBlock, aMeta, aCasing1, aCasingMeta1)) {
							if (aTile == null) {
								aCasingCount1++;								
							}
						}
						else {
							log("Bad block at Y:"+(y)+", X:"+aWorldOffsetX+", Z:"+aWorldOffsetZ);
							return false;
						}
					}				
				}	
			}
		}
		if (aCasingCount1 < 68) {
			log("Inconel Casings found: "+aCasingCount1);
			log("Inconel Casings required: 68");
			return false;
		}
		if (aCasingCount2 < 52) {
			log("Flotation Casings found: "+aCasingCount2);
			log("Flotation Casings required: 52");
			return false;
		}		
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);		
		// Fix GT bug
		if (this.getBaseMetaTileEntity().getFrontFacing() == 0 && this.getBaseMetaTileEntity().getBackFacing() == 1) {
			log("Fixing Bad Facing. (GT Bug)");
			this.getBaseMetaTileEntity().setFrontFacing((byte) 1); 
		}
	}

	@Override
	public boolean checkRecipe(ItemStack arg0) {
		return super.checkRecipeGeneric();
	}
	
	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {
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
		
		/*
		 * 
		 * Material Hash checks
		 * Makes sure we can only ever use one type of material in this flotation cell.
		 * 
		 */
		int aExpectedMaterialHash;
		// Set the hash of expected material type
		if (mLockedOreType == -1) {
			mLockedOreType = FlotationRecipeHandler.getHashForMaterial(FlotationRecipeHandler.getMaterialOfMilledProduct(FlotationRecipeHandler.findMilledStack(aRecipe)));
		}
		// Set the hash for this recipe check
		aExpectedMaterialHash = mLockedOreType;
		
		// Compute hash of current inputs
		int aFoundMaterialHash = FlotationRecipeHandler.getHashForMaterial(FlotationRecipeHandler.getMaterialOfMilledProduct(FlotationRecipeHandler.findMilledStack(aItemInputs)));
		
		// Check hashes match
		if (aExpectedMaterialHash != aFoundMaterialHash) {
			log("Did not find the correct milled type.");
			log("Found: "+aFoundMaterialHash);
			log("Expected: "+mLockedOreType);
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
	
	/*
	 * Handle NBT
	 */

	private int mLockedOreType = -1;

	@Override
	public void setItemNBT(NBTTagCompound aNBT) {
		if (mLockedOreType != -1) {
			aNBT.setInteger("mLockedOreType", mLockedOreType);			
		}
		super.setItemNBT(aNBT);
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		if (mLockedOreType != -1) {
			aNBT.setInteger("mLockedOreType", mLockedOreType);
		}
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		mLockedOreType = aNBT.getInteger("mLockedOreType");
	}
	
}
