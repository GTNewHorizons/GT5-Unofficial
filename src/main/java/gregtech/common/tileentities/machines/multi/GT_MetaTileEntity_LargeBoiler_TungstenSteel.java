package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_LargeBoiler_TungstenSteel extends GT_MetaTileEntity_LargeBoiler {

    public GT_MetaTileEntity_LargeBoiler_TungstenSteel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeTungstenSteelBoilerPerSecond;
    }

    public GT_MetaTileEntity_LargeBoiler_TungstenSteel(String aName) {
        super(aName);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeTungstenSteelBoilerPerSecond;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeBoiler_TungstenSteel(this.mName);
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
    public Block getCasingBlock() {
        return GregTech_API.sBlockCasings4;
    }

    @Override
    public byte getCasingMeta() {
        return 0;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 48;
    }

    @Override
    public Block getPipeBlock() {
        return GregTech_API.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 15;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTech_API.sBlockCasings3;
    }

    @Override
    public byte getFireboxMeta() {
        return 15;
    }

    @Override
    public byte getFireboxTextureIndex() {
        return 47;
    }

    @Override
    public int getEUt() {
        return 16000;
    }

    @Override
    public int getEfficiencyIncrease() {
        return 4;
    }

    @Override
    int runtimeBoost(int mTime) {
        return mTime * 120 / 750;
    }

    @Override
    boolean isSuperheated() {
        return true;
    }
}
