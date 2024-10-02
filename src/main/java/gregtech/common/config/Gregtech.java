package gregtech.common.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;
import gregtech.common.GTProxy;

@Config(modid = Mods.Names.GREG_TECH, category = "gregtech", configSubDirectory = "GregTech", filename = "GregTech")
@Config.LangKey("GT5U.gui.config.gregtech")
public class Gregtech {

    @Config.Comment("Debug section")
    public static final Debug debug = new Debug();

    @Config.Comment("Features section")
    public static final Features features = new Features();

    @Config.Comment("General section")
    public static final General general = new General();

    @Config.Comment("Harvest level section")
    public static final HarvestLevel harvestLevel = new HarvestLevel();

    @Config.Comment("Machines section")
    public static final Machines machines = new Machines();

    @Config.Comment("Ore drop behavior section")
    public static final OreDropBehavior oreDropBehavior = new OreDropBehavior();

    @Config.Comment("Pollution section")
    public static final Pollution pollution = new Pollution();

    @Config.LangKey("GT5U.gui.config.gregtech.debug")
    public static class Debug {

        @Config.Comment("enable D1 flag (a set of debug logs)")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean D1;

        @Config.Comment("enable D2 flag (another set of debug logs)")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean D2;

        @Config.Comment("This will prevent NEI from crashing but spams the Log.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean allowBrokenRecipeMap;

        @Config.Comment("Debug parameters for cleanroom testing.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugCleanroom;

        @Config.Comment("Debug parameter for driller testing.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugDriller;

        @Config.Comment("Debug parameter for world generation. Tracks chunks added/removed from run queue.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugWorldgen;

        @Config.Comment("Debug parameter for orevein generation.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugOrevein;

        @Config.Comment("Debug parameter for small ore generation.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugSmallOres;

        @Config.Comment("Debug parameter for stones generation.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugStones;

        @Config.Comment("Debug parameter for single block miner.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugBlockMiner;

        @Config.Comment("Debug parameter for single block pump.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugBlockPump;

        @Config.Comment("Debug parameter for entity cramming reduction.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugEntityCramming;

        @Config.Comment("Debug parameter for gregtech.api.util.GT_ChunkAssociatedData")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugWorldData;

        @Config.Comment("Debug parameter for chunk loaders.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean debugChunkloaders;
    }

    @Config.LangKey("GT5U.gui.config.gregtech.features")
    public static class Features {

        @Config.Comment("Controls the stacksize of tree related blocks.")
        @Config.DefaultInt(64)
        @Config.RequiresMcRestart
        public int maxLogStackSize;

        @Config.Comment("Controls the stacksize of every oredicted prefix based items used for blocks (if that even makes sense)")
        @Config.DefaultInt(64)
        @Config.RequiresMcRestart
        public int maxOtherBlocksStackSize;

        @Config.Comment("Controls the stacksize of oredicted planks.")
        @Config.DefaultInt(64)
        @Config.RequiresMcRestart
        public int maxPlankStackSize;

        @Config.Comment("Controls the stacksize of oredicted items used in ore treatment.")
        @Config.DefaultInt(64)
        @Config.RequiresMcRestart
        public int maxOreStackSize;

        @Config.Comment("Controls the stacksize of IC2 overclocker upgrades.")
        @Config.DefaultInt(4)
        @Config.RequiresMcRestart
        public int upgradeStackSize;
    }

    @Config.LangKey("GT5U.gui.config.gregtech.general")
    public static class General {

        @Config.Comment("Control percentage of filled 3x3 chunks. Lower number means less oreveins spawn.")
        @Config.DefaultInt(100)
        @Config.RequiresMcRestart
        public int oreveinPercentage;

        @Config.Comment("Control number of attempts to find a valid orevein. Generally this maximum limit isn't hit, selecting a vein is cheap")
        @Config.DefaultInt(64)
        @Config.RequiresMcRestart
        public int oreveinAttempts;

        @Config.Comment("Control number of attempts to place a valid ore vein. If a vein wasn't placed due to height restrictions, completely in the water, etc, another attempt is tried.")
        @Config.DefaultInt(8)
        @Config.RequiresMcRestart
        public int oreveinMaxPlacementAttempts;

        @Config.Comment("Whether to place small ores as placer ores for an orevein.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean oreveinPlacerOres;

        @Config.Comment("Multiplier to control how many placer ores get generated.")
        @Config.DefaultInt(2)
        @Config.RequiresMcRestart
        public int oreveinPlacerOresMultiplier;

        @Config.Comment("If true, enables the timber axe (cuts down whole tree in a single hit).")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean timber;

        @Config.Comment("If true, all the GT5U potions are always drinkable.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean drinksAlwaysDrinkable;

        @Config.Comment("if true, shows all the metaitems in creative and in NEI.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean doShowAllItemsInCreative;

        @Config.Comment("if true, makes the GT5U sounds multi-threaded.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean multiThreadedSounds;

        @Config.Comment("Max entity amount in the same block for entity craming.")
        @Config.DefaultInt(6)
        @Config.RequiresMcRestart
        public int maxEqualEntitiesAtOneSpot;

        @Config.Comment("The chance of success to start a fire from the flint and steel.")
        @Config.DefaultInt(30)
        @Config.RequiresMcRestart
        public int flintChance;

        @Config.Comment("Entity despawn time.")
        @Config.DefaultInt(6000)
        @Config.RequiresMcRestart
        public int itemDespawnTime;

        @Config.Comment("If true, allows small boiler automation.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean allowSmallBoilerAutomation;

        @Config.Comment("If true, increases dungeon loots in vanilla structures.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean increaseDungeonLoot;

        @Config.Comment("If true, spawns an axe at the start in adventure mode. Does nothing if the advanture mode isn't forced.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean axeWhenAdventure;

        @Config.Comment("If true, forces the survival map into adventure mode.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean survivalIntoAdventure;

        @Config.Comment("If true, hungers the players based on his amount of stuff in the inventory every 6s, regardless of player movement.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean hungerEffect;

        @Config.Comment("If true, enables the item oredification of the items in the inventory.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean inventoryUnification;

        @Config.Comment("if true, enables GT5U and GT++ bees.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean GTBees;

        @Config.Comment("if true, enables crafting unification.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean craftingUnification;

        @Config.Comment("If true, nerfs planks recipes.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean nerfedWoodPlank;

        @Config.Comment("if true, reduces the durability of the vanilla tools.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean nerfedVanillaTools;

        @Config.Comment("if true, enables GT5U achievements.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean achievements;

        @Config.Comment("if true, hides unused ores.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean hideUnusedOres;

        @Config.Comment("if true, enables all the materials in GT5U.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean enableAllMaterials;

        @Config.Comment("Controls the amount of tick over the lag averaging is done with the scanner.")
        @Config.DefaultInt(25)
        @Config.RequiresMcRestart
        public int ticksForLagAveraging;

        @Config.Comment("Controls the threshold (in ms) above which a lag warning is issued in log for a specific tile entity.")
        @Config.DefaultInt(100)
        @Config.RequiresMcRestart
        public int millisecondThesholdUntilLagWarning;

        @Config.Comment("if true, drops the content of the machine inventory before exploding.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean explosionItemDrop;

        @Config.Comment("if true, enables the cleanroom multi.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean enableCleanroom;

        @Config.Comment("if true, enables low gravity requirement in some crafts. Is forced to false if GalactiCraft is not present.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean lowGravProcessing;

        @Config.Comment("if true, crops need a block below to fully grow.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean cropNeedBlock;

        @Config.Comment("if yes, allows the automatic interactions with the maintenance hatches.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean autoMaintenaceHatchesInteraction;

        @Config.Comment("if true, mixed ores only yields the equivalent of 2/3 of the pure ores.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean mixedOreOnlyYieldsTwoThirdsOfPureOre;

        @Config.Comment("if true, rich ores yield twice as much as normal ores.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean richOreYieldMultiplier;

        @Config.Comment("if true, nether ores yield twice as much as normal ores.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean netherOreYieldMultiplier;

        @Config.Comment("if true, end ores yield twice as much as normal ores.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean endOreYieldMultiplier;

        @Config.Comment("if true, enables GT6 styled pipe connections.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean gt6Pipe;

        @Config.Comment("if true, enables GT6 styled wire connections.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean gt6Cable;

        @Config.Comment("if true, allows GT5U cables to be IC2 power sources.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean ic2EnergySourceCompat;

        @Config.Comment("if true, wires will require soldering material to be connected.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean costlyCableConnection;

        @Config.Comment("if true, crashes on null recipe input.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean crashOnNullRecipeInput;

        @Config.Comment("if true, enable placeholder for material names in lang file.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean i18nPlaceholder;

        @Config.Comment("if true, sets the hardness of the mobspawers to 500 and their blast resistance to 6 000 000.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean harderMobSpawner;

        @Config.Comment("Controls the minimum distance allowed for the long distance pipelines to form.")
        @Config.DefaultInt(64)
        @Config.RequiresMcRestart
        public int minimalDistancePoints;

        @Config.Comment("This will set the blacklist of blocks for CTM blocks.")
        @Config.DefaultStringList({ "team.chisel.block.BlockRoadLine" })
        @Config.RequiresMcRestart
        public String[] CTMBlacklist;

        @Config.Comment("This will set the whitelist of blocks for CTM blocks.")
        @Config.DefaultStringList({ "team.chisel.block.BlockCarvable", "team.chisel.block.BlockCarvableGlass" })
        @Config.RequiresMcRestart
        public String[] CTMWhitelist;

        @Config.Comment("if true, logs all the oredict in logs/OreDict.log.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean loggingOreDict;

        @Config.Comment("if true, logs all the oredict in logs/Explosion.log.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean loggingExplosions;

        @Config.Comment("if true, log all the oredict in logs/PlayerActivity.log.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean loggingPlayerActicity;
    }

    @Config.LangKey("GT5U.gui.config.gregtech.harvest_level")
    public static class HarvestLevel {

        @Config.Comment("Activate Harvest Level Change")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean activateHarvestLevelChange;

        @Config.Comment("Maximum harvest level")
        @Config.DefaultInt(7)
        @Config.RequiresMcRestart
        public int maxHarvestLevel;

        @Config.Comment("GraniteHarvestLevel harvest level")
        @Config.DefaultInt(3)
        @Config.RequiresMcRestart
        public int graniteHarvestLevel;
    }

    @Config.LangKey("GT5U.gui.config.gregtech.machines")
    public static class Machines {

        @Config.Comment("Number of ticks between sending sound packets to clients for electric machines. Default is 1.5 seconds. Trying to mitigate lag and FPS drops.")
        @Config.DefaultInt(30)
        @Config.RequiresMcRestart
        public int ticksBetweenSounds;

        @Config.Comment("This will set the blacklist for the world accelerator in TE mode.")
        @Config.DefaultStringList({ "com.rwtema.extrautils.tileentity.enderquarry.TileEntityEnderQuarry",
            "advsolar.common.tiles.TileEntityUltimateSolarPanel", "advsolar.common.tiles.TileEntitySolarPanel",
            "advsolar.common.tiles.TileEntityQuantumSolarPanel", "advsolar.common.tiles.TileEntityHybridSolarPanel",
            "advsolar.common.tiles.TileEntityAdvancedSolarPanel", "com.supsolpans.tiles.TileAdminSolarPanel",
            "com.supsolpans.tiles.TilePhotonicSolarPanel", "com.supsolpans.tiles.TileSingularSolarPanel",
            "com.supsolpans.tiles.TileSpectralSolarPanel", "emt.tile.solar.air.TileEntityAirSolar",
            "emt.tile.solar.air.TileEntityDoubleAirSolar", "emt.tile.solar.air.TileEntityTripleAirSolar",
            "emt.tile.solar.air.TileEntityQuadrupleAirSolar", "emt.tile.solar.air.TileEntityQuintupleAirSolar",
            "emt.tile.solar.air.TileEntitySextupleAirSolar", "emt.tile.solar.air.TileEntitySeptupleAirSolar",
            "emt.tile.solar.air.TileEntityOctupleAirSolar", "emt.tile.solar.compressed.TileEntityCompressedSolar",
            "emt.tile.solar.compressed.TileEntityDoubleCompressedSolar",
            "emt.tile.solar.compressed.TileEntityTripleCompressedSolar",
            "emt.tile.solar.compressed.TileEntityQuadrupleAirSolar",
            "emt.tile.solar.compressed.TileEntityQuintupleAirSolar",
            "emt.tile.solar.compressed.TileEntitySextupleAirSolar",
            "emt.tile.solar.compressed.TileEntitySeptupleAirSolar",
            "emt.tile.solar.compressed.TileEntityOctupleAirSolar", "emt.tile.solar.dark.TileEntityDarkSolar",
            "emt.tile.solar.dark.TileEntityDoubleDarkSolar", "emt.tile.solar.dark.TileEntityTripleDarkSolar",
            "emt.tile.solar.dark.TileEntityQuadrupleAirSolar", "emt.tile.solar.dark.TileEntityQuintupleAirSolar",
            "emt.tile.solar.dark.TileEntitySextupleAirSolar", "emt.tile.solar.dark.TileEntitySeptupleAirSolar",
            "emt.tile.solar.dark.TileEntityOctupleAirSolar", "emt.tile.solar.earth.TileEntityDoubleEarthSolar",
            "emt.tile.solar.earth.TileEntityEarthSolar", "emt.tile.solar.earth.TileEntityTripleEarthSolar",
            "emt.tile.solar.earth.TileEntityQuadrupleAirSolar", "emt.tile.solar.earth.TileEntityQuintupleAirSolar",
            "emt.tile.solar.earth.TileEntitySextupleAirSolar", "emt.tile.solar.earth.TileEntitySeptupleAirSolar",
            "emt.tile.solar.earth.TileEntityOctupleAirSolar", "emt.tile.solar.fire.TileEntityDoubleFireSolar",
            "emt.tile.solar.fire.TileEntityFireSolar", "emt.tile.solar.fire.TileEntityTripleFireSolar",
            "emt.tile.solar.fire.TileEntityQuadrupleAirSolar", "emt.tile.solar.fire.TileEntityQuintupleAirSolar",
            "emt.tile.solar.fire.TileEntitySextupleAirSolar", "emt.tile.solar.fire.TileEntitySeptupleAirSolar",
            "emt.tile.solar.fire.TileEntityOctupleAirSolar", "emt.tile.solar.order.TileEntityDoubleOrderSolar",
            "emt.tile.solar.order.TileEntityOrderSolar", "emt.tile.solar.order.TileEntityTripleOrderSolar",
            "emt.tile.solar.order.TileEntityQuadrupleAirSolar", "emt.tile.solar.order.TileEntityQuintupleAirSolar",
            "emt.tile.solar.order.TileEntitySextupleAirSolar", "emt.tile.solar.order.TileEntitySeptupleAirSolar",
            "emt.tile.solar.order.TileEntityOctupleAirSolar", "emt.tile.solar.water.TileEntityDoubleWaterSolar",
            "emt.tile.solar.water.TileEntityTripleWaterSolar", "emt.tile.solar.water.TileEntityWaterSolar",
            "emt.tile.solar.water.TileEntityQuadrupleAirSolar", "emt.tile.solar.water.TileEntityQuintupleAirSolar",
            "emt.tile.solar.water.TileEntitySextupleAirSolar", "emt.tile.solar.water.TileEntitySeptupleAirSolar",
            "emt.tile.solar.water.TileEntityOctupleAirSolar", "com.lulan.compactkineticgenerators.tileentity.TileCkgE",
            "com.lulan.compactkineticgenerators.tileentity.TileCkgH",
            "com.lulan.compactkineticgenerators.tileentity.TileCkgL",
            "com.lulan.compactkineticgenerators.tileentity.TileCkgM",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwaE",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwaH",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwaL",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwaM",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwmE",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwmH",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwmL",
            "com.lulan.compactkineticgenerators.tileentity.TileCkwmM", "com.supsolpans.tiles.TileSpectralSolarPanel",
            "com.supsolpans.tiles.TileSingularSolarPanel", "com.supsolpans.tiles.TileAdminSolarPanel",
            "com.supsolpans.tiles.TilePhotonicSolarPanel", "gtPlusPlus.core.tileentities.general.TileEntityFishTrap",
            "gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest",
            "net.bdew.gendustry.machines.apiary.TileApiary", "goodgenerator.blocks.tileEntity.EssentiaHatch",
            "magicbees.tileentity.TileEntityApimancersDrainerCommon",
            "magicbees.tileentity.TileEntityApimancersDrainerGT" })
        @Config.RequiresMcRestart
        public String[] blacklistedTileEntiyClassNamesForWA;

        @Config.Comment("This will set the percentage how much ReinforcedGlass is Allowed in Cleanroom Walls.")
        @Config.DefaultFloat(5.0f)
        @Config.RequiresMcRestart
        public float cleanroomGlass;

        @Config.Comment("This will let machines such as drills and pumps chunkload their work area.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean enableChunkloaders;

        @Config.Comment("This will make all chunkloading machines act as World Anchors (true) or Passive Anchors (false).")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean alwaysReloadChunkloaders;

        @Config.Comment("If true, then digital chest with AE2 storage bus will be accessible only through AE2")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean disableDigitalChestsExternalAccess;

        @Config.Comment("If true, machines can explode.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean machineExplosions;

        @Config.Comment("If true, machine can take fire.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean machineFlammable;

        @Config.Comment("If true, explodes if the machine is dismantled without a wrench.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean machineNonWrenchExplosions;

        @Config.Comment("If true, burn the wires on explosion. (by sending IV amps into the cables)")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean machineWireFire;

        @Config.Comment("If true, machine will randomly explode if there is fire on adjacent blocks.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean machineFireExplosions;

        @Config.Comment("If true, will randomly explode if it is raining.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean machineRainExplosions;

        @Config.Comment("If true, will randomly explode during thunderstorm if the machine can be exposed to rain.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean machineThunderExplosions;

        @Config.Comment("If true, enable the guis of the machines to get a tint and it will be of the color of the dye applied to the machine.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean coloredGUI;

        @Config.Comment("If true and if the machine tint is activated, the guis will have a uniform metallic tint no matter what color is applied to the machines.")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean machineMetalGUI;

        // Implementation for this is actually handled in NewHorizonsCoreMod in MainRegistry.java!
        @Config.Comment("If true, use the definition of the metallic tint in GT5U, otherwise NHCore will set it to white.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean useMachineMetal;

        @Config.Comment("if true, enables MuTEs(multitile entities) to be added to the game. MuTEs are in the start of development and its not recommended to enable them unless you know what you are doing. (always activated in dev env)")
        @Config.DefaultBoolean(false)
        @Config.RequiresMcRestart
        public boolean enableMultiTileEntities;
    }

    @Config.LangKey("GT5U.gui.config.gregtech.ore_drop_behavior")
    public static class OreDropBehavior {

        @Config.Comment({ "Settings:",
            " - 'PerDimBlock': Sets the drop to the block variant of the ore block based on dimension, defaults to stone type",
            " - 'UnifiedBlock': Sets the drop to the stone variant of the ore block",
            " - 'Block': Sets the drop to the ore  mined",
            " - 'FortuneItem': Sets the drop to the new ore item and makes it affected by fortune"
                + " - 'Item': Sets the drop to the new ore item" })

        @Config.DefaultEnum("FortuneItem")
        @Config.RequiresMcRestart
        public GTProxy.OreDropSystem setting = GTProxy.OreDropSystem.FortuneItem;
    }

    @Config.LangKey("GT5U.gui.config.gregtech.pollution")
    public static class Pollution {

        @Config.Comment("if true, enables pollution in the game.")
        @Config.DefaultBoolean(true)
        @Config.RequiresMcRestart
        public boolean pollution;

        @Config.Comment("Controls the threshold starting from which you can see fog.")
        @Config.DefaultInt(550_000)
        @Config.RequiresMcRestart
        public int pollutionSmogLimit;
        @Config.Comment("Controls the threshold starting from which players get poison effect.")
        @Config.DefaultInt(750_000)
        @Config.RequiresMcRestart
        public int pollutionPoisonLimit;
        @Config.Comment("Controls the threshold starting from which vegetation starts to be killed.")
        @Config.DefaultInt(1_000_000)
        @Config.RequiresMcRestart
        public int pollutionVegetationLimit;
        @Config.Comment("Controls the threshold starting from which if it rains, will turn cobblestone into gravel and gravel into sand.")
        @Config.DefaultInt(2_000_000)
        @Config.RequiresMcRestart
        public int pollutionSourRainLimit;
        @Config.Comment("Controls the pollution released by an explosion.")
        @Config.DefaultInt(100_000)
        @Config.RequiresMcRestart
        public int pollutionOnExplosion;
        @Config.Comment("Controls the pollution released per second by the bricked blast furnace.")
        @Config.DefaultInt(200)
        @Config.RequiresMcRestart
        public int pollutionPrimitveBlastFurnacePerSecond;
        @Config.Comment("Controls the pollution released per second by the charcoal pile igniter.")
        @Config.DefaultInt(100)
        @Config.RequiresMcRestart
        public int pollutionCharcoalPitPerSecond;
        @Config.Comment("Controls the pollution released per second by the EBF.")
        @Config.DefaultInt(400)
        @Config.RequiresMcRestart
        public int pollutionEBFPerSecond;
        @Config.Comment("Controls the pollution released per second by the large combustion engine.")
        @Config.DefaultInt(480)
        @Config.RequiresMcRestart
        public int pollutionLargeCombustionEnginePerSecond;
        @Config.Comment("Controls the pollution released per second by the extreme combustion engine.")
        @Config.DefaultInt(3_840)
        @Config.RequiresMcRestart
        public int pollutionExtremeCombustionEnginePerSecond;
        @Config.Comment("Controls the pollution released per second by the implosion compressor.")
        @Config.DefaultInt(10_000)
        @Config.RequiresMcRestart
        public int pollutionImplosionCompressorPerSecond;
        @Config.Comment("Controls the pollution released per second by the large bronze boiler.")
        @Config.DefaultInt(1_000)
        @Config.RequiresMcRestart
        public int pollutionLargeBronzeBoilerPerSecond;
        @Config.Comment("Controls the pollution released per second by the large steel boiler.")
        @Config.DefaultInt(2_000)
        @Config.RequiresMcRestart
        public int pollutionLargeSteelBoilerPerSecond;
        @Config.Comment("Controls the pollution released per second by the large titanium boiler.")
        @Config.DefaultInt(3_000)
        @Config.RequiresMcRestart
        public int pollutionLargeTitaniumBoilerPerSecond;
        @Config.Comment("Controls the pollution released per second by the large tungstensteel boiler.")
        @Config.DefaultInt(4_000)
        @Config.RequiresMcRestart
        public int pollutionLargeTungstenSteelBoilerPerSecond;
        @Config.Comment("Controls the pollution reduction obtained with each increment of the circuit when throttling large boilers.")
        @Config.DefaultFloat(1.0f / 24.0f) // divided by 24 because there are 24 circuit configs.
        @Config.RequiresMcRestart
        public float pollutionReleasedByThrottle;
        @Config.Comment("Controls the pollution released per second by the large gas turbine.")
        @Config.DefaultInt(300)
        @Config.RequiresMcRestart
        public int pollutionLargeGasTurbinePerSecond;
        @Config.Comment("Controls the pollution released per second by the multi smelter.")
        @Config.DefaultInt(400)
        @Config.RequiresMcRestart
        public int pollutionMultiSmelterPerSecond;
        @Config.Comment("Controls the pollution released per second by the pyrolyse oven.")
        @Config.DefaultInt(300)
        @Config.RequiresMcRestart
        public int pollutionPyrolyseOvenPerSecond;
        @Config.Comment("Controls the pollution released per second by the small coil boiler.")
        @Config.DefaultInt(20)
        @Config.RequiresMcRestart
        public int pollutionSmallCoalBoilerPerSecond;
        @Config.Comment("Controls the pollution released per second by the high pressure lava boiler.")
        @Config.DefaultInt(20)
        @Config.RequiresMcRestart
        public int pollutionHighPressureLavaBoilerPerSecond;
        @Config.Comment("Controls the pollution released per second by the high pressure coil boiler.")
        @Config.DefaultInt(30)
        @Config.RequiresMcRestart
        public int pollutionHighPressureCoalBoilerPerSecond;

        @Config.Comment("Controls the pollution released per second by the base diesel generator.")
        @Config.DefaultInt(40)
        @Config.RequiresMcRestart
        public int pollutionBaseDieselGeneratorPerSecond;

        // reading double as strings, not perfect, but better than nothing
        @Config.Comment({
            "Pollution released by tier, with the following formula: PollutionBaseDieselGeneratorPerSecond * PollutionDieselGeneratorReleasedByTier[Tier]",
            "The first entry has meaning as it is here to since machine tier with array index: LV is 1, etc." })
        @Config.DefaultDoubleList({ 0.1, 1.0, 0.9, 0.8 })
        @Config.RequiresMcRestart
        public double[] pollutionDieselGeneratorReleasedByTier;

        @Config.Comment("Controls the pollution released per second by the base gas turbine.")
        @Config.DefaultInt(40)
        @Config.RequiresMcRestart
        public int pollutionBaseGasTurbinePerSecond;

        // reading double as strings, not perfect, but better than nothing
        @Config.Comment({
            "Pollution released by tier, with the following formula: PollutionBaseGasTurbinePerSecond * PollutionGasTurbineReleasedByTier[Tier]",
            "The first entry has meaning as it is here to since machine tier with array index: LV is 1, etc." })
        @Config.DefaultDoubleList({ 0.1, 1.0, 0.9, 0.8, 0.7, 0.6 })
        @Config.RequiresMcRestart
        public double[] pollutionGasTurbineReleasedByTier;
    }
}
