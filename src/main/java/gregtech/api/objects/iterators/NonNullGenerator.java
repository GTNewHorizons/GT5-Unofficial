package gregtech.api.objects.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class NonNullGenerator<T> implements Iterator<T> {

    private boolean hasValue = false, finished = false;
    private T value = null;
    private Generator<T> generator;

    public NonNullGenerator(Generator<T> generator) {
        this.generator = generator;
    }

    @Override
    public boolean hasNext() {
        if (!finished && !hasValue) {
            value = generator.next();

            if (value == null) {
                finished = true;
                generator = null;
            } else {
                hasValue = true;
            }
        }

        return hasValue;
    }

    @Override
    public T next() {
        if (!finished && !hasValue) {
            value = generator.next();

            if (value == null) {
                finished = true;
                generator = null;
            } else {
                hasValue = true;
            }
        }

        if (!hasValue) throw new NoSuchElementException();

        T temp = value;
        value = null;
        hasValue = false;

        return temp;
    }
}
