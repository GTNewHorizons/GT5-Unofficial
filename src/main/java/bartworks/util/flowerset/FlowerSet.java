/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.util.flowerset;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class FlowerSet<T> implements Set<T> {

    public FlowerSet(int petals, Function<FlowerNode<T>, Integer> comparerison) {
        this.petals = petals;
        this.comparerison = comparerison;
    }

    final int petals;
    final Function<FlowerNode<T>, Integer> comparerison;

    public static <U> FlowerSet<U> createBase64(Function<FlowerNode<U>, Integer> comparerison) {
        return new FlowerSet<>(64, comparerison);
    }

    public static <U> FlowerSet<U> createHexflower(Function<FlowerNode<U>, Integer> comparerison) {
        return new FlowerSet<>(16, comparerison);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {}

    static class FlowerNode<V> {

        private final FlowerSet<V> map;
        final V value;
        final FlowerNode<V>[] links;

        @SuppressWarnings("unchecked")
        public FlowerNode(V value, FlowerSet<V> map) {
            this.value = value;
            this.map = map;
            this.links = new FlowerNode[map.petals];
        }

        private static final int DEPTH = 20480;

        public void TryToSetSingleNode(FlowerNode<V> node, FlowerNode<V> toset, int place, int depth) {
            if (depth > DEPTH) throw new IllegalStateException("Recursive Call went too deep.");
            if (node.links[place] == null) node.links[place] = toset;
            else {
                this.TryToSetSingleNode(node.links[place], toset, place, depth);
                depth++;
            }
        }

        public void TryToSetSingleNode(FlowerNode<V> node, FlowerNode<V> toset, int place) {
            if (node.links[place] == null) node.links[place] = toset;
            else this.TryToSetSingleNode(node.links[place], toset, place, 0);
        }

        @SafeVarargs
        public final void SetUpLinks(FlowerNode<V>... links) {
            for (FlowerNode<V> node : links) {
                int place = this.map.comparerison.apply(node);
                this.TryToSetSingleNode(this, node, place);
            }
        }
    }

    static class Functions {

        public static <V> Function<FlowerNode<V>, Integer> HashBasedFunction() {
            return function -> function.hashCode() % function.map.petals;
        }
    }
}
