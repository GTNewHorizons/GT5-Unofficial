package gregtech.common.tileentities.machines.multi;

import net.minecraft.block.Block;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class MTELargeBoilerTungstenSteel extends MTELargeBoiler {

    public MTELargeBoilerTungstenSteel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeTungstenSteelBoilerPerSecond;
    }

    public MTELargeBoilerTungstenSteel(String aName) {
        super(aName);
        pollutionPerSecond = GTMod.proxy.mPollutionLargeTungstenSteelBoilerPerSecond;
    }

    public static final int EUT_GENERATED = 16000;

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeBoilerTungstenSteel(this.mName);
    }

    @Override
    public Block getCasingBlock() {
        return GregTechAPI.sBlockCasings4;
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
        return GregTechAPI.sBlockCasings2;
    }

    @Override
    public byte getPipeMeta() {
        return 15;
    }

    @Override
    public Block getFireboxBlock() {
        return GregTechAPI.sBlockCasings3;
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
        return MTELargeBoilerTungstenSteel.EUT_GENERATED;
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
