package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class GregtechMetaTileEntityMultiTank
        extends GregtechMeta_MultiBlockBase {
    public GregtechMetaTileEntityMultiTank(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private long fluidStored = 0;
    private short multiblockCasingCount = 0;
    private short storageMultiplier = getStorageMultiplier();
    private long maximumFluidStorage = getMaximumTankStorage();
    
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mEUt", mEUt);
        aNBT.setInteger("mProgresstime", mProgresstime);
        aNBT.setInteger("mMaxProgresstime", mMaxProgresstime);
        aNBT.setInteger("mEfficiencyIncrease", mEfficiencyIncrease);
        aNBT.setInteger("mEfficiency", mEfficiency);
        aNBT.setInteger("mPollution", mPollution);
        aNBT.setInteger("mRuntime", mRuntime);
        aNBT.setLong("mFluidStored", fluidStored);
        aNBT.setShort("mStorageMultiplier", storageMultiplier);
        aNBT.setLong("mMaxFluidStored", maximumFluidStorage);
        aNBT.setShort("mCasingCount", multiblockCasingCount);

        if (mOutputItems != null) for (int i = 0; i < mOutputItems.length; i++)
            if (mOutputItems[i] != null) {
                NBTTagCompound tNBT = new NBTTagCompound();
                mOutputItems[i].writeToNBT(tNBT);
                aNBT.setTag("mOutputItem" + i, tNBT);
            }
        if (mOutputFluids != null) for (int i = 0; i < mOutputFluids.length; i++)
            if (mOutputFluids[i] != null) {
                NBTTagCompound tNBT = new NBTTagCompound();
                mOutputFluids[i].writeToNBT(tNBT);
                aNBT.setTag("mOutputFluids" + i, tNBT);
            }

        aNBT.setBoolean("mWrench", mWrench);
        aNBT.setBoolean("mScrewdriver", mScrewdriver);
        aNBT.setBoolean("mSoftHammer", mSoftHammer);
        aNBT.setBoolean("mHardHammer", mHardHammer);
        aNBT.setBoolean("mSolderingTool", mSolderingTool);
        aNBT.setBoolean("mCrowbar", mCrowbar);
    }
    
    private short getStorageMultiplier(){
    	int tempstorageMultiplier = (1*multiblockCasingCount);
    	if (tempstorageMultiplier <= 0){
    		return 1;
    	}
    	return (short) tempstorageMultiplier;
    }

    private long getMaximumTankStorage(){
    	int multiplier = getStorageMultiplier();
    	Utils.LOG_INFO("x = "+multiplier+" * 96000");
    	long tempTankStorageMax = (96000*multiplier);
    	Utils.LOG_INFO("x = "+tempTankStorageMax);
    	if (tempTankStorageMax <= 0){
    		return 96000;
    	}
    	return tempTankStorageMax;
    }

    
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mEUt = aNBT.getInteger("mEUt");
        mProgresstime = aNBT.getInteger("mProgresstime");
        mMaxProgresstime = aNBT.getInteger("mMaxProgresstime");
        if (mMaxProgresstime > 0) mRunningOnLoad = true;
        mEfficiencyIncrease = aNBT.getInteger("mEfficiencyIncrease");
        mEfficiency = aNBT.getInteger("mEfficiency");
        mPollution = aNBT.getInteger("mPollution");
        mRuntime = aNBT.getInteger("mRuntime");
        fluidStored = aNBT.getLong("mFluidStored");
        storageMultiplier = aNBT.getShort("mStorageMultiplier");
        maximumFluidStorage = aNBT.getLong("mMaxFluidStored");
        multiblockCasingCount = aNBT.getShort("mCasingCount");
        mOutputItems = new ItemStack[getAmountOfOutputs()];
        for (int i = 0; i < mOutputItems.length; i++) mOutputItems[i] = GT_Utility.loadItem(aNBT, "mOutputItem" + i);
        mOutputFluids = new FluidStack[getAmountOfOutputs()];
        for (int i = 0; i < mOutputFluids.length; i++)
            mOutputFluids[i] = GT_Utility.loadFluid(aNBT, "mOutputFluids" + i);
        mWrench = aNBT.getBoolean("mWrench");
        mScrewdriver = aNBT.getBoolean("mScrewdriver");
        mSoftHammer = aNBT.getBoolean("mSoftHammer");
        mHardHammer = aNBT.getBoolean("mHardHammer");
        mSolderingTool = aNBT.getBoolean("mSolderingTool");
        mCrowbar = aNBT.getBoolean("mCrowbar");
    }
    
    public GregtechMetaTileEntityMultiTank(String aName) {
        super(aName);
    }

    @Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityMultiTank(this.mName);
    }

    @Override
	public String[] getDescription() {
        return new String[]{
        		"Controller Block for the Multitank",
        		"Size: 3xHx3 (Block behind controller must be air)", 
        		"Structure must be at least 4 blocks tall, maximum 20.",
        		"Each casing within the structure adds 96000L storage.",
        		"Controller (front centered)",
        		"1x Input hatch (anywhere)", 
        		"1x Output hatch (anywhere)",
        		"1x Energy Hatch (anywhere)",
        		"1x Maintenance Hatch (anywhere)",
        		"Frost Proof Casings for the rest (16 at least!)",
        		"Stored Fluid: "+fluidStored};
    }

    @Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[17]};
    }

    @Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "VacuumFreezer.png");
    }

    @Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sVacuumRecipes;
    }

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
	public boolean isFacingValid(byte aFacing) {
        return aFacing > 1;
    }

    @Override
	public boolean checkRecipe(ItemStack aStack) {
        ArrayList<ItemStack> tInputList = getStoredInputs();
        for (ItemStack tInput : tInputList) {
            long tVoltage = getMaxInputVoltage();
            byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sVacuumRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], null, new ItemStack[]{tInput});
            if (tRecipe != null) {
                if (tRecipe.isRecipeInputEqual(true, null, new ItemStack[]{tInput})) {
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    if (tRecipe.mEUt <= 16) {
                        this.mEUt = (tRecipe.mEUt * (1 << tTier - 1) * (1 << tTier - 1));
                        this.mMaxProgresstime = (tRecipe.mDuration / (1 << tTier - 1));
                    } else {
                        this.mEUt = tRecipe.mEUt;
                        this.mMaxProgresstime = tRecipe.mDuration;
                        while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                            this.mEUt *= 4;
                            this.mMaxProgresstime /= 2;
                        }
                    }
                    if (this.mEUt > 0) {
                        this.mEUt = (-this.mEUt);
                    }
                    this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
                    this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
                    updateSlots();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 0, zDir)) {
        	Utils.LOG_INFO("Must be hollow.");
            return false;
        }
        int tAmount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 19; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if ((!addMaintenanceToMachineList(tTileEntity, 17)) && (!addInputToMachineList(tTileEntity, 17)) && (!addOutputToMachineList(tTileEntity, 17)) && (!addEnergyInputToMachineList(tTileEntity, 17))) {
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GregTech_API.sBlockCasings2) {
                            	if (h < 3){
                                	Utils.LOG_INFO("Casing Expected.");
                                    return false;
                            	}
                            	else if (h >= 3){
                                	//Utils.LOG_INFO("Your Multitank can be 20 blocks tall.");
                            	}
                            }
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 1) {
                            	if (h < 3){
                                	Utils.LOG_INFO("Wrong Meta.");
                                    return false;
                            	}
                            	else if (h >= 3){
                                	//Utils.LOG_INFO("Your Multitank can be 20 blocks tall.");
                            	}
                            }
                            if (h < 3){
                                tAmount++;
                            }
                            else if (h >= 3){
                            	if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) == Blocks.air || aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j).getUnlocalizedName().contains("residual")){
                                	Utils.LOG_INFO("Found air");
                            	}
                            	else {
                            		Utils.LOG_INFO("Layer "+(h+2)+" is complete. Adding "+(64000*9)+"L storage to the tank.");
                                    tAmount++;
                            	}
                        	}
                        }
                    }
                }
            }
        }
        multiblockCasingCount = (short) tAmount;
        Utils.LOG_INFO("Your Multitank can be 20 blocks tall.");
        Utils.LOG_INFO("Casings Count: "+tAmount+" Valid Multiblock: "+(tAmount >= 16)+" Tank Storage Capacity:"+getMaximumTankStorage()+"L");
        return tAmount >= 16;
    }

    @Override
	public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
	public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

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
}
