package gregtech.api.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.MetaTileEntity;

public class ValidMTEList<E extends Collection<T>, T extends MetaTileEntity> implements Iterable<T> {

    private final E collection;

    public ValidMTEList(E collection) {
        this.collection = collection;
    }

    private class ValidMTEIterator implements Iterator<T> {

        Iterator<T> iterator = collection.iterator();

        T nextObject = null;
        boolean nextObjectSet = false;

        private boolean setNextObject() {
            while (iterator.hasNext()) {
                final T object = iterator.next();
                if (object != null && object.isValid()) {
                    nextObject = object;
                    nextObjectSet = true;
                    return true;
                } else {
                    iterator.remove();
                }
            }
            return false;
        }

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

    @Override
    public @NotNull Iterator<T> iterator() {
        return new ValidMTEIterator();
    }
}
