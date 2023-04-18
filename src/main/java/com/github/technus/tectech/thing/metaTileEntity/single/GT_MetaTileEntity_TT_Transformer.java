package com.github.technus.tectech.thing.metaTileEntity.single;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_MULTI_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_IN_POWER_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_MULTI_TT;
import static com.github.technus.tectech.thing.metaTileEntity.Textures.OVERLAYS_ENERGY_OUT_POWER_TT;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;

public class GT_MetaTileEntity_TT_Transformer extends GT_MetaTileEntity_Transformer {

    public GT_MetaTileEntity_TT_Transformer(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "");
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_TT_Transformer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        TT_Utility.setTier(aTier, this);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TT_Transformer(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        for (byte b = -1; b < 16; b++) {
            rTextures[0][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_OUT_MULTI_TT[mTier] };
            rTextures[1][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_OUT_MULTI_TT[mTier] };
            rTextures[2][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_OUT_MULTI_TT[mTier] };
            rTextures[3][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1] };
            rTextures[4][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1] };
            rTextures[5][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1] };
            rTextures[6][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_IN_MULTI_TT[mTier] };
            rTextures[7][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_IN_MULTI_TT[mTier] };
            rTextures[8][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_IN_MULTI_TT[mTier] };
            rTextures[9][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1] };
            rTextures[10][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1] };
            rTextures[11][b + 1] = new ITexture[] { MACHINE_CASINGS_TT[mTier][b + 1],
                    OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1] };
        }
        return rTextures;
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                translateToLocal("gt.blockmachines.tt.transformer.tier." + (mTier > 9 ? "" : "0") + mTier + ".desc"),
                CommonValues.TEC_MARK_GENERAL };
    }
}
