package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;

import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMTE_NuclearReactor extends GT_MetaTileEntity_MultiBlockBase {

	public GT_Recipe mLastRecipe;
	public int mEUStore;

	//public FluidStack mFluidOut = Materials.UUMatter.getFluid(1L);

	public GregtechMTE_NuclearReactor(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMTE_NuclearReactor(String aName) {
		super(aName);
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
		return aSide != getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Nuclear Reactor",
				"Produces heat from Radioactive beta decay.",
				"Size(WxHxD): 7x4x7, Controller (Bottom, Center)",				
				CORE.GT_Tooltip};
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
	public void onConfigLoad(GT_Config aConfig) {
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
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									aBaseMetaTileEntity.getWorld().setBlock(
											(aBaseMetaTileEntity.getXCoord()+(xDir+i)),
											(aBaseMetaTileEntity.getYCoord()+(h)),
											(aBaseMetaTileEntity.getZCoord()+(zDir+j)),
											Blocks.melon_block);
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3. Wrong Meta for Casing.");
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
								aBaseMetaTileEntity.getWorld().setBlock(
										(aBaseMetaTileEntity.getXCoord()+(xDir+i)),
										(aBaseMetaTileEntity.getYCoord()+(h)),
										(aBaseMetaTileEntity.getZCoord()+(zDir+j)),
										Blocks.melon_block);
								return false;
							}

						}

						//TODO - Add Caron Moderation Rods
						/*
							else { //carbon moderation rods are at 1,1 & -1,-1 & 1,-1 & -1,1
								if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
									Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
									return false;
								}
								if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
									Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the top layers inner 3x3.");
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
								return false;
							}							
						}

						//Deal with top and Bottom edges (Inner 5x5)
						else if (h == 0 || h == 3) {
							if ((!addMaintenanceToMachineList(tTileEntity, 70)) && (!addInputToMachineList(tTileEntity, 70)) && (!addOutputToMachineList(tTileEntity, 70)) && (!addDynamoToMachineList(tTileEntity, 70))) {
								if ((xDir + i != 0) || (zDir + j != 0)) {//no controller
									if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
										Utils.LOG_INFO("Instead, found "+aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getLocalizedName());
										return false;
									}
									if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 12) {
										Utils.LOG_INFO("Matter Fabricator Casings Missing from one of the edges on the top layer.");
										return false;
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
		Utils.LOG_INFO("Multiblock Formed.");
		return true;
	}

	@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}

	@Override
	public int getAmountOfOutputs() {
		return 1;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
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
		ArrayList<FluidStack> tFluidList = getStoredFluids();
		for (int i = 0; i < tFluidList.size() - 1; i++) {
			for (int j = i + 1; j < tFluidList.size(); j++) {
				if (GT_Utility.areFluidsEqual((FluidStack) tFluidList.get(i), (FluidStack) tFluidList.get(j))) {
					if (((FluidStack) tFluidList.get(i)).amount >= ((FluidStack) tFluidList.get(j)).amount) {
						tFluidList.remove(j--);
					} else {
						tFluidList.remove(i--);
						break;
					}
				}
			}
		}
		if (tFluidList.size() > 1) {
			FluidStack[] tFluids = tFluidList.toArray(new FluidStack[tFluidList.size()]);
			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sFusionRecipes.findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, false, GT_Values.V[8], tFluids, new ItemStack[]{});
			if (tRecipe == null && !mRunningOnLoad) {
				turnCasingActive(false);
				this.mLastRecipe = null;
				return false;
			}
			
			if (tRecipe == null){
				return false;
			}
			
			if (mRunningOnLoad || tRecipe.isRecipeInputEqual(true, tFluids, new ItemStack[]{})) {
				if (tRecipe != null){
					this.mLastRecipe = tRecipe;	
				}
				this.mEUt = (this.mLastRecipe.mEUt * overclock(this.mLastRecipe.mSpecialValue));
				this.mMaxProgresstime = this.mLastRecipe.mDuration / overclock(this.mLastRecipe.mSpecialValue);
				this.mEfficiencyIncrease = 10000;
				this.mOutputFluids = this.mLastRecipe.mFluidOutputs;
				turnCasingActive(true);
				mRunningOnLoad = false;
				return true;
			}
		}
		return false;
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if (aBaseMetaTileEntity.isServerSide()) {
			if (mEfficiency < 0)
				mEfficiency = 0;
			if (mRunningOnLoad && checkMachine(aBaseMetaTileEntity, mInventory[1])) {
				this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
				checkRecipe(mInventory[1]);
			}
			if (--mUpdate == 0 || --mStartUpCheck == 0) {
				mInputHatches.clear();
				mInputBusses.clear();
				mOutputHatches.clear();
				mOutputBusses.clear();
				mDynamoHatches.clear();
				mEnergyHatches.clear();
				mMufflerHatches.clear();
				mMaintenanceHatches.clear();
				mMachine = checkMachine(aBaseMetaTileEntity, mInventory[1]);
			}
			if (mStartUpCheck < 0) {
				if (mMachine) {
					if (this.mEnergyHatches != null) {
						for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches)
							if (isValidMetaTileEntity(tHatch)) {
								if (aBaseMetaTileEntity.getStoredEU() + (2048 * 4) < maxEUStore()
										&& tHatch.getBaseMetaTileEntity().decreaseStoredEnergyUnits(2048 * 4, false)) {
									aBaseMetaTileEntity.increaseStoredEnergyUnits(2048 * 4, true);
								}
							}
					}
					if (this.mEUStore <= 0 && mMaxProgresstime > 0) {
						stopMachine();
					}
					if (getRepairStatus() > 0) {
						if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {
							this.getBaseMetaTileEntity().decreaseStoredEnergyUnits(mEUt, true);
							if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
								if (mOutputItems != null)
									for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
								if (mOutputFluids != null)
									for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
								mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
								mOutputItems = null;
								mProgresstime = 0;
								mMaxProgresstime = 0;
								mEfficiencyIncrease = 0;
								if (mOutputFluids != null && mOutputFluids.length > 0) {
									try {
										GT_Mod.instance.achievements.issueAchivementHatchFluid(aBaseMetaTileEntity.getWorld().getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()), mOutputFluids[0]);
									} catch (Exception e) {
									}
								}
								this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
								if (aBaseMetaTileEntity.isAllowedToWork())
									checkRecipe(mInventory[1]);
							}
						} else {
							if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()) {
								turnCasingActive(mMaxProgresstime > 0);
								if (aBaseMetaTileEntity.isAllowedToWork()) {
									this.mEUStore = (int) aBaseMetaTileEntity.getStoredEU();
									if (checkRecipe(mInventory[1])) {
										if (this.mEUStore < this.mLastRecipe.mSpecialValue) {
											mMaxProgresstime = 0;
											turnCasingActive(false);
										}
										aBaseMetaTileEntity.decreaseStoredEnergyUnits(this.mLastRecipe.mSpecialValue, true);
									}
								}
								if (mMaxProgresstime <= 0)
									mEfficiency = Math.max(0, mEfficiency - 1000);
							}
						}
					} else {
						this.mLastRecipe = null;
						stopMachine();
					}
				} else {
					turnCasingActive(false);
					this.mLastRecipe = null;
					stopMachine();
				}
			}
			aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8)
					| (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64));
			aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
		}
	}

	@Override
	public boolean onRunningTick(ItemStack aStack) {
		if (mEUt < 0) {
			if (!drainEnergyInput(((long) -mEUt * 10000) / Math.max(1000, mEfficiency))) {
				this.mLastRecipe = null;
				stopMachine();
				return false;
			}
		}
		if (this.mEUStore <= 0) {
			this.mLastRecipe = null;
			stopMachine();
			return false;
		}
		return true;
	}

	@Override
	public boolean drainEnergyInput(long aEU) {
		return false;
	}

	@Override
	public long maxEUStore() {
		return 640000000L * (Math.min(16, this.mEnergyHatches.size())) / 16L;
	}

}