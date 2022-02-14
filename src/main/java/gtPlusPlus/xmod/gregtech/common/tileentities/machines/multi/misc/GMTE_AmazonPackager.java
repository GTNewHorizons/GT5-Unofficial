package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemStackData;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GMTE_AmazonPackager extends GregtechMeta_MultiBlockBase<GMTE_AmazonPackager> {

	private long mVoltage;
	private byte mTier;
	private int mCasing;

	private ItemStack mSchematicCache;;
	private ItemStack mInputCache;
	private ItemStack mOutputCache;
	private GT_Recipe mCachedRecipe;

	private IStructureDefinition<GMTE_AmazonPackager> STRUCTURE_DEFINITION = null;

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GMTE_AmazonPackager(mName);
	}

	public GMTE_AmazonPackager(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GMTE_AmazonPackager(String aName) {
		super(aName);
	}

	@Override
	public String getMachineType() {
		return "Packager";
	}

	@Override
	public boolean hasSlotInGUI() {
		return true;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "Generic3By3";
	}

	@Override
	public IStructureDefinition<GMTE_AmazonPackager> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GMTE_AmazonPackager>builder()
					.addShape(mName, transpose(new String[][]{
						{"CCC", "CCC", "CCC"},
						{"C~C", "C-C", "CCC"},
						{"CCC", "CCC", "CCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GMTE_AmazonPackager::addAmazonPackagerList, TAE.getIndexFromPage(2, 9), 1
											),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasings3Misc, 9
													)
											)
									)
							)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}



	public final boolean addAmazonPackagerList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
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
			}
		}
		return false;
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Controller Block for the Amazon Warehouse")
		.addInfo("This Multiblock is used for EXTREME packaging requirements")
		.addInfo("Dust Schematics are inserted into the input busses")
		.addInfo("If inserted into the controller, it is shared across all busses")
		.addInfo("1x, 2x, 3x & Other Schematics are to be placed into the controller GUI slot")
		.addInfo("Uncomparably fast compared to a single packager of the same tier")
		.addInfo("Only uses 75% of the eu/t normally required")
		.addInfo("Processes 16 items per voltage tier")
		.addPollutionAmount(getPollutionPerSecond(null))
		.addSeparator()
		.beginStructureBlock(3, 3, 3, true)
		.addController("Front center")
		.addCasingInfo("Supply Depot Casings", 10)
		.addInputBus("Any casing", 1)
		.addOutputBus("Any casing", 1)
		.addEnergyHatch("Any casing", 1)
		.addMaintenanceHatch("Any casing", 1)
		.addMufflerHatch("Any casing", 1)
		.toolTipFinisher("GT++");
		return tt;
	}

	private final void initFields() {
		mVoltage = getMaxInputVoltage();
		mTier = (byte) Math.max(1, GT_Utility.getTier(mVoltage));
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(2, 9)), new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_Machine_Controller_Default_Active : TexturesGtBlock.Overlay_Machine_Controller_Default)};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.getIndexFromPage(2, 9))};
	}



	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes;
	}

	public void sortInputBusses() {
		for (GT_MetaTileEntity_Hatch_InputBus h : this.mInputBusses) {
			h.updateSlots();
		}
	}

	private static final FluidStack[] sNoFluids = new FluidStack[] {};

	@Override
	public boolean checkRecipe(ItemStack aStack) {	

		//Just the best place to check this~
		initFields();

		ArrayList<ItemStack> tItems = getStoredInputs();
		if (this.getGUIItemStack() != null) {
			tItems.add(this.getGUIItemStack());		
		}
		ItemStack[] tItemInputs = tItems.toArray(new ItemStack[tItems.size()]);
		boolean state = checkRecipeGeneric(tItemInputs, sNoFluids, 16 * GT_Utility.getTier(this.getMaxInputVoltage()), 75, 500, 10000);


		if (state) {
			return true;
		}
		else {
			tItems = getStoredInputs();
			AutoMap<ItemStackData> mCompleted = new AutoMap<ItemStackData>();
			AutoMap<ItemStackData> mSchematics = new AutoMap<ItemStackData>();
			for (ItemStack tInputItem : tItems) {
				if (tInputItem != null) {					
					if (ItemList.Schematic_1by1.isStackEqual((Object) tInputItem) || ItemList.Schematic_2by2.isStackEqual((Object) tInputItem) || ItemList.Schematic_3by3.isStackEqual((Object) tInputItem)) {
						mSchematics.put(new ItemStackData(tInputItem));
					}
				}
			}
			if (mSchematics.size() > 0) {
				for (ItemStackData g : mSchematics) {
					for (ItemStack tInputItem : tItems) {
						if (tInputItem != null) {							
							mCompleted.put(new ItemStackData(tInputItem));
							checkRecipe(tInputItem, g.getStack());	
						}
					}
				}
			}

			return mCompleted != null && mCompleted.size() > 0;
		}		
	}

	public boolean checkRecipe(ItemStack inputStack, ItemStack schematicStack) {		
		if (GT_Utility.isStackValid((Object) inputStack) && GT_Utility.isStackValid((Object) schematicStack)
				&& GT_Utility.getContainerItem(inputStack, true) == null) {			
			ItemStack tOutputStack;			
			if (ItemList.Schematic_1by1.isStackEqual((Object) schematicStack)&& inputStack.stackSize >= 1) {				
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack});
				if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
					final ItemStack input = inputStack;
					--input.stackSize;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					//this.mMaxProgresstime = 16 / (1 << this.mTier - 1);
					this.mMaxProgresstime = 2;
					this.addOutput(tOutputStack);
					updateSlots();
					return true;
				}
				return false;
			} else if (ItemList.Schematic_2by2.isStackEqual((Object) schematicStack)
					&& inputStack.stackSize >= 4) {
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack,
						inputStack, null, inputStack, inputStack});
				if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
					final ItemStack input2 = inputStack;
					input2.stackSize -= 4;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					//this.mMaxProgresstime = 32 / (1 << this.mTier - 1);
					this.mMaxProgresstime = 4;
					this.addOutput(tOutputStack);
					updateSlots();
					return true;
				}
				return false;
			} else if (ItemList.Schematic_3by3.isStackEqual((Object) schematicStack)
					&& inputStack.stackSize >= 9) {
				tOutputStack = GT_ModHandler.getRecipeOutput(new ItemStack[]{inputStack,
						inputStack, inputStack, inputStack, inputStack,
						inputStack, inputStack, inputStack, inputStack});
				if (tOutputStack != null && this.allowPutStack(tOutputStack, schematicStack)) {
					final ItemStack input3 = inputStack;
					input3.stackSize -= 9;
					this.mEUt = 32 * (1 << this.mTier - 1) * (1 << this.mTier - 1);
					//this.mMaxProgresstime = 64 / (1 << this.mTier - 1);
					this.mMaxProgresstime = 6;
					this.addOutput(tOutputStack);
					updateSlots();
					return true;
				}
				return false;
			}
		}
		return false;
	}

	private ItemStack getSchematic(ItemStack[] aInputs) {
		for (ItemStack aStack : aInputs) {
			if (ItemList.Schematic_Dust.isStackEqual(aStack) || ItemList.Schematic_1by1.isStackEqual(aStack) || ItemList.Schematic_2by2.isStackEqual(aStack) || ItemList.Schematic_3by3.isStackEqual(aStack)) {
				return aStack;
			}
		}
		return null;
	}

	private ItemStack getRecipeInput(ItemStack[] aInputs) {
		for (ItemStack aStack : aInputs) {
			if (!ItemList.Schematic_Dust.isStackEqual(aStack) && !ItemList.Schematic_1by1.isStackEqual(aStack) && !ItemList.Schematic_2by2.isStackEqual(aStack) && !ItemList.Schematic_3by3.isStackEqual(aStack)) {
				return aStack;
			}
		}
		return null;
	}

	private boolean hasValidCache(ItemStack aStack, ItemStack aSchematic, boolean aClearOnFailure) {
		if (mSchematicCache != null && mInputCache != null && mOutputCache != null && mCachedRecipe != null) {        	
			if (GT_Utility.areStacksEqual(aStack, mInputCache) && GT_Utility.areStacksEqual(aSchematic, mSchematicCache)) {
				return true;        		
			}        	
		}
		// clear cache if it was invalid
		if (aClearOnFailure) {
			mSchematicCache = null;
			mInputCache = null;
			mOutputCache = null;
			mCachedRecipe = null;
		}
		return false;
	}

	private void cacheItem(ItemStack aSchematic, ItemStack aInputItem, ItemStack aOutputItem, GT_Recipe aRecipe) {
		mSchematicCache = aSchematic.copy();
		mInputCache = aInputItem.copy();
		mOutputCache = aOutputItem.copy();
		mCachedRecipe = aRecipe;
	}

	private GT_Recipe generatePackageRecipe(ItemStack aSchematic, ItemStack aInput) {
		boolean tIsCached = hasValidCache(aInput, aSchematic, true);
		if (tIsCached) {
			ItemStack tOutput = mOutputCache.copy();			
			if (tOutput != null) {				
				if (mCachedRecipe != null && GT_Utility.areStacksEqual(aInput, mInputCache) && GT_Utility.areStacksEqual(tOutput, mOutputCache)) {
					int aRequiredInputSize = 0;
					if (ItemList.Schematic_Dust.isStackEqual(aSchematic)) {
						if (OrePrefixes.dustTiny.contains(aInput)) {
							aRequiredInputSize = 9;
						}
						if (OrePrefixes.dustSmall.contains(aInput)) {
							aRequiredInputSize = 4;
						}
						if (OrePrefixes.dust.contains(aInput)) {
							aRequiredInputSize = 1;
						}
					}
					if (ItemList.Schematic_1by1.isStackEqual(aSchematic)) {
						aRequiredInputSize = 1;
					}
					if (ItemList.Schematic_2by2.isStackEqual(aSchematic)) {
						aRequiredInputSize = 4;
					}
					if (ItemList.Schematic_3by3.isStackEqual(aSchematic)) {
						aRequiredInputSize = 9;
					}
					if (aInput.stackSize >= aRequiredInputSize) {
						log("Using Cached Recipe. Require: "+aRequiredInputSize+", Found: "+aInput.stackSize);
						return mCachedRecipe;	
					}
					else {
						log("Not enough input");
					}					
				}				
			}
		}
		// We can package this
		GT_Recipe aRecipe = lookupRecipe();
		log("Looking up new recipe");
		if (aRecipe != null) {
			// Cache it
			aInput = aInput != null ? aInput : getRecipeInput(aRecipe.mInputs);
			cacheItem(aSchematic, aInput, aRecipe.mOutputs[0], aRecipe);	
			if (hasValidCache(aInput, aSchematic, false)) {
				log("Caching Recipe");
				return aRecipe;
			}
		}
		return null;
	}

	private GT_Recipe lookupRecipe() {
		ArrayList<ItemStack> aItems = getStoredInputs();
		if (this.getGUIItemStack() != null) {
			aItems.add(this.getGUIItemStack());		
		}
		ItemStack[] aItemInputs = aItems.toArray(new ItemStack[aItems.size()]);
		GT_Recipe tRecipe = findRecipe(
				getBaseMetaTileEntity(), mLastRecipe, false, false,
				gregtech.api.enums.GT_Values.V[mTier], sNoFluids, aItemInputs);	

		if (tRecipe != null) {
			return tRecipe;
		}
		return null;
	}

	@Override
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

		ItemStack aInput = getRecipeInput(aItemInputs);
		ItemStack aSchematic = getSchematic(aItemInputs);
		GT_Recipe tRecipe = generatePackageRecipe(aSchematic, aInput);
		
		ItemStack[] aRealInputs = new ItemStack[] {aSchematic, aInput};
		log("Running checkRecipeGeneric(1)");
		// Remember last recipe - an optimization for findRecipe()
		this.mLastRecipe = tRecipe;

		if (tRecipe == null) {
			log("BAD RETURN - 1");
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
			if (!tRecipe.isRecipeInputEqual(true, sNoFluids, aItemInputs)) {
				log("Broke at "+parallelRecipes+".");
				break;
			}
			log("Bumped EU from "+tTotalEUt+" to "+(tTotalEUt+tRecipeEUt)+".");
			tTotalEUt += tRecipeEUt;
		}

		if (parallelRecipes == 0) {
			mCachedRecipe = null;
			log("BAD RETURN - 3 - Reset Cached Recipe");
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
				if (this.hasPerfectOverclock()) this.mMaxProgresstime /= 4;
				else this.mMaxProgresstime /= 2;
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

	public boolean allowPutStack(final ItemStack aStack, ItemStack schematicStack) {
		//If Schematic Static is not 1x1, 2x2, 3x3
		if (!ItemList.Schematic_1by1.isStackEqual((Object) schematicStack) && !ItemList.Schematic_2by2.isStackEqual((Object) schematicStack) && !ItemList.Schematic_3by3.isStackEqual((Object) schematicStack)) {
			return GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.containsInput(aStack);
		}
		//Something
		if (GT_Recipe.GT_Recipe_Map.sBoxinatorRecipes.findRecipe((IHasWorldObjectAndCoords) this.getBaseMetaTileEntity(), true, GT_Values.V[this.mTier],
				(FluidStack[]) null, new ItemStack[]{GT_Utility.copyAmount(64L, new Object[]{aStack}), schematicStack}) != null) {
			return true;
		}
		//1x1
		if (ItemList.Schematic_1by1.isStackEqual((Object) schematicStack)
				&& GT_ModHandler.getRecipeOutput(new ItemStack[]{aStack}) != null) {
			return true;
		}
		//2x2
		if (ItemList.Schematic_2by2.isStackEqual((Object) schematicStack)
				&& GT_ModHandler.getRecipeOutput(new ItemStack[]{aStack, aStack, null, aStack, aStack}) != null) {
			return true;
		}
		//3x3
		if (ItemList.Schematic_3by3.isStackEqual((Object) schematicStack) && GT_ModHandler.getRecipeOutput(
				new ItemStack[]{aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack, aStack}) != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
	}

	@Override
	public int getMaxEfficiency(ItemStack p0) {
		return 10000;
	}

	@Override
	public int getPollutionPerSecond(ItemStack arg0){
		return CORE.ConfigSwitches.pollutionPerSecondMultiPackager;
	}

	@Override
	public int getMaxParallelRecipes() {
		return 9;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
	}
}
