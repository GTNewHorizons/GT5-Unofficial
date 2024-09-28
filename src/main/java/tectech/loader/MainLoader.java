package tectech.loader;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static tectech.TecTech.LOGGER;
import static tectech.TecTech.creativeTabTecTech;
import static tectech.TecTech.proxy;

import net.minecraft.block.Block;
import net.minecraft.util.DamageSource;

import cpw.mods.fml.common.ProgressManager;
import tectech.TecTech;
import tectech.loader.gui.CreativeTabTecTech;
import tectech.loader.recipe.BaseRecipeLoader;
import tectech.loader.recipe.ResearchStationAssemblyLine;
import tectech.loader.thing.CoverLoader;
import tectech.loader.thing.MachineLoader;
import tectech.loader.thing.ThingsLoader;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.Textures;

@SuppressWarnings("deprecation")
public final class MainLoader {

    public static DamageSource microwaving;

    private MainLoader() {}

    public static void preLoad() {
        creativeTabTecTech = new CreativeTabTecTech("TecTech");

        // set expanded texture arrays for tiers
        try {
            Textures.run();
        } catch (Throwable t) {
            LOGGER.error("Loading textures...", t);
        }

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
        new NetworkDispatcher();
        LOGGER.info("Packet Dispatcher registered");

        progressBarLoad.step("Register GUI Handler");
        proxy.registerRenderInfo();
        LOGGER.info("GUI Handler registered");

        ProgressManager.pop(progressBarLoad);
    }

    public static void postLoad() {
        ProgressManager.ProgressBar progressBarPostLoad = ProgressManager.push("TecTech Post Loader", 2);

        progressBarPostLoad.step("Dreamcraft Compatibility");
        if (NewHorizonsCoreMod.isModLoaded()) {
            try {
                Class<?> clazz = Class.forName("com.dreammaster.gthandler.casings.GT_Container_CasingsNH");
                TTCasingsContainer.sBlockCasingsNH = (Block) clazz.getField("sBlockCasingsNH")
                    .get(null);

                if (TTCasingsContainer.sBlockCasingsNH == null) {
                    throw new NullPointerException("sBlockCasingsNH Is not set at this time");
                }
            } catch (Exception e) {
                throw new Error("Unable to get NH casings", e);
            }
        }

        progressBarPostLoad.step("Recipes");
        new BaseRecipeLoader().run();
        TecTech.LOGGER.info("Recipe Init Done");
    }

    public static void onLoadCompleted() {
        new ResearchStationAssemblyLine().runLateRecipes();
    }
}
