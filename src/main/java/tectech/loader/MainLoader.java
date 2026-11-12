package tectech.loader;

import static tectech.TecTech.LOGGER;
import static tectech.TecTech.creativeTabTecTech;
import static tectech.TecTech.proxy;

import net.minecraft.util.DamageSource;

import cpw.mods.fml.common.ProgressManager;
import tectech.TecTech;
import tectech.loader.gui.CreativeTabTecTech;
import tectech.loader.recipe.BaseRecipeLoader;
import tectech.loader.recipe.ResearchStationAssemblyLine;
import tectech.loader.thing.CoverLoader;
import tectech.loader.thing.MachineLoader;
import tectech.loader.thing.ThingsLoader;

@SuppressWarnings("deprecation")
public final class MainLoader {

    public static DamageSource microwaving;

    private MainLoader() {}

    public static void preLoad() {
        creativeTabTecTech = new CreativeTabTecTech("TecTech");

        // set expanded texture arrays for tiers
        // Textures.run();

        ProgressManager.ProgressBar progressBarPreload = ProgressManager.push("TecTech Preload", 1);

        progressBarPreload.step("Regular Things");
        new ThingsLoader().run();
        LOGGER.info("Block/Item Init Done");

        ProgressManager.pop(progressBarPreload);
    }

    public static void load() {
        ProgressManager.ProgressBar progressBarLoad = ProgressManager.push("TecTech Loader", 5);

        progressBarLoad.step("Machine Things");
        new MachineLoader().run();
        LOGGER.info("Machine Init Done");

        progressBarLoad.step("Cover Things");
        new CoverLoader().run();
        LOGGER.info("Cover Init Done");

        progressBarLoad.step("Add damage types");
        microwaving = new DamageSource("microwaving").setDamageBypassesArmor();
        LOGGER.info("Damage types addition Done");

        progressBarLoad.step("Register Packet Dispatcher");
        NetworkDispatcher.registerPackets();
        LOGGER.info("Packet Dispatcher registered");

        progressBarLoad.step("Register GUI Handler");
        proxy.registerRenderInfo();
        LOGGER.info("GUI Handler registered");

        ProgressManager.pop(progressBarLoad);
    }

    public static void postLoad() {
        new BaseRecipeLoader().run();
        TecTech.LOGGER.info("Recipe Init Done");
    }

    public static void onLoadCompleted() {
        new ResearchStationAssemblyLine().runLateRecipes();
    }
}
