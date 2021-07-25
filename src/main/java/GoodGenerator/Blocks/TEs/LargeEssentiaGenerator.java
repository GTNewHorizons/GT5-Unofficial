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
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectSourceHelper;

import java.util.ArrayList;
import java.util.List;

import static com.github.technus.tectech.mechanics.structure.StructureUtility.transpose;

public class LargeEssentiaGenerator extends GT_MetaTileEntity_MultiblockBase_EM implements TecTechEnabledMulti, IConstructable {

    private IStructureDefinition<LargeEssentiaGenerator> multiDefinition = null;
    protected List<Aspect> mAvailableAspects;
    protected final int MAX_RANGE = 20;
    protected final int ENERGY_PER_ESSENTIA = 512;

    public LargeEssentiaGenerator(String name){
        super(name);
    }

    public LargeEssentiaGenerator(int id, String name, String nameRegional){
        super(id,name,nameRegional);
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
    public void construct(ItemStack itemStack, boolean b) {

    }

    @Override
    public IStructureDefinition<LargeEssentiaGenerator> getStructure_EM() {
        if(multiDefinition == null) {
            multiDefinition = StructureDefinition
                    .<LargeEssentiaGenerator>builder()
                    .addShape(mName,
                            transpose(new String[][]{
                                    {},
                                    {},
                                    {},
                                    {},
                                    {},
                                    {},
                                    {}
                            })
                    )
                    .build();
        }
        return multiDefinition;
    }

    List<Aspect> getAvailableAspects() {
        return mAvailableAspects;
    }

    private long absorbFromEssentiaContainers() {
        long tEU = 0;

        long tEUtoGen = getBaseMetaTileEntity().getEUCapacity() - getBaseMetaTileEntity().getUniversalEnergyStored();
        List<Aspect> mAvailableEssentiaAspects = getAvailableAspects();

        for (int i = mAvailableEssentiaAspects.size() - 1; i >= 0 && tEUtoGen > 0; i--) {
            Aspect aspect = mAvailableEssentiaAspects.get(i);
            long tAspectEU = (ENERGY_PER_ESSENTIA * mEfficiency) / 100;
            if (tAspectEU <= tEUtoGen
                    && AspectSourceHelper.drainEssentia((TileEntity) getBaseMetaTileEntity(), aspect, ForgeDirection.UNKNOWN, MAX_RANGE)) {
                tEUtoGen -= tAspectEU;
                tEU += tAspectEU;
            }
        }
        return tEU;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new LargeEssentiaGenerator(this.mName);
    }
}
