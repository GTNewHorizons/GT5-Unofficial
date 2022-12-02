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

import com.github.bartimaeusnek.bartworks.API.ITileAddsInformation;
import com.github.bartimaeusnek.bartworks.API.ITileDropsContent;
import com.github.bartimaeusnek.bartworks.API.ITileHasDifferentTextureSides;
import com.github.bartimaeusnek.bartworks.API.modularUI.BW_UITextures;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.InvWrapper;
import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Pollution;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class BW_TileEntity_HeatedWaterPump extends TileEntity
        implements ITileDropsContent,
                IFluidHandler,
                IFluidTank,
                ITileWithModularUI,
                ITileAddsInformation,
                ITileHasDifferentTextureSides {

    public static final int FUELSLOT = 0;
    public static final Fluid WATER = FluidRegistry.WATER;
    public ItemStack fuelstack;
    public FluidStack outputstack = new FluidStack(FluidRegistry.WATER, 0);
    public int fuel;
    public byte tick;
    public int maxfuel;
    public ItemStack fakestack = new ItemStack(Blocks.water);

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_) {
        NBTTagCompound subItemStack = new NBTTagCompound();
        if (this.fuelstack != null) {
            this.fuelstack.writeToNBT(subItemStack);
        }
        p_145841_1_.setTag("ItemStack", subItemStack);
        NBTTagCompound subFluidStack = new NBTTagCompound();
        this.outputstack.writeToNBT(subFluidStack);
        p_145841_1_.setTag("FluidStack", subFluidStack);
        p_145841_1_.setInteger("fuel", this.fuel);
        p_145841_1_.setInteger("maxfuel", this.maxfuel);
        p_145841_1_.setByte("tick", this.tick);
        super.writeToNBT(p_145841_1_);
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_) {
        this.tick = p_145839_1_.getByte("tick");
        this.fuel = p_145839_1_.getInteger("fuel");
        this.maxfuel = p_145839_1_.getInteger("maxfuel");
        this.outputstack = FluidStack.loadFluidStackFromNBT(p_145839_1_.getCompoundTag("FluidStack"));
        if (!p_145839_1_.getCompoundTag("ItemStack").equals(new NBTTagCompound()))
            this.fuelstack = ItemStack.loadItemStackFromNBT(p_145839_1_.getCompoundTag("ItemStack"));
        super.readFromNBT(p_145839_1_);
    }

    private boolean checkPreUpdate() {
        return (this.fuelstack == null || this.fuelstack.stackSize <= 0) && this.fuel <= 0;
    }

    private void fixUnderlflow() {
        if (this.fuel < 0) this.fuel = 0;
    }

    private void handleRefuel() {
        if (this.fuelstack != null && this.fuel == 0) {
            this.fuel = this.maxfuel = TileEntityFurnace.getItemBurnTime(this.fuelstack);
            --this.fuelstack.stackSize;
            if (this.fuelstack.stackSize <= 0) this.fuelstack = null;
        }
    }

    private void handleWaterGeneration() {
        if (this.fuel > 0) {
            ++this.tick;
            --this.fuel;
            if (this.tick % 20 == 0) {
                if (this.outputstack.amount <= (8000 - ConfigHandler.mbWaterperSec))
                    this.outputstack.amount += ConfigHandler.mbWaterperSec;
                this.tick = 0;
            }
        }
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) return;

        pushWaterToAdjacentTiles();
        fakestack.setStackDisplayName(outputstack.amount + "L Water");
        if (checkPreUpdate()) return;

        fixUnderlflow();
        handleRefuel();
        handleWaterGeneration();
        causePollution();
    }

    private void pushWaterToAdjacentTiles() {
        Arrays.stream(ForgeDirection.values(), 0, 6) // All but Unknown
                .forEach(direction -> Optional.ofNullable(this.worldObj.getTileEntity(
                                this.xCoord + direction.offsetX,
                                this.yCoord + direction.offsetY,
                                this.zCoord + direction.offsetZ))
                        .ifPresent(te -> {
                            if (te instanceof IFluidHandler) {
                                IFluidHandler tank = (IFluidHandler) te;
                                if (tank.canFill(direction.getOpposite(), this.outputstack.getFluid())) {
                                    int drainage;
                                    if ((drainage = tank.fill(direction.getOpposite(), this.outputstack, false)) > 0) {
                                        tank.fill(direction.getOpposite(), this.outputstack, true);
                                        this.drain(drainage, true);
                                    }
                                }
                            } else if (te instanceof IFluidTank) {
                                IFluidTank tank = (IFluidTank) te;
                                int drainage;
                                if ((drainage = tank.fill(this.outputstack, false)) > 0) {
                                    tank.fill(this.outputstack, true);
                                    this.drain(drainage, true);
                                }
                            }
                        }));
    }

    private void causePollution() {
        Optional.ofNullable(this.worldObj).ifPresent(e -> {
            if (e.getTotalWorldTime() % 20 == 0) {
                Optional.ofNullable(e.getChunkFromBlockCoords(this.xCoord, this.zCoord))
                        .ifPresent(c -> GT_Pollution.addPollution(c, ConfigHandler.pollutionHeatedWaterPumpSecond));
            }
        });
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[] {0};
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
        if (p_70301_1_ == 0) return this.fuelstack;
        else return this.fakestack;
    }

    @Override
    public ItemStack decrStackSize(int slot, int ammount) {
        if (slot != BW_TileEntity_HeatedWaterPump.FUELSLOT
                || this.fuelstack == null
                || ammount > this.fuelstack.stackSize) return null;

        return this.fuelstack.splitStack(ammount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack p_70299_2_) {
        if (slot == BW_TileEntity_HeatedWaterPump.FUELSLOT) this.fuelstack = p_70299_2_;
        else this.fakestack = p_70299_2_;
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
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return TileEntityFurnace.getItemBurnTime(p_94041_2_) > 0
                && p_94041_1_ == BW_TileEntity_HeatedWaterPump.FUELSLOT;
    }

    @Override
    public FluidStack getFluid() {
        return this.outputstack.amount > 0 ? this.outputstack : null;
    }

    @Override
    public int getFluidAmount() {
        return this.outputstack.amount;
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
        if (actualdrain > this.outputstack.amount) actualdrain = this.outputstack.amount;
        FluidStack ret = new FluidStack(BW_TileEntity_HeatedWaterPump.WATER, actualdrain);
        if (ret.amount == 0) ret = null;
        if (doDrain) {
            this.outputstack.amount -= actualdrain;
            FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(
                    this.outputstack, this.getWorldObj(), this.xCoord, this.yCoord, this.zCoord, this, actualdrain));
        }
        return ret;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource != null
                && resource.getFluid() == BW_TileEntity_HeatedWaterPump.WATER
                && this.drain(resource.amount, false) != null) return this.drain(resource.amount, doDrain);
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return this.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid == null || fluid == BW_TileEntity_HeatedWaterPump.WATER;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] {this.getInfo()};
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            StatCollector.translateToLocal("tooltip.tile.waterpump.0.name") + " "
                    + GT_Utility.formatNumbers(ConfigHandler.mbWaterperSec)
                    + String.format(
                            StatCollector.translateToLocal("tooltip.tile.waterpump.1.name"),
                            ConfigHandler.pollutionHeatedWaterPumpSecond),
            StatCollector.translateToLocal("tooltip.tile.waterpump.2.name")
        };
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        ITileHasDifferentTextureSides.texture[ForgeDirection.UP.ordinal()] =
                par1IconRegister.registerIcon(MainMod.MOD_ID + ":heatedWaterPumpTop");
        ITileHasDifferentTextureSides.texture[ForgeDirection.DOWN.ordinal()] =
                par1IconRegister.registerIcon(MainMod.MOD_ID + ":heatedWaterPumpDown");
        for (int i = 2; i < 7; i++) {
            ITileHasDifferentTextureSides.texture[i] =
                    par1IconRegister.registerIcon(MainMod.MOD_ID + ":heatedWaterPumpSide");
        }
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext) {
        ModularWindow.Builder builder = ModularWindow.builder(176, 166);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.bindPlayerInventory(buildContext.getPlayer());
        final IItemHandlerModifiable invWrapper = new InvWrapper(this);

        builder.widget(new SlotWidget(invWrapper, 0)
                        .setFilter(stack -> TileEntityFurnace.getItemBurnTime(stack) > 0)
                        .setPos(55, 52))
                .widget(SlotWidget.phantom(invWrapper, 1).disableInteraction().setPos(85, 32))
                .widget(new ProgressBar()
                        .setProgress(() -> (float) fuel / maxfuel)
                        .setTexture(BW_UITextures.PROGRESSBAR_FUEL, 14)
                        .setDirection(ProgressBar.Direction.UP)
                        .setPos(56, 36)
                        .setSize(14, 14));

        return builder.build();
    }
}
