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

package com.github.bartimaeusnek.bartworks;


import com.github.bartimaeusnek.bartworks.API.*;
import com.github.bartimaeusnek.bartworks.client.ClientEventHandler.TooltipEventHandler;
import com.github.bartimaeusnek.bartworks.client.creativetabs.BioTab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.GT2Tab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.bartworksTab;
import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.*;
import com.github.bartimaeusnek.bartworks.common.net.BW_Network;
import com.github.bartimaeusnek.bartworks.server.EventHandler.ServerEventHandler;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitPartLoader;
import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.PlatinumSludgeOverHaul;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.material.processingLoaders.DownTierLoader;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.log.DebugLog;
import com.github.bartimaeusnek.bartworks.util.log.STFUGTPPLOG;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static com.github.bartimaeusnek.bartworks.common.loaders.BioRecipeLoader.runOnServerStarted;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.removeIC2Recipes;
import static gregtech.api.enums.GT_Values.VN;

@SuppressWarnings("ALL")
@Mod(
        modid = MainMod.MOD_ID, name = MainMod.NAME, version = MainMod.VERSION,
        dependencies = "required-after:IC2; "
                + "required-after:gregtech; "
                + "after:berriespp; "
                + "after:GalacticraftMars; "
                + "after:GalacticraftCore; "
                + "after:Forestry; "
                + "before:miscutils; "
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

    @SideOnly(Side.CLIENT)
    private void ClientGTppWarning() {
        javax.swing.JOptionPane.showMessageDialog(null,
                "BartWorks was NOT meant to be played with GT++," +
                        " since GT++'s Multiblocks break the Platinum Processing chain. " +
                        "Feel free to continue, but be aware of this.","GT++ Warning", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {

        if (!(API_REFERENCE.VERSION.equals(MainMod.APIVERSION))) {
            MainMod.LOGGER.error("Something has loaded an old API. Please contact the Mod authors to update!");
        }

        LoaderReference.init(); //Check for ALL the mods.

        if (LoaderReference.miscutils) {
            //if (SideReference.Side.Client)
                //ClientGTppWarning();

            MainMod.LOGGER.error("BartWorks was NOT meant to be played with GT++," +
                        " since GT++'s Multiblocks break the Platinum Processing chain. " +
                        "Feel free to continue, but be aware of this.");
        }

        if (LoaderReference.miscutils && ConfigHandler.GTppLogDisabler) {
            STFUGTPPLOG.replaceLogger();
        }

        if (LoaderReference.dreamcraft)
            ConfigHandler.hardmode = true;

        ConfigHandler.hardmode = ConfigHandler.ezmode != ConfigHandler.hardmode;

        if (API_ConfigValues.debugLog) {
            try {
                DebugLog.initDebugLog(preinit);
            } catch (IOException e) {
                MainMod.LOGGER.catching(e);
            }
        }

        if (ConfigHandler.newStuff) {
            WerkstoffLoader.setUp();
        }

        if (ConfigHandler.hardmode)
            MainMod.LOGGER.info(". . . ACTIVATED HARDMODE.");

        if (ConfigHandler.BioLab) {
            BioCultureLoader.run();
        }

        if (ConfigHandler.newStuff) {
            Werkstoff.init();
            GregTech_API.sAfterGTPostload.add(new CircuitPartLoader());
            if (SideReference.Side.Client)
                GregTech_API.sBeforeGTLoad.add(new PrefixTextureLinker());
        }

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (SideReference.Side.Client && ConfigHandler.tooltips)
            MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
        ServerEventHandler serverEventHandler = new ServerEventHandler();
        if (SideReference.Side.Server) {
            MinecraftForge.EVENT_BUS.register(serverEventHandler);
        }
        FMLCommonHandler.instance().bus().register(serverEventHandler);
        if (ConfigHandler.BioLab)
            BioLabLoader.run();
        if (ConfigHandler.newStuff) {
            WerkstoffLoader.runInit();
        }
        ItemRegistry.run();
        RecipeLoader.run();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postinit) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainMod.instance, MainMod.GH);
        if (ConfigHandler.BioLab) {
            GTNHBlocks.run();
            for (Map.Entry<BioVatLogicAdder.BlockMetaPair, Byte> pair : BioVatLogicAdder.BioVatGlass.getGlassMap().entrySet()) {
                GT_OreDictUnificator.registerOre("blockGlass" + VN[pair.getValue()], new ItemStack(pair.getKey().getBlock(), 1, pair.getKey().getaByte()));
            }
        }

        BioObjectAdder.regenerateBioFluids();
        if (ConfigHandler.newStuff) {
            WerkstoffLoader.run();
            LocalisationLoader.localiseAll();
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        RegisterServerCommands.registerAll(event);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        MainMod.runOnPlayerJoined(ConfigHandler.classicMode, ConfigHandler.disableExtraGassesForEBF);
    }

    @Mod.EventHandler
    public void onModLoadingComplete(FMLLoadCompleteEvent event) {
        removeIC2Recipes();
        StaticRecipeChangeLoaders.addElectricImplosionCompressorRecipes();
        PlatinumSludgeOverHaul.replacePureElements();

        runOnServerStarted();
        StaticRecipeChangeLoaders.unificationRecipeEnforcer();
    }

    private static boolean recipesAdded;

    public static void runOnPlayerJoined(boolean classicMode, boolean disableExtraGasRecipes) {
        OreDictHandler.adaptCacheForWorld();
        CircuitImprintLoader.run();
        BioVatLogicAdder.RadioHatch.runBasicItemIntegration();
        if (!recipesAdded) {
            if (!disableExtraGasRecipes)
                StaticRecipeChangeLoaders.addEBFGasRecipes();

            if (classicMode)
                DownTierLoader.run();

            //StaticRecipeChangeLoaders.patchEBFMapForCircuitUnification();

            recipesAdded = true;
        }

        StaticRecipeChangeLoaders.fixEnergyRequirements();
        //StaticRecipeChangeLoaders.synchroniseCircuitUseMulti();

        //Accept recipe map changes into Buffers
        GT_Recipe.GT_Recipe_Map.sMappings.forEach(GT_Recipe.GT_Recipe_Map::reInit);
    }

}
