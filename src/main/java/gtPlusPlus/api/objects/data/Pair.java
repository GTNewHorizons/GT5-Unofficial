package gtPlusPlus.api.objects.data;

import com.google.common.base.Objects;
import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    /**
     * SVUID
     */
    private static final long serialVersionUID = 1250550491092812443L;

    private final K key;
    private final V value;

    public Pair(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    public final K getKey() {
        return this.key;
    }

    public final V getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        Integer aCode = Objects.hashCode(getKey(), getValue());
        return aCode != null ? aCode : super.hashCode();
    }
}
