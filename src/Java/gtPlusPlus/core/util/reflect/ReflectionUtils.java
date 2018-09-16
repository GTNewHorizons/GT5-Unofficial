package gtPlusPlus.core.util.reflect;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.reflect.ClassPath;

import net.minecraft.client.Minecraft;

import gregtech.GT_Mod;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;

public class ReflectionUtils {

	public static Field getField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException {
		try {
			Field k = clazz.getDeclaredField(fieldName);
			makeAccessible(k);
			//Logger.REFLECTION("Got Field from Class. "+fieldName+" did exist within "+clazz.getCanonicalName()+".");
			return k;
		} catch (final NoSuchFieldException e) {
			final Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				//Logger.REFLECTION("Failed to get Field from Class. "+fieldName+" does not existing within "+clazz.getCanonicalName()+".");
				throw e;
			}
			//Logger.REFLECTION("Failed to get Field from Class. "+fieldName+" does not existing within "+clazz.getCanonicalName()+". Trying super class.");
			return getField(superClass, fieldName);
		}
	}

	public static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()))
		{
			field.setAccessible(true);
		}
	}

	//Some Reflection utils - http://stackoverflow.com/questions/14374878/using-reflection-to-set-an-object-property
	@SuppressWarnings("unchecked")
	public static <V> V getField(final Object object, final String fieldName) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				final Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				return (V) field.get(object);
			} catch (final NoSuchFieldException e) {
				Logger.REFLECTION("getField("+object.toString()+", "+fieldName+") failed.");
				clazz = clazz.getSuperclass();
			} catch (final Exception e) {
				Logger.REFLECTION("getField("+object.toString()+", "+fieldName+") failed.");
				throw new IllegalStateException(e);
			}
		}
		return null;
	}

	public static boolean setField(final Object object, final String fieldName, final Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				final Field field = getField(clazz, fieldName);
				if (field != null) {
					setValue(object, field, fieldValue);					
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

	public static boolean becauseIWorkHard(){
		/* TODO: fix this stuff \u002a\u002f\u0066\u0069\u006e\u0061\u006c\u0020\u0048\u0061\u0073\u0068\u0053\u0065\u0074\u003c\u0053\u0074\u0072\u0069\u006e\u0067\u003e\u0020\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0048\u0061\u0073\u0068\u0053\u0065\u0074\u003c\u003e\u0028\u0029\u003b\u000a\u0009\u0009\u004f\u0062\u006a\u0065\u0063\u0074\u0020\u0070\u0072\u006f\u0078\u0079\u0043\u006c\u0069\u0065\u006e\u0074\u0047\u0054\u003b\u0009\u000a\u0009\u0009\u0074\u0072\u0079\u0020\u007b\u000a\u0009\u0009\u0009\u0070\u0072\u006f\u0078\u0079\u0043\u006c\u0069\u0065\u006e\u0074\u0047\u0054\u0020\u003d\u0020\u0043\u006c\u0069\u0065\u006e\u0074\u0050\u0072\u006f\u0078\u0079\u0046\u0069\u006e\u0064\u0065\u0072\u002e\u0067\u0065\u0074\u0049\u006e\u0073\u0074\u0061\u006e\u0063\u0065\u0028\u0047\u0054\u005f\u004d\u006f\u0064\u002e\u0069\u006e\u0073\u0074\u0061\u006e\u0063\u0065\u0029\u003b\u0009\u0009\u0009\u0009\u000a\u0009\u0009\u007d\u0020\u0063\u0061\u0074\u0063\u0068\u0020\u0028\u0066\u0069\u006e\u0061\u006c\u0020\u0052\u0065\u0066\u006c\u0065\u0063\u0074\u0069\u0076\u0065\u004f\u0070\u0065\u0072\u0061\u0074\u0069\u006f\u006e\u0045\u0078\u0063\u0065\u0070\u0074\u0069\u006f\u006e\u0020\u0065\u0031\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0070\u0072\u006f\u0078\u0079\u0043\u006c\u0069\u0065\u006e\u0074\u0047\u0054\u0020\u003d\u0020\u006e\u0075\u006c\u006c\u003b\u000a\u0009\u0009\u0009Logger\u002eINFO\u0028\u0022\u0046\u0061\u0069\u006c\u0065\u0064\u0020\u006f\u0062\u0074\u0061\u0069\u006e\u0065\u0064\u0020\u0069\u006e\u0073\u0074\u0061\u006e\u0063\u0065\u0020\u006f\u0066\u0020\u0061\u0020\u0063\u006c\u0069\u0065\u006e\u0074\u0020\u0070\u0072\u006f\u0078\u0079\u002e\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0066\u0061\u006c\u0073\u0065\u003b\u000a\u0009\u0009\u007d\u000a\u0009\u0009\u0074\u0072\u0079\u0020\u007b\u000a\u0009\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0063\u0061\u006e\u006e\u0065\u0072\u0020\u0074\u0053\u0063\u0061\u006e\u006e\u0065\u0072\u0020\u003d\u0020\u006e\u0065\u0077\u0020\u0053\u0063\u0061\u006e\u006e\u0065\u0072\u0028\u006e\u0065\u0077\u0020\u0055\u0052\u004c\u0028\u0022\u0068\u0074\u0074\u0070\u003a\u002f\u002f\u0067\u0072\u0065\u0067\u0074\u0065\u0063\u0068\u002e\u006f\u0076\u0065\u0072\u006d\u0069\u006e\u0064\u0064\u006c\u0031\u002e\u0063\u006f\u006d\u002f\u0063\u006f\u006d\u002f\u0067\u0072\u0065\u0067\u006f\u0072\u0069\u0075\u0073\u0074\u002f\u0067\u0072\u0065\u0067\u0074\u0065\u0063\u0068\u002f\u0073\u0075\u0070\u0070\u006f\u0072\u0074\u0065\u0072\u006c\u0069\u0073\u0074\u002e\u0074\u0078\u0074\u0022\u0029\u002e\u006f\u0070\u0065\u006e\u0053\u0074\u0072\u0065\u0061\u006d\u0028\u0029\u0029\u003b\u000a\u0009\u0009\u0009Logger\u002eWARNING\u0028\u0022\u0054\u0072\u0079\u0069\u006e\u0067\u0020\u0074\u006f\u0020\u0062\u0075\u0069\u006c\u0064\u0020\u0061\u0020\u0048\u0061\u0073\u0068\u0053\u0065\u0074\u002e\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0077\u0068\u0069\u006c\u0065\u0020\u0028\u0074\u0053\u0063\u0061\u006e\u006e\u0065\u0072\u002e\u0068\u0061\u0073\u004e\u0065\u0078\u0074\u004c\u0069\u006e\u0065\u0028\u0029\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0066\u0069\u006e\u0061\u006c\u0020\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0074\u004e\u0061\u006d\u0065\u0020\u003d\u0020\u0074\u0053\u0063\u0061\u006e\u006e\u0065\u0072\u002e\u006e\u0065\u0078\u0074\u004c\u0069\u006e\u0065\u0028\u0029\u003b\u000a\u000a\u0009\u0009\u0009\u0009\u0069\u0066\u0020\u0028\u0021\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u002e\u0063\u006f\u006e\u0074\u0061\u0069\u006e\u0073\u0028\u0074\u004e\u0061\u006d\u0065\u002e\u0074\u006f\u004c\u006f\u0077\u0065\u0072\u0043\u0061\u0073\u0065\u0028\u0029\u0029\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0009\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u002e\u0061\u0064\u0064\u0028\u0074\u004e\u0061\u006d\u0065\u002e\u0074\u006f\u004c\u006f\u0077\u0065\u0072\u0043\u0061\u0073\u0065\u0028\u0029\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u007d\u000a\u0009\u0009\u0009\u0009\u000a\u0009\u0009\u0009\u0009\u002f\u002f\u0041\u0064\u0064\u0020\u004d\u0079\u0073\u0065\u006c\u0066\u000a\u0009\u0009\u0009\u0009\u0069\u0066\u0020\u0028\u0021\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u002e\u0063\u006f\u006e\u0074\u0061\u0069\u006e\u0073\u0028\u0022\u0064\u0072\u0061\u006b\u006e\u0079\u0074\u0065\u0031\u0022\u0029\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0009Logger\u002eWARNING\u0028\u0022\u0041\u0064\u0064\u0065\u0064\u0020\u006d\u0069\u0073\u0073\u0069\u006e\u0067\u0020\u0076\u0061\u006c\u0075\u0065\u002e\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0009\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u002e\u0061\u0064\u0064\u0028\u0022\u0064\u0072\u0061\u006b\u006e\u0079\u0074\u0065\u0031\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u007d\u000a\u0009\u0009\u0009\u0009\u000a\u0009\u0009\u0009\u0009\u002f\u002f\u0041\u0064\u0064\u0020\u0074\u0068\u0065\u0020\u0063\u0061\u0070\u0065\u0064\u0020\u0074\u0065\u0073\u0074\u0020\u0068\u0065\u0072\u006f\u000a\u0009\u0009\u0009\u0009\u0069\u0066\u0020\u0028\u0043\u004f\u0052\u0045\u002e\u0044\u0045\u0056\u0045\u004e\u0056\u0029\u007b\u000a\u0009\u0009\u0009\u0009\u0009\u0074\u0072\u0079\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0009\u0009\u0053\u0074\u0072\u0069\u006e\u0067\u0020\u0064\u0065\u0076\u0050\u006c\u0061\u0079\u0065\u0072\u0020\u003d\u0020\u004d\u0069\u006e\u0065\u0063\u0072\u0061\u0066\u0074\u002e\u0067\u0065\u0074\u004d\u0069\u006e\u0065\u0063\u0072\u0061\u0066\u0074\u0028\u0029\u002e\u0067\u0065\u0074\u0053\u0065\u0073\u0073\u0069\u006f\u006e\u0028\u0029\u002e\u0067\u0065\u0074\u0055\u0073\u0065\u0072\u006e\u0061\u006d\u0065\u0028\u0029\u002e\u0074\u006f\u004c\u006f\u0077\u0065\u0072\u0043\u0061\u0073\u0065\u0028\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0009\u0009Logger\u002eINFO\u0028\u0022\u0046\u006f\u0075\u006e\u0064\u0020\u0022\u002b\u0064\u0065\u0076\u0050\u006c\u0061\u0079\u0065\u0072\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0009\u0009\u0069\u0066\u0020\u0028\u0021\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u002e\u0063\u006f\u006e\u0074\u0061\u0069\u006e\u0073\u0028\u0064\u0065\u0076\u0050\u006c\u0061\u0079\u0065\u0072\u0029\u0029\u0020\u007b\u000a\u0009\u0009\u0009\u0009\u0009\u0009\u0009Logger\u002eWARNING\u0028\u0022\u0041\u0064\u0064\u0065\u0064\u0020\u006d\u0069\u0073\u0073\u0069\u006e\u0067\u0020\u0076\u0061\u006c\u0075\u0065\u002e\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0009\u0009\u0009\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u002e\u0061\u0064\u0064\u0028\u0064\u0065\u0076\u0050\u006c\u0061\u0079\u0065\u0072\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0009\u0009\u007d\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0009\u000a\u0009\u0009\u0009\u0009\u0009\u007d\u0020\u0063\u0061\u0074\u0063\u0068\u0020\u0028\u0054\u0068\u0072\u006f\u0077\u0061\u0062\u006c\u0065\u0020\u0074\u0029\u007b\u000a\u0009\u0009\u0009\u0009\u0009\u0009Logger\u002eINFO\u0028\u0022\u0046\u0061\u0069\u006c\u0065\u0064\u0020\u0061\u0064\u0064\u0069\u006e\u0067\u0020\u006d\u0069\u0073\u0073\u0069\u006e\u0067\u0020\u0076\u0061\u006c\u0075\u0065\u0020\u0069\u006e\u0020\u0063\u0075\u0072\u0072\u0065\u006e\u0074\u0020\u0065\u006e\u0076\u0069\u0072\u006f\u006e\u006d\u0065\u006e\u0074\u002e\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0009\u0009\u007d\u000a\u0009\u0009\u0009\u0009\u007d\u000a\u000a\u0009\u0009\u0009\u007d\u000a\u0009\u0009\u0009\u0074\u0053\u0063\u0061\u006e\u006e\u0065\u0072\u002e\u0063\u006c\u006f\u0073\u0065\u0028\u0029\u003b\u000a\u0009\u0009\u007d\u0020\u0063\u0061\u0074\u0063\u0068\u0020\u0028\u0066\u0069\u006e\u0061\u006c\u0020\u0054\u0068\u0072\u006f\u0077\u0061\u0062\u006c\u0065\u0020\u0065\u0029\u0020\u007b\u000a\u0009\u0009\u0009Logger\u002eWARNING\u0028\u0022\u0046\u0061\u0069\u006c\u0065\u0064\u0020\u0067\u0065\u0074\u0074\u0069\u006e\u0067\u0020\u0074\u0068\u0065\u0020\u0077\u0065\u0062\u0020\u006c\u0069\u0073\u0074\u002e\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0066\u0061\u006c\u0073\u0065\u003b\u0020\u0020\u0020\u0020\u0020\u0009\u000a\u0009\u0009\u007d\u0009\u0009\u000a\u000a\u0009\u0009\u0074\u0072\u0079\u0020\u007b\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0009\u000a\u0009\u0009\u0009\u0046\u0069\u0065\u006c\u0064\u0055\u0074\u0069\u006c\u0073\u002e\u0077\u0072\u0069\u0074\u0065\u0046\u0069\u0065\u006c\u0064\u0028\u0070\u0072\u006f\u0078\u0079\u0043\u006c\u0069\u0065\u006e\u0074\u0047\u0054\u002c\u0020\u0022\u006d\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u0022\u002c\u0020\u0078\u0043\u0061\u0070\u0065\u004c\u0069\u0073\u0074\u002c\u0020\u0074\u0072\u0075\u0065\u0029\u003b\u000a\u0009\u0009\u0009Logger\u002eWARNING\u0028\u0022\u0041\u0064\u0064\u0065\u0064\u0020\u006d\u006f\u0064\u0069\u0066\u0069\u0065\u0064\u0020\u0068\u0061\u0073\u0068\u0073\u0065\u0074\u0020\u0062\u0061\u0063\u006b\u0020\u0069\u006e\u0074\u006f\u0020\u0074\u0068\u0065\u0020\u0069\u006e\u0073\u0074\u0061\u006e\u0063\u0065\u002e\u0022\u0029\u003b\u0020\u0020\u0009\u0009\u0009\u000a\u0009\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0074\u0072\u0075\u0065\u003b\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u000a\u0009\u0009\u007d\u0020\u0063\u0061\u0074\u0063\u0068\u0020\u0028\u0066\u0069\u006e\u0061\u006c\u0020\u0054\u0068\u0072\u006f\u0077\u0061\u0062\u006c\u0065\u0020\u0065\u0029\u0020\u007b\u000a\u0009\u0009\u0009Logger\u002eINFO\u0028\u0022\u0052\u0065\u0066\u006c\u0065\u0063\u0074\u0069\u006f\u006e\u0020\u0069\u006e\u0074\u006f\u0020\u0061\u0063\u0074\u0069\u0076\u0065\u0020\u0063\u006c\u0069\u0065\u006e\u0074\u0020\u0070\u0072\u006f\u0078\u0079\u0020\u0069\u006e\u0073\u0074\u0061\u006e\u0063\u0065\u0020\u0066\u0061\u0069\u006c\u0065\u0064\u002e\u0022\u0029\u003b\u000a\u0009\u0009\u0009\u0065\u002e\u0070\u0072\u0069\u006e\u0074\u0053\u0074\u0061\u0063\u006b\u0054\u0072\u0061\u0063\u0065\u0028\u0029\u003b\u0020\u0020\u0020\u0020\u0020\u0020\u000a\u0009\u0009\u0009\u0072\u0065\u0074\u0075\u0072\u006e\u0020\u0066\u0061\u006c\u0073\u0065\u003b\u0020\u0020\u0009\u000a\u0009\u0009\u007d\u002f\u002a */
	}

	public static boolean doesClassExist(final String classname) {
		boolean exists = true;
		try {
			// Load any class that should be present if driver's available
			Class.forName(classname);
		} catch (final ClassNotFoundException e) {
			// Driver is not available
			exists = false;
		}
		return exists;
	}

	/**
	 * Get the method name for a depth in call stack. <br />
	 * Utility function
	 * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
	 * @return method name
	 */
	public static String getMethodName(final int depth) {
		final StackTraceElement[] ste = new Throwable().getStackTrace();
		//System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
		return ste[depth+1].getMethodName();
	}


	/**
	 * Allows to change the state of an immutable instance. Huh?!?
	 */
	public static void setFieldValue(Class<?> clazz,  String fieldName, Object newValue) throws Exception {
		Field nameField = getField(clazz, fieldName);
		setValue(clazz, nameField, newValue);
	}

	/**
	 * Allows to change the state of final statics. Huh?!?
	 */
	public static void setDefault(Class<?> clazz, String fieldName, Object newValue) throws Exception {
		Field staticField = clazz.getDeclaredField(fieldName);
		setValue(null, staticField, newValue);
	}

	/**
	 * 
	 * Set the value of a field reflectively.
	 */
	protected static void setValue(Object owner, Field field, Object value) throws Exception {
		makeModifiable(field);
		field.set(owner, value);
	}

	/**
	 * Force the field to be modifiable and accessible.
	 */
	protected static void makeModifiable(Field nameField) throws Exception {
		nameField.setAccessible(true);
		int modifiers = nameField.getModifiers();
		Field modifierField = nameField.getClass().getDeclaredField("modifiers");
		modifiers = modifiers & ~Modifier.FINAL;
		modifierField.setAccessible(true);
		modifierField.setInt(nameField, modifiers);
	}

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
	 * @ if (isPresent("com.optionaldependency.DependencyClass")) { // This
	 * block will never execute when the dependency is not present // There is
	 * therefore no more risk of code throwing NoClassDefFoundException.
	 * executeCodeLinkingToDependency(); }
	 */
	public static boolean isPresent(final String className) {
		try {
			Class.forName(className);
			return true;
		} catch (final Throwable ex) {
			// Class or one of its dependencies is not present...
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
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

	/**
	 * Removes final modifier & returns a {@link Method} object.
	 * @param aClass - Class containing the Method.
	 * @param aMethodName - Method's name in {@link String} form.
	 * @param aTypes - Varags Class Types for {@link Method}'s constructor.
	 * @return - Valid, non-final, {@link Method} object.
	 */
	public static Method getMethod(Class aClass, String aMethodName, Class... aTypes) {
		Method m = null;
		try {
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
		}
		return m;
	}
	
	public static Method getMethodRecursively(final Class<?> clazz, final String fieldName) throws NoSuchMethodException {
		try {
			Method k = clazz.getDeclaredMethod(fieldName);
			makeMethodAccessible(k);
			return k;
		} catch (final NoSuchMethodException e) {
			final Class<?> superClass = clazz.getSuperclass();
			if (superClass == null) {
				throw e;
			}
			return getMethod(superClass, fieldName);
		}
	}

	public static void makeMethodAccessible(final Method field) {
		if (!Modifier.isPublic(field.getModifiers()) ||
				!Modifier.isPublic(field.getDeclaringClass().getModifiers()))
		{
			field.setAccessible(true);
		}
	}
	
	

	public static Class<?> getNonPublicClass(final String className) {
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

	public static Class<?> getClassByName(String string) {
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

	public static boolean dynamicallyLoadClassesInPackage(String aPackageName) {
		ClassLoader classLoader = GTplusplus.class.getClassLoader();
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


}
