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

package bartworks.system.material;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

import bartworks.common.net.PacketBWMetaBlock;
import gregtech.api.enums.GTValues;

public abstract class TileEntityMetaGeneratedBlock extends TileEntity {

    public short mMetaData;

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        this.mMetaData = aNBT.getShort("m");
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        aNBT.setShort("m", this.mMetaData);
    }

    @Override
    public Packet getDescriptionPacket() {
        if (!this.worldObj.isRemote) GTValues.NW.sendPacketToAllPlayersInRange(
            this.worldObj,
            new PacketBWMetaBlock(this.xCoord, (short) this.yCoord, this.zCoord, this.mMetaData),
            this.xCoord,
            this.zCoord);
        return null;
    }

    protected abstract Block getProperBlock();

    public ArrayList<ItemStack> getDrops(int aFortune) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        if (this.mMetaData <= 0) {
            rList.add(new ItemStack(Blocks.cobblestone, 1, 0));
            return rList;
        }
        rList.add(new ItemStack(this.getProperBlock(), 1, this.mMetaData));
        return rList;
    }
}
