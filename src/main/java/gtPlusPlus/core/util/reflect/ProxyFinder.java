package gtPlusPlus.core.util.reflect;

import java.lang.reflect.Field;

import cpw.mods.fml.common.SidedProxy;

public class ProxyFinder {

	public static Object getServerProxy(final Object modInstance) throws ReflectiveOperationException {
		for(final Field field : modInstance.getClass().getDeclaredFields()) {
			if(field.isAnnotationPresent(SidedProxy.class)) {
				final SidedProxy sidedProxy = field.getAnnotation(SidedProxy.class);
				final Object fieldValue = field.get(modInstance);
				try {
					final Class<?> serverSideClass = ReflectionUtils.getClass(sidedProxy.serverSide());
					if(serverSideClass.isAssignableFrom(fieldValue.getClass())) {
						final Object serverProxy = serverSideClass.cast(fieldValue);
						//do what you want with server proxy instance
						return serverProxy;
					}

				} catch (final NoClassDefFoundError err) {
					//its client side
					return null;
				}
				break;
			}
		}
		return null;
	}
	
	public static Object getClientProxy(final Object modInstance) throws ReflectiveOperationException {
		for(final Field field : modInstance.getClass().getDeclaredFields()) {
			if(field.isAnnotationPresent(SidedProxy.class)) {
				final SidedProxy sidedProxy = field.getAnnotation(SidedProxy.class);
				final Object fieldValue = field.get(modInstance);
				try {
					final Class<?> clientSideClass = ReflectionUtils.getClass(sidedProxy.clientSide());
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