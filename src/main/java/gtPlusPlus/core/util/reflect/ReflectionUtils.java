package gtPlusPlus.core.util.reflect;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.reflect.ClassPath;
import com.gtnewhorizon.gtnhlib.reflect.Fields;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.data.StringUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReflectionUtils {

    public static Map<String, Class<?>> mCachedClasses = new LinkedHashMap<>();
    public static Map<String, CachedMethod> mCachedMethods = new LinkedHashMap<>();
    public static Map<String, CachedField> mCachedFields = new LinkedHashMap<>();
    public static Map<String, CachedConstructor> mCachedConstructors = new LinkedHashMap<>();
    public static Map<Field, Fields.ClassFields.Field> mCachedFieldAccessors = new LinkedHashMap<>();

    private static class CachedConstructor {

        private final Constructor<?> METHOD;

        public CachedConstructor(Constructor<?> aCons) {
            METHOD = aCons;
        }

        public Constructor<?> get() {
            return METHOD;
        }
    }

    private static class CachedMethod {

        private final Method METHOD;

        public CachedMethod(Method aMethod, boolean isStatic) {
            METHOD = aMethod;
        }

        public Method get() {
            return METHOD;
        }

    }

    private static class CachedField {

        private final Field FIELD;

        public CachedField(Field aField, boolean isStatic) {
            FIELD = aField;
        }

        public Field get() {
            return FIELD;
        }

    }

    private static Fields.ClassFields.Field cacheAccessor(Field f) {
        return mCachedFieldAccessors.computeIfAbsent(
                f,
                (field) -> Fields.ofClass(field.getDeclaringClass())
                        .getUntypedField(Fields.LookupType.DECLARED_IN_HIERARCHY, field.getName()));
    }

    private static boolean cacheClass(Class<?> aClass) {
        if (aClass == null) {
            return false;
        }
        Class<?> y = mCachedClasses.get(aClass.getCanonicalName());
        if (y == null) {
            mCachedClasses.put(aClass.getCanonicalName(), aClass);
            return true;
        }
        return false;
    }

    private static boolean cacheMethod(Class<?> aClass, Method aMethod) {
        if (aMethod == null) {
            return false;
        }
        boolean isStatic = Modifier.isStatic(aMethod.getModifiers());
        CachedMethod y = mCachedMethods.get(
                aClass.getName() + "." + aMethod.getName() + "." + ArrayUtils.toString(aMethod.getParameterTypes()));
        if (y == null) {
            mCachedMethods.put(
                    aClass.getName() + "." + aMethod.getName() + "." + ArrayUtils.toString(aMethod.getParameterTypes()),
                    new CachedMethod(aMethod, isStatic));
            return true;
        }
        return false;
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

    private static void cacheConstructor(Class<?> aClass, Constructor<?> aConstructor) {
        if (aConstructor == null) {
            return;
        }
        mCachedConstructors.computeIfAbsent(
                aClass.getName() + "." + ArrayUtils.toString(aConstructor.getParameterTypes()),
                k -> new CachedConstructor(aConstructor));
    }

    /**
     * Returns a cached {@link Constructor} object.
     * 
     * @param aClass - Class containing the Constructor.
     * @param aTypes - Varags Class Types for objects constructor.
     * @return - Valid, non-final, {@link Method} object, or {@link null}.
     */
    public static Constructor<?> getConstructor(Class<?> aClass, Class<?>... aTypes) {
        if (aClass == null || aTypes == null) {
            return null;
        }

        String aMethodKey = ArrayUtils.toString(aTypes);
        // Logger.REFLECTION("Looking up method in cache: "+(aClass.getName()+"."+aMethodName + "." + aMethodKey));
        CachedConstructor y = mCachedConstructors.get(aClass.getName() + "." + aMethodKey);
        if (y == null) {
            Constructor<?> u = getConstructor_Internal(aClass, aTypes);
            if (u != null) {
                Logger.REFLECTION("Caching Constructor: " + aClass.getName() + "." + aMethodKey);
                cacheConstructor(aClass, u);
                return u;
            } else {
                return null;
            }
        } else {
            return y.get();
        }
    }

    /**
     * Returns a cached {@link Class} object.
     * 
     * @param aClassCanonicalName - The canonical name of the underlying class.
     * @return - Valid, {@link Class} object, or {@link null}.
     */
    public static Class<?> getClass(String aClassCanonicalName) {
        if (aClassCanonicalName == null || aClassCanonicalName.length() <= 0) {
            return null;
        }
        Class<?> y = mCachedClasses.get(aClassCanonicalName);
        if (y == null) {
            y = getClass_Internal(aClassCanonicalName);
            if (y != null) {
                Logger.REFLECTION("Caching Class: " + aClassCanonicalName);
                cacheClass(y);
            }
        }
        return y;
    }

    /**
     * Returns a cached {@link Method} object. Wraps {@link #getMethod(Class, String, Class...)}.
     * 
     * @param aObject     - Object containing the Method.
     * @param aMethodName - Method's name in {@link String} form.
     * @param aTypes      - Class Array of Types for {@link Method}'s constructor.
     * @return - Valid, non-final, {@link Method} object, or {@link null}.
     */
    public static Method getMethod(Object aObject, String aMethodName, Class[] aTypes) {
        return getMethod(aObject.getClass(), aMethodName, aTypes);
    }

    /**
     * Returns a cached {@link Method} object.
     * 
     * @param aClass      - Class containing the Method.
     * @param aMethodName - Method's name in {@link String} form.
     * @param aTypes      - Varags Class Types for {@link Method}'s constructor.
     * @return - Valid, non-final, {@link Method} object, or {@link null}.
     */
    public static Method getMethod(Class<?> aClass, String aMethodName, Class<?>... aTypes) {
        if (aClass == null || aMethodName == null || aMethodName.length() <= 0) {
            return null;
        }
        String aMethodKey = ArrayUtils.toString(aTypes);
        // Logger.REFLECTION("Looking up method in cache: "+(aClass.getName()+"."+aMethodName + "." + aMethodKey));
        CachedMethod y = mCachedMethods.get(aClass.getName() + "." + aMethodName + "." + aMethodKey);
        if (y == null) {
            Method u = getMethod_Internal(aClass, aMethodName, aTypes);
            if (u != null) {
                Logger.REFLECTION("Caching Method: " + aMethodName + "." + aMethodKey);
                cacheMethod(aClass, u);
                return u;
            } else {
                return null;
            }
        } else {
            return y.get();
        }
    }

    /**
     * Returns a cached {@link Field} object.
     * 
     * @param aClass     - Class containing the Method.
     * @param aFieldName - Field name in {@link String} form.
     * @return - Valid, non-final, {@link Field} object, or {@link null}.
     */
    public static Field getField(final Class<?> aClass, final String aFieldName) {
        if (aClass == null || aFieldName == null || aFieldName.length() <= 0) {
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

    /**
     * Returns a cached {@link Field} object.
     * 
     * @param aInstance  - {@link Object} to get the field instance from.
     * @param aFieldName - Field name in {@link String} form.
     * @return - Valid, non-final, {@link Field} object, or {@link null}.
     */
    public static <T> T getField(final Object aInstance, final String aFieldName) {
        try {
            return (T) getField(aInstance.getClass(), aFieldName).get(aInstance);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
    }

    /*
     * Utility Functions
     */

    public static boolean doesClassExist(final String classname) {
        return isClassPresent(classname);
    }

    public static void makeFieldAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    public static void makeMethodAccessible(final Method field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     * 
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return Method name
     */
    public static String getMethodName(final int depth) {
        final StackTraceElement[] ste = new Throwable().getStackTrace();
        // System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        if (ste.length < depth) {
            return "No valid stack.";
        }
        return ste[depth + 1].getMethodName();
    }

    /**
     *
     * @param aPackageName - The full {@link Package} name in {@link String} form.
     * @return - {@link Boolean} object. True if loaded > 0 classes.
     */
    public static boolean dynamicallyLoadClassesInPackage(String aPackageName) {
        ClassLoader classLoader = ReflectionUtils.class.getClassLoader();
        int loaded = 0;
        try {
            ClassPath path = ClassPath.from(classLoader);
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive(aPackageName)) {
                Class<?> clazz = Class.forName(info.getName(), true, classLoader);
                if (clazz != null) {
                    loaded++;
                    Logger.INFO("Found " + clazz.getCanonicalName() + ". [" + loaded + "]");
                }
            }
        } catch (ClassNotFoundException | IOException e) {

        }

        return loaded > 0;
    }

    public static void loadClass(String aClassName) {
        try {
            Class.forName(aClassName, true, ReflectionUtils.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean setField(final Object object, final String fieldName, final Object fieldValue) {
        Class<?> clazz;
        if (object instanceof Class) {
            clazz = (Class<?>) object;
        } else {
            clazz = object.getClass();
        }
        while (clazz != null) {
            try {
                final Field field = getField(clazz, fieldName);
                if (field != null) {
                    setFieldValue_Internal(object, field, fieldValue);
                    return true;
                }
            } catch (final NoSuchFieldException e) {
                Logger.REFLECTION("setField(" + object + ", " + fieldName + ") failed.");
                clazz = clazz.getSuperclass();
            } catch (final Exception e) {
                Logger.REFLECTION("setField(" + object + ", " + fieldName + ") failed.");
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    public static boolean setField(final Object object, final Field field, final Object fieldValue) {
        if (field == null) return false;
        Class<?> clazz;
        if (object instanceof Class) {
            clazz = (Class<?>) object;
        } else {
            clazz = object.getClass();
        }
        while (clazz != null) {
            try {
                final Field field2 = getField(clazz, field.getName());
                if (field2 != null) {
                    setFieldValue_Internal(object, field, fieldValue);
                    return true;
                }
            } catch (final NoSuchFieldException e) {
                Logger.REFLECTION("setField(" + object + ", " + field.getName() + ") failed.");
                clazz = clazz.getSuperclass();
            } catch (final Exception e) {
                Logger.REFLECTION("setField(" + object + ", " + field.getName() + ") failed.");
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    /**
     * Allows to change the state of an immutable instance. Huh?!?
     */
    public static void setFinalFieldValue(Class<?> clazz, String fieldName, Object newValue) {
        Field nameField = getField(clazz, fieldName);
        try {
            setFieldValue_Internal(clazz, nameField, newValue);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void setByte(Object clazz, String fieldName, byte newValue) {
        Field nameField = getField(clazz.getClass(), fieldName);
        cacheAccessor(nameField).setValue(null, newValue);
    }

    public static boolean invokeVoid(Object objectInstance, String methodName, Class[] parameters, Object[] values) {
        if (objectInstance == null || methodName == null || parameters == null || values == null) {
            return false;
        }
        Class<?> mLocalClass = (objectInstance instanceof Class ? (Class<?>) objectInstance
                : objectInstance.getClass());
        Logger.REFLECTION(
                "Trying to invoke " + methodName + " on an instance of " + mLocalClass.getCanonicalName() + ".");
        try {
            Method mInvokingMethod = mLocalClass.getDeclaredMethod(methodName, parameters);
            if (mInvokingMethod != null) {
                Logger.REFLECTION(methodName + " was not null.");
                mInvokingMethod.invoke(objectInstance, values);
                Logger.REFLECTION("Successfully invoked " + methodName + ".");
                return true;
            } else {
                Logger.REFLECTION(methodName + " is null.");
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            Logger.REFLECTION(
                    "Failed to Dynamically invoke " + methodName + " on an object of type: " + mLocalClass.getName());
        }

        Logger.REFLECTION("Invoke failed or did something wrong.");
        return false;
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

    /**
     * if (isPresent("com.optionaldependency.DependencyClass")) || This block will never execute when the dependency is
     * not present. There is therefore no more risk of code throwing NoClassDefFoundException.
     */
    private static boolean isClassPresent(final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (final Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }

    private static Method getMethod_Internal(Class<?> aClass, String aMethodName, Class<?>... aTypes) {
        Method m = null;
        try {
            Logger.REFLECTION("Method: Internal Lookup: " + aMethodName);
            m = aClass.getDeclaredMethod(aMethodName, aTypes);
            if (m != null) {
                m.setAccessible(true);
            }
        } catch (Throwable t) {
            Logger.REFLECTION("Method: Internal Lookup Failed: " + aMethodName);
            try {
                m = getMethodRecursively(aClass, aMethodName);
            } catch (NoSuchMethodException e) {
                Logger.REFLECTION("Unable to find method '" + aMethodName + "'");
                e.printStackTrace();
                dumpClassInfo(aClass);
            }
        }
        return m;
    }

    private static Constructor<?> getConstructor_Internal(Class<?> aClass, Class<?>... aTypes) {
        Constructor<?> c = null;
        try {
            Logger.REFLECTION("Constructor: Internal Lookup: " + aClass.getName());
            c = aClass.getDeclaredConstructor(aTypes);
            if (c != null) {
                c.setAccessible(true);
            }
        } catch (Throwable t) {
            Logger.REFLECTION("Constructor: Internal Lookup Failed: " + aClass.getName());
            try {
                c = getConstructorRecursively(aClass, aTypes);
            } catch (Exception e) {
                Logger.REFLECTION("Unable to find method '" + aClass.getName() + "'");
                e.printStackTrace();
                dumpClassInfo(aClass);
            }
        }
        return c;
    }

    private static Constructor<?> getConstructorRecursively(Class<?> aClass, Class<?>... aTypes) throws Exception {
        try {
            Logger.REFLECTION("Constructor: Recursion Lookup: " + aClass.getName());
            Constructor<?> c = aClass.getConstructor(aTypes);
            if (c != null) {
                c.setAccessible(true);
            }
            return c;
        } catch (final NoSuchMethodException | IllegalArgumentException e) {
            final Class<?> superClass = aClass.getSuperclass();
            if (superClass == null || superClass == Object.class) {
                throw e;
            }
            return getConstructor_Internal(superClass, aTypes);
        }
    }

    private static Method getMethodRecursively(final Class<?> clazz, final String aMethodName)
            throws NoSuchMethodException {
        try {
            Logger.REFLECTION("Method: Recursion Lookup: " + aMethodName);
            Method k = clazz.getDeclaredMethod(aMethodName);
            makeMethodAccessible(k);
            return k;
        } catch (final NoSuchMethodException e) {
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass == null || superClass == Object.class) {
                throw e;
            }
            return getMethod_Internal(superClass, aMethodName);
        }
    }

    private static void dumpClassInfo(Class<?> aClass) {
        Logger.INFO(
                "We ran into an error processing reflection in " + aClass.getName()
                        + ", dumping all data for debugging.");
        // Get the methods
        Method[] methods = aClass.getDeclaredMethods();
        Field[] fields = aClass.getDeclaredFields();
        Constructor[] consts = aClass.getDeclaredConstructors();

        Logger.INFO("Dumping all Methods.");
        for (Method method : methods) {
            System.out
                    .println(method.getName() + " | " + StringUtils.getDataStringFromArray(method.getParameterTypes()));
        }
        Logger.INFO("Dumping all Fields.");
        for (Field f : fields) {
            System.out.println(f.getName());
        }
        Logger.INFO("Dumping all Constructors.");
        for (Constructor<?> c : consts) {
            System.out.println(
                    c.getName() + " | "
                            + c.getParameterCount()
                            + " | "
                            + StringUtils.getDataStringFromArray(c.getParameterTypes()));
        }
    }

    private static Class<?> getNonPublicClass(final String className) {
        Class<?> c = null;
        try {
            c = Class.forName(className);
        } catch (final ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // full package name --------^^^^^^^^^^
        // or simpler without Class.forName:
        // Class<package1.A> c = package1.A.class;

        if (null != c) {
            // In our case we need to use
            Constructor<?> constructor = null;
            try {
                constructor = c.getDeclaredConstructor();
            } catch (NoSuchMethodException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // note: getConstructor() can return only public constructors
            // so we needed to search for any Declared constructor

            // now we need to make this constructor accessible
            if (null != constructor) {
                constructor.setAccessible(true); // ABRACADABRA!

                try {
                    final Object o = constructor.newInstance();
                    return (Class<?>) o;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static Class<?> getClass_Internal(String string) {
        Class<?> aClass = null;
        if (ReflectionUtils.doesClassExist(string)) {
            try {
                aClass = Class.forName(string);
            } catch (ClassNotFoundException e) {
                aClass = getNonPublicClass(string);
            }
        }

        if (aClass == null) {
            String aClassName = "";
            Logger.REFLECTION("Splitting " + string + " to try look for hidden classes.");
            String[] aData = string.split("\\.");
            Logger.REFLECTION("Obtained " + aData.length + " pieces.");
            for (int i = 0; i < (aData.length - 1); i++) {
                aClassName += (i > 0) ? "." + aData[i] : "" + aData[i];
                Logger.REFLECTION("Building: " + aClassName);
            }
            if (aClassName != null && aClassName.length() > 0) {
                Logger.REFLECTION("Trying to search '" + aClassName + "' for inner classes.");
                Class<?> clazz = ReflectionUtils.getClass(aClassName);
                if (clazz != null) {
                    Class[] y = clazz.getDeclaredClasses();
                    if (y == null || y.length <= 0) {
                        Logger.REFLECTION("No hidden inner classes found.");
                        return null;
                    } else {
                        boolean found = false;
                        for (Class<?> h : y) {
                            Logger.REFLECTION("Found hidden inner class: " + h.getCanonicalName());
                            if (h.getSimpleName().toLowerCase().equals(aData[aData.length - 1].toLowerCase())) {
                                Logger.REFLECTION(
                                        "Found correct class. [" + aData[aData.length - 1]
                                                + "] Caching at correct location: "
                                                + string);
                                Logger.REFLECTION("Found at location: " + h.getCanonicalName());
                                ReflectionUtils.mCachedClasses.put(string, h);
                                aClass = h;
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return aClass;
    }

    /**
     *
     * Set the value of a field reflectively.
     */
    private static void setFieldValue_Internal(Object owner, Field field, Object value) throws Exception {
        cacheAccessor(field).setValue(owner, value);
    }

    public static boolean doesFieldExist(Class<?> clazz, String string) {
        if (clazz != null) {
            if (ReflectionUtils.getField(clazz, string) != null) {
                return true;
            }
        }
        return false;
    }

    public static <T> T getFieldValue(Field field) {
        return getFieldValue(field, null);
    }

    public static <T> T getFieldValue(Field field, Object instance) {
        try {
            return (T) field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException e) {}
        return null;
    }

    public static <T> T createNewInstanceFromConstructor(Constructor aConstructor, Object[] aArgs) {
        T aInstance;
        try {
            aInstance = (T) aConstructor.newInstance(aArgs);
            if (aInstance != null) {
                return aInstance;
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Enum getEnum(Class<Enum> sgtbees, String name) {
        if (sgtbees.isEnum()) {
            Object[] aValues = sgtbees.getEnumConstants();
            for (Object o : aValues) {
                if (o.toString().toLowerCase().equals(name.toLowerCase())) {
                    return (Enum) o;
                }
            }
        }
        return null;
    }
}
