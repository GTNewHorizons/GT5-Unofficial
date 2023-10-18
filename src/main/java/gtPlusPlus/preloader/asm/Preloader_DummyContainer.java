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
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.Preloader_Logger;

public class Preloader_DummyContainer extends DummyModContainer {

    public Preloader_DummyContainer() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = CORE_Preloader.MODID;
        meta.name = CORE_Preloader.NAME;
        meta.version = CORE_Preloader.VERSION;
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
        Preloader_Logger.INFO("Loading " + CORE_Preloader.MODID + " V" + CORE_Preloader.VERSION);
        // Handle GT++ Config
        handleConfigFile(event);
    }

    public static void handleConfigFile(final FMLPreInitializationEvent event) {
        final Configuration config = new Configuration(
                new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
        config.load();

        // BGM Watchdog
        CORE_Preloader.enableWatchdogBGM = config.getInt(
                "enableWatchdogBGM",
                "features",
                0,
                0,
                Short.MAX_VALUE,
                "Set to a value greater than 0 to reduce the ticks taken to delay between BGM tracks. Acceptable Values are 1-32767, where 0 is disabled. Vanilla Uses 12,000 & 24,000. 200 is 10s.");

        // Circuits
        CORE_Preloader.enableOldGTcircuits = config.getBoolean(
                "enableOldGTcircuits",
                GregTech.ID,
                false,
                "Restores circuits and their recipes from Pre-5.09.28 times.");
    }

}
