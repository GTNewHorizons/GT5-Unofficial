package com.github.technus.tectech;

import cofh.lib.util.position.BlockPosition;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.world.ChunkPosition;

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
        if (y > o.y) return 1;
        if (y < o.y) return -1;
        if (x > o.x) return 1;
        if (x < o.x) return -1;
        if (z > o.z) return 1;
        if (z < o.z) return -1;
        return 0;
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
