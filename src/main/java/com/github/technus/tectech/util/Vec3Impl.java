package com.github.technus.tectech.util;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.dispenser.IPosition;
import net.minecraftforge.common.util.ForgeDirection;

public class Vec3Impl implements Comparable<Vec3Impl> {
    public static final Vec3Impl NULL_VECTOR = new Vec3Impl(0, 0, 0);
    private final int val0;
    private final int val1;
    private final int val2;

    public Vec3Impl(int in0, int in1, int in2) {
        this.val0 = in0;
        this.val1 = in1;
        this.val2 = in2;
    }

    public Vec3Impl(IGregTechTileEntity baseMetaTileEntity) {
        this(baseMetaTileEntity.getXCoord(),baseMetaTileEntity.getYCoord(),baseMetaTileEntity.getZCoord());
    }

    public int compareTo(Vec3Impl o) {
        return val1 == o.val1 ? val2 == o.val2 ? val0 - o.val0 : val2 - o.val2 : val1 - o.val1;
    }

    /**
     * Gets the coordinate.
     */
    public int get(int index) {
        switch (index){
            case 0: return val0;
            case 1: return val1;
            case 2: return val2;
            default: return 0;
        }
    }

    /**
     * Gets the X coordinate.
     */
    public int get0() {
        return this.val0;
    }

    /**
     * Gets the Y coordinate.
     */
    public int get1() {
        return this.val1;
    }

    /**
     * Gets the Z coordinate.
     */
    public int get2() {
        return this.val2;
    }

    public Vec3Impl offset(ForgeDirection facing, int n) {
        return n == 0 ? this : new Vec3Impl(val0 + facing.offsetX * n, val1 + facing.offsetY * n, val2 + facing.offsetZ * n);
    }

    public Vec3Impl add(IGregTechTileEntity tileEntity) {
        return new Vec3Impl(val0 + tileEntity.getXCoord(), val1 + tileEntity.getYCoord(), val2 + tileEntity.getZCoord());
    }

    public Vec3Impl sub(IGregTechTileEntity tileEntity) {
        return new Vec3Impl(val0 - tileEntity.getXCoord(), val1 - tileEntity.getYCoord(), val2 - tileEntity.getZCoord());
    }

    public Vec3Impl add(Vec3Impl pos) {
        return new Vec3Impl(val0 + pos.val0, val1 + pos.val1, val2 + pos.val2);
    }

    public Vec3Impl sub(Vec3Impl pos) {
        return new Vec3Impl(val0 - pos.val0, val1 - pos.val1, val2 - pos.val2);
    }

    public Vec3Impl add(int pos0,int pos1,int pos2) {
        return new Vec3Impl(val0 + pos0, val1 + pos1, val2 + pos2);
    }

    public Vec3Impl sub(int pos0,int pos1,int pos2) {
        return new Vec3Impl(val0 - pos0, val1 - pos1, val2 - pos2);
    }

    public Vec3Impl crossProduct(Vec3Impl vec) {
        return new Vec3Impl(val1 * vec.val2 - val2 * vec.val1, val2 * vec.val0 - val0 * vec.val2,
                val0 * vec.val1 - val1 * vec.val0);
    }

    public boolean withinDistance(Vec3Impl to, double distance) {
        return this.distanceSq(to.val0, to.val1, to.val2, false) < distance * distance;
    }

    public boolean withinDistance(IPosition to, double distance) {
        return this.distanceSq(to.getX(), to.getY(), to.getZ(), true) < distance * distance;
    }

    public double distanceSq(Vec3Impl to) {
        return this.distanceSq(to.val0, to.val1, to.val2, true);
    }

    public double distanceSq(IPosition to, boolean useCenter) {
        return this.distanceSq(to.getX(), to.getY(), to.getZ(), useCenter);
    }

    public double distanceSq(double x, double y, double z, boolean useCenter) {
        double d0 = useCenter ? 0.5D : 0.0D;
        double d1 = (double)val0 + d0 - x;
        double d2 = (double)val1 + d0 - y;
        double d3 = (double)val2 + d0 - z;
        return d1 * d1 + d2 * d2 + d3 * d3;
    }

    public int manhattanDistance(Vec3Impl to) {
        float f = (float)Math.abs(to.val0 - val0);
        float f1 = (float)Math.abs(to.val1 - val1);
        float f2 = (float)Math.abs(to.val2 - val2);
        return (int)(f + f1 + f2);
    }

    @Override
    public String toString() {
        return "Vec3[" + val0 + ", " + val1 + ", " + val2 + "]";
    }

    public Vec3Impl abs() {
        return new Vec3Impl(Math.abs(val0),Math.abs(val1),Math.abs(val2));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Vec3Impl) {
            Vec3Impl vec3i = (Vec3Impl)o;
            return val0 == vec3i.val0 && val1 == vec3i.val1 && val2 == vec3i.val2;
        }
        return false;
    }

    public int hashCode() {
        return (val1 + val2 * 31) * 31 + val0;
    }
}