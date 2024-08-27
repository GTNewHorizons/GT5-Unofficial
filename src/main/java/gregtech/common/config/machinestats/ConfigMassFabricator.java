package gregtech.common.config.machinestats;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "mass_fabricator",
    configSubDirectory = "GregTech",
    filename = "MachineStats")
public class ConfigMassFabricator {

    @Config.Comment("if true, requires UUA to run the mass fab.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean requiresUUA;

    @Config.Comment("Duration multiplier.")
    @Config.DefaultInt(3215)
    @Config.RequiresMcRestart
    public static int durationMultiplier;

    @Config.Comment("mb of UUA per UUM.")
    @Config.DefaultInt(1)
    @Config.RequiresMcRestart
    public static int UUAPerUUM;

    @Config.Comment("Speed bonus delivered by the UUA.")
    @Config.DefaultInt(40)
    @Config.RequiresMcRestart
    public static int UUASpeedBonus;
}
