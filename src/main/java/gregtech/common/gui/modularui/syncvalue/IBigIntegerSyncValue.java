package gregtech.common.gui.modularui.syncvalue;

import java.math.BigInteger;

import com.cleanroommc.modularui.api.value.sync.IValueSyncHandler;

/**
 * A helper interface for sync values which can be turned into an integer.
 *
 * @param <T> value type
 */
public interface IBigIntegerSyncValue<T> extends IValueSyncHandler<T>, IBigIntegerValue<T> {

    default void setBigIntegerValue(BigInteger val) {
        setBigIntegerValue(val, true, true);
    }

    default void setBigIntegerValue(BigInteger val, boolean setSource) {
        setBigIntegerValue(val, setSource, true);
    }

    void setBigIntegerValue(BigInteger value, boolean setSource, boolean sync);
}
