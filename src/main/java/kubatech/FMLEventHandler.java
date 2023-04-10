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

package kubatech;

import kubatech.api.helpers.UUIDFinder;
import kubatech.api.network.LoadConfigPacket;
import kubatech.savedata.PlayerDataManager;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class FMLEventHandler {

    // Gets fired only server-sided
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) return;
        UUIDFinder.updateMapping(event.player.getCommandSenderName(), event.player.getPersistentID());
        PlayerDataManager.initializePlayer((EntityPlayerMP) event.player);
        kubatech.info("Sending config to " + event.player.getDisplayName());
        kubatech.NETWORK.sendTo(LoadConfigPacket.instance, (EntityPlayerMP) event.player);
    }
}
