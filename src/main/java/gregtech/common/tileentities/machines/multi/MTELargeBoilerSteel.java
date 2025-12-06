package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerSteel extends MTELargeBoiler {

    public MTELargeBoilerSteel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeSteelBoilerPerSecond;
    }

    public MTELargeBoilerSteel(String aName) {
        super(aName);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeSteelBoilerPerSecond;
    }

    public static final int EUT_GENERATED = 1000;

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerSteel(this.mName);
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
        return GregTechAPI.sBlockCasings2;
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
        return GregTechAPI.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 13;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTechAPI.sBlockCasings3;
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
        return MTELargeBoilerSteel.EUT_GENERATED;
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
    public boolean isSuperheated() {
        return false;
    }

    @Override
    public float getOverdriveMult() {
        return 1.75F;
    }
}
