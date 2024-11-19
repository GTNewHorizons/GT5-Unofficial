package com.gtnewhorizons.gtnhintergalactic.config;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.G_T_N_H_INTERGALACTIC, filename = "gtnhintergalactic")
public class IGConfig {

    public static SpaceElevator spaceElevator = new SpaceElevator();

    @Config.Comment("Space Elevator section")
    public static class SpaceElevator {

        @Config.Comment("If true, the Space Elevator will use it's fancy renderer, otherwise a simple block renderer")
        @Config.DefaultBoolean(true)
        public boolean isCableRenderingEnabled;
    }
}
