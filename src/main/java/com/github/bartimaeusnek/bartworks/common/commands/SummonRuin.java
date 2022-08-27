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

import com.github.bartimaeusnek.bartworks.system.worldgen.MapGenRuins;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class SummonRuin extends CommandBase {

    @Override
    public String getCommandName() {
        return "SummonRuin";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "SummonRuin <x> <z>";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] p_71515_2_) {
        try {
            new MapGenRuins.RuinsBase()
                    .generate(
                            iCommandSender.getEntityWorld(),
                            iCommandSender.getEntityWorld().rand,
                            Integer.parseInt(p_71515_2_[0]),
                            256,
                            Integer.parseInt(p_71515_2_[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
