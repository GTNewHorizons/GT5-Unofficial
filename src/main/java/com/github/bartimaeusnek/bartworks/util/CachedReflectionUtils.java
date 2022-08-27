package com.github.bartimaeusnek.bartworks.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.reflect.FieldUtils;

public class CachedReflectionUtils {
    private static final ClassValue<Map<String, Field>> fields = new ConcurrentMapClassValue();
    private static final ClassValue<Map<String, Field>> declaredFields = new ConcurrentMapClassValue();

    public static Field getField(final Class<?> cls, final String fieldName) {
        return fields.get(cls).computeIfAbsent(fieldName, f -> FieldUtils.getField(cls, f, true));
    }

    public static Field getDeclaredField(final Class<?> cls, final String fieldName) {
        return declaredFields.get(cls).computeIfAbsent(fieldName, f -> FieldUtils.getDeclaredField(cls, f, true));
    }

    private static class ConcurrentMapClassValue extends ClassValue<Map<String, Field>> {
        @Override
        protected Map<String, Field> computeValue(Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    }
}
