package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_LargeBoiler_Titanium extends GT_MetaTileEntity_LargeBoiler {

    public GT_MetaTileEntity_LargeBoiler_Titanium(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond;
    }

    public GT_MetaTileEntity_LargeBoiler_Titanium(String aName) {
        super(aName);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeBoiler_Titanium(this.mName);
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
        return GregTech_API.sBlockCasings4;
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
        return GregTech_API.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 14;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTech_API.sBlockCasings4;
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
