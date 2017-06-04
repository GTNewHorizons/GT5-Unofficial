package com.github.technus.tectech;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by Tec on 05.04.2017.
 */
public class vec3pos implements Comparable<vec3pos> {
    public final int x, z;
    public final short y;

    public vec3pos(int x, short y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public vec3pos(IGregTechTileEntity te) {
        this.x = te.getXCoord();
        this.y = te.getYCoord();
        this.z = te.getZCoord();
    }

    @Override
    public int compareTo(vec3pos o) {
        int tmp=y-o.y;
        if (tmp!=0) return tmp;
        tmp=x-o.x;
        if (tmp!=0) return tmp;
        return z-o.z;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  vec3pos){
            vec3pos v=(vec3pos) obj;
            return x==v.x && z==v.z && y==v.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return x*524288+z*256+y;
    }
}
