package gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.block.Block;

public class GT_MetaTileEntity_FusionComputer5 extends GT_MetaTileEntity_FusionComputer {

    public GT_MetaTileEntity_FusionComputer5(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 6);
    }

    public GT_MetaTileEntity_FusionComputer5(String aName) {
        super(aName);
    }

    @Override
    public int tier() {
        return 10;
    }

    @Override
    public long maxEUStore() {
        return 2560010000L * (Math.min(16, this.mEnergyHatches.size())) / 16L;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_FusionComputer5(mName);
    }

    @Override
    public int getCasingMeta() {
        return 10;
    }

    @Override
    public Block getFusionCoil() {
        return GregTech_API.sBlockCasings7;
    }

    @Override
    public int getFusionCoilMeta() {
        return 5;
    }

    public String[] getDescription() {
        return new String[]{
        		"A WHITE DWARF DOWN ON YOUR BASE", 
        		"Fusion Machine Casings MK IV around Superconductor Fusion Coil Blocks MK II", 
        		"2-16 Input Hatches", 
        		"1-16 Output Hatches", 
        		"1-16 Energy Hatches", 
        		"All Hatches must be UEV or better", 
        		"131072EU/t and 160mio EU Cap per Energy Hatch"};
    }

    @Override
    public int tierOverclock() {
        return 16;
    }

    @Override
    public Block getCasing() {
        return GregTech_API.sBlockCasings5;
    }

    @Override
    public IIconContainer getIconOverlay() {
        return Textures.BlockIcons.OVERLAY_FUSION3;
    }
}