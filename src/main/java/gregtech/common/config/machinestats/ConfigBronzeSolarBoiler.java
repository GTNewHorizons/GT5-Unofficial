package gregtech.common.config.machinestats;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "bronze_solar_boiler",
    configSubDirectory = "GregTech",
    filename = "MachineStats")
public class ConfigBronzeSolarBoiler {

    @Config.Comment({ "Number of run-time ticks before boiler starts calcification.",
        "100% calcification and minimal output will be reached at 2 times this." })
    @Config.DefaultInt(1_080_000)
    @Config.RequiresMcRestart
    public static int calcificationTicks;

    @Config.Comment("Number of ticks it takes to lose 1°C.")
    @Config.DefaultInt(45)
    @Config.RequiresMcRestart
    public static int cooldownTicks;

    @Config.DefaultInt(120)
    @Config.RequiresMcRestart
    public static int maxOutputPerSecond;

    @Config.DefaultInt(40)
    @Config.RequiresMcRestart
    public static int minOutputPerSecond;
}
