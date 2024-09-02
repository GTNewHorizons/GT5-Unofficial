/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
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

package bartworks.common.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;

import bartworks.common.items.ItemCircuitProgrammer;
import gregtech.api.net.GTPacketNew;
import io.netty.buffer.ByteBuf;

public class CircuitProgrammerPacket extends GTPacketNew {

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
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.dimID)
            .writeInt(this.playerID)
            .writeByte(this.hasChip ? this.chipCfg : -1);
    }

    @Override
    public GTPacketNew decode(ByteArrayDataInput byteArrayDataInput) {
        return new CircuitProgrammerPacket(
            byteArrayDataInput.readInt(),
            byteArrayDataInput.readInt(),
            byteArrayDataInput.readByte() > -1,
            byteArrayDataInput.readByte());
    }

    @Override
    public void process(IBlockAccess iBlockAccess) {
        World w = DimensionManager.getWorld(this.dimID);
        if (w != null && w.getEntityByID(this.playerID) instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) w.getEntityByID(this.playerID)).getHeldItem();
            if (stack != null && stack.stackSize > 0) {
                Item item = stack.getItem();
                if (item instanceof ItemCircuitProgrammer) {
                    NBTTagCompound nbt = stack.getTagCompound();
                    nbt.setBoolean("HasChip", this.hasChip);
                    if (this.hasChip) nbt.setByte("ChipConfig", this.chipCfg);
                    stack.setTagCompound(nbt);
                    ((EntityPlayer) w.getEntityByID(this.playerID)).inventory.setInventorySlotContents(
                        ((EntityPlayer) w.getEntityByID(this.playerID)).inventory.currentItem,
                        stack);
                }
            }
        }
    }
}
