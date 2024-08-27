package gregtech.common.config.gregtech;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "features", configSubDirectory = "GregTech", filename = "GregTech")
public class ConfigFeatures {

    @Config.Comment("Controls the stacksize of tree related blocks.")
    @Config.DefaultInt(64)
    @Config.RequiresMcRestart
    public static int maxLogStackSize;

    @Config.Comment("Controls the stacksize of every oredicted prefix based items used for blocks (if that even makes sense)")
    @Config.DefaultInt(64)
    @Config.RequiresMcRestart
    public static int maxOtherBlocksStackSize;

    @Config.Comment("Controls the stacksize of oredicted planks.")
    @Config.DefaultInt(64)
    @Config.RequiresMcRestart
    public static int maxPlankStackSize;

    @Config.Comment("Controls the stacksize of oredicted items used in ore treatment.")
    @Config.DefaultInt(64)
    @Config.RequiresMcRestart
    public static int maxOreStackSize;

    @Config.Comment("Controls the stacksize of IC2 overclocker upgrades.")
    @Config.DefaultInt(4)
    @Config.RequiresMcRestart
    public static int upgradeStackSize;
}
