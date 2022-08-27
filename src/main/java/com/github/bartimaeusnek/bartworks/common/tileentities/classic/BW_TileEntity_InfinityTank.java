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

import com.github.bartimaeusnek.bartworks.API.ITileWithGUI;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class BW_TileEntity_InfinityTank extends TileEntity implements IFluidTank, IFluidHandler, ITileWithGUI {

    final ArrayList<FluidStack> INTERNALTANKS = new ArrayList<>();

    int selectedTank;

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return this.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return this.drain(from, resource != null ? resource.amount : 0, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return this.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        boolean ret = false;
        for (FluidStack stack : this.INTERNALTANKS) {
            ret = GT_Utility.areFluidsEqual(stack, new FluidStack(fluid, 0));
            if (ret) {
                this.selectedTank = this.INTERNALTANKS.indexOf(stack);
                break;
            }
        }
        return ret;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[0];
    }

    @Override
    public FluidStack getFluid() {
        if (this.INTERNALTANKS.get(this.selectedTank) == null || this.INTERNALTANKS.get(this.selectedTank).amount == 0)
            if (this.selectedTank > 0) this.selectedTank = this.INTERNALTANKS.size() - 1;
        return this.INTERNALTANKS.get(this.selectedTank);
    }

    @Override
    public int getFluidAmount() {
        return this.INTERNALTANKS.get(this.selectedTank) != null ? this.INTERNALTANKS.get(this.selectedTank).amount : 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_) {
        super.writeToNBT(p_145841_1_);

        NBTTagList lInternalTank = new NBTTagList();

        for (FluidStack internaltank : this.INTERNALTANKS) {
            if (internaltank != null) {
                NBTTagCompound entry = new NBTTagCompound();
                entry.setString("FluidName", internaltank.getFluid().getName());
                entry.setInteger("Ammount", internaltank.amount);
                entry.setTag("FluidTag", internaltank.tag);
                lInternalTank.appendTag(entry);
            }
        }
        p_145841_1_.setTag("InternalTank", lInternalTank);
    }

    @Override
    public int getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public FluidTankInfo getInfo() {
        return null;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (this.worldObj.isRemote || resource == null || resource.amount == 0) return 0;

        if (!doFill) return resource.amount;

        int id = 0;

        if (this.canDrain(null, resource.getFluid())) {
            for (FluidStack stack : this.INTERNALTANKS)
                if (GT_Utility.areFluidsEqual(stack, resource)) {
                    this.INTERNALTANKS.get(id = this.INTERNALTANKS.indexOf(stack)).amount += resource.amount;
                    this.selectedTank = id;
                }
        } else {
            this.INTERNALTANKS.add(resource);
            id = this.INTERNALTANKS.size() - 1;
            this.selectedTank = id;
        }
        return this.INTERNALTANKS.get(id).amount;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {

        FluidStack outputstack = this.INTERNALTANKS.get(this.selectedTank);
        if (this.worldObj.isRemote || maxDrain == 0 || this.getFluid() == null || outputstack == null) return null;

        int actualdrain = maxDrain;
        if (actualdrain > outputstack.amount) actualdrain = outputstack.amount;
        FluidStack ret = new FluidStack(outputstack.getFluid(), actualdrain);
        if (ret.amount == 0) ret = null;
        if (doDrain) {
            outputstack.amount -= actualdrain;
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(
                    outputstack, this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, this, actualdrain));
        }
        return ret;
    }

    @Override
    public int getGUIID() {
        return 4;
    }
}
