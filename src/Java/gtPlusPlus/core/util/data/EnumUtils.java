package gtPlusPlus.core.util.data;

import com.google.common.base.Enums;
import com.google.common.base.Optional;

import gregtech.api.enums.Materials;

public class EnumUtils {

	/** 
	 * Returns the value of an Enum if it exists.
	 * If value is not found, case-insensitive search will occur.
	 * If value still not found, an IllegalArgumentException is thrown.   
	 **/
	public static <T extends Enum<T>> T getValue(Class<T> enumeration, String name) {
		Optional<T> j = Enums.getIfPresent(enumeration, name);
		T VALUE = j.get();
		if (j.get() == null) {
			VALUE = valueOfIgnoreCase(enumeration, name);
		}
		return VALUE;
	}
	
	/** 
	 * Finds the value of the given enumeration by name, case-insensitive. 
	 * Throws an IllegalArgumentException if no match is found.  
	 **/
	private static <T extends Enum<T>> T valueOfIgnoreCase(Class<T> enumeration, String name) {

	    for (T enumValue : enumeration.getEnumConstants()) {
	        if (enumValue.name().equalsIgnoreCase(name)) {
	            return enumValue;
	        }
	    }

	    throw new IllegalArgumentException(String.format(
	        "There is no value with name '%s' in Enum %s",
	        name, enumeration.getName()
	    ));
	}

	public static Object getValue(Class class1, String materialName, boolean bool) {
		if (Enum.class.isInstance(class1)){
			return getValue(class1, materialName);
		}
		return null;		
	}
	
	
}
