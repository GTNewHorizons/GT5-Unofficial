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

package com.github.bartimaeusnek.bartworks.common.commands;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import java.lang.reflect.Field;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ChangeConfig extends CommandBase {
    @Override
    public String getCommandName() {
        return "bwcfg";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "bwcfg <NameOfVariable> <newValue>";
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        try {
            Field f = ConfigHandler.class.getField(p_71515_2_[0]);
            Class c = f.getType();
            if (c.equals(int.class)) {
                int l;
                try {
                    l = Integer.parseInt(p_71515_2_[1]);
                } catch (NumberFormatException e) {
                    p_71515_1_.addChatMessage(new ChatComponentText("you need to enter a number!"));
                    return;
                }
                f.setInt(null, l);
            } else if (c.equals(long.class)) {
                long l;
                try {
                    l = Long.parseLong(p_71515_2_[1]);
                } catch (NumberFormatException e) {
                    p_71515_1_.addChatMessage(new ChatComponentText("you need to enter a number!"));
                    return;
                }
                f.setLong(null, l);
            } else if (c.equals(boolean.class)) {
                if (p_71515_2_[1].equalsIgnoreCase("true") || p_71515_2_[1].equalsIgnoreCase("1"))
                    f.setBoolean(null, true);
                else if (p_71515_2_[1].equalsIgnoreCase("false") || p_71515_2_[1].equalsIgnoreCase("0"))
                    f.setBoolean(null, false);
                else {
                    p_71515_1_.addChatMessage(new ChatComponentText("booleans need to be set to true or false"));
                }
            }
            p_71515_1_.addChatMessage(new ChatComponentText("Set " + p_71515_2_[0] + " to " + p_71515_2_[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
