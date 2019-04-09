package gregtech.common.tileentities.machines.multi;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.GT_GUIContainer_AntimatterReactor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GT_MetaTileEntity_AntimatterReactor extends GT_MetaTileEntity_MultiBlockBase {
	
    protected int xDir = 0, zDir = 0;
    protected int mColantConsumption = 0;
	private FluidStack[] mInputFluids;
	
    public GT_MetaTileEntity_AntimatterReactor(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_AntimatterReactor(String aName) {
        super(aName);
    }
    
    public String[] getDescription() {
        return new String[]{
                "REBIRTH IN A NEW ERA!!!",
                "Size(WxHxD): 15x15x15 (Almost Chunk)",
                "116 x Magnetic Coil Block",
                "90 x Radiation Proof Casing",
                "60 x Dyson Ring Casing",
                "40 x Fusion Casing MKII",
                "6 x Core Chamber Casing",
                "6 x Intermix Chamber Casing",
                "2/2 x Input/Output Hatch (All UV)",
                "1/1 x Input/Output Bus (All UV)",
                "1 x Dynamo Hatch (UHV)",
                "2097152 EU/t and 4 L/t plasma (in cooling)"};
    }
    
    //Set textures for main block
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] sTexture;
        if (aSide == aFacing) {
            sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1, Dyes._NULL.mRGBa)), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FUSION1)};
        } else {
            if (!aActive) {
                sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
            } else {
                sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
            }
        }
        return sTexture;
    }
    
    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }
    
    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_AntimatterReactor(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "AntimatterReactor.png", GT_Recipe.GT_Recipe_Map.sAntimatterReactorFuels.mNEIName);
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
             GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map_Fuel.sAntimatterReactorFuels.findRecipe(getBaseMetaTileEntity(), false, Long.MAX_VALUE, tFluids, tInputs);
             if (tRecipe == null && !mRunningOnLoad) {
                 //turnCasingActive(false);
                 return false;
             }
             if (mRunningOnLoad || tRecipe.isRecipeInputEqual(true, tFluids, tInputs)) {
                 this.mEUt = 2097152;
                 this.mMaxProgresstime = tRecipe.mSpecialValue * 20;
                 this.mEfficiency = 10000;
                 this.mEfficiencyIncrease = 10000;
                 this.mInputFluids = tRecipe.mFluidInputs;
                 this.mOutputFluids = tRecipe.mFluidOutputs;
                 this.mOutputItems = tRecipe.mOutputs;
                 this.mColantConsumption = tRecipe.mFluidInputs[0].amount;
                 mRunningOnLoad = false;
                 //turnCasingActive(true);
                 updateSlots();
                 return true;
             }
         }
        return false;
    }
    
    /* Activate Block Casing change textures
    public boolean turnCasingActive(boolean status) {
        if (this.mDynamoHatches != null) {
            for (GT_MetaTileEntity_Hatch_Dynamo hatch : this.mDynamoHatches) {
                hatch.mMachineBlock = status ? (byte) 52 : (byte) 53;
            }
        }
        if (this.mInputHatches != null) {
            for (GT_MetaTileEntity_Hatch_Input hatch : this.mInputHatches) {
                hatch.mMachineBlock = status ? (byte) 52 : (byte) 53;
            }
        }
        return true;
    }*/
    
    //Main logic construct check
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
    	
        xDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetX;
        zDir = ForgeDirection.getOrientation(getBaseMetaTileEntity().getBackFacing()).offsetZ;
    	
    	int xCenter = getBaseMetaTileEntity().getXCoord() + xDir * 7;
        int yCenter = getBaseMetaTileEntity().getYCoord();
        int zCenter = getBaseMetaTileEntity().getZCoord() + zDir * 7;
                
        if ((checkRingInYPlane(xCenter, yCenter, zCenter)) && (checkRingInVPlane(xCenter, yCenter, zCenter)) //Check rings (Dyson Sphere)
        		&& (checkKineticCoils(xCenter, yCenter, zCenter))//Check Kinetic Ring
        		&& (checkMagneticCoilsInXPlane(xCenter, yCenter, zCenter)) && (checkMagneticCoilsInYPlane(xCenter, yCenter, zCenter)) // Check Magnetic Ring
        		&& (checkCoreCasings(xCenter, yCenter, zCenter)) && (checkCoreChamber(xCenter, yCenter, zCenter)) //Check Core Components
        		&& (checkIntermixChamber(xCenter, yCenter, zCenter)) // Check Intermix Chamber
        		&& (addIfEnergyExtractor(xCenter + xDir * 7, yCenter, zCenter + zDir * 7, aBaseMetaTileEntity))
        		&& (addIfFluidIO(xCenter, yCenter, zCenter, aBaseMetaTileEntity))
        		&& (addIfItemIO(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getYCoord(), aBaseMetaTileEntity.getZCoord(), aBaseMetaTileEntity))
        		&& (this.mDynamoHatches.size() == 1) && (this.mInputHatches.size() == 2) && (this.mOutputHatches.size() == 2) && (this.mInputBusses.size() == 1) && (this.mOutputBusses.size() == 1)) {
            if (this.mDynamoHatches != null) {
                for (int i = 0; i < this.mDynamoHatches.size(); i++) {
                    if (this.mDynamoHatches.get(i).mTier < 8)
                        return false;
                }
            }
            if (this.mInputBusses != null) {
                for (int i = 0; i < this.mInputBusses.size(); i++) {
                    if (this.mInputBusses.get(i).mTier < 7)
                        return false;
                }
            }
            if (this.mOutputBusses != null) {
                for (int i = 0; i < this.mOutputBusses.size(); i++) {
                    if (this.mOutputBusses.get(i).mTier < 7)
                        return false;
                }
            }
            if (this.mInputHatches != null) {
                for (int i = 0; i < this.mInputHatches.size(); i++) {
                    if (this.mInputHatches.get(i).mTier < 7)
                        return false;
                }
            }
            if (this.mOutputHatches != null) {
                for (int i = 0; i < this.mOutputHatches.size(); i++) {
                    if (this.mOutputHatches.get(i).mTier < 7)
                        return false;
                }
            }
            mWrench = true;
            mScrewdriver = true;
            mSoftHammer = true;
            mHardHammer = true;
            mSolderingTool = true;
            mCrowbar = true;
            return true;
        }
        return false;
    	
    }
    
    //Check horizontal casing rings
    private boolean checkRingInYPlane(int aX, int aY, int aZ) {
        return (isAdvancedMachineCasing(aX + 7, aY - 1, aZ - 1)) && (isAdvancedMachineCasing(aX + 7, aY + 1, aZ - 1)) && (isAdvancedMachineCasing(aX + 7, aY - 1, aZ + 1)) && (isAdvancedMachineCasing(aX + 7, aY + 1, aZ + 1))
        		&& (isAdvancedMachineCasing(aX + 7, aY, aZ - 2)) && (isAdvancedMachineCasing(aX + 7, aY, aZ + 2)) && (isAdvancedMachineCasing(aX + 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX + 6, aY, aZ + 3))
        		&& (isAdvancedMachineCasing(aX + 5, aY, aZ - 4)) && (isAdvancedMachineCasing(aX + 5, aY, aZ + 4)) && (isAdvancedMachineCasing(aX + 4, aY, aZ - 5)) && (isAdvancedMachineCasing(aX + 4, aY, aZ + 5))
        		&& (isAdvancedMachineCasing(aX + 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX + 3, aY, aZ + 6)) && (isAdvancedMachineCasing(aX + 2, aY, aZ - 7)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 7))
        		&& (isAdvancedMachineCasing(aX + 1, aY - 1, aZ - 7)) && (isAdvancedMachineCasing(aX + 1, aY - 1, aZ + 7)) && (isAdvancedMachineCasing(aX + 1, aY + 1, aZ - 7)) && (isAdvancedMachineCasing(aX + 1, aY + 1, aZ + 7))
        		&& (isAdvancedMachineCasing(aX, aY - 1, aZ - 7)) && (isAdvancedMachineCasing(aX, aY - 1, aZ + 7)) && (isAdvancedMachineCasing(aX, aY + 1, aZ - 7)) && (isAdvancedMachineCasing(aX, aY + 1, aZ + 7))
        		&& (isAdvancedMachineCasing(aX - 1, aY - 1, aZ - 7)) && (isAdvancedMachineCasing(aX - 1, aY - 1, aZ + 7))
        		&& (isAdvancedMachineCasing(aX - 1, aY + 1, aZ - 7)) && (isAdvancedMachineCasing(aX - 1, aY + 1, aZ + 7)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 7)) && (isAdvancedMachineCasing(aX - 2, aY, aZ + 7))
        		&& (isAdvancedMachineCasing(aX - 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX - 3, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 4, aY, aZ - 5)) && (isAdvancedMachineCasing(aX - 4, aY, aZ + 5))
        		&& (isAdvancedMachineCasing(aX - 5, aY, aZ - 4)) && (isAdvancedMachineCasing(aX - 5, aY, aZ + 4)) && (isAdvancedMachineCasing(aX - 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX - 6, aY, aZ + 3))
        		&& (isAdvancedMachineCasing(aX - 7, aY, aZ - 2)) && (isAdvancedMachineCasing(aX - 7, aY, aZ + 2)) && (isAdvancedMachineCasing(aX - 7, aY - 1, aZ - 1)) && (isAdvancedMachineCasing(aX - 7, aY - 1, aZ + 1))
        		&& (isAdvancedMachineCasing(aX - 7, aY + 1, aZ - 1)) && (isAdvancedMachineCasing(aX - 7, aY + 1, aZ + 1))
        		&& (((isAdvancedMachineCasing(aX + 1, aY, aZ - 7)) && (isAdvancedMachineCasing(aX + 1, aY, aZ + 7)) && (isAdvancedMachineCasing(aX, aY, aZ - 7)) && (isAdvancedMachineCasing(aX, aY, aZ + 7)) && (isAdvancedMachineCasing(aX - 1, aY, aZ - 7)) && (isAdvancedMachineCasing(aX - 1, aY, aZ + 7)))
        		|| ((isAdvancedMachineCasing(aX - 7, aY, aZ + 1)) && (isAdvancedMachineCasing(aX + 7, aY, aZ + 1)) && (isAdvancedMachineCasing(aX - 7, aY, aZ)) && (isAdvancedMachineCasing(aX + 7, aY, aZ)) && (isAdvancedMachineCasing(aX - 7, aY, aZ - 1)) && (isAdvancedMachineCasing(aX + 7, aY, aZ -1))));
    } 
    
    //Check vertical casing rings
    private boolean checkRingInVPlane(int aX, int aY, int aZ) {
        return (isAdvancedMachineCasing(aX, aY - 7, aZ)) && (isAdvancedMachineCasing(aX, aY + 7, aZ))
        		&& (((isAdvancedMachineCasing(aX + 7, aY - 1, aZ)) && (isAdvancedMachineCasing(aX + 7, aY + 1, aZ)) && (isAdvancedMachineCasing(aX + 7, aY - 2, aZ)) && (isAdvancedMachineCasing(aX + 7, aY + 2, aZ))
        		&& (isAdvancedMachineCasing(aX + 7, aY - 3, aZ)) && (isAdvancedMachineCasing(aX + 7, aY + 3, aZ)) && (isAdvancedMachineCasing(aX + 6, aY - 4, aZ)) && (isAdvancedMachineCasing(aX + 6, aY + 4, aZ))
        		&& (isAdvancedMachineCasing(aX + 5, aY - 5, aZ)) && (isAdvancedMachineCasing(aX + 5, aY + 5, aZ)) && (isAdvancedMachineCasing(aX + 4, aY - 6, aZ)) && (isAdvancedMachineCasing(aX + 4, aY + 6, aZ))
        		&& (isAdvancedMachineCasing(aX + 3, aY - 7, aZ)) && (isAdvancedMachineCasing(aX + 3, aY + 7, aZ)) && (isAdvancedMachineCasing(aX + 2, aY - 7, aZ)) && (isAdvancedMachineCasing(aX + 2, aY + 7, aZ))
        		&& (isAdvancedMachineCasing(aX + 1, aY - 7, aZ)) && (isAdvancedMachineCasing(aX + 1, aY + 7, aZ))
        		&& (isAdvancedMachineCasing(aX - 1, aY - 7, aZ)) && (isAdvancedMachineCasing(aX - 1, aY + 7, aZ)) && (isAdvancedMachineCasing(aX - 2, aY - 7, aZ)) && (isAdvancedMachineCasing(aX - 2, aY + 7, aZ))
        		&& (isAdvancedMachineCasing(aX - 3, aY - 7, aZ)) && (isAdvancedMachineCasing(aX - 3, aY + 7, aZ)) && (isAdvancedMachineCasing(aX - 4, aY - 6, aZ)) && (isAdvancedMachineCasing(aX - 4, aY + 6, aZ))
        		&& (isAdvancedMachineCasing(aX - 5, aY - 5, aZ)) && (isAdvancedMachineCasing(aX - 5, aY + 5, aZ)) && (isAdvancedMachineCasing(aX - 6, aY - 4, aZ)) && (isAdvancedMachineCasing(aX - 6, aY + 4, aZ))
        		&& (isAdvancedMachineCasing(aX - 7, aY - 3, aZ)) && (isAdvancedMachineCasing(aX - 7, aY + 3, aZ)) && (isAdvancedMachineCasing(aX - 7, aY - 2, aZ)) && (isAdvancedMachineCasing(aX - 7, aY + 2, aZ))
        		&& (isAdvancedMachineCasing(aX - 7, aY - 1, aZ)) && (isAdvancedMachineCasing(aX - 7, aY + 1, aZ)))
        		|| ((isAdvancedMachineCasing(aX, aY - 1, aZ + 7)) && (isAdvancedMachineCasing(aX, aY + 1, aZ + 7)) && (isAdvancedMachineCasing(aX, aY - 2, aZ + 7)) && (isAdvancedMachineCasing(aX, aY + 2, aZ + 7))
                && (isAdvancedMachineCasing(aX, aY - 3, aZ + 7)) && (isAdvancedMachineCasing(aX, aY + 3, aZ + 7)) && (isAdvancedMachineCasing(aX, aY - 4, aZ + 6)) && (isAdvancedMachineCasing(aX, aY + 4, aZ + 6))
                && (isAdvancedMachineCasing(aX, aY - 5, aZ + 5)) && (isAdvancedMachineCasing(aX, aY + 5, aZ + 5)) && (isAdvancedMachineCasing(aX, aY - 6, aZ + 4)) && (isAdvancedMachineCasing(aX, aY + 6, aZ + 4))
                && (isAdvancedMachineCasing(aX, aY - 7, aZ + 3)) && (isAdvancedMachineCasing(aX, aY + 7, aZ + 3)) && (isAdvancedMachineCasing(aX, aY - 7, aZ + 2)) && (isAdvancedMachineCasing(aX, aY + 7, aZ + 2))
                && (isAdvancedMachineCasing(aX, aY - 7, aZ + 1)) && (isAdvancedMachineCasing(aX, aY + 7, aZ + 1))
                && (isAdvancedMachineCasing(aX, aY - 7, aZ - 1)) && (isAdvancedMachineCasing(aX, aY + 7, aZ - 1)) && (isAdvancedMachineCasing(aX, aY - 7, aZ - 2)) && (isAdvancedMachineCasing(aX, aY + 7, aZ - 2))
                && (isAdvancedMachineCasing(aX, aY - 7, aZ - 3)) && (isAdvancedMachineCasing(aX, aY + 7, aZ - 3)) && (isAdvancedMachineCasing(aX, aY - 6, aZ - 4)) && (isAdvancedMachineCasing(aX, aY + 6, aZ - 4))
                && (isAdvancedMachineCasing(aX, aY - 5, aZ - 5)) && (isAdvancedMachineCasing(aX, aY + 5, aZ - 5)) && (isAdvancedMachineCasing(aX, aY - 4, aZ - 6)) && (isAdvancedMachineCasing(aX, aY + 4, aZ - 6))
                && (isAdvancedMachineCasing(aX, aY - 3, aZ - 7)) && (isAdvancedMachineCasing(aX, aY + 3, aZ - 7)) && (isAdvancedMachineCasing(aX, aY - 2, aZ - 7)) && (isAdvancedMachineCasing(aX, aY + 2, aZ - 7))
                && (isAdvancedMachineCasing(aX, aY - 1, aZ - 7)) && (isAdvancedMachineCasing(aX, aY + 1, aZ - 7))));
    }
    
    //Check kinetic coils (Dyson Sphere)
    private boolean checkKineticCoils(int aX, int aY, int aZ) {
    	return (isKineticCoil(aX + 6, aY, aZ)) && (isKineticCoil(aX - 6, aY, aZ)) && (isKineticCoil(aX + 6, aY, aZ + 1)) && (isKineticCoil(aX + 6, aY, aZ - 1))// Horisontal Ring
    			&& (isKineticCoil(aX + 6, aY, aZ + 2)) && (isKineticCoil(aX + 6, aY, aZ - 2)) && (isKineticCoil(aX + 5, aY, aZ + 3)) && (isKineticCoil(aX + 5, aY, aZ - 3))
    			&& (isKineticCoil(aX + 4, aY, aZ + 4)) && (isKineticCoil(aX + 4, aY, aZ - 4)) && (isKineticCoil(aX + 3, aY, aZ + 5)) && (isKineticCoil(aX + 3, aY, aZ - 5))
    			&& (isKineticCoil(aX + 2, aY, aZ + 6)) && (isKineticCoil(aX + 2, aY, aZ - 6)) && (isKineticCoil(aX + 1, aY, aZ + 6)) && (isKineticCoil(aX + 1, aY, aZ - 6))
    			&& (isKineticCoil(aX, aY, aZ + 6)) && (isKineticCoil(aX, aY, aZ - 6)) && (isKineticCoil(aX - 1, aY, aZ + 6)) && (isKineticCoil(aX - 1, aY, aZ - 6))
    			&& (isKineticCoil(aX - 2, aY, aZ + 6)) && (isKineticCoil(aX - 2, aY, aZ - 6)) && (isKineticCoil(aX - 3, aY, aZ + 5)) && (isKineticCoil(aX - 3, aY, aZ - 5))
    			&& (isKineticCoil(aX - 4, aY, aZ + 4)) && (isKineticCoil(aX - 4, aY, aZ - 4)) && (isKineticCoil(aX - 5, aY, aZ + 3)) && (isKineticCoil(aX - 5, aY, aZ - 3))
    			&& (isKineticCoil(aX - 6, aY, aZ + 2)) && (isKineticCoil(aX - 6, aY, aZ - 2)) && (isKineticCoil(aX - 6, aY, aZ + 1)) && (isKineticCoil(aX - 6, aY, aZ - 1))
    			&& (((isKineticCoil(aX + 5, aY, aZ)) && (isKineticCoil(aX - 5, aY, aZ)) && (isKineticCoil(aX + 5, aY + 1, aZ)) && (isKineticCoil(aX + 5, aY - 1, aZ))//Vertical Ring in X Plane
    			&& (isKineticCoil(aX + 5, aY + 2, aZ)) && (isKineticCoil(aX + 5, aY - 2, aZ)) && (isKineticCoil(aX + 4, aY + 3, aZ)) && (isKineticCoil(aX + 4, aY - 3, aZ))
    			&& (isKineticCoil(aX + 3, aY + 4, aZ)) && (isKineticCoil(aX + 3, aY - 4, aZ)) && (isKineticCoil(aX + 2, aY + 5, aZ)) && (isKineticCoil(aX + 2, aY - 5, aZ))
    			&& (isKineticCoil(aX + 1, aY + 5, aZ)) && (isKineticCoil(aX + 1, aY - 5, aZ)) && (isKineticCoil(aX, aY + 5, aZ)) && (isKineticCoil(aX, aY - 5, aZ))
    			&& (isKineticCoil(aX - 1, aY + 5, aZ)) && (isKineticCoil(aX - 1, aY - 5, aZ)) && (isKineticCoil(aX - 2, aY + 5, aZ)) && (isKineticCoil(aX - 2, aY - 5, aZ))
    			&& (isKineticCoil(aX - 3, aY + 4, aZ)) && (isKineticCoil(aX - 3, aY - 4, aZ)) && (isKineticCoil(aX - 4, aY + 3, aZ)) && (isKineticCoil(aX - 4, aY - 3, aZ))
    			&& (isKineticCoil(aX - 5, aY + 2, aZ)) && (isKineticCoil(aX - 5, aY - 2, aZ)) && (isKineticCoil(aX - 5, aY + 1, aZ)) && (isKineticCoil(aX - 5, aY - 1, aZ)))
    			|| ((isKineticCoil(aX, aY, aZ + 5)) && (isKineticCoil(aX, aY, aZ - 5)) && (isKineticCoil(aX, aY + 1, aZ + 5)) && (isKineticCoil(aX, aY - 1, aZ + 5))//Vertical Ring in Z Plane
    	    	&& (isKineticCoil(aX, aY + 2, aZ + 5)) && (isKineticCoil(aX, aY - 2, aZ + 5)) && (isKineticCoil(aX, aY + 3, aZ + 4)) && (isKineticCoil(aX, aY - 3, aZ + 4))
    	    	&& (isKineticCoil(aX, aY + 4, aZ + 3)) && (isKineticCoil(aX, aY - 4, aZ + 3)) && (isKineticCoil(aX, aY + 5, aZ + 2)) && (isKineticCoil(aX, aY - 5, aZ + 2))
    	    	&& (isKineticCoil(aX, aY + 5, aZ + 1)) && (isKineticCoil(aX, aY - 5, aZ + 1)) && (isKineticCoil(aX, aY + 5, aZ)) && (isKineticCoil(aX, aY - 5, aZ))
    	    	&& (isKineticCoil(aX, aY + 5, aZ - 1)) && (isKineticCoil(aX, aY - 5, aZ - 1)) && (isKineticCoil(aX, aY + 5, aZ - 2)) && (isKineticCoil(aX, aY - 5, aZ - 2))
    	    	&& (isKineticCoil(aX, aY + 4, aZ - 3)) && (isKineticCoil(aX, aY - 4, aZ - 3)) && (isKineticCoil(aX, aY + 3, aZ - 4)) && (isKineticCoil(aX, aY - 3, aZ - 4))
    	    	&& (isKineticCoil(aX, aY + 2, aZ - 5)) && (isKineticCoil(aX, aY - 2, aZ - 5)) && (isKineticCoil(aX, aY + 1, aZ - 5)) && (isKineticCoil(aX, aY - 1, aZ - 5))));
    }
    
    //Check magnetic coils
    private boolean checkMagneticCoilsInXPlane(int aX, int aY, int aZ) {
    	return (isMagneticCoil(aX + 7, aY + 1, aZ + 2)) && (isMagneticCoil(aX + 7, aY + 1, aZ - 2)) && (isMagneticCoil(aX + 7, aY - 1, aZ + 2)) && (isMagneticCoil(aX + 7, aY - 1, aZ - 2))//Horisontal
    			&& (isMagneticCoil(aX + 6, aY + 1, aZ + 3)) && (isMagneticCoil(aX + 6, aY + 1, aZ - 3)) && (isMagneticCoil(aX + 6, aY - 1, aZ + 3)) && (isMagneticCoil(aX + 6, aY - 1, aZ - 3))
    			&& (isMagneticCoil(aX + 5, aY + 1, aZ + 4)) && (isMagneticCoil(aX + 5, aY + 1, aZ - 4)) && (isMagneticCoil(aX + 5, aY - 1, aZ + 4)) && (isMagneticCoil(aX + 5, aY - 1, aZ - 4))
    			&& (isMagneticCoil(aX + 4, aY + 1, aZ + 5)) && (isMagneticCoil(aX + 4, aY + 1, aZ - 5)) && (isMagneticCoil(aX + 4, aY - 1, aZ + 5)) && (isMagneticCoil(aX + 4, aY - 1, aZ - 5))
    			&& (isMagneticCoil(aX + 3, aY + 1, aZ + 6)) && (isMagneticCoil(aX + 3, aY + 1, aZ - 6)) && (isMagneticCoil(aX + 3, aY - 1, aZ + 6)) && (isMagneticCoil(aX + 3, aY - 1, aZ - 6))
    			&& (isMagneticCoil(aX + 2, aY + 1, aZ + 7)) && (isMagneticCoil(aX + 2, aY + 1, aZ - 7)) && (isMagneticCoil(aX + 2, aY - 1, aZ + 7)) && (isMagneticCoil(aX + 2, aY - 1, aZ - 7))
    			&& (isMagneticCoil(aX - 7, aY + 1, aZ + 2)) && (isMagneticCoil(aX - 7, aY + 1, aZ - 2)) && (isMagneticCoil(aX - 7, aY - 1, aZ + 2)) && (isMagneticCoil(aX - 7, aY - 1, aZ - 2))//Horisontal
    			&& (isMagneticCoil(aX - 6, aY + 1, aZ + 3)) && (isMagneticCoil(aX - 6, aY + 1, aZ - 3)) && (isMagneticCoil(aX - 6, aY - 1, aZ + 3)) && (isMagneticCoil(aX - 6, aY - 1, aZ - 3))
    			&& (isMagneticCoil(aX - 5, aY + 1, aZ + 4)) && (isMagneticCoil(aX - 5, aY + 1, aZ - 4)) && (isMagneticCoil(aX - 5, aY - 1, aZ + 4)) && (isMagneticCoil(aX - 5, aY - 1, aZ - 4))
    			&& (isMagneticCoil(aX - 4, aY + 1, aZ + 5)) && (isMagneticCoil(aX - 4, aY + 1, aZ - 5)) && (isMagneticCoil(aX - 4, aY - 1, aZ + 5)) && (isMagneticCoil(aX - 4, aY - 1, aZ - 5))
    			&& (isMagneticCoil(aX - 3, aY + 1, aZ + 6)) && (isMagneticCoil(aX - 3, aY + 1, aZ - 6)) && (isMagneticCoil(aX - 3, aY - 1, aZ + 6)) && (isMagneticCoil(aX - 3, aY - 1, aZ - 6))
    			&& (isMagneticCoil(aX - 2, aY + 1, aZ + 7)) && (isMagneticCoil(aX - 2, aY + 1, aZ - 7)) && (isMagneticCoil(aX - 2, aY - 1, aZ + 7)) && (isMagneticCoil(aX - 2, aY - 1, aZ - 7));
    }
    
    private boolean checkMagneticCoilsInYPlane(int aX, int aY, int aZ) {
    	return (((isMagneticCoil(aX + 7, aY + 2, aZ + 1)) && (isMagneticCoil(aX + 7, aY - 2, aZ + 1)) && (isMagneticCoil(aX + 7, aY + 2, aZ - 1)) && (isMagneticCoil(aX + 7, aY - 2, aZ - 1))//Vertical
    			&& (isMagneticCoil(aX + 7, aY + 3, aZ + 1)) && (isMagneticCoil(aX + 7, aY - 3, aZ + 1)) && (isMagneticCoil(aX + 7, aY + 3, aZ - 1)) && (isMagneticCoil(aX + 7, aY - 3, aZ - 1))
    			&& (isMagneticCoil(aX + 6, aY + 4, aZ + 1)) && (isMagneticCoil(aX + 6, aY - 4, aZ + 1)) && (isMagneticCoil(aX + 6, aY + 4, aZ - 1)) && (isMagneticCoil(aX + 6, aY - 4, aZ - 1))
    			&& (isMagneticCoil(aX + 5, aY + 5, aZ + 1)) && (isMagneticCoil(aX + 5, aY - 5, aZ + 1)) && (isMagneticCoil(aX + 5, aY + 5, aZ - 1)) && (isMagneticCoil(aX + 5, aY - 5, aZ - 1))
    			&& (isMagneticCoil(aX + 4, aY + 6, aZ + 1)) && (isMagneticCoil(aX + 4, aY - 6, aZ + 1)) && (isMagneticCoil(aX + 4, aY + 6, aZ - 1)) && (isMagneticCoil(aX + 4, aY - 6, aZ - 1))
    			&& (isMagneticCoil(aX + 3, aY + 7, aZ + 1)) && (isMagneticCoil(aX + 3, aY - 7, aZ + 1)) && (isMagneticCoil(aX + 3, aY + 7, aZ - 1)) && (isMagneticCoil(aX + 3, aY - 7, aZ - 1))
    			&& (isMagneticCoil(aX + 2, aY + 7, aZ + 1)) && (isMagneticCoil(aX + 2, aY - 7, aZ + 1)) && (isMagneticCoil(aX + 2, aY + 7, aZ - 1)) && (isMagneticCoil(aX + 2, aY - 7, aZ - 1))
    			&& (isMagneticCoil(aX + 1, aY + 7, aZ + 1)) && (isMagneticCoil(aX + 1, aY - 7, aZ + 1)) && (isMagneticCoil(aX + 1, aY + 7, aZ - 1)) && (isMagneticCoil(aX + 1, aY - 7, aZ - 1))
    			&& (isMagneticCoil(aX, aY + 7, aZ + 1)) && (isMagneticCoil(aX, aY - 7, aZ + 1)) && (isMagneticCoil(aX, aY + 7, aZ - 1)) && (isMagneticCoil(aX, aY - 7, aZ - 1))
    			&& (isMagneticCoil(aX - 7, aY + 2, aZ + 1)) && (isMagneticCoil(aX - 7, aY - 2, aZ + 1)) && (isMagneticCoil(aX - 7, aY + 2, aZ - 1)) && (isMagneticCoil(aX - 7, aY - 2, aZ - 1))//Vertical
    			&& (isMagneticCoil(aX - 7, aY + 3, aZ + 1)) && (isMagneticCoil(aX - 7, aY - 3, aZ + 1)) && (isMagneticCoil(aX - 7, aY + 3, aZ - 1)) && (isMagneticCoil(aX - 7, aY - 3, aZ - 1))
    			&& (isMagneticCoil(aX - 6, aY + 4, aZ + 1)) && (isMagneticCoil(aX - 6, aY - 4, aZ + 1)) && (isMagneticCoil(aX - 6, aY + 4, aZ - 1)) && (isMagneticCoil(aX - 6, aY - 4, aZ - 1))
    			&& (isMagneticCoil(aX - 5, aY + 5, aZ + 1)) && (isMagneticCoil(aX - 5, aY - 5, aZ + 1)) && (isMagneticCoil(aX - 5, aY + 5, aZ - 1)) && (isMagneticCoil(aX - 5, aY - 5, aZ - 1))
    			&& (isMagneticCoil(aX - 4, aY + 6, aZ + 1)) && (isMagneticCoil(aX - 4, aY - 6, aZ + 1)) && (isMagneticCoil(aX - 4, aY + 6, aZ - 1)) && (isMagneticCoil(aX - 4, aY - 6, aZ - 1))
    			&& (isMagneticCoil(aX - 3, aY + 7, aZ + 1)) && (isMagneticCoil(aX - 3, aY - 7, aZ + 1)) && (isMagneticCoil(aX - 3, aY + 7, aZ - 1)) && (isMagneticCoil(aX - 3, aY - 7, aZ - 1))
    			&& (isMagneticCoil(aX - 2, aY + 7, aZ + 1)) && (isMagneticCoil(aX - 2, aY - 7, aZ + 1)) && (isMagneticCoil(aX - 2, aY + 7, aZ - 1)) && (isMagneticCoil(aX - 2, aY - 7, aZ - 1))
    			&& (isMagneticCoil(aX - 1, aY + 7, aZ + 1)) && (isMagneticCoil(aX - 1, aY - 7, aZ + 1)) && (isMagneticCoil(aX - 1, aY + 7, aZ - 1)) && (isMagneticCoil(aX - 1, aY - 7, aZ - 1)))
    			|| ((isMagneticCoil(aX + 1, aY + 2, aZ + 7)) && (isMagneticCoil(aX + 1, aY - 2, aZ + 7)) && (isMagneticCoil(aX - 1, aY + 2, aZ + 7)) && (isMagneticCoil(aX - 1, aY - 2, aZ + 7))//Vertical
    	    	&& (isMagneticCoil(aX + 1, aY + 3, aZ + 7)) && (isMagneticCoil(aX + 1, aY - 3, aZ + 7)) && (isMagneticCoil(aX - 1, aY + 3, aZ + 7)) && (isMagneticCoil(aX - 1, aY - 3, aZ + 7))
    	    	&& (isMagneticCoil(aX + 1, aY + 4, aZ + 6)) && (isMagneticCoil(aX + 1, aY - 4, aZ + 6)) && (isMagneticCoil(aX - 1, aY + 4, aZ + 6)) && (isMagneticCoil(aX - 1, aY - 4, aZ + 6))
    	    	&& (isMagneticCoil(aX + 1, aY + 5, aZ + 5)) && (isMagneticCoil(aX + 1, aY - 5, aZ + 5)) && (isMagneticCoil(aX - 1, aY + 5, aZ + 5)) && (isMagneticCoil(aX - 1, aY - 5, aZ + 5))
    	    	&& (isMagneticCoil(aX + 1, aY + 6, aZ + 4)) && (isMagneticCoil(aX + 1, aY - 6, aZ + 4)) && (isMagneticCoil(aX - 1, aY + 6, aZ + 4)) && (isMagneticCoil(aX - 1, aY - 6, aZ + 4))
    	    	&& (isMagneticCoil(aX + 1, aY + 7, aZ + 3)) && (isMagneticCoil(aX + 1, aY - 7, aZ + 3)) && (isMagneticCoil(aX - 1, aY + 7, aZ + 3)) && (isMagneticCoil(aX - 1, aY - 7, aZ + 3))
    	    	&& (isMagneticCoil(aX + 1, aY + 7, aZ + 2)) && (isMagneticCoil(aX + 1, aY - 7, aZ + 2)) && (isMagneticCoil(aX - 1, aY + 7, aZ + 2)) && (isMagneticCoil(aX - 1, aY - 7, aZ + 2))
    	    	&& (isMagneticCoil(aX + 1, aY + 7, aZ + 1)) && (isMagneticCoil(aX + 1, aY - 7, aZ + 1)) && (isMagneticCoil(aX - 1, aY + 7, aZ + 1)) && (isMagneticCoil(aX - 1, aY - 7, aZ + 1))
    	    	&& (isMagneticCoil(aX + 1, aY + 7, aZ)) && (isMagneticCoil(aX + 1, aY - 7, aZ)) && (isMagneticCoil(aX - 1, aY + 7, aZ)) && (isMagneticCoil(aX - 1, aY - 7, aZ))
    	    	&& (isMagneticCoil(aX + 1, aY + 2, aZ - 7)) && (isMagneticCoil(aX + 1, aY - 2, aZ - 7)) && (isMagneticCoil(aX - 1, aY + 2, aZ - 7)) && (isMagneticCoil(aX - 1, aY - 2, aZ - 7))//Vertical
    	    	&& (isMagneticCoil(aX + 1, aY + 3, aZ - 7)) && (isMagneticCoil(aX + 1, aY - 3, aZ - 7)) && (isMagneticCoil(aX - 1, aY + 3, aZ - 7)) && (isMagneticCoil(aX - 1, aY - 3, aZ - 7))
    	    	&& (isMagneticCoil(aX + 1, aY + 4, aZ - 6)) && (isMagneticCoil(aX + 1, aY - 4, aZ - 6)) && (isMagneticCoil(aX - 1, aY + 4, aZ - 6)) && (isMagneticCoil(aX - 1, aY - 4, aZ - 6))
    	    	&& (isMagneticCoil(aX + 1, aY + 5, aZ - 5)) && (isMagneticCoil(aX + 1, aY - 5, aZ - 5)) && (isMagneticCoil(aX - 1, aY + 5, aZ - 5)) && (isMagneticCoil(aX - 1, aY - 5, aZ - 5))
    	    	&& (isMagneticCoil(aX + 1, aY + 6, aZ - 4)) && (isMagneticCoil(aX + 1, aY - 6, aZ - 4)) && (isMagneticCoil(aX - 1, aY + 6, aZ - 4)) && (isMagneticCoil(aX - 1, aY - 6, aZ - 4))
    	    	&& (isMagneticCoil(aX + 1, aY + 7, aZ - 3)) && (isMagneticCoil(aX + 1, aY - 7, aZ - 3)) && (isMagneticCoil(aX - 1, aY + 7, aZ - 3)) && (isMagneticCoil(aX - 1, aY - 7, aZ - 3))
    	    	&& (isMagneticCoil(aX + 1, aY + 7, aZ - 2)) && (isMagneticCoil(aX + 1, aY - 7, aZ - 2)) && (isMagneticCoil(aX - 1, aY + 7, aZ - 2)) && (isMagneticCoil(aX - 1, aY - 7, aZ - 2))
    	    	&& (isMagneticCoil(aX + 1, aY + 7, aZ - 1)) && (isMagneticCoil(aX + 1, aY - 7, aZ - 1)) && (isMagneticCoil(aX - 1, aY + 7, aZ - 1)) && (isMagneticCoil(aX - 1, aY - 7, aZ - 1))));
    }
    
    //Check core casing
    private boolean checkCoreCasings(int aX, int aY, int aZ) {
    	return (isCoreMachineCasing(aX + 2, aY, aZ)) && (isCoreMachineCasing(aX + 2, aY, aZ + 1)) && (isCoreMachineCasing(aX + 2, aY, aZ - 1))//Central layer
    			&& (isCoreMachineCasing(aX - 2, aY, aZ)) && (isCoreMachineCasing(aX - 2, aY, aZ + 1)) && (isCoreMachineCasing(aX - 2, aY, aZ - 1))
    			&&(isCoreMachineCasing(aX, aY, aZ + 2)) && (isCoreMachineCasing(aX + 1, aY, aZ + 2)) && (isCoreMachineCasing(aX - 1, aY, aZ + 2))
    			&&(isCoreMachineCasing(aX, aY, aZ - 2)) && (isCoreMachineCasing(aX + 1, aY, aZ - 2)) && (isCoreMachineCasing(aX - 1, aY, aZ - 2))
    			&&(isCoreMachineCasing(aX + 1, aY, aZ + 1)) && (isCoreMachineCasing(aX + 1, aY, aZ - 1)) && (isCoreMachineCasing(aX - 1, aY, aZ + 1)) && (isCoreMachineCasing(aX - 1, aY, aZ - 1))
    			&&(isCoreMachineCasing(aX + 2, aY + 1, aZ)) && (isCoreMachineCasing(aX + 1, aY + 1, aZ)) && (isCoreMachineCasing(aX + 1, aY + 1, aZ + 1)) && (isCoreMachineCasing(aX + 1, aY + 1, aZ - 1))//Upper layer
    			&&(isCoreMachineCasing(aX, aY + 1, aZ + 2)) && (isCoreMachineCasing(aX, aY + 1, aZ + 1)) && (isCoreMachineCasing(aX, aY + 1, aZ - 1)) && (isCoreMachineCasing(aX, aY + 1, aZ - 2))
    			&&(isCoreMachineCasing(aX - 2, aY + 1, aZ)) && (isCoreMachineCasing(aX - 1, aY + 1, aZ)) && (isCoreMachineCasing(aX - 1, aY + 1, aZ + 1)) && (isCoreMachineCasing(aX - 1, aY + 1, aZ - 1))
    			&&(isCoreMachineCasing(aX + 2, aY - 1, aZ)) && (isCoreMachineCasing(aX + 1, aY - 1, aZ)) && (isCoreMachineCasing(aX + 1, aY - 1, aZ + 1)) && (isCoreMachineCasing(aX + 1, aY - 1, aZ - 1))//Lower layer
    			&&(isCoreMachineCasing(aX, aY - 1, aZ + 2)) && (isCoreMachineCasing(aX, aY - 1, aZ + 1)) && (isCoreMachineCasing(aX, aY - 1, aZ - 1)) && (isCoreMachineCasing(aX, aY - 1, aZ - 2))
    			&&(isCoreMachineCasing(aX - 2, aY - 1, aZ)) && (isCoreMachineCasing(aX - 1, aY - 1, aZ)) && (isCoreMachineCasing(aX - 1, aY - 1, aZ + 1)) && (isCoreMachineCasing(aX - 1, aY - 1, aZ - 1));
    }
    
    //Check core chamber
    private boolean checkCoreChamber(int aX, int aY, int aZ) {
    	return (isCoreChamberCasing(aX + 1, aY, aZ)) && (isCoreChamberCasing(aX - 1, aY, aZ)) 
    			&& (isCoreChamberCasing(aX, aY, aZ + 1)) && (isCoreChamberCasing(aX, aY, aZ - 1))
    			&& (isCoreChamberCasing(aX, aY + 1, aZ)) && (isCoreChamberCasing(aX, aY - 1, aZ));
    }
    
    //Check intermix chamber
    private boolean checkIntermixChamber(int aX, int aY, int aZ) {
    	
    	if (xDir == 1 || xDir == -1) {
    		return (isIntermixChamberCasing(aX, aY + 2, aZ)) && (isIntermixChamberCasing(aX + 1, aY + 2, aZ)) && (isIntermixChamberCasing(aX - 1, aY + 2, aZ))
        			&&(isIntermixChamberCasing(aX, aY - 2, aZ)) && (isIntermixChamberCasing(aX + 1, aY - 2, aZ)) && (isIntermixChamberCasing(aX - 1, aY - 2, aZ));
    	}
    	return (isIntermixChamberCasing(aX, aY + 2, aZ)) && (isIntermixChamberCasing(aX, aY + 2, aZ + 1)) && (isIntermixChamberCasing(aX, aY + 2, aZ - 1))
    			&&(isIntermixChamberCasing(aX, aY - 2, aZ)) && (isIntermixChamberCasing(aX, aY - 2, aZ + 1)) && (isIntermixChamberCasing(aX, aY - 2, aZ - 1));
    }

    //Check and add Dynamo Hatch, and Casing on his side
    private boolean addIfEnergyExtractor(int aX, int aY, int aZ, IGregTechTileEntity aTileEntity) {
    	
    	if(xDir == 1 || xDir == -1) {
    		return (addDynamoToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY, aZ), 44))
            		&& isAdvancedMachineCasing(aX, aY, aZ - 1) && isAdvancedMachineCasing(aX, aY, aZ + 1);
    	}
        return (addDynamoToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY, aZ), 44))
        		&& isAdvancedMachineCasing(aX - 1, aY, aZ) && isAdvancedMachineCasing(aX + 1, aY, aZ);
    }
    
    //Check and add Fluid IO Hatch for Intermix chamber
    private boolean addIfFluidIO(int aX, int aY, int aZ, IGregTechTileEntity aTileEntity) {
    	
    	if (xDir == 1 || xDir == -1) {
    		return ((addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY + 2, aZ - 1), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY + 2, aZ + 1), 44))
    				&& (addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY - 2, aZ - 1), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY - 2, aZ + 1), 44)))
    				|| (((addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY + 2, aZ + 1), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY + 2, aZ - 1), 44))
    				&& (addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY - 2, aZ + 1), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY - 2, aZ - 1), 44))));
    	}
        return ((addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX - 1, aY + 2, aZ), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX + 1, aY + 2, aZ), 44))
				&& (addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX - 1, aY - 2, aZ), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX + 1, aY - 2, aZ), 44)))
				|| (((addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX + 1, aY + 2, aZ), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX - 1, aY + 2, aZ), 44))
				&& (addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX + 1, aY - 2, aZ), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX - 1, aY - 2, aZ), 44))));
    }
    
    //Check and add Item IO Bus for fuel
    private boolean addIfItemIO(int aX, int aY, int aZ, IGregTechTileEntity aTileEntity) {
    	
    	IMetaTileEntity aMetaTileEntity1 = aTileEntity.getIGregTechTileEntity(aX - zDir, aY, aZ - xDir).getMetaTileEntity();
    	IMetaTileEntity aMetaTileEntity2 = aTileEntity.getIGregTechTileEntity(aX + zDir, aY, aZ + xDir).getMetaTileEntity();
    	
    	//Check on Bus. Only Bus may place here
    	if (aMetaTileEntity1 instanceof GT_MetaTileEntity_Hatch_OutputBus || aMetaTileEntity1 instanceof GT_MetaTileEntity_Hatch_InputBus) {
    			if (aMetaTileEntity2 instanceof GT_MetaTileEntity_Hatch_OutputBus || aMetaTileEntity2 instanceof GT_MetaTileEntity_Hatch_InputBus) {
    				//DO NOTHING
    			} else return false;	
    	} else return false;
    	
        return ((addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX - zDir, aY, aZ - xDir), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX + zDir, aY, aZ + xDir), 44)))
				|| ((addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX + zDir, aY, aZ + xDir), 44)) && (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX - zDir, aY, aZ - xDir), 44)));
    }
    
    //Check needed block for Ring
    private boolean isAdvancedMachineCasing(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == GregTech_API.sBlockCasings3) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == 12);
    }
    
  //Check needed Kinetic Coil
    private boolean isKineticCoil(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == GregTech_API.sBlockCasings5) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == 14);
    }
    
  //Check needed Magnetic Coil
    private boolean isMagneticCoil(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == GregTech_API.sBlockCasings5) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == 7);
    }
    
    //Check needed Core Casing
    private boolean isCoreMachineCasing(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == GregTech_API.sBlockCasings4) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == 8);
    }
    
    //Check needed Core Chamber Casing
    private boolean isCoreChamberCasing(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == GregTech_API.sBlockCasings5) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == 15);
    }
    
    //Check Intermix Chamber Casing
    private boolean isIntermixChamberCasing(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == GregTech_API.sBlockCasings5) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == 8);
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
    
    //Called every tick. If no colant plasma - reacting stop
    @Override
    public boolean onRunningTick(ItemStack aStack) {
    	if (mEUt > 0) {
            if (depleteInput(mInputFluids[0])) {
        		addEnergyOutput(((long) mEUt * mEfficiency) / 10000);
        		addOutput(mOutputFluids[0]);
        		return true;  
            }
        }
        stopMachine();
        return false;
    }
    
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_AntimatterReactor(this.mName);
    }
    
    //Damage for item in GUI
    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }
    
    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }
    
    /*@Override
    public int getAmountOfOutputs() {
        return 1;
    }*/
    
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }
    
    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }
    
    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
    
    //CHANGE!!! Info for netCard? @.@
    public String[] getInfoData() {
        return new String[]{
            "Antimatter Reactor MKI",
            "Current Output: "+mEUt+" EU/t",
            "Current Colant Using: "+(mColantConsumption)+"L/t",
            "Reacting time: "+(mMaxProgresstime/20)+" sec",
            "Remaining reacting time: "+((mMaxProgresstime - mProgresstime)/20)+" sec"};
    }
    
    @Override
    public boolean isGivingInformation() {
        return true;
    }
}
