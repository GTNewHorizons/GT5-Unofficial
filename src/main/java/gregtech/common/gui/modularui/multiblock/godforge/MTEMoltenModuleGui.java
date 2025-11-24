package gregtech.common.gui.modularui.multiblock.godforge;

import gregtech.common.gui.modularui.multiblock.godforge.data.Modules;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;

public class MTEMoltenModuleGui extends MTEBaseModuleGui<MTEMoltenModule> {

    public MTEMoltenModuleGui(MTEMoltenModule multiblock) {
        super(multiblock);
    }

    @Override
    public Modules<MTEMoltenModule> getModuleType() {
        return Modules.MOLTEN;
    }
}
