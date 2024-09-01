/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
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

package kubatech.commands;

import static kubatech.commands.CommandConfig.Translations.INVALID_OPTION;
import static kubatech.commands.CommandConfig.Translations.SUCCESS;
import static kubatech.commands.CommandConfig.Translations.USAGE;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import kubatech.config.Config;
import kubatech.kubatech;
import kubatech.network.LoadConfigPacket;

public class CommandConfig extends CommandBase {

    enum Translations {

        INVALID_OPTION,
        SUCCESS,
        USAGE;

        final String key;

        Translations() {
            key = "kubatech.command.config." + this.name()
                .toLowerCase();
        }

        public String get() {
            return StatCollector.translateToLocal(key);
        }

        public String get(Object... args) {
            return StatCollector.translateToLocalFormatted(key, args);
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return get();
        }
    }

    @Override
    public String getCommandName() {
        return "config";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "config " + USAGE.get();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0 || !args[0].equals("reload")) {
            sender.addChatMessage(new ChatComponentText(INVALID_OPTION.get()));
            return;
        }
        Config.synchronizeConfiguration();
        MinecraftServer.getServer()
            .getConfigurationManager().playerEntityList.forEach(player -> {
                if (!(player instanceof EntityPlayerMP)) return;
                kubatech.info("Sending config to " + player.getDisplayName());
                kubatech.NETWORK.sendTo(LoadConfigPacket.instance, player);
            });
        sender.addChatMessage(new ChatComponentText(SUCCESS.get()));
    }
}
