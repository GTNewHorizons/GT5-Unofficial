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

package com.github.bartimaeusnek.bartworks.common.net;

import com.github.bartimaeusnek.bartworks.common.items.Circuit_Programmer;
import com.google.common.io.ByteArrayDataInput;
import gregtech.api.net.GT_Packet;
import java.nio.ByteBuffer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CircuitProgrammerPacket extends GT_Packet {

    private int dimID, playerID;
    private byte chipCfg;
    private boolean hasChip;

    public CircuitProgrammerPacket() {
        super(true);
    }

    public CircuitProgrammerPacket(int dimID, int playerID, boolean hasChip, byte chipCfg) {
        super(false);
        this.dimID = dimID;
        this.playerID = playerID;
        this.hasChip = hasChip;
        this.chipCfg = chipCfg;
    }

    @Override
    public byte getPacketID() {
        return 1;
    }

    @Override
    public byte[] encode() {
        return ByteBuffer.allocate(9)
                .putInt(0, this.dimID)
                .putInt(4, this.playerID)
                .put(8, (this.hasChip ? this.chipCfg : -1))
                .array();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput byteArrayDataInput) {
        byte[] ret = new byte[9];
        byteArrayDataInput.readFully(ret);
        return new CircuitProgrammerPacket(
                ByteBuffer.wrap(ret).getInt(0),
                ByteBuffer.wrap(ret).getInt(4),
                ByteBuffer.wrap(ret).get(8) > -1,
                ByteBuffer.wrap(ret).get(8));
    }

    @Override
    public void process(IBlockAccess iBlockAccess) {
        World w = DimensionManager.getWorld(this.dimID);
        if (w != null && w.getEntityByID(this.playerID) instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) w.getEntityByID(this.playerID)).getHeldItem();
            if ((stack != null) && (stack.stackSize > 0)) {
                Item item = stack.getItem();
                if (item instanceof Circuit_Programmer) {
                    NBTTagCompound nbt = stack.getTagCompound();
                    nbt.setBoolean("HasChip", this.hasChip);
                    if (this.hasChip) nbt.setByte("ChipConfig", this.chipCfg);
                    stack.setTagCompound(nbt);
                    ((EntityPlayer) w.getEntityByID(this.playerID))
                            .inventory.setInventorySlotContents(
                                    ((EntityPlayer) w.getEntityByID(this.playerID)).inventory.currentItem, stack);
                }
            }
        }
    }
}
