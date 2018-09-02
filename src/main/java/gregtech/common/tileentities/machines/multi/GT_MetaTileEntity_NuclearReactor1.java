package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;

public class GT_MetaTileEntity_NuclearReactor1
        extends GT_MetaTileEntity_MultiBlockBase {
	private FluidStack[] mInputFluids;
    public GT_MetaTileEntity_NuclearReactor1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_NuclearReactor1(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_NuclearReactor1(this.mName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Nuclear Reactor Mk 1",
                "Generate steam and energy from nuclear reaction",
                "Size(WxHxD): 3x3x3 (Hollow), Controller (Front centered)",
                "1x Input Bus (Any casing)",
                "1x Input Hatch (Any casing)",
                "1x Output Bus (Any casing)",
                "1x Output Hatch (Any casing)",
                "1x Dynamo Hatch (Any casing)",
                "Frost Proof Machine Casings for the rest (16 at least!)"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[44]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "VacuumFreezer.png");
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
    	 ArrayList<ItemStack> tInputList = getStoredInputs();
         for (int i = 0; i < tInputList.size() - 1; i++) {
             for (int j = i + 1; j < tInputList.size(); j++) {
                 if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
                     if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
                         tInputList.remove(j--);
                     } else {
                         tInputList.remove(i--);
                         break;
                     }
                 }
             }
         }
         ItemStack[] tInputs = (ItemStack[]) Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

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
         FluidStack[] tFluids = (FluidStack[]) Arrays.copyOfRange(tFluidList.toArray(new FluidStack[tInputList.size()]), 0, 1);
         
         if (tInputList.size() > 1) {
             GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map_Fuel.sNuclearReactor1Fuels.findRecipe(getBaseMetaTileEntity(), false, Long.MAX_VALUE, tFluids, tInputs);
             if (tRecipe == null && !mRunningOnLoad) {
                 //turnCasingActive(false);
                 return false;
             }
             if (mRunningOnLoad || tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                 this.mEUt = 2048;
                 this.mMaxProgresstime = tRecipe.mSpecialValue * 20;
                 this.mEfficiency = 10000;
                 this.mEfficiencyIncrease = 10000;
                 this.mInputFluids = tRecipe.mFluidInputs;
                 this.mOutputFluids = tRecipe.mFluidOutputs;
                 this.mOutputItems = tRecipe.mOutputs;
                 
                 mRunningOnLoad = false;
                 //turnCasingActive(true);
                 updateSlots();
                 return true;
             }
         }
        return false;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
            return false;
        }
        int tAmount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if ((!addInputToMachineList(tTileEntity, 12)) && (!addOutputToMachineList(tTileEntity, 12)) && (!addDynamoToMachineList(tTileEntity, 12))) {
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings3) {
                                return false;
                            }
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 1) {
                                return false;
                            }
                            tAmount++;
                        }
                    }
                }
            }
        }
        return tAmount >= 16;
    }
    
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mEfficiency < 0) mEfficiency = 0;
            if (mRunningOnLoad && checkMachine(aBaseMetaTileEntity, mInventory[1])) {
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
                	if (this.mInputFluids == null && mMaxProgresstime > 0) {
                        stopMachine();
                    }
                    if (getRepairStatus() > 0) {
                        if (mMaxProgresstime > 0 && doRandomMaintenanceDamage()) {
							if (onRunningTick(mInventory[1])) {
								if (mMaxProgresstime > 0 && ++mProgresstime >= mMaxProgresstime) {
									if (mOutputItems != null) 
										for (ItemStack tStack : mOutputItems) if (tStack != null) addOutput(tStack);
									if (mInputFluids != null) 
										for (FluidStack tStack : mInputFluids) if (tStack != null) depleteInput(tStack);
									if (mOutputFluids != null) 
										for (FluidStack tStack : mOutputFluids) if (tStack != null) addOutput(tStack);
									mEfficiency = Math.max(0, Math.min(mEfficiency + mEfficiencyIncrease, getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
									mProgresstime = 0;
									mMaxProgresstime = 0;
									mEfficiencyIncrease = 0;
									if (aBaseMetaTileEntity.isAllowedToWork()) checkRecipe(mInventory[1]);
								}
							}
                        } else {
                            if (aTick % 100 == 0 || aBaseMetaTileEntity.hasWorkJustBeenEnabled() || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                                //turnCasingActive(mMaxProgresstime > 0);
                                if (aBaseMetaTileEntity.isAllowedToWork()) {
                                    checkRecipe(mInventory[1]);
                                }
                                if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
                            }
                        }
                    } else {
                        stopMachine();
                    }
                } else {
                    //turnCasingActive(false);
                    stopMachine();
                }
            }
            aBaseMetaTileEntity.setErrorDisplayID((aBaseMetaTileEntity.getErrorDisplayID() & ~127) | (mWrench ? 0 : 1) | (mScrewdriver ? 0 : 2) | (mSoftHammer ? 0 : 4) | (mHardHammer ? 0 : 8) | (mSolderingTool ? 0 : 16) | (mCrowbar ? 0 : 32) | (mMachine ? 0 : 64));
            aBaseMetaTileEntity.setActive(mMaxProgresstime > 0);
        }
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
