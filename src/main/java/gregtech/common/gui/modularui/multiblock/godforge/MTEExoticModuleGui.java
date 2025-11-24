package gregtech.common.gui.modularui.multiblock.godforge;

import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;

public class MTEExoticModuleGui extends MTEBaseModuleGui<MTEExoticModule> {

    public MTEExoticModuleGui(MTEExoticModule multiblock) {
        super(multiblock);
    }

    @Override
    public Panels getMainPanel() {
        return Panels.MAIN_EXOTIC;
    }
}
