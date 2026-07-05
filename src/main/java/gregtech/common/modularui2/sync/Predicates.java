package gregtech.common.modularui2.sync;

import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class Predicates {

    public static boolean isPositive(SyncHandler<?> sh) {
        return sh instanceof IntSyncValue intSyncValue && intSyncValue.getIntValue() > 0;
    }
}
