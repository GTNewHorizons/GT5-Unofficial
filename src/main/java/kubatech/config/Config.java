/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
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

        public static double playerOnlyDropsModifier = .1d;

        private static void load(Configuration configuration) {
            Category category = Category.MOB_HANDLER;
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
