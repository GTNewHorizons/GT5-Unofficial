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
import java.util.LinkedList;
import java.util.Queue;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * This implementation is for some reason slower than the Recursive Check....
 * For ~3400 blocks this takes 8ms, the Recursive Check 5ms.
 */
public class ConnectedBlocksCheckerIteration {

    public final HashSet<Coords> hashset = new HashSet<>(2048);
    public final HashSet<Coords> checked = new HashSet<>(4096);
    private final Queue<Coords> kwoe = new LinkedList<>();

    public long get_connected(World w, int x, int y, int z, Block b) {
        kwoe.add(new Coords(x, y, z, w.provider.dimensionId));
        hashset.add(new Coords(x, y, z, w.provider.dimensionId));
        while (!kwoe.isEmpty()) {
            Coords tocheck = kwoe.poll();
            int wID = w.provider.dimensionId;
            checked.add(tocheck);
            Coords c;
            if (!checked.contains(c = new Coords(tocheck.x + 1, tocheck.y, tocheck.z, wID))
                    && w.getBlock(tocheck.x + 1, tocheck.y, tocheck.z).equals(b)) {
                kwoe.add(c);
                hashset.add(c);
            }
            if (!checked.contains(c = new Coords(tocheck.x - 1, tocheck.y, tocheck.z, wID))
                    && w.getBlock(tocheck.x - 1, tocheck.y, tocheck.z).equals(b)) {
                kwoe.add(c);
                hashset.add(c);
            }
            if (!checked.contains(c = new Coords(tocheck.x, tocheck.y, tocheck.z + 1, wID))
                    && w.getBlock(tocheck.x, tocheck.y, tocheck.z + 1).equals(b)) {
                kwoe.add(c);
                hashset.add(c);
            }
            if (!checked.contains(c = new Coords(tocheck.x, tocheck.y, tocheck.z - 1, wID))
                    && w.getBlock(tocheck.x, tocheck.y, tocheck.z - 1).equals(b)) {
                kwoe.add(c);
                hashset.add(c);
            }
            if (!checked.contains(c = new Coords(tocheck.x, tocheck.y + 1, tocheck.z, wID))
                    && w.getBlock(tocheck.x, tocheck.y + 1, tocheck.z).equals(b)) {
                kwoe.add(c);
                hashset.add(c);
            }
            if (!checked.contains(c = new Coords(tocheck.x, tocheck.y - 1, tocheck.z, wID))
                    && w.getBlock(tocheck.x, tocheck.y - 1, tocheck.z).equals(b)) {
                kwoe.add(c);
                hashset.add(c);
            }
        }
        return hashset.size();
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
