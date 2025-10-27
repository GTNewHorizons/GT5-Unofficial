package gregtech.common.gui.modularui.synchandler;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

public class TeslaNodeListSyncHandler extends GenericListSyncHandler<TeslaNodeData> {

    public TeslaNodeListSyncHandler(@NotNull Supplier<List<TeslaNodeData>> getter) {
        super(getter, null, TeslaNodeData::deserialize, TeslaNodeData::serialize, TeslaNodeData::areEqual, null);
    }
}
