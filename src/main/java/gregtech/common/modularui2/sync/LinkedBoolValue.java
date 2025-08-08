package gregtech.common.modularui2.sync;

import com.cleanroommc.modularui.api.value.IValue;
import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;

/**
 * Util class for getting a BoolValue for use with toggle buttons which are "linked together," meaning only one of them
 * can be in the pressed state at a time. Create sync value that takes the actual getter and setter, then use the
 * returned value for ToggleButton.
 */
public class LinkedBoolValue {

    private static <T> void setValueIfUnset(IValue<T> syncValue, T value) {
        if (syncValue.getValue() != value) {
            syncValue.setValue(value);
        }
    }

    public static <T extends Enum<T>> BoolValue.Dynamic of(EnumSyncValue<T> syncValue, T value) {
        return new BoolValue.Dynamic(() -> syncValue.getValue() == value, $ -> setValueIfUnset(syncValue, value));
    }

    public static BoolValue.Dynamic of(IntSyncValue syncValue, int value) {
        return new BoolValue.Dynamic(
            () -> syncValue.getValue() == value,
            enabled -> syncValue.setValue(value, true, syncValue.getValue() != value));
    }

    public static BoolValue.Dynamic of(BooleanSyncValue syncValue, boolean value) {
        return new BoolValue.Dynamic(
            () -> syncValue.getValue() == value,
            enabled -> syncValue.setValue(value, true, syncValue.getValue() != value));
    }
}
