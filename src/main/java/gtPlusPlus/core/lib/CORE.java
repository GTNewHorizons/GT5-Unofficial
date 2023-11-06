package gtPlusPlus.core.lib;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.objects.XSTR;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;

public class CORE {

    public static Map PlayerCache;

    // Math Related
    public static final float PI = (float) Math.PI;
    public static volatile Random RANDOM = new XSTR();

    public static boolean DEVENV = false;
    // Only can be set in Dev, no config or setting elsewhere.
    public static final boolean LOAD_ALL_CONTENT = false;;

    // Mod Variables

    public static final String name = "GT++";
    public static final String VERSION = "GRADLETOKEN_VERSION";

    // Tweakables
    public static int EVERGLADES_ID = 227;
    public static int EVERGLADESBIOME_ID = 238;

    public static int turbineCutoffBase = 75000;

    // GT++ Fake Player Profile
    public static final GameProfile gameProfile = new GameProfile(
            UUID.nameUUIDFromBytes("gtplusplus.core".getBytes()),
            "[GT++]");
    public static final WeakHashMap<World, EntityPlayerMP> fakePlayerCache = new WeakHashMap<>();
    // Tooltips;
    public static final Supplier<String> GT_Tooltip = () -> StatCollector.translateToLocal("GTPP.core.GT_Tooltip");
    public static final Supplier<String> GT_Tooltip_Builder = () -> StatCollector
            .translateToLocal("GTPP.core.GT_Tooltip_Builder");
    public static final Supplier<String> GT_Tooltip_Radioactive = () -> StatCollector
            .translateToLocal("GTPP.core.GT_Tooltip_Radioactive");
    public static final String noItem = "";

    public static final String SEPERATOR = "/";

    /**
     * Lists/Maps
     */

    // Burnables List
    public static List<Pair<Integer, ItemStack>> burnables = new ArrayList<>();

    // TesseractMaps
    public static final Map<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>> sTesseractGeneratorOwnershipMap = new HashMap<>();
    public static final Map<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>> sTesseractTerminalOwnershipMap = new HashMap<>();

    // BookMap
    public static final Map<String, ItemStack> sBookList = new ConcurrentHashMap<>();

    /**
     * Some Gregtech Material and Recipe Variables
     */
    public static IGregtech_RecipeAdder RA;

    public static final GT_Materials[] sMU_GeneratedMaterials = new GT_Materials[1000];

    /**
     * File Paths and Resource Paths
     */
    public static final String TEX_DIR = "textures/", TEX_DIR_GUI = TEX_DIR + "gui/", TEX_DIR_ITEM = TEX_DIR + "items/",
            TEX_DIR_BLOCK = TEX_DIR + "blocks/", TEX_DIR_ENTITY = TEX_DIR + "entity/",
            TEX_DIR_ASPECTS = TEX_DIR + "aspects/", TEX_DIR_FLUIDS = TEX_DIR_BLOCK + "fluids/",
            RES_PATH = GTPlusPlus.ID + ":" + TEX_DIR, RES_PATH_GUI = GTPlusPlus.ID + ":" + TEX_DIR_GUI,
            RES_PATH_ITEM = GTPlusPlus.ID + ":" + TEX_DIR_ITEM, RES_PATH_BLOCK = GTPlusPlus.ID + ":" + TEX_DIR_BLOCK,
            RES_PATH_ENTITY = GTPlusPlus.ID + ":" + TEX_DIR_ENTITY,
            RES_PATH_ASPECTS = GTPlusPlus.ID + ":" + TEX_DIR_ASPECTS,
            RES_PATH_FLUIDS = GTPlusPlus.ID + ":" + TEX_DIR_FLUIDS;

    /**
     * Used to create a {@link EntityPlayer} instance from {@link FakePlayerFactory}. If this instance already exists in
     * the cache, we will return that instead. These instances are held via weak reference, if the world object is
     * unloaded, they too will be removed. This is the suggested way to handle them, as suggested by Forge.
     *
     * @param world - The {@link World} object for which you want to check for in the cache. This object is used as a
     *              weak reference in a {@link WeakHashMap}.
     * @return - An {@link EntityPlayerMP} instance, returned either from cache or created and cached prior to return.
     */
    public static EntityPlayerMP getFakePlayer(World world) {
        if (fakePlayerCache.get(world) == null) {
            fakePlayerCache.put(world, FakePlayerFactory.get((WorldServer) world, CORE.gameProfile));
        }
        return fakePlayerCache.get(world);
    }

    /*
     * Config Switch Class
     */

    public static class ConfigSwitches {

        // Debug
        public static boolean disableEnderIOIntegration = false;
        public static boolean disableEnderIOIngotTooltips = false;
        public static boolean MACHINE_INFO = true;
        public static boolean showHiddenNEIItems = false;
        public static boolean dumpItemAndBlockData = false;

        // Tools
        public static boolean enableMultiSizeTools = true;

        // Block Drops
        public static int chanceToDropDrainedShard = 196;
        public static int chanceToDropFluoriteOre = 32;

        // Machine Related
        public static boolean enableAlternativeBatteryAlloy = false;
        public static boolean enableThaumcraftShardUnification = false;
        public static boolean disableIC2Recipes = false;
        public static boolean enableAlternativeDivisionSigilRecipe = false;
        public static int boilerSteamPerSecond = 750;
        public static final boolean requireControlCores = false;

        // Feature Related
        public static boolean enableCustomCapes = false;
        public static boolean enableCustomCircuits = true;
        public static boolean enableOldGTcircuits = false;
        public static int enableWatchdogBGM = CORE_Preloader.enableWatchdogBGM;
        public static boolean hideUniversalCells = true;

        // GT Fixes
        public static boolean enableSulfuricAcidFix = false;

        // Single Block Machines
        public static boolean enableMachine_SolarGenerators = false;
        public static boolean enableMachine_Dehydrators = true;
        public static boolean enableMachine_SteamConverter = true;
        public static boolean enableMachine_FluidTanks = true;
        public static boolean enableMachine_RocketEngines = true;
        public static boolean enableMachine_GeothermalEngines = true;
        public static boolean enableMachine_Tesseracts = true;
        public static boolean enableMachine_SimpleWasher = true;
        public static boolean enableMachine_Pollution = true;
        public static boolean enableCustom_Pipes = true;
        public static boolean enableCustom_Cables = true;
        public static boolean enableMachine_RF_Convetor = false;

        // Multiblocks
        public static boolean enableMultiblock_AlloyBlastSmelter = true;
        public static boolean enableMultiblock_QuantumForceTransformer = true;
        public static boolean enableMultiblock_IndustrialCentrifuge = true;
        public static boolean enableMultiblock_IndustrialCokeOven = true;
        public static boolean enableMultiblock_IndustrialElectrolyzer = true;
        public static boolean enableMultiblock_IndustrialMacerationStack = true;
        public static boolean enableMultiblock_IndustrialPlatePress = true;
        public static boolean enableMultiblock_IndustrialWireMill = true;
        public static boolean enableMultiblock_MatterFabricator = true;
        public static boolean enableMultiblock_MultiTank = true;
        public static boolean enableMultiblock_PowerSubstation = true;
        public static boolean enableMultiblock_LiquidFluorideThoriumReactor = true;
        public static boolean enableMultiblock_NuclearSaltProcessingPlant = true;
        public static boolean enableMultiblock_NuclearFuelRefinery = true;
        public static boolean enableMultiblock_TreeFarmer = true;
        public static boolean enableMultiblock_IndustrialSifter = true;
        public static boolean enableMultiblock_IndustrialThermalCentrifuge = true;
        public static boolean enableMultiblock_IndustrialWashPlant = true;
        public static boolean enableMultiblock_LargeAutoCrafter = true;
        public static boolean enableMultiblock_ThermalBoiler = true;
        public static boolean enableMultiblock_IndustrialCuttingMachine = true;
        public static boolean enableMultiblock_IndustrialFishingPort = true;
        public static boolean enableMultiblock_IndustrialExtrudingMachine = true;
        public static boolean enableMultiblock_IndustrialMultiMachine = true;
        public static boolean enableMultiblock_Cyclotron = true;

        // Visuals
        public static boolean enableTreeFarmerParticles = true;
        public static boolean useGregtechTextures = true;
        public static boolean enableAnimatedTextures = false;

        // Pollution
        public static int pollutionPerSecondMultiPackager = 40;
        public static int pollutionPerSecondMultiIndustrialAlloySmelter = 300;
        public static int pollutionPerSecondMultiIndustrialArcFurnace = 2400;
        public static int pollutionPerSecondMultiIndustrialCentrifuge = 300;
        public static int pollutionPerSecondMultiIndustrialCokeOven = 80;
        public static int pollutionPerSecondMultiIndustrialCuttingMachine = 160;
        public static int pollutionPerSecondMultiIndustrialDehydrator = 500;
        public static int pollutionPerSecondMultiIndustrialElectrolyzer = 300;
        public static int pollutionPerSecondMultiIndustrialExtruder = 1000;
        public static int pollutionPerSecondMultiIndustrialMacerator = 400;
        public static int pollutionPerSecondMultiIndustrialMixer = 800;
        public static int pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal = 400;
        public static int pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid = 400;
        public static int pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc = 600;
        public static int pollutionPerSecondMultiIndustrialPlatePress_ModeForming = 240;
        public static int pollutionPerSecondMultiIndustrialPlatePress_ModeBending = 480;
        public static int pollutionPerSecondMultiIndustrialForgeHammer = 250;
        public static int pollutionPerSecondMultiIndustrialSifter = 40;
        public static int pollutionPerSecondMultiIndustrialThermalCentrifuge = 1000;
        public static int pollutionPerSecondMultiIndustrialVacuumFreezer = 500;
        public static int pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath = 400;
        public static int pollutionPerSecondMultiIndustrialWashPlant_ModeWasher = 100;
        public static int pollutionPerSecondMultiIndustrialWireMill = 100;
        public static int pollutionPerSecondMultiIsaMill = 1280;
        public static int pollutionPerSecondMultiAdvDistillationTower_ModeDistillery = 240;
        public static int pollutionPerSecondMultiAdvDistillationTower_ModeDT = 480;
        public static int pollutionPerSecondMultiAdvEBF = 500;
        public static int pollutionPerSecondMultiAdvImplosion = 5000;
        public static int pollutionPerSecondMultiABS = 200;
        public static int pollutionPerSecondMultiCyclotron = 200;
        public static int pollutionPerSecondMultiIndustrialFishingPond = 20;
        public static int pollutionPerSecondMultiLargeRocketEngine;
        public static int pollutionPerSecondMultiLargeSemiFluidGenerator = 1280;
        public static int pollutionPerSecondMultiMassFabricator = 40;
        public static int pollutionPerSecondMultiRefinery = 4000;
        public static int pollutionPerSecondMultiGeneratorArray;
        public static int pollutionPerSecondMultiTreeFarm = 100;
        public static int pollutionPerSecondMultiFrothFlotationCell = 0;
        public static int pollutionPerSecondMultiAutoCrafter = 500;
        public static int pollutionPerSecondMultiThermalBoiler = 700;
        public static int pollutionPerSecondMultiMolecularTransformer = 1000;
        public static int pollutionPerSecondMultiAlgaePond = 0;
        public static int pollutionPerSecondMultiIndustrialRockBreaker = 100;
        public static int pollutionPerSecondMultiIndustrialChisel = 50;
        // pollution single blocks
        public static int basePollutionPerSecondSemiFluidGenerator = 40;
        public static double[] pollutionReleasedByTierSemiFluidGenerator = new double[] { 0, 2.0, 4.0, 8.0, 12.0, 16,
                0 };
        public static int basePollutionPerSecondBoiler = 35;
        public static double[] pollutionReleasedByTierBoiler = new double[] { 0, 1.0, 1.43, 1.86 };
        public static int baseMinPollutionPerSecondRocketFuelGenerator = 250;
        public static int baseMaxPollutionPerSecondRocketFuelGenerator = 2000;
        public static double[] pollutionReleasedByTierRocketFuelGenerator = new double[] { 0, 0, 0, 0, 1, 2, 3 };
        public static int basePollutionPerSecondGeothermalGenerator = 100;
        public static double[] pollutionReleasedByTierGeothermalGenerator = new double[] { 0, 0, 0, 0, 1, 1, 1 };
    }

    public static class Everglades {

        public static final String NAME = "GT++ Toxic Everglades";
        public static final String VERSION = "GRADLETOKEN_VERSION";
    }

    public static final void crash() {
        crash("Generic Crash");
    }

    public static final void crash(String aReason) {
        try {
            Logger.INFO("==========================================================");
            Logger.INFO("[GT++ CRASH]");
            Logger.INFO("==========================================================");
            Logger.INFO("Oooops...");
            Logger.INFO("This should only happy in a development environment or when something really bad happens.");
            Logger.INFO("Reason: " + aReason);
            Logger.INFO("==========================================================");
            Logger.INFO("Called from: " + ReflectionUtils.getMethodName(1));
            Logger.INFO(ReflectionUtils.getMethodName(2));
            Logger.INFO(ReflectionUtils.getMethodName(3));
            Logger.INFO(ReflectionUtils.getMethodName(4));
            Logger.INFO(ReflectionUtils.getMethodName(5));
            Logger.INFO(ReflectionUtils.getMethodName(6));
            Logger.INFO(ReflectionUtils.getMethodName(7));
            Logger.INFO(ReflectionUtils.getMethodName(8));
            Logger.INFO(ReflectionUtils.getMethodName(9));
            Logger.INFO(ReflectionUtils.getMethodName(10));
            Logger.INFO(ReflectionUtils.getMethodName(11));
            Logger.INFO(ReflectionUtils.getMethodName(12));
            Logger.INFO(ReflectionUtils.getMethodName(13));
            Logger.INFO(ReflectionUtils.getMethodName(14));
            Logger.INFO(ReflectionUtils.getMethodName(15));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        FMLCommonHandler.instance().exitJava(0, true);
    }
}
