package gregtech.common.gui.modularui.synchandler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

import gregtech.common.tileentities.machines.multi.drone.DroneConnection;

public class DroneConnectionListSyncHandler extends GenericListSyncHandler<DroneConnection> {

    public DroneConnectionListSyncHandler(@NotNull Supplier<List<DroneConnection>> getter,
        @NotNull Consumer<List<DroneConnection>> setter) {
        super(
            getter,
            setter,
            DroneConnection::deserialize,
            DroneConnection::serialize,
            DroneConnection::areEqual,
            null);
    }
}
