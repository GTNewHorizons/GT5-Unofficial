package gregtech.common.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.ModIDs.GREG_TECH,
    category = "machine_stats",
    configSubDirectory = "GregTech",
    filename = "MachineStats")
@Config.LangKey("GT5U.gui.config.machine_stats")
public class MachineStats {

    @Config.Comment("Bronze solar boiler section")
    public static BronzeSolarBoiler bronzeSolarBoiler = new BronzeSolarBoiler();

    @Config.Comment("Steel solar boiler section")
    public static SteelSolarBoiler steelSolarBoiler = new SteelSolarBoiler();

    @Config.Comment("Machines section")
    public static Machines machines = new Machines();

    @Config.Comment("Mass fabricator section")
    public static MassFabricator massFabricator = new MassFabricator();

    @Config.Comment("Microwave energy transmitter section")
    public static MicrowaveEnergyTransmitter microwaveEnergyTransmitter = new MicrowaveEnergyTransmitter();

    @Config.Comment("Teleporter section")
    public static Teleporter teleporter = new Teleporter();

    @Config.Comment("Cleanroom section")
    public static Cleanroom cleanroom = new Cleanroom();

    @Config.LangKey("GT5U.gui.config.machine_stats.bronze_solar_boiler")
    public static class BronzeSolarBoiler {

        @Config.Comment({ "Number of run-time ticks before boiler starts calcification.",
            "100% calcification and minimal output will be reached at 2 times this." })
        @Config.DefaultInt(1_080_000)
        @Config.RequiresMcRestart
        public int calcificationTicks;

        @Config.Comment("Number of ticks it takes to lose 1°C.")
        @Config.DefaultInt(45)
        @Config.RequiresMcRestart
        public int cooldownTicks;

        @Config.DefaultInt(120)
        @Config.RequiresMcRestart
        public int maxOutputPerSecond;

        @Config.DefaultInt(40)
        @Config.RequiresMcRestart
        public int minOutputPerSecond;
    }

    @Config.LangKey("GT5U.gui.config.machine_stats.steel_solar_boiler")
    public static class SteelSolarBoiler {

        @Config.Comment({ "Number of run-time ticks before boiler starts calcification.",
            "100% calcification and minimal output will be reached at 2 times this." })
        @Config.DefaultInt(1_080_000)
        @Config.RequiresMcRestart
        public int calcificationTicks;

        @Config.Comment("Number of ticks it takes to lose 1°C.")
        @Config.DefaultInt(75)
        @Config.RequiresMcRestart
        public int cooldownTicks;

        @Config.DefaultInt(360)
        @Config.RequiresMcRestart
        public int maxOutputPerSecond;

        @Config.DefaultInt(120)
        @Config.RequiresMcRestart
        public int minOutputPerSecond;
    }

    @Config.LangKey("GT5U.gui.config.machine_stats.machines")
    public static class Machines {

        @Config.Comment("Controls the damageFactorLow variable in the maintenance damage equation.")
        @Config.DefaultInt(5)
        @Config.RequiresMcRestart
        public int damageFactorLow;

        @Config.Comment("Controls the damageFactorHigh variable in the maintenance damage equation.")
        @Config.DefaultFloat(0.6f)
        @Config.RequiresMcRestart
        public float damageFactorHigh;

        @Config.Comment("if true, disable maintenance checks.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean disableMaintenanceChecks;

        @Config.Comment("If true, allows for multiple eggs on the magical energy absorber.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean allowMultipleEggs;

        @Config.Comment("If true, requires at least a free face to open a machine gui.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean forceFreeFace;
    }

    @Config.LangKey("GT5U.gui.config.machine_stats.mass_fabricator")
    public static class MassFabricator {

        @Config.Comment("if true, requires UUA to run the mass fab.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean requiresUUA;

        @Config.Comment("Duration multiplier.")
        @Config.DefaultInt(3215)
        @Config.RequiresMcRestart
        public int durationMultiplier;

        @Config.Comment("mb of UUA per UUM.")
        @Config.DefaultInt(1)
        @Config.RequiresMcRestart
        public int UUAPerUUM;

        @Config.Comment("Speed bonus delivered by the UUA.")
        @Config.DefaultInt(4)
        @Config.RequiresMcRestart
        public int UUASpeedBonus;
    }

    @Config.LangKey("GT5U.gui.config.machine_stats.microwave_energy_transmitter")
    public static class MicrowaveEnergyTransmitter {

        @Config.Comment("if true, it has a passive energy loss.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean passiveEnergyUse;

        @Config.Comment("max loss.")
        @Config.DefaultInt(50)
        @Config.RequiresMcRestart
        public int maxLoss;

        @Config.Comment("max loss distance.")
        @Config.DefaultInt(10_000)
        @Config.RequiresMcRestart
        public int maxLossDistance;
    }

    @Config.LangKey("GT5U.gui.config.machine_stats.teleporter")
    public static class Teleporter {

        @Config.Comment("if true, allows interdim tp")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean interDimensionalTPAllowed;

        @Config.Comment("passive energy loss.")
        @Config.DefaultInt(2048)
        @Config.RequiresMcRestart
        public int passiveEnergyDrain;

        @Config.Comment("power multiplier.")
        @Config.DefaultInt(100)
        @Config.RequiresMcRestart
        public int powerMultiplier;
    }

    @Config.LangKey("GT5U.gui.config.machine_stats.cleanroom")
    public static class Cleanroom {

        @Config.Comment("Minimum number of plascrete blocks in a valid cleanroom.")
        @Config.RangeInt(min = 0)
        @Config.DefaultInt(20)
        @Config.RequiresMcRestart
        public int minCasingCount;

        @Config.Comment("Maximum percentage of plascrete blocks which can be replaced by other valid blocks: glass, doors, hatches, etc.")
        @Config.RangeInt(min = 0, max = 100)
        @Config.DefaultInt(30)
        @Config.RequiresMcRestart
        public int maxReplacementPercentage;

        @Config.Comment("List of other blocks allowed as a part of the cleanroom. Format: <block name> or <block name>:<meta>.")
        @Config.DefaultStringList({ "BW_GlasBlocks", // All Bart glass (including HV tier)
            "tile.openblocks.elevator", "tile.openblocks.elevator_rotating", // OpenBlocks elevators
            "tile.blockTravelAnchor", // EnderIO travel anchors
            "tile.blockCosmeticOpaque:2", // TC Warded glass (usually HV tier)
            "tile.extrautils:etherealglass" // ExtraUtils ineffable glass and variants
        })
        @Config.RequiresMcRestart
        public String[] allowedBlocks;
    }
}
