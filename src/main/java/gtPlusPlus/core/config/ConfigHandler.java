package gtPlusPlus.core.config;

import static gregtech.api.enums.Mods.GregTech;
import static gtPlusPlus.core.lib.GTPPCore.ConfigSwitches.*;
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

        // Features
        enableCustomCapes = config.getBoolean("enableSupporterCape", "features", true, "Enables Custom GT++ Cape.");

        enableWatchdogBGM = config.getInt(
            "enableWatchdogBGM",
            "features",
            0,
            0,
            Short.MAX_VALUE,
            "Set to a value greater than 0 to reduce the ticks taken to delay between BGM tracks. Acceptable Values are 1-32767, where 0 is disabled. Vanilla Uses 12,000 & 24,000. 200 is 10s.");
        hideUniversalCells = config
            .getBoolean("hideUniversalCells", "features", true, "Hides every filled IC2 Universal Cell from NEI.");

        // Biomes
        EVERGLADES_ID = config.getInt("darkworld_ID", "worldgen", 227, 1, 254, "The ID of the Dark Dimension.");
        EVERGLADESBIOME_ID = config
            .getInt("darkbiome_ID", "worldgen", 238, 1, 254, "The biome within the Dark Dimension.");

        // Visual
        enableAnimatedTextures = config
            .getBoolean("enableAnimatedTextures", "visual", true, "Enables Animated GT++ Textures, Requires Restart");
        config.save();
    }
}
