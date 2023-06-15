/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.api.network;

import java.nio.charset.StandardCharsets;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kubatech.api.tileentity.CustomTileEntityPacketHandler;
import kubatech.api.utils.ModUtils;
import kubatech.kubatech;

public class CustomTileEntityPacket implements IMessage {

    public int w, x, y, z;
    public final ByteBuf customdata = Unpooled.buffer();

    @SuppressWarnings("unused")
    public CustomTileEntityPacket() {}

    public CustomTileEntityPacket(TileEntity te, byte[] customdata) {
        this.w = te.getWorldObj().provider.dimensionId;
        this.x = te.xCoord;
        this.y = te.yCoord;
        this.z = te.zCoord;
        if (customdata != null && customdata.length > 0) this.customdata.writeBytes(customdata);
    }

    public void sendToAllAround(int range) {
        kubatech.NETWORK.sendToAllAround(this, new NetworkRegistry.TargetPoint(w, x, y, z, range));
    }

    // Helper methods

    public void resetHelperData() {
        customdata.clear();
    }

    public void addData(byte[] data) {
        customdata.writeBytes(data);
    }

    public void addData(byte data) {
        customdata.writeByte(data);
    }

    public void addData(int data) {
        customdata.writeInt(data);
    }

    public void addData(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        addData(bytes.length);
        addData(bytes);
    }

    public void addData(boolean data) {
        customdata.writeBoolean(data);
    }

    public void getData(byte[] bytes) {
        customdata.readBytes(bytes);
    }

    public byte[] getData(int len) {
        byte[] bytes = new byte[len];
        getData(bytes);
        return bytes;
    }

    public int getDataInt() {
        return customdata.readInt();
    }

    public String getDataString() {
        return new String(getData(getDataInt()), StandardCharsets.UTF_8);
    }

    public boolean getDataBoolean() {
        return customdata.readBoolean();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        w = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        customdata.clear();
        buf.readBytes(customdata, buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(w);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(customdata.readableBytes());
        buf.writeBytes(customdata);
    }

    public static class Handler implements IMessageHandler<CustomTileEntityPacket, IMessage> {

        @Override
        public IMessage onMessage(CustomTileEntityPacket message, MessageContext ctx) {
            if (!ModUtils.isClientThreaded()) return null;
            Minecraft mc = Minecraft.getMinecraft();
            if (mc == null) return null;
            if (mc.thePlayer == null) return null;
            World w = mc.thePlayer.getEntityWorld();
            if (w == null) return null;
            if (message.w != w.provider.dimensionId) return null;
            TileEntity e = w.getTileEntity(message.x, message.y, message.z);
            if (e == null || e.isInvalid()) return null;
            if (e instanceof IGregTechTileEntity && !((IGregTechTileEntity) e).isInvalidTileEntity()) {
                IMetaTileEntity mte = ((IGregTechTileEntity) e).getMetaTileEntity();
                if (mte == null) return null;
                if (!(mte instanceof CustomTileEntityPacketHandler)) return null;
                ((CustomTileEntityPacketHandler) mte).HandleCustomPacket(message);
                return null;
            } else if (!(e instanceof CustomTileEntityPacketHandler)) return null;
            ((CustomTileEntityPacketHandler) e).HandleCustomPacket(message);
            return null;
        }
    }
}
