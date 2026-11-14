package gregtech.common.modularui2.sync;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.NaniteTier;

public class NaniteTierSyncValue extends NullableEnumSyncValue<NaniteTier> {

    public NaniteTierSyncValue(@NotNull Supplier<NaniteTier> getter, @Nullable Consumer<NaniteTier> setter) {
        super(NaniteTier.class, getter, setter);
    }

    public NaniteTierSyncValue(@NotNull Supplier<NaniteTier> getter) {
        super(NaniteTier.class, getter);
    }
}
