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
import com.github.bartimaeusnek.bartworks.API.ITileHasDifferentTextureSides;
import com.github.bartimaeusnek.bartworks.API.ITileWithGUI;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class BW_TileEntity_HeatedWaterPump extends TileEntity implements ISidedInventory, IFluidHandler, IFluidTank, ITileWithGUI, ITileAddsInformation, ITileHasDifferentTextureSides {

    public static final int FUELSLOT = 0;
    public static final Fluid WATER = FluidRegistry.WATER;
    public ItemStack fuelstack;
    public FluidStack outputstack = new FluidStack(FluidRegistry.WATER, 0);
    public int fuel = 0;
    public byte tick = 0;
    public int maxfuel = 0;
    public ItemStack fakestack = new ItemStack(Blocks.water);

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_) {
        NBTTagCompound subItemStack = new NBTTagCompound();
        if (fuelstack == null)
            p_145841_1_.setTag("ItemStack", subItemStack);
        else {
            fuelstack.writeToNBT(subItemStack);
            p_145841_1_.setTag("ItemStack", subItemStack);
        }
        NBTTagCompound subFluidStack = new NBTTagCompound();
        outputstack.writeToNBT(subFluidStack);
        p_145841_1_.setTag("FluidStack", subFluidStack);
        p_145841_1_.setInteger("fuel", fuel);
        p_145841_1_.setInteger("maxfuel", maxfuel);
        p_145841_1_.setByte("tick", tick);
        super.writeToNBT(p_145841_1_);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_) {
        tick = p_145839_1_.getByte("tick");
        fuel = p_145839_1_.getInteger("fuel");
        maxfuel = p_145839_1_.getInteger("maxfuel");
        outputstack = FluidStack.loadFluidStackFromNBT(p_145839_1_.getCompoundTag("FluidStack"));
        if (!p_145839_1_.getCompoundTag("ItemStack").equals(new NBTTagCompound()))
            fuelstack = ItemStack.loadItemStackFromNBT(p_145839_1_.getCompoundTag("ItemStack"));
        super.readFromNBT(p_145839_1_);
    }

    @Override
    public void updateEntity() {
        if (worldObj.isRemote || ((fuelstack == null || fuelstack.stackSize <= 0) && fuel <= 0) || (tick == 0 && worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == Blocks.air)) {
            return;
        }

        if (fuel < 0)
            fuel = 0;

        if (fuelstack != null && fuel == 0) {
            fuel = maxfuel = TileEntityFurnace.getItemBurnTime(fuelstack);
            --fuelstack.stackSize;
            if (this.fuelstack.stackSize <= 0)
                this.fuelstack = null;
        }

        if (fuel > 0) {
            ++tick;
            --fuel;
            if (tick % 20 == 0) {
                if (outputstack.amount <= (8000 - ConfigHandler.mbWaterperSec))
                    outputstack.amount += ConfigHandler.mbWaterperSec;
                tick = 0;
            }
        }
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return TileEntityFurnace.getItemBurnTime(p_102007_2_) > 0;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        if (p_70301_1_ == 0)
            return fuelstack;
        else
            return fakestack;
    }

    @Override
    public ItemStack decrStackSize(int slot, int ammount) {
        if (slot != FUELSLOT || fuelstack == null || ammount > fuelstack.stackSize)
            return null;

        return fuelstack.splitStack(ammount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack p_70299_2_) {
        if (slot == FUELSLOT)
            fuelstack = p_70299_2_;
        else
            fakestack = p_70299_2_;
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return TileEntityFurnace.getItemBurnTime(p_94041_2_) > 0 && p_94041_1_ == FUELSLOT;
    }

    @Override
    public FluidStack getFluid() {
        return outputstack.amount > 0 ? outputstack : null;
    }

    @Override
    public int getFluidAmount() {
        return outputstack.amount;
    }

    @Override
    public int getCapacity() {
        return 8000;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        int actualdrain = maxDrain;
        if (actualdrain > outputstack.amount)
            actualdrain = outputstack.amount;
        FluidStack ret = new FluidStack(WATER, actualdrain);
        if (ret.amount == 0)
            ret = null;
        if (doDrain) {
            outputstack.amount -= actualdrain;
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(outputstack, this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, this, actualdrain));
        }
        return ret;
    }

    @Override
    public int getGUIID() {
        return 3;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource != null && resource.getFluid() == WATER && drain(resource.amount, false) != null)
            return drain(resource.amount, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid == null || fluid == WATER;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{getInfo()};
    }

    @Override
    public String[] getInfoData() {
        return new String[]{StatCollector.translateToLocal("tooltip.tile.waterpump.0.name")+" " + ConfigHandler.mbWaterperSec + StatCollector.translateToLocal("tooltip.tile.waterpump.1.name"), StatCollector.translateToLocal("tooltip.tile.waterpump.2.name")};
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        texture[ForgeDirection.UP.ordinal()] = par1IconRegister.registerIcon(MainMod.MOD_ID + ":heatedWaterPumpTop");
        texture[ForgeDirection.DOWN.ordinal()] = par1IconRegister.registerIcon(MainMod.MOD_ID + ":heatedWaterPumpDown");
        for (int i = 2; i < 7; i++) {
            texture[i] = par1IconRegister.registerIcon(MainMod.MOD_ID + ":heatedWaterPumpSide");
        }
    }
}
