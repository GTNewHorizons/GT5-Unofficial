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

import java.nio.ByteBuffer;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import bartworks.MainMod;
import bartworks.system.material.TileEntityMetaGeneratedBlock;
import bartworks.util.MurmurHash3;
import gregtech.api.net.GTPacketNew;
import io.netty.buffer.ByteBuf;

public class MetaBlockPacket extends GTPacketNew {

    int x;
    short y;
    int z;
    short meta;

    public MetaBlockPacket(int x, int y, int z, int meta) {
        super(false);
        this.x = x;
        this.y = (short) y;
        this.z = z;
        this.meta = (short) meta;
    }

    public MetaBlockPacket() {
        super(true);
    }

    @Override
    public byte getPacketID() {
        return 2;
    }

    @Override
    public void encode(ByteBuf aOut) {
        int hash = MurmurHash3.murmurhash3_x86_32(
            ByteBuffer.allocate(12)
                .putInt(this.x)
                .putInt(this.z)
                .putShort(this.y)
                .putShort(this.meta)
                .array(),
            0,
            12,
            31);
        aOut.writeInt(this.x)
            .writeInt(this.z)
            .writeShort(this.y)
            .writeShort(this.meta)
            .writeInt(hash);
    }

    @Override
    public GTPacketNew decode(ByteArrayDataInput byteArrayDataInput) {
        this.x = byteArrayDataInput.readInt();
        this.z = byteArrayDataInput.readInt();
        this.y = byteArrayDataInput.readShort();
        this.meta = byteArrayDataInput.readShort();
        MetaBlockPacket todecode = new MetaBlockPacket(this.x, this.y, this.z, this.meta);
        if (byteArrayDataInput.readInt() != MurmurHash3.murmurhash3_x86_32(
            ByteBuffer.allocate(12)
                .putInt(this.x)
                .putInt(this.z)
                .putShort(this.y)
                .putShort(this.meta)
                .array(),
            0,
            12,
            31)) {
            MainMod.LOGGER.error("PACKET HASH DOES NOT MATCH!");
            return null;
        }
        return todecode;
    }

    @Override
    public void process(IBlockAccess iBlockAccess) {
        if (iBlockAccess != null) {
            TileEntity tTileEntity = iBlockAccess.getTileEntity(this.x, this.y, this.z);
            if (tTileEntity instanceof TileEntityMetaGeneratedBlock) {
                ((TileEntityMetaGeneratedBlock) tTileEntity).mMetaData = this.meta;
            }
            if (iBlockAccess instanceof World && ((World) iBlockAccess).isRemote) {
                ((World) iBlockAccess).markBlockForUpdate(this.x, this.y, this.z);
            }
        }
    }
}
