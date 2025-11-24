package gregtech.common.gui.modularui.multiblock.godforge;

import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;

public class MTEPlasmaModuleGui extends MTEBaseModuleGui<MTEPlasmaModule> {

    public MTEPlasmaModuleGui(MTEPlasmaModule multiblock) {
        super(multiblock);
    }

    @Override
    public Panels getMainPanel() {
        return Panels.MAIN_PLASMA;
    }
}
