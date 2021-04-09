package gregtech.api.objects;

import lombok.EqualsAndHashCode;
import lombok.Synchronized;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = false)
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
    @Synchronized
    public boolean add(T v) {
        return internalSet.add(v);
    }

    @Override
    @Synchronized
    public boolean addAll(Collection<? extends T> c) {
        return internalSet.addAll(c);
    }

    @Override
    @Synchronized
    public int size() {
        return internalSet.size();
    }

    @Override
    @Synchronized
    public boolean isEmpty() {
        return internalSet.isEmpty();
    }

    @Override
    @Synchronized
    public boolean contains(Object o) {
        return internalSet.contains(o);
    }

    @Override
    @Synchronized
    public Iterator<T> iterator() {
        return internalSet.iterator();
    }

    @Override
    @Synchronized
    public Object[] toArray() {
        return internalSet.toArray();
    }

    @Override
    @Synchronized
    public <T> T[] toArray(T[] a) {
        return internalSet.toArray(a);
    }

    @Override
    @Synchronized
    public boolean remove(Object o) {
        return internalSet.remove(o);
    }

    @Override
    @Synchronized
    public boolean containsAll(Collection<?> c) {
        return internalSet.containsAll(c);
    }

    @Override
    @Synchronized
    public boolean retainAll(Collection<?> c) {
        return internalSet.retainAll(c);
    }

    @Override
    @Synchronized
    public boolean removeAll(Collection<?> c) {
        return internalSet.removeAll(c);
    }

    @Override
    @Synchronized
    public void clear() {
        internalSet.clear();
    }

    @Override
    @Synchronized
    public Spliterator<T> spliterator() {
        return internalSet.spliterator();
    }

    @Override
    @Synchronized
    public boolean removeIf(Predicate<? super T> filter) {
        return internalSet.removeIf(filter);
    }

    @Override
    @Synchronized
    public Stream<T> stream() {
        return internalSet.stream();
    }

    @Override
    @Synchronized
    public Stream<T> parallelStream() {
        return internalSet.parallelStream();
    }

    @Override
    @Synchronized
    public void forEach(Consumer<? super T> action) {
        internalSet.forEach(action);
    }
}
