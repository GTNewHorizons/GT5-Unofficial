package gtPlusPlus.xmod.thaumcraft.aspect;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Wrapper class for Thaumcraft Aspects.
 * Used to avoid compile time dependencies.
 * @author Alkalus
 *
 */
public class TC_Aspect {

	private static Class mClass_Aspect;
	private static Field mField_Aspects;	
	
	private final String tag;
	private final TC_Aspect[] components;
	private final int color;
	private String chatcolor;
	private final ResourceLocation image;
	private final int blend;
	
	private final Object mAspect;
	
	
	
	
	/**
	 * Statically set the Class objects
	 */
	static {
		mClass_Aspect = ReflectionUtils.getClass("thaumcraft.api.aspects.Aspect");
	}	
	
	/**
	 * Gets the total aspect list from Thaumcraft, which should contain all other registered aspects.
	 * @return - A LinkedHashMap(String, Aspect);
	 */
	public static LinkedHashMap<String, Object> getVanillaAspectList() {
		try {
			if (mField_Aspects == null) {
				mField_Aspects =  ReflectionUtils.getField(mClass_Aspect, "aspects");
			}
			return (LinkedHashMap<String, Object>) mField_Aspects.get(null);			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			Logger.REFLECTION("Failed configuring TC Aspect compatibility.");
			return new LinkedHashMap<String, Object>();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Vanilla Aspect Constructor
	 * @param tag - Aspect Name
	 * @param color
	 * @param chatcolor
	 * @param blend
	 */
	public TC_Aspect(String tag, int color, String chatcolor, int blend) {
		this(tag, color, (TC_Aspect[]) null, blend);
		this.chatcolor = chatcolor;
	}

	/**
	 * 
	 * Vanilla Aspect Constructor
	 * @param tag - Aspect Name
	 * @param color
	 * @param components
	 */
	public TC_Aspect(String tag, int color, TC_Aspect[] components) {
		this(tag, color, components, true, 1);
	}

	/**
	 * 
	 * Vanilla Aspect Constructor
	 * @param tag - Aspect Name
	 * @param color
	 * @param components
	 * @param blend
	 */
	public TC_Aspect(String tag, int color, TC_Aspect[] components, int blend) {
		this(tag, color, components, true, blend);
	}
	
	
	/**
	 * 
	 * Vanilla Aspect Constructor
	 * @param tag - Aspect Name
	 * @param color
	 * @param components
	 * @param image
	 * @param blend
	 */
	public TC_Aspect(String tag, int color, TC_Aspect[] components, boolean vanilla, int blend) {
		this(tag, color, components, vanilla ? new ResourceLocation("thaumcraft", "textures/aspects/" + tag.toLowerCase() + ".png") : new ResourceLocation(CORE.MODID, "textures/aspects/" + tag.toLowerCase() + ".png"), blend);
	}
	

	public TC_Aspect(String tag, int color, TC_Aspect[] components, ResourceLocation image, int blend) {
		if (getAspectList().containsKey(tag)) {
			throw new IllegalArgumentException(tag + " already registered!");
		} else {
			this.tag = tag;
			this.components = components;
			this.color = color;
			this.image = image;
			this.blend = blend;
			this.mAspect = this.generateTcAspect();
		}
	}
	
	
	
	/**
	 * Generates a TC_Aspect from an object, presummed to be a TC Aspect.
	 * @param aBaseAspect - The TC Aspect to generate from.
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public TC_Aspect(Object aBaseAspect) throws IllegalArgumentException, IllegalAccessException {		
		this((String) ReflectionUtils.getField(mClass_Aspect, "tag").get(aBaseAspect), (int) ReflectionUtils.getField(mClass_Aspect, "color").get(aBaseAspect), generateAspectArrayInternal(ReflectionUtils.getField(mClass_Aspect, "components"), aBaseAspect), (ResourceLocation) ReflectionUtils.getField(mClass_Aspect, "image").get(aBaseAspect), (int) ReflectionUtils.getField(mClass_Aspect, "blend").get(aBaseAspect));
		Field aChatColour = ReflectionUtils.getField(mClass_Aspect, "chatcolor");
		chatcolor = (String) aChatColour.get(aBaseAspect);
	}
	
	
	/**
	 * Internal Map containing all the TC_Aspects.
	 */
	private static Map<String, TC_Aspect> mInternalAspectCache = new LinkedHashMap<String, TC_Aspect>();
	
	/**
	 * Public getter for all TC_Aspects
	 * @param aAspectName - Aspect Name
	 * @return - A GT++ Aspect wrapper or null. (TC_Aspect)
	 */
	public static TC_Aspect getAspect(String aAspectName) {
		TC_Aspect g = mInternalAspectCache.get(aAspectName);
		if (g != null) {
			return g;
		}
		else {
			try {
				TC_Aspect aTemp = new TC_Aspect(getAspectList().get(aAspectName));
				if (aTemp != null) {
					mInternalAspectCache.put(aAspectName, aTemp);
					return aTemp;
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Map<String, TC_Aspect> getAspectList(){
		return mInternalAspectCache;
	}
	
	
	private static TC_Aspect[] generateAspectArrayInternal(Field aField, Object aInstance) {
		//thaumcraft.api.aspects.Aspect.Aspect()
		Object[] components;
		TC_Aspect[] aAspectArray;
		try {
			components = (Object[]) aField.get(aInstance);
			aAspectArray = new TC_Aspect[components.length];
			if (components.length > 0) {
				int i = 0;
				for (Object g : components) {
					aAspectArray[i] = getAspect((String) ReflectionUtils.getField(mClass_Aspect, "tag").get(g));
					i++;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			aAspectArray = new TC_Aspect[0];
		}			
		return aAspectArray;
	}

	/**
	 * Tasty code to generate TC Aspects reflectively.
	 * @return
	 */
	public Object generateTcAspect() {
		try {			
			//thaumcraft.api.aspects.Aspect.Aspect()
			Object aAspectArray = (Object[]) Array.newInstance(mClass_Aspect, 1);
			if (components.length > 0) {
				aAspectArray = (Object[]) Array.newInstance(mClass_Aspect, components.length);
				int i = 0;
				for (TC_Aspect g : components) {
					((Object[]) aAspectArray)[i++] = g.mAspect;
				}
			}			
			Constructor constructor = mClass_Aspect.getConstructor(String.class, int.class, aAspectArray.getClass(), ResourceLocation.class, int.class);
			Object myObject = constructor.newInstance(tag, color, aAspectArray, image, blend);
			
			//Set chat colour
			if (chatcolor != null && chatcolor.length() > 0) {
				Method setChatColour = ReflectionUtils.getMethod(mClass_Aspect, "setChatcolor", String.class);
				if (setChatColour != null) {
					setChatColour.invoke(myObject, chatcolor);
				}
			}			
			return myObject;
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	public static boolean isObjectAnAspect(Object aAspect) {
		return mClass_Aspect.isInstance(aAspect);
	}
	
}
