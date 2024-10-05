package gregtech.api.util;

public class ReflectionUtil {

    public static Class<?> getClass(String classname) {
        try {
            return Class.forName(classname);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
