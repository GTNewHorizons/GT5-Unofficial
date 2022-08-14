/*
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022  kuba6000
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package kubatech.api.utils;

import cpw.mods.fml.common.Loader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.launchwrapper.Launch;

public class ModUtils {
    public static final boolean isDeobfuscatedEnvironment =
            (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static boolean isClientSided = false;
    private static final HashMap<String, String> classNamesToModIDs = new HashMap<>();
    private static final Map.Entry<String, String> emptyEntry = new AbstractMap.SimpleEntry<>("", "");

    public static String getModNameFromClassName(String classname) {
        if (classNamesToModIDs.size() == 0) {
            classNamesToModIDs.put("net.minecraft", "Minecraft");
            Loader.instance().getActiveModList().forEach(m -> {
                Object Mod = m.getMod();
                if (Mod != null)
                    classNamesToModIDs.put(Mod.getClass().getPackage().getName(), m.getName());
            });
        }
        return classNamesToModIDs.entrySet().stream()
                .filter(e -> classname.startsWith(e.getKey()))
                .findAny()
                .orElse(emptyEntry)
                .getValue();
    }
}
