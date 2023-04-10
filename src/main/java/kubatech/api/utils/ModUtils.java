/*
 * KubaTech - Gregtech Addon Copyright (C) 2022 - 2023 kuba6000 This library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version. This library is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this library. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package kubatech.api.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import javax.xml.bind.DatatypeConverter;

import kubatech.kubatech;

import net.minecraft.launchwrapper.Launch;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ModUtils {

    public static final boolean isDeobfuscatedEnvironment = (boolean) Launch.blackboard
        .get("fml.deobfuscatedEnvironment");
    public static boolean isClientSided = false;
    private static final HashMap<String, String> classNamesToModIDs = new HashMap<>();
    private static final Map.Entry<String, String> emptyEntry = new AbstractMap.SimpleEntry<>("", "");

    public static String getModNameFromClassName(String classname) {
        if (classNamesToModIDs.size() == 0) {
            classNamesToModIDs.put("net.minecraft", "Minecraft");
            Loader.instance()
                .getActiveModList()
                .forEach(m -> {
                    Object Mod = m.getMod();
                    if (Mod != null) {
                        Package modPackage = Mod.getClass()
                            .getPackage();
                        if (modPackage == null) { // HOW CAN THIS EVEN HAPPEN ?!
                            kubatech.warn("Mod " + m.getModId() + " package is not loaded yet!");
                            return;
                        }
                        classNamesToModIDs.put(modPackage.getName(), m.getName());
                    }
                });
        }
        return classNamesToModIDs.entrySet()
            .stream()
            .filter(e -> classname.startsWith(e.getKey()))
            .findAny()
            .orElse(emptyEntry)
            .getValue();
    }

    private static String modListVersion = null;

    public static String getModListVersion() {
        if (modListVersion != null) return modListVersion;
        @SuppressWarnings("unchecked")
        ArrayList<ModContainer> modlist = (ArrayList<ModContainer>) ((ArrayList<ModContainer>) Loader.instance()
            .getActiveModList()).clone();
        String sortedList = modlist.stream()
            .filter(m -> m.getMod() != null)
            .sorted(Comparator.comparing(ModContainer::getModId))
            .collect(
                StringBuilder::new,
                (a, b) -> a.append(b.getModId())
                    .append(b.getVersion()),
                (a, b) -> a.append(", ")
                    .append(b))
            .toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            modListVersion = DatatypeConverter.printHexBinary(md.digest(sortedList.getBytes(StandardCharsets.UTF_8)))
                .toUpperCase();
            return modListVersion;
        } catch (Exception e) {
            modListVersion = sortedList;
            return sortedList;
        }
    }

    private static String modListVersionIgnoringModVersions = null;

    public static String getModListVersionIgnoringModVersions() {
        if (modListVersionIgnoringModVersions != null) return modListVersionIgnoringModVersions;
        @SuppressWarnings("unchecked")
        ArrayList<ModContainer> modlist = (ArrayList<ModContainer>) ((ArrayList<ModContainer>) Loader.instance()
            .getActiveModList()).clone();
        String sortedList = modlist.stream()
            .filter(m -> m.getMod() != null)
            .sorted(Comparator.comparing(ModContainer::getModId))
            .collect(
                StringBuilder::new,
                (a, b) -> a.append(b.getModId()),
                (a, b) -> a.append(", ")
                    .append(b))
            .toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            modListVersionIgnoringModVersions = DatatypeConverter
                .printHexBinary(md.digest(sortedList.getBytes(StandardCharsets.UTF_8)))
                .toUpperCase();
            return modListVersionIgnoringModVersions;
        } catch (Exception e) {
            modListVersionIgnoringModVersions = sortedList;
            return sortedList;
        }
    }
}
