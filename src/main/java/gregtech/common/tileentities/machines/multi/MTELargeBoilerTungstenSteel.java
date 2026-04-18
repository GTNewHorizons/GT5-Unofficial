package gregtech.common.tileentities.machines.multi;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerTungstenSteel extends MTELargeBoilerBase {

    public static final int EUT_GENERATED = 16000;
    public static final int EFFICIENCY_INCREASE = 4;
    public static final boolean SUPERHEATED = true;

    public MTELargeBoilerTungstenSteel(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            Casings.RobustTungstenSteelMachineCasing,
            Casings.TungstensteelPipeCasing,
            Casings.TungstensteelFireboxCasing,
            EUT_GENERATED,
            EFFICIENCY_INCREASE,
            SUPERHEATED,
            GTMod.proxy.mPollutionLargeTungstenSteelBoilerPerSecond);
    }

    public MTELargeBoilerTungstenSteel(String aName) {
        super(
            aName,
            Casings.RobustTungstenSteelMachineCasing,
            Casings.TungstensteelPipeCasing,
            Casings.TungstensteelFireboxCasing,
            EUT_GENERATED,
            EFFICIENCY_INCREASE,
            SUPERHEATED,
            GTMod.proxy.mPollutionLargeTungstenSteelBoilerPerSecond);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerTungstenSteel(this.mName);
    }

    @Override
    public String getCasingMaterial() {
        return "TungstenSteel";
    }

    @Override
    public String getCasingBlockType() {
        return "Machine Casings";
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime * 120 / 750;
    }

    @Override
    public boolean isSuperheated() {
        return true;
    }
}
