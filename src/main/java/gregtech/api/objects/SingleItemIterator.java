package gregtech.api.objects;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleItemIterator<T> implements Iterator<T> {

    private T value;
    private boolean gotItem = false;

    public SingleItemIterator(T value) {
        this.value = value;
    }

    public boolean hasNext() {
        return !this.gotItem;
    }

    public T next() {
        if (this.gotItem) {
            throw new NoSuchElementException();
        }
        this.gotItem = true;
        T temp = value;
        value = null;
        return temp;
    }

    public void remove() {
        if (!this.gotItem) {
            this.gotItem = true;
            value = null;
        } else {
            throw new NoSuchElementException();
        }
    }
}
