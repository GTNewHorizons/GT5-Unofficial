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

    public int x, y, z, wID;

    public Coords(int x, int y, int z, int wID) {
        this(x, y, z);
        this.wID = wID;
    }

    public Coords(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.wID = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Coords coords = (Coords) o;
        return this.x == coords.x &&
                this.y == coords.y &&
                this.z == coords.z &&
                this.wID == coords.wID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z, this.wID);
    }

    @Override
    public String toString() {
        return x + "," + y + "," + z + "," + wID;
    }
}
