/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import java.util.HashSet;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class ConnectedBlocksChecker {

    public final HashSet<Coords> hashset = new HashSet<>(2048);

    public static byte check_sourroundings(Coords C, Block b) {
        byte ret = 0;
        World w = DimensionManager.getWorld(C.wID);
        int x = C.x, y = C.y, z = C.z;

        if (w.getBlock(x + 1, y, z).equals(b)) ret = (byte) (ret | 0b000100);

        if (w.getBlock(x - 1, y, z).equals(b)) ret = (byte) (ret | 0b001000);

        if (w.getBlock(x, y, z + 1).equals(b)) ret = (byte) (ret | 0b010000);

        if (w.getBlock(x, y, z - 1).equals(b)) ret = (byte) (ret | 0b100000);

        if (w.getBlock(x, y + 1, z).equals(b)) ret = (byte) (ret | 0b000001);

        if (w.getBlock(x, y - 1, z).equals(b)) ret = (byte) (ret | 0b000010);

        return ret;
    }

    public int get_connected(World w, int x, int y, int z, Block b) {
        int ret = 0;

        int wID = w.provider.dimensionId;

        byte sides = check_sourroundings(w, x, y, z, b);

        if (((sides | 0b111011) == 0b111111) && !hashset.contains(new Coords(x + 1, y, z, wID))) {
            ret++;
            ret += get_connected(w, x + 1, y, z, b);
        }

        if (((sides | 0b110111) == 0b111111) && !hashset.contains(new Coords(x - 1, y, z, wID))) {
            ret++;
            ret += get_connected(w, x - 1, y, z, b);
        }

        if (((sides | 0b101111) == 0b111111) && !hashset.contains(new Coords(x, y, z + 1, wID))) {
            ret++;
            ret += get_connected(w, x, y, z + 1, b);
        }

        if (((sides | 0b011111) == 0b111111) && !hashset.contains(new Coords(x, y, z - 1, wID))) {
            ret++;
            ret += get_connected(w, x, y, z - 1, b);
        }

        if (((sides | 0b111110) == 0b111111) && !hashset.contains(new Coords(x, y + 1, z, wID))) {
            ret++;
            ret += get_connected(w, x, y + 1, z, b);
        }

        if (((sides | 0b111101) == 0b111111) && !hashset.contains(new Coords(x, y - 1, z, wID))) {
            ret++;
            ret += get_connected(w, x, y - 1, z, b);
        }

        return ret;
    }

    public byte check_sourroundings(World w, int x, int y, int z, Block b) {

        byte ret = 0;
        int wID = w.provider.dimensionId;

        if (hashset.contains(new Coords(x, y, z, wID))) return ret;

        hashset.add(new Coords(x, y, z, wID));

        if (w.getBlock(x + 1, y, z).equals(b)) ret = (byte) (ret | 0b000100);

        if (w.getBlock(x - 1, y, z).equals(b)) ret = (byte) (ret | 0b001000);

        if (w.getBlock(x, y, z + 1).equals(b)) ret = (byte) (ret | 0b010000);

        if (w.getBlock(x, y, z - 1).equals(b)) ret = (byte) (ret | 0b100000);

        if (w.getBlock(x, y + 1, z).equals(b)) ret = (byte) (ret | 0b000001);

        if (w.getBlock(x, y - 1, z).equals(b)) ret = (byte) (ret | 0b000010);

        return ret;
    }

    public boolean get_meta_of_sideblocks(World w, int n, int[] xyz, boolean GT) {

        int wID = w.provider.dimensionId;
        Coords Controller = new Coords(xyz[0], xyz[1], xyz[2], wID);

        for (Coords C : hashset) {
            if (GT) {
                TileEntity t;
                t = w.getTileEntity(C.x, C.y + 1, C.z);
                if (t != null && !new Coords(C.x, C.y + 1, C.z, wID).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n) return true;
                }
                t = w.getTileEntity(C.x, C.y - 1, C.z);
                if (t != null && !new Coords(C.x, C.y - 1, C.z, wID).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n) return true;
                }
                t = w.getTileEntity(C.x + 1, C.y, C.z);
                if (t != null && !new Coords(C.x + 1, C.y, C.z, wID).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n) return true;
                }
                t = w.getTileEntity(C.x - 1, C.y, C.z);
                if (t != null && !new Coords(C.x - 1, C.y, C.z, wID).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n) return true;
                }
                t = w.getTileEntity(C.x, C.y, C.z + 1);
                if (t != null && !new Coords(C.x, C.y, C.z + 1, wID).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n) return true;
                }
                t = w.getTileEntity(C.x, C.y, C.z - 1);
                if (t != null && !new Coords(C.x, C.y, C.z - 1, wID).equals(Controller)) {
                    if (t instanceof IGregTechTileEntity)
                        if (((IGregTechTileEntity) t).getMetaTileID() == n) return true;
                }
            } else {
                if (n == w.getBlockMetadata(C.x, C.y + 1, C.z)
                        && !new Coords(C.x, C.y + 1, C.z, wID).equals(Controller)) return true;
                if (n == w.getBlockMetadata(C.x, C.y - 1, C.z)
                        && !new Coords(C.x, C.y - 1, C.z, wID).equals(Controller)) return true;
                if (n == w.getBlockMetadata(C.x + 1, C.y, C.z)
                        && !new Coords(C.x + 1, C.y, C.z, wID).equals(Controller)) return true;
                if (n == w.getBlockMetadata(C.x - 1, C.y, C.z)
                        && !new Coords(C.x - 1, C.y, C.z, wID).equals(Controller)) return true;
                if (n == w.getBlockMetadata(C.x, C.y, C.z + 1)
                        && !new Coords(C.x, C.y, C.z + 1, wID).equals(Controller)) return true;
                if (n == w.getBlockMetadata(C.x, C.y, C.z - 1)
                        && !new Coords(C.x, C.y, C.z - 1, wID).equals(Controller)) return true;
            }
        }
        return false;
    }
}
