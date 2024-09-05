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

package bartworks.common.commands;

import java.lang.reflect.Field;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import bartworks.common.configs.ConfigHandler;

public class ChangeConfig extends CommandBase {

    @Override
    public String getCommandName() {
        return "bwcfg";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "bwcfg <NameOfVariable> <newValue>";
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void processCommand(ICommandSender sender, String[] args) {
        try {
            Field f = ConfigHandler.class.getField(args[0]);
            Class c = f.getType();
            if (c.equals(int.class)) {
                int l;
                try {
                    l = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.addChatMessage(new ChatComponentText("you need to enter a number!"));
                    return;
                }
                f.setInt(null, l);
            } else if (c.equals(long.class)) {
                long l;
                try {
                    l = Long.parseLong(args[1]);
                } catch (NumberFormatException e) {
                    sender.addChatMessage(new ChatComponentText("you need to enter a number!"));
                    return;
                }
                f.setLong(null, l);
            } else if (c.equals(boolean.class)) {
                if ("true".equalsIgnoreCase(args[1]) || "1".equalsIgnoreCase(args[1])) f.setBoolean(null, true);
                else if ("false".equalsIgnoreCase(args[1]) || "0".equalsIgnoreCase(args[1])) f.setBoolean(null, false);
                else {
                    sender.addChatMessage(new ChatComponentText("booleans need to be set to true or false"));
                }
            }
            sender.addChatMessage(new ChatComponentText("Set " + args[0] + " to " + args[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
