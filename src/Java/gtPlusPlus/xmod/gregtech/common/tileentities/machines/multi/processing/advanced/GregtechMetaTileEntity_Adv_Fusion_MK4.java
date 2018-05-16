package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_FusionComputer;

import gtPlusPlus.core.block.ModBlocks;

import net.minecraft.block.Block;

public class GregtechMetaTileEntity_Adv_Fusion_MK4 extends GT_MetaTileEntity_FusionComputer {

    public GregtechMetaTileEntity_Adv_Fusion_MK4(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 6);
    }

    public GregtechMetaTileEntity_Adv_Fusion_MK4(String aName) {
        super(aName);
    }

    @Override
    public int tier() {
        return 9;
    }

    @Override
    public long maxEUStore() {
        return 640010000L * (Math.min(16, this.mEnergyHatches.size())) / 8L;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_Adv_Fusion_MK4(mName);
    }

    @Override
    public Block getCasing() {
        return getFusionCoil();
    }

    @Override
    public int getCasingMeta() {
        return 12;
    }

    @Override
    public Block getFusionCoil() {
        return ModBlocks.blockCasings3Misc;
    }

    @Override
    public int getFusionCoilMeta() {
        return 13;
    }

    public String[] getDescription() {
        return new String[]{
        		"HARNESSING THE POWER OF A NEUTRON STAR", 
        		"Fusion Machine Casings MK III around Advanced Fusion Coil Blocks", 
        		"2-16 Input Hatches", 
        		"1-16 Output Hatches", 
        		"1-16 Energy Hatches", 
        		"All Hatches must be UV or better", 
        		"32768 EU/t and 80mio EU Cap per Energy Hatch"};
    }

    @Override
    public int tierOverclock() {
        return 6;
    }

    @Override
    public IIconContainer getIconOverlay() {
        return Textures.BlockIcons.OVERLAY_FUSION3;
    }
}
