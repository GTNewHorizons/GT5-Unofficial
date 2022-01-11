package goodgenerator.blocks.tileEntity;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.item.ItemStack;

public class ExtremeHeatExchanger extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    protected IStructureDefinition<ExtremeHeatExchanger> multiDefinition = null;

    public ExtremeHeatExchanger(String name) {
        super(name);
    }

    public ExtremeHeatExchanger(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ExtremeHeatExchanger(mName);
    }
}
