package gtPlusPlus.api.objects.data;

import java.util.ArrayList;
import java.util.List;

public class Quad<K, V, C, R> {

    private final K key;
    private final V value;
    private final C value2;
    private final R value3;

    public Quad(final K key, final V value, final C value2, final R value3) {
        this.key = key;
        this.value = value;
        this.value2 = value2;
        this.value3 = value3;
    }

    public final K getKey() {
        return this.key;
    }

    public final V getValue_1() {
        return this.value;
    }

    public final C getValue_2() {
        return this.value2;
    }

    public final R getValue_3() {
        return this.value3;
    }

    public final List values() {
        List<Object> aVals = new ArrayList<>();
        aVals.add(key);
        aVals.add(value);
        aVals.add(value2);
        aVals.add(value3);
        return aVals;
    }
}
