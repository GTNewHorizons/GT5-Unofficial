package gregtech.common.config.machinestats;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "teleporter",
    configSubDirectory = "GregTech",
    filename = "MachineStats")
public class ConfigTeleporter {

    @Config.Comment("if true, allows interdim tp")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean interDimensionalTPAllowed;

    @Config.Comment("passive energy loss.")
    @Config.DefaultInt(2048)
    @Config.RequiresMcRestart
    public static int passiveEnergyDrain;

    @Config.Comment("power multiplier.")
    @Config.DefaultInt(100)
    @Config.RequiresMcRestart
    public static int powerMultiplier;
}
