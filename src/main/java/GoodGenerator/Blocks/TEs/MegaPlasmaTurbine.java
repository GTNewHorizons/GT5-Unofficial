package GoodGenerator.Blocks.TEs;


import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Plasma;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MegaPlasmaTurbine extends GT_MetaTileEntity_LargeTurbine_Plasma implements TecTechEnabledMulti, IConstructable {

    public MegaPlasmaTurbine(String name){super(name);}

    public MegaPlasmaTurbine(int id, String name, String nameRegional){
        super(id,name,nameRegional);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {

    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_Energy> getVanillaEnergyHatches() {
        return this.mEnergyHatches;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyTunnel> getTecTechEnergyTunnels() {
        return null;
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyMulti> getTecTechEnergyMultis() {
        return null;
    }

    @Override
    public long[] getCurrentInfoData() {
        return new long[0];
    }

    @Override
    public String[] getInfoDataArray(GT_MetaTileEntity_MultiBlockBase multiBlockBase) {
        return new String[0];
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return false;
    }
}
