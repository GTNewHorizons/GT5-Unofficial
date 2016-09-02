package miscutil.core.util;

public class ClassUtils {

	
	/*@ if (isPresent("com.optionaldependency.DependencyClass")) {
    //  This block will never execute when the dependency is not present
    //  There is therefore no more risk of code throwing NoClassDefFoundException.
    executeCodeLinkingToDependency();
	}*/
	public static boolean isPresent(String className) {
	    try {
	        Class.forName(className);
	        return true;
	    } catch (Throwable ex) {
	        // Class or one of its dependencies is not present...
	        return false;
	    }
	}

	
	
}
