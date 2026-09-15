package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEAirFilter3 extends MTEAirFilterBase {

    public MTEAirFilter3(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        multiTier = 3;
    }

    public MTEAirFilter3(String aName) {
        super(aName);
        multiTier = 3;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAirFilter3(mName);
    }

    @Override
    public long getEUt() {
        return TierEU.RECIPE_IV;
    }

    @Override
    public float getBonusByTier() {
        return 1.1f;
    }

    @Override
    public int getCasingIndex() {
        return 60;
    }

    @Override
    public int getPipeMeta() {
        return 6;
    }

    @Override
    public int getCasingMeta() {
        return 5;
    }
}
