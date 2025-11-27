package gregtech.common.gui.modularui.multiblock.godforge;

import gregtech.common.gui.modularui.multiblock.godforge.sync.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;

public class MTEMoltenModuleGui extends MTEBaseModuleGui<MTEMoltenModule> {

    public MTEMoltenModuleGui(MTEMoltenModule multiblock) {
        super(multiblock);
    }

    public MTEMoltenModuleGui(MTEMoltenModule multiblock, SyncHypervisor hypervisor) {
        super(multiblock, hypervisor);
    }

    @Override
    public Modules<MTEMoltenModule> getModuleType() {
        return Modules.MOLTEN;
    }
}
