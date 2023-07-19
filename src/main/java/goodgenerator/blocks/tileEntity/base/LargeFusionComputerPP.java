package goodgenerator.blocks.tileEntity.base;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;

import gregtech.api.logic.ProcessingLogic;

public abstract class LargeFusionComputerPP extends LargeFusionComputer {

    public LargeFusionComputerPP(String name) {
        super(name);
    }

    public LargeFusionComputerPP(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return super.createProcessingLogic().setOverclock(2, 2);
    }
}
