package com.github.technus.tectech.thing.metaTileEntity.hatch;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GT_MetaTileEntity_Hatch_CreativeMaintenance extends GT_MetaTileEntity_Hatch_Maintenance {
    public GT_MetaTileEntity_Hatch_CreativeMaintenance(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_CreativeMaintenance(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    @Override
    public String[] getDescription() {
        String[] desc = new String[this.mDescriptionArray.length + 1];
        System.arraycopy(this.mDescriptionArray, 0, desc, 0, this.mDescriptionArray.length);
        desc[this.mDescriptionArray.length] = "Fixing is for plebs!";
        return desc;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_CreativeMaintenance(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        this.mWrench = this.mScrewdriver = this.mSoftHammer = this.mHardHammer = this.mCrowbar = this.mSolderingTool = true;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }
}
