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

package com.github.bartimaeusnek.bartworks.ASM;

import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import net.minecraftforge.common.config.Configuration;

@IFMLLoadingPlugin.SortingIndex(Integer.MAX_VALUE) // Load as late as possible (after fastcraft/OptiFine).
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions("com.github.bartimaeusnek.bartworks.ASM")
@IFMLLoadingPlugin.Name(BWCorePlugin.BWCORE_PLUGIN_NAME)
public class BWCorePlugin implements IFMLLoadingPlugin {

    public static final String BWCORE_PLUGIN_NAME = "BartWorks ASM Core Plugin";

    public static File minecraftDir;

    public BWCorePlugin() {
        // Injection Code taken from CodeChickenLib
        if (BWCorePlugin.minecraftDir != null) return; // get called twice, once for IFMLCallHook
        BWCorePlugin.minecraftDir = (File) FMLInjectionData.data()[6];
        // do all the configuration already now...
        new ConfigHandler(new Configuration(new File(new File(BWCorePlugin.minecraftDir, "config"), "bartworks.cfg")));
        BWCoreTransformer.shouldTransform[2] = false;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {BWCoreTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return BWCore.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void injectData(Map<String, Object> data) {
        if (data.get("runtimeDeobfuscationEnabled") != null) {
            BWCoreTransformer.obfs = (boolean) data.get("runtimeDeobfuscationEnabled");
        }
        if (data.get("coremodList") != null) {
            for (Object o : (ArrayList) data.get("coremodList")) {
                if (o.toString().contains("MicdoodlePlugin")) {
                    BWCoreTransformer.shouldTransform[2] = ConfigHandler.enabledPatches[2];
                    break;
                }
            }
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
