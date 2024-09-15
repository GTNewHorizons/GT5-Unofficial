package gtPlusPlus.core.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {

    public static void handleConfigFile(final FMLPreInitializationEvent event) {
        final Configuration config = new Configuration(
            new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
        config.load();
    }
}
