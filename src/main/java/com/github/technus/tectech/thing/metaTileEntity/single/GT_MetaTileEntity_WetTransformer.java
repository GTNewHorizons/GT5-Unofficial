package com.github.technus.tectech.thing.metaTileEntity.single;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Util;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;

import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.*;

public class GT_MetaTileEntity_WetTransformer extends GT_MetaTileEntity_Transformer {
    public GT_MetaTileEntity_WetTransformer(int aID, String aName, String aNameRegional, int aTier, String aDescription) {
        super(aID,aName,aNameRegional,aTier,aDescription);
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_WetTransformer(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName,aTier,aDescription,aTextures);
        Util.setTier(aTier,this);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_WetTransformer(mName, mTier, mDescription, mTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        for (byte b = -1; b < 16; b++) {
            rTextures[0][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_OUT_MULTI_TT[mTier]};
            rTextures[1][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_OUT_MULTI_TT[mTier]};
            rTextures[2][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_OUT_MULTI_TT[mTier]};
            rTextures[3][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1]};
            rTextures[4][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1]};
            rTextures[5][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1]};
            rTextures[6][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_IN_MULTI_TT[mTier]};
            rTextures[7][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_IN_MULTI_TT[mTier]};
            rTextures[8][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_IN_MULTI_TT[mTier]};
            rTextures[9][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1]};
            rTextures[10][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1]};
            rTextures[11][b + 1] = new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][b + 1], OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1]};
        }
        return rTextures;
    }

    @Override
    public String[] getDescription() {
        return new String[]{mDescription, "Accepts 16A and outputs 64A", CommonValues.TEC_MARK_GENERAL};
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier + 1];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier + 1] * 128L;
    }


    @Override
    public long maxAmperesOut() {
        return getBaseMetaTileEntity().isAllowedToWork() ? 64 : 16;
    }

    @Override
    public long maxAmperesIn() {
        return getBaseMetaTileEntity().isAllowedToWork() ? 16 : 64;
    }
}
