/*
 * Copyright (c) 2019 bartimaeusnek
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

import java.util.Objects;

public class Coords {

    public int x, z, wID;
    public short y;

    public Coords(int x, int y, int z, int wID) {
        this(x, y, z);
        this.wID = wID;
    }

    public Coords(int x, int y, int z) {
        this.x = x;
        this.y = (short) y;
        this.z = z;
        this.wID = 0;
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//        if (o == null || this.getClass() != o.getClass())
//            return false;
        Coords coords = (Coords) o;
        return this.x == coords.x &&
                this.y == coords.y &&
                this.z == coords.z &&
                this.wID == coords.wID;
    }

    @Override
    public int hashCode() {
        byte[] data = new byte[14];
        data[0]= (byte) (this.x & 0b1111);
        data[1]= (byte) (this.x >> 4 & 0b1111);
        data[2]= (byte) (this.x >> 8 & 0b1111);
        data[3]= (byte) (this.x >> 12 & 0b1111);
        data[4]= (byte) (this.y & 0b1111);
        data[5]= (byte) (this.y >> 4 & 0b1111);
        data[6]= (byte) (this.z & 0b1111);
        data[7]= (byte) (this.z >> 4 & 0b1111);
        data[8]= (byte) (this.z >> 8 & 0b1111);
        data[9]= (byte) (this.z >> 12 & 0b1111);
        data[10]= (byte) (this.wID & 0b1111);
        data[11]= (byte) (this.wID >> 4 & 0b1111);
        data[12]= (byte) (this.wID >> 8 & 0b1111);
        data[13]= (byte) (this.wID >> 12 & 0b1111);
        return MurmurHash3.murmurhash3_x86_32(data,0,14,31);
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z + "," + wID;
    }
}
