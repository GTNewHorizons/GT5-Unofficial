package gregtech.api.objects;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SynchronisedHashSet<T> extends HashSet<T> {
    private final Set<T> internalSet;

    public SynchronisedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        internalSet = Collections.synchronizedSet(new HashSet<>());
    }

    public SynchronisedHashSet(int initialCapacity) {
        super(initialCapacity);
        internalSet = Collections.synchronizedSet(new HashSet<>());
    }

    public SynchronisedHashSet() {
        super();
        internalSet = Collections.synchronizedSet(new HashSet<>());
    }

    public SynchronisedHashSet(Collection<? extends T> c) {
        super(c);
        internalSet = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    public synchronized boolean add(T v) {
        return internalSet.add(v);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        return internalSet.addAll(c);
    }

    @Override
    public synchronized int size() {
        return internalSet.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return internalSet.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return internalSet.contains(o);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return internalSet.iterator();
    }

    @Override
    public synchronized Object[] toArray() {
        return internalSet.toArray();
    }

    @Override
    public synchronized <T> T[] toArray(T[] a) {
        return internalSet.toArray(a);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return internalSet.remove(o);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return internalSet.containsAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return internalSet.retainAll(c);
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return internalSet.removeAll(c);
    }

    @Override
    public synchronized void clear() {
        internalSet.clear();
    }

    @Override
    public synchronized Spliterator<T> spliterator() {
        return internalSet.spliterator();
    }

    @Override
    public synchronized boolean removeIf(Predicate<? super T> filter) {
        return internalSet.removeIf(filter);
    }

    @Override
    public synchronized Stream<T> stream() {
        return internalSet.stream();
    }

    @Override
    public synchronized Stream<T> parallelStream() {
        return internalSet.parallelStream();
    }

    @Override
    public synchronized void forEach(Consumer<? super T> action) {
        internalSet.forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SynchronisedHashSet<?> that = (SynchronisedHashSet<?>) o;
        return Objects.equals(internalSet, that.internalSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), internalSet);
    }
}
