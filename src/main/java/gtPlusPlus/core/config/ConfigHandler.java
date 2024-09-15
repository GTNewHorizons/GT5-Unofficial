package gtPlusPlus.core.config;

import static gtPlusPlus.core.lib.GTPPCore.EVERGLADESBIOME_ID;
import static gtPlusPlus.core.lib.GTPPCore.EVERGLADES_ID;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ConfigHandler {

    public static void handleConfigFile(final FMLPreInitializationEvent event) {
        final Configuration config = new Configuration(
            new File(event.getModConfigurationDirectory(), "GTplusplus/GTplusplus.cfg"));
        config.load();

        // Biomes
        EVERGLADES_ID = config.getInt("darkworld_ID", "worldgen", 227, 1, 254, "The ID of the Dark Dimension.");
        EVERGLADESBIOME_ID = config
            .getInt("darkbiome_ID", "worldgen", 238, 1, 254, "The biome within the Dark Dimension.");

    }
}
