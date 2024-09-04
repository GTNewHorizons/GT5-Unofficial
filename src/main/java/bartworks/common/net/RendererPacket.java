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

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import bartworks.API.SideReference;
import bartworks.MainMod;
import bartworks.common.tileentities.multis.MTEBioVat;
import bartworks.util.BWColorUtil;
import bartworks.util.Coords;
import gregtech.api.net.GTPacketNew;
import io.netty.buffer.ByteBuf;

public class RendererPacket extends GTPacketNew {

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
    public void encode(ByteBuf aOut) {
        byte r = (byte) (this.integer >> 16 & 0xFF);
        byte g = (byte) (this.integer >> 8 & 0xFF);
        byte b = (byte) (this.integer & 0xFF);
        byte checksum = (byte) (this.coords.x % 25 + this.coords.y % 25
            + this.coords.z % 25
            + this.coords.wID % 25
            + this.integer % 25
            + this.removal);
        aOut.writeInt(this.coords.x)
            .writeShort(this.coords.y)
            .writeInt(this.coords.z)
            .writeInt(this.coords.wID)
            .writeByte(r)
            .writeByte(g)
            .writeByte(b)
            .writeByte(this.removal)
            .writeByte(checksum);
    }

    @Override
    public GTPacketNew decode(ByteArrayDataInput dataInput) {
        this.coords = new Coords(dataInput.readInt(), dataInput.readShort(), dataInput.readInt(), dataInput.readInt());
        this.integer = BWColorUtil
            .getColorFromRGBArray(new int[] { dataInput.readByte(), dataInput.readByte(), dataInput.readByte() });
        this.removal = dataInput.readByte();

        byte checksum = (byte) (this.coords.x % 25 + this.coords.y % 25
            + this.coords.z % 25
            + this.coords.wID % 25
            + this.integer % 25
            + this.removal);

        if (checksum != dataInput.readByte()) {
            MainMod.LOGGER.error("BW Packet was corrupted or modified!");
            return null;
        }

        return new RendererPacket(this.coords, this.integer, this.removal == 1);
    }

    @Override
    public void process(IBlockAccess iBlockAccess) {
        if (SideReference.Side.Client) {
            if (this.removal == 0) MTEBioVat.staticColorMap.put(this.coords, this.integer);
            else MTEBioVat.staticColorMap.remove(this.coords);
        }
    }
}
