package gregtech.common.gui.modularui.multiblock.dronecentre.sync;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.GenericSyncValue;

import gregtech.common.tileentities.machines.multi.drone.production.StatsBundle;

public class ProductionStatsSyncHandler extends GenericSyncValue<StatsBundle> {

    private long tick = 0;

    public ProductionStatsSyncHandler(@NotNull Supplier<StatsBundle> getter) {
        super(getter, null, StatsBundle::readFromBuf, StatsBundle::writeToBuf, StatsBundle::areEqual, null);
    }

    @Override
    public boolean updateCacheFromSource(boolean isFirstSync) {
        if (isFirstSync || tick++ % 40 == 0) return super.updateCacheFromSource(isFirstSync);
        return false;
    }
}
