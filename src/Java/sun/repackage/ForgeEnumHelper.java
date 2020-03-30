package sun.repackage;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class ForgeEnumHelper
{
    private static Object reflectionFactory      = null;
    private static Method newConstructorAccessor = null;
    private static Method newInstance            = null;
    private static Method newFieldAccessor       = null;
    private static Method fieldAccessorSet       = null;
    private static boolean isSetup               = false;
    
    private static void setup()
    {
        if (isSetup)
        {
            return;
        }

        try
        {

        	Class aRefFac = ReflectionUtils.getClass("sun.repackage.ReflectionFactory");
        	Class aConAcc = ReflectionUtils.getClass("sun.repackage.ConstructorAccessor");
        	Class aFieAcc = ReflectionUtils.getClass("sun.repackage.FieldAccessor");

        	Method getReflectionFactory = ReflectionUtils.getMethod(aRefFac, "getReflectionFactory", new Class[] {});
        	reflectionFactory = ReflectionUtils.invoke(aRefFac, getReflectionFactory, new Object[] {});        	
        	newConstructorAccessor = ReflectionUtils.getMethod(aRefFac, "newConstructorAccessor", new Class[] {Constructor.class});
        	newFieldAccessor = ReflectionUtils.getMethod(aRefFac, "newFieldAccessor", new Class[] {Field.class, boolean.class});
        	newInstance = ReflectionUtils.getMethod(aConAcc, "newInstance", new Class[] {Object[].class});
        	fieldAccessorSet = ReflectionUtils.getMethod(aFieAcc, "set", new Class[] {Object.class, Object.class});
        	
        	
        	
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        isSetup = true;
    }

    static
    {
        if (!isSetup)
        {
            setup();
        }
    }
}