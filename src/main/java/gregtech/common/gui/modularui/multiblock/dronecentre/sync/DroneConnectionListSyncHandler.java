package gregtech.common.gui.modularui.multiblock.dronecentre.sync;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

import gregtech.common.tileentities.machines.multi.drone.DroneConnection;

public class DroneConnectionListSyncHandler extends GenericListSyncHandler<DroneConnection> {

    private long tick = 0;

    public DroneConnectionListSyncHandler(@NotNull Supplier<List<DroneConnection>> getter) {
        super(getter, null, DroneConnection::deserialize, DroneConnection::serialize, DroneConnection::areEqual, null);
    }

    @Override
    public boolean updateCacheFromSource(boolean isFirstSync) {
        if (isFirstSync || tick++ % 5 == 0) return super.updateCacheFromSource(isFirstSync);
        return false;
    }
}
