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

import java.util.HashSet;

import net.minecraft.world.IBlockAccess;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.io.ByteArrayDataInput;

import bartworks.system.oredict.OreDictHandler;
import gregtech.api.net.GTPacket;
import io.netty.buffer.ByteBuf;

public class PacketOreDictCache extends GTPacket {

    private HashSet<Pair<Integer, Short>> hashSet = new HashSet<>();

    public PacketOreDictCache() {
        super();
    }

    public PacketOreDictCache(HashSet<Pair<Integer, Short>> set) {
        super();
        this.hashSet = set;
    }

    @Override
    public byte getPacketID() {
        return 25;
    }

    @Override
    public void encode(ByteBuf aOut) {
        int size = this.hashSet.size();
        aOut.writeInt(size);
        for (Pair<Integer, Short> p : this.hashSet) {
            aOut.writeInt(p.getKey())
                .writeShort(p.getValue());
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput byteArrayDataInput) {
        int size = byteArrayDataInput.readInt();
        for (int i = 0; i < size; i++) {
            this.hashSet.add(Pair.of(byteArrayDataInput.readInt(), byteArrayDataInput.readShort()));
        }
        return new PacketOreDictCache(this.hashSet);
    }

    @Override
    public void process(IBlockAccess iBlockAccess) {
        OreDictHandler.getNonBWCache()
            .clear();
        OreDictHandler.getNonBWCache()
            .addAll(this.hashSet);
    }
}
