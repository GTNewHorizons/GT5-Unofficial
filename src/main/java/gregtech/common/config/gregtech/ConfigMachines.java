package gregtech.common.config.gregtech;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.GREG_TECH, category = "machines", configSubDirectory = "GregTech", filename = "GregTech")
public class ConfigMachines {

    @Config.Comment("Number of ticks between sending sound packets to clients for electric machines. Default is 1.5 seconds. Trying to mitigate lag and FPS drops.")
    @Config.DefaultInt(30)
    @Config.RequiresMcRestart
    public static int ticksBetweenSounds;

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
        "emt.tile.solar.compressed.TileEntityQuintupleAirSolar", "emt.tile.solar.compressed.TileEntitySextupleAirSolar",
        "emt.tile.solar.compressed.TileEntitySeptupleAirSolar", "emt.tile.solar.compressed.TileEntityOctupleAirSolar",
        "emt.tile.solar.dark.TileEntityDarkSolar", "emt.tile.solar.dark.TileEntityDoubleDarkSolar",
        "emt.tile.solar.dark.TileEntityTripleDarkSolar", "emt.tile.solar.dark.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.dark.TileEntityQuintupleAirSolar", "emt.tile.solar.dark.TileEntitySextupleAirSolar",
        "emt.tile.solar.dark.TileEntitySeptupleAirSolar", "emt.tile.solar.dark.TileEntityOctupleAirSolar",
        "emt.tile.solar.earth.TileEntityDoubleEarthSolar", "emt.tile.solar.earth.TileEntityEarthSolar",
        "emt.tile.solar.earth.TileEntityTripleEarthSolar", "emt.tile.solar.earth.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.earth.TileEntityQuintupleAirSolar", "emt.tile.solar.earth.TileEntitySextupleAirSolar",
        "emt.tile.solar.earth.TileEntitySeptupleAirSolar", "emt.tile.solar.earth.TileEntityOctupleAirSolar",
        "emt.tile.solar.fire.TileEntityDoubleFireSolar", "emt.tile.solar.fire.TileEntityFireSolar",
        "emt.tile.solar.fire.TileEntityTripleFireSolar", "emt.tile.solar.fire.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.fire.TileEntityQuintupleAirSolar", "emt.tile.solar.fire.TileEntitySextupleAirSolar",
        "emt.tile.solar.fire.TileEntitySeptupleAirSolar", "emt.tile.solar.fire.TileEntityOctupleAirSolar",
        "emt.tile.solar.order.TileEntityDoubleOrderSolar", "emt.tile.solar.order.TileEntityOrderSolar",
        "emt.tile.solar.order.TileEntityTripleOrderSolar", "emt.tile.solar.order.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.order.TileEntityQuintupleAirSolar", "emt.tile.solar.order.TileEntitySextupleAirSolar",
        "emt.tile.solar.order.TileEntitySeptupleAirSolar", "emt.tile.solar.order.TileEntityOctupleAirSolar",
        "emt.tile.solar.water.TileEntityDoubleWaterSolar", "emt.tile.solar.water.TileEntityTripleWaterSolar",
        "emt.tile.solar.water.TileEntityWaterSolar", "emt.tile.solar.water.TileEntityQuadrupleAirSolar",
        "emt.tile.solar.water.TileEntityQuintupleAirSolar", "emt.tile.solar.water.TileEntitySextupleAirSolar",
        "emt.tile.solar.water.TileEntitySeptupleAirSolar", "emt.tile.solar.water.TileEntityOctupleAirSolar",
        "com.lulan.compactkineticgenerators.tileentity.TileCkgE",
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
    public static String[] blacklistedTileEntiyClassNamesForWA;

    @Config.Comment("This will set the percentage how much ReinforcedGlass is Allowed in Cleanroom Walls.")
    @Config.DefaultFloat(5.0f)
    @Config.RequiresMcRestart
    public static float cleanroomGlass;

    @Config.Comment("This will let machines such as drills and pumps chunkload their work area.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean enableChunkloaders;

    @Config.Comment("This will make all chunkloading machines act as World Anchors (true) or Passive Anchors (false).")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean alwaysReloadChunkloaders;

    @Config.Comment("If true, then digital chest with AE2 storage bus will be accessible only through AE2")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean disableDigitalChestsExternalAccess;

    @Config.Comment("If true, machines can explode.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean machineExplosions;

    @Config.Comment("If true, machine can take fire.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean machineFlammable;

    @Config.Comment("If true, explodes if the machine is dismantled without a wrench.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean machineNonWrenchExplosions;

    @Config.Comment("If true, burn the wires on explosion. (by sending IV amps into the cables)")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean machineWireFire;

    @Config.Comment("If true, machine will randomly explode if there is fire on adjacent blocks.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean machineFireExplosions;

    @Config.Comment("If true, will randomly explode if it is raining.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean machineRainExplosions;

    @Config.Comment("If true, will randomly explode during thunderstorm if the machine can be exposed to rain.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean machineThunderExplosions;

    @Config.Comment("If true, enable the guis of the machines to get a tint and it will be of the color of the dye applied to the machine.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean coloredGUI;

    @Config.Comment("If true and if the machine tint is activated, the guis will have a uniform metallic tint no matter what color is applied to the machines.")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean machineMetalGUI;

    // Implementation for this is actually handled in NewHorizonsCoreMod in MainRegistry.java!
    @Config.Comment("If true, use the definition of the metallic tint in GT5U, otherwise NHCore will set it to white.")
    @Config.DefaultBoolean(true)
    @Config.RequiresMcRestart
    public static boolean useMachineMetal;

    @Config.Comment("if true, enables MuTEs(multitile entities) to be added to the game. MuTEs are in the start of development and its not recommended to enable them unless you know what you are doing. (always activated in dev env)")
    @Config.DefaultBoolean(false)
    @Config.RequiresMcRestart
    public static boolean enableMultiTileEntities;
}
