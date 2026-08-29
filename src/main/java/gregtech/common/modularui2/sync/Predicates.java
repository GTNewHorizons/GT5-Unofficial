package gregtech.common.modularui2.sync;

import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class Predicates {

    public static boolean isPositive(SyncHandler<?> sh) {
        return sh instanceof IntSyncValue intSyncValue && intSyncValue.getIntValue() > 0;
    }

    public static boolean arePositive(SyncHandler<?>... shs) {
        for (SyncHandler<?> sh : shs) {
            if (!isPositive(sh)) return false;
        }
        return true;
    }
}
