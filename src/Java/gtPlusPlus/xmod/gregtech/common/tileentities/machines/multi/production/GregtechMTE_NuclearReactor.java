package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

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
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;

public class GregtechMTE_NuclearReactor extends GregtechMeta_MultiBlockBase {

	protected int fuelConsumption = 0;
	protected int fuelValue = 0;
	protected int fuelRemaining = 0;
	protected boolean boostEu = false;
	protected boolean heliumSparging = false;
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
				.addStructureInfo("All hatches must have IV+ tier.")
				.addStructureInfo("10+ Output Hatches, 4+ Input Hatches, 4x Dynamo Hatches")
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
				"Fuel Consumption: "+this.fuelConsumption+"L/t",
				"Fuel Value: "+this.fuelValue+" EU/L",
				"Fuel Remaining: "+this.fuelRemaining+" Litres",
				"Current Efficiency: "+(this.mEfficiency/5)+"%",
				"Current Efficiency (Raw): "+(this.mEfficiency),
				"Boosted Output: "+this.boostEu+".",
				"Boosted Output gives 4x EU/t for double fuel usage.",
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
			} else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input && ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mTier >= 5) {
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
			if (mOutputHatches.size() >= 10 && mInputHatches.size() >= 4 && mDynamoHatches.size() == 4 &&
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
		return this.boostEu ? 30000 : 10000;
	}

	@Override
	public int getPollutionPerTick(final ItemStack aStack) {
		return this.boostEu ? 8 : 4;
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

	public static int overclock(final int mStartEnergy) {
		return mStartEnergy < 160000000 ? 4 : mStartEnergy < 320000000 ? 2 : 1;
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

	@Override
	public boolean checkRecipe(final ItemStack aStack) {
		final ArrayList<FluidStack> tFluids = this.getStoredFluids();
		final Collection<GT_Recipe> tRecipeList = GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.mRecipeList;
		if((tFluids.size() > 0) && (tRecipeList != null)) { //Does input hatch have a LFTR fuel?
			Logger.WARNING("Found more than one input fluid and a list of valid recipes.");
			for (final FluidStack hatchFluid1 : tFluids) { //Loops through hatches
				Logger.WARNING("Looping through Input hatches - Found "+hatchFluid1.getLocalizedName());
				for(final GT_Recipe aFuel : tRecipeList) { //Loops through LFTR fuel recipes
					Logger.WARNING("Looping through Recipes. "+aFuel.mSpecialValue);
					FluidStack tLiquid;
					final FluidStack testStack = aFuel.mFluidInputs[1];
					if ((tLiquid = testStack) != null) { //Create fluidstack from current recipe
						Logger.WARNING("Creating a fluidstack from the current recipe. "+testStack.getLocalizedName());
						if (hatchFluid1.isFluidEqual(tLiquid)) { //Has a LFTR fluid
							this.fuelConsumption = this.boostEu ? (aFuel.mSpecialValue/4096) : (aFuel.mSpecialValue/2048); //Calc fuel consumption
					
								this.mMaxProgresstime = 500;
								
								if(tFluids.contains(NUCLIDE.LiFBeF2ThF4UF4.getFluid(1)) ||
										tFluids.contains(NUCLIDE.LiFBeF2ZrF4UF4.getFluid(2)) ||
										tFluids.contains(NUCLIDE.LiFBeF2ZrF4U235.getFluid(10))) { //Has a Primary fuel salt?
									//Deplete Primary Salt. 1000L should = 1 hour of runtime (if baseEU = 2048) && using 1l each time
									if(((this.mRuntime % 72) == 0) || (this.mRuntime == 0)){
										//U235 fuel is 10x less efficient than UF4 with Thorium, UF4 with Zirconium is only 2x less efficient than UF4 with Thorium.
										//Most Efficient
										if(tFluids.contains(NUCLIDE.LiFBeF2ThF4UF4.getFluid(2))){
											
											FluidStack depletionStack = FluidUtils.getFluidStack(tLiquid, (this.boostEu ? (aFuel.mSpecialValue/4096) : (aFuel.mSpecialValue/2048)));
											Logger.WARNING("Input hatch contains some FLiBe Fuel, using "+this.fuelConsumption+" | "+aFuel.mSpecialValue+" | "+depletionStack.amount);
											if(this.depleteInput(depletionStack)) { //Deplete that amount
												Logger.WARNING("Depleted some FLiBe fluid");
											}
											
											this.depleteInput(NUCLIDE.LiFBeF2ThF4UF4.getFluid(this.boostEu ? 2 : 1));
											Logger.WARNING("Depleted "+(this.boostEu ? 2 : 1)+"L of LiFBeF2ThF4UF4 fluid");
										}
										//1/2 as Efficient
										if (tFluids.contains(NUCLIDE.LiFBeF2ZrF4UF4.getFluid(4))){
											
											FluidStack depletionStack = FluidUtils.getFluidStack(tLiquid, (this.boostEu ? (aFuel.mSpecialValue/4096) : (aFuel.mSpecialValue/2048)));
											Logger.WARNING("Input hatch contains some FLiBe Fuel, using "+this.fuelConsumption+" | "+aFuel.mSpecialValue+" | "+depletionStack.amount);
											if(this.depleteInput(depletionStack)) { //Deplete that amount
												Logger.WARNING("Depleted some FLiBe fluid");
											}
											
											this.depleteInput(NUCLIDE.LiFBeF2ZrF4UF4.getFluid(this.boostEu ? 4 : 2));
											Logger.WARNING("Depleted "+(this.boostEu ? 4 : 2)+"L of LiFBeF2ZrF4UF4 fluid");
										}
										//10x less Efficient.
										if (tFluids.contains(NUCLIDE.LiFBeF2ZrF4U235.getFluid(20))) {
											
											FluidStack depletionStack = FluidUtils.getFluidStack(tLiquid, (this.boostEu ? (aFuel.mSpecialValue/4096) : (aFuel.mSpecialValue/2048)));
											Logger.WARNING("Input hatch contains some FLiBe Fuel, using "+this.fuelConsumption+" | "+aFuel.mSpecialValue+" | "+depletionStack.amount);
											if(this.depleteInput(depletionStack)) { //Deplete that amount
												Logger.WARNING("Depleted some FLiBe fluid");
											}
											
											this.depleteInput(NUCLIDE.LiFBeF2ZrF4U235.getFluid(this.boostEu ? 20 : 10));
											Logger.WARNING("Depleted "+(this.boostEu ? 20 : 10)+"L of LiFBeF2ZrF4U235 fluid");
										}
									}
								} else {
									return false;
								}


								if (this.getBaseMetaTileEntity().getWorld().getTotalWorldTime() % 100 == 0) {
								//Try Sparge Noble Gases
								if (this.heliumSparging){
									if (this.depleteInput(Materials.Helium.getGas(1000L))){
										//Make an empty fluid stack for possible sparging output
										FluidStack[] spargeOutput = new FluidStack[]{};
										Logger.WARNING("Doing a Sparge with Helium - "+this.heliumSparging);
										this.heliumSparging = false;
										spargeOutput = this.getByproductsOfSparge(Materials.Helium.getGas(1000L));
										
										//If Sparging occurred, try add the outputs to the output hatches.
										try {
											if (spargeOutput.length >= 1){
												for (final FluidStack F : spargeOutput){
													Logger.WARNING("Adding Sparge Output - "+F.getLocalizedName());
													this.addOutput(F);
												}
											}
										} catch (final Throwable T){}
									}
								}
								//Try Sparge Fluorides
								else {
									if (this.depleteInput(Materials.Fluorine.getGas(100L))){
										//Make an empty fluid stack for possible sparging output
										FluidStack[] spargeOutput = new FluidStack[]{};
										Logger.WARNING("Doing a Sparge with Fluorine");
										spargeOutput = this.getByproductsOfSparge(Materials.Fluorine.getGas(100L));
										this.heliumSparging = true;
										//If Sparging occurred, try add the outputs to the output hatches.
										if (spargeOutput.length > 0){
											for (final FluidStack F : spargeOutput){
												Logger.WARNING("Adding Sparge Output - "+F.getLocalizedName());
												this.addOutput(F);
											}
										}
									}
								}
								}


								if (aFuel != null){
									//Utils.LOG_WARNING("Saving previous Recipe.");
									//this.mLastRecipe = aFuel;
								}

								this.fuelValue = aFuel.mSpecialValue;
								this.fuelRemaining = hatchFluid1.amount; //Record available fuel

								if (this.mEfficiency < 500){
									this.mEfficiency++;
									this.mMaxProgresstime = 500;
								}
								else if (this.mEfficiency == 500) {
									this.mMaxProgresstime = 300;
								}
								else if (this.mEfficiency > 500){
									this.mEfficiency = 500;
								}
								Logger.WARNING("Efficiency == "+this.mEfficiency);

								this.mEUt = (this.mEfficiency < 500 ? 2048 : (8196)); //Output 0 if startup is less than 20%
								Logger.WARNING("Generating "+this.mEUt+"EU/t @ an efficiency level of "+this.mEfficiency);
								
								this.mProgresstime = 1;
								this.mMaxProgresstime = 1;
								this.mEfficiencyIncrease = 15;

								//Best output some Fluids
								//this.mOutputFluids = this.mLastRecipe.mFluidOutputs;

								return true;
							}
						}
					}
				}			
		}
		this.mEUt = 0;
		this.mEfficiency = 0;
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


	protected FluidStack[] getByproductsOfSparge(final FluidStack spargeGas){
		FluidStack[] outputArrayOfGases = new FluidStack[]{};
		if (spargeGas != null){
			if (spargeGas.isFluidEqual(Materials.Helium.getGas(1000))){
				final int outputChances[] = {
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 1000)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 600)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 400)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 1000)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 100)/10)
				};
				final int heliumContent = (1000-outputChances[0]-outputChances[1]-outputChances[2]-outputChances[3]-outputChances[4]);
				Logger.WARNING("Helium remaining: "+heliumContent);
				outputArrayOfGases = new FluidStack[]{
						ELEMENT.getInstance().XENON.getFluid(outputChances[0]),
						ELEMENT.getInstance().NEON.getFluid(outputChances[1]),
						ELEMENT.getInstance().ARGON.getFluid(outputChances[2]),
						ELEMENT.getInstance().KRYPTON.getFluid(outputChances[3]),
						ELEMENT.getInstance().RADON.getFluid(outputChances[4]),
						Materials.Helium.getGas(heliumContent)
				};
			}
			else if (spargeGas.isFluidEqual(Materials.Fluorine.getGas(100))){
				final int outputChances[] = {
						MathUtils.roundToClosestInt(MathUtils.randDouble(10, 100)),
						MathUtils.roundToClosestInt(MathUtils.randDouble(1, 50)/10),
						MathUtils.roundToClosestInt(MathUtils.randDouble(1, 50)/10),
						MathUtils.roundToClosestInt(MathUtils.randDouble(1, 50)/10)
				};
				final int fluorineContent = (100-outputChances[0]-outputChances[1]-outputChances[2]-outputChances[3]);
				Logger.WARNING("Fluorine remaining: "+fluorineContent);
				outputArrayOfGases = new FluidStack[]{
						FLUORIDES.LITHIUM_FLUORIDE.getFluid(outputChances[0]),
						FLUORIDES.NEPTUNIUM_HEXAFLUORIDE.getFluid(outputChances[1]),
						FLUORIDES.TECHNETIUM_HEXAFLUORIDE.getFluid(outputChances[2]),
						FLUORIDES.SELENIUM_HEXAFLUORIDE.getFluid(outputChances[3]),
						Materials.Fluorine.getGas(fluorineContent)
				};
			}
		}
		return outputArrayOfGases;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		//Add Power if active
		if (aBaseMetaTileEntity.isActive()){
			//this.getBaseMetaTileEntity().increaseStoredEnergyUnits(this.mEUt, false);
			
			if (this.mEfficiency >= 500){
				this.boostEu = true;
				this.turnCasingActive(true);
			}
			else {
				this.boostEu = false;
				this.turnCasingActive(false);
			}
			
			if (MathUtils.randInt(1, 200) == 1){
				//Utils.LOG_INFO("Adding U233");
				this.addOutput(ELEMENT.getInstance().URANIUM233.getFluid(MathUtils.randInt(1, 10)));
			}
			
			if (this.mDynamoHatches != null) {
				for (GT_MetaTileEntity_Hatch_Dynamo tHatch : this.mDynamoHatches) {
					if (tHatch.mTier >= 5){
						if (isValidMetaTileEntity(tHatch)){
							tHatch.getBaseMetaTileEntity().increaseStoredEnergyUnits(this.mEUt, false);
							//Utils.LOG_WARNING("Adding "+this.mEUt+"eu to internal storage of dynamo "+hatchNo+".");
						}						
					}
				}
			}
			
		}
		else {
			this.turnCasingActive(false);
		}
		super.onPostTick(aBaseMetaTileEntity, aTick);
	}

}
