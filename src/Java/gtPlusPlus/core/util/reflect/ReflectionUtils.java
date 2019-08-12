package gtPlusPlus.core.util.reflect;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.reflect.ClassPath;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.data.StringUtils;

public class ReflectionUtils {

	public static Map<String, Class<?>> mCachedClasses = new LinkedHashMap<String, Class<?>>();
	public static Map<String, CachedMethod> mCachedMethods = new LinkedHashMap<String, CachedMethod>();
	public static Map<String, CachedField> mCachedFields = new LinkedHashMap<String, CachedField>();

	private static class CachedMethod {

		private final boolean STATIC;
		private final Method METHOD;

		public CachedMethod(Method aMethod, boolean isStatic) {
			METHOD = aMethod;
			STATIC = isStatic;
		}

		public Method get() {
			return METHOD;
		}

		public boolean type() {
			return STATIC;
		}

	}

	private static class CachedField {

		private final boolean STATIC;
		private final Field FIELD;

		public CachedField(Field aField, boolean isStatic) {
			FIELD = aField;
			STATIC = isStatic;
		}

		public Field get() {
			return FIELD;
		}

		public boolean type() {
			return STATIC;
		}

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
		CachedMethod y = mCachedMethods.get(aClass.getName()+"."+aMethod.getName()+"."+ArrayUtils.toString(aMethod.getParameterTypes()));
		if (y == null) {
			mCachedMethods.put(aClass.getName()+"."+aMethod.getName()+"."+ArrayUtils.toString(aMethod.getParameterTypes()), new CachedMethod(aMethod, isStatic));
			return true;
		}		
		return false;
	}

	private static boolean cacheField(Class<?> aClass, Field aField) {		
		if (aField == null) {
			return false;
		}		
		boolean isStatic = Modifier.isStatic(aField.getModifiers());
		CachedField y = mCachedFields.get(aClass.getName()+"."+aField.getName());
		if (y == null) {
			mCachedFields.put(aClass.getName()+"."+aField.getName(), new CachedField(aField, isStatic));
			return true;
		}		
		return false;
	}


	/**
	 * Returns a cached {@link Class} object.
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
				Logger.REFLECTION("Caching Class: "+aClassCanonicalName);
				cacheClass(y);
			}
		}
		return y;
	}



	/**
	 * Returns a cached {@link Method} object. Wraps {@link #getMethod(Class, String, Class...)}.
	 * @param aObject - Object containing the Method.
	 * @param aMethodName - Method's name in {@link String} form.
	 * @param aTypes - Class Array of Types for {@link Method}'s constructor.
	 * @return - Valid, non-final, {@link Method} object, or {@link null}.
	 */
	public static Method getMethod(Object aObject, String aMethodName, Class[] aTypes) {
		return getMethod(aObject.getClass(), aMethodName, aTypes);
	}


	/**
	 * Returns a cached {@link Method} object.
	 * @param aClass - Class containing the Method.
	 * @param aMethodName - Method's name in {@link String} form.
	 * @param aTypes - Varags Class Types for {@link Method}'s constructor.
	 * @return - Valid, non-final, {@link Method} object, or {@link null}.
	 */
	public static Method getMethod(Class<?> aClass, String aMethodName, Class<?>... aTypes) {
		if (aClass == null || aMethodName == null || aMethodName.length() <= 0) {
			return null;
		}		
		String aMethodKey = ArrayUtils.toString(aTypes);
		//Logger.REFLECTION("Looking up method in cache: "+(aClass.getName()+"."+aMethodName + "." + aMethodKey));
		CachedMethod y = mCachedMethods.get(aClass.getName()+"."+aMethodName + "." + aMethodKey);
		if (y == null) {
			Method u = getMethod_Internal(aClass, aMethodName, aTypes);
			if (u != null) {
				Logger.REFLECTION("Caching Method: "+aMethodName + "." + aMethodKey);
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
	 * @param aClass - Class containing the Method.
	 * @param aFieldName - Field name in {@link String} form.
	 * @return - Valid, non-final, {@link Field} object, or {@link null}.
	 */
	public static Field getField(final Class<?> aClass, final String aFieldName) {
		if (aClass == null || aFieldName == null || aFieldName.length() <= 0) {
			return null;
		}		
		CachedField y = mCachedFields.get(aClass.getName()+"."+aFieldName);
		if (y == null) {
			Field u;
			try {
				u = getField_Internal(aClass, aFieldName);
				if (u != null) {
					Logger.REFLECTION("Caching Field '"+aFieldName+"' from "+aClass.getName());
					cacheField(aClass, u);
					return u;
				}
			} catch (NoSuchFieldException e) {
			}
			return null;

		} else {
			return y.get();
		}
	}

	/**
	 * Returns a cached {@link Field} object.
	 * @param aInstance - {@link Object} to get the field instance from.
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


	/**
	 * Returns the class of the objects type parameter
	 * @param o - Object to examine paramters on
	 * @return - a Class<?> or null
	 */
	public static Class<?> getTypeOfGenericObject(Object o) {
		Class<?> aTypeParam = findSuperClassParameterType(o, o.getClass(), 0);	
		if (aTypeParam == null) {
			aTypeParam = findSubClassParameterType(o, o.getClass(), 0);
		}		
		return aTypeParam;
	}

	public static void makeFieldAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()))
		{
			field.setAccessible(true);
		}
	}

	public static void makeMethodAccessible(final Method field) {
		if (!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()))
		{
			field.setAccessible(true);
		}
	}

	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
	 * @return Method name
	 */
	public static String getMethodName(final int depth) {
		final StackTraceElement[] ste = new Throwable().getStackTrace();
		//System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
		return ste[depth+1].getMethodName();
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
					Logger.INFO("Found "+clazz.getCanonicalName()+". ["+loaded+"]");
				}
			}
		} catch (ClassNotFoundException | IOException e) {

		}

		return loaded > 0;
	}

	

	public static boolean setField(final Object object, final String fieldName, final Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				final Field field = getField(clazz, fieldName);
				if (field != null) {
					setFieldValue_Internal(object, field, fieldValue);					
					return true;
				}
			} catch (final NoSuchFieldException e) {
				Logger.REFLECTION("setField("+object.toString()+", "+fieldName+") failed.");
				clazz = clazz.getSuperclass();
			} catch (final Exception e) {
				Logger.REFLECTION("setField("+object.toString()+", "+fieldName+") failed.");
				throw new IllegalStateException(e);
			}
		}
		return false;
	
		
	}

	public static boolean setField(final Object object, final Field field, final Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				final Field field2 = getField(clazz, field.getName());
				if (field2 != null) {
					setFieldValue_Internal(object, field, fieldValue);					
					return true;
				}
			} catch (final NoSuchFieldException e) {
				Logger.REFLECTION("setField("+object.toString()+", "+field.getName()+") failed.");
				clazz = clazz.getSuperclass();
			} catch (final Exception e) {
				Logger.REFLECTION("setField("+object.toString()+", "+field.getName()+") failed.");
				throw new IllegalStateException(e);
			}
		}
		return false;
	}


	/**
	 * Allows to change the state of an immutable instance. Huh?!?
	 */
	public static void setFinalFieldValue(Class<?> clazz,  String fieldName, Object newValue) {
		Field nameField = getField(clazz, fieldName);
		try {
			setFieldValue_Internal(clazz, nameField, newValue);
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Deprecated
	public static void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}


	public static void setByte(Object clazz,  String fieldName, byte newValue) throws Exception {
		Field nameField = getField(clazz.getClass(), fieldName);
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
		//Utils.LOG_INFO("O-"+(byte) nameField.get(clazz) + " | "+newValue);
		nameField.setByte(clazz, newValue);
		//Utils.LOG_INFO("N-"+(byte) nameField.get(clazz));

		/*final Field fieldA = getField(clazz.getClass(), fieldName);
		fieldA.setAccessible(true);
		fieldA.setByte(clazz, newValue);*/

	}

	public static boolean invoke(Object objectInstance, String methodName, Class[] parameters, Object[] values){
		if (objectInstance == null || methodName == null || parameters == null || values == null){
			//Logger.REFLECTION("Null value when trying to Dynamically invoke "+methodName+" on an object of type: "+objectInstance.getClass().getName());
			return false;
		}		
		Class<?> mLocalClass = (objectInstance instanceof Class ? (Class<?>) objectInstance : objectInstance.getClass());
		Logger.REFLECTION("Trying to invoke "+methodName+" on an instance of "+mLocalClass.getCanonicalName()+".");
		try {
			Method mInvokingMethod = mLocalClass.getDeclaredMethod(methodName, parameters);
			if (mInvokingMethod != null){
				Logger.REFLECTION(methodName+" was not null.");
				if ((boolean) mInvokingMethod.invoke(objectInstance, values)){
					Logger.REFLECTION("Successfully invoked "+methodName+".");
					return true;
				}
				else {
					Logger.REFLECTION("Invocation failed for "+methodName+".");
				}
			}
			else {
				Logger.REFLECTION(methodName+" is null.");				
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.REFLECTION("Failed to Dynamically invoke "+methodName+" on an object of type: "+mLocalClass.getName());
		}		

		Logger.REFLECTION("Invoke failed or did something wrong.");		
		return false;
	}

	public static boolean invokeVoid(Object objectInstance, String methodName, Class[] parameters, Object[] values){
		if (objectInstance == null || methodName == null || parameters == null || values == null){
			return false;
		}		
		Class<?> mLocalClass = (objectInstance instanceof Class ? (Class<?>) objectInstance : objectInstance.getClass());
		Logger.REFLECTION("Trying to invoke "+methodName+" on an instance of "+mLocalClass.getCanonicalName()+".");
		try {
			Method mInvokingMethod = mLocalClass.getDeclaredMethod(methodName, parameters);
			if (mInvokingMethod != null){
				Logger.REFLECTION(methodName+" was not null.");
				mInvokingMethod.invoke(objectInstance, values);
				Logger.REFLECTION("Successfully invoked "+methodName+".");
				return true;				
			}
			else {
				Logger.REFLECTION(methodName+" is null.");				
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.REFLECTION("Failed to Dynamically invoke "+methodName+" on an object of type: "+mLocalClass.getName());
		}		

		Logger.REFLECTION("Invoke failed or did something wrong.");		
		return false;
	}

	public static Object invokeNonBool(Object objectInstance, String methodName, Class[] parameters, Object[] values){
		if (objectInstance == null || methodName == null || parameters == null || values == null){
			return false;
		}		
		Class<?> mLocalClass = (objectInstance instanceof Class ? (Class<?>) objectInstance : objectInstance.getClass());
		Logger.REFLECTION("Trying to invoke "+methodName+" on an instance of "+mLocalClass.getCanonicalName()+".");
		try {
			Method mInvokingMethod = mLocalClass.getDeclaredMethod(methodName, parameters);
			if (mInvokingMethod != null){
				Logger.REFLECTION(methodName+" was not null.");
				return mInvokingMethod.invoke(objectInstance, values);	
			}
			else {
				Logger.REFLECTION(methodName+" is null.");				
			}
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.REFLECTION("Failed to Dynamically invoke "+methodName+" on an object of type: "+mLocalClass.getName());
		}		

		Logger.REFLECTION("Invoke failed or did something wrong.");		
		return null;
	}





















	/*
	 * Internal Magic that probably should not get exposed.
	 */	

	
	
	
	
	



	/*
	 * 
	 * Below Code block is used for determining generic types associated with type<E>
	 * 
	 */


	//https://xebia.com/blog/acessing-generic-types-at-runtime-in-java/
	//https://www.javacodegeeks.com/2013/12/advanced-java-generics-retreiving-generic-type-arguments.html
	public static Class<?> findSuperClassParameterType(Object instance, Class<?> classOfInterest, int parameterIndex) {
		Class<?> subClass = instance.getClass();
		while (classOfInterest != subClass.getSuperclass()) {
			// instance.getClass() is no subclass of classOfInterest or instance is a direct instance of classOfInterest
			subClass = subClass.getSuperclass();
			if (subClass == null) {
				return null;
			}
		}
		ParameterizedType parameterizedType = (ParameterizedType) subClass.getGenericSuperclass();
		Class<?> aReturn;
		aReturn = (Class<?>) parameterizedType.getActualTypeArguments()[parameterIndex];
		return aReturn;
	}

	public static Class<?> findSubClassParameterType(Object instance, Class<?> classOfInterest, int parameterIndex) {
		Map<Type, Type> typeMap = new HashMap<Type, Type>();
		Class<?> instanceClass = instance.getClass();
		while (classOfInterest != instanceClass.getSuperclass()) {
			extractTypeArguments(typeMap, instanceClass);
			instanceClass = instanceClass.getSuperclass();
			if (instanceClass == null) {
				return null;
			}
		}

		ParameterizedType parameterizedType = (ParameterizedType) instanceClass.getGenericSuperclass();
		Type actualType = parameterizedType.getActualTypeArguments()[parameterIndex];
		if (typeMap.containsKey(actualType)) {
			actualType = typeMap.get(actualType);
		}
		if (actualType instanceof Class) {
			return (Class<?>) actualType;
		} else if (actualType instanceof TypeVariable) {
			return browseNestedTypes(instance, (TypeVariable<?>) actualType);
		} else {
			return null;
		}
	}

	private static void extractTypeArguments(Map<Type, Type> typeMap, Class<?> clazz) {
		Type genericSuperclass = clazz.getGenericSuperclass();
		if (!(genericSuperclass instanceof ParameterizedType)) {
			return;
		}

		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Type[] typeParameter = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
		Type[] actualTypeArgument = parameterizedType.getActualTypeArguments();
		for (int i = 0; i < typeParameter.length; i++) {
			if(typeMap.containsKey(actualTypeArgument[i])) {
				actualTypeArgument[i] = typeMap.get(actualTypeArgument[i]);
			}
			typeMap.put(typeParameter[i], actualTypeArgument[i]);
		}
	}

	private static Class<?> browseNestedTypes(Object instance, TypeVariable<?> actualType) {
		Class<?> instanceClass = instance.getClass();
		List<Class<?>> nestedOuterTypes = new LinkedList<Class<?>>();
		for (Class<?> enclosingClass = instanceClass
				.getEnclosingClass(); enclosingClass != null; enclosingClass = enclosingClass.getEnclosingClass()) {
			try {
				Field this$0 = instanceClass.getDeclaredField("this$0");
				Object outerInstance = this$0.get(instance);
				Class<?> outerClass = outerInstance.getClass();
				nestedOuterTypes.add(outerClass);
				Map<Type, Type> outerTypeMap = new HashMap<Type, Type>();
				extractTypeArguments(outerTypeMap, outerClass);
				for (Map.Entry<Type, Type> entry : outerTypeMap.entrySet()) {
					if (!(entry.getKey() instanceof TypeVariable)) {
						continue;
					}
					TypeVariable<?> foundType = (TypeVariable<?>) entry.getKey();
					if (foundType.getName().equals(actualType.getName())
							&& isInnerClass(foundType.getGenericDeclaration(), actualType.getGenericDeclaration())) {
						if (entry.getValue() instanceof Class) {
							return (Class<?>) entry.getValue();
						}
						actualType = (TypeVariable<?>) entry.getValue();
					}
				}
			} catch (NoSuchFieldException | IllegalAccessException e) {

			}

		}
		return null;
	}

	private static boolean isInnerClass(GenericDeclaration outerDeclaration, GenericDeclaration innerDeclaration) {
		if (!(outerDeclaration instanceof Class) || !(innerDeclaration instanceof Class)) {
			return false;
		}
		Class<?> outerClass = (Class<?>) outerDeclaration;
		Class<?> innerClass = (Class<?>) innerDeclaration;
		while ((innerClass = innerClass.getEnclosingClass()) != null) {
			if (innerClass == outerClass) {
				return true;
			}
		}
		return false;
	}


	/*
	 * 
	 * End of Generics Block
	 * 
	 */



	private static Field getField_Internal(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
		try {
			Logger.REFLECTION("Field: Internal Lookup: "+fieldName);
			Field k = clazz.getDeclaredField(fieldName);
			makeFieldAccessible(k);
			//Logger.REFLECTION("Got Field from Class. "+fieldName+" did exist within "+clazz.getCanonicalName()+".");
			return k;
		} catch (final NoSuchFieldException e) {
			Logger.REFLECTION("Field: Internal Lookup Failed: "+fieldName);
			final Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				Logger.REFLECTION("Unable to find field '"+fieldName+"'");	
				//Logger.REFLECTION("Failed to get Field from Class. "+fieldName+" does not existing within "+clazz.getCanonicalName()+".");
				throw e;
			}
			Logger.REFLECTION("Method: Recursion Lookup: "+fieldName+" - Checking in "+superClass.getName());
			//Logger.REFLECTION("Failed to get Field from Class. "+fieldName+" does not existing within "+clazz.getCanonicalName()+". Trying super class.");
			return getField_Internal(superClass, fieldName);
		}
	}	

	/**
	 * if (isPresent("com.optionaldependency.DependencyClass")) || 
	 * This block will never execute when the dependency is not present. There is
	 * therefore no more risk of code throwing NoClassDefFoundException.
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

	@Deprecated
	public static Method getMethodViaReflection(final Class<?> lookupClass, final String methodName,
			final boolean invoke) throws Exception {
		final Class<? extends Class> lookup = lookupClass.getClass();
		final Method m = lookup.getDeclaredMethod(methodName);
		m.setAccessible(true);// Abracadabra
		if (invoke) {
			m.invoke(lookup);// now its OK
		}
		return m;
	}

	private static Method getMethod_Internal(Class<?> aClass, String aMethodName, Class<?>... aTypes) {
		Method m = null;
		try {
			Logger.REFLECTION("Method: Internal Lookup: "+aMethodName);
			m = aClass.getDeclaredMethod(aMethodName, aTypes);	
			if (m != null) {
				m.setAccessible(true);
				int modifiers = m.getModifiers();
				Field modifierField = m.getClass().getDeclaredField("modifiers");
				modifiers = modifiers & ~Modifier.FINAL;
				modifierField.setAccessible(true);
				modifierField.setInt(m, modifiers);
			}
		}
		catch (Throwable t) {
			Logger.REFLECTION("Method: Internal Lookup Failed: "+aMethodName);
			try {
				m = getMethodRecursively(aClass, aMethodName);
			} catch (NoSuchMethodException e) {
				Logger.REFLECTION("Unable to find method '"+aMethodName+"'");	
				e.printStackTrace();
				dumpClassInfo(aClass);
			}
		}
		return m;
	}

	private static Method getMethodRecursively(final Class<?> clazz, final String aMethodName) throws NoSuchMethodException {
		try {
			Logger.REFLECTION("Method: Recursion Lookup: "+aMethodName);
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
		Logger.INFO("We ran into an error processing reflection in "+aClass.getName()+", dumping all data for debugging.");	
		// Get the methods
		Method[] methods = aClass.getDeclaredMethods();
		Field[] fields = aClass.getDeclaredFields();
		Constructor[] consts = aClass.getDeclaredConstructors(); 

		Logger.INFO("Dumping all Methods.");	
		for (Method method : methods) {
			System.out.println(method.getName()+" | "+StringUtils.getDataStringFromArray(method.getParameterTypes()));
		}
		Logger.INFO("Dumping all Fields.");	
		for (Field f : fields) {
			System.out.println(f.getName());
		}
		Logger.INFO("Dumping all Constructors.");	
		for (Constructor<?> c : consts) {
			System.out.println(c.getName()+" | "+c.getParameterCount()+" | "+StringUtils.getDataStringFromArray(c.getParameterTypes()));
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
				constructor.setAccessible(true);// ABRACADABRA!

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
		if (ReflectionUtils.doesClassExist(string)) {
			try {
				return Class.forName(string);
			}
			catch (ClassNotFoundException e) {
				return getNonPublicClass(string);
			}
		}
		return null;
	}

	/**
	 * 
	 * Set the value of a field reflectively.
	 */
	private static void setFieldValue_Internal(Object owner, Field field, Object value) throws Exception {
		makeModifiable(field);
		field.set(owner, value);
	}

	/**
	 * Force the field to be modifiable and accessible.
	 */
	private static void makeModifiable(Field nameField) throws Exception {
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}


	public static boolean doesFieldExist(String clazz, String string) {
		return doesFieldExist(ReflectionUtils.getClass(clazz), string);
	}
	
	public static boolean doesFieldExist(Class<?> clazz, String string) {
		if (clazz != null) {
			if (ReflectionUtils.getField(clazz, string) != null) {
				return true;
			}
		}
		return false;
	}

	public static Object getFieldValue(Field field) {
		return getFieldValue(field, null);
	}

	public static Object getFieldValue(Field field, Object instance) {
		try {
			return field.get(instance);
		} catch (IllegalArgumentException | IllegalAccessException e) {
		}
		return null;
	}


}
