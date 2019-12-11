package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae;

import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

public class GregtechMTE_AlgaePondBase extends GregtechMeta_MultiBlockBase {

	private int mLevel = -1;

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
	public String[] getTooltip() {
		return new String[] {
				"Grows Algae!",
				"Controller Block for the Algae Farm",
				"Size: 9x3x9 [WxHxL] (open)",
				"X           X",
				"X           X", 
				"XXXXXXXXX", 
				"Can process (Tier * 10) recipes",
				"Machine Hulls (all bottom layer)", 
				"Sterile Farm Casings (all non-hatches)", 
				"Controller (front centered)",
				"All hatches must be on the bottom layer",
				"All hulls must be the same tier, this dictates machine speed",
				"Does not require power or maintenance",
				"1x Output Bus", 
				"1x Input Bus (optional)",
				"1x Input Hatch (fill with water)",
		};
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
		return null;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public int getMaxParallelRecipes() {
		return 2;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {

		this.mLevel = 0;


		// Get Facing direction		
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

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX
				* mCurrentDirectionX;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ
				* mCurrentDirectionZ;

		// Get Expected Tier
		Block aCasingBlock = aBaseMetaTileEntity.getBlockAtSide((byte) 0);
		int aCasingMeta = aBaseMetaTileEntity.getMetaIDAtSide((byte) 0);

		// Bad Casings
		if ((aCasingBlock != GregTech_API.sBlockCasings1) || aCasingMeta > 9) {
			return false;
		}
		else {
			mLevel = this.getCasingTierOnClientSide();
		}
		int aID = TAE.getIndexFromPage(1, 15);
		int tAmount = 0;
		check : for (int i = mOffsetX_Lower; i <= mOffsetX_Upper; ++i) {
			for (int j = mOffsetZ_Lower; j <= mOffsetZ_Upper; ++j) {
				for (int h = -1; h < 2; ++h) {
					if ((h != 0) || ((((xDir + i != 0) || (zDir + j != 0))) && (((i != 0) || (j != 0))))) {
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h,	zDir + j);

						Logger.INFO("X: " + i + " | Z: " + j+" | Tier: "+aID);
						if (h == -1 && tTileEntity != null && addToMachineList(tTileEntity, aID)) {
							continue;
						}
						else if (h != -1 && tTileEntity != null) {
							Logger.INFO("Found hatch in wrong place, expected casing.");	
							return false;
						}

						Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
						byte tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);

						if ((tBlock == ModBlocks.blockCasings2Misc) && (tMeta == 15) && (h >= 0)) {
							++tAmount;
						}
						else if ((tBlock == GregTech_API.sBlockCasings1) && (tMeta == mLevel) && (h == -1)) {
							++tAmount;
						}
						else if ((tBlock == GregTech_API.sBlockCasings1) && (tMeta != mLevel) && (h == -1)) {
							Logger.INFO("Found wrong tiered casing.");
							return false;
						}
						else {
							if ((i != mOffsetX_Lower && j != mOffsetZ_Lower && i != mOffsetX_Upper
									&& j != mOffsetZ_Upper) && (h == 0 || h == 1)) {
								continue;
							} else {
								if (tBlock.getLocalizedName().contains("gt.blockmachines") || tBlock == Blocks.water
										|| tBlock == Blocks.flowing_water
										|| tBlock == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)) {
									continue;

								} else {
									Logger.INFO("[x] Did not form - Found: " + tBlock.getLocalizedName() + " | "
											+ tBlock.getDamageValue(aBaseMetaTileEntity.getWorld(),
													aBaseMetaTileEntity.getXCoord() + i,
													aBaseMetaTileEntity.getYCoord(),
													aBaseMetaTileEntity.getZCoord() + j)
											+ " | Special Meta: "
											+ (tTileEntity == null ? "0" : tTileEntity.getMetaTileID()));
									Logger.INFO("[x] Did not form - Found: "
											+ (aBaseMetaTileEntity.getXCoord() + xDir + i) + " | "
											+ aBaseMetaTileEntity.getYCoord() + " | "
											+ (aBaseMetaTileEntity.getZCoord() + zDir + j));
									break check;
								}
							}

						}

					}
				}
			}
		}
		if ((tAmount >= 64)) {
			Logger.INFO("Made structure.");
			this.getBaseMetaTileEntity().getWorld().markBlockForUpdate(this.getBaseMetaTileEntity().getXCoord(), this.getBaseMetaTileEntity().getYCoord(), this.getBaseMetaTileEntity().getZCoord());
		} else {
			Logger.INFO("Did not make structure.");
		}
		return (tAmount >= 64);
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

		boolean isValidWater = tAmount >= 60;

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
		this.fixAllMaintenanceIssue();		
		// Silly Client Syncing
		if (aBaseMetaTileEntity.isClientSide()) {
			this.mLevel = getCasingTierOnClientSide();
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

		if (!this.canBufferOutputs(tRecipe, aMaxParallelRecipes, false)) {
			return false;
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
				else {
					continue;
				}
			}
		}
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	private final int getCasingTierOnClientSide() {
		if (this == null || this.getBaseMetaTileEntity().getWorld() == null) {
			return 0;
		}
		try {
			Block aInitStructureCheck;
			int aInitStructureCheckMeta;
			IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
			aInitStructureCheck = aBaseMetaTileEntity.getBlockOffset(0, -1, 0);
			aInitStructureCheckMeta = aBaseMetaTileEntity.getMetaIDOffset(0, -1, 0);			
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
