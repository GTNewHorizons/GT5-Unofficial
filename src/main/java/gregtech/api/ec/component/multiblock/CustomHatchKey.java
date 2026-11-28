package gregtech.api.ec.component.multiblock;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.IECMultiblock;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;

public class CustomHatchKey<T extends MTEHatch> {

    public final @NotNull String id;
    public final @NotNull Class<T> hatchType;
    private final @NotNull Predicate<T> matcher;
    public final boolean explodeOnMultiblockExplosion;
    private final @Nullable BiConsumer<IECMultiblock, T> onAttach;
    private final @Nullable BiConsumer<IECMultiblock, T> onDetach;

    public boolean matches(@Nullable IMetaTileEntity metaTile) {
        return tryCast(metaTile) != null;
    }

    public @Nullable T tryCast(@Nullable IMetaTileEntity metaTile) {
        if (!hatchType.isInstance(metaTile)) return null;
        T casted = hatchType.cast(metaTile);
        return matcher.test(casted) ? casted : null;
    }

    public void onAttach(@NotNull IECMultiblock multiblock, T hatch) {
        if (onAttach != null) {
            onAttach.accept(multiblock, hatch);
        }
    }

    public void onDetach(@NotNull IECMultiblock multiblock, T hatch) {
        if (onDetach != null) {
            onDetach.accept(multiblock, hatch);
        }
    }

    public static <H extends MTEHatch> Builder<H> builder(@NotNull String id, @NotNull Class<H> hatchType) {
        return new Builder<>(id, hatchType);
    }

    private CustomHatchKey(Builder<T> builder) {
        id = builder.id;
        hatchType = builder.hatchType;
        matcher = builder.matcher;
        explodeOnMultiblockExplosion = builder.explodeOnMultiblockExplosion;
        onAttach = builder.onAttach;
        onDetach = builder.onDetach;
    }

    public static final class Builder<T extends MTEHatch> {

        private final @NotNull String id;
        private final @NotNull Class<T> hatchType;
        private @NotNull Predicate<T> matcher = _ -> true;
        private boolean explodeOnMultiblockExplosion = false;
        private @Nullable BiConsumer<IECMultiblock, T> onAttach = null;
        private @Nullable BiConsumer<IECMultiblock, T> onDetach = null;

        private Builder(@NotNull String id, @NotNull Class<T> hatchType) {
            this.id = id;
            this.hatchType = hatchType;
        }

        public Builder<T> matcher(@NotNull Predicate<T> matcher) {
            this.matcher = matcher;
            return this;
        }

        public Builder<T> explodeOnMultiblockExplosion() {
            this.explodeOnMultiblockExplosion = true;
            return this;
        }

        public Builder<T> onAttach(@NotNull BiConsumer<IECMultiblock, T> callback) {
            this.onAttach = callback;
            return this;
        }

        public Builder<T> onDetach(@NotNull BiConsumer<IECMultiblock, T> callback) {
            this.onDetach = callback;
            return this;
        }

        public CustomHatchKey<T> build() {
            return new CustomHatchKey<>(this);
        }
    }
}
