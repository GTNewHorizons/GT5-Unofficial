package gregtech.api.util;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {

    private boolean hasValue = false;
    private T value;

    private Supplier<T> getter;

    public Lazy(Supplier<T> getter) {
        this.getter = getter;
    }

    @Override
    public synchronized T get() {
        if (!hasValue) {
            value = getter.get();
            getter = null;
            hasValue = true;
        }

        return value;
    }
}
