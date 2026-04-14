package gregtech.common.tileentities.machines.multi;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerBronze extends MTELargeBoilerBase {

    public static final int EUT_GENERATED = 400;
    public static final int EFFICIENCY_INCREASE = 16;
    public static final boolean SUPERHEATED = false;

    public MTELargeBoilerBronze(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            Casings.BronzePlatedBricks,
            Casings.BronzePipeCasing,
            Casings.BronzeFireboxCasing,
            EUT_GENERATED,
            EFFICIENCY_INCREASE,
            SUPERHEATED,
            GTMod.proxy.mPollutionLargeBronzeBoilerPerSecond);
    }

    public MTELargeBoilerBronze(String aName) {
        super(
            aName,
            Casings.BronzePlatedBricks,
            Casings.BronzePipeCasing,
            Casings.BronzeFireboxCasing,
            EUT_GENERATED,
            EFFICIENCY_INCREASE,
            SUPERHEATED,
            GTMod.proxy.mPollutionLargeBronzeBoilerPerSecond);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerBronze(this.mName);
    }

    @Override
    public String getCasingMaterial() {
        return "Bronze";
    }

    @Override
    public String getCasingBlockType() {
        return "Plated Bricks";
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime * 2;
    }
}
