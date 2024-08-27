package gregtech.common.config.worldgen;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(
    modid = Mods.Names.GREG_TECH,
    category = "end_asteroids",
    configSubDirectory = "GregTech",
    filename = "WorldGeneration")
public class ConfigEndAsteroids {

    @Config.Comment("The maximum size for the end asteroids.")
    @Config.DefaultInt(200)
    @Config.RequiresMcRestart
    public static int EndAsteroidMaxSize;

    @Config.Comment("The minimum size for the end asteroids.")
    @Config.DefaultInt(200)
    @Config.RequiresMcRestart
    public static int EndAsteroidMinSize;

    @Config.Comment("The probability weight to generate end asteroids.")
    @Config.DefaultInt(300)
    @Config.RequiresMcRestart
    public static int EndAsteroidProbability;

    @Config.Comment("if true, enables end asteroids.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean generateEndAsteroids;
}
