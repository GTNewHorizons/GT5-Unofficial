package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gtPlusPlus.core.util.data.ArrayUtils.removeNulls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe.GTPP_Recipe_Map;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_DataOrb;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_ElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_ElementalDuplicator extends GregtechMeta_MultiBlockBase<GregtechMTE_ElementalDuplicator> {

	private ArrayList<GT_MetaTileEntity_Hatch_ElementalDataOrbHolder> mReplicatorDataOrbHatches = new ArrayList<GT_MetaTileEntity_Hatch_ElementalDataOrbHolder>();
	private static final int CASING_TEXTURE_ID = TAE.getIndexFromPage(0, 3);
	private int mCasing = 0;

	public GregtechMTE_ElementalDuplicator(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_ElementalDuplicator(final String aName) {
		super(aName);
	}

	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_ElementalDuplicator(this.mName);
	}

	@Override
	public String getMachineType() {
		return "Replicator";
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {

		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Produces Elemental Material from UU Matter")
		.addInfo("Speed: 100% | Eu Usage: 100% | Parallel: 8 * Tier")
		.addInfo("This multiblock cannot be overclocked")
		.addInfo("Maximum 1x of each bus/hatch.")
		.addInfo("Does not require both Output Hatch & Bus")
		.addPollutionAmount(getPollutionPerSecond(null))
		.addSeparator()
		.beginStructureBlock(9, 6, 9, true)
		.addController("Top Center")
		.addCasingInfo("Elemental Confinement Shell", 138)
		.addCasingInfo("Matter Fabricator Casing", 24)
		.addCasingInfo("Containment Casing", 24)
		.addCasingInfo("Matter Generation Coil", 24)
		.addCasingInfo("High Voltage Current Capacitor", 20)
		.addCasingInfo("Resonance Chamber III", 24)
		.addCasingInfo("Modulator III", 16)
		.addOtherStructurePart("Data Orb Repository", "1x", 1)
		.addInputHatch("Any 1 dot hint", 1)
		.addOutputBus("Any 1 dot hint", 1)
		.addOutputHatch("Any 1 dot hint", 1)
		.addEnergyHatch("Any 1 dot hint", 1)
		.addMaintenanceHatch("Any 1 dot hint", 1)
		.addMufflerHatch("Any 1 dot hint", 1)
		.toolTipFinisher(CORE.GT_Tooltip_Builder);
		return tt;
	}


	private static final String STRUCTURE_PIECE_MAIN = "main";
	private IStructureDefinition<GregtechMTE_ElementalDuplicator> STRUCTURE_DEFINITION = null;

	@Override
	public IStructureDefinition<GregtechMTE_ElementalDuplicator> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_ElementalDuplicator>builder()

					// h = Hatch
					// c = Casing

					// a = MF Casing 1
					// b = Matter Gen Coil

					// d = Current Capacitor
					// e = Particle

					// f = Resonance III
					// g = Modulator III

					.addShape(STRUCTURE_PIECE_MAIN, (new String[][]{
						{"   ccc   ", "  ccccc  ", " ccccccc ", "ccchhhccc", "ccch~hccc", "ccchhhccc", " ccccccc ", "  ccccc  ", "   ccc   "},
						{"   cac   ", "  abfba  ", " abfgfba ", "cbfgdgfbc", "afgdddgfa", "cbfgdgfbc", " abfgfba ", "  abfba  ", "   cac   "},
						{"   cec   ", "  e   e  ", " e     e ", "c   d   c", "e  ddd  e", "c   d   c", " e     e ", "  e   e  ", "   cec   "},
						{"   cec   ", "  e   e  ", " e     e ", "c   d   c", "e  ddd  e", "c   d   c", " e     e ", "  e   e  ", "   cec   "},
						{"   cac   ", "  abfba  ", " abfgfba ", "cbfgdgfbc", "afgdddgfa", "cbfgdgfbc", " abfgfba ", "  abfba  ", "   cac   "},
						{"   ccc   ", "  ccccc  ", " ccccccc ", "ccchhhccc", "ccchhhccc", "ccchhhccc", " ccccccc ", "  ccccc  ", "   ccc   "},
					}))						


					.addElement('a', ofBlock(getCasingBlock4(), getCasingMeta6()))
					.addElement('b', ofBlock(getCasingBlock4(), getCasingMeta7()))

					.addElement('d', ofBlock(getCasingBlock2(), getCasingMeta2()))
					.addElement('e', ofBlock(getCasingBlock2(), getCasingMeta3()))

					.addElement('f', ofBlock(getCasingBlock3(), getCasingMeta4()))
					.addElement('g', ofBlock(getCasingBlock3(), getCasingMeta5()))					
					.addElement('c', lazy(t -> onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
					.addElement('h', lazy(t -> ofChain(
							ofHatchAdder(GregtechMTE_ElementalDuplicator::addGenericHatch, getCasingTextureIndex(), 1),
							onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))
							)))
					.build();	
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(STRUCTURE_PIECE_MAIN , stackSize, hintsOnly, 4, 4, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		boolean aDidBuild = checkPiece(STRUCTURE_PIECE_MAIN, 4, 4, 0);
		if (this.mInputHatches.size() != 1 || (this.mOutputBusses.size() != 1 && this.mOutputHatches.size() !=0) || this.mEnergyHatches.size() != 1 || this.mReplicatorDataOrbHatches.size() != 1) {
			return false;
		}
		log("Casings: "+mCasing);
		return aDidBuild && mCasing >= 138 && checkHatch();
	}	

	protected static int getCasingTextureIndex() {
		return CASING_TEXTURE_ID;
	}

	protected static Block getCasingBlock() {
		return ModBlocks.blockCasings5Misc;
	}

	protected static Block getCasingBlock2() {
		return ModBlocks.blockSpecialMultiCasings;
	}

	protected static Block getCasingBlock3() {
		return ModBlocks.blockSpecialMultiCasings2;
	}

	protected static Block getCasingBlock4() {
		return ModBlocks.blockCasingsMisc;
	}

	protected static int getCasingMeta() {
		return 3;
	}

	protected static int getCasingMeta2() {
		return 12;
	}

	protected static int getCasingMeta3() {
		return 13;
	}

	protected static int getCasingMeta4() {
		return 2;
	}

	protected static int getCasingMeta5() {
		return 6;
	}

	protected static int getCasingMeta6() {
		return 9;
	}

	protected static int getCasingMeta7() {
		return 8;
	}

	public final boolean addGenericHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} 
		else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity == null) {
				return false;
			}
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
				return addToMachineList(aTileEntity, aBaseCasingIndex);
			}
			else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_ElementalDataOrbHolder) {
				try {
					((GT_MetaTileEntity_Hatch_ElementalDataOrbHolder) aMetaTileEntity).mRecipeMap = getRecipeMap();            
					return addToMachineListInternal(mReplicatorDataOrbHatches, aMetaTileEntity, aBaseCasingIndex);
				}
				catch (Throwable t) {
					t.printStackTrace();
				}
			} 
		}
		return false;
	}

	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing,
			final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID),
					new GT_RenderedTexture((IIconContainer) (aActive ? TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active : TexturesGtBlock.Overlay_Machine_Controller_Advanced))};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(CASING_TEXTURE_ID)};
	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public boolean requiresVanillaGtGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return null;
	}

	public GT_Recipe.GT_Recipe_Map getRecipeMap() {				
		return GTPP_Recipe_Map.sElementalDuplicatorRecipes;
	}

	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		return checkRecipeGeneric(getMaxParallelRecipes(), 100, 100);
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

		GT_Recipe tRecipe = null;

		try {
			log("Checking "+aItemInputs.length+" Data Orbs");

			for (int i=0;i<aItemInputs.length;i++) {
				ItemStack aItem = aItemInputs[i];
				log("Found: "+aItem.getDisplayName());
			}
			ItemStack aDataOrbStack = null;
			recipe : for (GT_Recipe nRecipe : this.getRecipeMap().mRecipeList) {
				log("Checking Recipe for: "+(nRecipe.mOutputs.length > 0 && nRecipe.mOutputs[0] != null ? nRecipe.mOutputs[0].getDisplayName() : nRecipe.mFluidOutputs[0].getLocalizedName()));
				ItemStack aTempStack = getSpecialSlotStack(nRecipe);
				if (aTempStack != null) {
					for (ItemStack aItem : aItemInputs) {
						if (nRecipe.mSpecialItems != null) {
							if (GT_Utility.areStacksEqual(aTempStack, aItem, false)) {
								Materials tMaterial = Element.get(Behaviour_DataOrb.getDataName(aTempStack)).mLinkedMaterials.get(0);
								log("Found: "+aTempStack.getDisplayName()+" for "+tMaterial.name());
								aDataOrbStack = aTempStack;
								break recipe;
							}				
						}
					}
				}				
			}
			if (aDataOrbStack != null) {
				tRecipe = findRecipe(
						getBaseMetaTileEntity(), mLastRecipe, false, false,
						gregtech.api.enums.GT_Values.V[tTier], aFluidInputs, aDataOrbStack, aItemInputs);
				if (tRecipe != null) {
					Materials tMaterial = Element.get(Behaviour_DataOrb.getDataName(aDataOrbStack)).mLinkedMaterials.get(0);
					log("Found recipe for "+tMaterial.name());
				}
				else {
					log("No Recipe Found");
				}
			}
			else {
				log("Null DO");
			}

		}
		catch (Throwable t) {
			t.printStackTrace();
		}



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
				this.mMaxProgresstime /= 4;
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


	@Override
	public int getMaxParallelRecipes() {
		return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 100;
	}

	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	public int getPollutionPerSecond(final ItemStack aStack) {
		return CORE.ConfigSwitches.pollutionPerSecondMultiMolecularTransformer;
	}

	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return false;
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);		
		// Fix GT bug
		if (this.getBaseMetaTileEntity().getFrontFacing() != 1) {
			this.getBaseMetaTileEntity().setFrontFacing((byte) 1); 
		}
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			if (this.mUpdate == 1 || this.mStartUpCheck == 1) {
				this.mReplicatorDataOrbHatches.clear();
			}
		}
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

	@Override
	public ArrayList<ItemStack> getStoredInputs() {
		ArrayList<ItemStack> tItems = super.getStoredInputs();
		for (GT_MetaTileEntity_Hatch_ElementalDataOrbHolder tHatch : mReplicatorDataOrbHatches) {
			tHatch.mRecipeMap = getRecipeMap();
			if (isValidMetaTileEntity(tHatch)) {
				tItems.addAll(tHatch.getInventory());               
			}
		}       
		tItems.removeAll(Collections.singleton(null));
		return tItems;
	}

	/**
	 * finds a Recipe matching the aFluid and ItemStack Inputs.
	 *
	 * @param aTileEntity    an Object representing the current coordinates of the executing Block/Entity/Whatever. This may be null, especially during Startup.
	 * @param aRecipe        in case this is != null it will try to use this Recipe first when looking things up.
	 * @param aNotUnificated if this is T the Recipe searcher will unificate the ItemStack Inputs
	 * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with the provided input
	 * @param aVoltage       Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
	 * @param aFluids        the Fluid Inputs
	 * @param aSpecialSlot   the content of the Special Slot, the regular Manager doesn't do anything with this, but some custom ones do.
	 * @param aInputs        the Item Inputs
	 * @return the Recipe it has found or null for no matching Recipe
	 */
	public GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot, ItemStack... aInputs) {

		GT_Recipe_Map mRecipeMap = this.getRecipeMap();
		// No Recipes? Well, nothing to be found then.
		if (mRecipeMap.mRecipeList.isEmpty()) {
			return null;
		}

		// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
		// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
		if (GregTech_API.sPostloadFinished) {
			if (mRecipeMap.mMinimalInputFluids > 0) {
				if (aFluids == null) return null;
				int tAmount = 0;
				for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
				if (tAmount < mRecipeMap.mMinimalInputFluids) return null;
			}
			if (mRecipeMap.mMinimalInputItems > 0) {
				if (aInputs == null) return null;
				int tAmount = 0;
				for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
				if (tAmount < mRecipeMap.mMinimalInputItems) return null;
			}
		}

		// Unification happens here in case the Input isn't already unificated.
		if (aNotUnificated) {
			aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
		}

		// Check the Recipe which has been used last time in order to not have to search for it again, if possible.
		if (aRecipe != null) {
			ItemStack aRecipeSpecial = getSpecialSlotStack(aRecipe);
			if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs) && GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false) && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
				return aRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= aRecipe.mEUt ? aRecipe : null;
			}
		}

		// Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
		if (mRecipeMap.mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs)
			if (tStack != null) {
				Collection<GT_Recipe> tRecipes = mRecipeMap.mRecipeItemMap.get(new GT_ItemStack(tStack));
				if (tRecipes != null) {
					for (GT_Recipe tRecipe : tRecipes) {
						if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
							ItemStack aRecipeSpecial = getSpecialSlotStack(tRecipe);
							if (GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false) && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
								return tRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= tRecipe.mEUt ? tRecipe : null;										
							}
						}
						tRecipes = mRecipeMap.mRecipeItemMap.get(new GT_ItemStack(tStack, true));
					}
				}
				if (tRecipes != null) {
					for (GT_Recipe tRecipe : tRecipes) {
						if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
							ItemStack aRecipeSpecial = getSpecialSlotStack(tRecipe);
							if (GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false) && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
								return tRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= tRecipe.mEUt ? tRecipe : null;
							}
						}
					}
				}
			}

		// If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
		if (mRecipeMap.mMinimalInputItems == 0 && aFluids != null) for (FluidStack aFluid : aFluids)
			if (aFluid != null) {
				Collection<GT_Recipe>
				tRecipes = mRecipeMap.mRecipeFluidMap.get(aFluid.getFluid());
				if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes) {
					if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
						ItemStack aRecipeSpecial = getSpecialSlotStack(tRecipe);
						if (GT_Utility.areStacksEqual(aRecipeSpecial, aSpecialSlot, false) && areDataOrbsEqual(aRecipeSpecial, aSpecialSlot)) {
							return tRecipe.mEnabled && aVoltage * mRecipeMap.mAmperage >= tRecipe.mEUt ? tRecipe : null;
						}
					}
				}
			}

		// And nothing has been found.
		return null;
	}

	public static ItemStack getSpecialSlotStack(GT_Recipe aRecipe) {
		ItemStack aStack = null;
		if (aRecipe.mSpecialItems != null) {
			if (aRecipe.mSpecialItems instanceof ItemStack[]) {
				ItemStack[] aTempStackArray = (ItemStack[]) aRecipe.mSpecialItems;
				aStack = aTempStackArray[0];				
			}					
		}
		return aStack;
	}

	private static boolean areDataOrbsEqual(ItemStack aOrb1, ItemStack aOrb2) {
		if (aOrb1 != null && aOrb2 != null) {
			Materials tMaterial1 = Element.get(Behaviour_DataOrb.getDataName(aOrb1)).mLinkedMaterials.get(0);
			Materials tMaterial2 = Element.get(Behaviour_DataOrb.getDataName(aOrb2)).mLinkedMaterials.get(0);
			if (tMaterial1.equals(tMaterial2)) {
				return true;
			}
		}

		return false;
	}

}