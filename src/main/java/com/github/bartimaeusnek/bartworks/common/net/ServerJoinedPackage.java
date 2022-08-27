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
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.google.common.io.ByteArrayDataInput;
import gregtech.api.net.GT_Packet;
import net.minecraft.world.IBlockAccess;

public class ServerJoinedPackage extends GT_Packet {

    private byte config;

    ServerJoinedPackage() {
        super(true);
    }

    public ServerJoinedPackage(Object obj) {
        super(false);
        this.config = (byte)
                (ConfigHandler.classicMode && ConfigHandler.disableExtraGassesForEBF
                        ? 3
                        : ConfigHandler.classicMode ? 2 : ConfigHandler.disableExtraGassesForEBF ? 1 : 0);
    }

    @Override
    public byte getPacketID() {
        return 4;
    }

    @Override
    public byte[] encode() {
        return new byte[] {this.config};
    }

    @Override
    public GT_Packet decode(ByteArrayDataInput byteArrayDataInput) {
        this.config = byteArrayDataInput.readByte();
        return this;
    }

    @Override
    public void process(IBlockAccess iBlockAccess) {
        boolean gas = (this.config & 1) != 0;
        boolean classic = (this.config & 0b10) != 0;
        MainMod.runOnPlayerJoined(classic, gas);
    }
}
