package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

import java.util.ArrayList;
import java.util.Collection;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MatterFab;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

	private int mCasing;
	private IStructureDefinition<GregtechMetaTileEntity_MassFabricator> STRUCTURE_DEFINITION = null;

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
	protected GT_Multiblock_Tooltip_Builder createTooltip() {

		if (mCasingName1.toLowerCase().contains(".name")) {
			mCasingName1 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 9);
		}
		if (mCasingName2.toLowerCase().contains(".name")) {
			mCasingName2 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasings3Misc, 15);
		}
		if (mCasingName3.toLowerCase().contains(".name")) {
			mCasingName3 = ItemUtils.getLocalizedNameOfBlock(ModBlocks.blockCasingsMisc, 8);
		}

		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
				.addInfo("Controller Block for the Matter Fabricator")
				.addInfo("Produces UU-A, UU-M & Scrap")
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
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(9)),
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_MatterFab_Active_Animated : TexturesGtBlock.Overlay_MatterFab_Animated)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(9))};
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
			mScrap[0] = ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrap"));
		}
		return mScrap[0];
	}	
	public static ItemStack getScrapBox() {		
		if (mScrap[1] == null) {
			mScrap[1] = ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrapbox"));
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
		return this.mMode == MODE_SCRAP ? GT_Recipe_Map.sRecyclerRecipes : GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes;
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
			long tEnergy = getMaxInputEnergy();		
			GT_Recipe c = new GTPP_Recipe(false, new ItemStack[] { GT_Utility.copyAmount(1, aItemInputs[0]) },
					GT_ModHandler.getRecyclerOutput(GT_Utility.copyAmount(64, aItemInputs[0]), 0) == null ? null
							: new ItemStack[] { ItemList.IC2_Scrap.get(1) },
					null, new int[] { 2000 }, null, null, 100,
					(int) gregtech.api.enums.GT_Values.V[2], 0);
			
			// EU discount
			float tRecipeEUt = (c.mEUt * aEUPercent) / 100.0f;
			float tTotalEUt = 0.0f;

			int parallelRecipes = 0;
			// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
			for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tEnergy - tRecipeEUt); parallelRecipes++) {
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
	public boolean hasPerfectOverclock() {
		return true;
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