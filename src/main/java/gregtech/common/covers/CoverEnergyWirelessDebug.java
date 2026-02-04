package gregtech.common.covers;

import static gregtech.api.enums.GTValues.V;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;

public class CoverEnergyWirelessDebug extends CoverLegacyData {

    private static final int VOLTAGE_TIER = 15; // MAX+
    private static final int AMPERAGE = 2;
    private static final int INTERVAL_TICKS = 20;

    public CoverEnergyWirelessDebug(CoverContext context) {
        super(context);
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
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        if (aTimer % INTERVAL_TICKS != 0L) return;

        ICoverable te = coveredTile.get();
        if (!(te instanceof BaseMetaTileEntity bmte)) return;

        long targetMinEU = (long) AMPERAGE * V[VOLTAGE_TIER];
        long currentEU = bmte.getStoredEUuncapped();
        if (currentEU >= targetMinEU) return;

        bmte.increaseStoredEnergyUnits(targetMinEU - currentEU, true);
    }

    @Override
    public int getMinimumTickRate() {
        return INTERVAL_TICKS;
    }

}
