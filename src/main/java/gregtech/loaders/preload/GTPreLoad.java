package gregtech.loaders.preload;

import static gregtech.GTMod.GT_FML_LOGGER;
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
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.recipe.RecipeCategoryHolder;
import gregtech.api.recipe.RecipeCategorySetting;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.GTProxy;
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
import gregtech.common.tileentities.machines.long_distance.MTELongDistancePipelineBase;
import gregtech.common.tileentities.machines.multi.MTECleanroom;

public class GTPreLoad {

    public static void sortToTheEnd() {
        try {
            GT_FML_LOGGER.info("GTMod: Sorting GregTech to the end of the Mod List for further processing.");
            LoadController tLoadController = (LoadController) GTUtility
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
            Objects.requireNonNull(GTUtility.getField(tLoadController, "activeModList", true, true))
                .set(tLoadController, tNewModsList);
        } catch (Throwable e) {
            GTMod.logStackTrace(e);
        }
    }

    public static void initLocalization(File languageDir) {
        GT_FML_LOGGER.info("GTMod: Generating Lang-File");

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
                GTLanguageManager.isEN_US = true;
                GTLanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
            } else {
                String l10nFileName = "GregTech_" + userLang + ".lang";
                File l10nFile = new File(languageDir, l10nFileName);
                if (l10nFile.isFile()) {
                    GT_FML_LOGGER.info("Loading l10n file: " + l10nFileName);
                    GTLanguageManager.sEnglishFile = new Configuration(l10nFile);
                } else {
                    GT_FML_LOGGER.info("Cannot find l10n file " + l10nFileName + ", fallback to GregTech.lang");
                    GTLanguageManager.isEN_US = true;
                    GTLanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
                }
            }
        } else {
            GTLanguageManager.isEN_US = true;
            GTLanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
        }
        GTLanguageManager.sEnglishFile.load();

        Materials.getMaterialsMap()
            .values()
            .parallelStream()
            .filter(Objects::nonNull)
            .forEach(
                aMaterial -> aMaterial.mLocalizedName = GTLanguageManager
                    .addStringLocalization("Material." + aMaterial.mName.toLowerCase(), aMaterial.mDefaultLocalName));
    }

    public static void getConfiguration(File configDir) {
        File tFile = new File(new File(configDir, "GregTech"), "IDs.cfg");
        GTConfig.sConfigFileIDs = new Configuration(tFile);
        GTConfig.sConfigFileIDs.load();
        GTConfig.sConfigFileIDs.save();

        tFile = new File(new File(configDir, "GregTech"), "Cleanroom.cfg");
        GTConfig.cleanroomFile = new Configuration(tFile);
        GTConfig.cleanroomFile.load();
        GTConfig.cleanroomFile.save();

        tFile = new File(new File(configDir, "GregTech"), "UndergroundFluids.cfg");
        GTConfig.undergroundFluidsFile = new Configuration(tFile);
        GTConfig.undergroundFluidsFile.load();
        GTConfig.undergroundFluidsFile.save();

        GregTechAPI.NEIClientFIle = new GTConfig(
            new Configuration(new File(new File(configDir, "GregTech"), "NEIClient.cfg")));

    }

    public static void createLogFiles(File parentFile) {
        GTLog.mLogFile = new File(parentFile, "logs/GregTech.log");
        if (!GTLog.mLogFile.exists()) {
            try {
                GTLog.mLogFile.createNewFile();
            } catch (Throwable ignored) {}
        }
        try {
            GTLog.out = GTLog.err = new PrintStream(GTLog.mLogFile);
        } catch (FileNotFoundException ignored) {}

        if (ConfigGeneral.loggingOreDict) {
            GTLog.mOreDictLogFile = new File(parentFile, "logs/OreDict.log");
            if (!GTLog.mOreDictLogFile.exists()) {
                try {
                    GTLog.mOreDictLogFile.createNewFile();
                } catch (Throwable ignored) {}
            }
            List<String> tList = ((GTLog.LogBuffer) GTLog.ore).mBufferedOreDictLog;
            try {
                GTLog.ore = new PrintStream(GTLog.mOreDictLogFile);
            } catch (Throwable ignored) {}
            GTLog.ore.println("******************************************************************************");
            GTLog.ore.println("* This is the complete log of the GT5-Unofficial OreDictionary Handler. It   *");
            GTLog.ore.println("* processes all OreDictionary entries and can sometimes cause errors. All    *");
            GTLog.ore.println("* entries and errors are being logged. If you see an error please raise an   *");
            GTLog.ore.println("* issue at https://github.com/GTNewHorizons/GT-New-Horizons-Modpack/issues.  *");
            GTLog.ore.println("******************************************************************************");
            tList.forEach(GTLog.ore::println);
        }
        if (ConfigGeneral.loggingExplosions) {
            GTLog.mExplosionLog = new File(parentFile, "logs/Explosion.log");
            if (!GTLog.mExplosionLog.exists()) {
                try {
                    GTLog.mExplosionLog.createNewFile();
                } catch (Throwable ignored) {}
            }
            try {
                GTLog.exp = new PrintStream(GTLog.mExplosionLog);
            } catch (Throwable ignored) {}
        }

        if (ConfigGeneral.loggingPlayerActicity) {
            GTLog.mPlayerActivityLogFile = new File(parentFile, "logs/PlayerActivity.log");
            if (!GTLog.mPlayerActivityLogFile.exists()) {
                try {
                    GTLog.mPlayerActivityLogFile.createNewFile();
                } catch (Throwable ignored) {}
            }
            try {
                GTLog.pal = new PrintStream(GTLog.mPlayerActivityLogFile);
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
                                if (GregTechAPI.sGeneratedMaterials[material] != null) {
                                    tag += GregTechAPI.sGeneratedMaterials[material].mName;
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
                if (GTValues.D1) GT_FML_LOGGER.info("oretag: " + test);
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
        GT_FML_LOGGER.info("GTMod: Removing all original Scrapbox Drops.");
        try {
            Objects.requireNonNull(GTUtility.getField("ic2.core.item.ItemScrapbox$Drop", "topChance", true, true))
                .set(null, 0);
            ((List<?>) Objects.requireNonNull(
                GTUtility.getFieldContent(
                    GTUtility.getFieldContent("ic2.api.recipe.Recipes", "scrapboxDrops", true, true),
                    "drops",
                    true,
                    true))).clear();
        } catch (Throwable e) {
            if (GTValues.D1) {
                e.printStackTrace(GTLog.err);
            }
        }
        GTLog.out.println("GTMod: Adding Scrap with a Weight of 200.0F to the Scrapbox Drops.");
        GTModHandler.addScrapboxDrop(200.0F, GTModHandler.getIC2Item("scrap", 1L));
    }

    public static void loadConfig() {
        // general
        GTValues.D1 = ConfigDebug.D1;
        GTValues.D2 = ConfigDebug.D2;
        GTValues.allow_broken_recipemap = ConfigDebug.allowBrokenRecipeMap;
        GTValues.debugCleanroom = ConfigDebug.debugCleanroom;
        GTValues.debugDriller = ConfigDebug.debugDriller;
        GTValues.debugWorldGen = ConfigDebug.debugWorldgen;
        GTValues.debugOrevein = ConfigDebug.debugOrevein;
        GTValues.debugSmallOres = ConfigDebug.debugSmallOres;
        GTValues.debugStones = ConfigDebug.debugStones;
        GTValues.debugBlockMiner = ConfigDebug.debugBlockMiner;
        GTValues.debugBlockPump = ConfigDebug.debugBlockPump;
        GTValues.debugEntityCramming = ConfigDebug.debugEntityCramming;
        GTValues.debugWorldData = ConfigDebug.debugWorldData;
        GTValues.oreveinPercentage = ConfigGeneral.oreveinPercentage;
        GTValues.oreveinAttempts = ConfigGeneral.oreveinAttempts;
        GTValues.oreveinMaxPlacementAttempts = ConfigGeneral.oreveinMaxPlacementAttempts;
        GTValues.oreveinPlacerOres = ConfigGeneral.oreveinPlacerOres;
        GTValues.oreveinPlacerOresMultiplier = ConfigGeneral.oreveinPlacerOresMultiplier;
        GregTechAPI.TICKS_FOR_LAG_AVERAGING = ConfigGeneral.ticksForLagAveraging;
        GregTechAPI.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = ConfigGeneral.millisecondThesholdUntilLagWarning;
        GregTechAPI.sTimber = ConfigGeneral.timber;
        GregTechAPI.sDrinksAlwaysDrinkable = ConfigGeneral.drinksAlwaysDrinkable;
        GregTechAPI.sDoShowAllItemsInCreative = ConfigGeneral.doShowAllItemsInCreative;
        GregTechAPI.sMultiThreadedSounds = ConfigGeneral.multiThreadedSounds;
        GTMod.gregtechproxy.mMaxEqualEntitiesAtOneSpot = ConfigGeneral.maxEqualEntitiesAtOneSpot;
        GTMod.gregtechproxy.mFlintChance = ConfigGeneral.flintChance;
        GTMod.gregtechproxy.mItemDespawnTime = ConfigGeneral.itemDespawnTime;
        GTMod.gregtechproxy.mAllowSmallBoilerAutomation = ConfigGeneral.allowSmallBoilerAutomation;
        GTMod.gregtechproxy.mDisableVanillaOres = gregtech.common.config.worldgen.ConfigGeneral.disableVanillaOres;
        GTMod.gregtechproxy.mIncreaseDungeonLoot = ConfigGeneral.increaseDungeonLoot;
        GTMod.gregtechproxy.mAxeWhenAdventure = ConfigGeneral.axeWhenAdventure;
        GTMod.gregtechproxy.mSurvivalIntoAdventure = ConfigGeneral.survivalIntoAdventure;
        GTMod.gregtechproxy.mHungerEffect = ConfigGeneral.hungerEffect;
        GTMod.gregtechproxy.mInventoryUnification = ConfigGeneral.inventoryUnification;
        GTMod.gregtechproxy.mGTBees = ConfigGeneral.GTBees;
        GTMod.gregtechproxy.mCraftingUnification = ConfigGeneral.craftingUnification;
        GTMod.gregtechproxy.mNerfedWoodPlank = ConfigGeneral.nerfedWoodPlank;
        GTMod.gregtechproxy.mNerfedVanillaTools = ConfigGeneral.nerfedVanillaTools;
        GTMod.gregtechproxy.mAchievements = ConfigGeneral.achievements;
        GTMod.gregtechproxy.mHideUnusedOres = ConfigGeneral.hideUnusedOres;
        GTMod.gregtechproxy.mEnableAllMaterials = ConfigGeneral.enableAllMaterials;
        GTMod.gregtechproxy.mExplosionItemDrop = ConfigGeneral.explosionItemDrop;
        GTMod.gregtechproxy.mEnableCleanroom = ConfigGeneral.enableCleanroom;
        GTMod.gregtechproxy.mLowGravProcessing = GalacticraftCore.isModLoaded() && ConfigGeneral.lowGravProcessing;
        GTMod.gregtechproxy.mCropNeedBlock = ConfigGeneral.cropNeedBlock;
        GTMod.gregtechproxy.mAMHInteraction = ConfigGeneral.autoMaintenaceHatchesInteraction;
        GTMod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre = ConfigGeneral.mixedOreOnlyYieldsTwoThirdsOfPureOre;
        GTMod.gregtechproxy.mRichOreYieldMultiplier = ConfigGeneral.richOreYieldMultiplier;
        GTMod.gregtechproxy.mNetherOreYieldMultiplier = ConfigGeneral.netherOreYieldMultiplier;
        GTMod.gregtechproxy.mEndOreYieldMultiplier = ConfigGeneral.endOreYieldMultiplier;
        GTMod.gregtechproxy.gt6Pipe = ConfigGeneral.gt6Pipe;
        GTMod.gregtechproxy.gt6Cable = ConfigGeneral.gt6Cable;
        GTMod.gregtechproxy.ic2EnergySourceCompat = ConfigGeneral.ic2EnergySourceCompat;
        GTMod.gregtechproxy.costlyCableConnection = ConfigGeneral.costlyCableConnection;
        GTMod.gregtechproxy.crashOnNullRecipeInput = ConfigGeneral.crashOnNullRecipeInput;
        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            GTMod.gregtechproxy.crashOnNullRecipeInput = false; // Use flags in GTRecipeBuilder instead!
        }
        GTLanguageManager.i18nPlaceholder = ConfigGeneral.i18nPlaceholder;
        MTELongDistancePipelineBase.minimalDistancePoints = ConfigGeneral.minimalDistancePoints;
        GTValues.mCTMEnabledBlock.addAll(Arrays.asList(ConfigGeneral.CTMWhitelist));
        GTValues.mCTMDisabledBlock.addAll(Arrays.asList(ConfigGeneral.CTMBlacklist));
        if (ConfigGeneral.harderMobSpawner) {
            Blocks.mob_spawner.setHardness(500.0F)
                .setResistance(6000000.0F);
        }

        // machines
        GTValues.ticksBetweenSounds = ConfigMachines.ticksBetweenSounds;
        GTValues.blacklistedTileEntiyClassNamesForWA = ConfigMachines.blacklistedTileEntiyClassNamesForWA;
        GTValues.cleanroomGlass = ConfigMachines.cleanroomGlass;
        GTValues.enableChunkloaders = ConfigMachines.enableChunkloaders;
        GTValues.alwaysReloadChunkloaders = ConfigMachines.alwaysReloadChunkloaders;
        GTValues.debugChunkloaders = ConfigDebug.debugChunkloaders;
        GTValues.disableDigitalChestsExternalAccess = ConfigMachines.disableDigitalChestsExternalAccess;
        GTValues.enableMultiTileEntities = ConfigMachines.enableMultiTileEntities
            || (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
        GregTechAPI.sMachineExplosions = ConfigMachines.machineExplosions;
        GregTechAPI.sMachineFlammable = ConfigMachines.machineFlammable;
        GregTechAPI.sMachineNonWrenchExplosions = ConfigMachines.machineNonWrenchExplosions;
        GregTechAPI.sMachineWireFire = ConfigMachines.machineWireFire;
        GregTechAPI.sMachineFireExplosions = ConfigMachines.machineFireExplosions;
        GregTechAPI.sMachineRainExplosions = ConfigMachines.machineRainExplosions;
        GregTechAPI.sMachineThunderExplosions = ConfigMachines.machineThunderExplosions;
        GregTechAPI.sColoredGUI = ConfigMachines.coloredGUI;
        GregTechAPI.sMachineMetalGUI = ConfigMachines.machineMetalGUI;
        // Implementation for this is actually handled in NewHorizonsCoreMod in MainRegistry.java!
        GregTechAPI.sUseMachineMetal = ConfigMachines.useMachineMetal;

        // client
        loadClientConfig();

        // Pollution
        GTMod.gregtechproxy.mPollution = ConfigPollution.pollution;
        GTMod.gregtechproxy.mPollutionSmogLimit = ConfigPollution.pollutionSmogLimit;
        GTMod.gregtechproxy.mPollutionPoisonLimit = ConfigPollution.pollutionPoisonLimit;
        GTMod.gregtechproxy.mPollutionVegetationLimit = ConfigPollution.pollutionVegetationLimit;
        GTMod.gregtechproxy.mPollutionSourRainLimit = ConfigPollution.pollutionSourRainLimit;
        GTMod.gregtechproxy.mPollutionOnExplosion = ConfigPollution.pollutionOnExplosion;
        GTMod.gregtechproxy.mPollutionPrimitveBlastFurnacePerSecond = ConfigPollution.pollutionPrimitveBlastFurnacePerSecond;
        GTMod.gregtechproxy.mPollutionCharcoalPitPerSecond = ConfigPollution.pollutionCharcoalPitPerSecond;
        GTMod.gregtechproxy.mPollutionEBFPerSecond = ConfigPollution.pollutionEBFPerSecond;
        GTMod.gregtechproxy.mPollutionLargeCombustionEnginePerSecond = ConfigPollution.pollutionLargeCombustionEnginePerSecond;
        GTMod.gregtechproxy.mPollutionExtremeCombustionEnginePerSecond = ConfigPollution.pollutionExtremeCombustionEnginePerSecond;
        GTMod.gregtechproxy.mPollutionImplosionCompressorPerSecond = ConfigPollution.pollutionImplosionCompressorPerSecond;
        GTMod.gregtechproxy.mPollutionLargeBronzeBoilerPerSecond = ConfigPollution.pollutionLargeBronzeBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionLargeSteelBoilerPerSecond = ConfigPollution.pollutionLargeSteelBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond = ConfigPollution.pollutionLargeTitaniumBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionLargeTungstenSteelBoilerPerSecond = ConfigPollution.pollutionLargeTungstenSteelBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionReleasedByThrottle = ConfigPollution.pollutionReleasedByThrottle;
        GTMod.gregtechproxy.mPollutionLargeGasTurbinePerSecond = ConfigPollution.pollutionLargeGasTurbinePerSecond;
        GTMod.gregtechproxy.mPollutionMultiSmelterPerSecond = ConfigPollution.pollutionMultiSmelterPerSecond;
        GTMod.gregtechproxy.mPollutionPyrolyseOvenPerSecond = ConfigPollution.pollutionPyrolyseOvenPerSecond;
        GTMod.gregtechproxy.mPollutionSmallCoalBoilerPerSecond = ConfigPollution.pollutionSmallCoalBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond = ConfigPollution.pollutionHighPressureLavaBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionHighPressureCoalBoilerPerSecond = ConfigPollution.pollutionHighPressureCoalBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionBaseDieselGeneratorPerSecond = ConfigPollution.pollutionBaseDieselGeneratorPerSecond;
        double[] mPollutionDieselGeneratorReleasedByTier = ConfigPollution.pollutionDieselGeneratorReleasedByTier;
        if (mPollutionDieselGeneratorReleasedByTier.length
            == GTMod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier.length) {
            GTMod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier = mPollutionDieselGeneratorReleasedByTier;
        } else {
            GT_FML_LOGGER
                .error("The Length of the Diesel Turbine Pollution Array Config must be the same as the Default");
        }
        GTMod.gregtechproxy.mPollutionBaseGasTurbinePerSecond = ConfigPollution.pollutionBaseGasTurbinePerSecond;
        double[] mPollutionGasTurbineReleasedByTier = ConfigPollution.pollutionGasTurbineReleasedByTier;
        if (mPollutionGasTurbineReleasedByTier.length
            == GTMod.gregtechproxy.mPollutionGasTurbineReleasedByTier.length) {
            GTMod.gregtechproxy.mPollutionGasTurbineReleasedByTier = mPollutionGasTurbineReleasedByTier;
        } else {
            GT_FML_LOGGER.error("The Length of the Gas Turbine Pollution Array Config must be the same as the Default");
        }

        // cleanroom file
        if (GTMod.gregtechproxy.mEnableCleanroom) MTECleanroom.loadConfig(GTConfig.cleanroomFile);

        // underground fluids file
        GTMod.gregtechproxy.mUndergroundOil.getConfig(GTConfig.undergroundFluidsFile, "undergroundfluid");

        // Worldgeneration.cfg
        GTMod.gregtechproxy.enableUndergroundGravelGen = gregtech.common.config.worldgen.ConfigGeneral.generateUndergroundGravelGen;
        GTMod.gregtechproxy.enableUndergroundDirtGen = gregtech.common.config.worldgen.ConfigGeneral.generateUndergroundDirtGen;
        GTMod.gregtechproxy.enableBlackGraniteOres = gregtech.common.config.worldgen.ConfigGeneral.generateBlackGraniteOres;
        GTMod.gregtechproxy.enableRedGraniteOres = gregtech.common.config.worldgen.ConfigGeneral.generateBlackGraniteOres;
        GTMod.gregtechproxy.enableMarbleOres = gregtech.common.config.worldgen.ConfigGeneral.generateMarbleOres;
        GTMod.gregtechproxy.enableBasaltOres = gregtech.common.config.worldgen.ConfigGeneral.generateBasaltOres;

        // OverpoweredStuff.cfg
        GregTechAPI.mOutputRF = gregtech.common.config.opstuff.ConfigGeneral.outputRF;
        GregTechAPI.mInputRF = gregtech.common.config.opstuff.ConfigGeneral.inputRF;
        GregTechAPI.mEUtoRF = gregtech.common.config.opstuff.ConfigGeneral.howMuchRFWith100EUInInput;
        GregTechAPI.mRFtoEU = gregtech.common.config.opstuff.ConfigGeneral.howMuchEUWith100RFInInput;
        GregTechAPI.mRFExplosions = gregtech.common.config.opstuff.ConfigGeneral.RFExplosions;

        // MachineStats.cfg
        GTMod.gregtechproxy.mForceFreeFace = gregtech.common.config.machinestats.ConfigMachines.forceFreeFace;

        // ore_drop_behavior
        try {
            GTLog.out.println("Trying to set it to: " + ConfigOreDropBehavior.setting);
            GTMod.gregtechproxy.oreDropSystem = GTProxy.OreDropSystem.valueOf(ConfigOreDropBehavior.setting);;
        } catch (IllegalArgumentException e) {
            GTLog.err.println(e);
            GTMod.gregtechproxy.oreDropSystem = GTProxy.OreDropSystem.FortuneItem;
        }

        // features
        GTMod.gregtechproxy.mUpgradeCount = Math.min(64, Math.max(1, ConfigFeatures.upgradeStackSize));
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

        GTRecipeBuilder.onConfigLoad();
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
        GTMod.gregtechproxy.mRenderTileAmbientOcclusion = ConfigRender.renderTileAmbientOcclusion;
        GTMod.gregtechproxy.mRenderGlowTextures = ConfigRender.renderGlowTextures;
        GTMod.gregtechproxy.mRenderFlippedMachinesFlipped = ConfigRender.renderFlippedMachinesFlipped;
        GTMod.gregtechproxy.mRenderIndicatorsOnHatch = ConfigRender.renderIndicatorsOnHatch;
        GTMod.gregtechproxy.mRenderDirtParticles = ConfigRender.renderDirtParticles;
        GTMod.gregtechproxy.mRenderPollutionFog = ConfigRender.renderPollutionFog;
        GTMod.gregtechproxy.mRenderItemDurabilityBar = ConfigRender.renderItemDurabilityBar;
        GTMod.gregtechproxy.mRenderItemChargeBar = ConfigRender.renderItemChargeBar;
        GTMod.gregtechproxy.mUseBlockUpdateHandler = ConfigRender.useBlockUpdateHandler;

        GTMod.gregtechproxy.mCoverTabsVisible = ConfigInterface.coverTabsVisible;
        GTMod.gregtechproxy.mCoverTabsFlipped = ConfigInterface.coverTabsFlipped;
        GTMod.gregtechproxy.mTooltipVerbosity = ConfigInterface.tooltipVerbosity;
        GTMod.gregtechproxy.mTooltipShiftVerbosity = ConfigInterface.tooltipShiftVerbosity;
        GTMod.gregtechproxy.mTitleTabStyle = ConfigInterface.titleTabStyle;

        GTMod.gregtechproxy.mNEIRecipeSecondMode = GregTechAPI.NEIClientFIle.get("nei", "RecipeSecondMode", true);
        GTMod.gregtechproxy.mNEIRecipeOwner = GregTechAPI.NEIClientFIle.get("nei", "RecipeOwner", false);
        GTMod.gregtechproxy.mNEIRecipeOwnerStackTrace = GregTechAPI.NEIClientFIle
            .get("nei", "RecipeOwnerStackTrace", false);
        GTMod.gregtechproxy.mNEIOriginalVoltage = GregTechAPI.NEIClientFIle.get("nei", "OriginalVoltage", false);

        GTMod.gregtechproxy.recipeCategorySettings.clear();
        for (RecipeCategory recipeCategory : findRecipeCategories()) {
            RecipeCategorySetting setting = RecipeCategorySetting.find(
                GregTechAPI.NEIClientFIle.getWithValidValues(
                    "nei.recipe_categories",
                    recipeCategory.unlocalizedName,
                    RecipeCategorySetting.NAMES,
                    RecipeCategorySetting.getDefault()
                        .toName()));
            GTMod.gregtechproxy.recipeCategorySettings.put(recipeCategory, setting);
        }

        GTMod.gregtechproxy.mWailaTransformerVoltageTier = ConfigWaila.wailaTransformerVoltageTier;
        GTMod.gregtechproxy.wailaAverageNS = ConfigWaila.wailaAverageNS;

        GTMod.gregtechproxy.reloadNEICache();
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
