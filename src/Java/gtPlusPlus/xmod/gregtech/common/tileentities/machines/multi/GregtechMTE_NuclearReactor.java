package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_NuclearReactor extends GT_MetaTileEntity_MultiBlockBase {

	public GT_Recipe mLastRecipe;
	protected long mEUStore;
	protected int fuelConsumption = 0;
	protected int fuelValue = 0;
	protected int fuelRemaining = 0;
	protected double realOptFlow = 0;
	protected boolean boostEu = false;
	protected boolean heliumSparging = false;
	protected boolean fluorideSparging = false;

	//public FluidStack mFluidOut = Materials.UUMatter.getFluid(1L);

	public GregtechMTE_NuclearReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_NuclearReactor(String aName) {
		super(aName);
	}

	@Override
	public long maxEUStore() {
		return 640000000L * (Math.min(16, this.mEnergyHatches.size())) / 16L;
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Liquid Fluoride Thorium Reactor.",
				"Produces Heat & Energy from Radioactive Beta Decay.",
				"Size(WxHxD): 7x4x7, Controller (Bottom, Center)",
				"--Hatches go in the top or bottom layer edges--",
				"10x IV+ Output Hatches",
				"4x IV+ Input Hatches",
				"4x IV+ Dynamo Hatches",
				"2x Maint. Hatch",
				"--Mufflers go in the top 3x3--",
				"4x IV+ Mufflers",
				CORE.GT_Tooltip};
	}

	@Override
	public String[] getInfoData() {

		String tRunning = (mMaxProgresstime>0 ? "Reactor running":"Reactor stopped");
		String tMaintainance = (getIdealStatus() == getRepairStatus() ? "No Maintainance issues" : "Needs Maintainance");

		return new String[]{
				"Liquid Fluoride Thorium Reactor",
				tRunning,
				tMaintainance,
				"Current Output: "+(mEUt*mEfficiency/10000)+" EU/t",
				"Fuel Consumption: "+fuelConsumption+"L/t",
				"Fuel Value: "+fuelValue+" EU/L",
				"Fuel Remaining: "+fuelRemaining+" Litres",
				"Current Efficiency: "+(mEfficiency/100)+"%",
				"Optimal Fuel Flow: "+(int)realOptFlow+" L/t",
				"Current Speed: "+(mEfficiency/100)+"%",};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[70],
					new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[70]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MatterFabricator.png");
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

		// Life Lessons from Greg.
		/**
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

		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 3; 
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 3;

		for (int i = -3; i <= 3; i++) {
			for (int j = -3; j <= 3; j++) {
				for (int h = 0; h < 4; h++) {
					IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					// Reactor Floor/Roof inner 5x5
					if ((i != -3 && i != 3) && (j != -3 && j != 3)) {

						// Reactor Floor & Roof (Inner 5x5) + Mufflers, Dynamos and Fluid outputs.
						if (h == 0 || h == 3) {

							//If not a hatch, continue, else add hatch and continue.
							if ((!addMufflerToMachineList(tTileEntity, 70)) && (!addOutputToMachineList(tTileEntity, 70)) && (!addDynamoToMachineList(tTileEntity, 70))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("LFTR Casing(s) Missing from one of the top layers inner 3x3.");
									Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO("LFTR Casing(s) Missing from one of the top layers inner 3x3. Wrong Meta for Casing.");
									return false;
								}
							}	
						} 

						// Inside 2 layers, mostly air
						else {		

							// Reactor Inner 5x5
							//if ((i != -1 && i != 1) && (j != -1 && j != 1)) {
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Utils.LOG_INFO("Make sure the inner 3x3 of the Multiblock is Air.");								
								return false;
							}

						}

						//TODO - Add Carbon Moderation Rods
						/*
							else { //carbon moderation rods are at 1,1 & -1,-1 & 1,-1 & -1,1
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("LFTR Casing(s) Missing from one of the top layers inner 3x3.");
									Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO("LFTR Casing(s) Missing from one of the top layers inner 3x3.");
									return false;
								}
							}*/						

					}

					//Dealt with inner 5x5, now deal with the exterior.
					else {

						//Deal with all 4 sides (Reactor walls)
						if (h == 1 || h == 2) {														
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
								Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 13) {
								Utils.LOG_INFO("Glass Casings Missing from somewhere in the second layer.");
								Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}							
						}

						//Deal with top and Bottom edges (Inner 5x5)
						else if (h == 0 || h == 3) {
							if ((!addToMachineList(tTileEntity, 70)) && (!addInputToMachineList(tTileEntity, 70)) && (!addOutputToMachineList(tTileEntity, 70)) && (!addDynamoToMachineList(tTileEntity, 70))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller

									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("LFTR Casing(s) Missing from one of the edges on the top layer.");
										Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
										Utils.LOG_INFO("LFTR Casing(s) Missing from one of the edges on the top layer. "+h);
										Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										if (h ==0){
											if (tTileEntity instanceof GregtechMTE_NuclearReactor){

											}
										}
										else {
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (this.mMufflerHatches.size() != 4){
			Utils.LOG_INFO("You require EXACTLY 4 muffler hatches on top. FOUR.");
			return false;
		}
		if (this.mEnergyHatches != null) {
			for (int i = 0; i < this.mEnergyHatches.size(); i++) {
				if (this.mEnergyHatches.get(i).mTier < 5){
					Utils.LOG_INFO("You require at LEAST V tier Energy Hatches.");
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					return false;
				}
			}
		}
		if (this.mOutputHatches != null) {
			for (int i = 0; i < this.mOutputHatches.size(); i++) {

				if (this.mOutputHatches.get(i).mTier < 5 && (this.mOutputHatches.get(i).getBaseMetaTileEntity() instanceof GregtechMTE_NuclearReactor)){
					Utils.LOG_INFO("You require at LEAST V tier Output Hatches.");
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		if (this.mInputHatches != null) {
			for (int i = 0; i < this.mInputHatches.size(); i++) {
				if (this.mInputHatches.get(i).mTier < 5){
					Utils.LOG_INFO("You require at LEAST V tier Input Hatches.");
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Utils.LOG_INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		mWrench = true;
		mScrewdriver = true;
		mSoftHammer = true;
		mHardHammer = true;
		mSolderingTool = true;
		mCrowbar = true;	
		turnCasingActive(true);
		Utils.LOG_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return boostEu ? 30000 : 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 5;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return true;
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMTE_NuclearReactor(this.mName);
	}

	public static int overclock(int mStartEnergy) {	        
		return mStartEnergy < 160000000 ? 4 : mStartEnergy < 320000000 ? 2 : 1;
	}

	public boolean turnCasingActive(boolean status) {
		if (this.mDynamoHatches != null) {
			for (GT_MetaTileEntity_Hatch_Dynamo hatch : this.mDynamoHatches) {
				hatch.mMachineBlock = status ? (byte) 70 : (byte) 71;
			}
		}
		if (this.mMufflerHatches != null) {
			for (GT_MetaTileEntity_Hatch_Muffler hatch : this.mMufflerHatches) {
				hatch.mMachineBlock = status ? (byte) 70 : (byte) 71;
			}
		}
		if (this.mOutputHatches != null) {
			for (GT_MetaTileEntity_Hatch_Output hatch : this.mOutputHatches) {
				hatch.mMachineBlock = status ? (byte) 70 : (byte) 71;
			}
		}
		if (this.mInputHatches != null) {
			for (GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
				hatch.mMachineBlock = status ? (byte) 70 : (byte) 71;
			}
		}
		if (this.mOutputBusses != null) {
			for (GT_MetaTileEntity_Hatch_OutputBus hatch : this.mOutputBusses) {
				hatch.mMachineBlock = status ? (byte) 70 : (byte) 71;
			}
		}
		if (this.mInputBusses != null) {
			for (GT_MetaTileEntity_Hatch_InputBus hatch : this.mInputBusses) {
				hatch.mMachineBlock = status ? (byte) 70 : (byte) 71;
			}
		}
		return true;
	}



	@Override
	public boolean checkRecipe(ItemStack aStack) {
		ArrayList<FluidStack> tFluids = getStoredFluids();
		Collection<GT_Recipe> tRecipeList = Recipe_GT.Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.mRecipeList;
		if(tFluids.size() > 0 && tRecipeList != null) { //Does input hatch have a LFTR fuel?
			Utils.LOG_INFO("Found more than one input fluid and a list of valid recipes.");
			boolean containsFLIBE = false;
			boolean containsPrimarySalt = false;
			for (FluidStack hatchFluid1 : tFluids) { //Loops through hatches
				Utils.LOG_INFO("Looping through Input hatches - Found "+hatchFluid1.getLocalizedName());
				for(GT_Recipe aFuel : tRecipeList) { //Loops through LFTR fuel recipes
					Utils.LOG_INFO("Looping through Recipes. "+aFuel.mSpecialValue);
					FluidStack tLiquid;					
					FluidStack testStack = aFuel.mFluidInputs[1];					
					if ((tLiquid = testStack) != null) { //Create fluidstack from current recipe
						Utils.LOG_INFO("Creating a fluidstack from the current recipe");
						if (hatchFluid1.isFluidEqual(tLiquid)) { //Has a LFTR fluid
							fuelConsumption = tLiquid.amount = boostEu ? (aFuel.mSpecialValue/4096) : (aFuel.mSpecialValue/2048); //Calc fuel consumption
							Utils.LOG_INFO("Input hatch contains some FLiBe Fuel, using "+fuelConsumption);
							if(depleteInput(tLiquid)) { //Deplete that amount
								Utils.LOG_INFO("Depleted some FLiBe fluid");

								this.mMaxProgresstime = 500;
								
								if(tFluids.contains(NUCLIDE.LiFBeF2ThF4UF4.getFluid(1)) ||
										tFluids.contains(NUCLIDE.LiFBeF2ZrF4UF4.getFluid(2)) ||
										tFluids.contains(NUCLIDE.LiFBeF2ZrF4U235.getFluid(10))) { //Has a Primary fuel salt?
									//Deplete Primary Salt. 1000L should = 1 hour of runtime (if baseEU = 2048) && using 1l each time
									if(mRuntime % 72 == 0 || mRuntime == 0){
										//U235 fuel is 10x less efficient than UF4 with Thorium, UF4 with Zirconium is only 2x less efficient than UF4 with Thorium.
										//Most Efficient
										if(tFluids.contains(NUCLIDE.LiFBeF2ThF4UF4.getFluid(2))){
											depleteInput(NUCLIDE.LiFBeF2ThF4UF4.getFluid(boostEu ? 2 : 1));
											Utils.LOG_INFO("Depleted "+(boostEu ? 2 : 1)+"L of LiFBeF2ThF4UF4 fluid");
										}
										//1/2 as Efficient
										if (tFluids.contains(NUCLIDE.LiFBeF2ZrF4UF4.getFluid(4))){
											depleteInput(NUCLIDE.LiFBeF2ZrF4UF4.getFluid(boostEu ? 4 : 2));
											Utils.LOG_INFO("Depleted "+(boostEu ? 4 : 2)+"L of LiFBeF2ZrF4UF4 fluid");
										}
										//10x less Efficient.
										if (tFluids.contains(NUCLIDE.LiFBeF2ZrF4U235.getFluid(20))) { 
											depleteInput(NUCLIDE.LiFBeF2ZrF4U235.getFluid(boostEu ? 20 : 10));
											Utils.LOG_INFO("Depleted "+(boostEu ? 20 : 10)+"L of LiFBeF2ZrF4U235 fluid");
										}
									}
								} else {
									return false;
								}

								
								//Try Sparge Noble Gases
								if (depleteInput(Materials.Helium.getGas(1000L)) && heliumSparging){
									//Make an empty fluid stack for possible sparging output
									FluidStack[] spargeOutput = new FluidStack[]{};
									Utils.LOG_INFO("Doing a Sparge with Helium");
									spargeOutput = getByproductsOfSparge(Materials.Helium.getGas(1000L));
									heliumSparging = false;
									//If Sparging occurred, try add the outputs to the output hatches.
									try {
									if (spargeOutput.length >= 1){
										for (FluidStack F : spargeOutput){
											Utils.LOG_INFO("Adding Sparge Output - "+F.getLocalizedName());
											addOutput(F);
										}
									}
									} catch (Throwable T){}
								}
								
								//Try Sparge Fluorides
								if (depleteInput(Materials.Fluorine.getGas(100L)) && !heliumSparging){
									//Make an empty fluid stack for possible sparging output
									FluidStack[] spargeOutput = new FluidStack[]{};
									Utils.LOG_INFO("Doing a Sparge with Fluorine");
									spargeOutput = getByproductsOfSparge(Materials.Fluorine.getGas(100L));
									heliumSparging = true;
									//If Sparging occurred, try add the outputs to the output hatches.
									if (spargeOutput.length > 0){
										for (FluidStack F : spargeOutput){
											Utils.LOG_INFO("Adding Sparge Output - "+F.getLocalizedName());
											addOutput(F);
										}
									}
								}
								

								if (aFuel != null){
									Utils.LOG_INFO("Saving previous Recipe.");
									this.mLastRecipe = aFuel;	
								}

								
								fuelValue = aFuel.mSpecialValue;
								fuelRemaining = hatchFluid1.amount; //Record available fuel
								
								if (this.mEfficiency < 500){
									this.mEfficiency++;
								}
								
								this.mEUt = (mEfficiency < 500 ? 2048 : (8196*4)); //Output 0 if startup is less than 20%
								Utils.LOG_INFO("Generating "+this.mEUt+"EU/t @ an efficiency level of "+this.mEfficiency);
								
								this.mProgresstime = 1;
								this.mMaxProgresstime = 1;
								this.mEfficiencyIncrease = 15;

								//Best output some Fluids
								this.mOutputFluids = this.mLastRecipe.mFluidOutputs;

								return true;
							}
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
	public int getAmountOfOutputs() {
		return 1;
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


	protected FluidStack[] getByproductsOfSparge(FluidStack spargeGas){
		FluidStack[] outputArrayOfGases = new FluidStack[]{};
		if (spargeGas != null){
			if (spargeGas.isFluidEqual(Materials.Helium.getGas(1000))){
				int outputChances[] = {
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 1000)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 600)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 400)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 1000)/10),
						MathUtils.roundToClosestInt(MathUtils.randInt(10, 100)/10)						
				};
				int heliumContent = (1000-outputChances[0]-outputChances[1]-outputChances[2]-outputChances[3]-outputChances[4]);
				Utils.LOG_INFO("Helium remaining: "+heliumContent);
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
				int outputChances[] = {
						MathUtils.roundToClosestInt(MathUtils.randDouble(10, 100)),
						MathUtils.roundToClosestInt(MathUtils.randDouble(1, 50)/10),
						MathUtils.roundToClosestInt(MathUtils.randDouble(1, 50)/10),
						MathUtils.roundToClosestInt(MathUtils.randDouble(1, 50)/10)					
				};
				int fluorineContent = (100-outputChances[0]-outputChances[1]-outputChances[2]-outputChances[3]);
				Utils.LOG_INFO("Fluorine remaining: "+fluorineContent);
				outputArrayOfGases = new FluidStack[]{
						FLUORIDES.URANIUM_HEXAFLUORIDE.getFluid(outputChances[0]),
						FLUORIDES.NEPTUNIUM_HEXAFLUORIDE.getFluid(outputChances[1]),
						FLUORIDES.TECHNETIUM_HEXAFLUORIDE.getFluid(outputChances[2]),
						FLUORIDES.SELENIUM_HEXAFLUORIDE.getFluid(outputChances[3]),
						Materials.Fluorine.getGas(fluorineContent)						
				};
			}
		}
		return outputArrayOfGases;
	}

}