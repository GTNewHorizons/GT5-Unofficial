package gregtech.common.config.worldgen;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "general",
    configSubDirectory = "GregTech",
    filename = "WorldGeneration")
public class ConfigGeneral {

    @Config.Comment("if true, enables basalt ore gen.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean generateBasaltOres;

    @Config.Comment("if true, enables black granite ore gen.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean generateBlackGraniteOres;

    @Config.Comment("if true, enables marble ore gen.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean generateMarbleOres;

    @Config.Comment("if true, enables red granite ore gen.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean generateRedGraniteOres;

    @Config.Comment("If true, disables vanilla oregen.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean disableVanillaOres;

    @Config.Comment("if true, enables underground dirt gen. Does nothing if the vanilla oregen is enabled!")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean generateUndergroundDirtGen;

    @Config.Comment("if true, enables underground gravel gen. Does nothing if the vanilla oregen is enabled!")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean generateUndergroundGravelGen;
}
