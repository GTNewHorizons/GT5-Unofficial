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

import java.lang.reflect.Field;
import java.util.HashMap;

public class ReflectionHelper {
    private static final HashMap<String, HashMap<String, Field>> fields = new HashMap<>();

    public static <T> T getField(Object obj, String fieldName, boolean useBasicTypes, T defaultvalue) {
        Class<?> cl = obj.getClass();
        String clName = cl.getName();
        HashMap<String, Field> classmap = fields.computeIfAbsent(clName, s -> new HashMap<>());
        try {
            if (classmap.containsKey(fieldName)) {
                return (T) classmap.get(fieldName).get(obj);
            }
            boolean exceptionDetected = false;
            Field f = null;
            do {
                try {
                    f = cl.getDeclaredField(fieldName);
                    f.setAccessible(true);
                } catch (Exception ex) {
                    exceptionDetected = true;
                    cl = cl.getSuperclass();
                }
            } while (exceptionDetected && !cl.equals(Object.class));
            if (f == null) return defaultvalue;
            classmap.put(fieldName, f);
            return (T) f.get(obj);
        } catch (Exception ex) {
            return defaultvalue;
        }
    }

    public static <T> T getField(Object obj, String fieldName, boolean useBasicTypes) {
        return getField(obj, fieldName, useBasicTypes, null);
    }

    public static <T> T getField(Object obj, String fieldName) {
        return getField(obj, fieldName, true, null);
    }
}
