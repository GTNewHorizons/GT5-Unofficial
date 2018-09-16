package com.github.bartimaeusnek.bartworks.util;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Objects;

public class ConnectedBlocksChecker {

    public final HashSet<Coords> hashset = new HashSet<Coords>();

    public int get_connected(World w, int x, int y, int z, Block b){
        int ret = 0;

        byte sides = check_sourroundings(w, x, y, z, b);

        if (((sides | 0b111110) == 0b111111) && !hashset.contains(new Coords(x,y+1,z))) {
            ret++;
            ret += get_connected(w, x, y + 1, z,b);
        }

        if (( (sides | 0b111101) == 0b111111) && !hashset.contains(new Coords(x,y-1,z))) {
            ret++;
            ret += get_connected(w, x, y - 1, z,b);
        }

        if (( (sides | 0b111011) == 0b111111) && !hashset.contains(new Coords(x+1,y,z))) {
            ret++;
            ret += get_connected(w, x+1, y, z,b);
        }

        if (( (sides | 0b110111) == 0b111111) && !hashset.contains(new Coords(x-1,y,z)))  {
            ret++;
            ret += get_connected(w, x-1, y, z,b);
        }

        if (( (sides | 0b101111) == 0b111111) && !hashset.contains(new Coords(x,y,z+1)))  {
            ret++;
            ret += get_connected(w, x, y, z+1,b);
        }

        if (( (sides | 0b011111) == 0b111111) && !hashset.contains(new Coords(x,y,z-1)))  {
            ret++;
            ret += get_connected(w, x, y, z-1,b);
        }

        return ret;
    }


    public byte check_sourroundings(World w, int x, int y, int z,Block b){

        byte ret = 0;

        if (hashset.contains(new Coords(x,y,z)))
            return ret;

        hashset.add(new Coords(x,y,z));

        if (w.getBlock(x,y+1,z).equals(b))
            ret = (byte) (ret | 0b000001);

        if (w.getBlock(x,y-1,z).equals(b))
            ret = (byte) (ret | 0b000010);

        if (w.getBlock(x+1,y,z).equals(b))
            ret = (byte) (ret | 0b000100);

        if (w.getBlock(x-1,y,z).equals(b))
            ret = (byte) (ret | 0b001000);

        if (w.getBlock(x,y,z+1).equals(b))
            ret = (byte) (ret | 0b010000);

        if (w.getBlock(x,y,z-1).equals(b))
            ret = (byte) (ret | 0b100000);

        return ret;
    }

    public boolean get_meta_of_sideblocks(World w, int n, int[] xyz, boolean GT){

        Coords Controller = new Coords(xyz[0],xyz[1],xyz[2]);

        for (Coords C : hashset){
            if (GT) {
                TileEntity t;
                t = w.getTileEntity(C.x, C.y + 1, C.z);
                if (t != null && !new Coords(C.x, C.y + 1, C.z).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n)
                            return true;
                }
                t = w.getTileEntity(C.x, C.y - 1, C.z);
                if (t != null && !new Coords(C.x, C.y - 1, C.z).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n)
                            return true;
                }
                t = w.getTileEntity(C.x + 1, C.y, C.z);
                if (t != null && !new Coords(C.x + 1, C.y, C.z).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n)
                            return true;
                }
                t = w.getTileEntity(C.x - 1, C.y, C.z);
                if (t != null && !new Coords(C.x - 1, C.y, C.z).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n)
                            return true;
                }
                t = w.getTileEntity(C.x, C.y, C.z + 1);
                if (t != null && !new Coords(C.x, C.y, C.z + 1).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n)
                            return true;
                }
                t = w.getTileEntity(C.x, C.y, C.z - 1);
                if (t != null && !new Coords(C.x, C.y, C.z - 1).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n)
                            return true;
                }
            }else {
                if (n == w.getBlockMetadata(C.x, C.y + 1, C.z) && !new Coords(C.x, C.y + 1, C.z).equals(Controller))
                    return true;
                if (n == w.getBlockMetadata(C.x, C.y - 1, C.z) && !new Coords(C.x, C.y - 1, C.z).equals(Controller))
                    return true;
                if (n == w.getBlockMetadata(C.x + 1, C.y, C.z) && !new Coords(C.x + 1, C.y, C.z).equals(Controller))
                    return true;
                if (n == w.getBlockMetadata(C.x - 1, C.y, C.z) && !new Coords(C.x - 1, C.y, C.z).equals(Controller))
                    return true;
                if (n == w.getBlockMetadata(C.x, C.y, C.z + 1) && !new Coords(C.x, C.y, C.z + 1).equals(Controller))
                    return true;
                if (n == w.getBlockMetadata(C.x, C.y, C.z - 1) && !new Coords(C.x, C.y, C.z - 1).equals(Controller))
                    return true;
            }
        }
        return false;
    }


    class Coords {

        public int x,y,z;

        public Coords(int x, int y, int z){
            this.x=x;
            this.y=y;
            this.z=z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coords coords = (Coords) o;
            return x == coords.x &&
                    y == coords.y &&
                    z == coords.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
}
