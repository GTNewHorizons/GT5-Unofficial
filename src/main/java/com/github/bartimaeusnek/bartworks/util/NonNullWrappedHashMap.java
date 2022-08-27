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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NonNullWrappedHashMap<K, V> extends HashMap<K, V> {

    private V defaultValue;

    public NonNullWrappedHashMap(int initialCapacity, float loadFactor, V defaultValue) {
        super(initialCapacity, loadFactor);
        this.defaultValue = Objects.requireNonNull(defaultValue);
    }

    public NonNullWrappedHashMap(int initialCapacity, V defaultValue) {
        super(initialCapacity);
        this.defaultValue = Objects.requireNonNull(defaultValue);
    }

    public NonNullWrappedHashMap(V defaultValue) {
        this.defaultValue = Objects.requireNonNull(defaultValue);
    }

    public NonNullWrappedHashMap(Map<? extends K, ? extends V> m, V defaultValue) {
        super(m);
        this.defaultValue = Objects.requireNonNull(defaultValue);
    }

    @Override
    public V get(Object key) {
        return getOrDefault(key, defaultValue);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return Objects.requireNonNull(
                Optional.ofNullable(super.getOrDefault(key, defaultValue)).orElse(this.defaultValue));
    }
}
