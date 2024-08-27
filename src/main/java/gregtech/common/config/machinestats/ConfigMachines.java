package gregtech.common.config.machinestats;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "machines", configSubDirectory = "GregTech", filename = "MachineStats")
public class ConfigMachines {

    @Config.Comment("Controls the damageFactorLow variable in the maintenance damage equation.")
    @Config.DefaultInt(5)
    @Config.RequiresMcRestart
    public static int damageFactorLow;

    @Config.Comment("Controls the damageFactorHigh variable in the maintenance damage equation.")
    @Config.DefaultFloat(0.6f)
    @Config.RequiresMcRestart
    public static float damageFactorHigh;

    @Config.Comment("if true, disable maintenance checks.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean disableMaintenanceChecks;

    @Config.Comment("If true, allows for multiple eggs on the magical energy absorber.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean allowMultipleEggs;

    @Config.Comment("If true, requires at least a free face to open a machine gui.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean forceFreeFace;
}
