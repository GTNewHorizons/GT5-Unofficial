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

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_BioVat;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.Coords;
import com.google.common.io.ByteArrayDataInput;
import gregtech.api.net.GT_Packet;
import java.nio.ByteBuffer;
import net.minecraft.world.IBlockAccess;

public class RendererPacket extends GT_Packet {

    private Coords coords;
    private int integer;
    private byte removal;

    public RendererPacket() {
        super(true);
    }

    public RendererPacket(Coords coords, int integer, boolean removal) {
        super(false);
        this.coords = coords;
        this.integer = integer;
        this.removal = (byte) (removal ? 1 : 0);
    }

    @Override
    public byte getPacketID() {
        return 0;
    }

    @Override
    public byte[] encode() {

        byte r = (byte) (((this.integer >> 16) & 0xFF) + Byte.MIN_VALUE);
        byte g = (byte) (((this.integer >> 8) & 0xFF) + Byte.MIN_VALUE);
        byte b = (byte) (((this.integer) & 0xFF) + Byte.MIN_VALUE);
        byte checksum = (byte) (this.coords.x % 25
                + this.coords.y % 25
                + this.coords.z % 25
                + this.coords.wID % 25
                + this.integer % 25
                + this.removal);
        return ByteBuffer.allocate(19)
                .putInt(0, this.coords.x)
                .putShort(4, this.coords.y)
                .putInt(6, this.coords.z)
                .putInt(10, this.coords.wID)
                .put(14, r)
                .put(15, g)
                .put(16, b)
                .put(17, this.removal)
                .put(18, checksum)
                .array();
    }

    //    /**
    //     * only use in a debug enviroment, does not do anything on its own.
    //     * @param buffer
    //     */
    //    public void decodetest (byte[] buffer){
    //        this.coords=new
    // Coords(ByteBuffer.wrap(buffer).getInt(0),ByteBuffer.wrap(buffer).getShort(4),ByteBuffer.wrap(buffer).getInt(6),ByteBuffer.wrap(buffer).getInt(10));
    //        int[] rgb = {ByteBuffer.wrap(buffer).get(14)-Byte.MIN_VALUE,
    // ByteBuffer.wrap(buffer).get(15)-Byte.MIN_VALUE, ByteBuffer.wrap(buffer).get(16)-Byte.MIN_VALUE};
    //        this.integer= BW_Util.getColorFromRGBArray(rgb);
    //        this.removal=ByteBuffer.wrap(buffer).get(17);
    //
    //        byte checksum = (byte) (coords.x%25+coords.y%25+coords.z%25+coords.wID%25+integer%25+removal);
    //    }

    @Override
    public GT_Packet decode(ByteArrayDataInput dataInput) {

        byte[] buffer = new byte[19];
        dataInput.readFully(buffer);

        this.coords = new Coords(
                ByteBuffer.wrap(buffer).getInt(0),
                ByteBuffer.wrap(buffer).getShort(4),
                ByteBuffer.wrap(buffer).getInt(6),
                ByteBuffer.wrap(buffer).getInt(10));
        int[] rgb = {
            ByteBuffer.wrap(buffer).get(14) - Byte.MIN_VALUE,
            ByteBuffer.wrap(buffer).get(15) - Byte.MIN_VALUE,
            ByteBuffer.wrap(buffer).get(16) - Byte.MIN_VALUE
        };
        this.integer = BW_ColorUtil.getColorFromRGBArray(rgb);
        this.removal = ByteBuffer.wrap(buffer).get(17);

        byte checksum = (byte) (this.coords.x % 25
                + this.coords.y % 25
                + this.coords.z % 25
                + this.coords.wID % 25
                + this.integer % 25
                + this.removal);

        if (checksum != ByteBuffer.wrap(buffer).get(18)) {
            MainMod.LOGGER.error("BW Packet was corrupted or modified!");
            return null;
        }

        return new RendererPacket(this.coords, this.integer, this.removal == 1);
    }

    @Override
    public void process(IBlockAccess iBlockAccess) {
        if (SideReference.Side.Client) {
            if (this.removal == 0) GT_TileEntity_BioVat.staticColorMap.put(this.coords, this.integer);
            else GT_TileEntity_BioVat.staticColorMap.remove(this.coords);
        }
    }
}
