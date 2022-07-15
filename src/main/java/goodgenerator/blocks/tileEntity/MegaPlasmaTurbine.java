package goodgenerator.blocks.tileEntity;

import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Plasma;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

public class MegaPlasmaTurbine extends GT_MetaTileEntity_LargeTurbine_Plasma
        implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<MegaPlasmaTurbine> multiDefinition = null;

    public MegaPlasmaTurbine(String name) {
        super(name);
    }

    public MegaPlasmaTurbine(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {}

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
        return new ArrayList<>();
    }

    @Override
    public List<GT_MetaTileEntity_Hatch_EnergyMulti> getTecTechEnergyMultis() {
        return new ArrayList<>();
    }
}
