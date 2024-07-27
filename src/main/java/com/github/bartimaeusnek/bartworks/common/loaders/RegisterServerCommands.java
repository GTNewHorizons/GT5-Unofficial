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

package com.github.bartimaeusnek.bartworks.common.loaders;

import com.github.bartimaeusnek.bartworks.common.commands.ChangeConfig;
import com.github.bartimaeusnek.bartworks.common.commands.ClearCraftingCache;
import com.github.bartimaeusnek.bartworks.common.commands.GetWorkingDirectory;
import com.github.bartimaeusnek.bartworks.common.commands.PrintRecipeListToFile;
import com.github.bartimaeusnek.bartworks.common.commands.RunGC;
import com.github.bartimaeusnek.bartworks.common.commands.SummonRuin;

import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class RegisterServerCommands {

    public static void registerAll(FMLServerStartingEvent event) {
        event.registerServerCommand(new SummonRuin());
        event.registerServerCommand(new ChangeConfig());
        event.registerServerCommand(new PrintRecipeListToFile());
        event.registerServerCommand(new ClearCraftingCache());
        event.registerServerCommand(new GetWorkingDirectory());
        event.registerServerCommand(new RunGC());
    }
}
