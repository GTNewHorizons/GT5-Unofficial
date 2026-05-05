package gregtech.common.modularui2.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.value.sync.IntSyncValue;

public class NullableEnumSyncValue<E extends Enum<E>> extends IntSyncValue {

    private final E[] values;

    public NullableEnumSyncValue(Class<E> clazz, @NotNull Supplier<E> getter, @Nullable Consumer<E> setter) {
        super(() -> {
            E value = getter.get();

            return value == null ? -1 : value.ordinal();
        }, setter == null ? null : (i -> setter.accept(i < 0 ? null : clazz.getEnumConstants()[i])));
        values = clazz.getEnumConstants();
    }

    public NullableEnumSyncValue(Class<E> clazz, @NotNull Supplier<E> getter) {
        super(() -> {
            E value = getter.get();

            return value == null ? -1 : value.ordinal();
        });
        values = clazz.getEnumConstants();
    }

    public E getEnumValue() {
        return getIntValue() < 0 ? null : values[getIntValue()];
    }
}
