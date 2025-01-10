package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEAirFilter2 extends MTEAirFilterBase {

    public MTEAirFilter2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        multiTier = 2;
    }

    public MTEAirFilter2(String aName) {
        super(aName);
        multiTier = 2;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAirFilter2(mName);
    }

    @Override
    public long getEUt() {
        return TierEU.RECIPE_HV;
    }

    @Override
    public float getBonusByTier() {
        return 1.05f;
    }

    @Override
    public int getCasingIndex() {
        return 59;
    }

    @Override
    public int getPipeMeta() {
        return 4;
    }

    @Override
    public int getCasingMeta() {
        return 3;
    }
}
