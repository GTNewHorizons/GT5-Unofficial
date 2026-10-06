package gregtech.loaders.preload;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.Mods.GalacticraftCore;
import static gregtech.api.enums.Mods.GregTech;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.waila.TTRenderGTProgressBar;
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
        } catch (Exception e) {
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
            GTLanguageManager.LanguageCode = userLang;
            GT_FML_LOGGER.info("User lang is {}", userLang);
            if (userLang.equals("en_US")) {
                GT_FML_LOGGER.info("Loading GregTech.lang");
                GTLanguageManager.isEN_US = true;
                GTLanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
            } else {
                String l10nFileName = "GregTech_" + userLang + ".lang";
                File l10nFile = new File(languageDir, l10nFileName);
                if (l10nFile.isFile()) {
                    GT_FML_LOGGER.info("Loading l10n file: {}", l10nFileName);
                    GTLanguageManager.sEnglishFile = new Configuration(l10nFile);
                } else {
                    GT_FML_LOGGER.info("Cannot find l10n file {}, fallback to GregTech.lang", l10nFileName);
                    GTLanguageManager.isEN_US = true;
                    GTLanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
                }
            }
        } else {
            GTLanguageManager.isEN_US = true;
            GTLanguageManager.sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
        }
        GTLanguageManager.sEnglishFile.load();

        MaterialLibAPI.getMaterials()
            .parallelStream()
            .filter(material -> "gregtech".equals(material.getModId()))
            .forEach(
                material -> GTLanguageManager.addStringLocalization(
                    MaterialUtils.localizedNameKey(material),
                    MaterialUtils.localName(material)));
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

    public static void adjustScrap() {
        GT_FML_LOGGER.info("GTMod: Removing all original Scrapbox Drops.");
        try {
            Objects.requireNonNull(GTUtility.getField("ic2.core.item.ItemScrapbox$Drop", "topChance", true, true))
                .set(null, 0);
            ((List<?>) Objects
                .requireNonNull(GTUtility.getFieldContent(ic2.api.recipe.Recipes.scrapboxDrops, "drops", true, true)))
                    .clear();
        } catch (Exception e) {
            if (GTValues.D1) {
                GT_FML_LOGGER.error(e);
            }
        }
        GT_FML_LOGGER.debug("GTMod: Adding Scrap with a Weight of 200.0F to the Scrapbox Drops.");
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
        GTMod.proxy.mMaxEqualEntitiesAtOneSpot = Gregtech.general.maxEqualEntitiesAtOneSpot;
        GTMod.proxy.mFlintChance = Gregtech.general.flintChance;
        GTMod.proxy.mItemDespawnTime = Gregtech.general.itemDespawnTime;
        GTMod.proxy.mAllowSmallBoilerAutomation = Gregtech.general.allowSmallBoilerAutomation;
        GTMod.proxy.mDisableVanillaOres = Worldgen.general.disableVanillaOres;
        GTMod.proxy.mIncreaseDungeonLoot = Gregtech.general.increaseDungeonLoot;
        GTMod.proxy.mAxeWhenAdventure = Gregtech.general.axeWhenAdventure;
        GTMod.proxy.mSurvivalIntoAdventure = Gregtech.general.survivalIntoAdventure;
        GTMod.proxy.mGTBees = Gregtech.general.GTBees;
        GTMod.proxy.mNerfedWoodPlank = Gregtech.general.nerfedWoodPlank;
        GTMod.proxy.mChangeWoodenVanillaTools = Gregtech.general.changedWoodenVanillaTools;
        GTMod.proxy.mAchievements = Gregtech.general.achievements;
        GTMod.proxy.mExplosionItemDrop = Gregtech.general.explosionItemDrop;
        GTMod.proxy.mEnableCleanroom = Gregtech.general.enableCleanroom;
        GTMod.proxy.mLowGravProcessing = GalacticraftCore.isModLoaded() && Gregtech.general.lowGravProcessing;
        GTMod.proxy.mAMHInteraction = Gregtech.general.autoMaintenaceHatchesInteraction;
        GTMod.proxy.mMixedOreOnlyYieldsTwoThirdsOfPureOre = Gregtech.general.mixedOreOnlyYieldsTwoThirdsOfPureOre;
        GTMod.proxy.mRichOreYieldMultiplier = Gregtech.general.richOreYieldMultiplier;
        GTMod.proxy.mNetherOreYieldMultiplier = Gregtech.general.netherOreYieldMultiplier;
        GTMod.proxy.mEndOreYieldMultiplier = Gregtech.general.endOreYieldMultiplier;
        GTMod.proxy.gt6Pipe = Gregtech.general.gt6Pipe;
        GTMod.proxy.gt6Cable = Gregtech.general.gt6Cable;
        GTMod.proxy.ic2EnergySourceCompat = Gregtech.general.ic2EnergySourceCompat;
        GTMod.proxy.costlyCableConnection = Gregtech.general.costlyCableConnection;
        GTMod.proxy.crashOnNullRecipeInput = Gregtech.general.crashOnNullRecipeInput;
        if ((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            GTMod.proxy.crashOnNullRecipeInput = false; // Use flags in GTRecipeBuilder instead!
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

        // client
        loadClientConfig();

        // Pollution
        GTMod.proxy.mPollution = PollutionConfig.pollution;
        GTMod.proxy.mPollutionSmogLimit = PollutionConfig.pollutionSmogLimit;
        GTMod.proxy.mPollutionPoisonLimit = PollutionConfig.pollutionPoisonLimit;
        GTMod.proxy.mPollutionVegetationLimit = PollutionConfig.pollutionVegetationLimit;
        GTMod.proxy.mPollutionSourRainLimit = PollutionConfig.pollutionSourRainLimit;
        GTMod.proxy.mPollutionOnExplosion = PollutionConfig.pollutionOnExplosion;
        GTMod.proxy.mPollutionPrimitveBlastFurnacePerSecond = PollutionConfig.pollutionPrimitveBlastFurnacePerSecond;
        GTMod.proxy.mPollutionCharcoalPitPerSecond = PollutionConfig.pollutionCharcoalPitPerSecond;
        GTMod.proxy.mPollutionEBFPerSecond = PollutionConfig.pollutionEBFPerSecond;
        GTMod.proxy.mPollutionLargeCombustionEnginePerSecond = PollutionConfig.pollutionLargeCombustionEnginePerSecond;
        GTMod.proxy.mPollutionExtremeCombustionEnginePerSecond = PollutionConfig.pollutionExtremeCombustionEnginePerSecond;
        GTMod.proxy.mPollutionImplosionCompressorPerSecond = PollutionConfig.pollutionImplosionCompressorPerSecond;
        GTMod.proxy.mPollutionLargeBronzeBoilerPerSecond = PollutionConfig.pollutionLargeBronzeBoilerPerSecond;
        GTMod.proxy.mPollutionLargeSteelBoilerPerSecond = PollutionConfig.pollutionLargeSteelBoilerPerSecond;
        GTMod.proxy.mPollutionLargeTitaniumBoilerPerSecond = PollutionConfig.pollutionLargeTitaniumBoilerPerSecond;
        GTMod.proxy.mPollutionLargeTungstenSteelBoilerPerSecond = PollutionConfig.pollutionLargeTungstenSteelBoilerPerSecond;
        GTMod.proxy.mPollutionReleasedByThrottle = PollutionConfig.pollutionReleasedByThrottle;
        GTMod.proxy.mPollutionLargeGasTurbinePerSecond = PollutionConfig.pollutionLargeGasTurbinePerSecond;
        GTMod.proxy.mPollutionMultiSmelterPerSecond = PollutionConfig.pollutionMultiSmelterPerSecond;
        GTMod.proxy.mPollutionPyrolyseOvenPerSecond = PollutionConfig.pollutionPyrolyseOvenPerSecond;
        GTMod.proxy.mPollutionSmallCoalBoilerPerSecond = PollutionConfig.pollutionSmallCoalBoilerPerSecond;
        GTMod.proxy.mPollutionHighPressureLavaBoilerPerSecond = PollutionConfig.pollutionHighPressureLavaBoilerPerSecond;
        GTMod.proxy.mPollutionHighPressureCoalBoilerPerSecond = PollutionConfig.pollutionHighPressureCoalBoilerPerSecond;
        GTMod.proxy.mPollutionBaseDieselGeneratorPerSecond = PollutionConfig.pollutionBaseDieselGeneratorPerSecond;
        double[] mPollutionDieselGeneratorReleasedByTier = PollutionConfig.pollutionDieselGeneratorReleasedByTier;
        if (mPollutionDieselGeneratorReleasedByTier.length
            == GTMod.proxy.mPollutionDieselGeneratorReleasedByTier.length) {
            GTMod.proxy.mPollutionDieselGeneratorReleasedByTier = mPollutionDieselGeneratorReleasedByTier;
        } else {
            GT_FML_LOGGER
                .error("The Length of the Diesel Turbine Pollution Array Config must be the same as the Default");
        }
        GTMod.proxy.mPollutionBaseGasTurbinePerSecond = PollutionConfig.pollutionBaseGasTurbinePerSecond;
        double[] mPollutionGasTurbineReleasedByTier = PollutionConfig.pollutionGasTurbineReleasedByTier;
        if (mPollutionGasTurbineReleasedByTier.length == GTMod.proxy.mPollutionGasTurbineReleasedByTier.length) {
            GTMod.proxy.mPollutionGasTurbineReleasedByTier = mPollutionGasTurbineReleasedByTier;
        } else {
            GT_FML_LOGGER.error("The Length of the Gas Turbine Pollution Array Config must be the same as the Default");
        }

        // underground fluids file
        GTMod.proxy.mUndergroundOil.getConfig(GTConfig.undergroundFluidsFile, "undergroundfluid");

        // Worldgeneration.cfg
        GTMod.proxy.enableUndergroundGravelGen = Worldgen.general.generateUndergroundGravelGen;
        GTMod.proxy.enableUndergroundDirtGen = Worldgen.general.generateUndergroundDirtGen;
        GTMod.proxy.enableBlackGraniteOres = Worldgen.general.generateBlackGraniteOres;
        GTMod.proxy.enableRedGraniteOres = Worldgen.general.generateRedGraniteOres;
        GTMod.proxy.enableMarbleOres = Worldgen.general.generateMarbleOres;
        GTMod.proxy.enableBasaltOres = Worldgen.general.generateBasaltOres;

        // OverpoweredStuff.cfg
        GregTechAPI.mOutputRF = OPStuff.outputRF;
        GregTechAPI.mInputRF = OPStuff.inputRF;
        GregTechAPI.mEUtoRF = OPStuff.howMuchRFWith100EUInInput;
        GregTechAPI.mRFtoEU = OPStuff.howMuchEUWith100RFInInput;
        GregTechAPI.mRFExplosions = OPStuff.RFExplosions;

        // MachineStats.cfg
        GTMod.proxy.mForceFreeFace = MachineStats.machines.forceFreeFace;

        // ore_drop_behavior
        GTMod.proxy.oreDropSystem = Gregtech.oreDropBehavior.setting;

        // features
        GTMod.proxy.mUpgradeCount = Math.min(64, Math.max(1, Gregtech.features.upgradeStackSize));

        GTRecipeBuilder.onConfigLoad();
    }

    public static void loadClientConfig() {
        GTMod.proxy.mRenderTileAmbientOcclusion = Client.render.renderTileAmbientOcclusion;
        GTMod.proxy.mRenderGlowTextures = Client.render.renderGlowTextures;
        GTMod.proxy.mRenderFlippedMachinesFlipped = Client.render.renderFlippedMachinesFlipped;
        GTMod.proxy.mRenderIndicatorsOnHatch = Client.render.renderIndicatorsOnHatch;
        GTMod.proxy.mRenderDirtParticles = Client.render.renderDirtParticles;
        GTMod.proxy.mRenderPollutionFog = Client.render.renderPollutionFog;
        GTMod.proxy.mRenderItemDurabilityBar = Client.render.renderItemDurabilityBar;
        GTMod.proxy.mRenderItemChargeBar = Client.render.renderItemChargeBar;
        GTMod.proxy.mUseBlockUpdateHandler = Client.render.useBlockUpdateHandler;

        GTMod.proxy.mCoverTabsVisible = Client.iface.coverTabsVisible;
        GTMod.proxy.mCoverTabsFlipped = Client.iface.coverTabsFlipped;
        GTMod.proxy.mTooltipVerbosity = Client.iface.tooltipVerbosity;
        GTMod.proxy.mTooltipShiftVerbosity = Client.iface.tooltipShiftVerbosity;
        GTMod.proxy.mTitleTabStyle = Client.iface.titleTabStyle;
        GTMod.proxy.separatorStyle = Client.iface.separatorStyle;
        GTMod.proxy.tooltipFinisherStyle = Client.iface.tooltipFinisherStyle;

        GTMod.proxy.invertCircuitScrollDirection = Client.preference.invertCircuitScrollDirection;

        GTMod.proxy.mNEIRecipeSecondMode = Client.nei.NEIRecipeSecondMode;
        GTMod.proxy.mNEIRecipeOwner = Client.nei.NEIRecipeOwner;
        GTMod.proxy.mNEIRecipeOwnerStackTrace = Client.nei.NEIRecipeOwnerStackTrace;
        GTMod.proxy.mNEIOriginalVoltage = Client.nei.NEIOriginalVoltage;

        GTMod.proxy.mWailaTransformerVoltageTier = Client.waila.wailaTransformerVoltageTier;
        GTMod.proxy.wailaAverageNS = Client.waila.wailaAverageNS;
        if (Client.waila.ProgressBarColor != TTRenderGTProgressBar.ProgressBarColor.Custom) {
            switch (Client.waila.ProgressBarColor) {
                case Green:
                    GTMod.proxy.wailaProgressBarColor1 = 0xFF109010;
                    GTMod.proxy.wailaProgressBarColor2 = 0xFF0D7D0D;
                    break;
                case LightBlue:
                    GTMod.proxy.wailaProgressBarColor1 = 0xFF6060FF;
                    GTMod.proxy.wailaProgressBarColor2 = 0xFF5050E0;
                    break;
                case DarkBlue:
                    GTMod.proxy.wailaProgressBarColor1 = 0xFF3333DA;
                    GTMod.proxy.wailaProgressBarColor2 = 0xFF2020D0;
                    break;
                case Red:
                    GTMod.proxy.wailaProgressBarColor1 = 0xFFC02222;
                    GTMod.proxy.wailaProgressBarColor2 = 0xFFA00000;
                    break;
            }
        } else {
            try {
                GTMod.proxy.wailaProgressBarColor1 = 0xFF000000
                    + Integer.parseInt(Client.waila.ProgressBarCustomColor1, 16);
            } catch (NumberFormatException e) {
                GTMod.proxy.wailaProgressBarColor1 = 0xFF3333DA;
            }
            try {
                GTMod.proxy.wailaProgressBarColor2 = 0xFF000000
                    + Integer.parseInt(Client.waila.ProgressBarCustomColor2, 16);
            } catch (NumberFormatException e) {
                GTMod.proxy.wailaProgressBarColor2 = 0xFF2020D0;
            }
            try {
                GTMod.proxy.wailaProgressBorderColor1 = 0xFF000000
                    + Integer.parseInt(Client.waila.ProgressCustomBorderColor1, 16);
            } catch (NumberFormatException e) {
                GTMod.proxy.wailaProgressBorderColor1 = 0xFF505050;
            }
            try {
                GTMod.proxy.wailaProgressBorderColor2 = 0xFF000000
                    + Integer.parseInt(Client.waila.ProgressCustomBorderColor2, 16);
            } catch (NumberFormatException e) {
                GTMod.proxy.wailaProgressBorderColor2 = 0xFF505050;
            }
        }

        GTMod.proxy.reloadNEICache();
    }
}
