package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerBronze extends MTELargeBoiler {

    public MTELargeBoilerBronze(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeBronzeBoilerPerSecond;
    }

    public MTELargeBoilerBronze(String aName) {
        super(aName);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeBronzeBoilerPerSecond;
    }

    public static final int EUT_GENERATED = 400;

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerBronze(this.mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings1;
    }

    @Override
    public byte getCasingMeta() {
        return 10;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 10;
    }

    @Override
    public Block getPipeBlock() {
        return GregTechAPI.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 12;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTechAPI.sBlockCasings3;
    }

    @Override
    public byte getFireboxMeta() {
        return 13;
    }

    @Override
    public byte getFireboxTextureIndex() {
        return 45;
    }

    @Override
    public int getEUt() {
        return MTELargeBoilerBronze.EUT_GENERATED;
    }

    @Override
    public int getEfficiencyIncrease() {
        return 16;
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime * 2;
    }

    @Override
    boolean isSuperheated() {
        return false;
    }
}
