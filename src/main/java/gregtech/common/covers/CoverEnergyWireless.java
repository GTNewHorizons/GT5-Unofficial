package gregtech.common.covers;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.ticks_between_energy_addition;
import static java.lang.Long.min;

import java.util.UUID;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;

public class CoverEnergyWireless extends CoverLegacyData {

    private final long transferred_energy_per_operation;

    public CoverEnergyWireless(CoverContext context, int voltage) {
        super(context);
        this.transferred_energy_per_operation = 2 * voltage * ticks_between_energy_addition;
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public boolean allowsCopyPasteTool() {
        return false;
    }

    @Override
    public boolean allowsTickRateAddition() {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        if (coverData == 0 || aTimer % ticks_between_energy_addition == 0) {
            tryFetchingEnergy(coveredTile.get());
        }
        coverData = 1;
    }

    private static UUID getOwner(Object te) {
        if (te instanceof BaseMetaTileEntity igte) {
            return igte.getOwnerUuid();
        } else {
            return null;
        }
    }

    private void tryFetchingEnergy(ICoverable tileEntity) {
        if (tileEntity instanceof BaseMetaTileEntity bmte) {
            long currentEU = bmte.getStoredEUuncapped();
            long euToTransfer = min(transferred_energy_per_operation - currentEU, transferred_energy_per_operation);
            if (euToTransfer <= 0) return; // nothing to transfer
            if (!addEUToGlobalEnergyMap(getOwner(tileEntity), -euToTransfer)) return;
            bmte.increaseStoredEnergyUnits(euToTransfer, true);
        }
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 20;
    }
}
