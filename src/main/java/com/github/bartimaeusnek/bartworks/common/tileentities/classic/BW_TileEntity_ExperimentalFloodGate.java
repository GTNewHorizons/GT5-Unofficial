/*
 * Copyright (c) 2018-2020 bartimaeusnek
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

import static net.minecraftforge.common.util.ForgeDirection.*;

import com.github.bartimaeusnek.bartworks.API.ITileAddsInformation;
import com.github.bartimaeusnek.bartworks.util.Coords;
import java.util.Comparator;
import java.util.PriorityQueue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.TileFluidHandler;

public class BW_TileEntity_ExperimentalFloodGate extends TileFluidHandler implements ITileAddsInformation {

    private static final ForgeDirection[] allowed_directions = new ForgeDirection[] {DOWN, WEST, EAST, SOUTH, NORTH};
    private PriorityQueue<Coords> breadthFirstQueue = new PriorityQueue<>(Comparator.comparingInt(x -> x.y));
    private boolean wasInited = false;

    public BW_TileEntity_ExperimentalFloodGate() {
        this.tank.setCapacity(64000);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.canBePlacedInWorld();
    }

    public void initEntity() {
        if (wasInited) return;
        breadthFirstQueue.add(new Coords(this.xCoord, this.yCoord, this.zCoord));
        wasInited = true;
    }

    @Override
    public void updateEntity() {
        initEntity();
        Coords current = breadthFirstQueue.poll();
        if (current == null) return;
        setFluidBlock(current);
        for (ForgeDirection allowed_direction : allowed_directions) {
            addBlockToQueue(current, allowed_direction);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setBoolean("init", wasInited);

        int[] x = new int[this.breadthFirstQueue.size()];
        int[] y = new int[this.breadthFirstQueue.size()];
        int[] z = new int[this.breadthFirstQueue.size()];
        Coords[] arr = this.breadthFirstQueue.toArray(new Coords[0]);

        for (int i = 0; i < this.breadthFirstQueue.size(); i++) {
            x[i] = arr[i].x;
            y[i] = arr[i].y;
            z[i] = arr[i].z;
        }

        tag.setIntArray("queueX", x);
        tag.setIntArray("queueY", y);
        tag.setIntArray("queueZ", z);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.wasInited = tag.getBoolean("init");
        int[] x = tag.getIntArray("queueX");
        int[] y = tag.getIntArray("queueY");
        int[] z = tag.getIntArray("queueZ");
        for (int i = 0; i < x.length; i++) {
            this.breadthFirstQueue.add(new Coords(x[i], y[i], z[i]));
        }
    }

    private void setFluidBlock(Coords current) {
        if (!checkForAir(current)) return;
        if (this.tank.drain(1000, false) == null || this.tank.drain(1000, false).amount != 1000) return;
        FluidStack stack = this.tank.drain(1000, true);
        worldObj.setBlock(current.x, current.y, current.z, stack.getFluid().getBlock(), 0, 2);
    }

    private void addBlockToQueue(Coords current, ForgeDirection allowed_direction) {
        Coords side = current.getCoordsFromSide(allowed_direction);
        if (checkForAir(side)) breadthFirstQueue.add(side);
    }

    private boolean checkForAir(Coords coords) {
        return this.worldObj.isAirBlock(coords.x, coords.y, coords.z);
    }

    @Override
    public String[] getInfoData() {
        return new String[] {"Experimental Machine to fill Holes with Fluids"};
    }
}
