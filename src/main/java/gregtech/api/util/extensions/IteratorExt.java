package gregtech.api.util.extensions;

import gregtech.api.objects.iterators.MergedIterator;

import java.util.Iterator;

public class IteratorExt {
    @SafeVarargs
    public static <T> Iterator<T> merge(Iterator<T>... iterators) {
        return new MergedIterator<>(iterators);
    }
}
