package gregtech.common.modularui2.sync;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class Predicates {

    public static boolean isNonEmptyList(SyncHandler sh) {
        return sh instanceof GenericListSyncHandler<?>list && list.getValue() != null
            && !list.getValue()
                .isEmpty();
    }
}
