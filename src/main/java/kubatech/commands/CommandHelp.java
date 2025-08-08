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

import static kubatech.commands.CommandHelp.Translations.POSSIBLE_COMMANDS;
import static kubatech.commands.CommandHelp.Translations.USAGE;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class CommandHelp extends CommandBase {

    enum Translations {

        POSSIBLE_COMMANDS,
        USAGE;

        final String key;

        Translations() {
            key = "kubatech.command.help." + this.name()
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
        return "help";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "help " + USAGE.get();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        sender.addChatMessage(new ChatComponentText(POSSIBLE_COMMANDS.get()));
        CommandHandler.childCommands.values()
            .forEach(c -> sender.addChatMessage(new ChatComponentText("/kubatech " + c.getCommandUsage(sender))));
    }
}
