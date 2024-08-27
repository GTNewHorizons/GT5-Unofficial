package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.CraftTweaker;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GregTech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeCategoryHolder;
import gregtech.api.recipe.RecipeCategorySetting;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Proxy;
import gregtech.common.config.client.ConfigColorModulation;
import gregtech.common.config.client.ConfigInterface;
import gregtech.common.config.client.ConfigRender;
import gregtech.common.config.client.ConfigWaila;
import gregtech.common.config.gregtech.ConfigDebug;
import gregtech.common.config.gregtech.ConfigFeatures;
import gregtech.common.config.gregtech.ConfigGeneral;
import gregtech.common.config.gregtech.ConfigMachines;
import gregtech.common.config.gregtech.ConfigOreDropBehavior;
import gregtech.common.config.gregtech.ConfigPollution;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineBase;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_Cleanroom;

public class GT_PreLoad {

    public static void sortToTheEnd() {
        try {
            GT_FML_LOGGER.info("GT_Mod: Sorting GregTech to the end of the Mod List for further processing.");
            LoadController tLoadController = (LoadController) GT_Utility
                .getFieldContent(Loader.instance(), "modController", true, true);
            assert tLoadController != null;
            List<ModContainer> tModList = tLoadController.getActiveModList();
            List<ModContainer> tNewModsList = new ArrayList<>();
            ModContainer tGregTech = null;
            short tModList_sS = (short) tModList.size();
            for (short i = 0; i < tModList_sS; i = (short) (i + 1)) {
                ModContainer tMod = tModList.get(i);
                if (tMod.getModId()
                    .equalsIgnoreCase(GregTech.ID)) {
                    tGregTech = tMod;
                } else {
                    tNewModsList.add(tMod);
                }
            }
            if (tGregTech != null) {
                tNewModsList.add(tGregTech);
            }
            Objects.requireNonNull(GT_Utility.getField(tLoadController, "activeModList", true, true))
                .set(tLoadController, tNewModsList);
        } catch (Throwable e) {
            GT_Mod.logStackTrace(e);
        }
    }

    public static void initLocalization(File languageDir) {
        GT_FML_LOGGER.info("GT_Mod: Generating Lang-File");

        if (FMLCommonHandler.instance()
            .getEffectiveSide()
            .isClient()) {
            String userLang = Minecraft.getMinecraft()
                .getLanguageManager()
                .getCurrentLanguage()
                .getLanguageCode();
            GT_FML_LOGGER.info("User lang is " + userLang);
            if (userLang.equals("en_US")) {
                GT_FML_LOGGER.info("Loading GregTech.lang");
                GT_LanguageManager.isEN_US = true;
                GT_LanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
            } else {
                String l10nFileName = "GregTech_" + userLang + ".lang";
                File l10nFile = new File(languageDir, l10nFileName);
                if (l10nFile.isFile()) {
                    GT_FML_LOGGER.info("Loading l10n file: " + l10nFileName);
                    GT_LanguageManager.sEnglishFile = new Configuration(l10nFile);
                } else {
                    GT_FML_LOGGER.info("Cannot find l10n file " + l10nFileName + ", fallback to GregTech.lang");
                    GT_LanguageManager.isEN_US = true;
                    GT_LanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
                }
            }
        } else {
            GT_LanguageManager.isEN_US = true;
            GT_LanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
        }
        GT_LanguageManager.sEnglishFile.load();

        Materials.getMaterialsMap()
            .values()
            .parallelStream()
            .filter(Objects::nonNull)
            .forEach(
                aMaterial -> aMaterial.mLocalizedName = GT_LanguageManager
                    .addStringLocalization("Material." + aMaterial.mName.toLowerCase(), aMaterial.mDefaultLocalName));
    }

    public static void getConfiguration(File configDir) {
        File tFile = new File(new File(configDir, "GregTech"), "IDs.cfg");
        GT_Config.sConfigFileIDs = new Configuration(tFile);
        GT_Config.sConfigFileIDs.load();
        GT_Config.sConfigFileIDs.save();

        tFile = new File(new File(configDir, "GregTech"), "Cleanroom.cfg");
        GT_Config.cleanroomFile = new Configuration(tFile);
        GT_Config.cleanroomFile.load();
        GT_Config.cleanroomFile.save();

        tFile = new File(new File(configDir, "GregTech"), "UndergroundFluids.cfg");
        GT_Config.undergroundFluidsFile = new Configuration(tFile);
        GT_Config.undergroundFluidsFile.load();
        GT_Config.undergroundFluidsFile.save();

        GregTech_API.NEIClientFIle = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "NEIClient.cfg")));

    }

    public static void createLogFiles(File parentFile) {
        GT_Log.mLogFile = new File(parentFile, "logs/GregTech.log");
        if (!GT_Log.mLogFile.exists()) {
            try {
                GT_Log.mLogFile.createNewFile();
            } catch (Throwable ignored) {}
        }
        try {
            GT_Log.out = GT_Log.err = new PrintStream(GT_Log.mLogFile);
        } catch (FileNotFoundException ignored) {}

        if (ConfigGeneral.loggingOreDict) {
            GT_Log.mOreDictLogFile = new File(parentFile, "logs/OreDict.log");
            if (!GT_Log.mOreDictLogFile.exists()) {
                try {
                    GT_Log.mOreDictLogFile.createNewFile();
                } catch (Throwable ignored) {}
            }
            List<String> tList = ((GT_Log.LogBuffer) GT_Log.ore).mBufferedOreDictLog;
            try {
                GT_Log.ore = new PrintStream(GT_Log.mOreDictLogFile);
            } catch (Throwable ignored) {}
            GT_Log.ore.println("******************************************************************************");
            GT_Log.ore.println("* This is the complete log of the GT5-Unofficial OreDictionary Handler. It   *");
            GT_Log.ore.println("* processes all OreDictionary entries and can sometimes cause errors. All    *");
            GT_Log.ore.println("* entries and errors are being logged. If you see an error please raise an   *");
            GT_Log.ore.println("* issue at https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues.  *");
            GT_Log.ore.println("******************************************************************************");
            tList.forEach(GT_Log.ore::println);
        }
        if (ConfigGeneral.loggingExplosions) {
            GT_Log.mExplosionLog = new File(parentFile, "logs/Explosion.log");
            if (!GT_Log.mExplosionLog.exists()) {
                try {
                    GT_Log.mExplosionLog.createNewFile();
                } catch (Throwable ignored) {}
            }
            try {
                GT_Log.exp = new PrintStream(GT_Log.mExplosionLog);
            } catch (Throwable ignored) {}
        }

        if (ConfigGeneral.loggingPlayerActicity) {
            GT_Log.mPlayerActivityLogFile = new File(parentFile, "logs/PlayerActivity.log");
            if (!GT_Log.mPlayerActivityLogFile.exists()) {
                try {
                    GT_Log.mPlayerActivityLogFile.createNewFile();
                } catch (Throwable ignored) {}
            }
            try {
                GT_Log.pal = new PrintStream(GT_Log.mPlayerActivityLogFile);
            } catch (Throwable ignored) {}
        }
    }

    public static void runMineTweakerCompat() {
        if (!CraftTweaker.isModLoaded()) return;

        GT_FML_LOGGER.info("preReader");
        final List<String> oreTags = new ArrayList<>();
        final File globalDir = new File("scripts");
        if (globalDir.exists()) {
            final List<String> scripts = new ArrayList<>();
            for (File file : Objects.requireNonNull(globalDir.listFiles())) {
                if (file.getName()
                    .endsWith(".zs")) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            scripts.add(line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            String pattern1 = "<";
            String pattern2 = ">";

            Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
            for (String text : scripts) {
                Matcher m = p.matcher(text);
                while (m.find()) {
                    String hit = m.group(1);
                    if (hit.startsWith("ore:")) {
                        hit = hit.substring(4);
                        if (!oreTags.contains(hit)) oreTags.add(hit);
                    } else if (hit.startsWith("gregtech:gt.metaitem.0")) {
                        hit = hit.substring(22);
                        int mIt = Integer.parseInt(hit.substring(0, 1));
                        if (mIt > 0) {
                            int meta = 0;
                            try {
                                hit = hit.substring(2);
                                meta = Integer.parseInt(hit);
                            } catch (Exception e) {
                                GT_FML_LOGGER.info("parseError: " + hit);
                            }
                            if (meta > 0 && meta < 32000) {
                                int prefix = meta / 1000;
                                int material = meta % 1000;
                                String tag = "";
                                String[] tags = new String[] {};
                                if (mIt == 1) tags = new String[] { "dustTiny", "dustSmall", "dust", "dustImpure",
                                    "dustPure", "crushed", "crushedPurified", "crushedCentrifuged", "gem", "nugget",
                                    null, "ingot", "ingotHot", "ingotDouble", "ingotTriple", "ingotQuadruple",
                                    "ingotQuintuple", "plate", "plateDouble", "plateTriple", "plateQuadruple",
                                    "plateQuintuple", "plateDense", "stick", "lens", "round", "bolt", "screw", "ring",
                                    "foil", "cell", "cellPlasma", "cellMolten", "rawOre" };
                                if (mIt == 2) tags = new String[] { "toolHeadSword", "toolHeadPickaxe",
                                    "toolHeadShovel", "toolHeadAxe", "toolHeadHoe", "toolHeadHammer", "toolHeadFile",
                                    "toolHeadSaw", "toolHeadDrill", "toolHeadChainsaw", "toolHeadWrench",
                                    "toolHeadUniversalSpade", "toolHeadSense", "toolHeadPlow", "toolHeadArrow",
                                    "toolHeadBuzzSaw", "turbineBlade", null, null, "wireFine", "gearGtSmall", "rotor",
                                    "stickLong", "springSmall", "spring", "arrowGtWood", "arrowGtPlastic", "gemChipped",
                                    "gemFlawed", "gemFlawless", "gemExquisite", "gearGt" };
                                if (mIt == 3) tags = new String[] { "itemCasing", "nanite" };
                                if (tags.length > prefix) tag = tags[prefix];
                                if (GregTech_API.sGeneratedMaterials[material] != null) {
                                    tag += GregTech_API.sGeneratedMaterials[material].mName;
                                    if (!oreTags.contains(tag)) oreTags.add(tag);
                                } else if (material > 0) {
                                    GT_FML_LOGGER.info("MaterialDisabled: " + material + " " + m.group(1));
                                }
                            }
                        }
                    }
                }
            }
        }

        final String[] preS = new String[] { "dustTiny", "dustSmall", "dust", "dustImpure", "dustPure", "crushed",
            "crushedPurified", "crushedCentrifuged", "gem", "nugget", "ingot", "ingotHot", "ingotDouble", "ingotTriple",
            "ingotQuadruple", "ingotQuintuple", "plate", "plateDouble", "plateTriple", "plateQuadruple",
            "plateQuintuple", "plateDense", "stick", "lens", "round", "bolt", "screw", "ring", "foil", "cell",
            "cellPlasma", "toolHeadSword", "toolHeadPickaxe", "toolHeadShovel", "toolHeadAxe", "toolHeadHoe",
            "toolHeadHammer", "toolHeadFile", "toolHeadSaw", "toolHeadDrill", "toolHeadChainsaw", "toolHeadWrench",
            "toolHeadUniversalSpade", "toolHeadSense", "toolHeadPlow", "toolHeadArrow", "toolHeadBuzzSaw",
            "turbineBlade", "wireFine", "gearGtSmall", "rotor", "stickLong", "springSmall", "spring", "arrowGtWood",
            "arrowGtPlastic", "gemChipped", "gemFlawed", "gemFlawless", "gemExquisite", "gearGt", "nanite",
            "cellMolten", "rawOre" };

        List<String> mMTTags = new ArrayList<>();
        oreTags.stream()
            .filter(test -> StringUtils.startsWithAny(test, preS))
            .forEach(test -> {
                mMTTags.add(test);
                if (GT_Values.D1) GT_FML_LOGGER.info("oretag: " + test);
            });

        GT_FML_LOGGER.info("reenableMetaItems");

        for (String reEnable : mMTTags) {
            OrePrefixes tPrefix = OrePrefixes.getOrePrefix(reEnable);
            if (tPrefix != null) {
                Materials tName = Materials.get(reEnable.replaceFirst(tPrefix.toString(), ""));
                if (tName != null) {
                    tPrefix.mDisabledItems.remove(tName);
                    tPrefix.mGeneratedItems.add(tName);
                    if (tPrefix == OrePrefixes.screw) {
                        OrePrefixes.bolt.mDisabledItems.remove(tName);
                        OrePrefixes.bolt.mGeneratedItems.add(tName);
                        OrePrefixes.stick.mDisabledItems.remove(tName);
                        OrePrefixes.stick.mGeneratedItems.add(tName);
                    }
                    if (tPrefix == OrePrefixes.round) {
                        OrePrefixes.nugget.mDisabledItems.remove(tName);
                        OrePrefixes.nugget.mGeneratedItems.add(tName);
                    }
                    if (tPrefix == OrePrefixes.spring) {
                        OrePrefixes.stickLong.mDisabledItems.remove(tName);
                        OrePrefixes.stickLong.mGeneratedItems.add(tName);
                        OrePrefixes.stick.mDisabledItems.remove(tName);
                        OrePrefixes.stick.mGeneratedItems.add(tName);
                    }
                    if (tPrefix == OrePrefixes.springSmall) {
                        OrePrefixes.stick.mDisabledItems.remove(tName);
                        OrePrefixes.stick.mGeneratedItems.add(tName);
                    }
                    if (tPrefix == OrePrefixes.stickLong) {
                        OrePrefixes.stick.mDisabledItems.remove(tName);
                        OrePrefixes.stick.mGeneratedItems.add(tName);
                    }
                    if (tPrefix == OrePrefixes.rotor) {
                        OrePrefixes.ring.mDisabledItems.remove(tName);
                        OrePrefixes.ring.mGeneratedItems.add(tName);
                    }
                } else {
                    GT_FML_LOGGER.info("noMaterial " + reEnable);
                }
            } else {
                GT_FML_LOGGER.info("noPrefix " + reEnable);
            }
        }
    }

    public static void adjustScrap() {
        GT_FML_LOGGER.info("GT_Mod: Removing all original Scrapbox Drops.");
        try {
            Objects.requireNonNull(GT_Utility.getField("ic2.core.item.ItemScrapbox$Drop", "topChance", true, true))
                .set(null, 0);
            ((List<?>) Objects.requireNonNull(
                GT_Utility.getFieldContent(
                    GT_Utility.getFieldContent("ic2.api.recipe.Recipes", "scrapboxDrops", true, true),
                    "drops",
                    true,
                    true))).clear();
        } catch (Throwable e) {
            if (GT_Values.D1) {
                e.printStackTrace(GT_Log.err);
            }
        }
        GT_Log.out.println("GT_Mod: Adding Scrap with a Weight of 200.0F to the Scrapbox Drops.");
        GT_ModHandler.addScrapboxDrop(200.0F, GT_ModHandler.getIC2Item("scrap", 1L));
    }

    public static void loadConfig() {
        // general
        GT_Values.D1 = ConfigDebug.D1;
        GT_Values.D2 = ConfigDebug.D2;
        GT_Values.allow_broken_recipemap = ConfigDebug.allowBrokenRecipeMap;
        GT_Values.debugCleanroom = ConfigDebug.debugCleanroom;
        GT_Values.debugDriller = ConfigDebug.debugDriller;
        GT_Values.debugWorldGen = ConfigDebug.debugWorldgen;
        GT_Values.debugOrevein = ConfigDebug.debugOrevein;
        GT_Values.debugSmallOres = ConfigDebug.debugSmallOres;
        GT_Values.debugStones = ConfigDebug.debugStones;
        GT_Values.debugBlockMiner = ConfigDebug.debugBlockMiner;
        GT_Values.debugBlockPump = ConfigDebug.debugBlockPump;
        GT_Values.debugEntityCramming = ConfigDebug.debugEntityCramming;
        GT_Values.debugWorldData = ConfigDebug.debugWorldData;
        GT_Values.oreveinPercentage = ConfigGeneral.oreveinPercentage;
        GT_Values.oreveinAttempts = ConfigGeneral.oreveinAttempts;
        GT_Values.oreveinMaxPlacementAttempts = ConfigGeneral.oreveinMaxPlacementAttempts;
        GT_Values.oreveinPlacerOres = ConfigGeneral.oreveinPlacerOres;
        GT_Values.oreveinPlacerOresMultiplier = ConfigGeneral.oreveinPlacerOresMultiplier;
        GregTech_API.TICKS_FOR_LAG_AVERAGING = ConfigGeneral.ticksForLagAveraging;
        GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = ConfigGeneral.millisecondThesholdUntilLagWarning;
        GregTech_API.sTimber = ConfigGeneral.timber;
        GregTech_API.sDrinksAlwaysDrinkable = ConfigGeneral.drinksAlwaysDrinkable;
        GregTech_API.sDoShowAllItemsInCreative = ConfigGeneral.doShowAllItemsInCreative;
        GregTech_API.sMultiThreadedSounds = ConfigGeneral.multiThreadedSounds;
        GT_Mod.gregtechproxy.mMaxEqualEntitiesAtOneSpot = ConfigGeneral.maxEqualEntitiesAtOneSpot;
        GT_Mod.gregtechproxy.mFlintChance = ConfigGeneral.flintChance;
        GT_Mod.gregtechproxy.mItemDespawnTime = ConfigGeneral.itemDespawnTime;
        GT_Mod.gregtechproxy.mAllowSmallBoilerAutomation = ConfigGeneral.allowSmallBoilerAutomation;
        GT_Mod.gregtechproxy.mDisableVanillaOres = gregtech.common.config.worldgen.ConfigGeneral.disableVanillaOres;
        GT_Mod.gregtechproxy.mIncreaseDungeonLoot = ConfigGeneral.increaseDungeonLoot;
        GT_Mod.gregtechproxy.mAxeWhenAdventure = ConfigGeneral.axeWhenAdventure;
        GT_Mod.gregtechproxy.mSurvivalIntoAdventure = ConfigGeneral.survivalIntoAdventure;
        GT_Mod.gregtechproxy.mHungerEffect = ConfigGeneral.hungerEffect;
        GT_Mod.gregtechproxy.mInventoryUnification = ConfigGeneral.inventoryUnification;
        GT_Mod.gregtechproxy.mGTBees = ConfigGeneral.GTBees;
        GT_Mod.gregtechproxy.mCraftingUnification = ConfigGeneral.craftingUnification;
        GT_Mod.gregtechproxy.mNerfedWoodPlank = ConfigGeneral.nerfedWoodPlank;
        GT_Mod.gregtechproxy.mNerfedVanillaTools = ConfigGeneral.nerfedVanillaTools;
        GT_Mod.gregtechproxy.mAchievements = ConfigGeneral.achievements;
        GT_Mod.gregtechproxy.mHideUnusedOres = ConfigGeneral.hideUnusedOres;
        GT_Mod.gregtechproxy.mEnableAllMaterials = ConfigGeneral.enableAllMaterials;
        GT_Mod.gregtechproxy.mExplosionItemDrop = ConfigGeneral.explosionItemDrop;
        GT_Mod.gregtechproxy.mEnableCleanroom = ConfigGeneral.enableCleanroom;
        GT_Mod.gregtechproxy.mLowGravProcessing = GalacticraftCore.isModLoaded() && ConfigGeneral.lowGravProcessing;
        GT_Mod.gregtechproxy.mCropNeedBlock = ConfigGeneral.cropNeedBlock;
        GT_Mod.gregtechproxy.mAMHInteraction = ConfigGeneral.autoMaintenaceHatchesInteraction;
        GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre = ConfigGeneral.mixedOreOnlyYieldsTwoThirdsOfPureOre;
        GT_Mod.gregtechproxy.mRichOreYieldMultiplier = ConfigGeneral.richOreYieldMultiplier;
        GT_Mod.gregtechproxy.mNetherOreYieldMultiplier = ConfigGeneral.netherOreYieldMultiplier;
        GT_Mod.gregtechproxy.mEndOreYieldMultiplier = ConfigGeneral.endOreYieldMultiplier;
        GT_Mod.gregtechproxy.gt6Pipe = ConfigGeneral.gt6Pipe;
        GT_Mod.gregtechproxy.gt6Cable = ConfigGeneral.gt6Cable;
        GT_Mod.gregtechproxy.ic2EnergySourceCompat = ConfigGeneral.ic2EnergySourceCompat;
        GT_Mod.gregtechproxy.costlyCableConnection = ConfigGeneral.costlyCableConnection;
        GT_Mod.gregtechproxy.crashOnNullRecipeInput = ConfigGeneral.crashOnNullRecipeInput;
        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            GT_Mod.gregtechproxy.crashOnNullRecipeInput = false; // Use flags in GT_RecipeBuilder instead!
        }
        GT_LanguageManager.i18nPlaceholder = ConfigGeneral.i18nPlaceholder;
        GT_MetaTileEntity_LongDistancePipelineBase.minimalDistancePoints = ConfigGeneral.minimalDistancePoints;
        GT_Values.mCTMEnabledBlock.addAll(Arrays.asList(ConfigGeneral.CTMWhitelist));
        GT_Values.mCTMDisabledBlock.addAll(Arrays.asList(ConfigGeneral.CTMBlacklist));
        if (ConfigGeneral.harderMobSpawner) {
            Blocks.mob_spawner.setHardness(500.0F)
                .setResistance(6000000.0F);
        }

        // machines
        GT_Values.ticksBetweenSounds = ConfigMachines.ticksBetweenSounds;
        GT_Values.blacklistedTileEntiyClassNamesForWA = ConfigMachines.blacklistedTileEntiyClassNamesForWA;
        GT_Values.cleanroomGlass = ConfigMachines.cleanroomGlass;
        GT_Values.enableChunkloaders = ConfigMachines.enableChunkloaders;
        GT_Values.alwaysReloadChunkloaders = ConfigMachines.alwaysReloadChunkloaders;
        GT_Values.debugChunkloaders = ConfigDebug.debugChunkloaders;
        GT_Values.disableDigitalChestsExternalAccess = ConfigMachines.disableDigitalChestsExternalAccess;
        GT_Values.enableMultiTileEntities = ConfigMachines.enableMultiTileEntities
            || (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        GregTech_API.sMachineExplosions = ConfigMachines.machineExplosions;
        GregTech_API.sMachineFlammable = ConfigMachines.machineFlammable;
        GregTech_API.sMachineNonWrenchExplosions = ConfigMachines.machineNonWrenchExplosions;
        GregTech_API.sMachineWireFire = ConfigMachines.machineWireFire;
        GregTech_API.sMachineFireExplosions = ConfigMachines.machineFireExplosions;
        GregTech_API.sMachineRainExplosions = ConfigMachines.machineRainExplosions;
        GregTech_API.sMachineThunderExplosions = ConfigMachines.machineThunderExplosions;
        GregTech_API.sColoredGUI = ConfigMachines.coloredGUI;
        GregTech_API.sMachineMetalGUI = ConfigMachines.machineMetalGUI;
        // Implementation for this is actually handled in NewHorizonsCoreMod in MainRegistry.java!
        GregTech_API.sUseMachineMetal = ConfigMachines.useMachineMetal;

        // client
        loadClientConfig();

        // Pollution
        GT_Mod.gregtechproxy.mPollution = ConfigPollution.pollution;
        GT_Mod.gregtechproxy.mPollutionSmogLimit = ConfigPollution.pollutionSmogLimit;
        GT_Mod.gregtechproxy.mPollutionPoisonLimit = ConfigPollution.pollutionPoisonLimit;
        GT_Mod.gregtechproxy.mPollutionVegetationLimit = ConfigPollution.pollutionVegetationLimit;
        GT_Mod.gregtechproxy.mPollutionSourRainLimit = ConfigPollution.pollutionSourRainLimit;
        GT_Mod.gregtechproxy.mPollutionOnExplosion = ConfigPollution.pollutionOnExplosion;
        GT_Mod.gregtechproxy.mPollutionPrimitveBlastFurnacePerSecond = ConfigPollution.pollutionPrimitveBlastFurnacePerSecond;
        GT_Mod.gregtechproxy.mPollutionCharcoalPitPerSecond = ConfigPollution.pollutionCharcoalPitPerSecond;
        GT_Mod.gregtechproxy.mPollutionEBFPerSecond = ConfigPollution.pollutionEBFPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeCombustionEnginePerSecond = ConfigPollution.pollutionLargeCombustionEnginePerSecond;
        GT_Mod.gregtechproxy.mPollutionExtremeCombustionEnginePerSecond = ConfigPollution.pollutionExtremeCombustionEnginePerSecond;
        GT_Mod.gregtechproxy.mPollutionImplosionCompressorPerSecond = ConfigPollution.pollutionImplosionCompressorPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeBronzeBoilerPerSecond = ConfigPollution.pollutionLargeBronzeBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeSteelBoilerPerSecond = ConfigPollution.pollutionLargeSteelBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond = ConfigPollution.pollutionLargeTitaniumBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeTungstenSteelBoilerPerSecond = ConfigPollution.pollutionLargeTungstenSteelBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionReleasedByThrottle = ConfigPollution.pollutionReleasedByThrottle;
        GT_Mod.gregtechproxy.mPollutionLargeGasTurbinePerSecond = ConfigPollution.pollutionLargeGasTurbinePerSecond;
        GT_Mod.gregtechproxy.mPollutionMultiSmelterPerSecond = ConfigPollution.pollutionMultiSmelterPerSecond;
        GT_Mod.gregtechproxy.mPollutionPyrolyseOvenPerSecond = ConfigPollution.pollutionPyrolyseOvenPerSecond;
        GT_Mod.gregtechproxy.mPollutionSmallCoalBoilerPerSecond = ConfigPollution.pollutionSmallCoalBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond = ConfigPollution.pollutionHighPressureLavaBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionHighPressureCoalBoilerPerSecond = ConfigPollution.pollutionHighPressureCoalBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionBaseDieselGeneratorPerSecond = ConfigPollution.pollutionBaseDieselGeneratorPerSecond;
        double[] mPollutionDieselGeneratorReleasedByTier = ConfigPollution.pollutionDieselGeneratorReleasedByTier;
        if (mPollutionDieselGeneratorReleasedByTier.length
            == GT_Mod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier.length) {
            GT_Mod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier = mPollutionDieselGeneratorReleasedByTier;
        } else {
            GT_FML_LOGGER
                .error("The Length of the Diesel Turbine Pollution Array Config must be the same as the Default");
        }
        GT_Mod.gregtechproxy.mPollutionBaseGasTurbinePerSecond = ConfigPollution.pollutionBaseGasTurbinePerSecond;
        double[] mPollutionGasTurbineReleasedByTier = ConfigPollution.pollutionGasTurbineReleasedByTier;
        if (mPollutionGasTurbineReleasedByTier.length
            == GT_Mod.gregtechproxy.mPollutionGasTurbineReleasedByTier.length) {
            GT_Mod.gregtechproxy.mPollutionGasTurbineReleasedByTier = mPollutionGasTurbineReleasedByTier;
        } else {
            GT_FML_LOGGER.error("The Length of the Gas Turbine Pollution Array Config must be the same as the Default");
        }

        // cleanroom file
        if (GT_Mod.gregtechproxy.mEnableCleanroom) GT_MetaTileEntity_Cleanroom.loadConfig(GT_Config.cleanroomFile);

        // underground fluids file
        GT_Mod.gregtechproxy.mUndergroundOil.getConfig(GT_Config.undergroundFluidsFile, "undergroundfluid");

        // Worldgeneration.cfg
        GT_Mod.gregtechproxy.enableUndergroundGravelGen = gregtech.common.config.worldgen.ConfigGeneral.generateUndergroundGravelGen;
        GT_Mod.gregtechproxy.enableUndergroundDirtGen = gregtech.common.config.worldgen.ConfigGeneral.generateUndergroundDirtGen;
        GT_Mod.gregtechproxy.enableBlackGraniteOres = gregtech.common.config.worldgen.ConfigGeneral.generateBlackGraniteOres;
        GT_Mod.gregtechproxy.enableRedGraniteOres = gregtech.common.config.worldgen.ConfigGeneral.generateBlackGraniteOres;
        GT_Mod.gregtechproxy.enableMarbleOres = gregtech.common.config.worldgen.ConfigGeneral.generateMarbleOres;
        GT_Mod.gregtechproxy.enableBasaltOres = gregtech.common.config.worldgen.ConfigGeneral.generateBasaltOres;

        // OverpoweredStuff.cfg
        GregTech_API.mOutputRF = gregtech.common.config.opstuff.ConfigGeneral.outputRF;
        GregTech_API.mInputRF = gregtech.common.config.opstuff.ConfigGeneral.inputRF;
        GregTech_API.mEUtoRF = gregtech.common.config.opstuff.ConfigGeneral.howMuchRFWith100EUInInput;
        GregTech_API.mRFtoEU = gregtech.common.config.opstuff.ConfigGeneral.howMuchEUWith100RFInInput;
        GregTech_API.mRFExplosions = gregtech.common.config.opstuff.ConfigGeneral.RFExplosions;

        // MachineStats.cfg
        GT_Mod.gregtechproxy.mForceFreeFace = gregtech.common.config.machinestats.ConfigMachines.forceFreeFace;

        // ore_drop_behavior
        try {
            GT_Log.out.println("Trying to set it to: " + ConfigOreDropBehavior.setting);
            GT_Mod.gregtechproxy.oreDropSystem = GT_Proxy.OreDropSystem.valueOf(ConfigOreDropBehavior.setting);;
        } catch (IllegalArgumentException e) {
            GT_Log.err.println(e);
            GT_Mod.gregtechproxy.oreDropSystem = GT_Proxy.OreDropSystem.FortuneItem;
        }

        // features
        GT_Mod.gregtechproxy.mUpgradeCount = Math.min(64, Math.max(1, ConfigFeatures.upgradeStackSize));
        for (OrePrefixes tPrefix : OrePrefixes.values()) {
            if (tPrefix.mIsUsedForOreProcessing) {
                tPrefix.mDefaultStackSize = ((byte) Math.min(64, Math.max(1, ConfigFeatures.maxOreStackSize)));
            } else if (tPrefix == OrePrefixes.plank) {
                tPrefix.mDefaultStackSize = ((byte) Math.min(64, Math.max(16, ConfigFeatures.maxPlankStackSize)));
            } else if ((tPrefix == OrePrefixes.wood) || (tPrefix == OrePrefixes.treeLeaves)
                || (tPrefix == OrePrefixes.treeSapling)
                || (tPrefix == OrePrefixes.log)) {
                    tPrefix.mDefaultStackSize = ((byte) Math.min(64, Math.max(16, ConfigFeatures.maxLogStackSize)));
                } else if (tPrefix.mIsUsedForBlocks) {
                    tPrefix.mDefaultStackSize = ((byte) Math
                        .min(64, Math.max(16, ConfigFeatures.maxOtherBlocksStackSize)));
                }
        }

        GT_RecipeBuilder.onConfigLoad();
    }

    public static void parseHex(Dyes dye, String hexString) {
        dye.mRGBa[0] = Short.parseShort(hexString.substring(1, 3), 16);
        dye.mRGBa[1] = Short.parseShort(hexString.substring(3, 5), 16);
        dye.mRGBa[2] = Short.parseShort(hexString.substring(5), 16);
    }

    public static void loadClientConfig() {
        Arrays.stream(Dyes.values())
            .filter(dye -> (dye != Dyes._NULL) && (dye.mIndex < 0))
            .forEach(dye -> {
                switch (dye.toString()
                    .toLowerCase()) {
                    case "cable_insulation" -> parseHex(dye, ConfigColorModulation.cableInsulation);
                    case "construction_foam" -> parseHex(dye, ConfigColorModulation.constructionFoam);
                    case "machine_metal" -> parseHex(dye, ConfigColorModulation.machineMetal);
                    default -> {
                        GT_FML_LOGGER.warn(
                            "unknown color modulation entry: " + dye
                                + ". Report this pls, as config is missing this entry being parsed in code.");
                    }
                }
            });
        GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion = ConfigRender.renderTileAmbientOcclusion;
        GT_Mod.gregtechproxy.mRenderGlowTextures = ConfigRender.renderGlowTextures;
        GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped = ConfigRender.renderFlippedMachinesFlipped;
        GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch = ConfigRender.renderIndicatorsOnHatch;
        GT_Mod.gregtechproxy.mRenderDirtParticles = ConfigRender.renderDirtParticles;
        GT_Mod.gregtechproxy.mRenderPollutionFog = ConfigRender.renderPollutionFog;
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = ConfigRender.renderItemDurabilityBar;
        GT_Mod.gregtechproxy.mRenderItemChargeBar = ConfigRender.renderItemChargeBar;
        GT_Mod.gregtechproxy.mUseBlockUpdateHandler = ConfigRender.useBlockUpdateHandler;

        GT_Mod.gregtechproxy.mCoverTabsVisible = ConfigInterface.coverTabsVisible;
        GT_Mod.gregtechproxy.mCoverTabsFlipped = ConfigInterface.coverTabsFlipped;
        GT_Mod.gregtechproxy.mTooltipVerbosity = ConfigInterface.tooltipVerbosity;
        GT_Mod.gregtechproxy.mTooltipShiftVerbosity = ConfigInterface.tooltipShiftVerbosity;
        GT_Mod.gregtechproxy.mTitleTabStyle = ConfigInterface.titleTabStyle;

        GT_Mod.gregtechproxy.mNEIRecipeSecondMode = GregTech_API.NEIClientFIle.get("nei", "RecipeSecondMode", true);
        GT_Mod.gregtechproxy.mNEIRecipeOwner = GregTech_API.NEIClientFIle.get("nei", "RecipeOwner", false);
        GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace = GregTech_API.NEIClientFIle
            .get("nei", "RecipeOwnerStackTrace", false);
        GT_Mod.gregtechproxy.mNEIOriginalVoltage = GregTech_API.NEIClientFIle.get("nei", "OriginalVoltage", false);

        GT_Mod.gregtechproxy.recipeCategorySettings.clear();
        for (RecipeCategory recipeCategory : findRecipeCategories()) {
            RecipeCategorySetting setting = RecipeCategorySetting.find(
                GregTech_API.NEIClientFIle.getWithValidValues(
                    "nei.recipe_categories",
                    recipeCategory.unlocalizedName,
                    RecipeCategorySetting.NAMES,
                    RecipeCategorySetting.getDefault()
                        .toName()));
            GT_Mod.gregtechproxy.recipeCategorySettings.put(recipeCategory, setting);
        }

        GT_Mod.gregtechproxy.mWailaTransformerVoltageTier = ConfigWaila.wailaTransformerVoltageTier;
        GT_Mod.gregtechproxy.wailaAverageNS = ConfigWaila.wailaAverageNS;

        GT_Mod.gregtechproxy.reloadNEICache();
    }

    private static List<RecipeCategory> findRecipeCategories() {
        List<RecipeCategory> ret = new ArrayList<>();
        try {
            Field discovererField = Loader.class.getDeclaredField("discoverer");
            discovererField.setAccessible(true);
            ModDiscoverer discoverer = (ModDiscoverer) discovererField.get(Loader.instance());
            for (ASMDataTable.ASMData asmData : discoverer.getASMTable()
                .getAll(RecipeCategoryHolder.class.getName())) {
                try {
                    Object obj = Class.forName(asmData.getClassName())
                        .getDeclaredField(asmData.getObjectName())
                        .get(null);
                    if (obj instanceof RecipeCategory recipeCategory) {
                        ret.add(recipeCategory);
                    } else {
                        GT_FML_LOGGER.error(
                            "{}#{} is not an instance of RecipeCategory",
                            asmData.getClassName(),
                            asmData.getObjectName());
                    }
                } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
                    GT_FML_LOGGER.error("Failed to find RecipeCategory");
                    GT_FML_LOGGER.catching(e);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }
}
