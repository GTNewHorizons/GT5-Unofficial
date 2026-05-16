package gregtech.api.objects.iterators;

import java.util.Iterator;

public class MergedIterator<T> implements Iterator<T> {

    private final Iterator<T>[] inners;
    private int current;

    @SafeVarargs
    public MergedIterator(Iterator<T>... iterators) {
        inners = iterators;
        current = 0;
    }

    @Override
    public boolean hasNext() {
        while (current < inners.length && !inners[current].hasNext()) {
            current++;
        }

        return current < inners.length;
    }

    @Override
    public T next() {
        while (current < inners.length && !inners[current].hasNext()) {
            current++;
        }

        return inners[current].next();
    }
}
