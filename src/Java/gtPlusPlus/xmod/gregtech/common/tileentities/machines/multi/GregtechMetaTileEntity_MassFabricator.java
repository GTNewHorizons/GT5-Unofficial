package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static gtPlusPlus.core.util.array.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.GenericStack;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.AutoMap;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MatterFab;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntity_MassFabricator extends GregtechMeta_MultiBlockBase {


	public final static int JUNK_TO_SCRAP = 19;
	public final static int JUNK_TO_UUA = 20;
	public final static int SCRAP_UUA = 21;
	public final static int PRODUCE_UUM = 22;
	public static int sUUAperUUM = 1;
	public static int sUUASpeedBonus = 4;
	public static int sDurationMultiplier = 3200;
	private int mMatterProduced = 0;
	private int mScrapProduced = 0;
	private int mAmplifierProduced = 0;
	private int mScrapUsed = 0;
	private int mAmplifierUsed = 0;
	private int mMode = 0;

	public static boolean sRequiresUUA = false;
	private static FluidStack[] mUU = new FluidStack[2];
	private static ItemStack mScrap[] = new ItemStack[2];
	private static Block IC2Glass = Block.getBlockFromItem(ItemUtils.getItem("IC2:blockAlloyGlass"));

	private GT_Recipe mFakeRecipe;

	public int getAmplifierUsed(){
		return this.mAmplifierUsed;
	}

	public int getMatterProduced(){
		return this.mMatterProduced;
	}

	public GregtechMetaTileEntity_MassFabricator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_MassFabricator(final String aName) {
		super(aName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Matter Fabricator",
				"Produces UU-Matter from UU-Amplifier",
				"Size(WxHxD): 5x4x5, Controller (Bottom center)",
				"3x1x3 Matter Generation Coils (Inside bottom 5x1x5 layer)",
				"9x Matter Generation Coils (Centered 3x1x3 area in Bottom layer)",
				"1x Input Hatch (Any bottom layer casing)",
				"1x Output Hatch (Any bottom layer casing)",
				"1x Maintenance Hatch (Any bottom layer casing)",
				"1x Muffler Hatch (Centered 3x1x3 area in Top layer)",
				"1x Energy Hatch (Any bottom layer casing)",
				"24x IC2 Reinforced Glass for the walls",
				"Matter Fabricator Casings for the edges & top (40 at least!)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)],
					new GT_RenderedTexture(aActive ? TexturesGtBlock.Casing_Machine_Screen_3 : TexturesGtBlock.Casing_Machine_Screen_1)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(9)]};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	public ItemStack getScrapPile() {
		if (mScrap[0] == null) {
			mScrap[0] = ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrap"));
		}
		return mScrap[0];
	}	
	public ItemStack getScrapBox() {		
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
		Materials.UUAmplifier.mChemicalFormula = ("Mass Fabricator Eff/Speed Bonus: x" + sUUASpeedBonus);
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		ArrayList<ItemStack> tItems = getStoredInputs();
		ArrayList<FluidStack> tFluids = getStoredFluids();
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		FluidStack[] tFluidInputs = tFluids.toArray(new FluidStack[tFluids.size()]);

		if (tItems.size() == 0) {
			return false;
		}

		return checkRecipeGeneric(tItemInputs, tFluidInputs, 1, 500, 75, 100);
	}		


	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {
		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 2;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 2;
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
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Logger.INFO("Glass Casings Missing from somewhere in the second layer.");
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
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != IC2Glass) {
										Logger.INFO("Glass Casings Missing from somewhere in the third layer.");
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
		return 0;
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

	public static Map<Integer, GT_Recipe> mCachedRecipeMap = new ConcurrentHashMap<Integer, GT_Recipe>();

	/**
	 * Special Recipe Generator
	 */

	private GT_Recipe generateCustomRecipe(int mode, ItemStack[] aItemInputs) {
		ItemStack[] inputs = null;
		ItemStack[] outputs = null;
		FluidStack fluidIn = null;
		FluidStack fluidOut = null;		
		Pair<Integer, ItemStack[]> K = new Pair<Integer, ItemStack[]>(mode, aItemInputs);
		if (mCachedRecipeMap.containsKey(K.hashCode())) {
			return mCachedRecipeMap.get(K.hashCode());
		}

		final boolean oldRecipe = Utils.invertBoolean(CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK);

		int baseEuCost = 0;
		int baseTimeCost = 0;

		final int mEuPerRecycleOperation = 32;
		final int mTicksPerRecycleOperation = 4;
		final int mEuPerAmplifabOperation = 32;
		final int mTicksPerAmplifabOperation = 160*20;
		final int mEuPerMatterFabOperation = 32;
		final int mTicksPerMatterFabOperation = 160*20;

		final Item SP = this.getScrapPile().getItem();
		final Item SB = this.getScrapBox().getItem();

		/**
		 * Count Inputs
		 */	

		int inputCount = 0;
		if (aItemInputs.length > 0) {
			for (int y=0;y<aItemInputs.length;y++) {
				if (aItemInputs[y].getItem() != SP && aItemInputs[y].getItem() != SB) {
					inputCount += aItemInputs[y].stackSize;
				}
			}
		}


		/**
		 * Set Inputs and Outputs depending on mode.
		 */

		//Recycler mode
		if (mode == JUNK_TO_SCRAP) {
			if (aItemInputs.length > 0) {
				inputs = aItemInputs;
				outputs = getScrapPiles(inputCount);
				baseEuCost = mEuPerRecycleOperation;
				baseTimeCost = mTicksPerRecycleOperation;
			}
		}

		//Hybrid mode
		else if (mode == JUNK_TO_UUA) {
			if (aItemInputs.length > 0) {
				inputs = aItemInputs;
				GenericStack x = getUUAFromScrapStack(getScrapPiles(inputCount));
				outputs = new ItemStack[]{x.getItemStack()};
				fluidOut = x.getFluidStack();
				baseEuCost = 512;
				baseTimeCost = mTicksPerRecycleOperation;		
			}
		}

		//Amplifabricator mode
		else if (mode == SCRAP_UUA) {
			if (aItemInputs.length > 0) {				
				inputs = aItemInputs;
				GenericStack x = getUUAFromScrapStack(inputs);
				outputs = new ItemStack[]{x.getItemStack()};
				fluidOut = x.getFluidStack();				
				baseEuCost = mEuPerAmplifabOperation;
				baseTimeCost = mTicksPerAmplifabOperation;		
			}
		}

		//Matter Fabricator mode
		else if (mode == PRODUCE_UUM) {
			if (sDurationMultiplier != 0) {
				baseTimeCost = sDurationMultiplier;
			}
			if (doesHatchContainUUA()) {
				fluidIn = FluidUtils.getFluidStack(mUU[0], sUUAperUUM);				
			}
			else {	
				if (sRequiresUUA) {
					//Return null because if this is the case, the machine cannot run at all.
					return null;
				}
				else {
					fluidIn = GT_Values.NF;
				}
			}
			fluidOut = FluidUtils.getFluidStack(mUU[1], 1);
			baseEuCost = mEuPerMatterFabOperation;
			baseTimeCost =  (fluidIn == GT_Values.NF ? mTicksPerMatterFabOperation: mTicksPerMatterFabOperation/sUUASpeedBonus);			
		}


		//Pre 5.09 compat
		if (oldRecipe) {
			baseEuCost = (baseEuCost/8);
		}


		Recipe_GT B = new Recipe_GT(
				true,
				inputs, //Inputs
				outputs, //Outputs
				null, // Special?
				new int[] {10000}, //Chances
				new FluidStack[] {fluidIn}, //Fluid Inputs
				new FluidStack[] {fluidOut}, //Fluid Outputs
				baseTimeCost, //duration
				baseEuCost, //eu/t
				0);

		mCachedRecipeMap.put(K.hashCode(), B);		
		Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(B);

		//The Recipe Itself.
		return B;
	}

	private ItemStack[] getScrapPiles(int inputSize) {
		//Generate Trash
		ItemStack[] mOutputs;
		AutoMap<ItemStack> mTemp = new AutoMap<ItemStack>();
		for (int r=0;r<inputSize;r++) {
			if (MathUtils.randInt(0, 5) == 5) {
				mTemp.put(getScrapPile());
			}
		}
		int mSlots = (int) Math.ceil((mTemp.size()/64) / 100.0);
		mOutputs = new ItemStack[mSlots];
		int totalScrap = mTemp.size();
		int index = 0;		
		while (totalScrap > 0) {
			if (mOutputs[index].stackSize == 64) {
				index++;
			}
			else {
				if (mOutputs[index] == null) {
					mOutputs[index] = getScrapPile();
					totalScrap--;
				}
				else {
					mOutputs[index].stackSize++;
					totalScrap--;
				}
			}
		}
		return mOutputs;
	}

	public GenericStack getUUAFromScrapStack(ItemStack[] scrapStack) {
		//9=1
		int mbUUA = 0;		
		int temp = 0;
		for (int u=0;u<scrapStack.length;u++) {
			temp++;
			if(temp == 9) {
				temp = 0;
				mbUUA++;
			}
		}

		int remainder = (scrapStack.length % 9);		
		GenericStack mOutput = new GenericStack();
		ItemStack mScrapOutput = null;
		FluidStack mUUAOutput = null;
		if (remainder > 0) {
			mScrapOutput= ItemUtils.getSimpleStack(getScrapPile(), remainder);			
		}
		if (mbUUA > 0) {
			mUUAOutput = FluidUtils.getFluidStack(mUU[0], mbUUA);			
		}		
		mOutput.setItemStack(mScrapOutput);
		mOutput.setFluidStack(mUUAOutput);		
		return mOutput;
	}

	private GT_Recipe getFakeRecipeForMode(ItemStack[] aItemInputs) {		
		if (this.mMode == JUNK_TO_SCRAP) {			
			return generateCustomRecipe(JUNK_TO_SCRAP, aItemInputs);
		}
		else if (this.mMode == JUNK_TO_UUA) {
			return generateCustomRecipe(JUNK_TO_UUA, aItemInputs);			
		}
		else if (this.mMode == SCRAP_UUA) {
			return generateCustomRecipe(SCRAP_UUA, aItemInputs);		
		}
		else if (this.mMode == PRODUCE_UUM) {
			return generateCustomRecipe(PRODUCE_UUM, aItemInputs);		
		}
		else {
			return null;
		}		
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
		return null;
		//return Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes;
	}

	@Override
	public boolean checkRecipeGeneric(
			ItemStack[] aItemInputs, FluidStack[] aFluidInputs,
			int aMaxParallelRecipes, int aEUPercent,
			int aSpeedBonusPercent, int aOutputChanceRoll) {

		// Based on the Processing Array. A bit overkill, but very flexible.
		// Reset outputs and progress stats
		this.mEUt = 0;
		this.mMaxProgresstime = 0;
		this.mOutputItems = new ItemStack[]{};
		this.mOutputFluids = new FluidStack[]{};
		//Set Mode
		this.mMode = getGUICircuit(aItemInputs);
		mFakeRecipe = getFakeRecipeForMode(aItemInputs);

		long tVoltage = getMaxInputVoltage();
		byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

		GT_Recipe tRecipe = mFakeRecipe;

		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;

		if (tRecipe == null) {
			return false;
		}

		if (!this.canBufferOutputs(tRecipe, aMaxParallelRecipes)) {
			return false;
		}

		// EU discount
		float tRecipeEUt = (tRecipe.mEUt * aEUPercent) / 100.0f;
		float tTotalEUt = 0.0f;

		int parallelRecipes = 0;

		// Count recipes to do in parallel, consuming input items and fluids and considering input voltage limits
		for (; parallelRecipes < aMaxParallelRecipes && tTotalEUt < (tVoltage - tRecipeEUt); parallelRecipes++) {
			if (!tRecipe.isRecipeInputEqual(true, aFluidInputs, aItemInputs)) {
				break;
			}
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
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

		/**
		 * Amp Stat Recording
		 */

		for (int u=0;u<tRecipe.mFluidInputs.length;u++) {
			if (tRecipe.mFluidInputs[u].isFluidEqual(mUU[0])) {
				if (tRecipe.mFluidInputs[u].amount > 0) {
					mAmplifierUsed += tRecipe.mFluidInputs[u].amount;
				}
			}
		}		
		for (int u=0;u<tOutputFluids.length;u++) {
			if (tOutputFluids[u].isFluidEqual(mUU[0])) {
				mAmplifierProduced += tOutputFluids[u].amount;
			}
			/**
			 * UUM Stat Recording
			 */	
			if (tOutputFluids[u].isFluidEqual(mUU[1])) {
				mMatterProduced += tOutputFluids[u].amount;
			}
		}

		/**
		 * Scrap Stat Recording
		 */
		for (int u=0;u<tRecipe.mInputs.length;u++) {
			if (tRecipe.mInputs[u].getItem() == getScrapPile().getItem()) {
				mScrapUsed += tRecipe.mInputs[u].stackSize;
			}
		}		
		for (int u=0;u<tOutputItems.length;u++) {
			if (tOutputItems[u].getItem() == getScrapPile().getItem()) {
				mScrapProduced += tOutputItems[u].stackSize;
			}
		}

		// Play sounds (GT++ addition - GT multiblocks play no sounds)
		startProcess();

		return true;
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