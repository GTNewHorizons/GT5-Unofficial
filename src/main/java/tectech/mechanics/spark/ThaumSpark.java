package tectech.mechanics.spark;

import java.io.Serializable;
import java.util.Objects;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

public class ThaumSpark implements Serializable {

    // This works regardless of if TC is loaded
    private static final long serialVersionUID = -7037856938316679566L;
    public int x, y, z, wID;
    public byte xR, yR, zR;

    public ThaumSpark(int x, int y, int z, byte xR, byte yR, byte zR, int wID) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.xR = xR;
        this.yR = yR;
        this.zR = zR;

        this.wID = wID;
    }

    public ThaumSpark(Vec3Impl origin, Vec3Impl target, int wID) {
        this.x = origin.get0();
        this.y = origin.get1();
        this.z = origin.get2();

        Vec3Impl offset = target.sub(origin);
        this.xR = (byte) offset.get0();
        this.yR = (byte) offset.get1();
        this.zR = (byte) offset.get2();

        this.wID = wID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThaumSpark that = (ThaumSpark) o;
        return x == that.x && y == that.y
            && z == that.z
            && wID == that.wID
            && xR == that.xR
            && yR == that.yR
            && zR == that.zR;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, wID, xR, yR, zR);
    }
}
