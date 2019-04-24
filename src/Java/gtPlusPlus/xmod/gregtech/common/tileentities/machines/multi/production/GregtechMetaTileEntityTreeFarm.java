package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;
import gtPlusPlus.xmod.gregtech.common.helpers.treefarm.TreeGenerator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntityTreeFarm extends GregtechMeta_MultiBlockBase {

	public static int CASING_TEXTURE_ID;
	public static String mCryoFuelName = "Gelid Cryotheum";
	public static String mCasingName = "Advanced Cryogenic Casing";
	public static String mHatchName = "Cryotheum Hatch";
	public static FluidStack mFuelStack;
	public static TreeGenerator mTreeData;

	public GregtechMetaTileEntityTreeFarm(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		mFuelStack = FluidUtils.getFluidStack("cryotheum", 1);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
		mCryoFuelName = mFuelStack.getLocalizedName();
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings2Misc, 15);
		mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 967);
	}

	public GregtechMetaTileEntityTreeFarm(final String aName) {
		super(aName);
		mFuelStack = FluidUtils.getFluidStack("cryotheum", 1);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
		mCryoFuelName = mFuelStack.getLocalizedName();
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings2Misc, 15);
		mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 967);
		
		if (mTreeData == null) {
			mTreeData = new TreeGenerator();
		}
		
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return (IMetaTileEntity) new GregtechMetaTileEntityTreeFarm(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Tree Farm";
	}

	public String[] getTooltip() {

		if (mCasingName.toLowerCase().contains(".name")) {
			mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings2Misc, 15);
		}
		if (mCryoFuelName.toLowerCase().contains(".")) {
			mCryoFuelName = FluidUtils.getFluidStack("cryotheum", 1).getLocalizedName();
		}
		if (mHatchName.toLowerCase().contains(".name")) {
			mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 967);
		}
		
		return new String[]{
				"Factory Grade Tree Growth Simulator",
				"Speed: Very Fast | Eu Usage: 100% | Parallel: 1",				
				//"Consumes 1L of "+mCryoFuelName+"/t during operation",
				"Constructed exactly the same as a normal Vacuum Freezer",
				"Use "+mCasingName+"s (10 at least!)",
				"1x " + mHatchName + " (Required)",
				"TAG_HIDE_HATCHES"
		};
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID],
					new GT_RenderedTexture((IIconContainer) (aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced))};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[CASING_TEXTURE_ID]};
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
		return "VacuumFreezer";
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {			
		return null;
	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return TreeFarmHelper.isCorrectPart(aStack);
	}

	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	public boolean checkRecipe(final ItemStack aStack) {
		Logger.INFO("Trying to process virtual tree farming");
		if (mTreeData != null) {
			Logger.INFO("Tree Data is valid");
			this.getBaseMetaTileEntity().enableWorking();
			this.mMaxProgresstime = 100;
			this.mEUt = -500;
			this.mEfficiencyIncrease = 10000;
			
			int aChance = MathUtils.randInt(0, 10000);
			AutoMap<ItemStack> aOutputs = new AutoMap<ItemStack>();
			
			try {
				Logger.INFO("Output Chance - "+aChance+" | Valid number? "+(aChance < 100));
			if (aChance < 100) {
				//1% Chance per Tick
				aOutputs = mTreeData.generateOutput(0);		
				if (aOutputs.size() > 0) {
					Logger.INFO("Generated some Loot, adding it to the output busses");
					for (ItemStack aOutputItemStack : aOutputs) {
						this.addOutput(aOutputItemStack);
					}
					Logger.INFO("Updating Slots");
					this.updateSlots();
				}	
			}				
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			
			Logger.INFO("Valid Recipe");
			return true;
		}
		else {
			Logger.INFO("Invalid Recipe");
			this.getBaseMetaTileEntity().disableWorking();
			return false;
		}
		//return this.checkRecipeGeneric(4, 100, 100);
	}
	
	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		int tAmount = 0;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
			return false;
		} else {
			for (int i = -1; i < 2; ++i) {
				for (int j = -1; j < 2; ++j) {
					for (int h = -1; h < 2; ++h) {
						if (h != 0 || (xDir + i != 0 || zDir + j != 0) && (i != 0 || j != 0)) {
							IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i,
									h, zDir + j);
							Block aBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
							int aMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);

							if (!isValidBlockForStructure(tTileEntity, CASING_TEXTURE_ID, true, aBlock, aMeta,
									ModBlocks.blockCasings2Misc, 15)) {
								Logger.INFO("Bad centrifuge casing");
								return false;
							}
							++tAmount;

						}
					}
				}
			}
			return tAmount >= 10;
		}
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerTick(final ItemStack aStack) {
		return 25;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {		
		if (mTreeData != null) {
			//this.getBaseMetaTileEntity().enableWorking();
		}
		
		
		/*if (this.getBaseMetaTileEntity().isActive()) {
			if (!this.depleteInput(mFuelStack.copy())) {
				this.getBaseMetaTileEntity().setActive(false);
			}
		}	*/
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}
}