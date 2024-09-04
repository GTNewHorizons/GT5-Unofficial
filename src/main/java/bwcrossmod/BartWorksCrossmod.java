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

package bwcrossmod;

import static gregtech.api.enums.Mods.GalacticraftCore;

import java.io.StringReader;

import net.minecraft.util.StringTranslate;

import org.apache.commons.io.input.ReaderInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bwcrossmod.GTpp.loader.RadioHatchCompat;
import bwcrossmod.galacticraft.GalacticraftProxy;
import bwcrossmod.tectech.TecTechResearchLoader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import gregtech.GT_Version;

@Mod(
    modid = BartWorksCrossmod.MOD_ID,
    name = BartWorksCrossmod.NAME,
    version = BartWorksCrossmod.VERSION,
    dependencies = """
        required-after:IC2;\
        required-after:gregtech;\
        required-after:bartworks;\
        after:GalacticraftMars;\
        after:GalacticraftCore;\
        after:Micdoodlecore;\
        after:miscutils;\
        after:EMT;\
        after:tectech;""")
public class BartWorksCrossmod {

    public static final String NAME = "BartWorks Mod Additions";
    public static final String VERSION = GT_Version.VERSION;
    public static final String MOD_ID = "bartworkscrossmod";
    public static final Logger LOGGER = LogManager.getLogger(BartWorksCrossmod.NAME);

    @Mod.Instance(BartWorksCrossmod.MOD_ID)
    public static BartWorksCrossmod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {
        if (GalacticraftCore.isModLoaded()) {
            GalacticraftProxy.preInit(preinit);
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (GalacticraftCore.isModLoaded()) {
            GalacticraftProxy.init(init);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent init) {
        if (GalacticraftCore.isModLoaded()) {
            GalacticraftProxy.postInit(init);
        }
        RadioHatchCompat.run();
        TecTechResearchLoader.runResearches();
    }

    @Mod.EventHandler
    public void onFMLServerStart(FMLServerStartingEvent event) {
        for (Object s : RadioHatchCompat.TranslateSet) {
            StringTranslate.inject(new ReaderInputStream(new StringReader((String) s)));
        }
    }
}
