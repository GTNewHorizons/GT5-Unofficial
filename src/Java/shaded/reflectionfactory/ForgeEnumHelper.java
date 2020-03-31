package shaded.reflectionfactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.Preloader_Logger;
import net.minecraftforge.common.util.EnumHelper;

public class ForgeEnumHelper {
    private static Object reflectionFactory      = null;
    private static Method newConstructorAccessor = null;
    private static Method newInstance            = null;
    private static Method newFieldAccessor       = null;
    private static Method fieldAccessorSet       = null;
    private static boolean isSetup               = false;

    
    public static void setup()
    {
        if (isSetup)
        {
            return;
        }

        try
        {

        	Preloader_Logger.INFO("Patching Fields in Forge's EnumHelper.");
        	
        	Class aRefFac = ReflectionUtils.getClass("shaded.reflectionfactory.ReflectionFactory");
        	Class aConAcc = ReflectionUtils.getClass("shaded.reflectionfactory.ConstructorAccessor");
        	Class aFieAcc = ReflectionUtils.getClass("shaded.reflectionfactory.FieldAccessor");

        	Method getReflectionFactory = ReflectionUtils.getMethod(aRefFac, "getReflectionFactory", new Class[] {});
        	reflectionFactory = ReflectionUtils.invokeNonBool(aRefFac, getReflectionFactory, new Object[] {});        	
        	newConstructorAccessor = ReflectionUtils.getMethod(aRefFac, "newConstructorAccessor", new Class[] {Constructor.class});
        	newFieldAccessor = ReflectionUtils.getMethod(aRefFac, "newFieldAccessor", new Class[] {Field.class, boolean.class});
        	newInstance = ReflectionUtils.getMethod(aConAcc, "newInstance", new Class[] {Object[].class});
        	fieldAccessorSet = ReflectionUtils.getMethod(aFieAcc, "set", new Class[] {Object.class, Object.class});

        	Field aIsSetup = ReflectionUtils.getField(EnumHelper.class, "isSetup");
        	Field aReflectionFactory = ReflectionUtils.getField(EnumHelper.class, "reflectionFactory");
        	Field aNewConstructorAccessor = ReflectionUtils.getField(EnumHelper.class, "newConstructorAccessor");
        	Field aNewInstance = ReflectionUtils.getField(EnumHelper.class, "newInstance");
        	Field aNewFieldAccessor = ReflectionUtils.getField(EnumHelper.class, "newFieldAccessor");
        	Field aFieldAccessorSet = ReflectionUtils.getField(EnumHelper.class, "fieldAccessorSet");

        	ReflectionUtils.setField(EnumHelper.class, aIsSetup, true);
        	ReflectionUtils.setField(EnumHelper.class, aReflectionFactory, reflectionFactory);
        	ReflectionUtils.setField(EnumHelper.class, aNewConstructorAccessor, newConstructorAccessor);
        	ReflectionUtils.setField(EnumHelper.class, aNewInstance, newInstance);
        	ReflectionUtils.setField(EnumHelper.class, aNewFieldAccessor, newFieldAccessor);
        	ReflectionUtils.setField(EnumHelper.class, aFieldAccessorSet, fieldAccessorSet);

        	Preloader_Logger.INFO("Finished patching Fields in Forge's EnumHelper.");
        	
        	Preloader_Logger.INFO("Testing: "+ReflectionUtils.getFieldValue(aReflectionFactory).getClass().getCanonicalName());
        	
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        isSetup = true;
    }

}