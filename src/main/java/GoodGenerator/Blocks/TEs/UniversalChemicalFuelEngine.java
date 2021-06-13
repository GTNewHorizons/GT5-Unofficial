package GoodGenerator.Blocks.TEs;

import com.github.bartimaeusnek.crossmod.tectech.TecTechEnabledMulti;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.transpose;

public class UniversalChemicalFuelEngine extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<UniversalChemicalFuelEngine> multiDefinition = null;

    public UniversalChemicalFuelEngine(String name){super(name);}

    public UniversalChemicalFuelEngine(int id, String name, String nameRegional){
        super(id,name,nameRegional);
    }

    @Override
    public IStructureDefinition<UniversalChemicalFuelEngine> getStructure_EM(){
        if (multiDefinition == null){
            multiDefinition = StructureDefinition
                    .<UniversalChemicalFuelEngine>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {},
                                    {},
                                    {},
                                    {},
                                    {},
                                    {},
                            })
                    )
                    .build();
        }
        return multiDefinition;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {

    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MultiNqGenerator(this.mName);
    }
}
