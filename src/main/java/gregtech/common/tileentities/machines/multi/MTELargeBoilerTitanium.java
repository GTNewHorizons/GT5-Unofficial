package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerTitanium extends MTELargeBoiler {

    public MTELargeBoilerTitanium(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GTMod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond;
    }

    public MTELargeBoilerTitanium(String aName) {
        super(aName);
        pollutionPerSecond = GTMod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond;
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
    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 2;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 50;
    }

    @Override
    public Block getPipeBlock() {
        return GregTechAPI.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 14;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTechAPI.sBlockCasings4;
    }

    @Override
    public byte getFireboxMeta() {
        return 3;
    }

    @Override
    public byte getFireboxTextureIndex() {
        return 51;
    }

    @Override
    public int getEUt() {
        return 4000;
    }

    @Override
    public int getEfficiencyIncrease() {
        return 8;
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime * 130 / 400;
    }

    @Override
    boolean isSuperheated() {
        return true;
    }
}
