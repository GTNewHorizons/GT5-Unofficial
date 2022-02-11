package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gtPlusPlus.core.lib.CORE;
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
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_AlgaeFarm;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_AlgaePondBase extends GregtechMeta_MultiBlockBase<GregtechMTE_AlgaePondBase> {

	private int mLevel = -1;
	private int mCasing;
	private IStructureDefinition<GregtechMTE_AlgaePondBase> STRUCTURE_DEFINITION = null;
	private int checkMeta;

	public GregtechMTE_AlgaePondBase(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_AlgaePondBase(final String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_AlgaePondBase(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Algae Pond";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Grows Algae!")
				.addInfo("Controller Block for the Algae Farm")
				.addInfo("Provide compost to boost production by one tier")
				.addInfo("Does not require power or maintenance")
				.addInfo("All Machine Casings must be the same tier, this dictates machine speed.")
				.addInfo("Fill Input Hatch with water.")
				.addPollutionAmount(getPollutionPerSecond(null))
				.addSeparator()
				.beginStructureBlock(9, 3, 9, true)
				.addController("Front Center")
				.addCasingInfo("Machine Casings", 64)
				.addCasingInfo("Sterile Farm Casings", 34)
				.addInputBus("Any Casing", 1)
				.addOutputBus("Any Casing", 1)
				.addInputHatch("Any Casing", 1)
				.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}

	public void setMeta(int meta) {
		checkMeta = meta;
	}

	public int getMeta() {
		return checkMeta;
	}

	@Override
	public IStructureDefinition<GregtechMTE_AlgaePondBase> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_AlgaePondBase>builder()
					.addShape(mName, transpose(new String[][]{
							{"XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "XXXXXXXXX"},
							{"XXXXXXXXX", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "X       X", "XXXXXXXXX"},
							{"CCCC~CCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMTE_AlgaePondBase::addAlgaePondBaseList, TAE.getIndexFromPage(1, 15), 1
									),
									onElementPass(
											x -> ++x.mCasing,
											addTieredBlock(
													GregTech_API.sBlockCasings1, GregtechMTE_AlgaePondBase::setMeta, GregtechMTE_AlgaePondBase::getMeta, 10
											)
									)
							)
					)
					.addElement(
							'X',
							ofBlock(
									ModBlocks.blockCasings2Misc, 15
							)
					)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 4, 2, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		mLevel = 0;
		checkMeta = 0;
		if (checkPiece(mName, 4, 2, 0) && mCasing >= 64 && checkMeta > 0) {
			mLevel = checkMeta - 1;
			return true;
		}
		return false;
	}

	public final boolean addAlgaePondBaseList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
		}
		return false;
	}

	@Override
	protected IAlignmentLimits getInitialAlignmentLimits() {
		// fuck
		return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
	}

	@Override
	public String getSound() {
		return GregTech_API.sSoundList.get(Integer.valueOf(207));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {

		int aID = TAE.getIndexFromPage(1, 15);
		if (mLevel > -1) {
			aID = mLevel;
		}		
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(aID), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(aID)};
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return null;
	}

	@Override
	public int getMaxParallelRecipes() {
		return 2;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	public boolean checkForWater() {		

		// Get Facing direction
		IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
		int mDirectionX = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int mCurrentDirectionX;
		int mCurrentDirectionZ;
		int mOffsetX_Lower = 0;
		int mOffsetX_Upper = 0;
		int mOffsetZ_Lower = 0;
		int mOffsetZ_Upper = 0;

		mCurrentDirectionX = 4;
		mCurrentDirectionZ = 4;

		mOffsetX_Lower = -4;
		mOffsetX_Upper = 4;
		mOffsetZ_Lower = -4;
		mOffsetZ_Upper = 4;

		// if (aBaseMetaTileEntity.fac)

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX
				* mCurrentDirectionX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ
				* mCurrentDirectionZ;

		int tAmount = 0;
		for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
			for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {
				for (int h = 0; h < 2; h++) {
					Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
					// byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
					if (tBlock == Blocks.air || tBlock == Blocks.flowing_water || tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)) {
						if (this.getStoredFluids() != null) {
							for (FluidStack stored : this.getStoredFluids()) {
								if (stored.isFluidEqual(FluidUtils.getFluidStack("water", 1))) {
									if (stored.amount >= 1000) {
										// Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
										stored.amount -= 1000;
										Block fluidUsed = Blocks.water;																			
										aBaseMetaTileEntity.getWorld().setBlock(
												aBaseMetaTileEntity.getXCoord() + xDir + i,
												aBaseMetaTileEntity.getYCoord() + h,
												aBaseMetaTileEntity.getZCoord() + zDir + j, fluidUsed);

									}
								}
							}
						}
					}
					tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
					if (tBlock == Blocks.water || tBlock == Blocks.flowing_water) {
						++tAmount;
						// Logger.INFO("Found Water");
					}
				}
			}
		}

		boolean isValidWater = tAmount >= 49;

		if (isValidWater) {
			Logger.INFO("Filled structure.");
			return true;
		}
		else {		
			return false;
		}
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiAlgaePond;
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
		this.fixAllMaintenanceIssue();		
		// Silly Client Syncing
		if (aBaseMetaTileEntity.isClientSide()) {
			this.mLevel = getCasingTier();
		}

	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(getMaxParallelRecipes(), getEuDiscountForParallelism(), 0);
	}

	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll, GT_Recipe aRecipe) {

		if (this.mLevel < 0) {
			Logger.INFO("Bad Tier.");
			return false;
		}

		if (!checkForWater()) {
			Logger.INFO("Not enough Water.");
			return false;
		}		

		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};

		Logger.INFO("Running checkRecipeGeneric(0)");			

		GT_Recipe tRecipe = RecipeLoader_AlgaeFarm.getTieredRecipeFromCache(this.mLevel, isUsingCompost(aItemInputs));

		this.mLastRecipe = tRecipe;

		if (tRecipe == null) {
			return false;
		}

		aMaxParallelRecipes = this.canBufferOutputs(tRecipe, aMaxParallelRecipes);
		if (aMaxParallelRecipes == 0) {
			return false;
		}
		if (tRecipe.mInputs.length > 0) {
			for (ItemStack aInputToConsume : tRecipe.mInputs) {
				this.depleteInput(aInputToConsume);			
			}
		}


		// -- Try not to fail after this point - inputs have already been consumed! --


		this.mMaxProgresstime = (int)(tRecipe.mDuration);
		this.mEUt = 0;
		this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
		this.mEfficiencyIncrease = 10000;	
		Logger.INFO("Recipe time: "+this.mMaxProgresstime);
		this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);

		// Collect fluid outputs
		FluidStack[] tOutputFluids = new FluidStack[tRecipe.mFluidOutputs.length];
		for (int h = 0; h < tRecipe.mFluidOutputs.length; h++) {
			if (tRecipe.getFluidOutput(h) != null) {
				tOutputFluids[h] = tRecipe.getFluidOutput(h).copy();
				tOutputFluids[h].amount *= aMaxParallelRecipes;
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
				for (int g = 0; g < aMaxParallelRecipes; g++) {
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

		Logger.INFO("GOOD RETURN - 1");
		return true;

	}

	private boolean isUsingCompost(ItemStack[] aItemInputs) {
		ItemStack aCompost = ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1);	
		for (ItemStack i : aItemInputs) {
			if (GT_Utility.areStacksEqual(aCompost, i)) {
				if (i.stackSize >= 8) {
					return true;
				}
			}
		}
		return false;
	}


	private int getCasingTier() {
		if (this.getBaseMetaTileEntity().getWorld() == null) {
			return 0;
		}
		try {
			Block aInitStructureCheck;
			int aInitStructureCheckMeta;
			IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
			int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
	        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
			aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(xDir, -1, zDir);
			aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir, -1, zDir);			
			if (aInitStructureCheck == GregTech_API.sBlockCasings1) {
				return aInitStructureCheckMeta;
			}
			return 0;
		}
		catch (Throwable t) {
			t.printStackTrace();
			return 0;
		}
	}



}
