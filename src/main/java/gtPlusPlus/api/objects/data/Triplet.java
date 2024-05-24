package gtPlusPlus.api.objects.data;

public class Triplet<K, V, C> {

    private final K key;
    private final V value;
    private final C count;

    public Triplet(final K key, final V value, final C value2) {
        this.key = key;
        this.value = value;
        this.count = value2;
    }

    public final K getValue_1() {
        return this.key;
    }

    public final V getValue_2() {
        return this.value;
    }

    public final C getValue_3() {
        return this.count;
    }
}
