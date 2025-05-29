package gregtech.api.objects.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class LazySingletonIterator<T> implements Iterator<T> {

    private Supplier<T> getter;

    public LazySingletonIterator(Supplier<T> getter) {
        this.getter = getter;
    }

    @Override
    public boolean hasNext() {
        return getter != null;
    }

    @Override
    public T next() {
        if (getter == null) {
            throw new NoSuchElementException();
        } else {
            T value = getter.get();
            getter = null;
            return value;
        }
    }
}
