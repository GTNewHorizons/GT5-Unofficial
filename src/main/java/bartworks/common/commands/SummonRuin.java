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

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import bartworks.system.worldgen.MapGenRuins;
import gregtech.commands.GTBaseCommand;

public class SummonRuin extends GTBaseCommand {

    @Override
    public String getCommandName() {
        return "summonRuin";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/summonRuin <x> <z>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            sendHelpMessage(sender);
            return;
        }
        try {
            final int x = Integer.parseInt(args[0]);
            final int z = Integer.parseInt(args[1]);
            new MapGenRuins.RuinsBase().generate(sender.getEntityWorld(), sender.getEntityWorld().rand, x, 256, z);
        } catch (Exception e) {
            sendChatToPlayer(sender, EnumChatFormatting.RED + "Something went wrong!");
        }
    }
}
