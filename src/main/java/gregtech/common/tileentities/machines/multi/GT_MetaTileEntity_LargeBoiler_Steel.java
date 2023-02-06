package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_LargeBoiler_Steel extends GT_MetaTileEntity_LargeBoiler {

    public GT_MetaTileEntity_LargeBoiler_Steel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeSteelBoilerPerSecond;
    }

    public GT_MetaTileEntity_LargeBoiler_Steel(String aName) {
        super(aName);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeSteelBoilerPerSecond;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeBoiler_Steel(this.mName);
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
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings2;
    }

    @Override
    public byte getCasingMeta() {
        return 0;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 16;
    }

    @Override
    public Block getPipeBlock() {
        return GregTech_API.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 13;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTech_API.sBlockCasings3;
    }

    @Override
    public byte getFireboxMeta() {
        return 14;
    }

    @Override
    public byte getFireboxTextureIndex() {
        return 46;
    }

    @Override
    public int getEUt() {
        return 1000;
    }

    @Override
    public int getEfficiencyIncrease() {
        return 12;
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime;
    }

    @Override
    boolean isSuperheated() {
        return false;
    }
}
