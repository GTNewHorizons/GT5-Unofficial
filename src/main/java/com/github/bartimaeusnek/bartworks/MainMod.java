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


import com.github.bartimaeusnek.bartworks.API.API_REFERENCE;
import com.github.bartimaeusnek.bartworks.API.BioObjectAdder;
import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.client.ClientEventHandler.TooltipEventHandler;
import com.github.bartimaeusnek.bartworks.client.creativetabs.BioTab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.GT2Tab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.bartworksTab;
import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import com.github.bartimaeusnek.bartworks.common.commands.ChangeConfig;
import com.github.bartimaeusnek.bartworks.common.commands.ClearCraftingCache;
import com.github.bartimaeusnek.bartworks.common.commands.PrintRecipeListToFile;
import com.github.bartimaeusnek.bartworks.common.commands.SummonRuin;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.loaders.BioCultureLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.BioLabLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.GTNHBlocks;
import com.github.bartimaeusnek.bartworks.common.loaders.LoaderRegistry;
import com.github.bartimaeusnek.bartworks.common.net.BW_Network;
import com.github.bartimaeusnek.bartworks.server.EventHandler.ServerEventHandler;
import com.github.bartimaeusnek.bartworks.system.log.DebugLog;
import com.github.bartimaeusnek.bartworks.system.log.STFUGTPPLOG;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitImprintLoader;
import com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration.CircuitPartLoader;
import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.PlatinumSludgeOverHaul;
import com.github.bartimaeusnek.bartworks.system.material.ThreadedLoader;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.processingLoaders.DownTierLoader;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Util;
import com.github.bartimaeusnek.bartworks.util.StreamUtils;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
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
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static com.github.bartimaeusnek.bartworks.common.loaders.BioRecipeLoader.runOnServerStarted;
import static com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_ElectricImplosionCompressor.eicMap;
import static com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader.*;
import static gregtech.api.enums.GT_Values.VN;

@Mod(
        modid = MainMod.MOD_ID, name = MainMod.NAME, version = MainMod.VERSION,
        dependencies = "required-after:IC2; "
                + "required-after:gregtech; "
                + "after:berriespp; "
                + "after:GalacticraftMars; "
                + "after:GalacticraftCore; "
                + "after:Forestry; "
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
    @SuppressWarnings("ALL")
    public void preInit(FMLPreInitializationEvent preinit) {

        if (!(API_REFERENCE.VERSION.equals(MainMod.APIVERSION))) {
            MainMod.LOGGER.error("Something has loaded an old API. Please contact the Mod authors to update!");
        }

        if (Loader.isModLoaded("miscutils") && ConfigHandler.GTppLogDisabler) {
            try {
                Field loggerField = FieldUtils.getField(Class.forName("gtPlusPlus.api.objects.Logger"), "modLogger", true);
                FieldUtils.removeFinalModifier(loggerField, true);
                loggerField.set(null, (Logger) new STFUGTPPLOG());
            } catch (IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (Loader.isModLoaded("dreamcraft"))
            ConfigHandler.GTNH = true;

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
            if (FMLCommonHandler.instance().getSide().isClient())
                new PrefixTextureLinker();
        }

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
        if (FMLCommonHandler.instance().getSide().isClient() && ConfigHandler.tooltips)
            MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
        ServerEventHandler serverEventHandler = new ServerEventHandler();
        if (FMLCommonHandler.instance().getSide().isServer()) {
            MinecraftForge.EVENT_BUS.register(serverEventHandler);
        }
        FMLCommonHandler.instance().bus().register(serverEventHandler);
        if (ConfigHandler.BioLab)
            new BioLabLoader().run();
        if (ConfigHandler.newStuff) {
            if (ConfigHandler.experimentalThreadedLoader)
                new ThreadedLoader().runInit();
            else
                INSTANCE.runInit();
        }
        new LoaderRegistry().run();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postinit) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MainMod.instance, MainMod.GH);
        if (ConfigHandler.BioLab) {
            new GTNHBlocks().run();
            for (Map.Entry<BioVatLogicAdder.BlockMetaPair, Byte> pair : BioVatLogicAdder.BioVatGlass.getGlassMap().entrySet()) {
                GT_OreDictUnificator.registerOre("blockGlass" + VN[pair.getValue()], new ItemStack(pair.getKey().getBlock(), 1, pair.getKey().getaByte()));
            }
        }

        BioObjectAdder.regenerateBioFluids();
        if (ConfigHandler.newStuff) {
            if (ConfigHandler.experimentalThreadedLoader)
                new ThreadedLoader().run();
            else
                INSTANCE.run();
            localiseAchievements();
        }
    }

    private static void localiseAchievements(){
        GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.electricimplosioncompressor", "Electric Implosions?");
        GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.electricimplosioncompressor.desc", "Basically a giant Hammer that presses Stuff - No more TNT!");
        GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.dehp", "Heat from below!");
        GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.dehp.desc", "Get ALL the thermal energy!");
        GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.circuitassemblyline", "Cheaper Circuits?");
        GT_LanguageManager.addStringLocalization("achievement.gt.blockmachines.circuitassemblyline.desc", "Well, yes, but actually no...");
        GT_LanguageManager.addStringLocalization("metaitem.01.tooltip.nqgen", "Can be used as Enriched Naquadah Fuel Substitute");
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new SummonRuin());
        event.registerServerCommand(new ChangeConfig());
        event.registerServerCommand(new PrintRecipeListToFile());
        event.registerServerCommand(new ClearCraftingCache());
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        MainMod.runOnPlayerJoined(ConfigHandler.classicMode, ConfigHandler.disableExtraGassesForEBF);
    }

    @Mod.EventHandler
    public void onModLoadingComplete(FMLLoadCompleteEvent event) {
        removeIC2Recipes();
        MainMod.addElectricImplosionCompressorRecipes();
        PlatinumSludgeOverHaul.replacePureElements();

        runOnServerStarted();
        MainMod.unificationRecipeEnforcer();
    }

    private static boolean recipesAdded;

    public static void runOnPlayerJoined(boolean classicMode, boolean extraGasRecipes) {
        OreDictHandler.adaptCacheForWorld();
        CircuitImprintLoader.run();

        if (!recipesAdded) {
            if (!extraGasRecipes) {
                ArrayListMultimap<SubTag, GT_Recipe> toChange = MainMod.getRecipesToChange(NOBLE_GAS, ANAEROBE_GAS);
                HashSet<ItemStack> noGas = MainMod.getNoGasItems(toChange);
                MainMod.editRecipes(toChange, noGas);
            }
            if (classicMode)
                new DownTierLoader().run();
//          removeDuplicateRecipes();
            recipesAdded = true;
        }

        fixEnergyRequirements();

        //Accept recipe map changes into Buffers
        GT_Recipe.GT_Recipe_Map.sMappings.forEach(GT_Recipe.GT_Recipe_Map::reInit);
    }

    private static void fixEnergyRequirements() {
        GT_Recipe.GT_Recipe_Map.sMappings.stream()
                .filter(StreamUtils::filterVisualMaps)
                .forEach(gt_recipe_map ->
                        gt_recipe_map.mRecipeList.parallelStream().forEach( gt_recipe -> {
                    for (int i = 0; i < (VN.length - 1); i++) {
                        if (gt_recipe.mEUt > BW_Util.getMachineVoltageFromTier(i) && gt_recipe.mEUt <= BW_Util.getTierVoltage(i)) {
                            gt_recipe.mEUt = BW_Util.getMachineVoltageFromTier(i);
                        }
                    }
                }));
    }

    private static void unificationRecipeEnforcer() {
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (werkstoff.getGenerationFeatures().enforceUnification) {
                if (werkstoff.contains(NOBLE_GAS)) {
                    String name = werkstoff.getFluidOrGas(1).getFluid().getName();
                    String wrongname = "molten." + name;
                    FluidStack wrongNamedFluid = FluidRegistry.getFluidStack(wrongname, 1);
                    if (wrongNamedFluid != null) {
                        for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
                            for (GT_Recipe recipe : map.mRecipeList) {
                                for (int i = 0; i < recipe.mFluidInputs.length; i++) {
                                    if (GT_Utility.areFluidsEqual(recipe.mFluidInputs[i], wrongNamedFluid)) {
                                        Collection<GT_Recipe> col = map.mRecipeFluidMap.get(wrongNamedFluid.getFluid());
                                        map.mRecipeFluidMap.remove(wrongNamedFluid.getFluid());
                                        map.mRecipeFluidMap.put(werkstoff.getFluidOrGas(1).getFluid(), col);
                                        recipe.mFluidInputs[i] = werkstoff.getFluidOrGas(recipe.mFluidInputs[i].amount);
                                        map.mRecipeFluidNameMap.add(werkstoff.getFluidOrGas(1).getFluid().getName());
                                    }
                                }
                                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                                    if (GT_Utility.areFluidsEqual(recipe.mFluidOutputs[i], wrongNamedFluid)) {
                                        recipe.mFluidOutputs[i] = werkstoff.getFluidOrGas(recipe.mFluidOutputs[i].amount);
                                    }
                                }
                            }
                        }
                        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.add(new BWRecipes.DynamicGTRecipe(false, null, null, null, null, new FluidStack[]{wrongNamedFluid}, new FluidStack[]{werkstoff.getFluidOrGas(1)}, 1, 1, 0));
                    }
                }
                HashSet<String> oreDictNames = new HashSet<>(werkstoff.getADDITIONAL_OREDICT());
                oreDictNames.add(werkstoff.getVarName());
                MainMod.runMoltenUnificationEnfocement(werkstoff);
                MainMod.runUnficationDeleter(werkstoff);
                for (String s : oreDictNames)
                    for (OrePrefixes prefixes : OrePrefixes.values()) {
                        if (OreDictionary.getOres(prefixes + s).size() > 1) {
                            for (int j = 0; j < OreDictionary.getOres(prefixes + s).size(); j++) {
                                ItemStack toReplace = OreDictionary.getOres(prefixes + s).get(j);
                                ItemStack replacement = werkstoff.get(prefixes);
                                if (GT_Utility.areStacksEqual(toReplace, replacement) || replacement == null || replacement.getItem() == null)
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
                                                    else {
                                                        int amount = recipe.mInputs[i].stackSize;
                                                        recipe.mInputs[i] = replacement.splitStack(amount);
                                                    }
                                                }
                                            }
                                            for (int i = 0; i < recipe.mOutputs.length; i++) {
                                                if (GT_Utility.areStacksEqual(recipe.mOutputs[i], toReplace)) {
                                                    if (removal)
                                                        toRem.add(recipe);
                                                    else {
                                                        int amount = recipe.mOutputs[i].stackSize;
                                                        recipe.mOutputs[i] = replacement.splitStack(amount);
                                                    }
                                                }
                                            }
                                            if (recipe.mSpecialItems instanceof ItemStack) {
                                                if (GT_Utility.areStacksEqual((ItemStack) recipe.mSpecialItems, toReplace)) {
                                                    if (removal)
                                                        toRem.add(recipe);
                                                    else {
                                                        int amount = ((ItemStack) recipe.mSpecialItems).stackSize;
                                                        recipe.mSpecialItems = replacement.splitStack(amount);
                                                    }
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

    @SuppressWarnings("ALL")
    private static void runMoltenUnificationEnfocement(Werkstoff werkstoff) {
        if (werkstoff.getGenerationFeatures().enforceUnification && werkstoff.getGenerationFeatures().hasMolten()) {
            try {
                FluidContainerRegistry.FluidContainerData data = new FluidContainerRegistry.FluidContainerData(new FluidStack(Objects.requireNonNull(molten.get(werkstoff)), 144), werkstoff.get(cellMolten), Materials.Empty.getCells(1));
                Field f = GT_Utility.class.getDeclaredField("sFilledContainerToData");
                f.setAccessible(true);
                Map<GT_ItemStack, FluidContainerRegistry.FluidContainerData> sFilledContainerToData = (Map<GT_ItemStack, FluidContainerRegistry.FluidContainerData>) f.get(null);
                HashSet torem = new HashSet<>();
                ItemStack toReplace = null;
                for (Map.Entry<GT_ItemStack, FluidContainerRegistry.FluidContainerData> entry : sFilledContainerToData.entrySet()) {
                    final String MODID = GameRegistry.findUniqueIdentifierFor(data.filledContainer.getItem()).modId;
                    if (MODID.equals(MainMod.MOD_ID) || MODID.equals(BartWorksCrossmod.MOD_ID))
                        continue;
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

    private static void runUnficationDeleter(Werkstoff werkstoff) {
        if (werkstoff.getType() == Werkstoff.Types.ELEMENT) {
            if (werkstoff.getBridgeMaterial() != null) {
                werkstoff.getBridgeMaterial().mElement = Element.get(werkstoff.getToolTip());
                Element.get(werkstoff.getToolTip()).mLinkedMaterials = new ArrayList<>();
                Element.get(werkstoff.getToolTip()).mLinkedMaterials.add(werkstoff.getBridgeMaterial());
            }
        }

        for (OrePrefixes prefixes : OrePrefixes.values())
            if ((werkstoff.getGenerationFeatures().toGenerate & Werkstoff.GenerationFeatures.prefixLogic.get(prefixes)) != 0 && ((werkstoff.getGenerationFeatures().blacklist & Werkstoff.GenerationFeatures.prefixLogic.get(prefixes)) == 0)) {
                GT_OreDictUnificator.set(prefixes, werkstoff.getBridgeMaterial(), werkstoff.get(prefixes), true, true);
                for (ItemStack stack : OreDictionary.getOres(prefixes + werkstoff.getVarName())) {
                    GT_OreDictUnificator.addAssociation(prefixes, werkstoff.getBridgeMaterial(), stack, false);
                    GT_OreDictUnificator.getAssociation(stack).mUnificationTarget = werkstoff.get(prefixes);
                }
            }
    }

    private static ArrayListMultimap<SubTag, GT_Recipe> getRecipesToChange(SubTag... GasTags) {
        Iterator<GT_Recipe> it = GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.iterator();
        ArrayListMultimap<SubTag, GT_Recipe> toAdd = ArrayListMultimap.create();
        while (it.hasNext()) {
            GT_Recipe recipe = it.next();
            if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
                String FluidString = recipe.mFluidInputs[0].getFluid().getName().replaceAll("molten", "").replaceAll("fluid", "");
                Materials mat = Materials.get(FluidString.substring(0, 1).toUpperCase() + FluidString.substring(1));
                if (mat != Materials._NULL) {
                    for (SubTag tag : GasTags) {
                        if (mat.contains(tag)) {
                            DebugLog.log("Found EBF Recipe to change, Output:" + BW_Util.translateGTItemStack(recipe.mOutputs[0]));
                            toAdd.put(tag, recipe);
                        }
                    }
                }
            }
        }
        return toAdd;
    }

    private static HashSet<ItemStack> getNoGasItems(ArrayListMultimap<SubTag, GT_Recipe> base) {
        Iterator<GT_Recipe> it = GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.iterator();
        HashSet<ItemStack> toAdd = new HashSet<>();
        ArrayListMultimap<SubTag, GT_Recipe> repToAdd = ArrayListMultimap.create();
        while (it.hasNext()) {
            GT_Recipe recipe = it.next();
            for (SubTag tag : base.keySet())
                recipeLoop:
                        for (GT_Recipe baseRe : base.get(tag)) {
                            if (recipe.mInputs.length == baseRe.mInputs.length && recipe.mOutputs.length == baseRe.mOutputs.length)
                                for (int i = 0; i < recipe.mInputs.length; i++) {
                                    if ((recipe.mFluidInputs == null || recipe.mFluidInputs.length == 0) && BW_Util.checkStackAndPrefix(recipe.mInputs[i]) && BW_Util.checkStackAndPrefix(baseRe.mInputs[i]) && GT_OreDictUnificator.getAssociation(recipe.mInputs[i]).mMaterial.mMaterial.equals(GT_OreDictUnificator.getAssociation(baseRe.mInputs[i]).mMaterial.mMaterial) && GT_Utility.areStacksEqual(recipe.mOutputs[0], baseRe.mOutputs[0])) {
                                        toAdd.add(recipe.mOutputs[0]);
                                        repToAdd.put(tag, recipe);
                                        continue recipeLoop;
                                    }
                                }
                        }
        }
        base.putAll(repToAdd);
        return toAdd;
    }

    private static void editRecipes(ArrayListMultimap<SubTag, GT_Recipe> base, HashSet<ItemStack> noGas) {
        if (GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeFluidNameMap.contains(Objects.requireNonNull(fluids.get(Oganesson)).getName()))
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
                            int time = (int) ((double) recipe.mDuration / 200D * (200D + (werkstoff.getStats().getProtons() >= mat.getProtons() ? (double) mat.getProtons() - (double) werkstoff.getStats().getProtons() : (double) mat.getProtons() * 2.75D - (double) werkstoff.getStats().getProtons())));
                            toAdd.add(new BWRecipes.DynamicGTRecipe(false, recipe.mInputs, recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, new FluidStack[]{new FluidStack(Objects.requireNonNull(fluids.get(werkstoff)), recipe.mFluidInputs[0].amount)}, recipe.mFluidOutputs, time, recipe.mEUt, recipe.mSpecialValue));
                        }
                        for (Materials materials : Materials.values()) {
                            if (!materials.contains(GasTag))
                                continue;
                            int time = (int) ((double) recipe.mDuration / 200D * (200D + (materials.getProtons() >= mat.getProtons() ? (double) mat.getProtons() - (double) materials.getProtons() : (double) mat.getProtons() * 2.75D - (double) materials.getProtons())));
                            toAdd.add(new BWRecipes.DynamicGTRecipe(false, recipe.mInputs, recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, new FluidStack[]{materials.getGas(recipe.mFluidInputs[0].amount)}, recipe.mFluidOutputs, time, recipe.mEUt, recipe.mSpecialValue));
                        }
                        for (ItemStack is : noGas) {
                            byte circuitConfiguration = 1;
                            if (GT_Utility.areStacksEqual(is, recipe.mOutputs[0])) {
                                ArrayList<ItemStack> inputs = new ArrayList<>(recipe.mInputs.length);
                                for (ItemStack stack : recipe.mInputs)
                                    if (!GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(11), stack) && !GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(14), stack) && !GT_Utility.areStacksEqual(GT_Utility.getIntegratedCircuit(19), stack)) {
                                        if (BW_Util.checkStackAndPrefix(stack))
                                            circuitConfiguration = (byte) (GT_OreDictUnificator.getAssociation(stack).mPrefix.equals(OrePrefixes.dustSmall) ? 4 : GT_OreDictUnificator.getAssociation(stack).mPrefix.equals(OrePrefixes.dustTiny) ? 9 : 1);
                                        inputs.add(stack);
                                    }
                                inputs.add(GT_Utility.getIntegratedCircuit(circuitConfiguration));
                                toAdd.add(new BWRecipes.DynamicGTRecipe(false, inputs.toArray(new ItemStack[0]), recipe.mOutputs, recipe.mSpecialItems, recipe.mChances, null, recipe.mFluidOutputs, (int) ((double) recipe.mDuration / 200D * (200D + ((double) mat.getProtons() * 2.75D))), recipe.mEUt, recipe.mSpecialValue));
                                break;
                            }
                        }
                    }
                }
            }
            GT_Recipe.GT_Recipe_Map.sBlastRecipes.mRecipeList.removeAll(base.get(GasTag));
        }
        HashSet<GT_Recipe> duplicates = new HashSet<>();
        for (GT_Recipe recipe : toAdd) {
            for (GT_Recipe recipe2 : toAdd) {
                if (recipe.mEUt != recipe2.mEUt || recipe.mDuration != recipe2.mDuration || recipe.mSpecialValue != recipe2.mSpecialValue || recipe == recipe2 || recipe.mInputs.length != recipe2.mInputs.length || recipe.mFluidInputs.length != recipe2.mFluidInputs.length)
                    continue;
                boolean isSame = true;
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    if (!GT_Utility.areStacksEqual(recipe.mInputs[i], recipe2.mInputs[i]))
                        isSame = false;
                }
                for (int i = 0; i < recipe.mFluidInputs.length; i++) {
                    if (!GT_Utility.areFluidsEqual(recipe.mFluidInputs[i], recipe2.mFluidInputs[i]))
                        isSame = false;
                }
                if (isSame)
                    duplicates.add(recipe2);
            }
        }
        toAdd.removeAll(duplicates);
        toAdd.forEach(GT_Recipe.GT_Recipe_Map.sBlastRecipes::add);
    }

    @SuppressWarnings("ALL")
    private static void addElectricImplosionCompressorRecipes() {
        if (eicMap == null) {
            eicMap = new GT_Recipe.GT_Recipe_Map(new HashSet<>(GT_Recipe.GT_Recipe_Map.sImplosionRecipes.mRecipeList.size()), "gt.recipe.electricimplosioncompressor", "Electric Implosion Compressor", (String) null, "gregtech:textures/gui/basicmachines/Default", 1, 2, 1, 0, 1, "", 1, "", true, true);
            GT_Recipe.GT_Recipe_Map.sImplosionRecipes.mRecipeList.stream().filter(e -> e.mInputs != null).forEach(recipe -> eicMap.addRecipe(true, Arrays.stream(recipe.mInputs).filter(e -> !MainMod.checkForExplosives(e)).distinct().toArray(ItemStack[]::new), recipe.mOutputs, null, null, null, 1, BW_Util.getMachineVoltageFromTier(10), 0));
        }
    }

    private static boolean checkForExplosives(ItemStack input) {
        return (GT_Utility.areStacksEqual(input, new ItemStack(Blocks.tnt)) || GT_Utility.areStacksEqual(input, GT_ModHandler.getIC2Item("industrialTnt", 1L)) || GT_Utility.areStacksEqual(input, GT_ModHandler.getIC2Item("dynamite", 1L)) || GT_Utility.areStacksEqual(input, ItemList.Block_Powderbarrel.get(1L)));
    }

//    private static void removeDuplicateRecipes(){
//        GT_Recipe.GT_Recipe_Map.sMappings.forEach(
//                gt_recipe_map -> {
//                    HashSet<Integer> mappings = new HashSet<>();
//                    HashSet<GT_Recipe> dupes = new HashSet<>();
//                    gt_recipe_map.mRecipeList.forEach(
//                            recipe -> {
//                               if (mappings.contains(BW_Util.getRecipeHash(recipe)))
//                                   dupes.add(recipe);
//                               mappings.add(BW_Util.getRecipeHash(recipe));
//                            }
//                    );
//                    gt_recipe_map.mRecipeList.removeAll(dupes);
//                }
//        );
//    }

}