package tectech.thing.metaTileEntity.single;

import static gregtech.api.enums.GTValues.V;
import static net.minecraft.util.StatCollector.translateToLocal;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.util.CommonValues;

@Deprecated
public class MTEWetTransformer extends MTETransformerTT {

    public MTEWetTransformer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEWetTransformer(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            translateToLocal("gt.blockmachines.wetransformer.tier." + (mTier > 9 ? "" : "0") + mTier + ".desc"),
            "Accepts 16A and outputs 64A", CommonValues.TEC_MARK_GENERAL };
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
