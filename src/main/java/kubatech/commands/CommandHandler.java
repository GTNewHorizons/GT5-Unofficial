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

import static kubatech.commands.CommandHandler.Translations.CANT_FIND;
import static kubatech.commands.CommandHandler.Translations.GENERIC_HELP;
import static kubatech.commands.CommandHandler.Translations.INVALID;
import static kubatech.commands.CommandHandler.Translations.USAGE;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class CommandHandler extends CommandBase {

    enum Translations {

        INVALID,
        CANT_FIND,
        GENERIC_HELP,
        USAGE,;

        final String key;

        Translations() {
            key = "kubatech.commandhandler." + this.name()
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

    public static final HashMap<String, ICommand> childCommands = new HashMap<>();

    static {
        registerChildCommand(new CommandBees());
        registerChildCommand(new CommandConfig());
        registerChildCommand(new CommandHelp());
        registerChildCommand(new CommandTea());
    }

    private static void registerChildCommand(ICommand command) {
        childCommands.put(command.getCommandName(), command);
    }

    @Override
    public String getCommandName() {
        return "kubatech";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "kubatech " + USAGE.get();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases() {
        return Collections.singletonList("kt");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (sender.getEntityWorld().isRemote) return;
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(INVALID.get(getCommandUsage(sender))));
            sender.addChatMessage(new ChatComponentText(GENERIC_HELP.get()));
            return;
        }
        if (!childCommands.containsKey(args[0])) {
            sender.addChatMessage(new ChatComponentText(CANT_FIND.get(args[0])));
            sender.addChatMessage(new ChatComponentText(GENERIC_HELP.get()));
            return;
        }
        ICommand cmd = childCommands.get(args[0]);
        if (!cmd.canCommandSenderUseCommand(sender)) {
            ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation(
                "commands.generic.permission");
            chatcomponenttranslation2.getChatStyle()
                .setColor(EnumChatFormatting.RED);
            sender.addChatMessage(chatcomponenttranslation2);
        } else cmd.processCommand(sender, args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

}
