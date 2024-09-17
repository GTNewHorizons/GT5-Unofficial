package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_PLUS_PLUS, configSubDirectory = "GTPlusPlus", filename = "GTPlusPlus")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.Comment("General section")
@Config.RequiresMcRestart
public class Configuration {

    public static final Debug debug = new Debug();
    public static final Machines machines = new Machines();
    public static final Gregtech gregtech = new Gregtech();
    public static final Pollution pollution = new Pollution();
    public static final Features features = new Features();
    public static final Visual visual = new Visual();
    public static final Worldgen worldgen = new Worldgen();

    @Config.Comment("Debug section")
    public static class Debug {

        @Config.Comment("Makes many machines display lots of debug logging.")
        @Config.DefaultBoolean(false)
        public boolean MachineInfo;

        @Config.Comment("Makes all items hidden from NEI display.")
        @Config.DefaultBoolean(false)
        public boolean showHiddenNEIItems;

        @Config.Comment("Dumps all GT++ and Toxic Everglade Data to en_US.lang in the config folder. This config option can be used by foreign players to generate blank .lang files, which they can populate with their language of choice.")
        @Config.DefaultBoolean(false)
        public boolean dumpItemAndBlockData;

    }

    @Config.Comment("Machines section")
    public static class Machines {

        @Config.Comment("Allows the use of TC shards across many recipes by oreDicting them into a common group.")
        @Config.DefaultBoolean(false)
        public boolean enableThaumcraftShardUnification;

        @Config.Comment("Alkaluscraft Related - Removes IC2 Cables Except glass fibre. Few other Misc Tweaks.")
        @Config.DefaultBoolean(false)
        public boolean disableIC2Recipes;
        @Config.Comment("Sets the steam per second value in LV,MV,HV boilers (respectively 1x,2x,3x this number for the tiers)")
        @Config.DefaultInt(750)
        public int boilerSteamPerSecond;
    }

    @Config.Comment("GregTech section")
    public static class Gregtech {

        @Config.Comment("Rotors below this durability will be removed, prevents NEI clutter. Minimum Durability is N * x, where N is the new value set and x is the turbine size, where 1 is Tiny and 4 is Huge. Set to 0 to disable.")
        @Config.DefaultInt(25_500)
        public int turbineCutoffBase;
    }

    @Config.Comment("Pollution section")
    public static class Pollution {

        @Config.Comment("pollution rate in gibbl/s for the Amazon warehousing depot")
        @Config.DefaultInt(40)
        public int pollutionPerSecondMultiPackager;
        @Config.Comment("pollution rate in gibbl/s for the Alloy blast smelter")
        @Config.DefaultInt(300)
        public int pollutionPerSecondMultiIndustrialAlloySmelter;
        @Config.Comment("pollution rate in gibbl/s for the High current arc furnace")
        @Config.DefaultInt(2_400)
        public int pollutionPerSecondMultiIndustrialArcFurnace;
        @Config.Comment("pollution rate in gibbl/s for the Industrial centrifuge")
        @Config.DefaultInt(300)
        public int pollutionPerSecondMultiIndustrialCentrifuge;
        @Config.Comment("pollution rate in gibbl/s for the Industrial coke oven")
        @Config.DefaultInt(80)
        public int pollutionPerSecondMultiIndustrialCokeOven;
        @Config.Comment("pollution rate in gibbl/s for the Cutting factory")
        @Config.DefaultInt(160)
        public int pollutionPerSecondMultiIndustrialCuttingMachine;
        @Config.Comment("pollution rate in gibbl/s for the Utupu-Tanuri")
        @Config.DefaultInt(500)
        public int pollutionPerSecondMultiIndustrialDehydrator;
        @Config.Comment("pollution rate in gibbl/s for the Industrial electrolyzer")
        @Config.DefaultInt(300)
        public int pollutionPerSecondMultiIndustrialElectrolyzer;
        @Config.Comment("pollution rate in gibbl/s for the Industrial extrusion machine")
        @Config.DefaultInt(1_000)
        public int pollutionPerSecondMultiIndustrialExtruder;
        @Config.Comment("pollution rate in gibbl/s for the Maceration stack")
        @Config.DefaultInt(400)
        public int pollutionPerSecondMultiIndustrialMacerator;
        @Config.Comment("pollution rate in gibbl/s for the Industrial mixing machine")
        @Config.DefaultInt(800)
        public int pollutionPerSecondMultiIndustrialMixer;
        @Config.Comment("pollution rate in gibbl/s for the Large processing factory in metal mode")
        @Config.DefaultInt(400)
        public int pollutionPerSecondMultiIndustrialMultiMachine_ModeMetal;
        @Config.Comment("pollution rate in gibbl/s for the Large processing factory in fluid mode")
        @Config.DefaultInt(400)
        public int pollutionPerSecondMultiIndustrialMultiMachine_ModeFluid;
        @Config.Comment("pollution rate in gibbl/s for the Large processing factory in misc mode")
        @Config.DefaultInt(600)
        public int pollutionPerSecondMultiIndustrialMultiMachine_ModeMisc;
        @Config.Comment("pollution rate in gibbl/s for the Industrial material press in forming mode")
        @Config.DefaultInt(240)
        public int pollutionPerSecondMultiIndustrialPlatePress_ModeForming;
        @Config.Comment("pollution rate in gibbl/s for the Industrial material press in bending mode")
        @Config.DefaultInt(480)
        public int pollutionPerSecondMultiIndustrialPlatePress_ModeBending;
        @Config.Comment("pollution rate in gibbl/s for the Industrial Forge Hammer")
        @Config.DefaultInt(250)
        public int pollutionPerSecondMultiIndustrialForgeHammer;
        @Config.Comment("pollution rate in gibbl/s for the Large Sifter")
        @Config.DefaultInt(40)
        public int pollutionPerSecondMultiIndustrialSifter;
        @Config.Comment("pollution rate in gibbl/s for the Large thermal refinery")
        @Config.DefaultInt(1_000)
        public int pollutionPerSecondMultiIndustrialThermalCentrifuge;
        @Config.Comment("pollution rate in gibbl/s for the Industrial fluid heater")
        @Config.DefaultInt(1_000)
        public int pollutionPerSecondMultiIndustrialFluidHeater;
        @Config.Comment("pollution rate in gibbl/s for the Cryogenic freezer")
        @Config.DefaultInt(500)
        public int pollutionPerSecondMultiIndustrialVacuumFreezer;
        @Config.Comment("pollution rate in gibbl/s for the Ore washing plant in chemical bath mode")
        @Config.DefaultInt(400)
        public int pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath;
        @Config.Comment("pollution rate in gibbl/s for the Ore washing plant in ore washer mode")
        @Config.DefaultInt(100)
        public int pollutionPerSecondMultiIndustrialWashPlant_ModeWasher;
        @Config.Comment("pollution rate in gibbl/s for the Wire factory")
        @Config.DefaultInt(100)
        public int pollutionPerSecondMultiIndustrialWireMill;
        @Config.Comment("pollution rate in gibbl/s for the IsaMill grinding machine")
        @Config.DefaultInt(1_280)
        public int pollutionPerSecondMultiIsaMill;
        @Config.Comment("pollution rate in gibbl/s for the Dangote distillus in distillery mode")
        @Config.DefaultInt(240)
        public int pollutionPerSecondMultiAdvDistillationTower_ModeDistillery;
        @Config.Comment("pollution rate in gibbl/s for the Dangote distillus in distillation tower mode")
        @Config.DefaultInt(480)
        public int pollutionPerSecondMultiAdvDistillationTower_ModeDT;
        @Config.Comment("pollution rate in gibbl/s for the Volcanus")
        @Config.DefaultInt(500)
        public int pollutionPerSecondMultiAdvEBF;
        @Config.Comment("pollution rate in gibbl/s for the Density^2")
        @Config.DefaultInt(5_000)
        public int pollutionPerSecondMultiAdvImplosion;
        @Config.Comment("pollution rate in gibbl/s for the Alloy blast furnace")
        @Config.DefaultInt(200)
        public int pollutionPerSecondMultiABS;
        @Config.Comment("pollution rate in gibbl/s for the Cyclotron")
        @Config.DefaultInt(200)
        public int pollutionPerSecondMultiCyclotron;
        @Config.Comment("pollution rate in gibbl/s for the Zuhai - fishing port")
        @Config.DefaultInt(20)
        public int pollutionPerSecondMultiIndustrialFishingPond;
        // pollutionPerSecondMultiLargeRocketEngine;
        @Config.Comment("pollution rate in gibbl/s for the Large semifluid burner")
        @Config.DefaultInt(1_280)
        public int pollutionPerSecondMultiLargeSemiFluidGenerator;
        @Config.Comment("pollution rate in gibbl/s for the Matter fabrication CPU")
        @Config.DefaultInt(40)
        public int pollutionPerSecondMultiMassFabricator;
        @Config.Comment("pollution rate in gibbl/s for the Reactor fuel processing plant")
        @Config.DefaultInt(4_000)
        public int pollutionPerSecondMultiRefinery;
        @Config.Comment("pollution rate in gibbl/s for the Industrial Rock Breaker")
        @Config.DefaultInt(100)
        public int pollutionPerSecondMultiIndustrialRockBreaker;
        @Config.Comment("pollution rate in gibbl/s for the Industrial Chisel")
        @Config.DefaultInt(50)
        public int pollutionPerSecondMultiIndustrialChisel;
        @Config.Comment("pollution rate in gibbl/s for the Tree growth simulator")
        @Config.DefaultInt(100)
        public int pollutionPerSecondMultiTreeFarm;
        @Config.Comment("pollution rate in gibbl/s for the Flotation cell regulator")
        @Config.DefaultInt(0)
        public int pollutionPerSecondMultiFrothFlotationCell;
        @Config.Comment("pollution rate in gibbl/s for the Large-Scale auto assembler v1.01")
        @Config.DefaultInt(500)
        public int pollutionPerSecondMultiAutoCrafter;
        @Config.Comment("pollution rate in gibbl/s for the Nuclear salt processing plant")
        @Config.DefaultInt(500)
        public int pollutionPerSecondNuclearSaltProcessingPlant;
        @Config.Comment("pollution rate in gibbl/s for the Multiblock Molecular Transformer")
        @Config.DefaultInt(1_000)
        public int pollutionPerSecondMultiMolecularTransformer;

        @Config.Comment("pollution rate in gibbl/s for the Elemental Duplicator")
        @Config.DefaultInt(1_000)
        public int pollutionPerSecondElementalDuplicator;

        @Config.Comment("pollution rate in gibbl/s for the Thermal boiler")
        @Config.DefaultInt(700)
        public int pollutionPerSecondMultiThermalBoiler;
        @Config.Comment("pollution rate in gibbl/s for the Algae farm")
        @Config.DefaultInt(0)
        public int pollutionPerSecondMultiAlgaePond;
        @Config.Comment("base pollution rate in gibbl/s for the single block semi fluid generators")
        @Config.DefaultInt(40)
        public int basePollutionPerSecondSemiFluidGenerator;
        @Config.Comment("coefficient applied to the base rate of the single block semi fluid generators based on its tier (first is tier 0 aka ULV)")
        @Config.DefaultDoubleList({ 0.0, 2.0, 4.0, 8.0, 12.0, 16.0 })
        public double[] pollutionReleasedByTierSemiFluidGenerator;
        @Config.Comment("base pollution rate in gibbl/s for the single block boilers")
        @Config.DefaultInt(35)
        public int basePollutionPerSecondBoiler;
        @Config.Comment("coefficient applied to the base rate of the single block boilers based on its tier (first is tier 0 aka ULV)")
        @Config.DefaultDoubleList({ 0.0, 1.0, 1.43, 1.86 })
        public double[] pollutionReleasedByTierBoiler;
        @Config.Comment("minimum base pollution rate in gibbl/s for the single block rocket engines")
        @Config.DefaultInt(250)
        public int baseMinPollutionPerSecondRocketFuelGenerator;
        @Config.Comment("maximum base pollution rate in gibbl/s for the single block rocket engines")
        @Config.DefaultInt(2_000)
        public int baseMaxPollutionPerSecondRocketFuelGenerator;
        @Config.Comment("coefficient applied to the base rate of the single block rocket engines based on its tier (first is tier 0 aka ULV)")
        @Config.DefaultDoubleList({ 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0 })
        public double[] pollutionReleasedByTierRocketFuelGenerator;
        @Config.Comment("base pollution rate in gibbl/s for the geothermal engines")
        @Config.DefaultInt(100)
        public int basePollutionPerSecondGeothermalGenerator;
        @Config.Comment("coefficient applied to the base rate of the single block geothermal engines based on its tier (first is tier 0 aka ULV)")
        @Config.DefaultDoubleList({ 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0 })
        public double[] pollutionReleasedByTierGeothermalGenerator;
    }

    @Config.Comment("Features section")
    public static class Features {
        @Config.Comment("Hides every filled IC2 Universal Cell from NEI.")
        @Config.DefaultBoolean(true)
        public boolean hideUniversalCells;
    }

    @Config.Comment("Visual section")
    public static class Visual {

        @Config.Comment("Enables Animated GT++ Textures")
        @Config.DefaultBoolean(true)
        public boolean enableAnimatedTextures;

        @Config.Comment("Use GT textures")
        @Config.DefaultBoolean(true)
        public boolean useGregtechTextures;
    }

    @Config.Comment("Worldgen section")
    public static class Worldgen {

        @Config.Comment("The ID of the Toxic Everglades.")
        @Config.DefaultInt(227)
        public int EVERGLADES_ID;
        @Config.Comment("The biome within the Toxic Everglades.")
        @Config.DefaultInt(199)
        public int EVERGLADESBIOME_ID;
    }
}
