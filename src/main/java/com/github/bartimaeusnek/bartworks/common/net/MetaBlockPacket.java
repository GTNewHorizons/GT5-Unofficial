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

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Block_TE;
import com.github.bartimaeusnek.bartworks.util.MurmurHash3;
import com.google.common.io.ByteArrayDataInput;
import gregtech.api.net.GT_Packet;
import java.nio.ByteBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MetaBlockPacket extends GT_Packet {

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
    public byte[] encode() {
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
        return ByteBuffer.allocate(16)
                .putInt(this.x)
                .putInt(this.z)
                .putShort(this.y)
                .putShort(this.meta)
                .putInt(hash)
                .array();
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput byteArrayDataInput) {
        byte[] tmp = new byte[16];
        byteArrayDataInput.readFully(tmp);
        ByteBuffer buff = ByteBuffer.wrap(tmp);
        this.x = buff.getInt();
        this.z = buff.getInt();
        this.y = buff.getShort();
        this.meta = buff.getShort();
        MetaBlockPacket todecode = new MetaBlockPacket(this.x, this.y, this.z, this.meta);
        if (buff.getInt()
                != MurmurHash3.murmurhash3_x86_32(
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
            if ((tTileEntity instanceof BW_MetaGenerated_Block_TE)) {
                ((BW_MetaGenerated_Block_TE) tTileEntity).mMetaData = this.meta;
            }
            if (((iBlockAccess instanceof World)) && (((World) iBlockAccess).isRemote)) {
                ((World) iBlockAccess).markBlockForUpdate(this.x, this.y, this.z);
            }
        }
    }
}
