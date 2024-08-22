package gregtech.loaders.preload;

import static gregtech.GT_Mod.GT_FML_LOGGER;
import static gregtech.GT_Mod.aTextGeneral;
import static gregtech.api.enums.Mods.CraftTweaker;
import static gregtech.api.enums.Mods.EnderIO;
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
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gregtech.common.config.ConfigDebug;
import gregtech.common.config.ConfigGeneral;
import gregtech.common.config.ConfigMachines;
import gregtech.common.config.ConfigPollution;
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
import gregtech.api.enums.ConfigCategories;
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

    public static Configuration getConfiguration(File configDir) {
        File tFile = new File(new File(configDir, "GregTech"), "GregTech.cfg");
        Configuration tMainConfig = new Configuration(tFile);
        tMainConfig.load();
        tFile = new File(new File(configDir, "GregTech"), "IDs.cfg");
        GT_Config.sConfigFileIDs = new Configuration(tFile);
        GT_Config.sConfigFileIDs.load();
        GT_Config.sConfigFileIDs.save();
        GregTech_API.sMachineFile = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "MachineStats.cfg")));
        GregTech_API.sWorldgenFile = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "WorldGeneration.cfg")));
        GregTech_API.sMaterialProperties = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "MaterialProperties.cfg")));
        GregTech_API.sUnification = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "Unification.cfg")));
        GregTech_API.sSpecialFile = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "Other.cfg")));
        GregTech_API.sOPStuff = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "OverpoweredStuff.cfg")));

        GregTech_API.sClientDataFile = new GT_Config(
            new Configuration(new File(new File(configDir, "GregTech"), "Client.cfg")));
        return tMainConfig;
    }

    public static void createLogFiles(File parentFile, Configuration tMainConfig) {
        GT_Log.mLogFile = new File(parentFile, "logs/GregTech.log");
        if (!GT_Log.mLogFile.exists()) {
            try {
                GT_Log.mLogFile.createNewFile();
            } catch (Throwable ignored) {}
        }
        try {
            GT_Log.out = GT_Log.err = new PrintStream(GT_Log.mLogFile);
        } catch (FileNotFoundException ignored) {}

        if (tMainConfig.get(GT_Mod.aTextGeneral, "LoggingOreDict", false)
            .getBoolean(false)) {
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
        if (tMainConfig.get(GT_Mod.aTextGeneral, "LoggingExplosions", true)
            .getBoolean(true)) {
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

        if (tMainConfig.get(GT_Mod.aTextGeneral, "LoggingPlayerActivity", true)
            .getBoolean(true)) {
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

    public static void loadConfig(Configuration tMainConfig) {
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
        GregTech_API.TICKS_FOR_LAG_AVERAGING = ConfigGeneral.TICKS_FOR_LAG_AVERAGING;
        GregTech_API.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = ConfigGeneral.MILLISECOND_THESHOLD_UNTIL_LAG_WARNING;
        GregTech_API.sTimber = ConfigGeneral.sTimber;
        GregTech_API.sDrinksAlwaysDrinkable = ConfigGeneral.sDrinksAlwaysDrinkable;
        GregTech_API.sDoShowAllItemsInCreative = ConfigGeneral.sDoShowAllItemsInCreative;
        GregTech_API.sMultiThreadedSounds = ConfigGeneral.sMultiThreadedSounds;
        GT_Mod.gregtechproxy.mMaxEqualEntitiesAtOneSpot = ConfigGeneral.mMaxEqualEntitiesAtOneSpot;
        GT_Mod.gregtechproxy.mFlintChance = ConfigGeneral.mFlintChance;
        GT_Mod.gregtechproxy.mItemDespawnTime = ConfigGeneral.mItemDespawnTime;
        GT_Mod.gregtechproxy.mAllowSmallBoilerAutomation = ConfigGeneral.mAllowSmallBoilerAutomation;
        GT_Mod.gregtechproxy.mDisableVanillaOres = ConfigGeneral.mDisableVanillaOres;
        GT_Mod.gregtechproxy.mIncreaseDungeonLoot = ConfigGeneral.mIncreaseDungeonLoot;
        GT_Mod.gregtechproxy.mAxeWhenAdventure = ConfigGeneral.mAxeWhenAdventure;
        GT_Mod.gregtechproxy.mSurvivalIntoAdventure = ConfigGeneral.mSurvivalIntoAdventure;
        GT_Mod.gregtechproxy.mHungerEffect = ConfigGeneral.mHungerEffect;
        GT_Mod.gregtechproxy.mInventoryUnification = ConfigGeneral.mInventoryUnification;
        GT_Mod.gregtechproxy.mGTBees = ConfigGeneral.mGTBees;
        GT_Mod.gregtechproxy.mCraftingUnification = ConfigGeneral.mCraftingUnification;
        GT_Mod.gregtechproxy.mNerfedWoodPlank = ConfigGeneral.mNerfedWoodPlank;
        GT_Mod.gregtechproxy.mNerfedVanillaTools = ConfigGeneral.mNerfedVanillaTools;
        GT_Mod.gregtechproxy.mAchievements = ConfigGeneral.mAchievements;
        GT_Mod.gregtechproxy.mHideUnusedOres = ConfigGeneral.mHideUnusedOres;
        GT_Mod.gregtechproxy.mEnableAllMaterials = ConfigGeneral.mEnableAllMaterials;
        GT_Mod.gregtechproxy.mExplosionItemDrop = tMainConfig
            .get("general", "ExplosionItemDrops", GT_Mod.gregtechproxy.mExplosionItemDrop)
            .getBoolean(GT_Mod.gregtechproxy.mExplosionItemDrop);

        // machines
        GT_Values.ticksBetweenSounds = ConfigMachines.ticksBetweenSounds;
        GT_Values.blacklistedTileEntiyClassNamesForWA = ConfigMachines.blacklistedTileEntiyClassNamesForWA;
        GT_Values.cleanroomGlass = ConfigMachines.cleanroomGlass;
        GT_Values.enableChunkloaders = ConfigMachines.enableChunkloaders;
        GT_Values.alwaysReloadChunkloaders = ConfigMachines.alwaysReloadChunkloaders;
        GT_Values.debugChunkloaders = ConfigDebug.debugChunkloaders;
        GT_Values.disableDigitalChestsExternalAccess = ConfigMachines.disableDigitalChestsExternalAccess;
        GT_Values.enableMultiTileEntities = ConfigMachines.enableMultiTileEntities || (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        GregTech_API.sMachineExplosions = ConfigMachines.sMachineExplosions;
        GregTech_API.sMachineFlammable = ConfigMachines.sMachineFlammable;
        GregTech_API.sMachineNonWrenchExplosions = ConfigMachines.sMachineNonWrenchExplosions;
        GregTech_API.sMachineWireFire = ConfigMachines.sMachineWireFire;
        GregTech_API.sMachineFireExplosions = ConfigMachines.sMachineFireExplosions;
        GregTech_API.sMachineRainExplosions = ConfigMachines.sMachineRainExplosions;
        GregTech_API.sMachineThunderExplosions = ConfigMachines.sMachineThunderExplosions;
        GregTech_API.sColoredGUI = ConfigMachines.sColoredGUI;
        GregTech_API.sMachineMetalGUI = ConfigMachines.sMachineMetalGUI;
        // Implementation for this is actually handled in NewHorizonsCoreMod in MainRegistry.java!
        GregTech_API.sUseMachineMetal = ConfigMachines.sUseMachineMetal;

        // client
        loadClientConfig();

        // Pollution
        GT_Mod.gregtechproxy.mPollution = ConfigPollution.mPollution;
        GT_Mod.gregtechproxy.mPollutionSmogLimit = ConfigPollution.mPollutionSmogLimit;
        GT_Mod.gregtechproxy.mPollutionPoisonLimit = ConfigPollution.mPollutionPoisonLimit;
        GT_Mod.gregtechproxy.mPollutionVegetationLimit = ConfigPollution.mPollutionVegetationLimit;
        GT_Mod.gregtechproxy.mPollutionSourRainLimit = ConfigPollution.mPollutionSourRainLimit;
        GT_Mod.gregtechproxy.mPollutionOnExplosion = ConfigPollution.mPollutionOnExplosion;
        GT_Mod.gregtechproxy.mPollutionPrimitveBlastFurnacePerSecond = ConfigPollution.mPollutionPrimitveBlastFurnacePerSecond;
        GT_Mod.gregtechproxy.mPollutionCharcoalPitPerSecond = ConfigPollution.mPollutionCharcoalPitPerSecond;
        GT_Mod.gregtechproxy.mPollutionEBFPerSecond = ConfigPollution.mPollutionEBFPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeCombustionEnginePerSecond = ConfigPollution.mPollutionLargeCombustionEnginePerSecond;
        GT_Mod.gregtechproxy.mPollutionExtremeCombustionEnginePerSecond = ConfigPollution.mPollutionExtremeCombustionEnginePerSecond;
        GT_Mod.gregtechproxy.mPollutionImplosionCompressorPerSecond = ConfigPollution.mPollutionImplosionCompressorPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeBronzeBoilerPerSecond = ConfigPollution.mPollutionLargeBronzeBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeSteelBoilerPerSecond = ConfigPollution.mPollutionLargeSteelBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond = ConfigPollution.mPollutionLargeTitaniumBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionLargeTungstenSteelBoilerPerSecond = ConfigPollution.mPollutionLargeTungstenSteelBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionReleasedByThrottle = ConfigPollution.mPollutionReleasedByThrottle;
        GT_Mod.gregtechproxy.mPollutionLargeGasTurbinePerSecond = ConfigPollution.mPollutionLargeGasTurbinePerSecond;
        GT_Mod.gregtechproxy.mPollutionMultiSmelterPerSecond = ConfigPollution.mPollutionMultiSmelterPerSecond;
        GT_Mod.gregtechproxy.mPollutionPyrolyseOvenPerSecond = ConfigPollution.mPollutionPyrolyseOvenPerSecond;
        GT_Mod.gregtechproxy.mPollutionSmallCoalBoilerPerSecond = ConfigPollution.mPollutionSmallCoalBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond = ConfigPollution.mPollutionHighPressureLavaBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionHighPressureCoalBoilerPerSecond = ConfigPollution.mPollutionHighPressureCoalBoilerPerSecond;
        GT_Mod.gregtechproxy.mPollutionBaseDieselGeneratorPerSecond = ConfigPollution.mPollutionBaseDieselGeneratorPerSecond;
        double[] mPollutionDieselGeneratorReleasedByTier = tMainConfig
            .get(
                "Pollution",
                "PollutionReleasedByTierDieselGenerator",
                GT_Mod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier)
            .getDoubleList();
        if (mPollutionDieselGeneratorReleasedByTier.length
            == GT_Mod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier.length) {
            GT_Mod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier = mPollutionDieselGeneratorReleasedByTier;
        } else {
            GT_FML_LOGGER
                .error("The Length of the Diesel Turbine Pollution Array Config must be the same as the Default");
        }
        GT_Mod.gregtechproxy.mPollutionBaseGasTurbinePerSecond = ConfigPollution.mPollutionBaseGasTurbinePerSecond;
        double[] mPollutionGasTurbineReleasedByTier = tMainConfig
            .get(
                "Pollution",
                "PollutionReleasedByTierGasTurbineGenerator",
                GT_Mod.gregtechproxy.mPollutionGasTurbineReleasedByTier)
            .getDoubleList();
        if (mPollutionGasTurbineReleasedByTier.length
            == GT_Mod.gregtechproxy.mPollutionGasTurbineReleasedByTier.length) {
            GT_Mod.gregtechproxy.mPollutionGasTurbineReleasedByTier = mPollutionGasTurbineReleasedByTier;
        } else {
            GT_FML_LOGGER.error("The Length of the Gas Turbine Pollution Array Config must be the same as the Default");
        }

        GT_Mod.gregtechproxy.mUndergroundOil.getConfig(tMainConfig, "undergroundfluid");
        GT_Mod.gregtechproxy.enableUndergroundGravelGen = GregTech_API.sWorldgenFile
            .get("general", "enableUndergroundGravelGen", GT_Mod.gregtechproxy.enableUndergroundGravelGen);
        GT_Mod.gregtechproxy.enableUndergroundDirtGen = GregTech_API.sWorldgenFile
            .get("general", "enableUndergroundDirtGen", GT_Mod.gregtechproxy.enableUndergroundDirtGen);
        GT_Mod.gregtechproxy.mEnableCleanroom = tMainConfig.get("general", "EnableCleanroom", true)
            .getBoolean(true);
        if (GT_Mod.gregtechproxy.mEnableCleanroom) GT_MetaTileEntity_Cleanroom.loadConfig(tMainConfig);
        GT_Mod.gregtechproxy.mLowGravProcessing = GalacticraftCore.isModLoaded()
            && tMainConfig.get("general", "LowGravProcessing", true)
                .getBoolean(true);
        GT_Mod.gregtechproxy.mUseGreatlyShrukenReplacementList = tMainConfig
            .get("general", "GTNH Optimised Material Loading", true)
            .getBoolean(true);
        Calendar now = Calendar.getInstance();
        GT_Mod.gregtechproxy.mAprilFool = GregTech_API.sSpecialFile.get(
            ConfigCategories.general,
            "AprilFool",
            now.get(Calendar.MONTH) == Calendar.APRIL && now.get(Calendar.DAY_OF_MONTH) == 1);
        GT_Mod.gregtechproxy.mCropNeedBlock = tMainConfig.get("general", "CropNeedBlockBelow", true)
            .getBoolean(true);
        GT_Mod.gregtechproxy.mAMHInteraction = tMainConfig.get("general", "AllowAutoMaintenanceHatchInteraction", false)
            .getBoolean(false);
        GregTech_API.mOutputRF = GregTech_API.sOPStuff.get(ConfigCategories.general, "OutputRF", true);
        GregTech_API.mInputRF = GregTech_API.sOPStuff.get(ConfigCategories.general, "InputRF", false);
        GregTech_API.mEUtoRF = GregTech_API.sOPStuff.get(ConfigCategories.general, "100EUtoRF", 360);
        GregTech_API.mRFtoEU = GregTech_API.sOPStuff.get(ConfigCategories.general, "100RFtoEU", 20);
        GregTech_API.mRFExplosions = GregTech_API.sOPStuff.get(ConfigCategories.general, "RFExplosions", false);
        GregTech_API.meIOLoaded = EnderIO.isModLoaded();
        GT_Mod.gregtechproxy.mForceFreeFace = GregTech_API.sMachineFile
            .get(ConfigCategories.machineconfig, "forceFreeFace", true);
        GT_Mod.gregtechproxy.mBrickedBlastFurnace = tMainConfig.get("general", "BrickedBlastFurnace", true)
            .getBoolean(true);

        GT_Mod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre = tMainConfig
            .get("general", "MixedOreOnlyYieldsTwoThirdsOfPureOre", false)
            .getBoolean(false);
        GT_Mod.gregtechproxy.mRichOreYieldMultiplier = tMainConfig.get("general", "RichOreYieldMultiplier", true)
            .getBoolean(false);
        GT_Mod.gregtechproxy.mNetherOreYieldMultiplier = tMainConfig.get("general", "NetherOreYieldMultiplier", true)
            .getBoolean(false);
        GT_Mod.gregtechproxy.mEndOreYieldMultiplier = tMainConfig.get("general", "EndOreYieldMultiplier", true)
            .getBoolean(false);
        GT_Mod.gregtechproxy.enableBlackGraniteOres = GregTech_API.sWorldgenFile
            .get("general", "enableBlackGraniteOres", GT_Mod.gregtechproxy.enableBlackGraniteOres);
        GT_Mod.gregtechproxy.enableRedGraniteOres = GregTech_API.sWorldgenFile
            .get("general", "enableRedGraniteOres", GT_Mod.gregtechproxy.enableRedGraniteOres);
        GT_Mod.gregtechproxy.enableMarbleOres = GregTech_API.sWorldgenFile
            .get("general", "enableMarbleOres", GT_Mod.gregtechproxy.enableMarbleOres);
        GT_Mod.gregtechproxy.enableBasaltOres = GregTech_API.sWorldgenFile
            .get("general", "enableBasaltOres", GT_Mod.gregtechproxy.enableBasaltOres);
        GT_Mod.gregtechproxy.gt6Pipe = tMainConfig.get("general", "GT6StyledPipesConnection", true)
            .getBoolean(true);
        GT_Mod.gregtechproxy.gt6Cable = tMainConfig.get("general", "GT6StyledWiresConnection", true)
            .getBoolean(true);
        GT_Mod.gregtechproxy.ic2EnergySourceCompat = tMainConfig.get("general", "Ic2EnergySourceCompat", true)
            .getBoolean(true);
        GT_Mod.gregtechproxy.costlyCableConnection = tMainConfig
            .get("general", "CableConnectionRequiresSolderingMaterial", false)
            .getBoolean(false);
        GT_Mod.gregtechproxy.crashOnNullRecipeInput = tMainConfig.get("general", "crashOnNullRecipeInput", false)
            .getBoolean(false);
        GT_LanguageManager.i18nPlaceholder = tMainConfig
            .get("general", "EnablePlaceholderForMaterialNamesInLangFile", true)
            .getBoolean(true);
        GT_MetaTileEntity_LongDistancePipelineBase.minimalDistancePoints = tMainConfig
            .get("general", "LongDistancePipelineMinimalDistancePoints", 64)
            .getInt(64);
        try {
            String setting_string = tMainConfig.get(
                "OreDropBehaviour",
                "general",
                "FortuneItem",
                "Settings: \n'PerDimBlock': Sets the drop to the block variant of the ore block based on dimension, defaults to stone type, \n'UnifiedBlock': Sets the drop to the stone variant of the ore block, \n'Block': Sets the drop to the ore  mined, \n'FortuneItem': Sets the drop to the new ore item and makes it affected by fortune, \n'Item': Sets the drop to the new ore item, \nDefaults to: 'FortuneItem'")
                .getString();
            GT_Log.out.println("Trying to set it to: " + setting_string);
            GT_Proxy.OreDropSystem setting = GT_Proxy.OreDropSystem.valueOf(setting_string);
            GT_Mod.gregtechproxy.oreDropSystem = setting;

        } catch (IllegalArgumentException e) {
            GT_Log.err.println(e);
            GT_Mod.gregtechproxy.oreDropSystem = GT_Proxy.OreDropSystem.FortuneItem;
        }

        GT_Mod.gregtechproxy.mChangeHarvestLevels = GregTech_API.sMaterialProperties
            .get("havestLevel", "activateHarvestLevelChange", false); // TODO CHECK
        if (GT_Mod.gregtechproxy.mChangeHarvestLevels) {
            GT_Mod.gregtechproxy.mGraniteHavestLevel = GregTech_API.sMaterialProperties
                .get("havestLevel", "graniteHarvestLevel", 3);
            GT_Mod.gregtechproxy.mMaxHarvestLevel = Math
                .min(15, GregTech_API.sMaterialProperties.get("havestLevel", "maxLevel", 7));
            Materials.getMaterialsMap()
                .values()
                .parallelStream()
                .filter(
                    tMaterial -> tMaterial != null && tMaterial.mToolQuality > 0
                        && tMaterial.mMetaItemSubID < GT_Mod.gregtechproxy.mHarvestLevel.length
                        && tMaterial.mMetaItemSubID >= 0)
                .forEach(
                    tMaterial -> GT_Mod.gregtechproxy.mHarvestLevel[tMaterial.mMetaItemSubID] = GregTech_API.sMaterialProperties
                        .get("materialHavestLevel", tMaterial.mDefaultLocalName, tMaterial.mToolQuality));
        }

        if (tMainConfig.get("general", "hardermobspawners", true)
            .getBoolean(true)) {
            Blocks.mob_spawner.setHardness(500.0F)
                .setResistance(6000000.0F);
        }

        GT_Mod.gregtechproxy.mUpgradeCount = Math.min(
            64,
            Math.max(
                1,
                tMainConfig.get("features", "UpgradeStacksize", 4)
                    .getInt()));
        for (OrePrefixes tPrefix : OrePrefixes.values()) {
            if (tPrefix.mIsUsedForOreProcessing) {
                tPrefix.mDefaultStackSize = ((byte) Math.min(
                    64,
                    Math.max(
                        1,
                        tMainConfig.get("features", "MaxOreStackSize", 64)
                            .getInt())));
            } else if (tPrefix == OrePrefixes.plank) {
                tPrefix.mDefaultStackSize = ((byte) Math.min(
                    64,
                    Math.max(
                        16,
                        tMainConfig.get("features", "MaxPlankStackSize", 64)
                            .getInt())));
            } else if ((tPrefix == OrePrefixes.wood) || (tPrefix == OrePrefixes.treeLeaves)
                || (tPrefix == OrePrefixes.treeSapling)
                || (tPrefix == OrePrefixes.log)) {
                    tPrefix.mDefaultStackSize = ((byte) Math.min(
                        64,
                        Math.max(
                            16,
                            tMainConfig.get("features", "MaxLogStackSize", 64)
                                .getInt())));
                } else if (tPrefix.mIsUsedForBlocks) {
                    tPrefix.mDefaultStackSize = ((byte) Math.min(
                        64,
                        Math.max(
                            16,
                            tMainConfig.get("features", "MaxOtherBlockStackSize", 64)
                                .getInt())));
                }
        }

        GT_Values.mCTMEnabledBlock
            .addAll(
                Arrays
                    .asList(
                        tMainConfig
                            .get(
                                "general",
                                "ctm_block_whitelist",
                                new String[] { "team.chisel.block.BlockCarvable",
                                    "team.chisel.block.BlockCarvableGlass" })
                            .getStringList()));
        GT_Values.mCTMDisabledBlock.addAll(
            Arrays.asList(
                tMainConfig.get("general", "ctm_block_blacklist", new String[] { "team.chisel.block.BlockRoadLine" })
                    .getStringList()));

        GT_RecipeBuilder.onConfigLoad();
    }

    public static void loadClientConfig() {
        final String sBDye0 = "ColorModulation.";
        Arrays.stream(Dyes.values())
            .filter(tDye -> (tDye != Dyes._NULL) && (tDye.mIndex < 0))
            .forEach(tDye -> {
                String sBDye1 = sBDye0 + tDye;
                tDye.mRGBa[0] = ((short) Math
                    .min(255, Math.max(0, GregTech_API.sClientDataFile.get(sBDye1, "R", tDye.mOriginalRGBa[0]))));
                tDye.mRGBa[1] = ((short) Math
                    .min(255, Math.max(0, GregTech_API.sClientDataFile.get(sBDye1, "G", tDye.mOriginalRGBa[1]))));
                tDye.mRGBa[2] = ((short) Math
                    .min(255, Math.max(0, GregTech_API.sClientDataFile.get(sBDye1, "B", tDye.mOriginalRGBa[2]))));
            });
        GT_Mod.gregtechproxy.mRenderTileAmbientOcclusion = GregTech_API.sClientDataFile
            .get("render", "TileAmbientOcclusion", true);
        GT_Mod.gregtechproxy.mRenderGlowTextures = GregTech_API.sClientDataFile.get("render", "GlowTextures", true);
        GT_Mod.gregtechproxy.mRenderFlippedMachinesFlipped = GregTech_API.sClientDataFile
            .get("render", "RenderFlippedMachinesFlipped", true);
        GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch = GregTech_API.sClientDataFile
            .get("render", "RenderIndicatorsOnHatch", true);
        GT_Mod.gregtechproxy.mRenderDirtParticles = GregTech_API.sClientDataFile
            .get("render", "RenderDirtParticles", true);
        GT_Mod.gregtechproxy.mRenderPollutionFog = GregTech_API.sClientDataFile
            .get("render", "RenderPollutionFog", true);
        GT_Mod.gregtechproxy.mRenderItemDurabilityBar = GregTech_API.sClientDataFile
            .get("render", "RenderItemDurabilityBar", true);
        GT_Mod.gregtechproxy.mRenderItemChargeBar = GregTech_API.sClientDataFile
            .get("render", "RenderItemChargeBar", true);
        GT_Mod.gregtechproxy.mUseBlockUpdateHandler = GregTech_API.sClientDataFile
            .get("render", "UseBlockUpdateHandler", false);

        GT_Mod.gregtechproxy.mCoverTabsVisible = GregTech_API.sClientDataFile
            .get("interface", "DisplayCoverTabs", true);
        GT_Mod.gregtechproxy.mCoverTabsFlipped = GregTech_API.sClientDataFile.get("interface", "FlipCoverTabs", false);
        GT_Mod.gregtechproxy.mTooltipVerbosity = GregTech_API.sClientDataFile.get("interface", "TooltipVerbosity", 2);
        GT_Mod.gregtechproxy.mTooltipShiftVerbosity = GregTech_API.sClientDataFile
            .get("interface", "TooltipShiftVerbosity", 3);
        GT_Mod.gregtechproxy.mTitleTabStyle = GregTech_API.sClientDataFile.get("interface", "TitleTabStyle", 0);

        GT_Mod.gregtechproxy.mNEIRecipeSecondMode = GregTech_API.sClientDataFile.get("nei", "RecipeSecondMode", true);
        GT_Mod.gregtechproxy.mNEIRecipeOwner = GregTech_API.sClientDataFile.get("nei", "RecipeOwner", false);
        GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace = GregTech_API.sClientDataFile
            .get("nei", "RecipeOwnerStackTrace", false);
        GT_Mod.gregtechproxy.mNEIOriginalVoltage = GregTech_API.sClientDataFile.get("nei", "OriginalVoltage", false);

        GT_Mod.gregtechproxy.recipeCategorySettings.clear();
        for (RecipeCategory recipeCategory : findRecipeCategories()) {
            RecipeCategorySetting setting = RecipeCategorySetting.find(
                GregTech_API.sClientDataFile.getWithValidValues(
                    "nei.recipe_categories",
                    recipeCategory.unlocalizedName,
                    RecipeCategorySetting.NAMES,
                    RecipeCategorySetting.getDefault()
                        .toName()));
            GT_Mod.gregtechproxy.recipeCategorySettings.put(recipeCategory, setting);
        }

        GT_Mod.gregtechproxy.mWailaTransformerVoltageTier = GregTech_API.sClientDataFile
            .get("waila", "WailaTransformerVoltageTier", true);
        GT_Mod.gregtechproxy.wailaAverageNS = GregTech_API.sClientDataFile.get("waila", "WailaAverageNS", false);

        final String[] Circuits = GregTech_API.sClientDataFile.get("interface", "CircuitsOrder");
        GT_Mod.gregtechproxy.mCircuitsOrder.clear();
        for (int i = 0; i < Circuits.length; i++) {
            GT_Mod.gregtechproxy.mCircuitsOrder.putIfAbsent(Circuits[i], i);
        }

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
