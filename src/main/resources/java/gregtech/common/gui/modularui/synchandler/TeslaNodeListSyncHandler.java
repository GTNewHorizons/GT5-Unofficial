package gregtech.common.gui.modularui.synchandler;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

public class TeslaNodeListSyncHandler extends GenericListSyncHandler<TeslaNodeData> {

    public TeslaNodeListSyncHandler(@NotNull Supplier<List<TeslaNodeData>> getter,
        Consumer<List<TeslaNodeData>> setter) {
        super(getter, setter, TeslaNodeData::deserialize, TeslaNodeData::serialize, TeslaNodeData::areEqual, null);
    }
}
