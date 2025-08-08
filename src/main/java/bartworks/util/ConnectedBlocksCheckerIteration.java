/*
 * Copyright (c) 2018-2019 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * This implementation is for some reason slower than the Recursive Check.... For ~3400 blocks this takes 8ms, the
 * Recursive Check 5ms.
 */
public class ConnectedBlocksCheckerIteration {

    public final HashSet<Coords> hashset = new HashSet<>(2048);
    public final HashSet<Coords> checked = new HashSet<>(4096);
    private final Queue<Coords> kwoe = new LinkedList<>();

    public long get_connected(World w, int x, int y, int z, Block b) {
        this.kwoe.add(new Coords(x, y, z, w.provider.dimensionId));
        this.hashset.add(new Coords(x, y, z, w.provider.dimensionId));
        while (!this.kwoe.isEmpty()) {
            Coords tocheck = this.kwoe.poll();
            int wID = w.provider.dimensionId;
            this.checked.add(tocheck);
            Coords c;
            if (!this.checked.contains(c = new Coords(tocheck.x + 1, tocheck.y, tocheck.z, wID))
                && w.getBlock(tocheck.x + 1, tocheck.y, tocheck.z)
                    .equals(b)) {
                this.kwoe.add(c);
                this.hashset.add(c);
            }
            if (!this.checked.contains(c = new Coords(tocheck.x - 1, tocheck.y, tocheck.z, wID))
                && w.getBlock(tocheck.x - 1, tocheck.y, tocheck.z)
                    .equals(b)) {
                this.kwoe.add(c);
                this.hashset.add(c);
            }
            if (!this.checked.contains(c = new Coords(tocheck.x, tocheck.y, tocheck.z + 1, wID))
                && w.getBlock(tocheck.x, tocheck.y, tocheck.z + 1)
                    .equals(b)) {
                this.kwoe.add(c);
                this.hashset.add(c);
            }
            if (!this.checked.contains(c = new Coords(tocheck.x, tocheck.y, tocheck.z - 1, wID))
                && w.getBlock(tocheck.x, tocheck.y, tocheck.z - 1)
                    .equals(b)) {
                this.kwoe.add(c);
                this.hashset.add(c);
            }
            if (!this.checked.contains(c = new Coords(tocheck.x, tocheck.y + 1, tocheck.z, wID))
                && w.getBlock(tocheck.x, tocheck.y + 1, tocheck.z)
                    .equals(b)) {
                this.kwoe.add(c);
                this.hashset.add(c);
            }
            if (!this.checked.contains(c = new Coords(tocheck.x, tocheck.y - 1, tocheck.z, wID))
                && w.getBlock(tocheck.x, tocheck.y - 1, tocheck.z)
                    .equals(b)) {
                this.kwoe.add(c);
                this.hashset.add(c);
            }
        }
        return this.hashset.size();
    }

    public boolean get_meta_of_sideblocks(World w, int n, int[] xyz, boolean GT) {

        int wID = w.provider.dimensionId;
        Coords Controller = new Coords(xyz[0], xyz[1], xyz[2], wID);

        for (Coords C : this.hashset) {
            if (GT) {
                if (!new Coords(C.x, C.y + 1, C.z, wID).equals(Controller)
                    && w.getTileEntity(C.x, C.y + 1, C.z) instanceof IGregTechTileEntity gtTE
                    && gtTE.getMetaTileID() == n) {
                    return true;
                }
                if (!new Coords(C.x, C.y - 1, C.z, wID).equals(Controller)
                    && w.getTileEntity(C.x, C.y - 1, C.z) instanceof IGregTechTileEntity gtTE
                    && gtTE.getMetaTileID() == n) {
                    return true;
                }
                if (!new Coords(C.x + 1, C.y, C.z, wID).equals(Controller)
                    && w.getTileEntity(C.x + 1, C.y, C.z) instanceof IGregTechTileEntity gtTE
                    && gtTE.getMetaTileID() == n) {
                    return true;
                }
                if (!new Coords(C.x - 1, C.y, C.z, wID).equals(Controller)
                    && w.getTileEntity(C.x - 1, C.y, C.z) instanceof IGregTechTileEntity gtTE
                    && gtTE.getMetaTileID() == n) {
                    return true;
                }
                if (!new Coords(C.x, C.y, C.z + 1, wID).equals(Controller)
                    && w.getTileEntity(C.x, C.y, C.z + 1) instanceof IGregTechTileEntity gtTE
                    && gtTE.getMetaTileID() == n) {
                    return true;
                }
                if (!new Coords(C.x, C.y, C.z - 1, wID).equals(Controller)
                    && w.getTileEntity(C.x, C.y, C.z - 1) instanceof IGregTechTileEntity gtTE
                    && gtTE.getMetaTileID() == n) {
                    return true;
                }
            } else {
                if (n == w.getBlockMetadata(C.x, C.y + 1, C.z) && !new Coords(C.x, C.y + 1, C.z, wID).equals(Controller)
                    || n == w.getBlockMetadata(C.x, C.y - 1, C.z)
                        && !new Coords(C.x, C.y - 1, C.z, wID).equals(Controller)) {
                    return true;
                }
                if (n == w.getBlockMetadata(C.x + 1, C.y, C.z)
                    && !new Coords(C.x + 1, C.y, C.z, wID).equals(Controller)) {
                    return true;
                }
                if (n == w.getBlockMetadata(C.x - 1, C.y, C.z)
                    && !new Coords(C.x - 1, C.y, C.z, wID).equals(Controller)) {
                    return true;
                }
                if (n == w.getBlockMetadata(C.x, C.y, C.z + 1)
                    && !new Coords(C.x, C.y, C.z + 1, wID).equals(Controller)) {
                    return true;
                }
                if (n == w.getBlockMetadata(C.x, C.y, C.z - 1)
                    && !new Coords(C.x, C.y, C.z - 1, wID).equals(Controller)) {
                    return true;
                }
            }
        }
        return false;
    }
}
