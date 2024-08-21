package gregtech.common;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.GT_Mod;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "general",configSubDirectory = "GregTech",filename = "GregTech")
public class GT_Config {

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

    @Config.Comment("Control percentage of filled 3x3 chunks. Lower number means less oreveins spawn.")
    @Config.DefaultInt(100)
    @Config.RequiresMcRestart
    public static int oreveinPercentage;

    @Config.Comment("Control number of attempts to find a valid orevein. Generally this maximum limit isn't hit, selecting a vein is cheap")
    @Config.DefaultInt(64)
    @Config.RequiresMcRestart
    public static int oreveinAttempts;

    @Config.Comment("Control number of attempts to place a valid ore vein. If a vein wasn't placed due to height restrictions, completely in the water, etc, another attempt is tried.")
    @Config.DefaultInt(8)
    @Config.RequiresMcRestart
    public static int oreveinMaxPlacementAttempts;

    @Config.Comment("Whether to place small ores as placer ores for an orevein.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean oreveinPlacerOres;

    @Config.Comment("Multiplier to control how many placer ores get generated.")
    @Config.DefaultInt(2)
    @Config.RequiresMcRestart
    public static int oreveinPlacerOresMultiplier;


    @Config.Comment("If true, enable the timber axe.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean sTimber;

    @Config.Comment("If true, all the GT5U potions are always drinkable.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean sDrinksAlwaysDrinkable;

    @Config.Comment("if true, shows all the metaitems in creative and in NEI.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean sDoShowAllItemsInCreative;

    @Config.Comment("if true, makes the GT5U sounds multi-threaded.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean sMultiThreadedSounds;
}
