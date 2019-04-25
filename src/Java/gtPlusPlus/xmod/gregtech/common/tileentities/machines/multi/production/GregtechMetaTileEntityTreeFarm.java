package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gregtech.api.GregTech_API;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ThreadFakeWorldGenerator;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;
import gtPlusPlus.xmod.gregtech.common.helpers.treefarm.TreeGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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

	
	
	/*
	 * Static thread for Fake World Handling
	 */
	
	
	private static ScheduledExecutorService executor;
	private static ThreadFakeWorldGenerator aThread;
	
	public GregtechMetaTileEntityTreeFarm(final String aName) {
		super(aName);
		mFuelStack = FluidUtils.getFluidStack("cryotheum", 1);
		CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
		mCryoFuelName = mFuelStack.getLocalizedName();
		mCasingName = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings2Misc, 15);
		mHatchName = ItemUtils.getLocalizedNameOfBlock(GregTech_API.sBlockMachines, 967);
		
		/*if (executor == null || mTreeData == null) {			
			if (executor == null) {
				executor = Executors.newScheduledThreadPool(10);				
			}
			if (executor != null) {				
				if (aThread == null) {
					aThread = new ThreadFakeWorldGenerator();	
					executor.scheduleAtFixedRate(aThread, 0, 1, TimeUnit.SECONDS);			
					while (aThread.mGenerator == null) {
						if (aThread.mGenerator != null) {
							break;
						}
					}		
					if (aThread.mGenerator != null) {
						mTreeData = aThread.mGenerator;
					}
				}
			}			
		}*/	
		
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
		//return TreeFarmHelper.isCorrectPart(aStack);
		return true;
	}

	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	public boolean checkRecipe(final ItemStack aStack) {
		//Logger.WARNING("Trying to process virtual tree farming");
		if (mTreeData != null) {
			//Logger.WARNING("Tree Data is valid");
			
			long tVoltage = getMaxInputVoltage();
			byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
			
			this.mMaxProgresstime = 100;
			this.mEUt = 500;
			
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
			
			
			
			int aChance = MathUtils.randInt(0, 10);
			AutoMap<ItemStack> aOutputs = new AutoMap<ItemStack>();
			
			try {
				//Logger.WARNING("Output Chance - "+aChance+" | Valid number? "+(aChance < 1000));
			if (aChance < 8) {
				//1% Chance per Tick				
				for (int u=0; u<(Math.max(1, (MathUtils.randInt((3*tTier), 100)*tTier*tTier)/8));u++) {
					aOutputs = mTreeData.generateOutput(0);		
					if (aOutputs.size() > 0) {
						Logger.WARNING("Generated some Loot, adding it to the output busses");
						
						ItemStack aLeaves = ItemUtils.getSimpleStack(Blocks.leaves);
						
						for (ItemStack aOutputItemStack : aOutputs) {
							if (!GT_Utility.areStacksEqual(aLeaves, aOutputItemStack)) {
								this.addOutput(aOutputItemStack);
							}
						}
						Logger.WARNING("Updating Slots");
						this.updateSlots();
					}	
				}			
				
			}				
			}
			catch (Throwable t) {
				t.printStackTrace();
			}
			
			//Logger.WARNING("Valid Recipe");
			return true;
		}
		else {
			//Logger.WARNING("Invalid Recipe");
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
								Logger.WARNING("Bad centrifuge casing");
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