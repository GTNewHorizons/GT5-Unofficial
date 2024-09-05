package gtPlusPlus.preloader.asm;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;

import java.io.File;
import java.util.Collections;

import net.minecraftforge.common.config.Configuration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gtPlusPlus.preloader.PreloaderCore;
import gtPlusPlus.preloader.PreloaderLogger;

public class PreloaderDummyContainer extends DummyModContainer {

    public PreloaderDummyContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = PreloaderCore.MODID;
        meta.name = PreloaderCore.NAME;
        meta.version = PreloaderCore.VERSION;
        meta.credits = "Roll Credits ...";
        meta.authorList = Collections.singletonList("Alkalus");
        meta.screenshots = new String[0];
        meta.parent = GTPlusPlus.ID;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        PreloaderLogger.INFO("Loading " + PreloaderCore.MODID + " V" + PreloaderCore.VERSION);
        // Handle GT++ Config
        handleConfigFile(event);
    }

    public static void handleConfigFile(final FMLPreInitializationEvent event) {
        final Configuration config = new Configuration(
            new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
        config.load();

        // BGM Watchdog
        PreloaderCore.enableWatchdogBGM = config.getInt(
            "enableWatchdogBGM",
            "features",
            0,
            0,
            Short.MAX_VALUE,
            "Set to a value greater than 0 to reduce the ticks taken to delay between BGM tracks. Acceptable Values are 1-32767, where 0 is disabled. Vanilla Uses 12,000 & 24,000. 200 is 10s.");

        // Circuits
        PreloaderCore.enableOldGTcircuits = config.getBoolean(
            "enableOldGTcircuits",
            GregTech.ID,
            false,
            "Restores circuits and their recipes from Pre-5.09.28 times.");
    }

}
