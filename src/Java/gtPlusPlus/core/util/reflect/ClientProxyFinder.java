package gtPlusPlus.core.util.reflect;

import java.lang.reflect.Field;

import cpw.mods.fml.common.SidedProxy;

public class ClientProxyFinder {

    public static Object getInstance(Object modInstance) throws ReflectiveOperationException {
        for(Field field : modInstance.getClass().getDeclaredFields()) {
            if(field.isAnnotationPresent(SidedProxy.class)) {
                SidedProxy sidedProxy = field.getAnnotation(SidedProxy.class);
                Object fieldValue = field.get(modInstance);
                try {
                    Class clientSideClass = Class.forName(sidedProxy.clientSide());
                    if(clientSideClass.isAssignableFrom(fieldValue.getClass())) {
                        Object clientProxy = clientSideClass.cast(fieldValue);
                        //do what you want with client proxy instance
                        return clientProxy;
                    }

                } catch (NoClassDefFoundError err) {
                    //its server side
                    return null;
                }
                break;
            }
        }
        return null;
    }

}