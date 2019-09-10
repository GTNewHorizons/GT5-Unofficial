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

package com.github.bartimaeusnek.crossmod;

import com.github.bartimaeusnek.bartworks.common.commands.ChangeConfig;
import com.github.bartimaeusnek.bartworks.common.commands.SummonRuin;
import com.github.bartimaeusnek.crossmod.GTpp.loader.RadioHatchCompat;
import com.github.bartimaeusnek.crossmod.galacticraft.GalacticraftProxy;
import com.github.bartimaeusnek.crossmod.thaumcraft.CustomAspects;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.util.StringTranslate;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.StringReader;

@Mod(
        modid = BartWorksCrossmod.MOD_ID, name = BartWorksCrossmod.NAME, version = BartWorksCrossmod.VERSION,
        dependencies = "required-after:IC2; "
                + "required-after:gregtech; "
                + "required-after:bartworks;"
                + "after:GalacticraftMars; "
                + "after:GalacticraftCore; "
                + "after:Micdoodlecore; "
                + "after:miscutils;"
                + "after:EMT;"
)
public class BartWorksCrossmod {
    public static final String NAME = "BartWorks Mod Additions";
    public static final String VERSION = "0.0.1";
    public static final String MOD_ID = "bartworkscrossmod";
    public static final Logger LOGGER = LogManager.getLogger(BartWorksCrossmod.NAME);

    @Mod.Instance(BartWorksCrossmod.MOD_ID)
    public static BartWorksCrossmod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {
        if (Loader.isModLoaded("GalacticraftCore"))
            GalacticraftProxy.preInit(preinit);
        if (Loader.isModLoaded("Thaumcraft"))
            new CustomAspects();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (Loader.isModLoaded("GalacticraftCore"))
            GalacticraftProxy.init(init);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent init) {
        if (Loader.isModLoaded("GalacticraftCore"))
            GalacticraftProxy.postInit(init);
        if (Loader.isModLoaded("miscutils"))
            new RadioHatchCompat().run();
    }

//    @Mod.EventHandler
//    public void onFMLMissingMappingsEvent(FMLMissingMappingsEvent event){
//        for (FMLMissingMappingsEvent.MissingMapping mapping : event.getAll()){
//            if (mapping.name.equalsIgnoreCase())
//        }
//    }


//    @Mod.EventHandler
//    public void onServerStarted(FMLServerStartedEvent event) {
//        if (Loader.isModLoaded("EMT")){
//            try {
//                TCRecipeHandler.init();
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Mod.EventHandler
    public void onFMLServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new SummonRuin());
        event.registerServerCommand(new ChangeConfig());
        if (Loader.isModLoaded("miscutils"))
            for (Object s : RadioHatchCompat.TranslateSet){
                StringTranslate.inject(new ReaderInputStream(new StringReader((String) s)));
            }
    }
}
