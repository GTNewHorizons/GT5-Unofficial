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

package bartworks;

import static bartworks.common.loaders.BioRecipeLoader.runOnServerStarted;
import static bartworks.system.material.WerkstoffLoader.removeIC2Recipes;
import static gregtech.api.enums.Mods.BartWorks;

import java.io.IOException;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bartworks.API.APIConfigValues;
import bartworks.API.BioObjectAdder;
import bartworks.API.BioVatLogicAdder;
import bartworks.API.SideReference;
import bartworks.client.ClientEventHandler.TooltipEventHandler;
import bartworks.client.creativetabs.BartWorksTab;
import bartworks.client.creativetabs.BioTab;
import bartworks.client.creativetabs.GT2Tab;
import bartworks.client.textures.PrefixTextureLinker;
import bartworks.common.configs.ConfigHandler;
import bartworks.common.items.BWItemBlocks;
import bartworks.common.loaders.ArtificialMicaLine;
import bartworks.common.loaders.BioCultureLoader;
import bartworks.common.loaders.BioLabLoader;
import bartworks.common.loaders.ItemRegistry;
import bartworks.common.loaders.LocalisationLoader;
import bartworks.common.loaders.RadioHatchMaterialLoader;
import bartworks.common.loaders.RecipeLoader;
import bartworks.common.loaders.RegisterGlassTiers;
import bartworks.common.loaders.RegisterServerCommands;
import bartworks.common.loaders.StaticRecipeChangeLoaders;
import bartworks.common.net.BWNetwork;
import bartworks.server.EventHandler.ServerEventHandler;
import bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import bartworks.system.material.CircuitGeneration.CircuitPartLoader;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.gtenhancement.PlatinumSludgeOverHaul;
import bartworks.system.material.processingLoaders.DownTierLoader;
import bartworks.system.oredict.OreDictHandler;
import bartworks.util.ResultWrongSievert;
import bartworks.util.log.DebugLog;
import bwcrossmod.galacticgreg.VoidMinerUtility;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Version;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Mods;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

@Mod(modid = MainMod.MOD_ID, name = MainMod.NAME, version = GT_Version.VERSION, dependencies = """
    required-after:IC2;\
    required-after:gregtech;\
    after:berriespp;\
    after:tectech;\
    after:GalacticraftMars;\
    after:GalacticraftCore;\
    after:Forestry;\
    after:ProjRed|Illumination;\
    after:RandomThings;\
    before:miscutils;""")
public final class MainMod {

    public static final String NAME = "BartWorks";
    public static final String MOD_ID = Mods.Names.BART_WORKS;
    public static final String APIVERSION = "11";
    public static final Logger LOGGER = LogManager.getLogger(MainMod.NAME);
    public static final CreativeTabs GT2 = new GT2Tab("GT2C");
    public static final CreativeTabs BIO_TAB = new BioTab("BioTab");
    public static final CreativeTabs BWT = new BartWorksTab(BartWorks.ID);
    public static final IGuiHandler GH = new GuiHandler();

    @Mod.Instance(MainMod.MOD_ID)
    public static MainMod instance;

    public static BWNetwork BW_Network_instance = new BWNetwork();

    public MainMod() {

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {
        MainMod.LOGGER.info("Found GT++, continuing");

        GameRegistry.registerBlock(ItemRegistry.bw_glasses[0], BWItemBlocks.class, "BW_GlasBlocks");
        GameRegistry.registerBlock(ItemRegistry.bw_glasses[1], BWItemBlocks.class, "BW_GlasBlocks2");

        if (APIConfigValues.debugLog) {
            try {
                DebugLog.initDebugLog(preinit);
            } catch (IOException e) {
                MainMod.LOGGER.catching(e);
            }
        }

        WerkstoffLoader.setUp();

        BioCultureLoader.run();

        Werkstoff.init();
        GregTechAPI.sAfterGTPostload.add(new CircuitPartLoader());
        if (SideReference.Side.Client) {
            GregTechAPI.sBeforeGTLoad.add(new PrefixTextureLinker());
        }

        RegisterGlassTiers.run();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (SideReference.Side.Client && ConfigHandler.tooltips)
            MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
        ServerEventHandler serverEventHandler = new ServerEventHandler();
        if (SideReference.Side.Server) {
            MinecraftForge.EVENT_BUS.register(serverEventHandler);
        }
        FMLCommonHandler.instance()
            .bus()
            .register(serverEventHandler);
        BioLabLoader.run();

        WerkstoffLoader.runInit();

        ItemRegistry.run();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postinit) {

        RecipeLoader.run();

        NetworkRegistry.INSTANCE.registerGuiHandler(MainMod.instance, MainMod.GH);

        ArtificialMicaLine.runArtificialMicaRecipe();
        BioObjectAdder.regenerateBioFluids();

        WerkstoffLoader.run();
        LocalisationLoader.localiseAll();

        CheckRecipeResultRegistry.register(new ResultWrongSievert(0, ResultWrongSievert.NeededSievertType.EXACTLY));

        RadioHatchMaterialLoader.run();
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
        VoidMinerUtility.generateDropMaps();
    }

    private static boolean recipesAdded;

    public static void runOnPlayerJoined(boolean classicMode, boolean disableExtraGasRecipes) {
        OreDictHandler.adaptCacheForWorld();
        CircuitImprintLoader.run();
        BioVatLogicAdder.RadioHatch.runBasicItemIntegration();
        if (!recipesAdded) {
            if (!disableExtraGasRecipes) StaticRecipeChangeLoaders.addEBFGasRecipes();

            if (classicMode) DownTierLoader.run();

            recipesAdded = true;
        }

        // Accept recipe map changes into Buffers
        RecipeMap.ALL_RECIPE_MAPS.values()
            .forEach(
                map -> map.getBackend()
                    .reInit());
    }
}
