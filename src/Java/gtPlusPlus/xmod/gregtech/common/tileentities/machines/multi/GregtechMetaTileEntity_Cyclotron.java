package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_MultiMachine;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_Cyclotron extends GT_MetaTileEntity_MultiBlockBase {

    public GT_Recipe mLastRecipe;
    public int mEUStore;

    public GregtechMetaTileEntity_Cyclotron(int aID, String aName, String aNameRegional, int tier) {
        super(aID, aName, aNameRegional);
    }

    public GregtechMetaTileEntity_Cyclotron(String aName) {
        super(aName);
    }

    public int tier(){
        return 6;
    }

    @Override
	public long maxEUStore() {
        return (640010000L/4) * (Math.min(16, this.mEnergyHatches.size())) / 16L;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        //return new GT_GUIContainer_FusionReactor(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "CyclotronComputer.png", GT_Recipe.GT_Recipe_Map.sFusionRecipes.mNEIName);
        return null;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_Cyclotron(this.mName);
	}

    @Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aStack) {
        return aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xCenter = getBaseMetaTileEntity().getXCoord() + ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetX * 5;
        int yCenter = getBaseMetaTileEntity().getYCoord();
        int zCenter = getBaseMetaTileEntity().getZCoord() + ForgeDirection.getOrientation(getBaseMetaTileEntity().getFrontFacing()).offsetZ * 5;
        if (((isAdvancedMachineCasing(xCenter + 5, yCenter, zCenter)) || (xCenter + 5 == getBaseMetaTileEntity().getXCoord()))
                && ((isAdvancedMachineCasing(xCenter - 5, yCenter, zCenter)) || (xCenter - 5 == getBaseMetaTileEntity().getXCoord()))
                && ((isAdvancedMachineCasing(xCenter, yCenter, zCenter + 5)) || (zCenter + 5 == getBaseMetaTileEntity().getZCoord()))
                && ((isAdvancedMachineCasing(xCenter, yCenter, zCenter - 5)) || (zCenter - 5 == getBaseMetaTileEntity().getZCoord())) && (checkCoils(xCenter, yCenter, zCenter))
                && (checkHulls(xCenter, yCenter, zCenter)) && (checkUpperOrLowerHulls(xCenter, yCenter + 1, zCenter)) && (checkUpperOrLowerHulls(xCenter, yCenter - 1, zCenter))
                && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter + 3, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter - 3, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter + 5, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter + 4, yCenter, zCenter - 5, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter + 3, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter - 3, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter + 5, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 4, yCenter, zCenter - 5, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 3, yCenter, zCenter + 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 3, yCenter, zCenter + 4, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 5, yCenter, zCenter + 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 5, yCenter, zCenter + 4, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 3, yCenter, zCenter - 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 3, yCenter, zCenter - 4, aBaseMetaTileEntity))
                && (addIfEnergyInjector(xCenter + 5, yCenter, zCenter - 4, aBaseMetaTileEntity)) && (addIfEnergyInjector(xCenter - 5, yCenter, zCenter - 4, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 1, yCenter, zCenter - 5, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 1, yCenter, zCenter + 5, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 1, yCenter, zCenter - 5, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 1, yCenter, zCenter + 5, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 1, yCenter, zCenter - 7, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 1, yCenter, zCenter + 7, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 1, yCenter, zCenter - 7, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 1, yCenter, zCenter + 7, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 5, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 5, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 5, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 5, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter + 7, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter + 7, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfExtractor(xCenter - 7, yCenter, zCenter - 1, aBaseMetaTileEntity)) && (addIfExtractor(xCenter - 7, yCenter, zCenter + 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter + 1, yCenter + 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 1, yCenter + 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 1, yCenter + 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter - 1, yCenter + 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter + 1, zCenter + 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter + 1, zCenter + 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter + 1, zCenter - 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter + 1, zCenter - 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter + 1, yCenter - 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 1, yCenter - 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 1, yCenter - 1, zCenter - 6, aBaseMetaTileEntity)) && (addIfInjector(xCenter - 1, yCenter - 1, zCenter + 6, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter - 1, zCenter + 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter - 1, zCenter + 1, aBaseMetaTileEntity))
                && (addIfInjector(xCenter - 6, yCenter - 1, zCenter - 1, aBaseMetaTileEntity)) && (addIfInjector(xCenter + 6, yCenter - 1, zCenter - 1, aBaseMetaTileEntity))
                && (this.mEnergyHatches.size() >= 1) && (this.mOutputHatches.size() >= 1) && (this.mInputHatches.size() >= 2)) {
            int mEnergyHatches_sS = this.mEnergyHatches.size();
            for (int i = 0; i < mEnergyHatches_sS; i++) {
                if (this.mEnergyHatches.get(i).mTier < tier())
                    return false;
            }
            int mOutputHatches_sS = this.mOutputHatches.size();
            for (int i = 0; i < mOutputHatches_sS; i++) {
                if (this.mOutputHatches.get(i).mTier < tier())
                    return false;
            }
            int mInputHatches_sS = this.mInputHatches.size();
            for (int i = 0; i < mInputHatches_sS; i++) {
                if (this.mInputHatches.get(i).mTier < tier())
                    return false;
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

    private boolean checkCoils(int aX, int aY, int aZ) {
        return (isCyclotronCoil(aX + 6, aY, aZ - 1)) && (isCyclotronCoil(aX + 6, aY, aZ)) && (isCyclotronCoil(aX + 6, aY, aZ + 1)) && (isCyclotronCoil(aX + 5, aY, aZ - 3)) && (isCyclotronCoil(aX + 5, aY, aZ - 2))
                && (isCyclotronCoil(aX + 5, aY, aZ + 2)) && (isCyclotronCoil(aX + 5, aY, aZ + 3)) && (isCyclotronCoil(aX + 4, aY, aZ - 4)) && (isCyclotronCoil(aX + 4, aY, aZ + 4))
                && (isCyclotronCoil(aX + 3, aY, aZ - 5)) && (isCyclotronCoil(aX + 3, aY, aZ + 5)) && (isCyclotronCoil(aX + 2, aY, aZ - 5)) && (isCyclotronCoil(aX + 2, aY, aZ + 5))
                && (isCyclotronCoil(aX + 1, aY, aZ - 6)) && (isCyclotronCoil(aX + 1, aY, aZ + 6)) && (isCyclotronCoil(aX, aY, aZ - 6)) && (isCyclotronCoil(aX, aY, aZ + 6)) && (isCyclotronCoil(aX - 1, aY, aZ - 6))
                && (isCyclotronCoil(aX - 1, aY, aZ + 6)) && (isCyclotronCoil(aX - 2, aY, aZ - 5)) && (isCyclotronCoil(aX - 2, aY, aZ + 5)) && (isCyclotronCoil(aX - 3, aY, aZ - 5))
                && (isCyclotronCoil(aX - 3, aY, aZ + 5)) && (isCyclotronCoil(aX - 4, aY, aZ - 4)) && (isCyclotronCoil(aX - 4, aY, aZ + 4)) && (isCyclotronCoil(aX - 5, aY, aZ - 3))
                && (isCyclotronCoil(aX - 5, aY, aZ - 2)) && (isCyclotronCoil(aX - 5, aY, aZ + 2)) && (isCyclotronCoil(aX - 5, aY, aZ + 3)) && (isCyclotronCoil(aX - 6, aY, aZ - 1))
                && (isCyclotronCoil(aX - 6, aY, aZ)) && (isCyclotronCoil(aX - 6, aY, aZ + 1));
    }

    private boolean checkUpperOrLowerHulls(int aX, int aY, int aZ) {
        return (isAdvancedMachineCasing(aX + 6, aY, aZ)) && (isAdvancedMachineCasing(aX + 5, aY, aZ - 3)) && (isAdvancedMachineCasing(aX + 5, aY, aZ - 2))
                && (isAdvancedMachineCasing(aX + 5, aY, aZ + 2)) && (isAdvancedMachineCasing(aX + 5, aY, aZ + 3)) && (isAdvancedMachineCasing(aX + 4, aY, aZ - 4))
                && (isAdvancedMachineCasing(aX + 4, aY, aZ + 4)) && (isAdvancedMachineCasing(aX + 3, aY, aZ - 5)) && (isAdvancedMachineCasing(aX + 3, aY, aZ + 5))
                && (isAdvancedMachineCasing(aX + 2, aY, aZ - 5)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 5)) && (isAdvancedMachineCasing(aX, aY, aZ - 6))
                && (isAdvancedMachineCasing(aX, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 5)) && (isAdvancedMachineCasing(aX - 2, aY, aZ + 5))
                && (isAdvancedMachineCasing(aX - 3, aY, aZ - 5)) && (isAdvancedMachineCasing(aX - 3, aY, aZ + 5)) && (isAdvancedMachineCasing(aX - 4, aY, aZ - 4))
                && (isAdvancedMachineCasing(aX - 4, aY, aZ + 4)) && (isAdvancedMachineCasing(aX - 5, aY, aZ - 3)) && (isAdvancedMachineCasing(aX - 5, aY, aZ - 2))
                && (isAdvancedMachineCasing(aX - 5, aY, aZ + 2)) && (isAdvancedMachineCasing(aX - 5, aY, aZ + 3)) && (isAdvancedMachineCasing(aX - 6, aY, aZ));
    }

    private boolean checkHulls(int aX, int aY, int aZ) {
        return (isAdvancedMachineCasing(aX + 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX + 6, aY, aZ - 2)) && (isAdvancedMachineCasing(aX + 6, aY, aZ + 2))
                && (isAdvancedMachineCasing(aX + 6, aY, aZ + 3)) && (isAdvancedMachineCasing(aX + 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX + 3, aY, aZ + 6))
                && (isAdvancedMachineCasing(aX + 2, aY, aZ - 6)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 6))
                && (isAdvancedMachineCasing(aX - 2, aY, aZ + 6)) && (isAdvancedMachineCasing(aX - 3, aY, aZ - 6)) && (isAdvancedMachineCasing(aX - 3, aY, aZ + 6))
                && (isAdvancedMachineCasing(aX - 7, aY, aZ)) && (isAdvancedMachineCasing(aX + 7, aY, aZ)) && (isAdvancedMachineCasing(aX, aY, aZ - 7)) && (isAdvancedMachineCasing(aX, aY, aZ + 7))
                && (isAdvancedMachineCasing(aX - 6, aY, aZ - 3)) && (isAdvancedMachineCasing(aX - 6, aY, aZ - 2)) && (isAdvancedMachineCasing(aX - 6, aY, aZ + 2))
                && (isAdvancedMachineCasing(aX - 6, aY, aZ + 3)) && (isAdvancedMachineCasing(aX - 4, aY, aZ - 2)) && (isAdvancedMachineCasing(aX - 4, aY, aZ + 2))
                && (isAdvancedMachineCasing(aX + 4, aY, aZ - 2)) && (isAdvancedMachineCasing(aX + 4, aY, aZ + 2)) && (isAdvancedMachineCasing(aX - 2, aY, aZ - 4))
                && (isAdvancedMachineCasing(aX - 2, aY, aZ + 4)) && (isAdvancedMachineCasing(aX + 2, aY, aZ - 4)) && (isAdvancedMachineCasing(aX + 2, aY, aZ + 4));
    }

    private boolean addIfEnergyInjector(int aX, int aY, int aZ, IGregTechTileEntity aBaseMetaTileEntity) {
        if (addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntity(aX, aY, aZ), 53)) {
            return true;
        }
        return isAdvancedMachineCasing(aX, aY, aZ);
    }

    private boolean addIfInjector(int aX, int aY, int aZ, IGregTechTileEntity aTileEntity) {
        if (addInputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY, aZ), 53)) {
            return true;
        }
        return isAdvancedMachineCasing(aX, aY, aZ);
    }

    private boolean addIfExtractor(int aX, int aY, int aZ, IGregTechTileEntity aTileEntity) {
        if (addOutputToMachineList(aTileEntity.getIGregTechTileEntity(aX, aY, aZ), 53)) {
            return true;
        }
        return isAdvancedMachineCasing(aX, aY, aZ);
    }

    private boolean isAdvancedMachineCasing(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCasing()) && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCasingMeta());
    }

    private boolean isCyclotronCoil(int aX, int aY, int aZ) {
        return (getBaseMetaTileEntity().getBlock(aX, aY, aZ) == getCyclotronCoil() && (getBaseMetaTileEntity().getMetaID(aX, aY, aZ) == getCyclotronCoilMeta()));
    }
    
    public Block getCasing() {
        return ModBlocks.blockCasings2Misc;
    }
    
    public int getCasingMeta() {
        return 10;
    }

    public Block getCyclotronCoil() {
        return ModBlocks.blockCasings2Misc;
    }

    public int getCyclotronCoilMeta() {
        return 9;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
        		"Super Magnetic Speed Shooter",
        		"------------------------------------------------------------",
        		"Particles are accelerated over 186 revolutions to 80% light speed",
        		"Can produce a continuous beam current of 2.2 mA at 590 MeV",
        		"Which will be extracted from the Isochronous cyclotron",
        		"------------------------------------------------------------",
        		"Consists of the same layout as a Fusion Reactor",
        		"Cyclotron Machine Casings around Cyclotron Coil Blocks", 
        		"2-16 Input Hatches", 
        		"1-16 Output Hatches", 
        		"1-16 Energy Hatches", 
        		"All Hatches must be ZPM or better"};
    }

    @Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] sTexture;
        if (aSide == aFacing) {
            sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1, Dyes._NULL.mRGBa)), new GT_RenderedTexture(getIconOverlay())};
        } else {
            if (!aActive) {
                sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
            } else {
                sTexture = new ITexture[]{new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_FUSION_GLASS_YELLOW, Dyes.getModulation(-1, Dyes._NULL.mRGBa))};
            }
        }
        return sTexture;
    }

    public IIconContainer getIconOverlay() {
        return Textures.BlockIcons.OVERLAY_FUSION3;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
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
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        String tier = tier() == 6 ? "I" : "II";
        float plasmaOut = 0;
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt;
            if (this.mLastRecipe.getFluidOutput(0) != null) {
                plasmaOut = (float)this.mLastRecipe.getFluidOutput(0).amount / (float)this.mLastRecipe.mDuration;
            }
        }

        return new String[]{
                "COMET - Compact Cyclotron MK "+tier,
                "EU Required: "+powerRequired+"EU/t",
                "Stored EU: "+mEUStore+" / "+maxEUStore(),
                "Plasma Output: "+plasmaOut+"L/t"};
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

	public int getAmountOfOutputs() {
		return 1;
	}
}