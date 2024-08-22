package gregtech.common.config;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "general",configSubDirectory = "GregTech",filename = "GregTech")
public class ConfigGeneral {
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


    @Config.Comment("If true, enable the timber axe (cuts down whole tree in a single hit).")
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

    @Config.Comment("Max entity amount in the same block for entity craming.")
    @Config.DefaultInt(6)
    @Config.RequiresMcRestart
    public static int mMaxEqualEntitiesAtOneSpot;

    @Config.Comment("The chance of success to start a fire from the flint and steel.")
    @Config.DefaultInt(30)
    @Config.RequiresMcRestart
    public static int mFlintChance;

    @Config.Comment("Entity despawn time.")
    @Config.DefaultInt(6000)
    @Config.RequiresMcRestart
    public static int mItemDespawnTime;

    @Config.Comment("If true, allow small boiler automation.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mAllowSmallBoilerAutomation;

    @Config.Comment("If True, disable vanilla oregen.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mDisableVanillaOres;

    @Config.Comment("If true, increase dungeon loots in vanilla structures.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mIncreaseDungeonLoot;

    @Config.Comment("If true, spawns an axe at the start in adventure mode. Does nothing if the advanture mode isn't forced.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mAxeWhenAdventure;

    @Config.Comment("If true, forces the survival map into adventure mode.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mSurvivalIntoAdventure;

    @Config.Comment("If true, hungers the players based on his amount of stuff in the inventory every 6s, regardless of player movement.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mHungerEffect;

    @Config.Comment("If true, enables the item oredification of the items in the inventory.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mInventoryUnification;

    @Config.Comment("if true, enables GT5U and GT++ bees.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mGTBees;

    @Config.Comment("if true, enables crafting unification.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mCraftingUnification;

    @Config.Comment("If true, nerf planks recipes.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mNerfedWoodPlank;

    @Config.Comment("if true, reduced the durability of the vanilla tools.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mNerfedVanillaTools;

    @Config.Comment("if true, enables GT5U achievements.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mAchievements;

    @Config.Comment("if true, hide unused ores.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mHideUnusedOres;

    @Config.Comment("if true, enables all the materials in GT5U.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mEnableAllMaterials;

    @Config.Comment("Controls the amount of tick over the lag averaging is done with the scanner.")
    @Config.DefaultInt(25)
    @Config.RequiresMcRestart
    public static int TICKS_FOR_LAG_AVERAGING;

    @Config.Comment("Controls the threshold (in ms) above which a lag warning is issued in log for a specific tile entity.")
    @Config.DefaultInt(100)
    @Config.RequiresMcRestart
    public static int MILLISECOND_THESHOLD_UNTIL_LAG_WARNING;
}
