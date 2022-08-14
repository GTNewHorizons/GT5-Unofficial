/*
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package kubatech;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class Config {

    private static class Categories {
        public static final String mobHandler = "MobHandler";
    }

    public static boolean mobHandlerEnabled = true;
    public static boolean includeEmptyMobs = true;
    public static String[] mobBlacklist;
    public static File configFile;

    public static void init(File configFile) {
        Config.configFile = configFile;
    }

    public static void synchronizeConfiguration() {
        Configuration configuration = new Configuration(configFile);
        configuration.load();

        mobHandlerEnabled = configuration
                .get(
                        Categories.mobHandler,
                        "Enabled",
                        true,
                        "Enable \"Mob Drops\" NEI page and Extreme Extermination Chamber")
                .getBoolean();
        includeEmptyMobs = configuration
                .get(Categories.mobHandler, "IncludeEmptyMobs", true, "Include mobs that have no drops in NEI")
                .getBoolean();
        mobBlacklist = configuration
                .get(
                        Categories.mobHandler,
                        "MobBlacklist",
                        new String[] {
                            "Giant",
                            "Thaumcraft.TravelingTrunk",
                            "chisel.snowman",
                            "OpenBlocks.Luggage",
                            "OpenBlocks.MiniMe",
                            "SpecialMobs.SpecialCreeper",
                            "SpecialMobs.SpecialZombie",
                            "SpecialMobs.SpecialPigZombie",
                            "SpecialMobs.SpecialSlime",
                            "SpecialMobs.SpecialSkeleton",
                            "SpecialMobs.SpecialEnderman",
                            "SpecialMobs.SpecialCaveSpider",
                            "SpecialMobs.SpecialGhast",
                            "SpecialMobs.SpecialWitch",
                            "SpecialMobs.SpecialSpider",
                            "TwilightForest.HydraHead",
                            "TwilightForest.RovingCube",
                            "TwilightForest.Harbinger Cube",
                            "TwilightForest.Adherent",
                            "SpecialMobs.SpecialSilverfish",
                        },
                        "These mobs will be skipped when generating recipe map")
                .getStringList();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
