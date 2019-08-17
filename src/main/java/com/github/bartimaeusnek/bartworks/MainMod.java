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


import com.github.bartimaeusnek.bartworks.API.API_REFERENCE;
import com.github.bartimaeusnek.bartworks.API.BioObjectAdder;
import com.github.bartimaeusnek.bartworks.client.ClientEventHandler.TooltipEventHandler;
import com.github.bartimaeusnek.bartworks.client.creativetabs.BioTab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.GT2Tab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.bartworksTab;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.*;
import com.github.bartimaeusnek.bartworks.common.net.BW_Network;
import com.github.bartimaeusnek.bartworks.server.EventHandler.ServerEventHandler;
import com.github.bartimaeusnek.bartworks.system.log.DebugLog;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitPartLoader;
import com.github.bartimaeusnek.bartworks.system.material.ThreadedLoader;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.processingLoaders.DownTierLoader;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ElectricImplosionCompressor.eicMap;

@Mod(
        modid = MainMod.MOD_ID, name = MainMod.NAME, version = MainMod.VERSION,
        dependencies = "required-after:IC2; "
                + "required-after:gregtech; "
                + "after:berriespp; "
                + "after:GalacticraftMars; "
                + "after:GalacticraftCore; "
    )
public final class MainMod {
    public static final String NAME = "BartWorks";
    public static final String VERSION = "@version@";
    public static final String MOD_ID = "bartworks";
    public static final String APIVERSION = "@apiversion@";
    public static final Logger LOGGER = LogManager.getLogger(MainMod.NAME);
    public static final CreativeTabs GT2 = new GT2Tab("GT2C");
    public static final CreativeTabs BIO_TAB = new BioTab("BioTab");
    public static final CreativeTabs BWT = new bartworksTab("bartworks");
    public static final IGuiHandler GH = new GuiHandler();

    @Mod.Instance(MainMod.MOD_ID)
    public static MainMod instance;
    public static BW_Network BW_Network_instance = new BW_Network();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {

        if (!(API_REFERENCE.VERSION.equals(MainMod.APIVERSION))) {
            MainMod.LOGGER.error("Something has loaded an old API. Please contact the Mod authors to update!");
        }

        //fixing BorosilicateGlass... -_-'
        Materials.BorosilicateGlass.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_RECYCLING, SubTag.SMELTING_TO_FLUID);

        if (Loader.isModLoaded("dreamcraft")) {
            ConfigHandler.GTNH = true;
        }
        ConfigHandler.GTNH = ConfigHandler.ezmode != ConfigHandler.GTNH;
        if (ConfigHandler.debugLog) {
            try {
                new DebugLog(preinit);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (ConfigHandler.GTNH)
            MainMod.LOGGER.info("GTNH-Detected . . . ACTIVATE HARDMODE.");

        if (ConfigHandler.BioLab) {
            BioCultureLoader bioCultureLoader = new BioCultureLoader();
            bioCultureLoader.run();
        }
        if (ConfigHandler.newStuff) {
            WerkstoffLoader.INSTANCE.init();
            Werkstoff.init();
            GregTech_API.sAfterGTPostload.add(new CircuitPartLoader());
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (FMLCommonHandler.instance().getSide().isClient() && ConfigHandler.tooltips)
            MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
        if (FMLCommonHandler.instance().getSide().isServer()) {
            ServerEventHandler serverEventHandler = new ServerEventHandler();
            MinecraftForge.EVENT_BUS.register(serverEventHandler);
//            FMLCommonHandler.instance().bus().register(serverEventHandler);
        }
        new LoaderRegistry().run();
        if (ConfigHandler.BioLab)
            new BioLabLoader().run();
        if (ConfigHandler.newStuff) {
            if (ConfigHandler.experimentalThreadedLoader)
                new ThreadedLoader().runInit();
            else
                WerkstoffLoader.INSTANCE.runInit();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postinit) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainMod.instance, MainMod.GH);
        if (ConfigHandler.BioLab)
            new GTNHBlocks().run();
        BioObjectAdder.regenerateBioFluids();
        if (ConfigHandler.newStuff) {
            if (ConfigHandler.experimentalThreadedLoader)
                new ThreadedLoader().run();
            else
                WerkstoffLoader.INSTANCE.run();
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.electricimplosioncompressor","Electric Implosions?");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.electricimplosioncompressor.desc","Basically a giant Hammer that presses Stuff - No more TNT!");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.DEHP","Heat from below!");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.DEHP.desc","Get ALL the thermal energy!");
        }
    }
    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        MainMod.runOnPlayerJoined(ConfigHandler.classicMode);
    }

    public static void runOnPlayerJoined(boolean classicMode){
        OreDictHandler.adaptCacheForWorld();
        WerkstoffLoader.removeIC2Recipes();
        MainMod.addElectricImplosionCompressorRecipes();
        new CircuitImprintLoader().run();
        if (classicMode)
            new DownTierLoader().run();
    }

    private static void addElectricImplosionCompressorRecipes() {
        if (eicMap == null) {
            eicMap = new GT_Recipe.GT_Recipe_Map(new HashSet<GT_Recipe>(GT_Recipe.GT_Recipe_Map.sImplosionRecipes.mRecipeList.size()), "gt.recipe.electricimplosioncompressor", "Electric Implosion Compressor", (String) null, "gregtech:textures/gui/basicmachines/Default", 1, 2, 1, 0, 1, "", 1, "", true, true);
            for (GT_Recipe recipe : GT_Recipe.GT_Recipe_Map.sImplosionRecipes.mRecipeList) {
                if (recipe == null || recipe.mInputs == null)
                    continue;
                HashSet<ItemStack> inputs = new HashSet<>();
                for (ItemStack is : recipe.mInputs)
                    if (!MainMod.checkForExplosives(is))
                        inputs.add(is);
                eicMap.addRecipe(true, inputs.toArray(new ItemStack[0]), recipe.mOutputs, null, null, null, recipe.mDuration, BW_Util.getMachineVoltageFromTier(10), 0);
            }
        }
    }

    private static boolean checkForExplosives(ItemStack input) {
        return (GT_Utility.areStacksEqual(input, new ItemStack(Blocks.tnt)) || GT_Utility.areStacksEqual(input, GT_ModHandler.getIC2Item("industrialTnt", 1L)) || GT_Utility.areStacksEqual(input, GT_ModHandler.getIC2Item("dynamite", 1L)) || GT_Utility.areStacksEqual(input, ItemList.Block_Powderbarrel.get(1L)));
    }

}
