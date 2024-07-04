package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_MetaTileEntity_LargeBoiler_RhodiumPalladium extends GT_MetaTileEntity_LargeBoiler {

    public GT_MetaTileEntity_LargeBoiler_RhodiumPalladium(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeRhodiumPalladiumBoilerPerSecond;
    }

    public GT_MetaTileEntity_LargeBoiler_RhodiumPalladium(String aName) {
        super(aName);
        pollutionPerSecond = GT_Mod.gregtechproxy.mPollutionLargeRhodiumPalladiumBoilerPerSecond;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LargeBoiler_RhodiumPalladium(this.mName);
    }

    @Override
    public String getCasingMaterial() {
        return "RhodiumPalladium";
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
        return 64000;
    }

    @Override
    public int getEfficiencyIncrease() {
        return 2;
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
