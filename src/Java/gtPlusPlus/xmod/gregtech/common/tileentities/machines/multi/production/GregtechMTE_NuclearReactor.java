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

import gregtech.api.enums.Materials;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_NuclearReactor extends GregtechMeta_MultiBlockBase {

	private static Fluid mHelium;
	private static Fluid mFluorine;
	protected int mFuelRemaining = 0;

	private int mCasing;
	private IStructureDefinition<GregtechMTE_NuclearReactor> STRUCTURE_DEFINITION = null;


	public GregtechMTE_NuclearReactor(final int aID, final String aName, final String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_NuclearReactor(final String aName) {
		super(aName);
	}

	@Override
	public long maxEUStore() {
		return (640000000L * (Math.min(16, this.mEnergyHatches.size()))) / 16L;
	}

	@Override
	public String getMachineType() {
		return "Reactor";
	}

	@Override
	public GT_Recipe_Map getRecipeMap() {
		return GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes;
	}

	@Override
	protected GT_Multiblock_Tooltip_Builder createTooltip() {
		GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
		tt.addMachineType(getMachineType())
		.addInfo("Controller Block for the Liquid Fluoride Thorium Reactor.")
		.addInfo("Produces Heat & Energy from Radioactive Beta Decay.")
		.addInfo("Outputs U233 every 10 seconds, on average")
		.addInfo("Input Fluorine and Helium for bonus byproducts")
		.addInfo("Input Li2BeF4 and a molten salt as fuel.")
		.addInfo("LiFBeF2ThF4UF4, LiFBeF2ZrF4UF4 or LiFBeF2ZrF4U235")
		.addPollutionAmount(getPollutionPerTick(null) * 20)
		.addSeparator()
		.beginStructureBlock(7, 4, 7, true)
		.addController("Bottom Center")
		.addCasingInfo("Hastelloy-N Reactor Casing", 27)
		.addCasingInfo("Zeron-100 Reactor Shielding", 26)
		.addInputHatch("Top or bottom layer edges", 1)
		.addOutputHatch("Top or bottom layer edges", 1)
		.addDynamoHatch("Top or bottom layer edges", 1)
		.addMaintenanceHatch("Top or bottom layer edges", 1)
		.addMufflerHatch("Top 3x3", 2)
		.addStructureInfo("All dynamos must be IV tier.")
		.addStructureInfo("All other hatches must be IV+ tier.")
		.addStructureInfo("14+ Output Hatches, 4+ Input Hatches, 4x Dynamo Hatches")
		.addStructureInfo("2x Maintenance Hatches, 4x Mufflers")
		.toolTipFinisher("GT++");
		return tt;
	}

	@Override
	public String[] getExtraInfoData() {
		final String tRunning = (this.mMaxProgresstime>0 ? "Reactor running":"Reactor stopped");
		final String tMaintainance = (this.getIdealStatus() == this.getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance");

		return new String[]{
				"Liquid Fluoride Thorium Reactor",
				tRunning,
				tMaintainance,
				"Current Output: "+this.mEUt+" EU/t",
				"Fuel Remaining: "+this.mFuelRemaining+" Litres",
				"Current Efficiency: "+(this.mEfficiency/5)+"%",
				"Current Efficiency (Raw): "+(this.mEfficiency),
		"It requires you to have 100% Efficiency."};
	}

	@Override
	public boolean allowCoverOnSide(final byte aSide, final GT_ItemStack aStack) {
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (!aBaseMetaTileEntity.isActive() || this.mEfficiency < 500){
			if (aSide == aFacing) {
				return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12)),
						new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)};
			}
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12))};
		}
		else if(aBaseMetaTileEntity.isActive() && this.mEfficiency >= 500){
			if (aSide == aFacing) {
				return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13)),
						new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)};
			}
			return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(13))};
		}
		return new ITexture[]{Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(12))};

	}

	@Override
	public boolean hasSlotInGUI() {
		return false;
	}

	@Override
	public String getCustomGUIResourceName() {
		return "MatterFabricator";
	}

	public final boolean addNuclearReactorEdgeList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Dynamo && ((GT_MetaTileEntity_Hatch_Dynamo) aMetaTileEntity).mTier >= 5){
				((GT_MetaTileEntity_Hatch)aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mDynamoHatches.add((GT_MetaTileEntity_Hatch_Dynamo)aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input && ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mTier == 5) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output && ((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity).mTier >= 5) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
			}
		}
		return false;
	}

	public final boolean addNuclearReactorTopList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
		if (aTileEntity == null) {
			return false;
		} else {
			IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
			if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler && ((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity).mTier >= 5) {
				((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
				return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
			}
		}
		return false;
	}

	@Override
	public IStructureDefinition<GregtechMTE_NuclearReactor> getStructureDefinition() {
		if (STRUCTURE_DEFINITION == null) {
			STRUCTURE_DEFINITION = StructureDefinition.<GregtechMTE_NuclearReactor>builder()
					.addShape(mName, transpose(new String[][]{
						{"CCCCCCC", "COOOOOC", "COXXXOC", "COXXXOC", "COXXXOC", "COOOOOC", "CCCCCCC"},
						{"GGGGGGG", "G-----G", "G-----G", "G-----G", "G-----G", "G-----G", "GGGGGGG"},
						{"GGGGGGG", "G-----G", "G-----G", "G-----G", "G-----G", "G-----G", "GGGGGGG"},
						{"CCC~CCC", "COOOOOC", "COOOOOC", "COOOOOC", "COOOOOC", "COOOOOC", "CCCCCCC"},
					}))
					.addElement(
							'C',
							ofChain(
									ofHatchAdder(
											GregtechMTE_NuclearReactor::addNuclearReactorEdgeList, TAE.GTPP_INDEX(12), 1
											),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasingsMisc, 12
													)
											)
									)
							)
					.addElement(
							'X',
							ofChain(
									ofHatchAdder(
											GregtechMTE_NuclearReactor::addNuclearReactorTopList, TAE.GTPP_INDEX(12), 2
											),
									onElementPass(
											x -> ++x.mCasing,
											ofBlock(
													ModBlocks.blockCasingsMisc, 12
													)
											)
									)

							)
					.addElement(
							'O',
							ofBlock(
									ModBlocks.blockCasingsMisc, 12
									)
							)
					.addElement(
							'G',
							ofBlock(
									ModBlocks.blockCasingsMisc, 13
									)
							)
					.build();
		}
		return STRUCTURE_DEFINITION;
	}

	@Override
	public void construct(ItemStack stackSize, boolean hintsOnly) {
		buildPiece(mName , stackSize, hintsOnly, 3, 3, 0);
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		mCasing = 0;
		if (checkPiece(mName, 3, 3, 0) && mCasing >= 27) {
			if (mOutputHatches.size() >= 14 && mInputHatches.size() >= 4 && mDynamoHatches.size() == 4 &&
					mMufflerHatches.size() == 4 && mMaintenanceHatches.size() == 2) {
				this.mWrench = true;
				this.mScrewdriver = true;
				this.mSoftHammer = true;
				this.mHardHammer = true;
				this.mSolderingTool = true;
				this.mCrowbar = true;
				this.turnCasingActive(false);
				return true;
			}
		}
		return false;
	}

	// Alk's Life Lessons from Greg.
	/*
			[23:41:15] <GregoriusTechneticies> xdir and zdir are x2 and not x3
			[23:41:26] <GregoriusTechneticies> thats you issue
			[23:44:33] <Alkalus> mmm?
			[23:44:49] <Alkalus> Should they be x3?
			[23:44:50] <GregoriusTechneticies> you just do a x2, what is for a 5x5 multiblock
			[23:45:01] <GregoriusTechneticies> x3 is for a 7x7 one
			[23:45:06] <Alkalus> I have no idea what that value does, tbh..
			[23:45:15] <GregoriusTechneticies> its the offset
			[23:45:23] <Alkalus> Debugging checkMachine has been a pain and I usually trash designs that don't work straight up..
			[23:45:28] <GregoriusTechneticies> it determines the horizontal middle of the multiblock
			[23:45:47] <GregoriusTechneticies> which is in your case THREE blocks away from the controller
			[23:45:51] <Alkalus> Ahh
			[23:45:57] <GregoriusTechneticies> and not 2
			[23:46:06] <Alkalus> Noted, thanks :D
	 */

	@Override
	public boolean isCorrectMachinePart(final ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(final ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return 10;
	}

	@Override
	public int getDamageToComponent(final ItemStack aStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(final ItemStack aStack) {
		return true;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_NuclearReactor(this.mName);
	}

	public boolean turnCasingActive(final boolean status) {
		//TODO
		if (this.mDynamoHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Dynamo hatch : this.mDynamoHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(13) : (byte) TAE.GTPP_INDEX(12);
			}
		}
		if (this.mMufflerHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Muffler hatch : this.mMufflerHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(13) : (byte) TAE.GTPP_INDEX(12);
			}
		}
		if (this.mOutputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(13) : (byte) TAE.GTPP_INDEX(12);
			}
		}
		if (this.mInputHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(13) : (byte) TAE.GTPP_INDEX(12);
			}
		}
		if (this.mMaintenanceHatches != null) {
			for (final GT_MetaTileEntity_Hatch_Maintenance hatch : this.mMaintenanceHatches) {
				hatch.mMachineBlock = status ? (byte) TAE.GTPP_INDEX(13) : (byte) TAE.GTPP_INDEX(12);
			}
		}
		return true;
	}

	public FluidStack[] getStoredFluidsAsArray() {	
		final ArrayList<FluidStack> tFluids = this.getStoredFluids();
		FluidStack[] aStored = new FluidStack[tFluids.size()];	
		for (int i = 0; i < aStored.length; i++) {
			aStored[i] = tFluids.get(i);
		}
		return aStored;
	}

	public int getStoredFuel(GT_Recipe aRecipe) {
		int aFuelStored = 0;
		FluidStack aFuelFluid = null;
		for (FluidStack aFluidInput : aRecipe.mFluidInputs) {
			if (!aFluidInput.getFluid().equals(ModItems.fluidFLiBeSalt)) {
				aFuelFluid = aFluidInput;
				break;
			}
		}
		if (aFuelFluid != null) {
			for (GT_MetaTileEntity_Hatch_Input aInputHatch : this.mInputHatches) {
				if (aInputHatch.getFluid() != null && aInputHatch.getFluidAmount() > 0) {
					if (aInputHatch.getFluid().isFluidEqual(aFuelFluid)) {
						aFuelStored += aInputHatch.getFluidAmount();
					}
				}
			}
		}		
		return aFuelStored;
	}

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		// Warm up for 4~ minutes
		Logger.WARNING("Checking LFTR recipes.");
		if (mEfficiency < this.getMaxEfficiency(null)) {
			this.mProgresstime = 1;
			this.mMaxProgresstime = 1;
			this.mEfficiencyIncrease = 2;
			Logger.WARNING("Warming Up! "+this.mEfficiency+"/"+this.getMaxEfficiency(null));
			return true;
		}
		Logger.WARNING("Warmed up, checking LFTR recipes.");

		final FluidStack[] tFluids = getStoredFluidsAsArray();
		final Collection<GT_Recipe> tRecipeList = getRecipeMap().mRecipeList;
		if(tFluids.length > 0 && tRecipeList != null && tRecipeList.size() > 0) { //Does input hatch have a LFTR fuel?
			Logger.WARNING("Found more than one input fluid and a list of valid recipes.");
			boolean foundLi2bef4 = false;
			// Find a valid recipe
			GT_Recipe aFuelProcessing = this.findRecipe(getBaseMetaTileEntity(), mLastRecipe, true, 0, tFluids, new ItemStack[] {});
			if (aFuelProcessing == null) {
				Logger.WARNING("Did not find valid recipe for given inputs.");
				return false;
			}
			else {
				Logger.WARNING("Found recipe? "+(aFuelProcessing != null ? "true" : "false"));
				for (FluidStack aFluidInput : aFuelProcessing.mFluidInputs) {
					Logger.WARNING("Using "+aFluidInput.getLocalizedName());				
				}
			}
			// Find li2bef4, Helium & Fluorine
			for (final FluidStack hatchFluid1 : tFluids) { //Loops through hatches
				if (hatchFluid1 != null) {
					if (hatchFluid1.getFluid().equals(ModItems.fluidFLiBeSalt)){
						foundLi2bef4 = true;
						Logger.WARNING("Found "+hatchFluid1.getLocalizedName());
						continue;
					}
				}
			}			
			if (!foundLi2bef4) {
				Logger.WARNING("Did not find "+ModItems.fluidFLiBeSalt.getLocalizedName());
				return false;
			}
			// Reset outputs and progress stats
			this.mEUt = 0;
			this.mMaxProgresstime = 0;
			this.mOutputItems = new ItemStack[]{};
			this.mOutputFluids = new FluidStack[]{};
			this.mLastRecipe = aFuelProcessing;			
			// Deplete Inputs
			if (aFuelProcessing.mFluidInputs.length > 0) {
				for (FluidStack aInputToConsume : aFuelProcessing.mFluidInputs) {
					Logger.WARNING("Depleting "+aInputToConsume.getLocalizedName()+" - "+aInputToConsume.amount+"L");
					this.depleteInput(aInputToConsume);			
				}
			}
			// -- Try not to fail after this point - inputs have already been consumed! --
			this.mMaxProgresstime = (int)(aFuelProcessing.mDuration);
			this.mEUt = aFuelProcessing.mSpecialValue * 4;
			Logger.WARNING("Outputting "+this.mEUt+"eu/t");
			this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
			this.mEfficiencyIncrease = 10000;		
			this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
			Logger.WARNING("Recipe time: "+this.mMaxProgresstime);	
			mFuelRemaining = getStoredFuel(aFuelProcessing); //Record available fuel	

			FluidStack[] tOutputFluids = new FluidStack[aFuelProcessing.mFluidOutputs.length];
			for (int h = 0; h < aFuelProcessing.mFluidOutputs.length; h++) {
				if (aFuelProcessing.getFluidOutput(h) != null) {
					tOutputFluids[h] = aFuelProcessing.getFluidOutput(h).copy();
				}
			}

			this.mOutputFluids = tOutputFluids;
			updateSlots();					
			Logger.WARNING("Recipe Good!");	
			return true;
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
		Logger.WARNING("Recipe Bad!");
		return false;
	}	

	@Override
	public int getMaxParallelRecipes() {
		return 1;
	}

	@Override
	public int getEuDiscountForParallelism() {
		return 0;
	}

	public int getAmountOfOutputs() {
		return 10;
	}

	@Override
	public void explodeMultiblock() {
		this.mInventory[1] = null;
		long explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
		for (final MetaTileEntity tTileEntity : this.mInputBusses) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		for (final MetaTileEntity tTileEntity : this.mOutputBusses) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		for (final MetaTileEntity tTileEntity : this.mInputHatches) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		for (final MetaTileEntity tTileEntity : this.mOutputHatches) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		for (final MetaTileEntity tTileEntity : this.mDynamoHatches) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		for (final MetaTileEntity tTileEntity : this.mMufflerHatches) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		for (final MetaTileEntity tTileEntity : this.mEnergyHatches) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		for (final MetaTileEntity tTileEntity : this.mMaintenanceHatches) {
			explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
			tTileEntity.getBaseMetaTileEntity().doExplosion(explodevalue);
		}
		explodevalue = MathUtils.randLong(Integer.MAX_VALUE, 8589934588L);
		this.getBaseMetaTileEntity().doExplosion(explodevalue);
	}
	
	private int mSpargeTime = 0;
	private int mSpargeTicks = 0;

	private void trySparge() {
		if (mHelium == null) {
			mHelium = Materials.Helium.getGas(1).getFluid();
			Logger.WARNING("Set Helium.");
		}
		if (mFluorine == null) {
			Logger.WARNING("Set Fluorine.");
			mFluorine = Materials.Fluorine.getGas(1).getFluid();
		}
		final FluidStack[] tFluids = getStoredFluidsAsArray();
		FluidStack aHeliumSparge = null;
		FluidStack aFluorineSparge = null;
		// Find Helium & Fluorine
		for (final FluidStack hatchFluid1 : tFluids) { //Loops through hatches
			if (hatchFluid1 != null) {
				if (hatchFluid1.getFluid().equals(mHelium) && hatchFluid1.amount >= 100){
					aHeliumSparge = hatchFluid1;
					Logger.WARNING("Found "+hatchFluid1.getLocalizedName());
					continue;
				}
				else if (hatchFluid1.getFluid().equals(mFluorine) && hatchFluid1.amount >= 10){
					aFluorineSparge = hatchFluid1;
					Logger.WARNING("Found "+hatchFluid1.getLocalizedName());
					continue;
				}
			}
		}
		if (aHeliumSparge != null) {
			Logger.WARNING("Sparging Helium.");
			AutoMap<FluidStack> aSpargeOutputs = getByproductsOfSparge(aHeliumSparge);
			for (FluidStack aSparge : aSpargeOutputs) {
				this.addOutput(aSparge);
			}				
		}
		if (aFluorineSparge != null) {
			Logger.WARNING("Sparging Fluorine.");
			AutoMap<FluidStack> aSpargeOutputs = getByproductsOfSparge(aFluorineSparge);
			for (FluidStack aSparge : aSpargeOutputs) {
				this.addOutput(aSparge);
			}
		}
		updateSlots();
	}

	private static AutoMap<Fluid> mNobleGases;
	private static AutoMap<Fluid> mFluorideGases;
	private static AutoMap<Fluid> mSpargeGases;

	private AutoMap<FluidStack> getByproductsOfSparge(final FluidStack spargeGas){
		AutoMap<FluidStack> aOutputGases = new AutoMap<FluidStack>();
		if (mNobleGases == null) {
			mNobleGases = new AutoMap<Fluid>();
			mNobleGases.add(Materials.Helium.getGas(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().XENON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().NEON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().ARGON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().KRYPTON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().RADON.getFluid(1).getFluid());
		}
		if (mFluorideGases == null) {
			mFluorideGases = new AutoMap<Fluid>();
			mFluorideGases.add(Materials.Fluorine.getGas(1).getFluid());
			mFluorideGases.add(FLUORIDES.LITHIUM_FLUORIDE.getFluid(1).getFluid());
			mFluorideGases.add(FLUORIDES.NEPTUNIUM_HEXAFLUORIDE.getFluid(1).getFluid());
			mFluorideGases.add(FLUORIDES.TECHNETIUM_HEXAFLUORIDE.getFluid(1).getFluid());
			mFluorideGases.add(FLUORIDES.SELENIUM_HEXAFLUORIDE.getFluid(1).getFluid());
		}
		if (mSpargeGases == null) {
			mSpargeGases = new AutoMap<Fluid>();
			mSpargeGases.add(Materials.Helium.getGas(1).getFluid());
			mSpargeGases.add(Materials.Fluorine.getGas(1).getFluid());
		}
		if (spargeGas == null) {
			return aOutputGases;
		}
		int outputChances[] = null;
		int aDepletionAmount = 0;
		int aSpargeType = -1;
		if (spargeGas.getFluid().equals(mHelium)){
			outputChances = new int[]{
					0,
					MathUtils.roundToClosestInt(MathUtils.randInt(0, 20)),
					MathUtils.roundToClosestInt(MathUtils.randInt(0, 20)),
					MathUtils.roundToClosestInt(MathUtils.randInt(0, 20)),
					MathUtils.roundToClosestInt(MathUtils.randInt(0, 20)),
					MathUtils.roundToClosestInt(MathUtils.randInt(0, 20))
			};
			aDepletionAmount = 100;
			outputChances[0] = (aDepletionAmount-outputChances[1]-outputChances[2]-outputChances[3]-outputChances[4]-outputChances[5]);
			aSpargeType = 0;
		}
		else if (spargeGas.getFluid().equals(mFluorine)){
			outputChances = new int[]{
					0,
					MathUtils.roundToClosestInt(MathUtils.randDouble(0, 40)),
					MathUtils.roundToClosestInt(MathUtils.randDouble(0, 20)),
					MathUtils.roundToClosestInt(MathUtils.randDouble(0, 20)),
					MathUtils.roundToClosestInt(MathUtils.randDouble(0, 20))
			};
			aDepletionAmount = 100;
			outputChances[0] = (aDepletionAmount-outputChances[1]-outputChances[2]-outputChances[3]-outputChances[4]);
			aSpargeType = 1;
		}		
		if (outputChances == null) {
			return aOutputGases;			
		}
		FluidStack depletionStack = spargeGas.copy();
		depletionStack.amount = aDepletionAmount;
		AutoMap<Fluid> aTempMap = aSpargeType == 0 ? mNobleGases : mFluorideGases;
		for (int i = 0; i < aTempMap.size(); i++) {
			Fluid aFluid = aTempMap.get(i);
			aOutputGases.add(new FluidStack(aFluid, outputChances[i]));			
		}
		this.depleteInput(depletionStack);
		updateSlots();
		return aOutputGases;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.getWorld().isRemote) {
			if (aBaseMetaTileEntity.isActive()){
				// Set casings active if we're warmed up.
				if (this.mEfficiency == this.getMaxEfficiency(null)){
					this.turnCasingActive(true);
				}
				else {
					this.turnCasingActive(false);
				}
			}
			else {
				this.turnCasingActive(false);
			}
		}
		else {
			// Try output some Uranium-233
			if (MathUtils.randInt(300, 600) == 1){
				this.addOutput(ELEMENT.getInstance().URANIUM233.getFluid(MathUtils.randInt(1, 10)));
			}			
			// Set a random tick counter, count it up.
			if (this.mSpargeTime == 0) {
				this.mSpargeTime = MathUtils.randInt(1200, 2400);
				Logger.WARNING("Set Sparge Timer to "+this.mSpargeTime);
			}
			else {
				this.mSpargeTicks++;
			}
			// Try Sparge
			if (this.mSpargeTicks >= this.mSpargeTime) {
				this.mSpargeTime = 0;
				this.mSpargeTicks = 0;
				Logger.WARNING("Sparging!");
				trySparge();
			}
		}		
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}
	
	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setInteger("mSpargeTicks", this.mSpargeTicks);
		aNBT.setInteger("mSpargeTime", this.mSpargeTime);
		aNBT.setInteger("mFuelRemaining", this.mFuelRemaining);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		this.mSpargeTicks = aNBT.getInteger("mSpargeTicks");
		this.mSpargeTime = aNBT.getInteger("mSpargeTime");
		this.mFuelRemaining = aNBT.getInteger("mFuelRemaining");
		super.loadNBTData(aNBT);
	}

}
