package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_LargeBoiler_Bronze extends GT_MetaTileEntity_LargeBoiler {

    public GT_MetaTileEntity_LargeBoiler_Bronze(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeBronzeBoilerPerSecond;
    }

    public GT_MetaTileEntity_LargeBoiler_Bronze(String aName) {
        super(aName);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeBronzeBoilerPerSecond;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeBoiler_Bronze(this.mName);
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
        return GregTech_API.sBlockCasings1;
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
        return GregTech_API.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 12;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTech_API.sBlockCasings3;
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
        return 400;
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
