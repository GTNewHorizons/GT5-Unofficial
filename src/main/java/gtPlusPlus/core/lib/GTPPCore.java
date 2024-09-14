package gtPlusPlus.core.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.GT_Version;
import gregtech.api.objects.XSTR;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.preloader.PreloaderCore;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractTerminal;

public class GTPPCore {

    // Math Related
    public static final float PI = (float) Math.PI;
    public static volatile Random RANDOM = new XSTR();

    public static boolean DEVENV = false;

    // Mod Variables
    public static final String name = "GT++";
    public static final String VERSION = GT_Version.VERSION;

    // Tweakables
    public static int EVERGLADES_ID = 227;
    public static int EVERGLADESBIOME_ID = 238;


    // Tooltips;
    public static final Supplier<String> GT_Tooltip = () -> StatCollector.translateToLocal("GTPP.core.GT_Tooltip");
    public static final Supplier<String> GT_Tooltip_Builder = () -> StatCollector
        .translateToLocal("GTPP.core.GT_Tooltip_Builder");
    public static final Supplier<String> GT_Tooltip_Radioactive = () -> StatCollector
        .translateToLocal("GTPP.core.GT_Tooltip_Radioactive");

    public static final String SEPERATOR = "/";

    /**
     * Lists/Maps
     */

    // Burnables List
    public static List<Pair<Integer, ItemStack>> burnables = new ArrayList<>();

    // TesseractMaps
    public static final Map<UUID, Map<Integer, MTETesseractGenerator>> sTesseractGeneratorOwnershipMap = new HashMap<>();
    public static final Map<UUID, Map<Integer, MTETesseractTerminal>> sTesseractTerminalOwnershipMap = new HashMap<>();

    // BookMap
    public static final Map<String, ItemStack> sBookList = new ConcurrentHashMap<>();

    public static final GT_Materials[] sMU_GeneratedMaterials = new GT_Materials[1000];

    public static class ConfigSwitches {

        // Feature Related
        public static boolean enableCustomCapes = false;
        public static int enableWatchdogBGM = PreloaderCore.enableWatchdogBGM;
        public static boolean hideUniversalCells = true;


        public static boolean enableMachine_RedstoneBlocks = true;


        // Visuals
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
        public static int pollutionPerSecondMultiLargeSemiFluidGenerator = 1280;
        public static int pollutionPerSecondMultiMassFabricator = 40;
        public static int pollutionPerSecondMultiRefinery = 4000;
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
        public static final String VERSION = GT_Version.VERSION;
    }

    public static void crash() {
        crash("Generic Crash");
    }

    public static void crash(String aReason) {
        try {
            Logger.INFO("==========================================================");
            Logger.INFO("[GT++ CRASH]");
            Logger.INFO("==========================================================");
            Logger.INFO("Oooops...");
            Logger.INFO("This should only happen in a development environment or when something really bad happens.");
            Logger.INFO("Reason: " + aReason);
            Logger.INFO("==========================================================");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        FMLCommonHandler.instance()
            .exitJava(0, true);
    }
}
