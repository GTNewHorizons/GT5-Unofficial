package com.github.technus.tectech.mechanics.data;

import java.io.Serializable;
import java.util.Objects;

public class ThaumSpark implements Serializable {
    //This works regardless of if TC is loaded
    private static final long serialVersionUID = -7037856938316679566L;
    public int x, y, z, wID;
    public byte xR, yR, zR;

    public ThaumSpark(){
        this.x = 0;
        this.z = 0;
        this.y = 0;

        this.xR = 0;
        this.yR = 0;
        this.zR = 0;

        this.wID = 0;
    }

    public ThaumSpark(int x, int y, int z, byte xR, byte yR, byte zR, int wID) {
        this.x = x;
        this.z = z;
        this.y = y;

        this.xR = xR;
        this.yR = yR;
        this.zR = zR;

        this.wID = wID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThaumSpark that = (ThaumSpark) o;
        return x == that.x &&
                y == that.y &&
                z == that.z &&
                wID == that.wID &&
                xR == that.xR &&
                yR == that.yR &&
                zR == that.zR;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, wID, xR, yR, zR);
    }
}
