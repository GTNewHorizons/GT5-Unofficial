package gtPlusPlus.xmod.gregtech.api.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * A manager for managing synchronized variables. Handles large data type splitting and merging for you <b>correctly</b>.
 *
 * @author glee8e
 */
class SyncedValueManager {
    private int offset;
    private final List<SyncedLong> longs = new ArrayList<>();

    SyncedValueManager(int offset) {
        this.offset = offset;
    }

    public SyncedLong allocateLong() {
        SyncedLong ret = new SyncedLong(offset);
        offset += 4;
        longs.add(ret);
        return ret;
    }

    public void detectAndSendChanges(SendChanges func, int timer) {
        for (SyncedLong val : longs) {
            val.detectAndSendChanges(func, timer);
        }
    }


    public void updateProgressBar(int short1, int short2) {
        for (SyncedLong val : longs) {
            if (val.updateProgressBar(short1, short2)) return;
        }
    }

    @FunctionalInterface
    public interface SendChanges {
        void sendProgressBarUpdate(int short1, int short2);
    }
}
