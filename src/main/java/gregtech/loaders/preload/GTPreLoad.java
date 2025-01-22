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
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;
import gregtech.common.config.Gregtech;
import gregtech.common.config.MachineStats;
import gregtech.common.config.OPStuff;
import gregtech.common.config.Worldgen;
import gregtech.common.pollution.PollutionConfig;
import gregtech.common.tileentities.machines.long_distance.MTELongDistancePipelineBase;

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

        if (Gregtech.general.loggingOreDict) {
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
        if (Gregtech.general.loggingExplosions) {
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
                                    null, "ingot", "ingotHot", null, null, null, null, "plate", "plateDouble",
                                    "plateTriple", "plateQuadruple", "plateQuintuple", "plateDense", "stick", "lens",
                                    "round", "bolt", "screw", "ring", "foil", "cell", "cellPlasma", "cellMolten",
                                    "rawOre", "plateSuperdense" };
                                if (mIt == 2) tags = new String[] { "toolHeadSword", "toolHeadPickaxe",
                                    "toolHeadShovel", "toolHeadAxe", "toolHeadHoe", "toolHeadHammer", "toolHeadFile",
                                    "toolHeadSaw", "toolHeadDrill", "toolHeadChainsaw", "toolHeadWrench",
                                    "toolHeadUniversalSpade", "toolHeadSense", "toolHeadPlow", "toolHeadArrow",
                                    "toolHeadBuzzSaw", "turbineBlade", null, "itemCasing", "wireFine", "gearGtSmall",
                                    "rotor", "stickLong", "springSmall", "spring", "arrowGtWood", "arrowGtPlastic",
                                    "gemChipped", "gemFlawed", "gemFlawless", "gemExquisite", "gearGt" };
                                if (mIt == 3) tags = new String[] { "rawOre", "nanite", "plateSuperdense" };
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
            "crushedPurified", "crushedCentrifuged", "gem", "nugget", "ingot", "ingotHot", "plate", "plateDouble",
            "plateTriple", "plateQuadruple", "plateQuintuple", "plateDense", "stick", "lens", "round", "bolt", "screw",
            "ring", "foil", "cell", "cellPlasma", "toolHeadSword", "toolHeadPickaxe", "toolHeadShovel", "toolHeadAxe",
            "toolHeadHoe", "toolHeadHammer", "toolHeadFile", "toolHeadSaw", "toolHeadDrill", "toolHeadChainsaw",
            "toolHeadWrench", "toolHeadUniversalSpade", "toolHeadSense", "toolHeadPlow", "toolHeadArrow",
            "toolHeadBuzzSaw", "turbineBlade", "wireFine", "gearGtSmall", "rotor", "stickLong", "springSmall", "spring",
            "arrowGtWood", "arrowGtPlastic", "gemChipped", "gemFlawed", "gemFlawless", "gemExquisite", "gearGt",
            "nanite", "cellMolten", "rawOre", "plateSuperdense" };

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
            ((List<?>) Objects
                .requireNonNull(GTUtility.getFieldContent(ic2.api.recipe.Recipes.scrapboxDrops, "drops", true, true)))
                    .clear();
        } catch (Throwable e) {
            if (GTValues.D1) {
                e.printStackTrace(GTLog.err);
            }
        }
        GTLog.out.println("GTMod: Adding Scrap with a Weight of 200.0F to the Scrapbox Drops.");
        GTModHandler.addScrapboxDrop(200.0F, ItemList.IC2_Scrap.get(1L));
    }

    public static void loadConfig() {
        // general
        GTValues.D1 = Gregtech.debug.D1;
        GTValues.D2 = Gregtech.debug.D2;
        GTValues.allow_broken_recipemap = Gregtech.debug.allowBrokenRecipeMap;
        GTValues.debugCleanroom = Gregtech.debug.debugCleanroom;
        GTValues.debugDriller = Gregtech.debug.debugDriller;
        GTValues.debugWorldGen = Gregtech.debug.debugWorldgen;
        GTValues.debugOrevein = Gregtech.debug.debugOrevein;
        GTValues.debugSmallOres = Gregtech.debug.debugSmallOres;
        GTValues.debugStones = Gregtech.debug.debugStones;
        GTValues.debugBlockMiner = Gregtech.debug.debugBlockMiner;
        GTValues.debugBlockPump = Gregtech.debug.debugBlockPump;
        GTValues.debugEntityCramming = Gregtech.debug.debugEntityCramming;
        GTValues.debugWorldData = Gregtech.debug.debugWorldData;
        GTValues.oreveinPercentage = Gregtech.general.oreveinPercentage;
        GTValues.oreveinAttempts = Gregtech.general.oreveinAttempts;
        GTValues.oreveinMaxPlacementAttempts = Gregtech.general.oreveinMaxPlacementAttempts;
        GTValues.oreveinPlacerOres = Gregtech.general.oreveinPlacerOres;
        GTValues.oreveinPlacerOresMultiplier = Gregtech.general.oreveinPlacerOresMultiplier;
        GregTechAPI.TICKS_FOR_LAG_AVERAGING = Gregtech.general.ticksForLagAveraging;
        GregTechAPI.MILLISECOND_THRESHOLD_UNTIL_LAG_WARNING = Gregtech.general.millisecondThesholdUntilLagWarning;
        GregTechAPI.sTimber = Gregtech.general.timber;
        GregTechAPI.sDrinksAlwaysDrinkable = Gregtech.general.drinksAlwaysDrinkable;
        GregTechAPI.sDoShowAllItemsInCreative = Gregtech.general.doShowAllItemsInCreative;
        GregTechAPI.sMultiThreadedSounds = Gregtech.general.multiThreadedSounds;
        GTMod.gregtechproxy.mMaxEqualEntitiesAtOneSpot = Gregtech.general.maxEqualEntitiesAtOneSpot;
        GTMod.gregtechproxy.mFlintChance = Gregtech.general.flintChance;
        GTMod.gregtechproxy.mItemDespawnTime = Gregtech.general.itemDespawnTime;
        GTMod.gregtechproxy.mAllowSmallBoilerAutomation = Gregtech.general.allowSmallBoilerAutomation;
        GTMod.gregtechproxy.mDisableVanillaOres = Worldgen.general.disableVanillaOres;
        GTMod.gregtechproxy.mIncreaseDungeonLoot = Gregtech.general.increaseDungeonLoot;
        GTMod.gregtechproxy.mAxeWhenAdventure = Gregtech.general.axeWhenAdventure;
        GTMod.gregtechproxy.mSurvivalIntoAdventure = Gregtech.general.survivalIntoAdventure;
        GTMod.gregtechproxy.mHungerEffect = Gregtech.general.hungerEffect;
        GTMod.gregtechproxy.mInventoryUnification = Gregtech.general.inventoryUnification;
        GTMod.gregtechproxy.mGTBees = Gregtech.general.GTBees;
        GTMod.gregtechproxy.mCraftingUnification = Gregtech.general.craftingUnification;
        GTMod.gregtechproxy.mNerfedWoodPlank = Gregtech.general.nerfedWoodPlank;
        GTMod.gregtechproxy.mChangeWoodenVanillaTools = Gregtech.general.changedWoodenVanillaTools;
        GTMod.gregtechproxy.mAchievements = Gregtech.general.achievements;
        GTMod.gregtechproxy.mHideUnusedOres = Gregtech.general.hideUnusedOres;
        GTMod.gregtechproxy.mEnableAllMaterials = Gregtech.general.enableAllMaterials;
        GTMod.gregtechproxy.mExplosionItemDrop = Gregtech.general.explosionItemDrop;
        GTMod.gregtechproxy.mEnableCleanroom = Gregtech.general.enableCleanroom;
        GTMod.gregtechproxy.mLowGravProcessing = GalacticraftCore.isModLoaded() && Gregtech.general.lowGravProcessing;
        GTMod.gregtechproxy.mCropNeedBlock = Gregtech.general.cropNeedBlock;
        GTMod.gregtechproxy.mAMHInteraction = Gregtech.general.autoMaintenaceHatchesInteraction;
        GTMod.gregtechproxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre = Gregtech.general.mixedOreOnlyYieldsTwoThirdsOfPureOre;
        GTMod.gregtechproxy.mRichOreYieldMultiplier = Gregtech.general.richOreYieldMultiplier;
        GTMod.gregtechproxy.mNetherOreYieldMultiplier = Gregtech.general.netherOreYieldMultiplier;
        GTMod.gregtechproxy.mEndOreYieldMultiplier = Gregtech.general.endOreYieldMultiplier;
        GTMod.gregtechproxy.gt6Pipe = Gregtech.general.gt6Pipe;
        GTMod.gregtechproxy.gt6Cable = Gregtech.general.gt6Cable;
        GTMod.gregtechproxy.ic2EnergySourceCompat = Gregtech.general.ic2EnergySourceCompat;
        GTMod.gregtechproxy.costlyCableConnection = Gregtech.general.costlyCableConnection;
        GTMod.gregtechproxy.crashOnNullRecipeInput = Gregtech.general.crashOnNullRecipeInput;
        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            GTMod.gregtechproxy.crashOnNullRecipeInput = false; // Use flags in GTRecipeBuilder instead!
        }
        GTLanguageManager.i18nPlaceholder = Gregtech.general.i18nPlaceholder;
        MTELongDistancePipelineBase.minimalDistancePoints = Gregtech.general.minimalDistancePoints;
        GTValues.mCTMEnabledBlock.addAll(Arrays.asList(Gregtech.general.CTMWhitelist));
        GTValues.mCTMDisabledBlock.addAll(Arrays.asList(Gregtech.general.CTMBlacklist));
        if (Gregtech.general.harderMobSpawner) {
            Blocks.mob_spawner.setHardness(500.0F)
                .setResistance(6000000.0F);
        }

        // machines
        GTValues.ticksBetweenSounds = Gregtech.machines.ticksBetweenSounds;
        GTValues.blacklistedTileEntiyClassNamesForWA = Gregtech.machines.blacklistedTileEntiyClassNamesForWA;
        GTValues.cleanroomGlass = Gregtech.machines.cleanroomGlass;
        GTValues.enableChunkloaders = Gregtech.machines.enableChunkloaders;
        GTValues.alwaysReloadChunkloaders = Gregtech.machines.alwaysReloadChunkloaders;
        GTValues.debugChunkloaders = Gregtech.debug.debugChunkloaders;
        GTValues.disableDigitalChestsExternalAccess = Gregtech.machines.disableDigitalChestsExternalAccess;
        GregTechAPI.sMachineExplosions = Gregtech.machines.machineExplosions;
        GregTechAPI.sMachineFlammable = Gregtech.machines.machineFlammable;
        GregTechAPI.sMachineNonWrenchExplosions = Gregtech.machines.machineNonWrenchExplosions;
        GregTechAPI.sMachineWireFire = Gregtech.machines.machineWireFire;
        GregTechAPI.sMachineFireExplosions = Gregtech.machines.machineFireExplosions;
        GregTechAPI.sMachineRainExplosions = Gregtech.machines.machineRainExplosions;
        GregTechAPI.sMachineThunderExplosions = Gregtech.machines.machineThunderExplosions;
        GregTechAPI.sColoredGUI = Gregtech.machines.coloredGUI;
        GregTechAPI.sMachineMetalGUI = Gregtech.machines.machineMetalGUI;
        // Implementation for this is actually handled in NewHorizonsCoreMod in MainRegistry.java!
        GregTechAPI.sUseMachineMetal = Gregtech.machines.useMachineMetal;

        // client
        loadClientConfig();

        // Pollution
        GTMod.gregtechproxy.mPollution = PollutionConfig.pollution;
        GTMod.gregtechproxy.mPollutionSmogLimit = PollutionConfig.pollutionSmogLimit;
        GTMod.gregtechproxy.mPollutionPoisonLimit = PollutionConfig.pollutionPoisonLimit;
        GTMod.gregtechproxy.mPollutionVegetationLimit = PollutionConfig.pollutionVegetationLimit;
        GTMod.gregtechproxy.mPollutionSourRainLimit = PollutionConfig.pollutionSourRainLimit;
        GTMod.gregtechproxy.mPollutionOnExplosion = PollutionConfig.pollutionOnExplosion;
        GTMod.gregtechproxy.mPollutionPrimitveBlastFurnacePerSecond = PollutionConfig.pollutionPrimitveBlastFurnacePerSecond;
        GTMod.gregtechproxy.mPollutionCharcoalPitPerSecond = PollutionConfig.pollutionCharcoalPitPerSecond;
        GTMod.gregtechproxy.mPollutionEBFPerSecond = PollutionConfig.pollutionEBFPerSecond;
        GTMod.gregtechproxy.mPollutionLargeCombustionEnginePerSecond = PollutionConfig.pollutionLargeCombustionEnginePerSecond;
        GTMod.gregtechproxy.mPollutionExtremeCombustionEnginePerSecond = PollutionConfig.pollutionExtremeCombustionEnginePerSecond;
        GTMod.gregtechproxy.mPollutionImplosionCompressorPerSecond = PollutionConfig.pollutionImplosionCompressorPerSecond;
        GTMod.gregtechproxy.mPollutionLargeBronzeBoilerPerSecond = PollutionConfig.pollutionLargeBronzeBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionLargeSteelBoilerPerSecond = PollutionConfig.pollutionLargeSteelBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionLargeTitaniumBoilerPerSecond = PollutionConfig.pollutionLargeTitaniumBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionLargeTungstenSteelBoilerPerSecond = PollutionConfig.pollutionLargeTungstenSteelBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionReleasedByThrottle = PollutionConfig.pollutionReleasedByThrottle;
        GTMod.gregtechproxy.mPollutionLargeGasTurbinePerSecond = PollutionConfig.pollutionLargeGasTurbinePerSecond;
        GTMod.gregtechproxy.mPollutionMultiSmelterPerSecond = PollutionConfig.pollutionMultiSmelterPerSecond;
        GTMod.gregtechproxy.mPollutionPyrolyseOvenPerSecond = PollutionConfig.pollutionPyrolyseOvenPerSecond;
        GTMod.gregtechproxy.mPollutionSmallCoalBoilerPerSecond = PollutionConfig.pollutionSmallCoalBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionHighPressureLavaBoilerPerSecond = PollutionConfig.pollutionHighPressureLavaBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionHighPressureCoalBoilerPerSecond = PollutionConfig.pollutionHighPressureCoalBoilerPerSecond;
        GTMod.gregtechproxy.mPollutionBaseDieselGeneratorPerSecond = PollutionConfig.pollutionBaseDieselGeneratorPerSecond;
        double[] mPollutionDieselGeneratorReleasedByTier = PollutionConfig.pollutionDieselGeneratorReleasedByTier;
        if (mPollutionDieselGeneratorReleasedByTier.length
            == GTMod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier.length) {
            GTMod.gregtechproxy.mPollutionDieselGeneratorReleasedByTier = mPollutionDieselGeneratorReleasedByTier;
        } else {
            GT_FML_LOGGER
                .error("The Length of the Diesel Turbine Pollution Array Config must be the same as the Default");
        }
        GTMod.gregtechproxy.mPollutionBaseGasTurbinePerSecond = PollutionConfig.pollutionBaseGasTurbinePerSecond;
        double[] mPollutionGasTurbineReleasedByTier = PollutionConfig.pollutionGasTurbineReleasedByTier;
        if (mPollutionGasTurbineReleasedByTier.length
            == GTMod.gregtechproxy.mPollutionGasTurbineReleasedByTier.length) {
            GTMod.gregtechproxy.mPollutionGasTurbineReleasedByTier = mPollutionGasTurbineReleasedByTier;
        } else {
            GT_FML_LOGGER.error("The Length of the Gas Turbine Pollution Array Config must be the same as the Default");
        }

        // underground fluids file
        GTMod.gregtechproxy.mUndergroundOil.getConfig(GTConfig.undergroundFluidsFile, "undergroundfluid");

        // Worldgeneration.cfg
        GTMod.gregtechproxy.enableUndergroundGravelGen = Worldgen.general.generateUndergroundGravelGen;
        GTMod.gregtechproxy.enableUndergroundDirtGen = Worldgen.general.generateUndergroundDirtGen;
        GTMod.gregtechproxy.enableBlackGraniteOres = Worldgen.general.generateBlackGraniteOres;
        GTMod.gregtechproxy.enableRedGraniteOres = Worldgen.general.generateRedGraniteOres;
        GTMod.gregtechproxy.enableMarbleOres = Worldgen.general.generateMarbleOres;
        GTMod.gregtechproxy.enableBasaltOres = Worldgen.general.generateBasaltOres;

        // OverpoweredStuff.cfg
        GregTechAPI.mOutputRF = OPStuff.outputRF;
        GregTechAPI.mInputRF = OPStuff.inputRF;
        GregTechAPI.mEUtoRF = OPStuff.howMuchRFWith100EUInInput;
        GregTechAPI.mRFtoEU = OPStuff.howMuchEUWith100RFInInput;
        GregTechAPI.mRFExplosions = OPStuff.RFExplosions;

        // MachineStats.cfg
        GTMod.gregtechproxy.mForceFreeFace = MachineStats.machines.forceFreeFace;

        // ore_drop_behavior
        GTMod.gregtechproxy.oreDropSystem = Gregtech.oreDropBehavior.setting;

        // features
        GTMod.gregtechproxy.mUpgradeCount = Math.min(64, Math.max(1, Gregtech.features.upgradeStackSize));
        for (OrePrefixes tPrefix : OrePrefixes.values()) {
            if (tPrefix.mIsUsedForOreProcessing) {
                tPrefix.mDefaultStackSize = ((byte) Math.min(64, Math.max(1, Gregtech.features.maxOreStackSize)));
            } else if (tPrefix == OrePrefixes.plank) {
                tPrefix.mDefaultStackSize = ((byte) Math.min(64, Math.max(16, Gregtech.features.maxPlankStackSize)));
            } else if ((tPrefix == OrePrefixes.wood) || (tPrefix == OrePrefixes.treeLeaves)
                || (tPrefix == OrePrefixes.treeSapling)
                || (tPrefix == OrePrefixes.log)) {
                    tPrefix.mDefaultStackSize = ((byte) Math.min(64, Math.max(16, Gregtech.features.maxLogStackSize)));
                } else if (tPrefix.mIsUsedForBlocks) {
                    tPrefix.mDefaultStackSize = ((byte) Math
                        .min(64, Math.max(16, Gregtech.features.maxOtherBlocksStackSize)));
                }
        }

        GTRecipeBuilder.onConfigLoad();
    }

    /**
     * Clamp value between 0 and 255
     *
     * @param value the value to clamp
     * @return the clamped value
     */
    private static int sanitizeConfigInt(int value) {
        return Math.min(255, Math.max(0, value));
    }

    public static void loadClientConfig() {
        Arrays.stream(Dyes.values())
            .filter(dye -> (dye != Dyes._NULL) && (dye.mIndex < 0))
            .forEach(dye -> {
                switch (dye.toString()
                    .toLowerCase()) {
                    case "cable_insulation" -> {
                        dye.mRGBa[0] = (short) sanitizeConfigInt(Client.colorModulation.cableInsulation.red);
                        dye.mRGBa[1] = (short) sanitizeConfigInt(Client.colorModulation.cableInsulation.green);
                        dye.mRGBa[2] = (short) sanitizeConfigInt(Client.colorModulation.cableInsulation.blue);
                    }
                    case "construction_foam" -> {
                        dye.mRGBa[0] = (short) sanitizeConfigInt(Client.colorModulation.constructionFoam.red);
                        dye.mRGBa[1] = (short) sanitizeConfigInt(Client.colorModulation.constructionFoam.green);
                        dye.mRGBa[2] = (short) sanitizeConfigInt(Client.colorModulation.constructionFoam.blue);
                    }
                    case "machine_metal" -> {
                        dye.mRGBa[0] = (short) sanitizeConfigInt(Client.colorModulation.machineMetal.red);
                        dye.mRGBa[1] = (short) sanitizeConfigInt(Client.colorModulation.machineMetal.green);
                        dye.mRGBa[2] = (short) sanitizeConfigInt(Client.colorModulation.machineMetal.blue);
                    }
                    default -> {
                        GT_FML_LOGGER.warn(
                            "unknown color modulation entry: " + dye
                                + ". Report this pls, as config is missing this entry being parsed in code.");
                    }
                }
            });
        GTMod.gregtechproxy.mRenderTileAmbientOcclusion = Client.render.renderTileAmbientOcclusion;
        GTMod.gregtechproxy.mRenderGlowTextures = Client.render.renderGlowTextures;
        GTMod.gregtechproxy.mRenderFlippedMachinesFlipped = Client.render.renderFlippedMachinesFlipped;
        GTMod.gregtechproxy.mRenderIndicatorsOnHatch = Client.render.renderIndicatorsOnHatch;
        GTMod.gregtechproxy.mRenderDirtParticles = Client.render.renderDirtParticles;
        GTMod.gregtechproxy.mRenderPollutionFog = Client.render.renderPollutionFog;
        GTMod.gregtechproxy.mRenderItemDurabilityBar = Client.render.renderItemDurabilityBar;
        GTMod.gregtechproxy.mRenderItemChargeBar = Client.render.renderItemChargeBar;
        GTMod.gregtechproxy.mUseBlockUpdateHandler = Client.render.useBlockUpdateHandler;

        GTMod.gregtechproxy.mCoverTabsVisible = Client.iface.coverTabsVisible;
        GTMod.gregtechproxy.mCoverTabsFlipped = Client.iface.coverTabsFlipped;
        GTMod.gregtechproxy.mTooltipVerbosity = Client.iface.tooltipVerbosity;
        GTMod.gregtechproxy.mTooltipShiftVerbosity = Client.iface.tooltipShiftVerbosity;
        GTMod.gregtechproxy.mTitleTabStyle = Client.iface.titleTabStyle;
        GTMod.gregtechproxy.separatorStyle = Client.iface.separatorStyle;
        GTMod.gregtechproxy.tooltipFinisherStyle = Client.iface.tooltipFinisherStyle;

        GTMod.gregtechproxy.mNEIRecipeSecondMode = Client.nei.NEIRecipeSecondMode;
        GTMod.gregtechproxy.mNEIRecipeOwner = Client.nei.NEIRecipeOwner;
        GTMod.gregtechproxy.mNEIRecipeOwnerStackTrace = Client.nei.NEIRecipeOwnerStackTrace;
        GTMod.gregtechproxy.mNEIOriginalVoltage = Client.nei.NEIOriginalVoltage;

        GTMod.gregtechproxy.mWailaTransformerVoltageTier = Client.waila.wailaTransformerVoltageTier;
        GTMod.gregtechproxy.wailaAverageNS = Client.waila.wailaAverageNS;

        GTMod.gregtechproxy.reloadNEICache();
    }
}
