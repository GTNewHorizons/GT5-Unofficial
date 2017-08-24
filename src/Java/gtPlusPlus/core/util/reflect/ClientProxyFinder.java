package gtPlusPlus.core.util.reflect;

import java.lang.reflect.Field;

import cpw.mods.fml.common.SidedProxy;

public class ClientProxyFinder {

	public static Object getInstance(final Object modInstance) throws ReflectiveOperationException {
		for(final Field field : modInstance.getClass().getDeclaredFields()) {
			if(field.isAnnotationPresent(SidedProxy.class)) {
				final SidedProxy sidedProxy = field.getAnnotation(SidedProxy.class);
				final Object fieldValue = field.get(modInstance);
				try {
					final Class<?> clientSideClass = Class.forName(sidedProxy.clientSide());
					if(clientSideClass.isAssignableFrom(fieldValue.getClass())) {
						final Object clientProxy = clientSideClass.cast(fieldValue);
						//do what you want with client proxy instance
						return clientProxy;
					}

				} catch (final NoClassDefFoundError err) {
					//its server side
					return null;
				}
				break;
			}
		}
		return null;
	}

}