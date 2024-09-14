package gtPlusPlus.core.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_PLUS_PLUS,configSubDirectory = "GTPlusPlus", filename = "GTPlusPlus")
@Config.LangKeyPattern(pattern = "gtpp.gui.config.%cat.%field", fullyQualified = true)
@Config.RequiresMcRestart
public class Configuration {
    public static final Debug debug = new Debug();
    public static final Machines machines = new Machines();
    public static final Gregtech gregtech = new Gregtech();

    public static class Debug{

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

    public static class Machines{
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

    public static class Gregtech{
        @Config.Comment("Rotors below this durability will be removed, prevents NEI clutter. Minimum Durability is N * x, where N is the new value set and x is the turbine size, where 1 is Tiny and 4 is Huge. Set to 0 to disable.")
        @Config.DefaultInt(25_500)
        public int turbineCutoffBase;

        // Pipes & Cables
        @Config.Comment("Adds Custom GT Fluid Pipes.")
        @Config.DefaultBoolean(true)
        public boolean enableCustom_Pipes;
        @Config.Comment("Adds Custom GT Cables.")
        @Config.DefaultBoolean(true)
        public boolean enableCustom_Cables;
        @Config.Comment("These dehydrate stuff.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_Dehydrators;
        @Config.Comment("Converts IC2 steam -> Railcraft steam.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_SteamConverter;
        @Config.Comment("Portable fluid tanks.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_FluidTanks;
        @Config.Comment("Diesel egines with different internals, they consume less fuel overall.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_RocketEngines;
        @Config.Comment("These may be overpowered, Consult a local geologist.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_GeothermalEngines;
        @Config.Comment("Tesseracts for wireless item/fluid movement.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_Tesseracts;
        @Config.Comment("Very basic automated cauldron for dust washing.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_SimpleWasher;
        @Config.Comment("Pollution Detector & Scrubbers.")
        @Config.DefaultBoolean(true)
        public boolean enableMachine_Pollution;
        @Config.Comment("Required to smelt most high tier materials from GT++. Also smelts everything else to molten metal.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_AlloyBlastSmelter;
        @Config.Comment("Spin, Spin, Spiiiin.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialCentrifuge;
        @Config.Comment("Pyro Oven Alternative, older, more realistic, better.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialCokeOven;
        @Config.Comment("Electrolyzes things with extra bling factor.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialElectrolyzer;
        @Config.Comment("A hyper efficient maceration tower, nets more bonus outputs.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialMacerationStack;
        @Config.Comment("Industrial bendering machine thingo.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialPlatePress;
        @Config.Comment("Produces fine wire and exotic cables.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialWireMill;
        @Config.Comment("?FAB?RIC?ATE MA?TT?ER.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_MatterFabricator;
        @Config.Comment("Tall tanks, each layer adds extra fluid storage.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_MultiTank;
        @Config.Comment("For managing large power grids.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_PowerSubstation;
        @Config.Comment("For supplying large power grids.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_LiquidFluorideThoriumReactor;
        @Config.Comment("Refines molten chemicals into nuclear fuels.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_NuclearFuelRefinery;
        @Config.Comment("Reprocesses depleted nuclear salts into useful chemicals.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_NuclearSaltProcessingPlant;
        @Config.Comment("Large scale sifting.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialSifter;
        @Config.Comment("Can Assemble, Disassemble and Craft Project data from Data Sticks.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_LargeAutoCrafter;
        @Config.Comment("Your warm spin for the ore thing.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialThermalCentrifuge;
        @Config.Comment("Used to wash the dirt, riiiiight offff..")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialWashPlant;
        @Config.Comment("Thermal Boiler from GT4. Can Filter Lava for resources.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_ThermalBoiler;
        @Config.Comment("Very fast and efficient Cutting Machine.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialCuttingMachine;
        @Config.Comment("Fish the seas, except on land.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialFishingPort;
        @Config.Comment("Very fast and efficient Extruding Machine.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialExtrudingMachine;
        @Config.Comment("Can run recipes for 9 different types of machines.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_IndustrialMultiMachine;
        @Config.Comment( "COMET - Scientific Cyclotron.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_Cyclotron;

        @Config.Comment( "Enables QFT.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_QuantumForceTransformer;

        @Config.Comment( "Enables TGS.")
        @Config.DefaultBoolean(true)
        public boolean enableMultiblock_TreeFarmer;

    }
}
