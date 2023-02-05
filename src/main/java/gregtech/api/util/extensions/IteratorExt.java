package gregtech.api.util.extensions;

import java.util.Iterator;

import gregtech.api.objects.iterators.MergedIterator;

public class IteratorExt {

    @SafeVarargs
    public static <T> Iterator<T> merge(Iterator<T>... iterators) {
        return new MergedIterator<>(iterators);
    }
}
