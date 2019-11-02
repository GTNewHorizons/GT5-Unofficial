package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_POWER_TT;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 16.12.2016.
 */
public class GT_MetaTileEntity_Hatch_EnergyMulti extends GT_MetaTileEntity_Hatch {
    public final int Amperes;

    public GT_MetaTileEntity_Hatch_EnergyMulti(int aID, String aName, String aNameRegional, int aTier, int aAmp) {
        super(aID, aName, aNameRegional, aTier, 0, translateToLocal("gt.blockmachines.hatch.energymulti.desc.0"));//Multiple Ampere Energy Injector for Multiblocks
        Amperes = aAmp;
        Util.setTier(aTier, this);
    }

    public GT_MetaTileEntity_Hatch_EnergyMulti(String aName, int aTier, int aAmp, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        Amperes = aAmp;
    }

    public GT_MetaTileEntity_Hatch_EnergyMulti(int aID, String aName, String aNameRegional, int aTier, int i, String description, int aAmp) {
        super(aID, aName, aNameRegional, aTier, 0, description);
        Amperes = aAmp;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_IN_POWER_TT[mTier]};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, OVERLAYS_ENERGY_IN_POWER_TT[mTier]};
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return 128L * Amperes;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 4L * Amperes;
    }

    @Override
    public long maxAmperesIn() {
        return Amperes + (Amperes >> 2);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_EnergyMulti(mName, mTier, Amperes, mDescription, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_GENERAL,
                mDescription,
                translateToLocal("gt.blockmachines.hatch.energymulti.desc.1") + ": " + EnumChatFormatting.AQUA + maxAmperesIn() + " A"//Amperes In
        };
    }
}