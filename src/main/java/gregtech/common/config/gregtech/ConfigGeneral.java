package gregtech.common.config.gregtech;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.GT_Mod;
import gregtech.api.enums.Mods;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineBase;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_Cleanroom;

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

    @Config.Comment("if true, drops the content of the machine inventory before exploding.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mExplosionItemDrop;

    @Config.Comment("if true, enables the cleanroom multi.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mEnableCleanroom;

    @Config.Comment("if true, enables low gravity requirement in some crafts. Is forced to false if GalactiCraft is not present.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mLowGravProcessing;

    @Config.Comment("if true, crops need a block below to fully grow.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mCropNeedBlock;

    @Config.Comment("if yes, allows the automatic interactions with the maintenance hatches.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mAMHInteraction;

    @Config.Comment("if true, mixed ores only yields the equivalent of 2/3 of the pure ores.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean mMixedOreOnlyYieldsTwoThirdsOfPureOre;

    @Config.Comment("if true, rich ores yield twice as much as normal ores.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mRichOreYieldMultiplier;

    @Config.Comment("if true, nether ores yield twice as much as normal ores.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mNetherOreYieldMultiplier;

    @Config.Comment("if true, end ores yield twice as much as normal ores.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean mEndOreYieldMultiplier;

    @Config.Comment("if true, enables GT6 styled pipe connections.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean gt6Pipe;

    @Config.Comment("if true, enables GT6 styled wire connections.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean gt6Cable;

    @Config.Comment("if true, allows GT5U cables to be IC2 power sources.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean ic2EnergySourceCompat;

    @Config.Comment("if true, wires will require soldering material to be connected.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean costlyCableConnection;

    @Config.Comment("if true, crashes on null recipe input.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean crashOnNullRecipeInput;

    @Config.Comment("if true, enable placeholder for material names in lang file.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean i18nPlaceholder;

    @Config.Comment("if true,.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean harderMobSpawner;
    @Config.Comment("Controls the minimum distance allowed for the long distance pipelines to form.")
    @Config.DefaultInt(64)
    @Config.RequiresMcRestart
    public static int minimalDistancePoints;

    @Config.Comment("This will set the blacklist of blocks for CTM blocks.")
    @Config.DefaultStringList({"team.chisel.block.BlockRoadLine"})
    @Config.RequiresMcRestart
    public static String[] CTMBlacklist;

    @Config.Comment("This will set the whitelist of blocks for CTM blocks.")
    @Config.DefaultStringList({"team.chisel.block.BlockCarvable",
        "team.chisel.block.BlockCarvableGlass" })
    @Config.RequiresMcRestart
    public static String[] CTMWhitelist;

    @Config.Comment("if true, log all the oredict in logs/OreDict.log.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean loggingOreDict;

    @Config.Comment("if true, log all the oredict in logs/Explosion.log.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean loggingExplosions;

    @Config.Comment("if true, log all the oredict in logs/PlayerActivity.log.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean loggingPlayerActicity;
}
