package gtPlusPlus.xmod.gregtech.loaders;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class GT_Material_Loader {

	private volatile static GT_Material_Loader instance = new GT_Material_Loader();
	private volatile Object mProxyObject;
	private static AutoMap<Materials> mMaterials = new AutoMap<Materials>();
	private static volatile boolean mHasRun = false;

	public synchronized GT_Material_Loader getInstance(){
		return GT_Material_Loader.instance;
	}
	
	public synchronized boolean getRunAbility(){
		return (mHasRun ? false : true);
	}
	public synchronized void setRunAbility(boolean b){
		mHasRun = Utils.invertBoolean(b);
	}

	public GT_Material_Loader() {
		if (getRunAbility()){
		//Set Singleton Instance
		instance = this;				 

		//Try Reflectively add ourselves to the GT loader.
			Class mInterface = ReflectionUtils.getClass("gregtech.api.interfaces.IMaterialHandler");
			if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && mInterface != null){

				//Make this class Dynamically implement IMaterialHandler
				if (mProxyObject == null){
					mProxyObject = Proxy.newProxyInstance(
							mInterface.getClassLoader(), new Class[] { mInterface }, 
							new MaterialHandler(getInstance()));		
				}

				if (ReflectionUtils.invoke(Materials.class, "add", new Class[]{ReflectionUtils.getClass("gregtech.api.interfaces.IMaterialHandler")}, new Object[]{mProxyObject})){
					Logger.REFLECTION("Successfully invoked add, on the proxied object implementing IMaterialHandler.");


					Logger.REFLECTION("Examining Proxy to ensure it implements the correct Interface.");
					Class[] i = mProxyObject.getClass().getInterfaces();
					for (int r=0;r<i.length;r++){
						Logger.REFLECTION("Contains "+i[r].getCanonicalName()+".");
						if (i[r] == mInterface){
							Logger.REFLECTION("Found gregtech.api.interfaces.IMaterialHandler. This Proxy is valid.");
						}
					}
				}
				else {
					Logger.REFLECTION("Failed to invoke add, on the proxied object implementing IMaterialHandler.");
				}
			}	
		//Materials.add(this);
		
		//Stupid shit running twice, I don't think so.
		setRunAbility(false);
		}
	}

	public void onMaterialsInit() {
		Logger.DEBUG_MATERIALS("onMaterialsInit()");
	}

	public void onComponentInit() {
		Logger.DEBUG_MATERIALS("onComponentInit()");
		if (!mMaterials.isEmpty()){
			Logger.DEBUG_MATERIALS("Found "+mMaterials.size()+" materials to re-enable.");
			for (Materials M : mMaterials.values()){
				String name = MaterialUtils.getMaterialName(M);
				Logger.DEBUG_MATERIALS("Trying to enable "+name+".");
				boolean success = tryEnableAllComponentsForMaterial(M);
				if (success){
					Logger.DEBUG_MATERIALS("Success! Enabled "+name+".");
				}
				else {
					Logger.DEBUG_MATERIALS("Failure... Did not enable "+name+".");					
				}
			}
		}
	}

	public void onComponentIteration(Materials aMaterial) {
		Logger.DEBUG_MATERIALS("onComponentIteration()");
	}

	public synchronized boolean enableMaterial(Materials m){
		if (mMaterials.setValue(m)){
			Logger.DEBUG_MATERIALS("Added "+MaterialUtils.getMaterialName(m)+" to internal Map.");
			return true;
		}
		Logger.DEBUG_MATERIALS("Failed to add "+MaterialUtils.getMaterialName(m)+" to internal Map.");
		return false;
	}






	/*
	 * Static internal handler methods
	 */

	private static synchronized boolean tryEnableMaterial(Materials mMaterial){
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			return false;
		}			

		boolean value = ReflectionUtils.setField(mMaterial, "mHasParentMod", true);	
		if (value){
			Logger.DEBUG_MATERIALS("Set mHasParentMod true for "+mMaterial.mDefaultLocalName);
		}
		else {
			Logger.DEBUG_MATERIALS("Failed to set mHasParentMod true for "+mMaterial.mDefaultLocalName);
		}
		return value; 
	}

	private static synchronized boolean tryEnableMaterialPart(OrePrefixes prefix, Materials mMaterial){
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			return false;
		}		
		try {
			Method enableComponent = ReflectionUtils.getClass("gregtech.api.enums.OrePrefixes").getDeclaredMethod("enableComponent", Materials.class);
			enableComponent.invoke(prefix, mMaterial);
			Logger.DEBUG_MATERIALS("Enabled "+prefix.name()+" for "+mMaterial.mDefaultLocalName+".");
			return true;
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException error) {
			Logger.DEBUG_MATERIALS("Failed to enabled "+prefix.name()+" for "+mMaterial.mDefaultLocalName+". Caught "+error.getCause().toString()+".");
			error.printStackTrace();			
		}		
		Logger.DEBUG_MATERIALS("Did not enable "+prefix.name()+" for "+mMaterial.mDefaultLocalName+". Report this error to Alkalus on Github.");
		return false;
	}

	private static synchronized boolean tryEnableAllComponentsForMaterial(Materials material){
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK){
			return false;
		}		
		try {
			tryEnableMaterial(material);
			int mValid = 0;
			for(OrePrefixes ore:OrePrefixes.values()){
				if (tryEnableMaterialPart(ore, material)){
					mValid++;
				}
			}
			if (mValid > 0){
				Logger.DEBUG_MATERIALS("Success - Re-enabled all components for "+MaterialUtils.getMaterialName(material));
			}
			else {
				Logger.DEBUG_MATERIALS("Failure - Did not enable any components for "+MaterialUtils.getMaterialName(material));
			}
			return mValid > 0;			
		}
		catch (SecurityException | IllegalArgumentException e) {
			Logger.DEBUG_MATERIALS("Total Failure - Unable to re-enable "+MaterialUtils.getMaterialName(material)+". Most likely an IllegalArgumentException, but small chance it's a SecurityException.");
			return false;
		}		
	}









	/**
	 * Special Dynamic Interface Class
	 */

	public class MaterialHandler implements InvocationHandler {

		private final Map<String, Method> methods = new HashMap<String, Method>();
		private Object target;

		public MaterialHandler(Object target) {
			Logger.REFLECTION("Created a Proxy Interface which implements IMaterialHandler.");
			this.target = target;	 
			for(Method method: target.getClass().getDeclaredMethods()) {
				Logger.REFLECTION("Adding "+method.getName()+" to internal method map.");
				this.methods.put(method.getName(), method);
			}
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) 
				throws Throwable {
			long start = System.nanoTime();
			Object result = methods.get(method.getName()).invoke(target, args);
			long elapsed = System.nanoTime() - start;	 
			Logger.INFO("[Debug] Executed "+method.getName()+" in "+elapsed+" ns");	 
			return result;
		}
	}


	/*
	public static class ProxyListener implements java.lang.reflect.InvocationHandler {		

		public static Object IMaterialHandlerProxy;

		ProxyListener(){

			Logger.REFLECTION("Failed setting IMaterialHandler Proxy instance.");
		}

		//Loading the class at runtime
		public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		    Class<?> someInterface = ReflectionUtils.getClass("gregtech.api.interfaces.IMaterialHandler");
		    Object instance = Proxy.newProxyInstance(someInterface.getClassLoader(), new Class<?>[]{someInterface}, new InvocationHandler() {

		        @Override
		        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		            //Handle the invocations
		            if(method.getName().equals("onMaterialsInit")){
		            	Logger.REFLECTION("Invoked onMaterialsInit() via IMaterialHandler proxy");
		                return 1;
		            }
		            else if(method.getName().equals("onComponentInit")){
		            	Logger.REFLECTION("Invoked onComponentInit() via IMaterialHandler proxy");
		                return 2;
		            }
		            else if(method.getName().equals("onComponentIteration")){
		            	Logger.REFLECTION("Invoked onComponentIteration() via IMaterialHandler proxy");
		                return 3;
		            }
		            else {
		            	return -1;
		            }
		        }
		    }); 
		    System.out.println(instance.getClass().getDeclaredMethod("someMethod", (Class<?>[])null).invoke(instance, new Object[]{}));
		}

		private static class MaterialHandler implements InvocationHandler {
	        private final Object original;

	        public MaterialHandler(Object original) {
	            this.original = original;
	        }

	        @Override
			public Object invoke(Object proxy, Method method, Object[] args)
	                throws IllegalAccessException, IllegalArgumentException,
	                InvocationTargetException {
	            System.out.println("BEFORE");
	            method.invoke(original, args);
	            System.out.println("AFTER");
	            return null;
	        }
	    }

	    public static void init(){ 

	    	Class<?> someInterface = ReflectionUtils.getClass("gregtech.api.interfaces.IMaterialHandler");		   
	        GT_Material_Loader original = GT_Material_Loader.instance;
	        MaterialHandler handler = new MaterialHandler(original);

	        Object f = Proxy.newProxyInstance(someInterface.getClassLoader(),
	                new Class[] { someInterface },
	                handler);

	        f.originalMethod("Hallo");
	    }



	}

	 */
}
