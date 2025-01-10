package gregtech.common.tileentities.machines.multi;

import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTEAirFilter1 extends MTEAirFilterBase {

    public MTEAirFilter1(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        multiTier = 1;
    }

    public MTEAirFilter1(String aName) {
        super(aName);
        multiTier = 1;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAirFilter1(mName);
    }

    @Override
    public long getEUt() {
        return TierEU.RECIPE_LV;
    }

    @Override
    public float getBonusByTier() {
        return 1.0f;
    }

    @Override
    public int getCasingIndex() {
        return 57;
    }

    @Override
    public int getPipeMeta() {
        return 1;
    }

    @Override
    public int getCasingMeta() {
        return 0;
    }
}
