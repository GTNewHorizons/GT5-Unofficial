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

package com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration;

import com.github.bartimaeusnek.bartworks.util.MurmurHash3;
import java.nio.ByteBuffer;

public class CircuitData {

    private long aVoltage;
    private int aSpecial;
    private byte aTier;

    public CircuitData(long aVoltage, int aSpecial, byte aTier) {
        this.aVoltage = aVoltage;
        this.aSpecial = aSpecial;
        this.aTier = aTier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CircuitData)) return false;
        CircuitData that = (CircuitData) o;
        if (this.getaVoltage() != that.getaVoltage()) return false;
        if (this.getaSpecial() != that.getaSpecial()) return false;
        return this.getaTier() == that.getaTier();
    }

    @Override
    public int hashCode() {
        return MurmurHash3.murmurhash3_x86_32(
                ByteBuffer.allocate(13)
                        .put(this.aTier)
                        .putInt(this.aSpecial)
                        .putLong(this.aVoltage)
                        .array(),
                0,
                13,
                31);
    }

    public long getaVoltage() {
        return this.aVoltage;
    }

    public void setaVoltage(long aVoltage) {
        this.aVoltage = aVoltage;
    }

    public int getaSpecial() {
        return this.aSpecial;
    }

    public void setaSpecial(int aSpecial) {
        this.aSpecial = aSpecial;
    }

    public byte getaTier() {
        return this.aTier;
    }

    public void setaTier(byte aTier) {
        this.aTier = aTier;
    }
}
