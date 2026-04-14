package gregtech.common.tileentities.machines.multi;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerSteel extends MTELargeBoilerBase {

    public static final int EUT_GENERATED = 1000;
    public static final int EFFICIENCY_INCREASE = 12;
    public static final boolean SUPERHEATED = false;

    public MTELargeBoilerSteel(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            Casings.SolidSteelMachineCasing,
            Casings.SteelPipeCasing,
            Casings.SteelFireboxCasing,
            EUT_GENERATED,
            EFFICIENCY_INCREASE,
            SUPERHEATED,
            GTMod.proxy.mPollutionLargeSteelBoilerPerSecond);
    }

    public MTELargeBoilerSteel(String aName) {
        super(
            aName,
            Casings.SolidSteelMachineCasing,
            Casings.SteelPipeCasing,
            Casings.SteelFireboxCasing,
            EUT_GENERATED,
            EFFICIENCY_INCREASE,
            SUPERHEATED,
            GTMod.proxy.mPollutionLargeSteelBoilerPerSecond);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerSteel(this.mName);
    }

    @Override
    public String getCasingMaterial() {
        return "Steel";
    }

    @Override
    public String getCasingBlockType() {
        return "Machine Casings";
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime;
    }

    @Override
    protected long getMaxInternalWater() {
        return 64000L;
    }

    @Override
    protected int getMaxCooldownTicks() {
        return 2000;
    }
}
