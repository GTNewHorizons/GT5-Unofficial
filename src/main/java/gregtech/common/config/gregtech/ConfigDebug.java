package gregtech.common.config.gregtech;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "debug", configSubDirectory = "GregTech", filename = "GregTech")
public class ConfigDebug {

    @Config.Comment("enable D1 flag (a set of debug logs)")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean D1;

    @Config.Comment("enable D2 flag (another set of debug logs)")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean D2;

    @Config.Comment("This will prevent NEI from crashing but spams the Log.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean allowBrokenRecipeMap;

    @Config.Comment("Debug parameters for cleanroom testing.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugCleanroom;

    @Config.Comment("Debug parameter for driller testing.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugDriller;

    @Config.Comment("Debug parameter for world generation. Tracks chunks added/removed from run queue.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugWorldgen;

    @Config.Comment("Debug parameter for orevein generation.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugOrevein;

    @Config.Comment("Debug parameter for small ore generation.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugSmallOres;

    @Config.Comment("Debug parameter for stones generation.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugStones;

    @Config.Comment("Debug parameter for single block miner.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugBlockMiner;

    @Config.Comment("Debug parameter for single block pump.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugBlockPump;

    @Config.Comment("Debug parameter for entity cramming reduction.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugEntityCramming;

    @Config.Comment("Debug parameter for gregtech.api.util.GT_ChunkAssociatedData")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugWorldData;

    @Config.Comment("Debug parameter for chunk loaders.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean debugChunkloaders;
}
