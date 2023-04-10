package gregtech.api.objects;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorUtils {

    /**
     * Returns a merge function, suitable for use in {@link Map#merge(Object, Object, BiFunction) Map.merge()} or
     * {@link Collectors#toMap(Function, Function, BinaryOperator) toMap()}, which always throws
     * {@code IllegalStateException}. This can be used to enforce the assumption that the elements being collected are
     * distinct.
     *
     * @param <T> the type of input arguments to the merge function
     * @return a merge function which always throw {@code IllegalStateException}
     */
    public static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

    public static <K, V, E extends Map.Entry<K, V>, M extends Map<K, V>> Collector<E, ?, M> entriesToMap(
        Supplier<M> mapSupplier) {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, CollectorUtils.throwingMerger(), mapSupplier);
    }
}
