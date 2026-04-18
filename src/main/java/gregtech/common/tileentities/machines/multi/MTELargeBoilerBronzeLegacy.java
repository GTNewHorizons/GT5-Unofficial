package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerBronzeLegacy extends MTELargeBoiler {

    public MTELargeBoilerBronzeLegacy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeBronzeBoilerPerSecond;
    }

    public MTELargeBoilerBronzeLegacy(String aName) {
        super(aName);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeBronzeBoilerPerSecond;
    }

    public static final int EUT_GENERATED = 400;

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerBronzeLegacy(this.mName);
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
        return MTELargeBoilerBronzeLegacy.EUT_GENERATED;
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
    public boolean isSuperheated() {
        return false;
    }

    @Override
    public float getOverdriveMult() {
        return 1.25F;
    }
}
