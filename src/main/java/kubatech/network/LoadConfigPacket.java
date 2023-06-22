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

package kubatech.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import kubatech.config.Config;
import kubatech.kubatech;

public class LoadConfigPacket implements IMessage {

    public static final LoadConfigPacket instance = new LoadConfigPacket();

    @Override
    public void fromBytes(ByteBuf buf) {
        Config.MobHandler.playerOnlyDropsModifier = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(Config.MobHandler.playerOnlyDropsModifier);
    }

    public static class Handler implements IMessageHandler<LoadConfigPacket, IMessage> {

        @Override
        public IMessage onMessage(LoadConfigPacket message, MessageContext ctx) {
            kubatech.info("Received KubaTech config, parsing");
            return null;
        }
    }
}
