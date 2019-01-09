/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks;


import com.github.bartimaeusnek.bartworks.client.creativetabs.BioTab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.GT2Tab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.bartworksTab;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.GTNHBlocks;
import com.github.bartimaeusnek.bartworks.common.loaders.LoaderRegistry;
import com.github.bartimaeusnek.bartworks.common.net.BW_Network;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.github.bartimaeusnek.bartworks.API.API_REFERENCE.VERSION;

@Mod(
        modid = MainMod.modID, name = MainMod.name, version = MainMod.version,
        dependencies = "required-after:IC2; "
                + "required-after:gregtech; "
)
public final class MainMod {
    public static final String name = "BartWorks";
    public static final String version = "@version@";
    public static final String modID = "bartworks";
    public static final Logger logger = LogManager.getLogger(name);
    public static final CreativeTabs GT2 = new GT2Tab("GT2C");
    public static final CreativeTabs BioTab = new BioTab("BioTab");
    public static final CreativeTabs BWT = new bartworksTab("bartworks");
    public static final IGuiHandler GH = new GuiHandler();
    public static boolean GTNH = false;

    @Mod.Instance(modID)
    public static MainMod instance;
    public static ConfigHandler CHandler;
    public static BW_Network BW_Network_instance = new BW_Network();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {
        if (!(VERSION.equals(version))) {
            logger.error("Something has loaded an old API. Please contact the Mod authors to update!");
        }

        if (Loader.isModLoaded("dreamcraft")) {
            GTNH = true;
        }
        CHandler = new ConfigHandler(preinit);
        if (GTNH)
            logger.info("GTNH-Detected . . . ACTIVATE HARDMODE.");

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        new LoaderRegistry().run();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postinit) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, GH);
        if (ConfigHandler.BioLab)
            new GTNHBlocks().run();
    }


}
