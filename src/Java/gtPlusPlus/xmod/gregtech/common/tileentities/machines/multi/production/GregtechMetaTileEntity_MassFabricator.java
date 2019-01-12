package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import java.util.ArrayList;
import java.util.Collection;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MatterFab;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_MassFabricator extends GregtechMeta_MultiBlockBase {

	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3200;
	
	private int mMatterProduced = 0;
	private int mScrapProduced = 0;
	private int mAmplifierProduced = 0;
	private int mScrapUsed = 0;
	private int mAmplifierUsed = 0;

	public static String mCasingName1 = "Matter Fabricator Casing";
	public static String mCasingName2 = "Containment Casing";
	public static String mCasingName3 = "Matter Generation Coil";
	
	private int mMode = 0;

	private final static int MODE_SCRAP = 1;
	private final static int MODE_UU = 0;

	public static boolean sRequiresUUA = false;
	private static FluidStack[] mUU = new FluidStack[2];
	private static ItemStack mScrap[] = new ItemStack[2];

	public int getAmplifierUsed(){
		return this.mAmplifierUsed;
	}

	public int getMatterProduced(){
		return this.mMatterProduced;
	}

	public GregtechMetaTileEntity_MassFabricator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
		mCasingName1 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 9);
		mCasingName2 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 15);
		mCasingName3 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 8);
	}

	public GregtechMetaTileEntity_MassFabricator(final String aName) {
		super(aName);
		mCasingName1 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 9);
		mCasingName2 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 15);
		mCasingName3 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 8);
	}

	@Override
	public String getMachineType() {
		return "Mass Fabricator / Recycler";
	}

	@Override
	public String[] getTooltip() {

		//if (mCasingName1.toLowerCase().contains(".name")) {
			mCasingName1 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 9);
		//}
		//if (mCasingName2.toLowerCase().contains(".name")) {
			mCasingName2 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 15);
		//}
		//if (mCasingName3.toLowerCase().contains(".name")) {
			mCasingName3 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 8);
		//}
		
		return new String[]{
				"Controller Block for the Matter Fabricator",
				"Produces UU-A, UU-m & Scrap",
				"Size(WxHxD): 5x4x5, Controller (Bottom center)",
				"3x1x3 "+mCasingName3+"s (Inside bottom 5x1x5 layer)",
				"9x "+mCasingName3+" (Centered 3x1x3 area in Bottom layer)",
				"1x Input Hatch (Any bottom layer casing)",
				"1x Output Hatch (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)",
				"1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
				"1x Energy Hatch (Any bottom layer casing)",
				"24x "+mCasingName2+" for the walls",
				mCasingName1+"s for the edges & top (40 at least!)",
				};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_MatterFab_Active_Animated : TexturesGtBlock.Overlay_MatterFab_Animated)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "MatterFabricator";
	}

	public static ItemStack getScrapPile() {
		if (mScrap[0] == null) {
			mScrap[0] = ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrap"));
		}
		return mScrap[0];
	}	
	public static ItemStack getScrapBox() {		
		if (mScrap[1] == null) {
			mScrap[1] = ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrapbox"));
		}
		return mScrap[1];
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MatterFab(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
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
		return checkRecipeGeneric(tItemInputs, tFluidInputs, 4, 80, 00, 100);
	}		


	@Override
	public boolean checkMultiblock(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
		
		Block aContainmentGlass = ModBlocks.blockCasings3Misc;
		int aContainmentMeta = 15;
		
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				for (int h = 0; h < 4; h++) {

					//Utils.LOG_INFO("Logging Variables - xDir:"+xDir+" zDir:"+zDir+" h:"+h+" i:"+i+" j:"+j);

					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
					/*if (tTileEntity != Block.getBlockFromItem(UtilsItems.getItem("IC2:blockAlloyGlass"))) {
						Utils.LOG_INFO("h:"+h+" i:"+i+" j:"+j);
						double tX = tTileEntity.getXCoord();
						double tY = tTileEntity.getYCoord();
						double tZ = tTileEntity.getZCoord();
						Utils.LOG_INFO("Found Glass at X:"+tX+" Y:"+tY+" Z:"+tZ);
						//return false;
					}*/
					if (((i != -2) && (i != 2)) && ((j != -2) && (j != 2))) {// innerer 3x3 ohne h�he
						if (h == 0) {// innen boden (kantal coils)
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Logger.INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 8) {
								Logger.INFO("Matter Generation Coils missings from the bottom layer, inner 3x3.");
								return false;
							}
						} else if (h == 3) {// innen decke (ulv casings + input + muffler)
							if ((!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(9)))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									return false;
								}
							}
						} else {// innen air
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Logger.INFO("Make sure the inner 3x3 of the Multiblock is Air.");
								return false;
							}
						}
					} else {// Outer 5x5
						if (h == 0) {// au�en boden (controller, output, energy, maintainance, rest ulv casings)
							if ((!this.addMaintenanceToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(9))) && (!this.addEnergyInputToMachineList(tTileEntity, TAE.GTPP_INDEX(9)))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the edges of the bottom layer.");
										return false;
									}
								}
							}
						} else {// au�en �ber boden (ulv casings)
							if (h == 1) {

								if (((i == -2) || (i == 2)) && ((j == -2) || (j == 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the second layer.");
										return false;
									}
								}

								else if (((i != -2) || (i != 2)) && ((j != -2) || (j != 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != aContainmentGlass) {
										Logger.INFO("Glass Casings Missing from somewhere in the second layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != aContainmentMeta) {
										Logger.INFO("Glass Casings wrong meta from the second layer.");
										return false;
									}
								}
							}
							if (h == 2) {
								if (((i == -2) || (i == 2)) && ((j == -2) || (j == 2))){
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
										Logger.INFO("Matter Fabricator Casings Missing from one of the corners in the third layer.");
										return false;
									}
								}

								else if (((i != -2) || (i != 2)) && ((j != -2) || (j != 2))){
									
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != aContainmentGlass) {
										Logger.INFO("Glass Casings Missing from somewhere in the third layer.");
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != aContainmentMeta) {
										Logger.INFO("Glass Casings wrong meta from the third layer.");
										return false;
									}
								}
							}
							if (h == 3) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 9) {
									Logger.INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
									return false;
								}
							}
						}
					}
				}
			}
		}
		Logger.INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 2;
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

	public boolean doesHatchContainUUA() {		
		if (mUU[0] == null) {
			mUU[0] = Materials.UUAmplifier.getFluid(100);
		}
		if (mUU[1] == null) {
			mUU[1] = Materials.UUMatter.getFluid(100);
		}

		if (mUU[0] != null && mUU[1] != null) {
			for (GT_MetaTileEntity_Hatch_Input g : this.mInputHatches) {
				if (g.getFluid() != null) {
					if (g.mFluid.isFluidEqual(mUU[0])) {						
						return true;
					}
				}
			}
		}		

		return false;
	}


	/**
	 * Special Recipe Handling
	 */


	@Override
	public GT_Recipe_Map getRecipeMap() {
		return this.mMode == MODE_SCRAP ? GT_Recipe_Map.sRecyclerRecipes : Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes;
		//return Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes;
	}

	@Override
	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {	
		
		if (this.mMode == MODE_SCRAP) {

			long tVoltage = getMaxInputVoltage();
			byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));			
			GT_Recipe c = new Recipe_GT(false, new ItemStack[] { GT_Utility.copyAmount(1, aItemInputs[0]) },
					GT_ModHandler.getRecyclerOutput(GT_Utility.copyAmount(64, aItemInputs[0]), 0) == null ? null
							: new ItemStack[] { ItemList.IC2_Scrap.get(1) },
					null, new int[] { 2000 }, null, null, 100,
					(int) gregtech.api.enums.GT_Values.V[2], 0);
			
			// EU discount
			float tRecipeEUt = (c.mEUt * aEUPercent) / 100.0f;
			float tTotalEUt = 0.0f;

			int parallelRecipes = 0;
			// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
			for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) {
				if (!c.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
					log("Broke at "+parallelRecipes+".");
					break;
				}
				log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
				tTotalEUt += tRecipeEUt;
			}

			if (parallelRecipes == 0) {
				this.mEUt = (int) gregtech.api.enums.GT_Values.V[tTier];
				this.mMaxProgresstime = 10;
				return true;
			}
			
			return super.checkRecipeGeneric(c, getMaxParallelRecipes(), getEuDiscountForParallelism(), aSpeedBonusPercent, aOutputChanceRoll);
		}
		
		//Return normal Recipe handling
		return super.checkRecipeGeneric(aItemInputs, aFluidInputs, getMaxParallelRecipes(), getEuDiscountForParallelism(), aSpeedBonusPercent, aOutputChanceRoll);	
		}	
	
	@Override
	public int getMaxParallelRecipes() {
		return this.mMode == MODE_SCRAP ? 32 : 2 * (Math.max(1, GT_Utility.getTier(getMaxInputVoltage())));
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