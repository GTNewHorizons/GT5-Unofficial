/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Pair<A, B> implements Map.Entry<A, B> {
    Object[] pair = new Object[2];

    public Pair(Object[] pair) {
        this.pair = pair;
    }

    public Pair(A k, B v) {
        this.pair[0] = k;
        this.pair[1] = v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;

        Pair<?, ?> pair1 = (Pair<?, ?>) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(this.pair, pair1.pair);
    }

    @Override
    public int hashCode() {
        return MurmurHash3.murmurhash3_x86_32(
                ByteBuffer.allocate(8)
                        .putInt(pair[0].hashCode())
                        .putInt(pair[1].hashCode())
                        .array(),
                0,
                8,
                31);
    }

    @Override
    public A getKey() {
        return (A) pair[0];
    }

    @Override
    public B getValue() {
        return (B) pair[1];
    }

    @Override
    public B setValue(Object value) {
        pair[1] = value;
        return (B) pair[1];
    }

    public Pair<A, B> copyWithNewValue(B value) {
        return new Pair<>((A) this.pair[0], value);
    }

    public Pair<A, B> replaceValue(B value) {
        this.setValue(value);
        return this;
    }
}
