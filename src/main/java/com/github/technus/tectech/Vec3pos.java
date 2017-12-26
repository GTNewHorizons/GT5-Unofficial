package com.github.technus.tectech;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by Tec on 05.04.2017.
 */
public class Vec3pos implements Comparable<Vec3pos> {
    public final int x, z;
    public final short y;

    public Vec3pos(int x, short y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3pos(IGregTechTileEntity te) {
        x = te.getXCoord();
        y = te.getYCoord();
        z = te.getZCoord();
    }

    @Override
    public int compareTo(Vec3pos o) {
        int tmp=y-o.y;
        if (tmp!=0) return tmp;
        tmp=x-o.x;
        if (tmp!=0) return tmp;
        return z-o.z;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Vec3pos){
            Vec3pos v=(Vec3pos) obj;
            return x==v.x && z==v.z && y==v.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x*524288+z*256+y;
    }
}
