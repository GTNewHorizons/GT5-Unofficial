package com.gtnewhorizon.gtnhintergalactic.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

/**
 * Config file of this mod
 *
 * @author minecraft7771
 */
public class Config {

    /** Category for Space Elevator settigns */
    public static String CATEGORY_SPACE_ELEVATOR = "spaceElevator";

    // Space Elevator
    /** Whether to render the cable was block or with renderer */
    public static boolean isCableRenderingEnabled = true;

    /**
     * Read the configuration file
     *
     * @param configFile File to be read
     */
    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        isCableRenderingEnabled = configuration.getBoolean(
                "isCableRenderingEnabled",
                CATEGORY_SPACE_ELEVATOR,
                isCableRenderingEnabled,
                "If the Space Elevator should use it's fancy renderer, or simple block renderer");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
