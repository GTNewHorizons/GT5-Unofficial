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

package kubatech.api.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionHelper {

    private static class _FieldsMethods {

        final HashMap<String, Field> fields = new HashMap<>();
        final HashMap<String, Method> methods = new HashMap<>();
    }

    private static final HashMap<String, _FieldsMethods> classes = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getField(Object obj, String fieldName, T defaultvalue) {
        Class<?> cl = obj.getClass();
        String clName = cl.getName();
        HashMap<String, Field> classmap = classes.computeIfAbsent(clName, s -> new _FieldsMethods()).fields;
        try {
            if (classmap.containsKey(fieldName)) {
                Field f = classmap.get(fieldName);
                if (f == null) return defaultvalue;
                return (T) f.get(obj);
            }
            boolean exceptionDetected;
            Field f = null;
            do {
                exceptionDetected = false;
                try {
                    f = cl.getDeclaredField(fieldName);
                    f.setAccessible(true);
                } catch (Exception ex) {
                    exceptionDetected = true;
                    cl = cl.getSuperclass();
                }
            } while (exceptionDetected && !cl.equals(Object.class));
            classmap.put(fieldName, f);
            if (f == null) return defaultvalue;
            return (T) f.get(obj);
        } catch (Exception ex) {
            return defaultvalue;
        }
    }

    public static <T> boolean setField(Object obj, String fieldName, T value) {
        Class<?> cl = obj.getClass();
        String clName = cl.getName();
        HashMap<String, Field> classmap = classes.computeIfAbsent(clName, s -> new _FieldsMethods()).fields;
        try {
            if (classmap.containsKey(fieldName)) {
                Field f = classmap.get(fieldName);
                if (f == null) return false;
                f.set(obj, value);
                return true;
            }
            boolean exceptionDetected;
            Field f = null;
            do {
                exceptionDetected = false;
                try {
                    f = cl.getDeclaredField(fieldName);
                    f.setAccessible(true);
                } catch (Exception ex) {
                    exceptionDetected = true;
                    cl = cl.getSuperclass();
                }
            } while (exceptionDetected && !cl.equals(Object.class));
            classmap.put(fieldName, f);
            if (f == null) return false;
            f.set(obj, value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static <T> T getField(Object obj, String fieldName) {
        return getField(obj, fieldName, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T callMethod(Object obj, String methodName, T defaultValue, Object... args) {
        Class<?> cl = obj.getClass();
        String clName = cl.getName();
        HashMap<String, Method> classmap = classes.computeIfAbsent(clName, s -> new _FieldsMethods()).methods;
        StringBuilder builder = new StringBuilder(methodName);
        Class<?>[] argsTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            Class<?> arg = args[i].getClass();
            builder.append(";")
                .append(arg.getSimpleName());
            argsTypes[i] = arg;
        }
        String methodNameUnique = builder.toString();
        try {
            if (classmap.containsKey(methodNameUnique)) {
                Method m = classmap.get(methodNameUnique);
                if (m == null) return defaultValue;
                return (T) m.invoke(obj, args);
            }
            boolean exceptionDetected;
            Method m = null;
            do {
                exceptionDetected = false;
                try {
                    m = cl.getDeclaredMethod(methodName, argsTypes);
                    m.setAccessible(true);
                } catch (Exception ex) {
                    exceptionDetected = true;
                    cl = cl.getSuperclass();
                }
            } while (exceptionDetected && !cl.equals(Object.class));
            classmap.put(methodNameUnique, m);
            if (m == null) return defaultValue;
            return (T) m.invoke(obj, args);
        } catch (Exception ex) {
            return defaultValue;
        }
    }
}
