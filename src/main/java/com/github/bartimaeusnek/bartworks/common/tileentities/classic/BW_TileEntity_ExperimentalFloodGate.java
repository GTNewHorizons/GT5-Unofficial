/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.common.tileentities.classic;

import com.github.bartimaeusnek.bartworks.API.ITileAddsInformation;
import com.github.bartimaeusnek.bartworks.util.Coords;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.fluids.TileFluidHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BW_TileEntity_ExperimentalFloodGate extends TileFluidHandler implements ITileAddsInformation {

    recursiveBelowCheck check = new recursiveBelowCheck();
    private long ticks = 0;
    private long noOfIts = 0;
    private Coords paused;

    public BW_TileEntity_ExperimentalFloodGate() {

    }

    @Override
    public void updateEntity() {
        if (paused == null) {
            this.paused = new Coords(this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId);
        }
        ticks++;
        if (check.called != -1) {
            if (ticks % 20 == 0) {
                HashSet<Coords> toRem = new HashSet<>();
                for (Coords c : check.hashset) {
                    this.worldObj.setBlock(c.x, c.y, c.z, Blocks.water, 0, 4);
                    toRem.add(c);
                }
                check.hashset.removeAll(toRem);
            }
        } else {
            noOfIts = 0;
            setUpHashSet();
            this.paused = check.hashset.get(check.hashset.size() - 1);
        }
        if (ticks % 50 == 0)
            ticks = 0;
    }

    private synchronized void setUpHashSet() {
        check = new recursiveBelowCheck();
        Thread t = new Thread(check);
        t.run();
        while (t.isAlive()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        check.hashset.remove(new Coords(this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId));
    }

    @Override
    public String[] getInfoData() {
        return new String[]{"Experimental Machine to fill Holes with Fluids"};
    }

    private class recursiveBelowCheck implements Runnable {

        private final List<Coords> hashset = new ArrayList<Coords>();
        int called = -1;

        public int getCalled() {
            return this.called;
        }

        public void setCalled(int called) {
            this.called = called;
        }

        public synchronized List<Coords> getHashset() {
            return this.hashset;
        }

        public byte check_sourroundings(World w, int x, int y, int z, Block b) {
            byte ret = 0;
            int wID = w.provider.dimensionId;

            if (hashset.contains(new Coords(x, y, z, wID)))
                return ret;

            hashset.add(new Coords(x, y, z, wID));

            if (w.getBlock(x, y + 1, z).equals(b))
                ret = (byte) (ret | 0b000001);

            if (w.getBlock(x, y - 1, z).equals(b))
                ret = (byte) (ret | 0b000010);

            if (w.getBlock(x + 1, y, z).equals(b))
                ret = (byte) (ret | 0b000100);

            if (w.getBlock(x - 1, y, z).equals(b))
                ret = (byte) (ret | 0b001000);

            if (w.getBlock(x, y, z + 1).equals(b))
                ret = (byte) (ret | 0b010000);

            if (w.getBlock(x, y, z - 1).equals(b))
                ret = (byte) (ret | 0b100000);

            return ret;
        }

        public int get_connected(World w, int x, int y, int z, Block b, int iterations) {

            if (iterations >= 5000)
                return -1;
            int tail = 0;
            int ret = 0;
            iterations++;
            int wID = w.provider.dimensionId;
            byte sides = check_sourroundings(w, x, y, z, b);

            if (((sides | 0b111110) == 0b111111) && !hashset.contains(new Coords(x, y + 1, z, wID)) && y + 1 <= yCoord) {
                tail = get_connected(w, x, y + 1, z, b, iterations);
                if (tail == -1)
                    return tail;
                ret++;
                ret += tail;
            }

            if (((sides | 0b111101) == 0b111111) && !hashset.contains(new Coords(x, y - 1, z, wID))) {
                tail = get_connected(w, x, y - 1, z, b, iterations);
                if (tail == -1)
                    return tail;
                ret++;
                ret += tail;
            }

            if (((sides | 0b111011) == 0b111111) && !hashset.contains(new Coords(x + 1, y, z, wID))) {
                tail = get_connected(w, x + 1, y, z, b, iterations);
                if (tail == -1)
                    return tail;
                ret++;
                ret += tail;
            }

            if (((sides | 0b110111) == 0b111111) && !hashset.contains(new Coords(x - 1, y, z, wID))) {
                tail = get_connected(w, x - 1, y, z, b, iterations);
                if (tail == -1)
                    return tail;
                ret++;
                ret += tail;
            }

            if (((sides | 0b101111) == 0b111111) && !hashset.contains(new Coords(x, y, z + 1, wID))) {
                tail = get_connected(w, x, y, z + 1, b, iterations);
                if (tail == -1)
                    return tail;
                ret++;
                ret += tail;

            }

            if (((sides | 0b011111) == 0b111111) && !hashset.contains(new Coords(x, y, z - 1, wID))) {
                tail = get_connected(w, x, y, z - 1, b, iterations);
                if (tail == -1)
                    return tail;
                ret++;
                ret += tail;
            }

            return ret;
        }

        @Override
        public synchronized void run() {
            called = check.get_connected(worldObj, paused.x, paused.y, paused.z, Blocks.air, 0);
            notifyAll();
        }
    }
}
