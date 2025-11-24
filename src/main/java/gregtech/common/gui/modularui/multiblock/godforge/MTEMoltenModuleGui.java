package gregtech.common.gui.modularui.multiblock.godforge;

import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;

public class MTEMoltenModuleGui extends MTEBaseModuleGui<MTEMoltenModule> {

    public MTEMoltenModuleGui(MTEMoltenModule multiblock) {
        super(multiblock);
    }

    @Override
    public Panels getMainPanel() {
        return Panels.MAIN_MOLTEN;
    }
}
