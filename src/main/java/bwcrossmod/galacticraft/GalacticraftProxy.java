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

package bwcrossmod.galacticraft;

import net.minecraftforge.common.MinecraftForge;

import bartworks.API.SideReference;
import bartworks.common.configs.ConfigHandler;
import bartworks.system.oregen.BWWorldGenRoss128b;
import bartworks.system.oregen.BWWorldGenRoss128ba;
import bwcrossmod.galacticraft.atmosphere.BWAtmosphereManager;
import bwcrossmod.galacticraft.solarsystems.Ross128SolarSystem;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class GalacticraftProxy {

    private GalacticraftProxy() {}

    public static void postInit(FMLPostInitializationEvent e) {
        if (SideReference.Side.Server || SideReference.EffectiveSide.Server) {
            GalacticraftProxy.serverPostInit(e);
        } else {
            GalacticraftProxy.clientPostInit(e);
        }
        GalacticraftProxy.commonPostInit(e);
    }

    public static void preInit(FMLPreInitializationEvent e) {
        if (SideReference.Side.Server || SideReference.EffectiveSide.Server) {
            GalacticraftProxy.serverpreInit(e);
        } else {
            GalacticraftProxy.clientpreInit(e);
        }
        GalacticraftProxy.commonpreInit(e);
    }

    private static void serverpreInit(FMLPreInitializationEvent e) {}

    private static void clientpreInit(FMLPreInitializationEvent e) {}

    private static void commonpreInit(FMLPreInitializationEvent e) {
        BWWorldGenRoss128b.initOres();
        BWWorldGenRoss128ba.init_Ores();
        MinecraftForge.EVENT_BUS.register(BWAtmosphereManager.INSTANCE);
    }

    public static void init(FMLInitializationEvent e) {
        if (SideReference.Side.Server || SideReference.EffectiveSide.Server) {
            GalacticraftProxy.serverInit(e);
        } else {
            GalacticraftProxy.clientInit(e);
        }
        GalacticraftProxy.commonInit(e);
    }

    private static void serverInit(FMLInitializationEvent e) {}

    private static void clientInit(FMLInitializationEvent e) {}

    private static void commonInit(FMLInitializationEvent e) {
        if (ConfigHandler.Ross128Enabled) Ross128SolarSystem.init();
    }

    private static void serverPostInit(FMLPostInitializationEvent e) {}

    private static void clientPostInit(FMLPostInitializationEvent e) {}

    private static void commonPostInit(FMLPostInitializationEvent e) {}
}
