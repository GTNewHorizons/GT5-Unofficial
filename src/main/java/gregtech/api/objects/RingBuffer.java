package gregtech.api.objects;

import java.util.*;

import org.jetbrains.annotations.NotNull;

public class RingBuffer implements List<Double>, RandomAccess {

    private int capacity;
    private int index;
    private double[] buffer;

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.index = 0;
        buffer = new double[capacity];
    }

    /**
     * Resizes the buffer to the specified new capacity.
     *
     * If the new capacity is smaller than the current one,
     * the most recent elements (up to newCapacity) will be retained in the resized buffer.
     *
     * If the new capacity is larger, the existing elements will be placed at the end of the resized buffer.
     *
     * Does nothing if the new capacity is less than one or equal to the current capacity.
     *
     * @param newCapacity the new capacity for the buffer.
     *                    Must be greater than zero.
     */
    public void resize(int newCapacity) {
        if (newCapacity < 1) return;
        if (capacity == newCapacity) return;
        double[] newBuf = new double[newCapacity];

        if (newCapacity < capacity) {
            // grab the newest #newCapacity elements and put them in the new buffer
            for (int i = 0; i < newCapacity; i++) {
                // always add a capacity, for the edge case of index < newCapacity, so our index doesn't underflow 0
                newBuf[i] = buffer[(capacity + index + i - newCapacity) % capacity];
            }
        } else {
            // place all elements aligned with the end of the new buffer
            System.arraycopy(buffer, 0, newBuf, newCapacity - capacity, capacity);
        }
        this.capacity = newCapacity;
        this.buffer = newBuf;
        // set the last element as the newest, implies that all zeros from idx 0..(start of old buf) are older
        // values
        this.index = 0;
    }

    /**
     * Always will have {@link #capacity} as it's size, padding with zeros otherwise
     */
    @Override
    public int size() {
        return capacity;
    }

    /**
     * Always false, as it's always a constant size, considering the filled zeros as valid
     *
     * @return always {@code false}
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (o instanceof Number number) {
            for (double v : buffer) {
                if (v == number.doubleValue()) return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull Iterator<Double> iterator() {
        return new BufferIterator(this);
    }

    @Override
    public Object[] toArray() {
        Object[] out = new Object[capacity];
        for (int i = 0; i < capacity; i++) {
            out[i] = buffer[(index + i) % capacity];
        }
        return out;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a == null) throw new NullPointerException();
        if (!Double.class.isAssignableFrom(
            a.getClass()
                .getComponentType()))
            throw new IllegalArgumentException("provided type of array can't be cast to from type double");

        Object[] out = new Object[capacity];
        for (int i = 0; i < capacity; i++) {
            out[i] = buffer[(index + i) % capacity];
        }
        return (T[]) out;
    }

    /**
     * Has the side effect of always moving index forwards, until it wraps back to 0, once capacity is reached.
     *
     * @param doub double inserted into the ring buffer, potentially overwriting the oldest value
     * @return always {@code true}
     */
    @Override
    public boolean add(Double doub) {
        buffer[index] = doub;
        // increment after insertion, so when we were at the last index,
        // we equal capacity, so the modulo will have wrapped us back to 0
        index = (index + 1) % capacity;
        return true;
    }

    /**
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the replaced value
     * @see List#set(int, Object)
     */
    @Override
    public Double set(int index, Double element) {
        if (index < 0 || index >= capacity) throw new IndexOutOfBoundsException();
        int realIdx = (index + this.index) % capacity;
        double old = buffer[realIdx];
        buffer[realIdx] = element;
        return old;
    }

    /**
     * Careful, this will resize the backing array, if you plan to call this operation in a loop,
     * use {@link #resize(int)} appropriately, in combination with {@link #toArray(Object[]) #toArray(double[])} and
     * {@link #set(int, Double)} or {@link #addAll(int, Collection)}!
     *
     * @param index index at which the specified element is to be inserted
     * @param doub  element to be inserted
     */
    @Override
    public void add(int index, Double doub) {
        int realIdx = (index + this.index) % capacity;

        double[] newBuf = new double[capacity + 1];
        System.arraycopy(buffer, 0, newBuf, 0, realIdx);
        newBuf[realIdx] = doub;
        System.arraycopy(buffer, realIdx, newBuf, realIdx + 1, capacity - realIdx);

        this.capacity += 1;
        if (realIdx < this.index) this.index++;
        buffer = newBuf;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) return -1;
        if (!(o instanceof Number number)) return -1;
        for (int i = 0; i < capacity; i++) {
            if (buffer[(index + i) % capacity] == number.doubleValue()) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) return -1;
        if (!(o instanceof Number number)) return -1;
        for (int i = capacity - 1; i >= 0; i--) {
            if (buffer[(capacity + index - i) % capacity] == number.doubleValue()) return i;
        }
        return -1;
    }

    @Override
    public @NotNull ListIterator<Double> listIterator() {
        return new BufferIterator(this);
    }

    @Override
    public @NotNull ListIterator<Double> listIterator(int index) {
        return new BufferIterator(this);
    }

    @Override
    public @NotNull List<Double> subList(int fromIndex, int toIndex) {
        // eh, I don't feel like implementing a view unless requested
        throw new UnsupportedOperationException();
    }

    /**
     * Unneeded (as data is constantly overwritten) & potentially breaking to throw UnsupportedOperation here
     * Will never remove anything.
     * If you really need to shrink the ring buffer, use {@link #resize(int)}
     *
     * @return always {@code false}
     */
    @Override
    public boolean remove(Object o) {
        return false;
    }

    /**
     * Unneeded (as data is constantly overwritten) & potentially breaking to throw UnsupportedOperation here
     * Will never remove anything.
     * If you really need to shrink the ring buffer, use {@link #resize(int)}
     *
     * @return always {@code 0.0d}
     */
    @Override
    public Double remove(int index) {
        return 0.0;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) return false;
        }
        return true;
    }

    /**
     * Has the side effect of always moving index forwards, until it wraps back to 0, once capacity is reached.
     *
     * @param c collection of doubles inserted one by one into the ring buffer, potentially overwriting the oldest
     *          values
     * @return always {@code true}
     */
    @Override
    public boolean addAll(@NotNull Collection<? extends Double> c) {
        for (double v : c) {
            this.add(v);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends Double> c) {
        int realIdx = (index + this.index) % capacity;

        double[] newBuf = new double[capacity + c.size()];
        System.arraycopy(buffer, 0, newBuf, 0, realIdx);

        int i = 0;
        for (Double v : c) {
            newBuf[realIdx + i] = v;
            i++;
        }

        System.arraycopy(buffer, realIdx, newBuf, realIdx + c.size(), capacity - realIdx);

        this.capacity += c.size();
        if (realIdx < this.index) this.index += c.size();
        buffer = newBuf;
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        buffer = new double[capacity];
        index = 0;
    }

    @Override
    public Double get(int index) {
        if (index < 0 || index >= capacity) throw new IndexOutOfBoundsException();
        return buffer[(index + this.index) % capacity];
    }

    private class BufferIterator implements ListIterator<Double> {

        private final RingBuffer ringRef;
        private final int idx;
        private int i = 0;

        private BufferIterator(RingBuffer ringRef) {
            this.ringRef = ringRef;
            this.idx = ringRef.index;
        }

        @Override
        public boolean hasNext() {
            // "invalidates" iterator, once the buffer index is moved, or end is reached for avoiding endless
            // iteration, as the ring buffers index could be modified, if we try to read from a different thread
            return idx == ringRef.index && (i == 0 || (idx + i) % capacity != idx);
        }

        @Override
        public Double next() {
            return buffer[(idx + i++) % capacity];
        }

        @Override
        public boolean hasPrevious() {
            return idx == ringRef.index && (i == capacity || (capacity + idx + i - 1) % capacity != idx);
        }

        @Override
        public Double previous() {
            return buffer[(capacity + idx + --i) % capacity];
        }

        @Override
        public int nextIndex() {
            return (idx + i) % capacity;
        }

        @Override
        public int previousIndex() {
            return (capacity + idx + i - 1) % capacity;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(Double aDouble) {
            buffer[(idx + i) % capacity] = aDouble;
        }

        @Override
        public void add(Double aDouble) {
            throw new UnsupportedOperationException();
        }
    }
}
