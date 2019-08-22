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
import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioLabLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.GTNHBlocks;
import com.github.bartimaeusnek.bartworks.common.loaders.LoaderRegistry;
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
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ElectricImplosionCompressor.eicMap;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;

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
            INSTANCE.init();
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
                INSTANCE.runInit();
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
                INSTANCE.run();
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.electricimplosioncompressor","Electric Implosions?");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.electricimplosioncompressor.desc","Basically a giant Hammer that presses Stuff - No more TNT!");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.DEHP","Heat from below!");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.DEHP.desc","Get ALL the thermal energy!");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.CircuitAssemblyLine","Cheaper Circuits?");
            GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.CircuitAssemblyLine.desc","Well, yes, but actually no...");
        }
    }
    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        MainMod.runOnPlayerJoined(ConfigHandler.classicMode);
    }

    public static void runOnPlayerJoined(boolean classicMode){
        OreDictHandler.adaptCacheForWorld();
        removeIC2Recipes();
        MainMod.addElectricImplosionCompressorRecipes();
        MainMod.unificationEnforcer();

        ArrayListMultimap tochange = MainMod.getRecipesToChange(NOBLE_GAS,ANAEROBE_GAS);
        HashSet noGas = MainMod.getNoGasItems(tochange);
        MainMod.editRecipes(tochange,noGas);

        new CircuitImprintLoader().run();
        if (classicMode)
            new DownTierLoader().run();
    }

    private static void unificationEnforcer() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashMap.values()) {
            if (werkstoff.getGenerationFeatures().enforceUnification) {
                if (werkstoff.contains(NOBLE_GAS)){
                        String name = werkstoff.getFluidOrGas(1).getFluid().getName();
                        String wrongname ="molten."+name;
                        FluidStack wrongNamedFluid = FluidRegistry.getFluidStack(wrongname,1);
                        for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
                            for (GT_Recipe recipe : map.mRecipeList) {
                                for (int i = 0; i < recipe.mFluidInputs.length; i++) {
                                    if (GT_Utility.areFluidsEqual(recipe.mFluidInputs[i], wrongNamedFluid)) {
                                        recipe.mFluidInputs[i] = werkstoff.getFluidOrGas(recipe.mFluidInputs[i].amount);
                                    }
                                }
                                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                                    if (GT_Utility.areFluidsEqual(recipe.mFluidOutputs[i], wrongNamedFluid)) {
                                        recipe.mFluidOutputs[i] = werkstoff.getFluidOrGas(recipe.mFluidOutputs[i].amount);
                                    }
                                }
                            }
                        }
                }
                MainMod.runMoltenUnificationEnfocement(werkstoff);
                for (OrePrefixes prefixes : OrePrefixes.values()) {
                    if (OreDictionary.getOres(prefixes + werkstoff.getDefaultName()).size() > 1) {
                        for (int j = 0; j < OreDictionary.getOres(prefixes + werkstoff.getDefaultName()).size(); j++) {
                            ItemStack toReplace = OreDictionary.getOres(prefixes + werkstoff.getDefaultName()).get(j);
                            ItemStack replacement = werkstoff.get(prefixes);
                            if (GT_Utility.areStacksEqual(toReplace,replacement) || replacement == null || replacement.getItem() == null)
                                continue;
                            if (toReplace != null) {
                                for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
                                    HashSet<GT_Recipe> toRem = new HashSet<>();
                                    for (GT_Recipe recipe : map.mRecipeList) {
                                        boolean removal = map.equals(GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes) || map.equals(GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes);
                                        for (int i = 0; i < recipe.mInputs.length; i++) {
                                            if (GT_Utility.areStacksEqual(recipe.mInputs[i], toReplace)) {
                                                if (removal)
                                                    toRem.add(recipe);
                                                else
                                                    recipe.mInputs[i] = replacement;
                                            }
                                        }
                                        for (int i = 0; i < recipe.mOutputs.length; i++) {
                                            if (GT_Utility.areStacksEqual(recipe.mOutputs[i], toReplace)) {
                                                if (removal)
                                                    toRem.add(recipe);
                                                else
                                                    recipe.mOutputs[i] = replacement;
                                            }
                                        }
                                        if (recipe.mSpecialItems instanceof ItemStack) {
                                            if (GT_Utility.areStacksEqual((ItemStack) recipe.mSpecialItems, toReplace)) {
                                                if (removal)
                                                    toRem.add(recipe);
                                                else
                                                    recipe.mSpecialItems = replacement;
                                            }
                                        }
                                    }
                                    map.mRecipeList.removeAll(toRem);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void runMoltenUnificationEnfocement(Werkstoff werkstoff){
        if (werkstoff.getGenerationFeatures().enforceUnification && werkstoff.getGenerationFeatures().hasMolten()) {
            try {
                FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(new FluidStack(molten.get(werkstoff), 144), werkstoff.get(cellMolten), Materials.Empty.getCells(1));
                Field f = GT_Utility.class.getDeclaredField("sFilledContainerToData");
                f.setAccessible(true);
                Map<GT_ItemStack, FluidContainerRegistry.FluidContainerData> sFilledContainerToData = (Map<GT_ItemStack, FluidContainerRegistry.FluidContainerData>) f.get(null);
                HashSet torem = new HashSet<>();
                ItemStack toReplace = null;
                for (Map.Entry<GT_ItemStack, FluidContainerRegistry.FluidContainerData> entry : sFilledContainerToData.entrySet()) {
                    if (entry.getValue().fluid.equals(data.fluid) && !entry.getValue().filledContainer.equals(data.filledContainer)) {
                        toReplace = entry.getValue().filledContainer;
                        torem.add(entry);
                    }
                }
                sFilledContainerToData.entrySet().removeAll(torem);
                torem.clear();
                if (toReplace != null) {
                    for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
                        torem.clear();
                        for (GT_Recipe recipe : map.mRecipeList) {
                            for (int i = 0; i < recipe.mInputs.length; i++) {
                                if (GT_Utility.areStacksEqual(recipe.mInputs[i], toReplace)) {
                                    torem.add(recipe);
                                    // recipe.mInputs[i] = data.filledContainer;
                                }
                            }
                            for (int i = 0; i < recipe.mOutputs.length; i++) {
                                if (GT_Utility.areStacksEqual(recipe.mOutputs[i], toReplace)) {
                                    torem.add(recipe);
                                    // recipe.mOutputs[i] = data.filledContainer;
                                    if (map == GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes && GT_Utility.areStacksEqual(recipe.mOutputs[i], data.filledContainer) && !recipe.mFluidInputs[0].equals(data.fluid)) {
                                        torem.add(recipe);
                                        // recipe.mOutputs[i] = data.filledContainer;
                                    }
                                }
                            }
                            if (recipe.mSpecialItems instanceof ItemStack) {
                                if (GT_Utility.areStacksEqual((ItemStack) recipe.mSpecialItems, toReplace)) {
                                    torem.add(recipe);
                                    //  recipe.mSpecialItems = data.filledContainer;
                                }
                            }
                        }
                        map.mRecipeList.removeAll(torem);
                    }
                }
                GT_Utility.addFluidContainerData(data);
            } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayListMultimap<SubTag,GT_Recipe> getRecipesToChange(SubTag... GasTags){
        Iterator<GT_Recipe> it = GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.iterator();
        ArrayListMultimap<SubTag,GT_Recipe> toAdd = ArrayListMultimap.create();
        while (it.hasNext()) {
            GT_Recipe recipe = it.next();
            if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                String FluidString = recipe.mFluidInputs[0].getFluid().getName().replaceAll("molten", "").replaceAll("fluid", "");
                Materials mat = Materials.get(FluidString.substring(0, 1).toUpperCase() + FluidString.substring(1));
                if (mat != Materials._NULL) {
                    for (SubTag tag : GasTags){
                        if (mat.contains(tag))
                            toAdd.put(tag,recipe);
                    }
                }
            }
        }
        return toAdd;
    }

    private static HashSet<ItemStack> getNoGasItems(ArrayListMultimap<SubTag,GT_Recipe> base){
        Iterator<GT_Recipe> it = GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.iterator();
        HashSet<ItemStack> toAdd = new HashSet<>();
        ArrayListMultimap<SubTag,GT_Recipe> repToAdd = ArrayListMultimap.create();
        while (it.hasNext()) {
            GT_Recipe recipe = it.next();
            for (SubTag tag : base.keySet())
                recipeLoop:
                        for (GT_Recipe baseRe : base.get(tag)) {
                            if (recipe.mInputs.length == baseRe.mInputs.length && recipe.mOutputs.length == baseRe.mOutputs.length)
                                for (int i = 0; i < recipe.mInputs.length; i++) {
                                    if ((recipe.mFluidInputs == null || recipe.mFluidInputs.length == 0) && BW_Util.checkStackAndPrefix(recipe.mInputs[i]) && BW_Util.checkStackAndPrefix(baseRe.mInputs[i]) && GT_OreDictUnificator.getAssociation(recipe.mInputs[i]).mMaterial.mMaterial.equals(GT_OreDictUnificator.getAssociation(baseRe.mInputs[i]).mMaterial.mMaterial) && GT_Utility.areStacksEqual(recipe.mOutputs[0], recipe.mOutputs[0])) {
                                        toAdd.add(recipe.mOutputs[0]);
                                        repToAdd.put(tag,recipe);
                                        continue recipeLoop;
                                    }
                                }
                        }
        }
        base.putAll(repToAdd);
        return toAdd;
    }

    private static void editRecipes(ArrayListMultimap<SubTag,GT_Recipe> base, HashSet<ItemStack> noGas) {
        if (GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeFluidNameMap.contains(fluids.get(Oganesson).getName()))
            return;
        HashSet<GT_Recipe> toAdd = new HashSet<>();
        for (SubTag GasTag : base.keySet()) {
            for (GT_Recipe recipe : base.get(GasTag)) {
                if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                    String materialString = recipe.mFluidInputs[0].getFluid().getName().replaceAll("molten", "").replaceAll("fluid", "");
                    Materials mat = Materials.get(materialString.substring(0, 1).toUpperCase() + materialString.substring(1));
                    if (mat != Materials._NULL) {
                        for (Werkstoff werkstoff : Werkstoff.werkstoffHashMap.values()) {
                            if (!werkstoff.contains(GasTag))
                                continue;
                            toAdd.add(new BWRecipes.DynamicGTRecipe(false, recipe.mInputs, recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, new FluidStack[]{new FluidStack(fluids.get(werkstoff), recipe.mFluidInputs[0].amount)}, recipe.mFluidOutputs, (int) ((double) recipe.mDuration / 200D * (200D + (double) mat.getProtons() - (double) werkstoff.getStats().getProtons())), recipe.mEUt, recipe.mSpecialValue));
                        }
                        for (Materials materials : Materials.values()) {
                            if (!materials.contains(GasTag))
                                continue;
                            toAdd.add(new BWRecipes.DynamicGTRecipe(false, recipe.mInputs, recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, new FluidStack[]{materials.getGas(recipe.mFluidInputs[0].amount)}, recipe.mFluidOutputs, (int) ((double) recipe.mDuration / 200D * (200D + (double) mat.getProtons() - (double) materials.getProtons())), recipe.mEUt, recipe.mSpecialValue));
                        }
                        for (ItemStack is : noGas)
                            if (GT_Utility.areStacksEqual(is, recipe.mOutputs[0])) {
                                toAdd.add(new BWRecipes.DynamicGTRecipe(false, recipe.mInputs, recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, null, recipe.mFluidOutputs, (int) ((double) recipe.mDuration / 200D * (200D + (double) mat.getProtons())), recipe.mEUt, recipe.mSpecialValue));
                                break;
                            }
                    }
                }
            }
            GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.removeAll(base.get(GasTag));
        }
        for (GT_Recipe recipe : toAdd)
            GT_Recipe.GT_Recipe_Map.sBlastRecipes.add(recipe);
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
