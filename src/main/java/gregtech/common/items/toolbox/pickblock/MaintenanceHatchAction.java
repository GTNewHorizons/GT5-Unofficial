package gregtech.common.items.toolbox.pickblock;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;

public class MaintenanceHatchAction extends ForceDeselectAction<MTEHatchMaintenance> {

    public MaintenanceHatchAction() {
        super(MTEHatchMaintenance.class, MaintenanceHatchAction::predicate);
    }

    private static boolean predicate(MTEHatchMaintenance hatch, ForgeDirection side) {
        if (hatch == null) {
            return false;
        }

        final IGregTechTileEntity baseMTE = hatch.getBaseMetaTileEntity();
        return !hatch.mAuto && baseMTE != null && side == baseMTE.getFrontFacing();
    }
}
