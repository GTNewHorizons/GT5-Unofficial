package gtPlusPlus.xmod.thaumcraft.objects.wrapper.aspect;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import gregtech.api.enums.TC_Aspects;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.thaumcraft.util.ThaumcraftUtils;
import net.minecraft.util.ResourceLocation;

/**
 * Wrapper class for Thaumcraft Aspects.
 * Used to avoid compile time dependencies.
 * @author Alkalus
 *
 */
public class TC_Aspect_Wrapper {

	private static Class mClass_Aspect;
	private static Field mField_Aspects;	
	
	private final String tag;
	private final TC_Aspect_Wrapper[] components;
	private final int color;
	private String chatcolor;
	private final ResourceLocation image;
	private final int blend;
	
	public final Object mAspect;
	
	/**
	 * May be null, but links back to the TC_Aspects class from GT for convinience.
	 */
	public final TC_Aspects mGtEnumField;
	
	
	
	
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
	
	public static Object getVanillaAspectObject(String aAspectName) {
		return getVanillaAspectList().get(aAspectName);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Vanilla Aspect Constructor
	 * @param tag - Aspect Name
	 * @param color
	 * @param chatcolor
	 * @param blend
	 */
	public TC_Aspect_Wrapper(String tag, int color, String chatcolor, int blend, String aTooltip) {
		this(tag, color, (TC_Aspect_Wrapper[]) null, blend, aTooltip);
		this.chatcolor = chatcolor;
	}

	/**
	 * 
	 * Vanilla Aspect Constructor
	 * @param tag - Aspect Name
	 * @param color
	 * @param components
	 */
	public TC_Aspect_Wrapper(String tag, int color, TC_Aspect_Wrapper[] components, String aTooltip) {
		this(tag, color, components, false, 1, aTooltip);
	}

	/**
	 * 
	 * Vanilla Aspect Constructor
	 * @param tag - Aspect Name
	 * @param color
	 * @param components
	 * @param blend
	 */
	public TC_Aspect_Wrapper(String tag, int color, TC_Aspect_Wrapper[] components, int blend, String aTooltip) {
		this(tag, color, components, false, blend, aTooltip);
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
	public TC_Aspect_Wrapper(String tag, int color, TC_Aspect_Wrapper[] components, boolean vanilla, int blend, String aTooltip) {
		this(tag, color, components, vanilla ? new ResourceLocation("thaumcraft", "textures/aspects/" + tag.toLowerCase() + ".png") : new ResourceLocation(CORE.MODID, "textures/aspects/" + tag.toLowerCase() + ".png"), vanilla, blend, aTooltip);
	}
	
	private static int aInternalAspectIDAllocation = 1;

	public TC_Aspect_Wrapper(String tag, int color, TC_Aspect_Wrapper[] components, ResourceLocation image, boolean vanilla, int blend, String aTooltip) {		
		if (components == null) {
			components = new TC_Aspect_Wrapper[] {};
		}
		//String aTag = vanilla ? tag.toLowerCase() : "custom"+(aInternalAspectIDAllocation++);		
		String aTag = tag.toLowerCase();		
		if (getAspectList().containsKey(tag.toLowerCase())) {
			this.tag = aTag;
			this.components = components;
			this.color = color;
			this.image = image;
			this.blend = blend;
			this.mAspect = null;
			this.mGtEnumField = null;
		} else {			
			this.tag = aTag;			
			this.components = components;
			this.color = color;
			this.image = image;
			this.blend = blend;
			this.mAspect = vanilla ? getVanillaAspectObject(this.tag) : this.generateTcAspect();			
			
			// Set GT Type if exists
			TC_Aspects y = null;
			for (TC_Aspects e : TC_Aspects.values()) {
				try {
					String gtTag = ThaumcraftUtils.getTagFromAspectObject(e.mAspect);
					if (gtTag != null) {
						if (gtTag.equals(this.tag)) {
							y = e;
							break;
						}
					}
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				}
			}
			this.mGtEnumField = y;
			mInternalAspectCache.put(this.tag, this);
			// Double link custom Aspects, but internalise names using custom# instead
			if (!vanilla) {
				mInternalAspectCache.put("custom"+(aInternalAspectIDAllocation++), this);
				GT_LanguageManager.addStringLocalization("tc.aspect."+aTag, aTooltip);				
			}			
			Logger.INFO("[Thaumcraft++] Adding support for Aspect: "+tag);
		}
	}
	
	
	
	/**
	 * Generates a TC_Aspect from an object, presummed to be a TC Aspect.
	 * @param aBaseAspect - The TC Aspect to generate from.
	 * @return 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unused")
	public static TC_Aspect_Wrapper generate(Object aBaseAspect) {	
		try {
		Field aTagF = ReflectionUtils.getField(mClass_Aspect, "tag");		
		if (aTagF == null) {
			return null;
		}		
		String aTafB = (String) aTagF.get(aBaseAspect);
		if (aTafB == null) {
			return null;
		}
		String aTag = aTafB.toLowerCase();			
		if (aTag != null && getAspectList().containsKey(aTag.toLowerCase())) {
			return  getAspect(aTag);
		} else {
			TC_Aspect_Wrapper aTemp = new TC_Aspect_Wrapper(
					aTag,
					(int) ReflectionUtils.getField(mClass_Aspect, "color").get(aBaseAspect),
					generateAspectArrayInternal(ReflectionUtils.getField(mClass_Aspect, "components"), (aBaseAspect)),
					(ResourceLocation) ReflectionUtils.getField(mClass_Aspect, "image").get(aBaseAspect),
					true,
					(int) ReflectionUtils.getField(mClass_Aspect, "blend").get(aBaseAspect),
					""
					);
			if (aTemp != null) {
				aTemp.chatcolor = (String) ReflectionUtils.getField(mClass_Aspect, "chatcolor").get(aBaseAspect);				
				return aTemp;
			}
			else {
				return null;
			}
		}	
		}
		catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Internal Map containing all the TC_Aspects.
	 */
	private static Map<String, TC_Aspect_Wrapper> mInternalAspectCache = new LinkedHashMap<String, TC_Aspect_Wrapper>();
	
	/**
	 * Public getter for all TC_Aspects
	 * @param aAspectName - Aspect Name
	 * @return - A GT++ Aspect wrapper or null. (TC_Aspect)
	 */
	public static TC_Aspect_Wrapper getAspect(String aAspectName) {
		String aName = aAspectName.toLowerCase();
		TC_Aspect_Wrapper g = mInternalAspectCache.get(aName);
		if (g != null) {
			return g;
		}
		else {
			try {
				TC_Aspect_Wrapper aTemp = generate(getVanillaAspectList().get(aName));
				if (aTemp != null) {
					mInternalAspectCache.put(aName, aTemp);
					return aTemp;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Map<String, TC_Aspect_Wrapper> getAspectList(){
		return mInternalAspectCache;
	}
	
	
	private static TC_Aspect_Wrapper[] generateAspectArrayInternal(Field aField, Object aInstance) {
		//thaumcraft.api.aspects.Aspect.Aspect()
		Object[] components;
		TC_Aspect_Wrapper[] aAspectArray;
		try {
			components = (Object[]) aField.get(aInstance);
			aAspectArray = new TC_Aspect_Wrapper[components == null ? 0 : components.length];
			if (aAspectArray.length > 0) {
				int i = 0;
				for (Object g : components) {
					aAspectArray[i] = getAspect((String) ReflectionUtils.getField(mClass_Aspect, "tag").get(g));
					i++;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			aAspectArray = new TC_Aspect_Wrapper[0];
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
			Object aAspectArray = (Object[]) Array.newInstance(mClass_Aspect, 0);
			if (components.length > 0) {
				aAspectArray = (Object[]) Array.newInstance(mClass_Aspect, components.length);
				int i = 0;
				for (TC_Aspect_Wrapper g : components) {
					if (g != null && g.mAspect != null)					
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
