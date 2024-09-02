package tectech.thing.metaTileEntity.single;

import static net.minecraft.util.StatCollector.translateToLocal;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETransformer;
import tectech.thing.metaTileEntity.Textures;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

public class MTETransformerTT extends MTETransformer {

    public MTETransformerTT(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "");
        TTUtility.setTier(aTier, this);
    }

    public MTETransformerTT(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
        TTUtility.setTier(aTier, this);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETransformerTT(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[12][17][];
        for (byte b = -1; b < 16; b++) {
            rTextures[0][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_OUT_MULTI_TT[mTier] };
            rTextures[1][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_OUT_MULTI_TT[mTier] };
            rTextures[2][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_OUT_MULTI_TT[mTier] };
            rTextures[3][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1] };
            rTextures[4][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1] };
            rTextures[5][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_IN_POWER_TT[mTier + 1] };
            rTextures[6][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_IN_MULTI_TT[mTier] };
            rTextures[7][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_IN_MULTI_TT[mTier] };
            rTextures[8][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_IN_MULTI_TT[mTier] };
            rTextures[9][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1] };
            rTextures[10][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1] };
            rTextures[11][b + 1] = new ITexture[] { Textures.MACHINE_CASINGS_TT[mTier][b + 1],
                Textures.OVERLAYS_ENERGY_OUT_POWER_TT[mTier + 1] };
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
