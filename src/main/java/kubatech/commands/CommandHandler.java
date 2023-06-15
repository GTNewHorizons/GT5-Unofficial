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

package kubatech.commands;

import static kubatech.commands.CommandHandler.Translations.CANT_FIND;
import static kubatech.commands.CommandHandler.Translations.GENERIC_HELP;
import static kubatech.commands.CommandHandler.Translations.INVALID;
import static kubatech.commands.CommandHandler.Translations.USAGE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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

import kubatech.kubatech;

public class CommandHandler extends CommandBase {

    enum Translations {

        INVALID,
        CANT_FIND,
        GENERIC_HELP,
        USAGE,;

        final String key;

        Translations() {
            key = "commandhandler." + this.name()
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

    private static final List<String> aliases = Collections.singletonList("kt");
    public static final HashMap<String, ICommand> commands = new HashMap<>();

    @Override
    public String getCommandName() {
        return "kubatech";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "kubatech " + USAGE.get();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        if (p_71515_1_.getEntityWorld().isRemote) return;
        if (p_71515_2_.length == 0) {
            p_71515_1_.addChatMessage(new ChatComponentText(INVALID.get(getCommandUsage(p_71515_1_))));
            p_71515_1_.addChatMessage(new ChatComponentText(GENERIC_HELP.get()));
            return;
        }
        if (!commands.containsKey(p_71515_2_[0])) {
            p_71515_1_.addChatMessage(new ChatComponentText(CANT_FIND.get(p_71515_2_[0])));
            p_71515_1_.addChatMessage(new ChatComponentText(GENERIC_HELP.get()));
            return;
        }
        ICommand cmd = commands.get(p_71515_2_[0]);
        if (!cmd.canCommandSenderUseCommand(p_71515_1_)) {
            ChatComponentTranslation chatcomponenttranslation2 = new ChatComponentTranslation(
                "commands.generic.permission");
            chatcomponenttranslation2.getChatStyle()
                .setColor(EnumChatFormatting.RED);
            p_71515_1_.addChatMessage(chatcomponenttranslation2);
        } else cmd.processCommand(
            p_71515_1_,
            p_71515_2_.length > 1 ? Arrays.copyOfRange(p_71515_2_, 1, p_71515_2_.length) : new String[0]);
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return true;
    }

    public static void addCommand(ICommand command) {
        commands.put(command.getCommandName(), command);
    }

    static {
        String ChildCommandDesc = "L" + ChildCommand.class.getName()
            .replace(".", "/") + ";";
        kubatech.myClasses.stream()
            .filter(
                clazz -> clazz.invisibleAnnotations != null && clazz.invisibleAnnotations.stream()
                    .anyMatch(ann -> ann.desc.equals(ChildCommandDesc)))
            .forEach(clazz -> {
                try {
                    addCommand(
                        (ICommand) Class.forName(clazz.name.replace("/", "."))
                            .newInstance());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.CLASS)
    public @interface ChildCommand {}
}
