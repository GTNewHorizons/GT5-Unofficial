package gregtech.common.tileentities.machines.multi;

import gregtech.GTMod;
import gregtech.api.casing.Casings;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerTitanium extends MTELargeBoilerBase {

    public MTELargeBoilerTitanium(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            Casings.StableTitaniumMachineCasing,
            Casings.TitaniumPipeCasing,
            Casings.TitaniumFireboxCasing,
            4000,
            8,
            true,
            GTMod.proxy.mPollutionLargeTitaniumBoilerPerSecond);
    }

    public MTELargeBoilerTitanium(String aName) {
        super(
            aName,
            Casings.StableTitaniumMachineCasing,
            Casings.TitaniumPipeCasing,
            Casings.TitaniumFireboxCasing,
            4000,
            8,
            true,
            GTMod.proxy.mPollutionLargeTitaniumBoilerPerSecond);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerTitanium(this.mName);
    }

    @Override
    public String getCasingMaterial() {
        return "Titanium";
    }

    @Override
    public String getCasingBlockType() {
        return "Machine Casings";
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime * 130 / 400;
    }
}
