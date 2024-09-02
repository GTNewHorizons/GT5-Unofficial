package galacticgreg;

import static gregtech.api.enums.Mods.AppliedEnergistics2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import galacticgreg.auxiliary.GalacticGregConfig;
import galacticgreg.auxiliary.LogHelper;
import galacticgreg.auxiliary.ProfilingStorage;
import galacticgreg.command.AEStorageCommand;
import galacticgreg.command.ProfilingCommand;
import galacticgreg.registry.GalacticGregRegistry;
import galacticgreg.schematics.SpaceSchematicHandler;
import gregtech.GT_Version;
import gregtech.api.world.GTWorldgen;

@Mod(
    modid = GalacticGreg.MODID,
    name = GalacticGreg.MODNAME,
    version = GalacticGreg.VERSION,
    dependencies = "after:GalacticraftCore; required-after:gregtech@5.09.32.30;",
    acceptableRemoteVersions = "*")
public class GalacticGreg {

    public static final List<GTWorldgen> smallOreWorldgenList = new ArrayList<>();
    public static final List<GTWorldgen> oreVeinWorldgenList = new ArrayList<>();

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
