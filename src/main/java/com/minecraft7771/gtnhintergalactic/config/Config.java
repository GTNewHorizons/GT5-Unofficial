package com.minecraft7771.gtnhintergalactic.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String CATEGORY_SPACE_ELEVATOR = "spaceElevator";

    // Space Elevator
    public static boolean isCableRenderingEnabled = true;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        isCableRenderingEnabled = configuration.getBoolean("isCableRenderingEnabled", CATEGORY_SPACE_ELEVATOR, isCableRenderingEnabled, "If the Space Elevator should use it's fancy renderer, or simple block renderer");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
