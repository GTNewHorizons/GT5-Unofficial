/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
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
 * spotless:on
 */

package kubatech.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import kubatech.Tags;

public class Config {

    private enum Category {

        MOB_HANDLER("MobHandler"),
        DEBUG("Debug");

        final String categoryName;

        Category(String s) {
            categoryName = s;
        }

        public String get() {
            return categoryName;
        }

        @Override
        public String toString() {
            return get();
        }
    }

    public static class MobHandler {

        public static boolean mobHandlerEnabled = true;

        public enum _CacheRegenerationTrigger {

            Never,
            ModAdditionRemoval,
            ModAdditionRemovalChange,
            Always;

            public static _CacheRegenerationTrigger get(int oridinal) {
                return values()[oridinal];
            }
        }

        public static _CacheRegenerationTrigger regenerationTrigger = _CacheRegenerationTrigger.ModAdditionRemovalChange;
        public static boolean includeEmptyMobs = true;
        public static String[] mobBlacklist;
        public static double playerOnlyDropsModifier = .1d;

        private static void load(Configuration configuration) {
            Category category = Category.MOB_HANDLER;
            mobHandlerEnabled = configuration
                .get(category.get(), "Enabled", true, "Enable \"Mob Drops\" NEI page and Extreme Extermination Chamber")
                .getBoolean();
            StringBuilder c = new StringBuilder("When will cache regeneration trigger? ");
            for (_CacheRegenerationTrigger value : _CacheRegenerationTrigger.values()) c.append(value.ordinal())
                .append(" - ")
                .append(value.name())
                .append(", ");
            regenerationTrigger = _CacheRegenerationTrigger.get(
                configuration
                    .get(
                        category.get(),
                        "CacheRegenerationTrigger",
                        _CacheRegenerationTrigger.ModAdditionRemovalChange.ordinal(),
                        c.toString())
                    .getInt());
            includeEmptyMobs = configuration
                .get(category.get(), "IncludeEmptyMobs", true, "Include mobs that have no drops in NEI")
                .getBoolean();
            mobBlacklist = configuration
                .get(
                    category.get(),
                    "MobBlacklist",
                    new String[] { "Giant", "Thaumcraft.TravelingTrunk", "chisel.snowman", "OpenBlocks.Luggage",
                        "OpenBlocks.MiniMe", "SpecialMobs.SpecialCreeper", "SpecialMobs.SpecialZombie",
                        "SpecialMobs.SpecialPigZombie", "SpecialMobs.SpecialSlime", "SpecialMobs.SpecialSkeleton",
                        "SpecialMobs.SpecialEnderman", "SpecialMobs.SpecialCaveSpider", "SpecialMobs.SpecialGhast",
                        "SpecialMobs.SpecialWitch", "SpecialMobs.SpecialSpider", "TwilightForest.HydraHead",
                        "TwilightForest.RovingCube", "TwilightForest.Harbinger Cube", "TwilightForest.Adherent",
                        "SpecialMobs.SpecialSilverfish", },
                    "These mobs will be skipped when generating recipe map")
                .getStringList();
            playerOnlyDropsModifier = configuration
                .get(
                    category.get(),
                    "PlayerOnlyDropsModifier",
                    .1d,
                    "Hard player only loot (such as core mod drops) will be multiplied by this number")
                .getDouble();
        }
    }

    public static class Debug {

        public static boolean showRenderErrors = false;

        private static void load(Configuration configuration) {
            Category category = Category.DEBUG;
            showRenderErrors = configuration.get(category.get(), "ShowRenderErrors", false)
                .getBoolean();
        }
    }

    public static File configFile;
    public static File configDirectory;

    public static void init(File configFile) {
        configDirectory = new File(configFile, Tags.MODID);
        Config.configFile = new File(configDirectory, Tags.MODID + ".cfg");
    }

    public static File getConfigFile(String file) {
        return new File(configDirectory, file);
    }

    public static void synchronizeConfiguration() {
        Configuration configuration = new Configuration(configFile);
        configuration.load();

        MobHandler.load(configuration);
        Debug.load(configuration);

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
