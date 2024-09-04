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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.crafting.CraftingManager;

public class PrintRecipeListToFile extends CommandBase {

    @Override
    public String getCommandName() {
        return "prltf";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "prltf <FilePath>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        File file = new File(args[0]);
        try {

            BufferedWriter fw = new BufferedWriter(new FileWriter(file));
            CraftingManager.getInstance()
                .getRecipeList()
                .forEach(e -> {
                    try {
                        fw.write(
                            e.toString() + " = "
                                + e.getRecipeOutput()
                                    .getDisplayName()
                                + "\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
