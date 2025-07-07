package gtPlusPlus.core.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import gtPlusPlus.api.objects.Logger;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReflectionUtils {

    private static final Map<String, CachedField> mCachedFields = new HashMap<>();

    private static class CachedField {

        private final Field FIELD;

        public CachedField(Field aField, boolean isStatic) {
            FIELD = aField;
        }

        public Field get() {
            return FIELD;
        }

    }

    private static boolean cacheField(Class<?> aClass, Field aField) {
        if (aField == null) {
            return false;
        }
        boolean isStatic = Modifier.isStatic(aField.getModifiers());
        CachedField y = mCachedFields.get(aClass.getName() + "." + aField.getName());
        if (y == null) {
            mCachedFields.put(aClass.getName() + "." + aField.getName(), new CachedField(aField, isStatic));
            return true;
        }
        return false;
    }

    /**
     * Returns a cached {@link Field} object.
     *
     * @param aClass     - Class containing the Method.
     * @param aFieldName - Field name in {@link String} form.
     * @return - Valid, non-final, {@link Field} object, or {@link null}.
     */
    public static Field getField(final Class<?> aClass, final String aFieldName) {
        if (aClass == null || aFieldName == null || aFieldName.length() == 0) {
            return null;
        }
        CachedField y = mCachedFields.get(aClass.getName() + "." + aFieldName);
        if (y == null) {
            Field u;
            try {
                u = getField_Internal(aClass, aFieldName);
                if (u != null) {
                    Logger.REFLECTION("Caching Field '" + aFieldName + "' from " + aClass.getName());
                    cacheField(aClass, u);
                    return u;
                }
            } catch (NoSuchFieldException e) {}
            return null;

        } else {
            return y.get();
        }
    }

    /*
     * Utility Functions
     */

    public static void makeFieldAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(
            field.getDeclaringClass()
                .getModifiers())) {
            field.setAccessible(true);
        }
    }

    /*
     * Below Code block is used for determining generic types associated with type<E>
     */

    /*
     * End of Generics Block
     */

    private static Field getField_Internal(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
        try {
            Logger.REFLECTION("Field: Internal Lookup: " + fieldName);
            Field k = clazz.getDeclaredField(fieldName);
            makeFieldAccessible(k);
            return k;
        } catch (final NoSuchFieldException e) {
            Logger.REFLECTION("Field: Internal Lookup Failed: " + fieldName);
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                Logger.REFLECTION("Unable to find field '" + fieldName + "'");
                throw e;
            }
            Logger.REFLECTION("Method: Recursion Lookup: " + fieldName + " - Checking in " + superClass.getName());
            return getField_Internal(superClass, fieldName);
        }
    }

    public static <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException e) {}
        return null;
    }

}
