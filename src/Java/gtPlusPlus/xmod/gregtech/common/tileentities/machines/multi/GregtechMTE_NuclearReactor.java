package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

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
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_NuclearReactor extends GT_MetaTileEntity_MultiBlockBase {

	public GT_Recipe mLastRecipe;
	protected long mEUStore;
	protected int fuelConsumption = 0;
	protected int fuelValue = 0;
	protected int fuelRemaining = 0;
	protected boolean boostEu = false;
	protected boolean heliumSparging = false;

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
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Liquid Fluoride Thorium Reactor.",
				"Produces Heat & Energy from Radioactive Beta Decay.",
				"Size(WxHxD): 7x4x7, Controller (Bottom, Center)",
				"Bottom and Top layer are Hastelloy-N Reactor Casing",
				"Middle two layers are Zeron-100 Reactor Shielding",
				"--Hatches go in the top or bottom layer edges--",
				"10x IV+ Output Hatches",
				"4x IV+ Input Hatches",
				"4x IV+ Dynamo Hatches",
				"2x Maint. Hatch",
				"--Mufflers go in the top 3x3--",
				"4x IV+ Mufflers",
				"Causes " + (20 * getPollutionPerTick(null)) + " Pollution per second",
				"Outputs U233 every 10 seconds, on average",
				"Input Fluorine and Helium for bonus byproducts",
				"Input Li2BeF4 and a molten salt as fuel.",
				"LiFBeF2ThF4UF4, LiFBeF2ZrF4UF4 or﻿ LiFBeF2ZrF4U235",
				CORE.GT_Tooltip};
	}

	@Override
	public String[] getInfoData() {

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
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean allowCoverOnSide(final byte aSide, final GT_ItemStack aStack) {
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		if (!aBaseMetaTileEntity.isActive() || this.mEfficiency < 500){
			if (aSide == aFacing) {
				return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(12)],
						new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)};
			}
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(12)]};
		}
		else if(aBaseMetaTileEntity.isActive() && this.mEfficiency >= 500){
			if (aSide == aFacing) {
				return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(13)],
						new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_REPLICATOR)};
			}
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(13)]};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[TAE.GTPP_INDEX(12)]};
		
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory, final IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "MatterFabricator.png");
	}

	@Override
	public boolean checkMachine(final IGregTechTileEntity aBaseMetaTileEntity, final ItemStack aStack) {

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

		final int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 3;
		final int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 3;

		for (int i = -3; i <= 3; i++) {
			for (int j = -3; j <= 3; j++) {
				for (int h = 0; h < 4; h++) {
					final IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);

					// Reactor Floor/Roof inner 5x5
					if (((i != -3) && (i != 3)) && ((j != -3) && (j != 3))) {

						// Reactor Floor & Roof (Inner 5x5) + Mufflers, Dynamos and Fluid outputs.
						if ((h == 0) || (h == 3)) {

							//If not a hatch, continue, else add hatch and continue.
							if ((!this.addMufflerToMachineList(tTileEntity, TAE.GTPP_INDEX(12))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(12))) && (!this.addDynamoToMachineList(tTileEntity, TAE.GTPP_INDEX(12)))) {
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Logger.INFO("Hastelloy-N Reactor Casing(s) Missing from one of the top layers inner 3x3.");
									Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Logger.INFO("Hastelloy-N Reactor Casing(s) Missing from one of the top layers inner 3x3. Wrong Meta for Casing.");
									return false;
								}
							}
						}

						// Inside 2 layers, mostly air
						else {

							// Reactor Inner 5x5
							//if ((i != -1 && i != 1) && (j != -1 && j != 1)) {
							if (!aBaseMetaTileEntity.getAirOffset(xDir + i, h, zDir + j)) {
								Logger.INFO("Make sure the inner 3x3 of the Multiblock is Air.");
								return false;
							}

						}

						//TODO - Add Carbon Moderation Rods
						/*
							else { //carbon moderation rods are at 1,1 & -1,-1 & 1,-1 & -1,1
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_WARNING("LFTR Casing(s) Missing from one of the top layers inner 3x3.");
									Utils.LOG_WARNING("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_WARNING("LFTR Casing(s) Missing from one of the top layers inner 3x3.");
									return false;
								}
							}*/

					}

					//Dealt with inner 5x5, now deal with the exterior.
					else {

						//Deal with all 4 sides (Reactor walls)
						if ((h == 1) || (h == 2)) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Logger.INFO("Reactor Shielding Missing from somewhere in the second layer.");
								Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 13) {
								Logger.INFO("Reactor Shielding Missing from somewhere in the second layer.");
								Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
								return false;
							}
						}

						//Deal with top and Bottom edges (Inner 5x5)
						else if ((h == 0) || (h == 3)) {
							if ((!this.addToMachineList(tTileEntity, TAE.GTPP_INDEX(12))) && (!this.addInputToMachineList(tTileEntity, TAE.GTPP_INDEX(12))) && (!this.addOutputToMachineList(tTileEntity, TAE.GTPP_INDEX(12))) && (!this.addDynamoToMachineList(tTileEntity, TAE.GTPP_INDEX(12)))) {
								if (((xDir + i) != 0) || ((zDir + j) != 0)) {//no controller

									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Logger.INFO("Hastelloy-N Reactor Casing(s) Missing from one of the edges on the top layer.");
										Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
										Logger.INFO("Hastelloy-N Reactor Casing(s) Missing from one of the edges on the top layer. "+h);
										Logger.INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
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

		
		if (this.mDynamoHatches != null) {
			for (int i = 0; i < this.mDynamoHatches.size(); i++) {
				if (this.mDynamoHatches.get(i).mTier < 5){
					Logger.INFO("You require at LEAST IV tier Dynamo Hatches.");
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					return false;
				}
			}
		}
		if (this.mOutputHatches != null) {
			for (int i = 0; i < this.mOutputHatches.size(); i++) {

				if ((this.mOutputHatches.get(i).mTier < 5) && (this.mOutputHatches.get(i).getBaseMetaTileEntity() instanceof GregtechMTE_NuclearReactor)){
					Logger.INFO("You require at LEAST IV tier Output Hatches.");
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		if (this.mInputHatches != null) {
			for (int i = 0; i < this.mInputHatches.size(); i++) {
				if (this.mInputHatches.get(i).mTier < 5){
					Logger.INFO("You require at LEAST IV tier Input Hatches.");
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getXCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getYCoord()+","+this.mOutputHatches.get(i).getBaseMetaTileEntity().getZCoord());
					Logger.INFO(this.mOutputHatches.get(i).getBaseMetaTileEntity().getInventoryName());
					return false;
				}
			}
		}
		if (this.mMufflerHatches.size() != 4){
			Logger.INFO("You require EXACTLY 4 muffler hatches on top. FOUR. You have "+this.mMufflerHatches.size());
			return false;
		}
		if (this.mInputHatches.size() < 4){
			Logger.INFO("You require 4 or more input hatches. You have "+this.mInputHatches.size());
			return false;
		}
		if (this.mOutputHatches.size() < 10){
			Logger.INFO("You require 10 or more output hatches. You have "+this.mOutputHatches.size());
			return false;
		}
		if (this.mDynamoHatches.size() != 4){
			Logger.INFO("You require EXACTLY 4 dynamo hatches. FOUR. You have "+this.mDynamoHatches.size());
			return false;
		}
		if (this.mMaintenanceHatches.size() != 2){
			Logger.INFO("You require EXACTLY 2 muffler hatches. TWO. You have "+this.mMaintenanceHatches.size());
			return false;
		}
		this.mWrench = true;
		this.mScrewdriver = true;
		this.mSoftHammer = true;
		this.mHardHammer = true;
		this.mSolderingTool = true;
		this.mCrowbar = true;
		this.turnCasingActive(false);
		Logger.INFO("Multiblock Formed.");
		return true;
	}

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
		final Collection<GT_Recipe> tRecipeList = Recipe_GT.Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.mRecipeList;
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
				this.addOutput(NUCLIDE.getInstance().URANIUM233.getFluid(MathUtils.randInt(1, 10)));
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