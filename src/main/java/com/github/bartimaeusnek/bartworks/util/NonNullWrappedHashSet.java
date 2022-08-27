/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util;

import java.util.Collection;
import java.util.HashSet;

public class NonNullWrappedHashSet<E> extends HashSet<E> {

    public NonNullWrappedHashSet() {
        super();
    }

    public NonNullWrappedHashSet(Collection<? extends E> c) {
        super();
        this.addAll(c);
    }

    public NonNullWrappedHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public NonNullWrappedHashSet(int initialCapacity) {
        super(initialCapacity);
    }

    public boolean add(E e) {
        if (e != null) return super.add(e);
        return false;
    }

    public boolean addAll(Collection<? extends E> c) {
        boolean wasChanged = false;
        for (E element : c) {
            if (element != null) wasChanged |= this.add(element);
        }
        return wasChanged;
    }
}
