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

package com.github.bartimaeusnek.bartworks.server.container;

import com.github.bartimaeusnek.bartworks.common.tileentities.classic.BW_TileEntity_HeatedWaterPump;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.GT_Slot_Render;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.nio.ByteBuffer;
import java.util.Iterator;

public class BW_Container_HeatedWaterPump extends Container {

    final BW_TileEntity_HeatedWaterPump TILE;
    final IInventory INVENTORY;
    public int fuel, water, awater, maxfuel;
    long timer = 0;
    byte[] anetfuel, netfuel, anetmaxfuel, netmaxfuel;

    public BW_Container_HeatedWaterPump(BW_TileEntity_HeatedWaterPump TILE, EntityPlayer INVENTORY) {
        this.TILE = TILE;
        this.INVENTORY = INVENTORY.inventory;

        this.addSlotToContainer(new Slot(TILE, 0, 56, 53));
        this.addSlotToContainer(new GT_Slot_Render(TILE, 1, 86, 33));
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(this.INVENTORY, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(this.INVENTORY, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
        return null;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (TILE.getWorldObj().isRemote)
            return;

        fuel = this.TILE.fuel;
        maxfuel = this.TILE.maxfuel;
        water = this.TILE.outputstack.amount;
        netfuel = ByteBuffer.allocate(8).putInt(fuel).array();
        netmaxfuel = ByteBuffer.allocate(8).putInt(maxfuel).array();
        ++timer;
        Iterator var2 = this.crafters.iterator();
        if (timer >= Long.MAX_VALUE - 1)
            timer = 0;
        while (true) {
            do {
                if (!var2.hasNext()) {
                    netfuel = anetfuel;
                    netmaxfuel = anetmaxfuel;
                    water = awater;
                    return;
                }
                ICrafting var1 = (ICrafting) var2.next();
                if (this.timer % 500 == 10 || this.water != awater)
                    var1.sendProgressBarUpdate(this, 0, water);
                if (this.timer % 500 == 10 || netfuel != anetfuel)
                    for (int i = 0; i < netfuel.length; i++) {
                        var1.sendProgressBarUpdate(this, i + 1, netfuel[i]);
                    }
                if (this.timer % 500 == 10 || netmaxfuel != anetmaxfuel)
                    for (int i = 0; i < netmaxfuel.length; i++) {
                        var1.sendProgressBarUpdate(this, i + 9, netmaxfuel[i]);
                    }
            } while (this.timer % 500 != 10 && this.water != awater);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int packetID, int valure) {
        if (netfuel == null)
            netfuel = new byte[8];
        if (netmaxfuel == null)
            netmaxfuel = new byte[8];
        switch (packetID) {
            case 0: {
                water = valure;
                this.getSlot(1).putStack(this.getSlot(1).getStack().setStackDisplayName(this.water + "L Water"));
                break;
            }
            case 1:
                netfuel[0] = (byte) valure;
                break;
            case 2:
                netfuel[1] = (byte) valure;
                break;
            case 3:
                netfuel[2] = (byte) valure;
                break;
            case 4:
                netfuel[3] = (byte) valure;
                break;
            case 5:
                netfuel[4] = (byte) valure;
                break;
            case 6:
                netfuel[5] = (byte) valure;
                break;
            case 7:
                netfuel[6] = (byte) valure;
                break;
            case 8:
                netfuel[7] = (byte) valure;
                break;
            case 9:
                netmaxfuel[0] = (byte) valure;
                break;
            case 10:
                netmaxfuel[1] = (byte) valure;
                break;
            case 11:
                netmaxfuel[2] = (byte) valure;
                break;
            case 12:
                netmaxfuel[3] = (byte) valure;
                break;
            case 13:
                netmaxfuel[4] = (byte) valure;
                break;
            case 14:
                netmaxfuel[5] = (byte) valure;
                break;
            case 15:
                netmaxfuel[6] = (byte) valure;
                break;
            case 16:
                netmaxfuel[7] = (byte) valure;
                break;
            default:
                break;
        }
        if (packetID > 0 && packetID < 9)
            this.fuel = ByteBuffer.wrap(netfuel).getInt();
        if (packetID > 8 && packetID < 17)
            this.maxfuel = ByteBuffer.wrap(netmaxfuel).getInt();
    }
}
