package gregtech.api.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class SelfCleaningIterator<T> implements Iterator<T> {

    private final Iterator<T> iterator;

    SelfCleaningIterator(Iterator<T> it) {
        this.iterator = it;
    }

    T nextObject = null;
    boolean nextObjectSet = false;

    private boolean setNextObject() {
        while (iterator.hasNext()) {
            final T object = iterator.next();
            if (object != null && isValid(object)) {
                nextObject = object;
                nextObjectSet = true;
                return true;
            } else {
                iterator.remove();
            }
        }
        return false;
    }

    protected abstract boolean isValid(T object);

    @Override
    public boolean hasNext() {
        return nextObjectSet || setNextObject();
    }

    @Override
    public T next() {
        if (!nextObjectSet && !setNextObject()) {
            throw new NoSuchElementException();
        }
        nextObjectSet = false;
        return nextObject;

    }
}
