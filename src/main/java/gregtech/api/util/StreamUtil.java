package gregtech.api.util;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class StreamUtil {

    /**
     * Backport of {@link Stream#ofNullable}.
     */
    public static <T> Stream<T> ofNullable(@Nullable T value) {
        return value == null ? Stream.empty() : Stream.of(value);
    }

    /**
     * Returns a sequential ordered {@code Stream} whose elements are the specified values,
     * if {@code condition} is true, otherwise returns an empty {@code Stream}.
     *
     * @param <T>    the type of stream elements
     * @param values the elements of the new stream
     * @return the new stream
     */
    public static <T> Stream<T> ofConditional(boolean condition, T[] values) {
        return condition ? Arrays.stream(values) : Stream.empty();
    }

    /**
     * Returns a sequential {@code Stream} containing a single element, which will be lazily evaluated from supplier.
     *
     * @param <T>      the type of stream elements
     * @param supplier the supplier for single stream element
     * @return the new stream
     */
    public static <T> Stream<T> ofSupplier(Supplier<T> supplier) {
        return Stream.generate(supplier)
            .limit(1);
    }
}
