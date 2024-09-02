package gregtech.api.util.function;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Either<L, R> {

    private Either() {}

    public abstract <T> T map(@NotNull Function<? super L, ? extends T> l, @NotNull Function<? super R, ? extends T> r);

    public abstract @Nullable L left();

    public abstract @Nullable R right();

    public static @NotNull <L, R> Either<L, R> left(@NotNull L value) {
        return new Left<>(value);
    }

    public static @NotNull <L, R> Either<L, R> right(@NotNull R value) {
        return new Right<>(value);
    }

    private static final class Left<L, R> extends Either<L, R> {

        private final L value;

        private Left(@NotNull L value) {
            this.value = value;
        }

        @Override
        public <T> T map(@NotNull Function<? super L, ? extends T> l, @NotNull Function<? super R, ? extends T> r) {
            return l.apply(value);
        }

        @Override
        public @NotNull L left() {
            return value;
        }

        @Override
        public @Nullable R right() {
            return null;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Left<?, ?>left)) return false;

            return value.equals(left.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    private static final class Right<L, R> extends Either<L, R> {

        private final R value;

        private Right(@NotNull R value) {
            this.value = value;
        }

        @Override
        public <T> T map(@NotNull Function<? super L, ? extends T> l, @NotNull Function<? super R, ? extends T> r) {
            return r.apply(value);
        }

        @Override
        public @Nullable L left() {
            return null;
        }

        @Override
        public @NotNull R right() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Right<?, ?>right)) return false;

            return value.equals(right.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }
}
