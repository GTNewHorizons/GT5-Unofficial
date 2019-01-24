package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class GT_MetaTileEntity_Hatch_CreativeMaintenance extends GT_MetaTileEntity_Hatch_Maintenance {
    public GT_MetaTileEntity_Hatch_CreativeMaintenance(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_Hatch_CreativeMaintenance(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.BASS_MARK,
                mDescription,
                "Does fix everything but itself.",
                EnumChatFormatting.AQUA + "Fixing is for plebs!"
        };
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
