package gregtech.common.gui.modularui.multiblock.godforge;

import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;

public class MTESmeltingModuleGui extends MTEBaseModuleGui<MTESmeltingModule> {

    public MTESmeltingModuleGui(MTESmeltingModule multiblock) {
        super(multiblock);
    }

    @Override
    public Panels getMainPanel() {
        return Panels.MAIN_SMELTING;
    }
}
