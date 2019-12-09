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

    BW_TileEntity_ExperimentalFloodGate.recursiveBelowCheck check = new BW_TileEntity_ExperimentalFloodGate.recursiveBelowCheck();
    private long ticks;
    private long noOfIts;
    private Coords paused;

    public BW_TileEntity_ExperimentalFloodGate() {

    }

    @Override
    public void updateEntity() {
        if (this.paused == null) {
            this.paused = new Coords(this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId);
        }
        this.ticks++;
        if (this.check.called != -1) {
            if (this.ticks % 20 == 0) {
                HashSet<Coords> toRem = new HashSet<>();
                for (Coords c : this.check.hashset) {
                    this.worldObj.setBlock(c.x, c.y, c.z, Blocks.water, 0, 4);
                    toRem.add(c);
                }
                this.check.hashset.removeAll(toRem);
            }
        } else {
            this.noOfIts = 0;
            this.setUpHashSet();
            this.paused = this.check.hashset.get(this.check.hashset.size() - 1);
        }
        if (this.ticks % 50 == 0)
            this.ticks = 0;
    }

    private synchronized void setUpHashSet() {
        this.check = new BW_TileEntity_ExperimentalFloodGate.recursiveBelowCheck();
        Thread t = new Thread(this.check);
        t.start();
        while (t.isAlive()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.check.hashset.remove(new Coords(this.xCoord, this.yCoord, this.zCoord, this.worldObj.provider.dimensionId));
    }

    @Override
    public String[] getInfoData() {
        return new String[]{"Experimental Machine to fill Holes with Fluids"};
    }

    private class recursiveBelowCheck implements Runnable {

        private final List<Coords> hashset = new ArrayList<>();
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

            if (this.hashset.contains(new Coords(x, y, z, wID)))
                return ret;

            this.hashset.add(new Coords(x, y, z, wID));

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
            int tail;
            int ret = 0;
            iterations++;
            int wID = w.provider.dimensionId;
            byte sides = this.check_sourroundings(w, x, y, z, b);

            if (((sides | 0b111110) == 0b111111) && !this.hashset.contains(new Coords(x, y + 1, z, wID)) && y + 1 <= BW_TileEntity_ExperimentalFloodGate.this.yCoord) {
                tail = this.get_connected(w, x, y + 1, z, b, iterations);
                if (tail == -1)
                    return -1;
                ret++;
                ret += tail;
            }

            if (((sides | 0b111101) == 0b111111) && !this.hashset.contains(new Coords(x, y - 1, z, wID))) {
                tail = this.get_connected(w, x, y - 1, z, b, iterations);
                if (tail == -1)
                    return -1;
                ret++;
                ret += tail;
            }

            if (((sides | 0b111011) == 0b111111) && !this.hashset.contains(new Coords(x + 1, y, z, wID))) {
                tail = this.get_connected(w, x + 1, y, z, b, iterations);
                if (tail == -1)
                    return -1;
                ret++;
                ret += tail;
            }

            if (((sides | 0b110111) == 0b111111) && !this.hashset.contains(new Coords(x - 1, y, z, wID))) {
                tail = this.get_connected(w, x - 1, y, z, b, iterations);
                if (tail == -1)
                    return -1;
                ret++;
                ret += tail;
            }

            if (((sides | 0b101111) == 0b111111) && !this.hashset.contains(new Coords(x, y, z + 1, wID))) {
                tail = this.get_connected(w, x, y, z + 1, b, iterations);
                if (tail == -1)
                    return -1;
                ret++;
                ret += tail;

            }

            if (((sides | 0b011111) == 0b111111) && !this.hashset.contains(new Coords(x, y, z - 1, wID))) {
                tail = this.get_connected(w, x, y, z - 1, b, iterations);
                if (tail == -1)
                    return -1;
                ret++;
                ret += tail;
            }

            return ret;
        }

        @Override
        public synchronized void run() {
            this.called = BW_TileEntity_ExperimentalFloodGate.this.check.get_connected(BW_TileEntity_ExperimentalFloodGate.this.worldObj, BW_TileEntity_ExperimentalFloodGate.this.paused.x, BW_TileEntity_ExperimentalFloodGate.this.paused.y, BW_TileEntity_ExperimentalFloodGate.this.paused.z, Blocks.air, 0);
            this.notifyAll();
        }
    }
}
