package bloodasp.galacticgreg;

import static gregtech.api.enums.Mods.AppliedEnergistics2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bloodasp.galacticgreg.auxiliary.GalacticGregConfig;
import bloodasp.galacticgreg.auxiliary.LogHelper;
import bloodasp.galacticgreg.auxiliary.ProfilingStorage;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_Layer_Space;
import bloodasp.galacticgreg.bartworks.BW_Worldgen_Ore_SmallOre_Space;
import bloodasp.galacticgreg.command.AEStorageCommand;
import bloodasp.galacticgreg.command.ProfilingCommand;
import bloodasp.galacticgreg.registry.GalacticGregRegistry;
import bloodasp.galacticgreg.schematics.SpaceSchematicHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import gregtech.GT_Version;
import gregtech.api.GregTech_API;
import gregtech.api.world.GT_Worldgen;

@Mod(
    modid = GalacticGreg.MODID,
    name = GalacticGreg.MODNAME,
    version = GalacticGreg.VERSION,
    dependencies = "after:GalacticraftCore; required-after:gregtech@5.09.32.30;",
    acceptableRemoteVersions = "*")
public class GalacticGreg {

    public static final List<GT_Worldgen> smallOreWorldgenList = new ArrayList<>();
    public static final List<GT_Worldgen> oreVeinWorldgenList = new ArrayList<>();

    public static final String NICE_MODID = "GalacticGreg";
    public static final String MODID = "galacticgreg";
    public static final String MODNAME = "Galactic Greg";

    public static final String VERSION = GT_Version.VERSION;

    public static final LogHelper Logger = new LogHelper(NICE_MODID);
    public static ProfilingStorage Profiler = new ProfilingStorage();
    public static SpaceSchematicHandler SchematicHandler;

    public static Random GalacticRandom = null;

    public static GalacticGregConfig GalacticConfig = null;

    /**
     * Preload phase. Read config values and set various features.. n stuff...
     *
     * @param aEvent
     */
    @EventHandler
    public void onPreLoad(FMLPreInitializationEvent aEvent) {
        GalacticConfig = new GalacticGregConfig(aEvent.getModConfigurationDirectory(), NICE_MODID, NICE_MODID);
        if (!GalacticConfig.LoadConfig()) GalacticGreg.Logger
            .warn("Something went wrong while reading GalacticGregs config file. Things will be wonky..");

        GalacticRandom = new Random(System.currentTimeMillis());

        if (GalacticConfig.SchematicsEnabled)
            SchematicHandler = new SpaceSchematicHandler(aEvent.getModConfigurationDirectory());

        Logger.trace("Leaving PRELOAD");
    }

    public static final ArrayList<Runnable> ADDITIONALVEINREGISTER = new ArrayList<>();

    /**
     * Postload phase. Mods can add their custom definition to our api in their own PreLoad or Init-phase Once
     * GalacticGregRegistry.InitRegistry() is called, no changes are accepted. (Well you can with reflection, but on a
     * "normal" way it's not possible)
     *
     * @param aEvent
     */
    @EventHandler
    public void onPostLoad(FMLPostInitializationEvent aEvent) {
        Logger.trace("Entering POSTLOAD");

        if (!GalacticGregRegistry.InitRegistry()) throw new RuntimeException(
            "GalacticGreg registry has been finalized from a 3rd-party mod, this is forbidden!");

        // new WorldGenGaGT().run(); DO NOT UNCOMMENT, was moved to gregtech.loaders.postload.GT_Worldgenloader

        for (int f = 0,
            j = GregTech_API.sWorldgenFile.get("worldgen.GaGregBartworks", "AmountOfCustomLargeVeinSlots", 0); f
                < j; f++) {
            new BW_Worldgen_Ore_Layer_Space(
                "mix.custom." + (f < 10 ? "0" : "") + f,
                GregTech_API.sWorldgenFile
                    .get("worldgen.GaGregBartworks." + "mix.custom." + (f < 10 ? "0" : "") + f, "Enabled", false));
        }

        for (int f = 0, j = GregTech_API.sWorldgenFile.get("worldgen.GaGregBartworks", "AmountOfCustomSmallSlots", 0); f
            < j; f++) {
            new BW_Worldgen_Ore_SmallOre_Space(
                "small.custom." + (f < 10 ? "0" : "") + f,
                GregTech_API.sWorldgenFile
                    .get("worldgen.GaGregBartworks." + "small.custom." + (f < 10 ? "0" : "") + f, "Enabled", false));
        }

        for (Runnable r : ADDITIONALVEINREGISTER) {
            try {
                r.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        GalacticConfig.serverPostInit();

        Logger.trace("Leaving POSTLOAD");
    }

    /**
     * If oregen profiling is enabled, then register the command
     *
     * @param pEvent
     */
    @EventHandler
    public void serverLoad(FMLServerStartingEvent pEvent) {
        Logger.trace("Entering SERVERLOAD");

        if (GalacticConfig.ProfileOreGen) pEvent.registerServerCommand(new ProfilingCommand());

        if (AppliedEnergistics2.isModLoaded() && GalacticConfig.EnableAEExportCommand
            && GalacticConfig.SchematicsEnabled) pEvent.registerServerCommand(new AEStorageCommand());

        Logger.trace("Leaving SERVERLOAD");
    }
}
